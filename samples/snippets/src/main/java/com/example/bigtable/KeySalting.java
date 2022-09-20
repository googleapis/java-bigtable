/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.bigtable;

import com.google.api.gax.rpc.ServerStream;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.protobuf.ByteString;

import java.io.IOException;

public class KeySalting {
    private static final String COLUMN_FAMILY_NAME = "stats_summary";
    public static final int SALT_RANGE = 4;

    public static void writeSaltedRow(String projectId, String instanceId, String tableId, String rowKey) throws IOException {
        BigtableDataClient dataClient = BigtableDataClient.create(projectId, instanceId);
        String saltedRowKey = getSaltedRowKey(rowKey, SALT_RANGE);
        RowMutation rowMutation =
                RowMutation.create(tableId, saltedRowKey)
                        .setCell(COLUMN_FAMILY_NAME, "os_build", "PQ2A.190405.003");

        dataClient.mutateRow(rowMutation);
        System.out.printf("Successfully wrote row %s as %s\n", rowKey, saltedRowKey);
    }

    public static void readSaltedRow(String projectId, String instanceId, String tableId, String rowKey) throws IOException {
        BigtableDataClient dataClient = BigtableDataClient.create(projectId, instanceId);
        Row row = dataClient.readRow(tableId, getSaltedRowKey(rowKey, SALT_RANGE));
        System.out.printf("Successfully read row %s\n", row.getKey().toStringUtf8());
    }

//    public static void scanSaltedRows(String projectId, String instanceId, String tableId, String prefix) throws IOException {
//        AccumulatingObserver observer = new AccumulatingObserver();
//
//        BigtableDataClient dataClient = BigtableDataClient.create(projectId, instanceId);
//        Query query = Query.create(tableId).prefix(prefix);
//        ServerStream<Row> rows = dataClient.readRowsAsync(query, );
//
//        bigtableDataClient.readRowsAsync(query, new ResponseObserver<Row>() {
//            StreamController controller;
//            int count = 0;
//            public void onStart(StreamController controller) {
//                this.controller = controller;
//            }
//            public void onResponse(Row row) {
//                if (++count > 10) {
//                    controller.cancel();
//                    return;
//                }
//                // Do something with Row
//            }
//            public void onError(Throwable t) {
//                if (t instanceof NotFoundException) {
//                    System.out.println("Tried to read a non-existent table");
//                } else {
//                    t.printStackTrace();
//                }
//            }
//
//            public void onComplete() {
//                // Handle stream completion
//            }
//        });
//
//
//        for (Row row : rows) {
//            System.out.printf("Successfully read row %s\n", row.getKey().toStringUtf8());
//        }
//    }

    public static String getSaltedRowKey(String rowKey, int saltRange) {
        int prefix = rowKey.hashCode() % saltRange;
        return prefix + "-" + rowKey;
    }
}
