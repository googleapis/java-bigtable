// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/v2/feature_flags.proto

package com.google.bigtable.v2;

public final class FeatureFlagsProto {
  private FeatureFlagsProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_FeatureFlags_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_FeatureFlags_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n&google/bigtable/v2/feature_flags.proto" +
      "\022\022google.bigtable.v2\"E\n\014FeatureFlags\022\025\n\r" +
      "reverse_scans\030\001 \001(\010\022\036\n\026mutate_rows_rate_" +
      "limit\030\003 \001(\010B\275\001\n\026com.google.bigtable.v2B\021" +
      "FeatureFlagsProtoP\001Z:google.golang.org/g" +
      "enproto/googleapis/bigtable/v2;bigtable\252" +
      "\002\030Google.Cloud.Bigtable.V2\312\002\030Google\\Clou" +
      "d\\Bigtable\\V2\352\002\033Google::Cloud::Bigtable:" +
      ":V2b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_google_bigtable_v2_FeatureFlags_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_v2_FeatureFlags_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_FeatureFlags_descriptor,
        new java.lang.String[] { "ReverseScans", "MutateRowsRateLimit", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
