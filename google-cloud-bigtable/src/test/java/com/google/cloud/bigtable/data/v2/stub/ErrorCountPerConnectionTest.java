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

import static com.google.cloud.bigtable.data.v2.MetadataSubject.assertThat;
import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.core.FixedExecutorProvider;
import com.google.api.gax.rpc.ServerStream;
import com.google.bigtable.v2.*;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.*;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.stats.StatsRecorderWrapper;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@RunWith(JUnit4.class)
public class ErrorCountPerConnectionTest {
  private Server server;
  private final FakeService fakeService = new FakeService();
  private BigtableDataSettings.Builder settings;
  private BigtableDataClient client;
  private EnhancedBigtableStub stub;
  private ArgumentCaptor<Runnable> runnableCaptor;
  private StatsRecorderWrapper statsRecorderWrapper;
  private final List<Metadata> serverMetadata = new ArrayList<>();

  private final Set<String> methods = new HashSet<>();

  @Before
  public void setup() throws Exception {
//    ServerInterceptor serverInterceptor =
//        new ServerInterceptor() {
//          @Override
//          public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
//              ServerCall<ReqT, RespT> serverCall,
//              Metadata metadata,
//              ServerCallHandler<ReqT, RespT> serverCallHandler) {
//            serverMetadata.add(metadata);
//            //            if (metadata.containsKey(ROUTING_COOKIE_1)) {
//            //              methods.add(serverCall.getMethodDescriptor().getBareMethodName());
//            //            }
//            return serverCallHandler.startCall(
//                new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
//                  @Override
//                  public void sendHeaders(Metadata responseHeaders) {
//                    //                    responseHeaders.put(ROUTING_COOKIE_HEADER,
//                    // testHeaderCookie);
//                    //                    responseHeaders.put(ROUTING_COOKIE_1,
//                    // routingCookie1Header);
//                    super.sendHeaders(responseHeaders);
//                  }
//                },
//                metadata);
//          }
//        };

//    server = FakeServiceBuilder.create(fakeService).intercept(serverInterceptor).start();
    server = FakeServiceBuilder.create(fakeService).start();

//    new
    System.out.println("reza start");
    ScheduledExecutorService executors = Mockito.mock(ScheduledExecutorService.class);
    EnhancedBigtableStubSettings.Builder builder =
            BigtableDataSettings.newBuilderForEmulator(server.getPort()).stubSettings()
//                    NOTE: when replacing this line with the one below, the stub creation hangs.
//                    .setBackgroundExecutorProvider(FixedExecutorProvider.create(executors))
                    .setMyExecutorProvider(FixedExecutorProvider.create(executors))
                    .setProjectId("fake-project")
                    .setInstanceId("fake-instance");
    runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    System.out.println("rez1 " + executors + " 2 = " + builder);
    Mockito.when(executors.scheduleAtFixedRate(runnableCaptor.capture(), anyLong(), anyLong(), any())).thenReturn(null);
    stub = EnhancedBigtableStub.create(builder.build());

    statsRecorderWrapper = Mockito.mock(StatsRecorderWrapper.class);
    List<Runnable> runnables = runnableCaptor.getAllValues();
    for (Runnable runnable : runnables) {
      if (runnable instanceof CountErrorsPerInterceptorTask) {
        System.out.println("REZA iterating over runnable.");
        ((CountErrorsPerInterceptorTask) runnable).setStatsRecorderWrapper(statsRecorderWrapper);
      }
    }
//    end new

//    BigtableDataSettings.Builder settings =
//        BigtableDataSettings.newBuilderForEmulator(server.getPort())
//            .setProjectId("fake-project")
//            .setInstanceId("fake-instance");
//
//    this.settings = settings;
//
//    client = BigtableDataClient.create(settings.build());
  }

  @After
  public void tearDown() throws Exception {
//    if (client != null) {
//      client.close();
//    }
    if (server != null) {
      server.shutdown();
    }
  }

  @Test
  public void testReadRows() {
    System.out.println("rezaar");
    Query query = Query.create("fake-table");

    ServerStream<Row> responses = stub.readRowsCallable().call(query);
    for (Row row : responses) {
      System.out.println("row = " + row);
    }
//    System.out.println("reza hasNext = " + stub.readRowsCallable().call(query).iterator().hasNext());
    ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(long.class);
    statsRecorderWrapper.putAndRecordPerConnectionErrorCount(longCaptor.capture());
    Mockito.doNothing().when(statsRecorderWrapper).putAndRecordPerConnectionErrorCount(longCaptor.capture());
    for (Runnable runnable : runnableCaptor.getAllValues()) {
      if (runnable instanceof CountErrorsPerInterceptorTask) {
        System.out.println("REZA iterating over runnable Second time.");
        runnable.run();
      }
    }
    for (long longVal : longCaptor.getAllValues()) {
      System.out.println("REZA got long = " + longVal);
    }

//    client.readRows(Query.create("fake-table")).iterator().hasNext();
//
//    assertThat(fakeService.count.get()).isGreaterThan(1);
//    assertThat(serverMetadata).hasSize(fakeService.count.get());
//
//    Metadata lastMetadata = serverMetadata.get(fakeService.count.get() - 1);
//
//    //    assertThat(lastMetadata)
//    //        .containsAtLeast(
//    //            ROUTING_COOKIE_1.name(),
//    //            "readRows",
//    //            ROUTING_COOKIE_2.name(),
//    //            testCookie,
//    //            ROUTING_COOKIE_HEADER.name(),
//    //            testHeaderCookie);
//    //    assertThat(lastMetadata).doesNotContainKeys(BAD_KEY.name());
//
//    serverMetadata.clear();
  }

  static class FakeService extends BigtableGrpc.BigtableImplBase {

    private boolean returnCookie = true;
    private final AtomicInteger count = new AtomicInteger();

    @Override
    public void readRows(
        ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
      if (count.getAndIncrement() > 3) {
        System.out.println("reza in server = " + count.get());
        Metadata trailers = new Metadata();
        //        maybePopulateCookie(trailers, "readRows");
        responseObserver.onNext(ReadRowsResponse.getDefaultInstance());
        StatusRuntimeException exception = new StatusRuntimeException(Status.UNAVAILABLE, trailers);
        responseObserver.onError(exception);
//        responseObserver.onCompleted();
        return;
      }
      System.out.println("reza in server = " + count.get());
      responseObserver.onNext(ReadRowsResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }
  }
}
