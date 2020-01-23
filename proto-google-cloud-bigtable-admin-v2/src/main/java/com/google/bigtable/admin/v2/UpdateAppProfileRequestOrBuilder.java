/*
 * Copyright 2020 Google LLC
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
// source: google/bigtable/admin/v2/bigtable_instance_admin.proto

package com.google.bigtable.admin.v2;

public interface UpdateAppProfileRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.UpdateAppProfileRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The app profile which will (partially) replace the current value.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.AppProfile app_profile = 1;</code>
   *
   * @return Whether the appProfile field is set.
   */
  boolean hasAppProfile();
  /**
   *
   *
   * <pre>
   * The app profile which will (partially) replace the current value.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.AppProfile app_profile = 1;</code>
   *
   * @return The appProfile.
   */
  com.google.bigtable.admin.v2.AppProfile getAppProfile();
  /**
   *
   *
   * <pre>
   * The app profile which will (partially) replace the current value.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.AppProfile app_profile = 1;</code>
   */
  com.google.bigtable.admin.v2.AppProfileOrBuilder getAppProfileOrBuilder();

  /**
   *
   *
   * <pre>
   * The subset of app profile fields which should be replaced.
   * If unset, all fields will be replaced.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2;</code>
   *
   * @return Whether the updateMask field is set.
   */
  boolean hasUpdateMask();
  /**
   *
   *
   * <pre>
   * The subset of app profile fields which should be replaced.
   * If unset, all fields will be replaced.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2;</code>
   *
   * @return The updateMask.
   */
  com.google.protobuf.FieldMask getUpdateMask();
  /**
   *
   *
   * <pre>
   * The subset of app profile fields which should be replaced.
   * If unset, all fields will be replaced.
   * </pre>
   *
   * <code>.google.protobuf.FieldMask update_mask = 2;</code>
   */
  com.google.protobuf.FieldMaskOrBuilder getUpdateMaskOrBuilder();

  /**
   *
   *
   * <pre>
   * If true, ignore safety checks when updating the app profile.
   * </pre>
   *
   * <code>bool ignore_warnings = 3;</code>
   *
   * @return The ignoreWarnings.
   */
  boolean getIgnoreWarnings();
}
