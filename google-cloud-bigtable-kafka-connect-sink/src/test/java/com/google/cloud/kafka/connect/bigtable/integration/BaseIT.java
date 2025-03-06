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
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.connect.runtime.ConnectorConfig;
import org.apache.kafka.connect.storage.StringConverter;

public abstract class BaseIT {
  public int numTasks = 1;
  public int maxKafkaMessageSizeBytes = 300 * 1024 * 1024;

  public Map<String, String> baseConnectorProps() {
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

  public BigtableDataClient getBigtableDataClient(Map<String, String> configProps) {
    return new BigtableSinkConfig(configProps).getBigtableDataClient();
  }

  public BigtableTableAdminClient getBigtableAdminClient(Map<String, String> configProps) {
    return new BigtableSinkConfig(configProps).getBigtableAdminClient();
  }

  public String getTestClassId() {
    return TestId.getTestClassId(this.getClass());
  }

  public String getTestCaseId() {
    return TestId.getTestCaseId(this.getClass());
  }
}
