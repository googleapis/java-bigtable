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
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LargeRowPaginationUtilIT {

  private static final Logger logger = Logger.getLogger(LargeRowPaginationUtilIT.class.getName());

  @ClassRule public static final TestEnvRule testEnvRule = new TestEnvRule();

  private BigtableTableAdminClient tableAdminClient;
  private BigtableDataClient dataClient;
  private Table table;
  private final String familyId = "cf";

  @Before
  public void setup() {
    tableAdminClient = testEnvRule.env().getTableAdminClient();
    dataClient = testEnvRule.env().getDataClient();
    String tableId = PrefixGenerator.newPrefix("LargeRowPagination");
    table =
        tableAdminClient.createTable(
            CreateTableRequest.of(tableId)
                .addFamily(familyId)
                .addFamily(familyId + "_1")
                .addFamily(familyId + "_2"));
  }

  @After
  public void tearDown() {
    if (table != null) {
      tableAdminClient.deleteTable(table.getId());
    }
  }

  @Test
  public void testReadLargeRowSingleFamily() throws Exception {
    assume()
        .withMessage("Large row read errors are not supported by emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    String rowKeyStr = UUID.randomUUID().toString();
    ByteString rowKey = ByteString.copyFromUtf8(rowKeyStr);

    byte[] largeValueBytes = new byte[100 * 1024 * 1024];
    Random random = new Random();
    random.nextBytes(largeValueBytes);
    ByteString largeValue = ByteString.copyFrom(largeValueBytes);

    for (int i = 0; i < 3; i++) {
      dataClient
          .mutateRowAsync(
              RowMutation.create(table.getId(), rowKeyStr)
                  .setCell(familyId, ByteString.copyFromUtf8("q" + i), largeValue))
          .get(10, TimeUnit.MINUTES);
    }

    try {
      dataClient.readRow(TableId.of(table.getId()), rowKey);
    } catch (ApiException e) {
      assertThat(e.getStatusCode()).isEqualTo(GrpcStatusCode.of(Status.Code.FAILED_PRECONDITION));
    }

    Row row = LargeRowPaginationUtil.readLargeRow(dataClient, table.getId(), rowKey);
    assertThat(row).isNotNull();
    assertThat(row.getKey()).isEqualTo(rowKey);
    assertThat(row.getCells()).hasSize(3);
    for (int i = 0; i < 3; i++) {
      assertThat(row.getCells().get(i).getFamily()).isEqualTo(familyId);
      assertThat(row.getCells().get(i).getQualifier()).isEqualTo(ByteString.copyFromUtf8("q" + i));
      assertThat(row.getCells().get(i).getValue()).isEqualTo(largeValue);
    }
  }

  @Test
  public void testReadLargeRowMultipleFamilies() throws Exception {
    assume()
        .withMessage("Large row read errors are not supported by emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    String rowKeyStr = UUID.randomUUID().toString();
    ByteString rowKey = ByteString.copyFromUtf8(rowKeyStr);

    byte[] largeValueBytes = new byte[100 * 1024 * 1024];
    Random random = new Random();
    random.nextBytes(largeValueBytes);
    ByteString largeValue = ByteString.copyFrom(largeValueBytes);

    String[] families = {familyId, familyId + "_1", familyId + "_2"};

    for (int i = 0; i < 3; i++) {
      dataClient
          .mutateRowAsync(
              RowMutation.create(table.getId(), rowKeyStr)
                  .setCell(families[i], ByteString.copyFromUtf8("q"), largeValue))
          .get(10, TimeUnit.MINUTES);
    }

    try {
      dataClient.readRow(TableId.of(table.getId()), rowKey);
    } catch (ApiException e) {
      assertThat(e.getStatusCode()).isEqualTo(GrpcStatusCode.of(Status.Code.FAILED_PRECONDITION));
    }

    Row row = LargeRowPaginationUtil.readLargeRow(dataClient, table.getId(), rowKey);

    assertThat(row).isNotNull();
    assertThat(row.getKey()).isEqualTo(rowKey);
    assertThat(row.getCells()).hasSize(3);
    for (int i = 0; i < 3; i++) {
      assertThat(row.getCells().get(i).getFamily()).isEqualTo(families[i]);
      assertThat(row.getCells().get(i).getQualifier()).isEqualTo(ByteString.copyFromUtf8("q"));
      assertThat(row.getCells().get(i).getValue()).isEqualTo(largeValue);
    }
  }
}
