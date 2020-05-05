/*
 * Copyright 2020 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.readrows;

import com.google.bigtable.v2.ReadRowsResponse;
import com.google.cloud.bigtable.data.v2.models.DefaultRowAdapter;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.StringValue;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class StateMachineTest {
    StateMachine<Row> stateMachine;

    @Before
    public void setUp() throws Exception {
        stateMachine = new StateMachine<>(new DefaultRowAdapter().createRowBuilder());
    }

    @Test
    public void testErrorHandlingStats() {
        StateMachine.InvalidInputException actualError = null;


        ReadRowsResponse.CellChunk chunk = ReadRowsResponse.CellChunk.newBuilder()
                .setRowKey(ByteString.copyFromUtf8("my-key1"))
                .setFamilyName(StringValue.newBuilder().setValue("my-family"))
                .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")))
                .setTimestampMicros(1_000)
                .setValue(ByteString.copyFromUtf8("my-value"))
                .setCommitRow(true)
                .build();
        try {
            stateMachine.handleChunk(chunk);
            stateMachine.consumeRow();
            stateMachine.handleChunk(
                    chunk.toBuilder()
                        .setRowKey(ByteString.copyFromUtf8("my-key2"))
                        .setValueSize(123) // invalid value size
                        .build()
            );
        } catch (StateMachine.InvalidInputException e) {
            actualError = e;
        }

        assertThat(actualError).hasMessageThat()
                .contains("my-key1");
        assertThat(actualError).hasMessageThat()
                .contains("my-key2");
        assertThat(actualError).hasMessageThat()
                .contains("numScannedNotifications: 0");
        assertThat(actualError).hasMessageThat()
                .contains("numChunksProcessed: 2");
        assertThat(actualError).hasMessageThat()
                .contains("numCellsInRow: 0");
        assertThat(actualError).hasMessageThat()
                .contains("numCellsInLastRow: 1");

    }
}