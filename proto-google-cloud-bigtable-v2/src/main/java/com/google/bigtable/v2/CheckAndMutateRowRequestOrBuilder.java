// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/v2/bigtable.proto

package com.google.bigtable.v2;

public interface CheckAndMutateRowRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.CheckAndMutateRowRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * Required. The unique name of the table to which the conditional mutation should be
   * applied.
   * Values are of the form
   * `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * </pre>
   *
   * <code>string table_name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }</code>
   * @return The tableName.
   */
  java.lang.String getTableName();
  /**
   * <pre>
   * Required. The unique name of the table to which the conditional mutation should be
   * applied.
   * Values are of the form
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
   * <code>string app_profile_id = 7;</code>
   * @return The appProfileId.
   */
  java.lang.String getAppProfileId();
  /**
   * <pre>
   * This value specifies routing for replication. If not specified, the
   * "default" application profile will be used.
   * </pre>
   *
   * <code>string app_profile_id = 7;</code>
   * @return The bytes for appProfileId.
   */
  com.google.protobuf.ByteString
      getAppProfileIdBytes();

  /**
   * <pre>
   * Required. The key of the row to which the conditional mutation should be applied.
   * </pre>
   *
   * <code>bytes row_key = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   * @return The rowKey.
   */
  com.google.protobuf.ByteString getRowKey();

  /**
   * <pre>
   * The filter to be applied to the contents of the specified row. Depending
   * on whether or not any results are yielded, either `true_mutations` or
   * `false_mutations` will be executed. If unset, checks that the row contains
   * any values at all.
   * </pre>
   *
   * <code>.google.bigtable.v2.RowFilter predicate_filter = 6;</code>
   * @return Whether the predicateFilter field is set.
   */
  boolean hasPredicateFilter();
  /**
   * <pre>
   * The filter to be applied to the contents of the specified row. Depending
   * on whether or not any results are yielded, either `true_mutations` or
   * `false_mutations` will be executed. If unset, checks that the row contains
   * any values at all.
   * </pre>
   *
   * <code>.google.bigtable.v2.RowFilter predicate_filter = 6;</code>
   * @return The predicateFilter.
   */
  com.google.bigtable.v2.RowFilter getPredicateFilter();
  /**
   * <pre>
   * The filter to be applied to the contents of the specified row. Depending
   * on whether or not any results are yielded, either `true_mutations` or
   * `false_mutations` will be executed. If unset, checks that the row contains
   * any values at all.
   * </pre>
   *
   * <code>.google.bigtable.v2.RowFilter predicate_filter = 6;</code>
   */
  com.google.bigtable.v2.RowFilterOrBuilder getPredicateFilterOrBuilder();

  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * yields at least one cell when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `false_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation true_mutations = 4;</code>
   */
  java.util.List<com.google.bigtable.v2.Mutation> 
      getTrueMutationsList();
  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * yields at least one cell when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `false_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation true_mutations = 4;</code>
   */
  com.google.bigtable.v2.Mutation getTrueMutations(int index);
  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * yields at least one cell when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `false_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation true_mutations = 4;</code>
   */
  int getTrueMutationsCount();
  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * yields at least one cell when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `false_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation true_mutations = 4;</code>
   */
  java.util.List<? extends com.google.bigtable.v2.MutationOrBuilder> 
      getTrueMutationsOrBuilderList();
  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * yields at least one cell when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `false_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation true_mutations = 4;</code>
   */
  com.google.bigtable.v2.MutationOrBuilder getTrueMutationsOrBuilder(
      int index);

  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * does not yield any cells when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `true_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation false_mutations = 5;</code>
   */
  java.util.List<com.google.bigtable.v2.Mutation> 
      getFalseMutationsList();
  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * does not yield any cells when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `true_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation false_mutations = 5;</code>
   */
  com.google.bigtable.v2.Mutation getFalseMutations(int index);
  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * does not yield any cells when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `true_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation false_mutations = 5;</code>
   */
  int getFalseMutationsCount();
  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * does not yield any cells when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `true_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation false_mutations = 5;</code>
   */
  java.util.List<? extends com.google.bigtable.v2.MutationOrBuilder> 
      getFalseMutationsOrBuilderList();
  /**
   * <pre>
   * Changes to be atomically applied to the specified row if `predicate_filter`
   * does not yield any cells when applied to `row_key`. Entries are applied in
   * order, meaning that earlier mutations can be masked by later ones.
   * Must contain at least one entry if `true_mutations` is empty, and at most
   * 100000.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Mutation false_mutations = 5;</code>
   */
  com.google.bigtable.v2.MutationOrBuilder getFalseMutationsOrBuilder(
      int index);
}
