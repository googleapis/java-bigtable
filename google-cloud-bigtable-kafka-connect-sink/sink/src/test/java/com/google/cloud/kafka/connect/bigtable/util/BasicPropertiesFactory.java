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
package com.google.cloud.kafka.connect.bigtable.util;

import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkTaskConfig;
import java.util.HashMap;
import java.util.Map;

public class BasicPropertiesFactory {
  public static Map<String, String> getSinkProps() {
    Map<String, String> props = new HashMap<>();
    props.put(BigtableSinkConfig.GCP_PROJECT_ID_CONFIG, "project");
    props.put(BigtableSinkConfig.BIGTABLE_INSTANCE_ID_CONFIG, "instance");
    return props;
  }

  public static Map<String, String> getTaskProps() {
    Map<String, String> props = getSinkProps();
    props.put(BigtableSinkTaskConfig.TASK_ID_CONFIG, "1");
    return props;
  }
}
