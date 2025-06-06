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
// source: google/bigtable/admin/v2/instance.proto

// Protobuf Java Version: 3.25.8
package com.google.bigtable.admin.v2;

public interface MaterializedViewOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.MaterializedView)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Identifier. The unique name of the materialized view.
   * Format:
   * `projects/{project}/instances/{instance}/materializedViews/{materialized_view}`
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = IDENTIFIER];</code>
   *
   * @return The name.
   */
  java.lang.String getName();

  /**
   *
   *
   * <pre>
   * Identifier. The unique name of the materialized view.
   * Format:
   * `projects/{project}/instances/{instance}/materializedViews/{materialized_view}`
   * </pre>
   *
   * <code>string name = 1 [(.google.api.field_behavior) = IDENTIFIER];</code>
   *
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * Required. Immutable. The materialized view's select query.
   * </pre>
   *
   * <code>
   * string query = 2 [(.google.api.field_behavior) = REQUIRED, (.google.api.field_behavior) = IMMUTABLE];
   * </code>
   *
   * @return The query.
   */
  java.lang.String getQuery();

  /**
   *
   *
   * <pre>
   * Required. Immutable. The materialized view's select query.
   * </pre>
   *
   * <code>
   * string query = 2 [(.google.api.field_behavior) = REQUIRED, (.google.api.field_behavior) = IMMUTABLE];
   * </code>
   *
   * @return The bytes for query.
   */
  com.google.protobuf.ByteString getQueryBytes();

  /**
   *
   *
   * <pre>
   * Optional. The etag for this materialized view.
   * This may be sent on update requests to ensure that the client has an
   * up-to-date value before proceeding. The server returns an ABORTED error on
   * a mismatched etag.
   * </pre>
   *
   * <code>string etag = 3 [(.google.api.field_behavior) = OPTIONAL];</code>
   *
   * @return The etag.
   */
  java.lang.String getEtag();

  /**
   *
   *
   * <pre>
   * Optional. The etag for this materialized view.
   * This may be sent on update requests to ensure that the client has an
   * up-to-date value before proceeding. The server returns an ABORTED error on
   * a mismatched etag.
   * </pre>
   *
   * <code>string etag = 3 [(.google.api.field_behavior) = OPTIONAL];</code>
   *
   * @return The bytes for etag.
   */
  com.google.protobuf.ByteString getEtagBytes();

  /**
   *
   *
   * <pre>
   * Set to true to make the MaterializedView protected against deletion.
   * </pre>
   *
   * <code>bool deletion_protection = 6;</code>
   *
   * @return The deletionProtection.
   */
  boolean getDeletionProtection();
}
