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
// source: google/bigtable/admin/v2/bigtable_instance_admin.proto

package com.google.bigtable.admin.v2;

public interface CreateClusterRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.CreateClusterRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The unique name of the instance in which to create the new
   * cluster. Values are of the form `projects/{project}/instances/{instance}`.
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
   * Required. The unique name of the instance in which to create the new
   * cluster. Values are of the form `projects/{project}/instances/{instance}`.
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
   * Required. The ID to be used when referring to the new cluster within its
   * instance, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * </pre>
   *
   * <code>string cluster_id = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The clusterId.
   */
  java.lang.String getClusterId();
  /**
   *
   *
   * <pre>
   * Required. The ID to be used when referring to the new cluster within its
   * instance, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * </pre>
   *
   * <code>string cluster_id = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for clusterId.
   */
  com.google.protobuf.ByteString getClusterIdBytes();

  /**
   *
   *
   * <pre>
   * Required. The cluster to be created.
   * Fields marked `OutputOnly` must be left blank.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Cluster cluster = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return Whether the cluster field is set.
   */
  boolean hasCluster();
  /**
   *
   *
   * <pre>
   * Required. The cluster to be created.
   * Fields marked `OutputOnly` must be left blank.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Cluster cluster = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return The cluster.
   */
  com.google.bigtable.admin.v2.Cluster getCluster();
  /**
   *
   *
   * <pre>
   * Required. The cluster to be created.
   * Fields marked `OutputOnly` must be left blank.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Cluster cluster = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.admin.v2.ClusterOrBuilder getClusterOrBuilder();
}
