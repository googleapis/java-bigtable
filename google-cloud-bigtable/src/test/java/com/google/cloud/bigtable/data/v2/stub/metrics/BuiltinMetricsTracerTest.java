/*
 * Copyright 2022 Google LLC
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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLIENT_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLUSTER_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.METHOD;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.STATUS;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.STREAMING;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.TABLE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.ZONE_ID;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.util.Lists;
import com.google.api.core.ApiFunction;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.batching.Batcher;
import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.batching.FlowControlSettings;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.NotFoundException;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.ResponseParams;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Range;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.StringValue;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingServerCall;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import io.opentelemetry.api.common.Attributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.threeten.bp.Duration;

@RunWith(JUnit4.class)
public class BuiltinMetricsTracerTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "default";
  private static final String TABLE = "fake-table";

  private static final String BAD_TABLE_ID = "non-exist-table";
  private static final String ZONE = "us-west-1";
  private static final String CLUSTER = "cluster-0";
  private static final long FAKE_SERVER_TIMING = 50;
  private static final long SERVER_LATENCY = 100;
  private static final long APPLICATION_LATENCY = 200;
  private static final long SLEEP_VARIABILITY = 15;

  private static final long CHANNEL_BLOCKING_LATENCY = 75;

  @Rule public final MockitoRule mockitoRule = MockitoJUnit.rule();

  private final FakeService fakeService = new FakeService();
  private Server server;

  private EnhancedBigtableStub stub;

  @Mock private BigtableMetricsRecorder mockInstruments;

  @Mock private BuiltinMetricsTracerFactory mockFactory;

  private int batchElementCount = 2;

  private Attributes baseAttributes;

  @Before
  public void setUp() throws Exception {
    // Add an interceptor to add server-timing in headers
    ServerInterceptor trailersInterceptor =
        new ServerInterceptor() {
          @Override
          public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
              ServerCall<ReqT, RespT> serverCall,
              Metadata metadata,
              ServerCallHandler<ReqT, RespT> serverCallHandler) {
            return serverCallHandler.startCall(
                new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(serverCall) {
                  @Override
                  public void sendHeaders(Metadata headers) {
                    headers.put(
                        Metadata.Key.of("server-timing", Metadata.ASCII_STRING_MARSHALLER),
                        String.format("gfet4t7; dur=%d", FAKE_SERVER_TIMING));

                    ResponseParams params =
                        ResponseParams.newBuilder().setZoneId(ZONE).setClusterId(CLUSTER).build();
                    byte[] byteArray = params.toByteArray();
                    headers.put(Util.LOCATION_METADATA_KEY, byteArray);

                    super.sendHeaders(headers);
                  }
                },
                metadata);
          }
        };

    ClientInterceptor clientInterceptor =
        new ClientInterceptor() {
          @Override
          public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
              MethodDescriptor<ReqT, RespT> methodDescriptor,
              CallOptions callOptions,
              Channel channel) {
            return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
                channel.newCall(methodDescriptor, callOptions)) {
              @Override
              public void sendMessage(ReqT message) {
                try {
                  Thread.sleep(CHANNEL_BLOCKING_LATENCY);
                } catch (InterruptedException e) {
                  throw new RuntimeException(e);
                }
                super.sendMessage(message);
              }
            };
          }
        };

    server = FakeServiceBuilder.create(fakeService).intercept(trailersInterceptor).start();

    BigtableDataSettings settings =
        BigtableDataSettings.newBuilderForEmulator(server.getPort())
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .build();
    EnhancedBigtableStubSettings.Builder stubSettingsBuilder =
        settings.getStubSettings().toBuilder();
    stubSettingsBuilder
        .mutateRowSettings()
        .retrySettings()
        .setInitialRetryDelay(Duration.ofMillis(200));

    stubSettingsBuilder
        .bulkMutateRowsSettings()
        .setBatchingSettings(
            // Each batch has 2 mutations, batch has 1 in-flight request, disable auto flush by
            // setting the delay to 1 hour.
            BatchingSettings.newBuilder()
                .setElementCountThreshold((long) batchElementCount)
                .setRequestByteThreshold(1000L)
                .setDelayThreshold(Duration.ofHours(1))
                .setFlowControlSettings(
                    FlowControlSettings.newBuilder()
                        .setMaxOutstandingElementCount((long) batchElementCount + 1)
                        .setMaxOutstandingRequestBytes(1001L)
                        .build())
                .build());

    stubSettingsBuilder.setTracerFactory(mockFactory);

    InstantiatingGrpcChannelProvider.Builder channelProvider =
        ((InstantiatingGrpcChannelProvider) stubSettingsBuilder.getTransportChannelProvider())
            .toBuilder();

    @SuppressWarnings("rawtypes")
    final ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> oldConfigurator =
        channelProvider.getChannelConfigurator();

    channelProvider.setChannelConfigurator(
        (builder) -> {
          if (oldConfigurator != null) {
            builder = oldConfigurator.apply(builder);
          }
          return builder.intercept(clientInterceptor);
        });
    stubSettingsBuilder.setTransportChannelProvider(channelProvider.build());

    EnhancedBigtableStubSettings stubSettings = stubSettingsBuilder.build();
    stub = new EnhancedBigtableStub(stubSettings, ClientContext.create(stubSettings));

    baseAttributes =
        Attributes.builder()
            .put(BuiltinMetricsConstants.PROJECT_ID, PROJECT_ID)
            .put(BuiltinMetricsConstants.INSTANCE_ID, INSTANCE_ID)
            .put(BuiltinMetricsConstants.APP_PROFILE, APP_PROFILE_ID)
            .build();
  }

  @After
  public void tearDown() {
    stub.close();
    server.shutdown();
  }

  @Test
  public void testReadRowsOperationLatencies() {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                mockInstruments,
                baseAttributes));

    Stopwatch stopwatch = Stopwatch.createStarted();
    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE)).iterator());
    long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

    ArgumentCaptor<Long> value = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> attributes = ArgumentCaptor.forClass(Attributes.class);
    verify(mockInstruments).recordOperationLatencies(value.capture(), attributes.capture());

    // TODO why is it operation latency always elapsed + 1?
    assertThat(value.getValue()).isIn(Range.closed(SERVER_LATENCY, elapsed + 1));
    assertThat(attributes.getValue().get(STATUS)).isEqualTo("OK");
    assertThat(attributes.getValue().get(TABLE_ID)).isEqualTo(TABLE);
    assertThat(attributes.getValue().get(ZONE_ID)).isEqualTo(ZONE);
    assertThat(attributes.getValue().get(CLUSTER_ID)).isEqualTo(CLUSTER);
    assertThat(attributes.getValue().get(METHOD)).isEqualTo("Bigtable.ReadRows");
    assertThat(attributes.getValue().get(STREAMING)).isTrue();
  }

  @Test
  public void testGfeMetrics() {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                mockInstruments,
                baseAttributes));

    Lists.newArrayList(stub.readRowsCallable().call(Query.create(TABLE)));

    // Verify record attempt are called multiple times
    ArgumentCaptor<Long> value = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> attributes = ArgumentCaptor.forClass(Attributes.class);

    verify(mockInstruments, times(fakeService.getAttemptCounter().get()))
        .recordAttemptLatencies(value.capture(), attributes.capture());

    // The request was retried and gfe latency is only recorded in the retry attempt
    ArgumentCaptor<Long> serverLatencies = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> serverLatenciesAttributes =
        ArgumentCaptor.forClass(Attributes.class);

    verify(mockInstruments)
        .recordServerLatencies(serverLatencies.capture(), serverLatenciesAttributes.capture());
    assertThat(serverLatencies.getValue()).isEqualTo(FAKE_SERVER_TIMING);
    assertThat(
            serverLatenciesAttributes.getAllValues().stream()
                .map(a -> a.get(STATUS))
                .collect(Collectors.toList()))
        .containsExactly("OK");
    assertThat(
            serverLatenciesAttributes.getAllValues().stream()
                .map(a -> a.get(TABLE_ID))
                .collect(Collectors.toList()))
        .containsExactly(TABLE);
    assertThat(
            serverLatenciesAttributes.getAllValues().stream()
                .map(a -> a.get(ZONE_ID))
                .collect(Collectors.toList()))
        .containsExactly(ZONE);
    assertThat(
            serverLatenciesAttributes.getAllValues().stream()
                .map(a -> a.get(CLUSTER_ID))
                .collect(Collectors.toList()))
        .containsExactly(CLUSTER);

    // The first time the request was retried, it'll increment missing header counter
    ArgumentCaptor<Long> connectivityErrorCount = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> errorCountAttributes = ArgumentCaptor.forClass(Attributes.class);

    verify(mockInstruments, times(fakeService.getAttemptCounter().get()))
        .recordConnectivityErrorCount(
            connectivityErrorCount.capture(), errorCountAttributes.capture());
    assertThat(connectivityErrorCount.getAllValues()).containsExactly(1L, 0L);

    assertThat(
            errorCountAttributes.getAllValues().stream()
                .map(a -> a.get(STATUS))
                .collect(Collectors.toList()))
        .containsExactly("UNAVAILABLE", "OK");
    assertThat(
            errorCountAttributes.getAllValues().stream()
                .map(a -> a.get(TABLE_ID))
                .collect(Collectors.toList()))
        .containsExactly(TABLE, TABLE);
    assertThat(
            errorCountAttributes.getAllValues().stream()
                .map(a -> a.get(ZONE_ID))
                .collect(Collectors.toList()))
        .containsExactly("global", ZONE);
    assertThat(
            errorCountAttributes.getAllValues().stream()
                .map(a -> a.get(CLUSTER_ID))
                .collect(Collectors.toList()))
        .containsExactly("unspecified", CLUSTER);
  }

  @Test
  public void testReadRowsApplicationLatencyWithAutoFlowControl() throws Exception {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                mockInstruments,
                baseAttributes));

    ArgumentCaptor<Long> applicationLatency = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> operationLatency = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> opLatencyAttributes = ArgumentCaptor.forClass(Attributes.class);
    ArgumentCaptor<Attributes> applicationLatencyAttributes =
        ArgumentCaptor.forClass(Attributes.class);

    final SettableApiFuture future = SettableApiFuture.create();
    final AtomicInteger counter = new AtomicInteger(0);
    // For auto flow control, application latency is the time application spent in onResponse.
    stub.readRowsCallable()
        .call(
            Query.create(TABLE),
            new ResponseObserver<Row>() {
              @Override
              public void onStart(StreamController streamController) {}

              @Override
              public void onResponse(Row row) {
                try {
                  counter.getAndIncrement();
                  Thread.sleep(APPLICATION_LATENCY);
                } catch (InterruptedException e) {
                }
              }

              @Override
              public void onError(Throwable throwable) {
                future.setException(throwable);
              }

              @Override
              public void onComplete() {
                future.set(null);
              }
            });
    future.get();

    verify(mockInstruments)
        .recordApplicationBlockingLatencies(
            applicationLatency.capture(), applicationLatencyAttributes.capture());
    verify(mockInstruments)
        .recordOperationLatencies(operationLatency.capture(), opLatencyAttributes.capture());

    assertThat(counter.get()).isEqualTo(fakeService.getResponseCounter().get());
    // Thread.sleep might not sleep for the requested amount depending on the interrupt period
    // defined by the OS.
    // On linux this is ~1ms but on windows may be as high as 15-20ms.
    assertThat(applicationLatency.getValue())
        .isAtLeast((APPLICATION_LATENCY - SLEEP_VARIABILITY) * counter.get());
    assertThat(applicationLatency.getValue())
        .isAtMost(operationLatency.getValue() - SERVER_LATENCY);
  }

  @Test
  public void testReadRowsApplicationLatencyWithManualFlowControl() throws Exception {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                mockInstruments,
                baseAttributes));

    ArgumentCaptor<Long> applicationLatency = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> operationLatency = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> opLatencyAttributes = ArgumentCaptor.forClass(Attributes.class);
    ArgumentCaptor<Attributes> applicationLatencyAttributes =
        ArgumentCaptor.forClass(Attributes.class);

    int counter = 0;

    Iterator<Row> rows = stub.readRowsCallable().call(Query.create(TABLE)).iterator();

    while (rows.hasNext()) {
      counter++;
      Thread.sleep(APPLICATION_LATENCY);
      rows.next();
    }

    verify(mockInstruments)
        .recordApplicationBlockingLatencies(
            applicationLatency.capture(), applicationLatencyAttributes.capture());
    verify(mockInstruments)
        .recordOperationLatencies(operationLatency.capture(), opLatencyAttributes.capture());

    // For manual flow control, the last application latency shouldn't count, because at that
    // point the server already sent back all the responses.
    assertThat(counter).isEqualTo(fakeService.getResponseCounter().get());
    assertThat(applicationLatency.getValue())
        .isAtLeast(APPLICATION_LATENCY * (counter - 1) - SERVER_LATENCY);
    assertThat(applicationLatency.getValue())
        .isAtMost(operationLatency.getValue() - SERVER_LATENCY);
  }

  @Test
  public void testRetryCount() {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                mockInstruments,
                baseAttributes));

    ArgumentCaptor<Long> retryCount = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> retryCountAttribute = ArgumentCaptor.forClass(Attributes.class);

    stub.mutateRowCallable()
        .call(RowMutation.create(TABLE, "random-row").setCell("cf", "q", "value"));

    // In TracedUnaryCallable, we create a future and add a TraceFinisher to the callback. Main
    // thread is blocked on waiting for the future to be completed. When onComplete is called on
    // the grpc thread, the future is completed, however we might not have enough time for
    // TraceFinisher to run. Add a 1 second time out to wait for the callback. This shouldn't
    // have any impact on production code.
    verify(mockInstruments, timeout(1000))
        .recordRetryCount(retryCount.capture(), retryCountAttribute.capture());

    assertThat(retryCount.getValue()).isEqualTo(fakeService.getAttemptCounter().get() - 1);
  }

  @Test
  public void testMutateRowAttemptsTagValues() {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.Unary,
                SpanName.of("Bigtable", "MutateRow"),
                mockInstruments,
                baseAttributes));

    stub.mutateRowCallable()
        .call(RowMutation.create(TABLE, "random-row").setCell("cf", "q", "value"));

    ArgumentCaptor<Long> value = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> attributes = ArgumentCaptor.forClass(Attributes.class);

    // Set a timeout to reduce flakiness of this test. BasicRetryingFuture will set
    // attempt succeeded and set the response which will call complete() in AbstractFuture which
    // calls releaseWaiters(). onOperationComplete() is called in TracerFinisher which will be
    // called after the mutateRow call is returned. So there's a race between when the call
    // returns and when the record() is called in onOperationCompletion().
    verify(mockInstruments, timeout(50).times(fakeService.getAttemptCounter().get()))
        .recordAttemptLatencies(value.capture(), attributes.capture());
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(BuiltinMetricsConstants.PROJECT_ID))
                .collect(Collectors.toList()))
        .containsExactly(PROJECT_ID, PROJECT_ID, PROJECT_ID);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(BuiltinMetricsConstants.INSTANCE_ID))
                .collect(Collectors.toList()))
        .containsExactly(INSTANCE_ID, INSTANCE_ID, INSTANCE_ID);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(BuiltinMetricsConstants.APP_PROFILE))
                .collect(Collectors.toList()))
        .containsExactly(APP_PROFILE_ID, APP_PROFILE_ID, APP_PROFILE_ID);
    assertThat(
            attributes.getAllValues().stream().map(a -> a.get(STATUS)).collect(Collectors.toList()))
        .containsExactly("UNAVAILABLE", "UNAVAILABLE", "OK");
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(TABLE_ID))
                .collect(Collectors.toList()))
        .containsExactly(TABLE, TABLE, TABLE);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(ZONE_ID))
                .collect(Collectors.toList()))
        .containsExactly("global", "global", ZONE);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(CLUSTER_ID))
                .collect(Collectors.toList()))
        .containsExactly("unspecified", "unspecified", CLUSTER);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(CLIENT_NAME))
                .collect(Collectors.toList()))
        .containsExactly("java-bigtable", "java-bigtable", "java-bigtable");
    assertThat(
            attributes.getAllValues().stream().map(a -> a.get(METHOD)).collect(Collectors.toList()))
        .containsExactly("Bigtable.MutateRow", "Bigtable.MutateRow", "Bigtable.MutateRow");
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(STREAMING))
                .collect(Collectors.toList()))
        .containsExactly(false, false, false);
  }

  @Test
  public void testReadRowsAttemptsTagValues() {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                mockInstruments,
                baseAttributes));

    Lists.newArrayList(stub.readRowsCallable().call(Query.create("fake-table")).iterator());

    ArgumentCaptor<Long> value = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> attributes = ArgumentCaptor.forClass(Attributes.class);

    // Set a timeout to reduce flakiness of this test. BasicRetryingFuture will set
    // attempt succeeded and set the response which will call complete() in AbstractFuture which
    // calls releaseWaiters(). onOperationComplete() is called in TracerFinisher which will be
    // called after the mutateRow call is returned. So there's a race between when the call
    // returns and when the record() is called in onOperationCompletion().
    verify(mockInstruments, timeout(50).times(fakeService.getAttemptCounter().get()))
        .recordAttemptLatencies(value.capture(), attributes.capture());
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(BuiltinMetricsConstants.PROJECT_ID))
                .collect(Collectors.toList()))
        .containsExactly(PROJECT_ID, PROJECT_ID);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(BuiltinMetricsConstants.INSTANCE_ID))
                .collect(Collectors.toList()))
        .containsExactly(INSTANCE_ID, INSTANCE_ID);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(BuiltinMetricsConstants.APP_PROFILE))
                .collect(Collectors.toList()))
        .containsExactly(APP_PROFILE_ID, APP_PROFILE_ID);
    assertThat(
            attributes.getAllValues().stream().map(a -> a.get(STATUS)).collect(Collectors.toList()))
        .containsExactly("UNAVAILABLE", "OK");
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(TABLE_ID))
                .collect(Collectors.toList()))
        .containsExactly(TABLE, TABLE);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(ZONE_ID))
                .collect(Collectors.toList()))
        .containsExactly("global", ZONE);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(CLUSTER_ID))
                .collect(Collectors.toList()))
        .containsExactly("unspecified", CLUSTER);
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(CLIENT_NAME))
                .collect(Collectors.toList()))
        .containsExactly("java-bigtable", "java-bigtable");
    assertThat(
            attributes.getAllValues().stream().map(a -> a.get(METHOD)).collect(Collectors.toList()))
        .containsExactly("Bigtable.ReadRows", "Bigtable.ReadRows");
    assertThat(
            attributes.getAllValues().stream()
                .map(a -> a.get(STREAMING))
                .collect(Collectors.toList()))
        .containsExactly(true, true);
  }

  @Test
  public void testBatchBlockingLatencies() throws InterruptedException {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "MutateRows"),
                mockInstruments,
                baseAttributes));

    try (Batcher<RowMutationEntry, Void> batcher = stub.newMutateRowsBatcher(TABLE, null)) {
      for (int i = 0; i < 6; i++) {
        batcher.add(RowMutationEntry.create("key").setCell("f", "q", "v"));
      }

      // closing the batcher to trigger the third flush
      batcher.close();

      int expectedNumRequests = 6 / batchElementCount;
      ArgumentCaptor<Long> throttledTime = ArgumentCaptor.forClass(Long.class);
      ArgumentCaptor<Attributes> attributes = ArgumentCaptor.forClass(Attributes.class);

      verify(mockInstruments, timeout(5000).times(expectedNumRequests))
          .recordClientBlockingLatencies(throttledTime.capture(), attributes.capture());

      // After the first request is sent, batcher will block on add because of the server latency.
      // Blocking latency should be around server latency.
      assertThat(throttledTime.getAllValues().get(1)).isAtLeast(SERVER_LATENCY - 10);
      assertThat(throttledTime.getAllValues().get(2)).isAtLeast(SERVER_LATENCY - 10);

      assertThat(
              attributes.getAllValues().stream()
                  .map(a -> a.get(BuiltinMetricsConstants.PROJECT_ID))
                  .collect(Collectors.toList()))
          .containsExactly(PROJECT_ID, PROJECT_ID, PROJECT_ID);
      assertThat(
              attributes.getAllValues().stream()
                  .map(a -> a.get(BuiltinMetricsConstants.INSTANCE_ID))
                  .collect(Collectors.toList()))
          .containsExactly(INSTANCE_ID, INSTANCE_ID, INSTANCE_ID);
      assertThat(
              attributes.getAllValues().stream()
                  .map(a -> a.get(BuiltinMetricsConstants.APP_PROFILE))
                  .collect(Collectors.toList()))
          .containsExactly(APP_PROFILE_ID, APP_PROFILE_ID, APP_PROFILE_ID);
      assertThat(
              attributes.getAllValues().stream()
                  .map(a -> a.get(TABLE_ID))
                  .collect(Collectors.toList()))
          .containsExactly(TABLE, TABLE, TABLE);
      assertThat(
              attributes.getAllValues().stream()
                  .map(a -> a.get(ZONE_ID))
                  .collect(Collectors.toList()))
          .containsExactly(ZONE, ZONE, ZONE);
      assertThat(
              attributes.getAllValues().stream()
                  .map(a -> a.get(CLUSTER_ID))
                  .collect(Collectors.toList()))
          .containsExactly(CLUSTER, CLUSTER, CLUSTER);
      assertThat(
              attributes.getAllValues().stream()
                  .map(a -> a.get(CLIENT_NAME))
                  .collect(Collectors.toList()))
          .containsExactly("java-bigtable", "java-bigtable", "java-bigtable");
      assertThat(
              attributes.getAllValues().stream()
                  .map(a -> a.get(METHOD))
                  .collect(Collectors.toList()))
          .containsExactly("Bigtable.MutateRows", "Bigtable.MutateRows", "Bigtable.MutateRows");
    }
  }

  @Test
  public void testQueuedOnChannelServerStreamLatencies() throws InterruptedException {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                mockInstruments,
                baseAttributes));

    stub.readRowsCallable().all().call(Query.create(TABLE));

    ArgumentCaptor<Long> blockedTime = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> attributes = ArgumentCaptor.forClass(Attributes.class);

    verify(mockInstruments, timeout(1000).times(fakeService.attemptCounter.get()))
        .recordClientBlockingLatencies(blockedTime.capture(), attributes.capture());

    assertThat(blockedTime.getAllValues().get(1)).isAtLeast(CHANNEL_BLOCKING_LATENCY);
  }

  @Test
  public void testQueuedOnChannelUnaryLatencies() throws InterruptedException {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "MutateRow"),
                mockInstruments,
                baseAttributes));

    stub.mutateRowCallable().call(RowMutation.create(TABLE, "a-key").setCell("f", "q", "v"));

    ArgumentCaptor<Long> blockedTime = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> attributes = ArgumentCaptor.forClass(Attributes.class);

    verify(mockInstruments, timeout(1000).times(fakeService.attemptCounter.get()))
        .recordClientBlockingLatencies(blockedTime.capture(), attributes.capture());

    assertThat(blockedTime.getAllValues().get(1)).isAtLeast(CHANNEL_BLOCKING_LATENCY);
    assertThat(blockedTime.getAllValues().get(2)).isAtLeast(CHANNEL_BLOCKING_LATENCY);
  }

  @Test
  public void testPermanentFailure() {
    when(mockFactory.newTracer(any(), any(), any()))
        .thenReturn(
            new BuiltinMetricsTracer(
                ApiTracerFactory.OperationType.ServerStreaming,
                SpanName.of("Bigtable", "ReadRows"),
                mockInstruments,
                baseAttributes));

    try {
      Lists.newArrayList(stub.readRowsCallable().call(Query.create(BAD_TABLE_ID)).iterator());
      Assert.fail("Request should throw not found error");
    } catch (NotFoundException e) {
    }

    ArgumentCaptor<Long> attemptLatency = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Long> operationLatency = ArgumentCaptor.forClass(Long.class);
    ArgumentCaptor<Attributes> attemptAttributes = ArgumentCaptor.forClass(Attributes.class);
    ArgumentCaptor<Attributes> operationAttributes = ArgumentCaptor.forClass(Attributes.class);

    verify(mockInstruments, timeout(50))
        .recordAttemptLatencies(attemptLatency.capture(), attemptAttributes.capture());
    verify(mockInstruments, timeout(50))
        .recordOperationLatencies(operationLatency.capture(), operationAttributes.capture());

    // verify attempt attributes
    assertThat(
            attemptAttributes.getAllValues().stream()
                .map(a -> a.get(STATUS))
                .collect(Collectors.toList()))
        .containsExactly("NOT_FOUND");
    assertThat(
            attemptAttributes.getAllValues().stream()
                .map(a -> a.get(TABLE_ID))
                .collect(Collectors.toList()))
        .containsExactly(BAD_TABLE_ID);
    assertThat(
            attemptAttributes.getAllValues().stream()
                .map(a -> a.get(ZONE_ID))
                .collect(Collectors.toList()))
        .containsExactly("global");
    assertThat(
            attemptAttributes.getAllValues().stream()
                .map(a -> a.get(CLUSTER_ID))
                .collect(Collectors.toList()))
        .containsExactly("unspecified");

    // verify operation attributes
    assertThat(
            operationAttributes.getAllValues().stream()
                .map(a -> a.get(STATUS))
                .collect(Collectors.toList()))
        .containsExactly("NOT_FOUND");
    assertThat(
            operationAttributes.getAllValues().stream()
                .map(a -> a.get(TABLE_ID))
                .collect(Collectors.toList()))
        .containsExactly(BAD_TABLE_ID);
    assertThat(
            operationAttributes.getAllValues().stream()
                .map(a -> a.get(ZONE_ID))
                .collect(Collectors.toList()))
        .containsExactly("global");
    assertThat(
            operationAttributes.getAllValues().stream()
                .map(a -> a.get(CLUSTER_ID))
                .collect(Collectors.toList()))
        .containsExactly("unspecified");
  }

  private static class FakeService extends BigtableGrpc.BigtableImplBase {

    static List<ReadRowsResponse> createFakeResponse() {
      List<ReadRowsResponse> responses = new ArrayList<>();
      for (int i = 0; i < 4; i++) {
        responses.add(
            ReadRowsResponse.newBuilder()
                .addChunks(
                    ReadRowsResponse.CellChunk.newBuilder()
                        .setRowKey(ByteString.copyFromUtf8("fake-key-" + i))
                        .setFamilyName(StringValue.of("cf"))
                        .setQualifier(
                            BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                        .setTimestampMicros(1_000)
                        .setValue(
                            ByteString.copyFromUtf8(
                                String.join("", Collections.nCopies(1024 * 1024, "A"))))
                        .setCommitRow(true))
                .build());
      }
      return responses;
    }

    private final AtomicInteger attemptCounter = new AtomicInteger(0);
    private final AtomicInteger responseCounter = new AtomicInteger(0);
    private final Iterator<ReadRowsResponse> source = createFakeResponse().listIterator();

    @Override
    public void readRows(
        ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
      if (request.getTableName().contains(BAD_TABLE_ID)) {
        responseObserver.onError(new StatusRuntimeException(Status.NOT_FOUND));
        return;
      }
      final AtomicBoolean done = new AtomicBoolean();
      final ServerCallStreamObserver<ReadRowsResponse> target =
          (ServerCallStreamObserver<ReadRowsResponse>) responseObserver;
      try {
        Thread.sleep(SERVER_LATENCY);
      } catch (InterruptedException e) {
      }
      if (attemptCounter.getAndIncrement() == 0) {
        target.onError(new StatusRuntimeException(Status.UNAVAILABLE));
        return;
      }

      // Only return the next response when the buffer is emptied for testing manual flow control.
      // The fake service won't keep calling onNext unless it received an onRequest event from
      // the application thread
      target.setOnReadyHandler(
          () -> {
            while (target.isReady() && source.hasNext()) {
              responseCounter.getAndIncrement();
              target.onNext(source.next());
            }
            if (!source.hasNext() && done.compareAndSet(false, true)) {
              target.onCompleted();
            }
          });
    }

    @Override
    public void mutateRow(
        MutateRowRequest request, StreamObserver<MutateRowResponse> responseObserver) {
      if (attemptCounter.getAndIncrement() < 2) {
        responseObserver.onError(new StatusRuntimeException(Status.UNAVAILABLE));
        return;
      }
      responseObserver.onNext(MutateRowResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }

    @Override
    public void mutateRows(
        MutateRowsRequest request, StreamObserver<MutateRowsResponse> responseObserver) {
      try {
        Thread.sleep(SERVER_LATENCY);
      } catch (InterruptedException e) {
      }
      responseObserver.onNext(MutateRowsResponse.getDefaultInstance());
      responseObserver.onCompleted();
    }

    public AtomicInteger getAttemptCounter() {
      return attemptCounter;
    }

    public AtomicInteger getResponseCounter() {
      return responseCounter;
    }
  }
}
