// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/admin/v2/bigtable_table_admin.proto

// Protobuf Java Version: 3.25.2
package com.google.bigtable.admin.v2;

public interface CreateAuthorizedViewMetadataOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.CreateAuthorizedViewMetadata)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * The request that prompted the initiation of this CreateInstance operation.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.CreateAuthorizedViewRequest original_request = 1;</code>
   * @return Whether the originalRequest field is set.
   */
  boolean hasOriginalRequest();
  /**
   * <pre>
   * The request that prompted the initiation of this CreateInstance operation.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.CreateAuthorizedViewRequest original_request = 1;</code>
   * @return The originalRequest.
   */
  com.google.bigtable.admin.v2.CreateAuthorizedViewRequest getOriginalRequest();
  /**
   * <pre>
   * The request that prompted the initiation of this CreateInstance operation.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.CreateAuthorizedViewRequest original_request = 1;</code>
   */
  com.google.bigtable.admin.v2.CreateAuthorizedViewRequestOrBuilder getOriginalRequestOrBuilder();

  /**
   * <pre>
   * The time at which the original request was received.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp request_time = 2;</code>
   * @return Whether the requestTime field is set.
   */
  boolean hasRequestTime();
  /**
   * <pre>
   * The time at which the original request was received.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp request_time = 2;</code>
   * @return The requestTime.
   */
  com.google.protobuf.Timestamp getRequestTime();
  /**
   * <pre>
   * The time at which the original request was received.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp request_time = 2;</code>
   */
  com.google.protobuf.TimestampOrBuilder getRequestTimeOrBuilder();

  /**
   * <pre>
   * The time at which the operation failed or was completed successfully.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp finish_time = 3;</code>
   * @return Whether the finishTime field is set.
   */
  boolean hasFinishTime();
  /**
   * <pre>
   * The time at which the operation failed or was completed successfully.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp finish_time = 3;</code>
   * @return The finishTime.
   */
  com.google.protobuf.Timestamp getFinishTime();
  /**
   * <pre>
   * The time at which the operation failed or was completed successfully.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp finish_time = 3;</code>
   */
  com.google.protobuf.TimestampOrBuilder getFinishTimeOrBuilder();
}
