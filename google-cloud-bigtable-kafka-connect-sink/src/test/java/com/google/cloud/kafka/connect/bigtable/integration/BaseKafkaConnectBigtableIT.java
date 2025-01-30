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

import static org.apache.kafka.test.TestUtils.waitForCondition;

import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import java.util.Map;
import org.junit.After;
import org.junit.Before;

public abstract class BaseKafkaConnectBigtableIT extends BaseKafkaConnectIT {
  public static long CONSUME_TIMEOUT_MILLIS = 30000;
  protected BigtableDataClient bigtableData;
  protected BigtableTableAdminClient bigtableAdmin;

  @Before
  public void setUpBigtable() {
    Map<String, String> props = baseConnectorProps();
    bigtableData = getBigtableDataClient(props);
    bigtableAdmin = getBigtableAdminClient(props);
  }

  @After
  public void tearDownBigtable() {
    if (bigtableData != null) {
      bigtableData.close();
    }
    if (bigtableAdmin != null) {
      bigtableAdmin.close();
    }
  }

  public void waitUntilBigtableContainsNumberOfRows(String tableId, long numberOfRows)
      throws InterruptedException {
    waitForCondition(
        () -> readAllRows(bigtableData, tableId).size() == numberOfRows,
        CONSUME_TIMEOUT_MILLIS,
        "Records not consumed in time.");
  }
}
