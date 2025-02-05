/*
 * Copyright 2024 Google LLC
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

import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.BIGTABLE_INSTANCE_ID_CONFIG;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.GCP_PROJECT_ID_CONFIG;
import static org.apache.kafka.connect.runtime.ConnectorConfig.CONNECTOR_CLASS_CONFIG;
import static org.apache.kafka.connect.runtime.ConnectorConfig.TASKS_MAX_CONFIG;
import static org.apache.kafka.connect.runtime.WorkerConfig.KEY_CONVERTER_CLASS_CONFIG;
import static org.apache.kafka.connect.runtime.WorkerConfig.VALUE_CONVERTER_CLASS_CONFIG;

import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.kafka.connect.bigtable.BigtableSinkConnector;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.util.TestId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.runtime.SinkConnectorConfig;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.runtime.isolation.PluginDiscoveryMode;
import org.apache.kafka.connect.storage.StringConverter;
import org.apache.kafka.connect.util.clusters.EmbeddedConnectCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseIT {
  // https://cloud.google.com/bigtable/docs/reference/admin/rpc/google.bigtable.admin.v2#createtablerequest
  public static int MAX_BIGTABLE_TABLE_NAME_LENGTH = 50;

  private final Logger logger = LoggerFactory.getLogger(BaseIT.class);
  protected EmbeddedConnectCluster connect;
  private Admin kafkaAdminClient;
  protected int numWorkers = 1;
  protected int numBrokers = 1;
  protected int numTasks = 1;
  protected int maxKafkaMessageSizeBytes = 300 * 1024 * 1024;

  protected void startConnect() {
    logger.info("Starting embedded Kafka Connect cluster...");
    Map<String, String> workerProps = new HashMap<>();
    workerProps.put(WorkerConfig.OFFSET_COMMIT_INTERVAL_MS_CONFIG, Long.toString(10000));
    workerProps.put(WorkerConfig.PLUGIN_DISCOVERY_CONFIG, PluginDiscoveryMode.HYBRID_WARN.name());

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
            .workerProps(workerProps)
            .clientConfigs(clientConfigs)
            .build();

    // Start the clusters
    connect.start();
    try {
      connect
          .assertions()
          .assertAtLeastNumWorkersAreUp(1, "Initial group of workers did not start in time.");
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    kafkaAdminClient = connect.kafka().createAdminClient();
    logger.info(
        "Started embedded Kafka Connect cluster using bootstrap servers: {}",
        connect.kafka().bootstrapServers());
  }

  protected void stopConnect() {
    logger.info("Stopping embedded Kafka Connect cluster...");
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

  protected Map<String, String> baseConnectorProps() {
    Map<String, String> result = new HashMap<>();

    result.put(CONNECTOR_CLASS_CONFIG, BigtableSinkConnector.class.getCanonicalName());
    result.put(TASKS_MAX_CONFIG, Integer.toString(numTasks));
    result.put(KEY_CONVERTER_CLASS_CONFIG, StringConverter.class.getName());
    result.put(VALUE_CONVERTER_CLASS_CONFIG, StringConverter.class.getName());
    // Needed so that all messages we send to the input topics can be also sent to the DLQ by
    // DeadLetterQueueReporter.
    result.put(
        ConnectorConfig.CONNECTOR_CLIENT_PRODUCER_OVERRIDES_PREFIX
            + ProducerConfig.MAX_REQUEST_SIZE_CONFIG,
        String.valueOf(maxKafkaMessageSizeBytes));
    result.put(
        ConnectorConfig.CONNECTOR_CLIENT_PRODUCER_OVERRIDES_PREFIX
            + ProducerConfig.BUFFER_MEMORY_CONFIG,
        String.valueOf(maxKafkaMessageSizeBytes));

    // TODO: get it from environment variables after migrating to kokoro.
    result.put(GCP_PROJECT_ID_CONFIG, "todotodo");
    result.put(BIGTABLE_INSTANCE_ID_CONFIG, "todotodo");

    return result;
  }

  protected BigtableDataClient getBigtableDataClient(Map<String, String> configProps) {
    return new BigtableSinkConfig(configProps).getBigtableDataClient();
  }

  protected BigtableTableAdminClient getBigtableAdminClient(Map<String, String> configProps) {
    return new BigtableSinkConfig(configProps).getBigtableAdminClient();
  }

  protected String getTestClassId() {
    return TestId.getTestClassId(this.getClass());
  }

  protected String getTestCaseId() {
    return TestId.getTestCaseId(this.getClass());
  }

  protected String startSingleTopicConnector(Map<String, String> configProps)
      throws InterruptedException {
    return startConnector(configProps, Collections.emptySet());
  }

  protected String startMultipleTopicConnector(
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
      connect.kafka().createTopic(id, numBrokers);
    } else {
      configProps.put(SinkConnectorConfig.TOPICS_REGEX_CONFIG, id + ".*");
      for (String suffix : topicNameSuffixes) {
        connect.kafka().createTopic(id + suffix, numBrokers);
      }
    }
    connect.configureConnector(id, configProps);
    connect
        .assertions()
        .assertConnectorAndAtLeastNumTasksAreRunning(id, numTasks, "Connector start timeout");
    return id;
  }
}
