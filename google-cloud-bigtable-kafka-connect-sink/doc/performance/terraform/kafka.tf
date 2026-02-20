locals {
  kafka_url           = "bootstrap.${google_managed_kafka_cluster.kafka.cluster_id}.${local.region}.managedkafka.${local.project}.cloud.goog:9092"
  kafka_topic         = "${local.name}-kafka-topic"
}

resource "google_managed_kafka_cluster" "kafka" {
  cluster_id = "${local.name}-kafka"
  location   = local.region
  capacity_config {
    vcpu_count   = local.kafka_vcpus
    memory_bytes = local.kafka_ram_gbs * 1073741824
  }
  gcp_config {
    access_config {
      network_configs {
        subnet = data.google_compute_subnetwork.default_subnet.id
      }
    }
  }
  depends_on = [google_project_service.kafka]
}

resource "google_managed_kafka_topic" "topic" {
  topic_id           = local.kafka_topic
  cluster            = google_managed_kafka_cluster.kafka.cluster_id
  location           = local.region
  partition_count    = local.kafka_partitions
  replication_factor = local.kafka_connect_nodes
  configs = {
    "cleanup.policy" = "compact"
  }
}
