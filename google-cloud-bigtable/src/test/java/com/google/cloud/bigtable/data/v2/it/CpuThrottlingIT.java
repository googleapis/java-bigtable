/*
 * Copyright 2021 Google LLC
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
package com.google.cloud.bigtable.data.v2.it;

import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.batching.BatcherImpl;
import com.google.api.gax.batching.FlowControlEventStats;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CpuThrottlingIT {

  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  @Test(timeout = 60 * 1000)
  public void test() throws IOException, InterruptedException {
    // I need to set up test to run multiple requests to the server
    BigtableDataSettings settings = testEnvRule.env().getDataClientSettings();
    BigtableDataSettings.Builder builder =
        settings.toBuilder().enableBatchMutationCpuBasedThrottling();

    BulkMutation mutations = BulkMutation.create(testEnvRule.env().getTableId())
        .add("row-key-1", Mutation.create().setCell("cf","qual", "value"))
        .add("row-key-2", Mutation.create().setCell("cf","qual", "value"))
        .add("row-key-3", Mutation.create().setCell("cf","qual", "value"));

    BigtableDataClient client = BigtableDataClient.create(builder.build());

    client.bulkMutateRows(mutations); // How can I mock the responses?? How do I get the cpu data inside of the response?
    //batcher.add();

    //client.bulkMutateRows();

    // Verify
  }
}
