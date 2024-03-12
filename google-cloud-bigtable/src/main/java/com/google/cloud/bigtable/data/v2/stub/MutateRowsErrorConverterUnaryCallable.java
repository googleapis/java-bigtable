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
package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.MutateRowsException;
import com.google.cloud.bigtable.data.v2.stub.mutaterows.MutateRowsAttemptResult;
import com.google.common.util.concurrent.MoreExecutors;

public class MutateRowsErrorConverterUnaryCallable extends UnaryCallable<BulkMutation, Void> {

  private final UnaryCallable<BulkMutation, MutateRowsAttemptResult> innerCallable;

  MutateRowsErrorConverterUnaryCallable(
      UnaryCallable<BulkMutation, MutateRowsAttemptResult> callable) {
    this.innerCallable = callable;
  }

  @Override
  public ApiFuture<Void> futureCall(BulkMutation request, ApiCallContext context) {
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
