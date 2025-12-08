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

// [START bigtableadmin_v2_generated_BigtableTableAdmin_SnapshotTable_LRO_async]
import com.google.api.gax.longrunning.OperationFuture;
import com.google.bigtable.admin.v2.ClusterName;
import com.google.bigtable.admin.v2.Snapshot;
import com.google.bigtable.admin.v2.SnapshotTableMetadata;
import com.google.bigtable.admin.v2.SnapshotTableRequest;
import com.google.bigtable.admin.v2.TableName;
import com.google.cloud.bigtable.admin.v2.BaseBigtableTableAdminClient;
import com.google.protobuf.Duration;

public class AsyncSnapshotTableLRO {

  public static void main(String[] args) throws Exception {
    asyncSnapshotTableLRO();
  }

  public static void asyncSnapshotTableLRO() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (BaseBigtableTableAdminClient baseBigtableTableAdminClient =
        BaseBigtableTableAdminClient.create()) {
      SnapshotTableRequest request =
          SnapshotTableRequest.newBuilder()
              .setName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
              .setCluster(ClusterName.of("[PROJECT]", "[INSTANCE]", "[CLUSTER]").toString())
              .setSnapshotId("snapshotId-1113817601")
              .setTtl(Duration.newBuilder().build())
              .setDescription("description-1724546052")
              .build();
      OperationFuture<Snapshot, SnapshotTableMetadata> future =
          baseBigtableTableAdminClient.snapshotTableOperationCallable().futureCall(request);
      // Do something.
      Snapshot response = future.get();
    }
  }
}
// [END bigtableadmin_v2_generated_BigtableTableAdmin_SnapshotTable_LRO_async]
