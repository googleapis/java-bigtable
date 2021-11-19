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
import com.google.api.gax.rpc.ClientContext;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceHelper;
import com.google.cloud.bigtable.data.v2.models.Query;
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
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BuiltinMetricsTracerTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";
  private static final String ZONE = "us-east-1";
  private static final String CLUSTER = "cluster-1";
  private static final long FAKE_SERVER_TIMING = 50;
  private static final long SERVER_LATENCY = 500;

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

  @Before
  public void setUp() throws Exception {
    mockService = new FakeService();

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
                    trailers.put(Util.ZONE_HEADER_KEY, ZONE);
                    trailers.put(Util.CLUSTER_HEADER_KEY, CLUSTER);
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
    RpcViews.registerBigtableClientGfeViews(clientStats.getViewManager());

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
                BuiltinMeasureConstants.CLIENT_NAME,
                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
                    "bigtable-java")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    assertThat(builtinMissingHeader).isEqualTo(0);
  }

  //  @Test
  //  public void testApplicationLatency() throws Exception {
  //    long applicationLatency = 1000;
  //    ServerStream<Row> rows = stub.readRowsCallable().call(Query.create(TABLE_ID));
  //    for (Row r : rows) {
  //      r.getCells();
  //      Thread.sleep(applicationLatency);
  //    }
  //
  //    long latency =
  //        StatsTestUtils.getAggregationValueAsLong(
  //            builtinStats,
  //            BuiltinViewConstants.APPLICATION_LATENCIES_VIEW,
  //            ImmutableMap.of(
  //                BuiltinMeasureConstants.METHOD,
  //                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
  //                    "Bigtable.ReadRows"),
  //                BuiltinMeasureConstants.STATUS,
  //                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create("OK"),
  //                BuiltinMeasureConstants.TABLE,
  //
  // com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(TABLE_ID),
  //                BuiltinMeasureConstants.ZONE,
  //                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(ZONE),
  //                BuiltinMeasureConstants.CLUSTER,
  //
  // com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(CLUSTER),
  //                BuiltinMeasureConstants.CLIENT_NAME,
  //                com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create(
  //                    "bigtable-java"),
  //                BuiltinMeasureConstants.STREAMING,
  //
  // com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue.create("true")),
  //            PROJECT_ID,
  //            INSTANCE_ID,
  //            APP_PROFILE_ID);

  // assertThat(latency).isAtLeast(applicationLatency);
  //  }

  private class FakeService extends BigtableGrpc.BigtableImplBase {
    @Override
    public void readRows(
        ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
      try {
        Thread.sleep(SERVER_LATENCY);
      } catch (InterruptedException e) {

      }
      responseObserver.onNext(ReadRowsResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }

    @Override
    public void mutateRows(
        MutateRowsRequest request, StreamObserver<MutateRowsResponse> responseObserver) {
      responseObserver.onNext(MutateRowsResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }
  }
}
