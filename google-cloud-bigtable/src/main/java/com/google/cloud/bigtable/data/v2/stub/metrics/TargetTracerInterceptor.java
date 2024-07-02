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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.tracing.ApiTracer;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import java.net.SocketAddress;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Level;
import java.util.logging.Logger;

/** An interceptor extracts remote target and appends metric. */
public class TargetTracerInterceptor implements ClientInterceptor {
  private static final Logger LOG =
      Logger.getLogger(TargetTracerInterceptor.class.toString());

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    ClientCall<ReqT,RespT> clientCall = channel.newCall(methodDescriptor,callOptions);
    return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        super.start(
            new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(
                responseListener) {
              @Override
              public void onHeaders(Metadata headers) {
                // Connection accounting is non-critical, so we log the exception, but let normal
                // processing proceed.
                try {
                  SocketAddress remoteAddr =
                      clientCall.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
                  ApiTracer bigtableTracer = callOptions.getOption(GrpcCallContext.TRACER_KEY);
                  if(bigtableTracer instanceof BuiltinMetricsTracer) {
                    ((BuiltinMetricsTracer)bigtableTracer).addTarget(String.valueOf(remoteAddr));
                  }
                } catch (Throwable t) {
                  LOG.log(
                      Level.WARNING, "Unexpected error while updating target label", t);
                }
                super.onHeaders(headers);
              }
            },
            headers);
      }
    };
  }

}
