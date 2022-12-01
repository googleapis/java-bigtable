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
package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.ApiFuture;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.google.api.core.ApiFutures;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ServerStats;
import com.google.bigtable.v2.ServerStats.ServerCPUStats;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.cloud.bigtable.data.v2.stub.RateLimitingStats;
import com.google.common.collect.Lists;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class RateLimitingCallableTest {

  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";

  private static final String FAKE_LOW_CPU_VALUES = "40.1,10.1,36.2";
  private static final String FAKE_HIGH_CPU_VALUES = "90.1,80.1,76.2";

  @Rule
  public final MockitoRule mockitoRule = MockitoJUnit.rule();

  private FakeService highCpuFakeService;
  private FakeService lowCpuFakeService;
  private Server lowCPUServer;
  private Server highCPUServer;
  private EnhancedBigtableStub lowCpuStub;
  private EnhancedBigtableStub highCpuStub;
  private ApiCallContext callContext;

  @Captor private ArgumentCaptor<Double> rate;

  @Mock
  RateLimitingStats mockLimitingStats;


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    highCpuFakeService = new FakeService(true);
    lowCpuFakeService = new FakeService(false);

    when(mockLimitingStats.getLastQpsUpdateTime()).thenReturn(10_000L);
    when(mockLimitingStats.getLowerQpsBound()).thenReturn(.00001);
    when(mockLimitingStats.getUpperQpsBound()).thenReturn(100000.0);

    rate = ArgumentCaptor.forClass(Double.class);
    callContext = GrpcCallContext.createDefault();

    //ServerInterceptor lowCPUInterceptor = cpuReturningIntercepter(FAKE_LOW_CPU_VALUES);
    //ServerInterceptor highCPUInterceptor = cpuReturningIntercepter(FAKE_HIGH_CPU_VALUES);

    lowCPUServer = FakeServiceBuilder.create(lowCpuFakeService).start();
    highCPUServer = FakeServiceBuilder.create(highCpuFakeService).start();

    BigtableDataSettings lowCPUSettings =
        BigtableDataSettings.newBuilderForEmulator(lowCPUServer.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    BigtableDataSettings highCPUSettings =
        BigtableDataSettings.newBuilderForEmulator(highCPUServer.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    // Fix naming
    EnhancedBigtableStubSettings lowCPUStubSettings = lowCPUSettings.getStubSettings();
    EnhancedBigtableStubSettings highCPUStubSettings = highCPUSettings.getStubSettings();
    lowCpuStub = new EnhancedBigtableStub(lowCPUStubSettings, ClientContext.create(lowCPUStubSettings), mockLimitingStats);
    highCpuStub = new EnhancedBigtableStub(highCPUStubSettings, ClientContext.create(highCPUStubSettings), mockLimitingStats);
  }

  @After
  public void tearDown() {
    lowCpuStub.close();
    lowCPUServer.shutdown();
    highCpuStub.close();
    highCPUServer.shutdown();
  }

  @Test
  public void testBulkMutateRowsWithNoChangeInRateLimiting()
      throws ExecutionException, InterruptedException {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    // Going to be changing the request
    // Need to get request submitted and pass in value through request
    MutateRowsRequest request =
        MutateRowsRequest.newBuilder().addEntries(MutateRowsRequest.Entry.getDefaultInstance()).build();

    ApiFuture<Void> future =
        lowCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);

    future.get();

    verify(mockLimitingStats, times(1)).updateQps(rate.capture());
    Assert.assertEquals((Double)10000.0, rate.getValue());
  }

  @Test
  public void testBulkMutateRowsWithIncreaseInRateLimiting()
      throws ExecutionException, InterruptedException {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future =
        highCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);

    future.get();

    verify(mockLimitingStats, times(1)).updateQps(rate.capture());
    Assert.assertEquals((Double)0.1, rate.getValue()); // Make default value
  }

  // Add bound tests (upper and lower)
  // Add several bulk Mutation requests

  private class FakeService extends BigtableGrpc.BigtableImplBase {
    boolean highCPU;

    // I need to add a constructor here to seperate the MutateRowsResponse from returning large or low cpu values
    FakeService(boolean highCPU) {
      this.highCPU = highCPU;
    }

    MutateRowsResponse createFakeMutateRowsResponse() {
      List<MutateRowsResponse> responses = new ArrayList<>();
      ServerStats serverStats;

      if (this.highCPU) {
        serverStats = ServerStats.newBuilder().addCpuStats(ServerCPUStats.newBuilder()
                .setMilligcuLimit(8000)
                .setRecentGcuMillisecondsPerSecond(7000))
            .build(); // I need to talk to Weihan about how the CpuStats correlate with each TS?
      } else {
        serverStats = ServerStats.newBuilder().addCpuStats(ServerCPUStats.newBuilder()
                .setMilligcuLimit(8000)
                .setRecentGcuMillisecondsPerSecond(1000))
            .build();
      }

      MutateRowsResponse response = MutateRowsResponse.newBuilder().setServerStats(serverStats).build(); // Do I need to have an Entry here


      return response;
    }

    @Override
    public void mutateRows(
        MutateRowsRequest request, StreamObserver<MutateRowsResponse> responseObserver) {
      responseObserver.onNext(createFakeMutateRowsResponse());
      responseObserver.onCompleted();
    }
  }

  // Need to move this to a shared class
  /*
  private ServerInterceptor cpuReturningIntercepter(String cpuValues) {
    return new ServerInterceptor() {
      @Override
      public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
          ServerCall<ReqT, RespT> serverCall,
          Metadata metadata,
          ServerCallHandler<ReqT, RespT> serverCallHandler) {
        return serverCallHandler.startCall(
            new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
              @Override
              public void sendHeaders(Metadata headers) {
                // Set CPU values
                headers.put(Metadata.Key.of(
                        "bigtable-cpu-values", Metadata.ASCII_STRING_MARSHALLER),
                    cpuValues);

                super.sendHeaders(headers);
              }
            },
            metadata);
      }
    };
  }*/

  static class MockMutateInnerCallable
      extends UnaryCallable<MutateRowsRequest, List<MutateRowsResponse>> {
    List<MutateRowsResponse> response = Lists.newArrayList();

    MutateRowsRequest lastRequest;
    ApiCallContext lastContext;

    @Override
    public ApiFuture<List<MutateRowsResponse>> futureCall(
        MutateRowsRequest request, ApiCallContext context) {
      lastRequest = request;
      lastContext = context;

      return ApiFutures.immediateFuture(response);
    }
  }
}
