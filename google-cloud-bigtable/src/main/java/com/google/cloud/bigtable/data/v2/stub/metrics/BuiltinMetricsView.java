/*
 * Copyright 2023 Google LLC
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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.APPLICATION_BLOCKING_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.APPLICATION_BLOCKING_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.ATTEMPT_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.ATTEMPT_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLIENT_BLOCKING_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLIENT_BLOCKING_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CONNECTIVITY_ERROR_COUNT_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CONNECTIVITY_ERROR_COUNT_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.FIRST_RESPONSE_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.FIRST_RESPONSE_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.OPERATION_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.OPERATION_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.RETRY_COUNT_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.RETRY_COUNT_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.SERVER_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.SERVER_LATENCIES_VIEW;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import java.io.IOException;

/** Register built-in metrics on a custom OpenTelemetry instance. */
public class BuiltinMetricsView {

  /**
   * Register built-in metrics on the {@link SdkMeterProviderBuilder} with application default
   * credentials.
   */
  public static void registerBuiltinMetrics(String projectId, SdkMeterProviderBuilder builder)
      throws IOException {
    BuiltinMetricsView.registerBuiltinMetrics(
        projectId, GoogleCredentials.getApplicationDefault(), builder);
  }

  /** Register built-in metrics on the {@link SdkMeterProviderBuilder} with credentials. */
  public static void registerBuiltinMetrics(
      String projectId, Credentials credentials, SdkMeterProviderBuilder builder)
      throws IOException {
    MetricExporter metricExporter = BigtableCloudMonitoringExporter.create(projectId, credentials);
    builder
        .registerMetricReader(PeriodicMetricReader.create(metricExporter))
        .registerView(OPERATION_LATENCIES_SELECTOR, OPERATION_LATENCIES_VIEW)
        .registerView(ATTEMPT_LATENCIES_SELECTOR, ATTEMPT_LATENCIES_VIEW)
        .registerView(SERVER_LATENCIES_SELECTOR, SERVER_LATENCIES_VIEW)
        .registerView(FIRST_RESPONSE_LATENCIES_SELECTOR, FIRST_RESPONSE_LATENCIES_VIEW)
        .registerView(APPLICATION_BLOCKING_LATENCIES_SELECTOR, APPLICATION_BLOCKING_LATENCIES_VIEW)
        .registerView(CLIENT_BLOCKING_LATENCIES_SELECTOR, CLIENT_BLOCKING_LATENCIES_VIEW)
        .registerView(RETRY_COUNT_SELECTOR, RETRY_COUNT_VIEW)
        .registerView(CONNECTIVITY_ERROR_COUNT_SELECTOR, CONNECTIVITY_ERROR_COUNT_VIEW);
  }
}
