/*
 * Copyright 2020 Google LLC
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
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;

/**
 * This settings holds the batching thresholds as well as retry configuration for bulk read API.
 *
 * <p>Sample configuration:
 *
 * <pre>{@code
 * BigtableBulkReadRowsCallSettings defaultBulkReadCallSettings =
 *     bigtableDataCallSettings.getStubSettings().bulkReadRowsSettings();
 *
 * BigtableBulkReadRowsCallSettings customBulkReadCallSettings = defaultBulkReadCallSettings
 *     .toBuilder()
 *     .setBatchingSettings(
 *         defaultBulkReadCallSettings.getBatchingSettings().toBuilder()
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
public class BigtableBulkReadRowsCallSettings extends UnaryCallSettings<Query, List<Row>> {

  private final BatchingCallSettings<ByteString, Row, Query, List<Row>> batchingCallSettings;

  private BigtableBulkReadRowsCallSettings(Builder builder) {
    super(builder);
    batchingCallSettings =
        BatchingCallSettings.newBuilder(builder.batchingDescriptor)
            .setBatchingSettings(builder.batchingSettings)
            .setRetrySettings(builder.getRetrySettings())
            .setRetryableCodes(builder.getRetryableCodes())
            .build();
  }

  /** Returns batching settings which contains multiple batch threshold levels. */
  public BatchingSettings getBatchingSettings() {
    return batchingCallSettings.getBatchingSettings();
  }

  /** Returns an adapter that packs and unpacks batching elements. */
  BatchingDescriptor<ByteString, Row, Query, List<Row>> getBatchingDescriptor() {
    return batchingCallSettings.getBatchingDescriptor();
  }

  static BigtableBulkReadRowsCallSettings.Builder newBuilder(
      BatchingDescriptor<ByteString, Row, Query, List<Row>> batchingDescriptor) {
    return new Builder(batchingDescriptor);
  }

  /**
   * Get a builder with the same values as this object. See the class documentation of {@link
   * BigtableBatchingCallSettings} for a sample settings configuration.
   */
  @Override
  public final BigtableBulkReadRowsCallSettings.Builder toBuilder() {
    return new BigtableBulkReadRowsCallSettings.Builder(this);
  }

  public static class Builder extends UnaryCallSettings.Builder<Query, List<Row>> {

    private BatchingDescriptor<ByteString, Row, Query, List<Row>> batchingDescriptor;
    private BatchingSettings batchingSettings;

    private Builder(
        @Nonnull BatchingDescriptor<ByteString, Row, Query, List<Row>> batchingDescriptor) {
      this.batchingDescriptor =
          Preconditions.checkNotNull(batchingDescriptor, "batching descriptor can't be null");
    }

    private Builder(@Nonnull BigtableBulkReadRowsCallSettings settings) {
      super(settings);
      this.batchingDescriptor = settings.getBatchingDescriptor();
      this.batchingSettings = settings.getBatchingSettings();
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

    /** Builds the {@link BigtableBulkReadRowsCallSettings} object with provided configuration. */
    @Override
    public BigtableBulkReadRowsCallSettings build() {
      return new BigtableBulkReadRowsCallSettings(this);
    }
  }
}
