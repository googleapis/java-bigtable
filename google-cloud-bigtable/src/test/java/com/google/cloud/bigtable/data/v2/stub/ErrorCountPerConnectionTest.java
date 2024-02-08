/*
 * Copyright 2024 Google LLC
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

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.google.api.gax.core.FixedExecutorProvider;
import com.google.bigtable.v2.*;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.*;
import com.google.cloud.bigtable.stats.StatsRecorderWrapper;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class ErrorCountPerConnectionTest {
  private static final String SUCCESS_TABLE_NAME = "some-table";
  private static final String ERROR_TABLE_NAME = "nonexistent-table";
  private Server server;
  private final FakeService fakeService = new FakeService();
  private EnhancedBigtableStubSettings.Builder builder;
  private ArgumentCaptor<Runnable> runnableCaptor;
  private StatsRecorderWrapper statsRecorderWrapper;

  @Before
  public void setup() throws Exception {
    server = FakeServiceBuilder.create(fakeService).start();

    ScheduledExecutorService executors = Mockito.mock(ScheduledExecutorService.class);
    builder =
        BigtableDataSettings.newBuilderForEmulator(server.getPort())
            .stubSettings()
            .setBackgroundExecutorProvider(FixedExecutorProvider.create(executors))
            .setProjectId("fake-project")
            .setInstanceId("fake-instance");
    runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    System.out.println("rez1 " + executors + " 2 = " + builder);
    Mockito.when(
            executors.scheduleAtFixedRate(runnableCaptor.capture(), anyLong(), anyLong(), any()))
        .thenReturn(null);

    statsRecorderWrapper = Mockito.mock(StatsRecorderWrapper.class);
  }

  @After
  public void tearDown() throws Exception {
    if (server != null) {
      server.shutdown();
    }
  }

  @Test
  public void singleRead() throws Exception {
    EnhancedBigtableStub stub = EnhancedBigtableStub.create(builder.build());
    long errorCount = 0;

    for (int i = 0; i < 20; i++) {
      Query query;
      if (i % 3 == 0) {
        query = Query.create(ERROR_TABLE_NAME);
        errorCount += 1;
      } else {
        query = Query.create(SUCCESS_TABLE_NAME);
      }
      try {
        stub.readRowsCallable().call(query).iterator().hasNext();
      } catch (Exception e) {
        // noop
      }
    }
    ArgumentCaptor<Long> errorCountCaptor = ArgumentCaptor.forClass(long.class);
    Mockito.doNothing()
        .when(statsRecorderWrapper)
        .putAndRecordPerConnectionErrorCount(errorCountCaptor.capture());
    runInterceptorTasksAndAssertCount(1);
    List<Long> allErrorCounts = errorCountCaptor.getAllValues();
    assertThat(allErrorCounts.size()).isEqualTo(1);
    assertThat(allErrorCounts.get(0)).isEqualTo(errorCount);
  }

  @Test
  public void readWithTwoStubs() throws Exception {
    EnhancedBigtableStub stub1 = EnhancedBigtableStub.create(builder.build());
    EnhancedBigtableStub stub2 = EnhancedBigtableStub.create(builder.build());
    long errorCount1 = 0;
    long errorCount2 = 0;

    for (int i = 0; i < 20; i++) {
      Query successQuery = Query.create(SUCCESS_TABLE_NAME);
      Query errorQuery = Query.create(ERROR_TABLE_NAME);
      try {
        if (i % 3 == 0) {
          errorCount2 += 1;
          stub1.readRowsCallable().call(successQuery).iterator().hasNext();
          stub2.readRowsCallable().call(errorQuery).iterator().hasNext();
        } else {
          errorCount1 += 1;
          stub1.readRowsCallable().call(errorQuery).iterator().hasNext();
          stub2.readRowsCallable().call(successQuery).iterator().hasNext();
        }
      } catch (Exception e) {
        // noop
      }
    }
    ArgumentCaptor<Long> errorCountCaptor = ArgumentCaptor.forClass(long.class);
    Mockito.doNothing()
        .when(statsRecorderWrapper)
        .putAndRecordPerConnectionErrorCount(errorCountCaptor.capture());
    runInterceptorTasksAndAssertCount(2);

    List<Long> allErrorCounts = errorCountCaptor.getAllValues();
    assertThat(allErrorCounts.size()).isEqualTo(2);
    assertThat(allErrorCounts).containsExactly(errorCount1, errorCount2);
  }

  @Test
  public void readOverTwoPeriods() throws Exception {
    EnhancedBigtableStub stub = EnhancedBigtableStub.create(builder.build());
    long errorCount = 0;

    for (int i = 0; i < 20; i++) {
      Query query;
      if (i % 3 == 0) {
        query = Query.create(ERROR_TABLE_NAME);
        errorCount += 1;
      } else {
        query = Query.create(SUCCESS_TABLE_NAME);
      }
      try {
        stub.readRowsCallable().call(query).iterator().hasNext();
      } catch (Exception e) {
        // noop
      }
    }
    ArgumentCaptor<Long> errorCountCaptor = ArgumentCaptor.forClass(long.class);
    Mockito.doNothing()
        .when(statsRecorderWrapper)
        .putAndRecordPerConnectionErrorCount(errorCountCaptor.capture());
    runInterceptorTasksAndAssertCount(1);
    List<Long> allErrorCounts = errorCountCaptor.getAllValues();
    assertThat(allErrorCounts.size()).isEqualTo(1);
    assertThat(allErrorCounts.get(0)).isEqualTo(errorCount);

    errorCount = 0;

    for (int i = 0; i < 20; i++) {
      Query query;
      if (i % 3 == 0) {
        query = Query.create(SUCCESS_TABLE_NAME);
      } else {
        query = Query.create(ERROR_TABLE_NAME);
        errorCount += 1;
      }
      try {
        stub.readRowsCallable().call(query).iterator().hasNext();
      } catch (Exception e) {
        // noop
      }
    }
    errorCountCaptor = ArgumentCaptor.forClass(long.class);
    Mockito.doNothing()
        .when(statsRecorderWrapper)
        .putAndRecordPerConnectionErrorCount(errorCountCaptor.capture());
    runInterceptorTasksAndAssertCount(1);
    allErrorCounts = errorCountCaptor.getAllValues();
    assertThat(allErrorCounts.size()).isEqualTo(1);
    assertThat(allErrorCounts.get(0)).isEqualTo(errorCount);
  }

  @Test
  public void ignoreInactiveConnection() throws Exception {
    EnhancedBigtableStub.create(builder.build());

    ArgumentCaptor<Long> errorCountCaptor = ArgumentCaptor.forClass(long.class);
    Mockito.doNothing()
        .when(statsRecorderWrapper)
        .putAndRecordPerConnectionErrorCount(errorCountCaptor.capture());
    runInterceptorTasksAndAssertCount(1);
    List<Long> allErrorCounts = errorCountCaptor.getAllValues();
    assertThat(allErrorCounts).isEmpty();
  }

  @Test
  public void noFailedRequests() throws Exception {
    EnhancedBigtableStub stub = EnhancedBigtableStub.create(builder.build());

    for (int i = 0; i < 20; i++) {
      try {
        stub.readRowsCallable().call(Query.create(SUCCESS_TABLE_NAME)).iterator().hasNext();
      } catch (Exception e) {
        // noop
      }
    }
    ArgumentCaptor<Long> errorCountCaptor = ArgumentCaptor.forClass(long.class);
    Mockito.doNothing()
        .when(statsRecorderWrapper)
        .putAndRecordPerConnectionErrorCount(errorCountCaptor.capture());
    runInterceptorTasksAndAssertCount(1);
    List<Long> allErrorCounts = errorCountCaptor.getAllValues();
    assertThat(allErrorCounts.size()).isEqualTo(1);
    assertThat(allErrorCounts.get(0)).isEqualTo(0);
  }

  private void runInterceptorTasksAndAssertCount(int expectNumOfTasks) {
    int actualNumOfTasks = 0;
    for (Runnable runnable : runnableCaptor.getAllValues()) {
      if (runnable instanceof CountErrorsPerInterceptorTask) {
        ((CountErrorsPerInterceptorTask) runnable).setStatsRecorderWrapper(statsRecorderWrapper);
        runnable.run();
        actualNumOfTasks++;
      }
    }
    assertThat(actualNumOfTasks).isEqualTo(expectNumOfTasks);
  }

  static class FakeService extends BigtableGrpc.BigtableImplBase {

    private final AtomicInteger count = new AtomicInteger();

    @Override
    public void readRows(
        ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
      count.getAndIncrement();
      if (request.getTableName().contains(SUCCESS_TABLE_NAME)) {
        responseObserver.onNext(ReadRowsResponse.getDefaultInstance());
        responseObserver.onCompleted();
      } else {
        // Send a non-retriable error, since otherwise the client tries to use the mocked
        // ScheduledExecutorService
        // object for retyring, resulting in a hang.
        StatusRuntimeException exception = new StatusRuntimeException(Status.INTERNAL);
        responseObserver.onError(exception);
      }
    }
  }
}
