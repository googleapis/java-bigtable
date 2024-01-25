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
// source: google/bigtable/v2/bigtable.proto

package com.google.bigtable.v2;

public interface ReadChangeStreamResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ReadChangeStreamResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * A mutation to the partition.
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadChangeStreamResponse.DataChange data_change = 1;</code>
   *
   * @return Whether the dataChange field is set.
   */
  boolean hasDataChange();
  /**
   *
   *
   * <pre>
   * A mutation to the partition.
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadChangeStreamResponse.DataChange data_change = 1;</code>
   *
   * @return The dataChange.
   */
  com.google.bigtable.v2.ReadChangeStreamResponse.DataChange getDataChange();
  /**
   *
   *
   * <pre>
   * A mutation to the partition.
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadChangeStreamResponse.DataChange data_change = 1;</code>
   */
  com.google.bigtable.v2.ReadChangeStreamResponse.DataChangeOrBuilder getDataChangeOrBuilder();

  /**
   *
   *
   * <pre>
   * A periodic heartbeat message.
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadChangeStreamResponse.Heartbeat heartbeat = 2;</code>
   *
   * @return Whether the heartbeat field is set.
   */
  boolean hasHeartbeat();
  /**
   *
   *
   * <pre>
   * A periodic heartbeat message.
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadChangeStreamResponse.Heartbeat heartbeat = 2;</code>
   *
   * @return The heartbeat.
   */
  com.google.bigtable.v2.ReadChangeStreamResponse.Heartbeat getHeartbeat();
  /**
   *
   *
   * <pre>
   * A periodic heartbeat message.
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadChangeStreamResponse.Heartbeat heartbeat = 2;</code>
   */
  com.google.bigtable.v2.ReadChangeStreamResponse.HeartbeatOrBuilder getHeartbeatOrBuilder();

  /**
   *
   *
   * <pre>
   * An indication that the stream should be closed.
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadChangeStreamResponse.CloseStream close_stream = 3;</code>
   *
   * @return Whether the closeStream field is set.
   */
  boolean hasCloseStream();
  /**
   *
   *
   * <pre>
   * An indication that the stream should be closed.
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadChangeStreamResponse.CloseStream close_stream = 3;</code>
   *
   * @return The closeStream.
   */
  com.google.bigtable.v2.ReadChangeStreamResponse.CloseStream getCloseStream();
  /**
   *
   *
   * <pre>
   * An indication that the stream should be closed.
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadChangeStreamResponse.CloseStream close_stream = 3;</code>
   */
  com.google.bigtable.v2.ReadChangeStreamResponse.CloseStreamOrBuilder getCloseStreamOrBuilder();

  com.google.bigtable.v2.ReadChangeStreamResponse.StreamRecordCase getStreamRecordCase();
}
