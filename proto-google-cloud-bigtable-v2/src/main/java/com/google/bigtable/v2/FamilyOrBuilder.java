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
// source: google/bigtable/v2/data.proto
// Protobuf Java Version: 4.29.0

package com.google.bigtable.v2;

public interface FamilyOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.Family)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The unique key which identifies this family within its row. This is the
   * same key that's used to identify the family in, for example, a RowFilter
   * which sets its "family_name_regex_filter" field.
   * Must match `[-_.a-zA-Z0-9]+`, except that AggregatingRowProcessors may
   * produce cells in a sentinel family with an empty name.
   * Must be no greater than 64 characters in length.
   * </pre>
   *
   * <code>string name = 1;</code>
   *
   * @return The name.
   */
  java.lang.String getName();
  /**
   *
   *
   * <pre>
   * The unique key which identifies this family within its row. This is the
   * same key that's used to identify the family in, for example, a RowFilter
   * which sets its "family_name_regex_filter" field.
   * Must match `[-_.a-zA-Z0-9]+`, except that AggregatingRowProcessors may
   * produce cells in a sentinel family with an empty name.
   * Must be no greater than 64 characters in length.
   * </pre>
   *
   * <code>string name = 1;</code>
   *
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of increasing "qualifier".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Column columns = 2;</code>
   */
  java.util.List<com.google.bigtable.v2.Column> getColumnsList();
  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of increasing "qualifier".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Column columns = 2;</code>
   */
  com.google.bigtable.v2.Column getColumns(int index);
  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of increasing "qualifier".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Column columns = 2;</code>
   */
  int getColumnsCount();
  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of increasing "qualifier".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Column columns = 2;</code>
   */
  java.util.List<? extends com.google.bigtable.v2.ColumnOrBuilder> getColumnsOrBuilderList();
  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of increasing "qualifier".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Column columns = 2;</code>
   */
  com.google.bigtable.v2.ColumnOrBuilder getColumnsOrBuilder(int index);
}
