/*
 * Copyright 2024 Google LLC
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

import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.dateType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.int64Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.timestampType;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.bigtable.v2.ProtoSchema;
import com.google.bigtable.v2.Type;
import com.google.cloud.bigtable.data.v2.models.sql.ColumnMetadata;
import com.google.cloud.bigtable.data.v2.models.sql.ResultSetMetadata;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ProtoResultSetMetadataTest {

  @Test
  public void getColumnIndex_returnsCorrectIndex() {
    ResultSetMetadata metadata =
        ProtoResultSetMetadata.create(
            Arrays.asList(
                ColumnMetadataImpl.create("0", stringType()),
                ColumnMetadataImpl.create("1", int64Type()),
                ColumnMetadataImpl.create("2", int64Type()),
                ColumnMetadataImpl.create("3", int64Type()),
                ColumnMetadataImpl.create("4", int64Type())));

    assertThat(metadata.getColumnIndex("0")).isEqualTo(0);
    assertThat(metadata.getColumnIndex("1")).isEqualTo(1);
    assertThat(metadata.getColumnIndex("2")).isEqualTo(2);
    assertThat(metadata.getColumnIndex("3")).isEqualTo(3);
    assertThat(metadata.getColumnIndex("4")).isEqualTo(4);
  }

  @Test
  public void getColumnType_worksByName() {
    ResultSetMetadata metadata =
        ProtoResultSetMetadata.create(
            Arrays.asList(
                ColumnMetadataImpl.create("col0", stringType()),
                ColumnMetadataImpl.create("col1", int64Type()),
                ColumnMetadataImpl.create("col2", timestampType()),
                ColumnMetadataImpl.create("col3", dateType()),
                ColumnMetadataImpl.create("col4", int64Type())));

    assertThat(metadata.getColumnType("col0")).isEqualTo(stringType());
    assertThat(metadata.getColumnType("col1")).isEqualTo(int64Type());
    assertThat(metadata.getColumnType("col2")).isEqualTo(timestampType());
    assertThat(metadata.getColumnType("col3")).isEqualTo(dateType());
    assertThat(metadata.getColumnType("col4")).isEqualTo(int64Type());
  }

  @Test
  public void getColumnType_worksByIndex() {
    ResultSetMetadata metadata =
        ProtoResultSetMetadata.create(
            Arrays.asList(
                ColumnMetadataImpl.create("col0", stringType()),
                ColumnMetadataImpl.create("col1", int64Type()),
                ColumnMetadataImpl.create("col2", timestampType()),
                ColumnMetadataImpl.create("col3", dateType()),
                ColumnMetadataImpl.create("col4", int64Type())));

    assertThat(metadata.getColumnType(0)).isEqualTo(stringType());
    assertThat(metadata.getColumnType(1)).isEqualTo(int64Type());
    assertThat(metadata.getColumnType(2)).isEqualTo(timestampType());
    assertThat(metadata.getColumnType(3)).isEqualTo(dateType());
    assertThat(metadata.getColumnType(4)).isEqualTo(int64Type());
  }

  @Test
  public void getColumns_returnsColumnsUnchanged() {
    List<ColumnMetadata> columns =
        Arrays.asList(
            ColumnMetadataImpl.create("col0", stringType()),
            ColumnMetadataImpl.create("col1", int64Type()),
            ColumnMetadataImpl.create("col2", timestampType()),
            ColumnMetadataImpl.create("col3", dateType()),
            ColumnMetadataImpl.create("col4", int64Type()));
    ResultSetMetadata metadata = ProtoResultSetMetadata.create(columns);

    assertThat(metadata.getColumns()).isEqualTo(columns);
  }

  @Test
  public void getColumnTypeByNonExistentName_throwsException() {
    ResultSetMetadata metadata =
        ProtoResultSetMetadata.create(
            Arrays.asList(
                ColumnMetadataImpl.create("a", stringType()),
                ColumnMetadataImpl.create("b", int64Type())));

    assertThrows(IllegalArgumentException.class, () -> metadata.getColumnType("c"));
  }

  @Test
  public void getColumnTypeByNonExistentIndex_throwsException() {
    ResultSetMetadata metadata =
        ProtoResultSetMetadata.create(
            Arrays.asList(
                ColumnMetadataImpl.create("a", stringType()),
                ColumnMetadataImpl.create("b", int64Type())));

    assertThrows(IndexOutOfBoundsException.class, () -> metadata.getColumnType(2));
  }

  @Test
  public void getColumnIndexForNonExistentName_throwsException() {
    ResultSetMetadata metadata =
        ProtoResultSetMetadata.create(
            Arrays.asList(
                ColumnMetadataImpl.create("a", stringType()),
                ColumnMetadataImpl.create("b", int64Type())));

    assertThrows(IllegalArgumentException.class, () -> metadata.getColumnIndex("c"));
  }

  @Test
  public void getColumnType_throwsExceptionForDuplicateName() {
    ResultSetMetadata metadata =
        ProtoResultSetMetadata.create(
            Arrays.asList(
                ColumnMetadataImpl.create("test", stringType()),
                ColumnMetadataImpl.create("test", int64Type())));

    assertThrows(IllegalArgumentException.class, () -> metadata.getColumnType("test"));
  }

  @Test
  public void getColumnType_allowsGetByIndexWithDuplicateType() {
    ResultSetMetadata metadata =
        ProtoResultSetMetadata.create(
            Arrays.asList(
                ColumnMetadataImpl.create("test", stringType()),
                ColumnMetadataImpl.create("test", int64Type())));

    assertThat(metadata.getColumnType(0)).isEqualTo(stringType());
    assertThat(metadata.getColumnType(1)).isEqualTo(int64Type());
  }

  @Test
  public void getColumnIndex_throwsExceptionForDuplicateName() {
    ResultSetMetadata metadata =
        ProtoResultSetMetadata.create(
            Arrays.asList(
                ColumnMetadataImpl.create("test", stringType()),
                ColumnMetadataImpl.create("test", int64Type())));

    assertThrows(IllegalArgumentException.class, () -> metadata.getColumnIndex("test"));
  }

  @Test
  public void fromProto_throwsExceptionWithEmptySchema() {
    com.google.bigtable.v2.ResultSetMetadata invalidProto =
        com.google.bigtable.v2.ResultSetMetadata.newBuilder().build();
    assertThrows(IllegalStateException.class, () -> ProtoResultSetMetadata.fromProto(invalidProto));
  }

  @Test
  public void fromProto_withEmptyTypeInSchema_throwsException() {
    com.google.bigtable.v2.ResultSetMetadata invalidProto =
        com.google.bigtable.v2.ResultSetMetadata.newBuilder()
            .setProtoSchema(
                ProtoSchema.newBuilder()
                    .addColumns(
                        com.google.bigtable.v2.ColumnMetadata.newBuilder()
                            .setName("test")
                            .setType(Type.newBuilder().build())))
            .build();
    assertThrows(IllegalStateException.class, () -> ProtoResultSetMetadata.fromProto(invalidProto));
  }

  @Test
  public void fromProto_allowsColumnWithNoName() {
    com.google.bigtable.v2.ResultSetMetadata proto =
        com.google.bigtable.v2.ResultSetMetadata.newBuilder()
            .setProtoSchema(
                ProtoSchema.newBuilder()
                    .addColumns(
                        com.google.bigtable.v2.ColumnMetadata.newBuilder()
                            .setType(stringType())
                            .build()))
            .build();
    ResultSetMetadata metadata = ProtoResultSetMetadata.fromProto(proto);
    assertThat(metadata.getColumns().size()).isEqualTo(1);
    assertThat(metadata.getColumns().get(0).type()).isEqualTo(stringType());
    assertThat(metadata.getColumns().get(0).name()).isEqualTo("");
    assertThat(metadata.getColumnIndex("")).isEqualTo(0);
    assertThat(metadata.getColumnType("")).isEqualTo(stringType());
  }
}
