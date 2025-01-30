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

import static org.apache.kafka.test.TestUtils.waitForCondition;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.admin.v2.models.ModifyColumnFamiliesRequest;
import com.google.cloud.kafka.connect.bigtable.util.JsonConverterFactory;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.StreamSupport;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.json.JsonConverterConfig;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.runtime.SinkConnectorConfig;
import org.apache.kafka.connect.runtime.errors.ToleranceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ResourceAutoCreationIT extends BaseKafkaConnectBigtableIT {
  private final JsonConverter jsonConverter = JsonConverterFactory.create(true, false);

  @Test
  public void testMissingAndLaterCreatedTableAndColumnFamily()
      throws InterruptedException, ExecutionException, TimeoutException {
    String dlqTopic = createDlq();
    Map<String, String> props = dlqAndJsonValuesProps(dlqTopic);

    String testId = startSingleTopicConnector(props);

    String key1 = "key1";
    String key2 = "key2";
    String key3 = "key3";
    String key4 = "key4";
    String columnFamily1 = "cf1";
    String columnFamily2 = "cf2";
    String columnQualifier = "cq";

    Integer value = 1;
    Struct columnValue =
        new Struct(SchemaBuilder.struct().field(columnQualifier, Schema.INT32_SCHEMA))
            .put(columnQualifier, value);

    Struct value1 =
        new Struct(SchemaBuilder.struct().field(columnFamily1, columnValue.schema()))
            .put(columnFamily1, columnValue);
    Struct value2 =
        new Struct(SchemaBuilder.struct().field(columnFamily2, columnValue.schema()))
            .put(columnFamily2, columnValue);

    String serializedValue1 =
        new String(
            jsonConverter.fromConnectData(testId, value1.schema(), value1), StandardCharsets.UTF_8);
    String serializedValue2 =
        new String(
            jsonConverter.fromConnectData(testId, value2.schema(), value2), StandardCharsets.UTF_8);

    // With the table missing.
    connect.kafka().produce(testId, key1, serializedValue1);
    ConsumerRecords<byte[], byte[]> dlqRecords =
        connect.kafka().consume(1, Duration.ofSeconds(120).toMillis(), dlqTopic);
    assertEquals(1, dlqRecords.count());
    assertArrayEquals(key1.getBytes(StandardCharsets.UTF_8), dlqRecords.iterator().next().key());
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Wrong number of tasks is running");

    // With the table and column family created.
    bigtableAdmin.createTable(CreateTableRequest.of(testId).addFamily(columnFamily1));
    connect.kafka().produce(testId, key2, serializedValue1);
    waitForCondition(
        () -> readAllRows(bigtableData, testId).size() == 1,
        Duration.ofSeconds(15).toMillis(),
        "Records not processed in time");
    assertTrue(
        readAllRows(bigtableData, testId)
            .containsKey(ByteString.copyFrom(key2.getBytes(StandardCharsets.UTF_8))));
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Wrong number of tasks is running");

    // With the column family missing.
    connect.kafka().produce(testId, key3, serializedValue2);
    dlqRecords = connect.kafka().consume(2, Duration.ofSeconds(120).toMillis(), dlqTopic);
    assertEquals(2, dlqRecords.count());
    assertTrue(
        StreamSupport.stream(dlqRecords.spliterator(), false)
            .anyMatch(r -> Arrays.equals(key3.getBytes(StandardCharsets.UTF_8), r.key())));
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Wrong number of tasks is running");
    // With the column family created.
    bigtableAdmin.modifyFamilies(ModifyColumnFamiliesRequest.of(testId).addFamily(columnFamily2));
    connect.kafka().produce(testId, key4, serializedValue2);
    waitForCondition(
        () -> readAllRows(bigtableData, testId).size() == 2,
        Duration.ofSeconds(15).toMillis(),
        "Records not " + "processed in time");
    assertTrue(
        readAllRows(bigtableData, testId)
            .containsKey(ByteString.copyFrom(key4.getBytes(StandardCharsets.UTF_8))));
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Wrong number of tasks is running");
  }

  private String createDlq() {
    String dlqTopic = getTestCaseId() + System.currentTimeMillis();
    connect.kafka().createTopic(dlqTopic, numBrokers);
    return dlqTopic;
  }

  private Map<String, String> dlqAndJsonValuesProps(String dlqTopic) {
    Map<String, String> props = baseConnectorProps();
    props.put(SinkConnectorConfig.DLQ_TOPIC_NAME_CONFIG, dlqTopic);
    props.put(SinkConnectorConfig.DLQ_CONTEXT_HEADERS_ENABLE_CONFIG, String.valueOf(false));
    props.put(SinkConnectorConfig.DLQ_TOPIC_REPLICATION_FACTOR_CONFIG, String.valueOf(numBrokers));
    props.put(SinkConnectorConfig.ERRORS_TOLERANCE_CONFIG, ToleranceType.ALL.value());
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, JsonConverter.class.getName());
    props.put(
        ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG
            + "."
            + JsonConverterConfig.SCHEMAS_ENABLE_CONFIG,
        String.valueOf(true));
    return props;
  }
}
