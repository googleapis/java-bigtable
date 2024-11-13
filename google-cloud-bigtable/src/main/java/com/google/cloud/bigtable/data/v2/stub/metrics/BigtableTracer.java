/*
 * Copyright 2020 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.tracing.ApiTracer;
import com.google.api.gax.tracing.BaseApiTracer;
import javax.annotation.Nullable;
import org.threeten.bp.Duration;

/**
 * A Bigtable specific {@link ApiTracer} that includes additional contexts. This class is a base
 * implementation that does nothing.
 */
@BetaApi("This surface is not stable yet it might be removed in the future.")
public class BigtableTracer extends BaseApiTracer {

  private volatile int attempt = 0;

  @InternalApi("for internal use only")
  public static final ApiCallContext.Key<Duration> OPERATION_TIMEOUT_KEY =
      ApiCallContext.Key.create("OPERATION_TIMEOUT");

  @Override
  public void attemptStarted(int attemptNumber) {
    this.attempt = attemptNumber;
  }

  /** annotate when onRequest is called. This will be called in BuiltinMetricsTracer. */
  public void onRequest(int requestCount) {
    // noop
  }

  /**
   * annotate when automatic flow control is disabled. This will be called in BuiltinMetricsTracer.
   */
  public void disableFlowControl() {
    // noop
  }

  /** annotate after the callback from onResponse. This will be called in BuiltinMetricsTracer. */
  public void afterResponse(long applicationLatency) {
    // noop
  }

  /**
   * Used by BigtableUnaryOperationCallable to signal that the user visible portion of the RPC is
   * complete and that metrics should freeze the timers and then publish the frozen values when the
   * internal portion of the operation completes.
   */
  public void operationFinishEarly() {}

  /**
   * Get the attempt number of the current call. Attempt number for the current call is passed in
   * and should be recorded in {@link #attemptStarted(int)}. With the getter we can access it from
   * {@link ApiCallContext}. Attempt number starts from 0.
   */
  public int getAttempt() {
    return attempt;
  }

  /**
   * Record the latency between Google's network receives the RPC and reads back the first byte of
   * the response from server-timing header. If server-timing header is missing, increment the
   * missing header count.
   */
  public void recordGfeMetadata(@Nullable Long latency, @Nullable Throwable throwable) {
    // noop
  }

  /** Adds an annotation of the total throttled time of a batch. */
  public void batchRequestThrottled(long throttledTimeMs) {
    // noop
  }

  /**
   * Set the Bigtable zone and cluster so metrics can be tagged with location information. This will
   * be called in BuiltinMetricsTracer.
   */
  public void setLocations(String zone, String cluster) {
    // noop
  }

  @Deprecated
  /** @deprecated {@link #grpcMessageSent()} is called instead. */
  public void grpcChannelQueuedLatencies(long queuedTimeMs) {
    // noop
  }

  /** Called when the message is sent on a grpc channel. */
  public void grpcMessageSent() {
    // noop
  }

  /**
   * Record the operation timeout from user settings for calculating remaining deadline. This will
   * be called in BuiltinMetricsTracer.
   */
  public void setOperationTimeout(Duration operationTimeout) {
    // noop
  }
}
