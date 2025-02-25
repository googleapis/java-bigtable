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
package com.google.cloud.kafka.connect.bigtable;

import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkTaskConfig.TASK_ID_CONFIG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.cloud.kafka.connect.bigtable.util.BasicPropertiesFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BigtableSinkConnectorTest {
  BigtableSinkConnector connector;

  @Before
  public void setUp() {
    connector = new BigtableSinkConnector();
  }

  @Test
  public void testConfig() {
    assertNotNull(connector.config());
  }

  @Test
  public void testValidate() {
    connector.validate(BasicPropertiesFactory.getSinkProps());
  }

  @Test
  public void testStart() {
    connector.start(BasicPropertiesFactory.getSinkProps());
  }

  @Test
  public void testStop() {
    connector.stop();
  }

  @Test
  public void testTaskClass() {
    assertEquals(BigtableSinkTask.class, connector.taskClass());
  }

  @Test
  public void testTaskConfigs() {
    Map<String, String> connectorConfig = BasicPropertiesFactory.getSinkProps();
    connector.start(new HashMap<>(connectorConfig));
    int maxTasks = 1000;
    List<Map<String, String>> taskConfigs = connector.taskConfigs(maxTasks);
    assertEquals(maxTasks, taskConfigs.size());
    for (Integer i = 0; i < maxTasks; i++) {
      Map<String, String> taskConfig = taskConfigs.get(i);
      assertEquals(i.toString(), taskConfig.get(TASK_ID_CONFIG));
      taskConfig.remove(TASK_ID_CONFIG);
      assertEquals(connectorConfig, taskConfig);
    }
  }

  @Test
  public void testVersion() {
    assertNotNull(connector.version());
  }
}
