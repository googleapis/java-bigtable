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

// Protobuf Java Version: 3.25.4
package com.google.bigtable.admin.v2;

public interface ListInstancesResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.ListInstancesResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The list of requested instances.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Instance instances = 1;</code>
   */
  java.util.List<com.google.bigtable.admin.v2.Instance> getInstancesList();
  /**
   *
   *
   * <pre>
   * The list of requested instances.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Instance instances = 1;</code>
   */
  com.google.bigtable.admin.v2.Instance getInstances(int index);
  /**
   *
   *
   * <pre>
   * The list of requested instances.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Instance instances = 1;</code>
   */
  int getInstancesCount();
  /**
   *
   *
   * <pre>
   * The list of requested instances.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Instance instances = 1;</code>
   */
  java.util.List<? extends com.google.bigtable.admin.v2.InstanceOrBuilder>
      getInstancesOrBuilderList();
  /**
   *
   *
   * <pre>
   * The list of requested instances.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Instance instances = 1;</code>
   */
  com.google.bigtable.admin.v2.InstanceOrBuilder getInstancesOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * Locations from which Instance information could not be retrieved,
   * due to an outage or some other transient condition.
   * Instances whose Clusters are all in one of the failed locations
   * may be missing from `instances`, and Instances with at least one
   * Cluster in a failed location may only have partial information returned.
   * Values are of the form `projects/&lt;project&gt;/locations/&lt;zone_id&gt;`
   * </pre>
   *
   * <code>repeated string failed_locations = 2;</code>
   *
   * @return A list containing the failedLocations.
   */
  java.util.List<java.lang.String> getFailedLocationsList();
  /**
   *
   *
   * <pre>
   * Locations from which Instance information could not be retrieved,
   * due to an outage or some other transient condition.
   * Instances whose Clusters are all in one of the failed locations
   * may be missing from `instances`, and Instances with at least one
   * Cluster in a failed location may only have partial information returned.
   * Values are of the form `projects/&lt;project&gt;/locations/&lt;zone_id&gt;`
   * </pre>
   *
   * <code>repeated string failed_locations = 2;</code>
   *
   * @return The count of failedLocations.
   */
  int getFailedLocationsCount();
  /**
   *
   *
   * <pre>
   * Locations from which Instance information could not be retrieved,
   * due to an outage or some other transient condition.
   * Instances whose Clusters are all in one of the failed locations
   * may be missing from `instances`, and Instances with at least one
   * Cluster in a failed location may only have partial information returned.
   * Values are of the form `projects/&lt;project&gt;/locations/&lt;zone_id&gt;`
   * </pre>
   *
   * <code>repeated string failed_locations = 2;</code>
   *
   * @param index The index of the element to return.
   * @return The failedLocations at the given index.
   */
  java.lang.String getFailedLocations(int index);
  /**
   *
   *
   * <pre>
   * Locations from which Instance information could not be retrieved,
   * due to an outage or some other transient condition.
   * Instances whose Clusters are all in one of the failed locations
   * may be missing from `instances`, and Instances with at least one
   * Cluster in a failed location may only have partial information returned.
   * Values are of the form `projects/&lt;project&gt;/locations/&lt;zone_id&gt;`
   * </pre>
   *
   * <code>repeated string failed_locations = 2;</code>
   *
   * @param index The index of the value to return.
   * @return The bytes of the failedLocations at the given index.
   */
  com.google.protobuf.ByteString getFailedLocationsBytes(int index);

  /**
   *
   *
   * <pre>
   * DEPRECATED: This field is unused and ignored.
   * </pre>
   *
   * <code>string next_page_token = 3;</code>
   *
   * @return The nextPageToken.
   */
  java.lang.String getNextPageToken();
  /**
   *
   *
   * <pre>
   * DEPRECATED: This field is unused and ignored.
   * </pre>
   *
   * <code>string next_page_token = 3;</code>
   *
   * @return The bytes for nextPageToken.
   */
  com.google.protobuf.ByteString getNextPageTokenBytes();
}
