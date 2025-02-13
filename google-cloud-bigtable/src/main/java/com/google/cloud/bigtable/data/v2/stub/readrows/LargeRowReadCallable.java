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
package com.google.cloud.bigtable.data.v2.stub.readrows;

import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.bigtable.data.v2.stub.SafeResponseObserver;

/**
 * This callable converts the "Received rst stream" exception into a retryable {@link ApiException}.
 */
public final class LargeRowReadCallable<RequestT, ResponseT, RowT>
    extends ServerStreamingCallable<RequestT, ResponseT> {

  private final ServerStreamingCallable<RequestT, ResponseT> innerCallable;

  private final StreamResumptionStrategy<RequestT, ResponseT> resumptionStrategy;

  public LargeRowReadCallable(
      ServerStreamingCallable<RequestT, ResponseT> innerCallable,
      StreamResumptionStrategy<RequestT, ResponseT> resumptionStrategy) {
    this.innerCallable = innerCallable;
    this.resumptionStrategy = resumptionStrategy;
  }

  @Override
  public void call(
      RequestT request, ResponseObserver<ResponseT> responseObserver, ApiCallContext context) {
    ConvertExceptionResponseObserver<ResponseT> observer =
        new ConvertExceptionResponseObserver<>(responseObserver);
    innerCallable.call(request, observer, context);
  }

  private class ConvertExceptionResponseObserver<ResponseT>
      extends SafeResponseObserver<ResponseT> {

    private final ResponseObserver<ResponseT> outerObserver;

    ConvertExceptionResponseObserver(ResponseObserver<ResponseT> outerObserver) {
      super(outerObserver);
      this.outerObserver = outerObserver;
    }

    @Override
    protected void onStartImpl(StreamController controller) {
      outerObserver.onStart(controller);
    }

    @Override
    protected void onResponseImpl(ResponseT response) {
      outerObserver.onResponse(response);
    }

    @Override
    protected void onErrorImpl(Throwable t) {
      // this has no impact
      // if (resumptionStrategy instanceof LargeReadRowsResumptionStrategy) {
      //   LargeReadRowsResumptionStrategy derived = (LargeReadRowsResumptionStrategy)
      // resumptionStrategy;
      //   derived.setLargeRowKey(t);
      // }
      outerObserver.onError(t);
    }

    @Override
    protected void onCompleteImpl() {
      outerObserver.onComplete();
    }
  }
}
