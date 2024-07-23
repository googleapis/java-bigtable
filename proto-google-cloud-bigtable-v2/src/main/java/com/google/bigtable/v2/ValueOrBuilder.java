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
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/v2/data.proto

// Protobuf Java Version: 3.25.3
package com.google.bigtable.v2;

public interface ValueOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.Value)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The verified `Type` of this `Value`, if it cannot be inferred.
   *
   * Read results will never specify the encoding for `type` since the value
   * will already have been decoded by the server. Furthermore, the `type` will
   * be omitted entirely if it can be inferred from a previous response. The
   * exact semantics for inferring `type` will vary, and are therefore
   * documented separately for each read method.
   *
   * When using composite types (Struct, Array, Map) only the outermost `Value`
   * will specify the `type`. This top-level `type` will define the types for
   * any nested `Struct' fields, `Array` elements, or `Map` key/value pairs.
   * If a nested `Value` provides a `type` on write, the request will be
   * rejected with INVALID_ARGUMENT.
   * </pre>
   *
   * <code>.google.bigtable.v2.Type type = 7;</code>
   *
   * @return Whether the type field is set.
   */
  boolean hasType();
  /**
   *
   *
   * <pre>
   * The verified `Type` of this `Value`, if it cannot be inferred.
   *
   * Read results will never specify the encoding for `type` since the value
   * will already have been decoded by the server. Furthermore, the `type` will
   * be omitted entirely if it can be inferred from a previous response. The
   * exact semantics for inferring `type` will vary, and are therefore
   * documented separately for each read method.
   *
   * When using composite types (Struct, Array, Map) only the outermost `Value`
   * will specify the `type`. This top-level `type` will define the types for
   * any nested `Struct' fields, `Array` elements, or `Map` key/value pairs.
   * If a nested `Value` provides a `type` on write, the request will be
   * rejected with INVALID_ARGUMENT.
   * </pre>
   *
   * <code>.google.bigtable.v2.Type type = 7;</code>
   *
   * @return The type.
   */
  com.google.bigtable.v2.Type getType();
  /**
   *
   *
   * <pre>
   * The verified `Type` of this `Value`, if it cannot be inferred.
   *
   * Read results will never specify the encoding for `type` since the value
   * will already have been decoded by the server. Furthermore, the `type` will
   * be omitted entirely if it can be inferred from a previous response. The
   * exact semantics for inferring `type` will vary, and are therefore
   * documented separately for each read method.
   *
   * When using composite types (Struct, Array, Map) only the outermost `Value`
   * will specify the `type`. This top-level `type` will define the types for
   * any nested `Struct' fields, `Array` elements, or `Map` key/value pairs.
   * If a nested `Value` provides a `type` on write, the request will be
   * rejected with INVALID_ARGUMENT.
   * </pre>
   *
   * <code>.google.bigtable.v2.Type type = 7;</code>
   */
  com.google.bigtable.v2.TypeOrBuilder getTypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Represents a raw byte sequence with no type information.
   * The `type` field must be omitted.
   * </pre>
   *
   * <code>bytes raw_value = 8;</code>
   *
   * @return Whether the rawValue field is set.
   */
  boolean hasRawValue();
  /**
   *
   *
   * <pre>
   * Represents a raw byte sequence with no type information.
   * The `type` field must be omitted.
   * </pre>
   *
   * <code>bytes raw_value = 8;</code>
   *
   * @return The rawValue.
   */
  com.google.protobuf.ByteString getRawValue();

  /**
   *
   *
   * <pre>
   * Represents a raw cell timestamp with no type information.
   * The `type` field must be omitted.
   * </pre>
   *
   * <code>int64 raw_timestamp_micros = 9;</code>
   *
   * @return Whether the rawTimestampMicros field is set.
   */
  boolean hasRawTimestampMicros();
  /**
   *
   *
   * <pre>
   * Represents a raw cell timestamp with no type information.
   * The `type` field must be omitted.
   * </pre>
   *
   * <code>int64 raw_timestamp_micros = 9;</code>
   *
   * @return The rawTimestampMicros.
   */
  long getRawTimestampMicros();

  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a byte sequence.
   * </pre>
   *
   * <code>bytes bytes_value = 2;</code>
   *
   * @return Whether the bytesValue field is set.
   */
  boolean hasBytesValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a byte sequence.
   * </pre>
   *
   * <code>bytes bytes_value = 2;</code>
   *
   * @return The bytesValue.
   */
  com.google.protobuf.ByteString getBytesValue();

  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a string.
   * </pre>
   *
   * <code>string string_value = 3;</code>
   *
   * @return Whether the stringValue field is set.
   */
  boolean hasStringValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a string.
   * </pre>
   *
   * <code>string string_value = 3;</code>
   *
   * @return The stringValue.
   */
  java.lang.String getStringValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a string.
   * </pre>
   *
   * <code>string string_value = 3;</code>
   *
   * @return The bytes for stringValue.
   */
  com.google.protobuf.ByteString getStringValueBytes();

  /**
   *
   *
   * <pre>
   * Represents a typed value transported as an integer.
   * </pre>
   *
   * <code>int64 int_value = 6;</code>
   *
   * @return Whether the intValue field is set.
   */
  boolean hasIntValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as an integer.
   * </pre>
   *
   * <code>int64 int_value = 6;</code>
   *
   * @return The intValue.
   */
  long getIntValue();

  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a boolean.
   * </pre>
   *
   * <code>bool bool_value = 10;</code>
   *
   * @return Whether the boolValue field is set.
   */
  boolean hasBoolValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a boolean.
   * </pre>
   *
   * <code>bool bool_value = 10;</code>
   *
   * @return The boolValue.
   */
  boolean getBoolValue();

  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a floating point number.
   * </pre>
   *
   * <code>double float_value = 11;</code>
   *
   * @return Whether the floatValue field is set.
   */
  boolean hasFloatValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a floating point number.
   * </pre>
   *
   * <code>double float_value = 11;</code>
   *
   * @return The floatValue.
   */
  double getFloatValue();

  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a timestamp.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp timestamp_value = 12;</code>
   *
   * @return Whether the timestampValue field is set.
   */
  boolean hasTimestampValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a timestamp.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp timestamp_value = 12;</code>
   *
   * @return The timestampValue.
   */
  com.google.protobuf.Timestamp getTimestampValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a timestamp.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp timestamp_value = 12;</code>
   */
  com.google.protobuf.TimestampOrBuilder getTimestampValueOrBuilder();

  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a date.
   * </pre>
   *
   * <code>.google.type.Date date_value = 13;</code>
   *
   * @return Whether the dateValue field is set.
   */
  boolean hasDateValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a date.
   * </pre>
   *
   * <code>.google.type.Date date_value = 13;</code>
   *
   * @return The dateValue.
   */
  com.google.type.Date getDateValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a date.
   * </pre>
   *
   * <code>.google.type.Date date_value = 13;</code>
   */
  com.google.type.DateOrBuilder getDateValueOrBuilder();

  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a sequence of values.
   * To differentiate between `Struct`, `Array`, and `Map`, the outermost
   * `Value` must provide an explicit `type` on write. This `type` will
   * apply recursively to the nested `Struct` fields, `Array` elements,
   * or `Map` key/value pairs, which *must not* supply their own `type`.
   * </pre>
   *
   * <code>.google.bigtable.v2.ArrayValue array_value = 4;</code>
   *
   * @return Whether the arrayValue field is set.
   */
  boolean hasArrayValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a sequence of values.
   * To differentiate between `Struct`, `Array`, and `Map`, the outermost
   * `Value` must provide an explicit `type` on write. This `type` will
   * apply recursively to the nested `Struct` fields, `Array` elements,
   * or `Map` key/value pairs, which *must not* supply their own `type`.
   * </pre>
   *
   * <code>.google.bigtable.v2.ArrayValue array_value = 4;</code>
   *
   * @return The arrayValue.
   */
  com.google.bigtable.v2.ArrayValue getArrayValue();
  /**
   *
   *
   * <pre>
   * Represents a typed value transported as a sequence of values.
   * To differentiate between `Struct`, `Array`, and `Map`, the outermost
   * `Value` must provide an explicit `type` on write. This `type` will
   * apply recursively to the nested `Struct` fields, `Array` elements,
   * or `Map` key/value pairs, which *must not* supply their own `type`.
   * </pre>
   *
   * <code>.google.bigtable.v2.ArrayValue array_value = 4;</code>
   */
  com.google.bigtable.v2.ArrayValueOrBuilder getArrayValueOrBuilder();

  com.google.bigtable.v2.Value.KindCase getKindCase();
}
