/*
 * Copyright 2021 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.metrics.exporter;

import com.google.api.MonitoredResource;
import com.google.api.core.InternalApi;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.auth.Credentials;
import com.google.bigtable.veneer.repackaged.io.opencensus.common.Duration;
import com.google.bigtable.veneer.repackaged.io.opencensus.exporter.metrics.util.IntervalMetricReader;
import com.google.bigtable.veneer.repackaged.io.opencensus.exporter.metrics.util.MetricReader;
import com.google.bigtable.veneer.repackaged.io.opencensus.exporter.stats.stackdriver.StackdriverStatsConfiguration;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.Metrics;
import com.google.cloud.bigtable.Version;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceSettings;
import com.google.cloud.monitoring.v3.stub.MetricServiceStub;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
@InternalApi
public class BigtableStackdriverStatsExporter {
  @VisibleForTesting static final Object monitor = new Object();

  @Nullable
  @GuardedBy("monitor")
  private static BigtableStackdriverStatsExporter instance = null;

  private static final String EXPORTER_SPAN_NAME = "ExportMetricsToStackdriver";
  private static final String USER_AGENT_KEY = "user-agent";
  private static final String USER_AGENT = "bigtable-java" + Version.VERSION;
  private static final HeaderProvider OPENCENSUS_USER_AGENT_HEADER_PROVIDER =
      FixedHeaderProvider.create(new String[] {USER_AGENT_KEY, USER_AGENT});
  private final IntervalMetricReader intervalMetricReader;

  private BigtableStackdriverStatsExporter(
      String projectId,
      MetricServiceClient metricServiceClient,
      Duration exportInterval,
      MonitoredResource monitoredResource) {
    IntervalMetricReader.Options.Builder intervalMetricReaderOptionsBuilder =
        IntervalMetricReader.Options.builder();
    intervalMetricReaderOptionsBuilder.setExportInterval(exportInterval);
    this.intervalMetricReader =
        IntervalMetricReader.create(
            new BigtableCreateTimeSeriesExporter(
                projectId, metricServiceClient, monitoredResource),
            MetricReader.create(
                com.google.bigtable.veneer.repackaged.io.opencensus.exporter.metrics.util
                    .MetricReader.Options.builder()
                    .setMetricProducerManager(
                        Metrics.getExportComponent().getMetricProducerManager())
                    .setSpanName(EXPORTER_SPAN_NAME)
                    .build()),
            intervalMetricReaderOptionsBuilder.build());
  }

  public static void createAndRegister(StackdriverStatsConfiguration configuration)
      throws IOException {
    Preconditions.checkNotNull(configuration, "configuration");
    createInternal(
        configuration.getCredentials(),
        configuration.getProjectId(),
        configuration.getExportInterval(),
        configuration.getMonitoredResource(),
        configuration.getDeadline(),
        configuration.getMetricServiceStub());
  }

  private static void createInternal(
      @Nullable Credentials credentials,
      String projectId,
      Duration exportInterval,
      MonitoredResource monitoredResource,
      Duration deadline,
      @Nullable MetricServiceStub stub)
      throws IOException {
    synchronized (monitor) {
      Preconditions.checkState(instance == null, "Stackdriver stats exporter is already created.");
      MetricServiceClient client =
          stub == null
              ? createMetricServiceClient(credentials, deadline)
              : MetricServiceClient.create(stub);
      instance =
          new BigtableStackdriverStatsExporter(
              projectId, client, exportInterval, monitoredResource);
    }
  }

  @GuardedBy("monitor")
  @VisibleForTesting
  static MetricServiceClient createMetricServiceClient(
      @Nullable Credentials credentials, Duration deadline) throws IOException {
    com.google.cloud.monitoring.v3.MetricServiceSettings.Builder settingsBuilder =
        (com.google.cloud.monitoring.v3.MetricServiceSettings.Builder)
            MetricServiceSettings.newBuilder()
                .setTransportChannelProvider(
                    InstantiatingGrpcChannelProvider.newBuilder()
                        .setHeaderProvider(OPENCENSUS_USER_AGENT_HEADER_PROVIDER)
                        .build());
    if (credentials != null) {
      settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials));
    }

    org.threeten.bp.Duration stackdriverDuration =
        org.threeten.bp.Duration.ofMillis(deadline.toMillis());
    settingsBuilder.createMetricDescriptorSettings().setSimpleTimeoutNoRetries(stackdriverDuration);
    settingsBuilder.createTimeSeriesSettings().setSimpleTimeoutNoRetries(stackdriverDuration);
    return MetricServiceClient.create(settingsBuilder.build());
  }

  public static void unregister() {
    synchronized (monitor) {
      if (instance != null) {
        instance.intervalMetricReader.stop();
      }

      instance = null;
    }
  }
}
