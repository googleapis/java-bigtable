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
package com.google.cloud.bigtable.data.v2.models;

import static com.google.cloud.bigtable.data.v2.models.Filters.FILTERS;
import static com.google.common.truth.Truth.assertThat;

import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsRequest.Builder;
import com.google.bigtable.v2.RowFilter;
import com.google.bigtable.v2.RowRange;
import com.google.bigtable.v2.RowSet;
import com.google.cloud.bigtable.data.v2.internal.ByteStringComparator;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.cloud.bigtable.data.v2.models.Range.ByteStringRange;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import com.google.protobuf.ByteString;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.SortedSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class QueryTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String TABLE_ID = "fake-table";
  private static final String APP_PROFILE_ID = "fake-profile-id";
  private RequestContext requestContext;

  @Rule public ExpectedException expect = ExpectedException.none();

  @Before
  public void setUp() {
    requestContext = RequestContext.create(PROJECT_ID, INSTANCE_ID, APP_PROFILE_ID);
  }

  @Test
  public void requestContextTest() {
    Query query = Query.create(TABLE_ID);

    ReadRowsRequest proto = query.toProto(requestContext);
    assertThat(proto).isEqualTo(expectedProtoBuilder().build());
  }

  @Test
  public void rowKeysTest() {
    Query query =
        Query.create(TABLE_ID)
            .rowKey("simple-string")
            .rowKey(ByteString.copyFromUtf8("byte-string"));

    ReadRowsRequest.Builder expectedProto = expectedProtoBuilder();
    expectedProto
        .getRowsBuilder()
        .addRowKeys(ByteString.copyFromUtf8("simple-string"))
        .addRowKeys(ByteString.copyFromUtf8("byte-string"));

    ReadRowsRequest actualProto = query.toProto(requestContext);
    assertThat(actualProto).isEqualTo(expectedProto.build());
  }

  @Test
  public void rowRangeTest() {
    Query query =
        Query.create(TABLE_ID)
            .range("simple-begin", "simple-end")
            .range(ByteString.copyFromUtf8("byte-begin"), ByteString.copyFromUtf8("byte-end"))
            .range(ByteStringRange.create("range-begin", "range-end"));

    Builder expectedProto = expectedProtoBuilder();
    expectedProto
        .getRowsBuilder()
        .addRowRanges(
            RowRange.newBuilder()
                .setStartKeyClosed(ByteString.copyFromUtf8("simple-begin"))
                .setEndKeyOpen(ByteString.copyFromUtf8("simple-end")))
        .addRowRanges(
            RowRange.newBuilder()
                .setStartKeyClosed(ByteString.copyFromUtf8("byte-begin"))
                .setEndKeyOpen(ByteString.copyFromUtf8("byte-end")))
        .addRowRanges(
            RowRange.newBuilder()
                .setStartKeyClosed(ByteString.copyFromUtf8("range-begin"))
                .setEndKeyOpen(ByteString.copyFromUtf8("range-end")));

    ReadRowsRequest actualProto = query.toProto(requestContext);
    assertThat(actualProto).isEqualTo(expectedProto.build());
  }

  @Test
  public void filterTestWithExceptions() {
    Exception actualException = null;
    try {
      Query.create(TABLE_ID).filter(null);
    } catch (Exception ex) {
      actualException = ex;
    }
    assertThat(actualException).isInstanceOf(NullPointerException.class);

    actualException = null;
    int maxFilterSize = 20 * 1024 * 1024;
    ByteString largeValue = ByteString.copyFrom(new byte[maxFilterSize + 1]);

    try {
      Query.create(TABLE_ID).filter(FILTERS.value().exactMatch(largeValue));
    } catch (Exception ex) {
      actualException = ex;
    }
    assertThat(actualException).hasMessageThat().contains("filter size can't be more than 20MB");
  }

  @Test
  public void filterTest() {
    Query query = Query.create(TABLE_ID).filter(FILTERS.key().regex(".*"));

    Builder expectedProto =
        expectedProtoBuilder()
            .setFilter(RowFilter.newBuilder().setRowKeyRegexFilter(ByteString.copyFromUtf8(".*")));

    ReadRowsRequest actualProto = query.toProto(requestContext);
    assertThat(actualProto).isEqualTo(expectedProto.build());
  }

  @Test
  public void limitTest() {
    Query query = Query.create(TABLE_ID).limit(10);

    Builder expectedProto = expectedProtoBuilder().setRowsLimit(10);

    ReadRowsRequest actualProto = query.toProto(requestContext);
    assertThat(actualProto).isEqualTo(expectedProto.build());
  }

  @Test
  public void serializationTest() throws IOException, ClassNotFoundException {
    Query expected = Query.create(TABLE_ID).filter(FILTERS.key().regex(".*"));

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(expected);
    oos.close();

    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

    Query actual = (Query) ois.readObject();
    assertThat(actual.toProto(requestContext)).isEqualTo(expected.toProto(requestContext));
  }

  @Test
  public void shardTestSplitPoints() {
    Query query = Query.create(TABLE_ID).range("a", "z");

    SortedSet<ByteString> splitPoints =
        ImmutableSortedSet.orderedBy(ByteStringComparator.INSTANCE)
            .add(ByteString.copyFromUtf8("j"))
            .build();

    List<Query> subQueries = query.shard(splitPoints);

    assertThat(subQueries).hasSize(2);
    assertThat(subQueries.get(0).toProto(requestContext))
        .isEqualTo(
            ReadRowsRequest.newBuilder()
                .setTableName(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
                .setAppProfileId(APP_PROFILE_ID)
                .setRows(
                    RowSet.newBuilder()
                        .addRowRanges(
                            RowRange.newBuilder()
                                .setStartKeyClosed(ByteString.copyFromUtf8("a"))
                                .setEndKeyClosed(ByteString.copyFromUtf8("j"))))
                .build());
    assertThat(subQueries.get(1).toProto(requestContext))
        .isEqualTo(
            ReadRowsRequest.newBuilder()
                .setTableName(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
                .setAppProfileId(APP_PROFILE_ID)
                .setRows(
                    RowSet.newBuilder()
                        .addRowRanges(
                            RowRange.newBuilder()
                                .setStartKeyOpen(ByteString.copyFromUtf8("j"))
                                .setEndKeyOpen(ByteString.copyFromUtf8("z"))))
                .build());
  }

  @Test
  public void shardTestKeyOffsets() {
    Query query = Query.create(TABLE_ID).range("a", "z");

    List<KeyOffset> keyOffsets =
        ImmutableList.of(
            KeyOffset.create(ByteString.copyFromUtf8("j"), 10),
            KeyOffset.create(ByteString.EMPTY, 100));

    List<Query> subQueries = query.shard(keyOffsets);

    assertThat(subQueries).hasSize(2);
    assertThat(subQueries.get(0).toProto(requestContext))
        .isEqualTo(
            ReadRowsRequest.newBuilder()
                .setTableName(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
                .setAppProfileId(APP_PROFILE_ID)
                .setRows(
                    RowSet.newBuilder()
                        .addRowRanges(
                            RowRange.newBuilder()
                                .setStartKeyClosed(ByteString.copyFromUtf8("a"))
                                .setEndKeyClosed(ByteString.copyFromUtf8("j"))))
                .build());
    assertThat(subQueries.get(1).toProto(requestContext))
        .isEqualTo(
            ReadRowsRequest.newBuilder()
                .setTableName(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
                .setAppProfileId(APP_PROFILE_ID)
                .setRows(
                    RowSet.newBuilder()
                        .addRowRanges(
                            RowRange.newBuilder()
                                .setStartKeyOpen(ByteString.copyFromUtf8("j"))
                                .setEndKeyOpen(ByteString.copyFromUtf8("z"))))
                .build());
  }

  private static ReadRowsRequest.Builder expectedProtoBuilder() {
    return ReadRowsRequest.newBuilder()
        .setTableName(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
        .setAppProfileId(APP_PROFILE_ID);
  }

  @Test
  public void testFromProto() {
    ReadRowsRequest request =
        ReadRowsRequest.newBuilder()
            .setTableName(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
            .setAppProfileId(APP_PROFILE_ID)
            .setFilter(RowFilter.newBuilder().setRowKeyRegexFilter(ByteString.copyFromUtf8(".*")))
            .setRows(
                RowSet.newBuilder()
                    .addRowKeys(ByteString.copyFromUtf8("row-key"))
                    .addRowRanges(
                        RowRange.newBuilder()
                            .setStartKeyClosed(ByteString.copyFromUtf8("j"))
                            .setEndKeyClosed(ByteString.copyFromUtf8("z"))))
            .build();
    Query query = Query.fromProto(request);

    assertThat(query.toProto(requestContext)).isEqualTo(request);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromProtoWithEmptyTableId() {
    Query.fromProto(ReadRowsRequest.getDefaultInstance());

    expect.expect(IllegalArgumentException.class);
    expect.expectMessage("Invalid table name:");
  }

  @Test
  public void testEquality() {
    Query request =
        Query.create(TABLE_ID)
            .rowKey("row-key")
            .range("a", "z")
            .limit(3)
            .filter(FILTERS.family().exactMatch("test"));

    // Query#toProto should not change the Query instance state
    request.toProto(requestContext);
    assertThat(request)
        .isEqualTo(
            Query.create(TABLE_ID)
                .rowKey("row-key")
                .range("a", "z")
                .limit(3)
                .filter(FILTERS.family().exactMatch("test")));

    assertThat(Query.create(TABLE_ID).rowKey("row-key"))
        .isNotEqualTo(Query.create(TABLE_ID).rowKey("row-key-1"));
    assertThat(Query.create(TABLE_ID).range("a", "z"))
        .isNotEqualTo(Query.create(TABLE_ID).range("a", "s"));
    assertThat(Query.create(TABLE_ID).filter(FILTERS.family().regex("test")))
        .isNotEqualTo(Query.create(TABLE_ID).filter(FILTERS.family().exactMatch("test-one")));
    assertThat(Query.create(TABLE_ID).limit(4)).isNotEqualTo(Query.create(TABLE_ID).limit(5));
  }

  @Test
  public void testClone() {
    Query query = Query.create(TABLE_ID).filter(FILTERS.key().regex("temp")).limit(10);
    ReadRowsRequest request =
        ReadRowsRequest.newBuilder()
            .setTableName(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
            .setAppProfileId(APP_PROFILE_ID)
            .setRowsLimit(10)
            .setFilter(
                RowFilter.newBuilder()
                    .setRowKeyRegexFilter(ByteString.copyFromUtf8("temp"))
                    .build())
            .build();

    Query clonedReq = query.clone();
    assertThat(clonedReq).isEqualTo(query);
    assertThat(clonedReq.toProto(requestContext)).isEqualTo(request);
  }
}
