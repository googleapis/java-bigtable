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

import static com.google.cloud.bigtable.admin.v2.BaseBigtableInstanceAdminClient.ListAppProfilesPagedResponse;
import static com.google.cloud.bigtable.admin.v2.BaseBigtableInstanceAdminClient.ListHotTabletsPagedResponse;

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
import com.google.bigtable.admin.v2.AppProfile;
import com.google.bigtable.admin.v2.Cluster;
import com.google.bigtable.admin.v2.CreateAppProfileRequest;
import com.google.bigtable.admin.v2.CreateClusterMetadata;
import com.google.bigtable.admin.v2.CreateClusterRequest;
import com.google.bigtable.admin.v2.CreateInstanceMetadata;
import com.google.bigtable.admin.v2.CreateInstanceRequest;
import com.google.bigtable.admin.v2.DeleteAppProfileRequest;
import com.google.bigtable.admin.v2.DeleteClusterRequest;
import com.google.bigtable.admin.v2.DeleteInstanceRequest;
import com.google.bigtable.admin.v2.GetAppProfileRequest;
import com.google.bigtable.admin.v2.GetClusterRequest;
import com.google.bigtable.admin.v2.GetInstanceRequest;
import com.google.bigtable.admin.v2.Instance;
import com.google.bigtable.admin.v2.ListAppProfilesRequest;
import com.google.bigtable.admin.v2.ListAppProfilesResponse;
import com.google.bigtable.admin.v2.ListClustersRequest;
import com.google.bigtable.admin.v2.ListClustersResponse;
import com.google.bigtable.admin.v2.ListHotTabletsRequest;
import com.google.bigtable.admin.v2.ListHotTabletsResponse;
import com.google.bigtable.admin.v2.ListInstancesRequest;
import com.google.bigtable.admin.v2.ListInstancesResponse;
import com.google.bigtable.admin.v2.PartialUpdateClusterMetadata;
import com.google.bigtable.admin.v2.PartialUpdateClusterRequest;
import com.google.bigtable.admin.v2.PartialUpdateInstanceRequest;
import com.google.bigtable.admin.v2.UpdateAppProfileMetadata;
import com.google.bigtable.admin.v2.UpdateAppProfileRequest;
import com.google.bigtable.admin.v2.UpdateClusterMetadata;
import com.google.bigtable.admin.v2.UpdateInstanceMetadata;
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
 * REST stub implementation for the BigtableInstanceAdmin service API.
 *
 * <p>This class is for advanced usage and reflects the underlying API directly.
 */
@Generated("by gapic-generator-java")
@BetaApi
public class HttpJsonBigtableInstanceAdminStub extends BigtableInstanceAdminStub {
  private static final TypeRegistry typeRegistry =
      TypeRegistry.newBuilder()
          .add(Cluster.getDescriptor())
          .add(CreateInstanceMetadata.getDescriptor())
          .add(UpdateInstanceMetadata.getDescriptor())
          .add(CreateClusterMetadata.getDescriptor())
          .add(UpdateAppProfileMetadata.getDescriptor())
          .add(PartialUpdateClusterMetadata.getDescriptor())
          .add(AppProfile.getDescriptor())
          .add(Instance.getDescriptor())
          .add(UpdateClusterMetadata.getDescriptor())
          .build();

  private static final ApiMethodDescriptor<CreateInstanceRequest, Operation>
      createInstanceMethodDescriptor =
          ApiMethodDescriptor.<CreateInstanceRequest, Operation>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/CreateInstance")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<CreateInstanceRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*}/instances",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<CreateInstanceRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<CreateInstanceRequest> serializer =
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
                  (CreateInstanceRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<GetInstanceRequest, Instance>
      getInstanceMethodDescriptor =
          ApiMethodDescriptor.<GetInstanceRequest, Instance>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/GetInstance")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<GetInstanceRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<GetInstanceRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<GetInstanceRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Instance>newBuilder()
                      .setDefaultInstance(Instance.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<ListInstancesRequest, ListInstancesResponse>
      listInstancesMethodDescriptor =
          ApiMethodDescriptor.<ListInstancesRequest, ListInstancesResponse>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/ListInstances")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ListInstancesRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*}/instances",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ListInstancesRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ListInstancesRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "pageToken", request.getPageToken());
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<ListInstancesResponse>newBuilder()
                      .setDefaultInstance(ListInstancesResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<Instance, Instance> updateInstanceMethodDescriptor =
      ApiMethodDescriptor.<Instance, Instance>newBuilder()
          .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/UpdateInstance")
          .setHttpMethod("PUT")
          .setType(ApiMethodDescriptor.MethodType.UNARY)
          .setRequestFormatter(
              ProtoMessageRequestFormatter.<Instance>newBuilder()
                  .setPath(
                      "/v2/{name=projects/*/instances/*}",
                      request -> {
                        Map<String, String> fields = new HashMap<>();
                        ProtoRestSerializer<Instance> serializer = ProtoRestSerializer.create();
                        serializer.putPathParam(fields, "name", request.getName());
                        return fields;
                      })
                  .setQueryParamsExtractor(
                      request -> {
                        Map<String, List<String>> fields = new HashMap<>();
                        ProtoRestSerializer<Instance> serializer = ProtoRestSerializer.create();
                        return fields;
                      })
                  .setRequestBodyExtractor(
                      request ->
                          ProtoRestSerializer.create()
                              .toBody("*", request.toBuilder().clearName().build()))
                  .build())
          .setResponseParser(
              ProtoMessageResponseParser.<Instance>newBuilder()
                  .setDefaultInstance(Instance.getDefaultInstance())
                  .setDefaultTypeRegistry(typeRegistry)
                  .build())
          .build();

  private static final ApiMethodDescriptor<PartialUpdateInstanceRequest, Operation>
      partialUpdateInstanceMethodDescriptor =
          ApiMethodDescriptor.<PartialUpdateInstanceRequest, Operation>newBuilder()
              .setFullMethodName(
                  "google.bigtable.admin.v2.BigtableInstanceAdmin/PartialUpdateInstance")
              .setHttpMethod("PATCH")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<PartialUpdateInstanceRequest>newBuilder()
                      .setPath(
                          "/v2/{instance.name=projects/*/instances/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<PartialUpdateInstanceRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(
                                fields, "instance.name", request.getInstance().getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<PartialUpdateInstanceRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "updateMask", request.getUpdateMask());
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("instance", request.getInstance()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Operation>newBuilder()
                      .setDefaultInstance(Operation.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .setOperationSnapshotFactory(
                  (PartialUpdateInstanceRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<DeleteInstanceRequest, Empty>
      deleteInstanceMethodDescriptor =
          ApiMethodDescriptor.<DeleteInstanceRequest, Empty>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/DeleteInstance")
              .setHttpMethod("DELETE")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<DeleteInstanceRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteInstanceRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteInstanceRequest> serializer =
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

  private static final ApiMethodDescriptor<CreateClusterRequest, Operation>
      createClusterMethodDescriptor =
          ApiMethodDescriptor.<CreateClusterRequest, Operation>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/CreateCluster")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<CreateClusterRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*}/clusters",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<CreateClusterRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<CreateClusterRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "clusterId", request.getClusterId());
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create().toBody("cluster", request.getCluster()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Operation>newBuilder()
                      .setDefaultInstance(Operation.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .setOperationSnapshotFactory(
                  (CreateClusterRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<GetClusterRequest, Cluster> getClusterMethodDescriptor =
      ApiMethodDescriptor.<GetClusterRequest, Cluster>newBuilder()
          .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/GetCluster")
          .setHttpMethod("GET")
          .setType(ApiMethodDescriptor.MethodType.UNARY)
          .setRequestFormatter(
              ProtoMessageRequestFormatter.<GetClusterRequest>newBuilder()
                  .setPath(
                      "/v2/{name=projects/*/instances/*/clusters/*}",
                      request -> {
                        Map<String, String> fields = new HashMap<>();
                        ProtoRestSerializer<GetClusterRequest> serializer =
                            ProtoRestSerializer.create();
                        serializer.putPathParam(fields, "name", request.getName());
                        return fields;
                      })
                  .setQueryParamsExtractor(
                      request -> {
                        Map<String, List<String>> fields = new HashMap<>();
                        ProtoRestSerializer<GetClusterRequest> serializer =
                            ProtoRestSerializer.create();
                        return fields;
                      })
                  .setRequestBodyExtractor(request -> null)
                  .build())
          .setResponseParser(
              ProtoMessageResponseParser.<Cluster>newBuilder()
                  .setDefaultInstance(Cluster.getDefaultInstance())
                  .setDefaultTypeRegistry(typeRegistry)
                  .build())
          .build();

  private static final ApiMethodDescriptor<ListClustersRequest, ListClustersResponse>
      listClustersMethodDescriptor =
          ApiMethodDescriptor.<ListClustersRequest, ListClustersResponse>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/ListClusters")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ListClustersRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*}/clusters",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ListClustersRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ListClustersRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "pageToken", request.getPageToken());
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<ListClustersResponse>newBuilder()
                      .setDefaultInstance(ListClustersResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<Cluster, Operation> updateClusterMethodDescriptor =
      ApiMethodDescriptor.<Cluster, Operation>newBuilder()
          .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/UpdateCluster")
          .setHttpMethod("PUT")
          .setType(ApiMethodDescriptor.MethodType.UNARY)
          .setRequestFormatter(
              ProtoMessageRequestFormatter.<Cluster>newBuilder()
                  .setPath(
                      "/v2/{name=projects/*/instances/*/clusters/*}",
                      request -> {
                        Map<String, String> fields = new HashMap<>();
                        ProtoRestSerializer<Cluster> serializer = ProtoRestSerializer.create();
                        serializer.putPathParam(fields, "name", request.getName());
                        return fields;
                      })
                  .setQueryParamsExtractor(
                      request -> {
                        Map<String, List<String>> fields = new HashMap<>();
                        ProtoRestSerializer<Cluster> serializer = ProtoRestSerializer.create();
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
              (Cluster request, Operation response) -> HttpJsonOperationSnapshot.create(response))
          .build();

  private static final ApiMethodDescriptor<PartialUpdateClusterRequest, Operation>
      partialUpdateClusterMethodDescriptor =
          ApiMethodDescriptor.<PartialUpdateClusterRequest, Operation>newBuilder()
              .setFullMethodName(
                  "google.bigtable.admin.v2.BigtableInstanceAdmin/PartialUpdateCluster")
              .setHttpMethod("PATCH")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<PartialUpdateClusterRequest>newBuilder()
                      .setPath(
                          "/v2/{cluster.name=projects/*/instances/*/clusters/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<PartialUpdateClusterRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(
                                fields, "cluster.name", request.getCluster().getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<PartialUpdateClusterRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "updateMask", request.getUpdateMask());
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create().toBody("cluster", request.getCluster()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Operation>newBuilder()
                      .setDefaultInstance(Operation.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .setOperationSnapshotFactory(
                  (PartialUpdateClusterRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<DeleteClusterRequest, Empty>
      deleteClusterMethodDescriptor =
          ApiMethodDescriptor.<DeleteClusterRequest, Empty>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/DeleteCluster")
              .setHttpMethod("DELETE")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<DeleteClusterRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/clusters/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteClusterRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteClusterRequest> serializer =
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

  private static final ApiMethodDescriptor<CreateAppProfileRequest, AppProfile>
      createAppProfileMethodDescriptor =
          ApiMethodDescriptor.<CreateAppProfileRequest, AppProfile>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/CreateAppProfile")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<CreateAppProfileRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*}/appProfiles",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<CreateAppProfileRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<CreateAppProfileRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(
                                fields, "appProfileId", request.getAppProfileId());
                            serializer.putQueryParam(
                                fields, "ignoreWarnings", request.getIgnoreWarnings());
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("appProfile", request.getAppProfile()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<AppProfile>newBuilder()
                      .setDefaultInstance(AppProfile.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<GetAppProfileRequest, AppProfile>
      getAppProfileMethodDescriptor =
          ApiMethodDescriptor.<GetAppProfileRequest, AppProfile>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/GetAppProfile")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<GetAppProfileRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/appProfiles/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<GetAppProfileRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<GetAppProfileRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<AppProfile>newBuilder()
                      .setDefaultInstance(AppProfile.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<ListAppProfilesRequest, ListAppProfilesResponse>
      listAppProfilesMethodDescriptor =
          ApiMethodDescriptor.<ListAppProfilesRequest, ListAppProfilesResponse>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/ListAppProfiles")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ListAppProfilesRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*}/appProfiles",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ListAppProfilesRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ListAppProfilesRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "pageSize", request.getPageSize());
                            serializer.putQueryParam(fields, "pageToken", request.getPageToken());
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<ListAppProfilesResponse>newBuilder()
                      .setDefaultInstance(ListAppProfilesResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<UpdateAppProfileRequest, Operation>
      updateAppProfileMethodDescriptor =
          ApiMethodDescriptor.<UpdateAppProfileRequest, Operation>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/UpdateAppProfile")
              .setHttpMethod("PATCH")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<UpdateAppProfileRequest>newBuilder()
                      .setPath(
                          "/v2/{appProfile.name=projects/*/instances/*/appProfiles/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<UpdateAppProfileRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(
                                fields, "appProfile.name", request.getAppProfile().getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<UpdateAppProfileRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(
                                fields, "ignoreWarnings", request.getIgnoreWarnings());
                            serializer.putQueryParam(fields, "updateMask", request.getUpdateMask());
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("appProfile", request.getAppProfile()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<Operation>newBuilder()
                      .setDefaultInstance(Operation.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .setOperationSnapshotFactory(
                  (UpdateAppProfileRequest request, Operation response) ->
                      HttpJsonOperationSnapshot.create(response))
              .build();

  private static final ApiMethodDescriptor<DeleteAppProfileRequest, Empty>
      deleteAppProfileMethodDescriptor =
          ApiMethodDescriptor.<DeleteAppProfileRequest, Empty>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/DeleteAppProfile")
              .setHttpMethod("DELETE")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<DeleteAppProfileRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*/appProfiles/*}",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteAppProfileRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<DeleteAppProfileRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(
                                fields, "ignoreWarnings", request.getIgnoreWarnings());
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

  private static final ApiMethodDescriptor<GetIamPolicyRequest, Policy>
      getIamPolicyMethodDescriptor =
          ApiMethodDescriptor.<GetIamPolicyRequest, Policy>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/GetIamPolicy")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<GetIamPolicyRequest>newBuilder()
                      .setPath(
                          "/v2/{resource=projects/*/instances/*}:getIamPolicy",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<GetIamPolicyRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "resource", request.getResource());
                            return fields;
                          })
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
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/SetIamPolicy")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<SetIamPolicyRequest>newBuilder()
                      .setPath(
                          "/v2/{resource=projects/*/instances/*}:setIamPolicy",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<SetIamPolicyRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "resource", request.getResource());
                            return fields;
                          })
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
              .setFullMethodName(
                  "google.bigtable.admin.v2.BigtableInstanceAdmin/TestIamPermissions")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<TestIamPermissionsRequest>newBuilder()
                      .setPath(
                          "/v2/{resource=projects/*/instances/*}:testIamPermissions",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<TestIamPermissionsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "resource", request.getResource());
                            return fields;
                          })
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

  private static final ApiMethodDescriptor<ListHotTabletsRequest, ListHotTabletsResponse>
      listHotTabletsMethodDescriptor =
          ApiMethodDescriptor.<ListHotTabletsRequest, ListHotTabletsResponse>newBuilder()
              .setFullMethodName("google.bigtable.admin.v2.BigtableInstanceAdmin/ListHotTablets")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ListHotTabletsRequest>newBuilder()
                      .setPath(
                          "/v2/{parent=projects/*/instances/*/clusters/*}/hotTablets",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ListHotTabletsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "parent", request.getParent());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ListHotTabletsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(fields, "endTime", request.getEndTime());
                            serializer.putQueryParam(fields, "pageSize", request.getPageSize());
                            serializer.putQueryParam(fields, "pageToken", request.getPageToken());
                            serializer.putQueryParam(fields, "startTime", request.getStartTime());
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<ListHotTabletsResponse>newBuilder()
                      .setDefaultInstance(ListHotTabletsResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private final UnaryCallable<CreateInstanceRequest, Operation> createInstanceCallable;
  private final OperationCallable<CreateInstanceRequest, Instance, CreateInstanceMetadata>
      createInstanceOperationCallable;
  private final UnaryCallable<GetInstanceRequest, Instance> getInstanceCallable;
  private final UnaryCallable<ListInstancesRequest, ListInstancesResponse> listInstancesCallable;
  private final UnaryCallable<Instance, Instance> updateInstanceCallable;
  private final UnaryCallable<PartialUpdateInstanceRequest, Operation>
      partialUpdateInstanceCallable;
  private final OperationCallable<PartialUpdateInstanceRequest, Instance, UpdateInstanceMetadata>
      partialUpdateInstanceOperationCallable;
  private final UnaryCallable<DeleteInstanceRequest, Empty> deleteInstanceCallable;
  private final UnaryCallable<CreateClusterRequest, Operation> createClusterCallable;
  private final OperationCallable<CreateClusterRequest, Cluster, CreateClusterMetadata>
      createClusterOperationCallable;
  private final UnaryCallable<GetClusterRequest, Cluster> getClusterCallable;
  private final UnaryCallable<ListClustersRequest, ListClustersResponse> listClustersCallable;
  private final UnaryCallable<Cluster, Operation> updateClusterCallable;
  private final OperationCallable<Cluster, Cluster, UpdateClusterMetadata>
      updateClusterOperationCallable;
  private final UnaryCallable<PartialUpdateClusterRequest, Operation> partialUpdateClusterCallable;
  private final OperationCallable<
          PartialUpdateClusterRequest, Cluster, PartialUpdateClusterMetadata>
      partialUpdateClusterOperationCallable;
  private final UnaryCallable<DeleteClusterRequest, Empty> deleteClusterCallable;
  private final UnaryCallable<CreateAppProfileRequest, AppProfile> createAppProfileCallable;
  private final UnaryCallable<GetAppProfileRequest, AppProfile> getAppProfileCallable;
  private final UnaryCallable<ListAppProfilesRequest, ListAppProfilesResponse>
      listAppProfilesCallable;
  private final UnaryCallable<ListAppProfilesRequest, ListAppProfilesPagedResponse>
      listAppProfilesPagedCallable;
  private final UnaryCallable<UpdateAppProfileRequest, Operation> updateAppProfileCallable;
  private final OperationCallable<UpdateAppProfileRequest, AppProfile, UpdateAppProfileMetadata>
      updateAppProfileOperationCallable;
  private final UnaryCallable<DeleteAppProfileRequest, Empty> deleteAppProfileCallable;
  private final UnaryCallable<GetIamPolicyRequest, Policy> getIamPolicyCallable;
  private final UnaryCallable<SetIamPolicyRequest, Policy> setIamPolicyCallable;
  private final UnaryCallable<TestIamPermissionsRequest, TestIamPermissionsResponse>
      testIamPermissionsCallable;
  private final UnaryCallable<ListHotTabletsRequest, ListHotTabletsResponse> listHotTabletsCallable;
  private final UnaryCallable<ListHotTabletsRequest, ListHotTabletsPagedResponse>
      listHotTabletsPagedCallable;

  private final BackgroundResource backgroundResources;
  private final HttpJsonOperationsStub httpJsonOperationsStub;
  private final HttpJsonStubCallableFactory callableFactory;

  public static final HttpJsonBigtableInstanceAdminStub create(
      BigtableInstanceAdminStubSettings settings) throws IOException {
    return new HttpJsonBigtableInstanceAdminStub(settings, ClientContext.create(settings));
  }

  public static final HttpJsonBigtableInstanceAdminStub create(ClientContext clientContext)
      throws IOException {
    return new HttpJsonBigtableInstanceAdminStub(
        BigtableInstanceAdminStubSettings.newHttpJsonBuilder().build(), clientContext);
  }

  public static final HttpJsonBigtableInstanceAdminStub create(
      ClientContext clientContext, HttpJsonStubCallableFactory callableFactory) throws IOException {
    return new HttpJsonBigtableInstanceAdminStub(
        BigtableInstanceAdminStubSettings.newHttpJsonBuilder().build(),
        clientContext,
        callableFactory);
  }

  /**
   * Constructs an instance of HttpJsonBigtableInstanceAdminStub, using the given settings. This is
   * protected so that it is easy to make a subclass, but otherwise, the static factory methods
   * should be preferred.
   */
  protected HttpJsonBigtableInstanceAdminStub(
      BigtableInstanceAdminStubSettings settings, ClientContext clientContext) throws IOException {
    this(settings, clientContext, new HttpJsonBigtableInstanceAdminCallableFactory());
  }

  /**
   * Constructs an instance of HttpJsonBigtableInstanceAdminStub, using the given settings. This is
   * protected so that it is easy to make a subclass, but otherwise, the static factory methods
   * should be preferred.
   */
  protected HttpJsonBigtableInstanceAdminStub(
      BigtableInstanceAdminStubSettings settings,
      ClientContext clientContext,
      HttpJsonStubCallableFactory callableFactory)
      throws IOException {
    this.callableFactory = callableFactory;
    this.httpJsonOperationsStub =
        HttpJsonOperationsStub.create(clientContext, callableFactory, typeRegistry);

    HttpJsonCallSettings<CreateInstanceRequest, Operation> createInstanceTransportSettings =
        HttpJsonCallSettings.<CreateInstanceRequest, Operation>newBuilder()
            .setMethodDescriptor(createInstanceMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<GetInstanceRequest, Instance> getInstanceTransportSettings =
        HttpJsonCallSettings.<GetInstanceRequest, Instance>newBuilder()
            .setMethodDescriptor(getInstanceMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<ListInstancesRequest, ListInstancesResponse>
        listInstancesTransportSettings =
            HttpJsonCallSettings.<ListInstancesRequest, ListInstancesResponse>newBuilder()
                .setMethodDescriptor(listInstancesMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<Instance, Instance> updateInstanceTransportSettings =
        HttpJsonCallSettings.<Instance, Instance>newBuilder()
            .setMethodDescriptor(updateInstanceMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<PartialUpdateInstanceRequest, Operation>
        partialUpdateInstanceTransportSettings =
            HttpJsonCallSettings.<PartialUpdateInstanceRequest, Operation>newBuilder()
                .setMethodDescriptor(partialUpdateInstanceMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<DeleteInstanceRequest, Empty> deleteInstanceTransportSettings =
        HttpJsonCallSettings.<DeleteInstanceRequest, Empty>newBuilder()
            .setMethodDescriptor(deleteInstanceMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<CreateClusterRequest, Operation> createClusterTransportSettings =
        HttpJsonCallSettings.<CreateClusterRequest, Operation>newBuilder()
            .setMethodDescriptor(createClusterMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<GetClusterRequest, Cluster> getClusterTransportSettings =
        HttpJsonCallSettings.<GetClusterRequest, Cluster>newBuilder()
            .setMethodDescriptor(getClusterMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<ListClustersRequest, ListClustersResponse> listClustersTransportSettings =
        HttpJsonCallSettings.<ListClustersRequest, ListClustersResponse>newBuilder()
            .setMethodDescriptor(listClustersMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<Cluster, Operation> updateClusterTransportSettings =
        HttpJsonCallSettings.<Cluster, Operation>newBuilder()
            .setMethodDescriptor(updateClusterMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<PartialUpdateClusterRequest, Operation>
        partialUpdateClusterTransportSettings =
            HttpJsonCallSettings.<PartialUpdateClusterRequest, Operation>newBuilder()
                .setMethodDescriptor(partialUpdateClusterMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<DeleteClusterRequest, Empty> deleteClusterTransportSettings =
        HttpJsonCallSettings.<DeleteClusterRequest, Empty>newBuilder()
            .setMethodDescriptor(deleteClusterMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<CreateAppProfileRequest, AppProfile> createAppProfileTransportSettings =
        HttpJsonCallSettings.<CreateAppProfileRequest, AppProfile>newBuilder()
            .setMethodDescriptor(createAppProfileMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<GetAppProfileRequest, AppProfile> getAppProfileTransportSettings =
        HttpJsonCallSettings.<GetAppProfileRequest, AppProfile>newBuilder()
            .setMethodDescriptor(getAppProfileMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<ListAppProfilesRequest, ListAppProfilesResponse>
        listAppProfilesTransportSettings =
            HttpJsonCallSettings.<ListAppProfilesRequest, ListAppProfilesResponse>newBuilder()
                .setMethodDescriptor(listAppProfilesMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<UpdateAppProfileRequest, Operation> updateAppProfileTransportSettings =
        HttpJsonCallSettings.<UpdateAppProfileRequest, Operation>newBuilder()
            .setMethodDescriptor(updateAppProfileMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<DeleteAppProfileRequest, Empty> deleteAppProfileTransportSettings =
        HttpJsonCallSettings.<DeleteAppProfileRequest, Empty>newBuilder()
            .setMethodDescriptor(deleteAppProfileMethodDescriptor)
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
    HttpJsonCallSettings<ListHotTabletsRequest, ListHotTabletsResponse>
        listHotTabletsTransportSettings =
            HttpJsonCallSettings.<ListHotTabletsRequest, ListHotTabletsResponse>newBuilder()
                .setMethodDescriptor(listHotTabletsMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();

    this.createInstanceCallable =
        callableFactory.createUnaryCallable(
            createInstanceTransportSettings, settings.createInstanceSettings(), clientContext);
    this.createInstanceOperationCallable =
        callableFactory.createOperationCallable(
            createInstanceTransportSettings,
            settings.createInstanceOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.getInstanceCallable =
        callableFactory.createUnaryCallable(
            getInstanceTransportSettings, settings.getInstanceSettings(), clientContext);
    this.listInstancesCallable =
        callableFactory.createUnaryCallable(
            listInstancesTransportSettings, settings.listInstancesSettings(), clientContext);
    this.updateInstanceCallable =
        callableFactory.createUnaryCallable(
            updateInstanceTransportSettings, settings.updateInstanceSettings(), clientContext);
    this.partialUpdateInstanceCallable =
        callableFactory.createUnaryCallable(
            partialUpdateInstanceTransportSettings,
            settings.partialUpdateInstanceSettings(),
            clientContext);
    this.partialUpdateInstanceOperationCallable =
        callableFactory.createOperationCallable(
            partialUpdateInstanceTransportSettings,
            settings.partialUpdateInstanceOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.deleteInstanceCallable =
        callableFactory.createUnaryCallable(
            deleteInstanceTransportSettings, settings.deleteInstanceSettings(), clientContext);
    this.createClusterCallable =
        callableFactory.createUnaryCallable(
            createClusterTransportSettings, settings.createClusterSettings(), clientContext);
    this.createClusterOperationCallable =
        callableFactory.createOperationCallable(
            createClusterTransportSettings,
            settings.createClusterOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.getClusterCallable =
        callableFactory.createUnaryCallable(
            getClusterTransportSettings, settings.getClusterSettings(), clientContext);
    this.listClustersCallable =
        callableFactory.createUnaryCallable(
            listClustersTransportSettings, settings.listClustersSettings(), clientContext);
    this.updateClusterCallable =
        callableFactory.createUnaryCallable(
            updateClusterTransportSettings, settings.updateClusterSettings(), clientContext);
    this.updateClusterOperationCallable =
        callableFactory.createOperationCallable(
            updateClusterTransportSettings,
            settings.updateClusterOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.partialUpdateClusterCallable =
        callableFactory.createUnaryCallable(
            partialUpdateClusterTransportSettings,
            settings.partialUpdateClusterSettings(),
            clientContext);
    this.partialUpdateClusterOperationCallable =
        callableFactory.createOperationCallable(
            partialUpdateClusterTransportSettings,
            settings.partialUpdateClusterOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.deleteClusterCallable =
        callableFactory.createUnaryCallable(
            deleteClusterTransportSettings, settings.deleteClusterSettings(), clientContext);
    this.createAppProfileCallable =
        callableFactory.createUnaryCallable(
            createAppProfileTransportSettings, settings.createAppProfileSettings(), clientContext);
    this.getAppProfileCallable =
        callableFactory.createUnaryCallable(
            getAppProfileTransportSettings, settings.getAppProfileSettings(), clientContext);
    this.listAppProfilesCallable =
        callableFactory.createUnaryCallable(
            listAppProfilesTransportSettings, settings.listAppProfilesSettings(), clientContext);
    this.listAppProfilesPagedCallable =
        callableFactory.createPagedCallable(
            listAppProfilesTransportSettings, settings.listAppProfilesSettings(), clientContext);
    this.updateAppProfileCallable =
        callableFactory.createUnaryCallable(
            updateAppProfileTransportSettings, settings.updateAppProfileSettings(), clientContext);
    this.updateAppProfileOperationCallable =
        callableFactory.createOperationCallable(
            updateAppProfileTransportSettings,
            settings.updateAppProfileOperationSettings(),
            clientContext,
            httpJsonOperationsStub);
    this.deleteAppProfileCallable =
        callableFactory.createUnaryCallable(
            deleteAppProfileTransportSettings, settings.deleteAppProfileSettings(), clientContext);
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
    this.listHotTabletsCallable =
        callableFactory.createUnaryCallable(
            listHotTabletsTransportSettings, settings.listHotTabletsSettings(), clientContext);
    this.listHotTabletsPagedCallable =
        callableFactory.createPagedCallable(
            listHotTabletsTransportSettings, settings.listHotTabletsSettings(), clientContext);

    this.backgroundResources =
        new BackgroundResourceAggregation(clientContext.getBackgroundResources());
  }

  @InternalApi
  public static List<ApiMethodDescriptor> getMethodDescriptors() {
    List<ApiMethodDescriptor> methodDescriptors = new ArrayList<>();
    methodDescriptors.add(createInstanceMethodDescriptor);
    methodDescriptors.add(getInstanceMethodDescriptor);
    methodDescriptors.add(listInstancesMethodDescriptor);
    methodDescriptors.add(updateInstanceMethodDescriptor);
    methodDescriptors.add(partialUpdateInstanceMethodDescriptor);
    methodDescriptors.add(deleteInstanceMethodDescriptor);
    methodDescriptors.add(createClusterMethodDescriptor);
    methodDescriptors.add(getClusterMethodDescriptor);
    methodDescriptors.add(listClustersMethodDescriptor);
    methodDescriptors.add(updateClusterMethodDescriptor);
    methodDescriptors.add(partialUpdateClusterMethodDescriptor);
    methodDescriptors.add(deleteClusterMethodDescriptor);
    methodDescriptors.add(createAppProfileMethodDescriptor);
    methodDescriptors.add(getAppProfileMethodDescriptor);
    methodDescriptors.add(listAppProfilesMethodDescriptor);
    methodDescriptors.add(updateAppProfileMethodDescriptor);
    methodDescriptors.add(deleteAppProfileMethodDescriptor);
    methodDescriptors.add(getIamPolicyMethodDescriptor);
    methodDescriptors.add(setIamPolicyMethodDescriptor);
    methodDescriptors.add(testIamPermissionsMethodDescriptor);
    methodDescriptors.add(listHotTabletsMethodDescriptor);
    return methodDescriptors;
  }

  public HttpJsonOperationsStub getHttpJsonOperationsStub() {
    return httpJsonOperationsStub;
  }

  @Override
  public UnaryCallable<CreateInstanceRequest, Operation> createInstanceCallable() {
    return createInstanceCallable;
  }

  @Override
  public OperationCallable<CreateInstanceRequest, Instance, CreateInstanceMetadata>
      createInstanceOperationCallable() {
    return createInstanceOperationCallable;
  }

  @Override
  public UnaryCallable<GetInstanceRequest, Instance> getInstanceCallable() {
    return getInstanceCallable;
  }

  @Override
  public UnaryCallable<ListInstancesRequest, ListInstancesResponse> listInstancesCallable() {
    return listInstancesCallable;
  }

  @Override
  public UnaryCallable<Instance, Instance> updateInstanceCallable() {
    return updateInstanceCallable;
  }

  @Override
  public UnaryCallable<PartialUpdateInstanceRequest, Operation> partialUpdateInstanceCallable() {
    return partialUpdateInstanceCallable;
  }

  @Override
  public OperationCallable<PartialUpdateInstanceRequest, Instance, UpdateInstanceMetadata>
      partialUpdateInstanceOperationCallable() {
    return partialUpdateInstanceOperationCallable;
  }

  @Override
  public UnaryCallable<DeleteInstanceRequest, Empty> deleteInstanceCallable() {
    return deleteInstanceCallable;
  }

  @Override
  public UnaryCallable<CreateClusterRequest, Operation> createClusterCallable() {
    return createClusterCallable;
  }

  @Override
  public OperationCallable<CreateClusterRequest, Cluster, CreateClusterMetadata>
      createClusterOperationCallable() {
    return createClusterOperationCallable;
  }

  @Override
  public UnaryCallable<GetClusterRequest, Cluster> getClusterCallable() {
    return getClusterCallable;
  }

  @Override
  public UnaryCallable<ListClustersRequest, ListClustersResponse> listClustersCallable() {
    return listClustersCallable;
  }

  @Override
  public UnaryCallable<Cluster, Operation> updateClusterCallable() {
    return updateClusterCallable;
  }

  @Override
  public OperationCallable<Cluster, Cluster, UpdateClusterMetadata>
      updateClusterOperationCallable() {
    return updateClusterOperationCallable;
  }

  @Override
  public UnaryCallable<PartialUpdateClusterRequest, Operation> partialUpdateClusterCallable() {
    return partialUpdateClusterCallable;
  }

  @Override
  public OperationCallable<PartialUpdateClusterRequest, Cluster, PartialUpdateClusterMetadata>
      partialUpdateClusterOperationCallable() {
    return partialUpdateClusterOperationCallable;
  }

  @Override
  public UnaryCallable<DeleteClusterRequest, Empty> deleteClusterCallable() {
    return deleteClusterCallable;
  }

  @Override
  public UnaryCallable<CreateAppProfileRequest, AppProfile> createAppProfileCallable() {
    return createAppProfileCallable;
  }

  @Override
  public UnaryCallable<GetAppProfileRequest, AppProfile> getAppProfileCallable() {
    return getAppProfileCallable;
  }

  @Override
  public UnaryCallable<ListAppProfilesRequest, ListAppProfilesResponse> listAppProfilesCallable() {
    return listAppProfilesCallable;
  }

  @Override
  public UnaryCallable<ListAppProfilesRequest, ListAppProfilesPagedResponse>
      listAppProfilesPagedCallable() {
    return listAppProfilesPagedCallable;
  }

  @Override
  public UnaryCallable<UpdateAppProfileRequest, Operation> updateAppProfileCallable() {
    return updateAppProfileCallable;
  }

  @Override
  public OperationCallable<UpdateAppProfileRequest, AppProfile, UpdateAppProfileMetadata>
      updateAppProfileOperationCallable() {
    return updateAppProfileOperationCallable;
  }

  @Override
  public UnaryCallable<DeleteAppProfileRequest, Empty> deleteAppProfileCallable() {
    return deleteAppProfileCallable;
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
  public UnaryCallable<ListHotTabletsRequest, ListHotTabletsResponse> listHotTabletsCallable() {
    return listHotTabletsCallable;
  }

  @Override
  public UnaryCallable<ListHotTabletsRequest, ListHotTabletsPagedResponse>
      listHotTabletsPagedCallable() {
    return listHotTabletsPagedCallable;
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
