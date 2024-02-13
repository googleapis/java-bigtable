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

import com.google.common.collect.ImmutableMap;
import io.grpc.ClientInterceptor;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* Sets up and starts the monitoring for per-connection metrics. */
class ConnectionMetricsTracker {
  private static final Integer PER_CONNECTION_ERROR_COUNT_PERIOD_SECONDS = 60;
  private final EnhancedBigtableStubSettings.Builder builder;
  private final ImmutableMap<String, String> builtinAttributes;
  private final Set<ConnectionErrorCountInterceptor> connectionErrorCountInterceptors;

  private final Object interceptorsLock = new Object();

  ConnectionMetricsTracker(
      EnhancedBigtableStubSettings.Builder builder,
      ImmutableMap<String, String> builtinAttributes) {
    this.builder = builder;
    this.builtinAttributes = builtinAttributes;
    connectionErrorCountInterceptors =
        Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    startConnectionErrorCountTracker();
  }

  private void startConnectionErrorCountTracker() {
    ScheduledExecutorService scheduler = builder.getBackgroundExecutorProvider().getExecutor();
    scheduler.scheduleAtFixedRate(
        new CountErrorsPerInterceptorTask(
            connectionErrorCountInterceptors, interceptorsLock, builtinAttributes),
        0,
        PER_CONNECTION_ERROR_COUNT_PERIOD_SECONDS,
        TimeUnit.SECONDS);
  }

  ClientInterceptor getInterceptor() {
    ConnectionErrorCountInterceptor connectionErrorCountInterceptor =
        new ConnectionErrorCountInterceptor();
    synchronized (interceptorsLock) {
      connectionErrorCountInterceptors.add(connectionErrorCountInterceptor);
    }
    return connectionErrorCountInterceptor;
  }
}
