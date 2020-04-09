/*
 * Copyright 2019 Google LLC
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
package com.google.bigtable.admin.v2;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 *
 *
 * <pre>
 * Service for creating, configuring, and deleting Cloud Bigtable tables.
 * Provides access to the table schemas only, not the data stored within
 * the tables.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.10.0)",
    comments = "Source: google/bigtable/admin/v2/bigtable_table_admin.proto")
public final class BigtableTableAdminGrpc {

  private BigtableTableAdminGrpc() {}

  public static final String SERVICE_NAME = "google.bigtable.admin.v2.BigtableTableAdmin";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCreateTableMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableRequest, com.google.bigtable.admin.v2.Table>
      METHOD_CREATE_TABLE = getCreateTableMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableRequest, com.google.bigtable.admin.v2.Table>
      getCreateTableMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableRequest, com.google.bigtable.admin.v2.Table>
      getCreateTableMethod() {
    return getCreateTableMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableRequest, com.google.bigtable.admin.v2.Table>
      getCreateTableMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.CreateTableRequest, com.google.bigtable.admin.v2.Table>
        getCreateTableMethod;
    if ((getCreateTableMethod = BigtableTableAdminGrpc.getCreateTableMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getCreateTableMethod = BigtableTableAdminGrpc.getCreateTableMethod) == null) {
          BigtableTableAdminGrpc.getCreateTableMethod =
              getCreateTableMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.CreateTableRequest,
                          com.google.bigtable.admin.v2.Table>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "CreateTable"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.CreateTableRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.Table.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("CreateTable"))
                      .build();
        }
      }
    }
    return getCreateTableMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCreateTableFromSnapshotMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
          com.google.longrunning.Operation>
      METHOD_CREATE_TABLE_FROM_SNAPSHOT = getCreateTableFromSnapshotMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
          com.google.longrunning.Operation>
      getCreateTableFromSnapshotMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
          com.google.longrunning.Operation>
      getCreateTableFromSnapshotMethod() {
    return getCreateTableFromSnapshotMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
          com.google.longrunning.Operation>
      getCreateTableFromSnapshotMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
            com.google.longrunning.Operation>
        getCreateTableFromSnapshotMethod;
    if ((getCreateTableFromSnapshotMethod = BigtableTableAdminGrpc.getCreateTableFromSnapshotMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getCreateTableFromSnapshotMethod =
                BigtableTableAdminGrpc.getCreateTableFromSnapshotMethod)
            == null) {
          BigtableTableAdminGrpc.getCreateTableFromSnapshotMethod =
              getCreateTableFromSnapshotMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin",
                              "CreateTableFromSnapshot"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("CreateTableFromSnapshot"))
                      .build();
        }
      }
    }
    return getCreateTableFromSnapshotMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getListTablesMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListTablesRequest,
          com.google.bigtable.admin.v2.ListTablesResponse>
      METHOD_LIST_TABLES = getListTablesMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListTablesRequest,
          com.google.bigtable.admin.v2.ListTablesResponse>
      getListTablesMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListTablesRequest,
          com.google.bigtable.admin.v2.ListTablesResponse>
      getListTablesMethod() {
    return getListTablesMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListTablesRequest,
          com.google.bigtable.admin.v2.ListTablesResponse>
      getListTablesMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.ListTablesRequest,
            com.google.bigtable.admin.v2.ListTablesResponse>
        getListTablesMethod;
    if ((getListTablesMethod = BigtableTableAdminGrpc.getListTablesMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getListTablesMethod = BigtableTableAdminGrpc.getListTablesMethod) == null) {
          BigtableTableAdminGrpc.getListTablesMethod =
              getListTablesMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.ListTablesRequest,
                          com.google.bigtable.admin.v2.ListTablesResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "ListTables"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListTablesRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListTablesResponse.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("ListTables"))
                      .build();
        }
      }
    }
    return getListTablesMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetTableMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetTableRequest, com.google.bigtable.admin.v2.Table>
      METHOD_GET_TABLE = getGetTableMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetTableRequest, com.google.bigtable.admin.v2.Table>
      getGetTableMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetTableRequest, com.google.bigtable.admin.v2.Table>
      getGetTableMethod() {
    return getGetTableMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetTableRequest, com.google.bigtable.admin.v2.Table>
      getGetTableMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.GetTableRequest, com.google.bigtable.admin.v2.Table>
        getGetTableMethod;
    if ((getGetTableMethod = BigtableTableAdminGrpc.getGetTableMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getGetTableMethod = BigtableTableAdminGrpc.getGetTableMethod) == null) {
          BigtableTableAdminGrpc.getGetTableMethod =
              getGetTableMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.GetTableRequest,
                          com.google.bigtable.admin.v2.Table>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "GetTable"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.GetTableRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.Table.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("GetTable"))
                      .build();
        }
      }
    }
    return getGetTableMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getDeleteTableMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>
      METHOD_DELETE_TABLE = getDeleteTableMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>
      getDeleteTableMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>
      getDeleteTableMethod() {
    return getDeleteTableMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>
      getDeleteTableMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>
        getDeleteTableMethod;
    if ((getDeleteTableMethod = BigtableTableAdminGrpc.getDeleteTableMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getDeleteTableMethod = BigtableTableAdminGrpc.getDeleteTableMethod) == null) {
          BigtableTableAdminGrpc.getDeleteTableMethod =
              getDeleteTableMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "DeleteTable"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.DeleteTableRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.protobuf.Empty.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("DeleteTable"))
                      .build();
        }
      }
    }
    return getDeleteTableMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getModifyColumnFamiliesMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
          com.google.bigtable.admin.v2.Table>
      METHOD_MODIFY_COLUMN_FAMILIES = getModifyColumnFamiliesMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
          com.google.bigtable.admin.v2.Table>
      getModifyColumnFamiliesMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
          com.google.bigtable.admin.v2.Table>
      getModifyColumnFamiliesMethod() {
    return getModifyColumnFamiliesMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
          com.google.bigtable.admin.v2.Table>
      getModifyColumnFamiliesMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
            com.google.bigtable.admin.v2.Table>
        getModifyColumnFamiliesMethod;
    if ((getModifyColumnFamiliesMethod = BigtableTableAdminGrpc.getModifyColumnFamiliesMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getModifyColumnFamiliesMethod = BigtableTableAdminGrpc.getModifyColumnFamiliesMethod)
            == null) {
          BigtableTableAdminGrpc.getModifyColumnFamiliesMethod =
              getModifyColumnFamiliesMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
                          com.google.bigtable.admin.v2.Table>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin",
                              "ModifyColumnFamilies"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.Table.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("ModifyColumnFamilies"))
                      .build();
        }
      }
    }
    return getModifyColumnFamiliesMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getDropRowRangeMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>
      METHOD_DROP_ROW_RANGE = getDropRowRangeMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>
      getDropRowRangeMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>
      getDropRowRangeMethod() {
    return getDropRowRangeMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>
      getDropRowRangeMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>
        getDropRowRangeMethod;
    if ((getDropRowRangeMethod = BigtableTableAdminGrpc.getDropRowRangeMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getDropRowRangeMethod = BigtableTableAdminGrpc.getDropRowRangeMethod) == null) {
          BigtableTableAdminGrpc.getDropRowRangeMethod =
              getDropRowRangeMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "DropRowRange"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.DropRowRangeRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.protobuf.Empty.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("DropRowRange"))
                      .build();
        }
      }
    }
    return getDropRowRangeMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGenerateConsistencyTokenMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
          com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
      METHOD_GENERATE_CONSISTENCY_TOKEN = getGenerateConsistencyTokenMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
          com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
      getGenerateConsistencyTokenMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
          com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
      getGenerateConsistencyTokenMethod() {
    return getGenerateConsistencyTokenMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
          com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
      getGenerateConsistencyTokenMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
            com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
        getGenerateConsistencyTokenMethod;
    if ((getGenerateConsistencyTokenMethod =
            BigtableTableAdminGrpc.getGenerateConsistencyTokenMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getGenerateConsistencyTokenMethod =
                BigtableTableAdminGrpc.getGenerateConsistencyTokenMethod)
            == null) {
          BigtableTableAdminGrpc.getGenerateConsistencyTokenMethod =
              getGenerateConsistencyTokenMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
                          com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin",
                              "GenerateConsistencyToken"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse
                                  .getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier(
                              "GenerateConsistencyToken"))
                      .build();
        }
      }
    }
    return getGenerateConsistencyTokenMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCheckConsistencyMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CheckConsistencyRequest,
          com.google.bigtable.admin.v2.CheckConsistencyResponse>
      METHOD_CHECK_CONSISTENCY = getCheckConsistencyMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CheckConsistencyRequest,
          com.google.bigtable.admin.v2.CheckConsistencyResponse>
      getCheckConsistencyMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CheckConsistencyRequest,
          com.google.bigtable.admin.v2.CheckConsistencyResponse>
      getCheckConsistencyMethod() {
    return getCheckConsistencyMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CheckConsistencyRequest,
          com.google.bigtable.admin.v2.CheckConsistencyResponse>
      getCheckConsistencyMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.CheckConsistencyRequest,
            com.google.bigtable.admin.v2.CheckConsistencyResponse>
        getCheckConsistencyMethod;
    if ((getCheckConsistencyMethod = BigtableTableAdminGrpc.getCheckConsistencyMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getCheckConsistencyMethod = BigtableTableAdminGrpc.getCheckConsistencyMethod)
            == null) {
          BigtableTableAdminGrpc.getCheckConsistencyMethod =
              getCheckConsistencyMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.CheckConsistencyRequest,
                          com.google.bigtable.admin.v2.CheckConsistencyResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "CheckConsistency"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.CheckConsistencyRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.CheckConsistencyResponse
                                  .getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("CheckConsistency"))
                      .build();
        }
      }
    }
    return getCheckConsistencyMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSnapshotTableMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.SnapshotTableRequest, com.google.longrunning.Operation>
      METHOD_SNAPSHOT_TABLE = getSnapshotTableMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.SnapshotTableRequest, com.google.longrunning.Operation>
      getSnapshotTableMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.SnapshotTableRequest, com.google.longrunning.Operation>
      getSnapshotTableMethod() {
    return getSnapshotTableMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.SnapshotTableRequest, com.google.longrunning.Operation>
      getSnapshotTableMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.SnapshotTableRequest, com.google.longrunning.Operation>
        getSnapshotTableMethod;
    if ((getSnapshotTableMethod = BigtableTableAdminGrpc.getSnapshotTableMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getSnapshotTableMethod = BigtableTableAdminGrpc.getSnapshotTableMethod) == null) {
          BigtableTableAdminGrpc.getSnapshotTableMethod =
              getSnapshotTableMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.SnapshotTableRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "SnapshotTable"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.SnapshotTableRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("SnapshotTable"))
                      .build();
        }
      }
    }
    return getSnapshotTableMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetSnapshotMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetSnapshotRequest, com.google.bigtable.admin.v2.Snapshot>
      METHOD_GET_SNAPSHOT = getGetSnapshotMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetSnapshotRequest, com.google.bigtable.admin.v2.Snapshot>
      getGetSnapshotMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetSnapshotRequest, com.google.bigtable.admin.v2.Snapshot>
      getGetSnapshotMethod() {
    return getGetSnapshotMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetSnapshotRequest, com.google.bigtable.admin.v2.Snapshot>
      getGetSnapshotMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.GetSnapshotRequest, com.google.bigtable.admin.v2.Snapshot>
        getGetSnapshotMethod;
    if ((getGetSnapshotMethod = BigtableTableAdminGrpc.getGetSnapshotMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getGetSnapshotMethod = BigtableTableAdminGrpc.getGetSnapshotMethod) == null) {
          BigtableTableAdminGrpc.getGetSnapshotMethod =
              getGetSnapshotMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.GetSnapshotRequest,
                          com.google.bigtable.admin.v2.Snapshot>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "GetSnapshot"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.GetSnapshotRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.Snapshot.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("GetSnapshot"))
                      .build();
        }
      }
    }
    return getGetSnapshotMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getListSnapshotsMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListSnapshotsRequest,
          com.google.bigtable.admin.v2.ListSnapshotsResponse>
      METHOD_LIST_SNAPSHOTS = getListSnapshotsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListSnapshotsRequest,
          com.google.bigtable.admin.v2.ListSnapshotsResponse>
      getListSnapshotsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListSnapshotsRequest,
          com.google.bigtable.admin.v2.ListSnapshotsResponse>
      getListSnapshotsMethod() {
    return getListSnapshotsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListSnapshotsRequest,
          com.google.bigtable.admin.v2.ListSnapshotsResponse>
      getListSnapshotsMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.ListSnapshotsRequest,
            com.google.bigtable.admin.v2.ListSnapshotsResponse>
        getListSnapshotsMethod;
    if ((getListSnapshotsMethod = BigtableTableAdminGrpc.getListSnapshotsMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getListSnapshotsMethod = BigtableTableAdminGrpc.getListSnapshotsMethod) == null) {
          BigtableTableAdminGrpc.getListSnapshotsMethod =
              getListSnapshotsMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.ListSnapshotsRequest,
                          com.google.bigtable.admin.v2.ListSnapshotsResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "ListSnapshots"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListSnapshotsRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListSnapshotsResponse
                                  .getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("ListSnapshots"))
                      .build();
        }
      }
    }
    return getListSnapshotsMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getDeleteSnapshotMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteSnapshotRequest, com.google.protobuf.Empty>
      METHOD_DELETE_SNAPSHOT = getDeleteSnapshotMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteSnapshotRequest, com.google.protobuf.Empty>
      getDeleteSnapshotMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteSnapshotRequest, com.google.protobuf.Empty>
      getDeleteSnapshotMethod() {
    return getDeleteSnapshotMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteSnapshotRequest, com.google.protobuf.Empty>
      getDeleteSnapshotMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.DeleteSnapshotRequest, com.google.protobuf.Empty>
        getDeleteSnapshotMethod;
    if ((getDeleteSnapshotMethod = BigtableTableAdminGrpc.getDeleteSnapshotMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getDeleteSnapshotMethod = BigtableTableAdminGrpc.getDeleteSnapshotMethod) == null) {
          BigtableTableAdminGrpc.getDeleteSnapshotMethod =
              getDeleteSnapshotMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.DeleteSnapshotRequest,
                          com.google.protobuf.Empty>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "DeleteSnapshot"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.DeleteSnapshotRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.protobuf.Empty.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("DeleteSnapshot"))
                      .build();
        }
      }
    }
    return getDeleteSnapshotMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCreateBackupMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateBackupRequest, com.google.longrunning.Operation>
      METHOD_CREATE_BACKUP = getCreateBackupMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateBackupRequest, com.google.longrunning.Operation>
      getCreateBackupMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateBackupRequest, com.google.longrunning.Operation>
      getCreateBackupMethod() {
    return getCreateBackupMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateBackupRequest, com.google.longrunning.Operation>
      getCreateBackupMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.CreateBackupRequest, com.google.longrunning.Operation>
        getCreateBackupMethod;
    if ((getCreateBackupMethod = BigtableTableAdminGrpc.getCreateBackupMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getCreateBackupMethod = BigtableTableAdminGrpc.getCreateBackupMethod) == null) {
          BigtableTableAdminGrpc.getCreateBackupMethod =
              getCreateBackupMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.CreateBackupRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "CreateBackup"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.CreateBackupRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("CreateBackup"))
                      .build();
        }
      }
    }
    return getCreateBackupMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetBackupMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetBackupRequest, com.google.bigtable.admin.v2.Backup>
      METHOD_GET_BACKUP = getGetBackupMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetBackupRequest, com.google.bigtable.admin.v2.Backup>
      getGetBackupMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetBackupRequest, com.google.bigtable.admin.v2.Backup>
      getGetBackupMethod() {
    return getGetBackupMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetBackupRequest, com.google.bigtable.admin.v2.Backup>
      getGetBackupMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.GetBackupRequest, com.google.bigtable.admin.v2.Backup>
        getGetBackupMethod;
    if ((getGetBackupMethod = BigtableTableAdminGrpc.getGetBackupMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getGetBackupMethod = BigtableTableAdminGrpc.getGetBackupMethod) == null) {
          BigtableTableAdminGrpc.getGetBackupMethod =
              getGetBackupMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.GetBackupRequest,
                          com.google.bigtable.admin.v2.Backup>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "GetBackup"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.GetBackupRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.Backup.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("GetBackup"))
                      .build();
        }
      }
    }
    return getGetBackupMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getUpdateBackupMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateBackupRequest, com.google.bigtable.admin.v2.Backup>
      METHOD_UPDATE_BACKUP = getUpdateBackupMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateBackupRequest, com.google.bigtable.admin.v2.Backup>
      getUpdateBackupMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateBackupRequest, com.google.bigtable.admin.v2.Backup>
      getUpdateBackupMethod() {
    return getUpdateBackupMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateBackupRequest, com.google.bigtable.admin.v2.Backup>
      getUpdateBackupMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.UpdateBackupRequest, com.google.bigtable.admin.v2.Backup>
        getUpdateBackupMethod;
    if ((getUpdateBackupMethod = BigtableTableAdminGrpc.getUpdateBackupMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getUpdateBackupMethod = BigtableTableAdminGrpc.getUpdateBackupMethod) == null) {
          BigtableTableAdminGrpc.getUpdateBackupMethod =
              getUpdateBackupMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.UpdateBackupRequest,
                          com.google.bigtable.admin.v2.Backup>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "UpdateBackup"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.UpdateBackupRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.Backup.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("UpdateBackup"))
                      .build();
        }
      }
    }
    return getUpdateBackupMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getDeleteBackupMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>
      METHOD_DELETE_BACKUP = getDeleteBackupMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>
      getDeleteBackupMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>
      getDeleteBackupMethod() {
    return getDeleteBackupMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>
      getDeleteBackupMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>
        getDeleteBackupMethod;
    if ((getDeleteBackupMethod = BigtableTableAdminGrpc.getDeleteBackupMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getDeleteBackupMethod = BigtableTableAdminGrpc.getDeleteBackupMethod) == null) {
          BigtableTableAdminGrpc.getDeleteBackupMethod =
              getDeleteBackupMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "DeleteBackup"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.DeleteBackupRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.protobuf.Empty.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("DeleteBackup"))
                      .build();
        }
      }
    }
    return getDeleteBackupMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getListBackupsMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListBackupsRequest,
          com.google.bigtable.admin.v2.ListBackupsResponse>
      METHOD_LIST_BACKUPS = getListBackupsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListBackupsRequest,
          com.google.bigtable.admin.v2.ListBackupsResponse>
      getListBackupsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListBackupsRequest,
          com.google.bigtable.admin.v2.ListBackupsResponse>
      getListBackupsMethod() {
    return getListBackupsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListBackupsRequest,
          com.google.bigtable.admin.v2.ListBackupsResponse>
      getListBackupsMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.ListBackupsRequest,
            com.google.bigtable.admin.v2.ListBackupsResponse>
        getListBackupsMethod;
    if ((getListBackupsMethod = BigtableTableAdminGrpc.getListBackupsMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getListBackupsMethod = BigtableTableAdminGrpc.getListBackupsMethod) == null) {
          BigtableTableAdminGrpc.getListBackupsMethod =
              getListBackupsMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.ListBackupsRequest,
                          com.google.bigtable.admin.v2.ListBackupsResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "ListBackups"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListBackupsRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListBackupsResponse
                                  .getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("ListBackups"))
                      .build();
        }
      }
    }
    return getListBackupsMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRestoreTableMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.RestoreTableRequest, com.google.longrunning.Operation>
      METHOD_RESTORE_TABLE = getRestoreTableMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.RestoreTableRequest, com.google.longrunning.Operation>
      getRestoreTableMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.RestoreTableRequest, com.google.longrunning.Operation>
      getRestoreTableMethod() {
    return getRestoreTableMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.RestoreTableRequest, com.google.longrunning.Operation>
      getRestoreTableMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.RestoreTableRequest, com.google.longrunning.Operation>
        getRestoreTableMethod;
    if ((getRestoreTableMethod = BigtableTableAdminGrpc.getRestoreTableMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getRestoreTableMethod = BigtableTableAdminGrpc.getRestoreTableMethod) == null) {
          BigtableTableAdminGrpc.getRestoreTableMethod =
              getRestoreTableMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.RestoreTableRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "RestoreTable"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.RestoreTableRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("RestoreTable"))
                      .build();
        }
      }
    }
    return getRestoreTableMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetIamPolicyMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>
      METHOD_GET_IAM_POLICY = getGetIamPolicyMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>
      getGetIamPolicyMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>
      getGetIamPolicyMethod() {
    return getGetIamPolicyMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>
      getGetIamPolicyMethodHelper() {
    io.grpc.MethodDescriptor<com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>
        getGetIamPolicyMethod;
    if ((getGetIamPolicyMethod = BigtableTableAdminGrpc.getGetIamPolicyMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getGetIamPolicyMethod = BigtableTableAdminGrpc.getGetIamPolicyMethod) == null) {
          BigtableTableAdminGrpc.getGetIamPolicyMethod =
              getGetIamPolicyMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "GetIamPolicy"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.iam.v1.GetIamPolicyRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.iam.v1.Policy.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("GetIamPolicy"))
                      .build();
        }
      }
    }
    return getGetIamPolicyMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetIamPolicyMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>
      METHOD_SET_IAM_POLICY = getSetIamPolicyMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>
      getSetIamPolicyMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>
      getSetIamPolicyMethod() {
    return getSetIamPolicyMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>
      getSetIamPolicyMethodHelper() {
    io.grpc.MethodDescriptor<com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>
        getSetIamPolicyMethod;
    if ((getSetIamPolicyMethod = BigtableTableAdminGrpc.getSetIamPolicyMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getSetIamPolicyMethod = BigtableTableAdminGrpc.getSetIamPolicyMethod) == null) {
          BigtableTableAdminGrpc.getSetIamPolicyMethod =
              getSetIamPolicyMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "SetIamPolicy"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.iam.v1.SetIamPolicyRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.iam.v1.Policy.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("SetIamPolicy"))
                      .build();
        }
      }
    }
    return getSetIamPolicyMethod;
  }

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getTestIamPermissionsMethod()} instead.
  public static final io.grpc.MethodDescriptor<
          com.google.iam.v1.TestIamPermissionsRequest, com.google.iam.v1.TestIamPermissionsResponse>
      METHOD_TEST_IAM_PERMISSIONS = getTestIamPermissionsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<
          com.google.iam.v1.TestIamPermissionsRequest, com.google.iam.v1.TestIamPermissionsResponse>
      getTestIamPermissionsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<
          com.google.iam.v1.TestIamPermissionsRequest, com.google.iam.v1.TestIamPermissionsResponse>
      getTestIamPermissionsMethod() {
    return getTestIamPermissionsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<
          com.google.iam.v1.TestIamPermissionsRequest, com.google.iam.v1.TestIamPermissionsResponse>
      getTestIamPermissionsMethodHelper() {
    io.grpc.MethodDescriptor<
            com.google.iam.v1.TestIamPermissionsRequest,
            com.google.iam.v1.TestIamPermissionsResponse>
        getTestIamPermissionsMethod;
    if ((getTestIamPermissionsMethod = BigtableTableAdminGrpc.getTestIamPermissionsMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getTestIamPermissionsMethod = BigtableTableAdminGrpc.getTestIamPermissionsMethod)
            == null) {
          BigtableTableAdminGrpc.getTestIamPermissionsMethod =
              getTestIamPermissionsMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.iam.v1.TestIamPermissionsRequest,
                          com.google.iam.v1.TestIamPermissionsResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(
                              "google.bigtable.admin.v2.BigtableTableAdmin", "TestIamPermissions"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.iam.v1.TestIamPermissionsRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.iam.v1.TestIamPermissionsResponse.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("TestIamPermissions"))
                      .build();
        }
      }
    }
    return getTestIamPermissionsMethod;
  }

  /** Creates a new async stub that supports all call types for the service */
  public static BigtableTableAdminStub newStub(io.grpc.Channel channel) {
    return new BigtableTableAdminStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BigtableTableAdminBlockingStub newBlockingStub(io.grpc.Channel channel) {
    return new BigtableTableAdminBlockingStub(channel);
  }

  /** Creates a new ListenableFuture-style stub that supports unary calls on the service */
  public static BigtableTableAdminFutureStub newFutureStub(io.grpc.Channel channel) {
    return new BigtableTableAdminFutureStub(channel);
  }

  /**
   *
   *
   * <pre>
   * Service for creating, configuring, and deleting Cloud Bigtable tables.
   * Provides access to the table schemas only, not the data stored within
   * the tables.
   * </pre>
   */
  public abstract static class BigtableTableAdminImplBase implements io.grpc.BindableService {

    /**
     *
     *
     * <pre>
     * Creates a new table in the specified instance.
     * The table can be created with a full set of initial column families,
     * specified in the request.
     * </pre>
     */
    public void createTable(
        com.google.bigtable.admin.v2.CreateTableRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateTableMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new table from the specified snapshot. The target table must
     * not exist. The snapshot and the table must be in the same instance.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void createTableFromSnapshot(
        com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateTableFromSnapshotMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all tables served from a specified instance.
     * </pre>
     */
    public void listTables(
        com.google.bigtable.admin.v2.ListTablesRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListTablesResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getListTablesMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified table.
     * </pre>
     */
    public void getTable(
        com.google.bigtable.admin.v2.GetTableRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table> responseObserver) {
      asyncUnimplementedUnaryCall(getGetTableMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified table and all of its data.
     * </pre>
     */
    public void deleteTable(
        com.google.bigtable.admin.v2.DeleteTableRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteTableMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Performs a series of column family modifications on the specified table.
     * Either all or none of the modifications will occur before this method
     * returns, but data requests received prior to that point may see a table
     * where only some modifications have taken effect.
     * </pre>
     */
    public void modifyColumnFamilies(
        com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table> responseObserver) {
      asyncUnimplementedUnaryCall(getModifyColumnFamiliesMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Permanently drop/delete a row range from a specified table. The request can
     * specify whether to delete all rows in a table, or only those that match a
     * particular prefix.
     * </pre>
     */
    public void dropRowRange(
        com.google.bigtable.admin.v2.DropRowRangeRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getDropRowRangeMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Generates a consistency token for a Table, which can be used in
     * CheckConsistency to check whether mutations to the table that finished
     * before this call started have been replicated. The tokens will be available
     * for 90 days.
     * </pre>
     */
    public void generateConsistencyToken(
        com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getGenerateConsistencyTokenMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Checks replication consistency based on a consistency token, that is, if
     * replication has caught up based on the conditions specified in the token
     * and the check request.
     * </pre>
     */
    public void checkConsistency(
        com.google.bigtable.admin.v2.CheckConsistencyRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.CheckConsistencyResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getCheckConsistencyMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new snapshot in the specified cluster from the specified
     * source table. The cluster and the table must be in the same instance.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void snapshotTable(
        com.google.bigtable.admin.v2.SnapshotTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      asyncUnimplementedUnaryCall(getSnapshotTableMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified snapshot.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void getSnapshot(
        com.google.bigtable.admin.v2.GetSnapshotRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Snapshot> responseObserver) {
      asyncUnimplementedUnaryCall(getGetSnapshotMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all snapshots associated with the specified cluster.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void listSnapshots(
        com.google.bigtable.admin.v2.ListSnapshotsRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListSnapshotsResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getListSnapshotsMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes the specified snapshot.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void deleteSnapshot(
        com.google.bigtable.admin.v2.DeleteSnapshotRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteSnapshotMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Starts creating a new Cloud Bigtable Backup. The returned backup
     * [long-running operation][google.longrunning.Operation] can be used to
     * track creation of the backup. The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [CreateBackupMetadata][google.bigtable.admin.v2.CreateBackupMetadata]. The
     * [response][google.longrunning.Operation.response] field type is
     * [Backup][google.bigtable.admin.v2.Backup], if successful. Cancelling the
     * returned operation will stop the creation and delete the backup.
     * </pre>
     */
    public void createBackup(
        com.google.bigtable.admin.v2.CreateBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateBackupMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata on a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    public void getBackup(
        com.google.bigtable.admin.v2.GetBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Backup> responseObserver) {
      asyncUnimplementedUnaryCall(getGetBackupMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    public void updateBackup(
        com.google.bigtable.admin.v2.UpdateBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Backup> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateBackupMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Deletes a pending or completed Cloud Bigtable backup.
     * </pre>
     */
    public void deleteBackup(
        com.google.bigtable.admin.v2.DeleteBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnimplementedUnaryCall(getDeleteBackupMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists Cloud Bigtable backups. Returns both completed and pending
     * backups.
     * </pre>
     */
    public void listBackups(
        com.google.bigtable.admin.v2.ListBackupsRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListBackupsResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getListBackupsMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Create a new table by restoring from a completed backup. The new table
     * must be in the same instance as the instance containing the backup. The
     * returned table [long-running operation][google.longrunning.Operation] can
     * be used to track the progress of the operation, and to cancel it. The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [RestoreTableMetadata][google.bigtable.admin.RestoreTableMetadata]. The
     * [response][google.longrunning.Operation.response] type is
     * [Table][google.bigtable.admin.v2.Table], if successful.
     * </pre>
     */
    public void restoreTable(
        com.google.bigtable.admin.v2.RestoreTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      asyncUnimplementedUnaryCall(getRestoreTableMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets the access control policy for a resource.
     * Returns an empty policy if the resource exists but does not have a policy
     * set.
     * </pre>
     */
    public void getIamPolicy(
        com.google.iam.v1.GetIamPolicyRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.Policy> responseObserver) {
      asyncUnimplementedUnaryCall(getGetIamPolicyMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Sets the access control policy on a Table or Backup resource.
     * Replaces any existing policy.
     * </pre>
     */
    public void setIamPolicy(
        com.google.iam.v1.SetIamPolicyRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.Policy> responseObserver) {
      asyncUnimplementedUnaryCall(getSetIamPolicyMethodHelper(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Returns permissions that the caller has on the specified table resource.
     * </pre>
     */
    public void testIamPermissions(
        com.google.iam.v1.TestIamPermissionsRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.TestIamPermissionsResponse>
            responseObserver) {
      asyncUnimplementedUnaryCall(getTestIamPermissionsMethodHelper(), responseObserver);
    }

    @java.lang.Override
    public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
              getCreateTableMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.CreateTableRequest,
                      com.google.bigtable.admin.v2.Table>(this, METHODID_CREATE_TABLE)))
          .addMethod(
              getCreateTableFromSnapshotMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
                      com.google.longrunning.Operation>(this, METHODID_CREATE_TABLE_FROM_SNAPSHOT)))
          .addMethod(
              getListTablesMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.ListTablesRequest,
                      com.google.bigtable.admin.v2.ListTablesResponse>(this, METHODID_LIST_TABLES)))
          .addMethod(
              getGetTableMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.GetTableRequest,
                      com.google.bigtable.admin.v2.Table>(this, METHODID_GET_TABLE)))
          .addMethod(
              getDeleteTableMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>(
                      this, METHODID_DELETE_TABLE)))
          .addMethod(
              getModifyColumnFamiliesMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
                      com.google.bigtable.admin.v2.Table>(this, METHODID_MODIFY_COLUMN_FAMILIES)))
          .addMethod(
              getDropRowRangeMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>(
                      this, METHODID_DROP_ROW_RANGE)))
          .addMethod(
              getGenerateConsistencyTokenMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
                      com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>(
                      this, METHODID_GENERATE_CONSISTENCY_TOKEN)))
          .addMethod(
              getCheckConsistencyMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.CheckConsistencyRequest,
                      com.google.bigtable.admin.v2.CheckConsistencyResponse>(
                      this, METHODID_CHECK_CONSISTENCY)))
          .addMethod(
              getSnapshotTableMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.SnapshotTableRequest,
                      com.google.longrunning.Operation>(this, METHODID_SNAPSHOT_TABLE)))
          .addMethod(
              getGetSnapshotMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.GetSnapshotRequest,
                      com.google.bigtable.admin.v2.Snapshot>(this, METHODID_GET_SNAPSHOT)))
          .addMethod(
              getListSnapshotsMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.ListSnapshotsRequest,
                      com.google.bigtable.admin.v2.ListSnapshotsResponse>(
                      this, METHODID_LIST_SNAPSHOTS)))
          .addMethod(
              getDeleteSnapshotMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.DeleteSnapshotRequest,
                      com.google.protobuf.Empty>(this, METHODID_DELETE_SNAPSHOT)))
          .addMethod(
              getCreateBackupMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.CreateBackupRequest,
                      com.google.longrunning.Operation>(this, METHODID_CREATE_BACKUP)))
          .addMethod(
              getGetBackupMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.GetBackupRequest,
                      com.google.bigtable.admin.v2.Backup>(this, METHODID_GET_BACKUP)))
          .addMethod(
              getUpdateBackupMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.UpdateBackupRequest,
                      com.google.bigtable.admin.v2.Backup>(this, METHODID_UPDATE_BACKUP)))
          .addMethod(
              getDeleteBackupMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>(
                      this, METHODID_DELETE_BACKUP)))
          .addMethod(
              getListBackupsMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.ListBackupsRequest,
                      com.google.bigtable.admin.v2.ListBackupsResponse>(
                      this, METHODID_LIST_BACKUPS)))
          .addMethod(
              getRestoreTableMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.bigtable.admin.v2.RestoreTableRequest,
                      com.google.longrunning.Operation>(this, METHODID_RESTORE_TABLE)))
          .addMethod(
              getGetIamPolicyMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>(
                      this, METHODID_GET_IAM_POLICY)))
          .addMethod(
              getSetIamPolicyMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>(
                      this, METHODID_SET_IAM_POLICY)))
          .addMethod(
              getTestIamPermissionsMethodHelper(),
              asyncUnaryCall(
                  new MethodHandlers<
                      com.google.iam.v1.TestIamPermissionsRequest,
                      com.google.iam.v1.TestIamPermissionsResponse>(
                      this, METHODID_TEST_IAM_PERMISSIONS)))
          .build();
    }
  }

  /**
   *
   *
   * <pre>
   * Service for creating, configuring, and deleting Cloud Bigtable tables.
   * Provides access to the table schemas only, not the data stored within
   * the tables.
   * </pre>
   */
  public static final class BigtableTableAdminStub
      extends io.grpc.stub.AbstractStub<BigtableTableAdminStub> {
    private BigtableTableAdminStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BigtableTableAdminStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BigtableTableAdminStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BigtableTableAdminStub(channel, callOptions);
    }

    /**
     *
     *
     * <pre>
     * Creates a new table in the specified instance.
     * The table can be created with a full set of initial column families,
     * specified in the request.
     * </pre>
     */
    public void createTable(
        com.google.bigtable.admin.v2.CreateTableRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateTableMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new table from the specified snapshot. The target table must
     * not exist. The snapshot and the table must be in the same instance.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void createTableFromSnapshot(
        com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateTableFromSnapshotMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all tables served from a specified instance.
     * </pre>
     */
    public void listTables(
        com.google.bigtable.admin.v2.ListTablesRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListTablesResponse>
            responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListTablesMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified table.
     * </pre>
     */
    public void getTable(
        com.google.bigtable.admin.v2.GetTableRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetTableMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified table and all of its data.
     * </pre>
     */
    public void deleteTable(
        com.google.bigtable.admin.v2.DeleteTableRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteTableMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Performs a series of column family modifications on the specified table.
     * Either all or none of the modifications will occur before this method
     * returns, but data requests received prior to that point may see a table
     * where only some modifications have taken effect.
     * </pre>
     */
    public void modifyColumnFamilies(
        com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getModifyColumnFamiliesMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Permanently drop/delete a row range from a specified table. The request can
     * specify whether to delete all rows in a table, or only those that match a
     * particular prefix.
     * </pre>
     */
    public void dropRowRange(
        com.google.bigtable.admin.v2.DropRowRangeRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDropRowRangeMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Generates a consistency token for a Table, which can be used in
     * CheckConsistency to check whether mutations to the table that finished
     * before this call started have been replicated. The tokens will be available
     * for 90 days.
     * </pre>
     */
    public void generateConsistencyToken(
        com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
            responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGenerateConsistencyTokenMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Checks replication consistency based on a consistency token, that is, if
     * replication has caught up based on the conditions specified in the token
     * and the check request.
     * </pre>
     */
    public void checkConsistency(
        com.google.bigtable.admin.v2.CheckConsistencyRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.CheckConsistencyResponse>
            responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCheckConsistencyMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new snapshot in the specified cluster from the specified
     * source table. The cluster and the table must be in the same instance.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void snapshotTable(
        com.google.bigtable.admin.v2.SnapshotTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSnapshotTableMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified snapshot.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void getSnapshot(
        com.google.bigtable.admin.v2.GetSnapshotRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Snapshot> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetSnapshotMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all snapshots associated with the specified cluster.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void listSnapshots(
        com.google.bigtable.admin.v2.ListSnapshotsRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListSnapshotsResponse>
            responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListSnapshotsMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes the specified snapshot.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public void deleteSnapshot(
        com.google.bigtable.admin.v2.DeleteSnapshotRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteSnapshotMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Starts creating a new Cloud Bigtable Backup. The returned backup
     * [long-running operation][google.longrunning.Operation] can be used to
     * track creation of the backup. The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [CreateBackupMetadata][google.bigtable.admin.v2.CreateBackupMetadata]. The
     * [response][google.longrunning.Operation.response] field type is
     * [Backup][google.bigtable.admin.v2.Backup], if successful. Cancelling the
     * returned operation will stop the creation and delete the backup.
     * </pre>
     */
    public void createBackup(
        com.google.bigtable.admin.v2.CreateBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateBackupMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata on a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    public void getBackup(
        com.google.bigtable.admin.v2.GetBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Backup> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetBackupMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    public void updateBackup(
        com.google.bigtable.admin.v2.UpdateBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Backup> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateBackupMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Deletes a pending or completed Cloud Bigtable backup.
     * </pre>
     */
    public void deleteBackup(
        com.google.bigtable.admin.v2.DeleteBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDeleteBackupMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists Cloud Bigtable backups. Returns both completed and pending
     * backups.
     * </pre>
     */
    public void listBackups(
        com.google.bigtable.admin.v2.ListBackupsRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListBackupsResponse>
            responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListBackupsMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Create a new table by restoring from a completed backup. The new table
     * must be in the same instance as the instance containing the backup. The
     * returned table [long-running operation][google.longrunning.Operation] can
     * be used to track the progress of the operation, and to cancel it. The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [RestoreTableMetadata][google.bigtable.admin.RestoreTableMetadata]. The
     * [response][google.longrunning.Operation.response] type is
     * [Table][google.bigtable.admin.v2.Table], if successful.
     * </pre>
     */
    public void restoreTable(
        com.google.bigtable.admin.v2.RestoreTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRestoreTableMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets the access control policy for a resource.
     * Returns an empty policy if the resource exists but does not have a policy
     * set.
     * </pre>
     */
    public void getIamPolicy(
        com.google.iam.v1.GetIamPolicyRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.Policy> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetIamPolicyMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Sets the access control policy on a Table or Backup resource.
     * Replaces any existing policy.
     * </pre>
     */
    public void setIamPolicy(
        com.google.iam.v1.SetIamPolicyRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.Policy> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetIamPolicyMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Returns permissions that the caller has on the specified table resource.
     * </pre>
     */
    public void testIamPermissions(
        com.google.iam.v1.TestIamPermissionsRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.TestIamPermissionsResponse>
            responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getTestIamPermissionsMethodHelper(), getCallOptions()),
          request,
          responseObserver);
    }
  }

  /**
   *
   *
   * <pre>
   * Service for creating, configuring, and deleting Cloud Bigtable tables.
   * Provides access to the table schemas only, not the data stored within
   * the tables.
   * </pre>
   */
  public static final class BigtableTableAdminBlockingStub
      extends io.grpc.stub.AbstractStub<BigtableTableAdminBlockingStub> {
    private BigtableTableAdminBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BigtableTableAdminBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BigtableTableAdminBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BigtableTableAdminBlockingStub(channel, callOptions);
    }

    /**
     *
     *
     * <pre>
     * Creates a new table in the specified instance.
     * The table can be created with a full set of initial column families,
     * specified in the request.
     * </pre>
     */
    public com.google.bigtable.admin.v2.Table createTable(
        com.google.bigtable.admin.v2.CreateTableRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateTableMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new table from the specified snapshot. The target table must
     * not exist. The snapshot and the table must be in the same instance.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.longrunning.Operation createTableFromSnapshot(
        com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateTableFromSnapshotMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all tables served from a specified instance.
     * </pre>
     */
    public com.google.bigtable.admin.v2.ListTablesResponse listTables(
        com.google.bigtable.admin.v2.ListTablesRequest request) {
      return blockingUnaryCall(
          getChannel(), getListTablesMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified table.
     * </pre>
     */
    public com.google.bigtable.admin.v2.Table getTable(
        com.google.bigtable.admin.v2.GetTableRequest request) {
      return blockingUnaryCall(getChannel(), getGetTableMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified table and all of its data.
     * </pre>
     */
    public com.google.protobuf.Empty deleteTable(
        com.google.bigtable.admin.v2.DeleteTableRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeleteTableMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Performs a series of column family modifications on the specified table.
     * Either all or none of the modifications will occur before this method
     * returns, but data requests received prior to that point may see a table
     * where only some modifications have taken effect.
     * </pre>
     */
    public com.google.bigtable.admin.v2.Table modifyColumnFamilies(
        com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest request) {
      return blockingUnaryCall(
          getChannel(), getModifyColumnFamiliesMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Permanently drop/delete a row range from a specified table. The request can
     * specify whether to delete all rows in a table, or only those that match a
     * particular prefix.
     * </pre>
     */
    public com.google.protobuf.Empty dropRowRange(
        com.google.bigtable.admin.v2.DropRowRangeRequest request) {
      return blockingUnaryCall(
          getChannel(), getDropRowRangeMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Generates a consistency token for a Table, which can be used in
     * CheckConsistency to check whether mutations to the table that finished
     * before this call started have been replicated. The tokens will be available
     * for 90 days.
     * </pre>
     */
    public com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse generateConsistencyToken(
        com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest request) {
      return blockingUnaryCall(
          getChannel(), getGenerateConsistencyTokenMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Checks replication consistency based on a consistency token, that is, if
     * replication has caught up based on the conditions specified in the token
     * and the check request.
     * </pre>
     */
    public com.google.bigtable.admin.v2.CheckConsistencyResponse checkConsistency(
        com.google.bigtable.admin.v2.CheckConsistencyRequest request) {
      return blockingUnaryCall(
          getChannel(), getCheckConsistencyMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new snapshot in the specified cluster from the specified
     * source table. The cluster and the table must be in the same instance.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.longrunning.Operation snapshotTable(
        com.google.bigtable.admin.v2.SnapshotTableRequest request) {
      return blockingUnaryCall(
          getChannel(), getSnapshotTableMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified snapshot.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.bigtable.admin.v2.Snapshot getSnapshot(
        com.google.bigtable.admin.v2.GetSnapshotRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetSnapshotMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all snapshots associated with the specified cluster.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.bigtable.admin.v2.ListSnapshotsResponse listSnapshots(
        com.google.bigtable.admin.v2.ListSnapshotsRequest request) {
      return blockingUnaryCall(
          getChannel(), getListSnapshotsMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes the specified snapshot.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.protobuf.Empty deleteSnapshot(
        com.google.bigtable.admin.v2.DeleteSnapshotRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeleteSnapshotMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Starts creating a new Cloud Bigtable Backup. The returned backup
     * [long-running operation][google.longrunning.Operation] can be used to
     * track creation of the backup. The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [CreateBackupMetadata][google.bigtable.admin.v2.CreateBackupMetadata]. The
     * [response][google.longrunning.Operation.response] field type is
     * [Backup][google.bigtable.admin.v2.Backup], if successful. Cancelling the
     * returned operation will stop the creation and delete the backup.
     * </pre>
     */
    public com.google.longrunning.Operation createBackup(
        com.google.bigtable.admin.v2.CreateBackupRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateBackupMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata on a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    public com.google.bigtable.admin.v2.Backup getBackup(
        com.google.bigtable.admin.v2.GetBackupRequest request) {
      return blockingUnaryCall(getChannel(), getGetBackupMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Updates a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    public com.google.bigtable.admin.v2.Backup updateBackup(
        com.google.bigtable.admin.v2.UpdateBackupRequest request) {
      return blockingUnaryCall(
          getChannel(), getUpdateBackupMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Deletes a pending or completed Cloud Bigtable backup.
     * </pre>
     */
    public com.google.protobuf.Empty deleteBackup(
        com.google.bigtable.admin.v2.DeleteBackupRequest request) {
      return blockingUnaryCall(
          getChannel(), getDeleteBackupMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Lists Cloud Bigtable backups. Returns both completed and pending
     * backups.
     * </pre>
     */
    public com.google.bigtable.admin.v2.ListBackupsResponse listBackups(
        com.google.bigtable.admin.v2.ListBackupsRequest request) {
      return blockingUnaryCall(
          getChannel(), getListBackupsMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Create a new table by restoring from a completed backup. The new table
     * must be in the same instance as the instance containing the backup. The
     * returned table [long-running operation][google.longrunning.Operation] can
     * be used to track the progress of the operation, and to cancel it. The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [RestoreTableMetadata][google.bigtable.admin.RestoreTableMetadata]. The
     * [response][google.longrunning.Operation.response] type is
     * [Table][google.bigtable.admin.v2.Table], if successful.
     * </pre>
     */
    public com.google.longrunning.Operation restoreTable(
        com.google.bigtable.admin.v2.RestoreTableRequest request) {
      return blockingUnaryCall(
          getChannel(), getRestoreTableMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets the access control policy for a resource.
     * Returns an empty policy if the resource exists but does not have a policy
     * set.
     * </pre>
     */
    public com.google.iam.v1.Policy getIamPolicy(com.google.iam.v1.GetIamPolicyRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetIamPolicyMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Sets the access control policy on a Table or Backup resource.
     * Replaces any existing policy.
     * </pre>
     */
    public com.google.iam.v1.Policy setIamPolicy(com.google.iam.v1.SetIamPolicyRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetIamPolicyMethodHelper(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Returns permissions that the caller has on the specified table resource.
     * </pre>
     */
    public com.google.iam.v1.TestIamPermissionsResponse testIamPermissions(
        com.google.iam.v1.TestIamPermissionsRequest request) {
      return blockingUnaryCall(
          getChannel(), getTestIamPermissionsMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   *
   *
   * <pre>
   * Service for creating, configuring, and deleting Cloud Bigtable tables.
   * Provides access to the table schemas only, not the data stored within
   * the tables.
   * </pre>
   */
  public static final class BigtableTableAdminFutureStub
      extends io.grpc.stub.AbstractStub<BigtableTableAdminFutureStub> {
    private BigtableTableAdminFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BigtableTableAdminFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BigtableTableAdminFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BigtableTableAdminFutureStub(channel, callOptions);
    }

    /**
     *
     *
     * <pre>
     * Creates a new table in the specified instance.
     * The table can be created with a full set of initial column families,
     * specified in the request.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.bigtable.admin.v2.Table>
        createTable(com.google.bigtable.admin.v2.CreateTableRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateTableMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new table from the specified snapshot. The target table must
     * not exist. The snapshot and the table must be in the same instance.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        createTableFromSnapshot(
            com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateTableFromSnapshotMethodHelper(), getCallOptions()),
          request);
    }

    /**
     *
     *
     * <pre>
     * Lists all tables served from a specified instance.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.bigtable.admin.v2.ListTablesResponse>
        listTables(com.google.bigtable.admin.v2.ListTablesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListTablesMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified table.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.bigtable.admin.v2.Table>
        getTable(com.google.bigtable.admin.v2.GetTableRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetTableMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified table and all of its data.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty>
        deleteTable(com.google.bigtable.admin.v2.DeleteTableRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteTableMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Performs a series of column family modifications on the specified table.
     * Either all or none of the modifications will occur before this method
     * returns, but data requests received prior to that point may see a table
     * where only some modifications have taken effect.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.bigtable.admin.v2.Table>
        modifyColumnFamilies(com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getModifyColumnFamiliesMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Permanently drop/delete a row range from a specified table. The request can
     * specify whether to delete all rows in a table, or only those that match a
     * particular prefix.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty>
        dropRowRange(com.google.bigtable.admin.v2.DropRowRangeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDropRowRangeMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Generates a consistency token for a Table, which can be used in
     * CheckConsistency to check whether mutations to the table that finished
     * before this call started have been replicated. The tokens will be available
     * for 90 days.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
        generateConsistencyToken(
            com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGenerateConsistencyTokenMethodHelper(), getCallOptions()),
          request);
    }

    /**
     *
     *
     * <pre>
     * Checks replication consistency based on a consistency token, that is, if
     * replication has caught up based on the conditions specified in the token
     * and the check request.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.bigtable.admin.v2.CheckConsistencyResponse>
        checkConsistency(com.google.bigtable.admin.v2.CheckConsistencyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCheckConsistencyMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new snapshot in the specified cluster from the specified
     * source table. The cluster and the table must be in the same instance.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        snapshotTable(com.google.bigtable.admin.v2.SnapshotTableRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSnapshotTableMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified snapshot.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.bigtable.admin.v2.Snapshot>
        getSnapshot(com.google.bigtable.admin.v2.GetSnapshotRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetSnapshotMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all snapshots associated with the specified cluster.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.bigtable.admin.v2.ListSnapshotsResponse>
        listSnapshots(com.google.bigtable.admin.v2.ListSnapshotsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListSnapshotsMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes the specified snapshot.
     * Note: This is a private alpha release of Cloud Bigtable snapshots. This
     * feature is not currently available to most Cloud Bigtable customers. This
     * feature might be changed in backward-incompatible ways and is not
     * recommended for production use. It is not subject to any SLA or deprecation
     * policy.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty>
        deleteSnapshot(com.google.bigtable.admin.v2.DeleteSnapshotRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteSnapshotMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Starts creating a new Cloud Bigtable Backup. The returned backup
     * [long-running operation][google.longrunning.Operation] can be used to
     * track creation of the backup. The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [CreateBackupMetadata][google.bigtable.admin.v2.CreateBackupMetadata]. The
     * [response][google.longrunning.Operation.response] field type is
     * [Backup][google.bigtable.admin.v2.Backup], if successful. Cancelling the
     * returned operation will stop the creation and delete the backup.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        createBackup(com.google.bigtable.admin.v2.CreateBackupRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateBackupMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata on a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.bigtable.admin.v2.Backup>
        getBackup(com.google.bigtable.admin.v2.GetBackupRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetBackupMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Updates a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.bigtable.admin.v2.Backup>
        updateBackup(com.google.bigtable.admin.v2.UpdateBackupRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateBackupMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Deletes a pending or completed Cloud Bigtable backup.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty>
        deleteBackup(com.google.bigtable.admin.v2.DeleteBackupRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDeleteBackupMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Lists Cloud Bigtable backups. Returns both completed and pending
     * backups.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.bigtable.admin.v2.ListBackupsResponse>
        listBackups(com.google.bigtable.admin.v2.ListBackupsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getListBackupsMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Create a new table by restoring from a completed backup. The new table
     * must be in the same instance as the instance containing the backup. The
     * returned table [long-running operation][google.longrunning.Operation] can
     * be used to track the progress of the operation, and to cancel it. The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [RestoreTableMetadata][google.bigtable.admin.RestoreTableMetadata]. The
     * [response][google.longrunning.Operation.response] type is
     * [Table][google.bigtable.admin.v2.Table], if successful.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        restoreTable(com.google.bigtable.admin.v2.RestoreTableRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRestoreTableMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Gets the access control policy for a resource.
     * Returns an empty policy if the resource exists but does not have a policy
     * set.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.iam.v1.Policy>
        getIamPolicy(com.google.iam.v1.GetIamPolicyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetIamPolicyMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Sets the access control policy on a Table or Backup resource.
     * Replaces any existing policy.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.iam.v1.Policy>
        setIamPolicy(com.google.iam.v1.SetIamPolicyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetIamPolicyMethodHelper(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Returns permissions that the caller has on the specified table resource.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.iam.v1.TestIamPermissionsResponse>
        testIamPermissions(com.google.iam.v1.TestIamPermissionsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getTestIamPermissionsMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_TABLE = 0;
  private static final int METHODID_CREATE_TABLE_FROM_SNAPSHOT = 1;
  private static final int METHODID_LIST_TABLES = 2;
  private static final int METHODID_GET_TABLE = 3;
  private static final int METHODID_DELETE_TABLE = 4;
  private static final int METHODID_MODIFY_COLUMN_FAMILIES = 5;
  private static final int METHODID_DROP_ROW_RANGE = 6;
  private static final int METHODID_GENERATE_CONSISTENCY_TOKEN = 7;
  private static final int METHODID_CHECK_CONSISTENCY = 8;
  private static final int METHODID_SNAPSHOT_TABLE = 9;
  private static final int METHODID_GET_SNAPSHOT = 10;
  private static final int METHODID_LIST_SNAPSHOTS = 11;
  private static final int METHODID_DELETE_SNAPSHOT = 12;
  private static final int METHODID_CREATE_BACKUP = 13;
  private static final int METHODID_GET_BACKUP = 14;
  private static final int METHODID_UPDATE_BACKUP = 15;
  private static final int METHODID_DELETE_BACKUP = 16;
  private static final int METHODID_LIST_BACKUPS = 17;
  private static final int METHODID_RESTORE_TABLE = 18;
  private static final int METHODID_GET_IAM_POLICY = 19;
  private static final int METHODID_SET_IAM_POLICY = 20;
  private static final int METHODID_TEST_IAM_PERMISSIONS = 21;

  private static final class MethodHandlers<Req, Resp>
      implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BigtableTableAdminImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BigtableTableAdminImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_TABLE:
          serviceImpl.createTable(
              (com.google.bigtable.admin.v2.CreateTableRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table>) responseObserver);
          break;
        case METHODID_CREATE_TABLE_FROM_SNAPSHOT:
          serviceImpl.createTableFromSnapshot(
              (com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_LIST_TABLES:
          serviceImpl.listTables(
              (com.google.bigtable.admin.v2.ListTablesRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListTablesResponse>)
                  responseObserver);
          break;
        case METHODID_GET_TABLE:
          serviceImpl.getTable(
              (com.google.bigtable.admin.v2.GetTableRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table>) responseObserver);
          break;
        case METHODID_DELETE_TABLE:
          serviceImpl.deleteTable(
              (com.google.bigtable.admin.v2.DeleteTableRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_MODIFY_COLUMN_FAMILIES:
          serviceImpl.modifyColumnFamilies(
              (com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table>) responseObserver);
          break;
        case METHODID_DROP_ROW_RANGE:
          serviceImpl.dropRowRange(
              (com.google.bigtable.admin.v2.DropRowRangeRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_GENERATE_CONSISTENCY_TOKEN:
          serviceImpl.generateConsistencyToken(
              (com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest) request,
              (io.grpc.stub.StreamObserver<
                      com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>)
                  responseObserver);
          break;
        case METHODID_CHECK_CONSISTENCY:
          serviceImpl.checkConsistency(
              (com.google.bigtable.admin.v2.CheckConsistencyRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.CheckConsistencyResponse>)
                  responseObserver);
          break;
        case METHODID_SNAPSHOT_TABLE:
          serviceImpl.snapshotTable(
              (com.google.bigtable.admin.v2.SnapshotTableRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_GET_SNAPSHOT:
          serviceImpl.getSnapshot(
              (com.google.bigtable.admin.v2.GetSnapshotRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Snapshot>)
                  responseObserver);
          break;
        case METHODID_LIST_SNAPSHOTS:
          serviceImpl.listSnapshots(
              (com.google.bigtable.admin.v2.ListSnapshotsRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListSnapshotsResponse>)
                  responseObserver);
          break;
        case METHODID_DELETE_SNAPSHOT:
          serviceImpl.deleteSnapshot(
              (com.google.bigtable.admin.v2.DeleteSnapshotRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_CREATE_BACKUP:
          serviceImpl.createBackup(
              (com.google.bigtable.admin.v2.CreateBackupRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_GET_BACKUP:
          serviceImpl.getBackup(
              (com.google.bigtable.admin.v2.GetBackupRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Backup>) responseObserver);
          break;
        case METHODID_UPDATE_BACKUP:
          serviceImpl.updateBackup(
              (com.google.bigtable.admin.v2.UpdateBackupRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Backup>) responseObserver);
          break;
        case METHODID_DELETE_BACKUP:
          serviceImpl.deleteBackup(
              (com.google.bigtable.admin.v2.DeleteBackupRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_LIST_BACKUPS:
          serviceImpl.listBackups(
              (com.google.bigtable.admin.v2.ListBackupsRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListBackupsResponse>)
                  responseObserver);
          break;
        case METHODID_RESTORE_TABLE:
          serviceImpl.restoreTable(
              (com.google.bigtable.admin.v2.RestoreTableRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_GET_IAM_POLICY:
          serviceImpl.getIamPolicy(
              (com.google.iam.v1.GetIamPolicyRequest) request,
              (io.grpc.stub.StreamObserver<com.google.iam.v1.Policy>) responseObserver);
          break;
        case METHODID_SET_IAM_POLICY:
          serviceImpl.setIamPolicy(
              (com.google.iam.v1.SetIamPolicyRequest) request,
              (io.grpc.stub.StreamObserver<com.google.iam.v1.Policy>) responseObserver);
          break;
        case METHODID_TEST_IAM_PERMISSIONS:
          serviceImpl.testIamPermissions(
              (com.google.iam.v1.TestIamPermissionsRequest) request,
              (io.grpc.stub.StreamObserver<com.google.iam.v1.TestIamPermissionsResponse>)
                  responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private abstract static class BigtableTableAdminBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier,
          io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BigtableTableAdminBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.google.bigtable.admin.v2.BigtableTableAdminProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BigtableTableAdmin");
    }
  }

  private static final class BigtableTableAdminFileDescriptorSupplier
      extends BigtableTableAdminBaseDescriptorSupplier {
    BigtableTableAdminFileDescriptorSupplier() {}
  }

  private static final class BigtableTableAdminMethodDescriptorSupplier
      extends BigtableTableAdminBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BigtableTableAdminMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor =
              result =
                  io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                      .setSchemaDescriptor(new BigtableTableAdminFileDescriptorSupplier())
                      .addMethod(getCreateTableMethodHelper())
                      .addMethod(getCreateTableFromSnapshotMethodHelper())
                      .addMethod(getListTablesMethodHelper())
                      .addMethod(getGetTableMethodHelper())
                      .addMethod(getDeleteTableMethodHelper())
                      .addMethod(getModifyColumnFamiliesMethodHelper())
                      .addMethod(getDropRowRangeMethodHelper())
                      .addMethod(getGenerateConsistencyTokenMethodHelper())
                      .addMethod(getCheckConsistencyMethodHelper())
                      .addMethod(getSnapshotTableMethodHelper())
                      .addMethod(getGetSnapshotMethodHelper())
                      .addMethod(getListSnapshotsMethodHelper())
                      .addMethod(getDeleteSnapshotMethodHelper())
                      .addMethod(getCreateBackupMethodHelper())
                      .addMethod(getGetBackupMethodHelper())
                      .addMethod(getUpdateBackupMethodHelper())
                      .addMethod(getDeleteBackupMethodHelper())
                      .addMethod(getListBackupsMethodHelper())
                      .addMethod(getRestoreTableMethodHelper())
                      .addMethod(getGetIamPolicyMethodHelper())
                      .addMethod(getSetIamPolicyMethodHelper())
                      .addMethod(getTestIamPermissionsMethodHelper())
                      .build();
        }
      }
    }
    return result;
  }
}
