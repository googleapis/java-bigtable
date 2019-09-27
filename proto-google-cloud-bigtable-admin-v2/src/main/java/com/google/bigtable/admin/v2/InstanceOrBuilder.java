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
// source: google/bigtable/admin/v2/instance.proto

package com.google.bigtable.admin.v2;

public interface InstanceOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.Instance)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * (`OutputOnly`)
   * The unique name of the instance. Values are of the form
   * `projects/&lt;project&gt;/instances/[a-z][a-z0-9&#92;&#92;-]+[a-z0-9]`.
   * </pre>
   *
   * <code>string name = 1;</code>
   */
  java.lang.String getName();
  /**
   *
   *
   * <pre>
   * (`OutputOnly`)
   * The unique name of the instance. Values are of the form
   * `projects/&lt;project&gt;/instances/[a-z][a-z0-9&#92;&#92;-]+[a-z0-9]`.
   * </pre>
   *
   * <code>string name = 1;</code>
   */
  com.google.protobuf.ByteString getNameBytes();

  /**
   *
   *
   * <pre>
   * The descriptive name for this instance as it appears in UIs.
   * Can be changed at any time, but should be kept globally unique
   * to avoid confusion.
   * </pre>
   *
   * <code>string display_name = 2;</code>
   */
  java.lang.String getDisplayName();
  /**
   *
   *
   * <pre>
   * The descriptive name for this instance as it appears in UIs.
   * Can be changed at any time, but should be kept globally unique
   * to avoid confusion.
   * </pre>
   *
   * <code>string display_name = 2;</code>
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
  java.lang.String getLabelsOrDefault(java.lang.String key, java.lang.String defaultValue);
  /**
   *
   *
   * <pre>
   * Labels are a flexible and lightweight mechanism for organizing cloud
   * resources into groups that reflect a customer's organizational needs and
   * deployment strategies. They can be used to filter resources and aggregate
   * metrics.
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
}
