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
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_OP;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_PROJECT_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_STATUS;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import com.google.api.gax.batching.Batcher;
import com.google.api.gax.batching.BatcherImpl;
import com.google.api.gax.batching.BatchingDescriptor;
import com.google.api.gax.batching.FlowController;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.ReadRowsResponse.CellChunk;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.cloud.bigtable.data.v2.stub.mutaterows.MutateRowsBatchingDescriptor;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.util.concurrent.SettableFuture;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.StringValue;
import io.grpc.Server;
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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

@RunWith(JUnit4.class)
public class MetricsTracerTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";

  private static final ReadRowsResponse DEFAULT_READ_ROWS_RESPONSES =
      ReadRowsResponse.newBuilder()
          .addChunks(
              CellChunk.newBuilder()
                  .setRowKey(ByteString.copyFromUtf8("fake-key"))
                  .setFamilyName(StringValue.of("cf"))
                  .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                  .setTimestampMicros(1_000)
                  .setValue(ByteString.copyFromUtf8("value"))
                  .setCommitRow(true))
          .build();

  @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

  private Server server;

  @Mock(answer = Answers.CALLS_REAL_METHODS)
  private BigtableGrpc.BigtableImplBase mockService;

  private EnhancedBigtableStub stub;
  private BigtableDataSettings settings;

  @Mock private MetricsTracerFactory mockFactory = Mockito.mock(MetricsTracerFactory.class);

  private Attributes baseAttributes;

  private InMemoryMetricReader reader = InMemoryMetricReader.create();
  private MetricsTracerRecorder recorder;

  @Before
  public void setUp() throws Exception {
    SdkMeterProvider testMeterProvider =
        SdkMeterProvider.builder().registerMetricReader(reader).build();
    this.recorder = new MetricsTracerRecorder(testMeterProvider.get("test"));

    server = FakeServiceBuilder.create(mockService).start();

    settings =
        BigtableDataSettings.newBuilderForEmulator(server.getPort())
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();
    EnhancedBigtableStubSettings.Builder builder = settings.getStubSettings().toBuilder();
    builder.setTracerFactory(mockFactory);
    stub = new EnhancedBigtableStub(builder.build(), ClientContext.create(builder.build()));

    this.baseAttributes =
        Attributes.of(
            BIGTABLE_PROJECT_ID, PROJECT_ID,
            BIGTABLE_INSTANCE_ID, INSTANCE_ID,
            BIGTABLE_APP_PROFILE_ID, APP_PROFILE_ID);
  }

  @After
  public void tearDown() {
    stub.close();
    server.shutdown();
  }

  @Test
  public void testReadRowsLatency() {
    final long sleepTime = 50;

    doAnswer(
            invocation ->
                new MetricsTracer(
                    ApiTracerFactory.OperationType.ServerStreaming,
                    SpanName.of("Bigtable", "ReadRows"),
                    recorder,
                    baseAttributes))
        .when(mockFactory)
        .newTracer(any(), any(), any());

    doAnswer(
            invocation -> {
              @SuppressWarnings("unchecked")
              StreamObserver<ReadRowsResponse> observer =
                  (StreamObserver<ReadRowsResponse>) invocation.getArguments()[1];
              Thread.sleep(sleepTime);
              observer.onNext(DEFAULT_READ_ROWS_RESPONSES);
              observer.onCompleted();
              return null;
            })
        .when(mockService)
        .readRows(any(ReadRowsRequest.class), any());

    Stopwatch stopwatch = Stopwatch.createStarted();
    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)));
    long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.OP_LATENCY_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.ReadRows", "OK");

    HistogramData histogramData = metric.getHistogramData();
    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
    assertThat((long) histogramPointData.iterator().next().getSum())
        .isIn(Range.closed(sleepTime, elapsed));
  }

  @Test
  public void testReadRowsOpCount() {
    doAnswer(
            invocation ->
                new MetricsTracer(
                    ApiTracerFactory.OperationType.ServerStreaming,
                    SpanName.of("Bigtable", "ReadRows"),
                    recorder,
                    baseAttributes))
        .when(mockFactory)
        .newTracer(any(), any(), any());

    doAnswer(
            invocation -> {
              @SuppressWarnings("unchecked")
              StreamObserver<ReadRowsResponse> observer =
                  (StreamObserver<ReadRowsResponse>) invocation.getArguments()[1];
              observer.onNext(DEFAULT_READ_ROWS_RESPONSES);
              observer.onCompleted();
              return null;
            })
        .when(mockService)
        .readRows(any(ReadRowsRequest.class), any());

    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)));
    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)));

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.COMPLETED_OPS_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.ReadRows", "OK");

    SumData<LongPointData> longData = metric.getLongSumData();
    long count = longData.getPoints().iterator().next().getValue();
    assertThat(count).isEqualTo(2);
  }

  @Test
  public void testReadRowsFirstRow() {
    final long beforeSleep = 50;
    final long afterSleep = 50;

    SettableFuture<Void> gotFirstRow = SettableFuture.create();

    ExecutorService executor = Executors.newCachedThreadPool();

    doAnswer(
            invocation ->
                new MetricsTracer(
                    ApiTracerFactory.OperationType.ServerStreaming,
                    SpanName.of("Bigtable", "ReadRows"),
                    recorder,
                    baseAttributes))
        .when(mockFactory)
        .newTracer(any(), any(), any());

    doAnswer(
            invocation -> {
              StreamObserver<ReadRowsResponse> observer = invocation.getArgument(1);
              executor.submit(
                  () -> {
                    Thread.sleep(beforeSleep);
                    observer.onNext(DEFAULT_READ_ROWS_RESPONSES);
                    // wait until the first row is consumed before padding the operation span
                    gotFirstRow.get();
                    Thread.sleep(afterSleep);
                    observer.onCompleted();
                    return null;
                  });
              return null;
            })
        .when(mockService)
        .readRows(any(ReadRowsRequest.class), any());

    Stopwatch stopwatch = Stopwatch.createStarted();

    // Get the first row and notify the mock that it can start padding the operation span
    Iterator<Row> it = stub.readRowsCallable().call(Query.create(TABLE_ID)).iterator();
    it.next();
    gotFirstRow.set(null);
    // finish the stream
    while (it.hasNext()) {
      it.next();
    }
    long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

    executor.shutdown();

    Collection<MetricData> metrics = reader.collectAllMetrics();
    MetricData metric =
        StatsTestUtils.getMetric(metrics, RpcViewConstants.READ_ROWS_FIRST_ROW_LATENCY_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();
    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.ReadRows", "OK");

    HistogramData histogramData = metric.getHistogramData();
    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
    assertThat((long) histogramPointData.iterator().next().getSum())
        .isIn(Range.closed(beforeSleep, elapsed - afterSleep));
  }

  @Test
  public void testReadRowsAttemptsPerOp() {
    final AtomicInteger callCount = new AtomicInteger(0);

    doAnswer(
            invocation ->
                new MetricsTracer(
                    ApiTracerFactory.OperationType.ServerStreaming,
                    SpanName.of("Bigtable", "ReadRows"),
                    recorder,
                    baseAttributes))
        .when(mockFactory)
        .newTracer(any(), any(), any());

    doAnswer(
            new Answer() {
              @Override
              public Object answer(InvocationOnMock invocation) {
                @SuppressWarnings("unchecked")
                StreamObserver<ReadRowsResponse> observer =
                    (StreamObserver<ReadRowsResponse>) invocation.getArguments()[1];

                // First call will trigger a transient error
                if (callCount.getAndIncrement() == 0) {
                  observer.onError(new StatusRuntimeException(Status.UNAVAILABLE));
                  return null;
                }

                // Next attempt will return a row
                observer.onNext(DEFAULT_READ_ROWS_RESPONSES);
                observer.onCompleted();
                return null;
              }
            })
        .when(mockService)
        .readRows(any(ReadRowsRequest.class), any());

    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)));

    Collection<MetricData> metrics = reader.collectAllMetrics();
    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.ATTEMPTS_PER_OP_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();

    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.ReadRows", "OK");

    SumData<LongPointData> longData = metric.getLongSumData();
    long count = longData.getPoints().iterator().next().getValue();
    assertThat(count).isEqualTo(2);
  }

  @Test
  public void testReadRowsAttemptLatency() {
    final long sleepTime = 50;
    final AtomicInteger callCount = new AtomicInteger(0);

    doAnswer(
            invocation ->
                new MetricsTracer(
                    ApiTracerFactory.OperationType.ServerStreaming,
                    SpanName.of("Bigtable", "ReadRows"),
                    recorder,
                    baseAttributes))
        .when(mockFactory)
        .newTracer(any(), any(), any());

    doAnswer(
            new Answer() {
              @Override
              public Object answer(InvocationOnMock invocation) throws Throwable {
                @SuppressWarnings("unchecked")
                StreamObserver<ReadRowsResponse> observer =
                    (StreamObserver<ReadRowsResponse>) invocation.getArguments()[1];

                Thread.sleep(sleepTime);

                // First attempt will return a transient error
                if (callCount.getAndIncrement() == 0) {
                  observer.onError(new StatusRuntimeException(Status.UNAVAILABLE));
                  return null;
                }
                // Next attempt will be ok
                observer.onNext(DEFAULT_READ_ROWS_RESPONSES);
                observer.onCompleted();
                return null;
              }
            })
        .when(mockService)
        .readRows(any(ReadRowsRequest.class), any());

    Stopwatch stopwatch = Stopwatch.createStarted();
    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)));
    long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

    Collection<MetricData> metrics = reader.collectAllMetrics();

    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.ATTEMPT_LATENCY_NAME);
    assertThat(metric).isNotNull();
    List<PointData> pointData = new ArrayList<>(metric.getData().getPoints());

    Collection<Attributes> attributes =
        pointData.stream().map(PointData::getAttributes).collect(Collectors.toList());
    List<String> values = new ArrayList<>();
    Attributes expected1 =
        Attributes.of(
            BIGTABLE_PROJECT_ID,
            PROJECT_ID,
            BIGTABLE_INSTANCE_ID,
            INSTANCE_ID,
            BIGTABLE_APP_PROFILE_ID,
            APP_PROFILE_ID,
            BIGTABLE_OP,
            "Bigtable.ReadRows",
            BIGTABLE_STATUS,
            "UNAVAILABLE");
    Attributes expected2 =
        Attributes.of(
            BIGTABLE_PROJECT_ID,
            PROJECT_ID,
            BIGTABLE_INSTANCE_ID,
            INSTANCE_ID,
            BIGTABLE_APP_PROFILE_ID,
            APP_PROFILE_ID,
            BIGTABLE_OP,
            "Bigtable.ReadRows",
            BIGTABLE_STATUS,
            "OK");
    assertThat(attributes).containsExactly(expected1, expected2);

    HistogramData histogramData = metric.getHistogramData();
    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
    // Average attempt latency will be just a single wait (as opposed to op latency which will be 2x
    // sleeptime)
    assertThat((long) histogramPointData.iterator().next().getSum())
        .isIn(Range.closed(sleepTime, elapsed - sleepTime));
  }

  @Test
  public void testInvalidRequest() {
    try {
      doAnswer(
              invocation ->
                  new MetricsTracer(
                      ApiTracerFactory.OperationType.ServerStreaming,
                      SpanName.of("Bigtable", "MutateRows"),
                      recorder,
                      baseAttributes))
          .when(mockFactory)
          .newTracer(any(), any(), any());

      stub.bulkMutateRowsCallable().call(BulkMutation.create(TABLE_ID));
      Assert.fail("Invalid request should throw exception");
    } catch (IllegalStateException e) {
      // Verify that the latency is recorded with an error code (in this case UNKNOWN)
      Collection<MetricData> metrics = reader.collectAllMetrics();

      MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.ATTEMPT_LATENCY_NAME);
      assertThat(metric).isNotNull();
      PointData pointData = metric.getData().getPoints().iterator().next();

      assertThat(pointData.getAttributes().asMap().values())
          .containsExactly(
              PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.MutateRows", "UNKNOWN");
    }
  }

  @Test
  public void testBatchReadRowsThrottledTime() throws Exception {
    doAnswer(
            invocation ->
                new MetricsTracer(
                    ApiTracerFactory.OperationType.ServerStreaming,
                    SpanName.of("Bigtable", "ReadRows"),
                    recorder,
                    baseAttributes))
        .when(mockFactory)
        .newTracer(any(), any(), any());

    doAnswer(
            new Answer() {
              @Override
              public Object answer(InvocationOnMock invocation) {
                @SuppressWarnings("unchecked")
                StreamObserver<ReadRowsResponse> observer =
                    (StreamObserver<ReadRowsResponse>) invocation.getArguments()[1];
                observer.onNext(DEFAULT_READ_ROWS_RESPONSES);
                observer.onCompleted();
                return null;
              }
            })
        .when(mockService)
        .readRows(any(ReadRowsRequest.class), any());

    try (Batcher batcher =
        stub.newBulkReadRowsBatcher(Query.create(TABLE_ID), GrpcCallContext.createDefault())) {
      batcher.add(ByteString.copyFromUtf8("row1"));
      batcher.sendOutstanding();

      Collection<MetricData> metrics = reader.collectAllMetrics();
      MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.THROTTLED_TIME_NAME);
      assertThat(metric).isNotNull();
      PointData pointData = metric.getData().getPoints().iterator().next();
      assertThat(pointData.getAttributes().asMap().values())
          .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.ReadRows");

      HistogramData histogramData = metric.getHistogramData();
      Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
      assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
      assertThat((long) histogramPointData.iterator().next().getSum()).isEqualTo(0);
    }
  }

  @Test
  public void testBatchMutateRowsThrottledTime() throws Exception {
    FlowController flowController = Mockito.mock(FlowController.class);
    BatchingDescriptor batchingDescriptor = Mockito.mock(MutateRowsBatchingDescriptor.class);
    // Mock throttling
    final long throttled = 50;
    doAnswer(
            invocation ->
                new MetricsTracer(
                    ApiTracerFactory.OperationType.ServerStreaming,
                    SpanName.of("Bigtable", "MutateRows"),
                    recorder,
                    baseAttributes))
        .when(mockFactory)
        .newTracer(any(), any(), any());

    doAnswer(
            new Answer() {
              @Override
              public Object answer(InvocationOnMock invocation) throws Throwable {
                Thread.sleep(throttled);
                return null;
              }
            })
        .when(flowController)
        .reserve(any(Long.class), any(Long.class));
    when(flowController.getMaxElementCountLimit()).thenReturn(null);
    when(flowController.getMaxRequestBytesLimit()).thenReturn(null);
    when(batchingDescriptor.countBytes(any())).thenReturn(1l);
    when(batchingDescriptor.newRequestBuilder(any())).thenCallRealMethod();

    doAnswer(
            new Answer() {
              @Override
              public Object answer(InvocationOnMock invocation) {
                @SuppressWarnings("unchecked")
                StreamObserver<MutateRowsResponse> observer =
                    (StreamObserver<MutateRowsResponse>) invocation.getArguments()[1];
                observer.onNext(MutateRowsResponse.getDefaultInstance());
                observer.onCompleted();
                return null;
              }
            })
        .when(mockService)
        .mutateRows(any(MutateRowsRequest.class), any());

    ApiCallContext defaultContext = GrpcCallContext.createDefault();

    Batcher batcher =
        new BatcherImpl(
            batchingDescriptor,
            stub.bulkMutateRowsCallable().withDefaultCallContext(defaultContext),
            BulkMutation.create(TABLE_ID),
            settings.getStubSettings().bulkMutateRowsSettings().getBatchingSettings(),
            Executors.newSingleThreadScheduledExecutor(),
            flowController,
            defaultContext);

    batcher.add(RowMutationEntry.create("key"));
    batcher.sendOutstanding();

    Collection<MetricData> metrics = reader.collectAllMetrics();
    MetricData metric = StatsTestUtils.getMetric(metrics, RpcViewConstants.THROTTLED_TIME_NAME);
    assertThat(metric).isNotNull();
    PointData pointData = metric.getData().getPoints().iterator().next();
    assertThat(pointData.getAttributes().asMap().values())
        .containsExactly(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID, "Bigtable.MutateRows");

    HistogramData histogramData = metric.getHistogramData();
    Collection<HistogramPointData> histogramPointData = histogramData.getPoints();
    assertThat(histogramPointData.iterator().next().getCount()).isEqualTo(1);
    assertThat((long) histogramPointData.iterator().next().getSum()).isAtLeast(throttled);
  }
}
