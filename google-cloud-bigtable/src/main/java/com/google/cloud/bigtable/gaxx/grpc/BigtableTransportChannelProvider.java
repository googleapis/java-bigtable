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

    import com.google.api.core.InternalExtensionOnly;
    import com.google.api.gax.grpc.ChannelFactory;
    import com.google.api.gax.grpc.ChannelPoolSettings;
    import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
    import com.google.auth.Credentials;
    import com.google.common.base.Preconditions;
    import io.grpc.Channel;
    import io.grpc.ManagedChannel;
    import java.io.IOException;
    import java.util.Map;
    import java.util.concurrent.Executor;
    import java.util.concurrent.ScheduledExecutorService;
    import com.google.api.gax.rpc.TransportChannel;
    import com.google.api.gax.rpc.TransportChannelProvider;
    import java.util.function.Supplier;


/** An instance of TransportChannelProvider that always provides the same TransportChannel. */
@InternalExtensionOnly
public class BigtableTransportChannelProvider implements TransportChannelProvider {

  private final InstantiatingGrpcChannelProvider ogGrpcChannelProvider;

  private BigtableTransportChannelProvider(
      InstantiatingGrpcChannelProvider instantiatingGrpcChannelProvider) {
    this.ogGrpcChannelProvider = Preconditions.checkNotNull(instantiatingGrpcChannelProvider);
  }

  @Override
  public boolean shouldAutoClose() {
    return ogGrpcChannelProvider.shouldAutoClose();
  }

  @Override
  public boolean needsExecutor() {
    return ogGrpcChannelProvider.needsExecutor();
  }

  @Override
  public BigtableTransportChannelProvider withExecutor(ScheduledExecutorService executor) {
   return withExecutor((Executor) executor);
  }

  @Override
  public BigtableTransportChannelProvider withExecutor(Executor executor) {
    InstantiatingGrpcChannelProvider newChannelProvider = (InstantiatingGrpcChannelProvider) ogGrpcChannelProvider.withExecutor(executor);
    return new BigtableTransportChannelProvider(newChannelProvider);
  }

  @Override
  public boolean needsHeaders() {
    return this.ogGrpcChannelProvider.needsHeaders();
  }

  @Override
  public BigtableTransportChannelProvider withHeaders(Map<String, String> headers) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider)
            ogGrpcChannelProvider.withHeaders(headers);
    return new BigtableTransportChannelProvider(newChannelProvider);
  }

  @Override
  public boolean needsEndpoint() {
    return false;
  }

  @Override
  public TransportChannelProvider withEndpoint(String endpoint) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider) ogGrpcChannelProvider.withEndpoint(endpoint);
    return new BigtableTransportChannelProvider(newChannelProvider);
  }

  /**
   * @deprecated FixedTransportChannelProvider doesn't support ChannelPool configuration
   */
  @Deprecated
  @Override
  public boolean acceptsPoolSize() {
    return ogGrpcChannelProvider.acceptsPoolSize();
  }

  /**
   * @deprecated FixedTransportChannelProvider doesn't support ChannelPool configuration
   */
  @Deprecated
  @Override
  public TransportChannelProvider withPoolSize(int size) {
    InstantiatingGrpcChannelProvider newChannelProvider = (InstantiatingGrpcChannelProvider) ogGrpcChannelProvider.withPoolSize(size);
    return new BigtableTransportChannelProvider(newChannelProvider);
  }

  @Override
  public TransportChannel getTransportChannel() throws IOException {
    ChannelPoolSettings ogPoolSettings = ogGrpcChannelProvider.getChannelPoolSettings();
    InstantiatingGrpcChannelProvider singleChannelProvider = ogGrpcChannelProvider.toBuilder().setChannelPoolSettings(ChannelPoolSettings.staticallySized(1)).build();

    BigtableChannelPoolSettings btPoolSettings = BigtableChannelPoolSettings.builder()
        .setInitialChannelCount(ogPoolSettings.getInitialChannelCount())
        .setMinChannelCount(ogPoolSettings.getMinChannelCount())
        .setMaxChannelCount(ogPoolSettings.getMaxChannelCount())
        .setMinRpcsPerChannel(ogPoolSettings.getMinRpcsPerChannel())
        .setMaxRpcsPerChannel(ogPoolSettings.getMaxRpcsPerChannel())
        .setPreemptiveRefreshEnabled(ogPoolSettings.isPreemptiveRefreshEnabled())
        .build();
    Supplier<ManagedChannel> channelSupplier = ()-> {
      try {
        Channel channel = (Channel) singleChannelProvider.getTransportChannel();
        return (ManagedChannel) channel;
      } catch (IllegalStateException | IOException e) {
        throw new IllegalStateException(e);
      }
    };

    ChannelFactory channelFactory = channelSupplier::get;

    BigtableChannelPool btChannelPool = BigtableChannelPool.create(btPoolSettings,channelFactory);

    return (TransportChannel) btChannelPool;
  }

  @Override
  public String getTransportName() {
    return transportChannel.getTransportName();
  }

  @Override
  public boolean needsCredentials() {
    return ogGrpcChannelProvider.needsCredentials();
  }

  @Override
  public TransportChannelProvider withCredentials(Credentials credentials) {
    InstantiatingGrpcChannelProvider newChannelProvider = (InstantiatingGrpcChannelProvider) ogGrpcChannelProvider.withCredentials(credentials);
    return new BigtableTransportChannelProvider(newChannelProvider);
  }

  /** Creates a FixedTransportChannelProvider. */
  public static BigtableTransportChannelProvider create(InstantiatingGrpcChannelProvider instantiatingGrpcChannelProvider) {
    return new BigtableTransportChannelProvider(instantiatingGrpcChannelProvider);
  }
}