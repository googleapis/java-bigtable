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

import static com.google.common.truth.Truth.assertThat;

import com.google.bigtable.v2.MutateRowsRequest;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.protobuf.ByteString;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BulkMutationTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String TABLE_ID = "fake-table";
  private static final String APP_PROFILE = "fake-profile";
  private static final RequestContext REQUEST_CONTEXT =
      RequestContext.create(PROJECT_ID, INSTANCE_ID, APP_PROFILE);

  @Test
  public void test() throws ParseException {
    BulkMutation m =
        BulkMutation.create(TABLE_ID)
            .add(
                "key-a",
                Mutation.create()
                    .setCell("fake-family1", "fake-qualifier1", 1_000, "fake-value1")
                    .setCell("fake-family2", "fake-qualifier2", 2_000, "fake-value2"))
            .add(
                ByteString.copyFromUtf8("key-b"),
                Mutation.create().setCell("fake-family3", "fake-qualifier3", 3_000, "fake-value3"));

    MutateRowsRequest actual = m.toProto(REQUEST_CONTEXT);

    MutateRowsRequest.Builder expected =
        MutateRowsRequest.newBuilder()
            .setTableName(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
            .setAppProfileId(APP_PROFILE);
    TextFormat.merge(
        "entries {"
            + "  row_key: 'key-a'"
            + "  mutations {"
            + "    set_cell {"
            + "      family_name: 'fake-family1'"
            + "      column_qualifier: 'fake-qualifier1'"
            + "      timestamp_micros: 1000"
            + "      value: 'fake-value1'"
            + "    }"
            + "  }"
            + "  mutations {"
            + "    set_cell {"
            + "      family_name: 'fake-family2'"
            + "      column_qualifier: 'fake-qualifier2'"
            + "      timestamp_micros: 2000"
            + "      value: 'fake-value2'"
            + "    }"
            + "  }"
            + "}"
            + "entries {"
            + "  row_key: 'key-b'"
            + "  mutations {"
            + "    set_cell {"
            + "      family_name: 'fake-family3'"
            + "      column_qualifier: 'fake-qualifier3'"
            + "      timestamp_micros: 3000"
            + "      value: 'fake-value3'"
            + "    }"
            + "  }"
            + "}",
        expected);

    assertThat(actual).isEqualTo(expected.build());
  }

  @Test
  public void serializationTest() throws IOException, ClassNotFoundException {
    BulkMutation expected =
        BulkMutation.create(TABLE_ID)
            .add(
                "key-a",
                Mutation.create().setCell("fake-family1", "fake-qualifier1", 1_000, "fake-value1"));

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(expected);
    oos.close();

    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

    BulkMutation actual = (BulkMutation) ois.readObject();
    assertThat(actual.toProto(REQUEST_CONTEXT)).isEqualTo(expected.toProto(REQUEST_CONTEXT));
  }

  @Test
  public void cloneTest() {
    BulkMutation originalBulkMutation =
        BulkMutation.create(TABLE_ID)
            .add(
                "test-rowKey",
                Mutation.create().setCell("fake-family1", "fake-qualifier1", 12345, "fake-value1"));

    MutateRowsRequest originalRequest = originalBulkMutation.toProto(REQUEST_CONTEXT);
    BulkMutation clonedMutation = originalBulkMutation.clone();
    MutateRowsRequest clonedRequest = clonedMutation.toProto(REQUEST_CONTEXT);

    // Both BulkMutations should be equals.
    assertThat(clonedRequest).isEqualTo(originalRequest);
    assertThat(clonedRequest.getTableName()).isEqualTo(originalRequest.getTableName());
    assertThat(clonedRequest.getEntriesList()).isEqualTo(originalRequest.getEntriesList());

    // Mutating cloned BulkMutation
    clonedMutation.add(
        "another-rowKey", Mutation.create().deleteCells("delete-family", "delete-qualifier"));
    assertThat(clonedMutation.toProto(REQUEST_CONTEXT)).isNotEqualTo(originalRequest);
  }

  @Test
  public void addRowMutationEntry() {
    RowMutationEntry entry =
        RowMutationEntry.create("test-rowKey")
            .setCell("fake-family1", "fake-qualifier1", "fake-value1");
    BulkMutation bulkMutation = BulkMutation.create(TABLE_ID);
    bulkMutation.add(entry);
    assertThat(bulkMutation.toProto(REQUEST_CONTEXT).getEntriesList()).contains(entry.toProto());
  }

  @Test
  public void fromProtoTest() {
    BulkMutation expected =
        BulkMutation.create(TABLE_ID)
            .add(
                "key",
                Mutation.create().setCell("fake-family", "fake-qualifier", 10_000L, "fake-value"));

    MutateRowsRequest protoRequest = expected.toProto(REQUEST_CONTEXT);
    BulkMutation actualBulkMutation = BulkMutation.fromProto(protoRequest);

    assertThat(actualBulkMutation.toProto(REQUEST_CONTEXT)).isEqualTo(protoRequest);

    String projectId = "fresh-project";
    String instanceId = "fresh-instance";
    String appProfile = "fresh-app-profile";
    MutateRowsRequest overriddenRequest =
        actualBulkMutation.toProto(RequestContext.create(projectId, instanceId, appProfile));

    assertThat(overriddenRequest).isNotEqualTo(protoRequest);
    assertThat(overriddenRequest.getTableName())
        .matches(NameUtil.formatTableName(projectId, instanceId, TABLE_ID));
    assertThat(overriddenRequest.getAppProfileId()).matches(appProfile);
  }
}
