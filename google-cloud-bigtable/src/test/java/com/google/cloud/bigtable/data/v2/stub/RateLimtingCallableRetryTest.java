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

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.DeadlineExceededException;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.api.gax.rpc.UnavailableException;
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
import com.google.cloud.bigtable.data.v2.stub.RateLimitingStats;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class RateLimtingCallableRetryTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";

  @Rule
  public final MockitoRule mockitoRule = MockitoJUnit.rule();
  private final FakeService FakeServiceDeadline = new FakeService(7000);
  private final FakeService FakeServiceUnavailable = new FakeService(1000);
  private Server unavailableServerRetry;
  private Server deadlineServerRetry;
  private EnhancedBigtableStub unavailableStubRetry;
  private EnhancedBigtableStub deadlineCpuStubRetry;

  private ApiCallContext callContext;

  @Captor
  private ArgumentCaptor<Double> rate;

  @Mock
  RateLimitingStats mockLimitingStats;

  @Before
  public void setUp() throws Exception {
    callContext = GrpcCallContext.createDefault();
    when(mockLimitingStats.getLastQpsUpdateTime()).thenReturn(100L);

    FakeServiceDeadline.exceptions.add(new DeadlineExceededException(
        new StatusRuntimeException(Status.DEADLINE_EXCEEDED.withDescription(
            "DEADLINE_EXCEEDED: HTTP/2 error code: DEADLINE_EXCEEDED")),
        GrpcStatusCode.of(Status.Code.DEADLINE_EXCEEDED),
        true));
    FakeServiceUnavailable.exceptions.add(new UnavailableException(
        new StatusRuntimeException(Status.UNAVAILABLE.withDescription(
            "UNAVAILABLE: HTTP/2 error code: UNAVAILABLE")),
        GrpcStatusCode.of(Code.UNAVAILABLE),
        true));

    deadlineServerRetry = FakeServiceBuilder.create(FakeServiceDeadline).start();
    unavailableServerRetry = FakeServiceBuilder.create(FakeServiceUnavailable).start();

    BigtableDataSettings unavailableCPUSettings =
        BigtableDataSettings.newBuilderForEmulator(unavailableServerRetry.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    BigtableDataSettings deadlineCPUSettings =
        BigtableDataSettings.newBuilderForEmulator(deadlineServerRetry.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    unavailableStubRetry = new EnhancedBigtableStub(unavailableCPUSettings.getStubSettings(), ClientContext.create(unavailableCPUSettings.getStubSettings()), mockLimitingStats);
    deadlineCpuStubRetry = new EnhancedBigtableStub(deadlineCPUSettings.getStubSettings(), ClientContext.create(deadlineCPUSettings.getStubSettings()), mockLimitingStats);
  }

  @After
  public void tearDown() {
    unavailableStubRetry.close();
    deadlineCpuStubRetry.close();
  }

  @Test
  public void testBulkMutateRowsRetryWithUnavailable()
      throws ExecutionException, InterruptedException {

    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future =
        unavailableStubRetry.bulkMutateRowsCallable().futureCall(mutations, callContext);
    future.get();

    Mockito.verify(mockLimitingStats, Mockito.times(2)).updateQps(rate.capture());
    Assert.assertEquals((Double)9100.0, rate.getValue());
  }

  @Test
  public void testBulkMutateRowsRetryWithDeadlineExceeded()
      throws ExecutionException, InterruptedException {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future =
        deadlineCpuStubRetry.bulkMutateRowsCallable().futureCall(mutations, callContext);

    future.get();

    Mockito.verify(mockLimitingStats, Mockito.times(2)).updateQps(rate.capture());
    Assert.assertEquals((Double)4900.0, rate.getValue());
  }

  private class FakeService extends BigtableGrpc.BigtableImplBase {
    Queue<Exception> exceptions = new ArrayDeque<>();
    int CPU;

    FakeService(int recentMilliGCU) {
      this.CPU = recentMilliGCU;
    }

    MutateRowsResponse createFakeMutateRowsResponse() {
      ServerStats serverStats;

      serverStats = ServerStats.newBuilder().addCpuStats(ServerCPUStats.newBuilder()
              .setMilligcuLimit(8000)
              .setRecentGcuMillisecondsPerSecond(CPU))
          .build();

      MutateRowsResponse response = MutateRowsResponse.newBuilder().setServerStats(serverStats)
          .build();
      return response;
    }

    @Override
    public void mutateRows(
        MutateRowsRequest request, StreamObserver<MutateRowsResponse> responseObserver) {
      if (exceptions.isEmpty()) {
        responseObserver.onNext(createFakeMutateRowsResponse());
        responseObserver.onCompleted();
      } else {
        Exception expectedRpc = exceptions.poll();
        responseObserver.onError(expectedRpc);
      }
    }
  }
}
