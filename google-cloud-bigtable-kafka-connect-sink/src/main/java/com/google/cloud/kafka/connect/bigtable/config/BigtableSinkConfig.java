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

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.admin.v2.stub.BigtableTableAdminStubSettings;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.common.annotations.VisibleForTesting;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.Config;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.ConfigValue;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.errors.RetriableException;
import org.threeten.bp.Duration;
import org.threeten.bp.temporal.ChronoUnit;

/**
 * A class defining the configuration of {@link
 * com.google.cloud.kafka.connect.bigtable.BigtableSinkConnector}.
 *
 * <p>It's responsible for the validation and parsing of the user-provided values.
 */
public class BigtableSinkConfig extends AbstractConfig {
  public static final String CONFIG_GCP_PROJECT_ID = "gcp.bigtable.project.id";
  public static final String CONFIG_GCP_CREDENTIALS_PATH = "gcp.bigtable.credentials.path";
  public static final String CONFIG_GCP_CREDENTIALS_JSON = "gcp.bigtable.credentials.json";
  public static final String CONFIG_BIGTABLE_INSTANCE_ID = "gcp.bigtable.instance.id";
  public static final String CONFIG_BIGTABLE_APP_PROFILE_ID = "gcp.bigtable.app.profile.id";
  public static final String CONFIG_INSERT_MODE = "insert.mode";
  public static final String CONFIG_MAX_BATCH_SIZE = "max.batch.size";
  public static final String CONFIG_VALUE_NULL_MODE = "value.null.mode";
  public static final String CONFIG_ERROR_MODE = "error.mode";
  public static final String CONFIG_TABLE_NAME_FORMAT = "table.name.format";
  public static final String CONFIG_ROW_KEY_DEFINITION = "row.key.definition";
  public static final String CONFIG_ROW_KEY_DELIMITER = "row.key.delimiter";
  public static final String CONFIG_AUTO_CREATE_TABLES = "auto.create.tables";
  public static final String CONFIG_AUTO_CREATE_COLUMN_FAMILIES = "auto.create.column.families";
  public static final String CONFIG_DEFAULT_COLUMN_FAMILY = "default.column.family";
  public static final String CONFIG_DEFAULT_COLUMN_QUALIFIER = "default.column.qualifier";
  public static final String CONFIG_RETRY_TIMEOUT_MILLIS = "retry.timeout.ms";
  private static final InsertMode DEFAULT_INSERT_MODE = InsertMode.INSERT;
  private static final NullValueMode DEFAULT_NULL_VALUE_MODE = NullValueMode.WRITE;
  private static final BigtableErrorMode DEFAULT_ERROR_MODE = BigtableErrorMode.FAIL;
  private static final Integer DEFAULT_MAX_BATCH_SIZE = 1;
  private static final List<String> BIGTABLE_CONFIGURATION_PROPERTIES =
      List.of(
          CONFIG_GCP_CREDENTIALS_JSON,
          CONFIG_GCP_CREDENTIALS_PATH,
          CONFIG_GCP_PROJECT_ID,
          CONFIG_BIGTABLE_INSTANCE_ID,
          CONFIG_BIGTABLE_APP_PROFILE_ID);
  private static final int BIGTABLE_CREDENTIALS_CHECK_TIMEOUT_SECONDS = 2;

  protected BigtableSinkConfig(ConfigDef definition, Map<String, String> properties) {
    super(definition, properties);
  }

  /**
   * The main constructor.
   *
   * @param properties The properties provided by the user.
   */
  public BigtableSinkConfig(Map<String, String> properties) {
    this(getDefinition(), properties);
  }

  /**
   * Validates that a valid {@link BigtableSinkConfig} can be created using the input properties.
   *
   * @param props The properties provided by the user.
   * @return {@link Config} containing validation results.
   */
  public static Config validate(Map<String, String> props) {
    return validate(props, true);
  }

  /**
   * Validates that a valid {@link BigtableSinkConfig} can be created using the input properties.
   *
   * @param props The properties provided by the user.
   * @param accessBigtableToValidateConfiguration If set to true, validation includes checking
   *     whether the Cloud Bigtable configuration is valid by connecting to Cloud Bigtable and
   *     attempting to execute a simple read-only operation.
   * @return {@link Config} containing validation results.
   */
  @VisibleForTesting
  static Config validate(Map<String, String> props, boolean accessBigtableToValidateConfiguration) {
    // Note that we only need to verify the properties we define, the generic Sink configuration is
    // handled in SinkConnectorConfig::validate().
    String credentialsPath = props.get(CONFIG_GCP_CREDENTIALS_PATH);
    String credentialsJson = props.get(CONFIG_GCP_CREDENTIALS_JSON);
    String insertMode = props.get(CONFIG_INSERT_MODE);
    String nullValueMode = props.get(CONFIG_VALUE_NULL_MODE);
    String maxBatchSize = props.get(CONFIG_MAX_BATCH_SIZE);
    String effectiveInsertMode =
        Optional.ofNullable(insertMode).orElse(DEFAULT_INSERT_MODE.name()).toUpperCase();
    String effectiveNullValueMode =
        Optional.ofNullable(nullValueMode).orElse(DEFAULT_NULL_VALUE_MODE.name()).toUpperCase();
    String effectiveMaxBatchSize =
        Optional.ofNullable(maxBatchSize).orElse(DEFAULT_MAX_BATCH_SIZE.toString()).trim();

    Map<String, ConfigValue> validationResult = getDefinition().validateAll(props);
    if (!Utils.isBlank(credentialsPath) && !Utils.isBlank(credentialsJson)) {
      String errorMessage =
          CONFIG_GCP_CREDENTIALS_JSON
              + " and "
              + CONFIG_GCP_CREDENTIALS_PATH
              + " are mutually exclusive options, but both are set.";
      addErrorMessage(validationResult, CONFIG_GCP_CREDENTIALS_JSON, credentialsJson, errorMessage);
      addErrorMessage(validationResult, CONFIG_GCP_CREDENTIALS_PATH, credentialsPath, errorMessage);
    }
    if (effectiveInsertMode.equals(InsertMode.INSERT.name())
        && !effectiveMaxBatchSize.equals("1")) {
      String errorMessage =
          "When using `"
              + CONFIG_INSERT_MODE
              + "` of `"
              + InsertMode.INSERT.name()
              + "`, "
              + CONFIG_MAX_BATCH_SIZE
              + " must be set to `1`.";
      addErrorMessage(validationResult, CONFIG_INSERT_MODE, insertMode, errorMessage);
      addErrorMessage(validationResult, CONFIG_MAX_BATCH_SIZE, maxBatchSize, errorMessage);
    }
    if (effectiveInsertMode.equals(InsertMode.INSERT.name())
        && effectiveNullValueMode.equals(NullValueMode.DELETE.name())) {
      String errorMessage =
          "When using `"
              + CONFIG_VALUE_NULL_MODE
              + "` of `"
              + NullValueMode.DELETE.name()
              + "`, "
              + CONFIG_INSERT_MODE
              + " must not be set to `"
              + InsertMode.INSERT.name()
              + "`.";
      addErrorMessage(validationResult, CONFIG_INSERT_MODE, insertMode, errorMessage);
      addErrorMessage(validationResult, CONFIG_VALUE_NULL_MODE, nullValueMode, errorMessage);
    }

    if (accessBigtableToValidateConfiguration
        && validationResult.values().stream().allMatch(v -> v.errorMessages().isEmpty())) {
      // We validate the user's credentials in order to warn them early rather than fill DLQ
      // with records whose processing would fail due to invalid credentials.
      // We only call it after validating that all other parameters are fine since creating
      // a Cloud Bigtable client uses many of these parameters, and we don't want to warn
      // the user unnecessarily.
      BigtableSinkConfig config = new BigtableSinkConfig(props);
      if (!config.isBigtableConfigurationValid()) {
        String errorMessage = "Cloud Bigtable configuration is invalid.";
        for (String bigtableProp : BIGTABLE_CONFIGURATION_PROPERTIES) {
          addErrorMessage(validationResult, bigtableProp, props.get(bigtableProp), errorMessage);
        }
      }
    }
    return new Config(new ArrayList<>(validationResult.values()));
  }

  /**
   * @return {@link ConfigDef} used by Kafka Connect to advertise configuration options to the user
   *     and by us to perform basic validation of the user-provided values.
   */
  public static ConfigDef getDefinition() {
    return new ConfigDef()
        .define(
            CONFIG_GCP_PROJECT_ID,
            ConfigDef.Type.STRING,
            ConfigDef.NO_DEFAULT_VALUE,
            ConfigDef.CompositeValidator.of(
                new ConfigDef.NonNullValidator(), new ConfigDef.NonEmptyString()),
            ConfigDef.Importance.HIGH,
            "The ID of the GCP project.")
        .define(
            CONFIG_BIGTABLE_INSTANCE_ID,
            ConfigDef.Type.STRING,
            ConfigDef.NO_DEFAULT_VALUE,
            ConfigDef.CompositeValidator.of(
                new ConfigDef.NonNullValidator(), new ConfigDef.NonEmptyString()),
            ConfigDef.Importance.HIGH,
            "The ID of the Cloud Bigtable instance.")
        .define(
            CONFIG_BIGTABLE_APP_PROFILE_ID,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.MEDIUM,
            "The application profile that the connector should use. If none is supplied,"
                + " the default app profile will be used.")
        .define(
            CONFIG_GCP_CREDENTIALS_PATH,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.HIGH,
            "The path to the JSON service key file. Configure at most one of `"
                + CONFIG_GCP_CREDENTIALS_PATH
                + "` and `"
                + CONFIG_GCP_CREDENTIALS_JSON
                + "`. If neither is provided, Application Default Credentials will be used.")
        .define(
            CONFIG_GCP_CREDENTIALS_JSON,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.HIGH,
            "The path to the JSON service key file. Configure at most one of `"
                + CONFIG_GCP_CREDENTIALS_PATH
                + "` and `"
                + CONFIG_GCP_CREDENTIALS_JSON
                + "`. If neither is provided, Application Default Credentials will be used.")
        .define(
            CONFIG_INSERT_MODE,
            ConfigDef.Type.STRING,
            DEFAULT_INSERT_MODE.name(),
            enumValidator(InsertMode.values()),
            ConfigDef.Importance.HIGH,
            "Defines the insertion mode to use. Supported modes are:"
                + "\n- insert - Insert new record only."
                + " If the row to be written already exists in the table, an error is thrown."
                + "\n- upsert - If the row to be written already exists,"
                + " then its column values are overwritten with the ones provided.")
        .define(
            CONFIG_MAX_BATCH_SIZE,
            ConfigDef.Type.INT,
            DEFAULT_MAX_BATCH_SIZE,
            ConfigDef.Range.atLeast(1),
            ConfigDef.Importance.MEDIUM,
            "The maximum number of records that can be batched into a batch of upserts."
                + " Note that since only a batch size of 1 for inserts is supported, `"
                + CONFIG_MAX_BATCH_SIZE
                + "` must be exactly `1` when `"
                + CONFIG_INSERT_MODE
                + "` is set to `INSERT`.")
        .define(
            CONFIG_VALUE_NULL_MODE,
            ConfigDef.Type.STRING,
            DEFAULT_NULL_VALUE_MODE.name(),
            enumValidator(NullValueMode.values()),
            ConfigDef.Importance.MEDIUM,
            "Defines what to do with `null`s within Kafka values. Supported modes are:"
                + "\n- write - Serialize `null`s to empty byte arrays."
                + "\n- ignore - Ignore `null`s."
                + "\n- delete - Use them to issue DELETE commands. Root-level `null` deletes a"
                + " row. `null` nested one level deletes a column family named after the"
                + " `null`-valued field. `null` nested two levels deletes a column named after the"
                + " `null`-valued field in column family named after the `null-valued` field parent"
                + " field. `null` values nested more than two levels are serialized like other"
                + " values and don't result in any DELETE commands.")
        .define(
            CONFIG_ERROR_MODE,
            ConfigDef.Type.STRING,
            DEFAULT_ERROR_MODE.name(),
            enumValidator(BigtableErrorMode.values()),
            ConfigDef.Importance.MEDIUM,
            "Specifies how to handle errors that result from writes, after retries. It is ignored"
                + " if DLQ is configured. Supported modes are:"
                + "\n- fail - The connector fails and must be manually restarted."
                + "\n- warn - The connector logs a warning and continues operating normally."
                + "\n- ignore - The connector does not log a warning but continues operating"
                + " normally.")
        .define(
            CONFIG_TABLE_NAME_FORMAT,
            ConfigDef.Type.STRING,
            ConfigInterpolation.TOPIC_PLACEHOLDER,
            ConfigDef.CompositeValidator.of(
                new ConfigDef.NonNullValidator(), new ConfigDef.NonEmptyString()),
            ConfigDef.Importance.MEDIUM,
            "Name of the destination table. Use `"
                + ConfigInterpolation.TOPIC_PLACEHOLDER
                + "` within the table name to specify the originating topic name.\n"
                + "For example, `user_"
                + ConfigInterpolation.TOPIC_PLACEHOLDER
                + "` for the topic `stats` will map to the table name `user_stats`.")
        .define(
            CONFIG_ROW_KEY_DEFINITION,
            ConfigDef.Type.LIST,
            "",
            ConfigDef.Importance.MEDIUM,
            "A comma separated list of Kafka Record key field names that specifies the order of"
                + " Kafka key fields to be concatenated to form the row key."
                + "\nFor example the list: `username, post_id, time_stamp` when applied to a Kafka"
                + " key: `{'username': 'bob','post_id': '213', 'time_stamp': '123123'}` and with"
                + " delimiter `#` gives the row key `bob#213#123123`. You can also access terms"
                + " nested in the key by using `.` as a delimiter. If this configuration is empty"
                + " or unspecified and the Kafka Message Key is a"
                + "\n- struct, all the fields in the struct are used to construct the row key."
                + "\n- byte array, the row key is set to the byte array as is."
                + "\n- primitive, the row key is set to the primitive stringified."
                + "If prefixes, more complicated delimiters, and string constants are required in"
                + " your Row Key, consider configuring an SMT to add relevant fields to the Kafka"
                + " Record key.")
        .define(
            CONFIG_ROW_KEY_DELIMITER,
            ConfigDef.Type.STRING,
            "",
            ConfigDef.Importance.LOW,
            "The delimiter used in concatenating Kafka key fields in the row key. If this"
                + " configuration is empty or unspecified, the key fields will be concatenated"
                + " together directly.")
        .define(
            CONFIG_AUTO_CREATE_TABLES,
            ConfigDef.Type.BOOLEAN,
            false,
            new ConfigDef.NonNullValidator(),
            ConfigDef.Importance.MEDIUM,
            "Whether to automatically create the destination table if it is found to be missing."
                + "\nWhen enabled, the records for which the auto-creation fails, are failed."
                + "\nRecreation of tables deleted by other Cloud Bigtable users is not supported.")
        .define(
            CONFIG_AUTO_CREATE_COLUMN_FAMILIES,
            ConfigDef.Type.BOOLEAN,
            false,
            new ConfigDef.NonNullValidator(),
            ConfigDef.Importance.MEDIUM,
            "Whether to automatically create missing columns families in the table relative to the"
                + " record schema.\n"
                + "Does not imply auto-creation of tables.\n"
                + "When enabled, the records for which the auto-creation fails, are failed.\n"
                + "When enabled, column families will be created also for deletions of nonexistent"
                + " column families and cells within them.\n"
                + "Recreation of column families deleted by other Cloud Bigtable users is not"
                + " supported.")
        .define(
            CONFIG_DEFAULT_COLUMN_FAMILY,
            ConfigDef.Type.STRING,
            ConfigInterpolation.TOPIC_PLACEHOLDER,
            ConfigDef.Importance.MEDIUM,
            "Any root-level fields on the SinkRecord that aren't objects will be added to this"
                + " column family. If empty, the fields will be ignored. Use `"
                + ConfigInterpolation.TOPIC_PLACEHOLDER
                + "` within the column family name to specify the originating topic name.")
        .define(
            CONFIG_DEFAULT_COLUMN_QUALIFIER,
            ConfigDef.Type.STRING,
            "KAFKA_VALUE",
            ConfigDef.Importance.MEDIUM,
            "Any root-level values on the SinkRecord that aren't objects will be added to this"
                + " column within default column family. If empty, the value will be ignored.")
        .define(
            CONFIG_RETRY_TIMEOUT_MILLIS,
            ConfigDef.Type.LONG,
            90000,
            ConfigDef.Range.atLeast(0),
            ConfigDef.Importance.MEDIUM,
            "Maximum time in milliseconds allocated for retrying database operations before trying"
                + " other error handling mechanisms.");
  }

  /**
   * Adds a validation error in the format expected by {@link BigtableSinkConfig#validate(Map)}.
   *
   * @param validatedConfig Input/output parameter containing current validation result.
   * @param name Configuration parameter name.
   * @param value Configuration parameter value.
   * @param errorMessage Error message to be added.
   */
  private static void addErrorMessage(
      Map<String, ConfigValue> validatedConfig, String name, String value, String errorMessage) {
    validatedConfig
        .computeIfAbsent(
            name, p -> new ConfigValue(name, value, Collections.emptyList(), new ArrayList<>()))
        .addErrorMessage(errorMessage);
  }

  public NullValueMode getNullValueMode() {
    return getEnum(CONFIG_VALUE_NULL_MODE, NullValueMode::valueOf);
  }

  public BigtableErrorMode getBigtableErrorMode() {
    return getEnum(CONFIG_ERROR_MODE, BigtableErrorMode::valueOf);
  }

  public InsertMode getInsertMode() {
    return getEnum(CONFIG_INSERT_MODE, InsertMode::valueOf);
  }

  /**
   * @return {@link BigtableTableAdminClient} connected to a Cloud Bigtable instance configured as
   *     described in {@link BigtableSinkConfig#getDefinition()}.
   */
  public BigtableTableAdminClient getBigtableAdminClient() {
    RetrySettings retrySettings = getRetrySettings();
    return getBigtableAdminClient(retrySettings);
  }

  @VisibleForTesting
  BigtableTableAdminClient getBigtableAdminClient(RetrySettings retrySettings) {
    Optional<CredentialsProvider> credentialsProvider =
        getUserConfiguredBigtableCredentialsProvider();

    BigtableTableAdminSettings.Builder adminSettingsBuilder =
        BigtableTableAdminSettings.newBuilder()
            .setProjectId(getString(BigtableSinkTaskConfig.CONFIG_GCP_PROJECT_ID))
            .setInstanceId(getString(BigtableSinkTaskConfig.CONFIG_BIGTABLE_INSTANCE_ID));
    if (credentialsProvider.isPresent()) {
      adminSettingsBuilder.setCredentialsProvider(credentialsProvider.get());
    } else {
      // Use the default credential provider that utilizes Application Default Credentials.
    }

    BigtableTableAdminStubSettings.Builder adminStubSettings = adminSettingsBuilder.stubSettings();
    adminStubSettings.createTableSettings().setRetrySettings(retrySettings);
    adminStubSettings.modifyColumnFamiliesSettings().setRetrySettings(retrySettings);
    adminStubSettings.listTablesSettings().setRetrySettings(retrySettings);
    adminStubSettings.getTableSettings().setRetrySettings(retrySettings);
    try {
      return BigtableTableAdminClient.create(adminSettingsBuilder.build());
    } catch (IOException e) {
      throw new RetriableException(e);
    }
  }

  /**
   * @return {@link BigtableDataClient} connected to Cloud Bigtable instance configured as described
   *     in {@link BigtableSinkConfig#getDefinition()}.
   */
  public BigtableDataClient getBigtableDataClient() {
    RetrySettings retrySettings = getRetrySettings();
    Optional<CredentialsProvider> credentialsProvider =
        getUserConfiguredBigtableCredentialsProvider();

    BigtableDataSettings.Builder dataSettingsBuilder =
        BigtableDataSettings.newBuilder()
            .setProjectId(getString(BigtableSinkTaskConfig.CONFIG_GCP_PROJECT_ID))
            .setInstanceId(getString(BigtableSinkTaskConfig.CONFIG_BIGTABLE_INSTANCE_ID));
    if (credentialsProvider.isPresent()) {
      dataSettingsBuilder.setCredentialsProvider(credentialsProvider.get());
    } else {
      // Use the default credential provider that utilizes Application Default Credentials.
    }
    String appProfileId = getString(BigtableSinkTaskConfig.CONFIG_BIGTABLE_APP_PROFILE_ID);
    if (appProfileId == null) {
      dataSettingsBuilder.setDefaultAppProfileId();
    } else {
      dataSettingsBuilder.setAppProfileId(appProfileId);
    }

    EnhancedBigtableStubSettings.Builder dataStubSettings = dataSettingsBuilder.stubSettings();
    dataStubSettings.mutateRowSettings().setRetrySettings(retrySettings);
    dataStubSettings.bulkMutateRowsSettings().setRetrySettings(retrySettings);
    dataStubSettings.readRowSettings().setRetrySettings(retrySettings);
    dataStubSettings.readRowsSettings().setRetrySettings(retrySettings);

    try {
      return BigtableDataClient.create(dataSettingsBuilder.build());
    } catch (IOException e) {
      throw new RetriableException(e);
    }
  }

  /**
   * Checks whether Cloud Bigtable configuration is valid by connecting to Cloud Bigtable and
   * attempting to execute a simple read-only operation.
   *
   * @return true if Cloud Bigtable configuration is valid, false otherwise.
   */
  @VisibleForTesting
  boolean isBigtableConfigurationValid() {
    BigtableTableAdminClient bigtable = null;
    try {
      RetrySettings retrySettings =
          RetrySettings.newBuilder()
              .setMaxAttempts(0)
              .setTotalTimeout(
                  Duration.of(BIGTABLE_CREDENTIALS_CHECK_TIMEOUT_SECONDS, ChronoUnit.SECONDS))
              .build();
      bigtable = getBigtableAdminClient(retrySettings);
      bigtable.listTables();
      return true;
    } catch (Throwable t) {
      return false;
    } finally {
      if (bigtable != null) {
        bigtable.close();
      }
    }
  }

  /**
   * @return {@link RetrySettings} of Cloud Bigtable clients configured as described in {@link
   *     BigtableSinkConfig#getDefinition()}.
   */
  protected RetrySettings getRetrySettings() {
    return RetrySettings.newBuilder()
        .setTotalTimeout(
            Duration.of(
                getLong(BigtableSinkTaskConfig.CONFIG_RETRY_TIMEOUT_MILLIS), ChronoUnit.MILLIS))
        .build();
  }

  /**
   * Extracts typed enum value from this object.
   *
   * @param configName Enum parameter name in {@link BigtableSinkConfig}.
   * @param converter Function that parses parameter value into an enum value. It's assumed to throw
   *     only {@link NullPointerException} and {@link IllegalArgumentException}.
   * @return Parsed enum value.
   * @param <T> Enum type.
   */
  private <T> T getEnum(String configName, Function<String, T> converter) {
    String s = this.getString(configName);
    try {
      return converter.apply(s.toUpperCase());
    } catch (NullPointerException | IllegalArgumentException e) {
      throw new ConfigException(configName, s);
    }
  }

  private static ConfigDef.Validator enumValidator(Enum<?>[] enumValues) {
    return ConfigDef.CaseInsensitiveValidString.in(
        Arrays.stream(enumValues).map(Enum::name).toArray(String[]::new));
  }

  /**
   * @return {@link Optional#empty()} if the user didn't configure the Cloud Bigtable credentials,
   *     {@link Optional} containing {@link CredentialsProvider} configured as described in {@link
   *     BigtableSinkConfig#getDefinition()} otherwise.
   */
  protected Optional<CredentialsProvider> getUserConfiguredBigtableCredentialsProvider() {
    String credentialsJson = getString(BigtableSinkTaskConfig.CONFIG_GCP_CREDENTIALS_JSON);
    String credentialsPath = getString(BigtableSinkTaskConfig.CONFIG_GCP_CREDENTIALS_PATH);
    byte[] credentials;
    if (!Utils.isBlank(credentialsJson)) {
      credentials = credentialsJson.getBytes(StandardCharsets.UTF_8);
    } else if (!Utils.isBlank(credentialsPath)) {
      try (FileInputStream is = new FileInputStream(credentialsPath)) {
        credentials = is.readAllBytes();
      } catch (IOException e) {
        throw new ConfigException(
            String.format("Error getting credentials from file: %s.", credentialsPath));
      }
    } else {
      // We will use the default CredentialsProvider, which doesn't need any application-level
      // configuration.
      return Optional.empty();
    }
    try {
      return Optional.of(
          FixedCredentialsProvider.create(
              GoogleCredentials.fromStream(new ByteArrayInputStream(credentials))));
    } catch (IOException e) {
      throw new ConfigException("Cloud Bigtable credentials creation failed.");
    }
  }
}
