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

import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.AUTO_CREATE_COLUMN_FAMILIES_CONFIG;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.AUTO_CREATE_TABLES_CONFIG;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.ERROR_MODE_CONFIG;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.INSERT_MODE_CONFIG;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.MAX_BATCH_SIZE_CONFIG;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.TABLE_NAME_FORMAT_CONFIG;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.VALUE_NULL_MODE_CONFIG;
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
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
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
            TABLE_NAME_FORMAT_CONFIG,
            AUTO_CREATE_TABLES_CONFIG,
            AUTO_CREATE_COLUMN_FAMILIES_CONFIG)) {
      Map<String, String> props = BasicPropertiesFactory.getSinkProps();
      props.put(configName, null);
      assertThrows(ConfigException.class, () -> new BigtableSinkConfig(new HashMap<>()));
    }
    for (String configName :
        List.of(INSERT_MODE_CONFIG, VALUE_NULL_MODE_CONFIG, ERROR_MODE_CONFIG)) {
      Map<String, String> props = BasicPropertiesFactory.getSinkProps();
      props.put(configName, "invalid");
      assertThrows(ConfigException.class, () -> new BigtableSinkConfig(new HashMap<>()));
    }
  }

  @Test
  public void testDefaults() {
    BigtableSinkConfig config = new BigtableSinkConfig(BasicPropertiesFactory.getSinkProps());
    assertEquals(config.getString(INSERT_MODE_CONFIG), InsertMode.INSERT.name());
    assertEquals((long) config.getInt(MAX_BATCH_SIZE_CONFIG), 1);
    assertEquals(config.getString(VALUE_NULL_MODE_CONFIG), NullValueMode.WRITE.name());
  }

  @Test
  public void testInsertModeOnlyAllowsMaxBatchSizeOf1() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.INSERT.name());
    props.put(BigtableSinkConfig.MAX_BATCH_SIZE_CONFIG, "2");
    BigtableSinkConfig config = new BigtableSinkConfig(props);
    assertFalse(configIsValid(config));
  }

  @Test
  public void testMultipleCredentialsAreDisallowed() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    props.put(BigtableSinkConfig.GCP_CREDENTIALS_JSON_CONFIG, "nonempty");
    props.put(BigtableSinkConfig.GCP_CREDENTIALS_PATH_CONFIG, "nonempty");
    BigtableSinkConfig config = new BigtableSinkConfig(props);
    assertFalse(configIsValid(config));
  }

  @Test
  public void testNullDeletionIsIncompatibleWithInsertMode() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    props.put(BigtableSinkConfig.INSERT_MODE_CONFIG, InsertMode.INSERT.name());
    props.put(VALUE_NULL_MODE_CONFIG, NullValueMode.DELETE.name());
    BigtableSinkConfig config = new BigtableSinkConfig(props);
    assertFalse(configIsValid(config));
  }

  @Test
  public void testGetBigtableDataClient() {
    BigtableSinkConfig config = new BigtableSinkConfig(BasicPropertiesFactory.getSinkProps());
    BigtableDataClient client = config.getBigtableDataClient();
    client.close();
  }

  @Test
  public void testGetBigtableAdminClient() {
    BigtableSinkConfig config = new BigtableSinkConfig(BasicPropertiesFactory.getSinkProps());
    BigtableTableAdminClient client = config.getBigtableAdminClient();
    client.close();
  }

  @Test
  public void testEnumCaseInsensitivity() {
    Map<String, String> props = BasicPropertiesFactory.getSinkProps();
    props.put(INSERT_MODE_CONFIG, "uPsErT");
    props.put(ERROR_MODE_CONFIG, "IGNORE");
    props.put(VALUE_NULL_MODE_CONFIG, "delete");
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
