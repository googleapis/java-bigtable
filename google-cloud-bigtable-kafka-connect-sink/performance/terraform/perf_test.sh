#!/bin/bash

set -euo pipefail

KAFKA_CONNECT_CR_PATH="kafka-connect.yaml"
KAFKA_CONNECT_CONNECTOR_CR_PATH="kafka-connect-connector.yaml"
LOAD_GENERATOR_MANIFEST_PATH="load-generator.yaml"
# Must match `Dockerfile`
BIGTABLE_PLUGIN_PATH=bigtable-sink

print_delimiter() {
    yes "=" | head -80 | tr -d '\n' || :
    echo
}

print_timestamp() {
    date --utc --iso-8601=seconds
}

do_prepare() {
    terraform apply -auto-approve
    TERRAFORM_OUTPUT="$(terraform output -json)"
    KAFKA_CONNECT_DOCKER_REPO="$(echo "$TERRAFORM_OUTPUT" | jq -rc .kafka_connect_docker_registry_url.value)"
    KAFKA_NAMESPACE="$(echo "$TERRAFORM_OUTPUT" | jq -rc .kubernetes_kafka_namespace.value)"
    GKE_CLUSTER_NAME="$(echo "$TERRAFORM_OUTPUT" | jq -rc .gke_cluster_name.value)"
    GCP_REGION="$(echo "$TERRAFORM_OUTPUT" | jq -rc .region.value)"

    gcloud container clusters get-credentials "$GKE_CLUSTER_NAME" "--region=$GCP_REGION"
    gcloud auth configure-docker "$GCP_REGION-docker.pkg.dev"

    pushd ../..
    mvn clean package -Dmaven.test.skip
    popd
    rm -rf --preserve-root $BIGTABLE_PLUGIN_PATH || true
    mkdir -p $BIGTABLE_PLUGIN_PATH
    cp -rf ../../target/sink-*-SNAPSHOT{.jar,-package} $BIGTABLE_PLUGIN_PATH

    docker build . -t "$KAFKA_CONNECT_DOCKER_REPO" --push

    # As per https://cloud.google.com/stackdriver/docs/instrumentation/opentelemetry-collector-gke#deploy_the_collector
    # For whathever reason, the GCLOUD_PROJECT seems not to be used.
    GCLOUD_PROJECT="$(gcloud config get-value project)"
    export GCLOUD_PROJECT
    kubectl kustomize ../kubernetes/otlp | envsubst | kubectl apply -f -

    kubectl apply -f ../kubernetes/strimzi-cluster-operator-0.45.0.yaml
    kubectl apply -f ../kubernetes/kafka-connect-metrics-config.yaml -n "$KAFKA_NAMESPACE"
    kubectl apply -f ../kubernetes/kafka-connect-managed-prometheus.yaml -n "$KAFKA_NAMESPACE"
}

do_run() {
    terraform apply -auto-approve
    TERRAFORM_OUTPUT="$(terraform output -json)"
    KAFKA_NAMESPACE="$(echo "$TERRAFORM_OUTPUT" | jq -rc .kubernetes_kafka_namespace.value)"
    CONNECTOR_TASKS="$(echo "$TERRAFORM_OUTPUT" | jq -rc .perf_test_config.value.kafka_partitions)"

    echo "$TERRAFORM_OUTPUT" | jq -rc .kafka_connect_manifest.value > $KAFKA_CONNECT_CR_PATH
    echo "$TERRAFORM_OUTPUT" | jq -rc .kafka_connect_connector_manifest.value > $KAFKA_CONNECT_CONNECTOR_CR_PATH
    echo "$TERRAFORM_OUTPUT" | jq -rc .load_generator_manifest.value > $LOAD_GENERATOR_MANIFEST_PATH

    kubectl apply -f $KAFKA_CONNECT_CR_PATH -n "$KAFKA_NAMESPACE"
    kubectl apply -f $KAFKA_CONNECT_CONNECTOR_CR_PATH -n "$KAFKA_NAMESPACE"

    echo "Waiting for all connector tasks to be running..."
    while :
    do
        RUNNING="$(kubectl describe KafkaConnector -n "$KAFKA_NAMESPACE" | { grep -c RUNNING || :; })"
        # `gt` rather than `ge` since there is one extra "RUNNING" comming from the connector itself
        if [[ "$RUNNING" -gt "$CONNECTOR_TASKS" ]] ; then
            break
        fi
        # A bit untrue, in reality `$RUNNING - 1` tasks are running - one RUNNING comes from the connector.
        # Still, it is good enough - we're probably running tens of tasks.
        printf "Only %s out of %s tasks are running. Sleeping...\n" "$RUNNING" "$CONNECTOR_TASKS"
        sleep 10
    done

    kubectl apply -f $LOAD_GENERATOR_MANIFEST_PATH -n "$KAFKA_NAMESPACE"

    print_delimiter
    echo "Perf test config:"
    echo "$TERRAFORM_OUTPUT" | jq .perf_test_config.value
    echo "Start time"
    print_timestamp
    echo "Waiting for the load generator to be done..."
    kubectl wait --for=condition=ready=True --timeout=-1s pod/load-generator -n "$KAFKA_NAMESPACE"
    kubectl wait --for=condition=ready=False --timeout=-1s pod/load-generator -n "$KAFKA_NAMESPACE"
    echo "Load generator done."
    echo "End time"
    print_timestamp
    print_delimiter
}

do_cleanup() {
    kubectl delete --ignore-not-found=true -f $KAFKA_CONNECT_CR_PATH -f $KAFKA_CONNECT_CONNECTOR_CR_PATH -f $LOAD_GENERATOR_MANIFEST_PATH -n kafka
    # Let's give time to the connector to spin down since Kafka Connect might otherwise recreate the topic.
    sleep 20
    terraform destroy -auto-approve -target google_bigtable_table.table -target google_managed_kafka_topic.topic
}

do_destroy() {
    # Some operator leaves some resources preventing destroy from working. Let's just ignore it and manually remove problematic resources.
    # They will disappear once the whole kubernetes cluster is destroyed.
    terraform state rm kubernetes_namespace.otel
    terraform state rm kubernetes_namespace.kafka
    # No auto-approve since it takes 15-20 minutes to be recreated.
    terraform destroy
}

usage() {
    printf "USAGE: %s [prepare|run|cleanup|destroy|rerun]" "$0"
    exit 1
}

[[ $# -eq 1 ]] || usage
case "$1" in
    prepare)
        do_prepare
        ;;
    run)
        do_run
        ;;
    rerun)
        do_cleanup
        do_run
        ;;
    cleanup)
        do_cleanup
        ;;
    destroy)
        do_destroy
        ;;
    *)
        usage
        ;;
esac
