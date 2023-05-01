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
package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.DeadlineExceededException;
import com.google.api.gax.rpc.ResourceExhaustedException;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import com.google.api.gax.rpc.UnavailableException;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.RateLimitInfo;
import com.google.cloud.bigtable.data.v2.stub.metrics.BigtableTracer;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.RateLimiter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import org.threeten.bp.Duration;

class RateLimitingServerStreamingCallable
    extends ServerStreamingCallable<MutateRowsRequest, MutateRowsResponse> {
  private static final Logger logger =
      Logger.getLogger(RateLimitingServerStreamingCallable.class.getName());
  private static final long DEFAULT_QPS = 10;
  // Default interval before changing the QPS on error response
  private static Duration DEFAULT_PERIOD = Duration.ofSeconds(10);
  private final RateLimiter limiter;
  private final AtomicLong lastQpsChangeTime = new AtomicLong(System.currentTimeMillis());
  private final ServerStreamingCallable<MutateRowsRequest, MutateRowsResponse> innerCallable;
  private static final double MIN_QPS = 0.1;
  private static final double MAX_QPS = 100_000;
  @VisibleForTesting static final double MIN_FACTOR = 0.7;
  private static final double MAX_FACTOR = 1.3;

  RateLimitingServerStreamingCallable(
      @Nonnull ServerStreamingCallable<MutateRowsRequest, MutateRowsResponse> innerCallable) {
    this.limiter = RateLimiter.create(DEFAULT_QPS);
    this.innerCallable = Preconditions.checkNotNull(innerCallable, "Inner callable must be set");
    logger.info("Rate limiting is enabled with initial QPS of " + limiter.getRate());
  }

  @Override
  public void call(
      MutateRowsRequest request,
      ResponseObserver<MutateRowsResponse> responseObserver,
      ApiCallContext context) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    limiter.acquire();
    stopwatch.stop();
    if (context.getTracer() instanceof BigtableTracer) {
      ((BigtableTracer) context.getTracer())
          .batchRequestThrottled(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
    RateLimitingResponseObserver innerObserver = new RateLimitingResponseObserver(responseObserver);
    innerCallable.call(request, innerObserver, context);
  }

  class RateLimitingResponseObserver extends SafeResponseObserver<MutateRowsResponse> {
    private final ResponseObserver<MutateRowsResponse> outerObserver;

    RateLimitingResponseObserver(ResponseObserver<MutateRowsResponse> observer) {
      super(observer);
      this.outerObserver = observer;
    }

    @Override
    protected void onStartImpl(final StreamController controller) {
      outerObserver.onStart(controller);
    }

    @Override
    protected void onResponseImpl(MutateRowsResponse response) {
      if (response.hasRateLimitInfo()) {
        RateLimitInfo info = response.getRateLimitInfo();
        // RateLimitInfo is an optional field. However, proto3 sub-message field always
        // have presence even thought it's marked as "optional". Check the factor and
        // period to make sure they're not 0.
        if (info.getFactor() != 0 && info.getPeriod().getSeconds() != 0) {
          updateQps(info.getFactor(), Duration.ofSeconds(info.getPeriod().getSeconds()));
        }
      }
    }

    @Override
    protected void onErrorImpl(Throwable t) {
      // When server returns DEADLINE_EXCEEDED, UNAVAILABLE or RESOURCE_EXHAUSTED,
      // assume cbt server is overloaded
      if (t instanceof DeadlineExceededException
          || t instanceof UnavailableException
          || t instanceof ResourceExhaustedException) {
        updateQps(MIN_FACTOR, DEFAULT_PERIOD);
      }
      outerObserver.onError(t);
    }

    @Override
    protected void onCompleteImpl() {
      outerObserver.onComplete();
    }

    private void updateQps(double factor, Duration period) {
      long lastTime = lastQpsChangeTime.get();
      long now = System.currentTimeMillis();
      if (now - lastTime > period.toMillis() && lastQpsChangeTime.compareAndSet(lastTime, now)) {
        double cappedFactor = Math.min(Math.max(factor, MIN_FACTOR), MAX_FACTOR);
        double currentRate = limiter.getRate();
        limiter.setRate(Math.min(Math.max(currentRate * cappedFactor, MIN_QPS), MAX_QPS));
        logger.fine(
            "Updated QPS from "
                + currentRate
                + " to "
                + limiter.getRate()
                + ", server returned factor is "
                + factor
                + ", capped factor is "
                + cappedFactor);
      }
    }
  }

  @VisibleForTesting
  AtomicLong getLastQpsChangeTime() {
    return lastQpsChangeTime;
  }

  @VisibleForTesting
  double getCurrentRate() {
    return limiter.getRate();
  }
}
