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
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CLIENT_UID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CLUSTER_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.INSTANCE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.PROJECT_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.TABLE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.ZONE_ID;

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
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Utils to convert OpenTelemetry types to Cloud Monitoring API types. */
class BigtableExporterUtils {

  private static final Logger logger = Logger.getLogger(BigtableExporterUtils.class.getName());

  // These metric labels will be promoted to the bigtable_table monitored resource fields
  private static final Set<AttributeKey<String>> PROMOTED_RESOURCE_LABELS =
      ImmutableSet.of(PROJECT_ID, INSTANCE_ID, TABLE_ID, CLUSTER_ID, ZONE_ID);

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
    return pointData.getAttributes().get(PROJECT_ID);
  }

  static List<TimeSeries> convertCollectionToListOfTimeSeries(
      Collection<MetricData> collection, String taskId, MonitoredResource monitoredResource) {
    List<TimeSeries> allTimeSeries = new ArrayList<>();

    for (MetricData metricData : collection) {
      if (!metricData.getInstrumentationScopeInfo().getName().equals("bigtable.googleapis.com")) {
        continue;
      }
      metricData.getData().getPoints().stream()
          .map(
              pointData ->
                  convertPointToTimeSeries(metricData, pointData, taskId, monitoredResource))
          .forEach(allTimeSeries::add);
    }

    return allTimeSeries;
  }

  private static TimeSeries convertPointToTimeSeries(
      MetricData metricData,
      PointData pointData,
      String taskId,
      MonitoredResource monitoredResource) {
    Attributes attributes = pointData.getAttributes();
    MonitoredResource.Builder monitoredResourceBuilder = monitoredResource.toBuilder();
    Metric.Builder metricBuilder = Metric.newBuilder().setType(metricData.getName());

    for (AttributeKey<?> key : attributes.asMap().keySet()) {
      if (PROMOTED_RESOURCE_LABELS.contains(key)) {
        monitoredResourceBuilder.putLabels(key.getKey(), String.valueOf(attributes.get(key)));
      } else {
        metricBuilder.putLabels(key.getKey(), String.valueOf(attributes.get(key)));
      }
    }
    metricBuilder.putLabels(CLIENT_UID.getKey(), taskId);

    TimeSeries.Builder builder =
        TimeSeries.newBuilder()
            .setResource(monitoredResourceBuilder.build())
            .setMetricKind(convertMetricKind(metricData))
            .setMetric(metricBuilder.build())
            .setValueType(convertValueType(metricData.getType()));

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
