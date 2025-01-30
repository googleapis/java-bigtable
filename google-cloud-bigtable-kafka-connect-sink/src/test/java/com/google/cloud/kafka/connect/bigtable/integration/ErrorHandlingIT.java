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

import static org.junit.Assert.assertThrows;

import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ErrorHandlingIT extends BaseKafkaConnectBigtableIT {
  @Test
  public void testBigtableCredentialsAreCheckedOnStartup() throws InterruptedException {
    Map<String, String> props = baseConnectorProps();
    props.put(BigtableSinkConfig.CONFIG_GCP_CREDENTIALS_JSON, "{}");

    String testId = getTestCaseId();
    assertThrows(Throwable.class, () -> connect.configureConnector(testId, props));
    assertThrows(Throwable.class, () -> connect.connectorStatus(testId));
  }
}
