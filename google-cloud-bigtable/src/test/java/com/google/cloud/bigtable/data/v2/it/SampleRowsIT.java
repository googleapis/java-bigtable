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

import static com.google.cloud.bigtable.misc_utilities.AuthorizedViewTestHelper.AUTHORIZED_VIEW_COLUMN_QUALIFIER;
import static com.google.cloud.bigtable.misc_utilities.AuthorizedViewTestHelper.AUTHORIZED_VIEW_ROW_PREFIX;
import static com.google.cloud.bigtable.misc_utilities.AuthorizedViewTestHelper.createTestAuthorizedView;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.bigtable.admin.v2.models.AuthorizedView;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.KeyOffset;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.test_helpers.env.EmulatorEnv;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SampleRowsIT {
  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  @Test
  public void test() throws InterruptedException, ExecutionException, TimeoutException {
    BigtableDataClient client = testEnvRule.env().getDataClient();
    String rowPrefix = UUID.randomUUID().toString();

    // Create some data so that sample row keys has something to show
    List<ApiFuture<?>> futures = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      ApiFuture<Void> future =
          client.mutateRowAsync(
              RowMutation.create(testEnvRule.env().getTableId(), rowPrefix + "-" + i)
                  .setCell(testEnvRule.env().getFamilyId(), "", "value"));
      futures.add(future);
    }
    ApiFutures.allAsList(futures).get(1, TimeUnit.MINUTES);

    ApiFuture<List<KeyOffset>> future = client.sampleRowKeysAsync(testEnvRule.env().getTableId());

    List<KeyOffset> results = future.get(1, TimeUnit.MINUTES);

    assertThat(results).isNotEmpty();
    assertThat(results.get(results.size() - 1).getOffsetBytes()).isGreaterThan(0L);
  }

  @Test
  public void testOnAuthorizedView()
      throws InterruptedException, ExecutionException, TimeoutException {
    assume()
        .withMessage("AuthorizedView is not supported on Emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    AuthorizedView testAuthorizedView = createTestAuthorizedView(testEnvRule);

    BigtableDataClient client = testEnvRule.env().getDataClient();
    String rowPrefix = AUTHORIZED_VIEW_ROW_PREFIX + UUID.randomUUID();
    String rowPrefixOutsideAuthorizedView = UUID.randomUUID() + "-outside-authorized-view";

    // Create some data so that sample row keys has something to show
    List<ApiFuture<?>> futures = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      ApiFuture<Void> future =
          client.mutateRowAsync(
              RowMutation.create(testEnvRule.env().getTableId(), rowPrefix + "-" + i)
                  .setCell(
                      testEnvRule.env().getFamilyId(), AUTHORIZED_VIEW_COLUMN_QUALIFIER, "value"));
      futures.add(future);
      ApiFuture<Void> futureOutsideAuthorizedView =
          client.mutateRowAsync(
              RowMutation.create(
                      testEnvRule.env().getTableId(), rowPrefixOutsideAuthorizedView + "-" + i)
                  .setCell(
                      testEnvRule.env().getFamilyId(), AUTHORIZED_VIEW_COLUMN_QUALIFIER, "value"));
      futures.add(futureOutsideAuthorizedView);
    }
    ApiFutures.allAsList(futures).get(1, TimeUnit.MINUTES);

    ApiFuture<List<KeyOffset>> future = client.sampleRowKeysAsync(testEnvRule.env().getTableId());

    List<KeyOffset> results = future.get(1, TimeUnit.MINUTES);

    assertThat(results).isNotEmpty();
    assertThat(results.get(results.size() - 1).getOffsetBytes()).isGreaterThan(0L);

    testEnvRule
        .env()
        .getTableAdminClient()
        .deleteAuthorizedView(testEnvRule.env().getTableId(), testAuthorizedView.getId());
  }
}
