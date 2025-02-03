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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.protobuf.ByteString;
import io.confluent.connect.avro.AvroConverter;
import io.confluent.kafka.formatter.AvroMessageReader;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import kafka.common.MessageReader;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ConfluentCompatibilityIT extends BaseKafkaConnectBigtableSchemaRegistryIT {
  public static String TEST_CASES_DIR = "compatibility_test_cases";
  // Needed since the default column family is the topic's name in the default configuration.
  public static String COMPATIBILITY_TEST_TOPIC = "confluent_compat_topic";

  private String testCase;
  private Compatibility compatibility;

  public ConfluentCompatibilityIT(String testCase, Compatibility compatibility) {
    this.testCase = testCase;
    this.compatibility = compatibility;
  }

  @Parameterized.Parameters
  public static Collection testCases() {
    return Arrays.asList(
        new Object[][] {
          {"key_containers", Compatibility.FULL},
          // We serialize `Date`-based logical types differently. For details, see the comments
          // in KeyMapper, especially the ones in serializeKeyElement().
          {"key_logicals", Compatibility.KEY_MISMATCH},
          {"key_matryoshkas", Compatibility.FULL},
          // We serialize `Date`-based logical types differently. For details, see the comments
          // in KeyMapper, especially the ones in serializeKeyElement().
          {"key_nestedlogicals", Compatibility.KEY_MISMATCH},
          {"key_primitives", Compatibility.FULL},
          {"value_containers", Compatibility.FULL},
          {"value_matryoshkas", Compatibility.FULL},
          {"value_nestedlogicals", Compatibility.FULL},
          {"value_nulls", Compatibility.FULL},
          {"value_primitives", Compatibility.FULL},
        });
  }

  @Test
  public void testCasesUsingSchemaRegistry()
      throws InterruptedException, URISyntaxException, IOException {
    String testId = startConnector();
    populateTopic(testId);
    Map<String, Map<Map.Entry<String, String>, ByteString>> expected = getExpectedOutput();
    waitUntilBigtableContainsNumberOfRows(testId, expected.size());
    Map<ByteString, Row> allRows = readAllRows(bigtableData, testId);
    switch (compatibility) {
      case FULL:
        assertRowsMatch(expected, allRows.values());
        break;
      case KEY_MISMATCH:
        assertEquals(expected.size(), allRows.size());
        assertTrue(allRows.values().stream().allMatch(r -> r.getCells().size() == 1));
        Set<ByteString> allValues =
            allRows.values().stream()
                .map(r -> r.getCells().get(0).getValue())
                .collect(Collectors.toSet());
        Set<ByteString> allExpectedValues =
            expected.values().stream()
                .flatMap(m -> m.values().stream())
                .collect(Collectors.toSet());
        assertEquals(allExpectedValues, allValues);
        break;
    }
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(testId, numTasks, "Some task failed.");
  }

  public String startConnector() throws InterruptedException {
    Map<String, String> connectorProps = baseConnectorProps();
    connectorProps.put(ConnectorConfig.KEY_CONVERTER_CLASS_CONFIG, AvroConverter.class.getName());
    connectorProps.put(
        ConnectorConfig.KEY_CONVERTER_CLASS_CONFIG
            + "."
            + AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
        schemaRegistry.schemaRegistryUrl());
    connectorProps.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, AvroConverter.class.getName());
    connectorProps.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
        schemaRegistry.schemaRegistryUrl());
    connectorProps.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES, "true");
    connectorProps.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES, "true");
    connectorProps.put(BigtableSinkConfig.CONFIG_DEFAULT_COLUMN_FAMILY, COMPATIBILITY_TEST_TOPIC);
    connectorProps.put(BigtableSinkConfig.CONFIG_ROW_KEY_DELIMITER, "#");
    String topic = startSingleTopicConnector(connectorProps);
    connect
        .assertions()
        .assertConnectorAndAtLeastNumTasksAreRunning(topic, numTasks, "Connector start timeout");
    return topic;
  }

  public void populateTopic(String topic) throws IOException {
    String keySchema = readStringResource(getTestCaseDir() + "/key-schema.json");
    String valueSchema = readStringResource(getTestCaseDir() + "/value-schema.json");
    Properties messageReaderProps = new Properties();
    messageReaderProps.put(
        AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
        schemaRegistry.schemaRegistryUrl());
    messageReaderProps.put("topic", topic);
    messageReaderProps.put("parse.key", "true");
    messageReaderProps.put("key.schema", keySchema);
    messageReaderProps.put("value.schema", valueSchema);
    InputStream dataStream = getClassLoader().getResourceAsStream(getTestCaseDir() + "/data.json");
    MessageReader messageReader = new AvroMessageReader();
    messageReader.init(dataStream, messageReaderProps);

    Producer<byte[], byte[]> kafkaProducer = getKafkaProducer();
    ProducerRecord<byte[], byte[]> message = messageReader.readMessage();
    while (message != null) {
      try {
        kafkaProducer.send(message).get(1, TimeUnit.SECONDS);
      } catch (InterruptedException | ExecutionException | TimeoutException e) {
        throw new RuntimeException(e);
      }
      message = messageReader.readMessage();
    }
  }

  private static String readStringResource(String resourceName) throws IOException {
    byte[] resourceBytes = getClassLoader().getResourceAsStream(resourceName).readAllBytes();
    return new String(resourceBytes, StandardCharsets.UTF_8);
  }

  private static ClassLoader getClassLoader() {
    return ConfluentCompatibilityIT.class.getClassLoader();
  }

  private String getTestCaseDir() {
    return String.format("%s/%s", TEST_CASES_DIR, testCase);
  }

  private Map<String, Map<Map.Entry<String, String>, ByteString>> getExpectedOutput()
      throws URISyntaxException, IOException {
    Map<String, Map<Map.Entry<String, String>, ByteString>> result = new HashMap<>();

    String expectedOutputDir = getTestCaseDir() + "/confluent_sink_output";
    List<Path> outputPaths =
        Files.find(
                Paths.get(getClassLoader().getResource(expectedOutputDir).toURI()),
                3,
                (path, attr) -> attr.isRegularFile())
            .collect(Collectors.toList());

    for (Path outputPath : outputPaths) {
      String[] outputPathParts = outputPath.toString().split("/");
      int outputPathPartsLength = outputPathParts.length;
      String row = outputPathParts[outputPathPartsLength - 3];
      String columnFamily = outputPathParts[outputPathPartsLength - 2];
      String columnQualifier = outputPathParts[outputPathPartsLength - 1];
      byte[] value = Files.readAllBytes(outputPath);

      result.putIfAbsent(row, new HashMap<>());
      Map.Entry<String, String> familyAndQualifier =
          new AbstractMap.SimpleImmutableEntry<>(columnFamily, columnQualifier);
      assertFalse(result.get(row).containsKey(familyAndQualifier));
      result.get(row).put(familyAndQualifier, ByteString.copyFrom(value));
    }

    return result;
  }

  private void assertRowsMatch(
      Map<String, Map<Map.Entry<String, String>, ByteString>> expected,
      Collection<Row> bigtableRows) {
    assertEquals(expected.size(), bigtableRows.size());
    for (Row row : bigtableRows) {
      String key = new String(row.getKey().toByteArray(), StandardCharsets.UTF_8);
      Map<Map.Entry<String, String>, ByteString> bigtableRow = new HashMap<>();
      for (RowCell cell : row.getCells()) {
        Map.Entry<String, String> familyAndQualifier =
            new AbstractMap.SimpleImmutableEntry<>(
                cell.getFamily(),
                new String(cell.getQualifier().toByteArray(), StandardCharsets.UTF_8));
        assertFalse(bigtableRow.containsKey(familyAndQualifier));
        bigtableRow.put(familyAndQualifier, cell.getValue());
      }
      assertEquals(expected.get(key), bigtableRow);
    }
  }

  public enum Compatibility {
    FULL,
    KEY_MISMATCH,
  }
}
