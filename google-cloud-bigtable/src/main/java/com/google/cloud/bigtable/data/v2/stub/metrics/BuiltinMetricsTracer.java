/*
 * Copyright 2022 Google LLC
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

import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.cloud.bigtable.stats.StatsRecorderWrapper;
import com.google.cloud.bigtable.stats.StatsWrapper;
import com.google.common.base.Stopwatch;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Nullable;
import org.threeten.bp.Duration;

/**
 * A {@link BigtableTracer} that records built-in metrics and publish under the
 * bigtable.googleapis.com/client namespace
 */
class BuiltinMetricsTracer extends BigtableTracer {

  private final StatsRecorderWrapper recorder;

  private final ApiTracerFactory.OperationType operationType;
  private final SpanName spanName;

  // Operation level metrics
  private final AtomicBoolean opFinished = new AtomicBoolean();
  private final Stopwatch operationTimer = Stopwatch.createStarted();
  private final Stopwatch firstResponsePerOpTimer = Stopwatch.createStarted();

  // Attempt level metrics
  private int attemptCount = 0;
  private Stopwatch attemptTimer;
  private volatile int attempt = 0;

  // Total application latency
  private final Stopwatch applicationLatencyTimer = Stopwatch.createUnstarted();
  private final AtomicLong totalApplicationLatency = new AtomicLong(0);

  // Monitored resource labels
  private String tableId = "undefined";
  private String zone = "undefined";
  private String cluster = "undefined";

  // gfe stats
  private AtomicLong gfeMissingHeaders = new AtomicLong(0);

  BuiltinMetricsTracer(
      ApiTracerFactory.OperationType operationType,
      SpanName spanName,
      Map<String, String> attributes,
      StatsWrapper statsWrapper,
      @Nullable StatsRecorderWrapper statsRecorderWrapper) {
    this.operationType = operationType;
    this.spanName = spanName;
    if (statsRecorderWrapper != null) {
      // A workaround for test to pass in a mock StatsRecorderWrapper
      this.recorder = statsRecorderWrapper;
    } else {
      this.recorder = new StatsRecorderWrapper(operationType, spanName, attributes, statsWrapper);
    }
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
  public void operationFailed(Throwable error) {
    recordOperationCompletion(error);
  }

  @Override
  public void attemptStarted(int attemptNumber) {
    attemptStarted(null, attemptNumber);
  }

  @Override
  public void attemptStarted(Object request, int attemptNumber) {
    this.attempt = attemptNumber;
    attemptCount++;
    attemptTimer = Stopwatch.createStarted();
    if (request != null) {
      this.tableId = Util.extractTableId(request);
    }
    if (applicationLatencyTimer.isRunning()) {
      totalApplicationLatency.addAndGet(applicationLatencyTimer.elapsed(TimeUnit.MILLISECONDS));
      applicationLatencyTimer.reset();
    }
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
  public void attemptFailed(Throwable error, Duration delay) {
    if (!applicationLatencyTimer.isRunning()) {
      applicationLatencyTimer.start();
    }
    recordAttemptCompletion(error);
  }

  @Override
  public void attemptFailedRetriesExhausted(Throwable error) {
    super.attemptFailedRetriesExhausted(error);
  }

  @Override
  public void attemptPermanentFailure(Throwable error) {
    super.attemptPermanentFailure(error);
  }

  @Override
  public void lroStartFailed(Throwable error) {
    super.lroStartFailed(error);
  }

  @Override
  public void lroStartSucceeded() {
    super.lroStartSucceeded();
  }

  @Override
  public void onRequest() {
    if (applicationLatencyTimer.isRunning()) {
      totalApplicationLatency.addAndGet(applicationLatencyTimer.elapsed(TimeUnit.MILLISECONDS));
      applicationLatencyTimer.reset();
    }
  }

  @Override
  public void responseReceived() {
    if (!applicationLatencyTimer.isRunning()) {
      applicationLatencyTimer.start();
    }
    if (firstResponsePerOpTimer.isRunning()) {
      firstResponsePerOpTimer.stop();
    }
  }

  @Override
  public void requestSent() {
    super.requestSent();
  }

  @Override
  public void batchRequestSent(long elementCount, long requestSize) {
    super.batchRequestSent(elementCount, requestSize);
  }

  @Override
  public int getAttempt() {
    return attempt;
  }

  @Override
  public void recordGfeMetadata(@Nullable Long latency, @Nullable Throwable throwable) {
    // Record the metrics and put in the map after the attempt is done, so we can have cluster and
    // zone information
    if (latency != null) {
      recorder.putGfeLatencies(latency);
    } else {
      gfeMissingHeaders.incrementAndGet();
    }
    recorder.putGfeMissingHeaders(gfeMissingHeaders.get());
  }

  @Override
  public void setLocations(String zone, String cluster) {
    this.zone = zone;
    this.cluster = cluster;
  }

  @Override
  public void batchRequestThrottled(long throttledTimeMs) {
    recorder.putBatchRequestThrottled(throttledTimeMs);
  }

  private void recordOperationCompletion(@Nullable Throwable status) {
    if (!opFinished.compareAndSet(false, true)) {
      return;
    }
    operationTimer.stop();

    recorder.putRetryCount(attemptCount);

    if (applicationLatencyTimer.isRunning()) {
      applicationLatencyTimer.stop();
      totalApplicationLatency.addAndGet(applicationLatencyTimer.elapsed(TimeUnit.MILLISECONDS));
    }
    recorder.putApplicationLatencies(totalApplicationLatency.get());

    recorder.putOperationLatencies(operationTimer.elapsed(TimeUnit.MILLISECONDS));

    if (operationType == ApiTracerFactory.OperationType.ServerStreaming
        && spanName.getMethodName().equals("ReadRows")) {
      recorder.putFirstResponseLatencies(firstResponsePerOpTimer.elapsed(TimeUnit.MILLISECONDS));
    }

    recorder.record(Util.extractStatus(status), tableId, zone, cluster);
  }

  private void recordAttemptCompletion(@Nullable Throwable status) {
    recorder.putAttemptLatencies(attemptTimer.elapsed(TimeUnit.MILLISECONDS));

    recorder.record(Util.extractStatus(status), tableId, zone, cluster);
  }
}
