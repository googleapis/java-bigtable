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

import com.google.api.core.ApiFuture;

import static org.mockito.Mockito.timeout;
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
import java.io.IOException;
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

  private static final int GCULimit = 8000;

  @Rule
  public final MockitoRule mockitoRule = MockitoJUnit.rule();

  private EnhancedBigtableStub inRangeCpuStub;
  private EnhancedBigtableStub lowCpuStub;
  private EnhancedBigtableStub highCpuStub;
  private ApiCallContext callContext;

  @Captor private ArgumentCaptor<Double> rate;

  @Mock
  RateLimitingStats mockLimitingStats;


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(mockLimitingStats.getLastQpsUpdateTime()).thenReturn(10_000L);

    rate = ArgumentCaptor.forClass(Double.class);
    callContext = GrpcCallContext.createDefault();

    inRangeCpuStub = createStubWithCPU(new FakeService(GCULimit * .7));
    lowCpuStub = createStubWithCPU(new FakeService(GCULimit * .1));
    highCpuStub = createStubWithCPU(new FakeService(GCULimit * .9));
  }

  @After
  public void tearDown() {
    inRangeCpuStub.close();
    lowCpuStub.close();
    highCpuStub.close();
  }

  @Test
  public void testBulkMutateRowsWithNoChangeInRate()
      throws ExecutionException, InterruptedException {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future =
        inRangeCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);

    future.get();

    verify(mockLimitingStats, times(1)).updateQps(rate.capture());
    Assert.assertEquals((Double)10000.0, rate.getValue());
  }

  @Test
  public void testBulkMutateRowsWithGradualIncreaseInRate()
      throws ExecutionException, InterruptedException {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future =
        lowCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);

    future.get();

    verify(mockLimitingStats, times(1)).updateQps(rate.capture());
    Assert.assertEquals((Double)13000.0, rate.getValue());
  }

  @Test
  public void testBulkMutateRowsWithDecreaseInRate()
      throws ExecutionException, InterruptedException {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future =
        highCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);

    future.get();

    verify(mockLimitingStats, times(1)).updateQps(rate.capture());
    Assert.assertEquals((Double)7000.0, rate.getValue());
  }

  @Test
  public void testBulkMutateRowsUpperBound() throws ExecutionException, InterruptedException {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future = lowCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);
    for (int i = 0; i < 40; i++) {
      future =
          lowCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);
    }

    future.get();

    verify(mockLimitingStats, timeout(1000).times(41)).updateQps(rate.capture());
    Assert.assertEquals((Double)100_000.0, rate.getValue());
  }

  @Test
  public void testBulkMutateRowsLowerBound() throws ExecutionException, InterruptedException {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future = highCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);
    for (int i = 0; i < 40; i++) {
      future =
          highCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);
    }

    future.get();

    verify(mockLimitingStats, timeout(1000).times(41)).updateQps(rate.capture());
    Assert.assertEquals((Double)1.0, rate.getValue());
  }

  @Test
  public void testBulkMutateRowsTimePassesBetweenQpsUpdates()
      throws ExecutionException, InterruptedException {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    when(mockLimitingStats.getLastQpsUpdateTime()).thenReturn(10_000L).thenReturn(System.currentTimeMillis());

    ApiFuture<Void> future = inRangeCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);
    future.get();
    future = inRangeCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);
    future.get();

    // updateQps() should only be called once
    verify(mockLimitingStats, timeout(1000).times(1)).updateQps(rate.capture());
    Assert.assertEquals((Double)10_000.0, rate.getValue());
  }

  public EnhancedBigtableStub createStubWithCPU(FakeService service) throws IOException {
    Server server = FakeServiceBuilder.create(service).start();

    BigtableDataSettings dataSettings =
        BigtableDataSettings.newBuilderForEmulator(server.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    return new EnhancedBigtableStub(dataSettings.getStubSettings(), ClientContext.create(dataSettings.getStubSettings()), mockLimitingStats);
  }

  private class FakeService extends BigtableGrpc.BigtableImplBase {
    int recentMilliGCU;

    FakeService(double recentMilliGCU) {
      this.recentMilliGCU = (int) recentMilliGCU;
    }

    MutateRowsResponse createFakeMutateRowsResponse() {
      ServerStats serverStats;

      serverStats = ServerStats.newBuilder().addCpuStats(ServerCPUStats.newBuilder()
              .setMilligcuLimit(GCULimit)
              .setRecentGcuMillisecondsPerSecond(recentMilliGCU))
          .build();

      MutateRowsResponse response = MutateRowsResponse.newBuilder().setServerStats(serverStats).build();
      return response;
    }

    @Override
    public void mutateRows(
        MutateRowsRequest request, StreamObserver<MutateRowsResponse> responseObserver) {
      responseObserver.onNext(createFakeMutateRowsResponse());
      responseObserver.onCompleted();
    }
  }
}
