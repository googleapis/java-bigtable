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

import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.SampleRowKeysRequest;
import com.google.bigtable.v2.SampleRowKeysResponse;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.common.collect.ImmutableList;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CookieHolderTest {
  private Server server;
  private FakeService fakeService = new FakeService();
  private BigtableDataClient client;
  private List<Metadata> serverMetadata = new ArrayList<>();
  private String testCookie = "test-routing-cookie";

  private Metadata.Key<String> ROUTING_COOKIE_1 =
      Metadata.Key.of("x-goog-cbt-cookie-routing", Metadata.ASCII_STRING_MARSHALLER);
  private Metadata.Key<String> ROUTING_COOKIE_2 =
      Metadata.Key.of("x-goog-cbt-cookie-random", Metadata.ASCII_STRING_MARSHALLER);
  private Metadata.Key<String> BAD_KEY =
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
            return serverCallHandler.startCall(serverCall, metadata);
          }
        };

    server = FakeServiceBuilder.create(fakeService).intercept(serverInterceptor).start();

    BigtableDataSettings.Builder settings =
        BigtableDataSettings.newBuilderForEmulator(server.getPort())
            .setProjectId("fake-project")
            .setInstanceId("fake-instance");

    InstantiatingGrpcChannelProvider channelProvider =
        (InstantiatingGrpcChannelProvider) settings.stubSettings().getTransportChannelProvider();
    // We need to add the interceptor manually for emulator grpc channel
    settings
        .stubSettings()
        .setTransportChannelProvider(
            channelProvider
                .toBuilder()
                .setInterceptorProvider(() -> ImmutableList.of(new CookieInterceptor()))
                .build());

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
        if (returnCookie) {
          trailers.put(ROUTING_COOKIE_1, "readRows");
          trailers.put(ROUTING_COOKIE_2, testCookie);
          trailers.put(BAD_KEY, "bad-key");
        }
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
        if (returnCookie) {
          trailers.put(ROUTING_COOKIE_1, "mutateRow");
          trailers.put(ROUTING_COOKIE_2, testCookie);
          trailers.put(BAD_KEY, "bad-key");
        }
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
        if (returnCookie) {
          trailers.put(ROUTING_COOKIE_1, "mutateRows");
          trailers.put(ROUTING_COOKIE_2, testCookie);
          trailers.put(BAD_KEY, "bad-key");
        }
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
        if (returnCookie) {
          trailers.put(ROUTING_COOKIE_1, "sampleRowKeys");
          trailers.put(ROUTING_COOKIE_2, testCookie);
          trailers.put(BAD_KEY, "bad-key");
        }
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(SampleRowKeysResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }
  }
}
