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

import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelInsight;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelInsightsProvider;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class OutstandingRpcsMetricTracker implements Runnable {
  private static final int SAMPLING_PERIOD_SECONDS = 60;
  private final LongHistogram outstandingRpcsHistogram;
  private final Attributes baseAttributes;
  private final AtomicReference<BigtableChannelInsightsProvider>
      bigtableChannelInsightsProviderRef = new AtomicReference<>();

  public OutstandingRpcsMetricTracker(OpenTelemetry openTelemetry, String lbPolicy) {
    Meter meter = openTelemetry.getMeter(METER_NAME);
    this.outstandingRpcsHistogram =
        meter
            .histogramBuilder(OUTSTANDING_RPCS_PER_CHANNEL_NAME)
            .ofLongs()
            .setDescription(
                "A distribution of the number of outstanding RPCs per connection in the client pool, sampled periodically.")
            .setUnit("1")
            .build();

    this.baseAttributes =
        Attributes.builder().put("transport_type", "grpc").put("lb_policy", lbPolicy).build();
  }

  /**
   * Registers the provider for the channel pool entries. This should be called by the component
   * that creates the BigtableChannelPool.
   */
  public void registerChannelInsightsProvider(
      BigtableChannelInsightsProvider channelInsightsProvider) {
    this.bigtableChannelInsightsProviderRef.set(channelInsightsProvider);
  }

  /** Starts the periodic collection. */
  public ScheduledFuture<?> start(ScheduledExecutorService scheduler) {
    return scheduler.scheduleAtFixedRate(
        this, SAMPLING_PERIOD_SECONDS, SAMPLING_PERIOD_SECONDS, TimeUnit.SECONDS);
  }

  @Override
  public void run() {
    BigtableChannelInsightsProvider channelInsightsProvider =
        bigtableChannelInsightsProviderRef.get();
    if (channelInsightsProvider == null) {
      return; // Not registered yet
    }
    List<? extends BigtableChannelInsight> channelInsights =
        channelInsightsProvider.getChannelInfos();
    if (channelInsights == null || channelInsights.isEmpty()) {
      return;
    }
    for (BigtableChannelInsight info : channelInsights) {
      long currentOutstandingUnaryRpcs = info.getOutstandingStreamingRpcs();
      long currentOutstandingStreamingRpcs = info.getOutstandingStreamingRpcs();
      // Record outstanding unary RPCs with streaming=false
      Attributes unaryAttributes = baseAttributes.toBuilder().put("streaming", false).build();
      outstandingRpcsHistogram.record(currentOutstandingUnaryRpcs, unaryAttributes);

      // Record outstanding streaming RPCs with streaming=true
      Attributes streamingAttributes = baseAttributes.toBuilder().put("streaming", true).build();
      outstandingRpcsHistogram.record(currentOutstandingStreamingRpcs, streamingAttributes);
    }
  }
}
