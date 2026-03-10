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
package com.google.cloud.bigtable.gaxx.grpc;

import com.google.api.core.InternalApi;
import com.google.api.gax.grpc.ChannelFactory;
import com.google.api.gax.grpc.ChannelPoolSettings;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.rpc.TransportChannel;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.auth.Credentials;
import com.google.cloud.bigtable.data.v2.internal.csm.tracers.ChannelPoolMetricsTracer;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.common.base.Preconditions;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

/**
 * An instance of TransportChannelProvider that provides a TransportChannel through a supplied
 * InstantiatingGrpcChannelProvider.
 */
@InternalApi
public final class BigtableTransportChannelProvider implements TransportChannelProvider {
  private static final Logger LOG =
          Logger.getLogger(BigtableTransportChannelProvider.class.getName());
  private final InstantiatingGrpcChannelProvider delegate;
  private final ChannelPrimer channelPrimer;
  @Nullable private final ChannelPoolMetricsTracer channelPoolMetricsTracer;
  @Nullable private final ScheduledExecutorService backgroundExecutor;
  @Nullable private final Map<String, String> headers;

  private BigtableTransportChannelProvider(
      InstantiatingGrpcChannelProvider instantiatingGrpcChannelProvider,
      ChannelPrimer channelPrimer,
      ChannelPoolMetricsTracer channelPoolMetricsTracer,
      ScheduledExecutorService backgroundExecutor,
      @Nullable Map<String, String> headers) {
    delegate = Preconditions.checkNotNull(instantiatingGrpcChannelProvider);
    this.channelPrimer = channelPrimer;
    this.channelPoolMetricsTracer = channelPoolMetricsTracer;
    this.backgroundExecutor = backgroundExecutor;
    this.headers = headers;
  }

  @Override
  public boolean shouldAutoClose() {
    return delegate.shouldAutoClose();
  }

  @Override
  public boolean needsExecutor() {
    return delegate.needsExecutor();
  }

  @Override
  public BigtableTransportChannelProvider withExecutor(ScheduledExecutorService executor) {
    return withExecutor((Executor) executor);
  }

  // This executor if set is for handling rpc callbacks so we can't use it as the background
  // executor
  @Override
  public BigtableTransportChannelProvider withExecutor(Executor executor) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider) delegate.withExecutor(executor);
    return new BigtableTransportChannelProvider(
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, backgroundExecutor, headers);
  }

  @Override
  public boolean needsBackgroundExecutor() {
    return delegate.needsBackgroundExecutor();
  }

  @Override
  public TransportChannelProvider withBackgroundExecutor(ScheduledExecutorService executor) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider) delegate.withBackgroundExecutor(executor);
    return new BigtableTransportChannelProvider(
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, executor, headers);
  }

  @Override
  public boolean needsHeaders() {
    return delegate.needsHeaders();
  }

  @Override
  public BigtableTransportChannelProvider withHeaders(Map<String, String> headers) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider) delegate.withHeaders(headers);
    return new BigtableTransportChannelProvider(
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, backgroundExecutor, headers);
  }

  @Override
  public boolean needsEndpoint() {
    return delegate.needsEndpoint();
  }

  @Override
  public TransportChannelProvider withEndpoint(String endpoint) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider) delegate.withEndpoint(endpoint);
    return new BigtableTransportChannelProvider(
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, backgroundExecutor, headers);
  }

  @Deprecated
  @Override
  public boolean acceptsPoolSize() {
    return delegate.acceptsPoolSize();
  }

  @Deprecated
  @Override
  public TransportChannelProvider withPoolSize(int size) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider) delegate.withPoolSize(size);
    return new BigtableTransportChannelProvider(
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, backgroundExecutor, headers);
  }

  // We need this for direct access checker.
  private Map<String, String> updateFeatureFlags(
          Map<String, String> originalHeaders, boolean isDirectAccessEligible) {
    if (originalHeaders == null) {
      return java.util.Collections.emptyMap();
    }
    java.util.Map<String, String> newHeaders = new java.util.HashMap<>(originalHeaders);
    String encodedFlags = newHeaders.get("bigtable-features");

    if (encodedFlags != null) {
      try {
        byte[] decoded = java.util.Base64.getUrlDecoder().decode(encodedFlags);
        com.google.bigtable.v2.FeatureFlags flags =
                com.google.bigtable.v2.FeatureFlags.parseFrom(decoded);

        com.google.bigtable.v2.FeatureFlags updatedFlags =
                flags.toBuilder()
                        .setDirectAccessRequested(isDirectAccessEligible)
                        .setTrafficDirectorEnabled(isDirectAccessEligible)
                        .build();

        newHeaders.put(
                "bigtable-features",
                java.util.Base64.getUrlEncoder().encodeToString(updatedFlags.toByteArray()));
      } catch (Exception e) {
        // use original headers
      }
    }
    return newHeaders;
  }

  /** Expected to only be called once when BigtableClientContext is created */
  @Override
  public TransportChannel getTransportChannel() throws IOException {
    Map<String, String> directAccessEligibleHeaders = updateFeatureFlags(this.headers, true);

    InstantiatingGrpcChannelProvider.Builder directAccessProvider =
            EnhancedBigtableStubSettings.applyDirectAccessTraits(delegate.toBuilder())
                    .setChannelPoolSettings(ChannelPoolSettings.staticallySized(1));

    InstantiatingGrpcChannelProvider directAccessProviderWithHeaders =
            (InstantiatingGrpcChannelProvider)
                    directAccessProvider.build().withHeaders(directAccessEligibleHeaders);
    GrpcTransportChannel directAccessTransportChannel =
            (GrpcTransportChannel) directAccessProviderWithHeaders.getTransportChannel();
    Channel maybeDirectAccessChannel = directAccessTransportChannel.getChannel();
    DirectAccessChecker directAccessChecker = UnaryDirectAccessChecker.create(channelPrimer);
    boolean isDirectAccessEligible = false;

    try {
      isDirectAccessEligible = directAccessChecker.check(maybeDirectAccessChannel);
    } catch (Exception e) {
      LOG.log(Level.INFO, "Client is not direct access eligible, using standard transport.", e);
    }

    InstantiatingGrpcChannelProvider selectedProvider;

    if (isDirectAccessEligible) {
      selectedProvider = directAccessProviderWithHeaders;
    } else {
      Map<String, String> fallbackHeaders = updateFeatureFlags(this.headers, false);
      selectedProvider = (InstantiatingGrpcChannelProvider) delegate.withHeaders(fallbackHeaders);
    }


    if (maybeDirectAccessChannel instanceof ManagedChannel) {
      // call shutdown
      ((ManagedChannel) maybeDirectAccessChannel).shutdownNow();
    }

    // This provider's main purpose is to replace the default GAX ChannelPool
    // with a custom BigtableChannelPool, reusing the delegate's configuration.

    // To create our pool, we need a factory for raw gRPC channels.
    // We achieve this by configuring our delegate to not use its own pooling
    // (by setting pool size to 1) and then calling getTransportChannel() on it.
    InstantiatingGrpcChannelProvider singleChannelProvider =
            selectedProvider.toBuilder()
                    .setChannelPoolSettings(ChannelPoolSettings.staticallySized(1))
                    .build();

    ChannelFactory channelFactory =
        () -> {
          try {
            GrpcTransportChannel channel =
                (GrpcTransportChannel) singleChannelProvider.getTransportChannel();
            return (ManagedChannel) channel.getChannel();
          } catch (IOException e) {
            throw new java.io.UncheckedIOException(e);
          }
        };

    BigtableChannelPoolSettings btPoolSettings =
        BigtableChannelPoolSettings.copyFrom(delegate.getChannelPoolSettings());

    BigtableChannelPool btChannelPool =
        BigtableChannelPool.create(
            btPoolSettings, channelFactory, channelPrimer, backgroundExecutor);

    if (channelPoolMetricsTracer != null) {
      channelPoolMetricsTracer.registerChannelInsightsProvider(btChannelPool::getChannelInfos);
      channelPoolMetricsTracer.registerLoadBalancingStrategy(
          btPoolSettings.getLoadBalancingStrategy());
    }

    return GrpcTransportChannel.create(btChannelPool);
  }

  @Override
  public String getTransportName() {
    return "bigtable";
  }

  @Override
  public boolean needsCredentials() {
    return delegate.needsCredentials();
  }

  @Override
  public TransportChannelProvider withCredentials(Credentials credentials) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider) delegate.withCredentials(credentials);
    return new BigtableTransportChannelProvider(
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, backgroundExecutor, headers);
  }

  /** Creates a BigtableTransportChannelProvider. */
  public static BigtableTransportChannelProvider create(
      InstantiatingGrpcChannelProvider instantiatingGrpcChannelProvider,
      ChannelPrimer channelPrimer,
      ChannelPoolMetricsTracer outstandingRpcsMetricTracker,
      ScheduledExecutorService backgroundExecutor) {
    return new BigtableTransportChannelProvider(
        instantiatingGrpcChannelProvider,
        channelPrimer,
        outstandingRpcsMetricTracker,
        backgroundExecutor, null);
  }
}
