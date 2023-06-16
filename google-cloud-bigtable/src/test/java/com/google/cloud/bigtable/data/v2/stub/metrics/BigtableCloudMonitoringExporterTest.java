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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.APP_PROFILE;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CLIENT_UID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.CLUSTER_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.INSTANCE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.PROJECT_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.TABLE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.ZONE_ID;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.api.Distribution;
import com.google.api.MonitoredResource;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.stub.MetricServiceStub;
import com.google.common.collect.ImmutableList;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.monitoring.v3.TimeSeries;
import com.google.protobuf.Empty;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.common.InstrumentationScopeInfo;
import io.opentelemetry.sdk.metrics.data.AggregationTemporality;
import io.opentelemetry.sdk.metrics.data.HistogramPointData;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableHistogramData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableHistogramPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableLongPointData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableMetricData;
import io.opentelemetry.sdk.metrics.internal.data.ImmutableSumData;
import io.opentelemetry.sdk.resources.Resource;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BigtableCloudMonitoringExporterTest {
  private static final String projectId = "fake-project";
  private static final String instanceId = "fake-instance";
  private static final String appProfileId = "default";
  private static final String tableId = "fake-table";
  private static final String zone = "us-east-1";
  private static final String cluster = "cluster-1";

  private static final String taskId = "fake-task-id";

  @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock private MetricServiceStub mockMetricServiceStub;
  private MetricServiceClient fakeMetricServiceClient;
  private BigtableCloudMonitoringExporter exporter;

  private Attributes attributes;
  private Resource resource;
  private InstrumentationScopeInfo scope;

  @Before
  public void setUp() {

    fakeMetricServiceClient = new FakeMetricServiceClient(mockMetricServiceStub);

    exporter =
        new BigtableCloudMonitoringExporter(
            projectId,
            fakeMetricServiceClient,
            MonitoredResource.newBuilder().setType("bigtable-table").build(),
            taskId);

    attributes =
        Attributes.builder()
            .put(PROJECT_ID, projectId)
            .put(INSTANCE_ID, instanceId)
            .put(TABLE_ID, tableId)
            .put(CLUSTER_ID, cluster)
            .put(ZONE_ID, zone)
            .put(APP_PROFILE, appProfileId)
            .build();

    resource = Resource.create(Attributes.empty());

    scope = InstrumentationScopeInfo.create("bigtable");
  }

  @After
  public void tearDown() {}

  @Test
  public void testExportingSumData() {
    ArgumentCaptor<CreateTimeSeriesRequest> argumentCaptor =
        ArgumentCaptor.forClass(CreateTimeSeriesRequest.class);

    UnaryCallable<CreateTimeSeriesRequest, Empty> mockCallable = mock(UnaryCallable.class);
    when(mockMetricServiceStub.createServiceTimeSeriesCallable()).thenReturn(mockCallable);
    when(mockCallable.call(argumentCaptor.capture())).thenReturn(Empty.getDefaultInstance());

    long fakeValue = 11L;

    LongPointData longPointData = ImmutableLongPointData.create(0, 1, attributes, fakeValue);

    MetricData longData =
        ImmutableMetricData.createLongSum(
            resource,
            scope,
            "bigtable/test/long",
            "description",
            "1",
            ImmutableSumData.create(
                true, AggregationTemporality.CUMULATIVE, ImmutableList.of(longPointData)));

    exporter.export(Arrays.asList(longData));

    CreateTimeSeriesRequest request = argumentCaptor.getValue();

    assertThat(request.getTimeSeriesList()).hasSize(1);

    TimeSeries timeSeries = request.getTimeSeriesList().get(0);

    assertThat(timeSeries.getResource().getLabelsMap())
        .containsExactly(
            PROJECT_ID.getKey(), projectId,
            INSTANCE_ID.getKey(), instanceId,
            TABLE_ID.getKey(), tableId,
            CLUSTER_ID.getKey(), cluster,
            ZONE_ID.getKey(), zone);

    assertThat(timeSeries.getMetric().getLabelsMap()).hasSize(2);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(APP_PROFILE.getKey(), appProfileId);
    assertThat(timeSeries.getMetric().getLabelsMap()).containsAtLeast(CLIENT_UID.getKey(), taskId);
    assertThat(timeSeries.getPoints(0).getValue().getInt64Value()).isEqualTo(fakeValue);
  }

  @Test
  public void testExportingHistogramData() {
    ArgumentCaptor<CreateTimeSeriesRequest> argumentCaptor =
        ArgumentCaptor.forClass(CreateTimeSeriesRequest.class);

    UnaryCallable<CreateTimeSeriesRequest, Empty> mockCallable = mock(UnaryCallable.class);
    when(mockMetricServiceStub.createServiceTimeSeriesCallable()).thenReturn(mockCallable);
    when(mockCallable.call(argumentCaptor.capture())).thenReturn(Empty.getDefaultInstance());

    HistogramPointData histogramPointData =
        ImmutableHistogramPointData.create(
            0,
            1,
            attributes,
            3d,
            true,
            1d, // min
            true,
            2d, // max
            Arrays.asList(1.0),
            Arrays.asList(1L, 2L));

    MetricData histogramData =
        ImmutableMetricData.createDoubleHistogram(
            resource,
            scope,
            "bigtable/test/histogram",
            "description",
            "ms",
            ImmutableHistogramData.create(
                AggregationTemporality.CUMULATIVE, ImmutableList.of(histogramPointData)));

    exporter.export(Arrays.asList(histogramData));

    CreateTimeSeriesRequest request = argumentCaptor.getValue();

    assertThat(request.getTimeSeriesList()).hasSize(1);

    TimeSeries timeSeries = request.getTimeSeriesList().get(0);

    assertThat(timeSeries.getResource().getLabelsMap())
        .containsExactly(
            PROJECT_ID.getKey(), projectId,
            INSTANCE_ID.getKey(), instanceId,
            TABLE_ID.getKey(), tableId,
            CLUSTER_ID.getKey(), cluster,
            ZONE_ID.getKey(), zone);

    assertThat(timeSeries.getMetric().getLabelsMap()).hasSize(2);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(APP_PROFILE.getKey(), appProfileId);
    assertThat(timeSeries.getMetric().getLabelsMap()).containsAtLeast(CLIENT_UID.getKey(), taskId);
    Distribution distribution = timeSeries.getPoints(0).getValue().getDistributionValue();
    assertThat(distribution.getCount()).isEqualTo(3);
  }

  private static class FakeMetricServiceClient extends MetricServiceClient {

    protected FakeMetricServiceClient(MetricServiceStub stub) {
      super(stub);
    }
  }
}
