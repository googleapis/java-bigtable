/*
 * Copyright 2019 Google LLC
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

import static com.google.cloud.bigtable.misc_utilities.AuthorizedViewTestHelper.AUTHORIZED_VIEW_COLUMN_QUALIFIER;
import static com.google.cloud.bigtable.misc_utilities.AuthorizedViewTestHelper.AUTHORIZED_VIEW_ROW_PREFIX;
import static com.google.cloud.bigtable.misc_utilities.AuthorizedViewTestHelper.createTestAuthorizedView;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;
import static org.junit.Assert.fail;

import com.google.api.gax.batching.Batcher;
import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.admin.v2.models.AuthorizedView;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.AuthorizedViewId;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.cloud.bigtable.test_helpers.env.EmulatorEnv;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RowMutationEntryBatcherIT {

  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  @Test
  public void testNewBatcher() throws Exception {
    BigtableDataClient client = testEnvRule.env().getDataClient();
    String tableId = testEnvRule.env().getTableId();
    String family = testEnvRule.env().getFamilyId();
    String rowPrefix = UUID.randomUUID().toString();

    try (Batcher<RowMutationEntry, Void> batcher = client.newBulkMutationBatcher(tableId)) {
      for (int i = 0; i < 10; i++) {
        batcher.add(
            RowMutationEntry.create(rowPrefix + "-" + i)
                .setCell(family, "qualifier", 10_000L, "value-" + i));
      }
    }

    List<Row> expectedRows = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      expectedRows.add(
          Row.create(
              ByteString.copyFromUtf8(rowPrefix + "-" + i),
              ImmutableList.of(
                  RowCell.create(
                      family,
                      ByteString.copyFromUtf8("qualifier"),
                      10_000L,
                      ImmutableList.<String>of(),
                      ByteString.copyFromUtf8("value-" + i)))));
    }
    ServerStream<Row> actualRows = client.readRows(Query.create(tableId).prefix(rowPrefix));

    assertThat(actualRows).containsExactlyElementsIn(expectedRows);
  }

  @Test
  public void testNewBatcherOnAuthorizedView() throws Exception {
    assume()
        .withMessage("AuthorizedView is not supported on Emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    AuthorizedView testAuthorizedView = createTestAuthorizedView(testEnvRule);

    BigtableDataClient client = testEnvRule.env().getDataClient();
    String tableId = testEnvRule.env().getTableId();
    String family = testEnvRule.env().getFamilyId();
    String rowPrefix = AUTHORIZED_VIEW_ROW_PREFIX + UUID.randomUUID();

    try (Batcher<RowMutationEntry, Void> batcher =
        client.newBulkMutationBatcher(AuthorizedViewId.of(tableId, testAuthorizedView.getId()))) {
      for (int i = 0; i < 10; i++) {
        batcher.add(
            RowMutationEntry.create(rowPrefix + "-" + i)
                .setCell(family, AUTHORIZED_VIEW_COLUMN_QUALIFIER, 10_000L, "value-" + i));
      }
    }

    List<Row> expectedRows = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      expectedRows.add(
          Row.create(
              ByteString.copyFromUtf8(rowPrefix + "-" + i),
              ImmutableList.of(
                  RowCell.create(
                      family,
                      ByteString.copyFromUtf8(AUTHORIZED_VIEW_COLUMN_QUALIFIER),
                      10_000L,
                      ImmutableList.<String>of(),
                      ByteString.copyFromUtf8("value-" + i)))));
    }
    ServerStream<Row> actualRows = client.readRows(Query.create(tableId).prefix(rowPrefix));

    assertThat(actualRows).containsExactlyElementsIn(expectedRows);

    // We should not be able to mutate rows outside the authorized view
    String rowKeyOutsideAuthorizedView = UUID.randomUUID() + "-outside-authorized-view";
    try {
      try (Batcher<RowMutationEntry, Void> batcher =
          client.newBulkMutationBatcher(AuthorizedViewId.of(tableId, testAuthorizedView.getId()))) {
        batcher.add(
            RowMutationEntry.create(rowKeyOutsideAuthorizedView)
                .setCell(family, AUTHORIZED_VIEW_COLUMN_QUALIFIER, 10_000L, "value"));
      }
      fail("Should not be able to apply bulk mutation on rows outside authorized view");
    } catch (Exception e) {
      // Ignore.
    }

    testEnvRule
        .env()
        .getTableAdminClient()
        .deleteAuthorizedView(testEnvRule.env().getTableId(), testAuthorizedView.getId());
  }
}
