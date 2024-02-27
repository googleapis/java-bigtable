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
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLIENT_UID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLUSTER_ID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.INSTANCE_ID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.PROJECT_ID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.TABLE_ID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.ZONE_ID_KEY;

import com.google.api.Distribution;
import com.google.api.Metric;
import com.google.api.MonitoredResource;
import com.google.common.collect.ImmutableSet;
import com.google.monitoring.v3.Point;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.monitoring.v3.TypedValue;
import com.google.protobuf.util.Timestamps;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.DoublePointData;
import io.opentelemetry.sdk.metrics.data.HistogramData;
import io.opentelemetry.sdk.metrics.data.HistogramPointData;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.data.MetricDataType;
import io.opentelemetry.sdk.metrics.data.PointData;
import io.opentelemetry.sdk.metrics.data.SumData;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

/** Utils to convert OpenTelemetry types to Google Cloud Monitoring types. */
class BigtableExporterUtils {

  private static final Logger logger = Logger.getLogger(BigtableExporterUtils.class.getName());

  private static final String BIGTABLE_RESOURCE_TYPE = "bigtable_client_raw";

  // These metric labels will be promoted to the bigtable_table monitored resource fields
  private static final Set<AttributeKey<String>> BIGTABLE_PROMOTED_RESOURCE_LABELS =
      ImmutableSet.of(PROJECT_ID_KEY, INSTANCE_ID_KEY, TABLE_ID_KEY, CLUSTER_ID_KEY, ZONE_ID_KEY);

  private BigtableExporterUtils() {}

  /**
   * In most cases this should look like java-${UUID}@${hostname}. The hostname will be retrieved
   * from the jvm name and fallback to the local hostname.
   */
  static String getDefaultTaskValue() {
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
    return pointData.getAttributes().get(PROJECT_ID_KEY);
  }

  static List<TimeSeries> convertCollectionToListOfTimeSeries(
      Collection<MetricData> collection, String taskId, MonitoredResource gceOrGkeResource) {
    List<TimeSeries> allTimeSeries = new ArrayList<>();

    for (MetricData metricData : collection) {
      if (!metricData
          .getInstrumentationScopeInfo()
          .getName()
          .equals(BuiltinMetricsConstants.METER_NAME)) {
        // Filter out metric data for instruments that are not part of the bigtable builtin metrics
        continue;
      }
      if (gceOrGkeResource == null
          && metricData.getName().equals(BuiltinMetricsConstants.PER_CONNECTION_ERROR_COUNT_NAME)) {
        // Skip exporting per connection error count metric in none gce / gke environment
        continue;
      }
      metricData.getData().getPoints().stream()
          .map(
              pointData ->
                  convertPointToTimeSeries(metricData, pointData, taskId, gceOrGkeResource))
          .forEach(allTimeSeries::add);
    }

    return allTimeSeries;
  }

  @Nullable
  static MonitoredResource detectResource() {
    GCPMetadataConfig metadataConfig = GCPMetadataConfig.DEFAULT_INSTANCE;
    if (metadataConfig.getProjectId() == null || metadataConfig.getProjectId().isEmpty()) {
      // return null if the application is not running on GCP platform
      return null;
    }
    if (System.getenv("KUBERNETES_SERVICE_HOST") != null) {
      createGkeMonitoredResource(metadataConfig);
    } else if (System.getenv("K_CONFIGURATION") == null
        && System.getenv("FUNCTION_TARGET") == null
        && System.getenv("GAE_SERVICE") == null) {
      createGceMonitoredResource(metadataConfig);
    }
    return null;
  }

  private static MonitoredResource createGceMonitoredResource(GCPMetadataConfig metadataConfig) {
    return MonitoredResource.newBuilder()
        .setType("gce_instance")
        .putLabels("project_id", metadataConfig.getProjectId())
        .putLabels("instance_id", metadataConfig.getInstanceId())
        .putLabels("zone", metadataConfig.getZone())
        .build();
  }

  private static MonitoredResource createGkeMonitoredResource(GCPMetadataConfig metadataConfig) {
    return MonitoredResource.newBuilder()
        .setType("k8s_container")
        .putLabels("project_id", metadataConfig.getProjectId())
        .putLabels("location", metadataConfig.getZone())
        .putLabels("cluster_name", metadataConfig.getAttribute("k8s.cluster.name"))
        .putLabels("namespace_name", metadataConfig.getAttribute("k8s.namespace.name"))
        .putLabels("pod_name", metadataConfig.getAttribute("k8s.pod.name"))
        .putLabels("container_name", metadataConfig.getAttribute("k8s.container.name"))
        .build();
  }

  private static TimeSeries convertPointToTimeSeries(
      MetricData metricData,
      PointData pointData,
      String taskId,
      MonitoredResource gceOrGkeResource) {
    TimeSeries.Builder builder =
        TimeSeries.newBuilder()
            .setMetricKind(convertMetricKind(metricData))
            .setValueType(convertValueType(metricData.getType()));
    Metric.Builder metricBuilder = Metric.newBuilder().setType(metricData.getName());

    if (metricData.getName().equals(BuiltinMetricsConstants.PER_CONNECTION_ERROR_COUNT_NAME)) {
      if (gceOrGkeResource != null) {
        Attributes attributes = pointData.getAttributes();
        for (Map.Entry<AttributeKey<?>, Object> entry : attributes.asMap().entrySet()) {
          metricBuilder.putLabels(entry.getKey().getKey(), String.valueOf(entry.getValue()));
        }
        builder.setResource(gceOrGkeResource);
      } else {
        logger.warning(
            "Trying to export metric " + metricData.getName() + " in a non-GCE/GKE environment.");
        return TimeSeries.newBuilder().build();
      }
    } else {
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
    }

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
