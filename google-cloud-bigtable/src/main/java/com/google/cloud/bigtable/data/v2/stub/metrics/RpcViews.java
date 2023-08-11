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

import com.google.api.core.BetaApi;
import com.google.common.annotations.VisibleForTesting;
import io.opencensus.stats.Stats;
import io.opencensus.stats.ViewManager;

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

  public static void registerAllBigtableClientViewsWithExtraTags() {
    registerViewsWithExtraTags(Stats.getViewManager());
  }

  @VisibleForTesting
  static void registerBigtableClientViews(ViewManager viewManager) {
    viewManager.registerView(RpcViewConstants.createOpLatencyView(false));
    viewManager.registerView(RpcViewConstants.createCompletedOpsView(false));
    viewManager.registerView(RpcViewConstants.createReadRowsFirstResponseView(false));
    viewManager.registerView(RpcViewConstants.createAttemptLatencyView(false));
    viewManager.registerView(RpcViewConstants.createAttemptsPerOpView(false));
    viewManager.registerView(RpcViewConstants.createBatchThrottledTimeView(false));
  }

  @VisibleForTesting
  static void registerBigtableClientGfeViews(ViewManager viewManager) {
    gfeMetricsRegistered = true;
    viewManager.registerView(RpcViewConstants.createGfeLatencyView(false));
    viewManager.registerView(RpcViewConstants.createGfeMissingHeaderView(false));
  }

  static boolean isGfeMetricsRegistered() {
    return gfeMetricsRegistered;
  }

  @VisibleForTesting
  static void setGfeMetricsRegistered(boolean gfeMetricsRegistered) {
    RpcViews.gfeMetricsRegistered = gfeMetricsRegistered;
  }

  static void registerViewsWithExtraTags(ViewManager viewManager) {
    viewManager.registerView(RpcViewConstants.createOpLatencyView(true));
    viewManager.registerView(RpcViewConstants.createCompletedOpsView(true));
    viewManager.registerView(RpcViewConstants.createReadRowsFirstResponseView(true));
    viewManager.registerView(RpcViewConstants.createAttemptLatencyView(true));
    viewManager.registerView(RpcViewConstants.createAttemptsPerOpView(true));
    viewManager.registerView(RpcViewConstants.createGfeLatencyView(true));
    viewManager.registerView(RpcViewConstants.createGfeMissingHeaderView(true));
    viewManager.registerView(RpcViewConstants.createBatchThrottledTimeView(true));
  }
}
