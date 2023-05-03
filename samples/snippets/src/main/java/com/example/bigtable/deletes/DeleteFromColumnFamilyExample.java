/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bigtable.deletes;

// [START bigtable_delete_from_column_family]
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import java.io.IOException;

public class DeleteFromColumnFamilyExample {
  public void deleteFromColumnFamily(String projectId, String instanceId, String tableId)
      throws IOException {
    try (BigtableDataClient dataClient = BigtableDataClient.create(projectId, instanceId)) {
      dataClient.mutateRow(
          RowMutation.create(tableId, "phone#5c10102#20190501").deleteFamily("stats_summary"));
    }
  }
}
// [END bigtable_delete_from_column_family]
