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
package com.google.cloud.bigtable;

import static java.lang.Thread.sleep;

import com.google.cloud.bigtable.data.v2.*;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.protobuf.ByteString;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceConfiguration;
import io.opencensus.exporter.trace.stackdriver.StackdriverTraceExporter;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.samplers.Samplers;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SmokeTest {

  private static final Logger logger = Logger.getLogger(SmokeTest.class.getName());

  public static void main(String[] args) {
    String projectId = "autonomous-mote-782"; // my-gcp-project-id
    String instanceId = System.getProperty("bigtable.instance"); // my-bigtable-instance-id
    String tableId = System.getProperty("bigtable.table"); // my-bigtable-table-id

    System.out.println(
        "System property directpath-data-endpoint: "
            + System.getProperty("bigtable.directpath-data-endpoint"));
    System.out.println("System property instance id: " + instanceId);
    System.out.println("System property table id: " + tableId);
    quickstart(projectId, instanceId, tableId);
  }

  public static void quickstart(String projectId, String instanceId, String tableId) {
    try {
      StackdriverTraceExporter.createAndRegister(
          StackdriverTraceConfiguration.builder().setProjectId(projectId).build());

      Tracing.getTraceConfig()
          .updateActiveTraceParams(
              Tracing.getTraceConfig()
                  .getActiveTraceParams()
                  .toBuilder()
                  .setSampler(Samplers.probabilitySampler(1))
                  .build());
    } catch (Exception exception) {
      System.err.println("failed to setup tracing.");
    }

    BigtableDataSettings.Builder settings =
        BigtableDataSettings.newBuilder().setProjectId(projectId).setInstanceId(instanceId);
    settings.stubSettings().setEndpoint("test-bigtable.sandbox.googleapis.com:443");

    // Initialize client that will be used to send requests. This client only needs to be created
    // once, and can be reused for multiple requests. After completing all of your requests, call
    // the "close" method on the client to safely clean up any remaining background resources.
    try (BigtableDataClient dataClient = BigtableDataClient.create(settings.build())) {

      String rowKey = UUID.randomUUID().toString();
      String familyId = "cf";

      byte[] largeValueBytes = new byte[8];
      Random random = new Random();
      random.nextBytes(largeValueBytes);
      ByteString largeValue = ByteString.copyFrom(largeValueBytes);

      // Create a 200 MB row
      logger.info("Sending small row, this will take awhile");
      for (int i = 0; i < 200; i++) {
        dataClient
            .mutateRowAsync(
                RowMutation.create(tableId, rowKey)
                    .setCell(familyId, ByteString.copyFromUtf8("q" + i), largeValue))
            .get(10, TimeUnit.MINUTES);
      }

      logger.info("Reading large row, this will take awhile");
      // Read it back
      Row row = dataClient.readRowsCallable().first().call(Query.create(tableId).rowKey(rowKey));

      assert row.getCells().size() == 2;
      assert row.getCells().get(0).getValue() == largeValue;
      assert row.getCells().get(1).getValue() == largeValue;
        /*      //while(true) {
          String rowKey = String.valueOf(UUID.randomUUID());
          RowMutation rowMutation = RowMutation.create(tableId, rowKey)
              .setCell("cf", "q", "myVal")
              .setCell("cf", "q2", "myVal2")
              .setCell("cf", "q3", "myVal3")
              .setCell("cf", "q4", 0x12345678);
          System.out.println("Create a single row");
          dataClient.mutateRowAsync(rowMutation).get(1, TimeUnit.MINUTES);
          System.out.println("\nReading a single row by row key");

          Row row = dataClient.readRow(TableId.of(tableId), rowKey);
          System.out.println("Row: " + row.getKey().toStringUtf8());
          for (RowCell cell : row.getCells()) {
            System.out.printf(
                "Family: %s    Qualifier: %s    Value: %s%n",
                cell.getFamily(), cell.getQualifier().toStringUtf8(), cell.getValue().toStringUtf8());
          }
        }
        } catch (NotFoundException e) {
          System.err.println("Failed to read from a non-existent table: " + e.getMessage());*/
    } catch (Exception e) {
      System.out.print(e);
      System.out.println("Error during quickstart: \n" + e.toString());
    }
  }
}
