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

import com.google.api.core.ApiFuture;
import com.google.api.gax.batching.Batcher;
import com.google.api.gax.batching.BatcherImpl;
import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.batching.DynamicFlowControlSettings;
import com.google.api.gax.batching.FlowControlEventStats;
import com.google.api.gax.batching.FlowControlSettings;
import com.google.api.gax.batching.FlowController;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.cloud.bigtable.data.v2.stub.DynamicFlowControlStats;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BulkMutateIT {

  @ClassRule
  public static TestEnvRule testEnvRule = new TestEnvRule();

  @Test (timeout = 20 * 1000)
  public void test() throws IOException, InterruptedException {
    BigtableDataSettings settings = testEnvRule.env().getDataClientSettings();
    // Set target latency really low so it'll trigger adjusting thresholds
    BigtableDataSettings.Builder builder =
        settings.toBuilder().enableBatchMutationLatencyBasedThrottling(2L);
    BigtableDataClient client = BigtableDataClient.create(builder.build());
    BatcherImpl batcher =
        (BatcherImpl) client.newBulkMutationBatcher(testEnvRule.env().getTableId());
    try {
      FlowControlEventStats events = batcher.getFlowControlEventStats();
      FlowController flowController = batcher.getFlowController();
      // DynamicFlowControlStats dynamicFlowControlStats =
      //     settings.getStubSettings().bulkMutateRowsSettings().toBuilder()
      //         .getDynamicFlowControlStats();

      assertThat(batcher.getFlowController().getCurrentOutstandingElementCount()).isNotEqualTo(
          batcher.getFlowController().getMinOutstandingElementCount());
      assertThat(batcher.getFlowController().getCurrentOutstandingElementCount()).isNotEqualTo(
          batcher.getFlowController().getMaxOutstandingElementCount());

      String familyId = testEnvRule.env().getFamilyId();
      long initial = batcher.getFlowController().getCurrentOutstandingElementCount();
      for (long i = 0; i < initial * 3; i++) {
        String key = "test-key" + i;
        batcher.add(
            RowMutationEntry.create(key).setCell(familyId, "qualifier", i));
      }
      batcher.flush();
      assertThat(events.getLastFlowControlEvent()).isNotNull();
      //assertThat(dynamicFlowControlStats.getAdjustedCounter()).isEqualTo(1);
      Row row =
          testEnvRule
              .env()
              .getDataClient()
              .readRowsCallable()
              .first()
              .call(Query.create(testEnvRule.env().getTableId()).rowKey("test-key" + initial));
      assertThat(row.getCells()).hasSize(1);
    } finally {
      batcher.close();
    }
  }
}
