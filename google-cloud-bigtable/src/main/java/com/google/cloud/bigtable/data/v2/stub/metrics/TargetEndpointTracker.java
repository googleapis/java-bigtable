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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.METER_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.PER_CONNECTION_ERROR_COUNT_NAME;

import com.google.api.core.InternalApi;
import io.grpc.ClientInterceptor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* Background task that goes through all connections and updates the errors_per_connection metric. */
@InternalApi("For internal use only")
public class TargetEndpointTracker implements Runnable {

  private static final Integer PER_CONNECTION_ERROR_COUNT_PERIOD_SECONDS = 60;

  private final LongHistogram perConnectionErrorCountHistogram;
  private final Attributes attributes;

  private final Set<TargetEndpointInterceptor> targetEndpointInterceptors;
  private final Object interceptorsLock = new Object();

  public TargetEndpointTracker(OpenTelemetry openTelemetry, Attributes attributes) {
    targetEndpointInterceptors =
        Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

    HashSet<String> targets = new HashSet<>();
    for(TargetEndpointInterceptor interceptor: targetEndpointInterceptors) {
      targets.add(interceptor.getTarget());
    }

    attributes.toBuilder().put("targets", (String[])targets.toArray());
    this.attributes = attributes;
  }

  public void setTargetEndpointTracker(ScheduledExecutorService scheduler) {
    scheduler.scheduleAtFixedRate(
        this, 0, PER_CONNECTION_ERROR_COUNT_PERIOD_SECONDS, TimeUnit.SECONDS);
  }

  public ClientInterceptor getInterceptor() {
    TargetEndpointInterceptor targetEndpointInterceptor =
        new TargetEndpointInterceptor();
    synchronized (interceptorsLock) {
      targetEndpointInterceptors.add(targetEndpointInterceptor);
    }
    return targetEndpointInterceptor;
  }
}
