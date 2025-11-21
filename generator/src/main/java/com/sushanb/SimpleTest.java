/*
 * Copyright 2025 Google LLC
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
package com.sushanb;

import com.google.api.core.ApiFuture;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.TableId;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import com.google.common.base.Strings;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleTest {
  private static final String TABLE_NAME = "sushanb";

  private static final Logger logger = Logger.getLogger(SimpleTest.class.getName());

  public static BigtableDataClient createClient() throws IOException {
    BigtableDataSettings.Builder settingsBuilder =
        BigtableDataSettings.newBuilder().setProjectId("autonomous-mote-782").setInstanceId("test-sushanb");


    settingsBuilder
            .stubSettings()
            .setEndpoint("test-bigtable.sandbox.googleapis.com:443").build();

    BigtableDataSettings settings = settingsBuilder.build();
    BigtableDataClient client = BigtableDataClient.create(settings);
    logger.info("BigtableDataClient created successfully.");
    return client;
  }

  public static void performMutations(BigtableDataClient client) {
    int threadCount = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    AtomicInteger successCount = new AtomicInteger(0);
    AtomicInteger failureCount = new AtomicInteger(0);

    logger.info(
            String.format(
                    "Submitting %d mutations to a thread pool of size %d", 10000, 10));

    for (int i = 0; i < 100; i++) {
      final int index = i;
      executorService.submit(
              () -> {
                try {
                  String rowKey = "myrow-" + index;
                  RowMutation rowMutation =
                          RowMutation.create(TABLE_NAME, rowKey).setCell("cf12", "colq1", "val" + index);
                  client.mutateRow(rowMutation);
                  successCount.incrementAndGet();
                  if (index % 100 == 0) {
                    logger.info("Successfully mutated index: " + index);
                  }
                } catch (Exception e) {
                  failureCount.incrementAndGet();
                }
              });
    }

    logger.info("All tasks submitted. Shutting down executor...");
    executorService.shutdown();
    try {
      // Wait for all tasks to complete
      if (!executorService.awaitTermination(30, TimeUnit.MINUTES)) {
        logger.warning("Executor did not terminate in the specified time.");
        executorService.shutdownNow();
      } else {
        logger.info("All mutations completed.");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.info("All mutations completed.");

      executorService.shutdownNow();
    }

    logger.info(String.format("Mutation results: %d succeeded, %d failed.", successCount.get(), failureCount.get()));
  }


  public static void main(String[] args) throws IOException, InterruptedException {
    BigtableDataClient client = null;
    try {
      client = createClient();
      performMutations(client);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Failed to create Bigtable client", e);
    } finally {
      if (client != null) {
        client.close();
        logger.info("BigtableDataClient closed.");
      }
    }
    System.exit(0);
  }
}
