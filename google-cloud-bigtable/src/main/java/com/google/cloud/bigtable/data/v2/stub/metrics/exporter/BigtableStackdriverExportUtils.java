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

import com.google.api.Distribution.BucketOptions;
import com.google.api.Distribution.BucketOptions.Explicit;
import com.google.api.MetricDescriptor.MetricKind;
import com.google.api.MonitoredResource;
import com.google.bigtable.veneer.repackaged.io.opencensus.common.Function;
import com.google.bigtable.veneer.repackaged.io.opencensus.common.Functions;
import com.google.bigtable.veneer.repackaged.io.opencensus.contrib.exemplar.util.AttachmentValueSpanContext;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.data.AttachmentValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Distribution.Bucket;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Distribution.BucketOptions.ExplicitOptions;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor.Type;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Summary;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Value;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.monitoring.v3.TypedValue;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

class BigtableStackdriverExportUtils {
  private static final Logger logger;

  private static final com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
          Double, TypedValue>
      typedValueDoubleFunction;
  private static final com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
          Long, TypedValue>
      typedValueLongFunction;
  private static final com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
          com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Distribution,
          TypedValue>
      typedValueDistributionFunction;
  private static final com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
          Summary, TypedValue>
      typedValueSummaryFunction;
  private static final com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
          ExplicitOptions, BucketOptions>
      bucketOptionsExplicitFunction;

  static String generateDefaultTaskValue() {
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

  private static String generateType(String metricName, String domain) {
    return domain + metricName;
  }

  @VisibleForTesting
  static MetricKind createMetricKind(Type type) {
    if (type != Type.GAUGE_INT64 && type != Type.GAUGE_DOUBLE) {
      return type != Type.CUMULATIVE_INT64
              && type != Type.CUMULATIVE_DOUBLE
              && type != Type.CUMULATIVE_DISTRIBUTION
          ? MetricKind.UNRECOGNIZED
          : MetricKind.CUMULATIVE;
    } else {
      return MetricKind.GAUGE;
    }
  }

  @VisibleForTesting
  static com.google.api.MetricDescriptor.ValueType createValueType(Type type) {
    if (type != Type.CUMULATIVE_DOUBLE && type != Type.GAUGE_DOUBLE) {
      if (type != Type.GAUGE_INT64 && type != Type.CUMULATIVE_INT64) {
        return type != Type.GAUGE_DISTRIBUTION && type != Type.CUMULATIVE_DISTRIBUTION
            ? com.google.api.MetricDescriptor.ValueType.UNRECOGNIZED
            : com.google.api.MetricDescriptor.ValueType.DISTRIBUTION;
      } else {
        return com.google.api.MetricDescriptor.ValueType.INT64;
      }
    } else {
      return com.google.api.MetricDescriptor.ValueType.DOUBLE;
    }
  }

  static TimeSeries convertTimeSeries(
      String metricName,
      com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor.Type
          metricType,
      List<LabelKey> labelKeys,
      List<LabelValue> labelValues,
      com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries timeSeries,
      MonitoredResource monitoredResource,
      String domain,
      String projectId) {

    TimeSeries.Builder builder = TimeSeries.newBuilder();
    builder.setMetricKind(createMetricKind(metricType));
    builder.setResource(monitoredResource);
    builder.setValueType(createValueType(metricType));
    builder.setMetric(createMetric(metricName, labelKeys, labelValues, domain));
    com.google.bigtable.veneer.repackaged.io.opencensus.common.Timestamp startTimeStamp =
        timeSeries.getStartTimestamp();
    for (com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Point point :
        timeSeries.getPoints()) {
      builder.addPoints(createPoint(point, startTimeStamp));
    }
    return builder.build();
  }

  @VisibleForTesting
  static com.google.api.Metric createMetric(
      String metricName, List<LabelKey> labelKeys, List<LabelValue> labelValues, String domain) {
    com.google.api.Metric.Builder builder = com.google.api.Metric.newBuilder();
    builder.setType(generateType(metricName, domain));
    Map<String, String> stringTagMap = Maps.newHashMap();

    for (int i = 0; i < labelValues.size(); ++i) {
      String value = ((LabelValue) labelValues.get(i)).getValue();
      if (value != null) {
        stringTagMap.put(((LabelKey) labelKeys.get(i)).getKey(), value);
      }
    }

    builder.putAllLabels(stringTagMap);
    return builder.build();
  }

  @VisibleForTesting
  static com.google.monitoring.v3.Point createPoint(
      com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Point point,
      @Nullable
          com.google.bigtable.veneer.repackaged.io.opencensus.common.Timestamp startTimestamp) {
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
    return (TypedValue)
        value.match(
            typedValueDoubleFunction,
            typedValueLongFunction,
            typedValueDistributionFunction,
            typedValueSummaryFunction,
            Functions.throwIllegalArgumentException());
  }

  @VisibleForTesting
  static com.google.api.Distribution createDistribution(
      com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Distribution
          distribution) {
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
      @Nullable
          com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Distribution
                  .BucketOptions
              bucketOptions) {
    com.google.api.Distribution.BucketOptions.Builder builder = BucketOptions.newBuilder();
    return bucketOptions == null
        ? builder.build()
        : (BucketOptions)
            bucketOptions.match(
                bucketOptionsExplicitFunction, Functions.throwIllegalArgumentException());
  }

  private static void setBucketCountsAndExemplars(
      List<Bucket> buckets, com.google.api.Distribution.Builder builder) {
    builder.addBucketCounts(0L);

    for (Bucket bucket : buckets) {
      builder.addBucketCounts(bucket.getCount());
      com.google.bigtable.veneer.repackaged.io.opencensus.metrics.data.Exemplar exemplar =
          bucket.getExemplar();
      if (exemplar != null) {
        builder.addExemplars(toProtoExemplar(exemplar));
      }
    }
  }

  private static com.google.api.Distribution.Exemplar toProtoExemplar(
      com.google.bigtable.veneer.repackaged.io.opencensus.metrics.data.Exemplar exemplar) {
    com.google.api.Distribution.Exemplar.Builder builder =
        com.google.api.Distribution.Exemplar.newBuilder()
            .setValue(exemplar.getValue())
            .setTimestamp(convertTimestamp(exemplar.getTimestamp()));
    com.google.bigtable.veneer.repackaged.io.opencensus.trace.SpanContext spanContext = null;

    for (Map.Entry<String, AttachmentValue> attachment : exemplar.getAttachments().entrySet()) {
      String key = (String) attachment.getKey();
      AttachmentValue value = (AttachmentValue) attachment.getValue();
      if ("SpanContext".equals(key)) {
        spanContext = ((AttachmentValueSpanContext) value).getSpanContext();
      } else {
        builder.addAttachments(toProtoStringAttachment(value));
      }
    }

    return builder.build();
  }

  private static Any toProtoStringAttachment(AttachmentValue attachmentValue) {
    return Any.newBuilder()
        .setTypeUrl("type.googleapis.com/google.protobuf.StringValue")
        .setValue(ByteString.copyFromUtf8(attachmentValue.getValue()))
        .build();
  }

  private static Any toProtoSpanContextAttachment(
      com.google.monitoring.v3.SpanContext protoSpanContext) {
    return Any.newBuilder()
        .setTypeUrl("type.googleapis.com/google.monitoring.v3.SpanContext")
        .setValue(protoSpanContext.toByteString())
        .build();
  }

  private static com.google.monitoring.v3.SpanContext toProtoSpanContext(
      com.google.bigtable.veneer.repackaged.io.opencensus.trace.SpanContext spanContext,
      String projectId) {
    String spanName =
        String.format(
            "projects/%s/traces/%s/spans/%s",
            projectId,
            spanContext.getTraceId().toLowerBase16(),
            spanContext.getSpanId().toLowerBase16());
    return com.google.monitoring.v3.SpanContext.newBuilder().setSpanName(spanName).build();
  }

  @VisibleForTesting
  static com.google.protobuf.Timestamp convertTimestamp(
      com.google.bigtable.veneer.repackaged.io.opencensus.common.Timestamp censusTimestamp) {
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
        new com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
            Double, TypedValue>() {
          public TypedValue apply(Double arg) {
            com.google.monitoring.v3.TypedValue.Builder builder = TypedValue.newBuilder();
            builder.setDoubleValue(arg);
            return builder.build();
          }
        };
    typedValueLongFunction =
        new com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
            Long, TypedValue>() {
          public TypedValue apply(Long arg) {
            com.google.monitoring.v3.TypedValue.Builder builder = TypedValue.newBuilder();
            builder.setInt64Value(arg);
            return builder.build();
          }
        };
    typedValueDistributionFunction =
        new com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
            com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Distribution,
            TypedValue>() {
          public TypedValue apply(
              com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Distribution arg) {
            com.google.monitoring.v3.TypedValue.Builder builder = TypedValue.newBuilder();
            return builder
                .setDistributionValue(BigtableStackdriverExportUtils.createDistribution(arg))
                .build();
          }
        };
    typedValueSummaryFunction =
        new com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
            Summary, TypedValue>() {
          public TypedValue apply(Summary arg) {
            com.google.monitoring.v3.TypedValue.Builder builder = TypedValue.newBuilder();
            return builder.build();
          }
        };
    bucketOptionsExplicitFunction =
        new Function<ExplicitOptions, BucketOptions>() {
          public BucketOptions apply(ExplicitOptions arg) {
            com.google.api.Distribution.BucketOptions.Builder builder = BucketOptions.newBuilder();
            com.google.api.Distribution.BucketOptions.Explicit.Builder explicitBuilder =
                Explicit.newBuilder();
            explicitBuilder.addBounds(0.0D);
            explicitBuilder.addAllBounds(arg.getBucketBoundaries());
            builder.setExplicitBuckets(explicitBuilder.build());
            return builder.build();
          }
        };
  }
}
