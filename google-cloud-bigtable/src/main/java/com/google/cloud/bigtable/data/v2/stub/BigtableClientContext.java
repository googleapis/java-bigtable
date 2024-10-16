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
package com.google.cloud.bigtable.data.v2.stub;

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.APP_PROFILE_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.BIGTABLE_PROJECT_ID_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLIENT_NAME_KEY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.INSTANCE_ID_KEY;

import com.google.api.core.ApiFunction;
import com.google.api.core.InternalApi;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.rpc.ClientContext;
import com.google.auth.Credentials;
import com.google.cloud.bigtable.Version;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.stub.metrics.CustomOpenTelemetryMetricsProvider;
import com.google.cloud.bigtable.data.v2.stub.metrics.DefaultMetricsProvider;
import com.google.cloud.bigtable.data.v2.stub.metrics.ErrorCountPerConnectionMetricTracker;
import com.google.cloud.bigtable.data.v2.stub.metrics.MetricsProvider;
import com.google.cloud.bigtable.data.v2.stub.metrics.NoopMetricsProvider;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

/**
 * This class wraps a gax {@link ClientContext} and an {@link OpenTelemetry} instance that needs to
 * be created before creating the client context.
 */
@InternalApi
public class BigtableClientContext {

  private static final Logger logger = Logger.getLogger(BigtableClientContext.class.getName());

  private OpenTelemetry openTelemetry = null;
  private final ClientContext clientContext;

  public static BigtableClientContext create(EnhancedBigtableStubSettings settings)
      throws IOException {
    return new BigtableClientContext(settings);
  }

  private BigtableClientContext(EnhancedBigtableStubSettings settings) throws IOException {
    Credentials credentials = settings.getCredentialsProvider().getCredentials();

    EnhancedBigtableStubSettings.Builder builder = settings.toBuilder();

    InstantiatingGrpcChannelProvider.Builder transportProvider =
        builder.getTransportChannelProvider() instanceof InstantiatingGrpcChannelProvider
            ? ((InstantiatingGrpcChannelProvider) builder.getTransportChannelProvider()).toBuilder()
            : null;

    try {
      // We don't want client side metrics to crash the client, so catch any exception when getting
      // the OTEL instance and log the exception instead.
      this.openTelemetry =
          createOpenTelemetry(
              settings.getProjectId(),
              settings.getMetricsProvider(),
              credentials,
              settings.getMetricsEndpoint());
    } catch (Throwable t) {
      logger.log(Level.WARNING, "Failed to get OTEL, will skip exporting client side metrics", t);
    }
    ErrorCountPerConnectionMetricTracker errorCountPerConnectionMetricTracker;
    // Skip setting up ErrorCountPerConnectionMetricTracker if openTelemetry is null
    if (openTelemetry != null && transportProvider != null) {
      errorCountPerConnectionMetricTracker =
          new ErrorCountPerConnectionMetricTracker(
              openTelemetry, EnhancedBigtableStub.createBuiltinAttributes(settings));
      ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> oldChannelConfigurator =
          transportProvider.getChannelConfigurator();
      transportProvider.setChannelConfigurator(
          managedChannelBuilder -> {
            if (settings.getEnableRoutingCookie()) {
              managedChannelBuilder.intercept(new CookiesInterceptor());
            }

            managedChannelBuilder.intercept(errorCountPerConnectionMetricTracker.getInterceptor());

            if (oldChannelConfigurator != null) {
              managedChannelBuilder = oldChannelConfigurator.apply(managedChannelBuilder);
            }
            return managedChannelBuilder;
          });
    } else {
      errorCountPerConnectionMetricTracker = null;
    }

    // Inject channel priming
    if (settings.isRefreshingChannel()) {

      if (transportProvider != null) {
        transportProvider.setChannelPrimer(
            BigtableChannelPrimer.create(
                credentials,
                settings.getProjectId(),
                settings.getInstanceId(),
                settings.getAppProfileId()));
      }
    }

    if (transportProvider != null) {
      builder.setTransportChannelProvider(transportProvider.build());
    }

    clientContext = ClientContext.create(builder.build());

    if (errorCountPerConnectionMetricTracker != null) {
      errorCountPerConnectionMetricTracker.startConnectionErrorCountTracker(
          clientContext.getExecutor());
    }
  }

  public OpenTelemetry createOpenTelemetry() {
    return this.openTelemetry;
  }

  public ClientContext getClientContext() {
    return this.clientContext;
  }

  private static Attributes createBuiltinAttributes(EnhancedBigtableStubSettings settings) {
    return Attributes.of(
        BIGTABLE_PROJECT_ID_KEY,
        settings.getProjectId(),
        INSTANCE_ID_KEY,
        settings.getInstanceId(),
        APP_PROFILE_KEY,
        settings.getAppProfileId(),
        CLIENT_NAME_KEY,
        "bigtable-java/" + Version.VERSION);
  }

  private static OpenTelemetry createOpenTelemetry(
      String projectId,
      MetricsProvider metricsProvider,
      @Nullable Credentials defaultCredentials,
      @Nullable String metricsEndpoint)
      throws IOException {
    if (metricsProvider instanceof CustomOpenTelemetryMetricsProvider) {
      CustomOpenTelemetryMetricsProvider customMetricsProvider =
          (CustomOpenTelemetryMetricsProvider) metricsProvider;
      return customMetricsProvider.getOpenTelemetry();
    } else if (metricsProvider instanceof DefaultMetricsProvider) {
      Credentials credentials =
          BigtableDataSettings.getMetricsCredentials() != null
              ? BigtableDataSettings.getMetricsCredentials()
              : defaultCredentials;
      DefaultMetricsProvider defaultMetricsProvider = (DefaultMetricsProvider) metricsProvider;
      return defaultMetricsProvider.getOpenTelemetry(projectId, metricsEndpoint, credentials);
    } else if (metricsProvider instanceof NoopMetricsProvider) {
      return null;
    }
    throw new IOException("Invalid MetricsProvider type " + metricsProvider);
  }
}
