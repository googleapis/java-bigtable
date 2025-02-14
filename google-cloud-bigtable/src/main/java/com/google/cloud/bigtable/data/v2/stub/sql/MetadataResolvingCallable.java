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
package com.google.cloud.bigtable.data.v2.stub.sql;

import com.google.api.core.InternalApi;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import com.google.bigtable.v2.ExecuteQueryRequest;
import com.google.bigtable.v2.ExecuteQueryResponse;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.cloud.bigtable.data.v2.models.sql.ResultSetMetadata;
import com.google.cloud.bigtable.data.v2.stub.SafeResponseObserver;

/**
 * Callable that allows passing of {@link ResultSetMetadata} back to users throught the {@link
 * ExecuteQueryCallContext}.
 *
 * <p>This is considered an internal implementation detail and should not be used by applications.
 */
@InternalApi("For internal use only")
public class MetadataResolvingCallable
    extends ServerStreamingCallable<ExecuteQueryCallContext, ExecuteQueryResponse> {
  private final ServerStreamingCallable<ExecuteQueryRequest, ExecuteQueryResponse> inner;
  private final RequestContext requestContext;

  public MetadataResolvingCallable(
      ServerStreamingCallable<ExecuteQueryRequest, ExecuteQueryResponse> inner,
      RequestContext requestContext) {
    this.inner = inner;
    this.requestContext = requestContext;
  }

  @Override
  public void call(
      ExecuteQueryCallContext callContext,
      ResponseObserver<ExecuteQueryResponse> responseObserver,
      ApiCallContext apiCallContext) {
    MetadataObserver observer = new MetadataObserver(responseObserver, callContext);
    inner.call(callContext.toRequest(requestContext), observer, apiCallContext);
  }

  static final class MetadataObserver extends SafeResponseObserver<ExecuteQueryResponse> {

    private final ExecuteQueryCallContext callContext;
    private final ResponseObserver<ExecuteQueryResponse> outerObserver;
    // This doesn't need to be synchronized because this is called above the reframer
    // so onResponse will be called sequentially
    private boolean hasReceivedResumeToken;

    MetadataObserver(
        ResponseObserver<ExecuteQueryResponse> outerObserver, ExecuteQueryCallContext callContext) {
      super(outerObserver);
      this.outerObserver = outerObserver;
      this.callContext = callContext;
      this.hasReceivedResumeToken = false;
    }

    @Override
    protected void onStartImpl(StreamController streamController) {
      outerObserver.onStart(streamController);
    }

    @Override
    protected void onResponseImpl(ExecuteQueryResponse response) {
      // Defer finalizing metadata until we receive a resume token, because this is the
      // only point we can guarantee it won't change.
      //
      // An example of why this is necessary, for query "SELECT * FROM table":
      // - Make a request, table has one column family 'cf'
      // - Return an incomplete batch
      // - request fails with transient error
      // - Meanwhile the table has had a second column family added 'cf2'
      // - Retry the request, get an error indicating the `prepared_query` has expired
      // - Refresh the prepared_query and retry the request, the new prepared_query
      //   contains both 'cf' & 'cf2'
      // - It sends a new incomplete batch and resets the old outdated batch
      // - It send the next chunk with a checksum and resume_token, closing the batch.
      // In this case the row merger and the ResultSet should be using the updated schema from
      // the refreshed prepare request.
      if (!hasReceivedResumeToken && !response.getResults().getResumeToken().isEmpty()) {
        callContext.finalizeMetadata();
        hasReceivedResumeToken = true;
      }
      outerObserver.onResponse(response);
    }

    @Override
    protected void onErrorImpl(Throwable throwable) {
      // When we support retries this will have to move after the retrying callable in a separate
      // observer.
      callContext.setMetadataException(throwable);
      outerObserver.onError(throwable);
    }

    @Override
    protected void onCompleteImpl() {
      if (!callContext.resultSetMetadataFuture().isDone()) {
        // If stream succeeds with no responses, we can finalize the metadata
        callContext.finalizeMetadata();
      }
      outerObserver.onComplete();
    }
  }
}
