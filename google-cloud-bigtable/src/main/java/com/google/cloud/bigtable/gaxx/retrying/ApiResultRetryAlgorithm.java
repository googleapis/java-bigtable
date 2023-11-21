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
package com.google.cloud.bigtable.gaxx.retrying;

import com.google.api.core.InternalApi;
import com.google.api.gax.retrying.BasicResultRetryAlgorithm;
import com.google.api.gax.retrying.RetryingContext;
import com.google.api.gax.retrying.TimedAttemptSettings;
import com.google.api.gax.rpc.ApiException;
import com.google.protobuf.util.Durations;
import com.google.rpc.RetryInfo;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import org.threeten.bp.Duration;

/**
 * For internal use, public for technical reasons. This retry algorithm checks the metadata of an
 * exception for additional error details. If the metadata has a RetryInfo field, use the retry
 * delay to set the wait time between attempts.
 */
@InternalApi
public class ApiResultRetryAlgorithm<ResponseT> extends BasicResultRetryAlgorithm<ResponseT> {

  private static final Metadata.Key<RetryInfo> KEY_RETRY_INFO =
      ProtoUtils.keyForProto(RetryInfo.getDefaultInstance());

  @Override
  public TimedAttemptSettings createNextAttempt(
      Throwable prevThrowable, ResponseT prevResponse, TimedAttemptSettings prevSettings) {
    Duration retryDelay = extractRetryDelay(prevThrowable);
    if (retryDelay != null) {
      return prevSettings
          .toBuilder()
          .setRandomizedRetryDelay(retryDelay)
          .setAttemptCount(prevSettings.getAttemptCount() + 1)
          .build();
    }
    return null;
  }

  /** Returns true if previousThrowable is an {@link ApiException} that is retryable. */
  @Override
  public boolean shouldRetry(Throwable previousThrowable, ResponseT previousResponse) {
    return (extractRetryDelay(previousThrowable) != null)
        || (previousThrowable instanceof ApiException
            && ((ApiException) previousThrowable).isRetryable());
  }

  /**
   * If {@link RetryingContext#getRetryableCodes()} is not null: Returns true if the status code of
   * previousThrowable is in the list of retryable code of the {@link RetryingContext}.
   *
   * <p>Otherwise it returns the result of {@link #shouldRetry(Throwable, Object)}.
   */
  @Override
  public boolean shouldRetry(
      RetryingContext context, Throwable previousThrowable, ResponseT previousResponse) {
    if (context.getRetryableCodes() != null) {
      // Ignore the isRetryable() value of the throwable if the RetryingContext has a specific list
      // of codes that should be retried.
      return extractRetryDelay(previousThrowable) != null
          || ((previousThrowable instanceof ApiException)
              && context
                  .getRetryableCodes()
                  .contains(((ApiException) previousThrowable).getStatusCode().getCode()));
    }
    return shouldRetry(previousThrowable, previousResponse);
  }

  public static Duration extractRetryDelay(Throwable throwable) {
    if (throwable == null) {
      return null;
    }
    Metadata trailers = Status.trailersFromThrowable(throwable);
    if (trailers == null) {
      return null;
    }
    RetryInfo retryInfo = trailers.get(KEY_RETRY_INFO);
    if (retryInfo == null) {
      return null;
    }
    if (!retryInfo.hasRetryDelay()) {
      return null;
    }
    return Duration.ofMillis(Durations.toMillis(retryInfo.getRetryDelay()));
  }
}
