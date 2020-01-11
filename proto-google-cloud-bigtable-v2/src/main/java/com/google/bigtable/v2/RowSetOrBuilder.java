/*
 * Copyright 2019 Google LLC
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

package com.google.bigtable.v2;

public interface RowSetOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.RowSet)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Single rows included in the set.
   * </pre>
   *
   * <code>repeated bytes row_keys = 1;</code>
   *
   * @return A list containing the rowKeys.
   */
  java.util.List<com.google.protobuf.ByteString> getRowKeysList();
  /**
   *
   *
   * <pre>
   * Single rows included in the set.
   * </pre>
   *
   * <code>repeated bytes row_keys = 1;</code>
   *
   * @return The count of rowKeys.
   */
  int getRowKeysCount();
  /**
   *
   *
   * <pre>
   * Single rows included in the set.
   * </pre>
   *
   * <code>repeated bytes row_keys = 1;</code>
   *
   * @param index The index of the element to return.
   * @return The rowKeys at the given index.
   */
  com.google.protobuf.ByteString getRowKeys(int index);

  /**
   *
   *
   * <pre>
   * Contiguous row ranges included in the set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.RowRange row_ranges = 2;</code>
   */
  java.util.List<com.google.bigtable.v2.RowRange> getRowRangesList();
  /**
   *
   *
   * <pre>
   * Contiguous row ranges included in the set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.RowRange row_ranges = 2;</code>
   */
  com.google.bigtable.v2.RowRange getRowRanges(int index);
  /**
   *
   *
   * <pre>
   * Contiguous row ranges included in the set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.RowRange row_ranges = 2;</code>
   */
  int getRowRangesCount();
  /**
   *
   *
   * <pre>
   * Contiguous row ranges included in the set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.RowRange row_ranges = 2;</code>
   */
  java.util.List<? extends com.google.bigtable.v2.RowRangeOrBuilder> getRowRangesOrBuilderList();
  /**
   *
   *
   * <pre>
   * Contiguous row ranges included in the set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.RowRange row_ranges = 2;</code>
   */
  com.google.bigtable.v2.RowRangeOrBuilder getRowRangesOrBuilder(int index);
}
