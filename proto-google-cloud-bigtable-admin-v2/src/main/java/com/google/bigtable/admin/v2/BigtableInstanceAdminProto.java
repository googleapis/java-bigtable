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

public final class BigtableInstanceAdminProto {
  private BigtableInstanceAdminProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_CreateInstanceRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_CreateInstanceRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_CreateInstanceRequest_ClustersEntry_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_CreateInstanceRequest_ClustersEntry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_GetInstanceRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_GetInstanceRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_ListInstancesRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_ListInstancesRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_ListInstancesResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_ListInstancesResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_PartialUpdateInstanceRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_PartialUpdateInstanceRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_DeleteInstanceRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_DeleteInstanceRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_CreateClusterRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_CreateClusterRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_GetClusterRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_GetClusterRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_ListClustersRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_ListClustersRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_ListClustersResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_ListClustersResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_DeleteClusterRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_DeleteClusterRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_CreateInstanceMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_CreateInstanceMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_UpdateInstanceMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_UpdateInstanceMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_CreateClusterMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_CreateClusterMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_UpdateClusterMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_UpdateClusterMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_PartialUpdateClusterMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_PartialUpdateClusterMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_PartialUpdateClusterRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_PartialUpdateClusterRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_CreateAppProfileRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_CreateAppProfileRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_GetAppProfileRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_GetAppProfileRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_ListAppProfilesRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_ListAppProfilesRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_ListAppProfilesResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_ListAppProfilesResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_UpdateAppProfileRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_UpdateAppProfileRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_DeleteAppProfileRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_DeleteAppProfileRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_UpdateAppProfileMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_UpdateAppProfileMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_ListHotTabletsRequest_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_ListHotTabletsRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_admin_v2_ListHotTabletsResponse_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_admin_v2_ListHotTabletsResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n6google/bigtable/admin/v2/bigtable_inst"
          + "ance_admin.proto\022\030google.bigtable.admin."
          + "v2\032\034google/api/annotations.proto\032\027google"
          + "/api/client.proto\032\037google/api/field_beha"
          + "vior.proto\032\031google/api/resource.proto\032\'g"
          + "oogle/bigtable/admin/v2/instance.proto\032\036"
          + "google/iam/v1/iam_policy.proto\032\032google/i"
          + "am/v1/policy.proto\032#google/longrunning/o"
          + "perations.proto\032\033google/protobuf/empty.p"
          + "roto\032 google/protobuf/field_mask.proto\032\037"
          + "google/protobuf/timestamp.proto\"\333\002\n\025Crea"
          + "teInstanceRequest\022C\n\006parent\030\001 \001(\tB3\340A\002\372A"
          + "-\n+cloudresourcemanager.googleapis.com/P"
          + "roject\022\030\n\013instance_id\030\002 \001(\tB\003\340A\002\0229\n\010inst"
          + "ance\030\003 \001(\0132\".google.bigtable.admin.v2.In"
          + "stanceB\003\340A\002\022T\n\010clusters\030\004 \003(\0132=.google.b"
          + "igtable.admin.v2.CreateInstanceRequest.C"
          + "lustersEntryB\003\340A\002\032R\n\rClustersEntry\022\013\n\003ke"
          + "y\030\001 \001(\t\0220\n\005value\030\002 \001(\0132!.google.bigtable"
          + ".admin.v2.Cluster:\0028\001\"Q\n\022GetInstanceRequ"
          + "est\022;\n\004name\030\001 \001(\tB-\340A\002\372A\'\n%bigtableadmin"
          + ".googleapis.com/Instance\"o\n\024ListInstance"
          + "sRequest\022C\n\006parent\030\001 \001(\tB3\340A\002\372A-\n+cloudr"
          + "esourcemanager.googleapis.com/Project\022\022\n"
          + "\npage_token\030\002 \001(\t\"\201\001\n\025ListInstancesRespo"
          + "nse\0225\n\tinstances\030\001 \003(\0132\".google.bigtable"
          + ".admin.v2.Instance\022\030\n\020failed_locations\030\002"
          + " \003(\t\022\027\n\017next_page_token\030\003 \001(\t\"\217\001\n\034Partia"
          + "lUpdateInstanceRequest\0229\n\010instance\030\001 \001(\013"
          + "2\".google.bigtable.admin.v2.InstanceB\003\340A"
          + "\002\0224\n\013update_mask\030\002 \001(\0132\032.google.protobuf"
          + ".FieldMaskB\003\340A\002\"T\n\025DeleteInstanceRequest"
          + "\022;\n\004name\030\001 \001(\tB-\340A\002\372A\'\n%bigtableadmin.go"
          + "ogleapis.com/Instance\"\247\001\n\024CreateClusterR"
          + "equest\022=\n\006parent\030\001 \001(\tB-\340A\002\372A\'\n%bigtable"
          + "admin.googleapis.com/Instance\022\027\n\ncluster"
          + "_id\030\002 \001(\tB\003\340A\002\0227\n\007cluster\030\003 \001(\0132!.google"
          + ".bigtable.admin.v2.ClusterB\003\340A\002\"O\n\021GetCl"
          + "usterRequest\022:\n\004name\030\001 \001(\tB,\340A\002\372A&\n$bigt"
          + "ableadmin.googleapis.com/Cluster\"h\n\023List"
          + "ClustersRequest\022=\n\006parent\030\001 \001(\tB-\340A\002\372A\'\n"
          + "%bigtableadmin.googleapis.com/Instance\022\022"
          + "\n\npage_token\030\002 \001(\t\"~\n\024ListClustersRespon"
          + "se\0223\n\010clusters\030\001 \003(\0132!.google.bigtable.a"
          + "dmin.v2.Cluster\022\030\n\020failed_locations\030\002 \003("
          + "\t\022\027\n\017next_page_token\030\003 \001(\t\"R\n\024DeleteClus"
          + "terRequest\022:\n\004name\030\001 \001(\tB,\340A\002\372A&\n$bigtab"
          + "leadmin.googleapis.com/Cluster\"\306\001\n\026Creat"
          + "eInstanceMetadata\022I\n\020original_request\030\001 "
          + "\001(\0132/.google.bigtable.admin.v2.CreateIns"
          + "tanceRequest\0220\n\014request_time\030\002 \001(\0132\032.goo"
          + "gle.protobuf.Timestamp\022/\n\013finish_time\030\003 "
          + "\001(\0132\032.google.protobuf.Timestamp\"\315\001\n\026Upda"
          + "teInstanceMetadata\022P\n\020original_request\030\001"
          + " \001(\01326.google.bigtable.admin.v2.PartialU"
          + "pdateInstanceRequest\0220\n\014request_time\030\002 \001"
          + "(\0132\032.google.protobuf.Timestamp\022/\n\013finish"
          + "_time\030\003 \001(\0132\032.google.protobuf.Timestamp\""
          + "\304\001\n\025CreateClusterMetadata\022H\n\020original_re"
          + "quest\030\001 \001(\0132..google.bigtable.admin.v2.C"
          + "reateClusterRequest\0220\n\014request_time\030\002 \001("
          + "\0132\032.google.protobuf.Timestamp\022/\n\013finish_"
          + "time\030\003 \001(\0132\032.google.protobuf.Timestamp\"\267"
          + "\001\n\025UpdateClusterMetadata\022;\n\020original_req"
          + "uest\030\001 \001(\0132!.google.bigtable.admin.v2.Cl"
          + "uster\0220\n\014request_time\030\002 \001(\0132\032.google.pro"
          + "tobuf.Timestamp\022/\n\013finish_time\030\003 \001(\0132\032.g"
          + "oogle.protobuf.Timestamp\"\322\001\n\034PartialUpda"
          + "teClusterMetadata\0220\n\014request_time\030\001 \001(\0132"
          + "\032.google.protobuf.Timestamp\022/\n\013finish_ti"
          + "me\030\002 \001(\0132\032.google.protobuf.Timestamp\022O\n\020"
          + "original_request\030\003 \001(\01325.google.bigtable"
          + ".admin.v2.PartialUpdateClusterRequest\"\214\001"
          + "\n\033PartialUpdateClusterRequest\0227\n\007cluster"
          + "\030\001 \001(\0132!.google.bigtable.admin.v2.Cluste"
          + "rB\003\340A\002\0224\n\013update_mask\030\002 \001(\0132\032.google.pro"
          + "tobuf.FieldMaskB\003\340A\002\"\316\001\n\027CreateAppProfil"
          + "eRequest\022=\n\006parent\030\001 \001(\tB-\340A\002\372A\'\n%bigtab"
          + "leadmin.googleapis.com/Instance\022\033\n\016app_p"
          + "rofile_id\030\002 \001(\tB\003\340A\002\022>\n\013app_profile\030\003 \001("
          + "\0132$.google.bigtable.admin.v2.AppProfileB"
          + "\003\340A\002\022\027\n\017ignore_warnings\030\004 \001(\010\"U\n\024GetAppP"
          + "rofileRequest\022=\n\004name\030\001 \001(\tB/\340A\002\372A)\n\'big"
          + "tableadmin.googleapis.com/AppProfile\"~\n\026"
          + "ListAppProfilesRequest\022=\n\006parent\030\001 \001(\tB-"
          + "\340A\002\372A\'\n%bigtableadmin.googleapis.com/Ins"
          + "tance\022\021\n\tpage_size\030\003 \001(\005\022\022\n\npage_token\030\002"
          + " \001(\t\"\210\001\n\027ListAppProfilesResponse\022:\n\014app_"
          + "profiles\030\001 \003(\0132$.google.bigtable.admin.v"
          + "2.AppProfile\022\027\n\017next_page_token\030\002 \001(\t\022\030\n"
          + "\020failed_locations\030\003 \003(\t\"\250\001\n\027UpdateAppPro"
          + "fileRequest\022>\n\013app_profile\030\001 \001(\0132$.googl"
          + "e.bigtable.admin.v2.AppProfileB\003\340A\002\0224\n\013u"
          + "pdate_mask\030\002 \001(\0132\032.google.protobuf.Field"
          + "MaskB\003\340A\002\022\027\n\017ignore_warnings\030\003 \001(\010\"v\n\027De"
          + "leteAppProfileRequest\022=\n\004name\030\001 \001(\tB/\340A\002"
          + "\372A)\n\'bigtableadmin.googleapis.com/AppPro"
          + "file\022\034\n\017ignore_warnings\030\002 \001(\010B\003\340A\002\"\032\n\030Up"
          + "dateAppProfileMetadata\"\332\001\n\025ListHotTablet"
          + "sRequest\022<\n\006parent\030\001 \001(\tB,\340A\002\372A&\n$bigtab"
          + "leadmin.googleapis.com/Cluster\022.\n\nstart_"
          + "time\030\002 \001(\0132\032.google.protobuf.Timestamp\022,"
          + "\n\010end_time\030\003 \001(\0132\032.google.protobuf.Times"
          + "tamp\022\021\n\tpage_size\030\004 \001(\005\022\022\n\npage_token\030\005 "
          + "\001(\t\"k\n\026ListHotTabletsResponse\0228\n\013hot_tab"
          + "lets\030\001 \003(\0132#.google.bigtable.admin.v2.Ho"
          + "tTablet\022\027\n\017next_page_token\030\002 \001(\t2\313!\n\025Big"
          + "tableInstanceAdmin\022\332\001\n\016CreateInstance\022/."
          + "google.bigtable.admin.v2.CreateInstanceR"
          + "equest\032\035.google.longrunning.Operation\"x\202"
          + "\323\344\223\002&\"!/v2/{parent=projects/*}/instances"
          + ":\001*\332A$parent,instance_id,instance,cluste"
          + "rs\312A\"\n\010Instance\022\026CreateInstanceMetadata\022"
          + "\221\001\n\013GetInstance\022,.google.bigtable.admin."
          + "v2.GetInstanceRequest\032\".google.bigtable."
          + "admin.v2.Instance\"0\202\323\344\223\002#\022!/v2/{name=pro"
          + "jects/*/instances/*}\332A\004name\022\244\001\n\rListInst"
          + "ances\022..google.bigtable.admin.v2.ListIns"
          + "tancesRequest\032/.google.bigtable.admin.v2"
          + ".ListInstancesResponse\"2\202\323\344\223\002#\022!/v2/{par"
          + "ent=projects/*}/instances\332A\006parent\022\206\001\n\016U"
          + "pdateInstance\022\".google.bigtable.admin.v2"
          + ".Instance\032\".google.bigtable.admin.v2.Ins"
          + "tance\",\202\323\344\223\002&\032!/v2/{name=projects/*/inst"
          + "ances/*}:\001*\022\350\001\n\025PartialUpdateInstance\0226."
          + "google.bigtable.admin.v2.PartialUpdateIn"
          + "stanceRequest\032\035.google.longrunning.Opera"
          + "tion\"x\202\323\344\223\00262*/v2/{instance.name=project"
          + "s/*/instances/*}:\010instance\332A\024instance,up"
          + "date_mask\312A\"\n\010Instance\022\026UpdateInstanceMe"
          + "tadata\022\213\001\n\016DeleteInstance\022/.google.bigta"
          + "ble.admin.v2.DeleteInstanceRequest\032\026.goo"
          + "gle.protobuf.Empty\"0\202\323\344\223\002#*!/v2/{name=pr"
          + "ojects/*/instances/*}\332A\004name\022\334\001\n\rCreateC"
          + "luster\022..google.bigtable.admin.v2.Create"
          + "ClusterRequest\032\035.google.longrunning.Oper"
          + "ation\"|\202\323\344\223\0027\",/v2/{parent=projects/*/in"
          + "stances/*}/clusters:\007cluster\332A\031parent,cl"
          + "uster_id,cluster\312A \n\007Cluster\022\025CreateClus"
          + "terMetadata\022\231\001\n\nGetCluster\022+.google.bigt"
          + "able.admin.v2.GetClusterRequest\032!.google"
          + ".bigtable.admin.v2.Cluster\";\202\323\344\223\002.\022,/v2/"
          + "{name=projects/*/instances/*/clusters/*}"
          + "\332A\004name\022\254\001\n\014ListClusters\022-.google.bigtab"
          + "le.admin.v2.ListClustersRequest\032..google"
          + ".bigtable.admin.v2.ListClustersResponse\""
          + "=\202\323\344\223\002.\022,/v2/{parent=projects/*/instance"
          + "s/*}/clusters\332A\006parent\022\255\001\n\rUpdateCluster"
          + "\022!.google.bigtable.admin.v2.Cluster\032\035.go"
          + "ogle.longrunning.Operation\"Z\202\323\344\223\0021\032,/v2/"
          + "{name=projects/*/instances/*/clusters/*}"
          + ":\001*\312A \n\007Cluster\022\025UpdateClusterMetadata\022\364"
          + "\001\n\024PartialUpdateCluster\0225.google.bigtabl"
          + "e.admin.v2.PartialUpdateClusterRequest\032\035"
          + ".google.longrunning.Operation\"\205\001\202\323\344\223\002?24"
          + "/v2/{cluster.name=projects/*/instances/*"
          + "/clusters/*}:\007cluster\332A\023cluster,update_m"
          + "ask\312A\'\n\007Cluster\022\034PartialUpdateClusterMet"
          + "adata\022\224\001\n\rDeleteCluster\022..google.bigtabl"
          + "e.admin.v2.DeleteClusterRequest\032\026.google"
          + ".protobuf.Empty\";\202\323\344\223\002.*,/v2/{name=proje"
          + "cts/*/instances/*/clusters/*}\332A\004name\022\325\001\n"
          + "\020CreateAppProfile\0221.google.bigtable.admi"
          + "n.v2.CreateAppProfileRequest\032$.google.bi"
          + "gtable.admin.v2.AppProfile\"h\202\323\344\223\002>\"//v2/"
          + "{parent=projects/*/instances/*}/appProfi"
          + "les:\013app_profile\332A!parent,app_profile_id"
          + ",app_profile\022\245\001\n\rGetAppProfile\022..google."
          + "bigtable.admin.v2.GetAppProfileRequest\032$"
          + ".google.bigtable.admin.v2.AppProfile\">\202\323"
          + "\344\223\0021\022//v2/{name=projects/*/instances/*/a"
          + "ppProfiles/*}\332A\004name\022\270\001\n\017ListAppProfiles"
          + "\0220.google.bigtable.admin.v2.ListAppProfi"
          + "lesRequest\0321.google.bigtable.admin.v2.Li"
          + "stAppProfilesResponse\"@\202\323\344\223\0021\022//v2/{pare"
          + "nt=projects/*/instances/*}/appProfiles\332A"
          + "\006parent\022\372\001\n\020UpdateAppProfile\0221.google.bi"
          + "gtable.admin.v2.UpdateAppProfileRequest\032"
          + "\035.google.longrunning.Operation\"\223\001\202\323\344\223\002J2"
          + ";/v2/{app_profile.name=projects/*/instan"
          + "ces/*/appProfiles/*}:\013app_profile\332A\027app_"
          + "profile,update_mask\312A&\n\nAppProfile\022\030Upda"
          + "teAppProfileMetadata\022\235\001\n\020DeleteAppProfil"
          + "e\0221.google.bigtable.admin.v2.DeleteAppPr"
          + "ofileRequest\032\026.google.protobuf.Empty\">\202\323"
          + "\344\223\0021*//v2/{name=projects/*/instances/*/a"
          + "ppProfiles/*}\332A\004name\022\223\001\n\014GetIamPolicy\022\"."
          + "google.iam.v1.GetIamPolicyRequest\032\025.goog"
          + "le.iam.v1.Policy\"H\202\323\344\223\0027\"2/v2/{resource="
          + "projects/*/instances/*}:getIamPolicy:\001*\332"
          + "A\010resource\022\232\001\n\014SetIamPolicy\022\".google.iam"
          + ".v1.SetIamPolicyRequest\032\025.google.iam.v1."
          + "Policy\"O\202\323\344\223\0027\"2/v2/{resource=projects/*"
          + "/instances/*}:setIamPolicy:\001*\332A\017resource"
          + ",policy\022\305\001\n\022TestIamPermissions\022(.google."
          + "iam.v1.TestIamPermissionsRequest\032).googl"
          + "e.iam.v1.TestIamPermissionsResponse\"Z\202\323\344"
          + "\223\002=\"8/v2/{resource=projects/*/instances/"
          + "*}:testIamPermissions:\001*\332A\024resource,perm"
          + "issions\022\277\001\n\016ListHotTablets\022/.google.bigt"
          + "able.admin.v2.ListHotTabletsRequest\0320.go"
          + "ogle.bigtable.admin.v2.ListHotTabletsRes"
          + "ponse\"J\202\323\344\223\002;\0229/v2/{parent=projects/*/in"
          + "stances/*/clusters/*}/hotTablets\332A\006paren"
          + "t\032\232\003\312A\034bigtableadmin.googleapis.com\322A\367\002h"
          + "ttps://www.googleapis.com/auth/bigtable."
          + "admin,https://www.googleapis.com/auth/bi"
          + "gtable.admin.cluster,https://www.googlea"
          + "pis.com/auth/bigtable.admin.instance,htt"
          + "ps://www.googleapis.com/auth/cloud-bigta"
          + "ble.admin,https://www.googleapis.com/aut"
          + "h/cloud-bigtable.admin.cluster,https://w"
          + "ww.googleapis.com/auth/cloud-platform,ht"
          + "tps://www.googleapis.com/auth/cloud-plat"
          + "form.read-onlyB\342\001\n\034com.google.bigtable.a"
          + "dmin.v2B\032BigtableInstanceAdminProtoP\001Z=g"
          + "oogle.golang.org/genproto/googleapis/big"
          + "table/admin/v2;admin\252\002\036Google.Cloud.Bigt"
          + "able.Admin.V2\312\002\036Google\\Cloud\\Bigtable\\Ad"
          + "min\\V2\352\002\"Google::Cloud::Bigtable::Admin:"
          + ":V2b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.api.AnnotationsProto.getDescriptor(),
              com.google.api.ClientProto.getDescriptor(),
              com.google.api.FieldBehaviorProto.getDescriptor(),
              com.google.api.ResourceProto.getDescriptor(),
              com.google.bigtable.admin.v2.InstanceProto.getDescriptor(),
              com.google.iam.v1.IamPolicyProto.getDescriptor(),
              com.google.iam.v1.PolicyProto.getDescriptor(),
              com.google.longrunning.OperationsProto.getDescriptor(),
              com.google.protobuf.EmptyProto.getDescriptor(),
              com.google.protobuf.FieldMaskProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
            });
    internal_static_google_bigtable_admin_v2_CreateInstanceRequest_descriptor =
        getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_admin_v2_CreateInstanceRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_CreateInstanceRequest_descriptor,
            new java.lang.String[] {
              "Parent", "InstanceId", "Instance", "Clusters",
            });
    internal_static_google_bigtable_admin_v2_CreateInstanceRequest_ClustersEntry_descriptor =
        internal_static_google_bigtable_admin_v2_CreateInstanceRequest_descriptor
            .getNestedTypes()
            .get(0);
    internal_static_google_bigtable_admin_v2_CreateInstanceRequest_ClustersEntry_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_CreateInstanceRequest_ClustersEntry_descriptor,
            new java.lang.String[] {
              "Key", "Value",
            });
    internal_static_google_bigtable_admin_v2_GetInstanceRequest_descriptor =
        getDescriptor().getMessageTypes().get(1);
    internal_static_google_bigtable_admin_v2_GetInstanceRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_GetInstanceRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_bigtable_admin_v2_ListInstancesRequest_descriptor =
        getDescriptor().getMessageTypes().get(2);
    internal_static_google_bigtable_admin_v2_ListInstancesRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_ListInstancesRequest_descriptor,
            new java.lang.String[] {
              "Parent", "PageToken",
            });
    internal_static_google_bigtable_admin_v2_ListInstancesResponse_descriptor =
        getDescriptor().getMessageTypes().get(3);
    internal_static_google_bigtable_admin_v2_ListInstancesResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_ListInstancesResponse_descriptor,
            new java.lang.String[] {
              "Instances", "FailedLocations", "NextPageToken",
            });
    internal_static_google_bigtable_admin_v2_PartialUpdateInstanceRequest_descriptor =
        getDescriptor().getMessageTypes().get(4);
    internal_static_google_bigtable_admin_v2_PartialUpdateInstanceRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_PartialUpdateInstanceRequest_descriptor,
            new java.lang.String[] {
              "Instance", "UpdateMask",
            });
    internal_static_google_bigtable_admin_v2_DeleteInstanceRequest_descriptor =
        getDescriptor().getMessageTypes().get(5);
    internal_static_google_bigtable_admin_v2_DeleteInstanceRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_DeleteInstanceRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_bigtable_admin_v2_CreateClusterRequest_descriptor =
        getDescriptor().getMessageTypes().get(6);
    internal_static_google_bigtable_admin_v2_CreateClusterRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_CreateClusterRequest_descriptor,
            new java.lang.String[] {
              "Parent", "ClusterId", "Cluster",
            });
    internal_static_google_bigtable_admin_v2_GetClusterRequest_descriptor =
        getDescriptor().getMessageTypes().get(7);
    internal_static_google_bigtable_admin_v2_GetClusterRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_GetClusterRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_bigtable_admin_v2_ListClustersRequest_descriptor =
        getDescriptor().getMessageTypes().get(8);
    internal_static_google_bigtable_admin_v2_ListClustersRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_ListClustersRequest_descriptor,
            new java.lang.String[] {
              "Parent", "PageToken",
            });
    internal_static_google_bigtable_admin_v2_ListClustersResponse_descriptor =
        getDescriptor().getMessageTypes().get(9);
    internal_static_google_bigtable_admin_v2_ListClustersResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_ListClustersResponse_descriptor,
            new java.lang.String[] {
              "Clusters", "FailedLocations", "NextPageToken",
            });
    internal_static_google_bigtable_admin_v2_DeleteClusterRequest_descriptor =
        getDescriptor().getMessageTypes().get(10);
    internal_static_google_bigtable_admin_v2_DeleteClusterRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_DeleteClusterRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_bigtable_admin_v2_CreateInstanceMetadata_descriptor =
        getDescriptor().getMessageTypes().get(11);
    internal_static_google_bigtable_admin_v2_CreateInstanceMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_CreateInstanceMetadata_descriptor,
            new java.lang.String[] {
              "OriginalRequest", "RequestTime", "FinishTime",
            });
    internal_static_google_bigtable_admin_v2_UpdateInstanceMetadata_descriptor =
        getDescriptor().getMessageTypes().get(12);
    internal_static_google_bigtable_admin_v2_UpdateInstanceMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_UpdateInstanceMetadata_descriptor,
            new java.lang.String[] {
              "OriginalRequest", "RequestTime", "FinishTime",
            });
    internal_static_google_bigtable_admin_v2_CreateClusterMetadata_descriptor =
        getDescriptor().getMessageTypes().get(13);
    internal_static_google_bigtable_admin_v2_CreateClusterMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_CreateClusterMetadata_descriptor,
            new java.lang.String[] {
              "OriginalRequest", "RequestTime", "FinishTime",
            });
    internal_static_google_bigtable_admin_v2_UpdateClusterMetadata_descriptor =
        getDescriptor().getMessageTypes().get(14);
    internal_static_google_bigtable_admin_v2_UpdateClusterMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_UpdateClusterMetadata_descriptor,
            new java.lang.String[] {
              "OriginalRequest", "RequestTime", "FinishTime",
            });
    internal_static_google_bigtable_admin_v2_PartialUpdateClusterMetadata_descriptor =
        getDescriptor().getMessageTypes().get(15);
    internal_static_google_bigtable_admin_v2_PartialUpdateClusterMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_PartialUpdateClusterMetadata_descriptor,
            new java.lang.String[] {
              "RequestTime", "FinishTime", "OriginalRequest",
            });
    internal_static_google_bigtable_admin_v2_PartialUpdateClusterRequest_descriptor =
        getDescriptor().getMessageTypes().get(16);
    internal_static_google_bigtable_admin_v2_PartialUpdateClusterRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_PartialUpdateClusterRequest_descriptor,
            new java.lang.String[] {
              "Cluster", "UpdateMask",
            });
    internal_static_google_bigtable_admin_v2_CreateAppProfileRequest_descriptor =
        getDescriptor().getMessageTypes().get(17);
    internal_static_google_bigtable_admin_v2_CreateAppProfileRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_CreateAppProfileRequest_descriptor,
            new java.lang.String[] {
              "Parent", "AppProfileId", "AppProfile", "IgnoreWarnings",
            });
    internal_static_google_bigtable_admin_v2_GetAppProfileRequest_descriptor =
        getDescriptor().getMessageTypes().get(18);
    internal_static_google_bigtable_admin_v2_GetAppProfileRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_GetAppProfileRequest_descriptor,
            new java.lang.String[] {
              "Name",
            });
    internal_static_google_bigtable_admin_v2_ListAppProfilesRequest_descriptor =
        getDescriptor().getMessageTypes().get(19);
    internal_static_google_bigtable_admin_v2_ListAppProfilesRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_ListAppProfilesRequest_descriptor,
            new java.lang.String[] {
              "Parent", "PageSize", "PageToken",
            });
    internal_static_google_bigtable_admin_v2_ListAppProfilesResponse_descriptor =
        getDescriptor().getMessageTypes().get(20);
    internal_static_google_bigtable_admin_v2_ListAppProfilesResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_ListAppProfilesResponse_descriptor,
            new java.lang.String[] {
              "AppProfiles", "NextPageToken", "FailedLocations",
            });
    internal_static_google_bigtable_admin_v2_UpdateAppProfileRequest_descriptor =
        getDescriptor().getMessageTypes().get(21);
    internal_static_google_bigtable_admin_v2_UpdateAppProfileRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_UpdateAppProfileRequest_descriptor,
            new java.lang.String[] {
              "AppProfile", "UpdateMask", "IgnoreWarnings",
            });
    internal_static_google_bigtable_admin_v2_DeleteAppProfileRequest_descriptor =
        getDescriptor().getMessageTypes().get(22);
    internal_static_google_bigtable_admin_v2_DeleteAppProfileRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_DeleteAppProfileRequest_descriptor,
            new java.lang.String[] {
              "Name", "IgnoreWarnings",
            });
    internal_static_google_bigtable_admin_v2_UpdateAppProfileMetadata_descriptor =
        getDescriptor().getMessageTypes().get(23);
    internal_static_google_bigtable_admin_v2_UpdateAppProfileMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_UpdateAppProfileMetadata_descriptor,
            new java.lang.String[] {});
    internal_static_google_bigtable_admin_v2_ListHotTabletsRequest_descriptor =
        getDescriptor().getMessageTypes().get(24);
    internal_static_google_bigtable_admin_v2_ListHotTabletsRequest_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_ListHotTabletsRequest_descriptor,
            new java.lang.String[] {
              "Parent", "StartTime", "EndTime", "PageSize", "PageToken",
            });
    internal_static_google_bigtable_admin_v2_ListHotTabletsResponse_descriptor =
        getDescriptor().getMessageTypes().get(25);
    internal_static_google_bigtable_admin_v2_ListHotTabletsResponse_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_admin_v2_ListHotTabletsResponse_descriptor,
            new java.lang.String[] {
              "HotTablets", "NextPageToken",
            });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.ClientProto.defaultHost);
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    registry.add(com.google.api.AnnotationsProto.http);
    registry.add(com.google.api.ClientProto.methodSignature);
    registry.add(com.google.api.ClientProto.oauthScopes);
    registry.add(com.google.api.ResourceProto.resourceReference);
    registry.add(com.google.longrunning.OperationsProto.operationInfo);
    com.google.protobuf.Descriptors.FileDescriptor.internalUpdateFileDescriptor(
        descriptor, registry);
    com.google.api.AnnotationsProto.getDescriptor();
    com.google.api.ClientProto.getDescriptor();
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.bigtable.admin.v2.InstanceProto.getDescriptor();
    com.google.iam.v1.IamPolicyProto.getDescriptor();
    com.google.iam.v1.PolicyProto.getDescriptor();
    com.google.longrunning.OperationsProto.getDescriptor();
    com.google.protobuf.EmptyProto.getDescriptor();
    com.google.protobuf.FieldMaskProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
