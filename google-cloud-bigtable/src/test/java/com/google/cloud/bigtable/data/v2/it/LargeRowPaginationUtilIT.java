/*
 * Copyright 2026 Google LLC
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
import static com.google.common.truth.TruthJUnit.assume;

import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.admin.v2.models.Table;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Filters;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.TableId;
import com.google.cloud.bigtable.data.v2.stub.readrows.LargeRowPaginationUtil;
import com.google.cloud.bigtable.test_helpers.env.EmulatorEnv;
import com.google.cloud.bigtable.test_helpers.env.PrefixGenerator;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.protobuf.ByteString;
import io.grpc.Status;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LargeRowPaginationUtilIT {

  @ClassRule public static final TestEnvRule testEnvRule = new TestEnvRule();

  private BigtableTableAdminClient tableAdminClient;
  private BigtableDataClient dataClient;
  private Table table;
  private final String familyId1 = "cf1";
  private final String familyId2 = "cf2";
  private ByteString rowKey;
  private ByteString largeValue;

  @Before
  public void setup() throws Exception {
    tableAdminClient = testEnvRule.env().getTableAdminClient();
    dataClient = testEnvRule.env().getDataClient();

    // Skip population for emulator as it doesn't support the expected failure
    if (testEnvRule.env() instanceof EmulatorEnv) {
      return;
    }

    String tableId = PrefixGenerator.newPrefix("LargeRowPagination");
    table =
        tableAdminClient.createTable(
            CreateTableRequest.of(tableId).addFamily(familyId1).addFamily(familyId2));

    rowKey = ByteString.copyFromUtf8("large-row-key");
    byte[] largeValueBytes = new byte[100 * 1024 * 1024];
    new Random().nextBytes(largeValueBytes);
    largeValue = ByteString.copyFrom(largeValueBytes);

    // Populate cf1: 1 qualifier, 2 versions
    for (int i = 0; i < 2; i++) {
      dataClient
          .mutateRowAsync(
              RowMutation.create(TableId.of(table.getId()), rowKey)
                  .setCell(familyId1, ByteString.copyFromUtf8("q1"), largeValue))
          .get(10, TimeUnit.MINUTES);
    }

    // Populate cf2: 2 qualifiers, 2 versions each
    for (int q = 1; q <= 2; q++) {
      for (int v = 0; v < 2; v++) {
        dataClient
            .mutateRowAsync(
                RowMutation.create(TableId.of(table.getId()), rowKey)
                    .setCell(familyId2, ByteString.copyFromUtf8("q" + q), largeValue))
            .get(10, TimeUnit.MINUTES);
      }
    }
  }

  @After
  public void tearDown() {
    if (table != null) {
      tableAdminClient.deleteTable(table.getId());
    }
  }

  @Test
  public void testReadLargeRow() throws Exception {
    assume()
        .withMessage("Large row read errors are not supported by emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    // Verify it fails with standard read
    try {
      dataClient.readRow(TableId.of(table.getId()), rowKey);
      org.junit.Assert.fail("Should have failed with FAILED_PRECONDITION");
    } catch (ApiException e) {
      assertThat(e.getStatusCode()).isEqualTo(GrpcStatusCode.of(Status.Code.FAILED_PRECONDITION));
    }

    // Read without filter: 2 (cf1) + 4 (cf2) = 6 cells
    Row row = LargeRowPaginationUtil.readLargeRow(dataClient, table.getId(), rowKey, null);
    assertThat(row).isNotNull();
    assertThat(row.getKey()).isEqualTo(rowKey);
    assertThat(row.getCells()).hasSize(6);

    // Verify cell content
    // cf1:q1 (2 versions)
    assertThat(row.getCells().get(0).getFamily()).isEqualTo(familyId1);
    assertThat(row.getCells().get(0).getQualifier().toStringUtf8()).isEqualTo("q1");
    assertThat(row.getCells().get(0).getValue()).isEqualTo(largeValue);

    assertThat(row.getCells().get(1).getFamily()).isEqualTo(familyId1);
    assertThat(row.getCells().get(1).getQualifier().toStringUtf8()).isEqualTo("q1");
    assertThat(row.getCells().get(1).getValue()).isEqualTo(largeValue);

    // cf2:q1 (2 versions)
    assertThat(row.getCells().get(2).getFamily()).isEqualTo(familyId2);
    assertThat(row.getCells().get(2).getQualifier().toStringUtf8()).isEqualTo("q1");
    assertThat(row.getCells().get(2).getValue()).isEqualTo(largeValue);

    assertThat(row.getCells().get(3).getFamily()).isEqualTo(familyId2);
    assertThat(row.getCells().get(3).getQualifier().toStringUtf8()).isEqualTo("q1");
    assertThat(row.getCells().get(3).getValue()).isEqualTo(largeValue);

    // cf2:q2 (2 versions)
    assertThat(row.getCells().get(4).getFamily()).isEqualTo(familyId2);
    assertThat(row.getCells().get(4).getQualifier().toStringUtf8()).isEqualTo("q2");
    assertThat(row.getCells().get(4).getValue()).isEqualTo(largeValue);

    assertThat(row.getCells().get(5).getFamily()).isEqualTo(familyId2);
    assertThat(row.getCells().get(5).getQualifier().toStringUtf8()).isEqualTo("q2");
    assertThat(row.getCells().get(5).getValue()).isEqualTo(largeValue);
  }

  @Test
  public void testReadLargeRowWithFilter() throws Exception {
    assume()
        .withMessage("Large row read errors are not supported by emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    // Filter for most recent version: 1 (cf1) + 2 (cf2) = 3 cells
    Filters.Filter filter = Filters.FILTERS.limit().cellsPerColumn(1);
    Row row = LargeRowPaginationUtil.readLargeRow(dataClient, table.getId(), rowKey, filter);

    assertThat(row).isNotNull();
    assertThat(row.getKey()).isEqualTo(rowKey);
    assertThat(row.getCells()).hasSize(3);

    // cf1:q1
    assertThat(row.getCells().get(0).getFamily()).isEqualTo(familyId1);
    assertThat(row.getCells().get(0).getQualifier().toStringUtf8()).isEqualTo("q1");
    assertThat(row.getCells().get(0).getValue()).isEqualTo(largeValue);

    // cf2:q1
    assertThat(row.getCells().get(1).getFamily()).isEqualTo(familyId2);
    assertThat(row.getCells().get(1).getQualifier().toStringUtf8()).isEqualTo("q1");
    assertThat(row.getCells().get(1).getValue()).isEqualTo(largeValue);

    // cf2:q2
    assertThat(row.getCells().get(2).getFamily()).isEqualTo(familyId2);
    assertThat(row.getCells().get(2).getQualifier().toStringUtf8()).isEqualTo("q2");
    assertThat(row.getCells().get(2).getValue()).isEqualTo(largeValue);
  }
}
