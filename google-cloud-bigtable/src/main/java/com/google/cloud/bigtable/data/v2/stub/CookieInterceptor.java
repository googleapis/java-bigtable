/*
 * Copyright 2023 Google LLC
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

import static com.google.cloud.bigtable.data.v2.stub.CookiesHolder.COOKIES_HOLDER_KEY;
import static com.google.cloud.bigtable.data.v2.stub.CookiesHolder.ROUTING_COOKIE_KEY;
import static com.google.cloud.bigtable.data.v2.stub.CookiesHolder.ROUTING_COOKIE_METADATA_KEY;

import com.google.protobuf.ByteString;
import com.google.rpc.ErrorInfo;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;

/**
 * A cookie interceptor that checks the cookie value from returned ErrorInfo, updates the cookie
 * holder, and inject it in the header of the next request.
 */
class CookieInterceptor implements ClientInterceptor {

  static final Metadata.Key<ErrorInfo> ERROR_INFO_KEY =
      ProtoUtils.keyForProto(ErrorInfo.getDefaultInstance());

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
        channel.newCall(methodDescriptor, callOptions)) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        CookiesHolder cookie = callOptions.getOption(COOKIES_HOLDER_KEY);
        if (cookie != null && cookie.getRoutingCookie() != null) {
          headers.put(ROUTING_COOKIE_METADATA_KEY, cookie.getRoutingCookie().toByteArray());
        }
        super.start(new UpdateCookieListener<>(responseListener, callOptions), headers);
      }
    };
  }

  static class UpdateCookieListener<RespT>
      extends ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT> {

    private final CallOptions callOptions;

    UpdateCookieListener(ClientCall.Listener<RespT> delegate, CallOptions callOptions) {
      super(delegate);
      this.callOptions = callOptions;
    }

    @Override
    public void onClose(Status status, Metadata trailers) {
      if (status != Status.OK && trailers != null) {
        ErrorInfo errorInfo = trailers.get(ERROR_INFO_KEY);
        if (errorInfo != null) {
          CookiesHolder cookieHolder = callOptions.getOption(COOKIES_HOLDER_KEY);
          cookieHolder.setRoutingCookie(
              ByteString.copyFromUtf8(errorInfo.getMetadataMap().get(ROUTING_COOKIE_KEY)));
        }
      }
      super.onClose(status, trailers);
    }
  }
}
