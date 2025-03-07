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

// Protobuf Java Version: 3.25.5
package com.google.bigtable.v2;

public interface ExecuteQueryRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ExecuteQueryRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The unique name of the instance against which the query should be
   * executed.
   * Values are of the form `projects/&lt;project&gt;/instances/&lt;instance&gt;`
   * </pre>
   *
   * <code>
   * string instance_name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The instanceName.
   */
  java.lang.String getInstanceName();
  /**
   *
   *
   * <pre>
   * Required. The unique name of the instance against which the query should be
   * executed.
   * Values are of the form `projects/&lt;project&gt;/instances/&lt;instance&gt;`
   * </pre>
   *
   * <code>
   * string instance_name = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for instanceName.
   */
  com.google.protobuf.ByteString getInstanceNameBytes();

  /**
   *
   *
   * <pre>
   * Optional. This value specifies routing for replication. If not specified,
   * the `default` application profile will be used.
   * </pre>
   *
   * <code>string app_profile_id = 2 [(.google.api.field_behavior) = OPTIONAL];</code>
   *
   * @return The appProfileId.
   */
  java.lang.String getAppProfileId();
  /**
   *
   *
   * <pre>
   * Optional. This value specifies routing for replication. If not specified,
   * the `default` application profile will be used.
   * </pre>
   *
   * <code>string app_profile_id = 2 [(.google.api.field_behavior) = OPTIONAL];</code>
   *
   * @return The bytes for appProfileId.
   */
  com.google.protobuf.ByteString getAppProfileIdBytes();

  /**
   *
   *
   * <pre>
   * Required. The query string.
   *
   * Exactly one of `query` and `prepared_query` is required. Setting both
   * or neither is an `INVALID_ARGUMENT`.
   * </pre>
   *
   * <code>string query = 3 [deprecated = true, (.google.api.field_behavior) = REQUIRED];</code>
   *
   * @deprecated google.bigtable.v2.ExecuteQueryRequest.query is deprecated. See
   *     google/bigtable/v2/bigtable.proto;l=1064
   * @return The query.
   */
  @java.lang.Deprecated
  java.lang.String getQuery();
  /**
   *
   *
   * <pre>
   * Required. The query string.
   *
   * Exactly one of `query` and `prepared_query` is required. Setting both
   * or neither is an `INVALID_ARGUMENT`.
   * </pre>
   *
   * <code>string query = 3 [deprecated = true, (.google.api.field_behavior) = REQUIRED];</code>
   *
   * @deprecated google.bigtable.v2.ExecuteQueryRequest.query is deprecated. See
   *     google/bigtable/v2/bigtable.proto;l=1064
   * @return The bytes for query.
   */
  @java.lang.Deprecated
  com.google.protobuf.ByteString getQueryBytes();

  /**
   *
   *
   * <pre>
   * A prepared query that was returned from `PrepareQueryResponse`.
   *
   * Exactly one of `query` and `prepared_query` is required. Setting both
   * or neither is an `INVALID_ARGUMENT`.
   *
   * Setting this field also places restrictions on several other fields:
   * - `data_format` must be empty.
   * - `validate_only` must be false.
   * - `params` must match the `param_types` set in the `PrepareQueryRequest`.
   * </pre>
   *
   * <code>bytes prepared_query = 9;</code>
   *
   * @return The preparedQuery.
   */
  com.google.protobuf.ByteString getPreparedQuery();

  /**
   *
   *
   * <pre>
   * Protocol buffer format as described by ProtoSchema and ProtoRows
   * messages.
   * </pre>
   *
   * <code>.google.bigtable.v2.ProtoFormat proto_format = 4 [deprecated = true];</code>
   *
   * @deprecated google.bigtable.v2.ExecuteQueryRequest.proto_format is deprecated. See
   *     google/bigtable/v2/bigtable.proto;l=1085
   * @return Whether the protoFormat field is set.
   */
  @java.lang.Deprecated
  boolean hasProtoFormat();
  /**
   *
   *
   * <pre>
   * Protocol buffer format as described by ProtoSchema and ProtoRows
   * messages.
   * </pre>
   *
   * <code>.google.bigtable.v2.ProtoFormat proto_format = 4 [deprecated = true];</code>
   *
   * @deprecated google.bigtable.v2.ExecuteQueryRequest.proto_format is deprecated. See
   *     google/bigtable/v2/bigtable.proto;l=1085
   * @return The protoFormat.
   */
  @java.lang.Deprecated
  com.google.bigtable.v2.ProtoFormat getProtoFormat();
  /**
   *
   *
   * <pre>
   * Protocol buffer format as described by ProtoSchema and ProtoRows
   * messages.
   * </pre>
   *
   * <code>.google.bigtable.v2.ProtoFormat proto_format = 4 [deprecated = true];</code>
   */
  @java.lang.Deprecated
  com.google.bigtable.v2.ProtoFormatOrBuilder getProtoFormatOrBuilder();

  /**
   *
   *
   * <pre>
   * Optional. If this request is resuming a previously interrupted query
   * execution, `resume_token` should be copied from the last
   * PartialResultSet yielded before the interruption. Doing this
   * enables the query execution to resume where the last one left
   * off.
   * The rest of the request parameters must exactly match the
   * request that yielded this token. Otherwise the request will fail.
   * </pre>
   *
   * <code>bytes resume_token = 8 [(.google.api.field_behavior) = OPTIONAL];</code>
   *
   * @return The resumeToken.
   */
  com.google.protobuf.ByteString getResumeToken();

  /**
   *
   *
   * <pre>
   * Required. params contains string type keys and Bigtable type values that
   * bind to placeholders in the query string. In query string, a parameter
   * placeholder consists of the
   * `&#64;` character followed by the parameter name (for example, `&#64;firstName`) in
   * the query string.
   *
   * For example, if
   * `params["firstName"] = bytes_value: "foo" type {bytes_type {}}`
   * then `&#64;firstName` will be replaced with googlesql bytes value "foo" in the
   * query string during query evaluation.
   *
   * If `Value.kind` is not set, the value is treated as a NULL value of the
   * given type. For example, if
   * `params["firstName"] = type {string_type {}}`
   * then `&#64;firstName` will be replaced with googlesql null string.
   *
   * If `query` is set, any empty `Value.type` in the map will be rejected with
   * `INVALID_ARGUMENT`.
   *
   * If `prepared_query` is set, any empty `Value.type` in the map will be
   * inferred from the `param_types` in the `PrepareQueryRequest`. Any non-empty
   * `Value.type` must match the corresponding `param_types` entry, or be
   * rejected with `INVALID_ARGUMENT`.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.v2.Value&gt; params = 7 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  int getParamsCount();
  /**
   *
   *
   * <pre>
   * Required. params contains string type keys and Bigtable type values that
   * bind to placeholders in the query string. In query string, a parameter
   * placeholder consists of the
   * `&#64;` character followed by the parameter name (for example, `&#64;firstName`) in
   * the query string.
   *
   * For example, if
   * `params["firstName"] = bytes_value: "foo" type {bytes_type {}}`
   * then `&#64;firstName` will be replaced with googlesql bytes value "foo" in the
   * query string during query evaluation.
   *
   * If `Value.kind` is not set, the value is treated as a NULL value of the
   * given type. For example, if
   * `params["firstName"] = type {string_type {}}`
   * then `&#64;firstName` will be replaced with googlesql null string.
   *
   * If `query` is set, any empty `Value.type` in the map will be rejected with
   * `INVALID_ARGUMENT`.
   *
   * If `prepared_query` is set, any empty `Value.type` in the map will be
   * inferred from the `param_types` in the `PrepareQueryRequest`. Any non-empty
   * `Value.type` must match the corresponding `param_types` entry, or be
   * rejected with `INVALID_ARGUMENT`.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.v2.Value&gt; params = 7 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  boolean containsParams(java.lang.String key);
  /** Use {@link #getParamsMap()} instead. */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, com.google.bigtable.v2.Value> getParams();
  /**
   *
   *
   * <pre>
   * Required. params contains string type keys and Bigtable type values that
   * bind to placeholders in the query string. In query string, a parameter
   * placeholder consists of the
   * `&#64;` character followed by the parameter name (for example, `&#64;firstName`) in
   * the query string.
   *
   * For example, if
   * `params["firstName"] = bytes_value: "foo" type {bytes_type {}}`
   * then `&#64;firstName` will be replaced with googlesql bytes value "foo" in the
   * query string during query evaluation.
   *
   * If `Value.kind` is not set, the value is treated as a NULL value of the
   * given type. For example, if
   * `params["firstName"] = type {string_type {}}`
   * then `&#64;firstName` will be replaced with googlesql null string.
   *
   * If `query` is set, any empty `Value.type` in the map will be rejected with
   * `INVALID_ARGUMENT`.
   *
   * If `prepared_query` is set, any empty `Value.type` in the map will be
   * inferred from the `param_types` in the `PrepareQueryRequest`. Any non-empty
   * `Value.type` must match the corresponding `param_types` entry, or be
   * rejected with `INVALID_ARGUMENT`.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.v2.Value&gt; params = 7 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  java.util.Map<java.lang.String, com.google.bigtable.v2.Value> getParamsMap();
  /**
   *
   *
   * <pre>
   * Required. params contains string type keys and Bigtable type values that
   * bind to placeholders in the query string. In query string, a parameter
   * placeholder consists of the
   * `&#64;` character followed by the parameter name (for example, `&#64;firstName`) in
   * the query string.
   *
   * For example, if
   * `params["firstName"] = bytes_value: "foo" type {bytes_type {}}`
   * then `&#64;firstName` will be replaced with googlesql bytes value "foo" in the
   * query string during query evaluation.
   *
   * If `Value.kind` is not set, the value is treated as a NULL value of the
   * given type. For example, if
   * `params["firstName"] = type {string_type {}}`
   * then `&#64;firstName` will be replaced with googlesql null string.
   *
   * If `query` is set, any empty `Value.type` in the map will be rejected with
   * `INVALID_ARGUMENT`.
   *
   * If `prepared_query` is set, any empty `Value.type` in the map will be
   * inferred from the `param_types` in the `PrepareQueryRequest`. Any non-empty
   * `Value.type` must match the corresponding `param_types` entry, or be
   * rejected with `INVALID_ARGUMENT`.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.v2.Value&gt; params = 7 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  /* nullable */
  com.google.bigtable.v2.Value getParamsOrDefault(
      java.lang.String key,
      /* nullable */
      com.google.bigtable.v2.Value defaultValue);
  /**
   *
   *
   * <pre>
   * Required. params contains string type keys and Bigtable type values that
   * bind to placeholders in the query string. In query string, a parameter
   * placeholder consists of the
   * `&#64;` character followed by the parameter name (for example, `&#64;firstName`) in
   * the query string.
   *
   * For example, if
   * `params["firstName"] = bytes_value: "foo" type {bytes_type {}}`
   * then `&#64;firstName` will be replaced with googlesql bytes value "foo" in the
   * query string during query evaluation.
   *
   * If `Value.kind` is not set, the value is treated as a NULL value of the
   * given type. For example, if
   * `params["firstName"] = type {string_type {}}`
   * then `&#64;firstName` will be replaced with googlesql null string.
   *
   * If `query` is set, any empty `Value.type` in the map will be rejected with
   * `INVALID_ARGUMENT`.
   *
   * If `prepared_query` is set, any empty `Value.type` in the map will be
   * inferred from the `param_types` in the `PrepareQueryRequest`. Any non-empty
   * `Value.type` must match the corresponding `param_types` entry, or be
   * rejected with `INVALID_ARGUMENT`.
   * </pre>
   *
   * <code>
   * map&lt;string, .google.bigtable.v2.Value&gt; params = 7 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.v2.Value getParamsOrThrow(java.lang.String key);

  com.google.bigtable.v2.ExecuteQueryRequest.DataFormatCase getDataFormatCase();
}
