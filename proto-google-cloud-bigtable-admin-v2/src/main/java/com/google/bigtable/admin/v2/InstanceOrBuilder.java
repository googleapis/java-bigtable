/*
 * Copyright 2024 Google LLC
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

// Protobuf Java Version: 3.25.4
package com.google.bigtable.admin.v2;

public interface InstanceOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.Instance)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The unique name of the instance. Values are of the form
   * `projects/{project}/instances/[a-z][a-z0-9&#92;&#92;-]+[a-z0-9]`.
   * </pre>
   *
   * <code>string name = 1;</code>
   *
   * @return The name.
   */
  java.lang.String getName();
  /**
   *
   *
   * <pre>
   * The unique name of the instance. Values are of the form
   * `projects/{project}/instances/[a-z][a-z0-9&#92;&#92;-]+[a-z0-9]`.
   * </pre>
   *
   * <code>string name = 1;</code>
   *
   * @return The bytes for name.
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * Required. The descriptive name for this instance as it appears in UIs.
   * Can be changed at any time, but should be kept globally unique
   * to avoid confusion.
   * </pre>
   *
   * <code>string display_name = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The displayName.
   */
  java.lang.String getDisplayName();
  /**
   *
   *
   * <pre>
   * Required. The descriptive name for this instance as it appears in UIs.
   * Can be changed at any time, but should be kept globally unique
   * to avoid confusion.
   * </pre>
   *
   * <code>string display_name = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for displayName.
   */
  com.google.protobuf.ByteString getDisplayNameBytes();

  /**
   *
   *
   * <pre>
   * (`OutputOnly`)
   * The current state of the instance.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Instance.State state = 3;</code>
   *
   * @return The enum numeric value on the wire for state.
   */
  int getStateValue();
  /**
   *
   *
   * <pre>
   * (`OutputOnly`)
   * The current state of the instance.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Instance.State state = 3;</code>
   *
   * @return The state.
   */
  com.google.bigtable.admin.v2.Instance.State getState();

  /**
   *
   *
   * <pre>
   * The type of the instance. Defaults to `PRODUCTION`.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Instance.Type type = 4;</code>
   *
   * @return The enum numeric value on the wire for type.
   */
  int getTypeValue();
  /**
   *
   *
   * <pre>
   * The type of the instance. Defaults to `PRODUCTION`.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Instance.Type type = 4;</code>
   *
   * @return The type.
   */
  com.google.bigtable.admin.v2.Instance.Type getType();

  /**
   *
   *
   * <pre>
   * Labels are a flexible and lightweight mechanism for organizing cloud
   * resources into groups that reflect a customer's organizational needs and
   * deployment strategies. They can be used to filter resources and aggregate
   * metrics.
   *
   * * Label keys must be between 1 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}][&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,62}`.
   * * Label values must be between 0 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,63}`.
   * * No more than 64 labels can be associated with a given resource.
   * * Keys and values must both be under 128 bytes.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
   */
  int getLabelsCount();
  /**
   *
   *
   * <pre>
   * Labels are a flexible and lightweight mechanism for organizing cloud
   * resources into groups that reflect a customer's organizational needs and
   * deployment strategies. They can be used to filter resources and aggregate
   * metrics.
   *
   * * Label keys must be between 1 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}][&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,62}`.
   * * Label values must be between 0 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,63}`.
   * * No more than 64 labels can be associated with a given resource.
   * * Keys and values must both be under 128 bytes.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
   */
  boolean containsLabels(java.lang.String key);
  /** Use {@link #getLabelsMap()} instead. */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String> getLabels();
  /**
   *
   *
   * <pre>
   * Labels are a flexible and lightweight mechanism for organizing cloud
   * resources into groups that reflect a customer's organizational needs and
   * deployment strategies. They can be used to filter resources and aggregate
   * metrics.
   *
   * * Label keys must be between 1 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}][&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,62}`.
   * * Label values must be between 0 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,63}`.
   * * No more than 64 labels can be associated with a given resource.
   * * Keys and values must both be under 128 bytes.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
   */
  java.util.Map<java.lang.String, java.lang.String> getLabelsMap();
  /**
   *
   *
   * <pre>
   * Labels are a flexible and lightweight mechanism for organizing cloud
   * resources into groups that reflect a customer's organizational needs and
   * deployment strategies. They can be used to filter resources and aggregate
   * metrics.
   *
   * * Label keys must be between 1 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}][&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,62}`.
   * * Label values must be between 0 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,63}`.
   * * No more than 64 labels can be associated with a given resource.
   * * Keys and values must both be under 128 bytes.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
   */
  /* nullable */
  java.lang.String getLabelsOrDefault(
      java.lang.String key,
      /* nullable */
      java.lang.String defaultValue);
  /**
   *
   *
   * <pre>
   * Labels are a flexible and lightweight mechanism for organizing cloud
   * resources into groups that reflect a customer's organizational needs and
   * deployment strategies. They can be used to filter resources and aggregate
   * metrics.
   *
   * * Label keys must be between 1 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}][&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,62}`.
   * * Label values must be between 0 and 63 characters long and must conform to
   *   the regular expression: `[&#92;p{Ll}&#92;p{Lo}&#92;p{N}_-]{0,63}`.
   * * No more than 64 labels can be associated with a given resource.
   * * Keys and values must both be under 128 bytes.
   * </pre>
   *
   * <code>map&lt;string, string&gt; labels = 5;</code>
   */
  java.lang.String getLabelsOrThrow(java.lang.String key);

  /**
   *
   *
   * <pre>
   * Output only. A server-assigned timestamp representing when this Instance
   * was created. For instances created before this field was added (August
   * 2021), this value is `seconds: 0, nanos: 1`.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp create_time = 7 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return Whether the createTime field is set.
   */
  boolean hasCreateTime();
  /**
   *
   *
   * <pre>
   * Output only. A server-assigned timestamp representing when this Instance
   * was created. For instances created before this field was added (August
   * 2021), this value is `seconds: 0, nanos: 1`.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp create_time = 7 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   *
   * @return The createTime.
   */
  com.google.protobuf.Timestamp getCreateTime();
  /**
   *
   *
   * <pre>
   * Output only. A server-assigned timestamp representing when this Instance
   * was created. For instances created before this field was added (August
   * 2021), this value is `seconds: 0, nanos: 1`.
   * </pre>
   *
   * <code>.google.protobuf.Timestamp create_time = 7 [(.google.api.field_behavior) = OUTPUT_ONLY];
   * </code>
   */
  com.google.protobuf.TimestampOrBuilder getCreateTimeOrBuilder();

  /**
   *
   *
   * <pre>
   * Output only. Reserved for future use.
   * </pre>
   *
   * <code>optional bool satisfies_pzs = 8 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
   *
   * @return Whether the satisfiesPzs field is set.
   */
  boolean hasSatisfiesPzs();
  /**
   *
   *
   * <pre>
   * Output only. Reserved for future use.
   * </pre>
   *
   * <code>optional bool satisfies_pzs = 8 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
   *
   * @return The satisfiesPzs.
   */
  boolean getSatisfiesPzs();
}
