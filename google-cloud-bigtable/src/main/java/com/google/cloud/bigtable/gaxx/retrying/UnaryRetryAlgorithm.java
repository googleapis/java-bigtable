/*
 * Copyright 2023 Google LLC
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
import com.google.api.gax.retrying.ResultRetryAlgorithmWithContext;
import com.google.api.gax.retrying.RetryAlgorithm;
import com.google.api.gax.retrying.RetryingContext;
import com.google.api.gax.retrying.TimedAttemptSettings;
import com.google.api.gax.retrying.TimedRetryAlgorithmWithContext;
import java.util.concurrent.CancellationException;

/**
 * Retry algorithm for unary calls. It'll use the result from result algorithm first and only fall
 * back to timedAlgorithm if resultAlgorithm#shouldRetry is false.
 */
@InternalApi
public class UnaryRetryAlgorithm<ResponseT> extends RetryAlgorithm<ResponseT> {

  public UnaryRetryAlgorithm(
      ResultRetryAlgorithmWithContext<ResponseT> resultAlgorithm,
      TimedRetryAlgorithmWithContext timedAlgorithm) {
    super(resultAlgorithm, timedAlgorithm);
  }

  @Override
  public boolean shouldRetry(
      RetryingContext context,
      Throwable previousThrowable,
      ResponseT previousResponse,
      TimedAttemptSettings nextAttemptSettings)
      throws CancellationException {
    if (getResultAlgorithm().shouldRetry(previousThrowable, previousResponse)) {
      return true;
    }
    return super.shouldRetry(context, previousThrowable, previousResponse, nextAttemptSettings);
  }
}
