/*
 * Copyright 2026 Google LLC
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
import com.google.cloud.bigtable.data.v2.stub.BigtableChannelPrimer;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ClientInterceptors;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.alts.AltsContextUtil;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

@InternalApi
public class BigtableDirectAccessChecker implements DirectAccessChecker {
  private static final Logger LOG = Logger.getLogger(BigtableDirectAccessChecker.class.getName());
  private final ChannelPrimer channelPrimer;

  public BigtableDirectAccessChecker(ChannelPrimer channelPrimer) {
    this.channelPrimer = channelPrimer;
  }

  /// Performs a request on the provided channel to check for Direct Access eligibility.
  @Override
  public boolean check(ManagedChannel channel) {
    // Return false in case channelPrime is not an instance, rare
    if (!(channelPrimer instanceof BigtableChannelPrimer)) {
      return false;
    }

    BigtableChannelPrimer primer = (BigtableChannelPrimer) channelPrimer;
    final AtomicBoolean isDirectAccessEligible = new AtomicBoolean(false);

    // Create the interceptor to check the headers
    ClientInterceptor altsInterceptor =
        new ClientInterceptor() {
          @Override
          public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
              MethodDescriptor<ReqT, RespT> methodDescriptor,
              CallOptions callOptions,
              Channel next) {

            // Capture the actual ClientCall to access its attributes later
            final ClientCall<ReqT, RespT> thisCall = next.newCall(methodDescriptor, callOptions);

            return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(thisCall) {
              @Override
              public void start(Listener<RespT> responseListener, Metadata headers) {

                // Wrap the listener to intercept the response headers
                Listener<RespT> forwardingListener =
                    new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(
                        responseListener) {
                      @Override
                      public void onHeaders(Metadata responseHeaders) {
                        boolean altsCheckPassed = false;
                        try {
                          // Verify ALTS context is present
                          if (AltsContextUtil.check(thisCall.getAttributes())) {
                            altsCheckPassed = true;
                          }
                        } catch (Exception e) {
                          LOG.warning("direct access check failed: " + e.getMessage());
                        }
                        if (altsCheckPassed) {
                          isDirectAccessEligible.set(true);
                        }

                        super.onHeaders(responseHeaders);
                      }
                    };

                super.start(forwardingListener, headers);
              }
            };
          }
        };

    try {
      // Wrap the channel with our custom ALTS interceptor
      Channel interceptedChannel = ClientInterceptors.intercept(channel, altsInterceptor);

      ManagedChannel wrappedManagedChannel =
          new ManagedChannel() {
            @Override
            public <RequestT, ResponseT> ClientCall<RequestT, ResponseT> newCall(
                MethodDescriptor<RequestT, ResponseT> methodDescriptor, CallOptions callOptions) {
              // Delegate RPCs to the intercepted channel!
              return interceptedChannel.newCall(methodDescriptor, callOptions);
            }

            @Override
            public String authority() {
              return channel.authority();
            }

            // Delegate all lifecycle methods to the original ManagedChannel
            @Override
            public ManagedChannel shutdown() {
              return channel.shutdown();
            }

            @Override
            public boolean isShutdown() {
              return channel.isShutdown();
            }

            @Override
            public boolean isTerminated() {
              return channel.isTerminated();
            }

            @Override
            public ManagedChannel shutdownNow() {
              return channel.shutdownNow();
            }

            @Override
            public boolean awaitTermination(long timeout, TimeUnit unit)
                throws InterruptedException {
              return channel.awaitTermination(timeout, unit);
            }
          };

      // Delegate the actual RPC execution to the primer.
      // This synchronously sends the PingAndWarm request and triggers the onHeaders callback.
      primer.primeChannel(wrappedManagedChannel);

    } catch (Exception e) {
      LOG.warning("The direct access probe failed to execute: " + e.getMessage());
    }

    return isDirectAccessEligible.get();
  }
}
