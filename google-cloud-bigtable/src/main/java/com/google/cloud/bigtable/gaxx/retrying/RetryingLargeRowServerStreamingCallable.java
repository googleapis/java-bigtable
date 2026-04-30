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

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.core.InternalApi;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.api.gax.retrying.ScheduledRetryingExecutor;
import com.google.api.gax.retrying.ServerStreamingAttemptException;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.cloud.bigtable.data.v2.models.LargeRowException;
import com.google.cloud.bigtable.data.v2.stub.readrows.LargeReadRowsResumptionStrategy;
import com.google.protobuf.ByteString;
import java.util.List;

/** A ServerStreamingCallable that throws large row keys at the end of the stream. */
@InternalApi
public final class RetryingLargeRowServerStreamingCallable<ResponseT>
    extends ServerStreamingCallable<ReadRowsRequest, ResponseT> {

  private final ServerStreamingCallable<ReadRowsRequest, ResponseT> innerCallable;
  private final ScheduledRetryingExecutor<Void> executor;
  private final LargeReadRowsResumptionStrategy<ResponseT> resumptionStrategy;

  public RetryingLargeRowServerStreamingCallable(
      ServerStreamingCallable<ReadRowsRequest, ResponseT> innerCallable,
      ScheduledRetryingExecutor<Void> executor,
      LargeReadRowsResumptionStrategy<ResponseT> resumptionStrategy) {
    this.innerCallable = innerCallable;
    this.executor = executor;
    this.resumptionStrategy = resumptionStrategy;
  }

  @Override
  public void call(
      ReadRowsRequest request,
      final ResponseObserver<ResponseT> responseObserver,
      ApiCallContext context) {

    final LargeReadRowsResumptionStrategy<ResponseT> strategy =
        (LargeReadRowsResumptionStrategy<ResponseT>) resumptionStrategy.createNew();

    ServerStreamingAttemptCallable<ReadRowsRequest, ResponseT> attemptCallable =
        new ServerStreamingAttemptCallable<>(
            innerCallable, strategy, request, context, responseObserver);

    RetryingFuture<Void> retryingFuture = executor.createFuture(attemptCallable, context);
    attemptCallable.setExternalFuture(retryingFuture);
    attemptCallable.start();

    // Bridge the future result back to the external responseObserver
    ApiFutures.addCallback(
        retryingFuture,
        new ApiFutureCallback<Void>() {
          @Override
          public void onFailure(Throwable throwable) {
            // Make sure to unwrap the underlying ApiException
            if (throwable instanceof ServerStreamingAttemptException) {
              throwable = throwable.getCause();
            }
            List<ByteString> encounteredKeys = strategy.getLargeRowKeys();
            if (!encounteredKeys.isEmpty()) {
              throwable.addSuppressed(new LargeRowException(encounteredKeys));
            }
            responseObserver.onError(throwable);
          }

          @Override
          public void onSuccess(Void ignored) {
            List<ByteString> encounteredKeys = strategy.getLargeRowKeys();
            if (!encounteredKeys.isEmpty()) {
              responseObserver.onError(new LargeRowException(encounteredKeys));
            } else {
              responseObserver.onComplete();
            }
          }
        },
        directExecutor());
  }
}
