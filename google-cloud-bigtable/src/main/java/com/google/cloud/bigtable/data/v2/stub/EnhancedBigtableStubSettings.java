/*
 * Copyright 2018 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.BetaApi;
import com.google.api.gax.batching.BatchingCallSettings;
import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.batching.FlowControlSettings;
import com.google.api.gax.batching.FlowController.LimitExceededBehavior;
import com.google.api.gax.core.GaxProperties;
import com.google.api.gax.core.GoogleCredentialsProvider;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.api.gax.rpc.ServerStreamingCallSettings;
import com.google.api.gax.rpc.StatusCode.Code;
import com.google.api.gax.rpc.StubSettings;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.api.gax.rpc.UnaryCallSettings;
import com.google.cloud.bigtable.data.v2.models.ConditionalRowMutation;
import com.google.cloud.bigtable.data.v2.models.KeyOffset;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.ReadModifyWriteRow;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.stub.mutaterows.MutateRowsBatchingDescriptor;
import com.google.cloud.bigtable.data.v2.stub.readrows.ReadRowsBatchingDescriptor;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import org.threeten.bp.Duration;

/**
 * Settings class to configure an instance of {@link EnhancedBigtableStub}.
 *
 * <p>Sane defaults are provided for most settings:
 *
 * <ul>
 *   <li>The default service address (bigtable.googleapis.com) and default port (443) are used.
 *   <li>Credentials are acquired automatically through Application Default Credentials.
 *   <li>Retries are configured for idempotent methods but not for non-idempotent methods.
 * </ul>
 *
 * <p>The only required setting is the instance name.
 *
 * <p>The builder of this class is recursive, so contained classes are themselves builders. When
 * build() is called, the tree of builders is called to create the complete settings object.
 *
 * <pre>{@code
 * BigtableDataSettings.Builder settingsBuilder = BigtableDataSettings.newBuilder()
 *   .setProjectId("my-project-id")
 *   .setInstanceId("my-instance-id")
 *   .setAppProfileId("default");
 *
 * settingsBuilder.readRowsSettings()
 *  .setRetryableCodes(Code.DEADLINE_EXCEEDED, Code.UNAVAILABLE);
 *
 * BigtableDataSettings settings = builder.build();
 * }</pre>
 */
public class EnhancedBigtableStubSettings extends StubSettings<EnhancedBigtableStubSettings> {
  private static final Logger logger =
      Logger.getLogger(EnhancedBigtableStubSettings.class.getName());

  // The largest message that can be received is a 256 MB ReadRowsResponse.
  private static final int MAX_MESSAGE_SIZE = 256 * 1024 * 1024;
  private static final String SERVER_DEFAULT_APP_PROFILE_ID = "";

  // TODO(weiranf): Remove this temporary endpoint once DirectPath goes to public beta
  private static final String DIRECT_PATH_ENDPOINT =
      "testdirectpath-bigtable.sandbox.googleapis.com:443";

  private static final Set<Code> IDEMPOTENT_RETRY_CODES =
      ImmutableSet.of(Code.DEADLINE_EXCEEDED, Code.UNAVAILABLE);

  // Copy of default retrying settings in the yaml
  private static final RetrySettings IDEMPOTENT_RETRY_SETTINGS =
      RetrySettings.newBuilder()
          .setInitialRetryDelay(Duration.ofMillis(10))
          .setRetryDelayMultiplier(2)
          .setMaxRetryDelay(Duration.ofMinutes(1))
          .setInitialRpcTimeout(Duration.ofSeconds(20))
          .setRpcTimeoutMultiplier(1.0)
          .setMaxRpcTimeout(Duration.ofSeconds(20))
          .setTotalTimeout(Duration.ofMinutes(10))
          .build();

  // Allow retrying ABORTED statuses. These will be returned by the server when the client is
  // too slow to read the rows. This makes sense for the java client because retries happen
  // after the row merging logic. Which means that the retry will not be invoked until the
  // current buffered chunks are consumed.
  private static final Set<Code> READ_ROWS_RETRY_CODES =
      ImmutableSet.<Code>builder().addAll(IDEMPOTENT_RETRY_CODES).add(Code.ABORTED).build();

  private static final RetrySettings READ_ROWS_RETRY_SETTINGS =
      RetrySettings.newBuilder()
          .setInitialRetryDelay(Duration.ofMillis(10))
          .setRetryDelayMultiplier(2.0)
          .setMaxRetryDelay(Duration.ofMinutes(1))
          .setMaxAttempts(10)
          .setJittered(true)
          .setInitialRpcTimeout(Duration.ofMinutes(5))
          .setRpcTimeoutMultiplier(2.0)
          .setMaxRpcTimeout(Duration.ofMinutes(5))
          .setTotalTimeout(Duration.ofHours(12))
          .build();

  private static final RetrySettings MUTATE_ROWS_RETRY_SETTINGS =
      RetrySettings.newBuilder()
          .setInitialRetryDelay(Duration.ofMillis(10))
          .setRetryDelayMultiplier(2)
          .setMaxRetryDelay(Duration.ofMinutes(1))
          .setInitialRpcTimeout(Duration.ofMinutes(1))
          .setRpcTimeoutMultiplier(1.0)
          .setMaxRpcTimeout(Duration.ofMinutes(1))
          .setTotalTimeout(Duration.ofMinutes(10))
          .build();

  /**
   * Scopes that are equivalent to JWT's audience.
   *
   * <p>When the credentials provider contains any of these scopes (default behavior) and the
   * application default credentials point to a service account, then OAuth2 tokens will be replaced
   * with JWT tokens. This removes the need for access token refreshes.
   */
  private static final ImmutableList<String> JWT_ENABLED_SCOPES =
      ImmutableList.<String>builder()
          .add("https://www.googleapis.com/auth/bigtable.data")
          .add("https://www.googleapis.com/auth/cloud-bigtable.data")
          .add("https://www.googleapis.com/auth/cloud-platform")
          .build();

  private final String projectId;
  private final String instanceId;
  private final String appProfileId;
  private final boolean isRefreshingChannel;
  private ImmutableList<String> primedTableIds;

  private final ServerStreamingCallSettings<Query, Row> readRowsSettings;
  private final UnaryCallSettings<Query, Row> readRowSettings;
  private final UnaryCallSettings<String, List<KeyOffset>> sampleRowKeysSettings;
  private final UnaryCallSettings<RowMutation, Void> mutateRowSettings;
  private final BigtableBatchingCallSettings bulkMutateRowsSettings;
  private final BigtableBulkReadRowsCallSettings bulkReadRowsSettings;
  private final UnaryCallSettings<ConditionalRowMutation, Boolean> checkAndMutateRowSettings;
  private final UnaryCallSettings<ReadModifyWriteRow, Row> readModifyWriteRowSettings;

  private EnhancedBigtableStubSettings(Builder builder) {
    super(builder);

    if (DIRECT_PATH_ENDPOINT.equals(builder.getEndpoint())) {
      logger.warning(
          "Connecting to Bigtable using DirectPath."
              + " This is currently an experimental feature and should not be used in production.");
    }

    // Since point reads, streaming reads, bulk reads share the same base callable that converts
    // grpc errors into ApiExceptions, they must have the same retry codes.
    Preconditions.checkState(
        builder
            .readRowSettings
            .getRetryableCodes()
            .equals(builder.readRowsSettings.getRetryableCodes()),
        "Single ReadRow retry codes must match ReadRows retry codes");
    Preconditions.checkState(
        builder
            .bulkReadRowsSettings
            .getRetryableCodes()
            .equals(builder.readRowsSettings.getRetryableCodes()),
        "Bulk ReadRow retry codes must match ReadRows retry codes");

    projectId = builder.projectId;
    instanceId = builder.instanceId;
    appProfileId = builder.appProfileId;
    isRefreshingChannel = builder.isRefreshingChannel;
    primedTableIds = builder.primedTableIds;

    // Per method settings.
    readRowsSettings = builder.readRowsSettings.build();
    readRowSettings = builder.readRowSettings.build();
    sampleRowKeysSettings = builder.sampleRowKeysSettings.build();
    mutateRowSettings = builder.mutateRowSettings.build();
    bulkMutateRowsSettings = builder.bulkMutateRowsSettings.build();
    bulkReadRowsSettings = builder.bulkReadRowsSettings.build();
    checkAndMutateRowSettings = builder.checkAndMutateRowSettings.build();
    readModifyWriteRowSettings = builder.readModifyWriteRowSettings.build();
  }

  /** Create a new builder. */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Returns the project id of the target instance. */
  public String getProjectId() {
    return projectId;
  }

  /** Returns the target instance id. */
  public String getInstanceId() {
    return instanceId;
  }

  /** Returns the configured AppProfile to use */
  public String getAppProfileId() {
    return appProfileId;
  }

  /** Returns if channels will gracefully refresh connections to Cloud Bigtable service */
  @BetaApi("This API depends on experimental gRPC APIs")
  public boolean isRefreshingChannel() {
    return isRefreshingChannel;
  }

  /** Gets the tables that will be primed during a channel refresh. */
  @BetaApi("Channel priming is not currently stable and might change in the future")
  public List<String> getPrimedTableIds() {
    return primedTableIds;
  }

  /** Returns a builder for the default ChannelProvider for this service. */
  public static InstantiatingGrpcChannelProvider.Builder defaultGrpcTransportProviderBuilder() {
    return BigtableStubSettings.defaultGrpcTransportProviderBuilder()
        .setPoolSize(getDefaultChannelPoolSize())
        .setMaxInboundMessageSize(MAX_MESSAGE_SIZE)
        // TODO(weiranf): Set this to true by default once DirectPath goes to public beta
        .setAttemptDirectPath(isDirectPathEnabled());
  }

  // TODO(weiranf): Remove this once DirectPath goes to public beta
  private static boolean isDirectPathEnabled() {
    return Boolean.getBoolean("bigtable.attempt-directpath");
  }

  static int getDefaultChannelPoolSize() {
    // TODO: tune channels
    return 2 * Runtime.getRuntime().availableProcessors();
  }

  @SuppressWarnings("WeakerAccess")
  public static TransportChannelProvider defaultTransportChannelProvider() {
    return defaultGrpcTransportProviderBuilder().build();
  }

  /** Returns a builder for the default credentials for this service. */
  public static GoogleCredentialsProvider.Builder defaultCredentialsProviderBuilder() {
    return BigtableStubSettings.defaultCredentialsProviderBuilder()
        .setJwtEnabledScopes(JWT_ENABLED_SCOPES);
  }

  /**
   * Returns the object with the settings used for calls to ReadRows.
   *
   * <p>This is idempotent and streaming operation.
   *
   * <p>Default retry and timeout settings:
   *
   * <ul>
   *   <li>{@link ServerStreamingCallSettings.Builder#setIdleTimeout Default idle timeout} is set to
   *       5 mins.
   *   <li>Retry {@link ServerStreamingCallSettings.Builder#setRetryableCodes error codes} are:
   *       {@link Code#DEADLINE_EXCEEDED}, {@link Code#UNAVAILABLE} and {@link Code#ABORTED}.
   *   <li>RetryDelay between failed attempts {@link RetrySettings.Builder#setInitialRetryDelay
   *       starts} at 10ms and {@link RetrySettings.Builder#setRetryDelayMultiplier increases
   *       exponentially} by a factor of 2 until a {@link RetrySettings.Builder#setMaxRetryDelay
   *       maximum of} 1 minute.
   *   <li>The default read timeout for {@link RetrySettings.Builder#setMaxRpcTimeout each row} in a
   *       response stream is 5 minutes with {@link RetrySettings.Builder#setMaxAttempts maximum
   *       attempt} count of 10 times and the timeout to read the {@link
   *       RetrySettings.Builder#setTotalTimeout entire stream} is 12 hours.
   * </ul>
   */
  public ServerStreamingCallSettings<Query, Row> readRowsSettings() {
    return readRowsSettings;
  }

  /**
   * Returns the object with the settings used for calls to SampleRowKeys.
   *
   * <p>This is idempotent and non-streaming operation.
   *
   * <p>Default retry and timeout settings:
   *
   * <ul>
   *   <li>Retry {@link UnaryCallSettings.Builder#setRetryableCodes error codes} are: {@link
   *       Code#DEADLINE_EXCEEDED} and {@link Code#UNAVAILABLE}.
   *   <li>RetryDelay between failed attempts {@link RetrySettings.Builder#setInitialRetryDelay
   *       starts} at 10ms and {@link RetrySettings.Builder#setRetryDelayMultiplier increases
   *       exponentially} by a factor of 2 until a {@link RetrySettings.Builder#setMaxRetryDelay
   *       maximum of} 1 minute.
   *   <li>The default timeout for {@link RetrySettings.Builder#setMaxRpcTimeout each attempt} is 20
   *       seconds and the timeout for the {@link RetrySettings.Builder#setTotalTimeout entire
   *       operation} across all of the attempts is 10 mins.
   * </ul>
   */
  public UnaryCallSettings<String, List<KeyOffset>> sampleRowKeysSettings() {
    return sampleRowKeysSettings;
  }

  /**
   * Returns the object with the settings used for point reads via ReadRows.
   *
   * <p>This is an idempotent and non-streaming operation.
   *
   * <p>Default retry and timeout settings:
   *
   * <ul>
   *   <li>Retry {@link UnaryCallSettings.Builder#setRetryableCodes error codes} are: {@link
   *       Code#DEADLINE_EXCEEDED}, {@link Code#UNAVAILABLE} and {@link Code#ABORTED}.
   *   <li>RetryDelay between failed attempts {@link RetrySettings.Builder#setInitialRetryDelay
   *       starts} at 10ms and {@link RetrySettings.Builder#setRetryDelayMultiplier increases
   *       exponentially} by a factor of 2 until a {@link RetrySettings.Builder#setMaxRetryDelay
   *       maximum of} 1 minute.
   *   <li>The default timeout for {@link RetrySettings.Builder#setMaxRpcTimeout each attempt} is 20
   *       seconds and the timeout for the {@link RetrySettings.Builder#setTotalTimeout entire
   *       operation} across all of the attempts is 10 mins.
   * </ul>
   *
   * @see RetrySettings for more explanation.
   */
  public UnaryCallSettings<Query, Row> readRowSettings() {
    return readRowSettings;
  }

  /**
   * Returns the object with the settings used for calls to MutateRow.
   *
   * <p>This is an idempotent and non-streaming operation.
   *
   * <p>Default retry and timeout settings:
   *
   * <ul>
   *   <li>Retry {@link UnaryCallSettings.Builder#setRetryableCodes error codes} are: {@link
   *       Code#DEADLINE_EXCEEDED} and {@link Code#UNAVAILABLE}.
   *   <li>RetryDelay between failed attempts {@link RetrySettings.Builder#setInitialRetryDelay
   *       starts} at 10ms and {@link RetrySettings.Builder#setRetryDelayMultiplier increases
   *       exponentially} by a factor of 2 until a {@link RetrySettings.Builder#setMaxRetryDelay
   *       maximum of} 60 seconds.
   *   <li>The default timeout for {@link RetrySettings.Builder#setMaxRpcTimeout each attempt} is 20
   *       seconds and the timeout for the {@link RetrySettings.Builder#setTotalTimeout entire
   *       operation} across all of the attempts is 10 mins.
   * </ul>
   *
   * @see RetrySettings for more explanation.
   */
  public UnaryCallSettings<RowMutation, Void> mutateRowSettings() {
    return mutateRowSettings;
  }

  /**
   * Returns the object with the settings used for calls to MutateRows.
   *
   * <p>Please note that these settings will affect both manually batched calls
   * (bulkMutateRowsCallable) and automatic batched calls (bulkMutateRowsBatchingCallable). The
   * {@link RowMutation} request signature is ignored for the manual batched calls.
   *
   * <p>Default retry and timeout settings:
   *
   * <ul>
   *   <li>Retry {@link BatchingCallSettings.Builder#setRetryableCodes error codes} are: {@link
   *       Code#DEADLINE_EXCEEDED} and {@link Code#UNAVAILABLE}.
   *   <li>RetryDelay between failed attempts {@link RetrySettings.Builder#setInitialRetryDelay
   *       starts} at 10ms and {@link RetrySettings.Builder#setRetryDelayMultiplier increases
   *       exponentially} by a factor of 2 until a {@link RetrySettings.Builder#setMaxRetryDelay
   *       maximum of} 1 minute.
   *   <li>The default timeout for {@link RetrySettings.Builder#setMaxRpcTimeout each attempt} is 1
   *       minute and the timeout for the {@link RetrySettings.Builder#setTotalTimeout entire
   *       operation} across all of the attempts is 10 mins.
   * </ul>
   *
   * <p>On breach of certain triggers, the operation initiates processing of accumulated request for
   * which the default settings are:
   *
   * <ul>
   *   <li>When the {@link BatchingSettings.Builder#setElementCountThreshold request count} reaches
   *       100.
   *   <li>When accumulated {@link BatchingSettings.Builder#setRequestByteThreshold request size}
   *       reaches to 20MB.
   *   <li>When an {@link BatchingSettings.Builder#setDelayThreshold interval of} 1 second passes
   *       after batching initialization or last processed batch.
   * </ul>
   *
   * <p>When the pending {@link FlowControlSettings.Builder#setMaxOutstandingElementCount request
   * count} reaches a default of 1000 entries per channel or their {@link
   * FlowControlSettings.Builder#setMaxOutstandingRequestBytes accumulated size} reaches default
   * value of 100MB, then this operation will by default be {@link
   * FlowControlSettings.Builder#setLimitExceededBehavior blocked} until some of the pending batch
   * are resolved.
   *
   * @see RetrySettings for more explanation.
   * @see BatchingSettings for batch related configuration explanation.
   */
  public BigtableBatchingCallSettings bulkMutateRowsSettings() {
    return bulkMutateRowsSettings;
  }

  /**
   * Returns the call settings used for bulk read rows.
   *
   * <p>Default retry and timeout settings:
   *
   * <ul>
   *   <li>Retry {@link BatchingCallSettings.Builder#setRetryableCodes error codes} are: {@link
   *       Code#DEADLINE_EXCEEDED}, {@link Code#UNAVAILABLE} and {@link Code#ABORTED}.
   *   <li>RetryDelay between failed attempts {@link RetrySettings.Builder#setInitialRetryDelay
   *       starts} at 10ms and {@link RetrySettings.Builder#setRetryDelayMultiplier increases
   *       exponentially} by a factor of 2 until a {@link RetrySettings.Builder#setMaxRetryDelay
   *       maximum of} 1 minute.
   *   <li>The default timeout for {@link RetrySettings.Builder#setMaxRpcTimeout each attempt} is 5
   *       minute and the timeout for the {@link RetrySettings.Builder#setTotalTimeout entire
   *       operation} across all of the attempts is 10 mins.
   * </ul>
   *
   * <p>On breach of certain triggers, the operation initiates processing of accumulated request for
   * which the default settings are:
   *
   * <ul>
   *   <li>When the {@link BatchingSettings.Builder#setElementCountThreshold request count} reaches
   *       100.
   *   <li>When accumulated {@link BatchingSettings.Builder#setRequestByteThreshold request size}
   *       reaches to 400KB.
   *   <li>When an {@link BatchingSettings.Builder#setDelayThreshold interval of} 1 second passes
   *       after batching initialization or last processed batch.
   * </ul>
   *
   * <p>When the pending {@link FlowControlSettings.Builder#setMaxOutstandingElementCount request
   * count} reaches a default of 1000 outstanding row keys per channel then this operation will by
   * default be {@link FlowControlSettings.Builder#setLimitExceededBehavior blocked} until some of
   * the pending batch are resolved.
   *
   * @see RetrySettings for more explanation.
   * @see BatchingSettings for batch related configuration explanation.
   */
  public BigtableBulkReadRowsCallSettings bulkReadRowsSettings() {
    return bulkReadRowsSettings;
  }

  /**
   * Returns the object with the settings used for calls to CheckAndMutateRow.
   *
   * <p>This is a non-idempotent and non-streaming operation.
   *
   * <p>By default this operation does not reattempt in case of RPC failure. The default timeout for
   * the {@link RetrySettings.Builder#setTotalTimeout entire operation} is 20 seconds.
   *
   * @see RetrySettings for more explanation.
   */
  public UnaryCallSettings<ConditionalRowMutation, Boolean> checkAndMutateRowSettings() {
    return checkAndMutateRowSettings;
  }

  /**
   * Returns the object with the settings used for calls to ReadModifyWriteRow.
   *
   * <p>This is a non-idempotent and non-streaming operation.
   *
   * <p>By default this operation does not reattempt in case of RPC failure. The default timeout for
   * the {@link RetrySettings.Builder#setTotalTimeout entire operation} is 20 seconds.
   *
   * @see RetrySettings for more explanation.
   */
  public UnaryCallSettings<ReadModifyWriteRow, Row> readModifyWriteRowSettings() {
    return readModifyWriteRowSettings;
  }

  /** Returns a builder containing all the values of this settings class. */
  public Builder toBuilder() {
    return new Builder(this);
  }

  /** Builder for BigtableDataSettings. */
  public static class Builder extends StubSettings.Builder<EnhancedBigtableStubSettings, Builder> {

    private String projectId;
    private String instanceId;
    private String appProfileId;
    private boolean isRefreshingChannel;
    private ImmutableList<String> primedTableIds;

    private final ServerStreamingCallSettings.Builder<Query, Row> readRowsSettings;
    private final UnaryCallSettings.Builder<Query, Row> readRowSettings;
    private final UnaryCallSettings.Builder<String, List<KeyOffset>> sampleRowKeysSettings;
    private final UnaryCallSettings.Builder<RowMutation, Void> mutateRowSettings;
    private final BigtableBatchingCallSettings.Builder bulkMutateRowsSettings;
    private final BigtableBulkReadRowsCallSettings.Builder bulkReadRowsSettings;
    private final UnaryCallSettings.Builder<ConditionalRowMutation, Boolean>
        checkAndMutateRowSettings;
    private final UnaryCallSettings.Builder<ReadModifyWriteRow, Row> readModifyWriteRowSettings;

    /**
     * Initializes a new Builder with sane defaults for all settings.
     *
     * <p>Most defaults are extracted from BaseBigtableDataSettings, however some of the more
     * complex defaults are configured explicitly here. Once the overlayed defaults are configured,
     * the base settings are augmented to work with overlayed functionality (like disabling retries
     * in the underlying GAPIC client for batching).
     */
    private Builder() {
      this.appProfileId = SERVER_DEFAULT_APP_PROFILE_ID;
      this.isRefreshingChannel = false;
      primedTableIds = ImmutableList.of();
      setCredentialsProvider(defaultCredentialsProviderBuilder().build());

      // Defaults provider
      BigtableStubSettings.Builder baseDefaults = BigtableStubSettings.newBuilder();

      // TODO(weiranf): remove this once DirectPath goes to public Beta and uses the default
      // endpoint.
      if (isDirectPathEnabled()) {
        setEndpoint(DIRECT_PATH_ENDPOINT);
      } else {
        setEndpoint(baseDefaults.getEndpoint());
      }

      setTransportChannelProvider(defaultTransportChannelProvider());
      setStreamWatchdogCheckInterval(baseDefaults.getStreamWatchdogCheckInterval());
      setStreamWatchdogProvider(baseDefaults.getStreamWatchdogProvider());

      // Inject the UserAgent in addition to api-client header
      Map<String, String> headers =
          ImmutableMap.<String, String>builder()
              .putAll(
                  BigtableStubSettings.defaultApiClientHeaderProviderBuilder().build().getHeaders())
              .put(
                  "user-agent",
                  "bigtable-java/"
                      + GaxProperties.getLibraryVersion(EnhancedBigtableStubSettings.class))
              .build();
      setInternalHeaderProvider(FixedHeaderProvider.create(headers));

      // Per-method settings using baseSettings for defaults.
      readRowsSettings = ServerStreamingCallSettings.newBuilder();

      readRowsSettings
          .setRetryableCodes(READ_ROWS_RETRY_CODES)
          .setRetrySettings(READ_ROWS_RETRY_SETTINGS)
          .setIdleTimeout(Duration.ofMinutes(5));

      // Point reads should use same defaults as streaming reads, but with a shorter timeout
      readRowSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      readRowSettings
          .setRetryableCodes(readRowsSettings.getRetryableCodes())
          .setRetrySettings(
              baseDefaults
                  .readRowsSettings()
                  .getRetrySettings()
                  .toBuilder()
                  .setTotalTimeout(IDEMPOTENT_RETRY_SETTINGS.getTotalTimeout())
                  .build());

      sampleRowKeysSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      sampleRowKeysSettings
          .setRetryableCodes(IDEMPOTENT_RETRY_CODES)
          .setRetrySettings(IDEMPOTENT_RETRY_SETTINGS);

      mutateRowSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      copyRetrySettings(baseDefaults.mutateRowSettings(), mutateRowSettings);

      long maxBulkMutateElementPerBatch = 100L;
      // Enables bulkMutate to support 10 outstanding batches upto per channel or up to 20K entries.
      long maxBulkMutateOutstandingElementCount =
          Math.min(20_000L, 10L * maxBulkMutateElementPerBatch * getDefaultChannelPoolSize());

      bulkMutateRowsSettings =
          BigtableBatchingCallSettings.newBuilder(new MutateRowsBatchingDescriptor())
              .setRetryableCodes(IDEMPOTENT_RETRY_CODES)
              .setRetrySettings(MUTATE_ROWS_RETRY_SETTINGS)
              .setBatchingSettings(
                  BatchingSettings.newBuilder()
                      .setIsEnabled(true)
                      .setElementCountThreshold(maxBulkMutateElementPerBatch)
                      .setRequestByteThreshold(20L * 1024 * 1024)
                      .setDelayThreshold(Duration.ofSeconds(1))
                      .setFlowControlSettings(
                          FlowControlSettings.newBuilder()
                              .setLimitExceededBehavior(LimitExceededBehavior.Block)
                              .setMaxOutstandingRequestBytes(100L * 1024 * 1024)
                              .setMaxOutstandingElementCount(maxBulkMutateOutstandingElementCount)
                              .build())
                      .build());

      long maxBulkReadElementPerBatch = 100L;
      long maxBulkReadRequestSizePerBatch = 400L * 1024L;
      // Enables bulkRead to support 10 outstanding batches per channel
      long maxBulkReadOutstandingElementCount =
          10L * maxBulkReadElementPerBatch * getDefaultChannelPoolSize();

      bulkReadRowsSettings =
          BigtableBulkReadRowsCallSettings.newBuilder(new ReadRowsBatchingDescriptor())
              .setRetryableCodes(readRowsSettings.getRetryableCodes())
              .setRetrySettings(IDEMPOTENT_RETRY_SETTINGS)
              .setBatchingSettings(
                  BatchingSettings.newBuilder()
                      .setElementCountThreshold(maxBulkReadElementPerBatch)
                      .setRequestByteThreshold(maxBulkReadRequestSizePerBatch)
                      .setDelayThreshold(Duration.ofSeconds(1))
                      .setFlowControlSettings(
                          FlowControlSettings.newBuilder()
                              .setLimitExceededBehavior(LimitExceededBehavior.Block)
                              .setMaxOutstandingElementCount(maxBulkReadOutstandingElementCount)
                              .build())
                      .build());

      checkAndMutateRowSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      copyRetrySettings(baseDefaults.checkAndMutateRowSettings(), checkAndMutateRowSettings);

      readModifyWriteRowSettings = UnaryCallSettings.newUnaryCallSettingsBuilder();
      copyRetrySettings(baseDefaults.readModifyWriteRowSettings(), readModifyWriteRowSettings);
    }

    private Builder(EnhancedBigtableStubSettings settings) {
      super(settings);
      projectId = settings.projectId;
      instanceId = settings.instanceId;
      appProfileId = settings.appProfileId;
      isRefreshingChannel = settings.isRefreshingChannel;
      primedTableIds = settings.primedTableIds;

      // Per method settings.
      readRowsSettings = settings.readRowsSettings.toBuilder();
      readRowSettings = settings.readRowSettings.toBuilder();
      sampleRowKeysSettings = settings.sampleRowKeysSettings.toBuilder();
      mutateRowSettings = settings.mutateRowSettings.toBuilder();
      bulkMutateRowsSettings = settings.bulkMutateRowsSettings.toBuilder();
      bulkReadRowsSettings = settings.bulkReadRowsSettings.toBuilder();
      checkAndMutateRowSettings = settings.checkAndMutateRowSettings.toBuilder();
      readModifyWriteRowSettings = settings.readModifyWriteRowSettings.toBuilder();
    }
    // <editor-fold desc="Private Helpers">

    /**
     * Copies settings from unary RPC to another. This is necessary when modifying request and
     * response types while trying to retain retry settings.
     */
    private static void copyRetrySettings(
        UnaryCallSettings.Builder<?, ?> source, UnaryCallSettings.Builder<?, ?> dest) {
      dest.setRetryableCodes(source.getRetryableCodes());
      dest.setRetrySettings(source.getRetrySettings());
    }
    // </editor-fold>

    // <editor-fold desc="Public API">
    /**
     * Sets the project id of that target instance. This setting is required. All RPCs will be made
     * in the context of this setting.
     */
    public Builder setProjectId(@Nonnull String projectId) {
      Preconditions.checkNotNull(projectId);
      this.projectId = projectId;
      return this;
    }

    /** Gets the project id of the target instance that was previously set on this Builder. */
    public String getProjectId() {
      return projectId;
    }

    /**
     * Sets the target instance id. This setting is required. All RPCs will be made in the context
     * of this setting.
     */
    public Builder setInstanceId(@Nonnull String instanceId) {
      Preconditions.checkNotNull(instanceId);
      this.instanceId = instanceId;
      return this;
    }

    /** Gets the target instance id that was previously set on this Builder. */
    public String getInstanceId() {
      return instanceId;
    }

    /**
     * Sets the AppProfile to use. An application profile (sometimes also shortened to "app
     * profile") is a group of configuration parameters for an individual use case. A client will
     * identify itself with an application profile ID at connection time, and the requests will be
     * handled according to that application profile.
     */
    public Builder setAppProfileId(@Nonnull String appProfileId) {
      Preconditions.checkNotNull(appProfileId, "AppProfileId can't be null");
      this.appProfileId = appProfileId;
      return this;
    }

    /**
     * Resets the AppProfile id to the default for the instance.
     *
     * <p>An application profile (sometimes also shortened to "app profile") is a group of
     * configuration parameters for an individual use case. A client will identify itself with an
     * application profile ID at connection time, and the requests will be handled according to that
     * application profile.
     *
     * <p>Every Bigtable Instance has a default application profile associated with it, this method
     * configures the client to use it.
     */
    public Builder setDefaultAppProfileId() {
      setAppProfileId(SERVER_DEFAULT_APP_PROFILE_ID);
      return this;
    }

    /** Gets the app profile id that was previously set on this Builder. */
    public String getAppProfileId() {
      return appProfileId;
    }

    /**
     * Sets if channels will gracefully refresh connections to Cloud Bigtable service.
     *
     * <p>When enabled, this will wait for the connection to complete the SSL handshake. The effect
     * can be enhanced by configuring table ids that can be used warm serverside caches using {@link
     * #setPrimedTableIds(String...)}.
     *
     * @see com.google.cloud.bigtable.data.v2.BigtableDataSettings.Builder#setRefreshingChannel
     */
    @BetaApi("This API depends on experimental gRPC APIs")
    public Builder setRefreshingChannel(boolean isRefreshingChannel) {
      this.isRefreshingChannel = isRefreshingChannel;
      return this;
    }

    /** Configures which tables will be primed when a connection is created. */
    @BetaApi("Channel priming is not currently stable and might change in the future")
    public Builder setPrimedTableIds(String... tableIds) {
      this.primedTableIds = ImmutableList.copyOf(tableIds);
      return this;
    }

    /** Gets if channels will gracefully refresh connections to Cloud Bigtable service */
    @BetaApi("This API depends on experimental gRPC APIs")
    public boolean isRefreshingChannel() {
      return isRefreshingChannel;
    }

    /** Gets the tables that will be primed during a channel refresh. */
    @BetaApi("Channel priming is not currently stable and might change in the future")
    public List<String> getPrimedTableIds() {
      return primedTableIds;
    }

    /** Returns the builder for the settings used for calls to readRows. */
    public ServerStreamingCallSettings.Builder<Query, Row> readRowsSettings() {
      return readRowsSettings;
    }

    /** Returns the builder for the settings used for point reads using readRow. */
    public UnaryCallSettings.Builder<Query, Row> readRowSettings() {
      return readRowSettings;
    }

    /** Returns the builder for the settings used for calls to SampleRowKeysSettings. */
    public UnaryCallSettings.Builder<String, List<KeyOffset>> sampleRowKeysSettings() {
      return sampleRowKeysSettings;
    }

    /** Returns the builder for the settings used for calls to MutateRow. */
    public UnaryCallSettings.Builder<RowMutation, Void> mutateRowSettings() {
      return mutateRowSettings;
    }

    /** Returns the builder for the settings used for calls to MutateRows. */
    public BigtableBatchingCallSettings.Builder bulkMutateRowsSettings() {
      return bulkMutateRowsSettings;
    }

    /** Returns the builder for the settings used for calls to MutateRows. */
    public BigtableBulkReadRowsCallSettings.Builder bulkReadRowsSettings() {
      return bulkReadRowsSettings;
    }

    /** Returns the builder for the settings used for calls to CheckAndMutateRow. */
    public UnaryCallSettings.Builder<ConditionalRowMutation, Boolean> checkAndMutateRowSettings() {
      return checkAndMutateRowSettings;
    }

    /** Returns the builder with the settings used for calls to ReadModifyWriteRow. */
    public UnaryCallSettings.Builder<ReadModifyWriteRow, Row> readModifyWriteRowSettings() {
      return readModifyWriteRowSettings;
    }

    @SuppressWarnings("unchecked")
    public EnhancedBigtableStubSettings build() {
      Preconditions.checkState(projectId != null, "Project id must be set");
      Preconditions.checkState(instanceId != null, "Instance id must be set");

      if (isRefreshingChannel) {
        Preconditions.checkArgument(
            getTransportChannelProvider() instanceof InstantiatingGrpcChannelProvider,
            "refreshingChannel only works with InstantiatingGrpcChannelProviders");
      }
      return new EnhancedBigtableStubSettings(this);
    }
    // </editor-fold>
  }
}
