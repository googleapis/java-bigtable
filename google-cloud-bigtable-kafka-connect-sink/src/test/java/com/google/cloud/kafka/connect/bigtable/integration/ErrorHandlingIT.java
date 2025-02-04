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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.config.InsertMode;
import com.google.cloud.kafka.connect.bigtable.config.NullValueMode;
import com.google.cloud.kafka.connect.bigtable.util.JsonConverterFactory;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.connect.converters.ByteArrayConverter;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.storage.StringConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ErrorHandlingIT extends BaseKafkaConnectBigtableIT {
  @Test
  public void testBigtableCredentialsAreCheckedOnStartup() {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.GCP_CREDENTIALS_JSON_CONFIG, "{}");

    String testId = getTestCaseId();
    assertThrows(Throwable.class, () -> connect.configureConnector(testId, props));
    assertThrows(Throwable.class, () -> connect.connectorStatus(testId));
  }

  @Test
  public void testTooLargeData() throws InterruptedException, ExecutionException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, ByteArrayConverter.class.getName());
    configureDlq(props, dlqTopic);
    String testId = startSingleTopicConnector(props);

    byte[] key = "key".getBytes(StandardCharsets.UTF_8);
    // The hard limit is 100 MB as per https://cloud.google.com/bigtable/quotas#limits-data-size
    int twoHundredMegabytes = 200 * 1000 * 1000;
    byte[] value = new byte[twoHundredMegabytes];
    getKafkaProducer().send(new ProducerRecord<>(testId, key, value)).get();

    assertSingleDlqEntry(dlqTopic, key, value, null);
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testSecondInsertIntoARowCausesAnError() throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    configureDlq(props, dlqTopic);
    String testId = startSingleTopicConnector(props);

    String key = "key";
    String valueOk = "ok";
    String valueRejected = "rejected";

    connect.kafka().produce(testId, key, valueOk);

    waitUntilBigtableContainsNumberOfRows(testId, 1);
    Map<ByteString, Row> rows = readAllRows(bigtableData, testId);
    assertEquals(1, rows.size());
    Row rowOk = rows.get(ByteString.copyFrom(key.getBytes(StandardCharsets.UTF_8)));
    assertEquals(1, rowOk.getCells().size());
    assertArrayEquals(
        valueOk.getBytes(StandardCharsets.UTF_8), rowOk.getCells().get(0).getValue().toByteArray());

    connect.kafka().produce(testId, key, valueRejected);
    assertSingleDlqEntry(dlqTopic, key, valueRejected, null);

    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testPartialBatchErrorWhenRelyingOnInputOrdering() throws InterruptedException {
    long dataSize = 1000;

    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.INSERT.name());
    props.put(
        ConnectorConfig.CONNECTOR_CLIENT_CONSUMER_OVERRIDES_PREFIX
            + ConsumerConfig.MAX_POLL_RECORDS_CONFIG,
        Long.toString(dataSize));
    props.put(
        ConnectorConfig.CONNECTOR_CLIENT_CONSUMER_OVERRIDES_PREFIX
            + ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
        Integer.toString(Integer.MAX_VALUE));
    props.put(
        ConnectorConfig.CONNECTOR_CLIENT_CONSUMER_OVERRIDES_PREFIX
            + ConsumerConfig.FETCH_MAX_BYTES_CONFIG,
        Integer.toString(Integer.MAX_VALUE));
    configureDlq(props, dlqTopic);
    String testId = startSingleTopicConnector(props);

    connect.pauseConnector(testId);
    List<Map.Entry<SchemaAndValue, SchemaAndValue>> keysAndValues = new ArrayList<>();
    // Every second record fails since every two consecutive records share a key and we use insert
    // mode. We rely on max batch size of 1 to know which of the records is going to fail.
    Function<Long, String> keyGenerator = i -> "key" + (i / 2);
    Function<Long, String> valueGenerator = i -> "value" + i;
    for (long i = 0; i < dataSize; i++) {
      SchemaAndValue key = new SchemaAndValue(Schema.STRING_SCHEMA, keyGenerator.apply(i));
      SchemaAndValue value = new SchemaAndValue(Schema.STRING_SCHEMA, valueGenerator.apply(i));
      keysAndValues.add(new AbstractMap.SimpleImmutableEntry<>(key, value));
    }
    sendRecords(testId, keysAndValues, new StringConverter(), new StringConverter());
    connect.resumeConnector(testId);

    waitUntilBigtableContainsNumberOfRows(testId, dataSize / 2);

    Map<ByteString, Row> rows = readAllRows(bigtableData, testId);
    assertEquals(dataSize / 2, rows.size());

    ConsumerRecords<byte[], byte[]> dlqRecords =
        connect.kafka().consume((int) dataSize / 2, Duration.ofSeconds(120).toMillis(), dlqTopic);
    Map<ByteString, byte[]> dlqValues = new HashMap<>();
    for (ConsumerRecord<byte[], byte[]> r : dlqRecords) {
      ByteString key = ByteString.copyFrom(r.key());
      dlqValues.put(key, r.value());
    }
    assertEquals(dataSize / 2, dlqValues.size());

    for (long i = 0; i < dataSize; i++) {
      ByteString key = ByteString.copyFrom(keyGenerator.apply(i).getBytes(StandardCharsets.UTF_8));
      byte[] expectedValue = valueGenerator.apply(i).getBytes(StandardCharsets.UTF_8);
      byte[] value;
      if (i % 2 == 0) {
        List<RowCell> cells = rows.get(key).getCells();
        assertEquals(1, cells.size());
        value = cells.get(0).getValue().toByteArray();
      } else {
        value = dlqValues.get(key);
      }
      assertTrue(rows.containsKey(key));
      assertTrue(dlqValues.containsKey(key));
      assertArrayEquals(expectedValue, value);
    }
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testDeletingARowTwiceWorks()
      throws InterruptedException, ExecutionException, TimeoutException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.DELETE.name());
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.UPSERT.name());
    configureDlq(props, dlqTopic);
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(false));
    String testId = startSingleTopicConnector(props);

    String key = "key";
    String putValue = "1";
    String deleteValue = "null";

    connect.kafka().produce(testId, key, putValue);
    waitUntilBigtableContainsNumberOfRows(testId, 1);
    assertEquals(
        key,
        new String(
            bigtableData.readRow(testId, key).getKey().toByteArray(), StandardCharsets.UTF_8));

    connect.kafka().produce(testId, key, deleteValue);
    waitUntilBigtableContainsNumberOfRows(testId, 0);
    assertTrue(readAllRows(bigtableData, testId).isEmpty());

    connect.kafka().produce(testId, key, deleteValue);
    assertDlqIsEmpty(dlqTopic);
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testNonexistentCellDeletionWorks()
      throws InterruptedException, ExecutionException, TimeoutException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.DELETE.name());
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.UPSERT.name());
    configureDlq(props, dlqTopic);
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));
    String testId = startSingleTopicConnector(props);

    String key = "nonexistentKey";
    Struct innerStruct =
        new Struct(SchemaBuilder.struct().field("b", Schema.OPTIONAL_INT8_SCHEMA)).put("b", null);
    Struct struct =
        new Struct(SchemaBuilder.struct().field("a", innerStruct.schema())).put("a", innerStruct);
    byte[] valueBytes =
        JsonConverterFactory.create(true, false).fromConnectData(testId, struct.schema(), struct);
    String value = new String(valueBytes, StandardCharsets.UTF_8);
    connect.kafka().produce(testId, key, value);

    assertDlqIsEmpty(dlqTopic);
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testNonexistentColumnFamilyDeletionWorks()
      throws ExecutionException, InterruptedException, TimeoutException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.DELETE.name());
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.UPSERT.name());
    configureDlq(props, dlqTopic);
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));
    String testId = startSingleTopicConnector(props);

    String key = "nonexistentKey";
    Struct struct =
        new Struct(SchemaBuilder.struct().field("b", Schema.OPTIONAL_INT8_SCHEMA)).put("b", null);
    byte[] valueBytes =
        JsonConverterFactory.create(true, false).fromConnectData(testId, struct.schema(), struct);
    String value = new String(valueBytes, StandardCharsets.UTF_8);
    connect.kafka().produce(testId, key, value);

    assertDlqIsEmpty(dlqTopic);
    assertConnectorAndAllTasksAreRunning(testId);
  }
}
