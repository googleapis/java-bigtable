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
import com.google.api.LabelDescriptor;
import com.google.api.LabelDescriptor.ValueType;
import com.google.api.MetricDescriptor;
import com.google.api.MetricDescriptor.MetricKind;
import com.google.api.MonitoredResource;
import com.google.bigtable.veneer.repackaged.io.opencensus.common.Function;
import com.google.bigtable.veneer.repackaged.io.opencensus.common.Functions;
import com.google.bigtable.veneer.repackaged.io.opencensus.contrib.exemplar.util.AttachmentValueSpanContext;
import com.google.bigtable.veneer.repackaged.io.opencensus.contrib.resource.util.ResourceUtils;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.data.AttachmentValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Distribution.Bucket;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Distribution.BucketOptions.ExplicitOptions;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor.Type;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Summary;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Value;
import com.google.bigtable.veneer.repackaged.io.opencensus.resource.Resource;
import com.google.cloud.MetadataConfig;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

class BigtableStackdriverExportUtils {
  @VisibleForTesting
  static final LabelKey OPENCENSUS_TASK_KEY =
      LabelKey.create("opencensus_task", "Opencensus task identifier");

  @VisibleForTesting
  static final LabelValue OPENCENSUS_TASK_VALUE_DEFAULT =
      LabelValue.create(generateDefaultTaskValue());

  static final Map<LabelKey, LabelValue> DEFAULT_CONSTANT_LABELS;
  @VisibleForTesting static final String STACKDRIVER_PROJECT_ID_KEY = "project_id";
  @VisibleForTesting static final String DEFAULT_DISPLAY_NAME_PREFIX = "OpenCensus/";
  @VisibleForTesting static final String CUSTOM_METRIC_DOMAIN = "custom.googleapis.com/";

  @VisibleForTesting
  static final String CUSTOM_OPENCENSUS_DOMAIN = CUSTOM_METRIC_DOMAIN + "opencensus/";

  // TODO: clean up unused other types
  @VisibleForTesting static final int MAX_BATCH_EXPORT_SIZE = 200;
  private static final String K8S_CONTAINER = "k8s_container";
  private static final String GCP_GCE_INSTANCE = "gce_instance";
  private static final String AWS_EC2_INSTANCE = "aws_ec2_instance";
  private static final String GLOBAL = "global";
  @VisibleForTesting static final String AWS_REGION_VALUE_PREFIX = "aws:";
  private static final Logger logger;
  private static final Map<String, String> GCP_RESOURCE_MAPPING;
  private static final Map<String, String> K8S_RESOURCE_MAPPING;
  private static final Map<String, String> AWS_RESOURCE_MAPPING;
  @VisibleForTesting static final LabelKey PERCENTILE_LABEL_KEY;

  @VisibleForTesting
  static final String SNAPSHOT_SUFFIX_PERCENTILE = "_summary_snapshot_percentile";

  @VisibleForTesting static final String SUMMARY_SUFFIX_COUNT = "_summary_count";
  @VisibleForTesting static final String SUMMARY_SUFFIX_SUM = "_summary_sum";
  @Nullable private static volatile String cachedProjectIdForExemplar;

  @VisibleForTesting
  static final String EXEMPLAR_ATTACHMENT_TYPE_STRING =
      "type.googleapis.com/google.protobuf.StringValue";

  @VisibleForTesting
  static final String EXEMPLAR_ATTACHMENT_TYPE_SPAN_CONTEXT =
      "type.googleapis.com/google.monitoring.v3.SpanContext";

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

  static MetricDescriptor createMetricDescriptor(
      com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor
          metricDescriptor,
      String projectId,
      String domain,
      String displayNamePrefix,
      Map<LabelKey, LabelValue> constantLabels) {
    MetricDescriptor.Builder builder = MetricDescriptor.newBuilder();
    String type = generateType(metricDescriptor.getName(), domain);
    builder.setName("projects/" + projectId + "/metricDescriptors/" + type);
    builder.setType(type);
    builder.setDescription(metricDescriptor.getDescription());
    builder.setDisplayName(createDisplayName(metricDescriptor.getName(), displayNamePrefix));
    for (LabelKey labelKey : metricDescriptor.getLabelKeys()) {
      builder.addLabels(createLabelDescriptor(labelKey));
    }
    for (LabelKey labelKey : constantLabels.keySet()) {
      builder.addLabels(createLabelDescriptor(labelKey));
    }
    builder.setUnit(metricDescriptor.getUnit());
    builder.setMetricKind(createMetricKind(metricDescriptor.getType()));
    builder.setValueType(createValueType(metricDescriptor.getType()));
    return builder.build();
  }

  private static String generateType(String metricName, String domain) {
    return domain + metricName;
  }

  private static String createDisplayName(String metricName, String displayNamePrefix) {
    return displayNamePrefix + metricName;
  }

  @VisibleForTesting
  static LabelDescriptor createLabelDescriptor(LabelKey labelKey) {
    com.google.api.LabelDescriptor.Builder builder = LabelDescriptor.newBuilder();
    builder.setKey(labelKey.getKey());
    builder.setDescription(labelKey.getDescription());
    builder.setValueType(ValueType.STRING);
    return builder.build();
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
    if (!projectId.equals(cachedProjectIdForExemplar)) {
      cachedProjectIdForExemplar = projectId;
    }

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

    if (spanContext != null && cachedProjectIdForExemplar != null) {
      com.google.monitoring.v3.SpanContext protoSpanContext =
          toProtoSpanContext(spanContext, cachedProjectIdForExemplar);
      builder.addAttachments(toProtoSpanContextAttachment(protoSpanContext));
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
  static void setCachedProjectIdForExemplar(@Nullable String projectId) {
    cachedProjectIdForExemplar = projectId;
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

  static MonitoredResource getDefaultResource() {
    com.google.api.MonitoredResource.Builder builder = MonitoredResource.newBuilder();
    if (MetadataConfig.getProjectId() != null) {
      builder.putLabels(STACKDRIVER_PROJECT_ID_KEY, MetadataConfig.getProjectId());
    }

    Resource autoDetectedResource = ResourceUtils.detectResource();
    if (autoDetectedResource != null && autoDetectedResource.getType() != null) {
      setResourceForBuilder(builder, autoDetectedResource);
      return builder.build();
    } else {
      builder.setType("global");
      return builder.build();
    }
  }

  @VisibleForTesting
  static void setResourceForBuilder(
      com.google.api.MonitoredResource.Builder builder, Resource autoDetectedResource) {
    String type = autoDetectedResource.getType();
    if (type != null) {
      String sdType = GLOBAL;
      Map<String, String> mappings = null;
      if ("host".equals(type)) {
        String provider = (String) autoDetectedResource.getLabels().get("cloud.provider");
        if ("gcp".equals(provider)) {
          sdType = GCP_GCE_INSTANCE;
          mappings = GCP_RESOURCE_MAPPING;
        } else if ("aws".equals(provider)) {
          sdType = AWS_EC2_INSTANCE;
          mappings = AWS_RESOURCE_MAPPING;
        }
      } else if ("container".equals(type)) {
        sdType = K8S_CONTAINER;
        mappings = K8S_RESOURCE_MAPPING;
      }

      builder.setType(sdType);
      if (!GLOBAL.equals(sdType) && mappings != null) {
        Map<String, String> resLabels = autoDetectedResource.getLabels();

        for (Map.Entry<String, String> entry : mappings.entrySet()) {
          if (entry.getValue() != null && resLabels.containsKey(entry.getValue())) {
            String resourceLabelKey = (String) entry.getKey();
            String resourceLabelValue = (String) resLabels.get(entry.getValue());
            if (AWS_EC2_INSTANCE.equals(sdType) && "region".equals(resourceLabelKey)) {
              resourceLabelValue = AWS_REGION_VALUE_PREFIX + resourceLabelValue;
            }

            builder.putLabels(resourceLabelKey, resourceLabelValue);
          }
        }
      }
    }
  }

  @VisibleForTesting
  static List<com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Metric>
      convertSummaryMetric(
          com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Metric summaryMetric) {
    List<com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Metric> metricsList =
        Lists.newArrayList();
    final List<com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries>
        percentileTimeSeries = new ArrayList();
    final List<com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries>
        summaryCountTimeSeries = new ArrayList();
    final List<com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries>
        summarySumTimeSeries = new ArrayList();

    for (final com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries
        timeSeries : summaryMetric.getTimeSeriesList()) {
      final List<LabelValue> labelValuesWithPercentile = new ArrayList(timeSeries.getLabelValues());
      final com.google.bigtable.veneer.repackaged.io.opencensus.common.Timestamp
          timeSeriesTimestamp = timeSeries.getStartTimestamp();

      for (com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Point point :
          timeSeries.getPoints()) {
        final com.google.bigtable.veneer.repackaged.io.opencensus.common.Timestamp pointTimestamp =
            point.getTimestamp();
        point
            .getValue()
            .match(
                Functions.returnNull(),
                Functions.returnNull(),
                Functions.returnNull(),
                new com.google.bigtable.veneer.repackaged.io.opencensus.common.Function<
                    Summary, Void>() {
                  public Void apply(Summary summary) {
                    Long count = summary.getCount();
                    if (count != null) {
                      BigtableStackdriverExportUtils.createTimeSeries(
                          timeSeries.getLabelValues(),
                          Value.longValue(count),
                          pointTimestamp,
                          timeSeriesTimestamp,
                          summaryCountTimeSeries);
                    }

                    Double sum = summary.getSum();
                    if (sum != null) {
                      BigtableStackdriverExportUtils.createTimeSeries(
                          timeSeries.getLabelValues(),
                          Value.doubleValue(sum),
                          pointTimestamp,
                          timeSeriesTimestamp,
                          summarySumTimeSeries);
                    }

                    Summary.Snapshot snapshot = summary.getSnapshot();
                    for (Summary.Snapshot.ValueAtPercentile valueAtPercentile :
                        snapshot.getValueAtPercentiles()) {
                      labelValuesWithPercentile.add(
                          LabelValue.create(valueAtPercentile.getPercentile() + ""));
                      BigtableStackdriverExportUtils.createTimeSeries(
                          labelValuesWithPercentile,
                          Value.doubleValue(valueAtPercentile.getValue()),
                          pointTimestamp,
                          (com.google.bigtable.veneer.repackaged.io.opencensus.common.Timestamp)
                              null,
                          percentileTimeSeries);
                      labelValuesWithPercentile.remove(labelValuesWithPercentile.size() - 1);
                    }

                    return null;
                  }
                },
                Functions.returnNull());
      }
    }

    if (summaryCountTimeSeries.size() > 0) {
      addMetric(
          metricsList,
          com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor
              .create(
                  summaryMetric.getMetricDescriptor().getName() + "_summary_count",
                  summaryMetric.getMetricDescriptor().getDescription(),
                  "1",
                  Type.CUMULATIVE_INT64,
                  summaryMetric.getMetricDescriptor().getLabelKeys()),
          summaryCountTimeSeries);
    }

    if (summarySumTimeSeries.size() > 0) {
      addMetric(
          metricsList,
          com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor
              .create(
                  summaryMetric.getMetricDescriptor().getName() + "_summary_sum",
                  summaryMetric.getMetricDescriptor().getDescription(),
                  summaryMetric.getMetricDescriptor().getUnit(),
                  Type.CUMULATIVE_DOUBLE,
                  summaryMetric.getMetricDescriptor().getLabelKeys()),
          summarySumTimeSeries);
    }

    List<LabelKey> labelKeys = new ArrayList(summaryMetric.getMetricDescriptor().getLabelKeys());
    labelKeys.add(PERCENTILE_LABEL_KEY);
    addMetric(
        metricsList,
        com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor.create(
            summaryMetric.getMetricDescriptor().getName() + "_summary_snapshot_percentile",
            summaryMetric.getMetricDescriptor().getDescription(),
            summaryMetric.getMetricDescriptor().getUnit(),
            Type.GAUGE_DOUBLE,
            labelKeys),
        percentileTimeSeries);
    return metricsList;
  }

  private static void addMetric(
      List<com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Metric> metricsList,
      com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor
          metricDescriptor,
      List<com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries>
          timeSeriesList) {
    metricsList.add(
        com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Metric.create(
            metricDescriptor, timeSeriesList));
  }

  private static void createTimeSeries(
      List<LabelValue> labelValues,
      Value value,
      com.google.bigtable.veneer.repackaged.io.opencensus.common.Timestamp pointTimestamp,
      @Nullable
          com.google.bigtable.veneer.repackaged.io.opencensus.common.Timestamp timeSeriesTimestamp,
      List<com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries>
          timeSeriesList) {
    timeSeriesList.add(
        com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries
            .createWithOnePoint(
                labelValues,
                com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Point.create(
                    value, pointTimestamp),
                timeSeriesTimestamp));
  }

  private static Map<String, String> getGcpResourceLabelsMappings() {
    Map<String, String> resourceLabels = new LinkedHashMap();
    resourceLabels.put("project_id", "project_id");
    resourceLabels.put("instance_id", "host.id");
    resourceLabels.put("zone", "cloud.zone");
    return Collections.unmodifiableMap(resourceLabels);
  }

  private static Map<String, String> getK8sResourceLabelsMappings() {
    Map<String, String> resourceLabels = new LinkedHashMap();
    resourceLabels.put("project_id", "project_id");
    resourceLabels.put("location", "cloud.zone");
    resourceLabels.put("cluster_name", "k8s.cluster.name");
    resourceLabels.put("namespace_name", "k8s.namespace.name");
    resourceLabels.put("pod_name", "k8s.pod.name");
    resourceLabels.put("container_name", "container.name");
    return Collections.unmodifiableMap(resourceLabels);
  }

  private static Map<String, String> getAwsResourceLabelsMappings() {
    Map<String, String> resourceLabels = new LinkedHashMap();
    resourceLabels.put("project_id", "project_id");
    resourceLabels.put("instance_id", "host.id");
    resourceLabels.put("region", "cloud.region");
    resourceLabels.put("aws_account", "cloud.account.id");
    return Collections.unmodifiableMap(resourceLabels);
  }

  private BigtableStackdriverExportUtils() {}

  static String exceptionMessage(Throwable e) {
    return e.getMessage() != null ? e.getMessage() : e.getClass().getName();
  }

  static String getDomain(@Nullable String metricNamePrefix) {
    String domain;
    if (Strings.isNullOrEmpty(metricNamePrefix)) {
      domain = CUSTOM_OPENCENSUS_DOMAIN;
    } else if (!metricNamePrefix.endsWith("/")) {
      domain = metricNamePrefix + '/';
    } else {
      domain = metricNamePrefix;
    }

    return domain;
  }

  static String getDisplayNamePrefix(@Nullable String metricNamePrefix) {
    if (metricNamePrefix == null) {
      return DEFAULT_DISPLAY_NAME_PREFIX;
    } else {
      if (!metricNamePrefix.endsWith("/") && !metricNamePrefix.isEmpty()) {
        metricNamePrefix = metricNamePrefix + '/';
      }

      return metricNamePrefix;
    }
  }

  static {
    DEFAULT_CONSTANT_LABELS =
        Collections.singletonMap(OPENCENSUS_TASK_KEY, OPENCENSUS_TASK_VALUE_DEFAULT);
    logger = Logger.getLogger(BigtableStackdriverExportUtils.class.getName());
    GCP_RESOURCE_MAPPING = getGcpResourceLabelsMappings();
    K8S_RESOURCE_MAPPING = getK8sResourceLabelsMappings();
    AWS_RESOURCE_MAPPING = getAwsResourceLabelsMappings();
    PERCENTILE_LABEL_KEY =
        LabelKey.create("percentile", "the value at a given percentile of a distribution");
    cachedProjectIdForExemplar = null;
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
