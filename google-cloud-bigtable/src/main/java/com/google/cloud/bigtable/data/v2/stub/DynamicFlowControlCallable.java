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

import static java.lang.Math.round;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.batching.FlowControlEventStats.FlowControlEvent;
import com.google.api.gax.batching.FlowController;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.DeadlineExceededException;
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
  // Defining adjusting criteria and adjusting rates
  // Latency thresholds multipliers that will trigger flow control changes
  static final double VERY_HIGH_LATENCY_MULTIPLIER = 3;
  static final double HIGH_LATENCY_MULTIPLIER = 1.2;
  static final double LOW_LATENCY_MULTIPLIER = 0.8;
  static final double LOW_CONCURRENCY_MULTIPLIER = 0.05;
  static final double LOW_CONCURRENCY_LATENCY_MULTIPLIER = 2;
  // Rate of change that corresponds to the the thresholds above
  static final double VERY_HIGH_LATENCY_DECREASE_CONCURRENCY_RATE = 0.3;
  static final double HIGH_LATENCY_DECREASE_CONCURRENCY_RATE = 0.1;
  // Increase parallelism at a slower rate than decrease. The lower rate should help the system
  // maintain stability.
  static final double LOW_LATENCY_INCREASE_CONCURRENCY_RATE = 0.05;
  static final double LOW_CONCURRENCY_INCREASE_CONCURRENCY_RATE = 0.02;
  // only look for throttling events in the past 5 minutes
  static final long THROTTLING_EVENT_TIME_RANGE_MS = TimeUnit.MINUTES.toMillis(5);

  private final FlowController flowController;
  private final DynamicFlowControlStats dynamicFlowControlStats;
  private final long targetLatencyMs;
  private final long adjustingIntervalMs;
  private final UnaryCallable innerCallable;

  DynamicFlowControlCallable(
      @Nonnull UnaryCallable innerCallable,
      @Nonnull FlowController flowController,
      @Nonnull DynamicFlowControlStats stats,
      long targetLatencyMs,
      long adjustingIntervalMs) {
    this.innerCallable = innerCallable;
    this.flowController = flowController;
    this.dynamicFlowControlStats = stats;
    this.targetLatencyMs = targetLatencyMs;
    this.adjustingIntervalMs = adjustingIntervalMs;
  }

  @Override
  public ApiFuture futureCall(Object request, ApiCallContext context) {
    final Runnable flowControllerRunnable = new DynamicFlowControlRunnable();
    ApiFuture future = innerCallable.futureCall(request, context);
    ApiFutures.addCallback(future, new ApiFutureCallback() {
      @Override
      public void onFailure(Throwable t) {
        if (t instanceof DeadlineExceededException) {
          flowControllerRunnable.run();
        }
      }

      @Override
      public void onSuccess(Object result) {
        flowControllerRunnable.run();
      }
    }, MoreExecutors.directExecutor());
    return future;
  }

  class DynamicFlowControlRunnable implements Runnable {

    // Latency and throttling thresholds that will trigger flow control changes
    private final double highTargetLatencyMs;
    private final double lowTargetLatencyMs;
    private final double veryHighTargetLatencyMs;
    private final double lowConcurrencyLatencyMs;
    private final double lowConcurrencyLimit;
    // Rate of change that corresponds to the the thresholds above:
    private final long highLatencyDecreaseConcurrencyStep;
    private final long lowLatencyIncreaseConcurrencyStep;
    private final long veryHighLatencyDecreaseConcurrencyStep;
    private final long lowConcurrencyIncreaseConcurrencyStep;

    private final Stopwatch timer;

    DynamicFlowControlRunnable() {
      long maxElementLimit = flowController.getMaxElementCountLimit();
      // targeting roughly 20% around target latency so there isn't too much churn
      highTargetLatencyMs = targetLatencyMs * HIGH_LATENCY_MULTIPLIER;
      lowTargetLatencyMs = targetLatencyMs * LOW_LATENCY_MULTIPLIER;
      veryHighTargetLatencyMs = targetLatencyMs * VERY_HIGH_LATENCY_MULTIPLIER;
      // make sure the parallelism is not too low
      lowConcurrencyLimit = maxElementLimit * LOW_CONCURRENCY_MULTIPLIER;
      lowConcurrencyLatencyMs = targetLatencyMs * LOW_CONCURRENCY_LATENCY_MULTIPLIER;

      highLatencyDecreaseConcurrencyStep = round(maxElementLimit * HIGH_LATENCY_DECREASE_CONCURRENCY_RATE);
      lowLatencyIncreaseConcurrencyStep = round(maxElementLimit * LOW_LATENCY_INCREASE_CONCURRENCY_RATE);
      veryHighLatencyDecreaseConcurrencyStep = round(maxElementLimit * VERY_HIGH_LATENCY_DECREASE_CONCURRENCY_RATE);
      lowConcurrencyIncreaseConcurrencyStep = round(maxElementLimit * LOW_CONCURRENCY_INCREASE_CONCURRENCY_RATE);

      timer = Stopwatch.createStarted();
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
      FlowControlEvent flowControlEvent =
          flowController.getFlowControlEventStats().getLastFlowControlEvent();
      boolean throttled =
          flowControlEvent == null
              ? false
              : (now - flowControlEvent.getTimestampMs() <= THROTTLING_EVENT_TIME_RANGE_MS);
      long maxElementLimit = flowController.getMaxElementCountLimit();
      if (meanLatency > veryHighTargetLatencyMs) {
        // Decrease at 30% of the maximum
        decrease(lastAdjustedTimestamp, now, veryHighLatencyDecreaseConcurrencyStep);
      } else if (meanLatency > highTargetLatencyMs) {
        // Decrease at 10% of the maximum
        decrease(lastAdjustedTimestamp, now, highLatencyDecreaseConcurrencyStep);
      } else if (throttled && meanLatency < lowTargetLatencyMs) {
        // If latency is low, and there was throttling, then increase the parallelism so that new
        // calls will not be throttled.

        // Increase parallelism at a slower than we decrease. The lower rate should help the
        // system maintain stability.
        increase(lastAdjustedTimestamp, now, lowLatencyIncreaseConcurrencyStep);
      } else if (throttled
          && flowController.getCurrentElementCountLimit() < lowConcurrencyLimit
          && meanLatency < lowConcurrencyLatencyMs) {
        // When parallelism is reduced latency tends to be artificially higher.
        // Increase slowly to ensure that the system restabilizes.
        increase(lastAdjustedTimestamp, now, lowConcurrencyIncreaseConcurrencyStep);
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
