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

import com.google.api.gax.tracing.BaseApiTracer;

import com.google.api.gax.tracing.ApiTracerFactory.OperationType;
import com.google.api.gax.tracing.SpanName;
import com.google.bigtable.repackaged.io.opencensus.stats.MeasureMap;
import com.google.bigtable.repackaged.io.opencensus.stats.StatsRecorder;
import com.google.bigtable.repackaged.io.opencensus.tags.TagContext;
import com.google.bigtable.repackaged.io.opencensus.tags.TagContextBuilder;
import com.google.bigtable.repackaged.io.opencensus.tags.TagKey;
import com.google.bigtable.repackaged.io.opencensus.tags.TagValue;
import com.google.bigtable.repackaged.io.opencensus.tags.Tagger;
import com.google.cloud.bigtable.data.v2.stub.metrics.Util;
import com.google.common.base.Stopwatch;
import org.threeten.bp.Duration;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class BuiltinMetricsTracer extends BaseApiTracer {

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
            public void close() {
            }
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
    public void attemptStarted(Object request, int attemptNumber) {
        attemptCount++;
        attemptTimer = Stopwatch.createStarted();
        tableId = Util.extractTableId(request);
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
    public void responseReceived() {
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

    public void addZone(String zone) {
        this.zone = zone;
    }

    public void addCluster(String cluster) {
        this.cluster = cluster;
    }

    private void recordOperationCompletion(@Nullable Throwable throwable) {
        if (!opFinished.compareAndSet(false, true)) {
            return;
        }
        operationTimer.stop();

        long operationLatency = operationTimer.elapsed(TimeUnit.MILLISECONDS);

        MeasureMap measures =
                statsRecorder.newMeasureMap()
                        .put(BuiltinMeasureConstants.OPERATION_LATENCIES, operationLatency)
                        .put(BuiltinMeasureConstants.RETRY_COUNT, attemptCount);

        if (operationType == OperationType.ServerStreaming && spanName.getMethodName().equals("ReadRows")) {
            measures.put(BuiltinMeasureConstants.FIRST_RESPONSE_LATENCIES, firstResponsePerOpTimer.elapsed(TimeUnit.MILLISECONDS));
        }

        TagContextBuilder tagCtx = newTagCtxBuilder()
                .putLocal(BuiltinMeasureConstants.STATUS, TagValue.create(Util.extractStatus(throwable)));

        measures.record(tagCtx.build());
    }

    private void recordAttemptCompletion(@Nullable Throwable throwable) {
        MeasureMap measures =
                statsRecorder.newMeasureMap()
                        .put(BuiltinMeasureConstants.ATTEMPT_LATENCIES, attemptTimer.elapsed(TimeUnit.MILLISECONDS));
        TagContextBuilder tagCtx = newTagCtxBuilder()
                .putLocal(BuiltinMeasureConstants.STATUS, TagValue.create(Util.extractStatus(throwable)));

        measures.record(tagCtx.build());
    }

    private TagContextBuilder newTagCtxBuilder() {
        TagContextBuilder tagContextBuilder =
                tagger.toBuilder(parentContext)
                        .putLocal(BuiltinMeasureConstants.METHOD, TagValue.create(spanName.getMethodName()))
                        .putLocal(BuiltinMeasureConstants.TABLE, TagValue.create(tableId))
                        .putLocal(BuiltinMeasureConstants.ZONE, TagValue.create(zone))
                        .putLocal(BuiltinMeasureConstants.CLUSTER, TagValue.create(cluster));
        for (Map.Entry<TagKey, TagValue> entry : statsAttributes.entrySet()) {
            tagContextBuilder.putLocal(entry.getKey(), entry.getValue());
        }
        return tagContextBuilder;
    }
}
