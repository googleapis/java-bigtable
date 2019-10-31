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
package com.google.cloud.bigtable.data.v2.internal;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FallbackToCloudpathTest {
  private final static int NUM_ROWS = 50 * 1000;
  private final static int MAX_WAIT = 5;  // in sec

  private String prefix;

  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  @Before
  public void setUp() {
    prefix = UUID.randomUUID().toString() + "-read-";
  }

  @After
  public void tearDown() {
    
  }

  @Test
  public void read() {
    long timestampMicros = System.currentTimeMillis() * 1_000;
    String tableId = testEnvRule.env().getTableId();
    String familyId = testEnvRule.env().getFamilyId();
    BigtableDataClient client = testEnvRule.env().getDataClient();
    for (int i = 0; i < NUM_ROWS; i++) {
      client.mutateRowCallable()
          .call(RowMutation.create(tableId, prefix + i)
              .setCell(familyId,"q", timestampMicros, "my-value"));
    }

    // Read the rows inserted
    int count = fetchRows(client, tableId);
    assertEquals(NUM_ROWS, count);

    // start a thread to inject failures
    CountDownLatch callbackLatch = new CountDownLatch(1);
    new FailureInjector(callbackLatch).start();
    // wait for the failures injection to finish
    try {
      callbackLatch.await(MAX_WAIT, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
      fail("It took more than a few sec to finish injection of failure!");
    }

    // Read the rows again
    int count = fetchRows(client, tableId);
    assertEquals(NUM_ROWS, count);
  }

  private int fetchRows(BigtableDataClient client, String tableId) {
    Query query = Query.create(tableId).range(prefix + "0", null);
    ServerStream<Row> stream = client.readRows(query);
    // record the last part of the key - which is an int in the range [0, NUM_ROWS)
    int[] ids = new int[NUM_ROWS];
    for (int i = 0; i < NUM_ROWS; i++) ids[i] = 0;
    long start = System.currentTimeMillis();
    int count = 0;
    for (Row row : stream) {
      String[] parts = (new String(row.getKey().toByteArray())).split("-");
      int index = Integer.parseInt(parts[parts.length - 1]);
      if (ids[index] != 0) {
        fail("got the same row multiple times!");
      }
      ids[index] = 1;
      count++;
    }
    long duration = System.currentTimeMillis() - start;
    System.out.println("Completed reading of " + count + " rows in time(ms): " + duration);
    return count;
  }

  class FailureInjector extends Thread {
    private final CountDownLatch callbackLatch;

    FailureInjector(CountDownLatch c) {
      callbackLatch = c;
    }

    public void run() {
      String cmd = "sysctl -w net.ipv6.conf.eth0.accept_ra_rt_info_max_plen=0 "
          + "&& ip -6 route del 2001:4860:8040::/42 dev eth0 ";
      ProcessBuilder processBuilder = new ProcessBuilder();
      processBuilder.command("sh", "-c", "hostname");

      try {
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
          System.out.println(line);
        }

        int exitCode = process.waitFor();
        System.out.println("Exited with error code : " + exitCode);
        callbackLatch.countDown();
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

