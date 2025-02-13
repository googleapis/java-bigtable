/*
 * Copyright 2019 Google LLC
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
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LargeRowIT {

  private static final Logger logger = Logger.getLogger(LargeRowIT.class.getName());

  @ClassRule
  public static final TestEnvRule testEnvRule = new TestEnvRule();

  @Test
  public void testWriteRead() throws Exception {
    String rowKey = UUID.randomUUID().toString();
    String familyId = testEnvRule.env().getFamilyId();

    byte[] largeValueBytes = new byte[100 * 1024 * 1024];
    Random random = new Random();
    random.nextBytes(largeValueBytes);
    ByteString largeValue = ByteString.copyFrom(largeValueBytes);

    // Create a 200 MB row
    logger.info("Sending large row, this will take awhile");
    for (int i = 0; i < 2; i++) {
      testEnvRule
          .env()
          .getDataClient()
          .mutateRowAsync(
              RowMutation.create(testEnvRule.env().getTableId(), rowKey)
                  .setCell(familyId, ByteString.copyFromUtf8("q" + i), largeValue))
          .get(10, TimeUnit.MINUTES);
    }

    logger.info("Reading large row, this will take awhile");
    // Read it back
    Row row =
        testEnvRule
            .env()
            .getDataClient()
            .readRowsCallable()
            .first()
            .call(Query.create(testEnvRule.env().getTableId()).rowKey(rowKey));

    assertThat(row.getCells()).hasSize(2);
    assertThat(row.getCells().get(0).getValue()).isEqualTo(largeValue);
    assertThat(row.getCells().get(1).getValue()).isEqualTo(largeValue);
  }


  @Test
  public void testLargeRowRead() throws Exception {
    String rowKey = UUID.randomUUID().toString();
    String familyId = testEnvRule.env().getFamilyId();

    byte[] largeValueBytes = new byte[100 * 1024 * 1024];
    Random random = new Random();
    random.nextBytes(largeValueBytes);
    ByteString largeValue = ByteString.copyFrom(largeValueBytes);

    // Create a 200 MB row
    logger.info("Sending large row, this will take awhile");
    for (int i = 0; i < 2; i++) {
      testEnvRule
          .env()
          .getDataClient()
          .mutateRowAsync(
              RowMutation.create(testEnvRule.env().getTableId(), rowKey)
                  .setCell(familyId, ByteString.copyFromUtf8("q" + i), largeValue))
          .get(10, TimeUnit.MINUTES);
    }

    logger.info("Reading large row, this will take awhile");
    // Read it back
    Row row =
        testEnvRule
            .env()
            .getDataClient()
            .readRowsCallable()
            .first()
            .call(Query.create(testEnvRule.env().getTableId()).rowKey(rowKey));

    assertThat(row.getCells()).hasSize(2);
    assertThat(row.getCells().get(0).getValue()).isEqualTo(largeValue);
    assertThat(row.getCells().get(1).getValue()).isEqualTo(largeValue);
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
    public void onStart(StreamController controller) {
    }

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

  @Test
  public void read()  throws Throwable {
// create test case
    BigtableDataClient client = testEnvRule.env().getDataClient();

    String tableId = testEnvRule.env().getTableId();
    String familyId = testEnvRule.env().getFamilyId();
    String rowKeyPrefix = "rowKey-";
    long timestampMicros = System.currentTimeMillis() * 1_000;

    // small row creations
    client.bulkMutateRows(
        BulkMutation.create(tableId)
            .add(RowMutationEntry.create("r1")
                .setCell(familyId, "qualifier", timestampMicros, "my-value"))
            .add(RowMutationEntry.create("r2")
                .setCell(familyId, "qualifier", timestampMicros, "my-value"))
            .add(RowMutationEntry.create("r4")
                .setCell(familyId, "qualifier", timestampMicros, "my-value"))
            .add(RowMutationEntry.create("r5")
                .setCell(familyId, "qualifier", timestampMicros, "my-value"))
            .add(RowMutationEntry.create("r6")
                .setCell(familyId, "qualifier", timestampMicros, "my-value")));

    Row expectedRow1 =
        Row.create(
            ByteString.copyFromUtf8("r1"),
            ImmutableList.of(
                RowCell.create(
                    familyId,
                    ByteString.copyFromUtf8("qualifier"),
                    timestampMicros,
                    ImmutableList.<String>of(),
                    ByteString.copyFromUtf8("my-value"))));

    Row expectedRow2 =
        Row.create(
            ByteString.copyFromUtf8("r2"),
            ImmutableList.of(
                RowCell.create(
                    familyId,
                    ByteString.copyFromUtf8("qualifier"),
                    timestampMicros,
                    ImmutableList.<String>of(),
                    ByteString.copyFromUtf8("my-value"))));

    Row expectedRow4 =
        Row.create(
            ByteString.copyFromUtf8("r4"),
            ImmutableList.of(
                RowCell.create(
                    familyId,
                    ByteString.copyFromUtf8("qualifier"),
                    timestampMicros,
                    ImmutableList.<String>of(),
                    ByteString.copyFromUtf8("my-value"))));

    assertThat(
        ImmutableList.copyOf(
            client.readRows(
                Query.create(tableId)
                    .range(ByteStringRange.unbounded().startClosed("r1").endOpen("r3")))))
        .containsExactly(expectedRow1, expectedRow2);

    // --- large row creation START----
    byte[] largeValueBytes = new byte[100 * 1024 * 1024];
    ByteString largeValue = ByteString.copyFrom(largeValueBytes);

    int threadCount = 5;
    int offset = 0;
    int iterations = 100;
    ExecutorService executor = Executors.newFixedThreadPool(4);

    for (int i = offset; i < threadCount + offset; i++) {
      for (int j = 0; j < iterations; j++) {
        timestampMicros = System.currentTimeMillis() * 1_000;
        ByteString qualifier = ByteString.copyFromUtf8("qualifier1_" + String.valueOf(i) + "_" + String.valueOf(j));
        executor.execute(
            () -> {
              client.bulkMutateRows(
                  BulkMutation.create(tableId)
                      .add(RowMutationEntry.create("r3")
                          .setCell(familyId, qualifier,largeValue)));
            });
      }

    }
    executor.shutdown();
    executor.awaitTermination(2, TimeUnit.MINUTES);
    // --- large row creation END ----

    // sync
    assertThat(
        ImmutableList.copyOf(
            client.readRows(
                Query.create(tableId)
                    .range(ByteStringRange.unbounded().startClosed("r1").endOpen("r3")))))
        .containsExactly(expectedRow1, expectedRow2);

    assertThat(
        ImmutableList.copyOf(
            client.readRows(
                Query.create(tableId)
                    .range(ByteStringRange.unbounded().startClosed("r1").endClosed("r3")))))
        .containsExactly(expectedRow1, expectedRow2);

    assertThat(
        ImmutableList.copyOf(
            client.readRows(
                Query.create(tableId)
                    .range(ByteStringRange.unbounded().startClosed("r1").endClosed("r4")))))
        .containsExactly(expectedRow1, expectedRow2, expectedRow4);

    //async
    AccumulatingObserver observer = new AccumulatingObserver();
    Query query = Query.create(tableId).range("r1", "r3");
    client.readRowsAsync(query, observer);
    observer.awaitCompletion();
    assertThat(observer.responses).containsExactly(expectedRow1, expectedRow2);

    AccumulatingObserver observer2 = new AccumulatingObserver();
    Query query2 = Query.create(tableId).range("r1", "r5");
    client.readRowsAsync(query2, observer2);
    observer2.awaitCompletion();
    assertThat(observer2.responses).containsExactly(expectedRow1, expectedRow2, expectedRow4);

  }
}

// how do you share the failed rows with the client
// how do I point these to point to the testing env - https://source.corp.google.com/piper///depot/google3/bigtable/anviltop/test/kokoro/gcp_ubuntu/github/nightly_test/java_bigtable_latest.sh?q=bigtable-prod-it&sq=(file:google3%2Fbigtable%2Fanviltop%20OR%20file:production%2Fsisyphus%2Fcloud_bigtable%20OR%20file:%2F%2Fdepot%2Fgoogle3%2Fgoogle%2Fbigtable%20OR%20file:%2F%2Fdepot%2Fgoogle3%2Fbigtable%2Fperftest%2Fsplode%20OR%20file:production%2Fborg%2Fcloud-bigtable%20OR%20file:configs%2Fmonitoring%2Fcloud_pulse_monarch%2Fbigtable%20OR%20file:configs%2Fmonitoring%2Fcloud_bigtable%2Fconfig)%20AND%20-f:anviltop_experiments_branch
// weekly share the email
// bigtable  -
// status update
// who will help with the project
