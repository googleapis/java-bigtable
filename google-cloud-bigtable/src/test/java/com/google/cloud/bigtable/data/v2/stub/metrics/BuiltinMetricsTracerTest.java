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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import static com.google.common.truth.Truth.assertThat;

import com.google.api.client.util.Lists;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.ResponseParams;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceHelper;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.StringValue;
import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.opencensus.impl.stats.StatsComponentImpl;
import io.opencensus.tags.Tags;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.threeten.bp.Duration;

public class BuiltinMetricsTracerTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";
  private static final String ZONE = "us-east-1";
  private static final String CLUSTER = "cluster-1";
  private static final long FAKE_SERVER_TIMING = 50;
  private static final long SERVER_LATENCY = 500;

  private final AtomicInteger rpcCount = new AtomicInteger(0);

  private static final ReadRowsResponse READ_ROWS_RESPONSE_1 =
      ReadRowsResponse.newBuilder()
          .addChunks(
              ReadRowsResponse.CellChunk.newBuilder()
                  .setRowKey(ByteString.copyFromUtf8("fake-key-1"))
                  .setFamilyName(StringValue.of("cf"))
                  .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                  .setTimestampMicros(1_000)
                  .setValue(ByteString.copyFromUtf8("value"))
                  .setCommitRow(true))
          .build();
  private static final ReadRowsResponse READ_ROWS_RESPONSE_2 =
      ReadRowsResponse.newBuilder()
          .addChunks(
              ReadRowsResponse.CellChunk.newBuilder()
                  .setRowKey(ByteString.copyFromUtf8("fake-key-2"))
                  .setFamilyName(StringValue.of("cf"))
                  .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                  .setTimestampMicros(1_000)
                  .setValue(ByteString.copyFromUtf8("value"))
                  .setCommitRow(true))
          .build();
  private static final ReadRowsResponse READ_ROWS_RESPONSE_3 =
      ReadRowsResponse.newBuilder()
          .addChunks(
              ReadRowsResponse.CellChunk.newBuilder()
                  .setRowKey(ByteString.copyFromUtf8("fake-key-3"))
                  .setFamilyName(StringValue.of("cf"))
                  .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                  .setTimestampMicros(1_000)
                  .setValue(ByteString.copyFromUtf8("value"))
                  .setCommitRow(true))
          .build();

  @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

  private FakeServiceHelper serviceHelper;

  private BigtableGrpc.BigtableImplBase mockService;

  private EnhancedBigtableStub stub;

  private StatsComponentImpl clientStats = new StatsComponentImpl();
  private com.google.bigtable.veneer.repackaged.io.opencensus.impl.stats.StatsComponentImpl
      builtinStats =
          new com.google.bigtable.veneer.repackaged.io.opencensus.impl.stats.StatsComponentImpl();

  private Stopwatch serverRetryDelayStopwatch;
  private AtomicLong serverTotalRetryDelay;

  @Before
  public void setUp() throws Exception {
    mockService = new FakeService();

    serverRetryDelayStopwatch = Stopwatch.createUnstarted();
    serverTotalRetryDelay = new AtomicLong(0);

    // Add an interceptor to send location information in the trailers and add server-timing in
    // headers
    ServerInterceptor trailersInterceptor =
        new ServerInterceptor() {
          @Override
          public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
              ServerCall<ReqT, RespT> serverCall,
              Metadata metadata,
              ServerCallHandler<ReqT, RespT> serverCallHandler) {
            return serverCallHandler.startCall(
                new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
                  @Override
                  public void sendHeaders(Metadata headers) {
                    headers.put(
                        Metadata.Key.of("server-timing", Metadata.ASCII_STRING_MARSHALLER),
                        String.format("gfet4t7; dur=%d", FAKE_SERVER_TIMING));
                    super.sendHeaders(headers);
                  }

                  @Override
                  public void close(Status status, Metadata trailers) {
                    ResponseParams params = ResponseParams.newBuilder()
                            .setClusterId(CLUSTER)
                            .setZoneId(ZONE)
                            .build();
                    byte[] byteArray = params.toByteArray();
                    trailers.put(Metadata.Key.of(Util.TRAILER_KEY, Metadata.BINARY_BYTE_MARSHALLER), byteArray);
                    super.close(status, trailers);
                  }
                },
                metadata);
          }
        };

    serviceHelper = new FakeServiceHelper(trailersInterceptor, mockService);

    BigtableDataSettings settings =
        BigtableDataSettings.newBuilderForEmulator(serviceHelper.getPort())
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();
    EnhancedBigtableStubSettings.Builder stubSettingsBuilder =
        settings.getStubSettings().toBuilder();
    stubSettingsBuilder
        .mutateRowSettings()
        .retrySettings()
        .setInitialRetryDelay(Duration.ofMillis(200));
    EnhancedBigtableStubSettings stubSettings =
        EnhancedBigtableStub.finalizeSettings(
            stubSettingsBuilder.build(),
            Tags.getTagger(),
            clientStats.getStatsRecorder(),
            builtinStats.getStatsRecorder());
    stub = new EnhancedBigtableStub(stubSettings, ClientContext.create(stubSettings));
    RpcViews.registerBigtableClientViews(clientStats.getViewManager());
    RpcViews.registerBigtableClientGfeViews(clientStats.getViewManager());
    BuiltinViews.registerBigtableBuiltinViews(builtinStats.getViewManager());
    serviceHelper.start();
  }

  @After
  public void tearDown() {
    stub.close();
    serviceHelper.shutdown();
  }

  @Test
  public void testOperationLatencies() throws InterruptedException {
    Stopwatch stopwatch = Stopwatch.createStarted();
    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)).iterator());
    long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

    // Give OpenCensus a chance to update the views asynchronously.
    Thread.sleep(100);

    // Verify builtin metrics and client metrics are logged separately
    long clientOpLatency =
        StatsTestUtils.getAggregationValueAsLong(
            clientStats,
            RpcViewConstants.BIGTABLE_OP_LATENCY_VIEW,
            ImmutableMap.of(
                RpcMeasureConstants.BIGTABLE_OP,
                    io.opencensus.tags.TagValue.create("Bigtable.ReadRows"),
                RpcMeasureConstants.BIGTABLE_STATUS, io.opencensus.tags.TagValue.create("OK")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    assertThat(clientOpLatency).isIn(Range.closed(SERVER_LATENCY, elapsed));

    long builtinOpLatency =
        StatsTestUtils.getAggregationValueAsLong(
            builtinStats,
            BuiltinViewConstants.OPERATION_LATENCIES_VIEW,
            ImmutableMap.of(
                BuiltinMeasureConstants.METHOD,
                    com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                        "Bigtable.ReadRows"),
                BuiltinMeasureConstants.STATUS,
                    com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create("OK"),
                BuiltinMeasureConstants.TABLE,
                    com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                        TABLE_ID),
                BuiltinMeasureConstants.ZONE,
                    com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(ZONE),
                BuiltinMeasureConstants.CLUSTER,
                    com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                        CLUSTER),
                BuiltinMeasureConstants.CLIENT_NAME,
                    com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                        "bigtable-java"),
                BuiltinMeasureConstants.STREAMING,
                    com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                        "true")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);

    assertThat(builtinOpLatency).isIn(Range.closed(SERVER_LATENCY, elapsed));
  }

  @Test
  public void testGfeMetrics() throws Exception {
    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)));

    // Give OpenCensus a chance to update the views asynchronously.
    Thread.sleep(100);

    // Verify builtin metrics and client metrics are logged separately
    long gfeLatency =
        StatsTestUtils.getAggregationValueAsLong(
            clientStats,
            RpcViewConstants.BIGTABLE_GFE_LATENCY_VIEW,
            ImmutableMap.of(
                RpcMeasureConstants.BIGTABLE_OP,
                io.opencensus.tags.TagValue.create("Bigtable.ReadRows"),
                RpcMeasureConstants.BIGTABLE_STATUS,
                io.opencensus.tags.TagValue.create("OK")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    assertThat(gfeLatency).isEqualTo(FAKE_SERVER_TIMING);

    long builtinGfeLatency =
        StatsTestUtils.getAggregationValueAsLong(
            builtinStats,
            BuiltinViewConstants.SERVER_LATENCIES_VIEW,
            ImmutableMap.of(
                BuiltinMeasureConstants.METHOD,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                    "Bigtable.ReadRows"),
                BuiltinMeasureConstants.STATUS,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create("OK"),
                BuiltinMeasureConstants.TABLE,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(TABLE_ID),
                BuiltinMeasureConstants.ZONE,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(ZONE),
                BuiltinMeasureConstants.CLUSTER,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(CLUSTER),
                BuiltinMeasureConstants.CLIENT_NAME,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                    "bigtable-java")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    assertThat(builtinGfeLatency).isEqualTo(FAKE_SERVER_TIMING);

    long gfeMissingHeader =
        StatsTestUtils.getAggregationValueAsLong(
            clientStats,
            RpcViewConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT_VIEW,
            ImmutableMap.of(
                RpcMeasureConstants.BIGTABLE_OP,
                io.opencensus.tags.TagValue.create("Bigtable.ReadRows"),
                RpcMeasureConstants.BIGTABLE_STATUS,
                io.opencensus.tags.TagValue.create("OK")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    assertThat(gfeMissingHeader).isEqualTo(0);

    long builtinMissingHeader =
        StatsTestUtils.getAggregationValueAsLong(
            builtinStats,
            BuiltinViewConstants.CONNECTIVITY_ERROR_COUNT_VIEW,
            ImmutableMap.of(
                BuiltinMeasureConstants.METHOD,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                    "Bigtable.ReadRows"),
                BuiltinMeasureConstants.STATUS,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create("OK"),
                BuiltinMeasureConstants.TABLE,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(TABLE_ID),
                BuiltinMeasureConstants.ZONE,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(ZONE),
                BuiltinMeasureConstants.CLUSTER,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(CLUSTER),
                BuiltinMeasureConstants.CLIENT_NAME,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                    "bigtable-java")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    assertThat(builtinMissingHeader).isEqualTo(0);
  }

  @Test
  public void testReadRowsApplicationLatency() throws Exception {
    final long applicationLatency = 1000;
    final SettableApiFuture future = SettableApiFuture.create();
    final AtomicInteger counter = new AtomicInteger(0);
    // We want to measure how long application waited before requesting another message after
    // the previous message is returned from the server. Using ResponseObserver here so that the
    // flow will be
    // onResponse() -> sleep -> onRequest() (for the next message) which is exactly what we want to
    // measure for
    // application latency.
    // If we do readRowsCallable().call(Query.create(TABLE_ID)).iterator() and iterate through the
    // iterator and sleep in
    // between responses, when the call started, the client will pre-fetch the first response, which
    // won't be counted
    // in application latency. So the test will be flaky and hard to debug.
    stub.readRowsCallable()
        .call(
            Query.create(TABLE_ID),
            new ResponseObserver<Row>() {
              @Override
              public void onStart(StreamController streamController) {}

              @Override
              public void onResponse(Row row) {
                try {
                  counter.incrementAndGet();
                  Thread.sleep(applicationLatency);
                } catch (InterruptedException e) {
                }
              }

              @Override
              public void onError(Throwable throwable) {
                future.setException(throwable);
              }

              @Override
              public void onComplete() {
                future.set(null);
              }
            });
    future.get();

    Thread.sleep(500);

    long latency =
        StatsTestUtils.getAggregationValueAsLong(
            builtinStats,
            BuiltinViewConstants.APPLICATION_LATENCIES_VIEW,
            ImmutableMap.of(
                BuiltinMeasureConstants.METHOD,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                    "Bigtable.ReadRows"),
                BuiltinMeasureConstants.STATUS,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create("OK"),
                BuiltinMeasureConstants.TABLE,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(TABLE_ID),
                BuiltinMeasureConstants.ZONE,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(ZONE),
                BuiltinMeasureConstants.CLUSTER,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(CLUSTER),
                BuiltinMeasureConstants.CLIENT_NAME,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                    "bigtable-java"),
                BuiltinMeasureConstants.STREAMING,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create("true")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);

    assertThat(counter.get()).isGreaterThan(0);
    assertThat(latency).isAtLeast(applicationLatency * counter.get());
    assertThat(latency).isLessThan(applicationLatency * (counter.get() + 1));
  }

  @Test
  public void testMutateRowApplicationLatency() throws Exception {
    // Unary callable application latency is the delay between retries
    stub.mutateRowCallable()
        .call(RowMutation.create(TABLE_ID, "random-row").setCell("cf", "q", "value"));

    Thread.sleep(500);

    long latency =
        StatsTestUtils.getAggregationValueAsLong(
            builtinStats,
            BuiltinViewConstants.APPLICATION_LATENCIES_VIEW,
            ImmutableMap.of(
                BuiltinMeasureConstants.METHOD,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                    "Bigtable.MutateRow"),
                BuiltinMeasureConstants.STATUS,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create("OK"),
                BuiltinMeasureConstants.TABLE,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(TABLE_ID),
                BuiltinMeasureConstants.ZONE,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(ZONE),
                BuiltinMeasureConstants.CLUSTER,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(CLUSTER),
                BuiltinMeasureConstants.CLIENT_NAME,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                    "bigtable-java")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);

    // Application latency should be slightly less than the total delay between 2 requests observed
    // from the server side. To make
    // the test less flaky comparing with half of the server side delay here.
    assertThat(latency).isAtLeast(serverTotalRetryDelay.get() / 2);
    assertThat(latency).isAtMost(serverTotalRetryDelay.get());
  }

  private class FakeService extends BigtableGrpc.BigtableImplBase {

    @Override
    public void readRows(
        ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
      try {
        Thread.sleep(SERVER_LATENCY);
      } catch (InterruptedException e) {
      }
      responseObserver.onNext(READ_ROWS_RESPONSE_1);
      responseObserver.onNext(READ_ROWS_RESPONSE_2);
      responseObserver.onNext(READ_ROWS_RESPONSE_3);
      responseObserver.onCompleted();
    }

    @Override
    public void mutateRow(
        MutateRowRequest request, StreamObserver<MutateRowResponse> responseObserver) {
      if (serverRetryDelayStopwatch.isRunning()) {
        serverTotalRetryDelay.addAndGet(serverRetryDelayStopwatch.elapsed(TimeUnit.MILLISECONDS));
        serverRetryDelayStopwatch.reset();
      }
      if (rpcCount.get() < 2) {
        responseObserver.onError(new StatusRuntimeException(Status.UNAVAILABLE));
        rpcCount.getAndIncrement();
        serverRetryDelayStopwatch.start();
        return;
      }
      responseObserver.onNext(MutateRowResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }
  }
}
