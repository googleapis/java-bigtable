/*
 * Copyright 2022 Google LLC
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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.APPLICATION_BLOCKING_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.APPLICATION_BLOCKING_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.APP_PROFILE;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.ATTEMPT_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.ATTEMPT_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CLIENT_BLOCKING_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CLIENT_BLOCKING_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CONNECTIVITY_ERROR_COUNT_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CONNECTIVITY_ERROR_COUNT_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.FIRST_RESPONSE_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.FIRST_RESPONSE_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.INSTANCE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.OPERATION_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.OPERATION_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.PROJECT_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.RETRY_COUNT_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.RETRY_COUNT_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.SCOPE;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.SERVER_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.SERVER_LATENCIES_VIEW;

import com.google.api.core.InternalApi;
import com.google.api.gax.tracing.ApiTracer;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.BaseApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import java.io.IOException;

/**
 * {@link ApiTracerFactory} that will generate OpenTelemetry metrics by using the {@link ApiTracer}
 * api.
 */
@InternalApi("For internal use only")
public class BuiltinMetricsTracerFactory extends BaseApiTracerFactory {
  private final Attributes attributes;
  private final BigtableMetricsRecorder bigtableMetricsRecorder;

  public static BuiltinMetricsTracerFactory create(EnhancedBigtableStubSettings settings)
      throws IOException {
    return new BuiltinMetricsTracerFactory(settings);
  }

  BuiltinMetricsTracerFactory(EnhancedBigtableStubSettings settings) throws IOException {
    this.attributes =
        Attributes.builder()
            .put(PROJECT_ID, settings.getProjectId())
            .put(INSTANCE_ID, settings.getInstanceId())
            .put(APP_PROFILE, settings.getAppProfileId())
            .build();

    if (settings.isBuiltinMetricsEnabled()) {
      Resource resource = Resource.create(attributes);
      MetricExporter metricExporter =
          BigtableCloudMonitoringExporter.create(
              settings.getProjectId(), settings.getCredentialsProvider().getCredentials());

      SdkMeterProvider meterProvider =
          SdkMeterProvider.builder()
              .setResource(resource)
              .registerMetricReader(PeriodicMetricReader.create(metricExporter))
              .registerView(OPERATION_LATENCIES_SELECTOR, OPERATION_LATENCIES_VIEW)
              .registerView(ATTEMPT_LATENCIES_SELECTOR, ATTEMPT_LATENCIES_VIEW)
              .registerView(SERVER_LATENCIES_SELECTOR, SERVER_LATENCIES_VIEW)
              .registerView(FIRST_RESPONSE_LATENCIES_SELECTOR, FIRST_RESPONSE_LATENCIES_VIEW)
              .registerView(
                  APPLICATION_BLOCKING_LATENCIES_SELECTOR, APPLICATION_BLOCKING_LATENCIES_VIEW)
              .registerView(CLIENT_BLOCKING_LATENCIES_SELECTOR, CLIENT_BLOCKING_LATENCIES_VIEW)
              .registerView(RETRY_COUNT_SELECTOR, RETRY_COUNT_VIEW)
              .registerView(CONNECTIVITY_ERROR_COUNT_SELECTOR, CONNECTIVITY_ERROR_COUNT_VIEW)
              .build();
      OpenTelemetry openTelemetry =
          OpenTelemetrySdk.builder().setMeterProvider(meterProvider).build();
      bigtableMetricsRecorder = new BuiltinInMetricsRecorder(openTelemetry.getMeter(SCOPE));
    } else {
      bigtableMetricsRecorder = new BigtableMetricsRecorder();
    }
  }

  @Override
  public ApiTracer newTracer(ApiTracer parent, SpanName spanName, OperationType operationType) {
    return new BuiltinMetricsTracer(operationType, spanName, bigtableMetricsRecorder, attributes);
  }
}
