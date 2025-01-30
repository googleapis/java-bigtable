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

import com.google.cloud.kafka.connect.bigtable.util.SchemaRegistryTestUtil;
import org.junit.After;
import org.junit.Before;

public abstract class BaseKafkaConnectBigtableSchemaRegistryIT extends BaseKafkaConnectBigtableIT {
  protected SchemaRegistryTestUtil schemaRegistry;

  @Before
  public void setUpSchemaRegistry() throws Exception {
    schemaRegistry = new SchemaRegistryTestUtil(connect.kafka().bootstrapServers());
    schemaRegistry.start();
  }

  @After
  public void tearDownSchemaRegistry() throws Exception {
    if (schemaRegistry != null) {
      schemaRegistry.stop();
    }
  }
}
