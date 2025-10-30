/*
 * Copyright 2025 Google LLC
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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.METER_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.OUTSTANDING_RPCS_PER_CHANNEL_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.PER_CONNECTION_ERROR_COUNT_NAME;

import com.google.api.core.InternalApi;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelObserver;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelPoolObserver;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@InternalApi("For internal use only")
public class ChannelPoolMetricsTracer implements Runnable {
  private static final Logger logger = Logger.getLogger(ChannelPoolMetricsTracer.class.getName());

  private static final int SAMPLING_PERIOD_SECONDS = 60;
  private final LongHistogram outstandingRpcsHistogram;
  private final LongHistogram perConnectionErrorCountHistogram;

  private final AtomicReference<BigtableChannelPoolObserver> bigtableChannelInsightsProviderRef =
      new AtomicReference<>();
  private final AtomicReference<String> lbPolicyRef = new AtomicReference<>("ROUND_ROBIN");
  private final Attributes commonAttrs;

  // Attributes for unary and streaming RPCs, built on demand in run()
  @Nullable private Attributes unaryAttributes;
  @Nullable private Attributes streamingAttributes;

  public ChannelPoolMetricsTracer(OpenTelemetry openTelemetry, Attributes commonAttrs) {
    Meter meter = openTelemetry.getMeter(METER_NAME);
    this.commonAttrs = commonAttrs;
    this.outstandingRpcsHistogram =
        meter
            .histogramBuilder(OUTSTANDING_RPCS_PER_CHANNEL_NAME)
            .ofLongs()
            .setDescription(
                "A distribution of the number of outstanding RPCs per connection in the client pool, sampled periodically.")
            .setUnit("1")
            .build();

    this.perConnectionErrorCountHistogram =
        meter
            .histogramBuilder(PER_CONNECTION_ERROR_COUNT_NAME)
            .ofLongs()
            .setDescription("Distribution of counts of channels per 'error count per minute'.")
            .setUnit("1")
            .build();
  }

  /**
   * Registers the provider for the channel pool entries. This should be called by the component
   * that creates the BigtableChannelPool.
   */
  public void registerChannelInsightsProvider(BigtableChannelPoolObserver channelInsightsProvider) {
    this.bigtableChannelInsightsProviderRef.set(channelInsightsProvider);
  }

  /** Register the current lb policy * */
  public void registerLoadBalancingStrategy(String lbPolicy) {
    this.lbPolicyRef.set(lbPolicy);
  }

  /** Starts the periodic collection. */
  public ScheduledFuture<?> start(ScheduledExecutorService scheduler) {
    return scheduler.scheduleAtFixedRate(
        this, SAMPLING_PERIOD_SECONDS, SAMPLING_PERIOD_SECONDS, TimeUnit.SECONDS);
  }

  @Override
  public void run() {
    BigtableChannelPoolObserver channelInsightsProvider = bigtableChannelInsightsProviderRef.get();
    if (channelInsightsProvider == null) {
      logger.warning("No Bigtable ChannelPoolObserver available");
      return; // Not registered yet
    }
    String lbPolicy = lbPolicyRef.get();

    // Build attributes if they haven't been built yet.
    if (unaryAttributes == null || streamingAttributes == null) {
      Attributes baseAttrs = commonAttrs.toBuilder().put("lb_policy", lbPolicy).build();
      this.unaryAttributes = baseAttrs.toBuilder().put("streaming", false).build();
      this.streamingAttributes = baseAttrs.toBuilder().put("streaming", true).build();
    }
    List<? extends BigtableChannelObserver> channelInsights =
        channelInsightsProvider.getChannelInfos();
    if (channelInsights == null || channelInsights.isEmpty()) {
      return;
    }
    for (BigtableChannelObserver info : channelInsights) {
      String transportTypeValue = info.isAltsChannel() ? "DIRECTPATH" : "CLOUDPATH";
      this.unaryAttributes =
          this.unaryAttributes.toBuilder().put("transport_type", transportTypeValue).build();
      this.streamingAttributes =
          this.streamingAttributes.toBuilder().put("transport_type", transportTypeValue).build();

      long currentOutstandingUnaryRpcs = info.getOutstandingUnaryRpcs();
      long currentOutstandingStreamingRpcs = info.getOutstandingStreamingRpcs();
      // Record outstanding unary RPCs with streaming=false
      outstandingRpcsHistogram.record(currentOutstandingUnaryRpcs, unaryAttributes);
      // Record outstanding streaming RPCs with streaming=true
      outstandingRpcsHistogram.record(currentOutstandingStreamingRpcs, streamingAttributes);

      long errors = info.getAndResetErrorCount();
      perConnectionErrorCountHistogram.record(errors, commonAttrs);
    }
  }
}
