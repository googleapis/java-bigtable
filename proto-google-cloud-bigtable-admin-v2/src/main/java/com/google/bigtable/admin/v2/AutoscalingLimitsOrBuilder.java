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
// source: google/bigtable/admin/v2/instance.proto

package com.google.bigtable.admin.v2;

public interface AutoscalingLimitsOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.AutoscalingLimits)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. Minimum number of nodes to scale down to.
   * </pre>
   *
   * <code>int32 min_serve_nodes = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The minServeNodes.
   */
  int getMinServeNodes();

  /**
   *
   *
   * <pre>
   * Required. Maximum number of nodes to scale up to.
   * </pre>
   *
   * <code>int32 max_serve_nodes = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The maxServeNodes.
   */
  int getMaxServeNodes();
}
