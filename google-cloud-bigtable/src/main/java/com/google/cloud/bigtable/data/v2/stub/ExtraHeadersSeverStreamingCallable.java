/*
 * Copyright 2021 Google LLC
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

import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.cloud.bigtable.data.v2.stub.metrics.Util;

/**
 * A callable that injects client timestamp and current attempt number to request headers. Attempt
 * number starts from 0.
 */
final class ExtraHeadersSeverStreamingCallable<RequestT, ResponseT>
    extends ServerStreamingCallable<RequestT, ResponseT> {
  private final ServerStreamingCallable innerCallable;

  ExtraHeadersSeverStreamingCallable(ServerStreamingCallable innerCallable) {
    this.innerCallable = innerCallable;
  }

  @Override
  public void call(
      RequestT request,
      ResponseObserver<ResponseT> responseObserver,
      ApiCallContext apiCallContext) {
    ApiCallContext newCallContext =
        apiCallContext.withExtraHeaders(Util.createExtraHeaders(apiCallContext));
    innerCallable.call(request, responseObserver, newCallContext);
  }
}
