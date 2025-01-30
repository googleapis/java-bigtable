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

/*
 * This software contains code derived from the BigQuery Connector for Apache Kafka,
 * Copyright Aiven Oy, which in turn contains code derived from the Confluent BigQuery
 * Kafka Connector, Copyright Confluent, Inc, which in turn contains code derived from
 * the WePay BigQuery Kafka Connector, Copyright WePay, Inc.
 */

import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import io.confluent.connect.json.JsonSchemaConverter;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.apache.kafka.connect.data.Date;
import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Time;
import org.apache.kafka.connect.data.Timestamp;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.storage.Converter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DifferentConvertersIT extends BaseKafkaConnectBigtableSchemaRegistryIT {
  private static final Schema SUB_STRUCT_SCHEMA =
      SchemaBuilder.struct()
          .field("ssf1", Schema.INT64_SCHEMA)
          .field("ssf2", Schema.BOOLEAN_SCHEMA)
          .build();
  private static final Schema NESTED_STRUCT_SCHEMA =
      SchemaBuilder.struct()
          .field("sf1", Schema.STRING_SCHEMA)
          .field("sf2", SUB_STRUCT_SCHEMA)
          .field("sf3", Schema.FLOAT64_SCHEMA)
          .build();
  private static final Schema PRIMITIVES_SCHEMA =
      SchemaBuilder.struct()
          .field("boolean_field", Schema.BOOLEAN_SCHEMA)
          .field("float32_field", Schema.FLOAT32_SCHEMA)
          .field("float64_field", Schema.FLOAT64_SCHEMA)
          .field("int8_field", Schema.INT8_SCHEMA)
          .field("int16_field", Schema.INT16_SCHEMA)
          .field("int32_field", Schema.INT32_SCHEMA)
          .field("int64_field", Schema.INT64_SCHEMA)
          .field("string_field", Schema.STRING_SCHEMA);
  private static final Schema LOGICALS_SCHEMA =
      SchemaBuilder.struct()
          // klf = "Kafka logical field"
          .field("klf1", Timestamp.builder().optional().build())
          .field("klf2", Time.builder().optional().build())
          .field("klf3", Date.builder().optional().build())
          .field("klf4", Decimal.builder(5).optional().build())
          .build();
  private static final Schema ARRAY_SCHEMA = SchemaBuilder.array(Schema.STRING_SCHEMA);
  private static final Schema KEY_SCHEMA = SchemaBuilder.INT64_SCHEMA;
  private static final Schema VALUE_SCHEMA =
      SchemaBuilder.struct()
          .optional()
          .field("f1", Schema.STRING_SCHEMA)
          .field("f2", Schema.BOOLEAN_SCHEMA)
          .field("f3", Schema.FLOAT64_SCHEMA)
          .field("bytes_field", Schema.OPTIONAL_BYTES_SCHEMA)
          .field("nested_field", NESTED_STRUCT_SCHEMA)
          .field("primitives_field", PRIMITIVES_SCHEMA)
          .field("logicals_field", LOGICALS_SCHEMA)
          .field("array_field", ARRAY_SCHEMA)
          .build();
  private static final Long NUM_RECORDS = 100L;

  @Test
  public void testAvro() throws InterruptedException {
    testConverter(
        JsonSchemaConverter::new,
        Map.of(
            AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
            schemaRegistry.schemaRegistryUrl()));
  }

  @Test
  public void testProtobuf() throws InterruptedException {
    testConverter(
        JsonSchemaConverter::new,
        Map.of(
            AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
            schemaRegistry.schemaRegistryUrl()));
  }

  @Test
  public void testSchemaJson() throws InterruptedException {
    testConverter(
        JsonSchemaConverter::new,
        Map.of(
            AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
            schemaRegistry.schemaRegistryUrl()));
  }

  @Test
  public void testJsonWithInBandSchema() throws InterruptedException {
    testConverter(
        JsonConverter::new,
        Map.of(JsonConverterConfig.SCHEMAS_ENABLE_CONFIG, String.valueOf(true)));
  }

  @Test
  public void testJsonWithoutSchema() throws InterruptedException {
    testConverter(
        JsonConverter::new,
        Map.of(JsonConverterConfig.SCHEMAS_ENABLE_CONFIG, String.valueOf(false)));
  }

  private void testConverter(
      Supplier<Converter> converterConstructor, Map<String, String> converterProps)
      throws InterruptedException {
    Converter keyConverter = converterConstructor.get();
    keyConverter.configure(converterProps, true);
    Converter valueConverter = converterConstructor.get();
    valueConverter.configure(converterProps, false);

    Map<String, String> connectorProps = connectorProps();
    for (Map.Entry<String, String> prop : converterProps.entrySet()) {
      connectorProps.put(
          ConnectorConfig.KEY_CONVERTER_CLASS_CONFIG + "." + prop.getKey(), prop.getValue());
      connectorProps.put(
          ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG + "." + prop.getKey(), prop.getValue());
    }
    connectorProps.put(
        ConnectorConfig.KEY_CONVERTER_CLASS_CONFIG, keyConverter.getClass().getName());
    connectorProps.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, valueConverter.getClass().getName());
    String topic = startSingleTopicConnector(connectorProps);
    connect
        .assertions()
        .assertConnectorAndAtLeastNumTasksAreRunning(topic, numTasks, "Connector start timeout");
    populateKafkaTopic(topic, NUM_RECORDS, keyConverter, valueConverter);

    waitUntilBigtableContainsNumberOfRows(topic, NUM_RECORDS);
  }

  protected Map<String, String> connectorProps() {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES, "true");
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES, "true");
    return props;
  }

  private void populateKafkaTopic(
      String topic, long numRecords, Converter keyConverter, Converter valueConverter) {
    List<Map.Entry<SchemaAndValue, SchemaAndValue>> records = new ArrayList<>();
    for (long i = 0; i < numRecords; i++) {
      Object key = i;
      Object value = getValue(i);
      records.add(
          new AbstractMap.SimpleImmutableEntry<>(
              new SchemaAndValue(KEY_SCHEMA, key), new SchemaAndValue(VALUE_SCHEMA, value)));
    }
    sendRecords(topic, records, keyConverter, valueConverter);
  }

  private Struct getValue(long iteration) {
    Struct primitivesStruct = new Struct(VALUE_SCHEMA.field("primitives_field").schema());
    primitivesStruct.put("boolean_field", iteration % 3 == 1);
    primitivesStruct.put("float32_field", iteration * 1.5f);
    primitivesStruct.put("float64_field", iteration * 0.5);
    primitivesStruct.put("int8_field", (byte) (iteration % 10));
    primitivesStruct.put("int16_field", (short) (iteration % 30 + 1));
    primitivesStruct.put("int32_field", (int) (-1 * (iteration % 100)));
    primitivesStruct.put("int64_field", iteration * 10);
    primitivesStruct.put("string_field", Long.toString(iteration * 123));

    Struct logicalsStruct = new Struct(VALUE_SCHEMA.field("logicals_field").schema());
    long timestampMs = 1707835187396L;
    int msPerDay = 86400000;
    int time = (int) (timestampMs % msPerDay);
    int date = (int) (timestampMs / msPerDay);
    Schema klf1Schema = logicalsStruct.schema().field("klf1").schema();
    java.util.Date klf1Value =
        org.apache.kafka.connect.data.Timestamp.toLogical(klf1Schema, timestampMs);
    Schema klf2Schema = logicalsStruct.schema().field("klf2").schema();
    java.util.Date klf2Value = org.apache.kafka.connect.data.Time.toLogical(klf2Schema, time);
    Schema klf3Schema = logicalsStruct.schema().field("klf3").schema();
    java.util.Date klf3Value = org.apache.kafka.connect.data.Date.toLogical(klf3Schema, date);
    logicalsStruct
        .put("klf1", klf1Value)
        .put("klf2", klf2Value)
        .put("klf3", klf3Value)
        .put("klf4", BigDecimal.valueOf(6543).setScale(5));

    Struct subStruct =
        new Struct(VALUE_SCHEMA.field("nested_field").schema().field("sf2").schema());
    subStruct.put("ssf1", iteration / 2);
    subStruct.put("ssf2", false);

    Struct nestedStruct = new Struct(VALUE_SCHEMA.field("nested_field").schema());
    nestedStruct.put("sf1", "sv1");
    nestedStruct.put("sf2", subStruct);
    nestedStruct.put("sf3", iteration * 1.0);

    List<String> arrayValue =
        LongStream.of(iteration % 10)
            .mapToObj(l -> "array element " + l)
            .collect(Collectors.toList());

    byte[] bytesValue = new byte[(int) iteration % 4];
    for (int i = 0; i < bytesValue.length; i++) bytesValue[i] = (byte) i;

    return new Struct(VALUE_SCHEMA)
        .put("f1", "api" + iteration)
        .put("f2", iteration % 2 == 0)
        .put("f3", iteration * 0.01)
        .put("bytes_field", bytesValue)
        .put("nested_field", nestedStruct)
        .put("primitives_field", primitivesStruct)
        .put("logicals_field", logicalsStruct)
        .put("array_field", arrayValue);
  }
}
