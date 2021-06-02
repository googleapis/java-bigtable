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

import com.google.api.core.ApiClock;
import com.google.api.gax.batching.FlowController;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.threeten.bp.Instant;

/**
 * Records stats used in dynamic flow control, the decaying average of recorded latencies and the
 * last timestamp when the thresholds in {@link FlowController} are updated.
 *
 * <pre>Exponential decaying average = weightedSum / weightedCount, where
 *   weightedSum(n) = weight(n) * value(n) + weightedSum(n - 1)
 *   weightedCount(n) = weight(n) + weightedCount(n - 1),
 * and weight(n) grows exponentially over elapsed time.
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
    this(DEFAULT_DECAY_CONSTANT, null);
  }

  DynamicFlowControlStats(ApiClock clock) {
    this(DEFAULT_DECAY_CONSTANT, clock);
  }

  DynamicFlowControlStats(double decayConstant, ApiClock clock) {
    this.lastAdjustedTimestampMs = new AtomicLong(0);
    this.meanLatency = new DecayingAverage(decayConstant, clock);
  }

  void updateLatency(long latency) {
    meanLatency.update(latency);
  }

  /** Return the mean calculated from the last update, will not decay over time. */
  double getMeanLatency() {
    return meanLatency.getMean();
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
    private long startTimeSecond;
    private ApiClock clock;

    DecayingAverage(double decayConstant, ApiClock clock) {
      this.decayConstant = decayConstant;
      this.mean = 0.0;
      this.weightedCount = 0.0;
      this.clock = clock;
      this.startTimeSecond =
          clock == null
              ? Instant.now().getEpochSecond()
              : TimeUnit.MILLISECONDS.toSeconds(clock.millisTime());
    }

    synchronized void update(long value) {
      // Decay mean and weightedCount if now - startTime > threshold, so weight won't be infinite
      long now = getCurrentTimeInSecond();
      double decay = getDecay(now);
      mean /= decay;
      weightedCount /= decay;

      long elapsed = now - startTimeSecond;
      double weight = getWeight(elapsed);
      // Using weighted count in case the sum overflows.
      mean =
          mean * (weightedCount / (weightedCount + weight))
              + weight * value / (weightedCount + weight);
      weightedCount += weight;
    }

    double getMean() {
      return mean;
    }

    private long getCurrentTimeInSecond() {
      return clock == null
          ? Instant.now().getEpochSecond()
          : TimeUnit.MILLISECONDS.toSeconds(clock.millisTime());
    }

    private double getWeight(long elapsedSecond) {
      return Math.exp(decayConstant * elapsedSecond);
    }

    private synchronized double getDecay(long now) {
      long elapsed = now - startTimeSecond;
      if (elapsed > UPDATE_START_TIME_THRESHOLD_SECOND) {
        double decay = getWeight(elapsed);
        startTimeSecond = now;
        return decay;
      }
      return 1;
    }
  }
}
