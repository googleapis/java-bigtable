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
import com.google.api.gax.batching.DynamicFlowControlSettings;
import com.google.api.gax.batching.FlowControlEventStats;
import com.google.api.gax.batching.FlowController;
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
  private Long targetRpcLatency;
  private FlowController flowController;
  private FlowControlEventStats flowControlEvents;
  private DynamicFlowControlStats dynamicFlowControlStats;

  private BigtableBatchingCallSettings(Builder builder) {
    super(builder);
    batchingCallSettings =
        BatchingCallSettings.newBuilder(builder.batchingDescriptor)
            .setBatchingSettings(builder.batchingSettings)
            .setRetrySettings(builder.getRetrySettings())
            .setRetryableCodes(builder.getRetryableCodes())
            .build();
    this.isLatencyBasedThrottlingEnabled = builder.isLatencyBasedThrottlingEnabled;
    this.targetRpcLatency = builder.targetRpcLatency;
    this.flowController = builder.flowController;
    this.flowControlEvents = builder.flowControlEvents;
    this.dynamicFlowControlStats = builder.dynamicFlowControlStats;
  }

  /** Returns batching settings which contains multiple batch threshold levels. */
  public BatchingSettings getBatchingSettings() {
    return batchingCallSettings.getBatchingSettings();
  }

  /** Returns an adapter that packs and unpacks batching elements. */
  BatchingDescriptor<RowMutationEntry, Void, BulkMutation, Void> getBatchingDescriptor() {
    return batchingCallSettings.getBatchingDescriptor();
  }

  public boolean isLatencyBasedThrottlingEnabled() {
    return isLatencyBasedThrottlingEnabled;
  }

  @Nullable
  Long getTargetRpcLatency() {
    return targetRpcLatency;
  }

  FlowController getFlowController() {
    return flowController;
  }

  FlowControlEventStats getFlowControlEvents() {
    return flowControlEvents;
  }

  DynamicFlowControlStats getDynamicFlowControlStats() {
    return dynamicFlowControlStats;
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
        .add("targetRpcLatency", targetRpcLatency)
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
    private Long targetRpcLatency;
    private FlowController flowController;
    private FlowControlEventStats flowControlEvents;
    private DynamicFlowControlStats dynamicFlowControlStats;

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
      this.targetRpcLatency = settings.getTargetRpcLatency();
      this.flowController = settings.getFlowController();
      this.flowControlEvents = settings.getFlowControlEvents();
      this.dynamicFlowControlStats = settings.getDynamicFlowControlStats();
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
     * Enable / disable latency based throttling. If enabling the setting, targetRpcLatency needs to
     * be set.
     */
    public Builder setLatencyBasedThrottling(
        boolean isLatencyBasedThrottlingEnabled, @Nullable Long targetRpcLatency) {
      Preconditions.checkState(
          this.batchingSettings != null,
          "batchingSettings must be set before enabling / disabling throttling");
      Preconditions.checkArgument(
          !isLatencyBasedThrottlingEnabled || targetRpcLatency != null,
          "target RPC latency must be set if latency based throttling is enabled");
      Preconditions.checkArgument(
          targetRpcLatency == null || targetRpcLatency > 0,
          "if target RPC latency is set, it must be greater than 0");
      this.isLatencyBasedThrottlingEnabled = isLatencyBasedThrottlingEnabled;
      this.targetRpcLatency = isLatencyBasedThrottlingEnabled ? targetRpcLatency : null;
      if (isLatencyBasedThrottlingEnabled) {
        // Set up a flow controller with DynamicFlowControlSettings.
        Long maxThrottlingElementCount =
            batchingSettings.getFlowControlSettings().getMaxOutstandingElementCount();
        Long maxThrottlingRequestByteCount =
            batchingSettings.getFlowControlSettings().getMaxOutstandingRequestBytes();
        if (maxThrottlingElementCount == null) {
          long maxBulkMutateElementPerBatch = 100L;
          long defaultChannelPoolSize = 2 * Runtime.getRuntime().availableProcessors();
          maxThrottlingElementCount =
              Math.min(20_000L, 10L * maxBulkMutateElementPerBatch * defaultChannelPoolSize);
        }
        if (maxThrottlingRequestByteCount == null) {
          maxThrottlingRequestByteCount = 100L * 1024 * 1024;
        }

        long initialElementCount = maxThrottlingElementCount / 5;
        long minElementCount = maxThrottlingElementCount / 100;
        if (batchingSettings.getElementCountThreshold() != null) {
          initialElementCount =
              Math.max(initialElementCount, batchingSettings.getElementCountThreshold());
          minElementCount = Math.max(minElementCount, batchingSettings.getElementCountThreshold());
        }
        DynamicFlowControlSettings dynamicFlowControlSettings =
            DynamicFlowControlSettings.newBuilder()
                .setInitialOutstandingElementCount(initialElementCount)
                .setMaxOutstandingElementCount(maxThrottlingElementCount)
                .setMinOutstandingElementCount(minElementCount)
                .setInitialOutstandingRequestBytes(maxThrottlingRequestByteCount)
                .setMinOutstandingRequestBytes(maxThrottlingRequestByteCount)
                .setMaxOutstandingRequestBytes(maxThrottlingRequestByteCount)
                .build();
        this.flowController = new FlowController(dynamicFlowControlSettings);
      } else {
        this.flowController = new FlowController(batchingSettings.getFlowControlSettings());
      }
      return this;
    }

    /**
     * Get target rpc latency used in latency based throttling. Return null if dynamic throttling is
     * disabled.
     */
    @Nullable
    public Long getTargetRpcLatency() {
      if (isLatencyBasedThrottlingEnabled) {
        return this.targetRpcLatency;
      } else {
        return null;
      }
    }

    /** Return if latency based throttling is enabled */
    public boolean isLatencyBasedThrottlingEnabled() {
      return this.isLatencyBasedThrottlingEnabled;
    }

    public FlowController getFlowController() {
      return flowController;
    }

    public FlowControlEventStats getFlowControlEvents() {
      return flowControlEvents;
    }

    public DynamicFlowControlStats getDynamicFlowControlStats() {
      return dynamicFlowControlStats;
    }

    /** Builds the {@link BigtableBatchingCallSettings} object with provided configuration. */
    @Override
    public BigtableBatchingCallSettings build() {
      Preconditions.checkState(batchingSettings != null, "batchingSettings must be set");
      if (flowController == null) {
        this.flowController = new FlowController(batchingSettings.getFlowControlSettings());
      }
      if (flowControlEvents == null) {
        this.flowControlEvents = new FlowControlEventStats();
      }
      if (dynamicFlowControlStats == null) {
        this.dynamicFlowControlStats = new DynamicFlowControlStats();
      }
      return new BigtableBatchingCallSettings(this);
    }
  }
}
