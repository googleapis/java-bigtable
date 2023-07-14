/*
 * Copyright 2018 Google LLC
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
package com.google.cloud.bigtable.data.v2.it;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Range.ByteStringRange;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.cloud.bigtable.test_helpers.env.EmulatorEnv;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ReadIT {
  private String prefix;

  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  @Before
  public void setUp() {
    prefix = UUID.randomUUID().toString();
  }

  @Test
  public void isRowExists() throws Exception {
    String rowKey = prefix + "-test-row-key";
    String tableId = testEnvRule.env().getTableId();
    testEnvRule
        .env()
        .getDataClient()
        .mutateRow(
            RowMutation.create(tableId, rowKey)
                .setCell(testEnvRule.env().getFamilyId(), "qualifier", "value"));

    assertThat(testEnvRule.env().getDataClient().exists(tableId, rowKey)).isTrue();

    String nonExistingKey = prefix + "non-existing-key";
    assertThat(testEnvRule.env().getDataClient().exists(tableId, nonExistingKey)).isFalse();

    // Async
    assertThat(testEnvRule.env().getDataClient().existsAsync(tableId, rowKey).get()).isTrue();
  }

  @Test
  public void readEmpty() throws Throwable {
    String uniqueKey = prefix + "-readEmpty";

    Query query = Query.create(testEnvRule.env().getTableId()).rowKey(uniqueKey);

    // Sync
    ArrayList<Row> rows = Lists.newArrayList(testEnvRule.env().getDataClient().readRows(query));
    assertThat(rows).isEmpty();

    // Async
    AccumulatingObserver observer = new AccumulatingObserver();
    testEnvRule.env().getDataClient().readRowsAsync(query, observer);
    observer.awaitCompletion();
    assertThat(observer.responses).isEmpty();
  }

  @Test
  public void read() throws Throwable {
    int numRows = 5;
    List<Row> expectedRows = Lists.newArrayList();
    String uniqueKey = prefix + "-read";

    long timestampMicros = System.currentTimeMillis() * 1_000;

    for (int i = 0; i < numRows; i++) {
      testEnvRule
          .env()
          .getDataClient()
          .mutateRowCallable()
          .call(
              RowMutation.create(testEnvRule.env().getTableId(), uniqueKey + "-" + i)
                  .setCell(testEnvRule.env().getFamilyId(), "q", timestampMicros, "my-value"));

      expectedRows.add(
          Row.create(
              ByteString.copyFromUtf8(uniqueKey + "-" + i),
              ImmutableList.of(
                  RowCell.create(
                      testEnvRule.env().getFamilyId(),
                      ByteString.copyFromUtf8("q"),
                      timestampMicros,
                      ImmutableList.<String>of(),
                      ByteString.copyFromUtf8("my-value")))));
    }

    String tableId = testEnvRule.env().getTableId();

    // Sync
    Query query = Query.create(tableId).range(uniqueKey + "-0", uniqueKey + "-" + numRows);
    ArrayList<Row> actualResults =
        Lists.newArrayList(testEnvRule.env().getDataClient().readRows(query));

    assertThat(actualResults).containsExactlyElementsIn(expectedRows);

    // Async
    AccumulatingObserver observer = new AccumulatingObserver();
    testEnvRule.env().getDataClient().readRowsAsync(query, observer);
    observer.awaitCompletion();
    assertThat(observer.responses).containsExactlyElementsIn(expectedRows);

    // Point Sync
    Row actualRow =
        testEnvRule.env().getDataClient().readRow(tableId, expectedRows.get(0).getKey());
    assertThat(actualRow).isEqualTo(expectedRows.get(0));

    // Point Async
    ApiFuture<Row> actualRowFuture =
        testEnvRule.env().getDataClient().readRowAsync(tableId, expectedRows.get(0).getKey());
    assertThat(actualRowFuture.get()).isEqualTo(expectedRows.get(0));
  }

  @Test
  public void rangeQueries() {
    BigtableDataClient client = testEnvRule.env().getDataClient();
    String tableId = testEnvRule.env().getTableId();
    String familyId = testEnvRule.env().getFamilyId();
    String uniqueKey = prefix + "-range-queries";
    String keyA = uniqueKey + "-" + "a";
    String keyZ = uniqueKey + "-" + "z";

    long timestampMicros = System.currentTimeMillis() * 1_000;

    client.bulkMutateRows(
        BulkMutation.create(tableId)
            .add(RowMutationEntry.create(keyA).setCell(familyId, "", timestampMicros, "A"))
            .add(RowMutationEntry.create(keyZ).setCell(familyId, "", timestampMicros, "Z")));

    Row expectedRowA =
        Row.create(
            ByteString.copyFromUtf8(keyA),
            ImmutableList.of(
                RowCell.create(
                    testEnvRule.env().getFamilyId(),
                    ByteString.copyFromUtf8(""),
                    timestampMicros,
                    ImmutableList.<String>of(),
                    ByteString.copyFromUtf8("A"))));

    Row expectedRowZ =
        Row.create(
            ByteString.copyFromUtf8(keyZ),
            ImmutableList.of(
                RowCell.create(
                    testEnvRule.env().getFamilyId(),
                    ByteString.copyFromUtf8(""),
                    timestampMicros,
                    ImmutableList.<String>of(),
                    ByteString.copyFromUtf8("Z"))));

    // Closed/Open
    assertThat(
            ImmutableList.copyOf(
                client.readRows(
                    Query.create(tableId)
                        .range(ByteStringRange.unbounded().startClosed(keyA).endOpen(keyZ)))))
        .containsExactly(expectedRowA);

    // Closed/Closed
    assertThat(
            ImmutableList.copyOf(
                client.readRows(
                    Query.create(tableId)
                        .range(ByteStringRange.unbounded().startClosed(keyA).endClosed(keyZ)))))
        .containsExactly(expectedRowA, expectedRowZ);

    // Open/Closed
    assertThat(
            ImmutableList.copyOf(
                client.readRows(
                    Query.create(tableId)
                        .range(ByteStringRange.unbounded().startOpen(keyA).endClosed(keyZ)))))
        .containsExactly(expectedRowZ);

    // Open/Open
    assertThat(
            ImmutableList.copyOf(
                client.readRows(
                    Query.create(tableId)
                        .range(ByteStringRange.unbounded().startOpen(keyA).endOpen(keyZ)))))
        .isEmpty();
  }

  @Test
  public void reversed() {
    assume()
        .withMessage("reverse scans are not supported in the emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);
    BigtableDataClient client = testEnvRule.env().getDataClient();
    String tableId = testEnvRule.env().getTableId();
    String familyId = testEnvRule.env().getFamilyId();
    String uniqueKey = prefix + "-rev-queries";
    String keyA = uniqueKey + "-" + "a";
    String keyB = uniqueKey + "-" + "b";
    String keyC = uniqueKey + "-" + "c";

    long timestampMicros = System.currentTimeMillis() * 1_000;

    client.bulkMutateRows(
        BulkMutation.create(tableId)
            .add(RowMutationEntry.create(keyA).setCell(familyId, "", timestampMicros, "A"))
            .add(RowMutationEntry.create(keyB).setCell(familyId, "", timestampMicros, "B"))
            .add(RowMutationEntry.create(keyC).setCell(familyId, "", timestampMicros, "C")));

    Row expectedRowA =
        Row.create(
            ByteString.copyFromUtf8(keyA),
            ImmutableList.of(
                RowCell.create(
                    testEnvRule.env().getFamilyId(),
                    ByteString.copyFromUtf8(""),
                    timestampMicros,
                    ImmutableList.<String>of(),
                    ByteString.copyFromUtf8("A"))));

    Row expectedRowB =
        Row.create(
            ByteString.copyFromUtf8(keyB),
            ImmutableList.of(
                RowCell.create(
                    testEnvRule.env().getFamilyId(),
                    ByteString.copyFromUtf8(""),
                    timestampMicros,
                    ImmutableList.<String>of(),
                    ByteString.copyFromUtf8("B"))));
    Row expectedRowC =
        Row.create(
            ByteString.copyFromUtf8(keyC),
            ImmutableList.of(
                RowCell.create(
                    testEnvRule.env().getFamilyId(),
                    ByteString.copyFromUtf8(""),
                    timestampMicros,
                    ImmutableList.<String>of(),
                    ByteString.copyFromUtf8("C"))));

    assertThat(
            ImmutableList.copyOf(
                client.readRows(
                    Query.create(tableId).reversed(true).range(ByteStringRange.prefix(uniqueKey)))))
        .containsExactly(expectedRowC, expectedRowB, expectedRowA)
        .inOrder();

    assertThat(
            ImmutableList.copyOf(
                client.readRows(
                    Query.create(tableId)
                        .reversed(true)
                        .range(ByteStringRange.prefix(uniqueKey))
                        .limit(2))))
        .containsExactly(expectedRowC, expectedRowB)
        .inOrder();

    assertThat(
            ImmutableList.copyOf(
                client.readRows(
                    Query.create(tableId)
                        .reversed(true)
                        .range(ByteStringRange.unbounded().endOpen(keyC))
                        .limit(2))))
        .containsExactly(expectedRowB, expectedRowA)
        .inOrder();
  }

  @Test
  public void readSingleNonexistentAsyncCallback() throws Exception {
    ApiFuture<Row> future =
        testEnvRule
            .env()
            .getDataClient()
            .readRowAsync(testEnvRule.env().getTableId(), "somenonexistentkey");

    final AtomicReference<Throwable> unexpectedError = new AtomicReference<>();
    final AtomicBoolean found = new AtomicBoolean();
    final CountDownLatch latch = new CountDownLatch(1);

    ApiFutures.addCallback(
        future,
        new ApiFutureCallback<Row>() {
          @Override
          public void onFailure(Throwable t) {
            unexpectedError.set(t);
            latch.countDown();
          }

          @Override
          public void onSuccess(Row result) {
            found.set(true);
            latch.countDown();
          }
        },
        MoreExecutors.directExecutor());

    latch.await(1, TimeUnit.MINUTES);

    if (unexpectedError.get() != null) {
      throw new RuntimeException("Unexpected async error", unexpectedError.get());
    }
    assertThat(found.get()).isTrue();
  }

  static class AccumulatingObserver implements ResponseObserver<Row> {
    final List<Row> responses = Lists.newArrayList();
    final SettableApiFuture<Void> completionFuture = SettableApiFuture.create();

    void awaitCompletion() throws Throwable {
      try {
        completionFuture.get(10, TimeUnit.MINUTES);
      } catch (ExecutionException e) {
        throw e.getCause();
      }
    }

    @Override
    public void onStart(StreamController controller) {}

    @Override
    public void onResponse(Row row) {
      responses.add(row);
    }

    @Override
    public void onError(Throwable t) {
      completionFuture.setException(t);
    }

    @Override
    public void onComplete() {
      completionFuture.set(null);
    }
  }
}
