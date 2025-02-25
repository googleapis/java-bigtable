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

import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import io.confluent.connect.avro.AvroConverter;
import io.confluent.connect.json.JsonSchemaConverter;
import io.confluent.connect.protobuf.ProtobufConverter;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.storage.Converter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class DifferentConvertersIT extends BaseDataGeneratorIT {
  private Supplier<Converter> converterConstructor;
  private Map<String, String> converterBaseConfig;
  private boolean converterUsesSchemaRegistry;

  @Parameterized.Parameters
  public static Collection testCases() {
    return Arrays.asList(
        new Object[][] {
          {(Supplier<Converter>) AvroConverter::new, Map.of(), true},
          {(Supplier<Converter>) ProtobufConverter::new, Map.of(), true},
          {(Supplier<Converter>) JsonSchemaConverter::new, Map.of(), true},
          {
            (Supplier<Converter>) JsonConverter::new,
            Map.of(JsonConverterConfig.SCHEMAS_ENABLE_CONFIG, String.valueOf(false)),
            false
          },
          {
            (Supplier<Converter>) JsonConverter::new,
            Map.of(JsonConverterConfig.SCHEMAS_ENABLE_CONFIG, String.valueOf(true)),
            false
          },
        });
  }

  public DifferentConvertersIT(
      Supplier<Converter> converterConstructor,
      Map<String, String> converterBaseConfig,
      boolean converterUsesSchemaRegistry) {
    this.converterConstructor = converterConstructor;
    this.converterBaseConfig = converterBaseConfig;
    this.converterUsesSchemaRegistry = converterUsesSchemaRegistry;
  }

  @Test
  public void testConverter() throws InterruptedException {
    Map<String, String> converterProps = new HashMap<>(converterBaseConfig);
    if (converterUsesSchemaRegistry) {
      converterProps.put(
          AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
          schemaRegistry.schemaRegistryUrl());
    }
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
        .assertConnectorAndExactlyNumTasksAreRunning(topic, numTasks, "Connector start timeout");
    populateKafkaTopic(topic, numRecords, keyConverter, valueConverter);

    waitUntilBigtableContainsNumberOfRows(topic, numRecords);
  }

  private Map<String, String> connectorProps() {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, "true");
    props.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, "true");
    return props;
  }
}
