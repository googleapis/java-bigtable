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
// source: google/bigtable/admin/v2/table.proto

package com.google.bigtable.admin.v2;

public final class TableProto {
  private TableProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_RestoreInfo_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_RestoreInfo_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_Table_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Table_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_Table_ClusterState_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Table_ClusterState_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_Table_ClusterStatesEntry_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Table_ClusterStatesEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_Table_ColumnFamiliesEntry_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Table_ColumnFamiliesEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_ColumnFamily_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_ColumnFamily_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_GcRule_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_GcRule_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_GcRule_Intersection_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_GcRule_Intersection_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_GcRule_Union_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_GcRule_Union_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_Snapshot_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Snapshot_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_Backup_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_Backup_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_BackupInfo_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_BackupInfo_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n$google/bigtable/admin/v2/table.proto\022\030"
          + "google.bigtable.admin.v2\032\037google/api/fie"
          + "ld_behavior.proto\032\031google/api/resource.p"
          + "roto\032\036google/protobuf/duration.proto\032\037go"
          + "ogle/protobuf/timestamp.proto\"\233\001\n\013Restor"
          + "eInfo\022@\n\013source_type\030\001 \001(\0162+.google.bigt"
          + "able.admin.v2.RestoreSourceType\022;\n\013backu"
          + "p_info\030\002 \001(\0132$.google.bigtable.admin.v2."
          + "BackupInfoH\000B\r\n\013source_info\"\373\007\n\005Table\022\014\n"
          + "\004name\030\001 \001(\t\022J\n\016cluster_states\030\002 \003(\01322.go"
          + "ogle.bigtable.admin.v2.Table.ClusterStat"
          + "esEntry\022L\n\017column_families\030\003 \003(\01323.googl"
          + "e.bigtable.admin.v2.Table.ColumnFamilies"
          + "Entry\022I\n\013granularity\030\004 \001(\01624.google.bigt"
          + "able.admin.v2.Table.TimestampGranularity"
          + "\022;\n\014restore_info\030\006 \001(\0132%.google.bigtable"
          + ".admin.v2.RestoreInfo\032\371\001\n\014ClusterState\022X"
          + "\n\021replication_state\030\001 \001(\0162=.google.bigta"
          + "ble.admin.v2.Table.ClusterState.Replicat"
          + "ionState\"\216\001\n\020ReplicationState\022\023\n\017STATE_N"
          + "OT_KNOWN\020\000\022\020\n\014INITIALIZING\020\001\022\027\n\023PLANNED_"
          + "MAINTENANCE\020\002\022\031\n\025UNPLANNED_MAINTENANCE\020\003"
          + "\022\t\n\005READY\020\004\022\024\n\020READY_OPTIMIZING\020\005\032b\n\022Clu"
          + "sterStatesEntry\022\013\n\003key\030\001 \001(\t\022;\n\005value\030\002 "
          + "\001(\0132,.google.bigtable.admin.v2.Table.Clu"
          + "sterState:\0028\001\032]\n\023ColumnFamiliesEntry\022\013\n\003"
          + "key\030\001 \001(\t\0225\n\005value\030\002 \001(\0132&.google.bigtab"
          + "le.admin.v2.ColumnFamily:\0028\001\"I\n\024Timestam"
          + "pGranularity\022%\n!TIMESTAMP_GRANULARITY_UN"
          + "SPECIFIED\020\000\022\n\n\006MILLIS\020\001\"\\\n\004View\022\024\n\020VIEW_"
          + "UNSPECIFIED\020\000\022\r\n\tNAME_ONLY\020\001\022\017\n\013SCHEMA_V"
          + "IEW\020\002\022\024\n\020REPLICATION_VIEW\020\003\022\010\n\004FULL\020\004:Z\352"
          + "AW\n\035bigtable.googleapis.com/Table\0226proje"
          + "cts/{project}/instances/{instance}/table"
          + "s/{table}\"A\n\014ColumnFamily\0221\n\007gc_rule\030\001 \001"
          + "(\0132 .google.bigtable.admin.v2.GcRule\"\325\002\n"
          + "\006GcRule\022\032\n\020max_num_versions\030\001 \001(\005H\000\022,\n\007m"
          + "ax_age\030\002 \001(\0132\031.google.protobuf.DurationH"
          + "\000\022E\n\014intersection\030\003 \001(\0132-.google.bigtabl"
          + "e.admin.v2.GcRule.IntersectionH\000\0227\n\005unio"
          + "n\030\004 \001(\0132&.google.bigtable.admin.v2.GcRul"
          + "e.UnionH\000\032?\n\014Intersection\022/\n\005rules\030\001 \003(\013"
          + "2 .google.bigtable.admin.v2.GcRule\0328\n\005Un"
          + "ion\022/\n\005rules\030\001 \003(\0132 .google.bigtable.adm"
          + "in.v2.GcRuleB\006\n\004rule\"\307\003\n\010Snapshot\022\014\n\004nam"
          + "e\030\001 \001(\t\0225\n\014source_table\030\002 \001(\0132\037.google.b"
          + "igtable.admin.v2.Table\022\027\n\017data_size_byte"
          + "s\030\003 \001(\003\022/\n\013create_time\030\004 \001(\0132\032.google.pr"
          + "otobuf.Timestamp\022/\n\013delete_time\030\005 \001(\0132\032."
          + "google.protobuf.Timestamp\0227\n\005state\030\006 \001(\016"
          + "2(.google.bigtable.admin.v2.Snapshot.Sta"
          + "te\022\023\n\013description\030\007 \001(\t\"5\n\005State\022\023\n\017STAT"
          + "E_NOT_KNOWN\020\000\022\t\n\005READY\020\001\022\014\n\010CREATING\020\002:v"
          + "\352As\n bigtable.googleapis.com/Snapshot\022Op"
          + "rojects/{project}/instances/{instance}/c"
          + "lusters/{cluster}/snapshots/{snapshot}\"\327"
          + "\003\n\006Backup\022\021\n\004name\030\001 \001(\tB\003\340A\003\022\034\n\014source_t"
          + "able\030\002 \001(\tB\006\340A\005\340A\002\0224\n\013expire_time\030\003 \001(\0132"
          + "\032.google.protobuf.TimestampB\003\340A\002\0223\n\nstar"
          + "t_time\030\004 \001(\0132\032.google.protobuf.Timestamp"
          + "B\003\340A\003\0221\n\010end_time\030\005 \001(\0132\032.google.protobu"
          + "f.TimestampB\003\340A\003\022\027\n\nsize_bytes\030\006 \001(\003B\003\340A"
          + "\003\022:\n\005state\030\007 \001(\0162&.google.bigtable.admin"
          + ".v2.Backup.StateB\003\340A\003\"7\n\005State\022\025\n\021STATE_"
          + "UNSPECIFIED\020\000\022\014\n\010CREATING\020\001\022\t\n\005READY\020\002:p"
          + "\352Am\n\036bigtable.googleapis.com/Backup\022Kpro"
          + "jects/{project}/instances/{instance}/clu"
          + "sters/{cluster}/backups/{backup}\"\244\001\n\nBac"
          + "kupInfo\022\023\n\006backup\030\001 \001(\tB\003\340A\003\0223\n\nstart_ti"
          + "me\030\002 \001(\0132\032.google.protobuf.TimestampB\003\340A"
          + "\003\0221\n\010end_time\030\003 \001(\0132\032.google.protobuf.Ti"
          + "mestampB\003\340A\003\022\031\n\014source_table\030\004 \001(\tB\003\340A\003*"
          + "D\n\021RestoreSourceType\022#\n\037RESTORE_SOURCE_T"
          + "YPE_UNSPECIFIED\020\000\022\n\n\006BACKUP\020\001B\255\001\n\034com.go"
          + "ogle.bigtable.admin.v2B\nTableProtoP\001Z=go"
          + "ogle.golang.org/genproto/googleapis/bigt"
          + "able/admin/v2;admin\252\002\036Google.Cloud.Bigta"
          + "ble.Admin.V2\312\002\036Google\\Cloud\\Bigtable\\Adm"
          + "in\\V2b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.api.FieldBehaviorProto.getDescriptor(),
              com.google.api.ResourceProto.getDescriptor(),
              com.google.protobuf.DurationProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_bigtable_admin_v2_RestoreInfo_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_admin_v2_RestoreInfo_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_RestoreInfo_descriptor,
            new java.lang.String[] {
              "SourceType", "BackupInfo", "SourceInfo",
            });
    internal_static_google_bigtable_admin_v2_Table_descriptor =
        getDescriptor().getMessageTypes().get(1);
    internal_static_google_bigtable_admin_v2_Table_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_Table_descriptor,
            new java.lang.String[] {
              "Name", "ClusterStates", "ColumnFamilies", "Granularity", "RestoreInfo",
            });
    internal_static_google_bigtable_admin_v2_Table_ClusterState_descriptor =
        internal_static_google_bigtable_admin_v2_Table_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_admin_v2_Table_ClusterState_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_Table_ClusterState_descriptor,
            new java.lang.String[] {
              "ReplicationState",
            });
    internal_static_google_bigtable_admin_v2_Table_ClusterStatesEntry_descriptor =
        internal_static_google_bigtable_admin_v2_Table_descriptor.getNestedTypes().get(1);
    internal_static_google_bigtable_admin_v2_Table_ClusterStatesEntry_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_Table_ClusterStatesEntry_descriptor,
            new java.lang.String[] {
              "Key", "Value",
            });
    internal_static_google_bigtable_admin_v2_Table_ColumnFamiliesEntry_descriptor =
        internal_static_google_bigtable_admin_v2_Table_descriptor.getNestedTypes().get(2);
    internal_static_google_bigtable_admin_v2_Table_ColumnFamiliesEntry_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_Table_ColumnFamiliesEntry_descriptor,
            new java.lang.String[] {
              "Key", "Value",
            });
    internal_static_google_bigtable_admin_v2_ColumnFamily_descriptor =
        getDescriptor().getMessageTypes().get(2);
    internal_static_google_bigtable_admin_v2_ColumnFamily_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_ColumnFamily_descriptor,
            new java.lang.String[] {
              "GcRule",
            });
    internal_static_google_bigtable_admin_v2_GcRule_descriptor =
        getDescriptor().getMessageTypes().get(3);
    internal_static_google_bigtable_admin_v2_GcRule_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_GcRule_descriptor,
            new java.lang.String[] {
              "MaxNumVersions", "MaxAge", "Intersection", "Union", "Rule",
            });
    internal_static_google_bigtable_admin_v2_GcRule_Intersection_descriptor =
        internal_static_google_bigtable_admin_v2_GcRule_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_admin_v2_GcRule_Intersection_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_GcRule_Intersection_descriptor,
            new java.lang.String[] {
              "Rules",
            });
    internal_static_google_bigtable_admin_v2_GcRule_Union_descriptor =
        internal_static_google_bigtable_admin_v2_GcRule_descriptor.getNestedTypes().get(1);
    internal_static_google_bigtable_admin_v2_GcRule_Union_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_GcRule_Union_descriptor,
            new java.lang.String[] {
              "Rules",
            });
    internal_static_google_bigtable_admin_v2_Snapshot_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_google_bigtable_admin_v2_Snapshot_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_Snapshot_descriptor,
            new java.lang.String[] {
              "Name",
              "SourceTable",
              "DataSizeBytes",
              "CreateTime",
              "DeleteTime",
              "State",
              "Description",
            });
    internal_static_google_bigtable_admin_v2_Backup_descriptor =
        getDescriptor().getMessageTypes().get(5);
    internal_static_google_bigtable_admin_v2_Backup_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_Backup_descriptor,
            new java.lang.String[] {
              "Name", "SourceTable", "ExpireTime", "StartTime", "EndTime", "SizeBytes", "State",
            });
    internal_static_google_bigtable_admin_v2_BackupInfo_descriptor =
        getDescriptor().getMessageTypes().get(6);
    internal_static_google_bigtable_admin_v2_BackupInfo_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_BackupInfo_descriptor,
            new java.lang.String[] {
              "Backup", "StartTime", "EndTime", "SourceTable",
            });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    registry.add(com.google.api.ResourceProto.resource);
    com.google.protobuf.Descriptors.FileDescriptor.internalUpdateFileDescriptor(
        descriptor, registry);
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.protobuf.DurationProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
