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

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Status;

/**
 * A cookie interceptor that checks the cookie value from returned trailer, updates the cookie
 * holder, and inject it in the header of the next request.
 */
class CookiesInterceptor implements ClientInterceptor {

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(
        channel.newCall(methodDescriptor, callOptions)) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        // Gets the CookiesHolder added from CookiesServerStreamingCallable and
        // CookiesUnaryCallable.
        // Add CookiesHolder content to request headers if there's any.
        CookiesHolder cookie = CookiesHolder.fromCallOptions(callOptions);
        if (cookie != null) {
          cookie.injectCookiesInRequestHeaders(headers);
          responseListener = new UpdateCookieListener<>(responseListener, cookie);
        }
        super.start(responseListener, headers);
      }
    };
  }

  /** Add trailers to CookiesHolder if there's any. * */
  static class UpdateCookieListener<RespT>
      extends ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT> {

    private final CookiesHolder cookie;

    UpdateCookieListener(ClientCall.Listener<RespT> delegate, CookiesHolder cookiesHolder) {
      super(delegate);
      this.cookie = cookiesHolder;
    }

    @Override
    public void onClose(Status status, Metadata trailers) {
      cookie.extractCookiesFromResponseTrailers(trailers);
      super.onClose(status, trailers);
    }
  }
}
