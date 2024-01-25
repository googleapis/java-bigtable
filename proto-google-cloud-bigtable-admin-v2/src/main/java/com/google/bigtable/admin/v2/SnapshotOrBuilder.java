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
// source: google/bigtable/admin/v2/table.proto

package com.google.bigtable.admin.v2;

public interface SnapshotOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.Snapshot)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The unique name of the snapshot.
   * Values are of the form
   * `projects/{project}/instances/{instance}/clusters/{cluster}/snapshots/{snapshot}`.
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
   * The unique name of the snapshot.
   * Values are of the form
   * `projects/{project}/instances/{instance}/clusters/{cluster}/snapshots/{snapshot}`.
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
   * Output only. The source table at the time the snapshot was taken.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.Table source_table = 2 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return Whether the sourceTable field is set.
   */
  boolean hasSourceTable();
  /**
   *
   *
   * <pre>
   * Output only. The source table at the time the snapshot was taken.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.Table source_table = 2 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The sourceTable.
   */
  com.google.bigtable.admin.v2.Table getSourceTable();
  /**
   *
   *
   * <pre>
   * Output only. The source table at the time the snapshot was taken.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.Table source_table = 2 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   */
  com.google.bigtable.admin.v2.TableOrBuilder getSourceTableOrBuilder();

  /**
   *
   *
   * <pre>
   * Output only. The size of the data in the source table at the time the
   * snapshot was taken. In some cases, this value may be computed
   * asynchronously via a background process and a placeholder of 0 will be used
   * in the meantime.
   * </pre>
   *
   * <code>int64 data_size_bytes = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
   *
   * @return The dataSizeBytes.
   */
  long getDataSizeBytes();

  /**
   *
   *
   * <pre>
   * Output only. The time when the snapshot is created.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp create_time = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return Whether the createTime field is set.
   */
  boolean hasCreateTime();
  /**
   *
   *
   * <pre>
   * Output only. The time when the snapshot is created.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp create_time = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The createTime.
   */
  com.google.protobuf.Timestamp getCreateTime();
  /**
   *
   *
   * <pre>
   * Output only. The time when the snapshot is created.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp create_time = 4 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   */
  com.google.protobuf.TimestampOrBuilder getCreateTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * The time when the snapshot will be deleted. The maximum amount of time a
   * snapshot can stay active is 365 days. If 'ttl' is not specified,
   * the default maximum of 365 days will be used.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp delete_time = 5;</code>
   *
   * @return Whether the deleteTime field is set.
   */
  boolean hasDeleteTime();
  /**
   *
   *
   * <pre>
   * The time when the snapshot will be deleted. The maximum amount of time a
   * snapshot can stay active is 365 days. If 'ttl' is not specified,
   * the default maximum of 365 days will be used.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp delete_time = 5;</code>
   *
   * @return The deleteTime.
   */
  com.google.protobuf.Timestamp getDeleteTime();
  /**
   *
   *
   * <pre>
   * The time when the snapshot will be deleted. The maximum amount of time a
   * snapshot can stay active is 365 days. If 'ttl' is not specified,
   * the default maximum of 365 days will be used.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp delete_time = 5;</code>
   */
  com.google.protobuf.TimestampOrBuilder getDeleteTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * Output only. The current state of the snapshot.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.Snapshot.State state = 6 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The enum numeric value on the wire for state.
   */
  int getStateValue();
  /**
   *
   *
   * <pre>
   * Output only. The current state of the snapshot.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.Snapshot.State state = 6 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The state.
   */
  com.google.bigtable.admin.v2.Snapshot.State getState();

  /**
   *
   *
   * <pre>
   * Description of the snapshot.
   * </pre>
   *
   * <code>string description = 7;</code>
   *
   * @return The description.
   */
  java.lang.String getDescription();
  /**
   *
   *
   * <pre>
   * Description of the snapshot.
   * </pre>
   *
   * <code>string description = 7;</code>
   *
   * @return The bytes for description.
   */
  com.google.protobuf.ByteString getDescriptionBytes();
}
