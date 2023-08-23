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
import com.google.api.core.InternalApi;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.Credentials;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceSettings;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeSeries;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.threeten.bp.Duration;

/** Bigtable Cloud Monitoring OpenTelemetry Exporter. */
@InternalApi
public final class BigtableCloudMonitoringExporter implements MetricExporter {

  private static final Logger logger =
      Logger.getLogger(BigtableCloudMonitoringExporter.class.getName());
  private final MetricServiceClient client;

  private final String projectId;
  private final String taskId;
  private final MonitoredResource monitoredResource;
  private boolean isShutdown = false;

  private static final String RESOURCE_TYPE = "bigtable_client_raw";

  public static BigtableCloudMonitoringExporter create(String projectId, Credentials credentials)
      throws IOException {
    MetricServiceSettings.Builder settingsBuilder = MetricServiceSettings.newBuilder();
    settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials));

    org.threeten.bp.Duration timeout = Duration.ofMinutes(1);
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
    if (isShutdown) {
      return CompletableResultCode.ofFailure();
    }
    Preconditions.checkArgument(
        collection.stream()
            .flatMap(metricData -> metricData.getData().getPoints().stream())
            .allMatch(pd -> BigtableExporterUtils.getProjectId(pd).equals(projectId)),
        "Some metric data has unexpected projectId");
    for (MetricData metricData : collection) {
      if (!metricData.getInstrumentationScopeInfo().getName().equals("bigtable.googleapis.com")) {
        continue;
      }
      List<TimeSeries> timeSeries =
          metricData.getData().getPoints().stream()
              .map(
                  pointData ->
                      BigtableExporterUtils.convertPointToTimeSeries(
                          metricData, pointData, taskId, monitoredResource))
              .collect(Collectors.toList());

      ProjectName projectName = ProjectName.of(projectId);
      CreateTimeSeriesRequest request =
          CreateTimeSeriesRequest.newBuilder()
              .setName(projectName.toString())
              .addAllTimeSeries(timeSeries)
              .build();

      this.client.createServiceTimeSeriesCallable().futureCall(request);
    }

    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode flush() {
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode shutdown() {
    client.shutdown();
    isShutdown = true;
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public AggregationTemporality getAggregationTemporality(InstrumentType instrumentType) {
    return AggregationTemporality.CUMULATIVE;
  }
}
