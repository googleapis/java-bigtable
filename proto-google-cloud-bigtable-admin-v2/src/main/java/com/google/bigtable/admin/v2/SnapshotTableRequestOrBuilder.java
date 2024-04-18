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
// source: google/bigtable/admin/v2/bigtable_table_admin.proto

// Protobuf Java Version: 3.25.2
package com.google.bigtable.admin.v2;

public interface SnapshotTableRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.SnapshotTableRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The unique name of the table to have the snapshot taken.
   * Values are of the form
   * `projects/{project}/instances/{instance}/tables/{table}`.
   * </pre>
   *
   * <code>
   * string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The name.
   */
  java.lang.String getName();
  /**
   *
   *
   * <pre>
   * Required. The unique name of the table to have the snapshot taken.
   * Values are of the form
   * `projects/{project}/instances/{instance}/tables/{table}`.
   * </pre>
   *
   * <code>
   * string name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * Required. The name of the cluster where the snapshot will be created in.
   * Values are of the form
   * `projects/{project}/instances/{instance}/clusters/{cluster}`.
   * </pre>
   *
   * <code>
   * string cluster = 2 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The cluster.
   */
  java.lang.String getCluster();
  /**
   *
   *
   * <pre>
   * Required. The name of the cluster where the snapshot will be created in.
   * Values are of the form
   * `projects/{project}/instances/{instance}/clusters/{cluster}`.
   * </pre>
   *
   * <code>
   * string cluster = 2 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for cluster.
   */
  com.google.protobuf.ByteString getClusterBytes();

  /**
   *
   *
   * <pre>
   * Required. The ID by which the new snapshot should be referred to within the
   * parent cluster, e.g., `mysnapshot` of the form:
   * `[_a-zA-Z0-9][-_.a-zA-Z0-9]*` rather than
   * `projects/{project}/instances/{instance}/clusters/{cluster}/snapshots/mysnapshot`.
   * </pre>
   *
   * <code>string snapshot_id = 3 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The snapshotId.
   */
  java.lang.String getSnapshotId();
  /**
   *
   *
   * <pre>
   * Required. The ID by which the new snapshot should be referred to within the
   * parent cluster, e.g., `mysnapshot` of the form:
   * `[_a-zA-Z0-9][-_.a-zA-Z0-9]*` rather than
   * `projects/{project}/instances/{instance}/clusters/{cluster}/snapshots/mysnapshot`.
   * </pre>
   *
   * <code>string snapshot_id = 3 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for snapshotId.
   */
  com.google.protobuf.ByteString getSnapshotIdBytes();

  /**
   *
   *
   * <pre>
   * The amount of time that the new snapshot can stay active after it is
   * created. Once 'ttl' expires, the snapshot will get deleted. The maximum
   * amount of time a snapshot can stay active is 7 days. If 'ttl' is not
   * specified, the default value of 24 hours will be used.
   * </pre>
   *
   * <code>.google.protobuf.Duration ttl = 4;</code>
   *
   * @return Whether the ttl field is set.
   */
  boolean hasTtl();
  /**
   *
   *
   * <pre>
   * The amount of time that the new snapshot can stay active after it is
   * created. Once 'ttl' expires, the snapshot will get deleted. The maximum
   * amount of time a snapshot can stay active is 7 days. If 'ttl' is not
   * specified, the default value of 24 hours will be used.
   * </pre>
   *
   * <code>.google.protobuf.Duration ttl = 4;</code>
   *
   * @return The ttl.
   */
  com.google.protobuf.Duration getTtl();
  /**
   *
   *
   * <pre>
   * The amount of time that the new snapshot can stay active after it is
   * created. Once 'ttl' expires, the snapshot will get deleted. The maximum
   * amount of time a snapshot can stay active is 7 days. If 'ttl' is not
   * specified, the default value of 24 hours will be used.
   * </pre>
   *
   * <code>.google.protobuf.Duration ttl = 4;</code>
   */
  com.google.protobuf.DurationOrBuilder getTtlOrBuilder();

  /**
   *
   *
   * <pre>
   * Description of the snapshot.
   * </pre>
   *
   * <code>string description = 5;</code>
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
   * <code>string description = 5;</code>
   *
   * @return The bytes for description.
   */
  com.google.protobuf.ByteString getDescriptionBytes();
}
