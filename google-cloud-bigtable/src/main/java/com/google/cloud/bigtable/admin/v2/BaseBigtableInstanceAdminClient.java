/*
 * Copyright 2020 Google LLC
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

package com.google.cloud.bigtable.admin.v2;

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.api.gax.core.BackgroundResource;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.api.gax.paging.AbstractFixedSizeCollection;
import com.google.api.gax.paging.AbstractPage;
import com.google.api.gax.paging.AbstractPagedListResponse;
import com.google.api.gax.rpc.OperationCallable;
import com.google.api.gax.rpc.PageContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.api.resourcenames.ResourceName;
import com.google.bigtable.admin.v2.AppProfile;
import com.google.bigtable.admin.v2.AppProfileName;
import com.google.bigtable.admin.v2.Cluster;
import com.google.bigtable.admin.v2.ClusterName;
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
import com.google.bigtable.admin.v2.InstanceName;
import com.google.bigtable.admin.v2.ListAppProfilesRequest;
import com.google.bigtable.admin.v2.ListAppProfilesResponse;
import com.google.bigtable.admin.v2.ListClustersRequest;
import com.google.bigtable.admin.v2.ListClustersResponse;
import com.google.bigtable.admin.v2.ListInstancesRequest;
import com.google.bigtable.admin.v2.ListInstancesResponse;
import com.google.bigtable.admin.v2.PartialUpdateInstanceRequest;
import com.google.bigtable.admin.v2.ProjectName;
import com.google.bigtable.admin.v2.UpdateAppProfileMetadata;
import com.google.bigtable.admin.v2.UpdateAppProfileRequest;
import com.google.bigtable.admin.v2.UpdateClusterMetadata;
import com.google.bigtable.admin.v2.UpdateInstanceMetadata;
import com.google.cloud.bigtable.admin.v2.stub.BigtableInstanceAdminStub;
import com.google.cloud.bigtable.admin.v2.stub.BigtableInstanceAdminStubSettings;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.iam.v1.GetIamPolicyRequest;
import com.google.iam.v1.Policy;
import com.google.iam.v1.SetIamPolicyRequest;
import com.google.iam.v1.TestIamPermissionsRequest;
import com.google.iam.v1.TestIamPermissionsResponse;
import com.google.longrunning.Operation;
import com.google.longrunning.OperationsClient;
import com.google.protobuf.Empty;
import com.google.protobuf.FieldMask;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Generated;

// AUTO-GENERATED DOCUMENTATION AND CLASS.
/** For internal use only. */
@Generated("by gapic-generator")
@InternalApi
public class BaseBigtableInstanceAdminClient implements BackgroundResource {
  private final BaseBigtableInstanceAdminSettings settings;
  private final BigtableInstanceAdminStub stub;
  private final OperationsClient operationsClient;

  /** Constructs an instance of BaseBigtableInstanceAdminClient with default settings. */
  public static final BaseBigtableInstanceAdminClient create() throws IOException {
    return create(BaseBigtableInstanceAdminSettings.newBuilder().build());
  }

  /**
   * Constructs an instance of BaseBigtableInstanceAdminClient, using the given settings. The
   * channels are created based on the settings passed in, or defaults for any settings that are not
   * set.
   */
  public static final BaseBigtableInstanceAdminClient create(
      BaseBigtableInstanceAdminSettings settings) throws IOException {
    return new BaseBigtableInstanceAdminClient(settings);
  }

  /**
   * Constructs an instance of BaseBigtableInstanceAdminClient, using the given stub for making
   * calls. This is for advanced usage - prefer using create(BaseBigtableInstanceAdminSettings).
   */
  @BetaApi("A restructuring of stub classes is planned, so this may break in the future")
  public static final BaseBigtableInstanceAdminClient create(BigtableInstanceAdminStub stub) {
    return new BaseBigtableInstanceAdminClient(stub);
  }

  /**
   * Constructs an instance of BaseBigtableInstanceAdminClient, using the given settings. This is
   * protected so that it is easy to make a subclass, but otherwise, the static factory methods
   * should be preferred.
   */
  protected BaseBigtableInstanceAdminClient(BaseBigtableInstanceAdminSettings settings)
      throws IOException {
    this.settings = settings;
    this.stub = ((BigtableInstanceAdminStubSettings) settings.getStubSettings()).createStub();
    this.operationsClient = OperationsClient.create(this.stub.getOperationsStub());
  }

  @BetaApi("A restructuring of stub classes is planned, so this may break in the future")
  protected BaseBigtableInstanceAdminClient(BigtableInstanceAdminStub stub) {
    this.settings = null;
    this.stub = stub;
    this.operationsClient = OperationsClient.create(this.stub.getOperationsStub());
  }

  public final BaseBigtableInstanceAdminSettings getSettings() {
    return settings;
  }

  @BetaApi("A restructuring of stub classes is planned, so this may break in the future")
  public BigtableInstanceAdminStub getStub() {
    return stub;
  }

  /**
   * Returns the OperationsClient that can be used to query the status of a long-running operation
   * returned by another API method call.
   */
  public final OperationsClient getOperationsClient() {
    return operationsClient;
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Create an instance within a project.
   *
   * @param parent Required. The unique name of the project in which to create the new instance.
   *     Values are of the form `projects/{project}`.
   * @param instance_id Required. The ID to be used when referring to the new instance within its
   *     project, e.g., just `myinstance` rather than `projects/myproject/instances/myinstance`.
   * @param instance Required. The instance to create. Fields marked `OutputOnly` must be left
   *     blank.
   * @param clusters Required. The clusters to be created within the instance, mapped by desired
   *     cluster ID, e.g., just `mycluster` rather than
   *     `projects/myproject/instances/myinstance/clusters/mycluster`. Fields marked `OutputOnly`
   *     must be left blank. Currently, at most four clusters can be specified.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<Instance, CreateInstanceMetadata> createInstanceAsync(
      ProjectName parent, String instanceId, Instance instance, Map<String, Cluster> clusters) {
    CreateInstanceRequest request =
        CreateInstanceRequest.newBuilder()
            .setParent(parent == null ? null : parent.toString())
            .setInstanceId(instanceId)
            .setInstance(instance)
            .putAllClusters(clusters)
            .build();
    return createInstanceAsync(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Create an instance within a project.
   *
   * @param parent Required. The unique name of the project in which to create the new instance.
   *     Values are of the form `projects/{project}`.
   * @param instance_id Required. The ID to be used when referring to the new instance within its
   *     project, e.g., just `myinstance` rather than `projects/myproject/instances/myinstance`.
   * @param instance Required. The instance to create. Fields marked `OutputOnly` must be left
   *     blank.
   * @param clusters Required. The clusters to be created within the instance, mapped by desired
   *     cluster ID, e.g., just `mycluster` rather than
   *     `projects/myproject/instances/myinstance/clusters/mycluster`. Fields marked `OutputOnly`
   *     must be left blank. Currently, at most four clusters can be specified.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<Instance, CreateInstanceMetadata> createInstanceAsync(
      String parent, String instanceId, Instance instance, Map<String, Cluster> clusters) {
    CreateInstanceRequest request =
        CreateInstanceRequest.newBuilder()
            .setParent(parent)
            .setInstanceId(instanceId)
            .setInstance(instance)
            .putAllClusters(clusters)
            .build();
    return createInstanceAsync(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Create an instance within a project.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<Instance, CreateInstanceMetadata> createInstanceAsync(
      CreateInstanceRequest request) {
    return createInstanceOperationCallable().futureCall(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Create an instance within a project.
   *
   * <p>Sample code:
   */
  public final OperationCallable<CreateInstanceRequest, Instance, CreateInstanceMetadata>
      createInstanceOperationCallable() {
    return stub.createInstanceOperationCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Create an instance within a project.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<CreateInstanceRequest, Operation> createInstanceCallable() {
    return stub.createInstanceCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about an instance.
   *
   * @param name Required. The unique name of the requested instance. Values are of the form
   *     `projects/{project}/instances/{instance}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Instance getInstance(InstanceName name) {
    GetInstanceRequest request =
        GetInstanceRequest.newBuilder().setName(name == null ? null : name.toString()).build();
    return getInstance(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about an instance.
   *
   * @param name Required. The unique name of the requested instance. Values are of the form
   *     `projects/{project}/instances/{instance}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Instance getInstance(String name) {
    GetInstanceRequest request = GetInstanceRequest.newBuilder().setName(name).build();
    return getInstance(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about an instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Instance getInstance(GetInstanceRequest request) {
    return getInstanceCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<GetInstanceRequest, Instance> getInstanceCallable() {
    return stub.getInstanceCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about instances in a project.
   *
   * @param parent Required. The unique name of the project for which a list of instances is
   *     requested. Values are of the form `projects/{project}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ListInstancesResponse listInstances(ProjectName parent) {
    ListInstancesRequest request =
        ListInstancesRequest.newBuilder()
            .setParent(parent == null ? null : parent.toString())
            .build();
    return listInstances(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about instances in a project.
   *
   * @param parent Required. The unique name of the project for which a list of instances is
   *     requested. Values are of the form `projects/{project}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ListInstancesResponse listInstances(String parent) {
    ListInstancesRequest request = ListInstancesRequest.newBuilder().setParent(parent).build();
    return listInstances(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about instances in a project.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ListInstancesResponse listInstances(ListInstancesRequest request) {
    return listInstancesCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about instances in a project.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<ListInstancesRequest, ListInstancesResponse> listInstancesCallable() {
    return stub.listInstancesCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Updates an instance within a project. This method updates only the display name and type for an
   * Instance. To update other Instance properties, such as labels, use PartialUpdateInstance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Instance updateInstance(Instance request) {
    return updateInstanceCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Updates an instance within a project. This method updates only the display name and type for an
   * Instance. To update other Instance properties, such as labels, use PartialUpdateInstance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<Instance, Instance> updateInstanceCallable() {
    return stub.updateInstanceCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Partially updates an instance within a project. This method can modify all fields of an
   * Instance and is the preferred way to update an Instance.
   *
   * @param instance Required. The Instance which will (partially) replace the current value.
   * @param update_mask Required. The subset of Instance fields which should be replaced. Must be
   *     explicitly set.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<Instance, UpdateInstanceMetadata> partialUpdateInstanceAsync(
      Instance instance, FieldMask updateMask) {
    PartialUpdateInstanceRequest request =
        PartialUpdateInstanceRequest.newBuilder()
            .setInstance(instance)
            .setUpdateMask(updateMask)
            .build();
    return partialUpdateInstanceAsync(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Partially updates an instance within a project. This method can modify all fields of an
   * Instance and is the preferred way to update an Instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<Instance, UpdateInstanceMetadata> partialUpdateInstanceAsync(
      PartialUpdateInstanceRequest request) {
    return partialUpdateInstanceOperationCallable().futureCall(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Partially updates an instance within a project. This method can modify all fields of an
   * Instance and is the preferred way to update an Instance.
   *
   * <p>Sample code:
   */
  public final OperationCallable<PartialUpdateInstanceRequest, Instance, UpdateInstanceMetadata>
      partialUpdateInstanceOperationCallable() {
    return stub.partialUpdateInstanceOperationCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Partially updates an instance within a project. This method can modify all fields of an
   * Instance and is the preferred way to update an Instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<PartialUpdateInstanceRequest, Operation>
      partialUpdateInstanceCallable() {
    return stub.partialUpdateInstanceCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Delete an instance from a project.
   *
   * @param name Required. The unique name of the instance to be deleted. Values are of the form
   *     `projects/{project}/instances/{instance}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final void deleteInstance(InstanceName name) {
    DeleteInstanceRequest request =
        DeleteInstanceRequest.newBuilder().setName(name == null ? null : name.toString()).build();
    deleteInstance(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Delete an instance from a project.
   *
   * @param name Required. The unique name of the instance to be deleted. Values are of the form
   *     `projects/{project}/instances/{instance}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final void deleteInstance(String name) {
    DeleteInstanceRequest request = DeleteInstanceRequest.newBuilder().setName(name).build();
    deleteInstance(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Delete an instance from a project.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final void deleteInstance(DeleteInstanceRequest request) {
    deleteInstanceCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Delete an instance from a project.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<DeleteInstanceRequest, Empty> deleteInstanceCallable() {
    return stub.deleteInstanceCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Creates a cluster within an instance.
   *
   * @param parent Required. The unique name of the instance in which to create the new cluster.
   *     Values are of the form `projects/{project}/instances/{instance}`.
   * @param cluster_id Required. The ID to be used when referring to the new cluster within its
   *     instance, e.g., just `mycluster` rather than
   *     `projects/myproject/instances/myinstance/clusters/mycluster`.
   * @param cluster Required. The cluster to be created. Fields marked `OutputOnly` must be left
   *     blank.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<Cluster, CreateClusterMetadata> createClusterAsync(
      InstanceName parent, String clusterId, Cluster cluster) {
    CreateClusterRequest request =
        CreateClusterRequest.newBuilder()
            .setParent(parent == null ? null : parent.toString())
            .setClusterId(clusterId)
            .setCluster(cluster)
            .build();
    return createClusterAsync(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Creates a cluster within an instance.
   *
   * @param parent Required. The unique name of the instance in which to create the new cluster.
   *     Values are of the form `projects/{project}/instances/{instance}`.
   * @param cluster_id Required. The ID to be used when referring to the new cluster within its
   *     instance, e.g., just `mycluster` rather than
   *     `projects/myproject/instances/myinstance/clusters/mycluster`.
   * @param cluster Required. The cluster to be created. Fields marked `OutputOnly` must be left
   *     blank.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<Cluster, CreateClusterMetadata> createClusterAsync(
      String parent, String clusterId, Cluster cluster) {
    CreateClusterRequest request =
        CreateClusterRequest.newBuilder()
            .setParent(parent)
            .setClusterId(clusterId)
            .setCluster(cluster)
            .build();
    return createClusterAsync(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Creates a cluster within an instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<Cluster, CreateClusterMetadata> createClusterAsync(
      CreateClusterRequest request) {
    return createClusterOperationCallable().futureCall(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Creates a cluster within an instance.
   *
   * <p>Sample code:
   */
  public final OperationCallable<CreateClusterRequest, Cluster, CreateClusterMetadata>
      createClusterOperationCallable() {
    return stub.createClusterOperationCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Creates a cluster within an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<CreateClusterRequest, Operation> createClusterCallable() {
    return stub.createClusterCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about a cluster.
   *
   * @param name Required. The unique name of the requested cluster. Values are of the form
   *     `projects/{project}/instances/{instance}/clusters/{cluster}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Cluster getCluster(ClusterName name) {
    GetClusterRequest request =
        GetClusterRequest.newBuilder().setName(name == null ? null : name.toString()).build();
    return getCluster(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about a cluster.
   *
   * @param name Required. The unique name of the requested cluster. Values are of the form
   *     `projects/{project}/instances/{instance}/clusters/{cluster}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Cluster getCluster(String name) {
    GetClusterRequest request = GetClusterRequest.newBuilder().setName(name).build();
    return getCluster(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about a cluster.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Cluster getCluster(GetClusterRequest request) {
    return getClusterCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about a cluster.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<GetClusterRequest, Cluster> getClusterCallable() {
    return stub.getClusterCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about clusters in an instance.
   *
   * @param parent Required. The unique name of the instance for which a list of clusters is
   *     requested. Values are of the form `projects/{project}/instances/{instance}`. Use
   *     `{instance} = '-'` to list Clusters for all Instances in a project, e.g.,
   *     `projects/myproject/instances/-`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ListClustersResponse listClusters(InstanceName parent) {
    ListClustersRequest request =
        ListClustersRequest.newBuilder()
            .setParent(parent == null ? null : parent.toString())
            .build();
    return listClusters(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about clusters in an instance.
   *
   * @param parent Required. The unique name of the instance for which a list of clusters is
   *     requested. Values are of the form `projects/{project}/instances/{instance}`. Use
   *     `{instance} = '-'` to list Clusters for all Instances in a project, e.g.,
   *     `projects/myproject/instances/-`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ListClustersResponse listClusters(String parent) {
    ListClustersRequest request = ListClustersRequest.newBuilder().setParent(parent).build();
    return listClusters(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about clusters in an instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ListClustersResponse listClusters(ListClustersRequest request) {
    return listClustersCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about clusters in an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<ListClustersRequest, ListClustersResponse> listClustersCallable() {
    return stub.listClustersCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Updates a cluster within an instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<Cluster, UpdateClusterMetadata> updateClusterAsync(Cluster request) {
    return updateClusterOperationCallable().futureCall(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Updates a cluster within an instance.
   *
   * <p>Sample code:
   */
  public final OperationCallable<Cluster, Cluster, UpdateClusterMetadata>
      updateClusterOperationCallable() {
    return stub.updateClusterOperationCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Updates a cluster within an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<Cluster, Operation> updateClusterCallable() {
    return stub.updateClusterCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Deletes a cluster from an instance.
   *
   * @param name Required. The unique name of the cluster to be deleted. Values are of the form
   *     `projects/{project}/instances/{instance}/clusters/{cluster}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final void deleteCluster(ClusterName name) {
    DeleteClusterRequest request =
        DeleteClusterRequest.newBuilder().setName(name == null ? null : name.toString()).build();
    deleteCluster(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Deletes a cluster from an instance.
   *
   * @param name Required. The unique name of the cluster to be deleted. Values are of the form
   *     `projects/{project}/instances/{instance}/clusters/{cluster}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final void deleteCluster(String name) {
    DeleteClusterRequest request = DeleteClusterRequest.newBuilder().setName(name).build();
    deleteCluster(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Deletes a cluster from an instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final void deleteCluster(DeleteClusterRequest request) {
    deleteClusterCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Deletes a cluster from an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<DeleteClusterRequest, Empty> deleteClusterCallable() {
    return stub.deleteClusterCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Creates an app profile within an instance.
   *
   * @param parent Required. The unique name of the instance in which to create the new app profile.
   *     Values are of the form `projects/{project}/instances/{instance}`.
   * @param app_profile_id Required. The ID to be used when referring to the new app profile within
   *     its instance, e.g., just `myprofile` rather than
   *     `projects/myproject/instances/myinstance/appProfiles/myprofile`.
   * @param app_profile Required. The app profile to be created. Fields marked `OutputOnly` will be
   *     ignored.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final AppProfile createAppProfile(
      InstanceName parent, String appProfileId, AppProfile appProfile) {
    CreateAppProfileRequest request =
        CreateAppProfileRequest.newBuilder()
            .setParent(parent == null ? null : parent.toString())
            .setAppProfileId(appProfileId)
            .setAppProfile(appProfile)
            .build();
    return createAppProfile(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Creates an app profile within an instance.
   *
   * @param parent Required. The unique name of the instance in which to create the new app profile.
   *     Values are of the form `projects/{project}/instances/{instance}`.
   * @param app_profile_id Required. The ID to be used when referring to the new app profile within
   *     its instance, e.g., just `myprofile` rather than
   *     `projects/myproject/instances/myinstance/appProfiles/myprofile`.
   * @param app_profile Required. The app profile to be created. Fields marked `OutputOnly` will be
   *     ignored.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final AppProfile createAppProfile(
      String parent, String appProfileId, AppProfile appProfile) {
    CreateAppProfileRequest request =
        CreateAppProfileRequest.newBuilder()
            .setParent(parent)
            .setAppProfileId(appProfileId)
            .setAppProfile(appProfile)
            .build();
    return createAppProfile(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Creates an app profile within an instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final AppProfile createAppProfile(CreateAppProfileRequest request) {
    return createAppProfileCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Creates an app profile within an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<CreateAppProfileRequest, AppProfile> createAppProfileCallable() {
    return stub.createAppProfileCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about an app profile.
   *
   * @param name Required. The unique name of the requested app profile. Values are of the form
   *     `projects/{project}/instances/{instance}/appProfiles/{app_profile}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final AppProfile getAppProfile(AppProfileName name) {
    GetAppProfileRequest request =
        GetAppProfileRequest.newBuilder().setName(name == null ? null : name.toString()).build();
    return getAppProfile(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about an app profile.
   *
   * @param name Required. The unique name of the requested app profile. Values are of the form
   *     `projects/{project}/instances/{instance}/appProfiles/{app_profile}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final AppProfile getAppProfile(String name) {
    GetAppProfileRequest request = GetAppProfileRequest.newBuilder().setName(name).build();
    return getAppProfile(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about an app profile.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final AppProfile getAppProfile(GetAppProfileRequest request) {
    return getAppProfileCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets information about an app profile.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<GetAppProfileRequest, AppProfile> getAppProfileCallable() {
    return stub.getAppProfileCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about app profiles in an instance.
   *
   * @param parent Required. The unique name of the instance for which a list of app profiles is
   *     requested. Values are of the form `projects/{project}/instances/{instance}`. Use
   *     `{instance} = '-'` to list AppProfiles for all Instances in a project, e.g.,
   *     `projects/myproject/instances/-`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ListAppProfilesPagedResponse listAppProfiles(InstanceName parent) {
    ListAppProfilesRequest request =
        ListAppProfilesRequest.newBuilder()
            .setParent(parent == null ? null : parent.toString())
            .build();
    return listAppProfiles(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about app profiles in an instance.
   *
   * @param parent Required. The unique name of the instance for which a list of app profiles is
   *     requested. Values are of the form `projects/{project}/instances/{instance}`. Use
   *     `{instance} = '-'` to list AppProfiles for all Instances in a project, e.g.,
   *     `projects/myproject/instances/-`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ListAppProfilesPagedResponse listAppProfiles(String parent) {
    ListAppProfilesRequest request = ListAppProfilesRequest.newBuilder().setParent(parent).build();
    return listAppProfiles(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about app profiles in an instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ListAppProfilesPagedResponse listAppProfiles(ListAppProfilesRequest request) {
    return listAppProfilesPagedCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about app profiles in an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<ListAppProfilesRequest, ListAppProfilesPagedResponse>
      listAppProfilesPagedCallable() {
    return stub.listAppProfilesPagedCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Lists information about app profiles in an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<ListAppProfilesRequest, ListAppProfilesResponse>
      listAppProfilesCallable() {
    return stub.listAppProfilesCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Updates an app profile within an instance.
   *
   * @param app_profile Required. The app profile which will (partially) replace the current value.
   * @param update_mask Required. The subset of app profile fields which should be replaced. If
   *     unset, all fields will be replaced.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<AppProfile, UpdateAppProfileMetadata> updateAppProfileAsync(
      AppProfile appProfile, FieldMask updateMask) {
    UpdateAppProfileRequest request =
        UpdateAppProfileRequest.newBuilder()
            .setAppProfile(appProfile)
            .setUpdateMask(updateMask)
            .build();
    return updateAppProfileAsync(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Updates an app profile within an instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final OperationFuture<AppProfile, UpdateAppProfileMetadata> updateAppProfileAsync(
      UpdateAppProfileRequest request) {
    return updateAppProfileOperationCallable().futureCall(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Updates an app profile within an instance.
   *
   * <p>Sample code:
   */
  public final OperationCallable<UpdateAppProfileRequest, AppProfile, UpdateAppProfileMetadata>
      updateAppProfileOperationCallable() {
    return stub.updateAppProfileOperationCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Updates an app profile within an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<UpdateAppProfileRequest, Operation> updateAppProfileCallable() {
    return stub.updateAppProfileCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Deletes an app profile from an instance.
   *
   * @param name Required. The unique name of the app profile to be deleted. Values are of the form
   *     `projects/{project}/instances/{instance}/appProfiles/{app_profile}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final void deleteAppProfile(AppProfileName name) {
    DeleteAppProfileRequest request =
        DeleteAppProfileRequest.newBuilder().setName(name == null ? null : name.toString()).build();
    deleteAppProfile(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Deletes an app profile from an instance.
   *
   * @param name Required. The unique name of the app profile to be deleted. Values are of the form
   *     `projects/{project}/instances/{instance}/appProfiles/{app_profile}`.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final void deleteAppProfile(String name) {
    DeleteAppProfileRequest request = DeleteAppProfileRequest.newBuilder().setName(name).build();
    deleteAppProfile(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Deletes an app profile from an instance.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final void deleteAppProfile(DeleteAppProfileRequest request) {
    deleteAppProfileCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Deletes an app profile from an instance.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<DeleteAppProfileRequest, Empty> deleteAppProfileCallable() {
    return stub.deleteAppProfileCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets the access control policy for an instance resource. Returns an empty policy if an instance
   * exists but does not have a policy set.
   *
   * @param resource REQUIRED: The resource for which the policy is being requested. See the
   *     operation documentation for the appropriate value for this field.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Policy getIamPolicy(ResourceName resource) {
    GetIamPolicyRequest request =
        GetIamPolicyRequest.newBuilder()
            .setResource(resource == null ? null : resource.toString())
            .build();
    return getIamPolicy(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets the access control policy for an instance resource. Returns an empty policy if an instance
   * exists but does not have a policy set.
   *
   * @param resource REQUIRED: The resource for which the policy is being requested. See the
   *     operation documentation for the appropriate value for this field.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Policy getIamPolicy(String resource) {
    GetIamPolicyRequest request = GetIamPolicyRequest.newBuilder().setResource(resource).build();
    return getIamPolicy(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets the access control policy for an instance resource. Returns an empty policy if an instance
   * exists but does not have a policy set.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Policy getIamPolicy(GetIamPolicyRequest request) {
    return getIamPolicyCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Gets the access control policy for an instance resource. Returns an empty policy if an instance
   * exists but does not have a policy set.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<GetIamPolicyRequest, Policy> getIamPolicyCallable() {
    return stub.getIamPolicyCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Sets the access control policy on an instance resource. Replaces any existing policy.
   *
   * @param resource REQUIRED: The resource for which the policy is being specified. See the
   *     operation documentation for the appropriate value for this field.
   * @param policy REQUIRED: The complete policy to be applied to the `resource`. The size of the
   *     policy is limited to a few 10s of KB. An empty policy is a valid policy but certain Cloud
   *     Platform services (such as Projects) might reject them.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Policy setIamPolicy(ResourceName resource, Policy policy) {
    SetIamPolicyRequest request =
        SetIamPolicyRequest.newBuilder()
            .setResource(resource == null ? null : resource.toString())
            .setPolicy(policy)
            .build();
    return setIamPolicy(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Sets the access control policy on an instance resource. Replaces any existing policy.
   *
   * @param resource REQUIRED: The resource for which the policy is being specified. See the
   *     operation documentation for the appropriate value for this field.
   * @param policy REQUIRED: The complete policy to be applied to the `resource`. The size of the
   *     policy is limited to a few 10s of KB. An empty policy is a valid policy but certain Cloud
   *     Platform services (such as Projects) might reject them.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Policy setIamPolicy(String resource, Policy policy) {
    SetIamPolicyRequest request =
        SetIamPolicyRequest.newBuilder().setResource(resource).setPolicy(policy).build();
    return setIamPolicy(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Sets the access control policy on an instance resource. Replaces any existing policy.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final Policy setIamPolicy(SetIamPolicyRequest request) {
    return setIamPolicyCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Sets the access control policy on an instance resource. Replaces any existing policy.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<SetIamPolicyRequest, Policy> setIamPolicyCallable() {
    return stub.setIamPolicyCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Returns permissions that the caller has on the specified instance resource.
   *
   * @param resource REQUIRED: The resource for which the policy detail is being requested. See the
   *     operation documentation for the appropriate value for this field.
   * @param permissions The set of permissions to check for the `resource`. Permissions with
   *     wildcards (such as '&#42;' or 'storage.&#42;') are not allowed. For more information see
   *     [IAM Overview](https://cloud.google.com/iam/docs/overview#permissions).
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final TestIamPermissionsResponse testIamPermissions(
      ResourceName resource, List<String> permissions) {
    TestIamPermissionsRequest request =
        TestIamPermissionsRequest.newBuilder()
            .setResource(resource == null ? null : resource.toString())
            .addAllPermissions(permissions)
            .build();
    return testIamPermissions(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Returns permissions that the caller has on the specified instance resource.
   *
   * @param resource REQUIRED: The resource for which the policy detail is being requested. See the
   *     operation documentation for the appropriate value for this field.
   * @param permissions The set of permissions to check for the `resource`. Permissions with
   *     wildcards (such as '&#42;' or 'storage.&#42;') are not allowed. For more information see
   *     [IAM Overview](https://cloud.google.com/iam/docs/overview#permissions).
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final TestIamPermissionsResponse testIamPermissions(
      String resource, List<String> permissions) {
    TestIamPermissionsRequest request =
        TestIamPermissionsRequest.newBuilder()
            .setResource(resource)
            .addAllPermissions(permissions)
            .build();
    return testIamPermissions(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Returns permissions that the caller has on the specified instance resource.
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final TestIamPermissionsResponse testIamPermissions(TestIamPermissionsRequest request) {
    return testIamPermissionsCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Returns permissions that the caller has on the specified instance resource.
   *
   * <p>Sample code:
   */
  public final UnaryCallable<TestIamPermissionsRequest, TestIamPermissionsResponse>
      testIamPermissionsCallable() {
    return stub.testIamPermissionsCallable();
  }

  @Override
  public final void close() {
    stub.close();
  }

  @Override
  public void shutdown() {
    stub.shutdown();
  }

  @Override
  public boolean isShutdown() {
    return stub.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return stub.isTerminated();
  }

  @Override
  public void shutdownNow() {
    stub.shutdownNow();
  }

  @Override
  public boolean awaitTermination(long duration, TimeUnit unit) throws InterruptedException {
    return stub.awaitTermination(duration, unit);
  }

  public static class ListAppProfilesPagedResponse
      extends AbstractPagedListResponse<
          ListAppProfilesRequest,
          ListAppProfilesResponse,
          AppProfile,
          ListAppProfilesPage,
          ListAppProfilesFixedSizeCollection> {

    public static ApiFuture<ListAppProfilesPagedResponse> createAsync(
        PageContext<ListAppProfilesRequest, ListAppProfilesResponse, AppProfile> context,
        ApiFuture<ListAppProfilesResponse> futureResponse) {
      ApiFuture<ListAppProfilesPage> futurePage =
          ListAppProfilesPage.createEmptyPage().createPageAsync(context, futureResponse);
      return ApiFutures.transform(
          futurePage,
          new ApiFunction<ListAppProfilesPage, ListAppProfilesPagedResponse>() {
            @Override
            public ListAppProfilesPagedResponse apply(ListAppProfilesPage input) {
              return new ListAppProfilesPagedResponse(input);
            }
          },
          MoreExecutors.directExecutor());
    }

    private ListAppProfilesPagedResponse(ListAppProfilesPage page) {
      super(page, ListAppProfilesFixedSizeCollection.createEmptyCollection());
    }
  }

  public static class ListAppProfilesPage
      extends AbstractPage<
          ListAppProfilesRequest, ListAppProfilesResponse, AppProfile, ListAppProfilesPage> {

    private ListAppProfilesPage(
        PageContext<ListAppProfilesRequest, ListAppProfilesResponse, AppProfile> context,
        ListAppProfilesResponse response) {
      super(context, response);
    }

    private static ListAppProfilesPage createEmptyPage() {
      return new ListAppProfilesPage(null, null);
    }

    @Override
    protected ListAppProfilesPage createPage(
        PageContext<ListAppProfilesRequest, ListAppProfilesResponse, AppProfile> context,
        ListAppProfilesResponse response) {
      return new ListAppProfilesPage(context, response);
    }

    @Override
    public ApiFuture<ListAppProfilesPage> createPageAsync(
        PageContext<ListAppProfilesRequest, ListAppProfilesResponse, AppProfile> context,
        ApiFuture<ListAppProfilesResponse> futureResponse) {
      return super.createPageAsync(context, futureResponse);
    }
  }

  public static class ListAppProfilesFixedSizeCollection
      extends AbstractFixedSizeCollection<
          ListAppProfilesRequest,
          ListAppProfilesResponse,
          AppProfile,
          ListAppProfilesPage,
          ListAppProfilesFixedSizeCollection> {

    private ListAppProfilesFixedSizeCollection(
        List<ListAppProfilesPage> pages, int collectionSize) {
      super(pages, collectionSize);
    }

    private static ListAppProfilesFixedSizeCollection createEmptyCollection() {
      return new ListAppProfilesFixedSizeCollection(null, 0);
    }

    @Override
    protected ListAppProfilesFixedSizeCollection createCollection(
        List<ListAppProfilesPage> pages, int collectionSize) {
      return new ListAppProfilesFixedSizeCollection(pages, collectionSize);
    }
  }
}
