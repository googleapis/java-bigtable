/*
 * Copyright 2024 Google LLC
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
// source: google/bigtable/v2/data.proto

// Protobuf Java Version: 3.25.5
package com.google.bigtable.v2;

public final class DataProto {
  private DataProto() {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}

  public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
  }

  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Row_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Row_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Family_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Family_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Column_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Column_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Cell_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Cell_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Value_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Value_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ArrayValue_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ArrayValue_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_RowRange_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_RowRange_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_RowSet_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_RowSet_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ColumnRange_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ColumnRange_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_TimestampRange_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_TimestampRange_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ValueRange_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ValueRange_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_RowFilter_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_RowFilter_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_RowFilter_Chain_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_RowFilter_Chain_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_RowFilter_Interleave_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_RowFilter_Interleave_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_RowFilter_Condition_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_RowFilter_Condition_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Mutation_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Mutation_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Mutation_SetCell_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Mutation_SetCell_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Mutation_AddToCell_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Mutation_AddToCell_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Mutation_MergeToCell_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Mutation_MergeToCell_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Mutation_DeleteFromColumn_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Mutation_DeleteFromColumn_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Mutation_DeleteFromFamily_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Mutation_DeleteFromFamily_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_Mutation_DeleteFromRow_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_Mutation_DeleteFromRow_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ReadModifyWriteRule_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ReadModifyWriteRule_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_StreamPartition_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_StreamPartition_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_StreamContinuationTokens_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_StreamContinuationTokens_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_StreamContinuationToken_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_StreamContinuationToken_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ProtoFormat_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ProtoFormat_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ColumnMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ColumnMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ProtoSchema_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ProtoSchema_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ResultSetMetadata_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ResultSetMetadata_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ProtoRows_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ProtoRows_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_ProtoRowsBatch_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_ProtoRowsBatch_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
      internal_static_google_bigtable_v2_PartialResultSet_descriptor;
  static final com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_google_bigtable_v2_PartialResultSet_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
    return descriptor;
  }

  private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

  static {
    java.lang.String[] descriptorData = {
      "\n\035google/bigtable/v2/data.proto\022\022google."
          + "bigtable.v2\032\037google/api/field_behavior.p"
          + "roto\032\036google/bigtable/v2/types.proto\032\037go"
          + "ogle/protobuf/timestamp.proto\032\026google/ty"
          + "pe/date.proto\"@\n\003Row\022\013\n\003key\030\001 \001(\014\022,\n\010fam"
          + "ilies\030\002 \003(\0132\032.google.bigtable.v2.Family\""
          + "C\n\006Family\022\014\n\004name\030\001 \001(\t\022+\n\007columns\030\002 \003(\013"
          + "2\032.google.bigtable.v2.Column\"D\n\006Column\022\021"
          + "\n\tqualifier\030\001 \001(\014\022\'\n\005cells\030\002 \003(\0132\030.googl"
          + "e.bigtable.v2.Cell\"?\n\004Cell\022\030\n\020timestamp_"
          + "micros\030\001 \001(\003\022\r\n\005value\030\002 \001(\014\022\016\n\006labels\030\003 "
          + "\003(\t\"\364\002\n\005Value\022&\n\004type\030\007 \001(\0132\030.google.big"
          + "table.v2.Type\022\023\n\traw_value\030\010 \001(\014H\000\022\036\n\024ra"
          + "w_timestamp_micros\030\t \001(\003H\000\022\025\n\013bytes_valu"
          + "e\030\002 \001(\014H\000\022\026\n\014string_value\030\003 \001(\tH\000\022\023\n\tint"
          + "_value\030\006 \001(\003H\000\022\024\n\nbool_value\030\n \001(\010H\000\022\025\n\013"
          + "float_value\030\013 \001(\001H\000\0225\n\017timestamp_value\030\014"
          + " \001(\0132\032.google.protobuf.TimestampH\000\022\'\n\nda"
          + "te_value\030\r \001(\0132\021.google.type.DateH\000\0225\n\013a"
          + "rray_value\030\004 \001(\0132\036.google.bigtable.v2.Ar"
          + "rayValueH\000B\006\n\004kind\"7\n\nArrayValue\022)\n\006valu"
          + "es\030\001 \003(\0132\031.google.bigtable.v2.Value\"\212\001\n\010"
          + "RowRange\022\032\n\020start_key_closed\030\001 \001(\014H\000\022\030\n\016"
          + "start_key_open\030\002 \001(\014H\000\022\026\n\014end_key_open\030\003"
          + " \001(\014H\001\022\030\n\016end_key_closed\030\004 \001(\014H\001B\013\n\tstar"
          + "t_keyB\t\n\007end_key\"L\n\006RowSet\022\020\n\010row_keys\030\001"
          + " \003(\014\0220\n\nrow_ranges\030\002 \003(\0132\034.google.bigtab"
          + "le.v2.RowRange\"\306\001\n\013ColumnRange\022\023\n\013family"
          + "_name\030\001 \001(\t\022 \n\026start_qualifier_closed\030\002 "
          + "\001(\014H\000\022\036\n\024start_qualifier_open\030\003 \001(\014H\000\022\036\n"
          + "\024end_qualifier_closed\030\004 \001(\014H\001\022\034\n\022end_qua"
          + "lifier_open\030\005 \001(\014H\001B\021\n\017start_qualifierB\017"
          + "\n\rend_qualifier\"N\n\016TimestampRange\022\036\n\026sta"
          + "rt_timestamp_micros\030\001 \001(\003\022\034\n\024end_timesta"
          + "mp_micros\030\002 \001(\003\"\230\001\n\nValueRange\022\034\n\022start_"
          + "value_closed\030\001 \001(\014H\000\022\032\n\020start_value_open"
          + "\030\002 \001(\014H\000\022\032\n\020end_value_closed\030\003 \001(\014H\001\022\030\n\016"
          + "end_value_open\030\004 \001(\014H\001B\r\n\013start_valueB\013\n"
          + "\tend_value\"\337\010\n\tRowFilter\0224\n\005chain\030\001 \001(\0132"
          + "#.google.bigtable.v2.RowFilter.ChainH\000\022>"
          + "\n\ninterleave\030\002 \001(\0132(.google.bigtable.v2."
          + "RowFilter.InterleaveH\000\022<\n\tcondition\030\003 \001("
          + "\0132\'.google.bigtable.v2.RowFilter.Conditi"
          + "onH\000\022\016\n\004sink\030\020 \001(\010H\000\022\031\n\017pass_all_filter\030"
          + "\021 \001(\010H\000\022\032\n\020block_all_filter\030\022 \001(\010H\000\022\036\n\024r"
          + "ow_key_regex_filter\030\004 \001(\014H\000\022\033\n\021row_sampl"
          + "e_filter\030\016 \001(\001H\000\022\"\n\030family_name_regex_fi"
          + "lter\030\005 \001(\tH\000\022\'\n\035column_qualifier_regex_f"
          + "ilter\030\006 \001(\014H\000\022>\n\023column_range_filter\030\007 \001"
          + "(\0132\037.google.bigtable.v2.ColumnRangeH\000\022D\n"
          + "\026timestamp_range_filter\030\010 \001(\0132\".google.b"
          + "igtable.v2.TimestampRangeH\000\022\034\n\022value_reg"
          + "ex_filter\030\t \001(\014H\000\022<\n\022value_range_filter\030"
          + "\017 \001(\0132\036.google.bigtable.v2.ValueRangeH\000\022"
          + "%\n\033cells_per_row_offset_filter\030\n \001(\005H\000\022$"
          + "\n\032cells_per_row_limit_filter\030\013 \001(\005H\000\022\'\n\035"
          + "cells_per_column_limit_filter\030\014 \001(\005H\000\022!\n"
          + "\027strip_value_transformer\030\r \001(\010H\000\022!\n\027appl"
          + "y_label_transformer\030\023 \001(\tH\000\0327\n\005Chain\022.\n\007"
          + "filters\030\001 \003(\0132\035.google.bigtable.v2.RowFi"
          + "lter\032<\n\nInterleave\022.\n\007filters\030\001 \003(\0132\035.go"
          + "ogle.bigtable.v2.RowFilter\032\255\001\n\tCondition"
          + "\0227\n\020predicate_filter\030\001 \001(\0132\035.google.bigt"
          + "able.v2.RowFilter\0222\n\013true_filter\030\002 \001(\0132\035"
          + ".google.bigtable.v2.RowFilter\0223\n\014false_f"
          + "ilter\030\003 \001(\0132\035.google.bigtable.v2.RowFilt"
          + "erB\010\n\006filter\"\255\010\n\010Mutation\0228\n\010set_cell\030\001 "
          + "\001(\0132$.google.bigtable.v2.Mutation.SetCel"
          + "lH\000\022=\n\013add_to_cell\030\005 \001(\0132&.google.bigtab"
          + "le.v2.Mutation.AddToCellH\000\022A\n\rmerge_to_c"
          + "ell\030\006 \001(\0132(.google.bigtable.v2.Mutation."
          + "MergeToCellH\000\022K\n\022delete_from_column\030\002 \001("
          + "\0132-.google.bigtable.v2.Mutation.DeleteFr"
          + "omColumnH\000\022K\n\022delete_from_family\030\003 \001(\0132-"
          + ".google.bigtable.v2.Mutation.DeleteFromF"
          + "amilyH\000\022E\n\017delete_from_row\030\004 \001(\0132*.googl"
          + "e.bigtable.v2.Mutation.DeleteFromRowH\000\032a"
          + "\n\007SetCell\022\023\n\013family_name\030\001 \001(\t\022\030\n\020column"
          + "_qualifier\030\002 \001(\014\022\030\n\020timestamp_micros\030\003 \001"
          + "(\003\022\r\n\005value\030\004 \001(\014\032\255\001\n\tAddToCell\022\023\n\013famil"
          + "y_name\030\001 \001(\t\0223\n\020column_qualifier\030\002 \001(\0132\031"
          + ".google.bigtable.v2.Value\022,\n\ttimestamp\030\003"
          + " \001(\0132\031.google.bigtable.v2.Value\022(\n\005input"
          + "\030\004 \001(\0132\031.google.bigtable.v2.Value\032\257\001\n\013Me"
          + "rgeToCell\022\023\n\013family_name\030\001 \001(\t\0223\n\020column"
          + "_qualifier\030\002 \001(\0132\031.google.bigtable.v2.Va"
          + "lue\022,\n\ttimestamp\030\003 \001(\0132\031.google.bigtable"
          + ".v2.Value\022(\n\005input\030\004 \001(\0132\031.google.bigtab"
          + "le.v2.Value\032y\n\020DeleteFromColumn\022\023\n\013famil"
          + "y_name\030\001 \001(\t\022\030\n\020column_qualifier\030\002 \001(\014\0226"
          + "\n\ntime_range\030\003 \001(\0132\".google.bigtable.v2."
          + "TimestampRange\032\'\n\020DeleteFromFamily\022\023\n\013fa"
          + "mily_name\030\001 \001(\t\032\017\n\rDeleteFromRowB\n\n\010muta"
          + "tion\"\200\001\n\023ReadModifyWriteRule\022\023\n\013family_n"
          + "ame\030\001 \001(\t\022\030\n\020column_qualifier\030\002 \001(\014\022\026\n\014a"
          + "ppend_value\030\003 \001(\014H\000\022\032\n\020increment_amount\030"
          + "\004 \001(\003H\000B\006\n\004rule\"B\n\017StreamPartition\022/\n\tro"
          + "w_range\030\001 \001(\0132\034.google.bigtable.v2.RowRa"
          + "nge\"W\n\030StreamContinuationTokens\022;\n\006token"
          + "s\030\001 \003(\0132+.google.bigtable.v2.StreamConti"
          + "nuationToken\"`\n\027StreamContinuationToken\022"
          + "6\n\tpartition\030\001 \001(\0132#.google.bigtable.v2."
          + "StreamPartition\022\r\n\005token\030\002 \001(\t\"\r\n\013ProtoF"
          + "ormat\"F\n\016ColumnMetadata\022\014\n\004name\030\001 \001(\t\022&\n"
          + "\004type\030\002 \001(\0132\030.google.bigtable.v2.Type\"B\n"
          + "\013ProtoSchema\0223\n\007columns\030\001 \003(\0132\".google.b"
          + "igtable.v2.ColumnMetadata\"V\n\021ResultSetMe"
          + "tadata\0227\n\014proto_schema\030\001 \001(\0132\037.google.bi"
          + "gtable.v2.ProtoSchemaH\000B\010\n\006schema\"6\n\tPro"
          + "toRows\022)\n\006values\030\002 \003(\0132\031.google.bigtable"
          + ".v2.Value\"$\n\016ProtoRowsBatch\022\022\n\nbatch_dat"
          + "a\030\001 \001(\014\"\226\001\n\020PartialResultSet\022>\n\020proto_ro"
          + "ws_batch\030\003 \001(\0132\".google.bigtable.v2.Prot"
          + "oRowsBatchH\000\022\024\n\014resume_token\030\005 \001(\014\022\034\n\024es"
          + "timated_batch_size\030\004 \001(\005B\016\n\014partial_rows"
          + "B\263\001\n\026com.google.bigtable.v2B\tDataProtoP\001"
          + "Z8cloud.google.com/go/bigtable/apiv2/big"
          + "tablepb;bigtablepb\252\002\030Google.Cloud.Bigtab"
          + "le.V2\312\002\030Google\\Cloud\\Bigtable\\V2\352\002\033Googl"
          + "e::Cloud::Bigtable::V2b\006proto3"
    };
    descriptor =
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {
              com.google.api.FieldBehaviorProto.getDescriptor(),
              com.google.bigtable.v2.TypesProto.getDescriptor(),
              com.google.protobuf.TimestampProto.getDescriptor(),
              com.google.type.DateProto.getDescriptor(),
            });
    internal_static_google_bigtable_v2_Row_descriptor = getDescriptor().getMessageTypes().get(0);
    internal_static_google_bigtable_v2_Row_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Row_descriptor,
            new java.lang.String[] {
              "Key", "Families",
            });
    internal_static_google_bigtable_v2_Family_descriptor = getDescriptor().getMessageTypes().get(1);
    internal_static_google_bigtable_v2_Family_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Family_descriptor,
            new java.lang.String[] {
              "Name", "Columns",
            });
    internal_static_google_bigtable_v2_Column_descriptor = getDescriptor().getMessageTypes().get(2);
    internal_static_google_bigtable_v2_Column_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Column_descriptor,
            new java.lang.String[] {
              "Qualifier", "Cells",
            });
    internal_static_google_bigtable_v2_Cell_descriptor = getDescriptor().getMessageTypes().get(3);
    internal_static_google_bigtable_v2_Cell_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Cell_descriptor,
            new java.lang.String[] {
              "TimestampMicros", "Value", "Labels",
            });
    internal_static_google_bigtable_v2_Value_descriptor = getDescriptor().getMessageTypes().get(4);
    internal_static_google_bigtable_v2_Value_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Value_descriptor,
            new java.lang.String[] {
              "Type",
              "RawValue",
              "RawTimestampMicros",
              "BytesValue",
              "StringValue",
              "IntValue",
              "BoolValue",
              "FloatValue",
              "TimestampValue",
              "DateValue",
              "ArrayValue",
              "Kind",
            });
    internal_static_google_bigtable_v2_ArrayValue_descriptor =
        getDescriptor().getMessageTypes().get(5);
    internal_static_google_bigtable_v2_ArrayValue_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ArrayValue_descriptor,
            new java.lang.String[] {
              "Values",
            });
    internal_static_google_bigtable_v2_RowRange_descriptor =
        getDescriptor().getMessageTypes().get(6);
    internal_static_google_bigtable_v2_RowRange_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_RowRange_descriptor,
            new java.lang.String[] {
              "StartKeyClosed", "StartKeyOpen", "EndKeyOpen", "EndKeyClosed", "StartKey", "EndKey",
            });
    internal_static_google_bigtable_v2_RowSet_descriptor = getDescriptor().getMessageTypes().get(7);
    internal_static_google_bigtable_v2_RowSet_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_RowSet_descriptor,
            new java.lang.String[] {
              "RowKeys", "RowRanges",
            });
    internal_static_google_bigtable_v2_ColumnRange_descriptor =
        getDescriptor().getMessageTypes().get(8);
    internal_static_google_bigtable_v2_ColumnRange_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ColumnRange_descriptor,
            new java.lang.String[] {
              "FamilyName",
              "StartQualifierClosed",
              "StartQualifierOpen",
              "EndQualifierClosed",
              "EndQualifierOpen",
              "StartQualifier",
              "EndQualifier",
            });
    internal_static_google_bigtable_v2_TimestampRange_descriptor =
        getDescriptor().getMessageTypes().get(9);
    internal_static_google_bigtable_v2_TimestampRange_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_TimestampRange_descriptor,
            new java.lang.String[] {
              "StartTimestampMicros", "EndTimestampMicros",
            });
    internal_static_google_bigtable_v2_ValueRange_descriptor =
        getDescriptor().getMessageTypes().get(10);
    internal_static_google_bigtable_v2_ValueRange_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ValueRange_descriptor,
            new java.lang.String[] {
              "StartValueClosed",
              "StartValueOpen",
              "EndValueClosed",
              "EndValueOpen",
              "StartValue",
              "EndValue",
            });
    internal_static_google_bigtable_v2_RowFilter_descriptor =
        getDescriptor().getMessageTypes().get(11);
    internal_static_google_bigtable_v2_RowFilter_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_RowFilter_descriptor,
            new java.lang.String[] {
              "Chain",
              "Interleave",
              "Condition",
              "Sink",
              "PassAllFilter",
              "BlockAllFilter",
              "RowKeyRegexFilter",
              "RowSampleFilter",
              "FamilyNameRegexFilter",
              "ColumnQualifierRegexFilter",
              "ColumnRangeFilter",
              "TimestampRangeFilter",
              "ValueRegexFilter",
              "ValueRangeFilter",
              "CellsPerRowOffsetFilter",
              "CellsPerRowLimitFilter",
              "CellsPerColumnLimitFilter",
              "StripValueTransformer",
              "ApplyLabelTransformer",
              "Filter",
            });
    internal_static_google_bigtable_v2_RowFilter_Chain_descriptor =
        internal_static_google_bigtable_v2_RowFilter_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_v2_RowFilter_Chain_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_RowFilter_Chain_descriptor,
            new java.lang.String[] {
              "Filters",
            });
    internal_static_google_bigtable_v2_RowFilter_Interleave_descriptor =
        internal_static_google_bigtable_v2_RowFilter_descriptor.getNestedTypes().get(1);
    internal_static_google_bigtable_v2_RowFilter_Interleave_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_RowFilter_Interleave_descriptor,
            new java.lang.String[] {
              "Filters",
            });
    internal_static_google_bigtable_v2_RowFilter_Condition_descriptor =
        internal_static_google_bigtable_v2_RowFilter_descriptor.getNestedTypes().get(2);
    internal_static_google_bigtable_v2_RowFilter_Condition_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_RowFilter_Condition_descriptor,
            new java.lang.String[] {
              "PredicateFilter", "TrueFilter", "FalseFilter",
            });
    internal_static_google_bigtable_v2_Mutation_descriptor =
        getDescriptor().getMessageTypes().get(12);
    internal_static_google_bigtable_v2_Mutation_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Mutation_descriptor,
            new java.lang.String[] {
              "SetCell",
              "AddToCell",
              "MergeToCell",
              "DeleteFromColumn",
              "DeleteFromFamily",
              "DeleteFromRow",
              "Mutation",
            });
    internal_static_google_bigtable_v2_Mutation_SetCell_descriptor =
        internal_static_google_bigtable_v2_Mutation_descriptor.getNestedTypes().get(0);
    internal_static_google_bigtable_v2_Mutation_SetCell_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Mutation_SetCell_descriptor,
            new java.lang.String[] {
              "FamilyName", "ColumnQualifier", "TimestampMicros", "Value",
            });
    internal_static_google_bigtable_v2_Mutation_AddToCell_descriptor =
        internal_static_google_bigtable_v2_Mutation_descriptor.getNestedTypes().get(1);
    internal_static_google_bigtable_v2_Mutation_AddToCell_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Mutation_AddToCell_descriptor,
            new java.lang.String[] {
              "FamilyName", "ColumnQualifier", "Timestamp", "Input",
            });
    internal_static_google_bigtable_v2_Mutation_MergeToCell_descriptor =
        internal_static_google_bigtable_v2_Mutation_descriptor.getNestedTypes().get(2);
    internal_static_google_bigtable_v2_Mutation_MergeToCell_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Mutation_MergeToCell_descriptor,
            new java.lang.String[] {
              "FamilyName", "ColumnQualifier", "Timestamp", "Input",
            });
    internal_static_google_bigtable_v2_Mutation_DeleteFromColumn_descriptor =
        internal_static_google_bigtable_v2_Mutation_descriptor.getNestedTypes().get(3);
    internal_static_google_bigtable_v2_Mutation_DeleteFromColumn_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Mutation_DeleteFromColumn_descriptor,
            new java.lang.String[] {
              "FamilyName", "ColumnQualifier", "TimeRange",
            });
    internal_static_google_bigtable_v2_Mutation_DeleteFromFamily_descriptor =
        internal_static_google_bigtable_v2_Mutation_descriptor.getNestedTypes().get(4);
    internal_static_google_bigtable_v2_Mutation_DeleteFromFamily_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Mutation_DeleteFromFamily_descriptor,
            new java.lang.String[] {
              "FamilyName",
            });
    internal_static_google_bigtable_v2_Mutation_DeleteFromRow_descriptor =
        internal_static_google_bigtable_v2_Mutation_descriptor.getNestedTypes().get(5);
    internal_static_google_bigtable_v2_Mutation_DeleteFromRow_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_Mutation_DeleteFromRow_descriptor,
            new java.lang.String[] {});
    internal_static_google_bigtable_v2_ReadModifyWriteRule_descriptor =
        getDescriptor().getMessageTypes().get(13);
    internal_static_google_bigtable_v2_ReadModifyWriteRule_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ReadModifyWriteRule_descriptor,
            new java.lang.String[] {
              "FamilyName", "ColumnQualifier", "AppendValue", "IncrementAmount", "Rule",
            });
    internal_static_google_bigtable_v2_StreamPartition_descriptor =
        getDescriptor().getMessageTypes().get(14);
    internal_static_google_bigtable_v2_StreamPartition_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_StreamPartition_descriptor,
            new java.lang.String[] {
              "RowRange",
            });
    internal_static_google_bigtable_v2_StreamContinuationTokens_descriptor =
        getDescriptor().getMessageTypes().get(15);
    internal_static_google_bigtable_v2_StreamContinuationTokens_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_StreamContinuationTokens_descriptor,
            new java.lang.String[] {
              "Tokens",
            });
    internal_static_google_bigtable_v2_StreamContinuationToken_descriptor =
        getDescriptor().getMessageTypes().get(16);
    internal_static_google_bigtable_v2_StreamContinuationToken_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_StreamContinuationToken_descriptor,
            new java.lang.String[] {
              "Partition", "Token",
            });
    internal_static_google_bigtable_v2_ProtoFormat_descriptor =
        getDescriptor().getMessageTypes().get(17);
    internal_static_google_bigtable_v2_ProtoFormat_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ProtoFormat_descriptor, new java.lang.String[] {});
    internal_static_google_bigtable_v2_ColumnMetadata_descriptor =
        getDescriptor().getMessageTypes().get(18);
    internal_static_google_bigtable_v2_ColumnMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ColumnMetadata_descriptor,
            new java.lang.String[] {
              "Name", "Type",
            });
    internal_static_google_bigtable_v2_ProtoSchema_descriptor =
        getDescriptor().getMessageTypes().get(19);
    internal_static_google_bigtable_v2_ProtoSchema_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ProtoSchema_descriptor,
            new java.lang.String[] {
              "Columns",
            });
    internal_static_google_bigtable_v2_ResultSetMetadata_descriptor =
        getDescriptor().getMessageTypes().get(20);
    internal_static_google_bigtable_v2_ResultSetMetadata_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ResultSetMetadata_descriptor,
            new java.lang.String[] {
              "ProtoSchema", "Schema",
            });
    internal_static_google_bigtable_v2_ProtoRows_descriptor =
        getDescriptor().getMessageTypes().get(21);
    internal_static_google_bigtable_v2_ProtoRows_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ProtoRows_descriptor,
            new java.lang.String[] {
              "Values",
            });
    internal_static_google_bigtable_v2_ProtoRowsBatch_descriptor =
        getDescriptor().getMessageTypes().get(22);
    internal_static_google_bigtable_v2_ProtoRowsBatch_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_ProtoRowsBatch_descriptor,
            new java.lang.String[] {
              "BatchData",
            });
    internal_static_google_bigtable_v2_PartialResultSet_descriptor =
        getDescriptor().getMessageTypes().get(23);
    internal_static_google_bigtable_v2_PartialResultSet_fieldAccessorTable =
        new com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
            internal_static_google_bigtable_v2_PartialResultSet_descriptor,
            new java.lang.String[] {
              "ProtoRowsBatch", "ResumeToken", "EstimatedBatchSize", "PartialRows",
            });
    com.google.api.FieldBehaviorProto.getDescriptor();
    com.google.bigtable.v2.TypesProto.getDescriptor();
    com.google.protobuf.TimestampProto.getDescriptor();
    com.google.type.DateProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
