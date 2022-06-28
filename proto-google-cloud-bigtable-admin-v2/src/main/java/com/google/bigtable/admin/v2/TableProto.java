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
      internal_static_google_bigtable_admin_v2_EncryptionInfo_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_EncryptionInfo_fieldAccessorTable;
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
          + "ogle/protobuf/timestamp.proto\032\027google/rp"
          + "c/status.proto\"\233\001\n\013RestoreInfo\022@\n\013source"
          + "_type\030\001 \001(\0162+.google.bigtable.admin.v2.R"
          + "estoreSourceType\022;\n\013backup_info\030\002 \001(\0132$."
          + "google.bigtable.admin.v2.BackupInfoH\000B\r\n"
          + "\013source_info\"\361\010\n\005Table\022\014\n\004name\030\001 \001(\t\022O\n\016"
          + "cluster_states\030\002 \003(\01322.google.bigtable.a"
          + "dmin.v2.Table.ClusterStatesEntryB\003\340A\003\022L\n"
          + "\017column_families\030\003 \003(\01323.google.bigtable"
          + ".admin.v2.Table.ColumnFamiliesEntry\022N\n\013g"
          + "ranularity\030\004 \001(\01624.google.bigtable.admin"
          + ".v2.Table.TimestampGranularityB\003\340A\005\022@\n\014r"
          + "estore_info\030\006 \001(\0132%.google.bigtable.admi"
          + "n.v2.RestoreInfoB\003\340A\003\032\306\002\n\014ClusterState\022]"
          + "\n\021replication_state\030\001 \001(\0162=.google.bigta"
          + "ble.admin.v2.Table.ClusterState.Replicat"
          + "ionStateB\003\340A\003\022F\n\017encryption_info\030\002 \003(\0132("
          + ".google.bigtable.admin.v2.EncryptionInfo"
          + "B\003\340A\003\"\216\001\n\020ReplicationState\022\023\n\017STATE_NOT_"
          + "KNOWN\020\000\022\020\n\014INITIALIZING\020\001\022\027\n\023PLANNED_MAI"
          + "NTENANCE\020\002\022\031\n\025UNPLANNED_MAINTENANCE\020\003\022\t\n"
          + "\005READY\020\004\022\024\n\020READY_OPTIMIZING\020\005\032b\n\022Cluste"
          + "rStatesEntry\022\013\n\003key\030\001 \001(\t\022;\n\005value\030\002 \001(\013"
          + "2,.google.bigtable.admin.v2.Table.Cluste"
          + "rState:\0028\001\032]\n\023ColumnFamiliesEntry\022\013\n\003key"
          + "\030\001 \001(\t\0225\n\005value\030\002 \001(\0132&.google.bigtable."
          + "admin.v2.ColumnFamily:\0028\001\"I\n\024TimestampGr"
          + "anularity\022%\n!TIMESTAMP_GRANULARITY_UNSPE"
          + "CIFIED\020\000\022\n\n\006MILLIS\020\001\"q\n\004View\022\024\n\020VIEW_UNS"
          + "PECIFIED\020\000\022\r\n\tNAME_ONLY\020\001\022\017\n\013SCHEMA_VIEW"
          + "\020\002\022\024\n\020REPLICATION_VIEW\020\003\022\023\n\017ENCRYPTION_V"
          + "IEW\020\005\022\010\n\004FULL\020\004:_\352A\\\n\"bigtableadmin.goog"
          + "leapis.com/Table\0226projects/{project}/ins"
          + "tances/{instance}/tables/{table}\"A\n\014Colu"
          + "mnFamily\0221\n\007gc_rule\030\001 \001(\0132 .google.bigta"
          + "ble.admin.v2.GcRule\"\325\002\n\006GcRule\022\032\n\020max_nu"
          + "m_versions\030\001 \001(\005H\000\022,\n\007max_age\030\002 \001(\0132\031.go"
          + "ogle.protobuf.DurationH\000\022E\n\014intersection"
          + "\030\003 \001(\0132-.google.bigtable.admin.v2.GcRule"
          + ".IntersectionH\000\0227\n\005union\030\004 \001(\0132&.google."
          + "bigtable.admin.v2.GcRule.UnionH\000\032?\n\014Inte"
          + "rsection\022/\n\005rules\030\001 \003(\0132 .google.bigtabl"
          + "e.admin.v2.GcRule\0328\n\005Union\022/\n\005rules\030\001 \003("
          + "\0132 .google.bigtable.admin.v2.GcRuleB\006\n\004r"
          + "ule\"\331\002\n\016EncryptionInfo\022U\n\017encryption_typ"
          + "e\030\003 \001(\01627.google.bigtable.admin.v2.Encry"
          + "ptionInfo.EncryptionTypeB\003\340A\003\0222\n\021encrypt"
          + "ion_status\030\004 \001(\0132\022.google.rpc.StatusB\003\340A"
          + "\003\022I\n\017kms_key_version\030\002 \001(\tB0\340A\003\372A*\n(clou"
          + "dkms.googleapis.com/CryptoKeyVersion\"q\n\016"
          + "EncryptionType\022\037\n\033ENCRYPTION_TYPE_UNSPEC"
          + "IFIED\020\000\022\035\n\031GOOGLE_DEFAULT_ENCRYPTION\020\001\022\037"
          + "\n\033CUSTOMER_MANAGED_ENCRYPTION\020\002\"\314\003\n\010Snap"
          + "shot\022\014\n\004name\030\001 \001(\t\0225\n\014source_table\030\002 \001(\013"
          + "2\037.google.bigtable.admin.v2.Table\022\027\n\017dat"
          + "a_size_bytes\030\003 \001(\003\022/\n\013create_time\030\004 \001(\0132"
          + "\032.google.protobuf.Timestamp\022/\n\013delete_ti"
          + "me\030\005 \001(\0132\032.google.protobuf.Timestamp\0227\n\005"
          + "state\030\006 \001(\0162(.google.bigtable.admin.v2.S"
          + "napshot.State\022\023\n\013description\030\007 \001(\t\"5\n\005St"
          + "ate\022\023\n\017STATE_NOT_KNOWN\020\000\022\t\n\005READY\020\001\022\014\n\010C"
          + "REATING\020\002:{\352Ax\n%bigtableadmin.googleapis"
          + ".com/Snapshot\022Oprojects/{project}/instan"
          + "ces/{instance}/clusters/{cluster}/snapsh"
          + "ots/{snapshot}\"\237\004\n\006Backup\022\014\n\004name\030\001 \001(\t\022"
          + "\034\n\014source_table\030\002 \001(\tB\006\340A\005\340A\002\0224\n\013expire_"
          + "time\030\003 \001(\0132\032.google.protobuf.TimestampB\003"
          + "\340A\002\0223\n\nstart_time\030\004 \001(\0132\032.google.protobu"
          + "f.TimestampB\003\340A\003\0221\n\010end_time\030\005 \001(\0132\032.goo"
          + "gle.protobuf.TimestampB\003\340A\003\022\027\n\nsize_byte"
          + "s\030\006 \001(\003B\003\340A\003\022:\n\005state\030\007 \001(\0162&.google.big"
          + "table.admin.v2.Backup.StateB\003\340A\003\022F\n\017encr"
          + "yption_info\030\t \001(\0132(.google.bigtable.admi"
          + "n.v2.EncryptionInfoB\003\340A\003\"7\n\005State\022\025\n\021STA"
          + "TE_UNSPECIFIED\020\000\022\014\n\010CREATING\020\001\022\t\n\005READY\020"
          + "\002:u\352Ar\n#bigtableadmin.googleapis.com/Bac"
          + "kup\022Kprojects/{project}/instances/{insta"
          + "nce}/clusters/{cluster}/backups/{backup}"
          + "\"\244\001\n\nBackupInfo\022\023\n\006backup\030\001 \001(\tB\003\340A\003\0223\n\n"
          + "start_time\030\002 \001(\0132\032.google.protobuf.Times"
          + "tampB\003\340A\003\0221\n\010end_time\030\003 \001(\0132\032.google.pro"
          + "tobuf.TimestampB\003\340A\003\022\031\n\014source_table\030\004 \001"
          + "(\tB\003\340A\003*D\n\021RestoreSourceType\022#\n\037RESTORE_"
          + "SOURCE_TYPE_UNSPECIFIED\020\000\022\n\n\006BACKUP\020\001B\374\002"
          + "\n\034com.google.bigtable.admin.v2B\nTablePro"
          + "toP\001Z=google.golang.org/genproto/googlea"
          + "pis/bigtable/admin/v2;admin\252\002\036Google.Clo"
          + "ud.Bigtable.Admin.V2\312\002\036Google\\Cloud\\Bigt"
          + "able\\Admin\\V2\352\002\"Google::Cloud::Bigtable:"
          + ":Admin::V2\352A\246\001\n(cloudkms.googleapis.com/"
          + "CryptoKeyVersion\022zprojects/{project}/loc"
          + "ations/{location}/keyRings/{key_ring}/cr"
          + "yptoKeys/{crypto_key}/cryptoKeyVersions/"
          + "{crypto_key_version}b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.api.FieldBehaviorProto.getDescriptor(),
              com.google.api.ResourceProto.getDescriptor(),
              com.google.protobuf.DurationProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
              com.google.rpc.StatusProto.getDescriptor(),
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
              "ReplicationState", "EncryptionInfo",
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
    internal_static_google_bigtable_admin_v2_EncryptionInfo_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_google_bigtable_admin_v2_EncryptionInfo_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_EncryptionInfo_descriptor,
            new java.lang.String[] {
              "EncryptionType", "EncryptionStatus", "KmsKeyVersion",
            });
    internal_static_google_bigtable_admin_v2_Snapshot_descriptor =
        getDescriptor().getMessageTypes().get(5);
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
        getDescriptor().getMessageTypes().get(6);
    internal_static_google_bigtable_admin_v2_Backup_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_Backup_descriptor,
            new java.lang.String[] {
              "Name",
              "SourceTable",
              "ExpireTime",
              "StartTime",
              "EndTime",
              "SizeBytes",
              "State",
              "EncryptionInfo",
            });
    internal_static_google_bigtable_admin_v2_BackupInfo_descriptor =
        getDescriptor().getMessageTypes().get(7);
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
    registry.add(com.google.api.ResourceProto.resourceDefinition);
    registry.add(com.google.api.ResourceProto.resourceReference);
    com.google.protobuf.Descriptors.FileDescriptor.internalUpdateFileDescriptor(
        descriptor, registry);
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.protobuf.DurationProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
    com.google.rpc.StatusProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
