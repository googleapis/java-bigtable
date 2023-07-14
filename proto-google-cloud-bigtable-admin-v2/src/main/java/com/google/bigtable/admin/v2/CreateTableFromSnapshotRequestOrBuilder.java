/*
 * Copyright 2023 Google LLC
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

package com.google.bigtable.admin.v2;

public interface CreateTableFromSnapshotRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.CreateTableFromSnapshotRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The unique name of the instance in which to create the table.
   * Values are of the form `projects/{project}/instances/{instance}`.
   * </pre>
   *
   * <code>
   * string parent = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The parent.
   */
  java.lang.String getParent();
  /**
   *
   *
   * <pre>
   * Required. The unique name of the instance in which to create the table.
   * Values are of the form `projects/{project}/instances/{instance}`.
   * </pre>
   *
   * <code>
   * string parent = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for parent.
   */
  com.google.protobuf.ByteString getParentBytes();

  /**
   *
   *
   * <pre>
   * Required. The name by which the new table should be referred to within the parent
   * instance, e.g., `foobar` rather than `{parent}/tables/foobar`.
   * </pre>
   *
   * <code>string table_id = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The tableId.
   */
  java.lang.String getTableId();
  /**
   *
   *
   * <pre>
   * Required. The name by which the new table should be referred to within the parent
   * instance, e.g., `foobar` rather than `{parent}/tables/foobar`.
   * </pre>
   *
   * <code>string table_id = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for tableId.
   */
  com.google.protobuf.ByteString getTableIdBytes();

  /**
   *
   *
   * <pre>
   * Required. The unique name of the snapshot from which to restore the table. The
   * snapshot and the table must be in the same instance.
   * Values are of the form
   * `projects/{project}/instances/{instance}/clusters/{cluster}/snapshots/{snapshot}`.
   * </pre>
   *
   * <code>
   * string source_snapshot = 3 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The sourceSnapshot.
   */
  java.lang.String getSourceSnapshot();
  /**
   *
   *
   * <pre>
   * Required. The unique name of the snapshot from which to restore the table. The
   * snapshot and the table must be in the same instance.
   * Values are of the form
   * `projects/{project}/instances/{instance}/clusters/{cluster}/snapshots/{snapshot}`.
   * </pre>
   *
   * <code>
   * string source_snapshot = 3 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for sourceSnapshot.
   */
  com.google.protobuf.ByteString getSourceSnapshotBytes();
}
