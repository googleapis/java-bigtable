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

public interface CreateInstanceRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.CreateInstanceRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The unique name of the project in which to create the new
   * instance. Values are of the form `projects/{project}`.
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
   * Required. The unique name of the project in which to create the new
   * instance. Values are of the form `projects/{project}`.
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
   * Required. The ID to be used when referring to the new instance within its
   * project, e.g., just `myinstance` rather than
   * `projects/myproject/instances/myinstance`.
   * </pre>
   *
   * <code>string instance_id = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The instanceId.
   */
  java.lang.String getInstanceId();
  /**
   *
   *
   * <pre>
   * Required. The ID to be used when referring to the new instance within its
   * project, e.g., just `myinstance` rather than
   * `projects/myproject/instances/myinstance`.
   * </pre>
   *
   * <code>string instance_id = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for instanceId.
   */
  com.google.protobuf.ByteString getInstanceIdBytes();

  /**
   *
   *
   * <pre>
   * Required. The instance to create.
   * Fields marked `OutputOnly` must be left blank.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.Instance instance = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return Whether the instance field is set.
   */
  boolean hasInstance();
  /**
   *
   *
   * <pre>
   * Required. The instance to create.
   * Fields marked `OutputOnly` must be left blank.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.Instance instance = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return The instance.
   */
  com.google.bigtable.admin.v2.Instance getInstance();
  /**
   *
   *
   * <pre>
   * Required. The instance to create.
   * Fields marked `OutputOnly` must be left blank.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.Instance instance = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.admin.v2.InstanceOrBuilder getInstanceOrBuilder();

  /**
   *
   *
   * <pre>
   * Required. The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most four clusters can be specified.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  int getClustersCount();
  /**
   *
   *
   * <pre>
   * Required. The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most four clusters can be specified.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  boolean containsClusters(java.lang.String key);
  /** Use {@link #getClustersMap()} instead. */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.google.bigtable.admin.v2.Cluster> getClusters();
  /**
   *
   *
   * <pre>
   * Required. The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most four clusters can be specified.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  java.util.Map<java.lang.String, com.google.bigtable.admin.v2.Cluster> getClustersMap();
  /**
   *
   *
   * <pre>
   * Required. The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most four clusters can be specified.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  /* nullable */
  com.google.bigtable.admin.v2.Cluster getClustersOrDefault(
      java.lang.String key,
      /* nullable */
      com.google.bigtable.admin.v2.Cluster defaultValue);
  /**
   *
   *
   * <pre>
   * Required. The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most four clusters can be specified.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.admin.v2.Cluster getClustersOrThrow(java.lang.String key);
}
