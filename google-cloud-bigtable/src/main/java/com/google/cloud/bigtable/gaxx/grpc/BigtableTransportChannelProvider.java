/*
 * Copyright 2017 Google LLC
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google LLC nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.google.cloud.bigtable.gaxx.grpc;

    import com.google.api.core.InternalExtensionOnly;
    import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
    import com.google.auth.Credentials;
    import com.google.common.base.Preconditions;
    import java.io.IOException;
    import java.util.Map;
    import java.util.concurrent.Executor;
    import java.util.concurrent.ScheduledExecutorService;
    import com.google.api.gax.rpc.TransportChannel;
    import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
    import com.google.api.gax.rpc.TransportChannelProvider;
    import jdk.internal.net.http.websocket.Transport;


/** An instance of TransportChannelProvider that always provides the same TransportChannel. */
@InternalExtensionOnly
public class BigtableTransportChannelProvider implements TransportChannelProvider {

  private final InstantiatingGrpcChannelProvider ogGrpcChannelProvider;
  private final TransportChannel transportChannel;

  private BigtableTransportChannelProvider(
      InstantiatingGrpcChannelProvider instantiatingGrpcChannelProvider, TransportChannel transportChannel) {
    this.ogGrpcChannelProvider = instantiatingGrpcChannelProvider;
    this.transportChannel = Preconditions.checkNotNull(transportChannel);
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
    return new BigtableTransportChannelProvider(newChannelProvider, this.transportChannel);
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
    return new BigtableTransportChannelProvider(newChannelProvider, this.transportChannel);
  }

  @Override
  public boolean needsEndpoint() {
    return false;
  }

  @Override
  public TransportChannelProvider withEndpoint(String endpoint) {
    InstantiatingGrpcChannelProvider newChannelProvider =
        (InstantiatingGrpcChannelProvider) ogGrpcChannelProvider.withEndpoint(endpoint);
    return new BigtableTransportChannelProvider(newChannelProvider, this.transportChannel);
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
    return new BigtableTransportChannelProvider(newChannelProvider, this.transportChannel);
  }

  @Override
  public TransportChannel getTransportChannel() throws IOException {
    return transportChannel;
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
    return new BigtableTransportChannelProvider(newChannelProvider, this.transportChannel);
  }

  /** Creates a FixedTransportChannelProvider. */
  public static BigtableTransportChannelProvider create(InstantiatingGrpcChannelProvider instantiatingGrpcChannelProvider, TransportChannel transportChannel) {
    return new BigtableTransportChannelProvider(instantiatingGrpcChannelProvider, TransportChannel transportChannel);
  }
}