// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/admin/v2/instance.proto

// Protobuf Java Version: 3.25.2
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
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_AppProfile_StandardIsolation_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_AppProfile_StandardIsolation_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_AppProfile_DataBoostIsolationReadOnly_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_AppProfile_DataBoostIsolationReadOnly_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_admin_v2_HotTablet_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_HotTablet_fieldAccessorTable;

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
      ".proto\032\037google/protobuf/timestamp.proto\"" +
      "\306\004\n\010Instance\022\014\n\004name\030\001 \001(\t\022\031\n\014display_na" +
      "me\030\002 \001(\tB\003\340A\002\0227\n\005state\030\003 \001(\0162(.google.bi" +
      "gtable.admin.v2.Instance.State\0225\n\004type\030\004" +
      " \001(\0162\'.google.bigtable.admin.v2.Instance" +
      ".Type\022>\n\006labels\030\005 \003(\0132..google.bigtable." +
      "admin.v2.Instance.LabelsEntry\0224\n\013create_" +
      "time\030\007 \001(\0132\032.google.protobuf.TimestampB\003" +
      "\340A\003\022\037\n\rsatisfies_pzs\030\010 \001(\010B\003\340A\003H\000\210\001\001\032-\n\013" +
      "LabelsEntry\022\013\n\003key\030\001 \001(\t\022\r\n\005value\030\002 \001(\t:" +
      "\0028\001\"5\n\005State\022\023\n\017STATE_NOT_KNOWN\020\000\022\t\n\005REA" +
      "DY\020\001\022\014\n\010CREATING\020\002\"=\n\004Type\022\024\n\020TYPE_UNSPE" +
      "CIFIED\020\000\022\016\n\nPRODUCTION\020\001\022\017\n\013DEVELOPMENT\020" +
      "\002:S\352AP\n%bigtableadmin.googleapis.com/Ins" +
      "tance\022\'projects/{project}/instances/{ins" +
      "tance}B\020\n\016_satisfies_pzs\"_\n\022AutoscalingT" +
      "argets\022\037\n\027cpu_utilization_percent\030\002 \001(\005\022" +
      "(\n storage_utilization_gib_per_node\030\003 \001(" +
      "\005\"O\n\021AutoscalingLimits\022\034\n\017min_serve_node" +
      "s\030\001 \001(\005B\003\340A\002\022\034\n\017max_serve_nodes\030\002 \001(\005B\003\340" +
      "A\002\"\321\007\n\007Cluster\022\014\n\004name\030\001 \001(\t\022;\n\010location" +
      "\030\002 \001(\tB)\340A\005\372A#\n!locations.googleapis.com" +
      "/Location\022;\n\005state\030\003 \001(\0162\'.google.bigtab" +
      "le.admin.v2.Cluster.StateB\003\340A\003\022\023\n\013serve_" +
      "nodes\030\004 \001(\005\022I\n\016cluster_config\030\007 \001(\0132/.go" +
      "ogle.bigtable.admin.v2.Cluster.ClusterCo" +
      "nfigH\000\022H\n\024default_storage_type\030\005 \001(\0162%.g" +
      "oogle.bigtable.admin.v2.StorageTypeB\003\340A\005" +
      "\022R\n\021encryption_config\030\006 \001(\01322.google.big" +
      "table.admin.v2.Cluster.EncryptionConfigB" +
      "\003\340A\005\032\270\001\n\030ClusterAutoscalingConfig\022L\n\022aut" +
      "oscaling_limits\030\001 \001(\0132+.google.bigtable." +
      "admin.v2.AutoscalingLimitsB\003\340A\002\022N\n\023autos" +
      "caling_targets\030\002 \001(\0132,.google.bigtable.a" +
      "dmin.v2.AutoscalingTargetsB\003\340A\002\032o\n\rClust" +
      "erConfig\022^\n\032cluster_autoscaling_config\030\001" +
      " \001(\0132:.google.bigtable.admin.v2.Cluster." +
      "ClusterAutoscalingConfig\032P\n\020EncryptionCo" +
      "nfig\022<\n\014kms_key_name\030\001 \001(\tB&\372A#\n!cloudkm" +
      "s.googleapis.com/CryptoKey\"Q\n\005State\022\023\n\017S" +
      "TATE_NOT_KNOWN\020\000\022\t\n\005READY\020\001\022\014\n\010CREATING\020" +
      "\002\022\014\n\010RESIZING\020\003\022\014\n\010DISABLED\020\004:e\352Ab\n$bigt" +
      "ableadmin.googleapis.com/Cluster\022:projec" +
      "ts/{project}/instances/{instance}/cluste" +
      "rs/{cluster}B\010\n\006config\"\322\t\n\nAppProfile\022\014\n" +
      "\004name\030\001 \001(\t\022\014\n\004etag\030\002 \001(\t\022\023\n\013description" +
      "\030\003 \001(\t\022g\n\035multi_cluster_routing_use_any\030" +
      "\005 \001(\0132>.google.bigtable.admin.v2.AppProf" +
      "ile.MultiClusterRoutingUseAnyH\000\022[\n\026singl" +
      "e_cluster_routing\030\006 \001(\01329.google.bigtabl" +
      "e.admin.v2.AppProfile.SingleClusterRouti" +
      "ngH\000\022E\n\010priority\030\007 \001(\0162-.google.bigtable" +
      ".admin.v2.AppProfile.PriorityB\002\030\001H\001\022T\n\022s" +
      "tandard_isolation\030\013 \001(\01326.google.bigtabl" +
      "e.admin.v2.AppProfile.StandardIsolationH" +
      "\001\022i\n\036data_boost_isolation_read_only\030\n \001(" +
      "\0132?.google.bigtable.admin.v2.AppProfile." +
      "DataBoostIsolationReadOnlyH\001\0320\n\031MultiClu" +
      "sterRoutingUseAny\022\023\n\013cluster_ids\030\001 \003(\t\032N" +
      "\n\024SingleClusterRouting\022\022\n\ncluster_id\030\001 \001" +
      "(\t\022\"\n\032allow_transactional_writes\030\002 \001(\010\032T" +
      "\n\021StandardIsolation\022?\n\010priority\030\001 \001(\0162-." +
      "google.bigtable.admin.v2.AppProfile.Prio" +
      "rity\032\374\001\n\032DataBoostIsolationReadOnly\022w\n\025c" +
      "ompute_billing_owner\030\001 \001(\0162S.google.bigt" +
      "able.admin.v2.AppProfile.DataBoostIsolat" +
      "ionReadOnly.ComputeBillingOwnerH\000\210\001\001\"K\n\023" +
      "ComputeBillingOwner\022%\n!COMPUTE_BILLING_O" +
      "WNER_UNSPECIFIED\020\000\022\r\n\tHOST_PAYS\020\001B\030\n\026_co" +
      "mpute_billing_owner\"^\n\010Priority\022\030\n\024PRIOR" +
      "ITY_UNSPECIFIED\020\000\022\020\n\014PRIORITY_LOW\020\001\022\023\n\017P" +
      "RIORITY_MEDIUM\020\002\022\021\n\rPRIORITY_HIGH\020\003:o\352Al" +
      "\n\'bigtableadmin.googleapis.com/AppProfil" +
      "e\022Aprojects/{project}/instances/{instanc" +
      "e}/appProfiles/{app_profile}B\020\n\016routing_" +
      "policyB\013\n\tisolation\"\210\003\n\tHotTablet\022\014\n\004nam" +
      "e\030\001 \001(\t\022;\n\ntable_name\030\002 \001(\tB\'\372A$\n\"bigtab" +
      "leadmin.googleapis.com/Table\0223\n\nstart_ti" +
      "me\030\003 \001(\0132\032.google.protobuf.TimestampB\003\340A" +
      "\003\0221\n\010end_time\030\004 \001(\0132\032.google.protobuf.Ti" +
      "mestampB\003\340A\003\022\021\n\tstart_key\030\005 \001(\t\022\017\n\007end_k" +
      "ey\030\006 \001(\t\022#\n\026node_cpu_usage_percent\030\007 \001(\002" +
      "B\003\340A\003:\177\352A|\n&bigtableadmin.googleapis.com" +
      "/HotTablet\022Rprojects/{project}/instances" +
      "/{instance}/clusters/{cluster}/hotTablet" +
      "s/{hot_tablet}B\320\002\n\034com.google.bigtable.a" +
      "dmin.v2B\rInstanceProtoP\001Z=google.golang." +
      "org/genproto/googleapis/bigtable/admin/v" +
      "2;admin\252\002\036Google.Cloud.Bigtable.Admin.V2" +
      "\312\002\036Google\\Cloud\\Bigtable\\Admin\\V2\352\002\"Goog" +
      "le::Cloud::Bigtable::Admin::V2\352Ax\n!cloud" +
      "kms.googleapis.com/CryptoKey\022Sprojects/{" +
      "project}/locations/{location}/keyRings/{" +
      "key_ring}/cryptoKeys/{crypto_key}b\006proto" +
      "3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.api.FieldBehaviorProto.getDescriptor(),
          com.google.api.ResourceProto.getDescriptor(),
          com.google.bigtable.admin.v2.CommonProto.getDescriptor(),
          com.google.protobuf.TimestampProto.getDescriptor(),
        });
    internal_static_google_bigtable_admin_v2_Instance_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_admin_v2_Instance_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_Instance_descriptor,
        new java.lang.String[] { "Name", "DisplayName", "State", "Type", "Labels", "CreateTime", "SatisfiesPzs", });
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
        new java.lang.String[] { "CpuUtilizationPercent", "StorageUtilizationGibPerNode", });
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
        new java.lang.String[] { "Name", "Etag", "Description", "MultiClusterRoutingUseAny", "SingleClusterRouting", "Priority", "StandardIsolation", "DataBoostIsolationReadOnly", "RoutingPolicy", "Isolation", });
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
    internal_static_google_bigtable_admin_v2_AppProfile_StandardIsolation_descriptor =
      internal_static_google_bigtable_admin_v2_AppProfile_descriptor.getNestedTypes().get(2);
    internal_static_google_bigtable_admin_v2_AppProfile_StandardIsolation_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_AppProfile_StandardIsolation_descriptor,
        new java.lang.String[] { "Priority", });
    internal_static_google_bigtable_admin_v2_AppProfile_DataBoostIsolationReadOnly_descriptor =
      internal_static_google_bigtable_admin_v2_AppProfile_descriptor.getNestedTypes().get(3);
    internal_static_google_bigtable_admin_v2_AppProfile_DataBoostIsolationReadOnly_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_AppProfile_DataBoostIsolationReadOnly_descriptor,
        new java.lang.String[] { "ComputeBillingOwner", });
    internal_static_google_bigtable_admin_v2_HotTablet_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_google_bigtable_admin_v2_HotTablet_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_admin_v2_HotTablet_descriptor,
        new java.lang.String[] { "Name", "TableName", "StartTime", "EndTime", "StartKey", "EndKey", "NodeCpuUsagePercent", });
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
  }

  // @@protoc_insertion_point(outer_class_scope)
}
