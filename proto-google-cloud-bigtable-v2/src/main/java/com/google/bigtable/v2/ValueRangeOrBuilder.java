/*
 * Copyright 2020 Google LLC
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

public interface ValueRangeOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ValueRange)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Used when giving an inclusive lower bound for the range.
   * </pre>
   *
   * <code>bytes start_value_closed = 1;</code>
   *
   * @return The startValueClosed.
   */
  com.google.protobuf.ByteString getStartValueClosed();

  /**
   *
   *
   * <pre>
   * Used when giving an exclusive lower bound for the range.
   * </pre>
   *
   * <code>bytes start_value_open = 2;</code>
   *
   * @return The startValueOpen.
   */
  com.google.protobuf.ByteString getStartValueOpen();

  /**
   *
   *
   * <pre>
   * Used when giving an inclusive upper bound for the range.
   * </pre>
   *
   * <code>bytes end_value_closed = 3;</code>
   *
   * @return The endValueClosed.
   */
  com.google.protobuf.ByteString getEndValueClosed();

  /**
   *
   *
   * <pre>
   * Used when giving an exclusive upper bound for the range.
   * </pre>
   *
   * <code>bytes end_value_open = 4;</code>
   *
   * @return The endValueOpen.
   */
  com.google.protobuf.ByteString getEndValueOpen();

  public com.google.bigtable.v2.ValueRange.StartValueCase getStartValueCase();

  public com.google.bigtable.v2.ValueRange.EndValueCase getEndValueCase();
}
