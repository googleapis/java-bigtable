/*
 * Copyright 2018 Google LLC
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
package com.google.cloud.bigtable.admin.v2.stub;

import com.google.api.core.ApiAsyncFunction;
import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.retrying.ExponentialPollAlgorithm;
import com.google.api.gax.retrying.NonCancellableFuture;
import com.google.api.gax.retrying.ResultRetryAlgorithm;
import com.google.api.gax.retrying.RetryAlgorithm;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.retrying.RetryingExecutor;
import com.google.api.gax.retrying.RetryingFuture;
import com.google.api.gax.retrying.ScheduledRetryingExecutor;
import com.google.api.gax.retrying.TimedAttemptSettings;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.admin.v2.CheckConsistencyRequest;
import com.google.bigtable.admin.v2.CheckConsistencyResponse;
import com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest;
import com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse;
import com.google.bigtable.admin.v2.TableName;
import com.google.cloud.bigtable.admin.v2.models.ConsistencyParams;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;

/**
 * Callable that waits until replication has caught up to the point it was called.
 *
 * <p>This callable wraps GenerateConsistencyToken and CheckConsistency RPCs. It will generate a
 * token then poll until isConsistent is true.
 */
/** @deprecated Please use {@link AwaitConsistencyCallable instead. */
@Deprecated
class AwaitReplicationCallable extends UnaryCallable<TableName, Void> {
  private final AwaitConsistencyCallable awaitConsistencyCallable;

  static AwaitReplicationCallable create(AwaitConsistencyCallable awaitConsistencyCallable) {

    return new AwaitReplicationCallable(awaitConsistencyCallable);
  }

  @Override
  public ApiFuture<Void> futureCall(final TableName tableName, final ApiCallContext context) {
    ConsistencyParams consistencyParams = ConsistencyParams.of(tableName, ConsistencyParams.ConsistencyMode.STANDARD);

    return awaitConsistencyCallable.futureCall(consistencyParams);
  }

  @VisibleForTesting
  AwaitReplicationCallable(AwaitConsistencyCallable awaitConsistencyCallable) {
    this.awaitConsistencyCallable = awaitConsistencyCallable;
  }
}
