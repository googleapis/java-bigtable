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

import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkTaskConfig;
import com.google.cloud.kafka.connect.bigtable.version.PackageMetadata;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** {@link SinkConnector} class for Cloud Bigtable. */
public class BigtableSinkConnector extends SinkConnector {
  private Map<String, String> configProperties;
  private final Logger logger = LoggerFactory.getLogger(BigtableSinkConnector.class);

  @Override
  public ConfigDef config() {
    logger.trace("config()");
    return BigtableSinkConfig.getDefinition();
  }

  @Override
  public Config validate(Map<String, String> properties) {
    logger.trace("validate()");
    return BigtableSinkConfig.validate(properties);
  }

  @Override
  public void start(Map<String, String> props) {
    logger.trace("start()");
    configProperties = props;
  }

  @Override
  public void stop() {
    logger.trace("stop()");
  }

  @Override
  public Class<? extends Task> taskClass() {
    logger.trace("taskClass()");
    return BigtableSinkTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    logger.trace("taskClass({})", maxTasks);
    List<Map<String, String>> configs = new ArrayList<>(maxTasks);
    for (int i = 0; i < maxTasks; i++) {
      Map<String, String> config = new HashMap<>(configProperties);
      config.put(BigtableSinkTaskConfig.TASK_ID_CONFIG, Integer.toString(i));
      configs.add(config);
    }
    return configs;
  }

  @Override
  public String version() {
    logger.trace("version()");
    return PackageMetadata.getVersion();
  }
}
