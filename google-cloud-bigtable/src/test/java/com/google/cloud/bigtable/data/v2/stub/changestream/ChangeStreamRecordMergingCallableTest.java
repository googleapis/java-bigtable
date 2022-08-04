/*
 * Copyright 2022 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.changestream;

import com.google.bigtable.v2.Mutation;
import com.google.bigtable.v2.ReadChangeStreamRequest;
import com.google.bigtable.v2.ReadChangeStreamResponse;
import com.google.bigtable.v2.StreamContinuationToken;
import com.google.bigtable.v2.TimestampRange;
import com.google.cloud.bigtable.data.v2.models.ChangeStreamMutation;
import com.google.cloud.bigtable.data.v2.models.ChangeStreamRecord;
import com.google.cloud.bigtable.data.v2.models.CloseStream;
import com.google.cloud.bigtable.data.v2.models.DefaultChangeStreamRecordAdapter;
import com.google.cloud.bigtable.data.v2.models.Heartbeat;
import com.google.cloud.bigtable.data.v2.models.Range;
import com.google.cloud.bigtable.gaxx.testing.FakeStreamingApi;
import com.google.cloud.bigtable.gaxx.testing.FakeStreamingApi.ServerStreamingStashCallable;
import com.google.common.truth.Truth;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.google.rpc.Status;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Additional tests in addition to {@link
 * com.google.cloud.bigtable.data.v2.stub.changestream.ReadChangeStreamMergingAcceptanceTest}. At
 * some point they should be reintegrated into the json file.
 */
@RunWith(JUnit4.class)
public class ChangeStreamRecordMergingCallableTest {

  @Test
  public void heartbeatTest() {
    ReadChangeStreamResponse.Heartbeat heartbeat =
        ReadChangeStreamResponse.Heartbeat.newBuilder()
            .setLowWatermark(Timestamp.newBuilder().setSeconds(1000).build())
            .setContinuationToken(
                StreamContinuationToken.newBuilder().setToken("random-token").build())
            .build();
    ReadChangeStreamResponse response =
        ReadChangeStreamResponse.newBuilder().setHeartbeat(heartbeat).build();
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner = new ServerStreamingStashCallable<>(Collections.singletonList(response));

    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    Truth.assertThat(results).containsExactly(Heartbeat.fromProto(heartbeat));
  }

  @Test
  public void closeStreamTest() {
    ReadChangeStreamResponse.CloseStream closeStream =
        ReadChangeStreamResponse.CloseStream.newBuilder()
            .addContinuationTokens(
                StreamContinuationToken.newBuilder().setToken("random-token").build())
            .setStatus(Status.newBuilder().setCode(0).build())
            .build();
    ReadChangeStreamResponse response =
        ReadChangeStreamResponse.newBuilder().setCloseStream(closeStream).build();
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner = new ServerStreamingStashCallable<>(Collections.singletonList(response));

    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    Truth.assertThat(results).containsExactly(CloseStream.fromProto(closeStream));
  }

  // [{DeleteFamily, DeleteCells, SetCell}, {CloseStream}]
  // -> [ChangeStreamMutation{DeleteFamily, DeleteCells, SetCell}, CloseStream]
  @Test
  public void multipleModesNoChunkingTest() {
    // Construct the ReadChangeStreamResponse's.
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(100).build();
    Mutation deleteFromFamily =
        Mutation.newBuilder()
            .setDeleteFromFamily(
                Mutation.DeleteFromFamily.newBuilder().setFamilyName("fake-family").build())
            .build();
    Mutation deleteFromColumn =
        Mutation.newBuilder()
            .setDeleteFromColumn(
                Mutation.DeleteFromColumn.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimeRange(
                        TimestampRange.newBuilder()
                            .setStartTimestampMicros(1000L)
                            .setEndTimestampMicros(2000L)
                            .build())
                    .build())
            .build();
    Mutation setCell =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimestampMicros(1000L)
                    .setValue(ByteString.copyFromUtf8("fake-value"))
                    .build())
            .build();
    ReadChangeStreamResponse.DataChange dataChange =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.USER)
            .setSourceClusterId("fake-source-cluster-id")
            .setRowKey(ByteString.copyFromUtf8("key"))
            .setCommitTimestamp(Timestamp.newBuilder().setSeconds(100).build())
            .setTiebreaker(100)
            .setLowWatermark(fakeLowWatermark)
            .setToken("fake-token")
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(deleteFromFamily))
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(deleteFromColumn))
            .addChunks(ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(setCell))
            .setDone(true)
            .build();
    ReadChangeStreamResponse dataChangeResponse =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange).build();
    ReadChangeStreamResponse closeStreamResponse =
        ReadChangeStreamResponse.newBuilder()
            .setCloseStream(
                ReadChangeStreamResponse.CloseStream.newBuilder()
                    .addContinuationTokens(
                        StreamContinuationToken.newBuilder().setToken("random-token").build())
                    .setStatus(Status.newBuilder().setCode(0).build())
                    .build())
            .build();
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner =
            new ServerStreamingStashCallable<>(
                Arrays.asList(dataChangeResponse, closeStreamResponse));
    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());

    // Actual results.
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    // Expected results.
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(100).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 100)
            .deleteFamily("fake-family")
            .deleteCells(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                Range.TimestampRange.create(1000L, 2000L))
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000L,
                ByteString.copyFromUtf8("fake-value"))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    Truth.assertThat(results)
        .containsExactly(
            changeStreamMutation, CloseStream.fromProto(closeStreamResponse.getCloseStream()));
  }

  // [{DeleteFamily}] -> [ChangeStreamMutation{DeleteFamily}].
  // Tests that the resulting ChangeStreamMutation for a Garbage Collection mutation
  // doesn't have source cluster id.
  @Test
  public void GcModNoChunkingTest() {
    Mutation deleteFromFamily =
        Mutation.newBuilder()
            .setDeleteFromFamily(
                Mutation.DeleteFromFamily.newBuilder().setFamilyName("fake-family").build())
            .build();
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(100).build();
    ReadChangeStreamResponse.DataChange dataChange =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.GARBAGE_COLLECTION)
            .setRowKey(ByteString.copyFromUtf8("key"))
            .setCommitTimestamp(Timestamp.newBuilder().setSeconds(100).build())
            .setTiebreaker(100)
            .setLowWatermark(fakeLowWatermark)
            .setToken("fake-token")
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(deleteFromFamily))
            .setDone(true)
            .build();
    ReadChangeStreamResponse dataChangeResponse =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange).build();
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner = new ServerStreamingStashCallable<>(Collections.singletonList(dataChangeResponse));
    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());

    // Actual results.
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    // Expected results. Note that it shouldn't have source cluster id.
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(100).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createGcMutation(
                ByteString.copyFromUtf8("key"), fakeCommitTimestamp, 100)
            .deleteFamily("fake-family")
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    Truth.assertThat(results).containsExactly(changeStreamMutation);
  }

  // [{SetCell_chunk1}, {SetCell_chunk2}] -> [ChangeStreamMutation{SetCell}]
  @Test
  public void oneChunkedCellTest() {
    String firstCellValue = "chunk1-value";
    String secondCellValue = "chunk2-value";
    String totalCellValue = firstCellValue + secondCellValue;
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(100).build();
    // Build the first ReadChangeStreamResponse which contains the first chunk of a chunked SetCell.
    Mutation chunkedCell1 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimestampMicros(1000L)
                    .setValue(ByteString.copyFromUtf8(firstCellValue))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo chunkInfo1 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueSize(totalCellValue.length())
            .build();
    ReadChangeStreamResponse.DataChange dataChange1 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.USER)
            .setSourceClusterId("fake-source-cluster-id")
            .setRowKey(ByteString.copyFromUtf8("key"))
            .setCommitTimestamp(Timestamp.newBuilder().setSeconds(100).build())
            .setTiebreaker(100)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(chunkedCell1)
                    .setChunkInfo(chunkInfo1))
            .build();
    ReadChangeStreamResponse dataChangeResponse1 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange1).build();

    // Build the second ReadChangeStreamResponse which contains the second chunk of a chunked
    // SetCell.
    Mutation chunkedCell2 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setValue(ByteString.copyFromUtf8(secondCellValue))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo chunkInfo2 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueOffset(firstCellValue.length())
            .setChunkedValueSize(totalCellValue.length())
            .setLastChunk(true)
            .build();
    ReadChangeStreamResponse.DataChange dataChange2 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.CONTINUATION)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(chunkedCell2)
                    .setChunkInfo(chunkInfo2))
            .setDone(true)
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    ReadChangeStreamResponse dataChangeResponse2 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange2).build();

    // Feed the two ReadChangeStream responses to the merging callable.
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner =
            new ServerStreamingStashCallable<>(
                Arrays.asList(dataChangeResponse1, dataChangeResponse2));
    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());
    // Actual results.
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    // Expected results.
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(100).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 100)
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000L,
                ByteString.copyFromUtf8(totalCellValue))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    Truth.assertThat(results).containsExactly(changeStreamMutation);
  }

  // [{SetCell1_chunk1}, {SetCell1_chunk2, SetCell2_chunk1}, {SetCell2_chunk2}]
  // -> [ChangeStreamMutation{SetCell1, SetCell2}]
  @Test
  public void twoChunkedCellsTest() {
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(100).build();
    String setCell1Chunk1val = "cell1-chunk1";
    String setCell1Chunk2val = "cell1-chunk2";
    String setCell2Chunk1val = "cell2-chunk1";
    String setCell2Chunk2val = "cell2-chunk2";
    String totalCellValue1 = setCell1Chunk1val + setCell1Chunk2val;
    String totalCellValue2 = setCell2Chunk1val + setCell2Chunk2val;
    // Build the 1st ReadChangeStreamResponse which has 1/2 of SetCell1.
    Mutation setCell1Chunk1 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimestampMicros(1000L)
                    .setValue(ByteString.copyFromUtf8(setCell1Chunk1val))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo setCell1ChunkInfo1 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueSize(totalCellValue1.length())
            .build();
    ReadChangeStreamResponse.DataChange dataChange1 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.USER)
            .setSourceClusterId("fake-source-cluster-id")
            .setRowKey(ByteString.copyFromUtf8("key"))
            .setCommitTimestamp(Timestamp.newBuilder().setSeconds(100).build())
            .setTiebreaker(100)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(setCell1Chunk1)
                    .setChunkInfo(setCell1ChunkInfo1))
            .build();
    ReadChangeStreamResponse dataChangeResponse1 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange1).build();

    // Build the 2nd ReadChangeStreamResponse which has 2/2 of SetCell1 and 1/2 of SetCell2.
    Mutation setCell1Chunk2 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setValue(ByteString.copyFromUtf8(setCell1Chunk2val))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo setCell1ChunkInfo2 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueOffset(setCell1Chunk1val.length())
            .setChunkedValueSize(totalCellValue1.length())
            .setLastChunk(true)
            .build();
    Mutation setCell2Chunk1 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimestampMicros(1000L)
                    .setValue(ByteString.copyFromUtf8(setCell2Chunk1val))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo setCell2ChunkInfo1 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueSize(totalCellValue2.length())
            .build();
    ReadChangeStreamResponse.DataChange dataChange2 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.CONTINUATION)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(setCell1Chunk2)
                    .setChunkInfo(setCell1ChunkInfo2))
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(setCell2Chunk1)
                    .setChunkInfo(setCell2ChunkInfo1))
            .build();
    ReadChangeStreamResponse dataChangeResponse2 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange2).build();

    // Build the 3rd ReadChangeStreamResponse which has 2/2 of SetCell2.
    Mutation setCell2Chunk2 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setValue(ByteString.copyFromUtf8(setCell2Chunk2val))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo setCell2ChunkInfo2 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueOffset(setCell2Chunk1val.length())
            .setChunkedValueSize(totalCellValue2.length())
            .setLastChunk(true)
            .build();
    ReadChangeStreamResponse.DataChange dataChange3 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.CONTINUATION)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(setCell2Chunk2)
                    .setChunkInfo(setCell2ChunkInfo2))
            .setDone(true)
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    ReadChangeStreamResponse dataChangeResponse3 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange3).build();

    // Feed the 3 ReadChangeStream responses to the merging callable.
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner =
            new ServerStreamingStashCallable<>(
                Arrays.asList(dataChangeResponse1, dataChangeResponse2, dataChangeResponse3));
    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());
    // Actual results.
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    // Expected results.
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(100).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 100)
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000L,
                ByteString.copyFromUtf8(totalCellValue1))
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000L,
                ByteString.copyFromUtf8(totalCellValue2))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    Truth.assertThat(results).containsExactly(changeStreamMutation);
  }

  // [{SetCell1_chunk1}, {SetCell1_chunk2, DeleteFamily, DeleteCells, SetCell2}]
  // -> [ChangeStreamMutation{SetCell1, DeleteFamily, DeleteCells, SetCell2}]
  @Test
  public void oneChunkedCell_manyNonChunkedModsTest() {
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(100).build();
    String firstCellValue = "chunk1-value";
    String secondCellValue = "chunk2-value";
    String totalCellValue = firstCellValue + secondCellValue;
    // Build the 1st ReadChangeStreamResponse which contains 1/2 of a chunked SetCell.
    Mutation chunkedCell1 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimestampMicros(1000L)
                    .setValue(ByteString.copyFromUtf8(firstCellValue))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo chunkInfo1 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueSize(totalCellValue.length())
            .build();
    ReadChangeStreamResponse.DataChange dataChange1 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.USER)
            .setSourceClusterId("fake-source-cluster-id")
            .setRowKey(ByteString.copyFromUtf8("key"))
            .setCommitTimestamp(Timestamp.newBuilder().setSeconds(100).build())
            .setTiebreaker(100)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(chunkedCell1)
                    .setChunkInfo(chunkInfo1))
            .build();
    ReadChangeStreamResponse dataChangeResponse1 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange1).build();

    // Build the 2nd ReadChangeStreamResponse which contains 2/2 of a chunked SetCell, a
    // deleteFromFamily, a deleteFromColumn, and a non-chunked SetCell.
    Mutation chunkedCell2 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setValue(ByteString.copyFromUtf8(secondCellValue))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo chunkInfo2 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueOffset(firstCellValue.length())
            .setChunkedValueSize(totalCellValue.length())
            .setLastChunk(true)
            .build();
    Mutation deleteFromFamily =
        Mutation.newBuilder()
            .setDeleteFromFamily(
                Mutation.DeleteFromFamily.newBuilder().setFamilyName("fake-family").build())
            .build();
    Mutation deleteFromColumn =
        Mutation.newBuilder()
            .setDeleteFromColumn(
                Mutation.DeleteFromColumn.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimeRange(
                        TimestampRange.newBuilder()
                            .setStartTimestampMicros(1000L)
                            .setEndTimestampMicros(2000L)
                            .build())
                    .build())
            .build();
    Mutation nonChunkedCell =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimestampMicros(1000L)
                    .setValue(ByteString.copyFromUtf8("fake-value"))
                    .build())
            .build();
    ReadChangeStreamResponse.DataChange dataChange2 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.CONTINUATION)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(chunkedCell2)
                    .setChunkInfo(chunkInfo2))
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(deleteFromFamily))
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(deleteFromColumn))
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(nonChunkedCell))
            .setDone(true)
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    ReadChangeStreamResponse dataChangeResponse2 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange2).build();

    // Feed the two ReadChangeStream responses to the merging callable.
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner =
            new ServerStreamingStashCallable<>(
                Arrays.asList(dataChangeResponse1, dataChangeResponse2));
    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());
    // Actual results.
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    // Expected results.
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(100).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 100)
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000L,
                ByteString.copyFromUtf8(totalCellValue))
            .deleteFamily("fake-family")
            .deleteCells(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                Range.TimestampRange.create(1000L, 2000L))
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000L,
                ByteString.copyFromUtf8("fake-value"))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    Truth.assertThat(results).containsExactly(changeStreamMutation);
  }

  // [{SetCell1, SetCell2_chunk1}, {SetCell2_chunk2}]
  // -> [ChangeStreamMutation{SetCell1, SetCell2}]
  @Test
  public void oneUnChunkedCell_oneChunkedCellTest() {
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(100).build();
    Mutation nonChunkedCell =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimestampMicros(1000L)
                    .setValue(ByteString.copyFromUtf8("fake-value"))
                    .build())
            .build();
    String firstCellValue = "chunk1-value";
    String secondCellValue = "chunk2-value";
    String totalCellValue = firstCellValue + secondCellValue;
    // Build the 1st ReadChangeStreamResponse which contains a nonChunkedCell and 1/2 of the chunked
    // SetCell.
    Mutation chunkedCell1 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimestampMicros(1000L)
                    .setValue(ByteString.copyFromUtf8(firstCellValue))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo chunkInfo1 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueSize(totalCellValue.length())
            .build();
    ReadChangeStreamResponse.DataChange dataChange1 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.USER)
            .setSourceClusterId("fake-source-cluster-id")
            .setRowKey(ByteString.copyFromUtf8("key"))
            .setCommitTimestamp(Timestamp.newBuilder().setSeconds(100).build())
            .setTiebreaker(100)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(nonChunkedCell))
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(chunkedCell1)
                    .setChunkInfo(chunkInfo1))
            .build();
    ReadChangeStreamResponse dataChangeResponse1 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange1).build();

    // Build the 2nd ReadChangeStreamResponse which contains 2/2 of the chunked SetCell.
    Mutation chunkedCell2 =
        Mutation.newBuilder()
            .setSetCell(
                Mutation.SetCell.newBuilder()
                    .setValue(ByteString.copyFromUtf8(secondCellValue))
                    .build())
            .build();
    ReadChangeStreamResponse.MutationChunk.ChunkInfo chunkInfo2 =
        ReadChangeStreamResponse.MutationChunk.ChunkInfo.newBuilder()
            .setChunkedValueOffset(firstCellValue.length())
            .setChunkedValueSize(totalCellValue.length())
            .setLastChunk(true)
            .build();
    ReadChangeStreamResponse.DataChange dataChange2 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.CONTINUATION)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder()
                    .setMutation(chunkedCell2)
                    .setChunkInfo(chunkInfo2))
            .setDone(true)
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    ReadChangeStreamResponse dataChangeResponse2 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange2).build();

    // Feed the two ReadChangeStream responses to the merging callable.
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner =
            new ServerStreamingStashCallable<>(
                Arrays.asList(dataChangeResponse1, dataChangeResponse2));
    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());
    // Actual results.
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    // Expected results.
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(100).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 100)
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000L,
                ByteString.copyFromUtf8("fake-value"))
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000L,
                ByteString.copyFromUtf8(totalCellValue))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    Truth.assertThat(results).containsExactly(changeStreamMutation);
  }

  // [{DeleteFamily1, DeleteCells}, {DeleteFamily2}]
  // -> [ChangeStreamMutation{DeleteFamily1, DeleteCells, DeleteFamily2}]
  @Test
  public void nonSetCellChunkingTest() {
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(100).build();
    Mutation deleteFromFamily1 =
        Mutation.newBuilder()
            .setDeleteFromFamily(
                Mutation.DeleteFromFamily.newBuilder().setFamilyName("fake-family").build())
            .build();
    Mutation deleteFromColumn =
        Mutation.newBuilder()
            .setDeleteFromColumn(
                Mutation.DeleteFromColumn.newBuilder()
                    .setFamilyName("fake-family")
                    .setColumnQualifier(ByteString.copyFromUtf8("fake-qualifier"))
                    .setTimeRange(
                        TimestampRange.newBuilder()
                            .setStartTimestampMicros(1000L)
                            .setEndTimestampMicros(2000L)
                            .build())
                    .build())
            .build();
    Mutation deleteFromFamily2 =
        Mutation.newBuilder()
            .setDeleteFromFamily(
                Mutation.DeleteFromFamily.newBuilder().setFamilyName("fake-family2").build())
            .build();
    // Build the 1st ReadChangeStreamResponse which contains deleteFromFamily1 and deleteFromColumn.
    ReadChangeStreamResponse.DataChange dataChange1 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.USER)
            .setSourceClusterId("fake-source-cluster-id")
            .setRowKey(ByteString.copyFromUtf8("key"))
            .setCommitTimestamp(Timestamp.newBuilder().setSeconds(100).build())
            .setTiebreaker(100)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(deleteFromFamily1))
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(deleteFromColumn))
            .build();
    ReadChangeStreamResponse dataChangeResponse1 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange1).build();

    // Build the 2nd ReadChangeStreamResponse which contains deleteFromFamily2.
    ReadChangeStreamResponse.DataChange dataChange2 =
        ReadChangeStreamResponse.DataChange.newBuilder()
            .setType(ReadChangeStreamResponse.DataChange.Type.CONTINUATION)
            .addChunks(
                ReadChangeStreamResponse.MutationChunk.newBuilder().setMutation(deleteFromFamily2))
            .setDone(true)
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    ReadChangeStreamResponse dataChangeResponse2 =
        ReadChangeStreamResponse.newBuilder().setDataChange(dataChange2).build();

    // Feed the two ReadChangeStream responses to the merging callable.
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner =
            new ServerStreamingStashCallable<>(
                Arrays.asList(dataChangeResponse1, dataChangeResponse2));
    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());
    // Actual results.
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    // Expected results.
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(100).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 100)
            .deleteFamily("fake-family")
            .deleteCells(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                Range.TimestampRange.create(1000L, 2000L))
            .deleteFamily("fake-family2")
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();
    Truth.assertThat(results).containsExactly(changeStreamMutation);
  }
}
