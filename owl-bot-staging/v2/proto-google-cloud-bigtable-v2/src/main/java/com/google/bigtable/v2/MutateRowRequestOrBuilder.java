// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/v2/bigtable.proto

package com.google.bigtable.v2;

public interface MutateRowRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.MutateRowRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Required. The unique name of the table to which the mutation should be
   * applied. Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * </pre>
   *
   * <code>string table_name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }</code>
   * @return The tableName.
   */
  java.lang.String getTableName();
  /**
   * <pre>
   * Required. The unique name of the table to which the mutation should be
   * applied. Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * </pre>
   *
   * <code>string table_name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }</code>
   * @return The bytes for tableName.
   */
  com.google.protobuf.ByteString
      getTableNameBytes();

  /**
   * <pre>
   * This value specifies routing for replication. If not specified, the
   * "default" application profile will be used.
   * </pre>
   *
   * <code>string app_profile_id = 4;</code>
   * @return The appProfileId.
   */
  java.lang.String getAppProfileId();
  /**
   * <pre>
   * This value specifies routing for replication. If not specified, the
   * "default" application profile will be used.
   * </pre>
   *
   * <code>string app_profile_id = 4;</code>
   * @return The bytes for appProfileId.
   */
  com.google.protobuf.ByteString
      getAppProfileIdBytes();

  /**
   * <pre>
   * Required. The key of the row to which the mutation should be applied.
   * </pre>
   *
   * <code>bytes row_key = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   * @return The rowKey.
   */
  com.google.protobuf.ByteString getRowKey();

  /**
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];</code>
   */
  java.util.List<com.google.bigtable.v2.Mutation> 
      getMutationsList();
  /**
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];</code>
   */
  com.google.bigtable.v2.Mutation getMutations(int index);
  /**
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];</code>
   */
  int getMutationsCount();
  /**
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];</code>
   */
  java.util.List<? extends com.google.bigtable.v2.MutationOrBuilder> 
      getMutationsOrBuilderList();
  /**
   * <pre>
   * Required. Changes to be atomically applied to the specified row. Entries
   * are applied in order, meaning that earlier mutations can be masked by later
   * ones. Must contain at least one entry and at most 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation mutations = 3 [(.google.api.field_behavior) = REQUIRED];</code>
   */
  com.google.bigtable.v2.MutationOrBuilder getMutationsOrBuilder(
      int index);
}
