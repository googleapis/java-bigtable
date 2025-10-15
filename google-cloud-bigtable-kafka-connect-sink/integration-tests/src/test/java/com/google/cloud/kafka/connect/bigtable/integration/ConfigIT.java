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

import java.util.Map;
import org.apache.kafka.connect.runtime.SinkConnectorConfig;
import org.apache.kafka.connect.runtime.rest.errors.ConnectRestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConfigIT extends BaseKafkaConnectIT {
  @Test
  public void testBaseSuccess() throws InterruptedException {
    String topic = getTestCaseId();
    String connectorName = "connector-" + topic;
    connect.kafka().createTopic(topic, numTasks);
    Map<String, String> props = baseConnectorProps();
    props.put(SinkConnectorConfig.TOPICS_CONFIG, topic);
    connect.configureConnector(connectorName, props);
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            connectorName, numTasks, "Connector start timeout");
  }

  @Test(expected = ConnectRestException.class)
  public void testUnconfiguredError() {
    Map<String, String> props = baseConnectorProps();
    connect.configureConnector(getTestCaseId(), props);
  }
}
