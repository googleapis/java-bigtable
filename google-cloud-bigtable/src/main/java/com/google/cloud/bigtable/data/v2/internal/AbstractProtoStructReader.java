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
import com.google.bigtable.v2.Type;
import com.google.bigtable.v2.Value;
import com.google.bigtable.v2.Value.KindCase;
import com.google.cloud.Date;
import com.google.cloud.bigtable.data.v2.models.sql.Struct;
import com.google.cloud.bigtable.data.v2.models.sql.StructReader;
import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.threeten.bp.Instant;

@InternalApi
public abstract class AbstractProtoStructReader implements StructReader {

  abstract List<Value> values();

  // Force subclasses to override equals and hashcode. We need this for tests.
  public abstract boolean equals(Object other);

  public abstract int hashCode();

  /**
   * @param columnName name of the column
   * @return the index of the column named {@code columnName}
   * @throws IllegalArgumentException if there is not exactly one column with the given name
   */
  public abstract int getColumnIndex(String columnName);

  /**
   * @param columnIndex index of the column
   * @return the type of the column at the given index
   */
  // TODO(jackdingilian): Fix this so it uses type wrappers
  public abstract Type getColumnType(int columnIndex);

  /**
   * @param columnName name of the column
   * @return the type of the column with the given name
   * @throws IllegalArgumentException if there is not exactly one column with the given name
   */
  // TODO(jackdingilian): Fix this so it uses type wrappers
  public Type getColumnType(String columnName) {
    return getColumnType(getColumnIndex(columnName));
  }

  @Override
  public boolean isNull(int columnIndex) {
    Value value = values().get(columnIndex);
    return value.getKindCase().equals(KindCase.KIND_NOT_SET);
  }

  @Override
  public boolean isNull(String columnName) {
    return isNull(getColumnIndex(columnName));
  }

  @Override
  public ByteString getBytes(int columnIndex) {
    checkNonNullOfType(columnIndex, Type.KindCase.BYTES_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    return value.getBytesValue();
  }

  @Override
  public ByteString getBytes(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    checkNonNullOfType(columnIndex, Type.KindCase.BYTES_TYPE, columnName);
    Value value = values().get(columnIndex);
    return value.getBytesValue();
  }

  @Override
  public String getString(int columnIndex) {
    checkNonNullOfType(columnIndex, Type.KindCase.STRING_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    return value.getStringValue();
  }

  @Override
  public String getString(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    checkNonNullOfType(columnIndex, Type.KindCase.STRING_TYPE, columnName);
    Value value = values().get(columnIndex);
    return value.getStringValue();
  }

  @Override
  public long getLong(int columnIndex) {
    checkNonNullOfType(columnIndex, Type.KindCase.INT64_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    return value.getIntValue();
  }

  @Override
  public long getLong(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    checkNonNullOfType(columnIndex, Type.KindCase.INT64_TYPE, columnName);
    Value value = values().get(columnIndex);
    return value.getIntValue();
  }

  @Override
  public double getDouble(int columnIndex) {
    checkNonNullOfType(columnIndex, Type.KindCase.FLOAT64_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    return value.getFloatValue();
  }

  @Override
  public double getDouble(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    checkNonNullOfType(columnIndex, Type.KindCase.FLOAT64_TYPE, columnName);
    Value value = values().get(columnIndex);
    return value.getFloatValue();
  }

  @Override
  public float getFloat(int columnIndex) {
    checkNonNullOfType(columnIndex, Type.KindCase.FLOAT32_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    return (float) value.getFloatValue();
  }

  @Override
  public float getFloat(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    checkNonNullOfType(columnIndex, Type.KindCase.FLOAT32_TYPE, columnName);
    Value value = values().get(columnIndex);
    return (float) value.getFloatValue();
  }

  @Override
  public boolean getBoolean(int columnIndex) {
    checkNonNullOfType(columnIndex, Type.KindCase.BOOL_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    return value.getBoolValue();
  }

  @Override
  public boolean getBoolean(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    checkNonNullOfType(columnIndex, Type.KindCase.BOOL_TYPE, columnName);
    Value value = values().get(columnIndex);
    return value.getBoolValue();
  }

  @Override
  public Instant getTimestamp(int columnIndex) {
    checkNonNullOfType(columnIndex, Type.KindCase.TIMESTAMP_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    return toInstant(value.getTimestampValue());
  }

  @Override
  public Instant getTimestamp(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    checkNonNullOfType(columnIndex, Type.KindCase.TIMESTAMP_TYPE, columnName);
    Value value = values().get(columnIndex);
    return toInstant(value.getTimestampValue());
  }

  @Override
  public Date getDate(int columnIndex) {
    checkNonNullOfType(columnIndex, Type.KindCase.DATE_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    return fromProto(value.getDateValue());
  }

  @Override
  public Date getDate(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    checkNonNullOfType(columnIndex, Type.KindCase.DATE_TYPE, columnName);
    Value value = values().get(columnIndex);
    return fromProto(value.getDateValue());
  }

  @Override
  public Struct getStruct(int columnIndex) {
    checkNonNullOfType(columnIndex, Type.KindCase.STRUCT_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    // A struct value is represented as an array
    return ProtoStruct.create(getColumnType(columnIndex).getStructType(), value.getArrayValue());
  }

  @Override
  public Struct getStruct(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    checkNonNullOfType(columnIndex, Type.KindCase.STRUCT_TYPE, columnName);
    Value value = values().get(columnIndex);
    // A struct value is represented as an array
    return ProtoStruct.create(getColumnType(columnIndex).getStructType(), value.getArrayValue());
  }

  // TODO(jackdingilian): Add type param so we can avoid unsafe cast
  @Override
  public <ElemType> List<ElemType> getList(int columnIndex) {
    Type type = getColumnType(columnIndex);
    checkNonNullOfType(columnIndex, Type.KindCase.ARRAY_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    Type elemType = type.getArrayType().getElementType();
    return (List<ElemType>) decodeValue(value, type);
  }

  // TODO(jackdingilian): Add type param so we can avoid unsafe cast
  @Override
  public <ElemType> List<ElemType> getList(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    Type type = getColumnType(columnIndex);
    checkNonNullOfType(columnIndex, Type.KindCase.ARRAY_TYPE, columnName);
    Value value = values().get(columnIndex);
    return (List<ElemType>) decodeValue(value, type);
  }

  // TODO(jackdingilian): Add type param so we can avoid unsafe cast
  @Override
  public <K, V> Map<K, V> getMap(int columnIndex) {
    Type type = getColumnType(columnIndex);
    checkNonNullOfType(columnIndex, Type.KindCase.MAP_TYPE, columnIndex);
    Value value = values().get(columnIndex);
    return (Map<K, V>) decodeValue(value, type);
  }

  // TODO(jackdingilian): Add type param so we can avoid unsafe cast
  @Override
  public <K, V> Map<K, V> getMap(String columnName) {
    int columnIndex = getColumnIndex(columnName);
    Type type = getColumnType(columnIndex);
    checkNonNullOfType(columnIndex, Type.KindCase.MAP_TYPE, columnName);
    Value value = values().get(columnIndex);
    return (Map<K, V>) decodeValue(value, type);
  }

  Object decodeValue(Value value, Type type) {
    if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
      return null;
    }
    switch (type.getKindCase()) {
      case BYTES_TYPE:
        return value.getBytesValue();
      case STRING_TYPE:
        return value.getStringValue();
      case INT64_TYPE:
        return value.getIntValue();
      case FLOAT64_TYPE:
      case FLOAT32_TYPE:
        return value.getFloatValue();
      case BOOL_TYPE:
        return value.getBoolValue();
      case TIMESTAMP_TYPE:
        return toInstant(value.getTimestampValue());
      case DATE_TYPE:
        return fromProto(value.getDateValue());
      case STRUCT_TYPE:
        // A struct value is represented as an array
        return ProtoStruct.create(type.getStructType(), value.getArrayValue());
      case ARRAY_TYPE:
        ArrayList<Object> listBuilder = new ArrayList<>();
        Type elemType = type.getArrayType().getElementType();
        for (Value elem : value.getArrayValue().getValuesList()) {
          listBuilder.add(decodeValue(elem, elemType));
        }
        // We use unmodifiableList instead of guava ImmutableList to allow null elements
        return Collections.unmodifiableList(listBuilder);
      case MAP_TYPE:
        HashMap<Object, Object> mapBuilder = new HashMap<>();
        Type keyType = type.getMapType().getKeyType();
        Type valType = type.getMapType().getValueType();
        // A map value is represented as an array of k, v tuples where the tuple is a nested array
        for (Value entry : value.getArrayValue().getValuesList()) {
          Object key = decodeValue(entry.getArrayValue().getValues(0), keyType);
          Object val = decodeValue(entry.getArrayValue().getValues(1), valType);
          mapBuilder.put(key, val);
        }
        // We use unmodifiableMap instead of guava ImmutableMap to allow null keys & values
        return Collections.unmodifiableMap(mapBuilder);
      default:
        // We should have already thrown an exception in the SqlRowMerger
        throw new IllegalStateException("Unrecognized type: " + type.getKindCase().name());
    }
  }

  // TODO(jackdingilian) validate complex types
  private void checkNonNullOfType(
      int columnIndex, Type.KindCase expectedType, Object columnNameForError) {
    Type type = getColumnType(columnIndex);
    Preconditions.checkState(
        expectedType.equals(type.getKindCase()),
        "Column %s is not of correct type: expected %s but was %s",
        columnNameForError,
        expectedType,
        type.getKindCase());
    if (isNull(columnIndex)) {
      throw new NullPointerException("Column " + columnNameForError + " contains NULL value");
    }
  }

  private Instant toInstant(Timestamp timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  private Date fromProto(com.google.type.Date proto) {
    return Date.fromYearMonthDay(proto.getYear(), proto.getMonth(), proto.getDay());
  }
}
