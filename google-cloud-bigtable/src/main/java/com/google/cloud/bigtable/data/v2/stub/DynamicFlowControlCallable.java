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

final class DynamicFlowControlCallable extends UnaryCallable {

  private final FlowController flowController;
  private final FlowControlEventStats flowControlEvents;
  private final DynamicFlowControlStats dynamicFlowControlStats;
  private final long targetLatency;
  private final long adjustingIntervalMs;
  private final UnaryCallable innerCallable;

  DynamicFlowControlCallable(
      @Nonnull UnaryCallable innerCallable,
      @Nonnull FlowController flowController,
      @Nonnull FlowControlEventStats flowControlEvents,
      @Nonnull DynamicFlowControlStats stats,
      long targetLatency,
      long adjustingIntervalMs) {
    this.innerCallable = innerCallable;
    this.flowController = flowController;
    this.flowControlEvents = flowControlEvents;
    this.dynamicFlowControlStats = stats;
    this.targetLatency = targetLatency;
    this.adjustingIntervalMs = adjustingIntervalMs;
  }

  @Override
  public ApiFuture futureCall(Object request, ApiCallContext context) {
    Runnable flowControllerRunnable =
        new DynamicFlowControlRunnable(
            flowController,
            flowControlEvents,
            dynamicFlowControlStats,
            targetLatency,
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
    }

    @Override
    public void run() {
      dynamicFlowControlStats.updateLatency(timer.elapsed(TimeUnit.MILLISECONDS));
      long lastAdjustedTimestamp = dynamicFlowControlStats.getLastAdjustedTimestampMs();
      long now = System.currentTimeMillis();
      if (now - lastAdjustedTimestamp < adjustingIntervalMs) {
        return;
      }
      double meanLatency = dynamicFlowControlStats.getMeanLatency();
      boolean throttled =
          flowControlEvents.getLastFlowControlEvent() == null
              ? false
              : (now - flowControlEvents.getLastFlowControlEvent().getTimestampMs()
                  <= TimeUnit.MINUTES.toMillis(5));
      if (meanLatency > targetLatency * 3) {
        decrease(
            lastAdjustedTimestamp, now, flowController.getMaxOutstandingElementCount() * 3 / 10);
      } else if (meanLatency > targetLatency * 1.2) {
        decrease(lastAdjustedTimestamp, now, flowController.getMaxOutstandingElementCount() / 10);
      } else if (throttled && meanLatency < targetLatency * 8 / 10) {
        increase(
            lastAdjustedTimestamp, now, flowController.getMaxOutstandingElementCount() * 5 / 100);
      } else if (throttled
          && flowController.getCurrentOutstandingElementCount()
              < flowController.getMaxOutstandingElementCount() * 5 / 100
          && meanLatency < 2 * targetLatency) {
        increase(
            lastAdjustedTimestamp, now, flowController.getMaxOutstandingElementCount() * 2 / 100);
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
