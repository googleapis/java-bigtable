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

/** A wrapper to record built-in metrics */
@InternalApi("For internal use only")
public class StatsRecorderWrapper {

  private final OperationType operationType;

  private final Tagger tagger;
  private final StatsRecorder statsRecorder;
  private final TagContext parentContext;
  private final SpanName spanName;
  private final Map<String, String> statsAttributes;

  private MeasureMap attemptMeasureMap;
  private MeasureMap operationMeasureMap;

  public StatsRecorderWrapper(
      OperationType operationType,
      SpanName spanName,
      Map<String, String> statsAttributes,
      StatsRecorder statsRecorder) {
    this.operationType = operationType;
    this.tagger = Tags.getTagger();
    this.statsRecorder = statsRecorder;
    this.spanName = spanName;
    this.parentContext = tagger.getCurrentTagContext();
    this.statsAttributes = statsAttributes;

    this.attemptMeasureMap = statsRecorder.newMeasureMap();
    this.operationMeasureMap = statsRecorder.newMeasureMap();
  }

  public void recordOperation(String status, String tableId, String zone, String cluster) {
    TagContextBuilder tagCtx =
        newTagContextBuilder(tableId, zone, cluster)
            .putLocal(BuiltinMeasureConstants.STATUS, TagValue.create(status));

    boolean isStreaming = operationType == OperationType.ServerStreaming;
    tagCtx.putLocal(
        BuiltinMeasureConstants.STREAMING, TagValue.create(Boolean.toString(isStreaming)));

    operationMeasureMap.record(tagCtx.build());
    // Reinitialize a new map
    operationMeasureMap = statsRecorder.newMeasureMap();
  }

  public void recordAttempt(String status, String tableId, String zone, String cluster) {
    TagContextBuilder tagCtx =
        newTagContextBuilder(tableId, zone, cluster)
            .putLocal(BuiltinMeasureConstants.STATUS, TagValue.create(status));

    boolean isStreaming = operationType == OperationType.ServerStreaming;
    tagCtx.putLocal(
        BuiltinMeasureConstants.STREAMING, TagValue.create(Boolean.toString(isStreaming)));

    attemptMeasureMap.record(tagCtx.build());
    // Reinitialize a new map
    attemptMeasureMap = statsRecorder.newMeasureMap();
  }

  public void putOperationLatencies(long operationLatency) {
    operationMeasureMap.put(BuiltinMeasureConstants.OPERATION_LATENCIES, operationLatency);
  }

  public void putAttemptLatencies(long attemptLatency) {
    attemptMeasureMap.put(BuiltinMeasureConstants.ATTEMPT_LATENCIES, attemptLatency);
  }

  public void putRetryCount(int attemptCount) {
    operationMeasureMap.put(BuiltinMeasureConstants.RETRY_COUNT, attemptCount);
  }

  public void putApplicationLatencies(long applicationLatency) {
    operationMeasureMap.put(BuiltinMeasureConstants.APPLICATION_LATENCIES, applicationLatency);
  }

  public void putFirstResponseLatencies(long firstResponseLatency) {
    operationMeasureMap.put(BuiltinMeasureConstants.FIRST_RESPONSE_LATENCIES, firstResponseLatency);
  }

  public void putGfeLatencies(long serverLatency) {
    attemptMeasureMap.put(BuiltinMeasureConstants.SERVER_LATENCIES, serverLatency);
  }

  public void putGfeMissingHeaders(long connectivityErrors) {
    attemptMeasureMap.put(BuiltinMeasureConstants.CONNECTIVITY_ERROR_COUNT, connectivityErrors);
  }

  public void putClientBlockingLatencies(long clientBlockingLatency) {
    operationMeasureMap.put(BuiltinMeasureConstants.THROTTLING_LATENCIES, clientBlockingLatency);
  }

  private TagContextBuilder newTagContextBuilder(String tableId, String zone, String cluster) {
    TagContextBuilder tagContextBuilder =
        tagger
            .toBuilder(parentContext)
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
