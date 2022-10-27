package com.google.cloud.bigtable.data.v2.stub.metrics;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.DeadlineExceededException;
import com.google.api.gax.rpc.InternalException;
import com.google.api.gax.rpc.ServerStream;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse.Entry;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.stub.CpuThrottlingStats;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.RateLimiter;
import com.google.rpc.Code;
import com.google.api.gax.rpc.ClientContext;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.ResponseParams;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import io.grpc.ForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class CpuThrottlingCallableTest {

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
  private CpuThrottlingStats stats;

  private final FakeService FakeServiceComplete = new FakeService();
  private final FakeService FakeServiceRetry = new FakeService();
  private Server lowCPUServerComplete;
  private Server highCPUServerComplete;
  private Server lowCPUServerRetry;
  private Server highCPUServerRetry; // I need to look at retrying tests

  private EnhancedBigtableStub lowCpuStub;
  private EnhancedBigtableStub highCpuStub;
  private EnhancedBigtableStub lowCpuStubRetry;
  private EnhancedBigtableStub highCpuStubRetry;

  private MockMutateInnerCallable innerCallable; // I believe that I can delete this
  private ApiCallContext callContext;

  //@Captor private ArgumentCaptor<CpuThrottlingStats> statsArgumentCaptor;


  @Before
  public void setUp() throws Exception {
    //statsArgumentCaptor = ArgumentCaptor.forClass(CpuThrottlingStats.class);
    innerCallable = new MockMutateInnerCallable();
    callContext = GrpcCallContext.createDefault();

    ServerInterceptor lowCPUInterceptor = cpuReturningIntercepter(FAKE_LOW_CPU_VALUES);
    ServerInterceptor highCPUInterceptor = cpuReturningIntercepter(FAKE_HIGH_CPU_VALUES);

    lowCPUServerComplete = FakeServiceBuilder.create(FakeServiceComplete).intercept(lowCPUInterceptor).start();
    highCPUServerComplete = FakeServiceBuilder.create(FakeServiceComplete).intercept(highCPUInterceptor).start();

    FakeServiceRetry.expectations.add(new DeadlineExceededException(
            new StatusRuntimeException(Status.DEADLINE_EXCEEDED.withDescription(
                    "DEADLINE_EXCEEDED: HTTP/2 error code: DEADLINE_EXCEEDED")),
            GrpcStatusCode.of(Status.Code.DEADLINE_EXCEEDED),
            false));

    lowCPUServerRetry = FakeServiceBuilder.create(FakeServiceRetry).intercept(lowCPUInterceptor).start();
    highCPUServerRetry = FakeServiceBuilder.create(FakeServiceRetry).intercept(highCPUInterceptor).start();

    BigtableDataSettings lowCPUSettings =
        BigtableDataSettings.newBuilderForEmulator(lowCPUServerComplete.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    BigtableDataSettings highCPUSettings =
        BigtableDataSettings.newBuilderForEmulator(highCPUServerComplete.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    BigtableDataSettings lowCPUSettingsFail =
        BigtableDataSettings.newBuilderForEmulator(lowCPUServerRetry.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    BigtableDataSettings highCPUSettingsFail =
        BigtableDataSettings.newBuilderForEmulator(highCPUServerRetry.getPort())
            .enableBatchMutationCpuBasedThrottling()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();

    // Fix naming
    EnhancedBigtableStubSettings lowCPUStubSettings = lowCPUSettings.getStubSettings();
    EnhancedBigtableStubSettings highCPUStubSettings = highCPUSettings.getStubSettings();
    lowCpuStub = new EnhancedBigtableStub(lowCPUStubSettings, ClientContext.create(lowCPUStubSettings)/*, statsArgumentCaptor.capture()*/);
    highCpuStub = new EnhancedBigtableStub(highCPUStubSettings, ClientContext.create(highCPUStubSettings)/*, statsArgumentCaptor.capture()*/);
    lowCpuStubRetry = new EnhancedBigtableStub(lowCPUSettingsFail.getStubSettings(), ClientContext.create(lowCPUSettingsFail.getStubSettings()));
    highCpuStubRetry = new EnhancedBigtableStub(highCPUSettingsFail.getStubSettings(), ClientContext.create(highCPUSettingsFail.getStubSettings()));
    stats = new CpuThrottlingStats();
  }

  @After
  public void tearDown() {
    lowCpuStub.close();
    lowCPUServerComplete.shutdown();
    highCpuStub.close();
    highCPUServerComplete.shutdown();
    lowCpuStubRetry.close();
    lowCPUServerRetry.shutdown();
    highCpuStubRetry.close();
    highCPUServerRetry.shutdown();
  }

  @Test
  public void testBulkMutateRowsNoThrottling() throws Exception {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future =
        lowCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);

    // Tried a few different assertions and stub/callable calls
  }

  @Test
  public void testBulkMutateRowsHighThrottling() throws Exception { // Change n
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future =
        highCpuStub.bulkMutateRowsCallable().futureCall(mutations, callContext);

    future.get();

    //assertThat(highCpuStub.getCpuThrottlingStats().getTimesChanges()).isAtLeast(1);
    //Didn't work because of pass-by-value and can't access stats inside of CpuThrottlingCallable
  }

  @Test
  public void testBulkMutateRowsRPCFailureHighCPU() throws Exception {
    BulkMutation mutations = BulkMutation.create(TABLE_ID).add("fake-row", Mutation.create()
        .setCell("cf","qual","value"));

    ApiFuture<Void> future =
        lowCpuStubRetry.bulkMutateRowsCallable().futureCall(mutations, callContext);

    future.get();

    // Even for the retrying test, they use a client directly instead of the stub
    // I am looking at other retrying tests to see which one uses the stub
  }

  @Test
  public void testBulkMutateRowsRPCFailureNoCPU() {

  }

  private static class FakeService extends BigtableGrpc.BigtableImplBase {
    Queue<Exception> expectations = Queues.newArrayDeque();

    static List<MutateRowsResponse> createFakeMutateRowsResponse() {
      List<MutateRowsResponse> responses = new ArrayList<>();

      for (int i = 0; i < 1; i++) {
        ArrayList<MutateRowsResponse.Entry> entries = new ArrayList<>();
        entries.add(Entry.newBuilder().setIndex(0).setStatus(com.google.rpc.Status.newBuilder().setCode(0).build()).build()); // Definitely a better way to do this

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

  // Better name
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
                headers.put(Util.METADATA_KEY, byteArray);

                super.sendHeaders(headers);
              }
            },
            metadata);
      }
    };
  }
}

/*
 MutateRowsRequest request =
        MutateRowsRequest.newBuilder().addEntries(Entry.getDefaultInstance()).build();
    // I am going to mimic the singleEntrySuccessTest in this test

    // This test is getting this as the response back from the cpuCallable
    innerCallable.response.add(
        MutateRowsResponse.newBuilder()
            .addEntries(
                MutateRowsResponse.Entry.newBuilder().setIndex(0).setStatus(OK_STATUS_PROTO))
            .build());

    CpuThrottlingUnaryCallable cpuCallable =
        new CpuThrottlingUnaryCallable(innerCallable);

    ApiFuture future = cpuCallable.futureCall(request, callContext);

    // How am I suppose to use the future
    List<MutateRowsResponse> response = (ArrayList<MutateRowsResponse>) future.get();


    // innerCallable received the request
    assertThat(innerCallable.lastRequest).isEqualTo(request);
    assertThat(response).isNotNull();
 */

