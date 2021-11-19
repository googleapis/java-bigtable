/*
 * Copyright 2021 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.metrics.builtin;

import com.google.api.gax.tracing.ApiTracerFactory.OperationType;
import com.google.api.gax.tracing.SpanName;
import com.google.bigtable.veneer.repackaged.io.opencensus.stats.MeasureMap;
import com.google.bigtable.veneer.repackaged.io.opencensus.stats.StatsRecorder;
import com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagContext;
import com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagContextBuilder;
import com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.tags.Tagger;
import com.google.cloud.bigtable.data.v2.stub.metrics.BigtableTracer;
import com.google.cloud.bigtable.data.v2.stub.metrics.Util;
import com.google.common.base.Stopwatch;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import javax.annotation.Nullable;
import org.threeten.bp.Duration;

public class BuiltinMetricsTracer extends BigtableTracer {

  private final OperationType operationType;

  private final Tagger tagger;
  private final StatsRecorder statsRecorder;

  private final TagContext parentContext;
  private final SpanName spanName;
  private final Map<TagKey, TagValue> statsAttributes;

  // Operation level metrics
  private final AtomicBoolean opFinished = new AtomicBoolean();
  private final Stopwatch operationTimer = Stopwatch.createStarted();
  private final Stopwatch firstResponsePerOpTimer = Stopwatch.createStarted();

  // Attempt level metrics
  private int attemptCount = 0;
  private Stopwatch attemptTimer;
  private volatile int attempt;

  // Total latency on the server side
  private final Stopwatch serverLatencyTimer = Stopwatch.createUnstarted();
  private final AtomicLong totalServerLatency = new AtomicLong(0);

  // Monitored resource labels
  private String tableId = "undefined";
  private String zone = "undefined";
  private String cluster = "undefined";

  BuiltinMetricsTracer(
      OperationType operationType,
      Tagger tagger,
      StatsRecorder statsRecorder,
      SpanName spanName,
      Map<TagKey, TagValue> statsAttributes) {
    this.operationType = operationType;
    this.tagger = tagger;
    this.statsRecorder = statsRecorder;
    this.spanName = spanName;
    this.parentContext = tagger.getCurrentTagContext();
    this.statsAttributes = statsAttributes;
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
    serverLatencyTimer.start();
    if (request != null) {
      tableId = Util.extractTableId(request);
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
    if (!serverLatencyTimer.isRunning()) {
      serverLatencyTimer.start();
    }
  }

  @Override
  public void responseReceived() {
    totalServerLatency.addAndGet(serverLatencyTimer.elapsed(TimeUnit.MILLISECONDS));
    serverLatencyTimer.reset();
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
    MeasureMap measures = statsRecorder.newMeasureMap();
    if (latency != null) {
      measures
          .put(BuiltinMeasureConstants.SERVER_LATENCIES, latency)
          .put(BuiltinMeasureConstants.CONNECTIVITY_ERROR_COUNT, 0L);
    } else {
      measures.put(BuiltinMeasureConstants.CONNECTIVITY_ERROR_COUNT, 1L);
    }
    measures.record(
        newTagCtxBuilder()
            .putLocal(
                BuiltinMeasureConstants.STATUS, TagValue.create(Util.extractStatus(throwable)))
            .build());
  }

  @Override
  public void setLocations(String zone, String cluster) {
    if (zone != null) {
      this.zone = zone;
    }
    if (cluster != null) {
      this.cluster = cluster;
    }
  }

  private void recordOperationCompletion(@Nullable Throwable throwable) {
    if (!opFinished.compareAndSet(false, true)) {
      return;
    }
    operationTimer.stop();

    long operationLatency = operationTimer.elapsed(TimeUnit.MILLISECONDS);

    MeasureMap measures =
        statsRecorder
            .newMeasureMap()
            .put(BuiltinMeasureConstants.OPERATION_LATENCIES, operationLatency)
            .put(BuiltinMeasureConstants.RETRY_COUNT, attemptCount)
            .put(
                BuiltinMeasureConstants.APPLICATION_LATENCIES,
                operationLatency - totalServerLatency.get());

    if (operationType == OperationType.ServerStreaming
        && spanName.getMethodName().equals("ReadRows")) {
      measures.put(
          BuiltinMeasureConstants.FIRST_RESPONSE_LATENCIES,
          firstResponsePerOpTimer.elapsed(TimeUnit.MILLISECONDS));
    }

    TagContextBuilder tagCtx =
        tagContextBuilderWithLocations()
            .putLocal(
                BuiltinMeasureConstants.STATUS, TagValue.create(Util.extractStatus(throwable)));

    if (spanName.getMethodName().equals("ReadRows")) {
      // TODO: what's the streaming tag?
      tagCtx.putLocal(BuiltinMeasureConstants.STREAMING, TagValue.create("true"));
    }

    measures.record(tagCtx.build());
  }

  private void recordAttemptCompletion(@Nullable Throwable throwable) {
    totalServerLatency.addAndGet(serverLatencyTimer.elapsed(TimeUnit.MILLISECONDS));
    serverLatencyTimer.reset();
    MeasureMap measures =
        statsRecorder
            .newMeasureMap()
            .put(
                BuiltinMeasureConstants.ATTEMPT_LATENCIES,
                attemptTimer.elapsed(TimeUnit.MILLISECONDS));
    TagContextBuilder tagCtx =
        tagContextBuilderWithLocations()
            .putLocal(
                BuiltinMeasureConstants.STATUS, TagValue.create(Util.extractStatus(throwable)));

    measures.record(tagCtx.build());
  }

  private TagContextBuilder newTagCtxBuilder() {
    TagContextBuilder tagContextBuilder =
        tagger
            .toBuilder(parentContext)
            .putLocal(BuiltinMeasureConstants.CLIENT_NAME, TagValue.create("bigtable-java"))
            .putLocal(BuiltinMeasureConstants.METHOD, TagValue.create(spanName.toString()))
            .putLocal(BuiltinMeasureConstants.TABLE, TagValue.create(tableId));
    for (Map.Entry<TagKey, TagValue> entry : statsAttributes.entrySet()) {
      tagContextBuilder.putLocal(entry.getKey(), entry.getValue());
    }
    return tagContextBuilder;
  }

  private TagContextBuilder tagContextBuilderWithLocations() {
    return newTagCtxBuilder()
        .putLocal(BuiltinMeasureConstants.ZONE, TagValue.create(zone))
        .putLocal(BuiltinMeasureConstants.CLUSTER, TagValue.create(cluster));
  }
}
