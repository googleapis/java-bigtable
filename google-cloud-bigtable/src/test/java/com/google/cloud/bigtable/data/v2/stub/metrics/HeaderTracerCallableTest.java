/*
 * Copyright 2020 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;

import com.google.api.gax.rpc.ClientContext;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.CheckAndMutateRowRequest;
import com.google.bigtable.v2.CheckAndMutateRowResponse;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ReadModifyWriteRowRequest;
import com.google.bigtable.v2.ReadModifyWriteRowResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.SampleRowKeysRequest;
import com.google.bigtable.v2.SampleRowKeysResponse;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceHelper;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.ConditionalRowMutation;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.ReadModifyWriteRow;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import io.grpc.ForwardingServerCall.SimpleForwardingServerCall;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.stub.StreamObserver;
import io.opencensus.impl.stats.StatsComponentImpl;
import io.opencensus.stats.StatsComponent;
import io.opencensus.tags.TagKey;
import io.opencensus.tags.TagValue;
import io.opencensus.tags.Tags;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

@RunWith(JUnit4.class)
public class HeaderTracerCallableTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  private FakeServiceHelper serviceHelper;
  private FakeServiceHelper serviceHelperNoHeader;

  @Mock(answer = Answers.CALLS_REAL_METHODS)
  private BigtableGrpc.BigtableImplBase fakeService;

  private StatsComponent localStats = new StatsComponentImpl();
  private EnhancedBigtableStub stub;
  private EnhancedBigtableStub noHeaderStub;

  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE_ID = "fake-table";

  private static final long WAIT_FOR_METRICS_TIME_MS = 1_000;

  private int fakeServerTiming;

  @Before
  public void setUp() throws Exception {
    RpcViews.registerBigtableClientGfeViews(localStats.getViewManager());

    // Create a server that'll inject a server-timing header with a random number and a stub that
    // connects to this server.
    fakeServerTiming = new Random().nextInt(1000) + 1;
    serviceHelper =
        new FakeServiceHelper(
            new ServerInterceptor() {
              @Override
              public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
                  ServerCall<ReqT, RespT> serverCall,
                  Metadata metadata,
                  ServerCallHandler<ReqT, RespT> serverCallHandler) {
                return serverCallHandler.startCall(
                    new SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
                      @Override
                      public void sendHeaders(Metadata headers) {
                        headers.put(
                            Metadata.Key.of("server-timing", Metadata.ASCII_STRING_MARSHALLER),
                            String.format("gfet4t7; dur=%d", fakeServerTiming));
                        super.sendHeaders(headers);
                      }
                    },
                    metadata);
              }
            },
            fakeService);
    serviceHelper.start();

    BigtableDataSettings settings =
        BigtableDataSettings.newBuilderForEmulator(serviceHelper.getPort())
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();
    EnhancedBigtableStubSettings stubSettings =
        EnhancedBigtableStub.finalizeSettings(
            settings.getStubSettings(), Tags.getTagger(), localStats.getStatsRecorder());
    stub = new EnhancedBigtableStub(stubSettings, ClientContext.create(stubSettings));

    // Create another server without injecting the server-timing header and another stub that
    // connects to it.
    serviceHelperNoHeader = new FakeServiceHelper(fakeService);
    serviceHelperNoHeader.start();

    BigtableDataSettings noHeaderSettings =
        BigtableDataSettings.newBuilderForEmulator(serviceHelperNoHeader.getPort())
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();
    EnhancedBigtableStubSettings noHeaderStubSettings =
        EnhancedBigtableStub.finalizeSettings(
            noHeaderSettings.getStubSettings(), Tags.getTagger(), localStats.getStatsRecorder());
    noHeaderStub =
        new EnhancedBigtableStub(noHeaderStubSettings, ClientContext.create(noHeaderStubSettings));
  }

  @After
  public void tearDown() {
    stub.close();
    noHeaderStub.close();
    serviceHelper.shutdown();
    serviceHelperNoHeader.shutdown();
  }

  @Test
  public void testGFELatencyMetricReadRows() throws InterruptedException {
    doAnswer(new ReadRowsAnswer())
        .when(fakeService)
        .readRows(any(ReadRowsRequest.class), anyObserver(ReadRowsResponse.class));

    stub.readRowsCallable().call(Query.create(TABLE_ID));

    Thread.sleep(WAIT_FOR_METRICS_TIME_MS);

    long latency =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_LATENCY_VIEW,
            ImmutableMap.<TagKey, TagValue>of(
                RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.ReadRows")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);

    assertThat(latency).isEqualTo(fakeServerTiming);
  }

  @Test
  public void testGFELatencyMetricMutateRow() throws InterruptedException {
    doAnswer(new MutateRowAnswer())
        .when(fakeService)
        .mutateRow(any(MutateRowRequest.class), anyObserver(MutateRowResponse.class));

    stub.mutateRowCallable().call(RowMutation.create(TABLE_ID, "fake-key"));

    Thread.sleep(WAIT_FOR_METRICS_TIME_MS);

    long latency =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_LATENCY_VIEW,
            ImmutableMap.of(RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.MutateRow")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);

    assertThat(latency).isEqualTo(fakeServerTiming);
  }

  @Test
  public void testGFELatencyMetricMutateRows() throws InterruptedException {
    doAnswer(new MutateRowsAnswer())
        .when(fakeService)
        .mutateRows(any(MutateRowsRequest.class), anyObserver(MutateRowsResponse.class));

    BulkMutation mutations =
        BulkMutation.create(TABLE_ID)
            .add("key", Mutation.create().setCell("fake-family", "fake-qualifier", "fake-value"));
    stub.bulkMutateRowsCallable().call(mutations);

    Thread.sleep(WAIT_FOR_METRICS_TIME_MS);

    long latency =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_LATENCY_VIEW,
            ImmutableMap.of(
                RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.MutateRows")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);

    assertThat(latency).isEqualTo(fakeServerTiming);
  }

  @Test
  public void testGFELatencySampleRowKeys() throws InterruptedException {
    doAnswer(new SampleRowKeysAnswer())
        .when(fakeService)
        .sampleRowKeys(any(SampleRowKeysRequest.class), anyObserver(SampleRowKeysResponse.class));
    stub.sampleRowKeysCallable().call(TABLE_ID);

    Thread.sleep(WAIT_FOR_METRICS_TIME_MS);
    long latency =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_LATENCY_VIEW,
            ImmutableMap.of(
                RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.SampleRowKeys")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    assertThat(latency).isEqualTo(fakeServerTiming);
  }

  @Test
  public void testGFELatencyCheckAndMutateRow() throws InterruptedException {
    doAnswer(new CheckAndMutateRowAnswer())
        .when(fakeService)
        .checkAndMutateRow(
            any(CheckAndMutateRowRequest.class), anyObserver(CheckAndMutateRowResponse.class));

    ConditionalRowMutation mutation =
        ConditionalRowMutation.create(TABLE_ID, "fake-key")
            .then(Mutation.create().setCell("fake-family", "fake-qualifier", "fake-value"));
    stub.checkAndMutateRowCallable().call(mutation);

    Thread.sleep(WAIT_FOR_METRICS_TIME_MS);
    long latency =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_LATENCY_VIEW,
            ImmutableMap.of(
                RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.CheckAndMutateRow")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    assertThat(latency).isEqualTo(fakeServerTiming);
  }

  @Test
  public void testGFELatencyReadModifyWriteRow() throws InterruptedException {
    doAnswer(new ReadModifyWriteRowAnswer())
        .when(fakeService)
        .readModifyWriteRow(
            any(ReadModifyWriteRowRequest.class), anyObserver(ReadModifyWriteRowResponse.class));

    ReadModifyWriteRow request =
        ReadModifyWriteRow.create(TABLE_ID, "fake-key")
            .append("fake-family", "fake-qualifier", "suffix");
    stub.readModifyWriteRowCallable().call(request);

    Thread.sleep(WAIT_FOR_METRICS_TIME_MS);
    long latency =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_LATENCY_VIEW,
            ImmutableMap.of(
                RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.ReadModifyWriteRow")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    assertThat(latency).isEqualTo(fakeServerTiming);
  }

  @Test
  public void testGFEMissingHeaderMetric() throws InterruptedException {
    doAnswer(new ReadRowsAnswer())
        .when(fakeService)
        .readRows(any(ReadRowsRequest.class), anyObserver(ReadRowsResponse.class));
    doAnswer(new MutateRowAnswer())
        .when(fakeService)
        .mutateRow(any(MutateRowRequest.class), anyObserver(MutateRowResponse.class));

    // Make a few calls to the server which will inject the server-timing header and the counter
    // should be 0.
    stub.readRowsCallable().call(Query.create(TABLE_ID));
    stub.mutateRowCallable().call(RowMutation.create(TABLE_ID, "key"));

    Thread.sleep(WAIT_FOR_METRICS_TIME_MS);
    long mutateRowMissingCount =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT_VIEW,
            ImmutableMap.of(RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.MutateRow")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    long readRowsMissingCount =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT_VIEW,
            ImmutableMap.<TagKey, TagValue>of(
                RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.ReadRows")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);

    Thread.sleep(WAIT_FOR_METRICS_TIME_MS);

    assertThat(mutateRowMissingCount).isEqualTo(0);
    assertThat(readRowsMissingCount).isEqualTo(0);

    // Make a few more calls to the server which won't add the header and the counter should match
    // the number of requests sent.
    int readRowsCalls = new Random().nextInt(10) + 1;
    int mutateRowCalls = new Random().nextInt(10) + 1;
    for (int i = 0; i < mutateRowCalls; i++) {
      noHeaderStub.mutateRowCallable().call(RowMutation.create(TABLE_ID, "fake-key" + i));
    }
    for (int i = 0; i < readRowsCalls; i++) {
      noHeaderStub.readRowsCallable().call(Query.create(TABLE_ID));
    }

    Thread.sleep(WAIT_FOR_METRICS_TIME_MS);

    mutateRowMissingCount =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT_VIEW,
            ImmutableMap.of(RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.MutateRow")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);
    readRowsMissingCount =
        StatsTestUtils.getAggregationValueAsLong(
            localStats,
            RpcViewConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT_VIEW,
            ImmutableMap.<TagKey, TagValue>of(
                RpcMeasureConstants.BIGTABLE_OP, TagValue.create("Bigtable.ReadRows")),
            PROJECT_ID,
            INSTANCE_ID,
            APP_PROFILE_ID);

    assertThat(mutateRowMissingCount).isEqualTo(mutateRowCalls);
    assertThat(readRowsMissingCount).isEqualTo(readRowsCalls);
  }

  private class ReadRowAnswer implements Answer {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
      StreamObserver<Row> observer = (StreamObserver<Row>) invocation.getArguments()[1];
      observer.onNext(
          Row.create(ByteString.copyFromUtf8("fake-row-key"), ImmutableList.<RowCell>of()));
      observer.onCompleted();
      return null;
    }
  }

  private class ReadRowsAnswer implements Answer {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
      StreamObserver<ReadRowsResponse> observer =
          (StreamObserver<ReadRowsResponse>) invocation.getArguments()[1];
      observer.onNext(ReadRowsResponse.getDefaultInstance());
      observer.onCompleted();
      return null;
    }
  }

  private class MutateRowAnswer implements Answer {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
      StreamObserver<MutateRowResponse> observer =
          (StreamObserver<MutateRowResponse>) invocation.getArguments()[1];
      observer.onNext(MutateRowResponse.getDefaultInstance());
      observer.onCompleted();
      return null;
    }
  }

  private class MutateRowsAnswer implements Answer {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
      StreamObserver<MutateRowsResponse> observer =
          (StreamObserver<MutateRowsResponse>) invocation.getArguments()[1];
      observer.onNext(MutateRowsResponse.getDefaultInstance());
      observer.onCompleted();
      return null;
    }
  }

  private class SampleRowKeysAnswer implements Answer {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
      StreamObserver<SampleRowKeysResponse> observer =
          (StreamObserver<SampleRowKeysResponse>) invocation.getArguments()[1];
      observer.onNext(SampleRowKeysResponse.getDefaultInstance());
      observer.onCompleted();
      return null;
    }
  }

  private class CheckAndMutateRowAnswer implements Answer {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
      StreamObserver<CheckAndMutateRowResponse> observer =
          (StreamObserver<CheckAndMutateRowResponse>) invocation.getArguments()[1];
      observer.onNext(CheckAndMutateRowResponse.getDefaultInstance());
      observer.onCompleted();
      return null;
    }
  }

  private class ReadModifyWriteRowAnswer implements Answer {
    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
      StreamObserver<ReadModifyWriteRowResponse> observer =
          (StreamObserver<ReadModifyWriteRowResponse>) invocation.getArguments()[1];
      observer.onNext(ReadModifyWriteRowResponse.getDefaultInstance());
      observer.onCompleted();
      return null;
    }
  }

  private static <T> StreamObserver<T> anyObserver(Class<T> returnType) {
    return (StreamObserver<T>) any(returnType);
  }
}
