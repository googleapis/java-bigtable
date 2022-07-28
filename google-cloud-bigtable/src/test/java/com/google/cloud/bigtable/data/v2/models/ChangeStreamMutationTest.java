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
package com.google.cloud.bigtable.data.v2.models;

import static com.google.common.truth.Truth.assertThat;

import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.ReadChangeStreamResponse;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.common.primitives.Longs;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ChangeStreamMutationTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String TABLE_ID = "fake-table";
  private static final String APP_PROFILE_ID = "fake-profile";
  private static final RequestContext REQUEST_CONTEXT =
      RequestContext.create(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID);

  @Rule public ExpectedException expect = ExpectedException.none();

  @Test
  public void userInitiatedMutationTest() throws IOException, ClassNotFoundException {
    // Create a user initiated logical mutation.
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(1000).build();
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(2000).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 0)
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000,
                ByteString.copyFromUtf8("fake-value"))
            .deleteFamily("fake-family")
            .deleteCells(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                Range.TimestampRange.create(1000L, 2000L))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();

    // Test the getters.
    Assert.assertEquals(changeStreamMutation.getRowKey(), ByteString.copyFromUtf8("key"));
    Assert.assertEquals(
        changeStreamMutation.getType(), ReadChangeStreamResponse.DataChange.Type.USER);
    Assert.assertEquals(changeStreamMutation.getSourceClusterId(), "fake-source-cluster-id");
    Assert.assertEquals(changeStreamMutation.getCommitTimestamp(), fakeCommitTimestamp);
    Assert.assertEquals(changeStreamMutation.getTieBreaker(), 0);
    Assert.assertEquals(changeStreamMutation.getToken(), "fake-token");
    Assert.assertEquals(changeStreamMutation.getLowWatermark(), fakeLowWatermark);

    // Test serialization.
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(changeStreamMutation);
    oos.close();
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
    ChangeStreamMutation actual = (ChangeStreamMutation) ois.readObject();
    assertThat(actual.toString()).isEqualTo(changeStreamMutation.toString());
  }

  @Test
  public void gcMutationTest() throws IOException, ClassNotFoundException {
    // Create a GC mutation.
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(1000).build();
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(2000).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createGcMutation(
                ByteString.copyFromUtf8("key"), fakeCommitTimestamp, 0)
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000,
                ByteString.copyFromUtf8("fake-value"))
            .deleteFamily("fake-family")
            .deleteCells(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                Range.TimestampRange.create(1000L, 2000L))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();

    // Test the getters.
    Assert.assertEquals(changeStreamMutation.getRowKey(), ByteString.copyFromUtf8("key"));
    Assert.assertEquals(
        changeStreamMutation.getType(),
        ReadChangeStreamResponse.DataChange.Type.GARBAGE_COLLECTION);
    Assert.assertNull(changeStreamMutation.getSourceClusterId());
    Assert.assertEquals(changeStreamMutation.getCommitTimestamp(), fakeCommitTimestamp);
    Assert.assertEquals(changeStreamMutation.getTieBreaker(), 0);
    Assert.assertEquals(changeStreamMutation.getToken(), "fake-token");
    Assert.assertEquals(changeStreamMutation.getLowWatermark(), fakeLowWatermark);

    // Test serialization.
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(changeStreamMutation);
    oos.close();
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
    ChangeStreamMutation actual = (ChangeStreamMutation) ois.readObject();
    assertThat(actual.toString()).isEqualTo(changeStreamMutation.toString());
  }

  @Test
  public void toRowMutationTest() {
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(1000).build();
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(2000).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 0)
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000,
                ByteString.copyFromUtf8("fake-value"))
            .deleteFamily("fake-family")
            .deleteCells(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                Range.TimestampRange.create(1000L, 2000L))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();

    // Convert it to a rowMutation and construct a MutateRowRequest.
    RowMutation rowMutation = changeStreamMutation.toRowMutation(TABLE_ID);
    MutateRowRequest mutateRowRequest = rowMutation.toProto(REQUEST_CONTEXT);
    String tableName =
        NameUtil.formatTableName(
            REQUEST_CONTEXT.getProjectId(), REQUEST_CONTEXT.getInstanceId(), TABLE_ID);
    assertThat(mutateRowRequest.getTableName()).isEqualTo(tableName);
    assertThat(mutateRowRequest.getMutationsList()).hasSize(3);
    assertThat(mutateRowRequest.getMutations(0).getSetCell().getValue())
        .isEqualTo(ByteString.copyFromUtf8("fake-value"));
    assertThat(mutateRowRequest.getMutations(1).getDeleteFromFamily().getFamilyName())
        .isEqualTo("fake-family");
    assertThat(mutateRowRequest.getMutations(2).getDeleteFromColumn().getFamilyName())
        .isEqualTo("fake-family");
    assertThat(mutateRowRequest.getMutations(2).getDeleteFromColumn().getColumnQualifier())
        .isEqualTo(ByteString.copyFromUtf8("fake-qualifier"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void toRowMutationWithoutTokenShouldFailTest() {
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(1000).build();
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(2000).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 0)
            .deleteFamily("fake-family")
            .setLowWatermark(fakeLowWatermark)
            .build();
    expect.expect(IllegalArgumentException.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toRowMutationWithoutLowWatermarkShouldFailTest() {
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(1000).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 0)
            .deleteFamily("fake-family")
            .setToken("fake-token")
            .build();
    expect.expect(IllegalArgumentException.class);
  }

  @Test
  public void toRowMutationEntryTest() {
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(1000).build();
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(2000).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 0)
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000,
                ByteString.copyFromUtf8("fake-value"))
            .deleteFamily("fake-family")
            .deleteCells(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                Range.TimestampRange.create(1000L, 2000L))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();

    // Convert it to a rowMutationEntry and construct a MutateRowRequest.
    RowMutationEntry rowMutationEntry = changeStreamMutation.toRowMutationEntry();
    MutateRowsRequest.Entry mutateRowsRequestEntry = rowMutationEntry.toProto();
    assertThat(mutateRowsRequestEntry.getRowKey()).isEqualTo(ByteString.copyFromUtf8("key"));
    assertThat(mutateRowsRequestEntry.getMutationsList()).hasSize(3);
    assertThat(mutateRowsRequestEntry.getMutations(0).getSetCell().getValue())
        .isEqualTo(ByteString.copyFromUtf8("fake-value"));
    assertThat(mutateRowsRequestEntry.getMutations(1).getDeleteFromFamily().getFamilyName())
        .isEqualTo("fake-family");
    assertThat(mutateRowsRequestEntry.getMutations(2).getDeleteFromColumn().getFamilyName())
        .isEqualTo("fake-family");
    assertThat(mutateRowsRequestEntry.getMutations(2).getDeleteFromColumn().getColumnQualifier())
        .isEqualTo(ByteString.copyFromUtf8("fake-qualifier"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void toRowMutationEntryWithoutTokenShouldFailTest() {
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(1000).build();
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(2000).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 0)
            .deleteFamily("fake-family")
            .setLowWatermark(fakeLowWatermark)
            .build();
    expect.expect(IllegalArgumentException.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void toRowMutationEntryWithoutLowWatermarkShouldFailTest() {
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(1000).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 0)
            .deleteFamily("fake-family")
            .setToken("fake-token")
            .build();
    expect.expect(IllegalArgumentException.class);
  }

  @Test
  public void testWithLongValue() {
    Timestamp fakeCommitTimestamp = Timestamp.newBuilder().setSeconds(1000).build();
    Timestamp fakeLowWatermark = Timestamp.newBuilder().setSeconds(2000).build();
    ChangeStreamMutation changeStreamMutation =
        ChangeStreamMutation.createUserMutation(
                ByteString.copyFromUtf8("key"), "fake-source-cluster-id", fakeCommitTimestamp, 0)
            .setCell(
                "fake-family",
                ByteString.copyFromUtf8("fake-qualifier"),
                1000L,
                ByteString.copyFrom(Longs.toByteArray(1L)))
            .setToken("fake-token")
            .setLowWatermark(fakeLowWatermark)
            .build();

    RowMutation rowMutation = changeStreamMutation.toRowMutation(TABLE_ID);
    MutateRowRequest mutateRowRequest = rowMutation.toProto(REQUEST_CONTEXT);
    String tableName =
        NameUtil.formatTableName(
            REQUEST_CONTEXT.getProjectId(), REQUEST_CONTEXT.getInstanceId(), TABLE_ID);
    assertThat(mutateRowRequest.getTableName()).isEqualTo(tableName);
    assertThat(mutateRowRequest.getMutationsList()).hasSize(1);
    assertThat(mutateRowRequest.getMutations(0).getSetCell().getValue())
        .isEqualTo(ByteString.copyFromUtf8("\000\000\000\000\000\000\000\001"));
  }
}
