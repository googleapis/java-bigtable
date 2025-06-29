FROM rust:1.84-bullseye AS build
WORKDIR /root
COPY main.rs main.rs
RUN rustc main.rs -o main

# https://quay.io/repository/strimzi/kafka/manifest/sha256:752d3dcf2bb117b617705488b56e1102a24c33836ac257acb7da63d0a9d01759
# Keep it in sync with Kafka version as described in kubernetes.tf.
FROM quay.io/strimzi/kafka:0.45.0-kafka-3.8.1
USER root:root
RUN rpm -i https://dl.fedoraproject.org/pub/epel/9/Everything/x86_64/Packages/p/pv-1.6.20-1.el9.x86_64.rpm
RUN curl -L https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.12.0/opentelemetry-javaagent.jar > /opt/kafka/libs/opentelemetry-javaagent.jar
COPY bigtable-sink /opt/kafka/plugins/bigtable-sink
# Needed to force the connector to use opentelemetry jars from the host.
RUN rm -rf /opt/kafka/plugins/bigtable-sink/sink-1.0.0-SNAPSHOT-package/opentelemetry*
COPY --from=build /root/main /usr/bin/kafka-console-generator
COPY generate_load.sh /usr/bin/generate_load.sh
USER 1001
