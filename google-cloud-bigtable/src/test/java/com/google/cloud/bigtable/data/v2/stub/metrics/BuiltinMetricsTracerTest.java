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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import com.google.api.gax.rpc.ClientContext;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceHelper;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.cloud.bigtable.data.v2.stub.metrics.builtin.BuiltinMeasureConstants;
import com.google.cloud.bigtable.data.v2.stub.metrics.builtin.BuiltinViewConstants;
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
import io.grpc.stub.StreamObserver;
import io.opencensus.impl.stats.StatsComponentImpl;
import io.opencensus.tags.Tags;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

public class BuiltinMetricsTracerTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";
  private static final String ZONE = "us-east-1";
  private static final String CLUSTER = "cluster-1";
  private static final long FAKE_SERVER_TIMING = 50;

  private static final ReadRowsResponse READ_ROWS_RESPONSE_1 =
      ReadRowsResponse.newBuilder()
          .addChunks(
              ReadRowsResponse.CellChunk.newBuilder()
                  .setRowKey(ByteString.copyFromUtf8("fake-key-0"))
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
                  .setRowKey(ByteString.copyFromUtf8("fake-key-1"))
                  .setFamilyName(StringValue.of("cf"))
                  .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                  .setTimestampMicros(1_000)
                  .setValue(ByteString.copyFromUtf8("value"))
                  .setCommitRow(true))
          .build();

  @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

  private FakeServiceHelper serviceHelper;

  @Mock(answer = Answers.CALLS_REAL_METHODS)
  private BigtableGrpc.BigtableImplBase mockService;

  private EnhancedBigtableStub stub;

  private StatsComponentImpl clientStats = new StatsComponentImpl();
  private com.google.bigtable.repackaged.io.opencensus.impl.stats.StatsComponentImpl builtinStats =
      new com.google.bigtable.repackaged.io.opencensus.impl.stats.StatsComponentImpl();

  @Before
  public void setUp() throws Exception {
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
                    trailers.put(HeaderTracer.BIGTABLE_ZONE_HEADER_KEY, ZONE);
                    trailers.put(HeaderTracer.BIGTABLE_CLUSTER_HEADER_KEY, CLUSTER);
                    super.close(status, trailers);
                  }
                },
                metadata);
          }
        };

    serviceHelper = new FakeServiceHelper(trailersInterceptor, mockService);

    RpcViews.registerBigtableClientViews(clientStats.getViewManager());

    BigtableDataSettings settings =
        BigtableDataSettings.newBuilderForEmulator(serviceHelper.getPort())
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();
    EnhancedBigtableStubSettings stubSettings =
        EnhancedBigtableStub.finalizeSettings(
            settings.getStubSettings(),
            Tags.getTagger(),
            clientStats.getStatsRecorder(),
            builtinStats.getStatsRecorder(),
            builtinStats.getViewManager());
    stub = new EnhancedBigtableStub(stubSettings, ClientContext.create(stubSettings));
    RpcViews.registerBigtableClientViews(clientStats.getViewManager());

    serviceHelper.start();
  }

  @After
  public void tearDown() {
    stub.close();
    serviceHelper.shutdown();
  }

  @Test
  public void testReadRowsMetrics() throws InterruptedException {
    final long sleepTime = 500;
    doAnswer(
            new Answer() {
              @Override
              public Object answer(InvocationOnMock invocation) throws Throwable {
                @SuppressWarnings("unchecked")
                StreamObserver<ReadRowsResponse> observer =
                    (StreamObserver<ReadRowsResponse>) invocation.getArguments()[1];
                Thread.sleep(sleepTime);
                observer.onNext(READ_ROWS_RESPONSE_1);
                observer.onNext(READ_ROWS_RESPONSE_2);
                observer.onCompleted();
                return null;
              }
            })
        .when(mockService)
        .readRows(any(ReadRowsRequest.class), any());

    Stopwatch stopwatch = Stopwatch.createStarted();
    Iterator<Row> rows = stub.readRowsCallable().call(Query.create(TABLE_ID)).iterator();
    while (rows.hasNext()) {
      rows.next();
      Thread.sleep(50);
    }
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
    assertThat(clientOpLatency).isIn(Range.closed(sleepTime, elapsed));

    long builtinOpLatency =
        StatsTestUtils.getAggregationValueAsLong(
            builtinStats,
            BuiltinViewConstants.OPERATION_LATENCIES_VIEW,
            ImmutableMap.of(
                BuiltinMeasureConstants.METHOD,
                    com.google.bigtable.repackaged.io.opencensus.tags.TagValue.create(
                        "Bigtable.ReadRows"),
                BuiltinMeasureConstants.STATUS,
                    com.google.bigtable.repackaged.io.opencensus.tags.TagValue.create("OK"),
                BuiltinMeasureConstants.TABLE,
                    com.google.bigtable.repackaged.io.opencensus.tags.TagValue.create(TABLE_ID),
                BuiltinMeasureConstants.ZONE,
                    com.google.bigtable.repackaged.io.opencensus.tags.TagValue.create(ZONE),
                BuiltinMeasureConstants.CLUSTER,
                    com.google.bigtable.repackaged.io.opencensus.tags.TagValue.create(CLUSTER),
                BuiltinMeasureConstants.CLIENT_NAME,
                    com.google.bigtable.repackaged.io.opencensus.tags.TagValue.create(
                        "java-bigtable"),
                BuiltinMeasureConstants.STREAMING,
                    com.google.bigtable.repackaged.io.opencensus.tags.TagValue.create("true")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);

    assertThat(builtinOpLatency).isIn(Range.closed(sleepTime, elapsed));
  }
}
