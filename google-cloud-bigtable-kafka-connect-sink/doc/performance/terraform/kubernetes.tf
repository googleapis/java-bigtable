locals {
  kubernetes_kafka_connect_service_key  = base64decode(google_service_account_key.kubernetes_kafka_connect_key.private_key)
  kafka_namespace                       = kubernetes_namespace.kafka.metadata[0].name
  kafka_connect_crd_name                = "my-connect-cluster"
  kafka_secret_service_key_secret_field = "service_key"
  kafka_service_key_email               = jsondecode(local.kubernetes_kafka_connect_service_key)["client_email"]
  # Must match contents of ../kubernetes/otlp
  otel_endpoint_url = "http://opentelemetry-collector.opentelemetry.svc.cluster.local:4317"
  otel_namespace    = kubernetes_namespace.otel.metadata[0].name

  kafka_credentials_secret = kubernetes_secret.kafka_credentials.metadata[0].name
  kafka_image              = "${local.kafka_connect_docker_registry_url}:latest"

  # Test parameters, adjust them here.
  kafka_vcpus                         = 3
  kafka_ram_gbs                       = 3
  kafka_partitions                    = 60
  kafka_connect_nodes                 = 3
  kafka_connect_vcpus                 = "2.66"
  kafka_connect_ram                   = "6Gi"
  kafka_connect_version               = "3.8.1"
  load_generation_messages_per_second = "3000"
  load_generation_seconds             = "600"
  load_generation_field_value_size    = "50"
  load_generation_column_families     = "1"
  load_generation_columns_per_family  = "2"
  load_generation_vcpus               = "2"
  load_generation_ram                 = "8Gi"
  connector_batch_size                = "1000"
}

resource "kubernetes_namespace" "kafka" {
  metadata {
    name = "kafka"
  }
}

resource "kubernetes_secret" "kafka_credentials" {
  metadata {
    name      = "bigtable-kafka-connect-credentials"
    namespace = local.kafka_namespace
  }

  data = {
    username                                      = local.kafka_service_key_email
    (local.kafka_secret_service_key_secret_field) = base64encode(local.kubernetes_kafka_connect_service_key)
  }

  type = "generic"
}

resource "kubernetes_service_account" "kafka_connect_service_account" {
  metadata {
    name      = "${local.kafka_connect_crd_name}-connect"
    namespace = local.kafka_namespace
  }
  # To avoid clashing with Strimzi Operator.
  lifecycle {
    ignore_changes = [metadata["labels"], automount_service_account_token]
  }
}

resource "google_project_iam_member" "kafka_connect_connectors_bigtable_permission" {
  project = local.project
  role    = "roles/bigtable.admin"
  member  = "principal://iam.googleapis.com/projects/${data.google_project.project.number}/locations/global/workloadIdentityPools/${local.workload_identity_pool}/subject/ns/${local.kafka_namespace}/sa/${kubernetes_service_account.kafka_connect_service_account.metadata[0].name}"
}

resource "kubernetes_namespace" "otel" {
  metadata {
    name = "opentelemetry"
  }
}

resource "kubernetes_service_account" "otel_service_account" {
  metadata {
    name      = "opentelemetry-collector"
    namespace = local.otel_namespace
  }
  # To avoid clashing with OTEL manifest.
  lifecycle {
    ignore_changes = [metadata["labels"], automount_service_account_token]
  }
}

# https://cloud.google.com/stackdriver/docs/instrumentation/opentelemetry-collector-gke#configure_permissions_for_the_collector
resource "google_project_iam_member" "otel_permissions" {
  for_each = toset(["roles/logging.logWriter", "roles/monitoring.metricWriter", "roles/cloudtrace.agent"])

  project = local.project
  role    = each.value
  member  = "principal://iam.googleapis.com/projects/${data.google_project.project.number}/locations/global/workloadIdentityPools/${local.workload_identity_pool}/subject/ns/${local.otel_namespace}/sa/${kubernetes_service_account.otel_service_account.metadata[0].name}"
}

output "kubernetes_kafka_namespace" {
  value = local.kafka_namespace
}

output "kafka_connect_manifest" {
  value = yamlencode({
    "apiVersion" = "kafka.strimzi.io/v1beta2"
    "kind"       = "KafkaConnect"
    "metadata" = {
      "annotations" = {
        # So that we can configure the connector using the operator's custom resources.
        "strimzi.io/use-connector-resources" = "true"
      }
      "name"      = local.kafka_connect_crd_name
      "namespace" = local.kafka_namespace
    }
    "spec" = {
      "authentication" = {
        "passwordSecret" = {
          "password"   = local.kafka_secret_service_key_secret_field
          "secretName" = local.kafka_credentials_secret
        }
        "type"     = "plain"
        "username" = local.kafka_service_key_email
      }
      "bootstrapServers" = local.kafka_url
      "config" = {
        "config.storage.replication.factor" = -1
        "config.storage.topic"              = "connect-cluster-configs"
        "fetch.min.bytes"                   = 1
        "group.id"                          = "connect-cluster"
        "key.converter"                     = "org.apache.kafka.connect.json.JsonConverter"
        "key.converter.schemas.enable"      = false
        "offset.storage.replication.factor" = -1
        "offset.storage.topic"              = "connect-cluster-offsets"
        "status.storage.replication.factor" = -1
        "status.storage.topic"              = "connect-cluster-status"
        "value.converter"                   = "org.apache.kafka.connect.json.JsonConverter"
        "value.converter.schemas.enable"    = true
      }
      "image" = local.kafka_image
      "logging" = {
        "loggers" = {
          "connect.root.logger.level"                            = "INFO"
          "log4j.logger.com.google.cloud.kafka.connect.bigtable" = "INFO"
          "log4j.logger.io.opentelemetry"                        = "TRACE"
        }
        "type" = "inline"
      }
      "metricsConfig" = {
        "type" = "jmxPrometheusExporter"
        "valueFrom" = {
          "configMapKeyRef" = {
            # They must match value from ../kubernetes/kafka-connec-metrics.config.yaml
            "name" = "connect-metrics"
            "key"  = "metrics-config.yml"
          }
        }
      }
      "replicas" = local.kafka_connect_nodes
      "resources" = {
        "limits" = {
          "cpu"    = local.kafka_connect_vcpus
          "memory" = local.kafka_connect_ram
        }
      }
      "template" = {
        "connectContainer" = {
          "env" = [
            {
              "name"  = "OTEL_SERVICE_NAME"
              "value" = "kafka-connect"
            },
            {
              "name"  = "OTEL_EXPORTER_OTLP_ENDPOINT"
              "value" = local.otel_endpoint_url
            },
            # Set up GlobalOpenTelemetry in java code
            {
              "name"  = "OTEL_JAVA_GLOBAL_AUTOCONFIGURE_ENABLED"
              "value" = "true"
            },
            {
              "name"  = "OTEL_EXPORTER_OTLP_PROTOCOL"
              "value" = "grpc"
            },
            {
              "name"  = "OTEL_TRACES_EXPORTER"
              "value" = "otlp"
            },
            {
              "name"  = "OTEL_JAVAAGENT_DEBUG"
              "value" = "true"
            }
          ]
        }
      }
      "tls" = {
        "trustedCertificates" = []
      }
      "tracing" = {
        "type" = "opentelemetry"
      }
      "version" = local.kafka_connect_version
    }
  })
  sensitive = true
}

output "kafka_connect_connector_manifest" {
  value = yamlencode({
    "apiVersion" = "kafka.strimzi.io/v1beta2"
    "kind"       = "KafkaConnector"
    "metadata" = {
      "labels" = {
        "strimzi.io/cluster" = local.kafka_connect_crd_name
      }
      "name" = "${local.name}-connector"
    }
    "spec" = {
      "class" = "com.google.cloud.kafka.connect.bigtable.BigtableSinkConnector"
      "config" = {
        "auto.create.column.families" = "false",
        "auto.create.tables"          = "false",
        "connector.class"             = "com.google.cloud.kafka.connect.bigtable.BigtableSinkConnector",
        "default.column.family"       = local.bigtable_default_column_family,
        "default.column.qualifier"    = "default_column",
        "error.mode"                  = "FAIL",
        "gcp.bigtable.instance.id"    = google_bigtable_instance.bigtable.name,
        "gcp.bigtable.project.id"     = local.project,
        "insert.mode"                 = "upsert",
        "max.batch.size"              = local.connector_batch_size,
        "retry.timeout.ms"            = "90000",
        "row.key.definition"          = "",
        "row.key.delimiter"           = "#",
        "table.name.format"           = local.bigtable_table_name,
        "topics"                      = local.kafka_topic,
        "value.null.mode"             = "write"
      }
      "tasksMax" = local.kafka_partitions
    }
  })
  sensitive = true
}

output "load_generator_manifest" {
  value = yamlencode({
    "apiVersion" = "v1"
    "kind"       = "Pod"
    "metadata" = {
      "name"      = "load-generator"
      "namespace" = local.kafka_namespace
    }
    "spec" = {
      "containers" = [
        {
          "args" = ["/usr/bin/generate_load.sh"]
          "env" = [
            {
              "name"  = "KAFKA_CONNECT_BOOTSTRAP_SERVERS"
              "value" = local.kafka_url
            },
            {
              "name"  = "KAFKA_CONNECT_TLS"
              "value" = "true"
            },
            {
              "name"  = "KAFKA_CONNECT_SASL_USERNAME"
              "value" = local.kafka_service_key_email
            },
            {
              "name"  = "KAFKA_CONNECT_SASL_PASSWORD_FILE"
              "value" = "${local.kafka_credentials_secret}/${local.kafka_secret_service_key_secret_field}"
            },
            {
              "name"  = "KAFKA_CONNECT_SASL_MECHANISM"
              "value" = "plain"
            },
            # Load generation args
            {
              "name"  = "THROUGHPUT"
              "value" = local.load_generation_messages_per_second
            },
            {
              "name"  = "TIMEOUT"
              "value" = local.load_generation_seconds
            },
            {
              "name"  = "TOPIC"
              "value" = local.kafka_topic
            },
            {
              "name"  = "FIELD_VALUE_SIZE"
              "value" = local.load_generation_field_value_size
            },
            {
              "name"  = "COLUMN_FAMILIES"
              "value" = local.load_generation_column_families
            },
            {
              "name"  = "COLUMNS_PER_FAMILY"
              "value" = local.load_generation_columns_per_family
            },
            # Tracing
            {
              "name"  = "OTEL_SERVICE_NAME"
              "value" = "load-generator"
            },
            {
              "name"  = "OTEL_EXPORTER_OTLP_ENDPOINT"
              "value" = local.otel_endpoint_url
            },
          ]
          "image"           = local.kafka_image
          "imagePullPolicy" = "Always"
          "name"            = "kafka-load-generator"
          "resources" = {
            "requests" = {
              "cpu"    = local.load_generation_vcpus
              "memory" = local.load_generation_ram
            }
          }
          "volumeMounts" = [
            {
              "mountPath" = "/opt/kafka/connect-password/${local.kafka_credentials_secret}"
              "name"      = local.kafka_credentials_secret
            },
          ]
        },
      ]
      "restartPolicy" = "Never"
      "volumes" = [
        {
          "name" = local.kafka_credentials_secret
          "secret" = {
            "defaultMode" = 292 # 0x124
            "secretName"  = local.kafka_credentials_secret
          }
        },
      ]
    }
  })
  sensitive = true
}

output "kafka_connect_docker_registry_url" {
  value = local.kafka_connect_docker_registry_url
}

output "perf_test_config" {
  value = {
    kafka_vcpus                         = local.kafka_vcpus
    kafka_ram_gbs                       = local.kafka_ram_gbs
    kafka_partitions                    = local.kafka_partitions
    kafka_connect_nodes                 = local.kafka_connect_nodes
    kafka_connect_vcpus                 = local.kafka_connect_vcpus
    kafka_connect_ram                   = local.kafka_connect_ram
    kafka_connect_version               = local.kafka_connect_version
    load_generation_messages_per_second = local.load_generation_messages_per_second
    load_generation_seconds             = local.load_generation_seconds
    load_generation_field_value_size    = local.load_generation_field_value_size
    load_generation_column_families     = local.load_generation_column_families
    load_generation_columns_per_family  = local.load_generation_columns_per_family
    load_generation_vcpus               = local.load_generation_vcpus
    load_generation_ram                 = local.load_generation_ram
    connector_batch_size                = local.connector_batch_size
  }
}
