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
package com.google.cloud.bigtable.data.v2.stub;

import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.StatusCode;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.CheckAndMutateRowRequest;
import com.google.bigtable.v2.CheckAndMutateRowResponse;
import com.google.bigtable.v2.GenerateInitialChangeStreamPartitionsRequest;
import com.google.bigtable.v2.GenerateInitialChangeStreamPartitionsResponse;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ReadChangeStreamRequest;
import com.google.bigtable.v2.ReadChangeStreamResponse;
import com.google.bigtable.v2.ReadModifyWriteRowRequest;
import com.google.bigtable.v2.ReadModifyWriteRowResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.SampleRowKeysRequest;
import com.google.bigtable.v2.SampleRowKeysResponse;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.ConditionalRowMutation;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.ReadChangeStreamQuery;
import com.google.cloud.bigtable.data.v2.models.ReadModifyWriteRow;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Duration;

@RunWith(JUnit4.class)
public class CookiesHolderTest {
  private Server server;
  private final FakeService fakeService = new FakeService();
  private BigtableDataClient client;
  private final List<Metadata> serverMetadata = new ArrayList<>();
  private final String testCookie = "test-routing-cookie";

  private final Set<String> methods = new HashSet<>();

  private final Metadata.Key<String> ROUTING_COOKIE_1 =
      Metadata.Key.of("x-goog-cbt-cookie-routing", Metadata.ASCII_STRING_MARSHALLER);
  private final Metadata.Key<String> ROUTING_COOKIE_2 =
      Metadata.Key.of("x-goog-cbt-cookie-random", Metadata.ASCII_STRING_MARSHALLER);
  private final Metadata.Key<String> BAD_KEY =
      Metadata.Key.of("x-goog-cbt-not-cookie", Metadata.ASCII_STRING_MARSHALLER);

  @Before
  public void setup() throws Exception {
    ServerInterceptor serverInterceptor =
        new ServerInterceptor() {
          @Override
          public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
              ServerCall<ReqT, RespT> serverCall,
              Metadata metadata,
              ServerCallHandler<ReqT, RespT> serverCallHandler) {
            serverMetadata.add(metadata);
            if (metadata.containsKey(ROUTING_COOKIE_1)) {
              methods.add(serverCall.getMethodDescriptor().getBareMethodName());
            }
            return serverCallHandler.startCall(serverCall, metadata);
          }
        };

    server = FakeServiceBuilder.create(fakeService).intercept(serverInterceptor).start();

    BigtableDataSettings.Builder settings =
        BigtableDataSettings.newBuilderForEmulator(server.getPort())
            .setProjectId("fake-project")
            .setInstanceId("fake-instance");

    // Override CheckAndMutate and ReadModifyWrite retry settings here. These operations
    // are currently not retryable but this could change in the future after we
    // have routing cookie sends back meaningful information and changes how retry works.
    // Routing cookie still needs to be respected and handled by the callables.
    settings
        .stubSettings()
        .checkAndMutateRowSettings()
        .setRetrySettings(
            RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(10))
                .setMaxRetryDelay(Duration.ofMinutes(1))
                .setMaxAttempts(2)
                .build())
        .setRetryableCodes(StatusCode.Code.UNAVAILABLE);

    settings
        .stubSettings()
        .readModifyWriteRowSettings()
        .setRetrySettings(
            RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(10))
                .setMaxRetryDelay(Duration.ofMinutes(1))
                .setMaxAttempts(2)
                .build())
        .setRetryableCodes(StatusCode.Code.UNAVAILABLE);

    client = BigtableDataClient.create(settings.build());
  }

  @Test
  public void testReadRows() {
    client.readRows(Query.create("fake-table")).iterator().hasNext();

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());
    String bytes1 = serverMetadata.get(1).get(ROUTING_COOKIE_1);
    String bytes2 = serverMetadata.get(1).get(ROUTING_COOKIE_2);
    assertThat(bytes1).isNotNull();
    assertThat(bytes1).isEqualTo("readRows");
    assertThat(bytes2).isNotNull();
    assertThat(bytes2).isEqualTo(testCookie);

    // make sure bad key is not added
    assertThat(serverMetadata.get(1).get(BAD_KEY)).isNull();

    serverMetadata.clear();
  }

  @Test
  public void testReadRow() {
    client.readRow("fake-table", "key");

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());
    String bytes1 = serverMetadata.get(1).get(ROUTING_COOKIE_1);
    String bytes2 = serverMetadata.get(1).get(ROUTING_COOKIE_2);
    assertThat(bytes1).isNotNull();
    assertThat(bytes1).isEqualTo("readRows");
    assertThat(bytes2).isNotNull();
    assertThat(bytes2).isEqualTo(testCookie);

    // make sure bad key is not added
    assertThat(serverMetadata.get(1).get(BAD_KEY)).isNull();

    serverMetadata.clear();
  }

  @Test
  public void testMutateRows() {
    client.bulkMutateRows(
        BulkMutation.create("fake-table")
            .add(RowMutationEntry.create("key").setCell("cf", "q", "v")));

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());
    String bytes1 = serverMetadata.get(1).get(ROUTING_COOKIE_1);
    String bytes2 = serverMetadata.get(1).get(ROUTING_COOKIE_2);
    assertThat(bytes1).isNotNull();
    assertThat(bytes1).isEqualTo("mutateRows");
    assertThat(bytes2).isNotNull();
    assertThat(bytes2).isEqualTo(testCookie);

    // make sure bad key is not added
    assertThat(serverMetadata.get(1).get(BAD_KEY)).isNull();

    serverMetadata.clear();
  }

  @Test
  public void testMutateRow() {
    client.mutateRow(RowMutation.create("table", "key").setCell("cf", "q", "v"));

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());
    String bytes1 = serverMetadata.get(1).get(ROUTING_COOKIE_1);
    String bytes2 = serverMetadata.get(1).get(ROUTING_COOKIE_2);
    assertThat(bytes1).isNotNull();
    assertThat(bytes1).isEqualTo("mutateRow");
    assertThat(bytes2).isNotNull();
    assertThat(bytes2).isEqualTo(testCookie);

    // make sure bad key is not added
    assertThat(serverMetadata.get(1).get(BAD_KEY)).isNull();

    serverMetadata.clear();
  }

  @Test
  public void testSampleRowKeys() {

    client.sampleRowKeys("fake-table");

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());
    String bytes1 = serverMetadata.get(1).get(ROUTING_COOKIE_1);
    String bytes2 = serverMetadata.get(1).get(ROUTING_COOKIE_2);
    assertThat(bytes1).isNotNull();
    assertThat(bytes1).isEqualTo("sampleRowKeys");
    assertThat(bytes2).isNotNull();
    assertThat(bytes2).isEqualTo(testCookie);

    // make sure bad key is not added
    assertThat(serverMetadata.get(1).get(BAD_KEY)).isNull();

    serverMetadata.clear();
  }

  @Test
  public void testNoCookieSucceedReadRows() {
    fakeService.returnCookie = false;

    client.readRows(Query.create("fake-table")).iterator().hasNext();

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());

    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_1)).isNull();
    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_2)).isNull();
    serverMetadata.clear();
  }

  @Test
  public void testNoCookieSucceedReadRow() {
    fakeService.returnCookie = false;

    client.readRow("fake-table", "key");

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());

    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_1)).isNull();
    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_2)).isNull();
    serverMetadata.clear();
  }

  @Test
  public void testNoCookieSucceedMutateRows() {
    fakeService.returnCookie = false;

    client.bulkMutateRows(
        BulkMutation.create("fake-table")
            .add(RowMutationEntry.create("key").setCell("cf", "q", "v")));

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());

    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_1)).isNull();
    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_2)).isNull();

    serverMetadata.clear();
  }

  @Test
  public void testNoCookieSucceedMutateRow() {
    fakeService.returnCookie = false;

    client.mutateRow(RowMutation.create("fake-table", "key").setCell("cf", "q", "v"));

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());

    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_1)).isNull();
    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_2)).isNull();

    serverMetadata.clear();
  }

  @Test
  public void testNoCookieSucceedSampleRowKeys() {
    fakeService.returnCookie = false;

    client.sampleRowKeys("fake-table");

    assertThat(fakeService.count.get()).isGreaterThan(1);
    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());

    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_1)).isNull();
    assertThat(serverMetadata.get(1).get(ROUTING_COOKIE_2)).isNull();

    serverMetadata.clear();
  }

  @Test
  public void testAllMethodsAreCalled() throws InterruptedException {
    // This test ensures that all methods respect the retry cookie except for the ones that are
    // explicitly added to the methods list. It requires that any newly method is exercised in this
    // test.
    // This is enforced by introspecting grpc method descriptors.
    client.readRows(Query.create("fake-table")).iterator().hasNext();

    fakeService.count.set(0);
    client.mutateRow(RowMutation.create("fake-table", "key").setCell("cf", "q", "v"));

    fakeService.count.set(0);
    client.bulkMutateRows(
        BulkMutation.create("fake-table")
            .add(RowMutationEntry.create("key").setCell("cf", "q", "v")));

    fakeService.count.set(0);
    client.sampleRowKeys("fake-table");

    fakeService.count.set(0);
    client.checkAndMutateRow(
        ConditionalRowMutation.create("fake-table", "key")
            .then(Mutation.create().setCell("cf", "q", "v")));

    fakeService.count.set(0);
    client.readModifyWriteRow(
        ReadModifyWriteRow.create("fake-table", "key").append("cf", "q", "v"));

    fakeService.count.set(0);
    client.generateInitialChangeStreamPartitions("fake-table").iterator().hasNext();

    fakeService.count.set(0);
    client.readChangeStream(ReadChangeStreamQuery.create("fake-table")).iterator().hasNext();

    Set<String> expected =
        BigtableGrpc.getServiceDescriptor().getMethods().stream()
            .map(MethodDescriptor::getBareMethodName)
            .collect(Collectors.toSet());

    // Exclude methods that are not supported by routing cookie
    methods.add("PingAndWarm");

    assertThat(methods).containsExactlyElementsIn(expected);
  }

  @After
  public void tearDown() throws Exception {
    client.close();
    server.shutdown();
  }

  class FakeService extends BigtableGrpc.BigtableImplBase {

    private boolean returnCookie = true;
    private final AtomicInteger count = new AtomicInteger();

    @Override
    public void readRows(
        ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
      if (count.getAndIncrement() < 1) {
        Metadata trailers = new Metadata();
        maybePopulateCookie(trailers, "readRows");
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(ReadRowsResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }

    @Override
    public void mutateRow(
        MutateRowRequest request, StreamObserver<MutateRowResponse> responseObserver) {
      if (count.getAndIncrement() < 1) {
        Metadata trailers = new Metadata();
        maybePopulateCookie(trailers, "mutateRow");
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(MutateRowResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }

    @Override
    public void mutateRows(
        MutateRowsRequest request, StreamObserver<MutateRowsResponse> responseObserver) {
      if (count.getAndIncrement() < 1) {
        Metadata trailers = new Metadata();
        maybePopulateCookie(trailers, "mutateRows");
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(
          MutateRowsResponse.newBuilder()
              .addEntries(MutateRowsResponse.Entry.getDefaultInstance())
              .build());
      responseObserver.onCompleted();
    }

    @Override
    public void sampleRowKeys(
        SampleRowKeysRequest request, StreamObserver<SampleRowKeysResponse> responseObserver) {
      if (count.getAndIncrement() < 1) {
        Metadata trailers = new Metadata();
        maybePopulateCookie(trailers, "sampleRowKeys");
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(SampleRowKeysResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }

    @Override
    public void checkAndMutateRow(
        CheckAndMutateRowRequest request,
        StreamObserver<CheckAndMutateRowResponse> responseObserver) {
      if (count.getAndIncrement() < 1) {
        Metadata trailers = new Metadata();
        maybePopulateCookie(trailers, "checkAndMutate");
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(CheckAndMutateRowResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }

    @Override
    public void readModifyWriteRow(
        ReadModifyWriteRowRequest request,
        StreamObserver<ReadModifyWriteRowResponse> responseObserver) {
      if (count.getAndIncrement() < 1) {
        Metadata trailers = new Metadata();
        maybePopulateCookie(trailers, "readModifyWrite");
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(ReadModifyWriteRowResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }

    @Override
    public void readChangeStream(
        ReadChangeStreamRequest request,
        StreamObserver<ReadChangeStreamResponse> responseObserver) {
      if (count.getAndIncrement() < 1) {
        Metadata trailers = new Metadata();
        maybePopulateCookie(trailers, "readChangeStream");
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(
          ReadChangeStreamResponse.newBuilder()
              .setCloseStream(ReadChangeStreamResponse.CloseStream.getDefaultInstance())
              .build());
      responseObserver.onCompleted();
    }

    @Override
    public void generateInitialChangeStreamPartitions(
        GenerateInitialChangeStreamPartitionsRequest request,
        StreamObserver<GenerateInitialChangeStreamPartitionsResponse> responseObserver) {
      if (count.getAndIncrement() < 1) {
        Metadata trailers = new Metadata();
        maybePopulateCookie(trailers, "generateInitialChangeStreamPartitions");
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(GenerateInitialChangeStreamPartitionsResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }

    private void maybePopulateCookie(Metadata trailers, String label) {
      if (returnCookie) {
        trailers.put(ROUTING_COOKIE_1, label);
        trailers.put(ROUTING_COOKIE_2, testCookie);
        trailers.put(BAD_KEY, "bad-key");
      }
    }
  }
}
