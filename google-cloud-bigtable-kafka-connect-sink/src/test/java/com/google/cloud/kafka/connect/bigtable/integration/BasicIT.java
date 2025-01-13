/*
 * Copyright 2024 Google LLC
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
package com.google.cloud.kafka.connect.bigtable.integration;

import static org.apache.kafka.test.TestUtils.waitForCondition;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.config.InsertMode;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.kafka.connect.runtime.SinkConnectorConfig;
import org.apache.kafka.test.TestCondition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BasicIT extends BaseIT {
  private static final int TASK_CONSUME_TIMEOUT_MS = 10000;

  @Test
  public void testSimpleWrite() throws InterruptedException {
    BigtableDataClient bigtable = getBigtableDataClient();
    String topic = getTestCaseId();
    String connectorName = "connector-" + topic;
    connect.kafka().createTopic(topic, numTasks);
    Map<String, String> props = baseConnectorProps();
    props.put(SinkConnectorConfig.TOPICS_CONFIG, topic);
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES, "true");
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES, "true");
    props.put(BigtableSinkConfig.CONFIG_INSERT_MODE, InsertMode.UPSERT.name());
    connect.configureConnector(connectorName, props);
    connect
        .assertions()
        .assertConnectorAndAtLeastNumTasksAreRunning(
            connectorName, numTasks, "Connector start timeout");

    int numberOfRecords = 1;
    String key = "key";
    String value = "value";
    connect.kafka().produce(topic, key, value);

    TestCondition testCondition =
        () -> {
          List<Row> allRows = readAllRows(bigtable, topic);
          if (numberOfRecords != allRows.size()) {
            return false;
          }
          Row row = allRows.get(0);
          if (!ByteString.copyFrom(key.getBytes(StandardCharsets.UTF_8)).equals(row.getKey())) {
            return false;
          }
          List<RowCell> rowCells = row.getCells("default", "KAFKA_VALUE");
          if (numberOfRecords != rowCells.size()) {
            return false;
          }
          return ByteString.copyFrom(value.getBytes(StandardCharsets.UTF_8))
              .equals(rowCells.get(0).getValue());
        };
    waitForCondition(
        testCondition, TASK_CONSUME_TIMEOUT_MS, "Correct results not received in time");
  }
}
