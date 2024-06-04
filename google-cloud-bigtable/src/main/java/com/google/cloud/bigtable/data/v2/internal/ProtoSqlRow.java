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

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.ColumnMetadata;
import com.google.bigtable.v2.Type;
import com.google.bigtable.v2.Value;
import com.google.cloud.Date;
import com.google.cloud.bigtable.data.v2.models.sql.Struct;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.Map;
import org.threeten.bp.Instant;

@InternalApi
@BetaApi
@AutoValue
// TODO(jackdinglian): Remove bogus implementation of StructReader. This will inherit
// from a shared abstract class that implements the proto conversion logic. This Temporarily
// has a bogus implementation so tests can pass.
public abstract class ProtoSqlRow implements SqlRow {
  /**
   * Creates a new SqlRow
   *
   * @param schema list of column metadata for each column
   * @param values list of the values for each column
   */
  public static ProtoSqlRow create(List<ColumnMetadata> schema, List<Value> values) {
    return new AutoValue_ProtoSqlRow(schema, values);
  }

  /** List of {@link ColumnMetadata} describing the schema of the row. */
  abstract List<ColumnMetadata> schema();

  /** List of {@link Value} objects representing the sql columns for a logical row. */
  public abstract List<com.google.bigtable.v2.Value> values();

  @Override
  public int getColumnIndex(String columnName) {
    return 0;
  }

  @Override
  public Type getColumnType(int columnIndex) {
    return null;
  }

  @Override
  public Type getColumnType(String columnName) {
    return null;
  }

  @Override
  public boolean isNull(int columnIndex) {
    return false;
  }

  @Override
  public boolean isNull(String columnName) {
    return false;
  }

  @Override
  public ByteString getBytes(int columnIndex) {
    return null;
  }

  @Override
  public ByteString getBytes(String columnName) {
    return null;
  }

  @Override
  public String getString(int columnIndex) {
    return null;
  }

  @Override
  public String getString(String columnName) {
    return null;
  }

  @Override
  public long getLong(int columnIndex) {
    return 0;
  }

  @Override
  public long getLong(String columnName) {
    return 0;
  }

  @Override
  public double getDouble(int columnIndex) {
    return 0;
  }

  @Override
  public double getDouble(String columnName) {
    return 0;
  }

  @Override
  public float getFloat(int columnIndex) {
    return 0;
  }

  @Override
  public float getFloat(String columnName) {
    return 0;
  }

  @Override
  public boolean getBoolean(int columnIndex) {
    return false;
  }

  @Override
  public boolean getBoolean(String columnName) {
    return false;
  }

  @Override
  public Instant getTimestamp(int columnIndex) {
    return null;
  }

  @Override
  public Instant getTimestamp(String columnName) {
    return null;
  }

  @Override
  public Date getDate(int columnIndex) {
    return null;
  }

  @Override
  public Date getDate(String columnName) {
    return null;
  }

  @Override
  public Struct getStruct(int columnIndex) {
    return null;
  }

  @Override
  public Struct getStruct(String columnName) {
    return null;
  }

  @Override
  public <ElemType> List<ElemType> getList(int columnIndex) {
    return null;
  }

  @Override
  public <ElemType> List<ElemType> getList(String columnName) {
    return null;
  }

  @Override
  public <K, V> Map<K, V> getMap(int columnIndex) {
    return null;
  }

  @Override
  public <K, V> Map<K, V> getMap(String columnName) {
    return null;
  }
}
