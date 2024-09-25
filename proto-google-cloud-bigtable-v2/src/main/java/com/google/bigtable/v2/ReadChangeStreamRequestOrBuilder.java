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

// Protobuf Java Version: 3.25.5
package com.google.bigtable.v2;

public interface ReadChangeStreamRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ReadChangeStreamRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The unique name of the table from which to read a change stream.
   * Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * Change streaming must be enabled on the table.
   * </pre>
   *
   * <code>
   * string table_name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The tableName.
   */
  java.lang.String getTableName();
  /**
   *
   *
   * <pre>
   * Required. The unique name of the table from which to read a change stream.
   * Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * Change streaming must be enabled on the table.
   * </pre>
   *
   * <code>
   * string table_name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for tableName.
   */
  com.google.protobuf.ByteString getTableNameBytes();

  /**
   *
   *
   * <pre>
   * This value specifies routing for replication. If not specified, the
   * "default" application profile will be used.
   * Single cluster routing must be configured on the profile.
   * </pre>
   *
   * <code>string app_profile_id = 2;</code>
   *
   * @return The appProfileId.
   */
  java.lang.String getAppProfileId();
  /**
   *
   *
   * <pre>
   * This value specifies routing for replication. If not specified, the
   * "default" application profile will be used.
   * Single cluster routing must be configured on the profile.
   * </pre>
   *
   * <code>string app_profile_id = 2;</code>
   *
   * @return The bytes for appProfileId.
   */
  com.google.protobuf.ByteString getAppProfileIdBytes();

  /**
   *
   *
   * <pre>
   * The partition to read changes from.
   * </pre>
   *
   * <code>.google.bigtable.v2.StreamPartition partition = 3;</code>
   *
   * @return Whether the partition field is set.
   */
  boolean hasPartition();
  /**
   *
   *
   * <pre>
   * The partition to read changes from.
   * </pre>
   *
   * <code>.google.bigtable.v2.StreamPartition partition = 3;</code>
   *
   * @return The partition.
   */
  com.google.bigtable.v2.StreamPartition getPartition();
  /**
   *
   *
   * <pre>
   * The partition to read changes from.
   * </pre>
   *
   * <code>.google.bigtable.v2.StreamPartition partition = 3;</code>
   */
  com.google.bigtable.v2.StreamPartitionOrBuilder getPartitionOrBuilder();

  /**
   *
   *
   * <pre>
   * Start reading the stream at the specified timestamp. This timestamp must
   * be within the change stream retention period, less than or equal to the
   * current time, and after change stream creation, whichever is greater.
   * This value is inclusive and will be truncated to microsecond granularity.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp start_time = 4;</code>
   *
   * @return Whether the startTime field is set.
   */
  boolean hasStartTime();
  /**
   *
   *
   * <pre>
   * Start reading the stream at the specified timestamp. This timestamp must
   * be within the change stream retention period, less than or equal to the
   * current time, and after change stream creation, whichever is greater.
   * This value is inclusive and will be truncated to microsecond granularity.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp start_time = 4;</code>
   *
   * @return The startTime.
   */
  com.google.protobuf.Timestamp getStartTime();
  /**
   *
   *
   * <pre>
   * Start reading the stream at the specified timestamp. This timestamp must
   * be within the change stream retention period, less than or equal to the
   * current time, and after change stream creation, whichever is greater.
   * This value is inclusive and will be truncated to microsecond granularity.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp start_time = 4;</code>
   */
  com.google.protobuf.TimestampOrBuilder getStartTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * Tokens that describe how to resume reading a stream where reading
   * previously left off. If specified, changes will be read starting at the
   * the position. Tokens are delivered on the stream as part of `Heartbeat`
   * and `CloseStream` messages.
   *
   * If a single token is provided, the token’s partition must exactly match
   * the request’s partition. If multiple tokens are provided, as in the case
   * of a partition merge, the union of the token partitions must exactly
   * cover the request’s partition. Otherwise, INVALID_ARGUMENT will be
   * returned.
   * </pre>
   *
   * <code>.google.bigtable.v2.StreamContinuationTokens continuation_tokens = 6;</code>
   *
   * @return Whether the continuationTokens field is set.
   */
  boolean hasContinuationTokens();
  /**
   *
   *
   * <pre>
   * Tokens that describe how to resume reading a stream where reading
   * previously left off. If specified, changes will be read starting at the
   * the position. Tokens are delivered on the stream as part of `Heartbeat`
   * and `CloseStream` messages.
   *
   * If a single token is provided, the token’s partition must exactly match
   * the request’s partition. If multiple tokens are provided, as in the case
   * of a partition merge, the union of the token partitions must exactly
   * cover the request’s partition. Otherwise, INVALID_ARGUMENT will be
   * returned.
   * </pre>
   *
   * <code>.google.bigtable.v2.StreamContinuationTokens continuation_tokens = 6;</code>
   *
   * @return The continuationTokens.
   */
  com.google.bigtable.v2.StreamContinuationTokens getContinuationTokens();
  /**
   *
   *
   * <pre>
   * Tokens that describe how to resume reading a stream where reading
   * previously left off. If specified, changes will be read starting at the
   * the position. Tokens are delivered on the stream as part of `Heartbeat`
   * and `CloseStream` messages.
   *
   * If a single token is provided, the token’s partition must exactly match
   * the request’s partition. If multiple tokens are provided, as in the case
   * of a partition merge, the union of the token partitions must exactly
   * cover the request’s partition. Otherwise, INVALID_ARGUMENT will be
   * returned.
   * </pre>
   *
   * <code>.google.bigtable.v2.StreamContinuationTokens continuation_tokens = 6;</code>
   */
  com.google.bigtable.v2.StreamContinuationTokensOrBuilder getContinuationTokensOrBuilder();

  /**
   *
   *
   * <pre>
   * If specified, OK will be returned when the stream advances beyond
   * this time. Otherwise, changes will be continuously delivered on the stream.
   * This value is inclusive and will be truncated to microsecond granularity.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp end_time = 5;</code>
   *
   * @return Whether the endTime field is set.
   */
  boolean hasEndTime();
  /**
   *
   *
   * <pre>
   * If specified, OK will be returned when the stream advances beyond
   * this time. Otherwise, changes will be continuously delivered on the stream.
   * This value is inclusive and will be truncated to microsecond granularity.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp end_time = 5;</code>
   *
   * @return The endTime.
   */
  com.google.protobuf.Timestamp getEndTime();
  /**
   *
   *
   * <pre>
   * If specified, OK will be returned when the stream advances beyond
   * this time. Otherwise, changes will be continuously delivered on the stream.
   * This value is inclusive and will be truncated to microsecond granularity.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp end_time = 5;</code>
   */
  com.google.protobuf.TimestampOrBuilder getEndTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * If specified, the duration between `Heartbeat` messages on the stream.
   * Otherwise, defaults to 5 seconds.
   * </pre>
   *
   * <code>.google.protobuf.Duration heartbeat_duration = 7;</code>
   *
   * @return Whether the heartbeatDuration field is set.
   */
  boolean hasHeartbeatDuration();
  /**
   *
   *
   * <pre>
   * If specified, the duration between `Heartbeat` messages on the stream.
   * Otherwise, defaults to 5 seconds.
   * </pre>
   *
   * <code>.google.protobuf.Duration heartbeat_duration = 7;</code>
   *
   * @return The heartbeatDuration.
   */
  com.google.protobuf.Duration getHeartbeatDuration();
  /**
   *
   *
   * <pre>
   * If specified, the duration between `Heartbeat` messages on the stream.
   * Otherwise, defaults to 5 seconds.
   * </pre>
   *
   * <code>.google.protobuf.Duration heartbeat_duration = 7;</code>
   */
  com.google.protobuf.DurationOrBuilder getHeartbeatDurationOrBuilder();

  com.google.bigtable.v2.ReadChangeStreamRequest.StartFromCase getStartFromCase();
}
