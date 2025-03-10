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

import static org.apache.kafka.connect.runtime.ConnectorConfig.CONNECTOR_CLASS_CONFIG;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.protobuf.ByteString;
import io.confluent.connect.avro.AvroConverter;
import io.confluent.kafka.formatter.AvroMessageReader;
import io.confluent.kafka.formatter.SchemaMessageReader;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ConfluentCompatibilityIT extends BaseKafkaConnectBigtableSchemaRegistryIT {
  public static String TEST_CASES_DIR = "compatibility_test_cases";

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
          // Confluent serializes bytes in keys incorrectly. For details, see the comments
          // in KeyMapper#serializeKeyElement().
          {"key_bytes", Compatibility.KEY_MISMATCH},
          {"key_containers", Compatibility.FULL},
          // We serialize `Date`-based logical types differently. For details, see the comments
          // in KeyMapper, especially the ones in serializeKeyElement().
          {"key_logicals", Compatibility.KEY_MISMATCH},
          {"key_matryoshkas", Compatibility.FULL},
          // We serialize `Date`-based logical types differently. For details, see the comments
          // in KeyMapper, especially the ones in serializeKeyElement().
          {"key_nestedlogicals", Compatibility.KEY_MISMATCH},
          {"key_primitives", Compatibility.FULL},
          {"key_root_primitives", Compatibility.FULL},
          {"key_union", Compatibility.FULL},
          // Confluent connector fails with an invalid class cast.
          {"value_bytes", Compatibility.CONFLUENT_BROKEN},
          {"value_containers", Compatibility.FULL},
          // Confluent connector fails with an invalid class cast.
          {"value_logicals", Compatibility.CONFLUENT_BROKEN},
          {"value_matryoshkas", Compatibility.FULL},
          {"value_nestedlogicals", Compatibility.FULL},
          {"value_nulls", Compatibility.FULL},
          {"value_primitives", Compatibility.FULL},
          {"value_root_primitives", Compatibility.FULL},
          {"value_union", Compatibility.FULL},
        });
  }

  @Test
  public void testCasesUsingSchemaRegistry()
      throws InterruptedException, IOException, ExecutionException {
    String confluentTestId = startConfluentConnector();
    String googleTestId = startThisConnector(confluentTestId);
    assertNotEquals(confluentTestId, googleTestId);
    createTablesAndColumnFamilies(
        Map.of(
            confluentTestId, getNeededColumnFamilies(confluentTestId),
            googleTestId, getNeededColumnFamilies(confluentTestId)));

    populateTopic(confluentTestId);
    populateTopic(googleTestId);

    long expectedRows = getInputSize();
    waitUntilBigtableContainsNumberOfRows(googleTestId, expectedRows);
    Map<ByteString, Row> allGoogleRows = readAllRows(bigtableData, googleTestId);
    assertEquals(expectedRows, allGoogleRows.size());

    Map<ByteString, Row> allConfluentRows = null;
    switch (compatibility) {
      case FULL:
      case KEY_MISMATCH:
        // Done like this because Confluent sink seems to write rows cell-by-cell rather than
        // atomically.
        waitUntilBigtableContainsNumberOfCells(confluentTestId, cellCount(allGoogleRows));
        allConfluentRows = readAllRows(bigtableData, confluentTestId);
        assertEquals(expectedRows, allConfluentRows.size());
        break;
      case CONFLUENT_BROKEN:
        break;
    }
    switch (compatibility) {
      case FULL:
        assertRowsAreTheSame(allConfluentRows, allGoogleRows);
        break;
      case KEY_MISMATCH:
        assertEquals(rowToValues(allConfluentRows.values()), rowToValues(allGoogleRows.values()));
        break;
      case CONFLUENT_BROKEN:
        break;
    }
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(googleTestId, numTasks, "Some task failed.");
    switch (compatibility) {
      case FULL:
      case KEY_MISMATCH:
        connect
            .assertions()
            .assertConnectorAndExactlyNumTasksAreRunning(
                confluentTestId, numTasks, "Some Google connector task failed.");
        break;
      case CONFLUENT_BROKEN:
        connect
            .assertions()
            .assertConnectorIsRunningAndTasksHaveFailed(
                confluentTestId,
                numTasks,
                "Confluent sink should've been broken for this test case.");
        break;
    }
  }

  public String startConfluentConnector() throws InterruptedException {
    Map<String, String> connectorProps = baseConnectorProps();
    connectorProps.put(
        CONNECTOR_CLASS_CONFIG, "io.confluent.connect.gcp.bigtable.BigtableSinkConnector");
    connectorProps.put("confluent.license", "");
    connectorProps.put("confluent.topic.bootstrap.servers", connect.kafka().bootstrapServers());
    connectorProps.put("confluent.topic.replication.factor", "1");
    return startConnector(connectorProps);
  }

  public String startThisConnector(String confluentConnectorId) throws InterruptedException {
    Map<String, String> connectorProps = baseConnectorProps();
    connectorProps.put(BigtableSinkConfig.DEFAULT_COLUMN_FAMILY_CONFIG, confluentConnectorId);
    return startConnector(connectorProps);
  }

  public String startConnector(Map<String, String> connectorProps) throws InterruptedException {
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
    connectorProps.put(BigtableSinkConfig.ROW_KEY_DELIMITER_CONFIG, "#");
    String topic = startSingleTopicConnector(connectorProps);
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(topic, numTasks, "Connector start timeout");
    return topic;
  }

  @Override
  public Map<String, String> workerProps() {
    Map<String, String> props = super.workerProps();
    String pluginPath = Objects.requireNonNull(System.getenv(PLUGIN_PATH_ENV_VAR_NAME));
    // Enabling embedded Kafka Connect to use the Confluent's sink.
    props.put(WorkerConfig.PLUGIN_PATH_CONFIG, pluginPath);
    return props;
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
    SchemaMessageReader<Object> messageReader = new AvroMessageReader();
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

  public Set<String> getNeededColumnFamilies(String defaultColumnFamily) throws IOException {
    return Stream.concat(
            Stream.of(defaultColumnFamily),
            Arrays.stream(
                    readStringResource(getTestCaseDir() + "/column-families.strings").split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty()))
        .collect(Collectors.toSet());
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

  private long getInputSize() throws IOException {
    String data = readStringResource(getTestCaseDir() + "/data.json");
    return Arrays.stream(data.split("\n")).filter(s -> !s.trim().isEmpty()).count();
  }

  private void assertRowsAreTheSame(Map<ByteString, Row> expected, Map<ByteString, Row> actual) {
    assertEquals(expected.keySet(), actual.keySet());
    for (Row expectedRow : expected.values()) {
      Row actualRow = actual.get(expectedRow.getKey());

      List<RowCell> expectedCells = expectedRow.getCells();
      assertEquals(expectedCells.size(), actualRow.getCells().size());
      for (RowCell expectedCell : expectedCells) {
        ByteString expectedValue = expectedCell.getValue();
        List<RowCell> actualCells =
            actualRow.getCells(expectedCell.getFamily(), expectedCell.getQualifier());
        assertEquals(1, actualCells.size());
        ByteString actualValue = actualCells.get(0).getValue();
        assertEquals(expectedValue, actualValue);
      }
    }
  }

  private Map<String, Map<ByteString, Map<ByteString, Long>>> rowToValues(Collection<Row> rows) {
    Map<String, Map<ByteString, Map<ByteString, Long>>> result = new HashMap<>();
    for (Row r : rows) {
      for (RowCell c : r.getCells()) {
        result
            .computeIfAbsent(c.getFamily(), ignored -> new HashMap<>())
            .computeIfAbsent(c.getQualifier(), ignored -> new HashMap<>())
            .merge(c.getValue(), 1L, Long::sum);
      }
    }
    return result;
  }

  public enum Compatibility {
    FULL,
    KEY_MISMATCH,
    CONFLUENT_BROKEN,
  }
}
