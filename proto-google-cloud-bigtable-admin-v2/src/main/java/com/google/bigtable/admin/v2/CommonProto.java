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
// source: google/bigtable/admin/v2/common.proto

// Protobuf Java Version: 3.25.8
package com.google.bigtable.admin.v2;

public final class CommonProto {
  private CommonProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_OperationProgress_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_OperationProgress_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n"
          + "%google/bigtable/admin/v2/common.proto\022"
          + "\030google.bigtable.admin.v2\032\037google/protobuf/timestamp.proto\"\213\001\n"
          + "\021OperationProgress\022\030\n"
          + "\020progress_percent\030\001 \001(\005\022.\n\n"
          + "start_time\030\002 \001(\0132\032.google.protobuf.Timestamp\022,\n"
          + "\010end_time\030\003 \001(\0132\032.google.protobuf.Timestamp*=\n"
          + "\013StorageType\022\034\n"
          + "\030STORAGE_TYPE_UNSPECIFIED\020\000\022\007\n"
          + "\003SSD\020\001\022\007\n"
          + "\003HDD\020\002B\316\001\n"
          + "\034com.google.bigtable.admin.v2B\013CommonProtoP\001Z8cloud.g"
          + "oogle.com/go/bigtable/admin/apiv2/adminp"
          + "b;adminpb\252\002\036Google.Cloud.Bigtable.Admin."
          + "V2\312\002\036Google\\Cloud\\Bigtable\\Admin\\V2\352\002\"Go"
          + "ogle::Cloud::Bigtable::Admin::V2b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_bigtable_admin_v2_OperationProgress_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_admin_v2_OperationProgress_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_OperationProgress_descriptor,
            new java.lang.String[] {
              "ProgressPercent", "StartTime", "EndTime",
            });
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
