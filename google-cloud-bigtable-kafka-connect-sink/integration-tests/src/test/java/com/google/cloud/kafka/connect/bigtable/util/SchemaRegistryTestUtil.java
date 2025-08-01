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
package com.google.cloud.kafka.connect.bigtable.util;

/*
 * This software contains code derived from the BigQuery Connector for Apache Kafka,
 * Copyright Aiven Oy, which in turn contains code derived from the Confluent BigQuery
 * Kafka Connector, Copyright Confluent, Inc, which in turn contains code derived from
 * the WePay BigQuery Kafka Connector, Copyright WePay, Inc.
 */

import static io.confluent.kafka.schemaregistry.ClusterTestHarness.KAFKASTORE_TOPIC;
import static java.util.Objects.requireNonNull;

import io.confluent.kafka.schemaregistry.CompatibilityLevel;
import io.confluent.kafka.schemaregistry.RestApp;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import org.apache.kafka.test.TestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaRegistryTestUtil {
  private static long STARTUP_TIMEOUT_MILLIS = 10000L;
  private final Logger logger = LoggerFactory.getLogger(SchemaRegistryTestUtil.class);

  protected String bootstrapServers;

  private String schemaRegistryUrl;

  private RestApp restApp;

  public SchemaRegistryTestUtil(String bootstrapServers) {
    this.bootstrapServers = requireNonNull(bootstrapServers);
  }

  public void start() throws Exception {
    logger.info("Starting embedded Schema Registry...");
    int port = findAvailableOpenPort();
    restApp =
        new RestApp(
            port,
            null,
            this.bootstrapServers,
            KAFKASTORE_TOPIC,
            CompatibilityLevel.NONE.name,
            true,
            new Properties());
    restApp.start();

    TestUtils.waitForCondition(
        () -> restApp.restServer.isRunning(),
        STARTUP_TIMEOUT_MILLIS,
        "Schema Registry start timed out.");

    schemaRegistryUrl = restApp.restServer.getURI().toString();
    logger.info("Started embedded Schema Registry using bootstrap servers: {}", bootstrapServers);
  }

  public void stop() throws Exception {
    if (restApp != null) {
      logger.info("Stopping embedded Schema Registry...");
      restApp.stop();
    }
  }

  public String schemaRegistryUrl() {
    return schemaRegistryUrl;
  }

  private Integer findAvailableOpenPort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }
}
