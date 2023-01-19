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

import com.google.api.gax.rpc.ApiCallContext;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ServerStats;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

public class RateLimitingStats {
  private long lastQpsUpdateTime;
  private double currentQps = -1;
  public static double lowerQpsBound = 1;
  public static double upperQpsBound = 100_000;
  public static double PERCENT_CHANGE_LIMIT = .3;

  public RateLimitingStats() {
    this.lastQpsUpdateTime = System.currentTimeMillis();
  }

  public long getLastQpsUpdateTime() {
    return lastQpsUpdateTime;
  }

  public void updateLastQpsUpdateTime(long newQpsUpdateTime) {
    lastQpsUpdateTime = newQpsUpdateTime;
  }

  public double updateQps(double newQps) {
    this.currentQps = newQps;
    return newQps;
  }

  // This function is to calculate the QPS based on current CPU
  static double calculateQpsChange(double[] tsCpus, double target, double currentRate) {
    if (tsCpus.length == 0) {
      return currentRate;
    }
    double cpuDelta = DoubleStream.of(tsCpus).average().getAsDouble() - target;
    double newRate = currentRate;

    // When the CPU is above the target threshold, reduce the rate by a percentage from the target
    // If the average CPU is within 5% of the target, maintain the currentRate
    // If the average CPU is below the target, continue to increase till a maintainable CPU is met
    if (cpuDelta > 0) {
      double percentChange = 1 - Math.min(cpuDelta / (100 - target), PERCENT_CHANGE_LIMIT);
      newRate = (long)(percentChange * currentRate);
    } else if (Math.abs(cpuDelta) > 5){
      newRate = currentRate + (currentRate * PERCENT_CHANGE_LIMIT);
    }

    if (newRate < lowerQpsBound) {
      return lowerQpsBound;
    } else if (newRate > upperQpsBound) {
      return upperQpsBound;
    }
    return newRate;
  }

  static double[] getCpuList(MutateRowsResponse response) {
    if (response != null && response.hasServerStats()) {
      ServerStats stats = response.getServerStats();

      double[] cpus = new double[stats.getCpuStatsList().size()];
      for (int i = 0; i < stats.getCpuStatsList().size(); i++) {
        double currentCPU = 100 * ((double)stats.getCpuStats(i).getRecentGcuMillisecondsPerSecond() / stats.getCpuStats(i).getMilligcuLimit());

        // It's possible for CPU to be over 100, this will limit the CPU to be within [0, 100)
        cpus[i] = (currentCPU > 100) ? 99.9 : currentCPU;
      }
      return cpus;
    }
    return new double[]{};
  }
}
