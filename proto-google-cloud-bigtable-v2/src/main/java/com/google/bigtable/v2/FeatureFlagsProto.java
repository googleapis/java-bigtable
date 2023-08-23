/*
 * Copyright 2023 Google LLC
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
// source: google/bigtable/v2/feature_flags.proto

package com.google.bigtable.v2;

public final class FeatureFlagsProto {
  private FeatureFlagsProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_FeatureFlags_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_FeatureFlags_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n&google/bigtable/v2/feature_flags.proto"
          + "\022\022google.bigtable.v2\"i\n\014FeatureFlags\022\025\n\r"
          + "reverse_scans\030\001 \001(\010\022\036\n\026mutate_rows_rate_"
          + "limit\030\003 \001(\010\022\"\n\032last_scanned_row_response"
          + "s\030\004 \001(\010B\275\001\n\026com.google.bigtable.v2B\021Feat"
          + "ureFlagsProtoP\001Z:google.golang.org/genpr"
          + "oto/googleapis/bigtable/v2;bigtable\252\002\030Go"
          + "ogle.Cloud.Bigtable.V2\312\002\030Google\\Cloud\\Bi"
          + "gtable\\V2\352\002\033Google::Cloud::Bigtable::V2b"
          + "\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData, new com.google.protobuf.Descriptors.FileDescriptor[] {});
    internal_static_google_bigtable_v2_FeatureFlags_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_v2_FeatureFlags_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_FeatureFlags_descriptor,
            new java.lang.String[] {
              "ReverseScans", "MutateRowsRateLimit", "LastScannedRowResponses",
            });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
