package com.google.cloud.bigtable.data.v2.stub.metrics.builtin.exporter;


import com.google.api.MonitoredResource;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.api.gax.rpc.HeaderProvider;
import com.google.auth.Credentials;
import com.google.bigtable.veneer.repackaged.io.opencensus.common.Duration;
import com.google.bigtable.veneer.repackaged.io.opencensus.exporter.metrics.util.IntervalMetricReader;
import com.google.bigtable.veneer.repackaged.io.opencensus.exporter.metrics.util.MetricReader;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.Metrics;
import com.google.cloud.bigtable.Version;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.MetricServiceSettings;
import com.google.cloud.monitoring.v3.stub.MetricServiceStub;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
class BigtableStackdriverStatsExporter {
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
      MonitoredResource monitoredResource,
      @Nullable String metricNamePrefix,
      @Nullable String displayNamePrefix,
      Map<LabelKey, LabelValue> constantLabels) {
    IntervalMetricReader.Options.Builder intervalMetricReaderOptionsBuilder =
        IntervalMetricReader.Options.builder();
    intervalMetricReaderOptionsBuilder.setExportInterval(exportInterval);
    this.intervalMetricReader =
        IntervalMetricReader.create(
            new BigtableCreateMetricDescriptorExporter(
                projectId,
                metricServiceClient,
                metricNamePrefix,
                displayNamePrefix,
                constantLabels,
                new BigtableCreateTimeSeriesExporter(
                    projectId,
                    metricServiceClient,
                    monitoredResource,
                    metricNamePrefix,
                    constantLabels)),
            MetricReader.create(
                com.google.bigtable.veneer.repackaged.io.opencensus.exporter.metrics.util
                    .MetricReader.Options.builder()
                    .setMetricProducerManager(
                        Metrics.getExportComponent().getMetricProducerManager())
                    .setSpanName(EXPORTER_SPAN_NAME)
                    .build()),
            intervalMetricReaderOptionsBuilder.build());
  }

  /** @deprecated */
  @Deprecated
  public static void createAndRegisterWithCredentialsAndProjectId(
      Credentials credentials, String projectId, Duration exportInterval) throws IOException {
    Preconditions.checkNotNull(credentials, "credentials");
    Preconditions.checkNotNull(projectId, "projectId");
    Preconditions.checkNotNull(exportInterval, "exportInterval");
    createInternal(
        credentials,
        projectId,
        exportInterval,
        BigtableStackdriverStatsConfiguration.DEFAULT_RESOURCE,
        (String) null,
        (String) null,
        BigtableStackdriverExportUtils.DEFAULT_CONSTANT_LABELS,
        BigtableStackdriverStatsConfiguration.DEFAULT_DEADLINE,
        (MetricServiceStub) null);
  }

  /** @deprecated */
  @Deprecated
  public static void createAndRegisterWithProjectId(String projectId, Duration exportInterval)
      throws IOException {
    Preconditions.checkNotNull(projectId, "projectId");
    Preconditions.checkNotNull(exportInterval, "exportInterval");
    createInternal(
        (Credentials) null,
        projectId,
        exportInterval,
        BigtableStackdriverStatsConfiguration.DEFAULT_RESOURCE,
        (String) null,
        (String) null,
        BigtableStackdriverExportUtils.DEFAULT_CONSTANT_LABELS,
        BigtableStackdriverStatsConfiguration.DEFAULT_DEADLINE,
        (MetricServiceStub) null);
  }

  public static void createAndRegister(BigtableStackdriverStatsConfiguration configuration)
      throws IOException {
    Preconditions.checkNotNull(configuration, "configuration");
    createInternal(
        configuration.getCredentials(),
        configuration.getProjectId(),
        configuration.getExportInterval(),
        configuration.getMonitoredResource(),
        configuration.getMetricNamePrefix(),
        configuration.getDisplayNamePrefix(),
        configuration.getConstantLabels(),
        configuration.getDeadline(),
        configuration.getMetricServiceStub());
  }

  public static void createAndRegister() throws IOException {
    createAndRegister(BigtableStackdriverStatsConfiguration.builder().build());
  }

  /** @deprecated */
  @Deprecated
  public static void createAndRegister(Duration exportInterval) throws IOException {
    Preconditions.checkNotNull(exportInterval, "exportInterval");
    Preconditions.checkArgument(
        !BigtableStackdriverStatsConfiguration.DEFAULT_PROJECT_ID.isEmpty(),
        "Cannot find a project ID from application default.");
    createInternal(
        (Credentials) null,
        BigtableStackdriverStatsConfiguration.DEFAULT_PROJECT_ID,
        exportInterval,
        BigtableStackdriverStatsConfiguration.DEFAULT_RESOURCE,
        (String) null,
        (String) null,
        BigtableStackdriverExportUtils.DEFAULT_CONSTANT_LABELS,
        BigtableStackdriverStatsConfiguration.DEFAULT_DEADLINE,
        (MetricServiceStub) null);
  }

  /** @deprecated */
  @Deprecated
  public static void createAndRegisterWithProjectIdAndMonitoredResource(
      String projectId, Duration exportInterval, MonitoredResource monitoredResource)
      throws IOException {
    Preconditions.checkNotNull(projectId, "projectId");
    Preconditions.checkNotNull(exportInterval, "exportInterval");
    Preconditions.checkNotNull(monitoredResource, "monitoredResource");
    createInternal(
        (Credentials) null,
        projectId,
        exportInterval,
        monitoredResource,
        (String) null,
        (String) null,
        BigtableStackdriverExportUtils.DEFAULT_CONSTANT_LABELS,
        BigtableStackdriverStatsConfiguration.DEFAULT_DEADLINE,
        (MetricServiceStub) null);
  }

  /** @deprecated */
  @Deprecated
  public static void createAndRegisterWithMonitoredResource(
      Duration exportInterval, MonitoredResource monitoredResource) throws IOException {
    Preconditions.checkNotNull(exportInterval, "exportInterval");
    Preconditions.checkNotNull(monitoredResource, "monitoredResource");
    Preconditions.checkArgument(
        !BigtableStackdriverStatsConfiguration.DEFAULT_PROJECT_ID.isEmpty(),
        "Cannot find a project ID from application default.");
    createInternal(
        (Credentials) null,
        BigtableStackdriverStatsConfiguration.DEFAULT_PROJECT_ID,
        exportInterval,
        monitoredResource,
        (String) null,
        (String) null,
        BigtableStackdriverExportUtils.DEFAULT_CONSTANT_LABELS,
        BigtableStackdriverStatsConfiguration.DEFAULT_DEADLINE,
        (MetricServiceStub) null);
  }

  private static void createInternal(
      @Nullable Credentials credentials,
      String projectId,
      Duration exportInterval,
      MonitoredResource monitoredResource,
      @Nullable String metricNamePrefix,
      @Nullable String displayNamePrefix,
      Map<LabelKey, LabelValue> constantLabels,
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
              projectId,
              client,
              exportInterval,
              monitoredResource,
              metricNamePrefix,
              displayNamePrefix,
              constantLabels);
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
