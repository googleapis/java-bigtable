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

import com.google.api.MonitoredResource;
import com.google.api.gax.rpc.ApiException;
import com.google.bigtable.veneer.repackaged.io.opencensus.exporter.metrics.util.MetricExporter;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Metric;
import com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMeasureConstants;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeSeries;
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
  private final String domain;

  BigtableCreateTimeSeriesExporter(
      String projectId,
      MetricServiceClient metricServiceClient,
      MonitoredResource monitoredResource) {
    this.projectName = ProjectName.newBuilder().setProject(projectId).build();
    this.metricServiceClient = metricServiceClient;
    this.monitoredResource = monitoredResource;
    this.domain = "bigtable.googleapis.com/client/";
  }

  public void export(Collection<Metric> metrics) {
    List<TimeSeries> timeSeriesList = new ArrayList(metrics.size());

    for (Metric metric : metrics) {
      // only export bigtable metrics
      if (!metric.getMetricDescriptor().getName().contains("bigtable")) {
        continue;
      }

      for (com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries
          timeSeries : metric.getTimeSeriesList()) {
        MonitoredResource.Builder monitoredResourceBuilder = this.monitoredResource.toBuilder();

        List<LabelKey> keys = metric.getMetricDescriptor().getLabelKeys();
        List<LabelValue> labelValues = timeSeries.getLabelValues();

        List<LabelKey> updatedKeys = new ArrayList<>();
        List<LabelValue> updatedValues = new ArrayList<>();

        for (int i = 0; i < labelValues.size(); i++) {
          if (keys.get(i).getKey().equals(BuiltinMeasureConstants.PROJECT_ID.getName())) {
            monitoredResourceBuilder.putLabels(
                BuiltinMeasureConstants.PROJECT_ID.getName(), labelValues.get(i).getValue());
          } else if (keys.get(i).getKey().equals(BuiltinMeasureConstants.INSTANCE_ID.getName())) {
            monitoredResourceBuilder.putLabels(
                BuiltinMeasureConstants.INSTANCE_ID.getName(), labelValues.get(i).getValue());
          } else if (keys.get(i).getKey().equals(BuiltinMeasureConstants.CLUSTER.getName())) {
            monitoredResourceBuilder.putLabels(
                BuiltinMeasureConstants.CLUSTER.getName(), labelValues.get(i).getValue());
          } else if (keys.get(i).getKey().equals(BuiltinMeasureConstants.ZONE.getName())) {
            monitoredResourceBuilder.putLabels(
                BuiltinMeasureConstants.ZONE.getName(), labelValues.get(i).getValue());
          } else if (keys.get(i).getKey().equals(BuiltinMeasureConstants.TABLE.getName())) {
            monitoredResourceBuilder.putLabels(
                BuiltinMeasureConstants.TABLE.getName(), labelValues.get(i).getValue());
          } else {
            updatedKeys.add(keys.get(i));
            updatedValues.add(labelValues.get(i));
          }
        }

        updatedKeys.add(LabelKey.create(BuiltinMeasureConstants.CLIENT_ID.getName(), "client id"));
        updatedValues.add(
            LabelValue.create(BigtableStackdriverExportUtils.generateDefaultTaskValue()));

        timeSeriesList.add(
            BigtableStackdriverExportUtils.convertTimeSeries(
                metric.getMetricDescriptor().getName(),
                metric.getMetricDescriptor().getType(),
                updatedKeys,
                updatedValues,
                timeSeries,
                monitoredResourceBuilder.build(),
                this.domain,
                this.projectName.getProject()));
      }
    }

    try {
      CreateTimeSeriesRequest request =
          CreateTimeSeriesRequest.newBuilder()
              .setName(this.projectName.toString())
              .addAllTimeSeries(timeSeriesList)
              .build();

      this.metricServiceClient.createServiceTimeSeries(request);
    } catch (ApiException e) {
      logger.log(Level.WARNING, "ApiException thrown when exporting TimeSeries.", e);
    } catch (Throwable e) {
      logger.log(Level.WARNING, "Exception thrown when exporting TimeSeries.", e);
    }
  }
}
