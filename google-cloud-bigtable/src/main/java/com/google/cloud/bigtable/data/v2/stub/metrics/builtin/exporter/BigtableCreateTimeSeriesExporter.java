package com.google.cloud.bigtable.data.v2.stub.metrics.builtin.exporter;

import com.google.api.MonitoredResource;
import com.google.api.gax.rpc.ApiException;
import com.google.bigtable.veneer.repackaged.io.opencensus.exporter.metrics.util.MetricExporter;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Metric;
import com.google.bigtable.veneer.repackaged.io.opencensus.trace.Span;
import com.google.bigtable.veneer.repackaged.io.opencensus.trace.Status;
import com.google.bigtable.veneer.repackaged.io.opencensus.trace.Tracer;
import com.google.bigtable.veneer.repackaged.io.opencensus.trace.Tracing;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.common.collect.Lists;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeSeries;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

final class BigtableCreateTimeSeriesExporter extends MetricExporter {
  private static final Tracer tracer = Tracing.getTracer();
  private static final Logger logger =
      Logger.getLogger(BigtableCreateTimeSeriesExporter.class.getName());
  private final ProjectName projectName;
  private final MetricServiceClient metricServiceClient;
  private final MonitoredResource monitoredResource;
  private final String domain;
  private final Map<LabelKey, LabelValue> constantLabels;

  BigtableCreateTimeSeriesExporter(
      String projectId,
      MetricServiceClient metricServiceClient,
      MonitoredResource monitoredResource,
      @Nullable String metricNamePrefix,
      Map<LabelKey, LabelValue> constantLabels) {
    this.projectName = ProjectName.newBuilder().setProject(projectId).build();
    this.metricServiceClient = metricServiceClient;
    this.monitoredResource = monitoredResource;
    this.domain = BigtableStackdriverExportUtils.getDomain(metricNamePrefix);
    this.constantLabels = constantLabels;
  }

  public void export(Collection<Metric> metrics) {
    List<TimeSeries> timeSeriesList = new ArrayList(metrics.size());

    for (Metric metric : metrics) {
      timeSeriesList.addAll(
          BigtableStackdriverExportUtils.createTimeSeriesList(
              metric,
              this.monitoredResource,
              this.domain,
              this.projectName.getProject(),
              this.constantLabels));
    }

    Span span = tracer.getCurrentSpan();
    for (List<TimeSeries> batchedTimeSeries :
            Lists.partition(timeSeriesList, BigtableStackdriverExportUtils.MAX_BATCH_EXPORT_SIZE)) {
      span.addAnnotation("Export Stackdriver TimeSeries.");

      try {
        CreateTimeSeriesRequest request =
            CreateTimeSeriesRequest.newBuilder()
                .setName(this.projectName.toString())
                .addAllTimeSeries(batchedTimeSeries)
                .build();
        this.metricServiceClient.createTimeSeries(request);
        span.addAnnotation("Finish exporting TimeSeries.");
      } catch (ApiException var7) {
        logger.log(Level.WARNING, "ApiException thrown when exporting TimeSeries.", var7);
        span.setStatus(
            Status.CanonicalCode.valueOf(var7.getStatusCode().getCode().name())
                .toStatus()
                .withDescription(
                    "ApiException thrown when exporting TimeSeries: "
                        + BigtableStackdriverExportUtils.exceptionMessage(var7)));
      } catch (Throwable var8) {
        logger.log(Level.WARNING, "Exception thrown when exporting TimeSeries.", var8);
        span.setStatus(
            Status.UNKNOWN.withDescription(
                "Exception thrown when exporting TimeSeries: "
                    + BigtableStackdriverExportUtils.exceptionMessage(var8)));
      }
    }
  }
}
