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

import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_OP;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_STATUS;

import com.google.api.gax.retrying.ServerStreamingAttemptException;
import com.google.api.gax.tracing.ApiTracerFactory.OperationType;
import com.google.api.gax.tracing.SpanName;
import com.google.common.base.Stopwatch;
import io.opentelemetry.api.common.Attributes;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import org.threeten.bp.Duration;

class MetricsTracer extends BigtableTracer {

  private final OperationType operationType;

  private final MetricsTracerRecorder recorder;

  // Tags
  private final SpanName spanName;
  private final Attributes baseAttributes;

  // Operation level metrics
  private final AtomicBoolean opFinished = new AtomicBoolean();
  private final Stopwatch operationTimer = Stopwatch.createStarted();
  private final Stopwatch firstResponsePerOpTimer = Stopwatch.createStarted();
  private long operationResponseCount = 0;

  // Attempt level metrics
  private int attemptCount = 0;
  private Stopwatch attemptTimer;
  private long attemptResponseCount = 0;

  private volatile int attempt = 0;

  MetricsTracer(
      OperationType operationType,
      SpanName spanName,
      MetricsTracerRecorder recorder,
      Attributes attributes) {
    this.operationType = operationType;
    this.recorder = recorder;
    this.spanName = spanName;
    this.baseAttributes = attributes.toBuilder().put(BIGTABLE_OP, spanName.toString()).build();
  }

  @Override
  public Scope inScope() {
    return new Scope() {
      @Override
      public void close() {}
    };
  }

  @Override
  public void operationSucceeded() {
    recordOperationCompletion(null);
  }

  @Override
  public void operationCancelled() {
    recordOperationCompletion(new CancellationException());
  }

  @Override
  public void operationFailed(Throwable throwable) {
    recordOperationCompletion(throwable);
  }

  private void recordOperationCompletion(@Nullable Throwable throwable) {
    if (!opFinished.compareAndSet(false, true)) {
      return;
    }
    operationTimer.stop();

    Attributes attributes =
        baseAttributes.toBuilder().put(BIGTABLE_STATUS, Util.extractStatus(throwable)).build();

    if (operationType == OperationType.ServerStreaming
        && spanName.getMethodName().equals("ReadRows")) {
      recorder.recordFirstResponseLatencies(
          firstResponsePerOpTimer.elapsed(TimeUnit.MILLISECONDS), attributes);
    }

    recorder.recordOperationLatencies(operationTimer.elapsed(TimeUnit.MILLISECONDS), attributes);
    recorder.recordRetryCount(attemptCount, attributes);
  }

  @Override
  public void attemptStarted(int attemptNumber) {
    attempt = attemptNumber;
    attemptCount++;
    attemptTimer = Stopwatch.createStarted();
    attemptResponseCount = 0;
  }

  @Override
  public void attemptSucceeded() {
    recordAttemptCompletion(null);
  }

  @Override
  public void attemptCancelled() {
    recordAttemptCompletion(new CancellationException());
  }

  @Override
  public void attemptFailed(Throwable throwable, Duration duration) {
    recordAttemptCompletion(throwable);
  }

  @Override
  public void attemptFailedRetriesExhausted(Throwable throwable) {
    recordAttemptCompletion(throwable);
  }

  @Override
  public void attemptPermanentFailure(Throwable throwable) {
    recordAttemptCompletion(throwable);
  }

  private void recordAttemptCompletion(@Nullable Throwable throwable) {
    long attemptLatency = attemptTimer.elapsed(TimeUnit.MILLISECONDS);

    // Patch the throwable until it's fixed in gax. When an attempt failed,
    // it'll throw a ServerStreamingAttemptException. Unwrap the exception
    // so it could get processed by extractStatus
    if (throwable instanceof ServerStreamingAttemptException) {
      throwable = throwable.getCause();
    }

    recorder.recordAttemptLatencies(
        attemptLatency,
        baseAttributes.toBuilder().put(BIGTABLE_STATUS, Util.extractStatus(throwable)).build());
  }

  @Override
  public void responseReceived() {
    if (firstResponsePerOpTimer.isRunning()) {
      firstResponsePerOpTimer.stop();
    }
    attemptResponseCount++;
    operationResponseCount++;
  }

  @Override
  public int getAttempt() {
    return attempt;
  }

  @Override
  public void recordGfeMetadata(@Nullable Long latency, @Nullable Throwable throwable) {
    Attributes attributes =
        baseAttributes.toBuilder().put(BIGTABLE_STATUS, Util.extractStatus(throwable)).build();
    if (latency != null) {
      recorder.recordServerLatencies(latency, attributes);
      recorder.recordConnectivityErrorCount(0, attributes);
    } else {
      recorder.recordConnectivityErrorCount(1L, attributes);
    }
  }

  @Override
  public void batchRequestThrottled(long totalThrottledMs) {
    recorder.recordClientBlockingLatencies(totalThrottledMs, baseAttributes);
  }
}
