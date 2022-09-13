// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/admin/v2/bigtable_table_admin.proto

package com.google.bigtable.admin.v2;

public interface UpdateTableRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.UpdateTableRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Required. The table to update.
   * The table's `name` field is used to identify the table to update.
   * Format:
   * `projects/{project}/instances/{instance}/tables/[_a-zA-Z0-9][-_.a-zA-Z0-9]*`
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Table table = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   * @return Whether the table field is set.
   */
  boolean hasTable();
  /**
   * <pre>
   * Required. The table to update.
   * The table's `name` field is used to identify the table to update.
   * Format:
   * `projects/{project}/instances/{instance}/tables/[_a-zA-Z0-9][-_.a-zA-Z0-9]*`
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Table table = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   * @return The table.
   */
  com.google.bigtable.admin.v2.Table getTable();
  /**
   * <pre>
   * Required. The table to update.
   * The table's `name` field is used to identify the table to update.
   * Format:
   * `projects/{project}/instances/{instance}/tables/[_a-zA-Z0-9][-_.a-zA-Z0-9]*`
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Table table = 1 [(.google.api.field_behavior) = REQUIRED];</code>
   */
  com.google.bigtable.admin.v2.TableOrBuilder getTableOrBuilder();

  /**
   * <pre>
   * Required. The list of fields to update.
   * A mask specifying which fields (e.g. `deletion_protection`) in the `table`
   * field should be updated. This mask is relative to the `table` field, not to
   * the request message. The wildcard (*) path is currently not supported.
   * Currently UpdateTable is only supported for the following field:
   *  * `deletion_protection`
   * If `column_families` is set in `update_mask`, it will return an
   * UNIMPLEMENTED error.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   * @return Whether the updateMask field is set.
   */
  boolean hasUpdateMask();
  /**
   * <pre>
   * Required. The list of fields to update.
   * A mask specifying which fields (e.g. `deletion_protection`) in the `table`
   * field should be updated. This mask is relative to the `table` field, not to
   * the request message. The wildcard (*) path is currently not supported.
   * Currently UpdateTable is only supported for the following field:
   *  * `deletion_protection`
   * If `column_families` is set in `update_mask`, it will return an
   * UNIMPLEMENTED error.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   * @return The updateMask.
   */
  com.google.protobuf.FieldMask getUpdateMask();
  /**
   * <pre>
   * Required. The list of fields to update.
   * A mask specifying which fields (e.g. `deletion_protection`) in the `table`
   * field should be updated. This mask is relative to the `table` field, not to
   * the request message. The wildcard (*) path is currently not supported.
   * Currently UpdateTable is only supported for the following field:
   *  * `deletion_protection`
   * If `column_families` is set in `update_mask`, it will return an
   * UNIMPLEMENTED error.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   */
  com.google.protobuf.FieldMaskOrBuilder getUpdateMaskOrBuilder();
}
