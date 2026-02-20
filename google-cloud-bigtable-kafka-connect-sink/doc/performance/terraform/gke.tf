locals {
  workload_identity_pool = "${data.google_project.project.project_id}.svc.id.goog"
}

resource "google_service_account" "kubernetes" {
  account_id   = "${local.name}-kubernetes"
  display_name = "${local.name} Kubernetes Service Account"
}

resource "google_container_cluster" "kubernetes" {
  name     = "${local.name}-k8s-cluster"
  location = local.region

  # We can't create a cluster with no node pool defined, but we want to only use
  # separately managed node pools. So we create the smallest possible default
  # node pool and immediately delete it.
  remove_default_node_pool = true
  initial_node_count       = 1

  workload_identity_config {
    workload_pool = local.workload_identity_pool
  }

  deletion_protection = false
  depends_on          = [google_project_service.kubernetes]
}

resource "google_container_node_pool" "pool" {
  name           = "${local.name}-k8s-pool"
  location       = local.region
  node_locations = [local.subregion]
  cluster        = google_container_cluster.kubernetes.name
  # +1 for load generator and possibly other tools.
  node_count = local.kafka_connect_nodes + 1

  node_config {
    preemptible  = false
    machine_type = "n1-standard-4"

    service_account = google_service_account.kubernetes.email
    oauth_scopes = [
      "https://www.googleapis.com/auth/cloud-platform"
    ]
    workload_metadata_config {
      mode = "GKE_METADATA"
    }
  }
}

output "gke_cluster_name" {
  value = google_container_cluster.kubernetes.name
}
