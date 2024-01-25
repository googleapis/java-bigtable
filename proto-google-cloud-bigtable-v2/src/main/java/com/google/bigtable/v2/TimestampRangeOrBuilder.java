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

package com.google.bigtable.v2;

public interface TimestampRangeOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.TimestampRange)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Inclusive lower bound. If left empty, interpreted as 0.
   * </pre>
   *
   * <code>int64 start_timestamp_micros = 1;</code>
   *
   * @return The startTimestampMicros.
   */
  long getStartTimestampMicros();

  /**
   *
   *
   * <pre>
   * Exclusive upper bound. If left empty, interpreted as infinity.
   * </pre>
   *
   * <code>int64 end_timestamp_micros = 2;</code>
   *
   * @return The endTimestampMicros.
   */
  long getEndTimestampMicros();
}
