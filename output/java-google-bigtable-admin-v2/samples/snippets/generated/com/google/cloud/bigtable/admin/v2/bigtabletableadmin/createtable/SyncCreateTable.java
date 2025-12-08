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

// [START bigtableadmin_v2_generated_BigtableTableAdmin_CreateTable_sync]
import com.google.bigtable.admin.v2.CreateTableRequest;
import com.google.bigtable.admin.v2.InstanceName;
import com.google.bigtable.admin.v2.Table;
import com.google.cloud.bigtable.admin.v2.BaseBigtableTableAdminClient;
import java.util.ArrayList;

public class SyncCreateTable {

  public static void main(String[] args) throws Exception {
    syncCreateTable();
  }

  public static void syncCreateTable() throws Exception {
    // This snippet has been automatically generated and should be regarded as a code template only.
    // It will require modifications to work:
    // - It may require correct/in-range values for request initialization.
    // - It may require specifying regional endpoints when creating the service client as shown in
    // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
    try (BaseBigtableTableAdminClient baseBigtableTableAdminClient =
        BaseBigtableTableAdminClient.create()) {
      CreateTableRequest request =
          CreateTableRequest.newBuilder()
              .setParent(InstanceName.of("[PROJECT]", "[INSTANCE]").toString())
              .setTableId("tableId-1552905847")
              .setTable(Table.newBuilder().build())
              .addAllInitialSplits(new ArrayList<CreateTableRequest.Split>())
              .build();
      Table response = baseBigtableTableAdminClient.createTable(request);
    }
  }
}
// [END bigtableadmin_v2_generated_BigtableTableAdmin_CreateTable_sync]
