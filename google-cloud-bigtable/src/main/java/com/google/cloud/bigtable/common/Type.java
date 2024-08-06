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
package com.google.cloud.bigtable.common;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import com.google.cloud.bigtable.data.v2.internal.ColumnToIndexMapper;
import com.google.cloud.bigtable.data.v2.models.sql.SqlType;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import java.util.List;
import org.threeten.bp.Instant;

/**
 * Shared type implementations. Right now this is only used by SqlType but this will become a shared
 * definition with Schema type (called {@link com.google.cloud.bigtable.admin.v2.models.Type} right
 * now), and any other type interfaces needed in the future.
 *
 * <p>This is considered an internal implementation detail and not meant to be used by applications.
 * Types should only be used through the relevant interfaces and factories, e.g. {@link SqlType}.
 */
@BetaApi
@InternalApi
public interface Type {

  @AutoValue
  abstract class Bytes implements Type, SqlType<ByteString> {

    public static Bytes create() {
      return DefaultInstances.BYTES;
    }

    @Override
    public Code getCode() {
      return Code.BYTES;
    }

    @Override
    public java.lang.String toString() {
      return getCode().name();
    }
  }

  @AutoValue
  abstract class String implements Type, SqlType<java.lang.String> {
    public static String create() {
      return DefaultInstances.STRING;
    }

    @Override
    public Code getCode() {
      return Code.STRING;
    }

    @Override
    public java.lang.String toString() {
      return getCode().name();
    }
  }

  @AutoValue
  abstract class Int64 implements Type, SqlType<Long> {
    public static Int64 create() {
      return DefaultInstances.INT64;
    }

    @Override
    public Code getCode() {
      return Code.INT64;
    }

    @Override
    public java.lang.String toString() {
      return getCode().name();
    }
  }

  @AutoValue
  abstract class Float64 implements Type, SqlType<Double> {
    public static Float64 create() {
      return DefaultInstances.FLOAT64;
    }

    @Override
    public Code getCode() {
      return Code.FLOAT64;
    }

    @Override
    public java.lang.String toString() {
      return getCode().name();
    }
  }

  @AutoValue
  abstract class Float32 implements Type, SqlType<Float> {
    public static Float32 create() {
      return DefaultInstances.FLOAT32;
    }

    @Override
    public Code getCode() {
      return Code.FLOAT32;
    }

    @Override
    public java.lang.String toString() {
      return getCode().name();
    }
  }

  @AutoValue
  abstract class Bool implements Type, SqlType<Boolean> {
    public static Bool create() {
      return DefaultInstances.BOOL;
    }

    @Override
    public Code getCode() {
      return Code.BOOL;
    }

    @Override
    public java.lang.String toString() {
      return getCode().name();
    }
  }

  @AutoValue
  abstract class Timestamp implements Type, SqlType<Instant> {
    public static Timestamp create() {
      return DefaultInstances.TIMESTAMP;
    }

    @Override
    public Code getCode() {
      return Code.TIMESTAMP;
    }

    @Override
    public java.lang.String toString() {
      return getCode().name();
    }
  }

  @AutoValue
  abstract class Date implements Type, SqlType<com.google.cloud.Date> {
    public static Date create() {
      return DefaultInstances.DATE;
    }

    @Override
    public Code getCode() {
      return Code.DATE;
    }

    @Override
    public java.lang.String toString() {
      return getCode().name();
    }
  }

  /**
   * This is a special version of struct that is intended to only be used in the {@link
   * com.google.cloud.bigtable.data.v2.models.sql.StructReader} getters that require types. We don't
   * want users to need to specify the struct schema when the schema will be validated on calls to
   * {@link com.google.cloud.bigtable.data.v2.models.sql.StructReader} methods on the struct.
   *
   * <p>Any attempts to interact with the schema will throw an exception.
   *
   * <p>For example the historical map data type uses this as follows:
   *
   * <pre>{@code
   * Map<ByteString, List<Struct>> historicalMap =
   *     resultSet.getMap(
   *        "cf",
   *        SqlType.mapOf(SqlType.bytes(), SqlType.arrayOf(SqlType.struct())));
   * Struct struct = historicalMap.get("column").get(0);
   * // Struct schema will be validated here so there's no need for users to pass the schema to getMap above
   * ByteString value = struct.getBytes("value");
   * }</pre>
   */
  @AutoValue
  abstract class SchemalessStruct implements Type, SqlType.Struct {
    public static SchemalessStruct create() {
      return DefaultInstances.SCHEMALESS_STRUCT;
    }

    @Override
    public Code getCode() {
      return Code.STRUCT;
    }

    @Override
    public List<Field> getFields() {
      throw new UnsupportedOperationException(
          "Attempting to access schema of Schemaless Struct. These structs should only be used for typing of StructReader data access calls.");
    }

    @Override
    public SqlType<?> getType(int fieldIndex) {
      throw new UnsupportedOperationException(
          "Attempting to access schema of Schemaless Struct. These structs should only be used for typing of StructReader data access calls.");
    }

    @Override
    public SqlType<?> getType(java.lang.String fieldName) {
      throw new UnsupportedOperationException(
          "Attempting to access schema of Schemaless Struct. These structs should only be used for typing of StructReader data access calls.");
    }

    @Override
    public int getColumnIndex(java.lang.String fieldName) {
      throw new UnsupportedOperationException(
          "Attempting to access schema of Schemaless Struct. These structs should only be used for typing of StructReader data access calls.");
    }

    @Override
    public java.lang.String toString() {
      return getCode().name();
    }
  }

  /**
   * Struct implementation that contains a schema that users can access. This should never be
   * constructed by users. It is only intended to be created directly from Type protobufs.
   */
  class StructWithSchema extends ColumnToIndexMapper implements Type, SqlType.Struct {

    private final List<Field> fields;

    @InternalApi("Visible for testing")
    public StructWithSchema(List<Field> fields) {
      super(fields);
      this.fields = fields;
    }

    @InternalApi("Visible for testing")
    @AutoValue
    public abstract static class Field implements SqlType.Struct.Field {
      public static Field fromProto(com.google.bigtable.v2.Type.Struct.Field proto) {
        return new AutoValue_Type_StructWithSchema_Field(
            proto.getFieldName(), SqlType.fromProto(proto.getType()));
      }

      @Override
      public abstract java.lang.String name();

      @Override
      public abstract SqlType<?> type();
    }

    public static StructWithSchema fromProto(com.google.bigtable.v2.Type.Struct proto) {
      ImmutableList.Builder<Field> fields = ImmutableList.builder();
      for (com.google.bigtable.v2.Type.Struct.Field protoField : proto.getFieldsList()) {
        fields.add(Field.fromProto(protoField));
      }
      return new StructWithSchema(fields.build());
    }

    @Override
    public Code getCode() {
      return Code.STRUCT;
    }

    @Override
    public List<StructWithSchema.Field> getFields() {
      return fields;
    }

    @Override
    public SqlType<?> getType(int fieldIndex) {
      return fields.get(fieldIndex).type();
    }

    @Override
    public SqlType<?> getType(java.lang.String fieldName) {
      int index = getColumnIndex(fieldName);
      return getType(index);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      StructWithSchema struct = (StructWithSchema) obj;
      // Everything is derived from fields so that's all we need to compare;
      return Objects.equal(getFields(), struct.getFields());
    }

    @Override
    public int hashCode() {
      // Everything is derived from fields so that's all we need;
      return Objects.hashCode(fields);
    }

    @Override
    public java.lang.String toString() {
      return getCode().name() + "{fields=" + fields.toString() + "}";
    }
  }

  @AutoValue
  abstract class Array<Elem> implements Type, SqlType.Array<Elem> {
    // Do we need non-sql type array elements? Might get messy
    public static <T> Type.Array<T> create(SqlType<T> elemType) {
      return new AutoValue_Type_Array<>(elemType);
    }

    protected abstract SqlType<Elem> elementType();

    @Override
    public Code getCode() {
      return Code.ARRAY;
    }

    @Override
    public SqlType<Elem> getElementType() {
      return elementType();
    }

    @Override
    public java.lang.String toString() {
      return getCode().name() + "{elementType=" + getElementType().getCode() + "}";
    }
  }

  @AutoValue
  abstract class Map<K, V> implements Type, SqlType.Map<K, V> {
    // Same question as for array
    public static <K, V> Type.Map<K, V> create(SqlType<K> keyType, SqlType<V> valueType) {
      return new AutoValue_Type_Map<>(keyType, valueType);
    }

    protected abstract SqlType<K> keyType();

    protected abstract SqlType<V> valueType();

    @Override
    public Code getCode() {
      return Code.MAP;
    }

    @Override
    public SqlType<K> getKeyType() {
      return keyType();
    }

    @Override
    public SqlType<V> getValueType() {
      return valueType();
    }

    @Override
    public java.lang.String toString() {
      return getCode().name()
          + "{keyType="
          + getKeyType().toString()
          + ", valueType="
          + getValueType().toString()
          + "}";
    }
  }

  // Implementation detail to make singleton instances private without referencing the concrete
  // autovalue generated class from the abstract base classes.
  @InternalApi
  class DefaultInstances {
    private static final Bytes BYTES = new AutoValue_Type_Bytes();
    private static final String STRING = new AutoValue_Type_String();
    private static final Int64 INT64 = new AutoValue_Type_Int64();
    private static final Float64 FLOAT64 = new AutoValue_Type_Float64();
    private static final Float32 FLOAT32 = new AutoValue_Type_Float32();
    private static final Bool BOOL = new AutoValue_Type_Bool();
    private static final Timestamp TIMESTAMP = new AutoValue_Type_Timestamp();
    private static final Date DATE = new AutoValue_Type_Date();
    private static final SchemalessStruct SCHEMALESS_STRUCT = new AutoValue_Type_SchemalessStruct();
  }
}
