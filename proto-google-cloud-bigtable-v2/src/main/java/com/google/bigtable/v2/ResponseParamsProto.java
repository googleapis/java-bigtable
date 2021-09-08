/*
 * Copyright 2022 Google LLC
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
// source: google/bigtable/v2/response_params.proto

package com.google.bigtable.v2;

public final class ResponseParamsProto {
  private ResponseParamsProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ResponseParams_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ResponseParams_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n(google/bigtable/v2/response_params.pro"
          + "to\022\022google.bigtable.v2\"Z\n\016ResponseParams"
          + "\022\024\n\007zone_id\030\001 \001(\tH\000\210\001\001\022\027\n\ncluster_id\030\002 \001"
          + "(\tH\001\210\001\001B\n\n\010_zone_idB\r\n\013_cluster_idB\277\001\n\026c"
          + "om.google.bigtable.v2B\023ResponseParamsPro"
          + "toP\001Z:google.golang.org/genproto/googlea"
          + "pis/bigtable/v2;bigtable\252\002\030Google.Cloud."
          + "Bigtable.V2\312\002\030Google\\Cloud\\Bigtable\\V2\352\002"
          + "\033Google::Cloud::Bigtable::V2b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData, new com.google.protobuf.Descriptors.FileDescriptor[] {});
    internal_static_google_bigtable_v2_ResponseParams_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_v2_ResponseParams_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ResponseParams_descriptor,
            new java.lang.String[] {
              "ZoneId", "ClusterId", "ZoneId", "ClusterId",
            });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
