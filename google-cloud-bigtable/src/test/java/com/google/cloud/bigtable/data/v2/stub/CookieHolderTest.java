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

import static com.google.cloud.bigtable.data.v2.stub.CookieInterceptor.ERROR_INFO_KEY;
import static com.google.cloud.bigtable.data.v2.stub.CookiesHolder.ROUTING_COOKIE_KEY;
import static com.google.cloud.bigtable.data.v2.stub.CookiesHolder.ROUTING_COOKIE_METADATA_KEY;
import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.common.collect.ImmutableList;
import com.google.rpc.ErrorInfo;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.nio.charset.StandardCharsets;
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
  public void testRetryCookieIsForwarded() {
    client.mutateRow(RowMutation.create("fake-table", "fake-row").setCell("cf", "q", "v"));

    assertThat(serverMetadata.size()).isEqualTo(fakeService.count.get());
    byte[] bytes = serverMetadata.get(1).get(ROUTING_COOKIE_METADATA_KEY);
    assertThat(new String(bytes, StandardCharsets.UTF_8)).isEqualTo("test-routing-cookie");

    serverMetadata.clear();
  }

  @After
  public void tearDown() throws Exception {
    client.close();
    server.shutdown();
  }

  class FakeService extends BigtableGrpc.BigtableImplBase {

    private AtomicInteger count = new AtomicInteger();

    @Override
    public void mutateRow(
        MutateRowRequest request, StreamObserver<MutateRowResponse> responseObserver) {
      if (count.getAndIncrement() < 1) {
        Metadata trailers = new Metadata();
        ErrorInfo errorInfo =
            ErrorInfo.newBuilder().putMetadata(ROUTING_COOKIE_KEY, "test-routing-cookie").build();
        trailers.put(ERROR_INFO_KEY, errorInfo);
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
        return;
      }
      responseObserver.onNext(MutateRowResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }
  }
}
