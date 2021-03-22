/*
 * Copyright 2021 Google LLC
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

import com.google.api.core.ApiFuture;
import com.google.api.gax.batching.FlowControlEventStats;
import com.google.api.gax.batching.FlowController;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

/**
 * A callable that records rpc latency and adjusts flow control thresholds for latency based
 * throttling.
 */
final class DynamicFlowControlCallable extends UnaryCallable {
  private final FlowController flowController;
  private final FlowControlEventStats flowControlEvents;
  private final DynamicFlowControlStats dynamicFlowControlStats;
  private final long targetLatencyMs;
  private final long adjustingIntervalMs;
  private final UnaryCallable innerCallable;

  DynamicFlowControlCallable(
      @Nonnull UnaryCallable innerCallable,
      @Nonnull FlowController flowController,
      @Nonnull FlowControlEventStats flowControlEvents,
      @Nonnull DynamicFlowControlStats stats,
      long targetLatencyMs,
      long adjustingIntervalMs) {
    this.innerCallable = innerCallable;
    this.flowController = flowController;
    this.flowControlEvents = flowControlEvents;
    this.dynamicFlowControlStats = stats;
    this.targetLatencyMs = targetLatencyMs;
    this.adjustingIntervalMs = adjustingIntervalMs;
  }

  @Override
  public ApiFuture futureCall(Object request, ApiCallContext context) {
    Runnable flowControllerRunnable =
        new DynamicFlowControlRunnable(
            flowController,
            flowControlEvents,
            dynamicFlowControlStats,
            targetLatencyMs,
            adjustingIntervalMs);
    ApiFuture future = innerCallable.futureCall(request, context);
    future.addListener(flowControllerRunnable, MoreExecutors.directExecutor());
    return future;
  }

  class DynamicFlowControlRunnable implements Runnable {
    private final FlowController flowController;
    private final FlowControlEventStats flowControlEvents;
    private final DynamicFlowControlStats dynamicFlowControlStats;
    private final long targetLatency;
    private final long adjustingIntervalMs;
    private Stopwatch timer;

    private final long slowIncreaseStep;
    private final long fastIncreaseStep;
    private final long slowDecreaseStep;
    private final long fastDecreaseStep;
    private final double highTargetLatencyMs;
    private final double lowTargetLatencyMs;
    private final double highLatencyMs;
    private final double lowParallelismLatencyMs;
    private final double lowParallelismLimit;
    private final long throttlingEventTimeRangeMs;

    DynamicFlowControlRunnable(
        @Nonnull FlowController flowController,
        @Nonnull FlowControlEventStats flowControlStats,
        @Nonnull DynamicFlowControlStats stats,
        long targetLatency,
        long adjustingIntervalMs) {
      this.flowController = flowController;
      this.flowControlEvents = flowControlStats;
      this.dynamicFlowControlStats = stats;
      this.targetLatency = targetLatency;
      this.adjustingIntervalMs = adjustingIntervalMs;
      timer = Stopwatch.createStarted();

      // Defining adjusting criteria and adjusting steps
      // targeting roughly 20% around target latency so there isn't too much churn
      highTargetLatencyMs = targetLatencyMs * 1.2;
      lowTargetLatencyMs = targetLatencyMs * 0.8;
      // when latency is too high, decreasing the thresholds faster
      highLatencyMs = targetLatencyMs * 3;
      long maxElementCountLimit = flowController.getMaxElementCountLimit();
      // make sure the parallelism is not too low
      lowParallelismLimit = 0.05 * maxElementCountLimit;
      lowParallelismLatencyMs = targetLatency * 2;
      // Increase parallelism at a slower rate than decrease. The lower rate should help the system
      // maintain stability.
      slowIncreaseStep = Math.round(0.02 * maxElementCountLimit);
      fastIncreaseStep = Math.round(0.05 * maxElementCountLimit);
      slowDecreaseStep = Math.round(0.1 * maxElementCountLimit);
      fastDecreaseStep = Math.round(0.3 * maxElementCountLimit);
      // only look for throttling events in the past 5 minutes
      throttlingEventTimeRangeMs = TimeUnit.MINUTES.toMillis(5);
    }

    @Override
    public void run() {
      dynamicFlowControlStats.updateLatency(timer.elapsed(TimeUnit.MILLISECONDS));
      long lastAdjustedTimestamp = dynamicFlowControlStats.getLastAdjustedTimestampMs();
      long now = System.currentTimeMillis();
      // Avoid adjusting the thresholds too frequently
      if (now - lastAdjustedTimestamp < adjustingIntervalMs) {
        return;
      }
      double meanLatency = dynamicFlowControlStats.getMeanLatency();
      boolean throttled =
          flowControlEvents.getLastFlowControlEvent() == null
              ? false
              : (now - flowControlEvents.getLastFlowControlEvent().getTimestampMs()
                  <= throttlingEventTimeRangeMs);
      if (meanLatency > highLatencyMs) {
        // Decrease at 30% of the maximum
        decrease(lastAdjustedTimestamp, now, fastDecreaseStep);
      } else if (meanLatency > highTargetLatencyMs) {
        // Decrease at 10% of the maximum
        decrease(lastAdjustedTimestamp, now, slowDecreaseStep);
      } else if (throttled && meanLatency < lowTargetLatencyMs) {
        // If latency is low, and there was throttling, then increase the parallelism so that new
        // calls will not be throttled.

        // Increase parallelism at a slower than we decrease. The lower rate should help the
        // system maintain stability.
        increase(lastAdjustedTimestamp, now, fastIncreaseStep);
      } else if (throttled
          && flowController.getCurrentElementCountLimit() < lowParallelismLimit
          && meanLatency < lowParallelismLatencyMs) {
        // When parallelism is reduced latency tends to be artificially higher.
        // Increase slowly to ensure that the system restabilizes.
        increase(lastAdjustedTimestamp, now, slowIncreaseStep);
      }
    }

    private void decrease(long last, long now, long elementSteps) {
      if (dynamicFlowControlStats.setLastAdjustedTimestampMs(last, now)) {
        flowController.decreaseThresholds(elementSteps, 0);
      }
    }

    private void increase(long last, long now, long elementSteps) {
      if (dynamicFlowControlStats.setLastAdjustedTimestampMs(last, now)) {
        flowController.increaseThresholds(elementSteps, 0);
      }
    }
  }
}
