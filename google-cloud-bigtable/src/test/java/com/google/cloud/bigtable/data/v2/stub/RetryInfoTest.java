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

import static com.google.cloud.bigtable.gaxx.retrying.RetryInfoRetryAlgorithm.RETRY_INFO_KEY;
import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.InternalException;
import com.google.api.gax.rpc.UnavailableException;
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
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.ConditionalRowMutation;
import com.google.cloud.bigtable.data.v2.models.Filters;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.ReadModifyWriteRow;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Queues;
import com.google.protobuf.Duration;
import com.google.rpc.RetryInfo;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcServerRule;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RetryInfoTest {

  @Rule public GrpcServerRule serverRule = new GrpcServerRule();

  private FakeBigtableService service;
  private BigtableDataClient client;

  private AtomicInteger attemptCounter = new AtomicInteger();
  private Duration delay = Duration.newBuilder().setSeconds(1).setNanos(0).build();

  @Before
  public void setUp() throws IOException {
    service = new FakeBigtableService();
    serverRule.getServiceRegistry().addService(service);

    BigtableDataSettings.Builder settings =
        BigtableDataSettings.newBuilder()
            .setProjectId("fake-project")
            .setInstanceId("fake-instance")
            .setCredentialsProvider(NoCredentialsProvider.create());

    settings
        .stubSettings()
        .setTransportChannelProvider(
            FixedTransportChannelProvider.create(
                GrpcTransportChannel.create(serverRule.getChannel())))
        // channel priming doesn't work with FixedTransportChannelProvider. Disable it for the test
        .setRefreshingChannel(false)
        .build();

    this.client = BigtableDataClient.create(settings.build());
  }

  @Test
  public void testReadRow() {
    createRetryableExceptionWithDelay(delay);
    Stopwatch stopwatch = Stopwatch.createStarted();
    client.readRow("table", "row");
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  @Test
  public void testReadRowNonRetryableErrorWithRetryInfo() {
    createNonRetryableExceptionWithDelay(delay);
    Stopwatch stopwatch = Stopwatch.createStarted();
    client.readRow("table", "row");
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  @Test
  public void testReadRows() {
    createRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();
    client.readRows(Query.create("table")).iterator().hasNext();
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  @Test
  public void testReadRowsNonRetraybleErrorWithRetryInfo() {
    createNonRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();
    client.readRows(Query.create("table")).iterator().hasNext();
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  @Test
  public void testMutateRows() {
    createRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();

    client.bulkMutateRows(
        BulkMutation.create("fake-table")
            .add(RowMutationEntry.create("row-key-1").setCell("cf", "q", "v")));
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  @Test
  public void testMutateRowsNonRetryableErrorWithRetryInfo() {
    createNonRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();

    client.bulkMutateRows(
        BulkMutation.create("fake-table")
            .add(RowMutationEntry.create("row-key-1").setCell("cf", "q", "v")));
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  @Test
  public void testMutateRow() {
    createRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();
    client.mutateRow(RowMutation.create("table", "key").setCell("cf", "q", "v"));
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(1));
  }

  @Test
  public void testMutateRowNonRetryableErrorWithRetryInfo() {
    createNonRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();
    client.mutateRow(RowMutation.create("table", "key").setCell("cf", "q", "v"));
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(1));
  }

  @Test
  public void testSampleRowKeys() {
    createRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();

    client.sampleRowKeys("table");
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  @Test
  public void testSampleRowKeysNonRetryableErrorWithRetryInfo() {
    createNonRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();

    client.sampleRowKeys("table");
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  @Test
  public void testCheckAndMutateRow() {
    createRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();
    client.checkAndMutateRow(
        ConditionalRowMutation.create("table", "key")
            .condition(Filters.FILTERS.value().regex("old-value"))
            .then(Mutation.create().setCell("cf", "q", "v")));
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  @Test
  public void testReadModifyWrite() {
    createRetryableExceptionWithDelay(delay);

    Stopwatch stopwatch = Stopwatch.createStarted();
    client.readModifyWriteRow(ReadModifyWriteRow.create("table", "row").append("cf", "q", "v"));
    stopwatch.stop();

    assertThat(attemptCounter.get()).isEqualTo(2);
    assertThat(stopwatch.elapsed()).isAtLeast(java.time.Duration.ofSeconds(delay.getSeconds()));
  }

  private void createRetryableExceptionWithDelay(Duration delay) {
    Metadata trailers = new Metadata();
    RetryInfo retryInfo = RetryInfo.newBuilder().setRetryDelay(delay).build();
    trailers.put(RETRY_INFO_KEY, retryInfo);

    ApiException exception =
        new UnavailableException(
            new StatusRuntimeException(Status.UNAVAILABLE, trailers),
            GrpcStatusCode.of(Status.Code.UNAVAILABLE),
            true);

    service.expectations.add(exception);
  }

  private void createNonRetryableExceptionWithDelay(Duration delay) {
    Metadata trailers = new Metadata();
    RetryInfo retryInfo =
        RetryInfo.newBuilder()
            .setRetryDelay(Duration.newBuilder().setSeconds(1).setNanos(0).build())
            .build();
    trailers.put(RETRY_INFO_KEY, retryInfo);

    ApiException exception =
        new InternalException(
            new StatusRuntimeException(Status.INTERNAL, trailers),
            GrpcStatusCode.of(Status.Code.INTERNAL),
            false);

    service.expectations.add(exception);
  }

  private class FakeBigtableService extends BigtableGrpc.BigtableImplBase {
    Queue<Exception> expectations = Queues.newArrayDeque();

    @Override
    public void readRows(
        ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
      attemptCounter.incrementAndGet();
      if (expectations.isEmpty()) {
        responseObserver.onNext(ReadRowsResponse.getDefaultInstance());
        responseObserver.onCompleted();
      } else {
        Exception expectedRpc = expectations.poll();
        responseObserver.onError(expectedRpc);
      }
    }

    @Override
    public void mutateRow(
        MutateRowRequest request, StreamObserver<MutateRowResponse> responseObserver) {
      attemptCounter.incrementAndGet();
      if (expectations.isEmpty()) {
        responseObserver.onNext(MutateRowResponse.getDefaultInstance());
        responseObserver.onCompleted();
      } else {
        Exception expectedRpc = expectations.poll();
        responseObserver.onError(expectedRpc);
      }
    }

    @Override
    public void mutateRows(
        MutateRowsRequest request, StreamObserver<MutateRowsResponse> responseObserver) {
      attemptCounter.incrementAndGet();
      if (expectations.isEmpty()) {
        MutateRowsResponse.Builder builder = MutateRowsResponse.newBuilder();
        for (int i = 0; i < request.getEntriesCount(); i++) {
          builder.addEntriesBuilder().setIndex(i);
        }
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
      } else {
        Exception expectedRpc = expectations.poll();
        responseObserver.onError(expectedRpc);
      }
    }

    @Override
    public void sampleRowKeys(
        SampleRowKeysRequest request, StreamObserver<SampleRowKeysResponse> responseObserver) {
      attemptCounter.incrementAndGet();
      if (expectations.isEmpty()) {
        responseObserver.onNext(SampleRowKeysResponse.getDefaultInstance());
        responseObserver.onCompleted();
      } else {
        Exception expectedRpc = expectations.poll();
        responseObserver.onError(expectedRpc);
      }
    }

    @Override
    public void checkAndMutateRow(
        CheckAndMutateRowRequest request,
        StreamObserver<CheckAndMutateRowResponse> responseObserver) {
      attemptCounter.incrementAndGet();
      if (expectations.isEmpty()) {
        responseObserver.onNext(CheckAndMutateRowResponse.getDefaultInstance());
        responseObserver.onCompleted();
      } else {
        Exception expectedRpc = expectations.poll();
        responseObserver.onError(expectedRpc);
      }
    }

    @Override
    public void readModifyWriteRow(
        ReadModifyWriteRowRequest request,
        StreamObserver<ReadModifyWriteRowResponse> responseObserver) {
      attemptCounter.incrementAndGet();
      if (expectations.isEmpty()) {
        responseObserver.onNext(ReadModifyWriteRowResponse.getDefaultInstance());
        responseObserver.onCompleted();
      } else {
        Exception expectedRpc = expectations.poll();
        responseObserver.onError(expectedRpc);
      }
    }
  }
}
