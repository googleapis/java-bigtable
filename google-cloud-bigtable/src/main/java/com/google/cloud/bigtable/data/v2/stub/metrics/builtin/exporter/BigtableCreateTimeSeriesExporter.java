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
import com.google.cloud.bigtable.data.v2.stub.metrics.builtin.BuiltinMeasureConstants;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.common.collect.Lists;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeSeries;
import java.util.ArrayList;
import java.util.Collection;
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
    String clueterLabel = BuiltinMeasureConstants.CLUSTER.getName();
    String zoneLabel = BuiltinMeasureConstants.ZONE.getName();
    String tableLabel = BuiltinMeasureConstants.TABLE.getName();

    for (Metric metric : metrics) {
      List<LabelKey> keys = metric.getMetricDescriptor().getLabelKeys();
      int clusterIndex = -1, zoneIndex = -1, tableIndex = -1;
      for (int i = 0; i < keys.size(); i++) {
        String key = keys.get(i).getKey();
        if (key.equals(clueterLabel)) {
          clusterIndex = i;
        } else if (key.equals(zoneLabel)) {
          zoneIndex = i;
        } else if (key.equals(tableLabel)) {
          tableIndex = i;
        }
      }

      for (com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries
          timeSeries : metric.getTimeSeriesList()) {
        MonitoredResource.Builder timeSeriesResourceBuilder = this.monitoredResource.toBuilder();
        List<LabelValue> labelValues = timeSeries.getLabelValues();
        if (clusterIndex != -1) {
          timeSeriesResourceBuilder.putLabels(
              clueterLabel, labelValues.get(clusterIndex).getValue());
        }
        if (zoneIndex != -1) {
          timeSeriesResourceBuilder.putLabels(zoneLabel, labelValues.get(zoneIndex).getValue());
        }
        if (tableIndex != -1) {
          timeSeriesResourceBuilder.putLabels(tableLabel, labelValues.get(tableIndex).getValue());
        }
        timeSeriesList.add(
            BigtableStackdriverExportUtils.convertTimeSeries(
                metric,
                timeSeries,
                timeSeriesResourceBuilder.build(),
                this.domain,
                this.projectName.getProject(),
                this.constantLabels));
      }
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
      } catch (ApiException e) {
        logger.log(Level.WARNING, "ApiException thrown when exporting TimeSeries.", e);
        span.setStatus(
            Status.CanonicalCode.valueOf(e.getStatusCode().getCode().name())
                .toStatus()
                .withDescription(
                    "ApiException thrown when exporting TimeSeries: "
                        + BigtableStackdriverExportUtils.exceptionMessage(e)));
      } catch (Throwable e) {
        logger.log(Level.WARNING, "Exception thrown when exporting TimeSeries.", e);
        span.setStatus(
            Status.UNKNOWN.withDescription(
                "Exception thrown when exporting TimeSeries: "
                    + BigtableStackdriverExportUtils.exceptionMessage(e)));
      }
    }
  }
}
