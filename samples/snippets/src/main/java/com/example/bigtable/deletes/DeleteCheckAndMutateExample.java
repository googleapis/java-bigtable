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

// [START bigtable_delete_check_and_mutate]
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.ConditionalRowMutation;
import com.google.cloud.bigtable.data.v2.models.Filters;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import java.io.IOException;

public class DeleteCheckAndMutateExample {
  public void checkAndMutate(
      String projectId,
      String instanceId,
      String tableId,
      String rowKey,
      String family,
      String qualifier) {
    try (BigtableDataClient dataClient = BigtableDataClient.create(projectId, instanceId)) {
      Filters.Filter condition = Filters.FILTERS.qualifier().exactMatch(qualifier);
      Mutation mutation = Mutation.create().deleteCells(family, qualifier);
      dataClient.checkAndMutateRow(
          ConditionalRowMutation.create(tableId, rowKey).condition(condition).then(mutation));
    } catch (IOException e) {
      System.err.println("An exception has occurred: " + e.getMessage());
    }
  }
}
// [END bigtable_delete_check_and_mutate]
