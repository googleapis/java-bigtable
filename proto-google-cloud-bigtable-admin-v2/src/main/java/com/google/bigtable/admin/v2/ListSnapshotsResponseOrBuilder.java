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
// NO CHECKED-IN PROTOBUF GENCODE
// source: google/bigtable/admin/v2/bigtable_table_admin.proto
// Protobuf Java Version: 4.29.0

package com.google.bigtable.admin.v2;

public interface ListSnapshotsResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.ListSnapshotsResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The snapshots present in the requested cluster.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Snapshot snapshots = 1;</code>
   */
  java.util.List<com.google.bigtable.admin.v2.Snapshot> getSnapshotsList();
  /**
   *
   *
   * <pre>
   * The snapshots present in the requested cluster.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Snapshot snapshots = 1;</code>
   */
  com.google.bigtable.admin.v2.Snapshot getSnapshots(int index);
  /**
   *
   *
   * <pre>
   * The snapshots present in the requested cluster.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Snapshot snapshots = 1;</code>
   */
  int getSnapshotsCount();
  /**
   *
   *
   * <pre>
   * The snapshots present in the requested cluster.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Snapshot snapshots = 1;</code>
   */
  java.util.List<? extends com.google.bigtable.admin.v2.SnapshotOrBuilder>
      getSnapshotsOrBuilderList();
  /**
   *
   *
   * <pre>
   * The snapshots present in the requested cluster.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Snapshot snapshots = 1;</code>
   */
  com.google.bigtable.admin.v2.SnapshotOrBuilder getSnapshotsOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * Set if not all snapshots could be returned in a single response.
   * Pass this value to `page_token` in another request to get the next
   * page of results.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   *
   * @return The nextPageToken.
   */
  java.lang.String getNextPageToken();
  /**
   *
   *
   * <pre>
   * Set if not all snapshots could be returned in a single response.
   * Pass this value to `page_token` in another request to get the next
   * page of results.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   *
   * @return The bytes for nextPageToken.
   */
  com.google.protobuf.ByteString getNextPageTokenBytes();
}
