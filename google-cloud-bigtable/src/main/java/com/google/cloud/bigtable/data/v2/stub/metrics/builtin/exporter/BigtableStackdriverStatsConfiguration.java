package com.google.cloud.bigtable.data.v2.stub.metrics.builtin.exporter;

import com.google.api.MonitoredResource;
import com.google.auth.Credentials;
import com.google.auto.value.AutoValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.common.Duration;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelValue;
import com.google.cloud.ServiceOptions;
import com.google.cloud.monitoring.v3.stub.MetricServiceStub;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@AutoValue
@Immutable
public abstract class BigtableStackdriverStatsConfiguration {
  static final Duration DEFAULT_INTERVAL = Duration.create(60L, 0);
  static final MonitoredResource DEFAULT_RESOURCE =
      BigtableStackdriverExportUtils.getDefaultResource();
  static final String DEFAULT_PROJECT_ID =
      Strings.nullToEmpty(ServiceOptions.getDefaultProjectId());
  static final Duration DEFAULT_DEADLINE = Duration.create(60L, 0);

  BigtableStackdriverStatsConfiguration() {}

  @Nullable
  public abstract Credentials getCredentials();

  public abstract String getProjectId();

  public abstract Duration getExportInterval();

  public abstract MonitoredResource getMonitoredResource();

  @Nullable
  public abstract String getMetricNamePrefix();

  @Nullable
  public abstract String getDisplayNamePrefix();

  public abstract Map<LabelKey, LabelValue> getConstantLabels();

  public abstract Duration getDeadline();

  @Nullable
  public abstract MetricServiceStub getMetricServiceStub();

  public static BigtableStackdriverStatsConfiguration.Builder builder() {
    return (new AutoValue_BigtableStackdriverStatsConfiguration.Builder())
        .setProjectId(DEFAULT_PROJECT_ID)
        .setConstantLabels(BigtableStackdriverExportUtils.DEFAULT_CONSTANT_LABELS)
        .setExportInterval(DEFAULT_INTERVAL)
        .setMonitoredResource(DEFAULT_RESOURCE)
        .setDeadline(DEFAULT_DEADLINE);
  }

  @AutoValue.Builder
  public abstract static class Builder {
    @VisibleForTesting static final Duration ZERO = Duration.fromMillis(0L);

    Builder() {}

    public abstract BigtableStackdriverStatsConfiguration.Builder setCredentials(Credentials var1);

    public abstract BigtableStackdriverStatsConfiguration.Builder setProjectId(String var1);

    public abstract BigtableStackdriverStatsConfiguration.Builder setExportInterval(Duration var1);

    public abstract BigtableStackdriverStatsConfiguration.Builder setMonitoredResource(
        MonitoredResource var1);

    public abstract BigtableStackdriverStatsConfiguration.Builder setMetricNamePrefix(String var1);

    public abstract BigtableStackdriverStatsConfiguration.Builder setDisplayNamePrefix(String var1);

    public abstract BigtableStackdriverStatsConfiguration.Builder setConstantLabels(
        Map<LabelKey, LabelValue> var1);

    public abstract BigtableStackdriverStatsConfiguration.Builder setDeadline(Duration var1);

    public abstract BigtableStackdriverStatsConfiguration.Builder setMetricServiceStub(
        MetricServiceStub var1);

    abstract String getProjectId();

    abstract Map<LabelKey, LabelValue> getConstantLabels();

    abstract Duration getDeadline();

    abstract BigtableStackdriverStatsConfiguration autoBuild();

    public BigtableStackdriverStatsConfiguration build() {
      this.setConstantLabels(
          Collections.unmodifiableMap(new LinkedHashMap(this.getConstantLabels())));
      Preconditions.checkArgument(
          !Strings.isNullOrEmpty(this.getProjectId()),
          "Cannot find a project ID from either configurations or application default.");
      Iterator var1 = this.getConstantLabels().entrySet().iterator();

      while (var1.hasNext()) {
        Map.Entry<LabelKey, LabelValue> constantLabel = (Map.Entry) var1.next();
        Preconditions.checkNotNull(constantLabel.getKey(), "constant label key");
        Preconditions.checkNotNull(constantLabel.getValue(), "constant label value");
      }

      Preconditions.checkArgument(
          this.getDeadline().compareTo(ZERO) > 0, "Deadline must be positive.");
      return this.autoBuild();
    }
  }
}
