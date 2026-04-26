provider "google" {
  project = local.project
  region  = local.region
}

# As per https://registry.terraform.io/providers/hashicorp/google/latest/docs/guides/using_gke_with_terraform
data "google_client_config" "provider" {}

provider "kubernetes" {
  host  = "https://${google_container_cluster.kubernetes.endpoint}"
  token = data.google_client_config.provider.access_token
  cluster_ca_certificate = base64decode(
    google_container_cluster.kubernetes.master_auth[0].cluster_ca_certificate,
  )
  exec {
    api_version = "client.authentication.k8s.io/v1beta1"
    command     = "gke-gcloud-auth-plugin"
  }
}
