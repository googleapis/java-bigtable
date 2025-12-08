/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.bigtable.admin.v2.samples;

// [START bigtableadmin_v2_generated_BigtableInstanceAdmin_UpdateCluster_LRO_async]
import com.google.api.gax.longrunning.OperationFuture;
import com.google.bigtable.admin.v2.Cluster;
import com.google.bigtable.admin.v2.ClusterName;
import com.google.bigtable.admin.v2.LocationName;
import com.google.bigtable.admin.v2.StorageType;
import com.google.bigtable.admin.v2.UpdateClusterMetadata;
import com.google.cloud.bigtable.admin.v2.BaseBigtableInstanceAdminClient;

public class AsyncUpdateClusterLRO {

  public static void main(String[] args) throws Exception {
    asyncUpdateClusterLRO();
  }

  public static void asyncUpdateClusterLRO() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (BaseBigtableInstanceAdminClient baseBigtableInstanceAdminClient =
        BaseBigtableInstanceAdminClient.create()) {
      Cluster request =
          Cluster.newBuilder()
              .setName(ClusterName.of("[PROJECT]", "[INSTANCE]", "[CLUSTER]").toString())
              .setLocation(LocationName.of("[PROJECT]", "[LOCATION]").toString())
              .setServeNodes(-1288838783)
              .setDefaultStorageType(StorageType.forNumber(0))
              .setEncryptionConfig(Cluster.EncryptionConfig.newBuilder().build())
              .build();
      OperationFuture<Cluster, UpdateClusterMetadata> future =
          baseBigtableInstanceAdminClient.updateClusterOperationCallable().futureCall(request);
      // Do something.
      Cluster response = future.get();
    }
  }
}
// [END bigtableadmin_v2_generated_BigtableInstanceAdmin_UpdateCluster_LRO_async]
