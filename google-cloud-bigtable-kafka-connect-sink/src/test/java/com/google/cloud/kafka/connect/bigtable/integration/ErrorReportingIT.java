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

import com.google.cloud.kafka.connect.bigtable.config.BigtableErrorMode;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ErrorReportingIT extends BaseKafkaConnectIT {
  @Test
  public void testErrorModeFail() throws InterruptedException {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_ERROR_MODE, BigtableErrorMode.FAIL.name());

    String testId = startSingleTopicConnector(props);
    connect.kafka().produce(testId, "key", "value");
    connect
        .assertions()
        .assertConnectorIsRunningAndTasksHaveFailed(
            testId, 1, "Task didn't fail despite configured error mode.");
  }

  @Test
  public void testErrorModeWarn() throws InterruptedException {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_ERROR_MODE, BigtableErrorMode.WARN.name());

    String testId = startSingleTopicConnector(props);
    connect.kafka().produce(testId, "key", "value");
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Task failed despite configured error mode.");
  }

  @Test
  public void testErrorModeIgnore() throws InterruptedException {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_ERROR_MODE, BigtableErrorMode.IGNORE.name());

    String testId = startSingleTopicConnector(props);
    connect.kafka().produce(testId, "key", "value");
    connect
        .assertions()
        .assertConnectorAndExactlyNumTasksAreRunning(
            testId, numTasks, "Task failed despite configured error mode.");
  }

  @Test
  public void testErrorModeDLQOverridesErrorMode() throws InterruptedException {
    String dlqTopic = createDlq();
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_ERROR_MODE, BigtableErrorMode.FAIL.name());
    configureDlq(props, dlqTopic);

    String key = "key";
    String value = "value";
    String testId = startSingleTopicConnector(props);
    connect.kafka().produce(testId, key, value);
    assertSingleDlqEntry(dlqTopic, key, value, null);
    assertConnectorAndAllTasksAreRunning(testId);
  }
}
