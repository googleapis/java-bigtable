// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/admin/v2/table.proto

// Protobuf Java Version: 3.25.2
package com.google.bigtable.admin.v2;

public interface ColumnFamilyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.ColumnFamily)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Garbage collection rule specified as a protobuf.
   * Must serialize to at most 500 bytes.
   *
   * NOTE: Garbage collection executes opportunistically in the background, and
   * so it's possible for reads to return a cell even if it matches the active
   * GC expression for its family.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
   * @return Whether the gcRule field is set.
   */
  boolean hasGcRule();
  /**
   * <pre>
   * Garbage collection rule specified as a protobuf.
   * Must serialize to at most 500 bytes.
   *
   * NOTE: Garbage collection executes opportunistically in the background, and
   * so it's possible for reads to return a cell even if it matches the active
   * GC expression for its family.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
   * @return The gcRule.
   */
  com.google.bigtable.admin.v2.GcRule getGcRule();
  /**
   * <pre>
   * Garbage collection rule specified as a protobuf.
   * Must serialize to at most 500 bytes.
   *
   * NOTE: Garbage collection executes opportunistically in the background, and
   * so it's possible for reads to return a cell even if it matches the active
   * GC expression for its family.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
   */
  com.google.bigtable.admin.v2.GcRuleOrBuilder getGcRuleOrBuilder();

  /**
   * <pre>
   * The type of data stored in each of this family's cell values, including its
   * full encoding. If omitted, the family only serves raw untyped bytes.
   *
   * For now, only the `Aggregate` type is supported.
   *
   * `Aggregate` can only be set at family creation and is immutable afterwards.
   *
   *
   * If `value_type` is `Aggregate`, written data must be compatible with:
   *  * `value_type.input_type` for `AddInput` mutations
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
   * @return Whether the valueType field is set.
   */
  boolean hasValueType();
  /**
   * <pre>
   * The type of data stored in each of this family's cell values, including its
   * full encoding. If omitted, the family only serves raw untyped bytes.
   *
   * For now, only the `Aggregate` type is supported.
   *
   * `Aggregate` can only be set at family creation and is immutable afterwards.
   *
   *
   * If `value_type` is `Aggregate`, written data must be compatible with:
   *  * `value_type.input_type` for `AddInput` mutations
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
   * @return The valueType.
   */
  com.google.bigtable.admin.v2.Type getValueType();
  /**
   * <pre>
   * The type of data stored in each of this family's cell values, including its
   * full encoding. If omitted, the family only serves raw untyped bytes.
   *
   * For now, only the `Aggregate` type is supported.
   *
   * `Aggregate` can only be set at family creation and is immutable afterwards.
   *
   *
   * If `value_type` is `Aggregate`, written data must be compatible with:
   *  * `value_type.input_type` for `AddInput` mutations
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
   */
  com.google.bigtable.admin.v2.TypeOrBuilder getValueTypeOrBuilder();
}
