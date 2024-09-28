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
package com.google.cloud.bigtable.data.v2;

import com.google.api.core.BetaApi;
import com.google.api.gax.core.BackgroundResource;
import com.google.api.gax.rpc.ClientContext;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStub;
import io.opentelemetry.api.OpenTelemetry;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

/**
 * A factory to create multiple {@link BigtableDataClient} instances that all share the same channel
 * pool.
 *
 * <p>This allows multiple client instances to share the same gRPC channel pool, which makes client
 * creation very cheap. The intended use case is for applications that need to access multiple
 * Bigtable Instances from the same process.
 *
 * <p>Example Usage:
 *
 * <pre>{@code
 * BigtableDataSettings defaultSettings = BigtableDataSettings.newBuilder()
 *   .setProject("my-default-project")
 *   .setInstance("my-default-instance")
 *   .build();
 *
 * BigtableDataClientFactory clientFactory = BigtableDataClientFactory.create(defaultSettings);
 *
 * // Create a new client for "my-default-instance" in "my-default-project";
 * BigtableDataClient defaultInstanceClient = clientFactory.createDefault();
 *
 * // Create a new client for a different application profile
 * BigtableDataClient otherAppProfileClient = clientFactory.createForAppProfile("other-app-profile");
 *
 * // Create a new client for a completely different instance and application profile.
 * BigtableDataClient otherInstanceClient = clientFactory
 *   .createForInstance("my-other-project", "my-other-instance", "my-other-app-profile");
 *
 * // Clean up: make sure close the clients AND the factory.
 * defaultInstanceClient.close();
 * otherAppProfileClient.close();
 * otherInstanceClient.close();
 *
 * clientFactory.close();
 *
 * <p>Please note that this is an experimental feature and might be changed or removed in future.
 * }</pre>
 */
@BetaApi("This feature is currently experimental and can change in the future")
public final class BigtableDataClientFactory implements AutoCloseable {

  private static final Logger logger = Logger.getLogger(BigtableDataClientFactory.class.getName());

  private final BigtableDataSettings defaultSettings;
  private final ClientContext sharedClientContext;
  private final OpenTelemetry openTelemetry;

  /**
   * Create a instance of this factory.
   *
   * <p>The factory will be used to create clients using the provided settings as the base. Make
   * sure to call {@link #close()} on the factory after closing all clients.
   */
  public static BigtableDataClientFactory create(BigtableDataSettings defaultSettings)
      throws IOException {
    ClientContext sharedClientContext =
        EnhancedBigtableStub.createClientContext(defaultSettings.getStubSettings());
    OpenTelemetry openTelemetry = null;
    try {
      // We don't want client side metrics to crash the client, so catch any exception when getting
      // the OTEL instance and log the exception instead.
      openTelemetry =
          EnhancedBigtableStub.getOpenTelemetry(
              defaultSettings.getProjectId(),
              defaultSettings.getMetricsProvider(),
              sharedClientContext.getCredentials(),
              defaultSettings.getStubSettings().getMetricsEndpoint());
    } catch (Throwable t) {
      logger.log(Level.WARNING, "Failed to get OTEL, will skip exporting client side metrics", t);
    }
    return new BigtableDataClientFactory(sharedClientContext, defaultSettings, openTelemetry);
  }

  private BigtableDataClientFactory(
      ClientContext sharedClientContext,
      BigtableDataSettings defaultSettings,
      OpenTelemetry openTelemetry) {
    this.sharedClientContext = sharedClientContext;
    this.defaultSettings = defaultSettings;
    this.openTelemetry = openTelemetry;
  }

  /**
   * Release all of the resources associated with this factory.
   *
   * <p>This will close the underlying channel pooling, disconnecting all create clients.
   */
  @Override
  public void close() throws Exception {
    for (BackgroundResource resource : sharedClientContext.getBackgroundResources()) {
      resource.close();
    }
  }

  /**
   * Create a lightweight client using the default settings in this factory. This will use the
   * factory default project, instance and application profile ids. The client will also share
   * resources like the channel pool with other clients created using this factory.
   *
   * <p>The client should be closed when it is no longer needed. Closing the client will release
   * client specific resources, but will leave shared resources like the channel pool open. To
   * release all resources, first close all of the created clients and then this factory instance.
   */
  public BigtableDataClient createDefault() {
    try {
      ClientContext clientContext =
          sharedClientContext
              .toBuilder()
              .setTracerFactory(
                  EnhancedBigtableStub.createBigtableTracerFactory(
                      defaultSettings.getStubSettings(), openTelemetry))
              .build();

      return BigtableDataClient.createWithClientContext(defaultSettings, clientContext);
    } catch (IOException e) {
      // Should never happen because the connection has been established already
      throw new RuntimeException(
          "Failed to create a new client using factory default settings and shared resources.");
    }
  }

  /**
   * Create a lightweight client with an overriden application profile and the factory default
   * project and instance ids. The client will also share resources like the channel pool with other
   * clients created using this factory.
   *
   * <p>The client should be closed when it is no longer needed. Closing the client will release
   * client specific resources, but will leave shared resources like the channel pool open. To
   * release all resources, first close all of the created clients and then this factory instance.
   */
  public BigtableDataClient createForAppProfile(@Nonnull String appProfileId) throws IOException {
    BigtableDataSettings settings =
        defaultSettings.toBuilder().setAppProfileId(appProfileId).build();

    ClientContext clientContext =
        sharedClientContext
            .toBuilder()
            .setTracerFactory(
                EnhancedBigtableStub.createBigtableTracerFactory(
                    settings.getStubSettings(), openTelemetry))
            .build();
    return BigtableDataClient.createWithClientContext(settings, clientContext);
  }

  /**
   * Create a lightweight client with the specified project and instance id. The resulting client
   * will use the server default application profile. The client will also share resources like the
   * channel pool with other clients created using this factory.
   *
   * <p>The client should be closed when it is no longer needed. Closing the client will release
   * client specific resources, but will leave shared resources like the channel pool open. To
   * release all resources, first close all of the created clients and then this factory instance.
   */
  public BigtableDataClient createForInstance(@Nonnull String projectId, @Nonnull String instanceId)
      throws IOException {
    BigtableDataSettings settings =
        defaultSettings
            .toBuilder()
            .setProjectId(projectId)
            .setInstanceId(instanceId)
            .setDefaultAppProfileId()
            .build();

    ClientContext clientContext =
        sharedClientContext
            .toBuilder()
            .setTracerFactory(
                EnhancedBigtableStub.createBigtableTracerFactory(
                    settings.getStubSettings(), openTelemetry))
            .build();

    return BigtableDataClient.createWithClientContext(settings, clientContext);
  }

  /**
   * Create a lightweight client to the specified project, instance and application profile id. The
   * client will share resources like the channel pool with other clients created using this
   * factory.
   *
   * <p>The client should be closed when it is no longer needed. Closing the client will release
   * client specific resources, but will leave shared resources like the channel pool open. To
   * release all resources, first close all of the created clients and then this factory instance.
   */
  public BigtableDataClient createForInstance(
      @Nonnull String projectId, @Nonnull String instanceId, @Nonnull String appProfileId)
      throws IOException {
    BigtableDataSettings settings =
        defaultSettings
            .toBuilder()
            .setProjectId(projectId)
            .setInstanceId(instanceId)
            .setAppProfileId(appProfileId)
            .build();
    ClientContext clientContext =
        sharedClientContext
            .toBuilder()
            .setTracerFactory(
                EnhancedBigtableStub.createBigtableTracerFactory(
                    settings.getStubSettings(), openTelemetry))
            .build();
    return BigtableDataClient.createWithClientContext(settings, clientContext);
  }
}
