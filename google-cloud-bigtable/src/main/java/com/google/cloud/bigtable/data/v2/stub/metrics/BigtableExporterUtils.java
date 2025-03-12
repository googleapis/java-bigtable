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

import static com.google.api.Distribution.BucketOptions;
import static com.google.api.Distribution.BucketOptions.Explicit;
import static com.google.api.MetricDescriptor.MetricKind;
import static com.google.api.MetricDescriptor.MetricKind.CUMULATIVE;
import static com.google.api.MetricDescriptor.MetricKind.GAUGE;
import static com.google.api.MetricDescriptor.MetricKind.UNRECOGNIZED;
import static com.google.api.MetricDescriptor.ValueType;
import static com.google.api.MetricDescriptor.ValueType.DISTRIBUTION;
import static com.google.api.MetricDescriptor.ValueType.DOUBLE;
import static com.google.api.MetricDescriptor.ValueType.INT64;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.BIGTABLE_PROJECT_ID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLIENT_UID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLUSTER_ID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.INSTANCE_ID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.METER_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.TABLE_ID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.ZONE_ID_KEY;

import com.google.api.Distribution;
import com.google.api.Metric;
import com.google.api.MonitoredResource;
import com.google.api.core.InternalApi;
import com.google.auth.Credentials;
import com.google.cloud.opentelemetry.detectors.GCPResource;
import com.google.cloud.opentelemetry.metric.GoogleCloudMetricExporter;
import com.google.cloud.opentelemetry.metric.MetricConfiguration;
import com.google.cloud.opentelemetry.metric.MetricDescriptorStrategy;
import com.google.common.collect.ImmutableSet;
import com.google.monitoring.v3.Point;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.monitoring.v3.TypedValue;
import com.google.protobuf.util.Timestamps;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.DoublePointData;
import io.opentelemetry.sdk.metrics.data.HistogramData;
import io.opentelemetry.sdk.metrics.data.HistogramPointData;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.data.MetricDataType;
import io.opentelemetry.sdk.metrics.data.PointData;
import io.opentelemetry.sdk.metrics.data.SumData;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

/** Utils to convert OpenTelemetry types to Google Cloud Monitoring types. */
@InternalApi
public class BigtableExporterUtils {
  // This system property can be used to override the monitoring endpoint
  // to a different environment. It's meant for internal testing only and
  // will be removed in future versions. Use settings in EnhancedBigtableStubSettings
  // to override the endpoint.
  @Deprecated @Nullable
  public static final String MONITORING_ENDPOINT_OVERRIDE_SYS_PROP =
      System.getProperty("bigtable.test-monitoring-endpoint");

  private static final Set<String> SUPPORTED_INTERNAL_PLATFORMS =
      ImmutableSet.of("gcp_compute_engine", "gcp_kubernetes_engine");

  private static final Logger logger = Logger.getLogger(BigtableExporterUtils.class.getName());

  private static final String BIGTABLE_RESOURCE_TYPE = "bigtable_client_raw";

  // These metric labels will be promoted to the bigtable_table monitored resource fields
  private static final Set<AttributeKey<String>> BIGTABLE_PROMOTED_RESOURCE_LABELS =
      ImmutableSet.of(
          BIGTABLE_PROJECT_ID_KEY, INSTANCE_ID_KEY, TABLE_ID_KEY, CLUSTER_ID_KEY, ZONE_ID_KEY);

  private BigtableExporterUtils() {}

  /**
   * In most cases this should look like java-${UUID}@${hostname}. The hostname will be retrieved
   * from the jvm name and fallback to the local hostname.
   */
  public static String getDefaultTaskValue() {
    // Something like '<pid>@<hostname>'
    final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
    // If jvm doesn't have the expected format, fallback to the local hostname
    if (jvmName.indexOf('@') < 1) {
      String hostname = "localhost";
      try {
        hostname = InetAddress.getLocalHost().getHostName();
      } catch (UnknownHostException e) {
        logger.log(Level.INFO, "Unable to get the hostname.", e);
      }
      // Generate a random number and use the same format "random_number@hostname".
      return "java-" + UUID.randomUUID() + "@" + hostname;
    }
    return "java-" + UUID.randomUUID() + jvmName;
  }

  static String getProjectId(PointData pointData) {
    return pointData.getAttributes().get(BIGTABLE_PROJECT_ID_KEY);
  }

  // Returns a list of timeseries by project id
  static Map<String, List<TimeSeries>> convertToBigtableTimeSeries(
      List<MetricData> collection, String taskId) {
    Map<String, List<TimeSeries>> allTimeSeries = new HashMap<>();

    for (MetricData metricData : collection) {
      if (!metricData.getInstrumentationScopeInfo().getName().equals(METER_NAME)) {
        // Filter out metric data for instruments that are not part of the bigtable builtin metrics
        continue;
      }

      for (PointData pd : metricData.getData().getPoints()) {
        String projectId = getProjectId(pd);
        List<TimeSeries> current =
            allTimeSeries.computeIfAbsent(projectId, ignored -> new ArrayList<>());
        current.add(convertPointToBigtableTimeSeries(metricData, pd, taskId));
        allTimeSeries.put(projectId, current);
      }
    }

    return allTimeSeries;
  }

  @Nullable
  public static OpenTelemetrySdk createInternalOtel(@Nullable String endpoint, Credentials creds) {
    // TODO: replace with newer api
    com.google.cloud.opentelemetry.detectors.GCPResource gcpResource = new GCPResource();

    if (!SUPPORTED_INTERNAL_PLATFORMS.contains(
        gcpResource.getAttributes().get(AttributeKey.stringKey("cloud.platform")))) {
      return null;
    }

    MetricConfiguration.Builder exporterBuilder =
        MetricConfiguration.builder()
            .setProjectId(
                gcpResource.getAttributes().get(AttributeKey.stringKey("cloud.account.id")))
            .setDeadline(Duration.ofMinutes(1))
            .setDescriptorStrategy(MetricDescriptorStrategy.NEVER_SEND)
            .setCredentials(creds)
            .setPrefix("bigtable.googleapis.com")
            .setUseServiceTimeSeries(true);

    {
      @Nullable String effectiveEndpoint = endpoint;
      if (effectiveEndpoint == null) {
        effectiveEndpoint = MONITORING_ENDPOINT_OVERRIDE_SYS_PROP;
      }
      if (effectiveEndpoint != null) {
        exporterBuilder.setMetricServiceEndpoint(effectiveEndpoint);
      }
    }

    SdkMeterProviderBuilder meterProviderBuilder =
        SdkMeterProvider.builder()
            .setResource(Resource.create(gcpResource.getAttributes()))
            .registerMetricReader(
                PeriodicMetricReader.builder(
                        GoogleCloudMetricExporter.createWithConfiguration(exporterBuilder.build()))
                    .setInterval(Duration.ofMinutes(1))
                    .build());

    return OpenTelemetrySdk.builder().setMeterProvider(meterProviderBuilder.build()).build();
  }

  private static TimeSeries convertPointToBigtableTimeSeries(
      MetricData metricData, PointData pointData, String taskId) {
    TimeSeries.Builder builder =
        TimeSeries.newBuilder()
            .setMetricKind(convertMetricKind(metricData))
            .setValueType(convertValueType(metricData.getType()));
    Metric.Builder metricBuilder = Metric.newBuilder().setType(metricData.getName());

    Attributes attributes = pointData.getAttributes();
    MonitoredResource.Builder monitoredResourceBuilder =
        MonitoredResource.newBuilder().setType(BIGTABLE_RESOURCE_TYPE);

    for (AttributeKey<?> key : attributes.asMap().keySet()) {
      if (BIGTABLE_PROMOTED_RESOURCE_LABELS.contains(key)) {
        monitoredResourceBuilder.putLabels(key.getKey(), String.valueOf(attributes.get(key)));
      } else {
        metricBuilder.putLabels(key.getKey(), String.valueOf(attributes.get(key)));
      }
    }

    builder.setResource(monitoredResourceBuilder.build());

    metricBuilder.putLabels(CLIENT_UID_KEY.getKey(), taskId);
    builder.setMetric(metricBuilder.build());

    TimeInterval timeInterval =
        TimeInterval.newBuilder()
            .setStartTime(Timestamps.fromNanos(pointData.getStartEpochNanos()))
            .setEndTime(Timestamps.fromNanos(pointData.getEpochNanos()))
            .build();

    builder.addPoints(createPoint(metricData.getType(), pointData, timeInterval));

    return builder.build();
  }

  private static MetricKind convertMetricKind(MetricData metricData) {
    switch (metricData.getType()) {
      case HISTOGRAM:
      case EXPONENTIAL_HISTOGRAM:
        return convertHistogramType(metricData.getHistogramData());
      case LONG_GAUGE:
      case DOUBLE_GAUGE:
        return GAUGE;
      case LONG_SUM:
        return convertSumDataType(metricData.getLongSumData());
      case DOUBLE_SUM:
        return convertSumDataType(metricData.getDoubleSumData());
      default:
        return UNRECOGNIZED;
    }
  }

  private static MetricKind convertHistogramType(HistogramData histogramData) {
    if (histogramData.getAggregationTemporality() == AggregationTemporality.CUMULATIVE) {
      return CUMULATIVE;
    }
    return UNRECOGNIZED;
  }

  private static MetricKind convertSumDataType(SumData<?> sum) {
    if (!sum.isMonotonic()) {
      return GAUGE;
    }
    if (sum.getAggregationTemporality() == AggregationTemporality.CUMULATIVE) {
      return CUMULATIVE;
    }
    return UNRECOGNIZED;
  }

  private static ValueType convertValueType(MetricDataType metricDataType) {
    switch (metricDataType) {
      case LONG_GAUGE:
      case LONG_SUM:
        return INT64;
      case DOUBLE_GAUGE:
      case DOUBLE_SUM:
        return DOUBLE;
      case HISTOGRAM:
      case EXPONENTIAL_HISTOGRAM:
        return DISTRIBUTION;
      default:
        return ValueType.UNRECOGNIZED;
    }
  }

  private static Point createPoint(
      MetricDataType type, PointData pointData, TimeInterval timeInterval) {
    Point.Builder builder = Point.newBuilder().setInterval(timeInterval);
    switch (type) {
      case HISTOGRAM:
      case EXPONENTIAL_HISTOGRAM:
        return builder
            .setValue(
                TypedValue.newBuilder()
                    .setDistributionValue(convertHistogramData((HistogramPointData) pointData))
                    .build())
            .build();
      case DOUBLE_GAUGE:
      case DOUBLE_SUM:
        return builder
            .setValue(
                TypedValue.newBuilder()
                    .setDoubleValue(((DoublePointData) pointData).getValue())
                    .build())
            .build();
      case LONG_GAUGE:
      case LONG_SUM:
        return builder
            .setValue(TypedValue.newBuilder().setInt64Value(((LongPointData) pointData).getValue()))
            .build();
      default:
        logger.log(Level.WARNING, "unsupported metric type");
        return builder.build();
    }
  }

  private static Distribution convertHistogramData(HistogramPointData pointData) {
    return Distribution.newBuilder()
        .setCount(pointData.getCount())
        .setMean(pointData.getCount() == 0L ? 0.0D : pointData.getSum() / pointData.getCount())
        .setBucketOptions(
            BucketOptions.newBuilder()
                .setExplicitBuckets(Explicit.newBuilder().addAllBounds(pointData.getBoundaries())))
        .addAllBucketCounts(pointData.getCounts())
        .build();
  }
}
