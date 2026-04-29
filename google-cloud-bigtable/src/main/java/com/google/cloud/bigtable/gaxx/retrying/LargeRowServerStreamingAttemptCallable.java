/*
 * Copyright 2025 Google LLC
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
package com.google.cloud.bigtable.gaxx.retrying;

import com.google.api.core.InternalApi;
import com.google.api.gax.retrying.ServerStreamingAttemptException;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.cloud.bigtable.data.v2.stub.BigtableStreamResumptionStrategy;
import com.google.cloud.bigtable.data.v2.stub.readrows.LargeReadRowsResumptionStrategy;

/**
 * A specialized version of {@link ServerStreamingAttemptCallable} for large rows.
 *
 * <p>This class tracks all the large rows and return them in an exception in the end.
 */
@InternalApi
public final class LargeRowServerStreamingAttemptCallable<ResponseT>
    extends ServerStreamingAttemptCallable<ReadRowsRequest, ResponseT> {

  public LargeRowServerStreamingAttemptCallable(
      ServerStreamingCallable<ReadRowsRequest, ResponseT> innerCallable,
      LargeReadRowsResumptionStrategy<ResponseT> resumptionStrategy,
      ReadRowsRequest initialRequest,
      ApiCallContext context,
      ResponseObserver<ResponseT> outerObserver) {
    super(innerCallable, resumptionStrategy, initialRequest, context, outerObserver);
  }

  /**
   * Called when the current RPC fails. The error will be bubbled up to the outer {@link
   * com.google.api.gax.retrying.RetryingFuture} via the {@link #innerAttemptFuture}.
   */
  @Override
  protected void onAttemptError(Throwable throwable) {
    Throwable localCancellationCause;
    synchronized (lock) {
      localCancellationCause = cancellationCause;
    }
    if (resumptionStrategy instanceof BigtableStreamResumptionStrategy) {
      throwable = ((BigtableStreamResumptionStrategy) resumptionStrategy).processError(throwable);
    }

    if (localCancellationCause != null) {
      // Take special care to preserve the cancellation's stack trace.
      innerAttemptFuture.setException(localCancellationCause);
    } else {
      // Wrap the original exception and provide more context for StreamingRetryAlgorithm.
      innerAttemptFuture.setException(
          new ServerStreamingAttemptException(
              throwable, resumptionStrategy.canResume(), seenSuccessSinceLastError));
    }
  }

  /**
   * Called when the current RPC successfully completes. Notifies the outer {@link
   * com.google.api.gax.retrying.RetryingFuture} via {@link #innerAttemptFuture}.
   */
  @Override
  protected void onAttemptComplete() {
    innerAttemptFuture.set(null);
  }
}
