locals {
  cluster_id                     = "${local.name}-cluster"
  bigtable_table_name            = "${local.name}-table"
  bigtable_default_column_family = "default_column_family"
  bigtable_column_families       = 21
  // Note that it must match `main.rs`'s prefix for column families.
  bigtable_column_family_prefix = "cf"
}

resource "google_bigtable_instance" "bigtable" {
  name                = "${local.name}-instance"
  deletion_protection = false

  cluster {
    cluster_id   = local.cluster_id
    num_nodes    = 1
    storage_type = "SSD"
    zone         = local.subregion
  }

  depends_on = [google_project_service.bigtable]
}

resource "google_bigtable_table" "table" {
  name          = local.bigtable_table_name
  instance_name = google_bigtable_instance.bigtable.name

  dynamic "column_family" {
    for_each = toset([for i in range(local.bigtable_column_families) : "${local.bigtable_column_family_prefix}${i}"])
    content {
      family = column_family.value
    }
  }

  column_family {
    family = local.bigtable_default_column_family
  }
}

resource "google_bigtable_app_profile" "profile" {
  instance       = google_bigtable_instance.bigtable.name
  app_profile_id = "${local.name}-profile"

  single_cluster_routing {
    cluster_id                 = local.cluster_id
    allow_transactional_writes = true
  }

  ignore_warnings = true
}
