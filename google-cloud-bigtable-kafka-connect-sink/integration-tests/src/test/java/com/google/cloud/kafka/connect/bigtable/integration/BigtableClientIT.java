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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BigtableClientIT extends BaseIT {
  @Test
  public void testClient() {
    Map<String, String> props = baseConnectorProps();
    BigtableSinkConfig config = new BigtableSinkConfig(props);

    BigtableTableAdminClient admin = config.getBigtableAdminClient();
    String tableId = getTestCaseId() + System.currentTimeMillis();
    String columnFamily = "columnFamily";

    CreateTableRequest createTableRequest = CreateTableRequest.of(tableId).addFamily(columnFamily);
    assertFalse(admin.listTables().contains(tableId));
    admin.createTable(createTableRequest);
    assertTrue(admin.listTables().contains(tableId));
  }
}
