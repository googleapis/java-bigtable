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

import static io.grpc.Status.Code.DEADLINE_EXCEEDED;
import static io.grpc.Status.Code.UNAUTHENTICATED;
import static io.grpc.Status.Code.UNAVAILABLE;
import static io.grpc.Status.Code.UNIMPLEMENTED;
import static io.grpc.Status.Code.UNKNOWN;

import com.google.api.core.InternalApi;
import com.google.api.gax.grpc.ChannelFactory;
import com.google.api.gax.grpc.ChannelPoolSettings;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.rpc.TransportChannel;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.auth.Credentials;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.cloud.bigtable.data.v2.stub.metrics.ChannelPoolMetricsTracer;
import com.google.cloud.bigtable.gaxx.grpc.fallback.GcpFallbackChannel;
import com.google.cloud.bigtable.gaxx.grpc.fallback.GcpFallbackChannelOptions;
import com.google.common.base.Preconditions;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import javax.annotation.Nullable;

/**
 * An instance of TransportChannelProvider that provides a TransportChannel through a supplied
 * InstantiatingGrpcChannelProvider.
 */
@InternalApi
public final class BigtableTransportChannelProvider implements TransportChannelProvider {

  private final InstantiatingGrpcChannelProvider delegate;
  private final ChannelPrimer channelPrimer;
  @Nullable private final ChannelPoolMetricsTracer channelPoolMetricsTracer;
  @Nullable private final EnhancedBigtableStubSettings settings;

  private BigtableTransportChannelProvider(
      InstantiatingGrpcChannelProvider instantiatingGrpcChannelProvider,
      ChannelPrimer channelPrimer,
      ChannelPoolMetricsTracer channelPoolMetricsTracer,
      EnhancedBigtableStubSettings settings) {
    delegate = Preconditions.checkNotNull(instantiatingGrpcChannelProvider);
    this.channelPrimer = channelPrimer;
    this.channelPoolMetricsTracer = channelPoolMetricsTracer;
    this.settings = settings;
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

  @Override
  public BigtableTransportChannelProvider withExecutor(Executor executor) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider) delegate.withExecutor(executor);
    return new BigtableTransportChannelProvider(
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, settings);
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
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, settings);
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
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, settings);
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
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, settings);
  }

  /** Expected to only be called once when BigtableClientContext is created */
  @Override
  public TransportChannel getTransportChannel() throws IOException {
    // This provider's main purpose is to replace the default GAX ChannelPool
    // with a custom BigtableChannelPool, reusing the delegate's configuration.

    // To create our pool, we need a factory for raw gRPC channels.
    // We achieve this by configuring our delegate to not use its own pooling
    // (by setting pool size to 1) and then calling getTransportChannel() on it.
    InstantiatingGrpcChannelProvider singleChannelProvider =
        delegate.toBuilder().setChannelPoolSettings(ChannelPoolSettings.staticallySized(1)).build();

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
        BigtableChannelPool.create(btPoolSettings, channelFactory, channelPrimer);

    ManagedChannel resultingChannel = btChannelPool;

    // TODO: Also check if directpath is possible.
    if (settings != null && settings.isFallbackEnabled() && settings.isDirectpathEnabled()) {
      InstantiatingGrpcChannelProvider cloudpathChannelProvider =
          delegate.toBuilder()
              .setAttemptDirectPath(false)
              .setChannelPoolSettings(ChannelPoolSettings.staticallySized(1))
              .build();

      ChannelFactory cloudpathFactory =
          () -> {
            try {
              GrpcTransportChannel channel =
                  (GrpcTransportChannel) cloudpathChannelProvider.getTransportChannel();
              return (ManagedChannel) channel.getChannel();
            } catch (IOException e) {
              throw new java.io.UncheckedIOException(e);
            }
          };

      BigtableChannelPool btCloupathPool =
          BigtableChannelPool.create(btPoolSettings, cloudpathFactory, channelPrimer);

      Function<Channel, String> probingFn =
          (channel) -> {
            try {
              channelPrimer.sendPrimeRequestsAsync((ManagedChannel) channel).get();
            } catch (StatusRuntimeException e) {
              return e.getStatus().getCode().toString();
            } catch (Exception e) {
              return "EXCEPTION";
            }
            return "";
          };

      // Default options for now, but with probing.
      // TODO: enable oTel metrics if needed.
      GcpFallbackChannelOptions fallbackOptions =
          GcpFallbackChannelOptions.newBuilder()
              .setPrimaryChannelName("DIRECTPATH")
              .setFallbackChannelName("CLOUDPATH")
              .setEnableFallback(true)
              .setPeriod(Duration.ofMinutes(1))
              .setErroneousStates(
                  new HashSet<>(
                    Arrays.asList(UNAVAILABLE, UNAUTHENTICATED, DEADLINE_EXCEEDED, UNKNOWN, UNIMPLEMENTED))
                  )
              .setFallbackProbingInterval(Duration.ofMinutes(15))
              .setPrimaryProbingInterval(Duration.ofMinutes(1))
              .setMinFailedCalls(3)
              .setErrorRateThreshold(1f)
              .setFallbackProbingFunction(probingFn)
              .setPrimaryProbingFunction(probingFn)
              .build();

      resultingChannel = new GcpFallbackChannel(fallbackOptions, btChannelPool, btCloupathPool);
    }

    if (channelPoolMetricsTracer != null) {
      // resultingChannel is either BigtableChannelPool or GcpFallbackChannel here and both
      // implement BigtableChannelPoolObserver.
      channelPoolMetricsTracer.registerChannelInsightsProvider(
          ((BigtableChannelPoolObserver) resultingChannel)::getChannelInfos);
      channelPoolMetricsTracer.registerLoadBalancingStrategy(
          btPoolSettings.getLoadBalancingStrategy().name());
    }

    return GrpcTransportChannel.create(resultingChannel);
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
        newChannelProvider, channelPrimer, channelPoolMetricsTracer, settings);
  }

  /** Creates a BigtableTransportChannelProvider. */
  public static BigtableTransportChannelProvider create(
      InstantiatingGrpcChannelProvider instantiatingGrpcChannelProvider,
      ChannelPrimer channelPrimer,
      ChannelPoolMetricsTracer outstandingRpcsMetricTracke) {
    return new BigtableTransportChannelProvider(
        instantiatingGrpcChannelProvider, channelPrimer, outstandingRpcsMetricTracke, null);
  }

  public static BigtableTransportChannelProvider create(
      InstantiatingGrpcChannelProvider instantiatingGrpcChannelProvider,
      ChannelPrimer channelPrimer,
      ChannelPoolMetricsTracer outstandingRpcsMetricTracer,
      EnhancedBigtableStubSettings settings) {
    return new BigtableTransportChannelProvider(
        instantiatingGrpcChannelProvider, channelPrimer, outstandingRpcsMetricTracer, settings);
  }
}
