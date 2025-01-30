/*
 * Copyright 2025 Google LLC
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

import static org.junit.Assert.assertEquals;

import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.kafka.connect.bigtable.config.BigtableErrorMode;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.config.InsertMode;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class InsertUpsertIT extends BaseKafkaConnectBigtableIT {
  private static final String KEY1 = "key1";
  private static final String KEY2 = "key2";
  private static final ByteString KEY1_BYTES =
      ByteString.copyFrom(KEY1.getBytes(StandardCharsets.UTF_8));
  private static final ByteString KEY2_BYTES =
      ByteString.copyFrom(KEY2.getBytes(StandardCharsets.UTF_8));
  private static final String VALUE1 = "value1";
  private static final String VALUE2 = "value2";
  private static final String VALUE3 = "value3";

  @Test
  public void testInsert() throws InterruptedException {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES, "true");
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES, "true");
    props.put(BigtableSinkConfig.CONFIG_INSERT_MODE, InsertMode.INSERT.name());
    props.put(BigtableSinkConfig.CONFIG_ERROR_MODE, BigtableErrorMode.IGNORE.name());
    String testId = startSingleTopicConnector(props);

    connect.kafka().produce(testId, KEY1, VALUE1);
    connect.kafka().produce(testId, KEY1, VALUE2);
    connect.kafka().produce(testId, KEY2, VALUE3);

    waitUntilBigtableContainsNumberOfRows(testId, 2);

    Map<ByteString, Row> rows = readAllRows(bigtableData, testId);
    Row row1 = rows.get(KEY1_BYTES);
    Row row2 = rows.get(KEY2_BYTES);
    assertEquals(1, row1.getCells().size());
    assertEquals(1, row2.getCells().size());
  }

  @Test
  public void testUpsert() throws InterruptedException {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES, "true");
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES, "true");
    props.put(BigtableSinkConfig.CONFIG_INSERT_MODE, InsertMode.UPSERT.name());
    props.put(BigtableSinkConfig.CONFIG_ERROR_MODE, BigtableErrorMode.IGNORE.name());
    String testId = startSingleTopicConnector(props);

    connect.kafka().produce(testId, KEY1, VALUE1);
    connect.kafka().produce(testId, KEY1, VALUE2);
    connect.kafka().produce(testId, KEY2, VALUE3);

    waitUntilBigtableContainsNumberOfRows(testId, 2);

    Map<ByteString, Row> rows = readAllRows(bigtableData, testId);
    Row row1 = rows.get(KEY1_BYTES);
    Row row2 = rows.get(KEY2_BYTES);
    assertEquals(2, row1.getCells().size());
    assertEquals(1, row2.getCells().size());
  }
}
