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

import com.google.auth.Credentials;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import java.io.IOException;

/**
 * Set {@link
 * com.google.cloud.bigtable.data.v2.BigtableDataSettings.Builder#setMetricsProvider(MetricsProvider)},
 * to {@link this#INSTANCE} to enable collecting and export client side metrics
 * https://cloud.google.com/bigtable/docs/client-side-metrics. This is the default setting in {@link
 * com.google.cloud.bigtable.data.v2.BigtableDataSettings}.
 */
public final class DefaultMetricsProvider implements MetricsProvider {

  public static DefaultMetricsProvider INSTANCE = new DefaultMetricsProvider();

  private OpenTelemetry openTelemetry;

  private DefaultMetricsProvider() {}

  public OpenTelemetry getOpenTelemetry(String projectId, Credentials credentials)
      throws IOException {
    if (openTelemetry == null) {
      SdkMeterProviderBuilder meterProvider = SdkMeterProvider.builder();
      BuiltinMetricsView.registerBuiltinMetrics(projectId, credentials, meterProvider);
      openTelemetry = OpenTelemetrySdk.builder().setMeterProvider(meterProvider.build()).build();
    }
    return openTelemetry;
  }

  @Override
  public String toString() {
    return "DefaultMetricsProvider";
  }
}
