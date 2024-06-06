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

import com.google.api.core.InternalApi;
import com.google.bigtable.v2.ProtoSchema;
import com.google.bigtable.v2.ResultSetMetadata.SchemaCase;
import com.google.bigtable.v2.Type;
import com.google.cloud.bigtable.data.v2.models.sql.ColumnMetadata;
import com.google.cloud.bigtable.data.v2.models.sql.ResultSetMetadata;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * Implementation of {@link ResultSetMetadata} using an underlying protobuf schema.
 *
 * <p>This is considered an internal implementation detail and not meant to be used by applications.
 */
@InternalApi
public class ProtoResultSetMetadata implements ResultSetMetadata {

  // It is valid for a select query to return columns with the same name. This marker is used
  // internally in the client to designate that getting a value by column name is invalid and will
  // be converted into an exception.
  private static final int AMBIGUOUS_FIELD_MARKER = -1;

  private final List<ColumnMetadata> columns;
  private final Map<String, Integer> columnNameMapping;

  public static ResultSetMetadata create(List<ColumnMetadata> columns) {
    return new ProtoResultSetMetadata(columns);
  }

  private ProtoResultSetMetadata(List<ColumnMetadata> columns) {
    this.columns = ImmutableList.copyOf(columns);
    // consider deferring building this until first get-by-column-name call
    this.columnNameMapping = buildColumnNameMapping(columns);
  }

  @Override
  public List<ColumnMetadata> getColumns() {
    return columns;
  }

  @Override
  public Type getColumnType(int columnIndex) {
    return columns.get(columnIndex).type();
  }

  @Override
  public Type getColumnType(String columnName) {
    return getColumnType(getColumnIndex(columnName));
  }

  @Override
  public int getColumnIndex(String columnName) {
    Integer index = columnNameMapping.get(columnName);
    if (index == null) {
      throw new IllegalArgumentException("Column name not found: " + columnName);
    }
    int unboxedIndex = index;
    if (unboxedIndex == AMBIGUOUS_FIELD_MARKER) {
      throw new IllegalArgumentException(
          "Ambiguous column name: " + columnName + ". Same name is used for multiple columns.");
    }
    return unboxedIndex;
  }

  @InternalApi
  public static ResultSetMetadata fromProto(com.google.bigtable.v2.ResultSetMetadata proto) {
    Preconditions.checkState(
        proto.getSchemaCase().equals(SchemaCase.PROTO_SCHEMA),
        "Unsupported schema type: %s",
        proto.getSchemaCase().name());
    ProtoSchema schema = proto.getProtoSchema();
    validateSchema(schema);
    ImmutableList.Builder<ColumnMetadata> columnsBuilder = ImmutableList.builder();
    for (com.google.bigtable.v2.ColumnMetadata protoColumn : schema.getColumnsList()) {
      columnsBuilder.add(ColumnMetadataImpl.fromProto(protoColumn));
    }
    return create(columnsBuilder.build());
  }

  private static void validateSchema(ProtoSchema schema) {
    List<com.google.bigtable.v2.ColumnMetadata> columns = schema.getColumnsList();
    Preconditions.checkState(!columns.isEmpty(), "columns cannot be empty");
    for (com.google.bigtable.v2.ColumnMetadata column : columns) {
      Preconditions.checkState(
          column.getType().getKindCase() != Type.KindCase.KIND_NOT_SET,
          "Column type cannot be empty");
    }
  }

  private Map<String, Integer> buildColumnNameMapping(List<ColumnMetadata> columns) {
    HashMap<String, Integer> mapping = new HashMap<>(columns.size());
    for (int i = 0; i < columns.size(); i++) {
      ColumnMetadata column = columns.get(i);
      if (mapping.containsKey(column.name())) {
        mapping.put(column.name(), AMBIGUOUS_FIELD_MARKER);
      } else {
        mapping.put(column.name(), i);
      }
    }
    return ImmutableMap.copyOf(mapping);
  }

  @Override
  public boolean equals(@Nullable Object other) {
    if (other instanceof ProtoResultSetMetadata) {
      ProtoResultSetMetadata o = (ProtoResultSetMetadata) other;
      // columnNameMapping is derived from columns, so we only need to compare columns
      return columns.equals(o.columns);
    }
    return false;
  }
}
