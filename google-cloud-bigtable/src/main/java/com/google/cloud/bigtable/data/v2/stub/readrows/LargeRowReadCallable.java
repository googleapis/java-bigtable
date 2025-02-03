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
import com.google.api.gax.rpc.InternalException;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.cloud.bigtable.data.v2.stub.SafeResponseObserver;
import com.google.common.base.Throwables;

/**
 * This callable converts the "Received rst stream" exception into a retryable {@link ApiException}.
 */
public final class LargeRowReadCallable<RequestT, ResponseT,RowT>
    extends ServerStreamingCallable<RequestT, ResponseT> {

  private final ServerStreamingCallable<RequestT, ResponseT> innerCallable;

  private final LargeReadRowsResumptionStrategy<ResponseT> resumptionStrategy;


  public LargeRowReadCallable(ServerStreamingCallable<RequestT, ResponseT> innerCallable, LargeReadRowsResumptionStrategy<ResponseT> resumptionStrategy) {
    this.innerCallable = innerCallable;
    this.resumptionStrategy = resumptionStrategy;
  }


  @Override
  public void call(
      RequestT request, ResponseObserver<ResponseT> responseObserver, ApiCallContext context) {

    ConvertExceptionResponseObserver<ResponseT> observer =
        new ConvertExceptionResponseObserver<>(responseObserver);

    // calling outerObserver.onError ->
    innerCallable.call(request, observer, context);
    // innerCallable is RetryingServerStreamingCallable
    // Sarthak -  cannot find the actual implementation of this
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

    // Sarthak - checked here -> it is adding the error in the buffer || would this be analysed in procesResponse now? or where would this error be catched?
    @Override
    protected void onErrorImpl(Throwable t) {
      resumptionStrategy.setLargeRowKey(t);
      outerObserver.onError(t);
    }

    @Override
    protected void onCompleteImpl() {
      outerObserver.onComplete();
    }
  }


}
