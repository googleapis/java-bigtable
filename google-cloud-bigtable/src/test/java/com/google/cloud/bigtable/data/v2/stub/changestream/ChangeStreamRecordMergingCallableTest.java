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

import com.google.bigtable.v2.ReadChangeStreamRequest;
import com.google.bigtable.v2.ReadChangeStreamResponse;
import com.google.bigtable.v2.RowRange;
import com.google.bigtable.v2.StreamContinuationToken;
import com.google.bigtable.v2.StreamPartition;
import com.google.cloud.bigtable.data.v2.models.ChangeStreamContinuationToken;
import com.google.cloud.bigtable.data.v2.models.ChangeStreamRecord;
import com.google.cloud.bigtable.data.v2.models.CloseStream;
import com.google.cloud.bigtable.data.v2.models.DefaultChangeStreamRecordAdapter;
import com.google.cloud.bigtable.data.v2.models.Heartbeat;
import com.google.cloud.bigtable.data.v2.models.Range.ByteStringRange;
import com.google.cloud.bigtable.gaxx.testing.FakeStreamingApi;
import com.google.cloud.bigtable.gaxx.testing.FakeStreamingApi.ServerStreamingStashCallable;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.google.rpc.Status;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Additional tests in addition to {@link ReadChangeStreamMergingAcceptanceTest}.
 *
 * <p>All the ChangeStreamMutation tests are in {@link ReadChangeStreamMergingAcceptanceTest}.
 */
@RunWith(JUnit4.class)
public class ChangeStreamRecordMergingCallableTest {

  @Test
  public void heartbeatTest() {
    RowRange rowRange = RowRange.newBuilder().getDefaultInstanceForType();
    ReadChangeStreamResponse.Heartbeat heartbeatProto =
        ReadChangeStreamResponse.Heartbeat.newBuilder()
            .setLowWatermark(Timestamp.newBuilder().setSeconds(1000).build())
            .setContinuationToken(
                StreamContinuationToken.newBuilder()
                    .setPartition(StreamPartition.newBuilder().setRowRange(rowRange))
                    .setToken("random-token")
                    .build())
            .build();
    ReadChangeStreamResponse response =
        ReadChangeStreamResponse.newBuilder().setHeartbeat(heartbeatProto).build();
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner = new ServerStreamingStashCallable<>(Collections.singletonList(response));

    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    // Validate the result.
    Assert.assertEquals(results.size(), 1);
    ChangeStreamRecord record = results.get(0);
    Assert.assertTrue(record instanceof Heartbeat);
    Heartbeat heartbeat = (Heartbeat) record;
    Assert.assertEquals(
        heartbeat.getChangeStreamContinuationToken().getPartition(),
        ByteStringRange.create(rowRange.getStartKeyClosed(), rowRange.getEndKeyOpen()));
    Assert.assertEquals(
        heartbeat.getChangeStreamContinuationToken().getToken(),
        heartbeatProto.getContinuationToken().getToken());
    Assert.assertEquals(heartbeat.getLowWatermark(), heartbeatProto.getLowWatermark());
  }

  @Test
  public void closeStreamTest() {
    RowRange rowRange =
        RowRange.newBuilder()
            .setStartKeyClosed(ByteString.copyFromUtf8(""))
            .setEndKeyOpen(ByteString.copyFromUtf8(""))
            .build();
    StreamContinuationToken streamContinuationToken =
        StreamContinuationToken.newBuilder()
            .setPartition(StreamPartition.newBuilder().setRowRange(rowRange).build())
            .setToken("random-token")
            .build();
    ReadChangeStreamResponse.CloseStream closeStreamProto =
        ReadChangeStreamResponse.CloseStream.newBuilder()
            .addContinuationTokens(streamContinuationToken)
            .setStatus(Status.newBuilder().setCode(0).build())
            .build();
    ReadChangeStreamResponse response =
        ReadChangeStreamResponse.newBuilder().setCloseStream(closeStreamProto).build();
    FakeStreamingApi.ServerStreamingStashCallable<ReadChangeStreamRequest, ReadChangeStreamResponse>
        inner = new ServerStreamingStashCallable<>(Collections.singletonList(response));

    ChangeStreamRecordMergingCallable<ChangeStreamRecord> mergingCallable =
        new ChangeStreamRecordMergingCallable<>(inner, new DefaultChangeStreamRecordAdapter());
    List<ChangeStreamRecord> results =
        mergingCallable.all().call(ReadChangeStreamRequest.getDefaultInstance());

    // Validate the result.
    Assert.assertEquals(results.size(), 1);
    ChangeStreamRecord record = results.get(0);
    Assert.assertTrue(record instanceof CloseStream);
    CloseStream closeStream = (CloseStream) record;
    Assert.assertEquals(closeStream.getStatus(), closeStreamProto.getStatus());
    Assert.assertEquals(closeStream.getChangeStreamContinuationTokens().size(), 1);
    ChangeStreamContinuationToken changeStreamContinuationToken =
        closeStream.getChangeStreamContinuationTokens().get(0);
    Assert.assertEquals(
        changeStreamContinuationToken.getPartition(),
        ByteStringRange.create(rowRange.getStartKeyClosed(), rowRange.getEndKeyOpen()));
    Assert.assertEquals(
        changeStreamContinuationToken.getToken(), streamContinuationToken.getToken());
  }
}
