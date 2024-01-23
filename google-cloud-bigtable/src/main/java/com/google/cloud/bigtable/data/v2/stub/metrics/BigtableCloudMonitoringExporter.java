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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.threeten.bp.Duration;

/**
 * Bigtable Cloud Monitoring OpenTelemetry Exporter.
 *
 * <p>The exporter will look for all bigtable owned metrics under bigtable.googleapis.com
 * instrumentation scope and upload it via the Google Cloud Monitoring API.
 */
final class BigtableCloudMonitoringExporter implements MetricExporter {

  private static final Logger logger =
      Logger.getLogger(BigtableCloudMonitoringExporter.class.getName());
  private final MetricServiceClient client;

  private final String projectId;
  private final String taskId;
  private final MonitoredResource monitoredResource;
  private final AtomicBoolean isShutdown = new AtomicBoolean(false);

  private static final String RESOURCE_TYPE = "bigtable_client_raw";

  private CompletableResultCode lastExportCode;

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
      logger.log(Level.WARNING, "Exporter is shutting down");
      return CompletableResultCode.ofFailure();
    }
    if (!collection.stream()
        .flatMap(metricData -> metricData.getData().getPoints().stream())
        .allMatch(pd -> projectId.equals(BigtableExporterUtils.getProjectId(pd)))) {
      logger.log(Level.WARNING, "Metric data has different a projectId. Skip exporting.");
      return CompletableResultCode.ofFailure();
    }

    List<TimeSeries> allTimeSeries;
    try {
      allTimeSeries =
          BigtableExporterUtils.convertCollectionToListOfTimeSeries(
              collection, taskId, monitoredResource);
    } catch (Throwable e) {
      logger.log(Level.WARNING, "Failed to convert metric data to cloud monitoring timeseries.", e);
      return CompletableResultCode.ofFailure();
    }

    ProjectName projectName = ProjectName.of(projectId);
    CreateTimeSeriesRequest request =
        CreateTimeSeriesRequest.newBuilder()
            .setName(projectName.toString())
            .addAllTimeSeries(allTimeSeries)
            .build();

    ApiFuture<Empty> future = this.client.createServiceTimeSeriesCallable().futureCall(request);

    lastExportCode = new CompletableResultCode();

    ApiFutures.addCallback(
        future,
        new ApiFutureCallback<Empty>() {
          @Override
          public void onFailure(Throwable throwable) {
            logger.log(Level.WARNING, "createServiceTimeSeries request failed. ", throwable);
            lastExportCode.fail();
          }

          @Override
          public void onSuccess(Empty empty) {
            lastExportCode.succeed();
          }
        },
        MoreExecutors.directExecutor());

    return lastExportCode;
  }

  @Override
  public CompletableResultCode flush() {
    if (lastExportCode != null) {
      return lastExportCode;
    }
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode shutdown() {
    if (!isShutdown.compareAndSet(false, true)) {
      logger.log(Level.WARNING, "shutdown is called multiple times");
      return CompletableResultCode.ofSuccess();
    }
    CompletableResultCode flushResult = flush();
    CompletableResultCode shutdownResult = new CompletableResultCode();
    flushResult.whenComplete(
        () -> {
          Throwable throwable = null;
          try {
            client.shutdown();
          } catch (Throwable e) {
            logger.log(Level.WARNING, "failed to shutdown the monitoring client", e);
            throwable = e;
          }
          if (throwable != null) {
            shutdownResult.fail();
          } else {
            shutdownResult.succeed();
          }
        });
    return CompletableResultCode.ofAll(Arrays.asList(flushResult, shutdownResult));
  }

  /**
   * For Google Cloud Monitoring always return CUMULATIVE to keep track of the cumulative value of a
   * metric over time.
   */
  @Override
  public AggregationTemporality getAggregationTemporality(InstrumentType instrumentType) {
    return AggregationTemporality.CUMULATIVE;
  }
}
