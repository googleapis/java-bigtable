locals {
  project   = "unoperate-test"
  region    = "europe-central2"
  subregion = "europe-central2-a"
  name      = "bigtablesink"
}

data "google_compute_subnetwork" "default_subnet" {
  name   = "default"
  region = local.region
}

data "google_project" "project" {}

output "region" {
  value = local.region
}
