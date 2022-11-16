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
package com.google.cloud.bigtable.data.v2.stub.metrics;

// Make stats inside of EnhancedBigtableStub
// Pass in through constructor
// EnhancedBigtableStub is per client so we make the stats
// Have a lower and upper bound

public class RateLimitingStats {
  private long lastQpsUpdateTime;
  private double currentQps;
  private double lowerQpsBound;
  private double upperQpsBound;

  public RateLimitingStats() {
    this.lastQpsUpdateTime = System.currentTimeMillis();
    this.currentQps = -1; // Will be replaced by default value
    this.lowerQpsBound = 0.001;
    this.upperQpsBound = 100_000;
  }

  public long getLastQpsUpdateTime() {
    return lastQpsUpdateTime;
  }

  public void updateLastQpsUpdateTime(long newQpsUpdateTime) {
    lastQpsUpdateTime = newQpsUpdateTime;
  }

  public void updateQps(double qps) {
    if (qps < lowerQpsBound || qps > upperQpsBound) {
      // Log or report error here
      System.out.println("New QPS must be within bounds");
      return;
    }
    currentQps = qps;
  }

  public double getLowerQpsBound() {
    return lowerQpsBound;
  }

  public double getUpperQpsBound() {
    return upperQpsBound;
  }
}
