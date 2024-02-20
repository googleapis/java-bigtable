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
package com.google.cloud.bigtable.stats;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.cloud.monitoring.v3.stub.MetricServiceStub;
import com.google.common.collect.ImmutableMap;
import com.google.monitoring.v3.CreateTimeSeriesRequest;
import com.google.protobuf.Empty;
import io.opencensus.common.Timestamp;
import io.opencensus.contrib.resource.util.CloudResource;
import io.opencensus.contrib.resource.util.ContainerResource;
import io.opencensus.contrib.resource.util.HostResource;
import io.opencensus.contrib.resource.util.K8sResource;
import io.opencensus.metrics.LabelKey;
import io.opencensus.metrics.LabelValue;
import io.opencensus.metrics.export.Metric;
import io.opencensus.metrics.export.MetricDescriptor;
import io.opencensus.metrics.export.Point;
import io.opencensus.metrics.export.TimeSeries;
import io.opencensus.metrics.export.Value;
import io.opencensus.resource.Resource;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(JUnit4.class)
public class BigtableCreateTimeSeriesExporterTest {

  private static final String bigtableProjectId = "fake-bigtable-project";
  private static final String bigtableInstanceId = "fake-bigtable-instance";
  private static final String appProfileId = "default";
  private static final String tableId = "fake-table";
  private static final String bigtableZone = "us-east-1";
  private static final String bigtableCluster = "cluster-1";
  private static final String clientName = "client-name";

  private static final String gceProjectId = "fake-gce-project";
  private static final String gceInstanceId = "fake-gce-instance";
  private static final String gceZone = "fake-gce-zone";

  private static final String gkeProjectId = "fake-gke-project";
  private static final String gkeLocation = "fake-gke-location";
  private static final String gkeClusterName = "fake-gke-cluster";
  private static final String gkeNamespaceName = "fake-gke-namespace";
  private static final String gkePodName = "fake-gke-pod";
  private static final String gkeContainerName = "fake-gke-container";

  @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

  @Mock private MetricServiceStub mockMetricServiceStub;
  private MetricServiceClient fakeMetricServiceClient;
  private BigtableCreateTimeSeriesExporter exporter;

  @Before
  public void setUp() {

    fakeMetricServiceClient = new FakeMetricServiceClient(mockMetricServiceStub);

    exporter = new BigtableCreateTimeSeriesExporter(fakeMetricServiceClient);
  }

  @After
  public void tearDown() {}

  @Test
  public void testTimeSeriesForMetricWithBigtableResource() {
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
                "description",
                "ms",
                MetricDescriptor.Type.CUMULATIVE_DOUBLE,
                Arrays.asList(
                    LabelKey.create(BuiltinMeasureConstants.PROJECT_ID.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.INSTANCE_ID.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.TABLE.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.CLUSTER.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.ZONE.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.APP_PROFILE.getName(), ""))),
            Arrays.asList(
                TimeSeries.create(
                    Arrays.asList(
                        LabelValue.create(bigtableProjectId),
                        LabelValue.create(bigtableInstanceId),
                        LabelValue.create(tableId),
                        LabelValue.create(bigtableCluster),
                        LabelValue.create(bigtableZone),
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
            BuiltinMeasureConstants.PROJECT_ID.getName(), bigtableProjectId,
            BuiltinMeasureConstants.INSTANCE_ID.getName(), bigtableInstanceId,
            BuiltinMeasureConstants.TABLE.getName(), tableId,
            BuiltinMeasureConstants.CLUSTER.getName(), bigtableCluster,
            BuiltinMeasureConstants.ZONE.getName(), bigtableZone);

    assertThat(timeSeries.getMetric().getLabelsMap()).hasSize(2);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(BuiltinMeasureConstants.APP_PROFILE.getName(), appProfileId);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsKey(BuiltinMeasureConstants.CLIENT_UID.getName());

    assertThat(timeSeries.getPoints(0).getValue().getDoubleValue()).isEqualTo(fakeValue);
  }

  @Test
  public void testTimeSeriesForMetricWithGceResource() {
    ArgumentCaptor<CreateTimeSeriesRequest> argumentCaptor =
        ArgumentCaptor.forClass(CreateTimeSeriesRequest.class);

    UnaryCallable<CreateTimeSeriesRequest, Empty> mockCallable = mock(UnaryCallable.class);
    when(mockMetricServiceStub.createServiceTimeSeriesCallable()).thenReturn(mockCallable);
    when(mockCallable.call(argumentCaptor.capture())).thenReturn(Empty.getDefaultInstance());

    ConsumerEnvironmentUtils.ResourceUtilsWrapper resourceUtilsWrapperMock =
        Mockito.mock(ConsumerEnvironmentUtils.ResourceUtilsWrapper.class);
    ConsumerEnvironmentUtils.setResourceUtilsWrapper(resourceUtilsWrapperMock);
    Mockito.when(resourceUtilsWrapperMock.detectResource())
        .thenReturn(
            Resource.create(
                HostResource.TYPE,
                ImmutableMap.of(
                    CloudResource.PROVIDER_KEY,
                    CloudResource.PROVIDER_GCP,
                    CloudResource.ACCOUNT_ID_KEY,
                    gceProjectId,
                    HostResource.ID_KEY,
                    gceInstanceId,
                    CloudResource.ZONE_KEY,
                    gceZone)));

    double fakeValue = 10.0;
    Metric fakeMetric =
        Metric.create(
            MetricDescriptor.create(
                "bigtable.googleapis.com/internal/client/per_connection_error_count",
                "description",
                "ms",
                MetricDescriptor.Type.CUMULATIVE_DOUBLE,
                Arrays.asList(
                    LabelKey.create(BuiltinMeasureConstants.PROJECT_ID.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.INSTANCE_ID.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.APP_PROFILE.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.CLIENT_NAME.getName(), ""))),
            Arrays.asList(
                TimeSeries.create(
                    Arrays.asList(
                        LabelValue.create(bigtableProjectId),
                        LabelValue.create(bigtableInstanceId),
                        LabelValue.create(appProfileId),
                        LabelValue.create(clientName)),
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
            ConsumerEnvironmentUtils.GCE_PROJECT_ID_LABEL, gceProjectId,
            ConsumerEnvironmentUtils.GCE_INSTANCE_ID_LABEL, gceInstanceId,
            ConsumerEnvironmentUtils.GCE_ZONE_LABEL, gceZone);

    assertThat(timeSeries.getMetric().getLabelsMap()).hasSize(5);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(BuiltinMeasureConstants.PROJECT_ID.getName(), bigtableProjectId);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(BuiltinMeasureConstants.INSTANCE_ID.getName(), bigtableInstanceId);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(BuiltinMeasureConstants.APP_PROFILE.getName(), appProfileId);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(BuiltinMeasureConstants.CLIENT_NAME.getName(), clientName);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsKey(BuiltinMeasureConstants.CLIENT_UID.getName());

    assertThat(timeSeries.getPoints(0).getValue().getDoubleValue()).isEqualTo(fakeValue);
  }

  @Test
  public void testTimeSeriesForMetricWithGkeResource() {
    ArgumentCaptor<CreateTimeSeriesRequest> argumentCaptor =
        ArgumentCaptor.forClass(CreateTimeSeriesRequest.class);

    UnaryCallable<CreateTimeSeriesRequest, Empty> mockCallable = mock(UnaryCallable.class);
    when(mockMetricServiceStub.createServiceTimeSeriesCallable()).thenReturn(mockCallable);
    when(mockCallable.call(argumentCaptor.capture())).thenReturn(Empty.getDefaultInstance());

    ConsumerEnvironmentUtils.ResourceUtilsWrapper resourceUtilsWrapperMock =
        Mockito.mock(ConsumerEnvironmentUtils.ResourceUtilsWrapper.class);
    ConsumerEnvironmentUtils.setResourceUtilsWrapper(resourceUtilsWrapperMock);

    Mockito.when(resourceUtilsWrapperMock.detectResource())
        .thenReturn(
            Resource.create(
                ContainerResource.TYPE,
                ImmutableMap.of(
                    CloudResource.PROVIDER_KEY,
                    CloudResource.PROVIDER_GCP,
                    CloudResource.ACCOUNT_ID_KEY,
                    gkeProjectId,
                    CloudResource.ZONE_KEY,
                    gkeLocation,
                    K8sResource.CLUSTER_NAME_KEY,
                    gkeClusterName,
                    K8sResource.NAMESPACE_NAME_KEY,
                    gkeNamespaceName,
                    K8sResource.POD_NAME_KEY,
                    gkePodName,
                    ContainerResource.NAME_KEY,
                    gkeContainerName)));

    double fakeValue = 10.0;
    Metric fakeMetric =
        Metric.create(
            MetricDescriptor.create(
                "bigtable.googleapis.com/internal/client/per_connection_error_count",
                "description",
                "ms",
                MetricDescriptor.Type.CUMULATIVE_DOUBLE,
                Arrays.asList(
                    LabelKey.create(BuiltinMeasureConstants.PROJECT_ID.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.INSTANCE_ID.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.APP_PROFILE.getName(), ""),
                    LabelKey.create(BuiltinMeasureConstants.CLIENT_NAME.getName(), ""))),
            Arrays.asList(
                TimeSeries.create(
                    Arrays.asList(
                        LabelValue.create(bigtableProjectId),
                        LabelValue.create(bigtableInstanceId),
                        LabelValue.create(appProfileId),
                        LabelValue.create(clientName)),
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
            ConsumerEnvironmentUtils.GKE_PROJECT_ID_LABEL, gkeProjectId,
            ConsumerEnvironmentUtils.GKE_LOCATION_LABEL, gkeLocation,
            ConsumerEnvironmentUtils.GKE_CLUSTER_NAME_LABEL, gkeClusterName,
            ConsumerEnvironmentUtils.GKE_NAMESPACE_NAME_LABEL, gkeNamespaceName,
            ConsumerEnvironmentUtils.GKE_POD_NAME_LABEL, gkePodName,
            ConsumerEnvironmentUtils.GKE_CONTAINER_NAME_LABEL, gkeContainerName);

    assertThat(timeSeries.getMetric().getLabelsMap()).hasSize(5);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(BuiltinMeasureConstants.PROJECT_ID.getName(), bigtableProjectId);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(BuiltinMeasureConstants.INSTANCE_ID.getName(), bigtableInstanceId);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(BuiltinMeasureConstants.APP_PROFILE.getName(), appProfileId);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsAtLeast(BuiltinMeasureConstants.CLIENT_NAME.getName(), clientName);
    assertThat(timeSeries.getMetric().getLabelsMap())
        .containsKey(BuiltinMeasureConstants.CLIENT_UID.getName());

    assertThat(timeSeries.getPoints(0).getValue().getDoubleValue()).isEqualTo(fakeValue);
  }

  private class FakeMetricServiceClient extends MetricServiceClient {

    protected FakeMetricServiceClient(MetricServiceStub stub) {
      super(stub);
    }
  }
}
