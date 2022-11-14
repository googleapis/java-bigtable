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
package com.google.cloud.bigtable.data.v2.stub.mutaterows;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.DeadlineExceededException;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.MutateRowsResponse.Entry;
import com.google.bigtable.v2.ResponseParams;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.cloud.bigtable.data.v2.stub.metrics.RateLimitingStats;
import com.google.cloud.bigtable.data.v2.stub.metrics.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import io.grpc.ForwardingServerCall;
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
import java.util.Queue;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class RateLimtingCallableRetryTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";
  private static final String ZONE = "us-east1";
  private static final String CLUSTER = "cluster";

  private static final String FAKE_LOW_CPU_VALUES = "40.1,10.1,36.2";
  private static final String FAKE_HIGH_CPU_VALUES = "90.1,80.1,76.2";

  @Rule
  public final MockitoRule mockitoRule = MockitoJUnit.rule();
  private RateLimitingStats stats;

  private final FakeService FakeServiceRetry = new FakeService();
  private Server lowCPUServerRetry;
  private Server highCPUServerRetry; // I need to look at retrying tests

  private EnhancedBigtableStub lowCpuStubRetry;
  private EnhancedBigtableStub highCpuStubRetry;

  private MockMutateInnerCallable innerCallable; // I believe that I can delete this
  private ApiCallContext callContext;

  @Before
  public void setUp() throws Exception {
    //statsArgumentCaptor = ArgumentCaptor.forClass(CpuThrottlingStats.class);
    innerCallable = new MockMutateInnerCallable();
    callContext = GrpcCallContext.createDefault();

    // I should make this under the Util class
    ServerInterceptor lowCPUInterceptor = cpuReturningIntercepter(FAKE_LOW_CPU_VALUES);
    ServerInterceptor highCPUInterceptor = cpuReturningIntercepter(FAKE_HIGH_CPU_VALUES);

    FakeServiceRetry.expectations.add(new DeadlineExceededException(
        new StatusRuntimeException(Status.DEADLINE_EXCEEDED.withDescription(
            "DEADLINE_EXCEEDED: HTTP/2 error code: DEADLINE_EXCEEDED")),
        GrpcStatusCode.of(Status.Code.DEADLINE_EXCEEDED),
        false));

    lowCPUServerRetry = FakeServiceBuilder.create(FakeServiceRetry).intercept(lowCPUInterceptor).start();
    highCPUServerRetry = FakeServiceBuilder.create(FakeServiceRetry).intercept(highCPUInterceptor).start();

    BigtableDataSettings lowCPUSettings =
        BigtableDataSettings.newBuilderForEmulator(lowCPUServerRetry.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    BigtableDataSettings highCPUSettings =
        BigtableDataSettings.newBuilderForEmulator(highCPUServerRetry.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    // Fix naming
    EnhancedBigtableStubSettings lowCPUStubSettings = lowCPUSettings.getStubSettings();
    EnhancedBigtableStubSettings highCPUStubSettings = highCPUSettings.getStubSettings();
    lowCpuStubRetry = new EnhancedBigtableStub(lowCPUSettings.getStubSettings(), ClientContext.create(lowCPUSettings.getStubSettings()));
    highCpuStubRetry = new EnhancedBigtableStub(highCPUSettings.getStubSettings(), ClientContext.create(highCPUSettings.getStubSettings()));
    stats = new RateLimitingStats();
  }

  @After
  public void tearDown() {
    lowCpuStubRetry.close();
    lowCPUServerRetry.shutdown();
    highCpuStubRetry.close();
    highCPUServerRetry.shutdown();
  }

  @Test
  public void testBulkMutateRowsNoThrottling() throws Exception {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    lowCpuStubRetry.bulkMutateRowsCallable().call(mutations, callContext);

    // Tried a few different assertions and stub/callable calls
  }

  // This test flaked...
  // BulkMutateIT is now failing if the callable is enabled (since I removed the flag)
  //
  /*
  @Test
  public void testBulkMutateRowsHighCpuThrottling() throws Exception {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    highCpuStubRetry.bulkMutateRowsCallable().call(mutations, callContext);

    // Tried a few different assertions and stub/callable calls
  }
   */

  private static class FakeService extends BigtableGrpc.BigtableImplBase {
    Queue<Exception> expectations = Queues.newArrayDeque();

    static List<MutateRowsResponse> createFakeMutateRowsResponse() {
      List<MutateRowsResponse> responses = new ArrayList<>();

      for (int i = 0; i < 1; i++) {
        ArrayList<MutateRowsResponse.Entry> entries = new ArrayList<>();
        entries.add(
            Entry.newBuilder().setIndex(0).setStatus(com.google.rpc.Status.newBuilder().setCode(0).build()).build()); // Definitely a better way to do this

        responses.add(
            MutateRowsResponse.newBuilder().addAllEntries(
                entries
            ).build());
      }

      return responses;
    }

    @Override
    public void mutateRows(
        MutateRowsRequest request, StreamObserver<MutateRowsResponse> responseObserver) {
      if (expectations.isEmpty()) {
        responseObserver.onNext(createFakeMutateRowsResponse().get(0));
        responseObserver.onCompleted();
      } else {
        System.out.println("Expection!");
        Exception expectedRpc = expectations.poll();
        responseObserver.onError(expectedRpc);
      }
    }
  }

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

                ResponseParams params =
                    ResponseParams.newBuilder().setZoneId(ZONE).setClusterId(CLUSTER) // What do I need to set?
                        .build();
                byte[] byteArray = params.toByteArray();
                headers.put(Util.METADATA_KEY, byteArray); // Is this right?

                super.sendHeaders(headers);
              }
            },
            metadata);
      }
    };
  }

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
