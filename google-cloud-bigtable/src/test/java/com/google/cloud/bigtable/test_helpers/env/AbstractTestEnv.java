/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.test_helpers.env;

import com.google.cloud.bigtable.admin.v2.BigtableInstanceAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.models.AppProfile;
import com.google.cloud.bigtable.admin.v2.models.Cluster;
import com.google.cloud.bigtable.admin.v2.models.Instance;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import org.threeten.bp.Instant;
import org.threeten.bp.temporal.ChronoUnit;

/**
 * Defines the interface of a target environment.
 *
 * <p>This allows for integration tests to run against either production or an emulator.
 */
public abstract class AbstractTestEnv {
  private static final String PREFIX = "temp-";
  public static final String TEST_INSTANCE_PREFIX = "temp-instance-";
  public static final String TEST_CLUSTER_PREFIX = "temp-cluster-";
  public static final String TEST_APP_PREFIX = "temp-Ap-";

  abstract void start() throws Exception;

  abstract void stop() throws Exception;

  public abstract BigtableDataClient getDataClient();

  public abstract BigtableTableAdminClient getTableAdminClient();

  public abstract BigtableInstanceAdminClient getInstanceAdminClient();

  public abstract BigtableDataSettings getDataClientSettings();

  public abstract String getProjectId();

  public abstract String getInstanceId();

  public abstract String getTableId();

  public String getFamilyId() {
    return "cf";
  }

  public String generateTableId(String suffix) {
    return newPrefix() + "-" + suffix;
  }

  private static String newPrefix() {
    return newPrefix(Instant.now());
  }

  private static String newPrefix(Instant instant) {
    return String.format(PREFIX + "015%d", instant.getEpochSecond());
  }

  public boolean isInstanceAdminSupported() {
    return true;
  }

  public boolean isDirectPathEnabled() {
    return "bigtable".equals(System.getenv("GOOGLE_CLOUD_ENABLE_DIRECT_PATH"));
  }

  public String getPrimaryZone() {
    return "us-east1-b";
  }

  public String getSecondaryZone() {
    return "us-central1-b";
  }

  void cleanUpStale() {
    cleanupStaleTables();
    if (isInstanceAdminSupported()) {
      cleanUpStaleAppProfile();
      cleanUpStaleClusters();
      cleanUpStaleInstances();
    }
  }

  private void cleanupStaleTables() {
    String stalePrefix = newPrefix(Instant.now().minus(1, ChronoUnit.DAYS));

    for (String tableId : getTableAdminClient().listTables()) {
      if (!tableId.startsWith(PREFIX)) {
        continue;
      }
      if (stalePrefix.compareTo(tableId) > 0) {
        getTableAdminClient().deleteTable(tableId);
      }
    }
  }

  private void cleanUpStaleAppProfile() {
    String staleAPPattern = TEST_APP_PREFIX + Instant.now().minus(1, ChronoUnit.DAYS);

    for (AppProfile appProfile : getInstanceAdminClient().listAppProfiles(getInstanceId())) {
      String appProfileId = appProfile.getId();
      if (appProfileId.startsWith(staleAPPattern) && staleAPPattern.compareTo(appProfileId) > 0) {
        getInstanceAdminClient().deleteAppProfile(getInstanceId(), appProfileId, true);
      }
    }
  }

  private void cleanUpStaleClusters() {
    String staleClusterId = TEST_CLUSTER_PREFIX + Instant.now().minus(1, ChronoUnit.DAYS);

    for (Cluster cluster : getInstanceAdminClient().listClusters(getInstanceId())) {
      String clusterId = cluster.getId();
      if (clusterId.startsWith(staleClusterId) && staleClusterId.compareTo(clusterId) > 0) {
        getInstanceAdminClient().deleteCluster(getInstanceId(), clusterId);
      }
    }
  }

  private void cleanUpStaleInstances() {
    String staleInstanceId = TEST_INSTANCE_PREFIX + Instant.now().minus(1, ChronoUnit.DAYS);

    for (Instance ins : getInstanceAdminClient().listInstances()) {
      String insId = ins.getId();
      if (insId.startsWith(staleInstanceId) && staleInstanceId.compareTo(insId) > 0) {
        getInstanceAdminClient().deleteInstance(insId);
      }
    }
  }
}
