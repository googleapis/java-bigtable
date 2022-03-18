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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMeasureConstants.APP_PROFILE;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMeasureConstants.CLIENT_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMeasureConstants.CLUSTER;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMeasureConstants.INSTANCE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMeasureConstants.PROJECT_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMeasureConstants.TABLE;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMeasureConstants.ZONE;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.api.MonitoredResource;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.veneer.repackaged.io.opencensus.common.Timestamp;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.LabelValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Metric;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.MetricDescriptor;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Point;
import com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.Value;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.stub.MetricServiceStub;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.protobuf.Empty;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BigtableCreateTimeSeriesExporterTest {

  private static final String projectId = "fake-project";
  private static final String instanceId = "fake-instance";
  private static final String appProfileId = "default";
  private static final String tableId = "fake-table";
  private static final String zone = "us-east-1";
  private static final String cluster = "cluster-1";
  private static final String METRIC_PREFIX = "bigtable.googleapis.com/client/";

  @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock private MetricServiceStub mockMetricServiceStub;
  private MetricServiceClient fakeMetricServiceClient;
  private BigtableCreateTimeSeriesExporter exporter;

  @Before
  public void setUp() {

    fakeMetricServiceClient = new FakeMetricServiceClient(mockMetricServiceStub);

    exporter =
        new BigtableCreateTimeSeriesExporter(
            projectId,
            fakeMetricServiceClient,
            MonitoredResource.newBuilder().setType("bigtable-table").build(),
            METRIC_PREFIX);
  }

  @After
  public void tearDown() {}

  @Test
  public void testTimeSeries() throws Exception {
    ArgumentCaptor<CreateTimeSeriesRequest> argumentCaptor =
        ArgumentCaptor.forClass(CreateTimeSeriesRequest.class);

    UnaryCallable<CreateTimeSeriesRequest, Empty> mockCallable = mock(UnaryCallable.class);
    when(mockMetricServiceStub.createServiceTimeSeriesCallable()).thenReturn(mockCallable);
    when(mockCallable.call(argumentCaptor.capture())).thenReturn(Empty.getDefaultInstance());

    double fakeValue = 10.0;
    Metric fakeMetric =
        Metric.create(
            MetricDescriptor.create(
                "bigtable/test",
                "descritpion",
                "ms",
                MetricDescriptor.Type.CUMULATIVE_DOUBLE,
                Arrays.asList(
                    LabelKey.create(PROJECT_ID.getName(), ""),
                    LabelKey.create(INSTANCE_ID.getName(), ""),
                    LabelKey.create(TABLE.getName(), ""),
                    LabelKey.create(CLUSTER.getName(), ""),
                    LabelKey.create(ZONE.getName(), ""),
                    LabelKey.create(APP_PROFILE.getName(), ""))),
            Arrays.asList(
                com.google.bigtable.veneer.repackaged.io.opencensus.metrics.export.TimeSeries
                    .create(
                        Arrays.asList(
                            LabelValue.create(projectId),
                            LabelValue.create(instanceId),
                            LabelValue.create(tableId),
                            LabelValue.create(cluster),
                            LabelValue.create(zone),
                            LabelValue.create(appProfileId)),
                        Arrays.asList(
                            Point.create(
                                Value.doubleValue(fakeValue),
                                Timestamp.fromMillis(System.currentTimeMillis()))),
                        Timestamp.fromMillis(System.currentTimeMillis()))));

    exporter.export(Arrays.asList(fakeMetric));

    CreateTimeSeriesRequest request = argumentCaptor.getValue();

    assertThat(request.getTimeSeriesList()).hasSize(1);

    com.google.monitoring.v3.TimeSeries timeSeries = request.getTimeSeriesList().get(0);

    assertThat(timeSeries.getResource().getLabelsMap())
        .containsExactly(
            PROJECT_ID.getName(), projectId,
            INSTANCE_ID.getName(), instanceId,
            TABLE.getName(), tableId,
            CLUSTER.getName(), cluster,
            ZONE.getName(), zone);

    assertThat(timeSeries.getMetric().getLabelsMap()).hasSize(2);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(APP_PROFILE.getName(), appProfileId);
    assertThat(timeSeries.getMetric().getLabelsMap()).containsKey(CLIENT_ID.getName());

    assertThat(timeSeries.getPoints(0).getValue().getDoubleValue()).isEqualTo(fakeValue);
  }

  private class FakeMetricServiceClient extends MetricServiceClient {

    protected FakeMetricServiceClient(MetricServiceStub stub) {
      super(stub);
    }
  }
}
