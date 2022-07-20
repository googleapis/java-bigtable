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

import com.google.api.core.InternalApi;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.InternalException;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;

/**
 * This callable converts the "Received rst stream" exception into a retryable {@link ApiException}.
 */
@InternalApi
public final class ConvertStreamExceptionCallable<RequestT, ResponseT>
    extends ServerStreamingCallable<RequestT, ResponseT> {

  private final ServerStreamingCallable<RequestT, ResponseT> innerCallable;

  public ConvertStreamExceptionCallable(
      ServerStreamingCallable<RequestT, ResponseT> innerCallable) {
    this.innerCallable = innerCallable;
  }

  @Override
  public void call(
      RequestT request, ResponseObserver<ResponseT> responseObserver, ApiCallContext context) {
    ConvertStreamExceptionResponseObserver<ResponseT> observer =
        new ConvertStreamExceptionResponseObserver<>(responseObserver);
    innerCallable.call(request, observer, context);
  }

  private class ConvertStreamExceptionResponseObserver<ResponseT>
      implements ResponseObserver<ResponseT> {

    private final ResponseObserver<ResponseT> outerObserver;

    ConvertStreamExceptionResponseObserver(ResponseObserver<ResponseT> outerObserver) {
      this.outerObserver = outerObserver;
    }

    @Override
    public void onStart(StreamController controller) {
      outerObserver.onStart(controller);
    }

    @Override
    public void onResponse(ResponseT response) {
      outerObserver.onResponse(response);
    }

    @Override
    public void onError(Throwable t) {
      outerObserver.onError(convertException(t));
    }

    @Override
    public void onComplete() {
      outerObserver.onComplete();
    }
  }

  private Throwable convertException(Throwable t) {
    // Long lived connections sometimes are disconnected via an RST frame. This error is
    // transient and should be retried.
    if (t instanceof InternalException && t.getMessage() != null) {
      String error = t.getMessage().toLowerCase();
      if (error.contains("rst_stream") || error.contains("rst stream")) {
        return new InternalException(t, ((InternalException) t).getStatusCode(), true);
      }
    }
    return t;
  }
}
