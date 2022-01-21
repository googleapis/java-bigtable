// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/admin/v2/instance.proto

package com.google.bigtable.admin.v2;

public final class InstanceProto {
  private InstanceProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_Instance_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Instance_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_Instance_LabelsEntry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Instance_LabelsEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_AutoscalingTargets_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_AutoscalingTargets_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_AutoscalingLimits_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_AutoscalingLimits_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_Cluster_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Cluster_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_Cluster_ClusterAutoscalingConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Cluster_ClusterAutoscalingConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_Cluster_ClusterConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Cluster_ClusterConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_Cluster_EncryptionConfig_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Cluster_EncryptionConfig_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_AppProfile_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_AppProfile_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_AppProfile_MultiClusterRoutingUseAny_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_AppProfile_MultiClusterRoutingUseAny_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_AppProfile_SingleClusterRouting_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_AppProfile_SingleClusterRouting_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\'google/bigtable/admin/v2/instance.prot" +
      "o\022\030google.bigtable.admin.v2\032\037google/api/" +
      "field_behavior.proto\032\031google/api/resourc" +
      "e.proto\032%google/bigtable/admin/v2/common" +
      ".proto\032\037google/protobuf/timestamp.proto\032" +
      "\034google/api/annotations.proto\"\230\004\n\010Instan" +
      "ce\022\021\n\004name\030\001 \001(\tB\003\340A\003\022\031\n\014display_name\030\002 " +
      "\001(\tB\003\340A\002\0227\n\005state\030\003 \001(\0162(.google.bigtabl" +
      "e.admin.v2.Instance.State\0225\n\004type\030\004 \001(\0162" +
      "\'.google.bigtable.admin.v2.Instance.Type" +
      "\022>\n\006labels\030\005 \003(\0132..google.bigtable.admin" +
      ".v2.Instance.LabelsEntry\0224\n\013create_time\030" +
      "\007 \001(\0132\032.google.protobuf.TimestampB\003\340A\003\032-" +
      "\n\013LabelsEntry\022\013\n\003key\030\001 \001(\t\022\r\n\005value\030\002 \001(" +
      "\t:\0028\001\"5\n\005State\022\023\n\017STATE_NOT_KNOWN\020\000\022\t\n\005R" +
      "EADY\020\001\022\014\n\010CREATING\020\002\"=\n\004Type\022\024\n\020TYPE_UNS" +
      "PECIFIED\020\000\022\016\n\nPRODUCTION\020\001\022\017\n\013DEVELOPMEN" +
      "T\020\002:S\352AP\n%bigtableadmin.googleapis.com/I" +
      "nstance\022\'projects/{project}/instances/{i" +
      "nstance}\"5\n\022AutoscalingTargets\022\037\n\027cpu_ut" +
      "ilization_percent\030\002 \001(\005\"O\n\021AutoscalingLi" +
      "mits\022\034\n\017min_serve_nodes\030\001 \001(\005B\003\340A\002\022\034\n\017ma" +
      "x_serve_nodes\030\002 \001(\005B\003\340A\002\"\316\007\n\007Cluster\022\021\n\004" +
      "name\030\001 \001(\tB\003\340A\003\0228\n\010location\030\002 \001(\tB&\372A#\n!" +
      "locations.googleapis.com/Location\022;\n\005sta" +
      "te\030\003 \001(\0162\'.google.bigtable.admin.v2.Clus" +
      "ter.StateB\003\340A\003\022\023\n\013serve_nodes\030\004 \001(\005\022I\n\016c" +
      "luster_config\030\007 \001(\0132/.google.bigtable.ad" +
      "min.v2.Cluster.ClusterConfigH\000\022C\n\024defaul" +
      "t_storage_type\030\005 \001(\0162%.google.bigtable.a" +
      "dmin.v2.StorageType\022R\n\021encryption_config" +
      "\030\006 \001(\01322.google.bigtable.admin.v2.Cluste" +
      "r.EncryptionConfigB\003\340A\005\032\270\001\n\030ClusterAutos" +
      "calingConfig\022L\n\022autoscaling_limits\030\001 \001(\013" +
      "2+.google.bigtable.admin.v2.AutoscalingL" +
      "imitsB\003\340A\002\022N\n\023autoscaling_targets\030\002 \001(\0132" +
      ",.google.bigtable.admin.v2.AutoscalingTa" +
      "rgetsB\003\340A\002\032o\n\rClusterConfig\022^\n\032cluster_a" +
      "utoscaling_config\030\001 \001(\0132:.google.bigtabl" +
      "e.admin.v2.Cluster.ClusterAutoscalingCon" +
      "fig\032P\n\020EncryptionConfig\022<\n\014kms_key_name\030" +
      "\001 \001(\tB&\372A#\n!cloudkms.googleapis.com/Cryp" +
      "toKey\"Q\n\005State\022\023\n\017STATE_NOT_KNOWN\020\000\022\t\n\005R" +
      "EADY\020\001\022\014\n\010CREATING\020\002\022\014\n\010RESIZING\020\003\022\014\n\010DI" +
      "SABLED\020\004:e\352Ab\n$bigtableadmin.googleapis." +
      "com/Cluster\022:projects/{project}/instance" +
      "s/{instance}/clusters/{cluster}B\010\n\006confi" +
      "g\"\210\004\n\nAppProfile\022\014\n\004name\030\001 \001(\t\022\014\n\004etag\030\002" +
      " \001(\t\022\023\n\013description\030\003 \001(\t\022g\n\035multi_clust" +
      "er_routing_use_any\030\005 \001(\0132>.google.bigtab" +
      "le.admin.v2.AppProfile.MultiClusterRouti" +
      "ngUseAnyH\000\022[\n\026single_cluster_routing\030\006 \001" +
      "(\01329.google.bigtable.admin.v2.AppProfile" +
      ".SingleClusterRoutingH\000\0320\n\031MultiClusterR" +
      "outingUseAny\022\023\n\013cluster_ids\030\001 \003(\t\032N\n\024Sin" +
      "gleClusterRouting\022\022\n\ncluster_id\030\001 \001(\t\022\"\n" +
      "\032allow_transactional_writes\030\002 \001(\010:o\352Al\n\'" +
      "bigtableadmin.googleapis.com/AppProfile\022" +
      "Aprojects/{project}/instances/{instance}" +
      "/appProfiles/{app_profile}B\020\n\016routing_po" +
      "licyB\320\002\n\034com.google.bigtable.admin.v2B\rI" +
      "nstanceProtoP\001Z=google.golang.org/genpro" +
      "to/googleapis/bigtable/admin/v2;admin\252\002\036" +
      "Google.Cloud.Bigtable.Admin.V2\312\002\036Google\\" +
      "Cloud\\Bigtable\\Admin\\V2\352\002\"Google::Cloud:" +
      ":Bigtable::Admin::V2\352Ax\n!cloudkms.google" +
      "apis.com/CryptoKey\022Sprojects/{project}/l" +
      "ocations/{location}/keyRings/{key_ring}/" +
      "cryptoKeys/{crypto_key}b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.api.FieldBehaviorProto.getDescriptor(),
          com.google.api.ResourceProto.getDescriptor(),
          com.google.bigtable.admin.v2.CommonProto.getDescriptor(),
          com.google.protobuf.TimestampProto.getDescriptor(),
          com.google.api.AnnotationsProto.getDescriptor(),
        });
    internal_static_google_bigtable_admin_v2_Instance_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_admin_v2_Instance_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_Instance_descriptor,
        new java.lang.String[] { "Name", "DisplayName", "State", "Type", "Labels", "CreateTime", });
    internal_static_google_bigtable_admin_v2_Instance_LabelsEntry_descriptor =
      internal_static_google_bigtable_admin_v2_Instance_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_admin_v2_Instance_LabelsEntry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_Instance_LabelsEntry_descriptor,
        new java.lang.String[] { "Key", "Value", });
    internal_static_google_bigtable_admin_v2_AutoscalingTargets_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_google_bigtable_admin_v2_AutoscalingTargets_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_AutoscalingTargets_descriptor,
        new java.lang.String[] { "CpuUtilizationPercent", });
    internal_static_google_bigtable_admin_v2_AutoscalingLimits_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_google_bigtable_admin_v2_AutoscalingLimits_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_AutoscalingLimits_descriptor,
        new java.lang.String[] { "MinServeNodes", "MaxServeNodes", });
    internal_static_google_bigtable_admin_v2_Cluster_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_google_bigtable_admin_v2_Cluster_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_Cluster_descriptor,
        new java.lang.String[] { "Name", "Location", "State", "ServeNodes", "ClusterConfig", "DefaultStorageType", "EncryptionConfig", "Config", });
    internal_static_google_bigtable_admin_v2_Cluster_ClusterAutoscalingConfig_descriptor =
      internal_static_google_bigtable_admin_v2_Cluster_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_admin_v2_Cluster_ClusterAutoscalingConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_Cluster_ClusterAutoscalingConfig_descriptor,
        new java.lang.String[] { "AutoscalingLimits", "AutoscalingTargets", });
    internal_static_google_bigtable_admin_v2_Cluster_ClusterConfig_descriptor =
      internal_static_google_bigtable_admin_v2_Cluster_descriptor.getNestedTypes().get(1);
    internal_static_google_bigtable_admin_v2_Cluster_ClusterConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_Cluster_ClusterConfig_descriptor,
        new java.lang.String[] { "ClusterAutoscalingConfig", });
    internal_static_google_bigtable_admin_v2_Cluster_EncryptionConfig_descriptor =
      internal_static_google_bigtable_admin_v2_Cluster_descriptor.getNestedTypes().get(2);
    internal_static_google_bigtable_admin_v2_Cluster_EncryptionConfig_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_Cluster_EncryptionConfig_descriptor,
        new java.lang.String[] { "KmsKeyName", });
    internal_static_google_bigtable_admin_v2_AppProfile_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_google_bigtable_admin_v2_AppProfile_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_AppProfile_descriptor,
        new java.lang.String[] { "Name", "Etag", "Description", "MultiClusterRoutingUseAny", "SingleClusterRouting", "RoutingPolicy", });
    internal_static_google_bigtable_admin_v2_AppProfile_MultiClusterRoutingUseAny_descriptor =
      internal_static_google_bigtable_admin_v2_AppProfile_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_admin_v2_AppProfile_MultiClusterRoutingUseAny_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_AppProfile_MultiClusterRoutingUseAny_descriptor,
        new java.lang.String[] { "ClusterIds", });
    internal_static_google_bigtable_admin_v2_AppProfile_SingleClusterRouting_descriptor =
      internal_static_google_bigtable_admin_v2_AppProfile_descriptor.getNestedTypes().get(1);
    internal_static_google_bigtable_admin_v2_AppProfile_SingleClusterRouting_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_AppProfile_SingleClusterRouting_descriptor,
        new java.lang.String[] { "ClusterId", "AllowTransactionalWrites", });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    registry.add(com.google.api.ResourceProto.resource);
    registry.add(com.google.api.ResourceProto.resourceDefinition);
    registry.add(com.google.api.ResourceProto.resourceReference);
    com.google.protobuf.Descriptors.FileDescriptor
        .internalUpdateFileDescriptor(descriptor, registry);
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.bigtable.admin.v2.CommonProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
    com.google.api.AnnotationsProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
