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
// source: google/bigtable/v2/bigtable.proto

package com.google.bigtable.v2;

public interface ReadModifyWriteRowRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ReadModifyWriteRowRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The unique name of the table to which the read/modify/write rules should be
   * applied.
   * Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * </pre>
   *
   * <code>
   * string table_name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The tableName.
   */
  java.lang.String getTableName();
  /**
   *
   *
   * <pre>
   * Required. The unique name of the table to which the read/modify/write rules should be
   * applied.
   * Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * </pre>
   *
   * <code>
   * string table_name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for tableName.
   */
  com.google.protobuf.ByteString getTableNameBytes();

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
   * Required. The key of the row to which the read/modify/write rules should be applied.
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
   * Required. Rules specifying how the specified row's contents are to be transformed
   * into writes. Entries are applied in order, meaning that earlier rules will
   * affect the results of later ones.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.ReadModifyWriteRule rules = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  java.util.List<com.google.bigtable.v2.ReadModifyWriteRule> getRulesList();
  /**
   *
   *
   * <pre>
   * Required. Rules specifying how the specified row's contents are to be transformed
   * into writes. Entries are applied in order, meaning that earlier rules will
   * affect the results of later ones.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.ReadModifyWriteRule rules = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.v2.ReadModifyWriteRule getRules(int index);
  /**
   *
   *
   * <pre>
   * Required. Rules specifying how the specified row's contents are to be transformed
   * into writes. Entries are applied in order, meaning that earlier rules will
   * affect the results of later ones.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.ReadModifyWriteRule rules = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  int getRulesCount();
  /**
   *
   *
   * <pre>
   * Required. Rules specifying how the specified row's contents are to be transformed
   * into writes. Entries are applied in order, meaning that earlier rules will
   * affect the results of later ones.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.ReadModifyWriteRule rules = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  java.util.List<? extends com.google.bigtable.v2.ReadModifyWriteRuleOrBuilder>
      getRulesOrBuilderList();
  /**
   *
   *
   * <pre>
   * Required. Rules specifying how the specified row's contents are to be transformed
   * into writes. Entries are applied in order, meaning that earlier rules will
   * affect the results of later ones.
   * </pre>
   *
   * <code>
   * repeated .google.bigtable.v2.ReadModifyWriteRule rules = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.v2.ReadModifyWriteRuleOrBuilder getRulesOrBuilder(int index);
}
