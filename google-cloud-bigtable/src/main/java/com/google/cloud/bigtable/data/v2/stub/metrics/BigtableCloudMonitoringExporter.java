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

import com.google.api.MonitoredResource;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceSettings;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeSeries;
import com.google.protobuf.Empty;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.threeten.bp.Duration;

/** Bigtable Cloud Monitoring OpenTelemetry Exporter. */
final class BigtableCloudMonitoringExporter implements MetricExporter {

  private static final Logger logger =
      Logger.getLogger(BigtableCloudMonitoringExporter.class.getName());
  private final MetricServiceClient client;

  private final String projectId;
  private final String taskId;
  private final MonitoredResource monitoredResource;
  private AtomicBoolean isShutdown = new AtomicBoolean(false);

  private static final String RESOURCE_TYPE = "bigtable_client_raw";

  private CompletableResultCode lastCode;

  static BigtableCloudMonitoringExporter create(String projectId, Credentials credentials)
      throws IOException {
    MetricServiceSettings.Builder settingsBuilder = MetricServiceSettings.newBuilder();
    settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials));

    org.threeten.bp.Duration timeout = Duration.ofMinutes(1);
    // TODO: createServiceTimeSeries needs special handling if the request failed. Leaving
    // it as not retried for now.
    settingsBuilder.createServiceTimeSeriesSettings().setSimpleTimeoutNoRetries(timeout);
    return new BigtableCloudMonitoringExporter(
        projectId,
        MetricServiceClient.create(settingsBuilder.build()),
        MonitoredResource.newBuilder().setType(RESOURCE_TYPE).build(),
        BigtableExporterUtils.getDefaultTaskValue());
  }

  @VisibleForTesting
  BigtableCloudMonitoringExporter(
      String projectId,
      MetricServiceClient client,
      MonitoredResource monitoredResource,
      String taskId) {
    this.client = client;
    this.monitoredResource = monitoredResource;
    this.taskId = taskId;
    this.projectId = projectId;
  }

  @Override
  public CompletableResultCode export(Collection<MetricData> collection) {
    if (isShutdown.get()) {
      return CompletableResultCode.ofFailure();
    }
    if (!collection.stream()
        .flatMap(metricData -> metricData.getData().getPoints().stream())
        .allMatch(pd -> BigtableExporterUtils.getProjectId(pd).equals(projectId))) {
      logger.log(Level.WARNING, "Metric data has different a projectId. Skip exporting.");
      return CompletableResultCode.ofFailure();
    }

    lastCode = new CompletableResultCode();

    List<TimeSeries> allTimeSeries = new ArrayList<>();
    for (MetricData metricData : collection) {
      if (!metricData.getInstrumentationScopeInfo().getName().equals("bigtable.googleapis.com")) {
        continue;
      }

      CompletableResultCode code = new CompletableResultCode();

      List<TimeSeries> timeSeries =
          metricData.getData().getPoints().stream()
              .map(
                  pointData ->
                      BigtableExporterUtils.convertPointToTimeSeries(
                          metricData, pointData, taskId, monitoredResource))
              .collect(Collectors.toList());
      allTimeSeries.addAll(timeSeries);
    }

    ProjectName projectName = ProjectName.of(projectId);
    CreateTimeSeriesRequest request =
        CreateTimeSeriesRequest.newBuilder()
            .setName(projectName.toString())
            .addAllTimeSeries(allTimeSeries)
            .build();

    ApiFuture<Empty> future = this.client.createServiceTimeSeriesCallable().futureCall(request);

    ApiFutures.addCallback(
        future,
        new ApiFutureCallback<Empty>() {
          @Override
          public void onFailure(Throwable throwable) {
            lastCode.fail();
          }

          @Override
          public void onSuccess(Empty empty) {
            lastCode.succeed();
          }
        },
        MoreExecutors.directExecutor());

    return lastCode;
  }

  @Override
  public CompletableResultCode flush() {
    if (lastCode != null) {
      return lastCode;
    }
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode shutdown() {
    if (!isShutdown.compareAndSet(false, true)) {
      logger.log(Level.INFO, "shutdown is called multiple times");
      return CompletableResultCode.ofSuccess();
    }
    client.shutdown();
    return flush();
  }

  @Override
  public AggregationTemporality getAggregationTemporality(InstrumentType instrumentType) {
    return AggregationTemporality.CUMULATIVE;
  }
}
