/*
 * Copyright 2019 Google LLC
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
import com.google.api.gax.batching.BatchingDescriptor;
import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.UnaryCallSettings;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This settings holds the batching thresholds as well as retry configuration.
 *
 * <p>Sample configuration:
 *
 * <pre>{@code
 * BigtableBatchingCallSettings defaultBatchingCallSettings =
 *     bigtableDataCallSettings.getStubSettings().bulkMutateRowsSettings();
 *
 * BigtableBatchingCallSettings customBatchingCallSettings = defaultBatchingCallSettings.toBuilder()
 *     .setBatchingSettings(
 *         defaultBatchingCallSettings.getBatchingSettings().toBuilder()
 *             .setDelayThreshold(Duration.ofSeconds(10))
 *             .build())
 *     .setRetryableCodes(Code.DEADLINE_EXCEEDED)
 *     .setLatencyBasedThrottling(true, 1000L)
 *     .build();
 * }</pre>
 *
 * @see BatchingSettings for batching thresholds explantion.
 * @see RetrySettings for retry configuration.
 */
@BetaApi("This surface is likely to change as the batching surface evolves.")
public final class BigtableBatchingCallSettings extends UnaryCallSettings<BulkMutation, Void> {

  // This settings is just a simple wrapper for BatchingCallSettings to allow us to add
  // additional functionality.
  private BatchingCallSettings<RowMutationEntry, Void, BulkMutation, Void> batchingCallSettings;
  private boolean isLatencyBasedThrottlingEnabled;
  private Long targetRpcLatencyMs;

  private BigtableBatchingCallSettings(Builder builder) {
    super(builder);
    batchingCallSettings =
        BatchingCallSettings.newBuilder(builder.batchingDescriptor)
            .setBatchingSettings(builder.batchingSettings)
            .setRetrySettings(builder.getRetrySettings())
            .setRetryableCodes(builder.getRetryableCodes())
            .build();
    this.isLatencyBasedThrottlingEnabled = builder.isLatencyBasedThrottlingEnabled;
    this.targetRpcLatencyMs = builder.targetRpcLatencyMs;
  }

  /** Returns batching settings which contains multiple batch threshold levels. */
  public BatchingSettings getBatchingSettings() {
    return batchingCallSettings.getBatchingSettings();
  }

  /** Returns an adapter that packs and unpacks batching elements. */
  BatchingDescriptor<RowMutationEntry, Void, BulkMutation, Void> getBatchingDescriptor() {
    return batchingCallSettings.getBatchingDescriptor();
  }

  /** Gets if latency based throttling is enabled. */
  public boolean isLatencyBasedThrottlingEnabled() {
    return isLatencyBasedThrottlingEnabled;
  }

  /** Returns target rpc latency for bulk mutations if latency based throttling is enabled. */
  @Nullable
  Long getTargetRpcLatencyMs() {
    return targetRpcLatencyMs;
  }

  static Builder newBuilder(
      BatchingDescriptor<RowMutationEntry, Void, BulkMutation, Void> batchingDescriptor) {
    return new Builder(batchingDescriptor);
  }

  /**
   * Get a builder with the same values as this object. See the class documentation of {@link
   * BigtableBatchingCallSettings} for a sample settings configuration.
   */
  @Override
  public final Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("batchingCallSettings", batchingCallSettings)
        .add("isLatencyBasedThrottlingEnabled", isLatencyBasedThrottlingEnabled)
        .add("targetRpcLatency", targetRpcLatencyMs)
        .toString();
  }

  /**
   * A base builder class for {@link BigtableBatchingCallSettings}. See the class documentation of
   * {@link BigtableBatchingCallSettings} for a description of the different values that can be set.
   */
  public static class Builder extends UnaryCallSettings.Builder<BulkMutation, Void> {

    private BatchingDescriptor<RowMutationEntry, Void, BulkMutation, Void> batchingDescriptor;
    private BatchingSettings batchingSettings;
    private boolean isLatencyBasedThrottlingEnabled;
    private Long targetRpcLatencyMs;

    private Builder(
        @Nonnull
            BatchingDescriptor<RowMutationEntry, Void, BulkMutation, Void> batchingDescriptor) {
      this.batchingDescriptor =
          Preconditions.checkNotNull(batchingDescriptor, "batching descriptor can't be null");
    }

    private Builder(@Nonnull BigtableBatchingCallSettings settings) {
      super(settings);
      this.batchingDescriptor = settings.getBatchingDescriptor();
      this.batchingSettings = settings.getBatchingSettings();
      this.isLatencyBasedThrottlingEnabled = settings.isLatencyBasedThrottlingEnabled();
      this.targetRpcLatencyMs = settings.getTargetRpcLatencyMs();
    }

    /** Sets the batching settings with various thresholds. */
    public Builder setBatchingSettings(@Nonnull BatchingSettings batchingSettings) {
      Preconditions.checkNotNull(batchingSettings, "batching settings can't be null");
      this.batchingSettings = batchingSettings;
      return this;
    }

    /** Returns the {@link BatchingSettings}. */
    public BatchingSettings getBatchingSettings() {
      return batchingSettings;
    }

    /** Sets the rpc failure {@link StatusCode.Code code}, for which retries should be performed. */
    @Override
    public Builder setRetryableCodes(StatusCode.Code... codes) {
      super.setRetryableCodes(codes);
      return this;
    }

    /** Sets the rpc failure {@link StatusCode.Code code}, for which retries should be performed. */
    @Override
    public Builder setRetryableCodes(Set<StatusCode.Code> retryableCodes) {
      super.setRetryableCodes(retryableCodes);
      return this;
    }

    /** Sets the {@link RetrySettings} values for each retry attempts. */
    @Override
    public Builder setRetrySettings(@Nonnull RetrySettings retrySettings) {
      super.setRetrySettings(retrySettings);
      return this;
    }

    /**
     * Enables / disables latency based throttling. If enabling the setting, targetRpcLatency needs
     * to be set.
     */
    public Builder setLatencyBasedThrottling(
        boolean isLatencyBasedThrottlingEnabled, @Nullable Long targetRpcLatency) {
      Preconditions.checkArgument(
          !isLatencyBasedThrottlingEnabled || targetRpcLatency != null,
          "target RPC latency must be set if latency based throttling is enabled");
      Preconditions.checkArgument(
          targetRpcLatency == null || targetRpcLatency > 0,
          "if target RPC latency is set, it must be greater than 0");
      this.isLatencyBasedThrottlingEnabled = isLatencyBasedThrottlingEnabled;
      this.targetRpcLatencyMs = isLatencyBasedThrottlingEnabled ? targetRpcLatency : null;
      return this;
    }

    /** Gets target rpc latency if latency based throttling is enabled. Otherwise return null. */
    @Nullable
    public Long getTargetRpcLatencyMs() {
      return isLatencyBasedThrottlingEnabled ? targetRpcLatencyMs : null;
    }

    /** Gets if latency based throttling is enabled. */
    public boolean isLatencyBasedThrottlingEnabled() {
      return this.isLatencyBasedThrottlingEnabled;
    }

    /** Builds the {@link BigtableBatchingCallSettings} object with provided configuration. */
    @Override
    public BigtableBatchingCallSettings build() {
      return new BigtableBatchingCallSettings(this);
    }
  }
}
