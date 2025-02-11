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

import static com.google.cloud.kafka.connect.bigtable.util.NestedNullStructFactory.NESTED_NULL_STRUCT_FIELD_NAME;
import static com.google.cloud.kafka.connect.bigtable.util.NestedNullStructFactory.NESTED_NULL_STRUCT_FIELD_NAME_BYTES;
import static com.google.cloud.kafka.connect.bigtable.util.NestedNullStructFactory.getStructWithNullOnNthNestingLevel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.config.InsertMode;
import com.google.cloud.kafka.connect.bigtable.config.NullValueMode;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
public class NullHandlingIT extends BaseKafkaConnectBigtableIT {
  private static final Map<String, String> JSON_CONVERTER_PROPS =
      Map.of(JsonConverterConfig.SCHEMAS_ENABLE_CONFIG, String.valueOf(true));
  private static final StringConverter KEY_CONVERTER = new StringConverter();
  private static final JsonConverter VALUE_CONVERTER = new JsonConverter();

  private static final String KEY1 = "key1";
  private static final String KEY2 = "key2";
  private static final String KEY3 = "key3";
  private static final String KEY4 = "key4";
  private static final ByteString KEY1_BYTES =
      ByteString.copyFrom(KEY1.getBytes(StandardCharsets.UTF_8));
  private static final ByteString KEY2_BYTES =
      ByteString.copyFrom(KEY2.getBytes(StandardCharsets.UTF_8));
  private static final ByteString KEY3_BYTES =
      ByteString.copyFrom(KEY3.getBytes(StandardCharsets.UTF_8));
  private static final ByteString KEY4_BYTES =
      ByteString.copyFrom(KEY4.getBytes(StandardCharsets.UTF_8));

  static {
    VALUE_CONVERTER.configure(JSON_CONVERTER_PROPS, false);
  }

  @Test
  public void testIgnoreMode() throws InterruptedException {
    Map<String, String> connectorProps = connectorProps();
    connectorProps.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.IGNORE.name());
    String testId = startSingleTopicConnector(connectorProps);
    connect
        .assertions()
        .assertConnectorAndAtLeastNumTasksAreRunning(testId, numTasks, "Connector start timeout");

    String keyFinish = "key_finish";
    ByteString keyFinishBytes = ByteString.copyFrom(keyFinish.getBytes(StandardCharsets.UTF_8));

    List<Map.Entry<SchemaAndValue, SchemaAndValue>> records = new ArrayList<>();
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(Schema.STRING_SCHEMA, KEY1),
            new SchemaAndValue(SchemaBuilder.struct().optional().build(), null)));
    Struct nested1 = getStructWithNullOnNthNestingLevel(1);
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(Schema.STRING_SCHEMA, KEY2),
            new SchemaAndValue(nested1.schema(), nested1)));
    Struct nested2 = getStructWithNullOnNthNestingLevel(2);
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(Schema.STRING_SCHEMA, KEY3),
            new SchemaAndValue(nested2.schema(), nested2)));
    Struct nested3 = getStructWithNullOnNthNestingLevel(3);
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(Schema.STRING_SCHEMA, KEY4),
            new SchemaAndValue(nested3.schema(), nested3)));
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(Schema.STRING_SCHEMA, keyFinish),
            new SchemaAndValue(Schema.STRING_SCHEMA, "finish")));
    sendRecords(testId, records, KEY_CONVERTER, VALUE_CONVERTER);

    waitUntilBigtableContainsNumberOfRows(testId, 2);
    Map<ByteString, Row> rows = readAllRows(bigtableData, testId);
    assertEquals(rows.keySet(), Set.of(KEY4_BYTES, keyFinishBytes));

    assertEquals(1, rows.get(KEY4_BYTES).getCells().size());
    RowCell cell = rows.get(KEY4_BYTES).getCells().get(0);
    assertCellContents(
        cell,
        NESTED_NULL_STRUCT_FIELD_NAME,
        NESTED_NULL_STRUCT_FIELD_NAME_BYTES,
        jsonifiedStructWithNullField());
  }

  @Test
  public void testWriteMode() throws InterruptedException {
    String defaultColumnFamily = "family";
    String defaultColumnQualifier = "qualifier";
    ByteString defaultColumnQualifierBytes =
        ByteString.copyFrom(defaultColumnQualifier.getBytes(StandardCharsets.UTF_8));

    Map<String, String> connectorProps = connectorProps();
    connectorProps.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.WRITE.name());
    connectorProps.put(BigtableSinkConfig.DEFAULT_COLUMN_FAMILY_CONFIG, defaultColumnFamily);
    connectorProps.put(BigtableSinkConfig.DEFAULT_COLUMN_QUALIFIER_CONFIG, defaultColumnQualifier);
    String testId = startSingleTopicConnector(connectorProps);
    connect
        .assertions()
        .assertConnectorAndAtLeastNumTasksAreRunning(testId, numTasks, "Connector start timeout");

    List<Map.Entry<SchemaAndValue, SchemaAndValue>> records = new ArrayList<>();
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(Schema.STRING_SCHEMA, KEY1),
            new SchemaAndValue(SchemaBuilder.struct().optional().build(), null)));
    Struct nested1 = getStructWithNullOnNthNestingLevel(1);
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(Schema.STRING_SCHEMA, KEY2),
            new SchemaAndValue(nested1.schema(), nested1)));
    Struct nested2 = getStructWithNullOnNthNestingLevel(2);
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(Schema.STRING_SCHEMA, KEY3),
            new SchemaAndValue(nested2.schema(), nested2)));
    Struct nested3 = getStructWithNullOnNthNestingLevel(3);
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(Schema.STRING_SCHEMA, KEY4),
            new SchemaAndValue(nested3.schema(), nested3)));
    sendRecords(testId, records, KEY_CONVERTER, VALUE_CONVERTER);

    waitUntilBigtableContainsNumberOfRows(testId, 4);
    Map<ByteString, Row> rows = readAllRows(bigtableData, testId);
    assertEquals(rows.keySet(), Set.of(KEY1_BYTES, KEY2_BYTES, KEY3_BYTES, KEY4_BYTES));
    assertTrue(rows.values().stream().allMatch(vs -> vs.getCells().size() == 1));

    ByteString nullValueWriteMode = ByteString.copyFrom(new byte[0]);
    assertCellContents(
        rows.get(KEY1_BYTES).getCells().get(0),
        defaultColumnFamily,
        defaultColumnQualifierBytes,
        nullValueWriteMode);
    assertCellContents(
        rows.get(KEY2_BYTES).getCells().get(0),
        defaultColumnFamily,
        NESTED_NULL_STRUCT_FIELD_NAME_BYTES,
        nullValueWriteMode);
    assertCellContents(
        rows.get(KEY3_BYTES).getCells().get(0),
        NESTED_NULL_STRUCT_FIELD_NAME,
        NESTED_NULL_STRUCT_FIELD_NAME_BYTES,
        nullValueWriteMode);
    assertCellContents(
        rows.get(KEY4_BYTES).getCells().get(0),
        NESTED_NULL_STRUCT_FIELD_NAME,
        NESTED_NULL_STRUCT_FIELD_NAME_BYTES,
        jsonifiedStructWithNullField());
  }

  @Test
  public void testDeleteMode() throws InterruptedException {
    Map<String, String> connectorProps = connectorProps();
    connectorProps.put(BigtableSinkConfig.VALUE_NULL_MODE_CONFIG, NullValueMode.DELETE.name());
    connectorProps.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.UPSERT.name());
    String testId = startSingleTopicConnector(connectorProps);
    connect
        .assertions()
        .assertConnectorAndAtLeastNumTasksAreRunning(testId, numTasks, "Connector start timeout");

    String columnFamily1 = "cf1";
    String columnFamily2 = "cf2";
    bigtableAdmin.createTable(
        CreateTableRequest.of(testId).addFamily(columnFamily1).addFamily(columnFamily2));
    String columnQualifier1 = "cq1";
    String columnQualifier2 = "cq2";
    ByteString columnQualifierBytes1 =
        ByteString.copyFrom(columnQualifier1.getBytes(StandardCharsets.UTF_8));
    ByteString columnQualifierBytes2 =
        ByteString.copyFrom(columnQualifier2.getBytes(StandardCharsets.UTF_8));

    ByteString value = ByteString.copyFromUtf8("value");
    for (String key : List.of(KEY1, KEY2, KEY3, KEY4)) {
      RowMutation mutation = RowMutation.create(testId, key);
      for (String family : List.of(columnFamily1, columnFamily2)) {
        for (ByteString qualifier : List.of(columnQualifierBytes1, columnQualifierBytes2)) {
          mutation.setCell(family, qualifier, value);
        }
      }
      bigtableData.mutateRow(mutation);
    }
    Map<ByteString, Row> rowsBefore = readAllRows(bigtableData, testId);
    assertEquals(Set.of(KEY1_BYTES, KEY2_BYTES, KEY3_BYTES, KEY4_BYTES), rowsBefore.keySet());

    ByteString keyAddedJsonification = KEY1_BYTES;
    Struct nestedNullToBeJsonified = getStructWithNullOnNthNestingLevel(3);

    ByteString keyDeletedColumn = KEY2_BYTES;
    Map.Entry<String, String> deletedColumn =
        new AbstractMap.SimpleImmutableEntry<>(columnFamily2, columnQualifier2);
    Schema innerDeleteColumnSchema =
        SchemaBuilder.struct().field(deletedColumn.getValue(), Schema.OPTIONAL_INT8_SCHEMA);
    Struct deleteColumn =
        new Struct(
                SchemaBuilder.struct()
                    .field(deletedColumn.getKey(), innerDeleteColumnSchema)
                    .build())
            .put(
                deletedColumn.getKey(),
                new Struct(innerDeleteColumnSchema).put(deletedColumn.getValue(), null));

    ByteString keyDeletedColumnFamily = KEY3_BYTES;
    String deletedColumnFamily = columnFamily1;
    Struct deleteColumnFamily =
        new Struct(SchemaBuilder.struct().field(deletedColumnFamily, Schema.OPTIONAL_INT8_SCHEMA))
            .put(deletedColumnFamily, null);

    ByteString keyDeletedRow = KEY4_BYTES;
    Schema deleteRowSchema = SchemaBuilder.struct().optional().build();

    List<Map.Entry<SchemaAndValue, SchemaAndValue>> records = new ArrayList<>();
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(
                Schema.STRING_SCHEMA,
                new String(keyAddedJsonification.toByteArray(), StandardCharsets.UTF_8)),
            new SchemaAndValue(nestedNullToBeJsonified.schema(), nestedNullToBeJsonified)));
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(
                Schema.STRING_SCHEMA,
                new String(keyDeletedColumn.toByteArray(), StandardCharsets.UTF_8)),
            new SchemaAndValue(deleteColumn.schema(), deleteColumn)));
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(
                Schema.STRING_SCHEMA,
                new String(keyDeletedColumnFamily.toByteArray(), StandardCharsets.UTF_8)),
            new SchemaAndValue(deleteColumnFamily.schema(), deleteColumnFamily)));
    records.add(
        new AbstractMap.SimpleImmutableEntry<>(
            new SchemaAndValue(
                Schema.STRING_SCHEMA,
                new String(keyDeletedRow.toByteArray(), StandardCharsets.UTF_8)),
            new SchemaAndValue(deleteRowSchema, null)));

    sendRecords(testId, records, KEY_CONVERTER, VALUE_CONVERTER);
    waitUntilBigtableContainsNumberOfRows(testId, 3);
    Map<ByteString, Row> rowsAfter = readAllRows(bigtableData, testId);
    assertEquals(
        Set.of(keyAddedJsonification, keyDeletedColumn, keyDeletedColumnFamily),
        rowsAfter.keySet());

    Row rowAddedJsonification = rowsAfter.get(keyAddedJsonification);
    assertEquals(
        1,
        rowAddedJsonification
            .getCells(NESTED_NULL_STRUCT_FIELD_NAME, NESTED_NULL_STRUCT_FIELD_NAME_BYTES)
            .size());
    assertEquals(
        jsonifiedStructWithNullField(),
        rowAddedJsonification
            .getCells(NESTED_NULL_STRUCT_FIELD_NAME, NESTED_NULL_STRUCT_FIELD_NAME_BYTES)
            .get(0)
            .getValue());
    assertEquals(
        new HashSet<>(rowsBefore.get(keyAddedJsonification).getCells()),
        rowAddedJsonification.getCells().stream()
            .filter(c -> !c.getFamily().equals(NESTED_NULL_STRUCT_FIELD_NAME))
            .collect(Collectors.toSet()));

    Row rowDeletedColumnFamily = rowsAfter.get(keyDeletedColumnFamily);
    assertEquals(0, rowDeletedColumnFamily.getCells(deletedColumnFamily).size());
    assertEquals(
        rowsBefore.get(keyDeletedColumnFamily).getCells().stream()
            .filter(c -> !c.getFamily().equals(deletedColumnFamily))
            .collect(Collectors.toSet()),
        new HashSet<>(rowDeletedColumnFamily.getCells()));

    Row rowDeletedColumn = rowsAfter.get(keyDeletedColumn);
    assertEquals(
        0, rowDeletedColumn.getCells(deletedColumn.getKey(), deletedColumn.getValue()).size());
    assertEquals(
        rowsBefore.get(keyDeletedColumn).getCells().stream()
            .filter(
                c ->
                    !(c.getFamily().equals(deletedColumn.getKey())
                        && c.getQualifier()
                            .equals(
                                ByteString.copyFrom(
                                    deletedColumn.getValue().getBytes(StandardCharsets.UTF_8)))))
            .collect(Collectors.toSet()),
        new HashSet<>(rowDeletedColumn.getCells()));
  }

  private Map<String, String> connectorProps() {
    Map<String, String> props = super.baseConnectorProps();
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, "true");
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, "true");
    // We use JsonConverter since it doesn't care about schemas, so we may use differently-shaped
    // data within a single test.
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    for (Map.Entry<String, String> prop : JSON_CONVERTER_PROPS.entrySet()) {
      props.put(
          ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG + "." + prop.getKey(), prop.getValue());
    }
    return props;
  }

  private ByteString jsonifiedStructWithNullField() {
    String expectedJson = String.format("{\"%s\":null}", NESTED_NULL_STRUCT_FIELD_NAME);
    byte[] expectedJsonBytes = expectedJson.getBytes(StandardCharsets.UTF_8);
    return ByteString.copyFrom(expectedJsonBytes);
  }

  private void assertCellContents(
      RowCell cell, String family, ByteString qualifier, ByteString value) {
    assertEquals(family, cell.getFamily());
    assertEquals(qualifier, cell.getQualifier());
    assertEquals(value, cell.getValue());
  }
}
