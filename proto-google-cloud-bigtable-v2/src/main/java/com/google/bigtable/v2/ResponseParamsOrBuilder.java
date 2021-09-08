/*
 * Copyright 2022 Google LLC
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
// source: google/bigtable/v2/response_params.proto

package com.google.bigtable.v2;

public interface ResponseParamsOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ResponseParams)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The cloud bigtable zone associated with the cluster.
   * </pre>
   *
   * <code>optional string zone_id = 1;</code>
   *
   * @return Whether the zoneId field is set.
   */
  boolean hasZoneId();
  /**
   *
   *
   * <pre>
   * The cloud bigtable zone associated with the cluster.
   * </pre>
   *
   * <code>optional string zone_id = 1;</code>
   *
   * @return The zoneId.
   */
  java.lang.String getZoneId();
  /**
   *
   *
   * <pre>
   * The cloud bigtable zone associated with the cluster.
   * </pre>
   *
   * <code>optional string zone_id = 1;</code>
   *
   * @return The bytes for zoneId.
   */
  com.google.protobuf.ByteString getZoneIdBytes();

  /**
   *
   *
   * <pre>
   * Identifier for a cluster that represents set of
   * bigtable resources.
   * </pre>
   *
   * <code>optional string cluster_id = 2;</code>
   *
   * @return Whether the clusterId field is set.
   */
  boolean hasClusterId();
  /**
   *
   *
   * <pre>
   * Identifier for a cluster that represents set of
   * bigtable resources.
   * </pre>
   *
   * <code>optional string cluster_id = 2;</code>
   *
   * @return The clusterId.
   */
  java.lang.String getClusterId();
  /**
   *
   *
   * <pre>
   * Identifier for a cluster that represents set of
   * bigtable resources.
   * </pre>
   *
   * <code>optional string cluster_id = 2;</code>
   *
   * @return The bytes for clusterId.
   */
  com.google.protobuf.ByteString getClusterIdBytes();
}
