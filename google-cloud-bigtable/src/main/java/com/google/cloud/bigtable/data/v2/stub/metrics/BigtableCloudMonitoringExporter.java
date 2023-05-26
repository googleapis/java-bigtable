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
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.auth.Credentials;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceSettings;
import com.google.common.annotations.VisibleForTesting;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeSeries;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.threeten.bp.Duration;

final class BigtableCloudMonitoringExporter implements MetricExporter {

  private static final Logger logger =
      Logger.getLogger(BigtableCloudMonitoringExporter.class.getName());
  private final MetricServiceClient client;
  private final String taskId;
  private final MonitoredResource monitoredResource;

  private static final String RESOURCE_TYPE = "bigtable_client_raw";

  BigtableCloudMonitoringExporter(Credentials credentials) throws Exception {
    MetricServiceSettings.Builder settingsBuilder =
        MetricServiceSettings.newBuilder()
            .setTransportChannelProvider(InstantiatingGrpcChannelProvider.newBuilder().build());
    settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials));

    org.threeten.bp.Duration timeout = Duration.ofMinutes(1);
    settingsBuilder.createServiceTimeSeriesSettings().setSimpleTimeoutNoRetries(timeout);
    this.client = MetricServiceClient.create(settingsBuilder.build());
    this.taskId = BigtableExporterUtils.getDefaultTaskValue();
    this.monitoredResource = MonitoredResource.newBuilder().setType(RESOURCE_TYPE).build();
  }

  @VisibleForTesting
  BigtableCloudMonitoringExporter(
      MetricServiceClient client, MonitoredResource monitoredResource, String taskId) {
    this.client = client;
    this.monitoredResource = monitoredResource;
    this.taskId = taskId;
  }

  @Override
  public CompletableResultCode export(Collection<MetricData> collection) {
    Map<String, List<TimeSeries>> projectToTimeSeries;

    for (MetricData metricData : collection) {
      projectToTimeSeries =
          metricData.getData().getPoints().stream()
              .collect(
                  Collectors.groupingBy(
                      BigtableExporterUtils::getProjectId,
                      Collectors.mapping(
                          pointData ->
                              BigtableExporterUtils.convertPointToTimeSeries(
                                  metricData, pointData, taskId, monitoredResource),
                          Collectors.toList())));

      for (Map.Entry<String, List<TimeSeries>> entry : projectToTimeSeries.entrySet()) {
        ProjectName projectName = ProjectName.of(entry.getKey());
        CreateTimeSeriesRequest request =
            CreateTimeSeriesRequest.newBuilder()
                .setName(projectName.toString())
                .addAllTimeSeries(entry.getValue())
                .build();

        try {
          this.client.createServiceTimeSeries(request);
        } catch (Throwable e) {
          logger.log(
              Level.WARNING,
              "Exception thrown when exporting TimeSeries for projectName="
                  + projectName.getProject(),
              e);
        }
      }
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
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public AggregationTemporality getAggregationTemporality(InstrumentType instrumentType) {
    return AggregationTemporality.CUMULATIVE;
  }
}
