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
// source: google/bigtable/v2/data.proto

// Protobuf Java Version: 3.25.5
package com.google.bigtable.v2;

public interface ProtoRowsOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ProtoRows)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * A proto rows message consists of a list of values. Every N complete values
   * defines a row, where N is equal to the  number of entries in the
   * `metadata.proto_schema.columns` value received in the first response.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Value values = 2;</code>
   */
  java.util.List<com.google.bigtable.v2.Value> getValuesList();
  /**
   *
   *
   * <pre>
   * A proto rows message consists of a list of values. Every N complete values
   * defines a row, where N is equal to the  number of entries in the
   * `metadata.proto_schema.columns` value received in the first response.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Value values = 2;</code>
   */
  com.google.bigtable.v2.Value getValues(int index);
  /**
   *
   *
   * <pre>
   * A proto rows message consists of a list of values. Every N complete values
   * defines a row, where N is equal to the  number of entries in the
   * `metadata.proto_schema.columns` value received in the first response.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Value values = 2;</code>
   */
  int getValuesCount();
  /**
   *
   *
   * <pre>
   * A proto rows message consists of a list of values. Every N complete values
   * defines a row, where N is equal to the  number of entries in the
   * `metadata.proto_schema.columns` value received in the first response.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Value values = 2;</code>
   */
  java.util.List<? extends com.google.bigtable.v2.ValueOrBuilder> getValuesOrBuilderList();
  /**
   *
   *
   * <pre>
   * A proto rows message consists of a list of values. Every N complete values
   * defines a row, where N is equal to the  number of entries in the
   * `metadata.proto_schema.columns` value received in the first response.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Value values = 2;</code>
   */
  com.google.bigtable.v2.ValueOrBuilder getValuesOrBuilder(int index);
}
