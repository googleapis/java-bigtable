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

import com.google.api.MonitoredResource;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeSeries;
import io.opencensus.exporter.metrics.util.MetricExporter;
import io.opencensus.metrics.export.Metric;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

final class BigtableCreateTimeSeriesExporter extends MetricExporter {
  private static final Logger logger =
      Logger.getLogger(BigtableCreateTimeSeriesExporter.class.getName());

  private final ProjectName projectName;
  private final MetricServiceClient metricServiceClient;
  private final MonitoredResource monitoredResource;
  private final String clientId;

  BigtableCreateTimeSeriesExporter(
      String projectId,
      MetricServiceClient metricServiceClient,
      MonitoredResource monitoredResource) {
    this.projectName = ProjectName.newBuilder().setProject(projectId).build();
    this.metricServiceClient = metricServiceClient;
    this.monitoredResource = monitoredResource;
    this.clientId = BigtableStackdriverExportUtils.getDefaultTaskValue();
  }

  public void export(Collection<Metric> metrics) {
    List<TimeSeries> timeSeriesList = new ArrayList(metrics.size());

    for (Metric metric : metrics) {
      // only export bigtable metrics
      if (!metric.getMetricDescriptor().getName().contains("bigtable")) {
        continue;
      }

      for (io.opencensus.metrics.export.TimeSeries timeSeries : metric.getTimeSeriesList()) {
        timeSeriesList.add(
            BigtableStackdriverExportUtils.convertTimeSeries(
                metric.getMetricDescriptor().getName(),
                metric.getMetricDescriptor().getType(),
                metric.getMetricDescriptor().getLabelKeys(),
                timeSeries,
                clientId,
                monitoredResource));
      }
    }

    try {
      CreateTimeSeriesRequest request =
          CreateTimeSeriesRequest.newBuilder()
              .setName(this.projectName.toString())
              .addAllTimeSeries(timeSeriesList)
              .build();

      this.metricServiceClient.createServiceTimeSeries(request);
    } catch (Throwable e) {
      logger.log(Level.WARNING, "Exception thrown when exporting TimeSeries.", e);
    }
  }
}
