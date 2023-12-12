/*
 * Copyright 2017 Google LLC
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *     * Neither the name of Google LLC nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.google.cloud.bigtable.gaxx.retrying;

import com.google.api.core.BetaApi;
import com.google.api.gax.retrying.ExponentialRetryAlgorithm;
import com.google.api.gax.retrying.RetryAlgorithm;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.retrying.ScheduledRetryingExecutor;
import com.google.api.gax.retrying.StreamingRetryAlgorithm;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.ServerStreamingCallSettings;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.UnaryCallSettings;
import com.google.api.gax.rpc.UnaryCallable;
import java.util.Collection;

// TODO: remove this once ApiResultRetryAlgorithm is added to gax.
/**
 * Class with utility methods to create callable objects using provided settings.
 *
 * <p>The callable objects wrap a given direct callable with features like retry and exception
 * translation.
 */
@BetaApi
public class Callables {

  private Callables() {}

  public static <RequestT, ResponseT> UnaryCallable<RequestT, ResponseT> retrying(
      UnaryCallable<RequestT, ResponseT> innerCallable,
      UnaryCallSettings<?, ?> callSettings,
      ClientContext clientContext) {

    UnaryCallSettings<?, ?> settings = callSettings;

    if (areRetriesDisabled(settings.getRetryableCodes(), settings.getRetrySettings())) {
      // When retries are disabled, the total timeout can be treated as the rpc timeout.
      settings =
          settings
              .toBuilder()
              .setSimpleTimeoutNoRetries(settings.getRetrySettings().getTotalTimeout())
              .build();
    }

    RetryAlgorithm<ResponseT> retryAlgorithm =
        new RetryAlgorithm<>(
            new ApiResultRetryAlgorithm<ResponseT>(),
            new ExponentialRetryAlgorithm(settings.getRetrySettings(), clientContext.getClock()));
    ScheduledRetryingExecutor<ResponseT> retryingExecutor =
        new ScheduledRetryingExecutor<>(retryAlgorithm, clientContext.getExecutor());
    return new RetryingCallable<>(
        clientContext.getDefaultCallContext(), innerCallable, retryingExecutor);
  }

  public static <RequestT, ResponseT> ServerStreamingCallable<RequestT, ResponseT> retrying(
      ServerStreamingCallable<RequestT, ResponseT> innerCallable,
      ServerStreamingCallSettings<RequestT, ResponseT> callSettings,
      ClientContext clientContext) {

    ServerStreamingCallSettings<RequestT, ResponseT> settings = callSettings;
    if (areRetriesDisabled(settings.getRetryableCodes(), settings.getRetrySettings())) {
      // When retries are disabled, the total timeout can be treated as the rpc timeout.
      settings =
          settings
              .toBuilder()
              .setSimpleTimeoutNoRetries(settings.getRetrySettings().getTotalTimeout())
              .build();
    }

    StreamingRetryAlgorithm<Void> retryAlgorithm =
        new StreamingRetryAlgorithm<>(
            new ApiResultRetryAlgorithm<Void>(),
            new ExponentialRetryAlgorithm(settings.getRetrySettings(), clientContext.getClock()));

    ScheduledRetryingExecutor<Void> retryingExecutor =
        new ScheduledRetryingExecutor<>(retryAlgorithm, clientContext.getExecutor());

    return new RetryingServerStreamingCallable<>(
        innerCallable, retryingExecutor, settings.getResumptionStrategy());
  }

  private static boolean areRetriesDisabled(
      Collection<StatusCode.Code> retryableCodes, RetrySettings retrySettings) {
    return retrySettings.getMaxAttempts() == 1
        || retryableCodes.isEmpty()
        || (retrySettings.getMaxAttempts() == 0 && retrySettings.getTotalTimeout().isZero());
  }
}
