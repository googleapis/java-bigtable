resource "google_service_account" "kubernetes_kafka_connect" {
  account_id   = "${local.name}-k8s-kafka-connect"
  display_name = "${local.name}-k8s-kafka-connect-SA"
}

resource "google_project_iam_member" "kubernetes_kafka_connect_permissions" {
  for_each = toset([
    "roles/managedkafka.admin",
  ])

  project = local.project
  role    = each.key
  member  = "serviceAccount:${google_service_account.kubernetes_kafka_connect.email}"
}

resource "google_service_account_key" "kubernetes_kafka_connect_key" {
  service_account_id = google_service_account.kubernetes_kafka_connect.name
}

# https://cloud.google.com/kubernetes-engine/docs/troubleshooting/dashboards#write_permissions
resource "google_project_iam_member" "gke_monitoring" {
  for_each = toset(["roles/monitoring.metricWriter", "roles/monitoring.editor", "roles/logging.logWriter", "roles/stackdriver.resourceMetadata.writer"])
  project  = local.project
  role     = each.value
  member   = "serviceAccount:${google_service_account.kubernetes.email}"
}

resource "google_project_iam_member" "kafka_connect_registry" {
  project = local.project
  role    = "roles/artifactregistry.reader"
  member  = "serviceAccount:${google_service_account.kubernetes.email}"
}

