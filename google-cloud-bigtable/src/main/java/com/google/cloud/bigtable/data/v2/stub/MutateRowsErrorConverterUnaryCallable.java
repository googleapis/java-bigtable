package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.cloud.bigtable.data.v2.models.MutateRowsException;
import com.google.cloud.bigtable.data.v2.stub.mutaterows.MutateRowsAttemptResult;
import com.google.common.util.concurrent.MoreExecutors;

public class MutateRowsErrorConverterUnaryCallable extends UnaryCallable<MutateRowsRequest, Void> {

  private final UnaryCallable<MutateRowsRequest, MutateRowsAttemptResult> innerCallable;

  MutateRowsErrorConverterUnaryCallable(
      UnaryCallable<MutateRowsRequest, MutateRowsAttemptResult> callable) {
    this.innerCallable = callable;
  }

  @Override
  public ApiFuture<Void> futureCall(MutateRowsRequest request, ApiCallContext context) {
    ApiFuture<MutateRowsAttemptResult> future = innerCallable.futureCall(request, context);
    return ApiFutures.transform(
        future,
        result -> {
          if (!result.failedMutations.isEmpty()) {
            throw MutateRowsException.create(null, result.failedMutations, result.isRetryable);
          }
          return null;
        },
        MoreExecutors.directExecutor());
  }
}
