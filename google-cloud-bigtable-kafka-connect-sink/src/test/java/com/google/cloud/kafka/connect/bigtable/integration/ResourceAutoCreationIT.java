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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.cloud.bigtable.admin.v2.models.ColumnFamily;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.admin.v2.models.ModifyColumnFamiliesRequest;
import com.google.cloud.bigtable.data.v2.models.Range;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkTaskConfig;
import com.google.cloud.kafka.connect.bigtable.config.InsertMode;
import com.google.cloud.kafka.connect.bigtable.config.NullValueMode;
import com.google.cloud.kafka.connect.bigtable.exception.InvalidBigtableSchemaModificationException;
import com.google.cloud.kafka.connect.bigtable.util.JsonConverterFactory;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ResourceAutoCreationIT extends BaseKafkaConnectBigtableIT {
  private static final JsonConverter JSON_CONVERTER = JsonConverterFactory.create(true, false);
  private static final String KEY1 = "key1";
  private static final String KEY2 = "key2";
  private static final String KEY3 = "key3";
  private static final String KEY4 = "key4";
  private static final String COLUMN_FAMILY1 = "cf1";
  private static final String COLUMN_FAMILY2 = "cf2";
  private static final String COLUMN_QUALIFIER = "cq";

  @Test
  public void testDisabledResourceAutoCreation() throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    configureDlq(props, dlqTopic);
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));

    String testId = startSingleTopicConnector(props);

    Integer value = 1;
    Struct columnValue =
        new Struct(SchemaBuilder.struct().field(COLUMN_QUALIFIER, Schema.INT32_SCHEMA))
            .put(COLUMN_QUALIFIER, value);

    Struct value1 =
        new Struct(SchemaBuilder.struct().field(COLUMN_FAMILY1, columnValue.schema()))
            .put(COLUMN_FAMILY1, columnValue);
    Struct value2 =
        new Struct(SchemaBuilder.struct().field(COLUMN_FAMILY2, columnValue.schema()))
            .put(COLUMN_FAMILY2, columnValue);

    String serializedValue1 = jsonify(testId, value1.schema(), value1);
    String serializedValue2 = jsonify(testId, value2.schema(), value2);

    // With the table missing.
    connect.kafka().produce(testId, KEY1, serializedValue1);
    assertSingleDlqEntry(dlqTopic, KEY1, null, null);
    assertConnectorAndAllTasksAreRunning(testId);

    // With the table and column family created.
    bigtableAdmin.createTable(CreateTableRequest.of(testId).addFamily(COLUMN_FAMILY1));
    connect.kafka().produce(testId, KEY2, serializedValue1);
    waitUntilBigtableContainsNumberOfRows(testId, 1);
    assertTrue(
        readAllRows(bigtableData, testId)
            .containsKey(ByteString.copyFrom(KEY2.getBytes(StandardCharsets.UTF_8))));
    assertConnectorAndAllTasksAreRunning(testId);

    // With the column family missing.
    connect.kafka().produce(testId, KEY3, serializedValue2);
    ConsumerRecords<byte[], byte[]> dlqRecords =
        connect.kafka().consume(2, Duration.ofSeconds(120).toMillis(), dlqTopic);
    assertEquals(2, dlqRecords.count());
    assertTrue(
        StreamSupport.stream(dlqRecords.spliterator(), false)
            .anyMatch(r -> Arrays.equals(KEY3.getBytes(StandardCharsets.UTF_8), r.key())));
    assertConnectorAndAllTasksAreRunning(testId);
    // With the column family created.
    bigtableAdmin.modifyFamilies(ModifyColumnFamiliesRequest.of(testId).addFamily(COLUMN_FAMILY2));
    connect.kafka().produce(testId, KEY4, serializedValue2);
    waitUntilBigtableContainsNumberOfRows(testId, 2);
    assertTrue(
        readAllRows(bigtableData, testId)
            .containsKey(ByteString.copyFrom(KEY4.getBytes(StandardCharsets.UTF_8))));
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testTableAutoCreationEnabledColumnFamilyAutoCreationDisabled()
      throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    configureDlq(props, dlqTopic);
    props.put(BigtableSinkTaskConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));

    String testId = startSingleTopicConnector(props);

    String value = jsonify(testId, Schema.INT64_SCHEMA, 1234L);

    assertThrows(Throwable.class, () -> bigtableAdmin.getTable(testId));
    connect.kafka().produce(testId, KEY1, value);
    waitUntilBigtableTableExists(testId);

    assertSingleDlqEntry(dlqTopic, KEY1, value, null);
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testTableAutoCreationDisabledColumnFamilyAutoCreationEnabled()
      throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    configureDlq(props, dlqTopic);
    props.put(BigtableSinkTaskConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));

    String testId = startSingleTopicConnector(props);
    assertThrows(Throwable.class, () -> bigtableAdmin.getTable(testId));
    String value = jsonify(testId, Schema.INT64_SCHEMA, 1234L);
    connect.kafka().produce(testId, KEY1, value);
    assertSingleDlqEntry(dlqTopic, KEY1, value, null);

    bigtableAdmin.createTable(CreateTableRequest.of(testId));
    assertTrue(bigtableAdmin.getTable(testId).getColumnFamilies().isEmpty());
    connect.kafka().produce(testId, KEY2, value);
    waitUntilBigtableContainsNumberOfRows(testId, 1);
    assertFalse(bigtableAdmin.getTable(testId).getColumnFamilies().isEmpty());

    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testAutoTableAndColumnFamilyAutoCreationWhenReadingMultipleTopics()
      throws InterruptedException, ExecutionException, TimeoutException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    configureDlq(props, dlqTopic);
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));

    Set<String> topicSuffixes = Set.of("topic1", "topic2", "topic3", "topic4");
    String testId = startMultipleTopicConnector(props, topicSuffixes);
    Set<String> topics = topicSuffixes.stream().map(s -> testId + s).collect(Collectors.toSet());

    for (String topic : topics) {
      assertThrows(Throwable.class, () -> bigtableAdmin.getTable(topic));
      connect.kafka().produce(topic, topic, "value");
      waitUntilBigtableContainsNumberOfRows(topic, 1);
      assertEquals(
          Set.of(topic),
          bigtableAdmin.getTable(topic).getColumnFamilies().stream()
              .map(ColumnFamily::getId)
              .collect(Collectors.toSet()));
    }
    assertDlqIsEmpty(dlqTopic);

    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testCreationOfInvalidTable() throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    String invalidTableName = "T".repeat(MAX_BIGTABLE_TABLE_NAME_LENGTH + 1);
    props.put(BigtableSinkConfig.TABLE_NAME_FORMAT_CONFIG, invalidTableName);
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.RETRY_TIMEOUT_MILLIS_CONFIG, "10000");

    configureDlq(props, dlqTopic);
    String testId = startSingleTopicConnector(props);

    String value = "value";

    connect.kafka().produce(testId, KEY1, value);

    assertSingleDlqEntry(dlqTopic, KEY1, value, InvalidBigtableSchemaModificationException.class);
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testRowDeletionCreatesTableWhenAutoCreationEnabled() throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    configureDlq(props, dlqTopic);
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.DELETE.name());
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.UPSERT.name());
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));
    String testId = startSingleTopicConnector(props);

    String rowDeletionValue = jsonify(testId, Schema.OPTIONAL_BYTES_SCHEMA, null);
    assertThrows(Throwable.class, () -> bigtableAdmin.getTable(testId));
    connect.kafka().produce(testId, KEY1, rowDeletionValue);
    waitUntilBigtableTableExists(testId);
    assertTrue(bigtableAdmin.getTable(testId).getColumnFamilies().isEmpty());

    assertSingleDlqEntry(dlqTopic, KEY1, null, null);
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testColumnFamilyDeletionCreatesTableAndColumnFamilyWhenAutoCreationEnabled()
      throws InterruptedException, ExecutionException, TimeoutException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    configureDlq(props, dlqTopic);
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.DELETE.name());
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.UPSERT.name());
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));
    String testId = startSingleTopicConnector(props);

    Struct deleteColumnFamily =
        new Struct(SchemaBuilder.struct().field(COLUMN_FAMILY1, Schema.OPTIONAL_BYTES_SCHEMA))
            .put(COLUMN_FAMILY1, null);
    assertThrows(Throwable.class, () -> bigtableAdmin.getTable(testId));
    connect
        .kafka()
        .produce(testId, KEY1, jsonify(testId, deleteColumnFamily.schema(), deleteColumnFamily));
    waitUntilBigtableTableHasColumnFamily(testId, COLUMN_FAMILY1);
    assertEquals(
        Set.of(COLUMN_FAMILY1),
        bigtableAdmin.getTable(testId).getColumnFamilies().stream()
            .map(ColumnFamily::getId)
            .collect(Collectors.toSet()));

    assertDlqIsEmpty(dlqTopic);
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testColumnDeletionCreatesTableAndColumnFamilyWhenAutoCreationEnabled()
      throws InterruptedException, ExecutionException, TimeoutException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    configureDlq(props, dlqTopic);
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, String.valueOf(true));
    props.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.DELETE.name());
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.UPSERT.name());
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));
    String testId = startSingleTopicConnector(props);

    Struct innerStruct =
        new Struct(SchemaBuilder.struct().field(COLUMN_QUALIFIER, Schema.OPTIONAL_BYTES_SCHEMA))
            .put(COLUMN_QUALIFIER, null);
    Struct deleteColumn =
        new Struct(SchemaBuilder.struct().field(COLUMN_FAMILY2, innerStruct.schema()))
            .put(COLUMN_FAMILY2, innerStruct);
    assertThrows(Throwable.class, () -> bigtableAdmin.getTable(testId));
    connect.kafka().produce(testId, KEY3, jsonify(testId, deleteColumn.schema(), deleteColumn));
    waitUntilBigtableTableHasColumnFamily(testId, COLUMN_FAMILY2);
    assertEquals(
        Set.of(COLUMN_FAMILY2),
        bigtableAdmin.getTable(testId).getColumnFamilies().stream()
            .map(ColumnFamily::getId)
            .collect(Collectors.toSet()));

    assertDlqIsEmpty(dlqTopic);
    assertConnectorAndAllTasksAreRunning(testId);
  }

  /**
   * This test checks consequences of design choices described in comments in {@link
   * com.google.cloud.kafka.connect.bigtable.mapping.MutationDataBuilder#deleteCells(String,
   * ByteString, Range.TimestampRange)} and {@link
   * com.google.cloud.kafka.connect.bigtable.mapping.MutationDataBuilder#deleteFamily(String)}.
   */
  @Test
  public void testDeletionFailsWhenAutoCreationDisabled() throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    configureDlq(props, dlqTopic);
    props.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.DELETE.name());
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.UPSERT.name());
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));

    String deleteRowSuffix = "deleteRow";
    String deleteColumnFamilySuffix = "deleteColumnFamily";
    String deleteColumnSuffix = "deleteColumn";
    String testId =
        startMultipleTopicConnector(
            props, Set.of(deleteRowSuffix, deleteColumnFamilySuffix, deleteColumnSuffix));

    String deleteRowTopic = testId + deleteRowSuffix;
    String rowDeletionValue = jsonify(deleteRowTopic, Schema.OPTIONAL_BYTES_SCHEMA, null);
    assertThrows(Throwable.class, () -> bigtableAdmin.getTable(deleteRowTopic));
    connect.kafka().produce(deleteRowTopic, KEY1, rowDeletionValue);

    String deleteColumnFamilyTopic = testId + deleteColumnFamilySuffix;
    Struct deleteColumnFamily =
        new Struct(SchemaBuilder.struct().field(COLUMN_FAMILY1, Schema.OPTIONAL_BYTES_SCHEMA))
            .put(COLUMN_FAMILY1, null);
    assertThrows(Throwable.class, () -> bigtableAdmin.getTable(deleteColumnFamilyTopic));
    connect
        .kafka()
        .produce(
            deleteColumnFamilyTopic,
            KEY2,
            jsonify(deleteColumnFamilyTopic, deleteColumnFamily.schema(), deleteColumnFamily));

    String deleteColumnTopic = testId + deleteColumnSuffix;
    Struct deleteColumn =
        new Struct(SchemaBuilder.struct().field(COLUMN_FAMILY2, deleteColumnFamily.schema()))
            .put(COLUMN_FAMILY2, deleteColumnFamily);
    assertThrows(Throwable.class, () -> bigtableAdmin.getTable(deleteColumnTopic));
    connect
        .kafka()
        .produce(
            deleteColumnTopic,
            KEY3,
            jsonify(deleteColumnTopic, deleteColumn.schema(), deleteColumn));

    ConsumerRecords<byte[], byte[]> dlqRecords =
        connect.kafka().consume(3, Duration.ofSeconds(120).toMillis(), dlqTopic);

    Set<String> dlqKeys =
        StreamSupport.stream(dlqRecords.spliterator(), false)
            .map(r -> new String(r.key(), StandardCharsets.UTF_8))
            .collect(Collectors.toSet());
    assertEquals(Set.of(KEY1, KEY2, KEY3), dlqKeys);
  }

  private static String jsonify(String topic, Schema schema, Object value) {
    byte[] bytes = JSON_CONVERTER.fromConnectData(topic, schema, value);
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
