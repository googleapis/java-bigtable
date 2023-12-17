package com.google.cloud.bigtable.gaxx.retrying;

import com.google.api.core.InternalApi;
import com.google.api.gax.retrying.BasicResultRetryAlgorithm;
import com.google.api.gax.retrying.RetryingContext;
import com.google.api.gax.retrying.TimedAttemptSettings;
import com.google.api.gax.rpc.ApiException;
import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.util.Durations;
import com.google.rpc.RetryInfo;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import org.threeten.bp.Duration;

// TODO move this algorithm to gax
/**
 * This retry algorithm checks the metadata of an exception for additional error details. If the
 * metadata has a RetryInfo field, use the retry delay to set the wait time between attempts.
 */
@InternalApi
public class RetryInfoRetryAlgorithm<ResponseT> extends BasicResultRetryAlgorithm<ResponseT> {

  @VisibleForTesting
  public static final Metadata.Key<RetryInfo> RETRY_INFO_KEY =
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
    if (extractRetryDelay(previousThrowable) != null) {
      // First check if server wants us to retry
      return true;
    }
    // Server didn't have retry information, use the local status code config.
    return (previousThrowable instanceof ApiException
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
    if (extractRetryDelay(previousThrowable) != null) {
      // First check if server wants us to retry
      return true;
    }
    if (context.getRetryableCodes() != null) {
      // Ignore the isRetryable() value of the throwable if the RetryingContext has a specific list
      // of codes that should be retried.
      return ((previousThrowable instanceof ApiException)
          && context
              .getRetryableCodes()
              .contains(((ApiException) previousThrowable).getStatusCode().getCode()));
    }
    return shouldRetry(previousThrowable, previousResponse);
  }

  static Duration extractRetryDelay(Throwable throwable) {
    if (throwable == null) {
      return null;
    }
    Metadata trailers = Status.trailersFromThrowable(throwable);
    if (trailers == null) {
      return null;
    }
    RetryInfo retryInfo = trailers.get(RETRY_INFO_KEY);
    if (retryInfo == null) {
      return null;
    }
    if (!retryInfo.hasRetryDelay()) {
      return null;
    }
    return Duration.ofMillis(Durations.toMillis(retryInfo.getRetryDelay()));
  }
}
