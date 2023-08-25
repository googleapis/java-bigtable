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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.APPLICATION_BLOCKING_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.APPLICATION_BLOCKING_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.ATTEMPT_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.ATTEMPT_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CLIENT_BLOCKING_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CLIENT_BLOCKING_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CONNECTIVITY_ERROR_COUNT_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CONNECTIVITY_ERROR_COUNT_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.FIRST_RESPONSE_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.FIRST_RESPONSE_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.OPERATION_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.OPERATION_LATENCIES_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.RETRY_COUNT_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.RETRY_COUNT_VIEW;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.SERVER_LATENCIES_SELECTOR;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.SERVER_LATENCIES_VIEW;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import java.io.IOException;

public class BuiltinViews {

  public static void registerBuiltinViews(String project, SdkMeterProviderBuilder builder)
      throws IOException {
    BuiltinViews.registerBuiltinViews(project, GoogleCredentials.getApplicationDefault(), builder);
  }

  public static void registerBuiltinViews(
      String project, Credentials credentials, SdkMeterProviderBuilder builder) throws IOException {
    builder
        .registerMetricReader(
            PeriodicMetricReader.create(
                BigtableCloudMonitoringExporter.create(project, credentials)))
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
