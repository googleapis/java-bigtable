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

import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.config.InsertMode;
import io.confluent.connect.avro.AvroConverter;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import java.util.Map;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.storage.Converter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MultipleConnectorTasksIT extends BaseDataGeneratorIT {
  // Note that this test possibly could become flaky if Admin API write retry's initial delay is set
  // to a too low value and retries of table/column family creation exhausted the quota that should
  // be used for creation of different column families.
  @Test
  public void testMultipleTasks() throws InterruptedException {
    numRecords = 1000L;
    numTasks = 10;

    Map<String, String> converterProps =
        Map.of(
            AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
            schemaRegistry.schemaRegistryUrl());
    Converter keyConverter = new AvroConverter();
    keyConverter.configure(converterProps, true);
    Converter valueConverter = new AvroConverter();
    valueConverter.configure(converterProps, false);

    Map<String, String> connectorProps = baseConnectorProps();
    connectorProps.put(BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG, "true");
    connectorProps.put(BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG, "true");
    connectorProps.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.UPSERT.name());

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

    String testId = startSingleTopicConnector(connectorProps);
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(testId, numTasks, "Connector start timeout");
    populateKafkaTopic(testId, numRecords, keyConverter, valueConverter);

    waitUntilBigtableContainsNumberOfRows(testId, numRecords);
    assertConnectorAndAllTasksAreRunning(testId);
  }

  @Test
  public void testRestartPauseStop() throws InterruptedException {
    numTasks = 10;
    int expectedRowsInBigtable = 0;
    String defaultColumnFamily = "default";
    String value = "1";
    Map<String, String> connectorProps = baseConnectorProps();
    connectorProps.put(BigtableSinkConfig.DEFAULT_COLUMN_FAMILY_CONFIG, defaultColumnFamily);
    String testId = startSingleTopicConnector(connectorProps);
    bigtableAdmin.createTable(CreateTableRequest.of(testId).addFamily(defaultColumnFamily));

    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(testId, numTasks, "Connector start timeout");
    assertEquals(expectedRowsInBigtable, readAllRows(bigtableData, testId).size());
    connect.kafka().produce(testId, "started", value);
    expectedRowsInBigtable += 1;
    waitUntilBigtableContainsNumberOfRows(testId, expectedRowsInBigtable);

    connect.restartConnectorAndTasks(testId, false, true, false);
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(testId, numTasks, "Connector restart timeout");
    assertEquals(expectedRowsInBigtable, readAllRows(bigtableData, testId).size());
    connect.kafka().produce(testId, "restarted", value);
    expectedRowsInBigtable += 1;
    waitUntilBigtableContainsNumberOfRows(testId, expectedRowsInBigtable);

    connect.pauseConnector(testId);
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksArePaused(testId, numTasks, "Connector pause timeout");
    connect.resumeConnector(testId);
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Connector post-pause resume timeout");
    assertEquals(expectedRowsInBigtable, readAllRows(bigtableData, testId).size());
    connect.kafka().produce(testId, "pause", value);
    expectedRowsInBigtable += 1;
    waitUntilBigtableContainsNumberOfRows(testId, expectedRowsInBigtable);

    connect.stopConnector(testId);
    connect.assertions().assertConnectorIsStopped(testId, "Connector stop timeout");
    connect.resumeConnector(testId);
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Connector post-stop resume timeout");
    assertEquals(expectedRowsInBigtable, readAllRows(bigtableData, testId).size());
    connect.kafka().produce(testId, "stop", value);
    expectedRowsInBigtable += 1;
    waitUntilBigtableContainsNumberOfRows(testId, expectedRowsInBigtable);

    connect.deleteConnector(testId);
    connect.assertions().assertConnectorDoesNotExist(testId, "Connector deletion timeout");
  }
}
