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
package com.google.cloud.kafka.connect.bigtable.config;

import java.util.Map;
import org.apache.kafka.common.config.ConfigDef;

/**
 * A class defining configuration of {@link
 * com.google.cloud.kafka.connect.bigtable.BigtableSinkTask}.
 */
public class BigtableSinkTaskConfig extends BigtableSinkConfig {
  public static String CONFIG_TASK_ID = "taskId";

  /**
   * The main constructor.
   *
   * @param properties The properties provided by the caller.
   */
  public BigtableSinkTaskConfig(Map<String, String> properties) {
    super(getDefinition(), properties);
  }

  /**
   * @return {@link ConfigDef} used by Kafka Connect to advertise configuration options to the user
   *     and by us to perform basic validation of the user-provided values.
   */
  public static ConfigDef getDefinition() {
    return BigtableSinkConfig.getDefinition()
        .defineInternal(
            CONFIG_TASK_ID,
            ConfigDef.Type.INT,
            ConfigDef.NO_DEFAULT_VALUE,
            ConfigDef.Importance.LOW);
  }
}
