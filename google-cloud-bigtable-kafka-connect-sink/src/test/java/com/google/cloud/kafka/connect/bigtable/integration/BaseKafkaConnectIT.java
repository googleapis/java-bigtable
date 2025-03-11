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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.runtime.SinkConnectorConfig;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.runtime.errors.DeadLetterQueueReporter;
import org.apache.kafka.connect.runtime.errors.ToleranceType;
import org.apache.kafka.connect.runtime.isolation.PluginDiscoveryMode;
import org.apache.kafka.connect.storage.Converter;
import org.apache.kafka.connect.util.clusters.EmbeddedConnectCluster;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseKafkaConnectIT extends BaseIT {
  public static final long PRODUCE_TIMEOUT_MILLIS = 15000L;
  // https://cloud.google.com/bigtable/docs/reference/admin/rpc/google.bigtable.admin.v2#createtablerequest
  public static int MAX_BIGTABLE_TABLE_NAME_LENGTH = 50;
  public static final String PLUGIN_PATH_ENV_VAR_NAME = "INTEGRATION_TEST_PLUGINS_PATH";

  private final Logger logger = LoggerFactory.getLogger(BaseKafkaConnectIT.class);

  public EmbeddedConnectCluster connect;
  public Admin kafkaAdminClient;
  public int numWorkers = 1;
  public int numBrokers = 1;

  @Before
  public void setUpConnect() {
    startConnect();
  }

  @After
  public void tearDownConnect() {
    stopConnect();
  }

  public void startConnect() {
    logger.info("Starting embedded Kafka Connect cluster...");

    Properties brokerProps = new Properties();
    brokerProps.put("socket.request.max.bytes", maxKafkaMessageSizeBytes);
    brokerProps.put("message.max.bytes", maxKafkaMessageSizeBytes);
    brokerProps.put("auto.create.topics.enable", "false");
    brokerProps.put("delete.topic.enable", "true");

    Map<String, String> clientConfigs = new HashMap<>();
    clientConfigs.put(
        ProducerConfig.MAX_REQUEST_SIZE_CONFIG, String.valueOf(maxKafkaMessageSizeBytes));
    clientConfigs.put(
        ProducerConfig.BUFFER_MEMORY_CONFIG, String.valueOf(maxKafkaMessageSizeBytes));
    clientConfigs.put(
        ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, String.valueOf(maxKafkaMessageSizeBytes));
    connect =
        new EmbeddedConnectCluster.Builder()
            .name("kcbt-connect-cluster-" + getTestClassId())
            .numWorkers(numWorkers)
            .numBrokers(numBrokers)
            .brokerProps(brokerProps)
            .workerProps(workerProps())
            .clientProps(clientConfigs)
            .build();

    // Start the clusters
    connect.start();
    try {
      connect
          .assertions()
          .assertExactlyNumWorkersAreUp(1, "Initial group of workers did not start in time.");
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    kafkaAdminClient = connect.kafka().createAdminClient();
    logger.info(
        "Started embedded Kafka Connect cluster using bootstrap servers: {}",
        connect.kafka().bootstrapServers());
  }

  public Map<String, String> workerProps() {
    Map<String, String> workerProps = new HashMap<>();
    workerProps.put(WorkerConfig.OFFSET_COMMIT_INTERVAL_MS_CONFIG, Long.toString(10000));
    workerProps.put(WorkerConfig.PLUGIN_DISCOVERY_CONFIG, PluginDiscoveryMode.HYBRID_WARN.name());
    return workerProps;
  }

  public void stopConnect() {
    logger.info(
        "Stopping embedded Kafka Connect cluster using bootstrap servers: {}",
        connect.kafka().bootstrapServers());
    if (kafkaAdminClient != null) {
      Utils.closeQuietly(kafkaAdminClient, "Admin client for embedded Kafka cluster");
      kafkaAdminClient = null;
    }

    // Stop all Connect, Kafka and Zk threads.
    if (connect != null) {
      Utils.closeQuietly(connect::stop, "Embedded Connect, Kafka, and Zookeeper clusters");
      connect = null;
    }
  }

  public String startSingleTopicConnector(Map<String, String> configProps)
      throws InterruptedException {
    return startConnector(configProps, Collections.emptySet());
  }

  public String startMultipleTopicConnector(
      Map<String, String> configProps, Set<String> topicNameSuffixes) throws InterruptedException {
    return startConnector(configProps, topicNameSuffixes);
  }

  private String startConnector(Map<String, String> configProps, Set<String> topicNameSuffixes)
      throws InterruptedException {
    int longestSuffix = topicNameSuffixes.stream().mapToInt(String::length).max().orElse(0);
    String id =
        StringUtils.right(
            getTestCaseId() + System.currentTimeMillis(),
            MAX_BIGTABLE_TABLE_NAME_LENGTH - longestSuffix);
    if (topicNameSuffixes.isEmpty()) {
      configProps.put(SinkConnectorConfig.TOPICS_CONFIG, id);
      connect.kafka().createTopic(id, numTasks);
    } else {
      configProps.put(SinkConnectorConfig.TOPICS_REGEX_CONFIG, id + ".*");
      for (String suffix : topicNameSuffixes) {
        connect.kafka().createTopic(id + suffix, numTasks);
      }
    }
    connect.configureConnector(id, configProps);
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(id, numTasks, "Connector start timeout");
    return id;
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
