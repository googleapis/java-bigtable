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
// source: google/bigtable/v2/request_stats.proto

// Protobuf Java Version: 3.25.4
package com.google.bigtable.v2;

public interface ReadIterationStatsOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ReadIterationStats)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The rows seen (scanned) as part of the request. This includes the count of
   * rows returned, as captured below.
   * </pre>
   *
   * <code>int64 rows_seen_count = 1;</code>
   *
   * @return The rowsSeenCount.
   */
  long getRowsSeenCount();

  /**
   *
   *
   * <pre>
   * The rows returned as part of the request.
   * </pre>
   *
   * <code>int64 rows_returned_count = 2;</code>
   *
   * @return The rowsReturnedCount.
   */
  long getRowsReturnedCount();

  /**
   *
   *
   * <pre>
   * The cells seen (scanned) as part of the request. This includes the count of
   * cells returned, as captured below.
   * </pre>
   *
   * <code>int64 cells_seen_count = 3;</code>
   *
   * @return The cellsSeenCount.
   */
  long getCellsSeenCount();

  /**
   *
   *
   * <pre>
   * The cells returned as part of the request.
   * </pre>
   *
   * <code>int64 cells_returned_count = 4;</code>
   *
   * @return The cellsReturnedCount.
   */
  long getCellsReturnedCount();
}
