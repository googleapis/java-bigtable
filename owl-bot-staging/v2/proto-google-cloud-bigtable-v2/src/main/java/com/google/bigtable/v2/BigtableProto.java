// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/v2/bigtable.proto

package com.google.bigtable.v2;

public final class BigtableProto {
  private BigtableProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_ReadRowsRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ReadRowsRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_ReadRowsResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ReadRowsResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_ReadRowsResponse_CellChunk_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ReadRowsResponse_CellChunk_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_SampleRowKeysRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_SampleRowKeysRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_SampleRowKeysResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_SampleRowKeysResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_MutateRowRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_MutateRowRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_MutateRowResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_MutateRowResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_MutateRowsRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_MutateRowsRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_MutateRowsRequest_Entry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_MutateRowsRequest_Entry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_MutateRowsResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_MutateRowsResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_MutateRowsResponse_Entry_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_MutateRowsResponse_Entry_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_CheckAndMutateRowRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_CheckAndMutateRowRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_CheckAndMutateRowResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_CheckAndMutateRowResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_PingAndWarmRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_PingAndWarmRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_PingAndWarmResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_PingAndWarmResponse_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_ReadModifyWriteRowRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ReadModifyWriteRowRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n!google/bigtable/v2/bigtable.proto\022\022goo" +
      "gle.bigtable.v2\032\034google/api/annotations." +
      "proto\032\027google/api/client.proto\032\037google/a" +
      "pi/field_behavior.proto\032\031google/api/reso" +
      "urce.proto\032\030google/api/routing.proto\032\035go" +
      "ogle/bigtable/v2/data.proto\032\036google/prot" +
      "obuf/wrappers.proto\032\027google/rpc/status.p" +
      "roto\"\326\001\n\017ReadRowsRequest\022>\n\ntable_name\030\001" +
      " \001(\tB*\340A\002\372A$\n\"bigtableadmin.googleapis.c" +
      "om/Table\022\026\n\016app_profile_id\030\005 \001(\t\022(\n\004rows" +
      "\030\002 \001(\0132\032.google.bigtable.v2.RowSet\022-\n\006fi" +
      "lter\030\003 \001(\0132\035.google.bigtable.v2.RowFilte" +
      "r\022\022\n\nrows_limit\030\004 \001(\003\"\370\002\n\020ReadRowsRespon" +
      "se\022>\n\006chunks\030\001 \003(\0132..google.bigtable.v2." +
      "ReadRowsResponse.CellChunk\022\034\n\024last_scann" +
      "ed_row_key\030\002 \001(\014\032\205\002\n\tCellChunk\022\017\n\007row_ke" +
      "y\030\001 \001(\014\0221\n\013family_name\030\002 \001(\0132\034.google.pr" +
      "otobuf.StringValue\022.\n\tqualifier\030\003 \001(\0132\033." +
      "google.protobuf.BytesValue\022\030\n\020timestamp_" +
      "micros\030\004 \001(\003\022\016\n\006labels\030\005 \003(\t\022\r\n\005value\030\006 " +
      "\001(\014\022\022\n\nvalue_size\030\007 \001(\005\022\023\n\treset_row\030\010 \001" +
      "(\010H\000\022\024\n\ncommit_row\030\t \001(\010H\000B\014\n\nrow_status" +
      "\"n\n\024SampleRowKeysRequest\022>\n\ntable_name\030\001" +
      " \001(\tB*\340A\002\372A$\n\"bigtableadmin.googleapis.c" +
      "om/Table\022\026\n\016app_profile_id\030\002 \001(\t\">\n\025Samp" +
      "leRowKeysResponse\022\017\n\007row_key\030\001 \001(\014\022\024\n\014of" +
      "fset_bytes\030\002 \001(\003\"\266\001\n\020MutateRowRequest\022>\n" +
      "\ntable_name\030\001 \001(\tB*\340A\002\372A$\n\"bigtableadmin" +
      ".googleapis.com/Table\022\026\n\016app_profile_id\030" +
      "\004 \001(\t\022\024\n\007row_key\030\002 \001(\014B\003\340A\002\0224\n\tmutations" +
      "\030\003 \003(\0132\034.google.bigtable.v2.MutationB\003\340A" +
      "\002\"\023\n\021MutateRowResponse\"\376\001\n\021MutateRowsReq" +
      "uest\022>\n\ntable_name\030\001 \001(\tB*\340A\002\372A$\n\"bigtab" +
      "leadmin.googleapis.com/Table\022\026\n\016app_prof" +
      "ile_id\030\003 \001(\t\022A\n\007entries\030\002 \003(\0132+.google.b" +
      "igtable.v2.MutateRowsRequest.EntryB\003\340A\002\032" +
      "N\n\005Entry\022\017\n\007row_key\030\001 \001(\014\0224\n\tmutations\030\002" +
      " \003(\0132\034.google.bigtable.v2.MutationB\003\340A\002\"" +
      "\217\001\n\022MutateRowsResponse\022=\n\007entries\030\001 \003(\0132" +
      ",.google.bigtable.v2.MutateRowsResponse." +
      "Entry\032:\n\005Entry\022\r\n\005index\030\001 \001(\003\022\"\n\006status\030" +
      "\002 \001(\0132\022.google.rpc.Status\"\256\002\n\030CheckAndMu" +
      "tateRowRequest\022>\n\ntable_name\030\001 \001(\tB*\340A\002\372" +
      "A$\n\"bigtableadmin.googleapis.com/Table\022\026" +
      "\n\016app_profile_id\030\007 \001(\t\022\024\n\007row_key\030\002 \001(\014B" +
      "\003\340A\002\0227\n\020predicate_filter\030\006 \001(\0132\035.google." +
      "bigtable.v2.RowFilter\0224\n\016true_mutations\030" +
      "\004 \003(\0132\034.google.bigtable.v2.Mutation\0225\n\017f" +
      "alse_mutations\030\005 \003(\0132\034.google.bigtable.v" +
      "2.Mutation\"6\n\031CheckAndMutateRowResponse\022" +
      "\031\n\021predicate_matched\030\001 \001(\010\"i\n\022PingAndWar" +
      "mRequest\022;\n\004name\030\001 \001(\tB-\340A\002\372A\'\n%bigtable" +
      "admin.googleapis.com/Instance\022\026\n\016app_pro" +
      "file_id\030\002 \001(\t\"\025\n\023PingAndWarmResponse\"\306\001\n" +
      "\031ReadModifyWriteRowRequest\022>\n\ntable_name" +
      "\030\001 \001(\tB*\340A\002\372A$\n\"bigtableadmin.googleapis" +
      ".com/Table\022\026\n\016app_profile_id\030\004 \001(\t\022\024\n\007ro" +
      "w_key\030\002 \001(\014B\003\340A\002\022;\n\005rules\030\003 \003(\0132\'.google" +
      ".bigtable.v2.ReadModifyWriteRuleB\003\340A\002\"B\n" +
      "\032ReadModifyWriteRowResponse\022$\n\003row\030\001 \001(\013" +
      "2\027.google.bigtable.v2.Row2\260\024\n\010Bigtable\022\233" +
      "\002\n\010ReadRows\022#.google.bigtable.v2.ReadRow" +
      "sRequest\032$.google.bigtable.v2.ReadRowsRe" +
      "sponse\"\301\001\202\323\344\223\002>\"9/v2/{table_name=project" +
      "s/*/instances/*/tables/*}:readRows:\001*\212\323\344" +
      "\223\002N\022:\n\ntable_name\022,{table_name=projects/" +
      "*/instances/*/tables/*}\022\020\n\016app_profile_i" +
      "d\332A\ntable_name\332A\031table_name,app_profile_" +
      "id0\001\022\254\002\n\rSampleRowKeys\022(.google.bigtable" +
      ".v2.SampleRowKeysRequest\032).google.bigtab" +
      "le.v2.SampleRowKeysResponse\"\303\001\202\323\344\223\002@\022>/v" +
      "2/{table_name=projects/*/instances/*/tab" +
      "les/*}:sampleRowKeys\212\323\344\223\002N\022:\n\ntable_name" +
      "\022,{table_name=projects/*/instances/*/tab" +
      "les/*}\022\020\n\016app_profile_id\332A\ntable_name\332A\031" +
      "table_name,app_profile_id0\001\022\301\002\n\tMutateRo" +
      "w\022$.google.bigtable.v2.MutateRowRequest\032" +
      "%.google.bigtable.v2.MutateRowResponse\"\346" +
      "\001\202\323\344\223\002?\":/v2/{table_name=projects/*/inst" +
      "ances/*/tables/*}:mutateRow:\001*\212\323\344\223\002N\022:\n\n" +
      "table_name\022,{table_name=projects/*/insta" +
      "nces/*/tables/*}\022\020\n\016app_profile_id\332A\034tab" +
      "le_name,row_key,mutations\332A+table_name,r" +
      "ow_key,mutations,app_profile_id\022\263\002\n\nMuta" +
      "teRows\022%.google.bigtable.v2.MutateRowsRe" +
      "quest\032&.google.bigtable.v2.MutateRowsRes" +
      "ponse\"\323\001\202\323\344\223\002@\";/v2/{table_name=projects" +
      "/*/instances/*/tables/*}:mutateRows:\001*\212\323" +
      "\344\223\002N\022:\n\ntable_name\022,{table_name=projects" +
      "/*/instances/*/tables/*}\022\020\n\016app_profile_" +
      "id\332A\022table_name,entries\332A!table_name,ent" +
      "ries,app_profile_id0\001\022\255\003\n\021CheckAndMutate" +
      "Row\022,.google.bigtable.v2.CheckAndMutateR" +
      "owRequest\032-.google.bigtable.v2.CheckAndM" +
      "utateRowResponse\"\272\002\202\323\344\223\002G\"B/v2/{table_na" +
      "me=projects/*/instances/*/tables/*}:chec" +
      "kAndMutateRow:\001*\212\323\344\223\002N\022:\n\ntable_name\022,{t" +
      "able_name=projects/*/instances/*/tables/" +
      "*}\022\020\n\016app_profile_id\332ABtable_name,row_ke" +
      "y,predicate_filter,true_mutations,false_" +
      "mutations\332AQtable_name,row_key,predicate" +
      "_filter,true_mutations,false_mutations,a" +
      "pp_profile_id\022\356\001\n\013PingAndWarm\022&.google.b" +
      "igtable.v2.PingAndWarmRequest\032\'.google.b" +
      "igtable.v2.PingAndWarmResponse\"\215\001\202\323\344\223\002+\"" +
      "&/v2/{name=projects/*/instances/*}:ping:" +
      "\001*\212\323\344\223\0029\022%\n\004name\022\035{name=projects/*/insta" +
      "nces/*}\022\020\n\016app_profile_id\332A\004name\332A\023name," +
      "app_profile_id\022\335\002\n\022ReadModifyWriteRow\022-." +
      "google.bigtable.v2.ReadModifyWriteRowReq" +
      "uest\032..google.bigtable.v2.ReadModifyWrit" +
      "eRowResponse\"\347\001\202\323\344\223\002H\"C/v2/{table_name=p" +
      "rojects/*/instances/*/tables/*}:readModi" +
      "fyWriteRow:\001*\212\323\344\223\002N\022:\n\ntable_name\022,{tabl" +
      "e_name=projects/*/instances/*/tables/*}\022" +
      "\020\n\016app_profile_id\332A\030table_name,row_key,r" +
      "ules\332A\'table_name,row_key,rules,app_prof" +
      "ile_id\032\333\002\312A\027bigtable.googleapis.com\322A\275\002h" +
      "ttps://www.googleapis.com/auth/bigtable." +
      "data,https://www.googleapis.com/auth/big" +
      "table.data.readonly,https://www.googleap" +
      "is.com/auth/cloud-bigtable.data,https://" +
      "www.googleapis.com/auth/cloud-bigtable.d" +
      "ata.readonly,https://www.googleapis.com/" +
      "auth/cloud-platform,https://www.googleap" +
      "is.com/auth/cloud-platform.read-onlyB\353\002\n" +
      "\026com.google.bigtable.v2B\rBigtableProtoP\001" +
      "Z:google.golang.org/genproto/googleapis/" +
      "bigtable/v2;bigtable\252\002\030Google.Cloud.Bigt" +
      "able.V2\312\002\030Google\\Cloud\\Bigtable\\V2\352\002\033Goo" +
      "gle::Cloud::Bigtable::V2\352A\\\n\"bigtableadm" +
      "in.googleapis.com/Table\0226projects/{proje" +
      "ct}/instances/{instance}/tables/{table}\352" +
      "AP\n%bigtableadmin.googleapis.com/Instanc" +
      "e\022\'projects/{project}/instances/{instanc" +
      "e}b\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.api.AnnotationsProto.getDescriptor(),
          com.google.api.ClientProto.getDescriptor(),
          com.google.api.FieldBehaviorProto.getDescriptor(),
          com.google.api.ResourceProto.getDescriptor(),
          com.google.api.RoutingProto.getDescriptor(),
          com.google.bigtable.v2.DataProto.getDescriptor(),
          com.google.protobuf.WrappersProto.getDescriptor(),
          com.google.rpc.StatusProto.getDescriptor(),
        });
    internal_static_google_bigtable_v2_ReadRowsRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_v2_ReadRowsRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_ReadRowsRequest_descriptor,
        new java.lang.String[] { "TableName", "AppProfileId", "Rows", "Filter", "RowsLimit", });
    internal_static_google_bigtable_v2_ReadRowsResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_google_bigtable_v2_ReadRowsResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_ReadRowsResponse_descriptor,
        new java.lang.String[] { "Chunks", "LastScannedRowKey", });
    internal_static_google_bigtable_v2_ReadRowsResponse_CellChunk_descriptor =
      internal_static_google_bigtable_v2_ReadRowsResponse_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_v2_ReadRowsResponse_CellChunk_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_ReadRowsResponse_CellChunk_descriptor,
        new java.lang.String[] { "RowKey", "FamilyName", "Qualifier", "TimestampMicros", "Labels", "Value", "ValueSize", "ResetRow", "CommitRow", "RowStatus", });
    internal_static_google_bigtable_v2_SampleRowKeysRequest_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_google_bigtable_v2_SampleRowKeysRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_SampleRowKeysRequest_descriptor,
        new java.lang.String[] { "TableName", "AppProfileId", });
    internal_static_google_bigtable_v2_SampleRowKeysResponse_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_google_bigtable_v2_SampleRowKeysResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_SampleRowKeysResponse_descriptor,
        new java.lang.String[] { "RowKey", "OffsetBytes", });
    internal_static_google_bigtable_v2_MutateRowRequest_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_google_bigtable_v2_MutateRowRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_MutateRowRequest_descriptor,
        new java.lang.String[] { "TableName", "AppProfileId", "RowKey", "Mutations", });
    internal_static_google_bigtable_v2_MutateRowResponse_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_google_bigtable_v2_MutateRowResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_MutateRowResponse_descriptor,
        new java.lang.String[] { });
    internal_static_google_bigtable_v2_MutateRowsRequest_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_google_bigtable_v2_MutateRowsRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_MutateRowsRequest_descriptor,
        new java.lang.String[] { "TableName", "AppProfileId", "Entries", });
    internal_static_google_bigtable_v2_MutateRowsRequest_Entry_descriptor =
      internal_static_google_bigtable_v2_MutateRowsRequest_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_v2_MutateRowsRequest_Entry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_MutateRowsRequest_Entry_descriptor,
        new java.lang.String[] { "RowKey", "Mutations", });
    internal_static_google_bigtable_v2_MutateRowsResponse_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_google_bigtable_v2_MutateRowsResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_MutateRowsResponse_descriptor,
        new java.lang.String[] { "Entries", });
    internal_static_google_bigtable_v2_MutateRowsResponse_Entry_descriptor =
      internal_static_google_bigtable_v2_MutateRowsResponse_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_v2_MutateRowsResponse_Entry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_MutateRowsResponse_Entry_descriptor,
        new java.lang.String[] { "Index", "Status", });
    internal_static_google_bigtable_v2_CheckAndMutateRowRequest_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_google_bigtable_v2_CheckAndMutateRowRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_CheckAndMutateRowRequest_descriptor,
        new java.lang.String[] { "TableName", "AppProfileId", "RowKey", "PredicateFilter", "TrueMutations", "FalseMutations", });
    internal_static_google_bigtable_v2_CheckAndMutateRowResponse_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_google_bigtable_v2_CheckAndMutateRowResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_CheckAndMutateRowResponse_descriptor,
        new java.lang.String[] { "PredicateMatched", });
    internal_static_google_bigtable_v2_PingAndWarmRequest_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_google_bigtable_v2_PingAndWarmRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_PingAndWarmRequest_descriptor,
        new java.lang.String[] { "Name", "AppProfileId", });
    internal_static_google_bigtable_v2_PingAndWarmResponse_descriptor =
      getDescriptor().getMessageTypes().get(11);
    internal_static_google_bigtable_v2_PingAndWarmResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_PingAndWarmResponse_descriptor,
        new java.lang.String[] { });
    internal_static_google_bigtable_v2_ReadModifyWriteRowRequest_descriptor =
      getDescriptor().getMessageTypes().get(12);
    internal_static_google_bigtable_v2_ReadModifyWriteRowRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_ReadModifyWriteRowRequest_descriptor,
        new java.lang.String[] { "TableName", "AppProfileId", "RowKey", "Rules", });
    internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_descriptor =
      getDescriptor().getMessageTypes().get(13);
    internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_descriptor,
        new java.lang.String[] { "Row", });
    com.google.protobuf.ExtensionRegistry registry =
        com.google.protobuf.ExtensionRegistry.newInstance();
    registry.add(com.google.api.ClientProto.defaultHost);
    registry.add(com.google.api.FieldBehaviorProto.fieldBehavior);
    registry.add(com.google.api.AnnotationsProto.http);
    registry.add(com.google.api.ClientProto.methodSignature);
    registry.add(com.google.api.ClientProto.oauthScopes);
    registry.add(com.google.api.ResourceProto.resourceDefinition);
    registry.add(com.google.api.ResourceProto.resourceReference);
    registry.add(com.google.api.RoutingProto.routing);
    com.google.protobuf.Descriptors.FileDescriptor
        .internalUpdateFileDescriptor(descriptor, registry);
    com.google.api.AnnotationsProto.getDescriptor();
    com.google.api.ClientProto.getDescriptor();
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.api.ResourceProto.getDescriptor();
    com.google.api.RoutingProto.getDescriptor();
    com.google.bigtable.v2.DataProto.getDescriptor();
    com.google.protobuf.WrappersProto.getDescriptor();
    com.google.rpc.StatusProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
