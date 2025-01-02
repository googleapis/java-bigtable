package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.ApiFuture;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.grpc.GrpcCallSettings;
import com.google.api.gax.grpc.GrpcRawCallableFactory;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.retrying.SimpleStreamResumptionStrategy;
import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStream;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.StreamController;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.TableName;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.TableId;
import com.google.cloud.bigtable.data.v2.models.TargetId;
import com.google.cloud.bigtable.data.v2.stub.readrows.ReadRowsResumptionStrategy;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.StringValue;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.Duration;
import java.util.concurrent.Executors;

public class TestRetry {

    EnhancedBigtableStub stub;
    Server server;

    @Before
    public void setup() throws Exception {
        server = ServerBuilder.forPort(1234)
                .addService(new FakeService())
                .build();

        BigtableDataSettings.Builder settings =
                BigtableDataSettings
                        .newBuilderForEmulator(1234)
                        .setProjectId("project")
                        .setInstanceId("instance");

        settings.stubSettings().mutateRowSettings()
                .setRetrySettings(RetrySettings.newBuilder()
                        .setInitialRpcTimeoutDuration(Duration.ofMillis(1000))
                        .setMaxRpcTimeoutDuration(Duration.ofMillis(1000))
                        .setTotalTimeoutDuration(Duration.ofSeconds(60))
                        .setRetryDelayMultiplier(1.5)
                        .setInitialRetryDelayDuration(Duration.ofSeconds(1))
                        .setMaxRetryDelayDuration(Duration.ofSeconds(10))
                        .build());

        settings.stubSettings().readRowsSettings()
                .setRetrySettings(RetrySettings.newBuilder()
                        .setInitialRpcTimeoutDuration(Duration.ofMillis(1000))
                        .setMaxRpcTimeoutDuration(Duration.ofMillis(1000))
                        .setTotalTimeoutDuration(Duration.ofSeconds(60))
                        .setRetryDelayMultiplier(1.5)
                        .setInitialRetryDelayDuration(Duration.ofSeconds(1))
                        .setMaxRetryDelayDuration(Duration.ofSeconds(10))
                        .build());

//        settings.stubSettings().setEnableSkipTrailers(false);

        server.start();

        stub = EnhancedBigtableStub.create(settings.stubSettings().build());
    }


    @After
    public void tearDown() {
        stub.close();
        server.shutdown();
    }

    @Test
    public void testUnary() throws Exception {

        ApiFuture<Void> future = stub.mutateRowCallable()
                .futureCall(RowMutation.create(TableId.of("table"), "row")
                        .setCell("cf", "q", "c"), null);

        future.get();
    }

    @Test
    public void testServerStream() throws Exception {
//        ServerStreamingCallable<ReadRowsRequest, ReadRowsResponse> callable = GrpcRawCallableFactory.createServerStreamingCallable(
//                GrpcCallSettings.<ReadRowsRequest, ReadRowsResponse>newBuilder()
//                        .setMethodDescriptor(BigtableGrpc.getReadRowsMethod())
//                        .setParamsExtractor(r -> ImmutableMap.of())
//                        .build(),
//                ImmutableSet.of(StatusCode.Code.DEADLINE_EXCEEDED));
//
//        ToNewCallableAdapter<ReadRowsRequest, ReadRowsResponse> toNew = new ToNewCallableAdapter<ReadRowsRequest, ReadRowsResponse>(callable);
//        RetryCallable<ReadRowsRequest, ReadRowsResponse> retry = new RetryCallable<>(toNew, new FakeResumptionStrategy(), Executors.newScheduledThreadPool(1));
//        ToOldCallableAdapter<ReadRowsRequest, ReadRowsResponse> toOld = new ToOldCallableAdapter<>(retry);
//
//
//        ReadRowsRequest request = ReadRowsRequest.newBuilder()
//                        .setTableName(TableName.of("project", "instance", "table").toString())
//                        .build();
//
//        Channel channel = ManagedChannelBuilder.forAddress("localhost", 1234).usePlaintext().build();
//
//        ServerStreamingCallable<ReadRowsRequest, ReadRowsResponse> userCallable = toOld.withDefaultCallContext(GrpcCallContext.of(channel, CallOptions.DEFAULT));
//
//        ServerStream<ReadRowsResponse> responses = userCallable.call(request);
//        for (ReadRowsResponse response : responses) {
//            System.out.println(response);
//        }
//
//        userCallable.call(request, new ResponseObserver<ReadRowsResponse>() {
//            private StreamController controller;
//            @Override
//            public void onStart(StreamController controller) {
//            }
//
//            @Override
//            public void onResponse(ReadRowsResponse response) {
//                System.out.println("Received response " + response);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println("complete");
//            }
//        });

//        Thread.sleep(50000);

//        userCallable.all().call(request);

        stub.readRowsCallable().all().call(Query.create("test"));
//        ServerStream<Row> stream = stub.readRowsCallable().call(Query.create("table"));
//
//        for (Row row : stream) {
//            System.out.println(row);
//        }
    }

    class FakeService extends BigtableGrpc.BigtableImplBase {
        int count = 0;

        @Override
        public void readRows(ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
            count++;
            if (count == 1) {
                System.out.println("attempt 1: " + request);
                responseObserver.onNext(
                        ReadRowsResponse.newBuilder()
                                .addChunks(
                                        ReadRowsResponse.CellChunk.newBuilder()
                                                .setRowKey(ByteString.copyFromUtf8("key1"))
                                                .setFamilyName(StringValue.newBuilder().setValue("cf"))
                                                .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                                                .setTimestampMicros(0)
                                                .setValue(ByteString.copyFromUtf8("value"))
                                                .setCommitRow(true))
                                .build());
                responseObserver.onError(new StatusRuntimeException(Status.DEADLINE_EXCEEDED));
            } else if (count == 2) {
                System.out.println("attempt 2: " + request);
                responseObserver.onNext(ReadRowsResponse.newBuilder()
                        .addChunks(
                                ReadRowsResponse.CellChunk.newBuilder()
                                        .setRowKey(ByteString.copyFromUtf8("key2"))
                                        .setFamilyName(StringValue.newBuilder().setValue("cf"))
                                        .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                                        .setTimestampMicros(0)
                                        .setValue(ByteString.copyFromUtf8("value"))
                                        .setCommitRow(true))
                        .build());
                responseObserver.onNext(ReadRowsResponse.newBuilder()
                        .addChunks(
                                ReadRowsResponse.CellChunk.newBuilder()
                                        .setRowKey(ByteString.copyFromUtf8("key3"))
                                        .setFamilyName(StringValue.newBuilder().setValue("cf"))
                                        .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                                        .setTimestampMicros(0)
                                        .setValue(ByteString.copyFromUtf8("value"))
                                        .setCommitRow(true))
                        .build());
                responseObserver.onError(new StatusRuntimeException(Status.DEADLINE_EXCEEDED));
            } else {
                System.out.println("attempt 3: " + request);
                responseObserver.onCompleted();
            }
        }

        @Override
        public void mutateRow(MutateRowRequest request, StreamObserver<MutateRowResponse> responseObserver) {
            System.out.println("server got mutateRow attempt " + count);
            if (count ++ < 2) {
                responseObserver.onError(new StatusRuntimeException(Status.UNAVAILABLE));
            } else {
                responseObserver.onNext(MutateRowResponse.newBuilder().build());
                responseObserver.onCompleted();
            }
        }
    }

    class FakeResumptionStrategy implements StreamResumptionStrategy {
        @Nonnull
        @Override
        public StreamResumptionStrategy createNew() {
            return null;
        }

        @Nonnull
        @Override
        public Object processResponse(Object response) {
            return null;
        }

        @Nullable
        @Override
        public Object getResumeRequest(Object originalRequest) {
            return originalRequest;
        }

        @Override
        public boolean canResume() {
            return true;
        }
    }
}
