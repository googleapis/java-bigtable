resource "google_project_service" "kubernetes" {
  service            = "container.googleapis.com"
  disable_on_destroy = false
}

resource "google_project_service" "kafka" {
  service            = "managedkafka.googleapis.com"
  disable_on_destroy = false
}

resource "google_project_service" "bigtable" {
  service            = "bigtable.googleapis.com"
  disable_on_destroy = false
}
