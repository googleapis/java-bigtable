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

import com.example.bigtable.MobileTimeSeriesBaseTest;
import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.common.truth.Truth;
import java.io.IOException;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/*
 * These tests are order dependent because they delete rows and cells from a table. They are prefixed with `testN_` to signal the order in which they should run.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeletesTest extends MobileTimeSeriesBaseTest {
  public static BigtableDataClient bigtableDataClient;

  @BeforeClass
  public static void beforeClass() throws IOException {
    initializeVariables();
    createTable();
    writeStatsData();
    writePlanData();
    bigtableDataClient = BigtableDataClient.create(projectId, instanceId);
  }

  @AfterClass
  public static void afterClass() throws IOException {
    cleanupTable();
  }

  @Test
  public void test1_testDeleteFromColumn() {
    String rowKey = "phone#4c410523#20190501";
    Row row = bigtableDataClient.readRow(TABLE_ID, rowKey);
    String familyName = "cell_plan";
    String qualifier = "data_plan_01gb";
    List<RowCell> cells = row.getCells(familyName, qualifier);
    Truth.assertThat(cells).isNotEmpty();
    DeleteFromColumnExample deleteFromColumnExample = new DeleteFromColumnExample();
    deleteFromColumnExample.deleteFromColumn(
        projectId, instanceId, TABLE_ID, rowKey, familyName, qualifier);

    row = bigtableDataClient.readRow(TABLE_ID, rowKey);
    List<RowCell> cellsAfterDelete = row.getCells(familyName, qualifier);
    Truth.assertThat(cellsAfterDelete).isEmpty();
  }

  @Test
  public void test2_testDeleteFromRow() {
    String rowKey = "phone#4c410523#20190501";
    Row row = bigtableDataClient.readRow(TABLE_ID, rowKey);
    Truth.assertThat(row).isNotNull();
    DeleteFromRowExample deleteFromRowExample = new DeleteFromRowExample();
    deleteFromRowExample.deleteFromRow(projectId, instanceId, TABLE_ID, rowKey);
    row = bigtableDataClient.readRow(TABLE_ID, rowKey);
    Truth.assertThat(row).isNull();
  }

  @Test
  public void test3_testStreamingAndBatching() {
    String rowKey = "phone#4c410523#20190502";
    Row row = bigtableDataClient.readRow(TABLE_ID, rowKey);

    String familyName = "cell_plan";
    String qualifier = "data_plan_05gb";
    List<RowCell> cells = row.getCells(familyName, qualifier);
    Truth.assertThat(cells).isNotEmpty();
    DeleteStreamingAndBatchingExample deleteStreamingAndBatchingExample =
        new DeleteStreamingAndBatchingExample();
    deleteStreamingAndBatchingExample.streamingAndBatching(
        projectId, instanceId, TABLE_ID, familyName, qualifier);
    row = bigtableDataClient.readRow(TABLE_ID, rowKey);
    List<RowCell> cellsAfterDelete = row.getCells(familyName, qualifier);
    Truth.assertThat(cellsAfterDelete).isEmpty();
  }

  @Test
  public void test4_testCheckAndMutate() {
    String rowKey = "phone#5c10102#20190502";
    Row row = bigtableDataClient.readRow(TABLE_ID, rowKey);

    String family = "cell_plan";
    String qualifier = "data_plan_10gb";
    List<RowCell> cells = row.getCells(family, qualifier);
    Truth.assertThat(cells).isNotEmpty();
    DeleteCheckAndMutateExample deleteCheckAndMutateExample = new DeleteCheckAndMutateExample();
    deleteCheckAndMutateExample.checkAndMutate(
        projectId, instanceId, TABLE_ID, rowKey, family, qualifier);
    row = bigtableDataClient.readRow(TABLE_ID, rowKey);
    List<RowCell> cellsAfterDelete = row.getCells(family, qualifier);
    Truth.assertThat(cellsAfterDelete).isEmpty();
  }

  @Test
  public void test5_testDropRowRange() {
    String rowPrefix = "phone#4c410523";
    Query query = Query.create(TABLE_ID).prefix(rowPrefix);
    ServerStream<Row> rows = bigtableDataClient.readRows(query);
    int rowCount = 0;
    for (Row ignored : rows) {
      rowCount++;
    }
    Truth.assertThat(rowCount).isGreaterThan(1);
    DeleteDropRowRangeExample deleteDropRowRangeExample = new DeleteDropRowRangeExample();
    deleteDropRowRangeExample.dropRowRange(projectId, instanceId, TABLE_ID, rowPrefix);
    rows = bigtableDataClient.readRows(query);
    rowCount = 0;
    for (Row ignored : rows) {
      rowCount++;
    }
    Truth.assertThat(rowCount).isEqualTo(0);
  }

  @Test
  public void test6_testDeleteColumnFamily() {
    String rowKey = "phone#5c10102#20190501";
    Row row = bigtableDataClient.readRow(TABLE_ID, rowKey);
    String familyName = "stats_summary";
    List<RowCell> cells = row.getCells(familyName);
    Truth.assertThat(cells).isNotEmpty();
    DeleteColumnFamilyExample deleteColumnFamilyExample = new DeleteColumnFamilyExample();
    deleteColumnFamilyExample.deleteColumnFamily(
        projectId, instanceId, TABLE_ID, rowKey, familyName);
    row = bigtableDataClient.readRow(TABLE_ID, rowKey);
    List<RowCell> cellsAfterDelete = row.getCells(familyName);
    Truth.assertThat(cellsAfterDelete).isEmpty();
  }

  @Test
  public void test7_testDeleteTable() throws IOException {
    try (BigtableTableAdminClient tableAdminClient =
        BigtableTableAdminClient.create(projectId, instanceId)) {
      Truth.assertThat(tableAdminClient.exists(TABLE_ID)).isTrue();
      DeleteTableExample deleteTableExample = new DeleteTableExample();
      deleteTableExample.deleteTable(projectId, instanceId, TABLE_ID);
      Truth.assertThat(tableAdminClient.exists(TABLE_ID)).isFalse();
    }
  }
}
