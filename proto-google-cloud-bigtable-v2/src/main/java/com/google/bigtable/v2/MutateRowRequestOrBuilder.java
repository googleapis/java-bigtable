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
// source: google/bigtable/v2/bigtable.proto

// Protobuf Java Version: 3.25.8
package com.google.bigtable.v2;

public interface MutateRowRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.MutateRowRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Optional. The unique name of the table to which the mutation should be
   * applied.
   *
   * Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * </pre>
   *
   * <code>
   * string table_name = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The tableName.
   */
  java.lang.String getTableName();

  /**
   *
   *
   * <pre>
   * Optional. The unique name of the table to which the mutation should be
   * applied.
   *
   * Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * </pre>
   *
   * <code>
   * string table_name = 1 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for tableName.
   */
  com.google.protobuf.ByteString getTableNameBytes();

  /**
   *
   *
   * <pre>
   * Optional. The unique name of the AuthorizedView to which the mutation
   * should be applied.
   *
   * Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;/authorizedViews/&lt;authorized_view&gt;`.
   * </pre>
   *
   * <code>
   * string authorized_view_name = 6 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The authorizedViewName.
   */
  java.lang.String getAuthorizedViewName();

  /**
   *
   *
   * <pre>
   * Optional. The unique name of the AuthorizedView to which the mutation
   * should be applied.
   *
   * Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;/authorizedViews/&lt;authorized_view&gt;`.
   * </pre>
   *
   * <code>
   * string authorized_view_name = 6 [(.google.api.field_behavior) = OPTIONAL, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for authorizedViewName.
   */
  com.google.protobuf.ByteString getAuthorizedViewNameBytes();

  /**
   *
   *
   * <pre>
   * This value specifies routing for replication. If not specified, the
   * "default" application profile will be used.
   * </pre>
   *
   * <code>string app_profile_id = 4;</code>
   *
   * @return The appProfileId.
   */
  java.lang.String getAppProfileId();

  /**
   *
   *
   * <pre>
   * This value specifies routing for replication. If not specified, the
   * "default" application profile will be used.
   * </pre>
   *
   * <code>string app_profile_id = 4;</code>
   *
   * @return The bytes for appProfileId.
   */
  com.google.protobuf.ByteString getAppProfileIdBytes();

  /**
   *
   *
   * <pre>
   * Required. The key of the row to which the mutation should be applied.
   * </pre>
   *
   * <code>bytes row_key = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The rowKey.
   */
  com.google.protobuf.ByteString getRowKey();

  /**
   *
   *
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  java.util.List<com.google.bigtable.v2.Mutation> getMutationsList();

  /**
   *
   *
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.v2.Mutation getMutations(int index);

  /**
   *
   *
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  int getMutationsCount();

  /**
   *
   *
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  java.util.List<? extends com.google.bigtable.v2.MutationOrBuilder> getMutationsOrBuilderList();

  /**
   *
   *
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.v2.MutationOrBuilder getMutationsOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * If set consistently across retries, prevents this mutation from being
   * double applied to aggregate column families within a 15m window.
   * </pre>
   *
   * <code>.google.bigtable.v2.Idempotency idempotency = 8;</code>
   *
   * @return Whether the idempotency field is set.
   */
  boolean hasIdempotency();

  /**
   *
   *
   * <pre>
   * If set consistently across retries, prevents this mutation from being
   * double applied to aggregate column families within a 15m window.
   * </pre>
   *
   * <code>.google.bigtable.v2.Idempotency idempotency = 8;</code>
   *
   * @return The idempotency.
   */
  com.google.bigtable.v2.Idempotency getIdempotency();

  /**
   *
   *
   * <pre>
   * If set consistently across retries, prevents this mutation from being
   * double applied to aggregate column families within a 15m window.
   * </pre>
   *
   * <code>.google.bigtable.v2.Idempotency idempotency = 8;</code>
   */
  com.google.bigtable.v2.IdempotencyOrBuilder getIdempotencyOrBuilder();
}
