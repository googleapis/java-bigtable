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

import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_BIGTABLE_INSTANCE_ID;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_GCP_PROJECT_ID;
import static org.apache.kafka.connect.runtime.ConnectorConfig.CONNECTOR_CLASS_CONFIG;
import static org.apache.kafka.connect.runtime.ConnectorConfig.TASKS_MAX_CONFIG;
import static org.apache.kafka.connect.runtime.WorkerConfig.KEY_CONVERTER_CLASS_CONFIG;
import static org.apache.kafka.connect.runtime.WorkerConfig.VALUE_CONVERTER_CLASS_CONFIG;

import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.kafka.connect.bigtable.BigtableSinkConnector;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.util.TestId;
import com.google.protobuf.ByteString;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.runtime.SinkConnectorConfig;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.runtime.isolation.PluginDiscoveryMode;
import org.apache.kafka.connect.storage.StringConverter;
import org.apache.kafka.connect.util.clusters.EmbeddedConnectCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseIT {
  private final Logger logger = LoggerFactory.getLogger(BaseIT.class);
  protected EmbeddedConnectCluster connect;
  private Admin kafkaAdminClient;
  protected int numWorkers = 1;
  protected int numBrokers = 1;
  protected int numTasks = 1;
  protected int maxKafkaMessageSizeBytes = 10 * 1024 * 1024;

  protected void startConnect() {
    logger.info("Starting embedded Kafka Connect cluster...");
    Map<String, String> workerProps = new HashMap<>();
    workerProps.put(WorkerConfig.OFFSET_COMMIT_INTERVAL_MS_CONFIG, Long.toString(10000));
    workerProps.put(WorkerConfig.PLUGIN_DISCOVERY_CONFIG, PluginDiscoveryMode.HYBRID_WARN.name());

    Properties brokerProps = new Properties();
    brokerProps.put("message.max.bytes", maxKafkaMessageSizeBytes);
    brokerProps.put("auto.create.topics.enable", "false");
    brokerProps.put("delete.topic.enable", "true");
    connect =
        new EmbeddedConnectCluster.Builder()
            .name("kcbt-connect-cluster-" + getTestClassId())
            .numWorkers(numWorkers)
            .numBrokers(numBrokers)
            .brokerProps(brokerProps)
            .workerProps(workerProps)
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

    // TODO: get it from environment variables after migrating to kokoro.
    result.put(CONFIG_GCP_PROJECT_ID, "todotodo");
    result.put(CONFIG_BIGTABLE_INSTANCE_ID, "todotodo");

    return result;
  }

  protected BigtableDataClient getBigtableDataClient(Map<String, String> configProps) {
    return new BigtableSinkConfig(configProps).getBigtableDataClient();
  }

  protected BigtableTableAdminClient getBigtableAdminClient(Map<String, String> configProps) {
    return new BigtableSinkConfig(configProps).getBigtableAdminClient();
  }

  protected Map<ByteString, Row> readAllRows(BigtableDataClient bigtable, String table) {
    Query query = Query.create(table);
    return bigtable.readRows(query).stream().collect(Collectors.toMap(Row::getKey, r -> r));
  }

  protected String getTestClassId() {
    return TestId.getTestClassId(this.getClass());
  }

  protected String getTestCaseId() {
    return TestId.getTestCaseId(this.getClass());
  }

  protected String startSingleTopicConnector(Map<String, String> configProps)
      throws InterruptedException {
    String id = getTestCaseId() + System.currentTimeMillis();
    configProps.put(SinkConnectorConfig.TOPICS_CONFIG, id);
    connect.kafka().createTopic(id, numBrokers);
    connect.configureConnector(id, configProps);
    connect
        .assertions()
        .assertConnectorAndAtLeastNumTasksAreRunning(id, numTasks, "Connector start timeout");
    return id;
  }
}
