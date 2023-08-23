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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.ATTEMPTS_PER_OP_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.ATTEMPT_LATENCY_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.BATCH_THROTTLED_TIME_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.BIGTABLE_ATTEMPTS_PER_OP_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.BIGTABLE_ATTEMPT_LATENCY_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.BIGTABLE_BATCH_THROTTLED_TIME_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.BIGTABLE_COMPLETED_OP_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.BIGTABLE_GFE_LATENCY_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.BIGTABLE_OP_LATENCY_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.BIGTABLE_READ_ROWS_FIRST_ROW_LATENCY_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.COMPLETED_OP_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.GFE_LATENCY_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.GFE_MISSING_HEADER_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.OP_LATENCY_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.READ_ROWS_FIRST_ROW_LATENCY_SELECTOR;

import com.google.api.core.BetaApi;
import com.google.common.annotations.VisibleForTesting;
import io.opencensus.stats.Stats;
import io.opencensus.stats.ViewManager;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;

@BetaApi
public class RpcViews {

  private static boolean gfeMetricsRegistered = false;

  /** Registers all Bigtable specific views. */
  public static void registerBigtableClientViews() {
    registerBigtableClientViews(Stats.getViewManager());
  }

  /**
   * Register views for GFE metrics, including gfe_latency and gfe_header_missing_count. gfe_latency
   * measures the latency between Google's network receives an RPC and reads back the first byte of
   * the response. gfe_header_missing_count is a counter of the number of RPC responses without a
   * server-timing header.
   */
  public static void registerBigtableClientGfeViews() {
    registerBigtableClientGfeViews(Stats.getViewManager());
  }

  public static void registerBigtableClientViews(SdkMeterProviderBuilder builder) {
    builder
        .registerView(OP_LATENCY_SELECTOR, BIGTABLE_OP_LATENCY_VIEW)
        .registerView(ATTEMPT_LATENCY_SELECTOR, BIGTABLE_ATTEMPT_LATENCY_VIEW)
        .registerView(
            READ_ROWS_FIRST_ROW_LATENCY_SELECTOR, BIGTABLE_READ_ROWS_FIRST_ROW_LATENCY_VIEW)
        .registerView(COMPLETED_OP_SELECTOR, BIGTABLE_COMPLETED_OP_VIEW)
        .registerView(ATTEMPTS_PER_OP_SELECTOR, BIGTABLE_ATTEMPTS_PER_OP_VIEW)
        .registerView(BATCH_THROTTLED_TIME_SELECTOR, BIGTABLE_BATCH_THROTTLED_TIME_VIEW);
  }

  public static void regsiterBigtableClientGfeViews(SdkMeterProviderBuilder builder) {
    builder
        .registerView(GFE_LATENCY_SELECTOR, BIGTABLE_GFE_LATENCY_VIEW)
        .registerView(GFE_MISSING_HEADER_SELECTOR, BIGTABLE_GFE_HEADER_MISSING_COUNT_VIEW);
  }

  @VisibleForTesting
  static void registerBigtableClientViews(ViewManager viewManager) {}

  @VisibleForTesting
  static void registerBigtableClientGfeViews(ViewManager viewManager) {}

  static boolean isGfeMetricsRegistered() {
    return gfeMetricsRegistered;
  }

  @VisibleForTesting
  static void setGfeMetricsRegistered(boolean gfeMetricsRegistered) {
    RpcViews.gfeMetricsRegistered = gfeMetricsRegistered;
  }
}
