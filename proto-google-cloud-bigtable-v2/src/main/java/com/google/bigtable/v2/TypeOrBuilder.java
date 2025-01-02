/*
 * Copyright 2025 Google LLC
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
// NO CHECKED-IN PROTOBUF GENCODE
// source: google/bigtable/v2/types.proto
// Protobuf Java Version: 4.29.0

package com.google.bigtable.v2;

public interface TypeOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.Type)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Bytes
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Bytes bytes_type = 1;</code>
   *
   * @return Whether the bytesType field is set.
   */
  boolean hasBytesType();
  /**
   *
   *
   * <pre>
   * Bytes
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Bytes bytes_type = 1;</code>
   *
   * @return The bytesType.
   */
  com.google.bigtable.v2.Type.Bytes getBytesType();
  /**
   *
   *
   * <pre>
   * Bytes
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Bytes bytes_type = 1;</code>
   */
  com.google.bigtable.v2.Type.BytesOrBuilder getBytesTypeOrBuilder();

  /**
   *
   *
   * <pre>
   * String
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.String string_type = 2;</code>
   *
   * @return Whether the stringType field is set.
   */
  boolean hasStringType();
  /**
   *
   *
   * <pre>
   * String
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.String string_type = 2;</code>
   *
   * @return The stringType.
   */
  com.google.bigtable.v2.Type.String getStringType();
  /**
   *
   *
   * <pre>
   * String
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.String string_type = 2;</code>
   */
  com.google.bigtable.v2.Type.StringOrBuilder getStringTypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Int64
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Int64 int64_type = 5;</code>
   *
   * @return Whether the int64Type field is set.
   */
  boolean hasInt64Type();
  /**
   *
   *
   * <pre>
   * Int64
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Int64 int64_type = 5;</code>
   *
   * @return The int64Type.
   */
  com.google.bigtable.v2.Type.Int64 getInt64Type();
  /**
   *
   *
   * <pre>
   * Int64
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Int64 int64_type = 5;</code>
   */
  com.google.bigtable.v2.Type.Int64OrBuilder getInt64TypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Float32
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Float32 float32_type = 12;</code>
   *
   * @return Whether the float32Type field is set.
   */
  boolean hasFloat32Type();
  /**
   *
   *
   * <pre>
   * Float32
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Float32 float32_type = 12;</code>
   *
   * @return The float32Type.
   */
  com.google.bigtable.v2.Type.Float32 getFloat32Type();
  /**
   *
   *
   * <pre>
   * Float32
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Float32 float32_type = 12;</code>
   */
  com.google.bigtable.v2.Type.Float32OrBuilder getFloat32TypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Float64
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Float64 float64_type = 9;</code>
   *
   * @return Whether the float64Type field is set.
   */
  boolean hasFloat64Type();
  /**
   *
   *
   * <pre>
   * Float64
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Float64 float64_type = 9;</code>
   *
   * @return The float64Type.
   */
  com.google.bigtable.v2.Type.Float64 getFloat64Type();
  /**
   *
   *
   * <pre>
   * Float64
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Float64 float64_type = 9;</code>
   */
  com.google.bigtable.v2.Type.Float64OrBuilder getFloat64TypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Bool
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Bool bool_type = 8;</code>
   *
   * @return Whether the boolType field is set.
   */
  boolean hasBoolType();
  /**
   *
   *
   * <pre>
   * Bool
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Bool bool_type = 8;</code>
   *
   * @return The boolType.
   */
  com.google.bigtable.v2.Type.Bool getBoolType();
  /**
   *
   *
   * <pre>
   * Bool
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Bool bool_type = 8;</code>
   */
  com.google.bigtable.v2.Type.BoolOrBuilder getBoolTypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Timestamp
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Timestamp timestamp_type = 10;</code>
   *
   * @return Whether the timestampType field is set.
   */
  boolean hasTimestampType();
  /**
   *
   *
   * <pre>
   * Timestamp
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Timestamp timestamp_type = 10;</code>
   *
   * @return The timestampType.
   */
  com.google.bigtable.v2.Type.Timestamp getTimestampType();
  /**
   *
   *
   * <pre>
   * Timestamp
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Timestamp timestamp_type = 10;</code>
   */
  com.google.bigtable.v2.Type.TimestampOrBuilder getTimestampTypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Date
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Date date_type = 11;</code>
   *
   * @return Whether the dateType field is set.
   */
  boolean hasDateType();
  /**
   *
   *
   * <pre>
   * Date
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Date date_type = 11;</code>
   *
   * @return The dateType.
   */
  com.google.bigtable.v2.Type.Date getDateType();
  /**
   *
   *
   * <pre>
   * Date
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Date date_type = 11;</code>
   */
  com.google.bigtable.v2.Type.DateOrBuilder getDateTypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Aggregate
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Aggregate aggregate_type = 6;</code>
   *
   * @return Whether the aggregateType field is set.
   */
  boolean hasAggregateType();
  /**
   *
   *
   * <pre>
   * Aggregate
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Aggregate aggregate_type = 6;</code>
   *
   * @return The aggregateType.
   */
  com.google.bigtable.v2.Type.Aggregate getAggregateType();
  /**
   *
   *
   * <pre>
   * Aggregate
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Aggregate aggregate_type = 6;</code>
   */
  com.google.bigtable.v2.Type.AggregateOrBuilder getAggregateTypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Struct
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Struct struct_type = 7;</code>
   *
   * @return Whether the structType field is set.
   */
  boolean hasStructType();
  /**
   *
   *
   * <pre>
   * Struct
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Struct struct_type = 7;</code>
   *
   * @return The structType.
   */
  com.google.bigtable.v2.Type.Struct getStructType();
  /**
   *
   *
   * <pre>
   * Struct
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Struct struct_type = 7;</code>
   */
  com.google.bigtable.v2.Type.StructOrBuilder getStructTypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Array
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Array array_type = 3;</code>
   *
   * @return Whether the arrayType field is set.
   */
  boolean hasArrayType();
  /**
   *
   *
   * <pre>
   * Array
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Array array_type = 3;</code>
   *
   * @return The arrayType.
   */
  com.google.bigtable.v2.Type.Array getArrayType();
  /**
   *
   *
   * <pre>
   * Array
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Array array_type = 3;</code>
   */
  com.google.bigtable.v2.Type.ArrayOrBuilder getArrayTypeOrBuilder();

  /**
   *
   *
   * <pre>
   * Map
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Map map_type = 4;</code>
   *
   * @return Whether the mapType field is set.
   */
  boolean hasMapType();
  /**
   *
   *
   * <pre>
   * Map
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Map map_type = 4;</code>
   *
   * @return The mapType.
   */
  com.google.bigtable.v2.Type.Map getMapType();
  /**
   *
   *
   * <pre>
   * Map
   * </pre>
   *
   * <code>.google.bigtable.v2.Type.Map map_type = 4;</code>
   */
  com.google.bigtable.v2.Type.MapOrBuilder getMapTypeOrBuilder();

  com.google.bigtable.v2.Type.KindCase getKindCase();
}
