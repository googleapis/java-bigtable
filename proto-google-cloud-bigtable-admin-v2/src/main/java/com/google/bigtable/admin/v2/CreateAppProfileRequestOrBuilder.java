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
// source: google/bigtable/admin/v2/bigtable_instance_admin.proto

package com.google.bigtable.admin.v2;

public interface CreateAppProfileRequestOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.admin.v2.CreateAppProfileRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Required. The unique name of the instance in which to create the new app
   * profile. Values are of the form `projects/{project}/instances/{instance}`.
   * </pre>
   *
   * <code>
   * string parent = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The parent.
   */
  java.lang.String getParent();
  /**
   *
   *
   * <pre>
   * Required. The unique name of the instance in which to create the new app
   * profile. Values are of the form `projects/{project}/instances/{instance}`.
   * </pre>
   *
   * <code>
   * string parent = 1 [(.google.api.field_behavior) = REQUIRED, (.google.api.resource_reference) = { ... }
   * </code>
   *
   * @return The bytes for parent.
   */
  com.google.protobuf.ByteString getParentBytes();

  /**
   *
   *
   * <pre>
   * Required. The ID to be used when referring to the new app profile within
   * its instance, e.g., just `myprofile` rather than
   * `projects/myproject/instances/myinstance/appProfiles/myprofile`.
   * </pre>
   *
   * <code>string app_profile_id = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The appProfileId.
   */
  java.lang.String getAppProfileId();
  /**
   *
   *
   * <pre>
   * Required. The ID to be used when referring to the new app profile within
   * its instance, e.g., just `myprofile` rather than
   * `projects/myproject/instances/myinstance/appProfiles/myprofile`.
   * </pre>
   *
   * <code>string app_profile_id = 2 [(.google.api.field_behavior) = REQUIRED];</code>
   *
   * @return The bytes for appProfileId.
   */
  com.google.protobuf.ByteString getAppProfileIdBytes();

  /**
   *
   *
   * <pre>
   * Required. The app profile to be created.
   * Fields marked `OutputOnly` will be ignored.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.AppProfile app_profile = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return Whether the appProfile field is set.
   */
  boolean hasAppProfile();
  /**
   *
   *
   * <pre>
   * Required. The app profile to be created.
   * Fields marked `OutputOnly` will be ignored.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.AppProfile app_profile = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   *
   * @return The appProfile.
   */
  com.google.bigtable.admin.v2.AppProfile getAppProfile();
  /**
   *
   *
   * <pre>
   * Required. The app profile to be created.
   * Fields marked `OutputOnly` will be ignored.
   * </pre>
   *
   * <code>
   * .google.bigtable.admin.v2.AppProfile app_profile = 3 [(.google.api.field_behavior) = REQUIRED];
   * </code>
   */
  com.google.bigtable.admin.v2.AppProfileOrBuilder getAppProfileOrBuilder();

  /**
   *
   *
   * <pre>
   * If true, ignore safety checks when creating the app profile.
   * </pre>
   *
   * <code>bool ignore_warnings = 4;</code>
   *
   * @return The ignoreWarnings.
   */
  boolean getIgnoreWarnings();
}
