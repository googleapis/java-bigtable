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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import static java.lang.String.*;

import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.tracing.ApiTracer;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * stuff.
 */
public class TargetEndpointInterceptor implements ClientInterceptor {

  private String target;
  private static final Logger LOG = Logger.getLogger(TargetEndpointInterceptor.class.getName());

  public String getTarget() {
    return target;
  }

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    System.out.println("INTECERCEPT!!!");
    ApiTracer tracer = callOptions.getOption(GrpcCallContext.TRACER_KEY);
    if (tracer == null) {
      System.out.println("grpc tracer is null outside of header");
    }
    ((BuiltinMetricsTracer) tracer).addTarget(target);

    final ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT> simpleForwardingClientCall =
        (SimpleForwardingClientCall<ReqT, RespT>) channel.newCall(methodDescriptor, callOptions);
    return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
        simpleForwardingClientCall) {

      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {

        super.start(
            new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(
                responseListener) {


              @Override
              public void onHeaders(Metadata headers) {
                SocketAddress remoteAddr =
                    simpleForwardingClientCall.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
                target = ((InetSocketAddress) remoteAddr).getAddress().toString();

                if (tracer == null) {
                  System.out.println("gax tracer is null");
                }
                if (tracer != null && target != null) {
                  ((BuiltinMetricsTracer) tracer).addTarget(target);
                }

                super.onHeaders(headers);
              }
            },
            headers);
      }
    };
  }
}
