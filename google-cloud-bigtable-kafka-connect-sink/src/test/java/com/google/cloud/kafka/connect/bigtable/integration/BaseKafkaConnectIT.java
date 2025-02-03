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
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.runtime.SinkConnectorConfig;
import org.apache.kafka.connect.runtime.errors.DeadLetterQueueReporter;
import org.apache.kafka.connect.runtime.errors.ToleranceType;
import org.apache.kafka.connect.storage.Converter;
import org.junit.After;
import org.junit.Before;

public abstract class BaseKafkaConnectIT extends BaseIT {
  private static final long PRODUCE_TIMEOUT_MILLIS = 15000L;

  @Before
  public void setUpConnect() {
    startConnect();
  }

  @After
  public void tearDownConnect() {
    stopConnect();
  }

  public KafkaProducer<byte[], byte[]> getKafkaProducer() {
    return connect.kafka().createProducer(Collections.emptyMap());
  }

  public void sendRecords(
      String topic,
      Collection<Map.Entry<SchemaAndValue, SchemaAndValue>> keysAndValues,
      Converter keyConverter,
      Converter valueConverter) {
    try (KafkaProducer<byte[], byte[]> producer = getKafkaProducer()) {
      List<Future<RecordMetadata>> produceFutures = new ArrayList<>();
      for (Map.Entry<SchemaAndValue, SchemaAndValue> keyAndValue : keysAndValues) {
        SchemaAndValue key = keyAndValue.getKey();
        SchemaAndValue value = keyAndValue.getValue();
        byte[] serializedKey = keyConverter.fromConnectData(topic, key.schema(), key.value());
        byte[] serializedValue =
            valueConverter.fromConnectData(topic, value.schema(), value.value());
        ProducerRecord<byte[], byte[]> msg =
            new ProducerRecord<>(topic, serializedKey, serializedValue);
        Future<RecordMetadata> produceFuture = producer.send(msg);
        produceFutures.add(produceFuture);
      }
      produceFutures.parallelStream()
          .forEach(
              f -> {
                try {
                  f.get(PRODUCE_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                  throw new RuntimeException(e);
                }
              });
    }
  }

  public String createDlq() {
    String dlqTopic = getTestCaseId() + System.currentTimeMillis();
    connect.kafka().createTopic(dlqTopic, numBrokers);
    return dlqTopic;
  }

  public void configureDlq(Map<String, String> props, String dlqTopic) {
    props.put(SinkConnectorConfig.DLQ_TOPIC_NAME_CONFIG, dlqTopic);
    props.put(SinkConnectorConfig.DLQ_CONTEXT_HEADERS_ENABLE_CONFIG, String.valueOf(true));
    props.put(SinkConnectorConfig.DLQ_TOPIC_REPLICATION_FACTOR_CONFIG, String.valueOf(numBrokers));
    props.put(SinkConnectorConfig.ERRORS_TOLERANCE_CONFIG, ToleranceType.ALL.value());
  }

  public void assertDlqIsEmpty(String dlqTopic)
      throws ExecutionException, InterruptedException, TimeoutException {
    ConsumerRecords<byte[], byte[]> dlqRecords =
        connect
            .kafka()
            .consumeAll(
                Duration.ofSeconds(15).toMillis(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                dlqTopic);
    assertEquals(0, dlqRecords.count());
  }

  public void assertConnectorAndAllTasksAreRunning(String connectorId) throws InterruptedException {
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            connectorId, numTasks, "Wrong number of tasks is running.");
  }

  public void assertSingleDlqEntry(
      String dlqTopic, String key, String value, Class<?> exceptionClass) {
    assertSingleDlqEntry(
        dlqTopic,
        Optional.ofNullable(key).map(s -> s.getBytes(StandardCharsets.UTF_8)).orElse(null),
        Optional.ofNullable(value).map(s -> s.getBytes(StandardCharsets.UTF_8)).orElse(null),
        exceptionClass);
  }

  public void assertSingleDlqEntry(
      String dlqTopic, byte[] key, byte[] value, Class<?> exceptionClass) {
    ConsumerRecords<byte[], byte[]> dlqRecords =
        connect.kafka().consume(1, Duration.ofSeconds(120).toMillis(), dlqTopic);
    assertEquals(1, dlqRecords.count());
    ConsumerRecord<byte[], byte[]> record = dlqRecords.iterator().next();
    if (key != null) {
      assertArrayEquals(record.key(), key);
    }
    if (value != null) {
      assertArrayEquals(record.value(), value);
    }
    if (exceptionClass != null) {
      assertTrue(
          Arrays.stream(record.headers().toArray())
              .anyMatch(
                  h ->
                      h.key().equals(DeadLetterQueueReporter.ERROR_HEADER_EXCEPTION)
                          && Arrays.equals(
                              h.value(),
                              exceptionClass.getName().getBytes(StandardCharsets.UTF_8))));
    }
  }
}
