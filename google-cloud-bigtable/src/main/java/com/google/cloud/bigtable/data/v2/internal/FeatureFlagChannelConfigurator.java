/*
 * Copyright 2022 Google LLC
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
package com.google.cloud.bigtable.data.v2.internal;

import com.google.api.core.ApiFunction;
import com.google.api.core.InternalApi;
import com.google.bigtable.v2.FeatureFlags;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.annotation.Nullable;

/** Notifies what features this client supports with each request. */
@InternalApi
public class FeatureFlagChannelConfigurator
    implements ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> {
  @Nullable private final ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> prevProvider;
  private final FeatureFlagInterceptor flagInterceptor;

  @SuppressWarnings("rawtypes")
  public FeatureFlagChannelConfigurator(
      @Nullable ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder> prevProvider,
      FeatureFlags flags) {
    this.prevProvider = prevProvider;
    this.flagInterceptor = new FeatureFlagInterceptor(flags);
  }

  @Override
  public ManagedChannelBuilder<?> apply(ManagedChannelBuilder managedChannelBuilder) {
    if (prevProvider != null) {
      managedChannelBuilder = prevProvider.apply(managedChannelBuilder);
    }
    return managedChannelBuilder.intercept(flagInterceptor);
  }

  private static class FeatureFlagInterceptor implements ClientInterceptor {
    static final Metadata.Key<byte[]> FEATURE_FLAG_KEY =
        Metadata.Key.of("bigtable-features-bin", Metadata.BINARY_BYTE_MARSHALLER);
    private final byte[] serializedFlags;

    public FeatureFlagInterceptor(FeatureFlags flags) {
      ByteArrayOutputStream boas = new ByteArrayOutputStream();
      try {
        flags.writeTo(boas);
      } catch (IOException e) {
        throw new IllegalStateException("Unexpected IOException while serializing feature flags");
      }
      serializedFlags = boas.toByteArray();
    }

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
        MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
      ClientCall<ReqT, RespT> baseCall = channel.newCall(methodDescriptor, callOptions);

      return new SimpleForwardingClientCall<ReqT, RespT>(baseCall) {
        @Override
        public void start(Listener<RespT> responseListener, Metadata headers) {
          headers.put(FEATURE_FLAG_KEY, Base64.getEncoder().encode(serializedFlags));
          super.start(responseListener, headers);
        }
      };
    }
  }
}