/*
 * Copyright 2024 Google LLC
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

import com.google.cloud.bigtable.stats.StatsRecorderWrapperForConnection;
import com.google.cloud.bigtable.stats.StatsWrapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import java.util.Set;

/**
 * A background task that goes through all channels and updates the errors_per_connection metric.
 */
class CountErrorsPerInterceptorTask implements Runnable {
  private final Set<ConnectionErrorCountInterceptor> interceptors;
  private final Object interceptorsLock;
  // This is not final so that it can be updated and mocked during testing.
  private StatsRecorderWrapperForConnection statsRecorderWrapperForConnection;

  @VisibleForTesting
  void setStatsRecorderWrapperForConnection(
      StatsRecorderWrapperForConnection statsRecorderWrapperForConnection) {
    this.statsRecorderWrapperForConnection = statsRecorderWrapperForConnection;
  }

  CountErrorsPerInterceptorTask(
      Set<ConnectionErrorCountInterceptor> interceptors,
      Object interceptorsLock,
      ImmutableMap<String, String> builtinAttributes) {
    this.interceptors = interceptors;
    this.interceptorsLock = interceptorsLock;
    // We only interact with the putAndRecordPerConnectionErrorCount method, so OperationType and
    // SpanName won't matter.
    this.statsRecorderWrapperForConnection =
        StatsWrapper.createRecorderForConnection(builtinAttributes);
  }

  @Override
  public void run() {
    synchronized (interceptorsLock) {
      for (ConnectionErrorCountInterceptor interceptor : interceptors) {
        long errors = interceptor.getAndResetNumOfErrors();
        long successes = interceptor.getAndResetNumOfSuccesses();
        // We avoid keeping track of inactive connections (i.e., without any failed or successful
        // requests).
        if (errors > 0 || successes > 0) {
          // TODO: add a metric to also keep track of the number of successful requests per each
          // connection.
          this.statsRecorderWrapperForConnection.putAndRecordPerConnectionErrorCount(errors);
        }
      }
    }
  }
}
