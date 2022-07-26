/*
 * Copyright 2022 Google LLC
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
package com.google.cloud.bigtable.stats;

import com.google.api.Distribution.BucketOptions;
import com.google.api.Distribution.BucketOptions.Explicit;
import com.google.api.MetricDescriptor.MetricKind;
import com.google.api.MonitoredResource;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.monitoring.v3.TypedValue;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import io.opencensus.common.Functions;
import io.opencensus.metrics.LabelKey;
import io.opencensus.metrics.LabelValue;
import io.opencensus.metrics.data.AttachmentValue;
import io.opencensus.metrics.export.Distribution.Bucket;
import io.opencensus.metrics.export.Distribution.BucketOptions.ExplicitOptions;
import io.opencensus.metrics.export.MetricDescriptor.Type;
import io.opencensus.metrics.export.Summary;
import io.opencensus.metrics.export.Value;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

class BigtableStackdriverExportUtils {
  private static final Logger logger;

  private static final io.opencensus.common.Function<Double, TypedValue> typedValueDoubleFunction;
  private static final io.opencensus.common.Function<Long, TypedValue> typedValueLongFunction;
  private static final io.opencensus.common.Function<
          io.opencensus.metrics.export.Distribution, TypedValue>
      typedValueDistributionFunction;
  private static final io.opencensus.common.Function<Summary, TypedValue> typedValueSummaryFunction;
  private static final io.opencensus.common.Function<ExplicitOptions, BucketOptions>
      bucketOptionsExplicitFunction;

  private static final Set<String> PROMOTED_RESOURCE_LABELS =
      ImmutableSet.of(
          BuiltinMeasureConstants.PROJECT_ID.getName(),
          BuiltinMeasureConstants.INSTANCE_ID.getName(),
          BuiltinMeasureConstants.CLUSTER.getName(),
          BuiltinMeasureConstants.ZONE.getName(),
          BuiltinMeasureConstants.TABLE.getName());

  static String getDefaultTaskValue() {
    String jvmName = ManagementFactory.getRuntimeMXBean().getName();
    if (jvmName.indexOf(64) < 1) {
      String hostname = "localhost";

      try {
        hostname = InetAddress.getLocalHost().getHostName();
      } catch (UnknownHostException e) {
        logger.log(Level.INFO, "Unable to get the hostname.", e);
      }

      return "java-" + (new SecureRandom()).nextInt() + "@" + hostname;
    } else {
      return "java-" + jvmName;
    }
  }

  @VisibleForTesting
  static MetricKind createMetricKind(Type type) {
    switch (type) {
      case GAUGE_INT64:
      case GAUGE_DOUBLE:
        return MetricKind.GAUGE;
      case CUMULATIVE_DOUBLE:
      case CUMULATIVE_INT64:
      case CUMULATIVE_DISTRIBUTION:
        return MetricKind.CUMULATIVE;
      default:
        return MetricKind.UNRECOGNIZED;
    }
  }

  @VisibleForTesting
  static com.google.api.MetricDescriptor.ValueType createValueType(Type type) {
    switch (type) {
      case GAUGE_DOUBLE:
      case CUMULATIVE_DOUBLE:
        return com.google.api.MetricDescriptor.ValueType.DOUBLE;
      case GAUGE_INT64:
      case CUMULATIVE_INT64:
        return com.google.api.MetricDescriptor.ValueType.INT64;
      case GAUGE_DISTRIBUTION:
      case CUMULATIVE_DISTRIBUTION:
        return com.google.api.MetricDescriptor.ValueType.DISTRIBUTION;
      default:
        return com.google.api.MetricDescriptor.ValueType.UNRECOGNIZED;
    }
  }

  static TimeSeries convertTimeSeries(
      String metricName,
      io.opencensus.metrics.export.MetricDescriptor.Type metricType,
      List<LabelKey> labelKeys,
      io.opencensus.metrics.export.TimeSeries timeSeries,
      String clientId,
      MonitoredResource monitoredResource) {

    MonitoredResource.Builder monitoredResourceBuilder = monitoredResource.toBuilder();

    List<LabelKey> metricTagKeys = new ArrayList<>();
    List<LabelValue> metricTagValues = new ArrayList<>();

    List<LabelValue> labelValues = timeSeries.getLabelValues();
    for (int i = 0; i < labelValues.size(); i++) {
      if (PROMOTED_RESOURCE_LABELS.contains(labelKeys.get(i).getKey())) {
        monitoredResourceBuilder.putLabels(
            labelKeys.get(i).getKey(), labelValues.get(i).getValue());
      } else {
        metricTagKeys.add(labelKeys.get(i));
        metricTagValues.add(labelValues.get(i));
      }
    }
    metricTagKeys.add(LabelKey.create(BuiltinMeasureConstants.CLIENT_UID.getName(), "client id"));
    metricTagValues.add(LabelValue.create(clientId));

    TimeSeries.Builder builder = TimeSeries.newBuilder();
    builder.setMetricKind(createMetricKind(metricType));
    builder.setResource(monitoredResourceBuilder.build());
    builder.setValueType(createValueType(metricType));
    builder.setMetric(createMetric(metricName, metricTagKeys, metricTagValues));
    io.opencensus.common.Timestamp startTimeStamp = timeSeries.getStartTimestamp();
    for (io.opencensus.metrics.export.Point point : timeSeries.getPoints()) {
      builder.addPoints(createPoint(point, startTimeStamp));
    }
    return builder.build();
  }

  @VisibleForTesting
  static com.google.api.Metric createMetric(
      String metricName, List<LabelKey> labelKeys, List<LabelValue> labelValues) {
    com.google.api.Metric.Builder builder = com.google.api.Metric.newBuilder();
    builder.setType(metricName);
    Map<String, String> stringTagMap = Maps.newHashMap();

    for (int i = 0; i < labelValues.size(); ++i) {
      String value = labelValues.get(i).getValue();
      if (value != null) {
        stringTagMap.put(labelKeys.get(i).getKey(), value);
      }
    }

    builder.putAllLabels(stringTagMap);
    return builder.build();
  }

  @VisibleForTesting
  static com.google.monitoring.v3.Point createPoint(
      io.opencensus.metrics.export.Point point,
      @Nullable io.opencensus.common.Timestamp startTimestamp) {
    com.google.monitoring.v3.TimeInterval.Builder timeIntervalBuilder = TimeInterval.newBuilder();
    timeIntervalBuilder.setEndTime(convertTimestamp(point.getTimestamp()));
    if (startTimestamp != null) {
      timeIntervalBuilder.setStartTime(convertTimestamp(startTimestamp));
    }

    com.google.monitoring.v3.Point.Builder builder = com.google.monitoring.v3.Point.newBuilder();
    builder.setInterval(timeIntervalBuilder.build());
    builder.setValue(createTypedValue(point.getValue()));
    return builder.build();
  }

  @VisibleForTesting
  static TypedValue createTypedValue(Value value) {
    return value.match(
        typedValueDoubleFunction,
        typedValueLongFunction,
        typedValueDistributionFunction,
        typedValueSummaryFunction,
        Functions.throwIllegalArgumentException());
  }

  @VisibleForTesting
  static com.google.api.Distribution createDistribution(
      io.opencensus.metrics.export.Distribution distribution) {
    com.google.api.Distribution.Builder builder =
        com.google.api.Distribution.newBuilder()
            .setBucketOptions(createBucketOptions(distribution.getBucketOptions()))
            .setCount(distribution.getCount())
            .setMean(
                distribution.getCount() == 0L
                    ? 0.0D
                    : distribution.getSum() / (double) distribution.getCount())
            .setSumOfSquaredDeviation(distribution.getSumOfSquaredDeviations());
    setBucketCountsAndExemplars(distribution.getBuckets(), builder);
    return builder.build();
  }

  @VisibleForTesting
  static BucketOptions createBucketOptions(
      @Nullable io.opencensus.metrics.export.Distribution.BucketOptions bucketOptions) {
    com.google.api.Distribution.BucketOptions.Builder builder = BucketOptions.newBuilder();
    return bucketOptions == null
        ? builder.build()
        : bucketOptions.match(
            bucketOptionsExplicitFunction, Functions.throwIllegalArgumentException());
  }

  private static void setBucketCountsAndExemplars(
      List<Bucket> buckets, com.google.api.Distribution.Builder builder) {
    builder.addBucketCounts(0L);

    for (Bucket bucket : buckets) {
      builder.addBucketCounts(bucket.getCount());
      io.opencensus.metrics.data.Exemplar exemplar = bucket.getExemplar();
      if (exemplar != null) {
        builder.addExemplars(toProtoExemplar(exemplar));
      }
    }
  }

  private static com.google.api.Distribution.Exemplar toProtoExemplar(
      io.opencensus.metrics.data.Exemplar exemplar) {
    com.google.api.Distribution.Exemplar.Builder builder =
        com.google.api.Distribution.Exemplar.newBuilder()
            .setValue(exemplar.getValue())
            .setTimestamp(convertTimestamp(exemplar.getTimestamp()));

    for (Map.Entry<String, AttachmentValue> attachment : exemplar.getAttachments().entrySet()) {
      AttachmentValue value = attachment.getValue();
      builder.addAttachments(toProtoStringAttachment(value));
    }

    return builder.build();
  }

  private static Any toProtoStringAttachment(AttachmentValue attachmentValue) {
    return Any.newBuilder()
        .setTypeUrl("type.googleapis.com/google.protobuf.StringValue")
        .setValue(ByteString.copyFromUtf8(attachmentValue.getValue()))
        .build();
  }

  @VisibleForTesting
  static com.google.protobuf.Timestamp convertTimestamp(
      io.opencensus.common.Timestamp censusTimestamp) {
    return censusTimestamp.getSeconds() < 0L
        ? com.google.protobuf.Timestamp.newBuilder().build()
        : com.google.protobuf.Timestamp.newBuilder()
            .setSeconds(censusTimestamp.getSeconds())
            .setNanos(censusTimestamp.getNanos())
            .build();
  }

  private BigtableStackdriverExportUtils() {}

  static {
    logger = Logger.getLogger(BigtableStackdriverExportUtils.class.getName());
    typedValueDoubleFunction =
        arg -> {
          TypedValue.Builder builder = TypedValue.newBuilder();
          builder.setDoubleValue(arg);
          return builder.build();
        };
    typedValueLongFunction =
        arg -> {
          TypedValue.Builder builder = TypedValue.newBuilder();
          builder.setInt64Value(arg);
          return builder.build();
        };
    typedValueDistributionFunction =
        arg -> {
          TypedValue.Builder builder = TypedValue.newBuilder();
          return builder
              .setDistributionValue(BigtableStackdriverExportUtils.createDistribution(arg))
              .build();
        };
    typedValueSummaryFunction =
        arg -> {
          TypedValue.Builder builder = TypedValue.newBuilder();
          return builder.build();
        };
    bucketOptionsExplicitFunction =
        arg -> {
          BucketOptions.Builder builder = BucketOptions.newBuilder();
          Explicit.Builder explicitBuilder = Explicit.newBuilder();
          explicitBuilder.addBounds(0.0D);
          explicitBuilder.addAllBounds(arg.getBucketBoundaries());
          builder.setExplicitBuckets(explicitBuilder.build());
          return builder.build();
        };
  }
}
