/*
 * Copyright 2019 Google LLC
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
   * The unique name of the project in which to create the new instance.
   * Values are of the form `projects/&lt;project&gt;`.
   * </pre>
   *
   * <code>string parent = 1;</code>
   */
  java.lang.String getParent();
  /**
   *
   *
   * <pre>
   * The unique name of the project in which to create the new instance.
   * Values are of the form `projects/&lt;project&gt;`.
   * </pre>
   *
   * <code>string parent = 1;</code>
   */
  com.google.protobuf.ByteString getParentBytes();

  /**
   *
   *
   * <pre>
   * The ID to be used when referring to the new instance within its project,
   * e.g., just `myinstance` rather than
   * `projects/myproject/instances/myinstance`.
   * </pre>
   *
   * <code>string instance_id = 2;</code>
   */
  java.lang.String getInstanceId();
  /**
   *
   *
   * <pre>
   * The ID to be used when referring to the new instance within its project,
   * e.g., just `myinstance` rather than
   * `projects/myproject/instances/myinstance`.
   * </pre>
   *
   * <code>string instance_id = 2;</code>
   */
  com.google.protobuf.ByteString getInstanceIdBytes();

  /**
   *
   *
   * <pre>
   * The instance to create.
   * Fields marked `OutputOnly` must be left blank.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Instance instance = 3;</code>
   */
  boolean hasInstance();
  /**
   *
   *
   * <pre>
   * The instance to create.
   * Fields marked `OutputOnly` must be left blank.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Instance instance = 3;</code>
   */
  com.google.bigtable.admin.v2.Instance getInstance();
  /**
   *
   *
   * <pre>
   * The instance to create.
   * Fields marked `OutputOnly` must be left blank.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Instance instance = 3;</code>
   */
  com.google.bigtable.admin.v2.InstanceOrBuilder getInstanceOrBuilder();

  /**
   *
   *
   * <pre>
   * The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most two clusters can be specified.
   * </pre>
   *
   * <code>map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4;</code>
   */
  int getClustersCount();
  /**
   *
   *
   * <pre>
   * The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most two clusters can be specified.
   * </pre>
   *
   * <code>map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4;</code>
   */
  boolean containsClusters(java.lang.String key);
  /** Use {@link #getClustersMap()} instead. */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.google.bigtable.admin.v2.Cluster> getClusters();
  /**
   *
   *
   * <pre>
   * The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most two clusters can be specified.
   * </pre>
   *
   * <code>map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4;</code>
   */
  java.util.Map<java.lang.String, com.google.bigtable.admin.v2.Cluster> getClustersMap();
  /**
   *
   *
   * <pre>
   * The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most two clusters can be specified.
   * </pre>
   *
   * <code>map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4;</code>
   */
  com.google.bigtable.admin.v2.Cluster getClustersOrDefault(
      java.lang.String key, com.google.bigtable.admin.v2.Cluster defaultValue);
  /**
   *
   *
   * <pre>
   * The clusters to be created within the instance, mapped by desired
   * cluster ID, e.g., just `mycluster` rather than
   * `projects/myproject/instances/myinstance/clusters/mycluster`.
   * Fields marked `OutputOnly` must be left blank.
   * Currently, at most two clusters can be specified.
   * </pre>
   *
   * <code>map&lt;string, .google.bigtable.admin.v2.Cluster&gt; clusters = 4;</code>
   */
  com.google.bigtable.admin.v2.Cluster getClustersOrThrow(java.lang.String key);
}
