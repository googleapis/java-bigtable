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

import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.protobuf.ByteString;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LargeRowIT {
  private static final Logger logger = Logger.getLogger(LargeRowIT.class.getName());

  @ClassRule public static final TestEnvRule testEnvRule = new TestEnvRule();

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
}
