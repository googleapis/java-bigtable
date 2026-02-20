locals {
  kafka_connect_docker_registry_url = "${google_artifact_registry_repository.kafka_connect.location}-docker.pkg.dev/${google_artifact_registry_repository.kafka_connect.project}/${google_artifact_registry_repository.kafka_connect.name}/kafkaconnect"
}

resource "google_artifact_registry_repository" "kafka_connect" {
  location      = local.region
  repository_id = "${local.name}-kafka-connect"
  format        = "DOCKER"
}
