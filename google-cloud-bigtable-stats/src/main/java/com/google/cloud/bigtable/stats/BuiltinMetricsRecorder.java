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
package com.google.cloud.bigtable.stats;

import com.google.api.core.InternalApi;
import com.google.api.gax.tracing.ApiTracerFactory.OperationType;
import com.google.api.gax.tracing.SpanName;
import io.opencensus.stats.MeasureMap;
import io.opencensus.stats.StatsRecorder;
import io.opencensus.tags.TagContext;
import io.opencensus.tags.TagContextBuilder;
import io.opencensus.tags.TagKey;
import io.opencensus.tags.TagValue;
import io.opencensus.tags.Tagger;
import io.opencensus.tags.Tags;
import java.util.Map;

/** Add built-in metrics to the measure map * */
@InternalApi("For internal use only")
public class BuiltinMetricsRecorder {

  private final OperationType operationType;

  private final Tagger tagger;
  private final StatsRecorder statsRecorder;
  private final TagContext parentContext;
  private final SpanName spanName;
  private final Map<String, String> statsAttributes;

  private MeasureMap attemptLevelNoStreaming;
  private MeasureMap attemptLevelWithStreaming;
  private MeasureMap operationLevelNoStreaming;
  private MeasureMap operationLevelWithStreaming;

  public BuiltinMetricsRecorder(
      OperationType operationType,
      SpanName spanName,
      Map<String, String> statsAttributes,
      StatsWrapper builtinMetricsWrapper) {
    this.operationType = operationType;
    this.tagger = Tags.getTagger();
    this.statsRecorder = builtinMetricsWrapper.getStatsRecorder();
    this.spanName = spanName;
    this.parentContext = tagger.getCurrentTagContext();
    this.statsAttributes = statsAttributes;

    this.attemptLevelNoStreaming = statsRecorder.newMeasureMap();
    this.attemptLevelWithStreaming = statsRecorder.newMeasureMap();
    this.operationLevelNoStreaming = statsRecorder.newMeasureMap();
    this.operationLevelWithStreaming = statsRecorder.newMeasureMap();
  }

  public void recordAttemptLevelWithoutStreaming(
      String status, String tableId, String zone, String cluster) {
    TagContextBuilder tagCtx =
        newTagContextBuilder(tableId, zone, cluster)
            .putLocal(BuiltinMeasureConstants.STATUS, TagValue.create(status));

    attemptLevelNoStreaming.record(tagCtx.build());
  }

  public void recordAttemptLevelWithStreaming(
      String status, String tableId, String zone, String cluster) {
    TagContextBuilder tagCtx =
        newTagContextBuilder(tableId, zone, cluster)
            .putLocal(BuiltinMeasureConstants.STATUS, TagValue.create(status));

    if (operationType == OperationType.ServerStreaming
        && spanName.getMethodName().equals("ReadRows")) {
      tagCtx.putLocal(BuiltinMeasureConstants.STREAMING, TagValue.create("true"));
    } else {
      tagCtx.putLocal(BuiltinMeasureConstants.STREAMING, TagValue.create("false"));
    }

    attemptLevelWithStreaming.record(tagCtx.build());
  }

  public void recordOperationLevelWithoutStreaming(
      String status, String tableId, String zone, String cluster) {
    TagContextBuilder tagCtx =
        newTagContextBuilder(tableId, zone, cluster)
            .putLocal(BuiltinMeasureConstants.STATUS, TagValue.create(status));

    operationLevelNoStreaming.record(tagCtx.build());
  }

  public void recordOperationLevelWithStreaming(
      String status, String tableId, String zone, String cluster) {
    TagContextBuilder tagCtx =
        newTagContextBuilder(tableId, zone, cluster)
            .putLocal(BuiltinMeasureConstants.STATUS, TagValue.create(status));

    if (operationType == OperationType.ServerStreaming
        && spanName.getMethodName().equals("ReadRows")) {
      tagCtx.putLocal(BuiltinMeasureConstants.STREAMING, TagValue.create("true"));
    } else {
      tagCtx.putLocal(BuiltinMeasureConstants.STREAMING, TagValue.create("false"));
    }

    operationLevelWithStreaming.record(tagCtx.build());
  }

  public void recordOperationLatencies(long operationLatency) {
    operationLevelWithStreaming.put(BuiltinMeasureConstants.OPERATION_LATENCIES, operationLatency);
  }

  public void recordAttemptLatency(long attemptLatency) {
    attemptLevelWithStreaming.put(BuiltinMeasureConstants.ATTEMPT_LATENCIES, attemptLatency);
  }

  public void recordRetryCount(int attemptCount) {
    operationLevelNoStreaming.put(BuiltinMeasureConstants.RETRY_COUNT, attemptCount);
  }

  public void recordApplicationLatency(
      long applicationLatency, String tableId, String zone, String cluster) {
    MeasureMap measures =
        statsRecorder
            .newMeasureMap()
            .put(BuiltinMeasureConstants.APPLICATION_LATENCIES, applicationLatency);

    TagContextBuilder tagCtx = newTagContextBuilder(tableId, zone, cluster);
    if (operationType == OperationType.ServerStreaming
        && spanName.getMethodName().equals("ReadRows")) {
      tagCtx.putLocal(BuiltinMeasureConstants.STREAMING, TagValue.create("true"));
    } else {
      tagCtx.putLocal(BuiltinMeasureConstants.STREAMING, TagValue.create("false"));
    }

    measures.record(tagCtx.build());
  }

  public void recordFirstResponseLatency(long firstResponseLatency) {
    operationLevelNoStreaming.put(
        BuiltinMeasureConstants.FIRST_RESPONSE_LATENCIES, firstResponseLatency);
  }

  public void recordGfeLatencies(long serverLatency) {
    attemptLevelWithStreaming.put(BuiltinMeasureConstants.SERVER_LATENCIES, serverLatency);
  }

  public void recordGfeMissingHeaders(long connectivityErrors) {
    attemptLevelNoStreaming.put(
        BuiltinMeasureConstants.CONNECTIVITY_ERROR_COUNT, connectivityErrors);
  }

  public void recordBatchRequestThrottled(
      long throttledTimeMs, String tableId, String zone, String cluster) {
    MeasureMap measures =
        statsRecorder
            .newMeasureMap()
            .put(BuiltinMeasureConstants.THROTTLING_LATENCIES, throttledTimeMs);
    measures.record(newTagContextBuilder(tableId, zone, cluster).build());
  }

  private TagContextBuilder newTagContextBuilder(String tableId, String zone, String cluster) {
    TagContextBuilder tagContextBuilder =
        tagger
            .toBuilder(parentContext)
            .putLocal(BuiltinMeasureConstants.CLIENT_NAME, TagValue.create("bigtable-java"))
            .putLocal(BuiltinMeasureConstants.METHOD, TagValue.create(spanName.toString()))
            .putLocal(BuiltinMeasureConstants.TABLE, TagValue.create(tableId))
            .putLocal(BuiltinMeasureConstants.ZONE, TagValue.create(zone))
            .putLocal(BuiltinMeasureConstants.CLUSTER, TagValue.create(cluster));
    for (Map.Entry<String, String> entry : statsAttributes.entrySet()) {
      tagContextBuilder.putLocal(TagKey.create(entry.getKey()), TagValue.create(entry.getValue()));
    }
    return tagContextBuilder;
  }
}
