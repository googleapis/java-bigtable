package com.google.cloud.bigtable.data.v2.stub.metrics.builtin.exporter;

import com.google.api.gax.rpc.ApiException;
import com.google.bigtable.veneer.repackaged.io.opencensus.exporter.metrics.util.MetricExporter;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Metric;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor;
import com.google.bigtable.veneer.repackaged.io.opencensus.trace.Span;
import com.google.bigtable.veneer.repackaged.io.opencensus.trace.Status;
import com.google.bigtable.veneer.repackaged.io.opencensus.trace.Tracer;
import com.google.bigtable.veneer.repackaged.io.opencensus.trace.Tracing;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.common.collect.ImmutableSet;
import com.google.monitoring.v3.CreateMetricDescriptorRequest;
import com.google.monitoring.v3.ProjectName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

final class BigtableCreateMetricDescriptorExporter extends MetricExporter {
  private static final Tracer tracer = Tracing.getTracer();
  private static final Logger logger =
      Logger.getLogger(BigtableCreateMetricDescriptorExporter.class.getName());
  private static final ImmutableSet<String> SUPPORTED_EXTERNAL_DOMAINS =
      ImmutableSet.of("custom.googleapis.com", "external.googleapis.com");
  private static final String GOOGLE_APIS_DOMAIN_SUFFIX = "googleapis.com";
  private final String projectId;
  private final ProjectName projectName;
  private final MetricServiceClient metricServiceClient;
  private final String domain;
  private final String displayNamePrefix;
  private final Map<String, MetricDescriptor> registeredMetricDescriptors = new LinkedHashMap();
  private final Map<LabelKey, LabelValue> constantLabels;
  private final MetricExporter nextExporter;

  BigtableCreateMetricDescriptorExporter(
      String projectId,
      MetricServiceClient metricServiceClient,
      @Nullable String metricNamePrefix,
      @Nullable String displayNamePrefix,
      Map<LabelKey, LabelValue> constantLabels,
      MetricExporter nextExporter) {
    this.projectId = projectId;
    this.projectName = ProjectName.newBuilder().setProject(projectId).build();
    this.metricServiceClient = metricServiceClient;
    this.domain = BigtableStackdriverExportUtils.getDomain(metricNamePrefix);
    this.displayNamePrefix =
        BigtableStackdriverExportUtils.getDisplayNamePrefix(
            displayNamePrefix == null ? metricNamePrefix : displayNamePrefix);
    this.constantLabels = constantLabels;
    this.nextExporter = nextExporter;
  }

  private boolean registerMetricDescriptor(MetricDescriptor metricDescriptor) {
    String metricName = metricDescriptor.getName();
    MetricDescriptor existingMetricDescriptor =
        (MetricDescriptor) this.registeredMetricDescriptors.get(metricName);
    if (existingMetricDescriptor != null) {
      if (existingMetricDescriptor.equals(metricDescriptor)) {
        return true;
      } else {
        logger.log(
            Level.WARNING,
            "A different metric with the same name is already registered: "
                + existingMetricDescriptor);
        return false;
      }
    } else {
      this.registeredMetricDescriptors.put(metricName, metricDescriptor);
      if (isBuiltInMetric(metricName)) {
        return true;
      } else {
        Span span = tracer.getCurrentSpan();
        span.addAnnotation("Create Stackdriver Metric.");
        com.google.api.MetricDescriptor stackDriverMetricDescriptor =
            BigtableStackdriverExportUtils.createMetricDescriptor(
                metricDescriptor,
                this.projectId,
                this.domain,
                this.displayNamePrefix,
                this.constantLabels);
        CreateMetricDescriptorRequest request =
            CreateMetricDescriptorRequest.newBuilder()
                .setName(this.projectName.toString())
                .setMetricDescriptor(stackDriverMetricDescriptor)
                .build();

        try {
          this.metricServiceClient.createMetricDescriptor(request);
          span.addAnnotation("Finish creating MetricDescriptor.");
          return true;
        } catch (ApiException var8) {
          logger.log(Level.WARNING, "ApiException thrown when creating MetricDescriptor.", var8);
          span.setStatus(
              Status.CanonicalCode.valueOf(var8.getStatusCode().getCode().name())
                  .toStatus()
                  .withDescription(
                      "ApiException thrown when creating MetricDescriptor: "
                          + BigtableStackdriverExportUtils.exceptionMessage(var8)));
          return false;
        } catch (Throwable var9) {
          logger.log(Level.WARNING, "Exception thrown when creating MetricDescriptor.", var9);
          span.setStatus(
              Status.UNKNOWN.withDescription(
                  "Exception thrown when creating MetricDescriptor: "
                      + BigtableStackdriverExportUtils.exceptionMessage(var9)));
          return false;
        }
      }
    }
  }

  public void export(Collection<Metric> metrics) {
    ArrayList<Metric> registeredMetrics = new ArrayList(metrics.size());

    for (Metric metric : metrics) {
        MetricDescriptor metricDescriptor = metric.getMetricDescriptor();
        if (metricDescriptor.getType() == MetricDescriptor.Type.SUMMARY) {
          List<Metric> convertedMetrics =
              BigtableStackdriverExportUtils.convertSummaryMetric(metric);
          registeredMetrics.ensureCapacity(registeredMetrics.size() + convertedMetrics.size());
          
          for (Metric convertedMetric : convertedMetrics) {
            if (this.registerMetricDescriptor(convertedMetric.getMetricDescriptor())) {
              registeredMetrics.add(convertedMetric);
            }
          }
        } else if (this.registerMetricDescriptor(metricDescriptor)) {
          registeredMetrics.add(metric);
        }
      }

      this.nextExporter.export(registeredMetrics);
      return;
  }

  private static boolean isBuiltInMetric(String metricName) {
    int domainIndex = metricName.indexOf(47);
    if (domainIndex < 0) {
      return false;
    } else {
      String metricDomain = metricName.substring(0, domainIndex);
      if (!metricDomain.endsWith("googleapis.com")) {
        return false;
      } else {
        return !SUPPORTED_EXTERNAL_DOMAINS.contains(metricDomain);
      }
    }
  }
}
