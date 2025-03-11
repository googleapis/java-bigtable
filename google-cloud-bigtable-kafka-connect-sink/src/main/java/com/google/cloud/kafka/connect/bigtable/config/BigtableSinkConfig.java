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
import com.google.api.gax.rpc.StatusCode;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.admin.v2.stub.BigtableTableAdminStubSettings;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
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
import java.util.Set;
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
  public static final String GCP_PROJECT_ID_CONFIG = "gcp.bigtable.project.id";
  public static final String GCP_CREDENTIALS_PATH_CONFIG = "gcp.bigtable.credentials.path";
  public static final String GCP_CREDENTIALS_JSON_CONFIG = "gcp.bigtable.credentials.json";
  public static final String BIGTABLE_INSTANCE_ID_CONFIG = "gcp.bigtable.instance.id";
  public static final String BIGTABLE_APP_PROFILE_ID_CONFIG = "gcp.bigtable.app.profile.id";
  public static final String INSERT_MODE_CONFIG = "insert.mode";
  public static final String MAX_BATCH_SIZE_CONFIG = "max.batch.size";
  public static final String VALUE_NULL_MODE_CONFIG = "value.null.mode";
  public static final String ERROR_MODE_CONFIG = "error.mode";
  public static final String TABLE_NAME_FORMAT_CONFIG = "table.name.format";
  public static final String ROW_KEY_DEFINITION_CONFIG = "row.key.definition";
  public static final String ROW_KEY_DELIMITER_CONFIG = "row.key.delimiter";
  public static final String AUTO_CREATE_TABLES_CONFIG = "auto.create.tables";
  public static final String AUTO_CREATE_COLUMN_FAMILIES_CONFIG = "auto.create.column.families";
  public static final String DEFAULT_COLUMN_FAMILY_CONFIG = "default.column.family";
  public static final String DEFAULT_COLUMN_QUALIFIER_CONFIG = "default.column.qualifier";
  public static final String RETRY_TIMEOUT_MILLIS_CONFIG = "retry.timeout.ms";
  private static final List<String> BIGTABLE_CONFIGURATION_PROPERTIES =
      List.of(
          GCP_CREDENTIALS_JSON_CONFIG,
          GCP_CREDENTIALS_PATH_CONFIG,
          GCP_PROJECT_ID_CONFIG,
          BIGTABLE_INSTANCE_ID_CONFIG,
          BIGTABLE_APP_PROFILE_ID_CONFIG);
  private static final Duration BIGTABLE_CREDENTIALS_CHECK_TIMEOUT =
      Duration.of(2, ChronoUnit.SECONDS);
  private static final Duration BIGTABLE_ADMIN_API_WRITE_RETRY_INITIAL_DELAY =
      Duration.of(100, ChronoUnit.MILLIS);
  private static final double BIGTABLE_CLIENT_RETRY_DELAY_MULTIPLIER = 1.5;

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
    // We create it without validation to use the same getters as the config users.
    Map<String, ConfigValue> validationResult = getDefinition().validateAll(props);

    if (validationResult.values().stream().allMatch(v -> v.errorMessages().isEmpty())) {
      BigtableSinkConfig config = new BigtableSinkConfig(props);

      // Note that we only need to verify the properties we define, the generic Sink configuration
      // is handled in SinkConnectorConfig::validate().
      String credentialsPath = config.getString(GCP_CREDENTIALS_PATH_CONFIG);
      String credentialsJson = config.getString(GCP_CREDENTIALS_JSON_CONFIG);
      String insertMode = config.getString(INSERT_MODE_CONFIG);
      String nullValueMode = config.getString(VALUE_NULL_MODE_CONFIG);
      Integer maxBatchSize = config.getInt(MAX_BATCH_SIZE_CONFIG);
      Boolean autoCreateTables = config.getBoolean(AUTO_CREATE_TABLES_CONFIG);
      Boolean autoCreateColumnFamilies = config.getBoolean(AUTO_CREATE_COLUMN_FAMILIES_CONFIG);

      if (!Utils.isBlank(credentialsPath) && !Utils.isBlank(credentialsJson)) {
        String errorMessage =
            GCP_CREDENTIALS_JSON_CONFIG
                + " and "
                + GCP_CREDENTIALS_PATH_CONFIG
                + " are mutually exclusive options, but both are set.";
        addErrorMessage(
            validationResult, GCP_CREDENTIALS_JSON_CONFIG, credentialsJson, errorMessage);
        addErrorMessage(
            validationResult, GCP_CREDENTIALS_PATH_CONFIG, credentialsPath, errorMessage);
      }
      if (InsertMode.INSERT.name().equals(insertMode) && !Integer.valueOf(1).equals(maxBatchSize)) {
        String errorMessage =
            "When using `"
                + INSERT_MODE_CONFIG
                + "` of `"
                + InsertMode.INSERT.name()
                + "`, "
                + MAX_BATCH_SIZE_CONFIG
                + " must be set to `1`.";
        addErrorMessage(validationResult, INSERT_MODE_CONFIG, insertMode, errorMessage);
        addErrorMessage(
            validationResult, MAX_BATCH_SIZE_CONFIG, String.valueOf(maxBatchSize), errorMessage);
      }
      if (InsertMode.INSERT.name().equals(insertMode)
          && NullValueMode.DELETE.name().equals(nullValueMode)) {
        String errorMessage =
            "When using `"
                + VALUE_NULL_MODE_CONFIG
                + "` of `"
                + NullValueMode.DELETE.name()
                + "`, "
                + INSERT_MODE_CONFIG
                + " must not be set to `"
                + InsertMode.INSERT.name()
                + "`.";
        addErrorMessage(validationResult, INSERT_MODE_CONFIG, insertMode, errorMessage);
        addErrorMessage(validationResult, VALUE_NULL_MODE_CONFIG, nullValueMode, errorMessage);
      }
      if (Boolean.TRUE.equals(autoCreateTables) && Boolean.FALSE.equals(autoCreateColumnFamilies)) {
        String errorMessage =
            "If you enable `"
                + AUTO_CREATE_TABLES_CONFIG
                + "`, you must also enable `"
                + AUTO_CREATE_COLUMN_FAMILIES_CONFIG
                + "`.";
        addErrorMessage(
            validationResult,
            AUTO_CREATE_TABLES_CONFIG,
            String.valueOf(autoCreateTables),
            errorMessage);
        addErrorMessage(
            validationResult,
            AUTO_CREATE_TABLES_CONFIG,
            String.valueOf(autoCreateColumnFamilies),
            errorMessage);
      }

      if (accessBigtableToValidateConfiguration) {
        // We validate the user's credentials in order to warn them early rather than fill DLQ
        // with records whose processing would fail due to invalid credentials.
        // We only call it after validating that all other parameters are fine since creating
        // a Cloud Bigtable client uses many of these parameters, and we don't want to warn
        // the user unnecessarily.
        if (!config.isBigtableConfigurationValid()) {
          String errorMessage = "Cloud Bigtable configuration is invalid.";
          for (String bigtableProp : BIGTABLE_CONFIGURATION_PROPERTIES) {
            addErrorMessage(validationResult, bigtableProp, props.get(bigtableProp), errorMessage);
          }
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
            GCP_PROJECT_ID_CONFIG,
            ConfigDef.Type.STRING,
            ConfigDef.NO_DEFAULT_VALUE,
            ConfigDef.CompositeValidator.of(
                new ConfigDef.NonNullValidator(), new ConfigDef.NonEmptyString()),
            ConfigDef.Importance.HIGH,
            "The ID of the GCP project.")
        .define(
            BIGTABLE_INSTANCE_ID_CONFIG,
            ConfigDef.Type.STRING,
            ConfigDef.NO_DEFAULT_VALUE,
            ConfigDef.CompositeValidator.of(
                new ConfigDef.NonNullValidator(), new ConfigDef.NonEmptyString()),
            ConfigDef.Importance.HIGH,
            "The ID of the Cloud Bigtable instance.")
        .define(
            BIGTABLE_APP_PROFILE_ID_CONFIG,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.MEDIUM,
            "The application profile that the connector should use. If none is supplied,"
                + " the default app profile will be used.")
        .define(
            GCP_CREDENTIALS_PATH_CONFIG,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.HIGH,
            "The path to the JSON service key file. Configure at most one of `"
                + GCP_CREDENTIALS_PATH_CONFIG
                + "` and `"
                + GCP_CREDENTIALS_JSON_CONFIG
                + "`. If neither is provided, Application Default Credentials will be used.")
        .define(
            GCP_CREDENTIALS_JSON_CONFIG,
            ConfigDef.Type.STRING,
            null,
            ConfigDef.Importance.HIGH,
            "The path to the JSON service key file. Configure at most one of `"
                + GCP_CREDENTIALS_PATH_CONFIG
                + "` and `"
                + GCP_CREDENTIALS_JSON_CONFIG
                + "`. If neither is provided, Application Default Credentials will be used.")
        .define(
            INSERT_MODE_CONFIG,
            ConfigDef.Type.STRING,
            InsertMode.INSERT.name(),
            enumValidator(InsertMode.values()),
            ConfigDef.Importance.HIGH,
            "Defines the insertion mode to use. Supported modes are:"
                + "\n- insert - Insert new record only."
                + " If the row to be written already exists in the table, an error is thrown."
                + "\n- upsert - If the row to be written already exists,"
                + " then its column values are overwritten with the ones provided.")
        .define(
            MAX_BATCH_SIZE_CONFIG,
            ConfigDef.Type.INT,
            1,
            ConfigDef.Range.atLeast(1),
            ConfigDef.Importance.MEDIUM,
            "The maximum number of records that can be batched into a batch of upserts."
                + " Note that since only a batch size of 1 for inserts is supported, `"
                + MAX_BATCH_SIZE_CONFIG
                + "` must be exactly `1` when `"
                + INSERT_MODE_CONFIG
                + "` is set to `INSERT`.")
        .define(
            VALUE_NULL_MODE_CONFIG,
            ConfigDef.Type.STRING,
            NullValueMode.WRITE.name(),
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
            ERROR_MODE_CONFIG,
            ConfigDef.Type.STRING,
            BigtableErrorMode.FAIL.name(),
            enumValidator(BigtableErrorMode.values()),
            ConfigDef.Importance.MEDIUM,
            "Specifies how to handle errors that result from writes, after retries. It is ignored"
                + " if DLQ is configured. Supported modes are:"
                + "\n- fail - The connector fails and must be manually restarted."
                + "\n- warn - The connector logs a warning and continues operating normally."
                + "\n- ignore - The connector does not log a warning but continues operating"
                + " normally.")
        .define(
            TABLE_NAME_FORMAT_CONFIG,
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
            ROW_KEY_DEFINITION_CONFIG,
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
            ROW_KEY_DELIMITER_CONFIG,
            ConfigDef.Type.STRING,
            "",
            ConfigDef.Importance.LOW,
            "The delimiter used in concatenating Kafka key fields in the row key. If this"
                + " configuration is empty or unspecified, the key fields will be concatenated"
                + " together directly.")
        .define(
            AUTO_CREATE_TABLES_CONFIG,
            ConfigDef.Type.BOOLEAN,
            false,
            new ConfigDef.NonNullValidator(),
            ConfigDef.Importance.MEDIUM,
            "Whether to automatically create the destination table if it is found to be missing.\n"
                + "When enabled, the records for which the auto-creation fails, are failed.\n"
                + "Recreation of tables deleted by other Cloud Bigtable users is not supported.\n"
                + "Note that table auto-creation is slow (multiple seconds). It may slow down not"
                + " only the records targeting nonexistent tables, but also other records batched"
                + " with them. To facilitate predictable latency leave this option disabled.")
        .define(
            AUTO_CREATE_COLUMN_FAMILIES_CONFIG,
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
                + " supported.\n"
                + "Note that column family auto-creation is slow. It may slow down"
                + " not only the records targeting nonexistent column families, but also other"
                + " records batched with them. To facilitate predictable latency leave this option"
                + " disabled.")
        .define(
            DEFAULT_COLUMN_FAMILY_CONFIG,
            ConfigDef.Type.STRING,
            ConfigInterpolation.TOPIC_PLACEHOLDER,
            ConfigDef.Importance.MEDIUM,
            "Any root-level fields on the SinkRecord that aren't objects will be added to this"
                + " column family. If empty, the fields will be ignored. Use `"
                + ConfigInterpolation.TOPIC_PLACEHOLDER
                + "` within the column family name to specify the originating topic name.")
        .define(
            DEFAULT_COLUMN_QUALIFIER_CONFIG,
            ConfigDef.Type.STRING,
            "KAFKA_VALUE",
            ConfigDef.Importance.MEDIUM,
            "Any root-level values on the SinkRecord that aren't objects will be added to this"
                + " column within default column family. If empty, the value will be ignored.")
        .define(
            RETRY_TIMEOUT_MILLIS_CONFIG,
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
    return getEnum(VALUE_NULL_MODE_CONFIG, NullValueMode::valueOf);
  }

  public BigtableErrorMode getBigtableErrorMode() {
    return getEnum(ERROR_MODE_CONFIG, BigtableErrorMode::valueOf);
  }

  public InsertMode getInsertMode() {
    return getEnum(INSERT_MODE_CONFIG, InsertMode::valueOf);
  }

  /**
   * @return {@link BigtableTableAdminClient} connected to a Cloud Bigtable instance configured as
   *     described in {@link BigtableSinkConfig#getDefinition()}.
   */
  public BigtableTableAdminClient getBigtableAdminClient() {
    Duration totalTimeout = getTotalRetryTimeout();
    RetrySettings defaultRetrySettings = getRetrySettings(totalTimeout, Duration.ZERO);
    // Retries of Admin API writes need to have a nontrivial initial delay to avoid hitting
    // the rate limit, which is low (1000 requests per minute).
    RetrySettings adminApiWriteRetrySettings =
        getRetrySettings(totalTimeout, BIGTABLE_ADMIN_API_WRITE_RETRY_INITIAL_DELAY);
    return getBigtableAdminClient(defaultRetrySettings, adminApiWriteRetrySettings);
  }

  @VisibleForTesting
  BigtableTableAdminClient getBigtableAdminClient(
      RetrySettings defaultRetrySettings, RetrySettings adminApiWriteRetrySettings) {
    Optional<CredentialsProvider> credentialsProvider =
        getUserConfiguredBigtableCredentialsProvider();

    BigtableTableAdminSettings.Builder adminSettingsBuilder =
        BigtableTableAdminSettings.newBuilder()
            .setProjectId(getString(GCP_PROJECT_ID_CONFIG))
            .setInstanceId(getString(BIGTABLE_INSTANCE_ID_CONFIG));
    if (credentialsProvider.isPresent()) {
      adminSettingsBuilder.setCredentialsProvider(credentialsProvider.get());
    } else {
      // Use the default credential provider that utilizes Application Default Credentials.
    }

    BigtableTableAdminStubSettings.Builder adminStubSettings = adminSettingsBuilder.stubSettings();
    adminStubSettings.listTablesSettings().setRetrySettings(defaultRetrySettings);
    adminStubSettings.getTableSettings().setRetrySettings(defaultRetrySettings);
    adminStubSettings
        .createTableSettings()
        .setRetrySettings(adminApiWriteRetrySettings)
        // Retry createTable() for status codes other admin operations retry by default as
        // seen in BigtableTableAdminStubSettings.
        .setRetryableCodes(
            Sets.union(
                adminStubSettings.createTableSettings().getRetryableCodes(),
                Set.of(StatusCode.Code.UNAVAILABLE, StatusCode.Code.DEADLINE_EXCEEDED)));
    adminStubSettings
        .modifyColumnFamiliesSettings()
        .setRetrySettings(adminApiWriteRetrySettings)
        // Retry modifyColumnFamilies() for status codes other admin operations retry by
        // default as seen in BigtableTableAdminStubSettings and for FAILED_PRECONDITION,
        // which is returned when concurrent column family creation is detected.
        .setRetryableCodes(
            Sets.union(
                adminStubSettings.createTableSettings().getRetryableCodes(),
                Set.of(
                    StatusCode.Code.UNAVAILABLE,
                    StatusCode.Code.DEADLINE_EXCEEDED,
                    StatusCode.Code.FAILED_PRECONDITION)));

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
    Duration totalTimeout = getTotalRetryTimeout();
    RetrySettings retrySettings = getRetrySettings(totalTimeout, Duration.ZERO);
    Optional<CredentialsProvider> credentialsProvider =
        getUserConfiguredBigtableCredentialsProvider();

    BigtableDataSettings.Builder dataSettingsBuilder =
        BigtableDataSettings.newBuilder()
            .setProjectId(getString(GCP_PROJECT_ID_CONFIG))
            .setInstanceId(getString(BIGTABLE_INSTANCE_ID_CONFIG));
    if (credentialsProvider.isPresent()) {
      dataSettingsBuilder.setCredentialsProvider(credentialsProvider.get());
    } else {
      // Use the default credential provider that utilizes Application Default Credentials.
    }
    String appProfileId = getString(BIGTABLE_APP_PROFILE_ID_CONFIG);
    if (appProfileId == null) {
      dataSettingsBuilder.setDefaultAppProfileId();
    } else {
      dataSettingsBuilder.setAppProfileId(appProfileId);
    }

    EnhancedBigtableStubSettings.Builder dataStubSettings = dataSettingsBuilder.stubSettings();
    dataStubSettings.mutateRowSettings().setRetrySettings(retrySettings);
    dataStubSettings.checkAndMutateRowSettings().setRetrySettings(retrySettings);
    dataStubSettings.bulkMutateRowsSettings().setRetrySettings(retrySettings);
    dataStubSettings.readRowSettings().setRetrySettings(retrySettings);
    dataStubSettings.readRowsSettings().setRetrySettings(retrySettings);
    dataStubSettings.bulkReadRowsSettings().setRetrySettings(retrySettings);

    // After a schema modification, Bigtable API sometimes transiently returns NOT_FOUND and
    // FAILED_PRECONDITION errors. We just need to retry them. We do it if - and only if - the
    // column family auto creation is enabled, because then BigtableSchemaManager ensures that
    // processing of records, for which the tables and/or column families are missing, is
    // finished early, before the actual data requests are sent.
    if (getBoolean(AUTO_CREATE_COLUMN_FAMILIES_CONFIG)) {
      Set<StatusCode.Code> transientErrorsAfterSchemaModification =
          Set.of(StatusCode.Code.NOT_FOUND, StatusCode.Code.FAILED_PRECONDITION);
      dataStubSettings
          .mutateRowSettings()
          .setRetryableCodes(
              Sets.union(
                  dataStubSettings.mutateRowSettings().getRetryableCodes(),
                  transientErrorsAfterSchemaModification));
      dataStubSettings
          .checkAndMutateRowSettings()
          .setRetryableCodes(
              Sets.union(
                  dataStubSettings.checkAndMutateRowSettings().getRetryableCodes(),
                  transientErrorsAfterSchemaModification));
      dataStubSettings
          .bulkMutateRowsSettings()
          .setRetryableCodes(
              Sets.union(
                  dataStubSettings.bulkMutateRowsSettings().getRetryableCodes(),
                  transientErrorsAfterSchemaModification));
      dataStubSettings
          .readRowSettings()
          .setRetryableCodes(
              Sets.union(
                  dataStubSettings.readRowSettings().getRetryableCodes(),
                  transientErrorsAfterSchemaModification));
      dataStubSettings
          .readRowsSettings()
          .setRetryableCodes(
              Sets.union(
                  dataStubSettings.readRowsSettings().getRetryableCodes(),
                  transientErrorsAfterSchemaModification));
      dataStubSettings
          .bulkReadRowsSettings()
          .setRetryableCodes(
              Sets.union(
                  dataStubSettings.bulkReadRowsSettings().getRetryableCodes(),
                  transientErrorsAfterSchemaModification));
    }
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
          getRetrySettings(BIGTABLE_CREDENTIALS_CHECK_TIMEOUT, Duration.ZERO);
      bigtable = getBigtableAdminClient(retrySettings, retrySettings);
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
   * @return {@link RetrySettings} of Cloud Bigtable clients configured with exponential backoff and
   *     specified timeout and retry delay.
   */
  protected RetrySettings getRetrySettings(Duration totalTimeout, Duration initialDelay) {
    return RetrySettings.newBuilder()
        .setTotalTimeout(totalTimeout)
        .setInitialRetryDelay(initialDelay)
        .setMaxRetryDelay(totalTimeout)
        .setRetryDelayMultiplier(BIGTABLE_CLIENT_RETRY_DELAY_MULTIPLIER)
        .build();
  }

  /**
   * @return Maximal time for Cloud Bigtable clients as described in {@link
   *     BigtableSinkConfig#getDefinition()}.
   */
  private Duration getTotalRetryTimeout() {
    return Duration.of(getLong(RETRY_TIMEOUT_MILLIS_CONFIG), ChronoUnit.MILLIS);
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
    String credentialsJson = getString(GCP_CREDENTIALS_JSON_CONFIG);
    String credentialsPath = getString(GCP_CREDENTIALS_PATH_CONFIG);
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
