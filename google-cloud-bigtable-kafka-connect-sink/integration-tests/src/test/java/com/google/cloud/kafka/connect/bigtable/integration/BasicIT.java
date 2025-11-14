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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BasicIT extends BaseKafkaConnectBigtableIT {
  @Test
  public void testSimpleWrite() throws InterruptedException, ExecutionException {
    Map<String, String> props = baseConnectorProps();
    String topic = startSingleTopicConnector(props);
    createTablesAndColumnFamilies(topic);

    String key = "key";
    ByteString keyBytes = ByteString.copyFrom(key.getBytes(StandardCharsets.UTF_8));
    String value = "value";
    connect.kafka().produce(topic, key, value);
    waitUntilBigtableContainsNumberOfRows(topic, 1);

    Map<ByteString, Row> allRows = readAllRows(bigtableData, topic);
    assertEquals(1, allRows.size());
    Row row = allRows.get(keyBytes);
    List<RowCell> rowCells = row.getCells(topic, "KAFKA_VALUE");
    assertEquals(1, rowCells.size());
    assertArrayEquals(
        value.getBytes(StandardCharsets.UTF_8), rowCells.get(0).getValue().toByteArray());
  }
}
