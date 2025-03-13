terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "~> 6.19"
    }
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = "~> 2.35"
    }
  }
}
