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
import com.google.cloud.kafka.connect.bigtable.exception.InvalidBigtableSchemaModificationException;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.connect.converters.ByteArrayConverter;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.runtime.errors.DeadLetterQueueReporter;
import org.apache.kafka.connect.storage.StringConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ErrorHandlingIT extends BaseKafkaConnectBigtableIT {
  @Test
  public void testBigtableCredentialsAreCheckedOnStartup() {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_GCP_CREDENTIALS_JSON, "{}");

    String testId = getTestCaseId();
    assertThrows(Throwable.class, () -> connect.configureConnector(testId, props));
    assertThrows(Throwable.class, () -> connect.connectorStatus(testId));
  }

  @org.junit.Ignore // TODO: unignore. For now, the emulator does not cause an exception.
  @Test
  public void testCreationOfInvalidTable() throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    String invalidTableName = "T".repeat(10000);
    props.put(BigtableSinkConfig.CONFIG_TABLE_NAME_FORMAT, invalidTableName);
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES, String.valueOf(true));

    configureDlq(props, dlqTopic);
    String testId = startSingleTopicConnector(props);

    String key = "key";
    String value = "value";

    connect.kafka().produce(testId, key, value);

    ConsumerRecords<byte[], byte[]> dlqRecords =
        connect.kafka().consume(1, Duration.ofSeconds(120).toMillis(), dlqTopic);
    assertEquals(1, dlqRecords.count());
    ConsumerRecord<byte[], byte[]> record = dlqRecords.iterator().next();
    assertArrayEquals(record.key(), key.getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(record.value(), value.getBytes(StandardCharsets.UTF_8));
    assertTrue(
        Arrays.stream(record.headers().toArray())
            .anyMatch(
                h ->
                    h.key().equals(DeadLetterQueueReporter.ERROR_HEADER_EXCEPTION)
                        && Arrays.equals(
                            h.value(),
                            InvalidBigtableSchemaModificationException.class
                                .getName()
                                .getBytes(StandardCharsets.UTF_8))));
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Wrong number of tasks is running.");
  }

  @org.junit.Ignore // TODO: unignore. For now, the emulator does not cause an exception.
  @Test
  public void testTooLargeData() throws InterruptedException, ExecutionException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES, String.valueOf(true));
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES, String.valueOf(true));
    props.put(ConnectorConfig.VALUE_CONVERTER_CLASS_CONFIG, ByteArrayConverter.class.getName());
    configureDlq(props, dlqTopic);
    String testId = startSingleTopicConnector(props);

    byte[] key = "key".getBytes(StandardCharsets.UTF_8);
    // The hard limit is 100 MB as per https://cloud.google.com/bigtable/quotas#limits-data-size
    int twoHundredMegabytes = 200 * 1000 * 1000;
    byte[] value = new byte[twoHundredMegabytes];
    getKafkaProducer().send(new ProducerRecord<>(testId, key, value)).get();

    ConsumerRecords<byte[], byte[]> dlqRecords =
        connect.kafka().consume(1, Duration.ofSeconds(120).toMillis(), dlqTopic);
    assertEquals(1, dlqRecords.count());
    ConsumerRecord<byte[], byte[]> record = dlqRecords.iterator().next();
    assertArrayEquals(record.key(), key);
    assertArrayEquals(record.value(), value);
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Wrong number of tasks is running.");
  }

  @Test
  public void testSecondInsertIntoARowCausesAnError() throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES, String.valueOf(true));
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES, String.valueOf(true));
    props.put(BigtableSinkConfig.CONFIG_INSERT_MODE, InsertMode.INSERT.name());
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
    ConsumerRecords<byte[], byte[]> dlqRecords =
        connect.kafka().consume(1, Duration.ofSeconds(120).toMillis(), dlqTopic);
    assertEquals(1, dlqRecords.count());
    ConsumerRecord<byte[], byte[]> record = dlqRecords.iterator().next();
    assertArrayEquals(record.key(), key.getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(record.value(), valueRejected.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testPartialBatchErrorWhenRelyingOnInputOrdering() throws InterruptedException {
    long dataSize = 10000;

    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES, String.valueOf(true));
    props.put(BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES, String.valueOf(true));
    props.put(BigtableSinkConfig.CONFIG_INSERT_MODE, InsertMode.INSERT.name());
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

    for (long i = 0; i < dataSize; i += 2) {
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
  }
}
