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

import com.google.api.gax.batching.FlowController;
import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Records stats used in dynamic flow control, the decaying average of recorded latencies and the
 * last timestamp when the thresholds in {@link FlowController} are updated.
 */
final class DynamicFlowControlStats {

  // Biased to the past 5 minutes (300 seconds), e^(-decay_constant * 300) = 0.01, decay_constant ~=
  // 0.015
  private static final double DEFAULT_DECAY_CONSTANT = 0.015;
  // Update start time every 15 minutes so the values won't be infinite
  private static final long UPDATE_START_TIME_THRESHOLD_SECOND = TimeUnit.MINUTES.toSeconds(15);

  private AtomicLong lastAdjustedTimestampMs;
  private DecayingAverage meanLatency;

  DynamicFlowControlStats() {
    this(DEFAULT_DECAY_CONSTANT);
  }

  DynamicFlowControlStats(double decayConstant) {
    this.lastAdjustedTimestampMs = new AtomicLong(0);
    this.meanLatency = new DecayingAverage(decayConstant);
  }

  void updateLatency(long latency) {
    updateLatency(latency, System.currentTimeMillis());
  }

  @VisibleForTesting
  void updateLatency(long latency, long timestampMs) {
    meanLatency.update(latency, timestampMs);
  }

  double getMeanLatency() {
    return getMeanLatency(System.currentTimeMillis());
  }

  @VisibleForTesting
  double getMeanLatency(long timestampMs) {
    return meanLatency.getMean(timestampMs);
  }

  public long getLastAdjustedTimestampMs() {
    return lastAdjustedTimestampMs.get();
  }

  boolean setLastAdjustedTimestampMs(long last, long now) {
    return lastAdjustedTimestampMs.compareAndSet(last, now);
  }

  private class DecayingAverage {
    private double decayConstant;
    private double mean;
    private double weightedCount;
    private AtomicLong lastUpdateTimeInSecond;
    private long startTimeSecond;

    DecayingAverage(double decayConstant) {
      this.decayConstant = decayConstant;
      this.mean = 0.0;
      this.weightedCount = 0.0;
      this.startTimeSecond = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
      this.lastUpdateTimeInSecond = new AtomicLong(0);
    }

    synchronized void update(long value, long timestampMs) {
      updateStartTime(timestampMs);

      long now = TimeUnit.MILLISECONDS.toSeconds(timestampMs);
      long elapsed = now - startTimeSecond;
      double weight = getWeight(elapsed);
      // Exponential moving average = weightedSum / weightedCount, where
      // weightedSum(n) = weight(n) * value(n) + weightedSum(n - 1)
      // weightedCount(n) = weight(n) + weightedCount(n - 1), where weight(n) grows exponentially
      // over elapsed time.
      // Using weighted count in case the sum overflows.
      mean =
          mean * (weightedCount / (weightedCount + weight))
              + weight * value / (weightedCount + weight);
      weightedCount += weight;

      // Set last update time so when we're getting the mean we can calculate the decay based on the
      // last time the mean was updated.
      if (now > lastUpdateTimeInSecond.get()) {
        lastUpdateTimeInSecond.set(now);
      }
    }

    double getMean(long timestampMs) {
      long timestampSecond = TimeUnit.MILLISECONDS.toSeconds(timestampMs);
      long elapsed = timestampSecond - lastUpdateTimeInSecond.get();
      double decay = getWeight(Math.min(0, -elapsed));
      return mean * decay;
    }

    private double getWeight(long elapsedSecond) {
      return Math.exp(decayConstant * elapsedSecond);
    }

    private synchronized void updateStartTime(long timestampMs) {
      long timestampSecond = TimeUnit.MILLISECONDS.toSeconds(timestampMs);
      long elapsed = timestampSecond - lastUpdateTimeInSecond.get();
      if (elapsed > UPDATE_START_TIME_THRESHOLD_SECOND) {
        double decay = getWeight(-elapsed);
        mean *= decay;
        weightedCount *= decay;
        startTimeSecond = timestampSecond;
        lastUpdateTimeInSecond.set(timestampSecond);
      }
    }
  }
}
