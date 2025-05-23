/*
 * Copyright 2018 Google LLC
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
package com.google.cloud.bigtable.admin.v2;

import static com.google.cloud.bigtable.admin.v2.BigtableTableAdminSettings.BIGTABLE_EMULATOR_HOST_ENV_VAR;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.ChannelPoolSettings;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.cloud.bigtable.admin.v2.stub.BigtableInstanceAdminStubSettings;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.base.Verify;
import io.grpc.ManagedChannelBuilder;
import java.io.IOException;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Settings class to configure an instance of {@link BigtableInstanceAdminClient}.
 *
 * <p>It must be configured with a project ID and can be used to change default RPC settings.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * BigtableInstanceAdminSettings.Builder settingsBuilder = BigtableInstanceAdminSettings.newBuilder()
 *  .setProjectId("my-project");
 *
 * settingsBuilder.stubSettings().createInstanceSettings()
 *   .setRetrySettings(
 *     RetrySettings.newBuilder()
 *       .setTotalTimeout(Duration.ofMinutes(15))
 *       .build());
 *
 * BigtableInstanceAdminSettings settings = settingsBuilder.build();
 * }</pre>
 */
public final class BigtableInstanceAdminSettings {
  private static final Logger LOGGER =
      Logger.getLogger(BigtableInstanceAdminSettings.class.getName());

  private final String projectId;
  private final BigtableInstanceAdminStubSettings stubSettings;

  private BigtableInstanceAdminSettings(Builder builder) throws IOException {
    Preconditions.checkNotNull(builder.projectId, "Project ID must be set");
    Verify.verifyNotNull(builder.stubSettings, "stubSettings should never be null");

    this.projectId = builder.projectId;
    this.stubSettings = builder.stubSettings.build();
  }

  /** Gets the ID of the project whose instances the client will manage. */
  @Nonnull
  public String getProjectId() {
    return projectId;
  }

  /** Gets the credentials provider to use for getting the credentials to make calls with. */
  public CredentialsProvider getCredentialsProvider() {
    return stubSettings.getCredentialsProvider();
  }

  /** Gets the underlying RPC settings. */
  @Nonnull
  public BigtableInstanceAdminStubSettings getStubSettings() {
    return stubSettings;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("projectId", projectId)
        .add("createInstanceSettings", stubSettings.createInstanceSettings())
        .add("createInstanceOperationSettings", stubSettings.createInstanceOperationSettings())
        .add("getInstanceSettings", stubSettings.getInstanceSettings())
        .add("listInstancesSettings", stubSettings.listInstancesSettings())
        .add("partialUpdateInstanceSettings", stubSettings.partialUpdateInstanceSettings())
        .add(
            "partialUpdateInstanceOperationSettings",
            stubSettings.partialUpdateInstanceOperationSettings())
        .add("deleteInstanceSettings", stubSettings.deleteInstanceSettings())
        .add("createClusterSettings", stubSettings.createClusterSettings())
        .add("createClusterOperationSettings", stubSettings.createClusterOperationSettings())
        .add("getClusterSettings", stubSettings.getClusterSettings())
        .add("listClustersSettings", stubSettings.listClustersSettings())
        .add("updateClusterSettings", stubSettings.updateClusterSettings())
        .add("updateClusterOperationSettings", stubSettings.updateClusterOperationSettings())
        .add("deleteClusterSettings", stubSettings.deleteClusterSettings())
        .add("createAppProfileSettings", stubSettings.createAppProfileSettings())
        .add("getAppProfileSettings", stubSettings.getAppProfileSettings())
        .add("listAppProfilesSettings", stubSettings.listAppProfilesSettings())
        .add("updateAppProfileSettings", stubSettings.updateAppProfileSettings())
        .add("updateAppProfileOperationSettings", stubSettings.updateAppProfileOperationSettings())
        .add("deleteAppProfileSettings", stubSettings.deleteAppProfileSettings())
        .add("getIamPolicySettings", stubSettings.getIamPolicySettings())
        .add("setIamPolicySettings", stubSettings.setIamPolicySettings())
        .add("testIamPermissionsSettings", stubSettings.testIamPermissionsSettings())
        .add("createMaterializedViewSettings", stubSettings.createMaterializedViewSettings())
        .add("getMaterializedViewSettings", stubSettings.getMaterializedViewSettings())
        .add("listMaterializedViewsSettings", stubSettings.listMaterializedViewsSettings())
        .add("updateMaterializedViewSettings", stubSettings.updateMaterializedViewSettings())
        .add("deleteMaterializedViewSettings", stubSettings.deleteMaterializedViewSettings())
        .add("createLogicalViewSettings", stubSettings.createLogicalViewSettings())
        .add("getLogicalViewSettings", stubSettings.getLogicalViewSettings())
        .add("listLogicalViewsSettings", stubSettings.listLogicalViewsSettings())
        .add("updateLogicalViewSettings", stubSettings.updateLogicalViewSettings())
        .add("deleteLogicalViewSettings", stubSettings.deleteLogicalViewSettings())
        .add("stubSettings", stubSettings)
        .toString();
  }

  /** Returns a builder containing all the values of this settings class. */
  public Builder toBuilder() {
    return new Builder(this);
  }

  /** Returns a new builder for this class. */
  public static Builder newBuilder() {
    String hostAndPort = System.getenv(BIGTABLE_EMULATOR_HOST_ENV_VAR);
    if (!Strings.isNullOrEmpty(hostAndPort)) {
      int port;
      try {
        port = Integer.parseInt(hostAndPort.substring(hostAndPort.lastIndexOf(":") + 1));
        return newBuilderForEmulator(hostAndPort.substring(0, hostAndPort.lastIndexOf(":")), port);
      } catch (NumberFormatException | IndexOutOfBoundsException ex) {
        throw new RuntimeException(
            "Invalid host/port in "
                + BIGTABLE_EMULATOR_HOST_ENV_VAR
                + " environment variable: "
                + hostAndPort);
      }
    }
    return new Builder();
  }

  /** Create a new builder preconfigured to connect to the Bigtable emulator with port number. */
  public static Builder newBuilderForEmulator(int port) {
    return newBuilderForEmulator("localhost", port);
  }

  /**
   * Creates a new builder preconfigured to connect to the Bigtable emulator with host name and port
   * number.
   */
  public static Builder newBuilderForEmulator(String hostname, int port) {
    Builder builder = new Builder();

    builder
        .stubSettings()
        .setCredentialsProvider(NoCredentialsProvider.create())
        .setEndpoint(hostname + ":" + port)
        .setTransportChannelProvider(
            InstantiatingGrpcChannelProvider.newBuilder()
                .setChannelPoolSettings(ChannelPoolSettings.staticallySized(1))
                .setChannelConfigurator(ManagedChannelBuilder::usePlaintext)
                .build());

    LOGGER.info("Connecting to the Bigtable emulator at " + hostname + ":" + port);
    return builder;
  }

  /** Builder for BigtableInstanceAdminSettings. */
  public static final class Builder {
    @Nullable private String projectId;
    private final BigtableInstanceAdminStubSettings.Builder stubSettings;

    private Builder() {
      stubSettings = BigtableInstanceAdminStubSettings.newBuilder();
    }

    private Builder(BigtableInstanceAdminSettings settings) {
      this.projectId = settings.projectId;
      this.stubSettings = settings.stubSettings.toBuilder();
    }

    /** Sets the ID of the project whose instances the client will manage. */
    public Builder setProjectId(@Nonnull String projectId) {
      Preconditions.checkNotNull(projectId);
      this.projectId = projectId;
      return this;
    }

    /** Gets the ID of the project whose instances the client will manage. */
    @Nullable
    public String getProjectId() {
      return projectId;
    }

    /** Sets the CredentialsProvider to use for getting the credentials to make calls with. */
    public Builder setCredentialsProvider(CredentialsProvider credentialsProvider) {
      stubSettings.setCredentialsProvider(credentialsProvider);

      return this;
    }

    /** Gets the CredentialsProvider to use for getting the credentials to make calls with. */
    public CredentialsProvider getCredentialsProvider() {
      return stubSettings.getCredentialsProvider();
    }

    /**
     * Returns the builder for the settings used for all RPCs.
     *
     * <p>This is meant for advanced usage. The default RPC settings are set to their recommended
     * values.
     */
    public BigtableInstanceAdminStubSettings.Builder stubSettings() {
      return stubSettings;
    }

    /** Builds an instance of the settings. */
    public BigtableInstanceAdminSettings build() throws IOException {
      return new BigtableInstanceAdminSettings(this);
    }
  }
}
