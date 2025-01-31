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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.runtime.SinkConnectorConfig;
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
    Map<String, Object> producerProps = new HashMap<>();
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, connect.kafka().bootstrapServers());
    producerProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, maxKafkaMessageSizeBytes);
    return new KafkaProducer<>(producerProps, new ByteArraySerializer(), new ByteArraySerializer());
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
}
