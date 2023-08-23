/*
 * Copyright 2020 Google LLC
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

import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_APP_PROFILE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_INSTANCE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_PROJECT_ID;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.api.client.util.Lists;
import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.UnavailableException;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.bigtable.v2.BigtableGrpc.BigtableImplBase;
import com.google.bigtable.v2.CheckAndMutateRowRequest;
import com.google.bigtable.v2.CheckAndMutateRowResponse;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ReadModifyWriteRowRequest;
import com.google.bigtable.v2.ReadModifyWriteRowResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.SampleRowKeysRequest;
import com.google.bigtable.v2.SampleRowKeysResponse;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.models.ConditionalRowMutation;
import com.google.cloud.bigtable.data.v2.models.KeyOffset;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.ReadModifyWriteRow;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.data.HistogramData;
import io.opentelemetry.sdk.metrics.data.HistogramPointData;
import io.opentelemetry.sdk.metrics.data.LongPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.metrics.data.PointData;
import io.opentelemetry.sdk.metrics.data.SumData;
import io.opentelemetry.sdk.testing.exporter.InMemoryMetricReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class BigtableTracerCallableTest {
  private Server server;
  private Server serverNoHeader;

  private FakeService fakeService = new FakeService();

  private EnhancedBigtableStub stub;
  private EnhancedBigtableStub noHeaderStub;
  private int attempts;

  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";

  @Mock private MetricsTracerFactory mockFactory = Mockito.mock(MetricsTracerFactory.class);

  private AtomicInteger fakeServerTiming;

  private Attributes baseAttributes;
  private InMemoryMetricReader reader = InMemoryMetricReader.create();
  private MetricsTracerRecorder recorder;

  @Before
  public void setUp() throws Exception {
    SdkMeterProvider testMeterProvider =
        SdkMeterProvider.builder().registerMetricReader(reader).build();
    this.recorder = new MetricsTracerRecorder(testMeterProvider.get("test"));

    this.baseAttributes =
        Attributes.of(
            BIGTABLE_PROJECT_ID, PROJECT_ID,
            BIGTABLE_INSTANCE_ID, INSTANCE_ID,
            BIGTABLE_APP_PROFILE_ID, APP_PROFILE_ID);

    // Create a server that'll inject a server-timing header with a random number and a stub that
    // connects to this server.
    fakeServerTiming = new AtomicInteger(new Random().nextInt(1000) + 1);
    server =
        FakeServiceBuilder.create(fakeService)
            .intercept(
                new ServerInterceptor() {
                  @Override
                  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                      ServerCall<ReqT, RespT> serverCall,
                      Metadata metadata,
                      ServerCallHandler<ReqT, RespT> serverCallHandler) {
                    return serverCallHandler.startCall(
                        new SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
                          @Override
                          public void sendHeaders(Metadata headers) {
                            headers.put(
                                Metadata.Key.of("server-timing", Metadata.ASCII_STRING_MARSHALLER),
                                String.format("gfet4t7; dur=%d", fakeServerTiming.get()));
                            super.sendHeaders(headers);
                          }
                        },
                        metadata);
                  }
                })
            .start();

    BigtableDataSettings settings =
        BigtableDataSettings.newBuilderForEmulator(server.getPort())
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();
    EnhancedBigtableStubSettings.Builder builder = settings.getStubSettings().toBuilder();
    builder.setTracerFactory(mockFactory);
    stub = new EnhancedBigtableStub(builder.build(), ClientContext.create(builder.build()));

    attempts = builder.readRowsSettings().getRetrySettings().getMaxAttempts();

    // Create another server without injecting the server-timing header and another stub that
    // connects to it.
    serverNoHeader = FakeServiceBuilder.create(fakeService).start();

    BigtableDataSettings noHeaderSettings =
        BigtableDataSettings.newBuilderForEmulator(serverNoHeader.getPort())
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();
    EnhancedBigtableStubSettings.Builder noHeaderBuilder =
        noHeaderSettings.getStubSettings().toBuilder();
    noHeaderBuilder.setTracerFactory(mockFactory);
    noHeaderStub =
        new EnhancedBigtableStub(
            noHeaderBuilder.build(), ClientContext.create(noHeaderBuilder.build()));
  }

  @After
  public void tearDown() {
    stub.close();
    noHeaderStub.close();
    server.shutdown();
    serverNoHeader.shutdown();
  }

  @Test
  public void testGFELatencyMetricReadRows() {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new MetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                recorder,
                baseAttributes));

    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)));

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.GFE_LATENCY_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.ReadRows", "OK");

    HistogramData histogramData = metric.getHistogramData();
    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
    assertThat((long) histogramPointData.iterator().next().getSum())
        .isEqualTo(fakeServerTiming.get());
  }

  @Test
  public void testGFELatencyMetricMutateRow() throws Exception {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new MetricsTracer(
                ApiTracerFactory.OperationType.Unary,
                SpanName.of("Bigtable", "MutateRow"),
                recorder,
                baseAttributes));

    ApiFuture<Void> future =
        stub.mutateRowCallable().futureCall(RowMutation.create(TABLE_ID, "fake-key"));
    future.get(10, TimeUnit.SECONDS);

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.GFE_LATENCY_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.MutateRow", "OK");

    HistogramData histogramData = metric.getHistogramData();
    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
    assertThat((long) histogramPointData.iterator().next().getSum())
        .isEqualTo(fakeServerTiming.get());
  }

  //  @Test
  //  public void testGFELatencyMetricMutateRows() throws Exception {
  //    when(mockFactory.newTracer(any(), any(), any())).thenReturn(new MetricsTracer(
  //            ApiTracerFactory.OperationType.Unary,
  //            SpanName.of("Bigtable", "MutateRows"),
  //            recorder,
  //            baseAttributes));
  //
  //    BulkMutation mutations =
  //        BulkMutation.create(TABLE_ID)
  //            .add("key", Mutation.create().setCell("fake-family", "fake-qualifier",
  // "fake-value"));
  //    ApiFuture<Void> future = stub.bulkMutateRowsCallable().futureCall(mutations);
  //    future.get(10, TimeUnit.SECONDS);
  //
  //    Collection<MetricData> metrics = reader.collectAllMetrics();
  //    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.GFE_LATENCY_NAME);
  //    assertThat(metric).isNotNull();
  //    PointData pointData = metric.getData().getPoints().iterator().next();
  //
  //    assertThat(pointData.getAttributes().asMap().values())
  //            .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.MutateRows",
  // "OK");
  //
  //    HistogramData histogramData = metric.getHistogramData();
  //    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
  //    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
  //    assertThat((long)
  // histogramPointData.iterator().next().getSum()).isEqualTo(fakeServerTiming.get());
  //  }

  @Test
  public void testGFELatencySampleRowKeys() throws Exception {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new MetricsTracer(
                ApiTracerFactory.OperationType.Unary,
                SpanName.of("Bigtable", "SampleRowKeys"),
                recorder,
                baseAttributes));

    ApiFuture<List<KeyOffset>> future = stub.sampleRowKeysCallable().futureCall(TABLE_ID);
    future.get(10, TimeUnit.SECONDS);

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.GFE_LATENCY_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.SampleRowKeys", "OK");

    HistogramData histogramData = metric.getHistogramData();
    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
    assertThat((long) histogramPointData.iterator().next().getSum())
        .isEqualTo(fakeServerTiming.get());
  }

  @Test
  public void testGFELatencyCheckAndMutateRow() throws Exception {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new MetricsTracer(
                ApiTracerFactory.OperationType.Unary,
                SpanName.of("Bigtable", "CheckAndMutateRow"),
                recorder,
                baseAttributes));

    ConditionalRowMutation mutation =
        ConditionalRowMutation.create(TABLE_ID, "fake-key")
            .then(Mutation.create().setCell("fake-family", "fake-qualifier", "fake-value"));
    ApiFuture<Boolean> future = stub.checkAndMutateRowCallable().futureCall(mutation);
    future.get(10, TimeUnit.SECONDS);

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.GFE_LATENCY_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(
            PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.CheckAndMutateRow", "OK");

    HistogramData histogramData = metric.getHistogramData();
    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
    assertThat((long) histogramPointData.iterator().next().getSum())
        .isEqualTo(fakeServerTiming.get());
  }

  @Test
  public void testGFELatencyReadModifyWriteRow() throws Exception {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new MetricsTracer(
                ApiTracerFactory.OperationType.Unary,
                SpanName.of("Bigtable", "ReadModifyWriteRow"),
                recorder,
                baseAttributes));

    ReadModifyWriteRow request =
        ReadModifyWriteRow.create(TABLE_ID, "fake-key")
            .append("fake-family", "fake-qualifier", "suffix");
    ApiFuture<Row> future = stub.readModifyWriteRowCallable().futureCall(request);
    future.get(10, TimeUnit.SECONDS);

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.GFE_LATENCY_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(
            PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.ReadModifyWriteRow", "OK");

    HistogramData histogramData = metric.getHistogramData();
    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
    assertThat((long) histogramPointData.iterator().next().getSum())
        .isEqualTo(fakeServerTiming.get());
  }

  @Test
  public void testGFEMissingHeaderMetric() throws Exception {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new MetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "MutateRow"),
                recorder,
                baseAttributes));

    // Make a few calls to the server which will inject the server-timing header and the counter
    // should be 0.
    ApiFuture<Void> future =
        stub.mutateRowCallable().futureCall(RowMutation.create(TABLE_ID, "key0"));
    future.get(10, TimeUnit.SECONDS);
    future = stub.mutateRowCallable().futureCall(RowMutation.create(TABLE_ID, "key1"));
    future.get(10, TimeUnit.SECONDS);

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.GFE_MISSING_HEADER_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.MutateRow", "OK");

    SumData<LongPointData> longData = metric.getLongSumData();
    long count = longData.getPoints().iterator().next().getValue();
    assertThat(count).isEqualTo(0);
  }

  @Test
  public void testGFEMissingHeaderMetricNonZero() {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new MetricsTracer(
                ApiTracerFactory.OperationType.Unary,
                SpanName.of("Bigtable", "MutateRow"),
                recorder,
                baseAttributes));

    // Make a few more calls to the server which won't add the header and the counter should
    // match
    // the number of requests sent.
    List<ApiFuture<Void>> futures = new ArrayList<>();
    int mutateRowCalls = new Random().nextInt(10) + 1;
    for (int i = 0; i < mutateRowCalls; i++) {
      futures.add(
          noHeaderStub
              .mutateRowCallable()
              .futureCall(RowMutation.create(TABLE_ID, "fake-key" + i)));
    }
    futures.forEach(
        f -> {
          try {
            f.get(1, TimeUnit.SECONDS);
          } catch (Exception e) {
          }
        });

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.GFE_MISSING_HEADER_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.MutateRow", "OK");

    SumData<LongPointData> longData = metric.getLongSumData();
    long count = longData.getPoints().iterator().next().getValue();
    assertThat(count).isEqualTo(mutateRowCalls);
  }

  @Test
  public void testMetricsWithErrorResponse() throws InterruptedException {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new MetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                recorder,
                baseAttributes));
    try {
      stub.readRowsCallable().call(Query.create("random-table-id")).iterator().next();
      fail("readrows should throw exception");
    } catch (Exception e) {
      assertThat(e).isInstanceOf(UnavailableException.class);
    }

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.GFE_MISSING_HEADER_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(
            PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.ReadRows", "UNAVAILABLE");

    SumData<LongPointData> longData = metric.getLongSumData();
    long count = longData.getPoints().iterator().next().getValue();
    assertThat(count).isEqualTo(attempts);
  }

  private class FakeService extends BigtableImplBase {
    private final String defaultTableName =
        NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID);

    @Override
    public void readRows(ReadRowsRequest request, StreamObserver<ReadRowsResponse> observer) {
      if (!request.getTableName().equals(defaultTableName)) {
        observer.onError(new StatusRuntimeException(Status.UNAVAILABLE));
        return;
      }
      observer.onNext(ReadRowsResponse.getDefaultInstance());
      observer.onCompleted();
    }

    @Override
    public void mutateRow(MutateRowRequest request, StreamObserver<MutateRowResponse> observer) {
      observer.onNext(MutateRowResponse.getDefaultInstance());
      observer.onCompleted();
    }

    @Override
    public void mutateRows(MutateRowsRequest request, StreamObserver<MutateRowsResponse> observer) {
      observer.onNext(MutateRowsResponse.getDefaultInstance());
      observer.onCompleted();
    }

    @Override
    public void sampleRowKeys(
        SampleRowKeysRequest request, StreamObserver<SampleRowKeysResponse> observer) {
      observer.onNext(SampleRowKeysResponse.getDefaultInstance());
      observer.onCompleted();
    }

    @Override
    public void checkAndMutateRow(
        CheckAndMutateRowRequest request, StreamObserver<CheckAndMutateRowResponse> observer) {
      observer.onNext(CheckAndMutateRowResponse.getDefaultInstance());
      observer.onCompleted();
    }

    @Override
    public void readModifyWriteRow(
        ReadModifyWriteRowRequest request, StreamObserver<ReadModifyWriteRowResponse> observer) {
      observer.onNext(ReadModifyWriteRowResponse.getDefaultInstance());
      observer.onCompleted();
    }
  }
}
