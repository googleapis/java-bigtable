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

import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_ERROR_MODE;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_INSERT_MODE;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_MAX_BATCH_SIZE;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_TABLE_NAME_FORMAT;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_VALUE_NULL_MODE;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.kafka.connect.bigtable.util.BasicPropertiesFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.config.ConfigException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BigtableSinkConfigTest {
  public static boolean configIsValid(BigtableSinkConfig config) {
    return BigtableSinkConfig.validate(config.originalsStrings(), false).configValues().stream()
        .allMatch(v -> v.errorMessages().isEmpty());
  }

  @Test
  public void testBasicSuccess() {
    BigtableSinkConfig config = new BigtableSinkConfig(BasicPropertiesFactory.getSinkProps());
    assertTrue(configIsValid(config));
  }

  @Test
  public void testBasicValidationFailure() {
    assertThrows(ConfigException.class, () -> new BigtableSinkConfig(new HashMap<>()));
    for (String configName :
        List.of(
            CONFIG_TABLE_NAME_FORMAT,
            CONFIG_AUTO_CREATE_TABLES,
            CONFIG_AUTO_CREATE_COLUMN_FAMILIES)) {
      Map<String, String> props = BasicPropertiesFactory.getSinkProps();
      props.put(configName, null);
      assertThrows(ConfigException.class, () -> new BigtableSinkConfig(new HashMap<>()));
    }
    for (String configName :
        List.of(CONFIG_INSERT_MODE, CONFIG_VALUE_NULL_MODE, CONFIG_ERROR_MODE)) {
      Map<String, String> props = BasicPropertiesFactory.getSinkProps();
      props.put(configName, "invalid");
      assertThrows(ConfigException.class, () -> new BigtableSinkConfig(new HashMap<>()));
    }
  }

  @Test
  public void testDefaults() {
    BigtableSinkConfig config = new BigtableSinkConfig(BasicPropertiesFactory.getSinkProps());
    assertEquals(config.getString(CONFIG_INSERT_MODE), InsertMode.INSERT.name());
    assertEquals((long) config.getInt(CONFIG_MAX_BATCH_SIZE), 1);
    assertEquals(config.getString(CONFIG_VALUE_NULL_MODE), NullValueMode.WRITE.name());
  }

  @Test
  public void testMultipleValuesValidationInsert() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    props.put(BigtableSinkConfig.CONFIG_INSERT_MODE, InsertMode.INSERT.name());
    props.put(BigtableSinkConfig.CONFIG_MAX_BATCH_SIZE, "2");
    BigtableSinkConfig config = new BigtableSinkConfig(props);
    assertFalse(configIsValid(config));
  }

  @Test
  public void testMultipleValuesValidationCredentials() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    props.put(BigtableSinkConfig.CONFIG_GCP_CREDENTIALS_JSON, "nonempty");
    props.put(BigtableSinkConfig.CONFIG_GCP_CREDENTIALS_PATH, "nonempty");
    BigtableSinkConfig config = new BigtableSinkConfig(props);
    assertFalse(configIsValid(config));
  }

  @Test
  public void testGetBigtableDataClient() {
    BigtableSinkConfig config = new BigtableSinkConfig(BasicPropertiesFactory.getSinkProps());
    config.getBigtableDataClient();
  }

  @Test
  public void testGetBigtableAdminClient() {
    BigtableSinkConfig config = new BigtableSinkConfig(BasicPropertiesFactory.getSinkProps());
    config.getBigtableAdminClient();
  }

  @Test
  public void testEnumCaseInsensitivity() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    props.put(CONFIG_INSERT_MODE, "uPsErT");
    props.put(CONFIG_ERROR_MODE, "IGNORE");
    props.put(CONFIG_VALUE_NULL_MODE, "delete");
    BigtableSinkConfig config = new BigtableSinkConfig(props);
  }

  @Test
  public void testIsBigtableConfigurationValidBasicSuccess() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    BigtableSinkConfig config = spy(new BigtableSinkConfig(props));
    BigtableTableAdminClient bigtable = mock(BigtableTableAdminClient.class);
    doReturn(emptyList()).when(bigtable).listTables();
    doReturn(bigtable).when(config).getBigtableAdminClient(any());
    assertTrue(config.isBigtableConfigurationValid());
    verify(bigtable, times(1)).close();
  }

  @Test
  public void testIsBigtableConfigurationValidClientConstructorError() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    BigtableSinkConfig config = spy(new BigtableSinkConfig(props));
    doThrow(new RuntimeException()).when(config).getBigtableAdminClient();
    assertFalse(config.isBigtableConfigurationValid());
  }

  @Test
  public void testIsBigtableConfigurationValidOperationError() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    BigtableSinkConfig config = spy(new BigtableSinkConfig(props));
    BigtableTableAdminClient bigtable = mock(BigtableTableAdminClient.class);
    doThrow(new RuntimeException()).when(bigtable).listTables();
    doReturn(bigtable).when(config).getBigtableAdminClient(any());
    assertFalse(config.isBigtableConfigurationValid());
    verify(bigtable, times(1)).close();
  }
}
