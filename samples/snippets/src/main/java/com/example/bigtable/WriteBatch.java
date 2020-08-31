/*
 * Copyright 2019 Google LLC
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

package com.example.bigtable;

// [START bigtable_writes_batch]

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.protobuf.ByteString;

public class WriteBatch {
  private static final String COLUMN_FAMILY_NAME = "stats_summary";

  public static void writeBatch(String projectId, String instanceId, String tableId) {
    // String projectId = "my-project-id";
    // String instanceId = "my-instance-id";
    // String tableId = "mobile-time-series";

    try (BigtableDataClient dataClient = BigtableDataClient.create(projectId, instanceId)) {
      long timestamp = System.currentTimeMillis() * 1000;

      BulkMutation bulkMutation =
          BulkMutation.create(tableId)
              .add(
                  "tablet#a0b81f74#20190501",
                  Mutation.create()
                      .setCell(
                          COLUMN_FAMILY_NAME,
                          ByteString.copyFrom("connected_wifi".getBytes()),
                          timestamp,
                          1)
                      .setCell(COLUMN_FAMILY_NAME, "os_build", timestamp, "12155.0.0-rc1"))
              .add(
                  "tablet#a0b81f74#20190502",
                  Mutation.create()
                      .setCell(
                          COLUMN_FAMILY_NAME,
                          ByteString.copyFrom("connected_wifi".getBytes()),
                          timestamp,
                          1)
                      .setCell(COLUMN_FAMILY_NAME, "os_build", timestamp, "12155.0.0-rc6"));

      dataClient.bulkMutateRows(bulkMutation);

      System.out.print("Successfully wrote 2 rows");
    } catch (Exception e) {
      System.out.println("Error during WriteBatch: \n" + e.toString());
    }
  }
}

// [END bigtable_writes_batch]
