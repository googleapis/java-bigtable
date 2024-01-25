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
// source: google/bigtable/admin/v2/bigtable_instance_admin.proto

package com.google.bigtable.admin.v2;

public interface PartialUpdateClusterRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.PartialUpdateClusterRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The Cluster which contains the partial updates to be applied,
   * subject to the update_mask.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Cluster cluster = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return Whether the cluster field is set.
   */
  boolean hasCluster();
  /**
   *
   *
   * <pre>
   * Required. The Cluster which contains the partial updates to be applied,
   * subject to the update_mask.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Cluster cluster = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return The cluster.
   */
  com.google.bigtable.admin.v2.Cluster getCluster();
  /**
   *
   *
   * <pre>
   * Required. The Cluster which contains the partial updates to be applied,
   * subject to the update_mask.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Cluster cluster = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.admin.v2.ClusterOrBuilder getClusterOrBuilder();

  /**
   *
   *
   * <pre>
   * Required. The subset of Cluster fields which should be replaced.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return Whether the updateMask field is set.
   */
  boolean hasUpdateMask();
  /**
   *
   *
   * <pre>
   * Required. The subset of Cluster fields which should be replaced.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return The updateMask.
   */
  com.google.protobuf.FieldMask getUpdateMask();
  /**
   *
   *
   * <pre>
   * Required. The subset of Cluster fields which should be replaced.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.protobuf.FieldMaskOrBuilder getUpdateMaskOrBuilder();
}
