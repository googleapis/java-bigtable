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
package com.google.cloud.bigtable.data.v2.stub;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import java.net.SocketAddress;

public class VerboseIntercptor implements ClientInterceptor {

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
    final ClientCall<ReqT, RespT> clientCall = next.newCall(method, callOptions);

    return new SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        super.start(
            new SimpleForwardingClientCallListener<RespT>(responseListener) {
              @Override
              public void onHeaders(Metadata headers) {
                // Check peer IP after connection is established.
                SocketAddress remoteAddr =
                    clientCall.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
                System.out.println(
                    String.format(
                        "Connected to %s for %s",
                        remoteAddr.toString(), method.getFullMethodName()));
                clientCall
                    .getAttributes()
                    .keys()
                    .forEach(
                        key ->
                            System.out.println(
                                String.format(
                                    "Client attribute key %s : value %s",
                                    key, clientCall.getAttributes().get(key).toString())));
                super.onHeaders(headers);
              }
            },
            headers);
      }
    };
  }
}
