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
import com.google.api.core.InternalApi;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.auth.Credentials;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceSettings;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
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
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.threeten.bp.Duration;

/**
 * Bigtable Cloud Monitoring OpenTelemetry Exporter.
 *
 * <p>The exporter will look for all bigtable owned metrics under bigtable.googleapis.com
 * instrumentation scope and upload it via the Google Cloud Monitoring API.
 */
@InternalApi
public final class BigtableCloudMonitoringExporter implements MetricExporter {

  private static final Logger logger =
      Logger.getLogger(BigtableCloudMonitoringExporter.class.getName());

  // This system property can be used to override the monitoring endpoint
  // to a different environment. It's meant for internal testing only.
  private static final String MONITORING_ENDPOINT =
      MoreObjects.firstNonNull(
          System.getProperty("bigtable.test-monitoring-endpoint"),
          MetricServiceSettings.getDefaultEndpoint());

  private static String GCE_OR_GKE_PROJECT_ID_LABEL = "project_id";

  private final MetricServiceClient client;

  private final String bigtableProjectId;
  private final String taskId;

  private final MonitoredResource gceOrGkeResource;

  private final AtomicBoolean isShutdown = new AtomicBoolean(false);

  private CompletableResultCode lastExportCode;

  public static BigtableCloudMonitoringExporter create(
      String projectId, @Nullable Credentials credentials) throws IOException {
    MetricServiceSettings.Builder settingsBuilder = MetricServiceSettings.newBuilder();
    CredentialsProvider credentialsProvider;
    if (credentials == null) {
      credentialsProvider = NoCredentialsProvider.create();
    } else {
      credentialsProvider = FixedCredentialsProvider.create(credentials);
    }
    settingsBuilder.setCredentialsProvider(credentialsProvider);
    settingsBuilder.setEndpoint(MONITORING_ENDPOINT);

    org.threeten.bp.Duration timeout = Duration.ofMinutes(1);
    // TODO: createServiceTimeSeries needs special handling if the request failed. Leaving
    // it as not retried for now.
    settingsBuilder.createServiceTimeSeriesSettings().setSimpleTimeoutNoRetries(timeout);

    MonitoredResource gceOrGkeResource = BigtableExporterUtils.detectResource();

    logger.log(Level.INFO, "Detected resource: " + gceOrGkeResource);

    return new BigtableCloudMonitoringExporter(
        projectId,
        MetricServiceClient.create(settingsBuilder.build()),
        gceOrGkeResource,
        BigtableExporterUtils.getDefaultTaskValue());
  }

  @VisibleForTesting
  BigtableCloudMonitoringExporter(
      String projectId,
      MetricServiceClient client,
      @Nullable MonitoredResource gceOrGkeResource,
      String taskId) {
    this.client = client;
    this.taskId = taskId;
    this.gceOrGkeResource = gceOrGkeResource;
    this.bigtableProjectId = projectId;
  }

  @Override
  public CompletableResultCode export(Collection<MetricData> collection) {
    if (isShutdown.get()) {
      logger.log(Level.WARNING, "Exporter is shutting down");
      return CompletableResultCode.ofFailure();
    }
    if (!collection.stream()
        .flatMap(metricData -> metricData.getData().getPoints().stream())
        .allMatch(pd -> bigtableProjectId.equals(BigtableExporterUtils.getProjectId(pd)))) {
      logger.log(Level.WARNING, "Metric data has different a projectId. Skip exporting.");
      return CompletableResultCode.ofFailure();
    }

    List<TimeSeries> bigtableTimeSeries;
    try {
      bigtableTimeSeries =
          BigtableExporterUtils.convertToBigtableTimeSeries(
              collection.stream()
                  .filter(
                      md ->
                          !md.getName()
                              .contains(BuiltinMetricsConstants.PER_CONNECTION_ERROR_COUNT_NAME))
                  .collect(Collectors.toList()),
              taskId);
    } catch (Throwable e) {
      logger.log(
          Level.WARNING,
          "Failed to convert bigtable metric data to cloud monitoring timeseries.",
          e);
      return CompletableResultCode.ofFailure();
    }

    ProjectName projectName = ProjectName.of(bigtableProjectId);
    CreateTimeSeriesRequest bigtableRequest =
        CreateTimeSeriesRequest.newBuilder()
            .setName(projectName.toString())
            .addAllTimeSeries(bigtableTimeSeries)
            .build();

    ApiFuture<Empty> future =
        this.client.createServiceTimeSeriesCallable().futureCall(bigtableRequest);

    CompletableResultCode bigtableExportCode = new CompletableResultCode();
    ApiFutures.addCallback(
        future,
        new ApiFutureCallback<Empty>() {
          @Override
          public void onFailure(Throwable throwable) {
            logger.log(
                Level.WARNING,
                "createServiceTimeSeries request failed for bigtable metrics. ",
                throwable);
            bigtableExportCode.fail();
          }

          @Override
          public void onSuccess(Empty empty) {
            bigtableExportCode.succeed();
          }
        },
        MoreExecutors.directExecutor());

    CompletableResultCode gceOrGkeExportCode = exportGceOrGkeMetrics(collection);

    lastExportCode =
        CompletableResultCode.ofAll(ImmutableList.of(gceOrGkeExportCode, bigtableExportCode));
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

  private CompletableResultCode exportGceOrGkeMetrics(Collection<MetricData> collection) {
    CompletableResultCode gceOrGkeExportCode = new CompletableResultCode();
    if (gceOrGkeResource == null) {
      return gceOrGkeExportCode.succeed();
    }
    List<TimeSeries> gceOrGceTimeSeries;
    try {
      gceOrGceTimeSeries =
          BigtableExporterUtils.convertToGceOrGkeTimeSeries(
              collection.stream()
                  .filter(
                      md ->
                          md.getName()
                              .contains(BuiltinMetricsConstants.PER_CONNECTION_ERROR_COUNT_NAME))
                  .collect(Collectors.toList()),
              taskId,
              gceOrGkeResource);
    } catch (Throwable e) {
      logger.log(
          Level.WARNING,
          "Failed to convert per connection error count data to cloud monitoring timeseries.",
          e);
      return CompletableResultCode.ofFailure();
    }

    ProjectName gceOrGkeProjectName =
        ProjectName.of(gceOrGkeResource.getLabelsOrThrow(GCE_OR_GKE_PROJECT_ID_LABEL));
    CreateTimeSeriesRequest gceOrGkeRequest =
        CreateTimeSeriesRequest.newBuilder()
            .setName(gceOrGkeProjectName.toString())
            .addAllTimeSeries(gceOrGceTimeSeries)
            .build();

    ApiFuture<Empty> gceOrGkeFuture =
        this.client.createServiceTimeSeriesCallable().futureCall(gceOrGkeRequest);

    ApiFutures.addCallback(
        gceOrGkeFuture,
        new ApiFutureCallback<Empty>() {
          @Override
          public void onFailure(Throwable throwable) {
            logger.log(
                Level.WARNING,
                "createServiceTimeSeries request failed for per connection error metrics.",
                throwable);
            gceOrGkeExportCode.fail();
          }

          @Override
          public void onSuccess(Empty empty) {
            gceOrGkeExportCode.succeed();
          }
        },
        MoreExecutors.directExecutor());

    return gceOrGkeExportCode;
  }
}
