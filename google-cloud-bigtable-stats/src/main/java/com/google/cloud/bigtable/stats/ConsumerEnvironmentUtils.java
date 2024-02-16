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
package com.google.cloud.bigtable.stats;

import com.google.api.MonitoredResource;
import com.google.common.annotations.VisibleForTesting;
import io.opencensus.contrib.resource.util.CloudResource;
import io.opencensus.contrib.resource.util.ContainerResource;
import io.opencensus.contrib.resource.util.HostResource;
import io.opencensus.contrib.resource.util.K8sResource;
import io.opencensus.contrib.resource.util.ResourceUtils;
import io.opencensus.resource.Resource;
import java.util.Objects;

/** A class for extracting details about consumer environments (GCE and GKE) for metrics. */
class ConsumerEnvironmentUtils {

  private static ResourceUtilsWrapper resourceUtilsWrapper = new ResourceUtilsWrapper();
  static final String GCE_PROJECT_ID_LABEL = "project_id";
  static final String GCE_INSTANCE_ID_LABEL = "instance_id";
  static final String GCE_ZONE_LABEL = "zone";

  static final String GKE_PROJECT_ID_LABEL = "project_id";
  static final String GKE_LOCATION_LABEL = "location";
  static final String GKE_CLUSTER_NAME_LABEL = "cluster_name";
  static final String GKE_NAMESPACE_NAME_LABEL = "namespace_name";
  static final String GKE_POD_NAME_LABEL = "pod_name";
  static final String GKE_CONTAINER_NAME_LABEL = "container_name";

  @VisibleForTesting
  public static void setResourceUtilsWrapper(ResourceUtilsWrapper newResourceUtilsWrapper) {
    resourceUtilsWrapper = newResourceUtilsWrapper;
  }

  public static boolean isEnvGce() {
    Resource resource = resourceUtilsWrapper.detectResource();
    return Objects.equals(resource.getType(), HostResource.TYPE)
        && Objects.equals(
            resource.getLabels().get(CloudResource.PROVIDER_KEY), CloudResource.PROVIDER_GCP);
  }

  public static boolean isEnvGke() {
    Resource resource = resourceUtilsWrapper.detectResource();
    return Objects.equals(resource.getType(), ContainerResource.TYPE)
        && Objects.equals(
            resource.getLabels().get(CloudResource.PROVIDER_KEY), CloudResource.PROVIDER_GCP);
  }

  public static void putGceResourceLabels(MonitoredResource.Builder builder) {
    Resource resource = resourceUtilsWrapper.detectResource();
    if (isEnvGce()) {
      builder.putLabels(
          GCE_PROJECT_ID_LABEL, resource.getLabels().get(CloudResource.ACCOUNT_ID_KEY));
      builder.putLabels(GCE_INSTANCE_ID_LABEL, resource.getLabels().get(HostResource.ID_KEY));
      builder.putLabels(GCE_ZONE_LABEL, resource.getLabels().get(CloudResource.ZONE_KEY));
    } else {
      throw new IllegalStateException("Can only put GCE labels inside a GCE environment.");
    }
  }

  public static void putGkeResourceLabels(MonitoredResource.Builder builder) {
    Resource resource = resourceUtilsWrapper.detectResource();
    if (isEnvGke()) {
      builder.putLabels(
          GKE_PROJECT_ID_LABEL, resource.getLabels().get(CloudResource.ACCOUNT_ID_KEY));
      builder.putLabels(GKE_LOCATION_LABEL, resource.getLabels().get(CloudResource.ZONE_KEY));
      builder.putLabels(
          GKE_CLUSTER_NAME_LABEL, resource.getLabels().get(K8sResource.CLUSTER_NAME_KEY));
      builder.putLabels(
          GKE_NAMESPACE_NAME_LABEL, resource.getLabels().get(K8sResource.NAMESPACE_NAME_KEY));
      builder.putLabels(GKE_POD_NAME_LABEL, resource.getLabels().get(K8sResource.POD_NAME_KEY));
      builder.putLabels(
          GKE_CONTAINER_NAME_LABEL, resource.getLabels().get(ContainerResource.NAME_KEY));
    } else {
      throw new IllegalStateException("Can only put GKE labels inside a GKE environment.");
    }
  }

  // We wrap the static ResourceUtils.detectResource() method in a non-static method for mocking.
  @VisibleForTesting
  public static class ResourceUtilsWrapper {
    public Resource detectResource() {
      return ResourceUtils.detectResource();
    }
  }
}
