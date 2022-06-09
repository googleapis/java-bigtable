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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.cloud.bigtable.stats.StatsRecorderWrapper;
import com.google.cloud.bigtable.stats.StatsWrapper;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.StringValue;
import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.threeten.bp.Duration;

public class BuiltinMetricsTracerTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";
  private static final String UNDEFINED = "undefined";
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

  private BigtableGrpc.BigtableImplBase mockService;
  private Server server;

  private EnhancedBigtableStub stub;

  @Mock private StatsRecorderWrapper statsRecorderWrapper;

  @Captor private ArgumentCaptor<Long> longValue;
  @Captor private ArgumentCaptor<Integer> intValue;
  @Captor private ArgumentCaptor<String> status;
  @Captor private ArgumentCaptor<String> tableId;
  @Captor private ArgumentCaptor<String> zone;
  @Captor private ArgumentCaptor<String> cluster;

  @Before
  public void setUp() throws Exception {
    mockService = new FakeService();

    // Add an interceptor to add server-timing in headers
    ServerInterceptor trailersInterceptor =
        new ServerInterceptor() {
          private AtomicInteger count = new AtomicInteger(0);

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
                },
                metadata);
          }
        };

    server = FakeServiceBuilder.create(mockService).intercept(trailersInterceptor).start();

    BigtableDataSettings settings =
        BigtableDataSettings.newBuilderForEmulator(server.getPort())
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
    stubSettingsBuilder.setTracerFactory(
        BuiltinMetricsTracerFactory.createWithRecorder(
            StatsWrapper.create(), ImmutableMap.of(), statsRecorderWrapper));

    EnhancedBigtableStubSettings stubSettings = stubSettingsBuilder.build();
    stub = new EnhancedBigtableStub(stubSettings, ClientContext.create(stubSettings));
  }

  @After
  public void tearDown() {
    stub.close();
    server.shutdown();
  }

  @Test
  public void testOperationLatencies() {
    Stopwatch stopwatch = Stopwatch.createStarted();
    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)).iterator());
    long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

    verify(statsRecorderWrapper).putOperationLatencies(longValue.capture());

    assertThat(longValue.getValue()).isIn(Range.closed(SERVER_LATENCY, elapsed));
  }

  @Test
  public void testGfeMetrics() {
    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE_ID)));

    verify(statsRecorderWrapper).putGfeLatencies(longValue.capture());
    assertThat(longValue.getValue()).isEqualTo(FAKE_SERVER_TIMING);

    verify(statsRecorderWrapper).putGfeMissingHeaders(longValue.capture());
    assertThat(longValue.getValue()).isEqualTo(0);
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

    verify(statsRecorderWrapper).putApplicationLatencies(longValue.capture());

    assertThat(counter.get()).isGreaterThan(0);
    assertThat(longValue.getValue()).isAtLeast(applicationLatency * counter.get());
    assertThat(longValue.getValue()).isLessThan(applicationLatency * (counter.get() + 1));
  }

  @Test
  public void testRetryCount() {
    stub.mutateRowCallable()
        .call(RowMutation.create(TABLE_ID, "random-row").setCell("cf", "q", "value"));

    verify(statsRecorderWrapper).putRetryCount(intValue.capture());

    assertThat(intValue.getValue()).isEqualTo(3);
  }

  @Test
  public void testMutateRowAttempts() {
    stub.mutateRowCallable()
        .call(RowMutation.create(TABLE_ID, "random-row").setCell("cf", "q", "value"));

    // record will get called 4 times, 3 times for attempts and 1 for recording operation level
    // metrics.
    verify(statsRecorderWrapper, times(4))
        .record(status.capture(), tableId.capture(), zone.capture(), cluster.capture());
    assertThat(zone.getAllValues()).containsExactly(UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED);
    assertThat(cluster.getAllValues()).containsExactly(UNDEFINED, UNDEFINED, UNDEFINED, UNDEFINED);
    assertThat(status.getAllValues()).containsExactly("UNAVAILABLE", "UNAVAILABLE", "OK", "OK");
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
      if (rpcCount.get() < 2) {
        responseObserver.onError(new StatusRuntimeException(Status.UNAVAILABLE));
        rpcCount.getAndIncrement();
        return;
      }
      responseObserver.onNext(MutateRowResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }
  }
}
