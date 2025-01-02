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
// NO CHECKED-IN PROTOBUF GENCODE
// source: google/bigtable/admin/v2/bigtable_table_admin.proto
// Protobuf Java Version: 4.29.0

package com.google.bigtable.admin.v2;

public interface UpdateAuthorizedViewRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.UpdateAuthorizedViewRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The AuthorizedView to update. The `name` in `authorized_view` is
   * used to identify the AuthorizedView. AuthorizedView name must in this
   * format
   * projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;/authorizedViews/&lt;authorized_view&gt;
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.AuthorizedView authorized_view = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return Whether the authorizedView field is set.
   */
  boolean hasAuthorizedView();
  /**
   *
   *
   * <pre>
   * Required. The AuthorizedView to update. The `name` in `authorized_view` is
   * used to identify the AuthorizedView. AuthorizedView name must in this
   * format
   * projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;/authorizedViews/&lt;authorized_view&gt;
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.AuthorizedView authorized_view = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return The authorizedView.
   */
  com.google.bigtable.admin.v2.AuthorizedView getAuthorizedView();
  /**
   *
   *
   * <pre>
   * Required. The AuthorizedView to update. The `name` in `authorized_view` is
   * used to identify the AuthorizedView. AuthorizedView name must in this
   * format
   * projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;/authorizedViews/&lt;authorized_view&gt;
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.AuthorizedView authorized_view = 1 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.admin.v2.AuthorizedViewOrBuilder getAuthorizedViewOrBuilder();

  /**
   *
   *
   * <pre>
   * Optional. The list of fields to update.
   * A mask specifying which fields in the AuthorizedView resource should be
   * updated. This mask is relative to the AuthorizedView resource, not to the
   * request message. A field will be overwritten if it is in the mask. If
   * empty, all fields set in the request will be overwritten. A special value
   * `*` means to overwrite all fields (including fields not set in the
   * request).
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2 [(.google.api.field_behavior) = OPTIONAL];
   * </code>
   *
   * @return Whether the updateMask field is set.
   */
  boolean hasUpdateMask();
  /**
   *
   *
   * <pre>
   * Optional. The list of fields to update.
   * A mask specifying which fields in the AuthorizedView resource should be
   * updated. This mask is relative to the AuthorizedView resource, not to the
   * request message. A field will be overwritten if it is in the mask. If
   * empty, all fields set in the request will be overwritten. A special value
   * `*` means to overwrite all fields (including fields not set in the
   * request).
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2 [(.google.api.field_behavior) = OPTIONAL];
   * </code>
   *
   * @return The updateMask.
   */
  com.google.protobuf.FieldMask getUpdateMask();
  /**
   *
   *
   * <pre>
   * Optional. The list of fields to update.
   * A mask specifying which fields in the AuthorizedView resource should be
   * updated. This mask is relative to the AuthorizedView resource, not to the
   * request message. A field will be overwritten if it is in the mask. If
   * empty, all fields set in the request will be overwritten. A special value
   * `*` means to overwrite all fields (including fields not set in the
   * request).
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2 [(.google.api.field_behavior) = OPTIONAL];
   * </code>
   */
  com.google.protobuf.FieldMaskOrBuilder getUpdateMaskOrBuilder();

  /**
   *
   *
   * <pre>
   * Optional. If true, ignore the safety checks when updating the
   * AuthorizedView.
   * </pre>
   *
   * <code>bool ignore_warnings = 3 [(.google.api.field_behavior) = OPTIONAL];</code>
   *
   * @return The ignoreWarnings.
   */
  boolean getIgnoreWarnings();
}
