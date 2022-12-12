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

// Make stats inside of EnhancedBigtableStub
// Pass in through constructor
// EnhancedBigtableStub is per client so we make the stats
// Have a lower and upper bound

import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ServerStats;
import java.util.stream.DoubleStream;

public class RateLimitingStats {
  private long lastQpsUpdateTime;
  private double currentQps;
  private static double lowerQpsBound = 0.001;
  private static double upperQpsBound = 100_000;
  private static double PERCENT_CHANGE_LIMIT = .3;

  public RateLimitingStats() {
    this.lastQpsUpdateTime = System.currentTimeMillis();
    this.currentQps = -1; // Will be replaced by default value
  }

  public long getLastQpsUpdateTime() {
    return lastQpsUpdateTime;
  }

  public void updateLastQpsUpdateTime(long newQpsUpdateTime) {
    lastQpsUpdateTime = newQpsUpdateTime;
  }

  public void updateQps(double qps) {
    currentQps = qps;
  }

  public double getLowerQpsBound() {
    return lowerQpsBound;
  }

  public double getUpperQpsBound() {
    return upperQpsBound;
  }

  // This function is to calculate the QPS based on current CPU
  static double calculateQpsChange(double[] tsCpus, double target, double currentRate) {
    if (tsCpus.length == 0) {
      return currentRate;
    }
    double cpuDelta = DoubleStream.of(tsCpus).average().getAsDouble() - target;
    double newRate = currentRate;

    // When the CPU is above the target threshold, reduce the rate by a percentage from the target
    // If the current CPU is within 5% of the target, maintain the currentRate
    // If the current CPU is below the target, continue to increase till a maintainable CPU is met
    if (cpuDelta > 0) {
      newRate = (long)(cpuDelta / (100 - target) * currentRate * PERCENT_CHANGE_LIMIT);
    } else if (Math.abs(cpuDelta) > 5){
      newRate = currentRate + (currentRate * (PERCENT_CHANGE_LIMIT / 2));
    }
    if (newRate < lowerQpsBound) {
      return lowerQpsBound;
    } else if (newRate > upperQpsBound) {
      return upperQpsBound;
    }
    return newRate;
  }

  static double[] getCpuList(MutateRowsResponse response) {
    //System.out.println("Response exists: "+response != null);
    if (response != null && response.hasServerStats()) {
      ServerStats stats = response.getServerStats();

      double[] cpus = new double[stats.getCpuStatsList().size()];
      for (int i = 0; i < stats.getCpuStatsList().size(); i++) {
        cpus[i] = 100 * ((double)stats.getCpuStats(i).getRecentGcuMillisecondsPerSecond() / stats.getCpuStats(i).getMilligcuLimit()); // Q: What is the list of CpuStats here?
        // Cpu will be a double [0,1]

        // ServerStats, there is 1 ServerStats and has many requests
        // CpuStats is basically one RPC, could have many CPU values
        // Average all the cpuStats under ServerStats
        // Collect how many CPUStats are returned (Testing for real world, Update RateLimitingStats)
        // Are there empty responses, what is the average amount of returned CPUStats

        // What is the gcuMillisecondLimit
        // Divide recentGcuMilliseconds
      }
      //System.out.println(cpus[0]);
      return cpus;
    }
    return new double[]{};
  }
}
