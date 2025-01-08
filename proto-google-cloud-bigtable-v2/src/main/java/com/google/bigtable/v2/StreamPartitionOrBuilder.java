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

public interface StreamPartitionOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.StreamPartition)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The row range covered by this partition and is specified by
   * [`start_key_closed`, `end_key_open`).
   * </pre>
   *
   * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
   *
   * @return Whether the rowRange field is set.
   */
  boolean hasRowRange();
  /**
   *
   *
   * <pre>
   * The row range covered by this partition and is specified by
   * [`start_key_closed`, `end_key_open`).
   * </pre>
   *
   * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
   *
   * @return The rowRange.
   */
  com.google.bigtable.v2.RowRange getRowRange();
  /**
   *
   *
   * <pre>
   * The row range covered by this partition and is specified by
   * [`start_key_closed`, `end_key_open`).
   * </pre>
   *
   * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
   */
  com.google.bigtable.v2.RowRangeOrBuilder getRowRangeOrBuilder();
}
