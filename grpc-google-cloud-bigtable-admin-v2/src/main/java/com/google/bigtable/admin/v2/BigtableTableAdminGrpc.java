/*
 * Copyright 2025 Google LLC
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
    value = "by gRPC proto compiler",
    comments = "Source: google/bigtable/admin/v2/bigtable_table_admin.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BigtableTableAdminGrpc {

  private BigtableTableAdminGrpc() {}

  public static final java.lang.String SERVICE_NAME = "google.bigtable.admin.v2.BigtableTableAdmin";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableRequest, com.google.bigtable.admin.v2.Table>
      getCreateTableMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateTable",
      requestType = com.google.bigtable.admin.v2.CreateTableRequest.class,
      responseType = com.google.bigtable.admin.v2.Table.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableRequest, com.google.bigtable.admin.v2.Table>
      getCreateTableMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateTable"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
          com.google.longrunning.Operation>
      getCreateTableFromSnapshotMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateTableFromSnapshot",
      requestType = com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
          com.google.longrunning.Operation>
      getCreateTableFromSnapshotMethod() {
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
                          generateFullMethodName(SERVICE_NAME, "CreateTableFromSnapshot"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListTablesRequest,
          com.google.bigtable.admin.v2.ListTablesResponse>
      getListTablesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListTables",
      requestType = com.google.bigtable.admin.v2.ListTablesRequest.class,
      responseType = com.google.bigtable.admin.v2.ListTablesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListTablesRequest,
          com.google.bigtable.admin.v2.ListTablesResponse>
      getListTablesMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListTables"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetTableRequest, com.google.bigtable.admin.v2.Table>
      getGetTableMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetTable",
      requestType = com.google.bigtable.admin.v2.GetTableRequest.class,
      responseType = com.google.bigtable.admin.v2.Table.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetTableRequest, com.google.bigtable.admin.v2.Table>
      getGetTableMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetTable"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateTableRequest, com.google.longrunning.Operation>
      getUpdateTableMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateTable",
      requestType = com.google.bigtable.admin.v2.UpdateTableRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateTableRequest, com.google.longrunning.Operation>
      getUpdateTableMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.UpdateTableRequest, com.google.longrunning.Operation>
        getUpdateTableMethod;
    if ((getUpdateTableMethod = BigtableTableAdminGrpc.getUpdateTableMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getUpdateTableMethod = BigtableTableAdminGrpc.getUpdateTableMethod) == null) {
          BigtableTableAdminGrpc.getUpdateTableMethod =
              getUpdateTableMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.UpdateTableRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateTable"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.UpdateTableRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("UpdateTable"))
                      .build();
        }
      }
    }
    return getUpdateTableMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>
      getDeleteTableMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteTable",
      requestType = com.google.bigtable.admin.v2.DeleteTableRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>
      getDeleteTableMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteTable"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UndeleteTableRequest, com.google.longrunning.Operation>
      getUndeleteTableMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UndeleteTable",
      requestType = com.google.bigtable.admin.v2.UndeleteTableRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UndeleteTableRequest, com.google.longrunning.Operation>
      getUndeleteTableMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.UndeleteTableRequest, com.google.longrunning.Operation>
        getUndeleteTableMethod;
    if ((getUndeleteTableMethod = BigtableTableAdminGrpc.getUndeleteTableMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getUndeleteTableMethod = BigtableTableAdminGrpc.getUndeleteTableMethod) == null) {
          BigtableTableAdminGrpc.getUndeleteTableMethod =
              getUndeleteTableMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.UndeleteTableRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UndeleteTable"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.UndeleteTableRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("UndeleteTable"))
                      .build();
        }
      }
    }
    return getUndeleteTableMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateAuthorizedViewRequest,
          com.google.longrunning.Operation>
      getCreateAuthorizedViewMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateAuthorizedView",
      requestType = com.google.bigtable.admin.v2.CreateAuthorizedViewRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateAuthorizedViewRequest,
          com.google.longrunning.Operation>
      getCreateAuthorizedViewMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.CreateAuthorizedViewRequest,
            com.google.longrunning.Operation>
        getCreateAuthorizedViewMethod;
    if ((getCreateAuthorizedViewMethod = BigtableTableAdminGrpc.getCreateAuthorizedViewMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getCreateAuthorizedViewMethod = BigtableTableAdminGrpc.getCreateAuthorizedViewMethod)
            == null) {
          BigtableTableAdminGrpc.getCreateAuthorizedViewMethod =
              getCreateAuthorizedViewMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.CreateAuthorizedViewRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(SERVICE_NAME, "CreateAuthorizedView"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.CreateAuthorizedViewRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("CreateAuthorizedView"))
                      .build();
        }
      }
    }
    return getCreateAuthorizedViewMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListAuthorizedViewsRequest,
          com.google.bigtable.admin.v2.ListAuthorizedViewsResponse>
      getListAuthorizedViewsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListAuthorizedViews",
      requestType = com.google.bigtable.admin.v2.ListAuthorizedViewsRequest.class,
      responseType = com.google.bigtable.admin.v2.ListAuthorizedViewsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListAuthorizedViewsRequest,
          com.google.bigtable.admin.v2.ListAuthorizedViewsResponse>
      getListAuthorizedViewsMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.ListAuthorizedViewsRequest,
            com.google.bigtable.admin.v2.ListAuthorizedViewsResponse>
        getListAuthorizedViewsMethod;
    if ((getListAuthorizedViewsMethod = BigtableTableAdminGrpc.getListAuthorizedViewsMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getListAuthorizedViewsMethod = BigtableTableAdminGrpc.getListAuthorizedViewsMethod)
            == null) {
          BigtableTableAdminGrpc.getListAuthorizedViewsMethod =
              getListAuthorizedViewsMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.ListAuthorizedViewsRequest,
                          com.google.bigtable.admin.v2.ListAuthorizedViewsResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(SERVICE_NAME, "ListAuthorizedViews"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListAuthorizedViewsRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListAuthorizedViewsResponse
                                  .getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("ListAuthorizedViews"))
                      .build();
        }
      }
    }
    return getListAuthorizedViewsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetAuthorizedViewRequest,
          com.google.bigtable.admin.v2.AuthorizedView>
      getGetAuthorizedViewMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAuthorizedView",
      requestType = com.google.bigtable.admin.v2.GetAuthorizedViewRequest.class,
      responseType = com.google.bigtable.admin.v2.AuthorizedView.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetAuthorizedViewRequest,
          com.google.bigtable.admin.v2.AuthorizedView>
      getGetAuthorizedViewMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.GetAuthorizedViewRequest,
            com.google.bigtable.admin.v2.AuthorizedView>
        getGetAuthorizedViewMethod;
    if ((getGetAuthorizedViewMethod = BigtableTableAdminGrpc.getGetAuthorizedViewMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getGetAuthorizedViewMethod = BigtableTableAdminGrpc.getGetAuthorizedViewMethod)
            == null) {
          BigtableTableAdminGrpc.getGetAuthorizedViewMethod =
              getGetAuthorizedViewMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.GetAuthorizedViewRequest,
                          com.google.bigtable.admin.v2.AuthorizedView>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAuthorizedView"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.GetAuthorizedViewRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.AuthorizedView.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("GetAuthorizedView"))
                      .build();
        }
      }
    }
    return getGetAuthorizedViewMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest,
          com.google.longrunning.Operation>
      getUpdateAuthorizedViewMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateAuthorizedView",
      requestType = com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest,
          com.google.longrunning.Operation>
      getUpdateAuthorizedViewMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest,
            com.google.longrunning.Operation>
        getUpdateAuthorizedViewMethod;
    if ((getUpdateAuthorizedViewMethod = BigtableTableAdminGrpc.getUpdateAuthorizedViewMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getUpdateAuthorizedViewMethod = BigtableTableAdminGrpc.getUpdateAuthorizedViewMethod)
            == null) {
          BigtableTableAdminGrpc.getUpdateAuthorizedViewMethod =
              getUpdateAuthorizedViewMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(SERVICE_NAME, "UpdateAuthorizedView"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("UpdateAuthorizedView"))
                      .build();
        }
      }
    }
    return getUpdateAuthorizedViewMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest, com.google.protobuf.Empty>
      getDeleteAuthorizedViewMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteAuthorizedView",
      requestType = com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest, com.google.protobuf.Empty>
      getDeleteAuthorizedViewMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest, com.google.protobuf.Empty>
        getDeleteAuthorizedViewMethod;
    if ((getDeleteAuthorizedViewMethod = BigtableTableAdminGrpc.getDeleteAuthorizedViewMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getDeleteAuthorizedViewMethod = BigtableTableAdminGrpc.getDeleteAuthorizedViewMethod)
            == null) {
          BigtableTableAdminGrpc.getDeleteAuthorizedViewMethod =
              getDeleteAuthorizedViewMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest,
                          com.google.protobuf.Empty>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(
                          generateFullMethodName(SERVICE_NAME, "DeleteAuthorizedView"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.protobuf.Empty.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("DeleteAuthorizedView"))
                      .build();
        }
      }
    }
    return getDeleteAuthorizedViewMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
          com.google.bigtable.admin.v2.Table>
      getModifyColumnFamiliesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ModifyColumnFamilies",
      requestType = com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest.class,
      responseType = com.google.bigtable.admin.v2.Table.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
          com.google.bigtable.admin.v2.Table>
      getModifyColumnFamiliesMethod() {
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
                          generateFullMethodName(SERVICE_NAME, "ModifyColumnFamilies"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>
      getDropRowRangeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DropRowRange",
      requestType = com.google.bigtable.admin.v2.DropRowRangeRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>
      getDropRowRangeMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DropRowRange"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
          com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
      getGenerateConsistencyTokenMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GenerateConsistencyToken",
      requestType = com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest.class,
      responseType = com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
          com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
      getGenerateConsistencyTokenMethod() {
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
                          generateFullMethodName(SERVICE_NAME, "GenerateConsistencyToken"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CheckConsistencyRequest,
          com.google.bigtable.admin.v2.CheckConsistencyResponse>
      getCheckConsistencyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CheckConsistency",
      requestType = com.google.bigtable.admin.v2.CheckConsistencyRequest.class,
      responseType = com.google.bigtable.admin.v2.CheckConsistencyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CheckConsistencyRequest,
          com.google.bigtable.admin.v2.CheckConsistencyResponse>
      getCheckConsistencyMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CheckConsistency"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.SnapshotTableRequest, com.google.longrunning.Operation>
      getSnapshotTableMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SnapshotTable",
      requestType = com.google.bigtable.admin.v2.SnapshotTableRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.SnapshotTableRequest, com.google.longrunning.Operation>
      getSnapshotTableMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SnapshotTable"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetSnapshotRequest, com.google.bigtable.admin.v2.Snapshot>
      getGetSnapshotMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSnapshot",
      requestType = com.google.bigtable.admin.v2.GetSnapshotRequest.class,
      responseType = com.google.bigtable.admin.v2.Snapshot.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetSnapshotRequest, com.google.bigtable.admin.v2.Snapshot>
      getGetSnapshotMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSnapshot"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListSnapshotsRequest,
          com.google.bigtable.admin.v2.ListSnapshotsResponse>
      getListSnapshotsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListSnapshots",
      requestType = com.google.bigtable.admin.v2.ListSnapshotsRequest.class,
      responseType = com.google.bigtable.admin.v2.ListSnapshotsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListSnapshotsRequest,
          com.google.bigtable.admin.v2.ListSnapshotsResponse>
      getListSnapshotsMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListSnapshots"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteSnapshotRequest, com.google.protobuf.Empty>
      getDeleteSnapshotMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteSnapshot",
      requestType = com.google.bigtable.admin.v2.DeleteSnapshotRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteSnapshotRequest, com.google.protobuf.Empty>
      getDeleteSnapshotMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteSnapshot"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateBackupRequest, com.google.longrunning.Operation>
      getCreateBackupMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateBackup",
      requestType = com.google.bigtable.admin.v2.CreateBackupRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateBackupRequest, com.google.longrunning.Operation>
      getCreateBackupMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateBackup"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetBackupRequest, com.google.bigtable.admin.v2.Backup>
      getGetBackupMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBackup",
      requestType = com.google.bigtable.admin.v2.GetBackupRequest.class,
      responseType = com.google.bigtable.admin.v2.Backup.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetBackupRequest, com.google.bigtable.admin.v2.Backup>
      getGetBackupMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBackup"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateBackupRequest, com.google.bigtable.admin.v2.Backup>
      getUpdateBackupMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateBackup",
      requestType = com.google.bigtable.admin.v2.UpdateBackupRequest.class,
      responseType = com.google.bigtable.admin.v2.Backup.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateBackupRequest, com.google.bigtable.admin.v2.Backup>
      getUpdateBackupMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateBackup"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>
      getDeleteBackupMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteBackup",
      requestType = com.google.bigtable.admin.v2.DeleteBackupRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>
      getDeleteBackupMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteBackup"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListBackupsRequest,
          com.google.bigtable.admin.v2.ListBackupsResponse>
      getListBackupsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListBackups",
      requestType = com.google.bigtable.admin.v2.ListBackupsRequest.class,
      responseType = com.google.bigtable.admin.v2.ListBackupsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListBackupsRequest,
          com.google.bigtable.admin.v2.ListBackupsResponse>
      getListBackupsMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListBackups"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.RestoreTableRequest, com.google.longrunning.Operation>
      getRestoreTableMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RestoreTable",
      requestType = com.google.bigtable.admin.v2.RestoreTableRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.RestoreTableRequest, com.google.longrunning.Operation>
      getRestoreTableMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RestoreTable"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CopyBackupRequest, com.google.longrunning.Operation>
      getCopyBackupMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CopyBackup",
      requestType = com.google.bigtable.admin.v2.CopyBackupRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CopyBackupRequest, com.google.longrunning.Operation>
      getCopyBackupMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.CopyBackupRequest, com.google.longrunning.Operation>
        getCopyBackupMethod;
    if ((getCopyBackupMethod = BigtableTableAdminGrpc.getCopyBackupMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getCopyBackupMethod = BigtableTableAdminGrpc.getCopyBackupMethod) == null) {
          BigtableTableAdminGrpc.getCopyBackupMethod =
              getCopyBackupMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.CopyBackupRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CopyBackup"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.CopyBackupRequest.getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("CopyBackup"))
                      .build();
        }
      }
    }
    return getCopyBackupMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>
      getGetIamPolicyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetIamPolicy",
      requestType = com.google.iam.v1.GetIamPolicyRequest.class,
      responseType = com.google.iam.v1.Policy.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>
      getGetIamPolicyMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetIamPolicy"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>
      getSetIamPolicyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetIamPolicy",
      requestType = com.google.iam.v1.SetIamPolicyRequest.class,
      responseType = com.google.iam.v1.Policy.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>
      getSetIamPolicyMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetIamPolicy"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.iam.v1.TestIamPermissionsRequest, com.google.iam.v1.TestIamPermissionsResponse>
      getTestIamPermissionsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "TestIamPermissions",
      requestType = com.google.iam.v1.TestIamPermissionsRequest.class,
      responseType = com.google.iam.v1.TestIamPermissionsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.iam.v1.TestIamPermissionsRequest, com.google.iam.v1.TestIamPermissionsResponse>
      getTestIamPermissionsMethod() {
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
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "TestIamPermissions"))
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

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateSchemaBundleRequest, com.google.longrunning.Operation>
      getCreateSchemaBundleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateSchemaBundle",
      requestType = com.google.bigtable.admin.v2.CreateSchemaBundleRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.CreateSchemaBundleRequest, com.google.longrunning.Operation>
      getCreateSchemaBundleMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.CreateSchemaBundleRequest,
            com.google.longrunning.Operation>
        getCreateSchemaBundleMethod;
    if ((getCreateSchemaBundleMethod = BigtableTableAdminGrpc.getCreateSchemaBundleMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getCreateSchemaBundleMethod = BigtableTableAdminGrpc.getCreateSchemaBundleMethod)
            == null) {
          BigtableTableAdminGrpc.getCreateSchemaBundleMethod =
              getCreateSchemaBundleMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.CreateSchemaBundleRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateSchemaBundle"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.CreateSchemaBundleRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("CreateSchemaBundle"))
                      .build();
        }
      }
    }
    return getCreateSchemaBundleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateSchemaBundleRequest, com.google.longrunning.Operation>
      getUpdateSchemaBundleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateSchemaBundle",
      requestType = com.google.bigtable.admin.v2.UpdateSchemaBundleRequest.class,
      responseType = com.google.longrunning.Operation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.UpdateSchemaBundleRequest, com.google.longrunning.Operation>
      getUpdateSchemaBundleMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.UpdateSchemaBundleRequest,
            com.google.longrunning.Operation>
        getUpdateSchemaBundleMethod;
    if ((getUpdateSchemaBundleMethod = BigtableTableAdminGrpc.getUpdateSchemaBundleMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getUpdateSchemaBundleMethod = BigtableTableAdminGrpc.getUpdateSchemaBundleMethod)
            == null) {
          BigtableTableAdminGrpc.getUpdateSchemaBundleMethod =
              getUpdateSchemaBundleMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.UpdateSchemaBundleRequest,
                          com.google.longrunning.Operation>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateSchemaBundle"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.UpdateSchemaBundleRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.longrunning.Operation.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("UpdateSchemaBundle"))
                      .build();
        }
      }
    }
    return getUpdateSchemaBundleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetSchemaBundleRequest,
          com.google.bigtable.admin.v2.SchemaBundle>
      getGetSchemaBundleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetSchemaBundle",
      requestType = com.google.bigtable.admin.v2.GetSchemaBundleRequest.class,
      responseType = com.google.bigtable.admin.v2.SchemaBundle.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.GetSchemaBundleRequest,
          com.google.bigtable.admin.v2.SchemaBundle>
      getGetSchemaBundleMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.GetSchemaBundleRequest,
            com.google.bigtable.admin.v2.SchemaBundle>
        getGetSchemaBundleMethod;
    if ((getGetSchemaBundleMethod = BigtableTableAdminGrpc.getGetSchemaBundleMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getGetSchemaBundleMethod = BigtableTableAdminGrpc.getGetSchemaBundleMethod) == null) {
          BigtableTableAdminGrpc.getGetSchemaBundleMethod =
              getGetSchemaBundleMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.GetSchemaBundleRequest,
                          com.google.bigtable.admin.v2.SchemaBundle>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetSchemaBundle"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.GetSchemaBundleRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.SchemaBundle.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("GetSchemaBundle"))
                      .build();
        }
      }
    }
    return getGetSchemaBundleMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListSchemaBundlesRequest,
          com.google.bigtable.admin.v2.ListSchemaBundlesResponse>
      getListSchemaBundlesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListSchemaBundles",
      requestType = com.google.bigtable.admin.v2.ListSchemaBundlesRequest.class,
      responseType = com.google.bigtable.admin.v2.ListSchemaBundlesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.ListSchemaBundlesRequest,
          com.google.bigtable.admin.v2.ListSchemaBundlesResponse>
      getListSchemaBundlesMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.ListSchemaBundlesRequest,
            com.google.bigtable.admin.v2.ListSchemaBundlesResponse>
        getListSchemaBundlesMethod;
    if ((getListSchemaBundlesMethod = BigtableTableAdminGrpc.getListSchemaBundlesMethod) == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getListSchemaBundlesMethod = BigtableTableAdminGrpc.getListSchemaBundlesMethod)
            == null) {
          BigtableTableAdminGrpc.getListSchemaBundlesMethod =
              getListSchemaBundlesMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.ListSchemaBundlesRequest,
                          com.google.bigtable.admin.v2.ListSchemaBundlesResponse>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListSchemaBundles"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListSchemaBundlesRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.ListSchemaBundlesResponse
                                  .getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("ListSchemaBundles"))
                      .build();
        }
      }
    }
    return getListSchemaBundlesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteSchemaBundleRequest, com.google.protobuf.Empty>
      getDeleteSchemaBundleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteSchemaBundle",
      requestType = com.google.bigtable.admin.v2.DeleteSchemaBundleRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<
          com.google.bigtable.admin.v2.DeleteSchemaBundleRequest, com.google.protobuf.Empty>
      getDeleteSchemaBundleMethod() {
    io.grpc.MethodDescriptor<
            com.google.bigtable.admin.v2.DeleteSchemaBundleRequest, com.google.protobuf.Empty>
        getDeleteSchemaBundleMethod;
    if ((getDeleteSchemaBundleMethod = BigtableTableAdminGrpc.getDeleteSchemaBundleMethod)
        == null) {
      synchronized (BigtableTableAdminGrpc.class) {
        if ((getDeleteSchemaBundleMethod = BigtableTableAdminGrpc.getDeleteSchemaBundleMethod)
            == null) {
          BigtableTableAdminGrpc.getDeleteSchemaBundleMethod =
              getDeleteSchemaBundleMethod =
                  io.grpc.MethodDescriptor
                      .<com.google.bigtable.admin.v2.DeleteSchemaBundleRequest,
                          com.google.protobuf.Empty>
                          newBuilder()
                      .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                      .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteSchemaBundle"))
                      .setSampledToLocalTracing(true)
                      .setRequestMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.bigtable.admin.v2.DeleteSchemaBundleRequest
                                  .getDefaultInstance()))
                      .setResponseMarshaller(
                          io.grpc.protobuf.ProtoUtils.marshaller(
                              com.google.protobuf.Empty.getDefaultInstance()))
                      .setSchemaDescriptor(
                          new BigtableTableAdminMethodDescriptorSupplier("DeleteSchemaBundle"))
                      .build();
        }
      }
    }
    return getDeleteSchemaBundleMethod;
  }

  /** Creates a new async stub that supports all call types for the service */
  public static BigtableTableAdminStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BigtableTableAdminStub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<BigtableTableAdminStub>() {
          @java.lang.Override
          public BigtableTableAdminStub newStub(
              io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new BigtableTableAdminStub(channel, callOptions);
          }
        };
    return BigtableTableAdminStub.newStub(factory, channel);
  }

  /** Creates a new blocking-style stub that supports all types of calls on the service */
  public static BigtableTableAdminBlockingV2Stub newBlockingV2Stub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BigtableTableAdminBlockingV2Stub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<BigtableTableAdminBlockingV2Stub>() {
          @java.lang.Override
          public BigtableTableAdminBlockingV2Stub newStub(
              io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new BigtableTableAdminBlockingV2Stub(channel, callOptions);
          }
        };
    return BigtableTableAdminBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BigtableTableAdminBlockingStub newBlockingStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BigtableTableAdminBlockingStub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<BigtableTableAdminBlockingStub>() {
          @java.lang.Override
          public BigtableTableAdminBlockingStub newStub(
              io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new BigtableTableAdminBlockingStub(channel, callOptions);
          }
        };
    return BigtableTableAdminBlockingStub.newStub(factory, channel);
  }

  /** Creates a new ListenableFuture-style stub that supports unary calls on the service */
  public static BigtableTableAdminFutureStub newFutureStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BigtableTableAdminFutureStub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<BigtableTableAdminFutureStub>() {
          @java.lang.Override
          public BigtableTableAdminFutureStub newStub(
              io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new BigtableTableAdminFutureStub(channel, callOptions);
          }
        };
    return BigtableTableAdminFutureStub.newStub(factory, channel);
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
  public interface AsyncService {

    /**
     *
     *
     * <pre>
     * Creates a new table in the specified instance.
     * The table can be created with a full set of initial column families,
     * specified in the request.
     * </pre>
     */
    default void createTable(
        com.google.bigtable.admin.v2.CreateTableRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getCreateTableMethod(), responseObserver);
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
    default void createTableFromSnapshot(
        com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getCreateTableFromSnapshotMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all tables served from a specified instance.
     * </pre>
     */
    default void listTables(
        com.google.bigtable.admin.v2.ListTablesRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListTablesResponse>
            responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListTablesMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified table.
     * </pre>
     */
    default void getTable(
        com.google.bigtable.admin.v2.GetTableRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetTableMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates a specified table.
     * </pre>
     */
    default void updateTable(
        com.google.bigtable.admin.v2.UpdateTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getUpdateTableMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified table and all of its data.
     * </pre>
     */
    default void deleteTable(
        com.google.bigtable.admin.v2.DeleteTableRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getDeleteTableMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Restores a specified table which was accidentally deleted.
     * </pre>
     */
    default void undeleteTable(
        com.google.bigtable.admin.v2.UndeleteTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getUndeleteTableMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new AuthorizedView in a table.
     * </pre>
     */
    default void createAuthorizedView(
        com.google.bigtable.admin.v2.CreateAuthorizedViewRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getCreateAuthorizedViewMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all AuthorizedViews from a specific table.
     * </pre>
     */
    default void listAuthorizedViews(
        com.google.bigtable.admin.v2.ListAuthorizedViewsRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListAuthorizedViewsResponse>
            responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getListAuthorizedViewsMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets information from a specified AuthorizedView.
     * </pre>
     */
    default void getAuthorizedView(
        com.google.bigtable.admin.v2.GetAuthorizedViewRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.AuthorizedView> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getGetAuthorizedViewMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates an AuthorizedView in a table.
     * </pre>
     */
    default void updateAuthorizedView(
        com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getUpdateAuthorizedViewMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified AuthorizedView.
     * </pre>
     */
    default void deleteAuthorizedView(
        com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getDeleteAuthorizedViewMethod(), responseObserver);
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
    default void modifyColumnFamilies(
        com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Table> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getModifyColumnFamiliesMethod(), responseObserver);
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
    default void dropRowRange(
        com.google.bigtable.admin.v2.DropRowRangeRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getDropRowRangeMethod(), responseObserver);
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
    default void generateConsistencyToken(
        com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>
            responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getGenerateConsistencyTokenMethod(), responseObserver);
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
    default void checkConsistency(
        com.google.bigtable.admin.v2.CheckConsistencyRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.CheckConsistencyResponse>
            responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getCheckConsistencyMethod(), responseObserver);
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
    default void snapshotTable(
        com.google.bigtable.admin.v2.SnapshotTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getSnapshotTableMethod(), responseObserver);
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
    default void getSnapshot(
        com.google.bigtable.admin.v2.GetSnapshotRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Snapshot> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getGetSnapshotMethod(), responseObserver);
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
    default void listSnapshots(
        com.google.bigtable.admin.v2.ListSnapshotsRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListSnapshotsResponse>
            responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getListSnapshotsMethod(), responseObserver);
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
    default void deleteSnapshot(
        com.google.bigtable.admin.v2.DeleteSnapshotRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getDeleteSnapshotMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Starts creating a new Cloud Bigtable Backup.  The returned backup
     * [long-running operation][google.longrunning.Operation] can be used to
     * track creation of the backup. The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [CreateBackupMetadata][google.bigtable.admin.v2.CreateBackupMetadata]. The
     * [response][google.longrunning.Operation.response] field type is
     * [Backup][google.bigtable.admin.v2.Backup], if successful. Cancelling the
     * returned operation will stop the creation and delete the backup.
     * </pre>
     */
    default void createBackup(
        com.google.bigtable.admin.v2.CreateBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getCreateBackupMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata on a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    default void getBackup(
        com.google.bigtable.admin.v2.GetBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Backup> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBackupMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates a pending or completed Cloud Bigtable Backup.
     * </pre>
     */
    default void updateBackup(
        com.google.bigtable.admin.v2.UpdateBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.Backup> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getUpdateBackupMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Deletes a pending or completed Cloud Bigtable backup.
     * </pre>
     */
    default void deleteBackup(
        com.google.bigtable.admin.v2.DeleteBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getDeleteBackupMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists Cloud Bigtable backups. Returns both completed and pending
     * backups.
     * </pre>
     */
    default void listBackups(
        com.google.bigtable.admin.v2.ListBackupsRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListBackupsResponse>
            responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getListBackupsMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Create a new table by restoring from a completed backup.  The
     * returned table [long-running operation][google.longrunning.Operation] can
     * be used to track the progress of the operation, and to cancel it.  The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [RestoreTableMetadata][google.bigtable.admin.v2.RestoreTableMetadata].  The
     * [response][google.longrunning.Operation.response] type is
     * [Table][google.bigtable.admin.v2.Table], if successful.
     * </pre>
     */
    default void restoreTable(
        com.google.bigtable.admin.v2.RestoreTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getRestoreTableMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Copy a Cloud Bigtable backup to a new backup in the destination cluster
     * located in the destination instance and project.
     * </pre>
     */
    default void copyBackup(
        com.google.bigtable.admin.v2.CopyBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCopyBackupMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets the access control policy for a Bigtable resource.
     * Returns an empty policy if the resource exists but does not have a policy
     * set.
     * </pre>
     */
    default void getIamPolicy(
        com.google.iam.v1.GetIamPolicyRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.Policy> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getGetIamPolicyMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Sets the access control policy on a Bigtable resource.
     * Replaces any existing policy.
     * </pre>
     */
    default void setIamPolicy(
        com.google.iam.v1.SetIamPolicyRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.Policy> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getSetIamPolicyMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Returns permissions that the caller has on the specified Bigtable
     * resource.
     * </pre>
     */
    default void testIamPermissions(
        com.google.iam.v1.TestIamPermissionsRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.TestIamPermissionsResponse>
            responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getTestIamPermissionsMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new schema bundle in the specified table.
     * </pre>
     */
    default void createSchemaBundle(
        com.google.bigtable.admin.v2.CreateSchemaBundleRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getCreateSchemaBundleMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates a schema bundle in the specified table.
     * </pre>
     */
    default void updateSchemaBundle(
        com.google.bigtable.admin.v2.UpdateSchemaBundleRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getUpdateSchemaBundleMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified schema bundle.
     * </pre>
     */
    default void getSchemaBundle(
        com.google.bigtable.admin.v2.GetSchemaBundleRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.SchemaBundle> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getGetSchemaBundleMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all schema bundles associated with the specified table.
     * </pre>
     */
    default void listSchemaBundles(
        com.google.bigtable.admin.v2.ListSchemaBundlesRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListSchemaBundlesResponse>
            responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getListSchemaBundlesMethod(), responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Deletes a schema bundle in the specified table.
     * </pre>
     */
    default void deleteSchemaBundle(
        com.google.bigtable.admin.v2.DeleteSchemaBundleRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(
          getDeleteSchemaBundleMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BigtableTableAdmin.
   *
   * <pre>
   * Service for creating, configuring, and deleting Cloud Bigtable tables.
   * Provides access to the table schemas only, not the data stored within
   * the tables.
   * </pre>
   */
  public abstract static class BigtableTableAdminImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override
    public final io.grpc.ServerServiceDefinition bindService() {
      return BigtableTableAdminGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BigtableTableAdmin.
   *
   * <pre>
   * Service for creating, configuring, and deleting Cloud Bigtable tables.
   * Provides access to the table schemas only, not the data stored within
   * the tables.
   * </pre>
   */
  public static final class BigtableTableAdminStub
      extends io.grpc.stub.AbstractAsyncStub<BigtableTableAdminStub> {
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateTableMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateTableFromSnapshotMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListTablesMethod(), getCallOptions()), request, responseObserver);
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetTableMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates a specified table.
     * </pre>
     */
    public void updateTable(
        com.google.bigtable.admin.v2.UpdateTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateTableMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteTableMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Restores a specified table which was accidentally deleted.
     * </pre>
     */
    public void undeleteTable(
        com.google.bigtable.admin.v2.UndeleteTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUndeleteTableMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new AuthorizedView in a table.
     * </pre>
     */
    public void createAuthorizedView(
        com.google.bigtable.admin.v2.CreateAuthorizedViewRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateAuthorizedViewMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all AuthorizedViews from a specific table.
     * </pre>
     */
    public void listAuthorizedViews(
        com.google.bigtable.admin.v2.ListAuthorizedViewsRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListAuthorizedViewsResponse>
            responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListAuthorizedViewsMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets information from a specified AuthorizedView.
     * </pre>
     */
    public void getAuthorizedView(
        com.google.bigtable.admin.v2.GetAuthorizedViewRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.AuthorizedView> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAuthorizedViewMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates an AuthorizedView in a table.
     * </pre>
     */
    public void updateAuthorizedView(
        com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateAuthorizedViewMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified AuthorizedView.
     * </pre>
     */
    public void deleteAuthorizedView(
        com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteAuthorizedViewMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getModifyColumnFamiliesMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDropRowRangeMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGenerateConsistencyTokenMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCheckConsistencyMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSnapshotTableMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSnapshotMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListSnapshotsMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteSnapshotMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Starts creating a new Cloud Bigtable Backup.  The returned backup
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateBackupMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBackupMethod(), getCallOptions()), request, responseObserver);
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateBackupMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteBackupMethod(), getCallOptions()),
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
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListBackupsMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Create a new table by restoring from a completed backup.  The
     * returned table [long-running operation][google.longrunning.Operation] can
     * be used to track the progress of the operation, and to cancel it.  The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [RestoreTableMetadata][google.bigtable.admin.v2.RestoreTableMetadata].  The
     * [response][google.longrunning.Operation.response] type is
     * [Table][google.bigtable.admin.v2.Table], if successful.
     * </pre>
     */
    public void restoreTable(
        com.google.bigtable.admin.v2.RestoreTableRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRestoreTableMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Copy a Cloud Bigtable backup to a new backup in the destination cluster
     * located in the destination instance and project.
     * </pre>
     */
    public void copyBackup(
        com.google.bigtable.admin.v2.CopyBackupRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCopyBackupMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets the access control policy for a Bigtable resource.
     * Returns an empty policy if the resource exists but does not have a policy
     * set.
     * </pre>
     */
    public void getIamPolicy(
        com.google.iam.v1.GetIamPolicyRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.Policy> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetIamPolicyMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Sets the access control policy on a Bigtable resource.
     * Replaces any existing policy.
     * </pre>
     */
    public void setIamPolicy(
        com.google.iam.v1.SetIamPolicyRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.Policy> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetIamPolicyMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Returns permissions that the caller has on the specified Bigtable
     * resource.
     * </pre>
     */
    public void testIamPermissions(
        com.google.iam.v1.TestIamPermissionsRequest request,
        io.grpc.stub.StreamObserver<com.google.iam.v1.TestIamPermissionsResponse>
            responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getTestIamPermissionsMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Creates a new schema bundle in the specified table.
     * </pre>
     */
    public void createSchemaBundle(
        com.google.bigtable.admin.v2.CreateSchemaBundleRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateSchemaBundleMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Updates a schema bundle in the specified table.
     * </pre>
     */
    public void updateSchemaBundle(
        com.google.bigtable.admin.v2.UpdateSchemaBundleRequest request,
        io.grpc.stub.StreamObserver<com.google.longrunning.Operation> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateSchemaBundleMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified schema bundle.
     * </pre>
     */
    public void getSchemaBundle(
        com.google.bigtable.admin.v2.GetSchemaBundleRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.SchemaBundle> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSchemaBundleMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Lists all schema bundles associated with the specified table.
     * </pre>
     */
    public void listSchemaBundles(
        com.google.bigtable.admin.v2.ListSchemaBundlesRequest request,
        io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListSchemaBundlesResponse>
            responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListSchemaBundlesMethod(), getCallOptions()),
          request,
          responseObserver);
    }

    /**
     *
     *
     * <pre>
     * Deletes a schema bundle in the specified table.
     * </pre>
     */
    public void deleteSchemaBundle(
        com.google.bigtable.admin.v2.DeleteSchemaBundleRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteSchemaBundleMethod(), getCallOptions()),
          request,
          responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BigtableTableAdmin.
   *
   * <pre>
   * Service for creating, configuring, and deleting Cloud Bigtable tables.
   * Provides access to the table schemas only, not the data stored within
   * the tables.
   * </pre>
   */
  public static final class BigtableTableAdminBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<BigtableTableAdminBlockingV2Stub> {
    private BigtableTableAdminBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BigtableTableAdminBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BigtableTableAdminBlockingV2Stub(channel, callOptions);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateTableMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateTableFromSnapshotMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListTablesMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTableMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Updates a specified table.
     * </pre>
     */
    public com.google.longrunning.Operation updateTable(
        com.google.bigtable.admin.v2.UpdateTableRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateTableMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteTableMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Restores a specified table which was accidentally deleted.
     * </pre>
     */
    public com.google.longrunning.Operation undeleteTable(
        com.google.bigtable.admin.v2.UndeleteTableRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUndeleteTableMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new AuthorizedView in a table.
     * </pre>
     */
    public com.google.longrunning.Operation createAuthorizedView(
        com.google.bigtable.admin.v2.CreateAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateAuthorizedViewMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all AuthorizedViews from a specific table.
     * </pre>
     */
    public com.google.bigtable.admin.v2.ListAuthorizedViewsResponse listAuthorizedViews(
        com.google.bigtable.admin.v2.ListAuthorizedViewsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListAuthorizedViewsMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets information from a specified AuthorizedView.
     * </pre>
     */
    public com.google.bigtable.admin.v2.AuthorizedView getAuthorizedView(
        com.google.bigtable.admin.v2.GetAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAuthorizedViewMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Updates an AuthorizedView in a table.
     * </pre>
     */
    public com.google.longrunning.Operation updateAuthorizedView(
        com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateAuthorizedViewMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified AuthorizedView.
     * </pre>
     */
    public com.google.protobuf.Empty deleteAuthorizedView(
        com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteAuthorizedViewMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getModifyColumnFamiliesMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDropRowRangeMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGenerateConsistencyTokenMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckConsistencyMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSnapshotTableMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSnapshotMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListSnapshotsMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteSnapshotMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Starts creating a new Cloud Bigtable Backup.  The returned backup
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateBackupMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBackupMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateBackupMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteBackupMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListBackupsMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Create a new table by restoring from a completed backup.  The
     * returned table [long-running operation][google.longrunning.Operation] can
     * be used to track the progress of the operation, and to cancel it.  The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [RestoreTableMetadata][google.bigtable.admin.v2.RestoreTableMetadata].  The
     * [response][google.longrunning.Operation.response] type is
     * [Table][google.bigtable.admin.v2.Table], if successful.
     * </pre>
     */
    public com.google.longrunning.Operation restoreTable(
        com.google.bigtable.admin.v2.RestoreTableRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRestoreTableMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Copy a Cloud Bigtable backup to a new backup in the destination cluster
     * located in the destination instance and project.
     * </pre>
     */
    public com.google.longrunning.Operation copyBackup(
        com.google.bigtable.admin.v2.CopyBackupRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCopyBackupMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets the access control policy for a Bigtable resource.
     * Returns an empty policy if the resource exists but does not have a policy
     * set.
     * </pre>
     */
    public com.google.iam.v1.Policy getIamPolicy(com.google.iam.v1.GetIamPolicyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetIamPolicyMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Sets the access control policy on a Bigtable resource.
     * Replaces any existing policy.
     * </pre>
     */
    public com.google.iam.v1.Policy setIamPolicy(com.google.iam.v1.SetIamPolicyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetIamPolicyMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Returns permissions that the caller has on the specified Bigtable
     * resource.
     * </pre>
     */
    public com.google.iam.v1.TestIamPermissionsResponse testIamPermissions(
        com.google.iam.v1.TestIamPermissionsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTestIamPermissionsMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new schema bundle in the specified table.
     * </pre>
     */
    public com.google.longrunning.Operation createSchemaBundle(
        com.google.bigtable.admin.v2.CreateSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateSchemaBundleMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Updates a schema bundle in the specified table.
     * </pre>
     */
    public com.google.longrunning.Operation updateSchemaBundle(
        com.google.bigtable.admin.v2.UpdateSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateSchemaBundleMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified schema bundle.
     * </pre>
     */
    public com.google.bigtable.admin.v2.SchemaBundle getSchemaBundle(
        com.google.bigtable.admin.v2.GetSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSchemaBundleMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all schema bundles associated with the specified table.
     * </pre>
     */
    public com.google.bigtable.admin.v2.ListSchemaBundlesResponse listSchemaBundles(
        com.google.bigtable.admin.v2.ListSchemaBundlesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListSchemaBundlesMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Deletes a schema bundle in the specified table.
     * </pre>
     */
    public com.google.protobuf.Empty deleteSchemaBundle(
        com.google.bigtable.admin.v2.DeleteSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteSchemaBundleMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service BigtableTableAdmin.
   *
   * <pre>
   * Service for creating, configuring, and deleting Cloud Bigtable tables.
   * Provides access to the table schemas only, not the data stored within
   * the tables.
   * </pre>
   */
  public static final class BigtableTableAdminBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BigtableTableAdminBlockingStub> {
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateTableMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateTableFromSnapshotMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListTablesMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetTableMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Updates a specified table.
     * </pre>
     */
    public com.google.longrunning.Operation updateTable(
        com.google.bigtable.admin.v2.UpdateTableRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateTableMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteTableMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Restores a specified table which was accidentally deleted.
     * </pre>
     */
    public com.google.longrunning.Operation undeleteTable(
        com.google.bigtable.admin.v2.UndeleteTableRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUndeleteTableMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new AuthorizedView in a table.
     * </pre>
     */
    public com.google.longrunning.Operation createAuthorizedView(
        com.google.bigtable.admin.v2.CreateAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateAuthorizedViewMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all AuthorizedViews from a specific table.
     * </pre>
     */
    public com.google.bigtable.admin.v2.ListAuthorizedViewsResponse listAuthorizedViews(
        com.google.bigtable.admin.v2.ListAuthorizedViewsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListAuthorizedViewsMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets information from a specified AuthorizedView.
     * </pre>
     */
    public com.google.bigtable.admin.v2.AuthorizedView getAuthorizedView(
        com.google.bigtable.admin.v2.GetAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAuthorizedViewMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Updates an AuthorizedView in a table.
     * </pre>
     */
    public com.google.longrunning.Operation updateAuthorizedView(
        com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateAuthorizedViewMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified AuthorizedView.
     * </pre>
     */
    public com.google.protobuf.Empty deleteAuthorizedView(
        com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteAuthorizedViewMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getModifyColumnFamiliesMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDropRowRangeMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGenerateConsistencyTokenMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCheckConsistencyMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSnapshotTableMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSnapshotMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListSnapshotsMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteSnapshotMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Starts creating a new Cloud Bigtable Backup.  The returned backup
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateBackupMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBackupMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateBackupMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteBackupMethod(), getCallOptions(), request);
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
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListBackupsMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Create a new table by restoring from a completed backup.  The
     * returned table [long-running operation][google.longrunning.Operation] can
     * be used to track the progress of the operation, and to cancel it.  The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [RestoreTableMetadata][google.bigtable.admin.v2.RestoreTableMetadata].  The
     * [response][google.longrunning.Operation.response] type is
     * [Table][google.bigtable.admin.v2.Table], if successful.
     * </pre>
     */
    public com.google.longrunning.Operation restoreTable(
        com.google.bigtable.admin.v2.RestoreTableRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRestoreTableMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Copy a Cloud Bigtable backup to a new backup in the destination cluster
     * located in the destination instance and project.
     * </pre>
     */
    public com.google.longrunning.Operation copyBackup(
        com.google.bigtable.admin.v2.CopyBackupRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCopyBackupMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets the access control policy for a Bigtable resource.
     * Returns an empty policy if the resource exists but does not have a policy
     * set.
     * </pre>
     */
    public com.google.iam.v1.Policy getIamPolicy(com.google.iam.v1.GetIamPolicyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetIamPolicyMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Sets the access control policy on a Bigtable resource.
     * Replaces any existing policy.
     * </pre>
     */
    public com.google.iam.v1.Policy setIamPolicy(com.google.iam.v1.SetIamPolicyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetIamPolicyMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Returns permissions that the caller has on the specified Bigtable
     * resource.
     * </pre>
     */
    public com.google.iam.v1.TestIamPermissionsResponse testIamPermissions(
        com.google.iam.v1.TestIamPermissionsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getTestIamPermissionsMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new schema bundle in the specified table.
     * </pre>
     */
    public com.google.longrunning.Operation createSchemaBundle(
        com.google.bigtable.admin.v2.CreateSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateSchemaBundleMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Updates a schema bundle in the specified table.
     * </pre>
     */
    public com.google.longrunning.Operation updateSchemaBundle(
        com.google.bigtable.admin.v2.UpdateSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateSchemaBundleMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified schema bundle.
     * </pre>
     */
    public com.google.bigtable.admin.v2.SchemaBundle getSchemaBundle(
        com.google.bigtable.admin.v2.GetSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSchemaBundleMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all schema bundles associated with the specified table.
     * </pre>
     */
    public com.google.bigtable.admin.v2.ListSchemaBundlesResponse listSchemaBundles(
        com.google.bigtable.admin.v2.ListSchemaBundlesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListSchemaBundlesMethod(), getCallOptions(), request);
    }

    /**
     *
     *
     * <pre>
     * Deletes a schema bundle in the specified table.
     * </pre>
     */
    public com.google.protobuf.Empty deleteSchemaBundle(
        com.google.bigtable.admin.v2.DeleteSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteSchemaBundleMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BigtableTableAdmin.
   *
   * <pre>
   * Service for creating, configuring, and deleting Cloud Bigtable tables.
   * Provides access to the table schemas only, not the data stored within
   * the tables.
   * </pre>
   */
  public static final class BigtableTableAdminFutureStub
      extends io.grpc.stub.AbstractFutureStub<BigtableTableAdminFutureStub> {
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateTableMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateTableFromSnapshotMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListTablesMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetTableMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Updates a specified table.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        updateTable(com.google.bigtable.admin.v2.UpdateTableRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateTableMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteTableMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Restores a specified table which was accidentally deleted.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        undeleteTable(com.google.bigtable.admin.v2.UndeleteTableRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUndeleteTableMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new AuthorizedView in a table.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        createAuthorizedView(com.google.bigtable.admin.v2.CreateAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateAuthorizedViewMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all AuthorizedViews from a specific table.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.bigtable.admin.v2.ListAuthorizedViewsResponse>
        listAuthorizedViews(com.google.bigtable.admin.v2.ListAuthorizedViewsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListAuthorizedViewsMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Gets information from a specified AuthorizedView.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.bigtable.admin.v2.AuthorizedView>
        getAuthorizedView(com.google.bigtable.admin.v2.GetAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAuthorizedViewMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Updates an AuthorizedView in a table.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        updateAuthorizedView(com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateAuthorizedViewMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Permanently deletes a specified AuthorizedView.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty>
        deleteAuthorizedView(com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteAuthorizedViewMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getModifyColumnFamiliesMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDropRowRangeMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGenerateConsistencyTokenMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCheckConsistencyMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSnapshotTableMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSnapshotMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListSnapshotsMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteSnapshotMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Starts creating a new Cloud Bigtable Backup.  The returned backup
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateBackupMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBackupMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateBackupMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteBackupMethod(), getCallOptions()), request);
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
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListBackupsMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Create a new table by restoring from a completed backup.  The
     * returned table [long-running operation][google.longrunning.Operation] can
     * be used to track the progress of the operation, and to cancel it.  The
     * [metadata][google.longrunning.Operation.metadata] field type is
     * [RestoreTableMetadata][google.bigtable.admin.v2.RestoreTableMetadata].  The
     * [response][google.longrunning.Operation.response] type is
     * [Table][google.bigtable.admin.v2.Table], if successful.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        restoreTable(com.google.bigtable.admin.v2.RestoreTableRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRestoreTableMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Copy a Cloud Bigtable backup to a new backup in the destination cluster
     * located in the destination instance and project.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        copyBackup(com.google.bigtable.admin.v2.CopyBackupRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCopyBackupMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Gets the access control policy for a Bigtable resource.
     * Returns an empty policy if the resource exists but does not have a policy
     * set.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.iam.v1.Policy>
        getIamPolicy(com.google.iam.v1.GetIamPolicyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetIamPolicyMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Sets the access control policy on a Bigtable resource.
     * Replaces any existing policy.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.iam.v1.Policy>
        setIamPolicy(com.google.iam.v1.SetIamPolicyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetIamPolicyMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Returns permissions that the caller has on the specified Bigtable
     * resource.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.iam.v1.TestIamPermissionsResponse>
        testIamPermissions(com.google.iam.v1.TestIamPermissionsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getTestIamPermissionsMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Creates a new schema bundle in the specified table.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        createSchemaBundle(com.google.bigtable.admin.v2.CreateSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateSchemaBundleMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Updates a schema bundle in the specified table.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.longrunning.Operation>
        updateSchemaBundle(com.google.bigtable.admin.v2.UpdateSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateSchemaBundleMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Gets metadata information about the specified schema bundle.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.bigtable.admin.v2.SchemaBundle>
        getSchemaBundle(com.google.bigtable.admin.v2.GetSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSchemaBundleMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Lists all schema bundles associated with the specified table.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<
            com.google.bigtable.admin.v2.ListSchemaBundlesResponse>
        listSchemaBundles(com.google.bigtable.admin.v2.ListSchemaBundlesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListSchemaBundlesMethod(), getCallOptions()), request);
    }

    /**
     *
     *
     * <pre>
     * Deletes a schema bundle in the specified table.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty>
        deleteSchemaBundle(com.google.bigtable.admin.v2.DeleteSchemaBundleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteSchemaBundleMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_TABLE = 0;
  private static final int METHODID_CREATE_TABLE_FROM_SNAPSHOT = 1;
  private static final int METHODID_LIST_TABLES = 2;
  private static final int METHODID_GET_TABLE = 3;
  private static final int METHODID_UPDATE_TABLE = 4;
  private static final int METHODID_DELETE_TABLE = 5;
  private static final int METHODID_UNDELETE_TABLE = 6;
  private static final int METHODID_CREATE_AUTHORIZED_VIEW = 7;
  private static final int METHODID_LIST_AUTHORIZED_VIEWS = 8;
  private static final int METHODID_GET_AUTHORIZED_VIEW = 9;
  private static final int METHODID_UPDATE_AUTHORIZED_VIEW = 10;
  private static final int METHODID_DELETE_AUTHORIZED_VIEW = 11;
  private static final int METHODID_MODIFY_COLUMN_FAMILIES = 12;
  private static final int METHODID_DROP_ROW_RANGE = 13;
  private static final int METHODID_GENERATE_CONSISTENCY_TOKEN = 14;
  private static final int METHODID_CHECK_CONSISTENCY = 15;
  private static final int METHODID_SNAPSHOT_TABLE = 16;
  private static final int METHODID_GET_SNAPSHOT = 17;
  private static final int METHODID_LIST_SNAPSHOTS = 18;
  private static final int METHODID_DELETE_SNAPSHOT = 19;
  private static final int METHODID_CREATE_BACKUP = 20;
  private static final int METHODID_GET_BACKUP = 21;
  private static final int METHODID_UPDATE_BACKUP = 22;
  private static final int METHODID_DELETE_BACKUP = 23;
  private static final int METHODID_LIST_BACKUPS = 24;
  private static final int METHODID_RESTORE_TABLE = 25;
  private static final int METHODID_COPY_BACKUP = 26;
  private static final int METHODID_GET_IAM_POLICY = 27;
  private static final int METHODID_SET_IAM_POLICY = 28;
  private static final int METHODID_TEST_IAM_PERMISSIONS = 29;
  private static final int METHODID_CREATE_SCHEMA_BUNDLE = 30;
  private static final int METHODID_UPDATE_SCHEMA_BUNDLE = 31;
  private static final int METHODID_GET_SCHEMA_BUNDLE = 32;
  private static final int METHODID_LIST_SCHEMA_BUNDLES = 33;
  private static final int METHODID_DELETE_SCHEMA_BUNDLE = 34;

  private static final class MethodHandlers<Req, Resp>
      implements io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
          io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
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
        case METHODID_UPDATE_TABLE:
          serviceImpl.updateTable(
              (com.google.bigtable.admin.v2.UpdateTableRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_DELETE_TABLE:
          serviceImpl.deleteTable(
              (com.google.bigtable.admin.v2.DeleteTableRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_UNDELETE_TABLE:
          serviceImpl.undeleteTable(
              (com.google.bigtable.admin.v2.UndeleteTableRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_CREATE_AUTHORIZED_VIEW:
          serviceImpl.createAuthorizedView(
              (com.google.bigtable.admin.v2.CreateAuthorizedViewRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_LIST_AUTHORIZED_VIEWS:
          serviceImpl.listAuthorizedViews(
              (com.google.bigtable.admin.v2.ListAuthorizedViewsRequest) request,
              (io.grpc.stub.StreamObserver<
                      com.google.bigtable.admin.v2.ListAuthorizedViewsResponse>)
                  responseObserver);
          break;
        case METHODID_GET_AUTHORIZED_VIEW:
          serviceImpl.getAuthorizedView(
              (com.google.bigtable.admin.v2.GetAuthorizedViewRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.AuthorizedView>)
                  responseObserver);
          break;
        case METHODID_UPDATE_AUTHORIZED_VIEW:
          serviceImpl.updateAuthorizedView(
              (com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_DELETE_AUTHORIZED_VIEW:
          serviceImpl.deleteAuthorizedView(
              (com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest) request,
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
        case METHODID_COPY_BACKUP:
          serviceImpl.copyBackup(
              (com.google.bigtable.admin.v2.CopyBackupRequest) request,
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
        case METHODID_CREATE_SCHEMA_BUNDLE:
          serviceImpl.createSchemaBundle(
              (com.google.bigtable.admin.v2.CreateSchemaBundleRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_UPDATE_SCHEMA_BUNDLE:
          serviceImpl.updateSchemaBundle(
              (com.google.bigtable.admin.v2.UpdateSchemaBundleRequest) request,
              (io.grpc.stub.StreamObserver<com.google.longrunning.Operation>) responseObserver);
          break;
        case METHODID_GET_SCHEMA_BUNDLE:
          serviceImpl.getSchemaBundle(
              (com.google.bigtable.admin.v2.GetSchemaBundleRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.SchemaBundle>)
                  responseObserver);
          break;
        case METHODID_LIST_SCHEMA_BUNDLES:
          serviceImpl.listSchemaBundles(
              (com.google.bigtable.admin.v2.ListSchemaBundlesRequest) request,
              (io.grpc.stub.StreamObserver<com.google.bigtable.admin.v2.ListSchemaBundlesResponse>)
                  responseObserver);
          break;
        case METHODID_DELETE_SCHEMA_BUNDLE:
          serviceImpl.deleteSchemaBundle(
              (com.google.bigtable.admin.v2.DeleteSchemaBundleRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
            getCreateTableMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.CreateTableRequest,
                    com.google.bigtable.admin.v2.Table>(service, METHODID_CREATE_TABLE)))
        .addMethod(
            getCreateTableFromSnapshotMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest,
                    com.google.longrunning.Operation>(
                    service, METHODID_CREATE_TABLE_FROM_SNAPSHOT)))
        .addMethod(
            getListTablesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.ListTablesRequest,
                    com.google.bigtable.admin.v2.ListTablesResponse>(
                    service, METHODID_LIST_TABLES)))
        .addMethod(
            getGetTableMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.GetTableRequest,
                    com.google.bigtable.admin.v2.Table>(service, METHODID_GET_TABLE)))
        .addMethod(
            getUpdateTableMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.UpdateTableRequest,
                    com.google.longrunning.Operation>(service, METHODID_UPDATE_TABLE)))
        .addMethod(
            getDeleteTableMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.DeleteTableRequest, com.google.protobuf.Empty>(
                    service, METHODID_DELETE_TABLE)))
        .addMethod(
            getUndeleteTableMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.UndeleteTableRequest,
                    com.google.longrunning.Operation>(service, METHODID_UNDELETE_TABLE)))
        .addMethod(
            getCreateAuthorizedViewMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.CreateAuthorizedViewRequest,
                    com.google.longrunning.Operation>(service, METHODID_CREATE_AUTHORIZED_VIEW)))
        .addMethod(
            getListAuthorizedViewsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.ListAuthorizedViewsRequest,
                    com.google.bigtable.admin.v2.ListAuthorizedViewsResponse>(
                    service, METHODID_LIST_AUTHORIZED_VIEWS)))
        .addMethod(
            getGetAuthorizedViewMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.GetAuthorizedViewRequest,
                    com.google.bigtable.admin.v2.AuthorizedView>(
                    service, METHODID_GET_AUTHORIZED_VIEW)))
        .addMethod(
            getUpdateAuthorizedViewMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.UpdateAuthorizedViewRequest,
                    com.google.longrunning.Operation>(service, METHODID_UPDATE_AUTHORIZED_VIEW)))
        .addMethod(
            getDeleteAuthorizedViewMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.DeleteAuthorizedViewRequest,
                    com.google.protobuf.Empty>(service, METHODID_DELETE_AUTHORIZED_VIEW)))
        .addMethod(
            getModifyColumnFamiliesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest,
                    com.google.bigtable.admin.v2.Table>(service, METHODID_MODIFY_COLUMN_FAMILIES)))
        .addMethod(
            getDropRowRangeMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.DropRowRangeRequest, com.google.protobuf.Empty>(
                    service, METHODID_DROP_ROW_RANGE)))
        .addMethod(
            getGenerateConsistencyTokenMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest,
                    com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse>(
                    service, METHODID_GENERATE_CONSISTENCY_TOKEN)))
        .addMethod(
            getCheckConsistencyMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.CheckConsistencyRequest,
                    com.google.bigtable.admin.v2.CheckConsistencyResponse>(
                    service, METHODID_CHECK_CONSISTENCY)))
        .addMethod(
            getSnapshotTableMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.SnapshotTableRequest,
                    com.google.longrunning.Operation>(service, METHODID_SNAPSHOT_TABLE)))
        .addMethod(
            getGetSnapshotMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.GetSnapshotRequest,
                    com.google.bigtable.admin.v2.Snapshot>(service, METHODID_GET_SNAPSHOT)))
        .addMethod(
            getListSnapshotsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.ListSnapshotsRequest,
                    com.google.bigtable.admin.v2.ListSnapshotsResponse>(
                    service, METHODID_LIST_SNAPSHOTS)))
        .addMethod(
            getDeleteSnapshotMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.DeleteSnapshotRequest, com.google.protobuf.Empty>(
                    service, METHODID_DELETE_SNAPSHOT)))
        .addMethod(
            getCreateBackupMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.CreateBackupRequest,
                    com.google.longrunning.Operation>(service, METHODID_CREATE_BACKUP)))
        .addMethod(
            getGetBackupMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.GetBackupRequest,
                    com.google.bigtable.admin.v2.Backup>(service, METHODID_GET_BACKUP)))
        .addMethod(
            getUpdateBackupMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.UpdateBackupRequest,
                    com.google.bigtable.admin.v2.Backup>(service, METHODID_UPDATE_BACKUP)))
        .addMethod(
            getDeleteBackupMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.DeleteBackupRequest, com.google.protobuf.Empty>(
                    service, METHODID_DELETE_BACKUP)))
        .addMethod(
            getListBackupsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.ListBackupsRequest,
                    com.google.bigtable.admin.v2.ListBackupsResponse>(
                    service, METHODID_LIST_BACKUPS)))
        .addMethod(
            getRestoreTableMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.RestoreTableRequest,
                    com.google.longrunning.Operation>(service, METHODID_RESTORE_TABLE)))
        .addMethod(
            getCopyBackupMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.CopyBackupRequest,
                    com.google.longrunning.Operation>(service, METHODID_COPY_BACKUP)))
        .addMethod(
            getGetIamPolicyMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<com.google.iam.v1.GetIamPolicyRequest, com.google.iam.v1.Policy>(
                    service, METHODID_GET_IAM_POLICY)))
        .addMethod(
            getSetIamPolicyMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<com.google.iam.v1.SetIamPolicyRequest, com.google.iam.v1.Policy>(
                    service, METHODID_SET_IAM_POLICY)))
        .addMethod(
            getTestIamPermissionsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.iam.v1.TestIamPermissionsRequest,
                    com.google.iam.v1.TestIamPermissionsResponse>(
                    service, METHODID_TEST_IAM_PERMISSIONS)))
        .addMethod(
            getCreateSchemaBundleMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.CreateSchemaBundleRequest,
                    com.google.longrunning.Operation>(service, METHODID_CREATE_SCHEMA_BUNDLE)))
        .addMethod(
            getUpdateSchemaBundleMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.UpdateSchemaBundleRequest,
                    com.google.longrunning.Operation>(service, METHODID_UPDATE_SCHEMA_BUNDLE)))
        .addMethod(
            getGetSchemaBundleMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.GetSchemaBundleRequest,
                    com.google.bigtable.admin.v2.SchemaBundle>(
                    service, METHODID_GET_SCHEMA_BUNDLE)))
        .addMethod(
            getListSchemaBundlesMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.ListSchemaBundlesRequest,
                    com.google.bigtable.admin.v2.ListSchemaBundlesResponse>(
                    service, METHODID_LIST_SCHEMA_BUNDLES)))
        .addMethod(
            getDeleteSchemaBundleMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
                new MethodHandlers<
                    com.google.bigtable.admin.v2.DeleteSchemaBundleRequest,
                    com.google.protobuf.Empty>(service, METHODID_DELETE_SCHEMA_BUNDLE)))
        .build();
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
    private final java.lang.String methodName;

    BigtableTableAdminMethodDescriptorSupplier(java.lang.String methodName) {
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
                      .addMethod(getCreateTableMethod())
                      .addMethod(getCreateTableFromSnapshotMethod())
                      .addMethod(getListTablesMethod())
                      .addMethod(getGetTableMethod())
                      .addMethod(getUpdateTableMethod())
                      .addMethod(getDeleteTableMethod())
                      .addMethod(getUndeleteTableMethod())
                      .addMethod(getCreateAuthorizedViewMethod())
                      .addMethod(getListAuthorizedViewsMethod())
                      .addMethod(getGetAuthorizedViewMethod())
                      .addMethod(getUpdateAuthorizedViewMethod())
                      .addMethod(getDeleteAuthorizedViewMethod())
                      .addMethod(getModifyColumnFamiliesMethod())
                      .addMethod(getDropRowRangeMethod())
                      .addMethod(getGenerateConsistencyTokenMethod())
                      .addMethod(getCheckConsistencyMethod())
                      .addMethod(getSnapshotTableMethod())
                      .addMethod(getGetSnapshotMethod())
                      .addMethod(getListSnapshotsMethod())
                      .addMethod(getDeleteSnapshotMethod())
                      .addMethod(getCreateBackupMethod())
                      .addMethod(getGetBackupMethod())
                      .addMethod(getUpdateBackupMethod())
                      .addMethod(getDeleteBackupMethod())
                      .addMethod(getListBackupsMethod())
                      .addMethod(getRestoreTableMethod())
                      .addMethod(getCopyBackupMethod())
                      .addMethod(getGetIamPolicyMethod())
                      .addMethod(getSetIamPolicyMethod())
                      .addMethod(getTestIamPermissionsMethod())
                      .addMethod(getCreateSchemaBundleMethod())
                      .addMethod(getUpdateSchemaBundleMethod())
                      .addMethod(getGetSchemaBundleMethod())
                      .addMethod(getListSchemaBundlesMethod())
                      .addMethod(getDeleteSchemaBundleMethod())
                      .build();
        }
      }
    }
    return result;
  }
}
