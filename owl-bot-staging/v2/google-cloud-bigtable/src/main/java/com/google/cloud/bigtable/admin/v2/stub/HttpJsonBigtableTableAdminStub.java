/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.bigtable.admin.v2.stub;

import static com.google.cloud.bigtable.admin.v2.BaseBigtableTableAdminClient.ListBackupsPagedResponse;
import static com.google.cloud.bigtable.admin.v2.BaseBigtableTableAdminClient.ListSnapshotsPagedResponse;
import static com.google.cloud.bigtable.admin.v2.BaseBigtableTableAdminClient.ListTablesPagedResponse;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.api.gax.core.BackgroundResource;
import com.google.api.gax.core.BackgroundResourceAggregation;
import com.google.api.gax.httpjson.ApiMethodDescriptor;
import com.google.api.gax.httpjson.HttpJsonCallSettings;
import com.google.api.gax.httpjson.HttpJsonOperationSnapshot;
import com.google.api.gax.httpjson.HttpJsonStubCallableFactory;
import com.google.api.gax.httpjson.ProtoMessageRequestFormatter;
import com.google.api.gax.httpjson.ProtoMessageResponseParser;
import com.google.api.gax.httpjson.ProtoRestSerializer;
import com.google.api.gax.httpjson.longrunning.stub.HttpJsonOperationsStub;
import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.OperationCallable;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.admin.v2.Backup;
import com.google.bigtable.admin.v2.CheckConsistencyRequest;
import com.google.bigtable.admin.v2.CheckConsistencyResponse;
import com.google.bigtable.admin.v2.CreateBackupMetadata;
import com.google.bigtable.admin.v2.CreateBackupRequest;
import com.google.bigtable.admin.v2.CreateTableFromSnapshotMetadata;
import com.google.bigtable.admin.v2.CreateTableFromSnapshotRequest;
import com.google.bigtable.admin.v2.CreateTableRequest;
import com.google.bigtable.admin.v2.DeleteBackupRequest;
import com.google.bigtable.admin.v2.DeleteSnapshotRequest;
import com.google.bigtable.admin.v2.DeleteTableRequest;
import com.google.bigtable.admin.v2.DropRowRangeRequest;
import com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest;
import com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse;
import com.google.bigtable.admin.v2.GetBackupRequest;
import com.google.bigtable.admin.v2.GetSnapshotRequest;
import com.google.bigtable.admin.v2.GetTableRequest;
import com.google.bigtable.admin.v2.ListBackupsRequest;
import com.google.bigtable.admin.v2.ListBackupsResponse;
import com.google.bigtable.admin.v2.ListSnapshotsRequest;
import com.google.bigtable.admin.v2.ListSnapshotsResponse;
import com.google.bigtable.admin.v2.ListTablesRequest;
import com.google.bigtable.admin.v2.ListTablesResponse;
import com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest;
import com.google.bigtable.admin.v2.RestoreTableMetadata;
import com.google.bigtable.admin.v2.RestoreTableRequest;
import com.google.bigtable.admin.v2.Snapshot;
import com.google.bigtable.admin.v2.SnapshotTableMetadata;
import com.google.bigtable.admin.v2.SnapshotTableRequest;
import com.google.bigtable.admin.v2.Table;
import com.google.bigtable.admin.v2.UndeleteTableMetadata;
import com.google.bigtable.admin.v2.UndeleteTableRequest;
import com.google.bigtable.admin.v2.UpdateBackupRequest;
import com.google.iam.v1.GetIamPolicyRequest;
import com.google.iam.v1.Policy;
import com.google.iam.v1.SetIamPolicyRequest;
import com.google.iam.v1.TestIamPermissionsRequest;
import com.google.iam.v1.TestIamPermissionsResponse;
import com.google.longrunning.Operation;
import com.google.protobuf.Empty;
import com.google.protobuf.TypeRegistry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Generated;

// AUTO-GENERATED DOCUMENTATION AND CLASS.
/**
 * REST stub implementation for the BigtableTableAdmin service API.
 *
 * <p>This class is for advanced usage and reflects the underlying API directly.
 */
@Generated("by gapic-generator-java")
@BetaApi
public class HttpJsonBigtableTableAdminStub extends BigtableTableAdminStub {
  private static final TypeRegistry typeRegistry =
      TypeRegistry.newBuilder()
          .add(Snapshot.getDescriptor())
          .add(UndeleteTableMetadata.getDescriptor())
          .add(Backup.getDescriptor())
          .add(Table.getDescriptor())
          .add(CreateTableFromSnapshotMetadata.getDescriptor())
          .add(RestoreTableMetadata.getDescriptor())
          .add(CreateBackupMetadata.getDescriptor())
          .add(SnapshotTableMetadata.getDescriptor())
          .build();

  private static final ApiMethodDescriptor<CreateTableRequest, Table> createTableMethodDescriptor =
      ApiMethodDescriptor.<CreateTableRequest, Table>newBuilder()
          .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/CreateTable")
          .setHttpMethod("POST")
          .setType(ApiMethodDescriptor.MethodType.UNARY)
          .setRequestFormatter(
              ProtoMessageRequestFormatter.<CreateTableRequest>newBuilder()
                  .setPath(
                      "/v2/{parent=projects/*/instances/*}/tables",
                      request -> {
                        Map<String, String> fields = new HashMap<>();
                        ProtoRestSerializer<CreateTableRequest> serializer =
                            ProtoRestSerializer.create();
                        serializer.putPathParam(fields, "parent", request.getParent());
                        return fields;
                      })
                  .setQueryParamsExtractor(
                      request -> {
                        Map<String, List<String>> fields = new HashMap<>();
                        ProtoRestSerializer<CreateTableRequest> serializer =
                            ProtoRestSerializer.create();
                        return fields;
                      })
                  .setRequestBodyExtractor(
                      request ->
                          ProtoRestSerializer.create()
                              .toBody("*", request.toBuilder().clearParent().build()))
                  .build())
          .setResponseParser(
              ProtoMessageResponseParser.<Table>newBuilder()
                  .setDefaultInstance(Table.getDefaultInstance())
                  .setDefaultTypeRegistry(typeRegistry)
                  .build())
          .build();

  private static final ApiMethodDescriptor<CreateTableFromSnapshotRequest, Operation>
      createTableFromSnapshotMethodDescriptor =
          ApiMethodDescriptor.<CreateTableFromSnapshotRequest, Operation>newBuilder()
              .setFullMethodName(
                  "google.bigtable.admin.v2.BigtableTableAdmin/CreateTableFromSnapshot")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<CreateTableFromSnapshotRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*}/tables:createFromSnapshot",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<CreateTableFromSnapshotRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<CreateTableFromSnapshotRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearParent().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Operation>newBuilder()
                      .setDefaultInstance(Operation.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .setOperationSnapshotFactory(
                  (CreateTableFromSnapshotRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<ListTablesRequest, ListTablesResponse>
      listTablesMethodDescriptor =
          ApiMethodDescriptor.<ListTablesRequest, ListTablesResponse>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/ListTables")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ListTablesRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*}/tables",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ListTablesRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ListTablesRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "pageSize", request.getPageSize());
                            serializer.putQueryParam(fields, "pageToken", request.getPageToken());
                            serializer.putQueryParam(fields, "view", request.getView());
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<ListTablesResponse>newBuilder()
                      .setDefaultInstance(ListTablesResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<GetTableRequest, Table> getTableMethodDescriptor =
      ApiMethodDescriptor.<GetTableRequest, Table>newBuilder()
          .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/GetTable")
          .setHttpMethod("GET")
          .setType(ApiMethodDescriptor.MethodType.UNARY)
          .setRequestFormatter(
              ProtoMessageRequestFormatter.<GetTableRequest>newBuilder()
                  .setPath(
                      "/v2/{name=projects/*/instances/*/tables/*}",
                      request -> {
                        Map<String, String> fields = new HashMap<>();
                        ProtoRestSerializer<GetTableRequest> serializer =
                            ProtoRestSerializer.create();
                        serializer.putPathParam(fields, "name", request.getName());
                        return fields;
                      })
                  .setQueryParamsExtractor(
                      request -> {
                        Map<String, List<String>> fields = new HashMap<>();
                        ProtoRestSerializer<GetTableRequest> serializer =
                            ProtoRestSerializer.create();
                        serializer.putQueryParam(fields, "view", request.getView());
                        return fields;
                      })
                  .setRequestBodyExtractor(request -> null)
                  .build())
          .setResponseParser(
              ProtoMessageResponseParser.<Table>newBuilder()
                  .setDefaultInstance(Table.getDefaultInstance())
                  .setDefaultTypeRegistry(typeRegistry)
                  .build())
          .build();

  private static final ApiMethodDescriptor<DeleteTableRequest, Empty> deleteTableMethodDescriptor =
      ApiMethodDescriptor.<DeleteTableRequest, Empty>newBuilder()
          .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/DeleteTable")
          .setHttpMethod("DELETE")
          .setType(ApiMethodDescriptor.MethodType.UNARY)
          .setRequestFormatter(
              ProtoMessageRequestFormatter.<DeleteTableRequest>newBuilder()
                  .setPath(
                      "/v2/{name=projects/*/instances/*/tables/*}",
                      request -> {
                        Map<String, String> fields = new HashMap<>();
                        ProtoRestSerializer<DeleteTableRequest> serializer =
                            ProtoRestSerializer.create();
                        serializer.putPathParam(fields, "name", request.getName());
                        return fields;
                      })
                  .setQueryParamsExtractor(
                      request -> {
                        Map<String, List<String>> fields = new HashMap<>();
                        ProtoRestSerializer<DeleteTableRequest> serializer =
                            ProtoRestSerializer.create();
                        return fields;
                      })
                  .setRequestBodyExtractor(request -> null)
                  .build())
          .setResponseParser(
              ProtoMessageResponseParser.<Empty>newBuilder()
                  .setDefaultInstance(Empty.getDefaultInstance())
                  .setDefaultTypeRegistry(typeRegistry)
                  .build())
          .build();

  private static final ApiMethodDescriptor<UndeleteTableRequest, Operation>
      undeleteTableMethodDescriptor =
          ApiMethodDescriptor.<UndeleteTableRequest, Operation>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/UndeleteTable")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<UndeleteTableRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/tables/*}:undelete",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<UndeleteTableRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<UndeleteTableRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Operation>newBuilder()
                      .setDefaultInstance(Operation.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .setOperationSnapshotFactory(
                  (UndeleteTableRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<ModifyColumnFamiliesRequest, Table>
      modifyColumnFamiliesMethodDescriptor =
          ApiMethodDescriptor.<ModifyColumnFamiliesRequest, Table>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/ModifyColumnFamilies")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ModifyColumnFamiliesRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/tables/*}:modifyColumnFamilies",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ModifyColumnFamiliesRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ModifyColumnFamiliesRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Table>newBuilder()
                      .setDefaultInstance(Table.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<DropRowRangeRequest, Empty>
      dropRowRangeMethodDescriptor =
          ApiMethodDescriptor.<DropRowRangeRequest, Empty>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/DropRowRange")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<DropRowRangeRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/tables/*}:dropRowRange",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<DropRowRangeRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<DropRowRangeRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Empty>newBuilder()
                      .setDefaultInstance(Empty.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<
          GenerateConsistencyTokenRequest, GenerateConsistencyTokenResponse>
      generateConsistencyTokenMethodDescriptor =
          ApiMethodDescriptor
              .<GenerateConsistencyTokenRequest, GenerateConsistencyTokenResponse>newBuilder()
              .setFullMethodName(
                  "google.bigtable.admin.v2.BigtableTableAdmin/GenerateConsistencyToken")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<GenerateConsistencyTokenRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/tables/*}:generateConsistencyToken",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<GenerateConsistencyTokenRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<GenerateConsistencyTokenRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<GenerateConsistencyTokenResponse>newBuilder()
                      .setDefaultInstance(GenerateConsistencyTokenResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<CheckConsistencyRequest, CheckConsistencyResponse>
      checkConsistencyMethodDescriptor =
          ApiMethodDescriptor.<CheckConsistencyRequest, CheckConsistencyResponse>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/CheckConsistency")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<CheckConsistencyRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/tables/*}:checkConsistency",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<CheckConsistencyRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<CheckConsistencyRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<CheckConsistencyResponse>newBuilder()
                      .setDefaultInstance(CheckConsistencyResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<SnapshotTableRequest, Operation>
      snapshotTableMethodDescriptor =
          ApiMethodDescriptor.<SnapshotTableRequest, Operation>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/SnapshotTable")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<SnapshotTableRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/tables/*}:snapshot",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<SnapshotTableRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<SnapshotTableRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Operation>newBuilder()
                      .setDefaultInstance(Operation.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .setOperationSnapshotFactory(
                  (SnapshotTableRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<GetSnapshotRequest, Snapshot>
      getSnapshotMethodDescriptor =
          ApiMethodDescriptor.<GetSnapshotRequest, Snapshot>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/GetSnapshot")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<GetSnapshotRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/clusters/*/snapshots/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<GetSnapshotRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<GetSnapshotRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Snapshot>newBuilder()
                      .setDefaultInstance(Snapshot.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<ListSnapshotsRequest, ListSnapshotsResponse>
      listSnapshotsMethodDescriptor =
          ApiMethodDescriptor.<ListSnapshotsRequest, ListSnapshotsResponse>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/ListSnapshots")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ListSnapshotsRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*/clusters/*}/snapshots",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ListSnapshotsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ListSnapshotsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "pageSize", request.getPageSize());
                            serializer.putQueryParam(fields, "pageToken", request.getPageToken());
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<ListSnapshotsResponse>newBuilder()
                      .setDefaultInstance(ListSnapshotsResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<DeleteSnapshotRequest, Empty>
      deleteSnapshotMethodDescriptor =
          ApiMethodDescriptor.<DeleteSnapshotRequest, Empty>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/DeleteSnapshot")
              .setHttpMethod("DELETE")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<DeleteSnapshotRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/clusters/*/snapshots/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteSnapshotRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteSnapshotRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Empty>newBuilder()
                      .setDefaultInstance(Empty.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<CreateBackupRequest, Operation>
      createBackupMethodDescriptor =
          ApiMethodDescriptor.<CreateBackupRequest, Operation>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/CreateBackup")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<CreateBackupRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*/clusters/*}/backups",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<CreateBackupRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<CreateBackupRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "backupId", request.getBackupId());
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create().toBody("backup", request.getBackup()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Operation>newBuilder()
                      .setDefaultInstance(Operation.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .setOperationSnapshotFactory(
                  (CreateBackupRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<GetBackupRequest, Backup> getBackupMethodDescriptor =
      ApiMethodDescriptor.<GetBackupRequest, Backup>newBuilder()
          .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/GetBackup")
          .setHttpMethod("GET")
          .setType(ApiMethodDescriptor.MethodType.UNARY)
          .setRequestFormatter(
              ProtoMessageRequestFormatter.<GetBackupRequest>newBuilder()
                  .setPath(
                      "/v2/{name=projects/*/instances/*/clusters/*/backups/*}",
                      request -> {
                        Map<String, String> fields = new HashMap<>();
                        ProtoRestSerializer<GetBackupRequest> serializer =
                            ProtoRestSerializer.create();
                        serializer.putPathParam(fields, "name", request.getName());
                        return fields;
                      })
                  .setQueryParamsExtractor(
                      request -> {
                        Map<String, List<String>> fields = new HashMap<>();
                        ProtoRestSerializer<GetBackupRequest> serializer =
                            ProtoRestSerializer.create();
                        return fields;
                      })
                  .setRequestBodyExtractor(request -> null)
                  .build())
          .setResponseParser(
              ProtoMessageResponseParser.<Backup>newBuilder()
                  .setDefaultInstance(Backup.getDefaultInstance())
                  .setDefaultTypeRegistry(typeRegistry)
                  .build())
          .build();

  private static final ApiMethodDescriptor<UpdateBackupRequest, Backup>
      updateBackupMethodDescriptor =
          ApiMethodDescriptor.<UpdateBackupRequest, Backup>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/UpdateBackup")
              .setHttpMethod("PATCH")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<UpdateBackupRequest>newBuilder()
                      .setPath(
                          "/v2/{backup.name=projects/*/instances/*/clusters/*/backups/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<UpdateBackupRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(
                                fields, "backup.name", request.getBackup().getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<UpdateBackupRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "updateMask", request.getUpdateMask());
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create().toBody("backup", request.getBackup()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Backup>newBuilder()
                      .setDefaultInstance(Backup.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<DeleteBackupRequest, Empty>
      deleteBackupMethodDescriptor =
          ApiMethodDescriptor.<DeleteBackupRequest, Empty>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/DeleteBackup")
              .setHttpMethod("DELETE")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<DeleteBackupRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/clusters/*/backups/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteBackupRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteBackupRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Empty>newBuilder()
                      .setDefaultInstance(Empty.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<ListBackupsRequest, ListBackupsResponse>
      listBackupsMethodDescriptor =
          ApiMethodDescriptor.<ListBackupsRequest, ListBackupsResponse>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/ListBackups")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ListBackupsRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*/clusters/*}/backups",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ListBackupsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ListBackupsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "filter", request.getFilter());
                            serializer.putQueryParam(fields, "orderBy", request.getOrderBy());
                            serializer.putQueryParam(fields, "pageSize", request.getPageSize());
                            serializer.putQueryParam(fields, "pageToken", request.getPageToken());
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<ListBackupsResponse>newBuilder()
                      .setDefaultInstance(ListBackupsResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<RestoreTableRequest, Operation>
      restoreTableMethodDescriptor =
          ApiMethodDescriptor.<RestoreTableRequest, Operation>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/RestoreTable")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<RestoreTableRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*}/tables:restore",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<RestoreTableRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<RestoreTableRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearParent().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Operation>newBuilder()
                      .setDefaultInstance(Operation.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .setOperationSnapshotFactory(
                  (RestoreTableRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<GetIamPolicyRequest, Policy>
      getIamPolicyMethodDescriptor =
          ApiMethodDescriptor.<GetIamPolicyRequest, Policy>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/GetIamPolicy")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<GetIamPolicyRequest>newBuilder()
                      .setPath(
                          "/v2/{resource=projects/*/instances/*/tables/*}:getIamPolicy",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<GetIamPolicyRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "resource", request.getResource());
                            return fields;
                          })
                      .setAdditionalPaths(
                          "/v2/{resource=projects/*/instances/*/clusters/*/backups/*}:getIamPolicy")
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<GetIamPolicyRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearResource().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Policy>newBuilder()
                      .setDefaultInstance(Policy.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<SetIamPolicyRequest, Policy>
      setIamPolicyMethodDescriptor =
          ApiMethodDescriptor.<SetIamPolicyRequest, Policy>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/SetIamPolicy")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<SetIamPolicyRequest>newBuilder()
                      .setPath(
                          "/v2/{resource=projects/*/instances/*/tables/*}:setIamPolicy",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<SetIamPolicyRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "resource", request.getResource());
                            return fields;
                          })
                      .setAdditionalPaths(
                          "/v2/{resource=projects/*/instances/*/clusters/*/backups/*}:setIamPolicy")
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<SetIamPolicyRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearResource().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Policy>newBuilder()
                      .setDefaultInstance(Policy.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<TestIamPermissionsRequest, TestIamPermissionsResponse>
      testIamPermissionsMethodDescriptor =
          ApiMethodDescriptor.<TestIamPermissionsRequest, TestIamPermissionsResponse>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableTableAdmin/TestIamPermissions")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<TestIamPermissionsRequest>newBuilder()
                      .setPath(
                          "/v2/{resource=projects/*/instances/*/tables/*}:testIamPermissions",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<TestIamPermissionsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "resource", request.getResource());
                            return fields;
                          })
                      .setAdditionalPaths(
                          "/v2/{resource=projects/*/instances/*/clusters/*/backups/*}:testIamPermissions")
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<TestIamPermissionsRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearResource().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<TestIamPermissionsResponse>newBuilder()
                      .setDefaultInstance(TestIamPermissionsResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private final UnaryCallable<CreateTableRequest, Table> createTableCallable;
  private final UnaryCallable<CreateTableFromSnapshotRequest, Operation>
      createTableFromSnapshotCallable;
  private final OperationCallable<
          CreateTableFromSnapshotRequest, Table, CreateTableFromSnapshotMetadata>
      createTableFromSnapshotOperationCallable;
  private final UnaryCallable<ListTablesRequest, ListTablesResponse> listTablesCallable;
  private final UnaryCallable<ListTablesRequest, ListTablesPagedResponse> listTablesPagedCallable;
  private final UnaryCallable<GetTableRequest, Table> getTableCallable;
  private final UnaryCallable<DeleteTableRequest, Empty> deleteTableCallable;
  private final UnaryCallable<UndeleteTableRequest, Operation> undeleteTableCallable;
  private final OperationCallable<UndeleteTableRequest, Table, UndeleteTableMetadata>
      undeleteTableOperationCallable;
  private final UnaryCallable<ModifyColumnFamiliesRequest, Table> modifyColumnFamiliesCallable;
  private final UnaryCallable<DropRowRangeRequest, Empty> dropRowRangeCallable;
  private final UnaryCallable<GenerateConsistencyTokenRequest, GenerateConsistencyTokenResponse>
      generateConsistencyTokenCallable;
  private final UnaryCallable<CheckConsistencyRequest, CheckConsistencyResponse>
      checkConsistencyCallable;
  private final UnaryCallable<SnapshotTableRequest, Operation> snapshotTableCallable;
  private final OperationCallable<SnapshotTableRequest, Snapshot, SnapshotTableMetadata>
      snapshotTableOperationCallable;
  private final UnaryCallable<GetSnapshotRequest, Snapshot> getSnapshotCallable;
  private final UnaryCallable<ListSnapshotsRequest, ListSnapshotsResponse> listSnapshotsCallable;
  private final UnaryCallable<ListSnapshotsRequest, ListSnapshotsPagedResponse>
      listSnapshotsPagedCallable;
  private final UnaryCallable<DeleteSnapshotRequest, Empty> deleteSnapshotCallable;
  private final UnaryCallable<CreateBackupRequest, Operation> createBackupCallable;
  private final OperationCallable<CreateBackupRequest, Backup, CreateBackupMetadata>
      createBackupOperationCallable;
  private final UnaryCallable<GetBackupRequest, Backup> getBackupCallable;
  private final UnaryCallable<UpdateBackupRequest, Backup> updateBackupCallable;
  private final UnaryCallable<DeleteBackupRequest, Empty> deleteBackupCallable;
  private final UnaryCallable<ListBackupsRequest, ListBackupsResponse> listBackupsCallable;
  private final UnaryCallable<ListBackupsRequest, ListBackupsPagedResponse>
      listBackupsPagedCallable;
  private final UnaryCallable<RestoreTableRequest, Operation> restoreTableCallable;
  private final OperationCallable<RestoreTableRequest, Table, RestoreTableMetadata>
      restoreTableOperationCallable;
  private final UnaryCallable<GetIamPolicyRequest, Policy> getIamPolicyCallable;
  private final UnaryCallable<SetIamPolicyRequest, Policy> setIamPolicyCallable;
  private final UnaryCallable<TestIamPermissionsRequest, TestIamPermissionsResponse>
      testIamPermissionsCallable;

  private final BackgroundResource backgroundResources;
  private final HttpJsonOperationsStub httpJsonOperationsStub;
  private final HttpJsonStubCallableFactory callableFactory;

  public static final HttpJsonBigtableTableAdminStub create(BigtableTableAdminStubSettings settings)
      throws IOException {
    return new HttpJsonBigtableTableAdminStub(settings, ClientContext.create(settings));
  }

  public static final HttpJsonBigtableTableAdminStub create(ClientContext clientContext)
      throws IOException {
    return new HttpJsonBigtableTableAdminStub(
        BigtableTableAdminStubSettings.newHttpJsonBuilder().build(), clientContext);
  }

  public static final HttpJsonBigtableTableAdminStub create(
      ClientContext clientContext, HttpJsonStubCallableFactory callableFactory) throws IOException {
    return new HttpJsonBigtableTableAdminStub(
        BigtableTableAdminStubSettings.newHttpJsonBuilder().build(),
        clientContext,
        callableFactory);
  }

  /**
   * Constructs an instance of HttpJsonBigtableTableAdminStub, using the given settings. This is
   * protected so that it is easy to make a subclass, but otherwise, the static factory methods
   * should be preferred.
   */
  protected HttpJsonBigtableTableAdminStub(
      BigtableTableAdminStubSettings settings, ClientContext clientContext) throws IOException {
    this(settings, clientContext, new HttpJsonBigtableTableAdminCallableFactory());
  }

  /**
   * Constructs an instance of HttpJsonBigtableTableAdminStub, using the given settings. This is
   * protected so that it is easy to make a subclass, but otherwise, the static factory methods
   * should be preferred.
   */
  protected HttpJsonBigtableTableAdminStub(
      BigtableTableAdminStubSettings settings,
      ClientContext clientContext,
      HttpJsonStubCallableFactory callableFactory)
      throws IOException {
    this.callableFactory = callableFactory;
    this.httpJsonOperationsStub =
        HttpJsonOperationsStub.create(clientContext, callableFactory, typeRegistry);

    HttpJsonCallSettings<CreateTableRequest, Table> createTableTransportSettings =
        HttpJsonCallSettings.<CreateTableRequest, Table>newBuilder()
            .setMethodDescriptor(createTableMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<CreateTableFromSnapshotRequest, Operation>
        createTableFromSnapshotTransportSettings =
            HttpJsonCallSettings.<CreateTableFromSnapshotRequest, Operation>newBuilder()
                .setMethodDescriptor(createTableFromSnapshotMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<ListTablesRequest, ListTablesResponse> listTablesTransportSettings =
        HttpJsonCallSettings.<ListTablesRequest, ListTablesResponse>newBuilder()
            .setMethodDescriptor(listTablesMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<GetTableRequest, Table> getTableTransportSettings =
        HttpJsonCallSettings.<GetTableRequest, Table>newBuilder()
            .setMethodDescriptor(getTableMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<DeleteTableRequest, Empty> deleteTableTransportSettings =
        HttpJsonCallSettings.<DeleteTableRequest, Empty>newBuilder()
            .setMethodDescriptor(deleteTableMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<UndeleteTableRequest, Operation> undeleteTableTransportSettings =
        HttpJsonCallSettings.<UndeleteTableRequest, Operation>newBuilder()
            .setMethodDescriptor(undeleteTableMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<ModifyColumnFamiliesRequest, Table> modifyColumnFamiliesTransportSettings =
        HttpJsonCallSettings.<ModifyColumnFamiliesRequest, Table>newBuilder()
            .setMethodDescriptor(modifyColumnFamiliesMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<DropRowRangeRequest, Empty> dropRowRangeTransportSettings =
        HttpJsonCallSettings.<DropRowRangeRequest, Empty>newBuilder()
            .setMethodDescriptor(dropRowRangeMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<GenerateConsistencyTokenRequest, GenerateConsistencyTokenResponse>
        generateConsistencyTokenTransportSettings =
            HttpJsonCallSettings
                .<GenerateConsistencyTokenRequest, GenerateConsistencyTokenResponse>newBuilder()
                .setMethodDescriptor(generateConsistencyTokenMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<CheckConsistencyRequest, CheckConsistencyResponse>
        checkConsistencyTransportSettings =
            HttpJsonCallSettings.<CheckConsistencyRequest, CheckConsistencyResponse>newBuilder()
                .setMethodDescriptor(checkConsistencyMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<SnapshotTableRequest, Operation> snapshotTableTransportSettings =
        HttpJsonCallSettings.<SnapshotTableRequest, Operation>newBuilder()
            .setMethodDescriptor(snapshotTableMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<GetSnapshotRequest, Snapshot> getSnapshotTransportSettings =
        HttpJsonCallSettings.<GetSnapshotRequest, Snapshot>newBuilder()
            .setMethodDescriptor(getSnapshotMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<ListSnapshotsRequest, ListSnapshotsResponse>
        listSnapshotsTransportSettings =
            HttpJsonCallSettings.<ListSnapshotsRequest, ListSnapshotsResponse>newBuilder()
                .setMethodDescriptor(listSnapshotsMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<DeleteSnapshotRequest, Empty> deleteSnapshotTransportSettings =
        HttpJsonCallSettings.<DeleteSnapshotRequest, Empty>newBuilder()
            .setMethodDescriptor(deleteSnapshotMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<CreateBackupRequest, Operation> createBackupTransportSettings =
        HttpJsonCallSettings.<CreateBackupRequest, Operation>newBuilder()
            .setMethodDescriptor(createBackupMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<GetBackupRequest, Backup> getBackupTransportSettings =
        HttpJsonCallSettings.<GetBackupRequest, Backup>newBuilder()
            .setMethodDescriptor(getBackupMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<UpdateBackupRequest, Backup> updateBackupTransportSettings =
        HttpJsonCallSettings.<UpdateBackupRequest, Backup>newBuilder()
            .setMethodDescriptor(updateBackupMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<DeleteBackupRequest, Empty> deleteBackupTransportSettings =
        HttpJsonCallSettings.<DeleteBackupRequest, Empty>newBuilder()
            .setMethodDescriptor(deleteBackupMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<ListBackupsRequest, ListBackupsResponse> listBackupsTransportSettings =
        HttpJsonCallSettings.<ListBackupsRequest, ListBackupsResponse>newBuilder()
            .setMethodDescriptor(listBackupsMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<RestoreTableRequest, Operation> restoreTableTransportSettings =
        HttpJsonCallSettings.<RestoreTableRequest, Operation>newBuilder()
            .setMethodDescriptor(restoreTableMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<GetIamPolicyRequest, Policy> getIamPolicyTransportSettings =
        HttpJsonCallSettings.<GetIamPolicyRequest, Policy>newBuilder()
            .setMethodDescriptor(getIamPolicyMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<SetIamPolicyRequest, Policy> setIamPolicyTransportSettings =
        HttpJsonCallSettings.<SetIamPolicyRequest, Policy>newBuilder()
            .setMethodDescriptor(setIamPolicyMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<TestIamPermissionsRequest, TestIamPermissionsResponse>
        testIamPermissionsTransportSettings =
            HttpJsonCallSettings.<TestIamPermissionsRequest, TestIamPermissionsResponse>newBuilder()
                .setMethodDescriptor(testIamPermissionsMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();

    this.createTableCallable =
        callableFactory.createUnaryCallable(
            createTableTransportSettings, settings.createTableSettings(), clientContext);
    this.createTableFromSnapshotCallable =
        callableFactory.createUnaryCallable(
            createTableFromSnapshotTransportSettings,
            settings.createTableFromSnapshotSettings(),
            clientContext);
    this.createTableFromSnapshotOperationCallable =
        callableFactory.createOperationCallable(
            createTableFromSnapshotTransportSettings,
            settings.createTableFromSnapshotOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.listTablesCallable =
        callableFactory.createUnaryCallable(
            listTablesTransportSettings, settings.listTablesSettings(), clientContext);
    this.listTablesPagedCallable =
        callableFactory.createPagedCallable(
            listTablesTransportSettings, settings.listTablesSettings(), clientContext);
    this.getTableCallable =
        callableFactory.createUnaryCallable(
            getTableTransportSettings, settings.getTableSettings(), clientContext);
    this.deleteTableCallable =
        callableFactory.createUnaryCallable(
            deleteTableTransportSettings, settings.deleteTableSettings(), clientContext);
    this.undeleteTableCallable =
        callableFactory.createUnaryCallable(
            undeleteTableTransportSettings, settings.undeleteTableSettings(), clientContext);
    this.undeleteTableOperationCallable =
        callableFactory.createOperationCallable(
            undeleteTableTransportSettings,
            settings.undeleteTableOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.modifyColumnFamiliesCallable =
        callableFactory.createUnaryCallable(
            modifyColumnFamiliesTransportSettings,
            settings.modifyColumnFamiliesSettings(),
            clientContext);
    this.dropRowRangeCallable =
        callableFactory.createUnaryCallable(
            dropRowRangeTransportSettings, settings.dropRowRangeSettings(), clientContext);
    this.generateConsistencyTokenCallable =
        callableFactory.createUnaryCallable(
            generateConsistencyTokenTransportSettings,
            settings.generateConsistencyTokenSettings(),
            clientContext);
    this.checkConsistencyCallable =
        callableFactory.createUnaryCallable(
            checkConsistencyTransportSettings, settings.checkConsistencySettings(), clientContext);
    this.snapshotTableCallable =
        callableFactory.createUnaryCallable(
            snapshotTableTransportSettings, settings.snapshotTableSettings(), clientContext);
    this.snapshotTableOperationCallable =
        callableFactory.createOperationCallable(
            snapshotTableTransportSettings,
            settings.snapshotTableOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.getSnapshotCallable =
        callableFactory.createUnaryCallable(
            getSnapshotTransportSettings, settings.getSnapshotSettings(), clientContext);
    this.listSnapshotsCallable =
        callableFactory.createUnaryCallable(
            listSnapshotsTransportSettings, settings.listSnapshotsSettings(), clientContext);
    this.listSnapshotsPagedCallable =
        callableFactory.createPagedCallable(
            listSnapshotsTransportSettings, settings.listSnapshotsSettings(), clientContext);
    this.deleteSnapshotCallable =
        callableFactory.createUnaryCallable(
            deleteSnapshotTransportSettings, settings.deleteSnapshotSettings(), clientContext);
    this.createBackupCallable =
        callableFactory.createUnaryCallable(
            createBackupTransportSettings, settings.createBackupSettings(), clientContext);
    this.createBackupOperationCallable =
        callableFactory.createOperationCallable(
            createBackupTransportSettings,
            settings.createBackupOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.getBackupCallable =
        callableFactory.createUnaryCallable(
            getBackupTransportSettings, settings.getBackupSettings(), clientContext);
    this.updateBackupCallable =
        callableFactory.createUnaryCallable(
            updateBackupTransportSettings, settings.updateBackupSettings(), clientContext);
    this.deleteBackupCallable =
        callableFactory.createUnaryCallable(
            deleteBackupTransportSettings, settings.deleteBackupSettings(), clientContext);
    this.listBackupsCallable =
        callableFactory.createUnaryCallable(
            listBackupsTransportSettings, settings.listBackupsSettings(), clientContext);
    this.listBackupsPagedCallable =
        callableFactory.createPagedCallable(
            listBackupsTransportSettings, settings.listBackupsSettings(), clientContext);
    this.restoreTableCallable =
        callableFactory.createUnaryCallable(
            restoreTableTransportSettings, settings.restoreTableSettings(), clientContext);
    this.restoreTableOperationCallable =
        callableFactory.createOperationCallable(
            restoreTableTransportSettings,
            settings.restoreTableOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.getIamPolicyCallable =
        callableFactory.createUnaryCallable(
            getIamPolicyTransportSettings, settings.getIamPolicySettings(), clientContext);
    this.setIamPolicyCallable =
        callableFactory.createUnaryCallable(
            setIamPolicyTransportSettings, settings.setIamPolicySettings(), clientContext);
    this.testIamPermissionsCallable =
        callableFactory.createUnaryCallable(
            testIamPermissionsTransportSettings,
            settings.testIamPermissionsSettings(),
            clientContext);

    this.backgroundResources =
        new BackgroundResourceAggregation(clientContext.getBackgroundResources());
  }

  @InternalApi
  public static List<ApiMethodDescriptor> getMethodDescriptors() {
    List<ApiMethodDescriptor> methodDescriptors = new ArrayList<>();
    methodDescriptors.add(createTableMethodDescriptor);
    methodDescriptors.add(createTableFromSnapshotMethodDescriptor);
    methodDescriptors.add(listTablesMethodDescriptor);
    methodDescriptors.add(getTableMethodDescriptor);
    methodDescriptors.add(deleteTableMethodDescriptor);
    methodDescriptors.add(undeleteTableMethodDescriptor);
    methodDescriptors.add(modifyColumnFamiliesMethodDescriptor);
    methodDescriptors.add(dropRowRangeMethodDescriptor);
    methodDescriptors.add(generateConsistencyTokenMethodDescriptor);
    methodDescriptors.add(checkConsistencyMethodDescriptor);
    methodDescriptors.add(snapshotTableMethodDescriptor);
    methodDescriptors.add(getSnapshotMethodDescriptor);
    methodDescriptors.add(listSnapshotsMethodDescriptor);
    methodDescriptors.add(deleteSnapshotMethodDescriptor);
    methodDescriptors.add(createBackupMethodDescriptor);
    methodDescriptors.add(getBackupMethodDescriptor);
    methodDescriptors.add(updateBackupMethodDescriptor);
    methodDescriptors.add(deleteBackupMethodDescriptor);
    methodDescriptors.add(listBackupsMethodDescriptor);
    methodDescriptors.add(restoreTableMethodDescriptor);
    methodDescriptors.add(getIamPolicyMethodDescriptor);
    methodDescriptors.add(setIamPolicyMethodDescriptor);
    methodDescriptors.add(testIamPermissionsMethodDescriptor);
    return methodDescriptors;
  }

  public HttpJsonOperationsStub getHttpJsonOperationsStub() {
    return httpJsonOperationsStub;
  }

  @Override
  public UnaryCallable<CreateTableRequest, Table> createTableCallable() {
    return createTableCallable;
  }

  @Override
  public UnaryCallable<CreateTableFromSnapshotRequest, Operation>
      createTableFromSnapshotCallable() {
    return createTableFromSnapshotCallable;
  }

  @Override
  public OperationCallable<CreateTableFromSnapshotRequest, Table, CreateTableFromSnapshotMetadata>
      createTableFromSnapshotOperationCallable() {
    return createTableFromSnapshotOperationCallable;
  }

  @Override
  public UnaryCallable<ListTablesRequest, ListTablesResponse> listTablesCallable() {
    return listTablesCallable;
  }

  @Override
  public UnaryCallable<ListTablesRequest, ListTablesPagedResponse> listTablesPagedCallable() {
    return listTablesPagedCallable;
  }

  @Override
  public UnaryCallable<GetTableRequest, Table> getTableCallable() {
    return getTableCallable;
  }

  @Override
  public UnaryCallable<DeleteTableRequest, Empty> deleteTableCallable() {
    return deleteTableCallable;
  }

  @Override
  public UnaryCallable<UndeleteTableRequest, Operation> undeleteTableCallable() {
    return undeleteTableCallable;
  }

  @Override
  public OperationCallable<UndeleteTableRequest, Table, UndeleteTableMetadata>
      undeleteTableOperationCallable() {
    return undeleteTableOperationCallable;
  }

  @Override
  public UnaryCallable<ModifyColumnFamiliesRequest, Table> modifyColumnFamiliesCallable() {
    return modifyColumnFamiliesCallable;
  }

  @Override
  public UnaryCallable<DropRowRangeRequest, Empty> dropRowRangeCallable() {
    return dropRowRangeCallable;
  }

  @Override
  public UnaryCallable<GenerateConsistencyTokenRequest, GenerateConsistencyTokenResponse>
      generateConsistencyTokenCallable() {
    return generateConsistencyTokenCallable;
  }

  @Override
  public UnaryCallable<CheckConsistencyRequest, CheckConsistencyResponse>
      checkConsistencyCallable() {
    return checkConsistencyCallable;
  }

  @Override
  public UnaryCallable<SnapshotTableRequest, Operation> snapshotTableCallable() {
    return snapshotTableCallable;
  }

  @Override
  public OperationCallable<SnapshotTableRequest, Snapshot, SnapshotTableMetadata>
      snapshotTableOperationCallable() {
    return snapshotTableOperationCallable;
  }

  @Override
  public UnaryCallable<GetSnapshotRequest, Snapshot> getSnapshotCallable() {
    return getSnapshotCallable;
  }

  @Override
  public UnaryCallable<ListSnapshotsRequest, ListSnapshotsResponse> listSnapshotsCallable() {
    return listSnapshotsCallable;
  }

  @Override
  public UnaryCallable<ListSnapshotsRequest, ListSnapshotsPagedResponse>
      listSnapshotsPagedCallable() {
    return listSnapshotsPagedCallable;
  }

  @Override
  public UnaryCallable<DeleteSnapshotRequest, Empty> deleteSnapshotCallable() {
    return deleteSnapshotCallable;
  }

  @Override
  public UnaryCallable<CreateBackupRequest, Operation> createBackupCallable() {
    return createBackupCallable;
  }

  @Override
  public OperationCallable<CreateBackupRequest, Backup, CreateBackupMetadata>
      createBackupOperationCallable() {
    return createBackupOperationCallable;
  }

  @Override
  public UnaryCallable<GetBackupRequest, Backup> getBackupCallable() {
    return getBackupCallable;
  }

  @Override
  public UnaryCallable<UpdateBackupRequest, Backup> updateBackupCallable() {
    return updateBackupCallable;
  }

  @Override
  public UnaryCallable<DeleteBackupRequest, Empty> deleteBackupCallable() {
    return deleteBackupCallable;
  }

  @Override
  public UnaryCallable<ListBackupsRequest, ListBackupsResponse> listBackupsCallable() {
    return listBackupsCallable;
  }

  @Override
  public UnaryCallable<ListBackupsRequest, ListBackupsPagedResponse> listBackupsPagedCallable() {
    return listBackupsPagedCallable;
  }

  @Override
  public UnaryCallable<RestoreTableRequest, Operation> restoreTableCallable() {
    return restoreTableCallable;
  }

  @Override
  public OperationCallable<RestoreTableRequest, Table, RestoreTableMetadata>
      restoreTableOperationCallable() {
    return restoreTableOperationCallable;
  }

  @Override
  public UnaryCallable<GetIamPolicyRequest, Policy> getIamPolicyCallable() {
    return getIamPolicyCallable;
  }

  @Override
  public UnaryCallable<SetIamPolicyRequest, Policy> setIamPolicyCallable() {
    return setIamPolicyCallable;
  }

  @Override
  public UnaryCallable<TestIamPermissionsRequest, TestIamPermissionsResponse>
      testIamPermissionsCallable() {
    return testIamPermissionsCallable;
  }

  @Override
  public final void close() {
    try {
      backgroundResources.close();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException("Failed to close resource", e);
    }
  }

  @Override
  public void shutdown() {
    backgroundResources.shutdown();
  }

  @Override
  public boolean isShutdown() {
    return backgroundResources.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return backgroundResources.isTerminated();
  }

  @Override
  public void shutdownNow() {
    backgroundResources.shutdownNow();
  }

  @Override
  public boolean awaitTermination(long duration, TimeUnit unit) throws InterruptedException {
    return backgroundResources.awaitTermination(duration, unit);
  }
}
