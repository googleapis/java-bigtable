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

  /**
   * Versions of the bigtable client stats.
   * <li>CLASSIC: include operation latencies, attempt latencies, completed ops, read first row
   *     latencies, attempts per op. Metrics are tagged with project id, instance id, app profile
   *     id, operation name and status.
   *
   *     <p>GFE: include GFE related metrics, gfe latencies and missing gfe header count. Metrics
   *     are tagged with project id, instance id, app profile id, operation name and status.
   *
   *     <p>ALL: include both BASIC and GFE metrics.
   *
   *     <p>CLASSIC_EXTRA_LABELS: same metrics as BASIC but tagged with extra labels including table
   *     id, zone id and cluster id.
   *
   *     <p>GFE_EXTRA_LABELS: same metrics as GFE but tagged with extra labels including table id,
   *     zone id and cluster id. When a request failed before it gets to the cluster, cluster id will be
   *     tagged with "unspecified" and zone id will be "global".
   *
   *     <p>ALL_EXTRA_LABELS: include both BASIC_EXTRA_LABELS and GFE_EXTRA_LABELS metrics.
   */
  public enum MetricVersion {
    BASIC,
    GFE,
    ALL,
    BASIC_EXTRA_LABELS,
    GFE_EXTRA_LABELS,
    ALL_EXTRA_LABELS
  }

  private static boolean gfeMetricsRegistered = false;

  /** Registers Bigtable specific BASIC views. */
  public static void registerBigtableClientViews() {
    registerBigtableClientViews(Stats.getViewManager(), MetricVersion.BASIC);
  }

  /**
   * Register views for GFE metrics, including gfe_latency and gfe_header_missing_count. gfe_latency
   * measures the latency between Google's network receives an RPC and reads back the first byte of
   * the response. gfe_header_missing_count is a counter of the number of RPC responses without a
   * server-timing header.
   */
  public static void registerBigtableClientGfeViews() {
    registerBigtableClientViews(Stats.getViewManager(), MetricVersion.GFE);
  }

  /** Register Bigtable client OpenCensus views with the given {@link MetricVersion}. **/
  public static void registerBigtableClientViews(MetricVersion metricVersion) {
    registerBigtableClientViews(Stats.getViewManager(), metricVersion);
  }

  @VisibleForTesting
  static void registerBigtableClientViews(ViewManager viewManager, MetricVersion metricVersion) {
    switch (metricVersion) {
      case BASIC:
        registerBigtableClientBasicViews(viewManager, false);
        break;
      case BASIC_EXTRA_LABELS:
        registerBigtableClientBasicViews(viewManager, true);
        break;
      case GFE:
        registerBigtableClientGfeViews(viewManager, false);
        break;
      case GFE_EXTRA_LABELS:
        registerBigtableClientGfeViews(viewManager, true);
        break;
      case ALL:
        registerBigtableClientBasicViews(viewManager, false);
        registerBigtableClientGfeViews(viewManager, false);
        break;
      case ALL_EXTRA_LABELS:
        registerBigtableClientBasicViews(viewManager, true);
        registerBigtableClientGfeViews(viewManager, true);
        break;
    }
  }

  private static void registerBigtableClientBasicViews(
      ViewManager viewManager, boolean withExtraTags) {
    viewManager.registerView(RpcViewConstants.createOpLatencyView(withExtraTags));
    viewManager.registerView(RpcViewConstants.createCompletedOpsView(withExtraTags));
    viewManager.registerView(RpcViewConstants.createReadRowsFirstResponseView(withExtraTags));
    viewManager.registerView(RpcViewConstants.createAttemptLatencyView(withExtraTags));
    viewManager.registerView(RpcViewConstants.createAttemptsPerOpView(withExtraTags));
    viewManager.registerView(RpcViewConstants.createBatchThrottledTimeView(withExtraTags));
  }

  private static void registerBigtableClientGfeViews(
      ViewManager viewManager, boolean withExtraTags) {
    gfeMetricsRegistered = true;
    viewManager.registerView(RpcViewConstants.createGfeLatencyView(withExtraTags));
    viewManager.registerView(RpcViewConstants.createGfeMissingHeaderView(withExtraTags));
  }

  static boolean isGfeMetricsRegistered() {
    return gfeMetricsRegistered;
  }
}
