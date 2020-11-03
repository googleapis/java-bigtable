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

import com.google.api.gax.tracing.SpanName;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import io.grpc.CallOptions;
import io.opencensus.stats.Measure.MeasureDouble;
import io.opencensus.stats.Measure.MeasureLong;
import io.opencensus.stats.MeasureMap;
import io.opencensus.stats.Stats;
import io.opencensus.stats.StatsRecorder;
import io.opencensus.tags.TagContextBuilder;
import io.opencensus.tags.TagKey;
import io.opencensus.tags.TagValue;
import io.opencensus.tags.Tagger;
import io.opencensus.tags.Tags;
import java.util.Map;
import javax.annotation.Nullable;

public class HeaderTracer {
  public static final CallOptions.Key<HeaderTracer> HEADER_TRACER_CONTEXT_KEY =
      CallOptions.Key.create("BigtableHeaderTracer");

  private Tagger tagger;
  private StatsRecorder stats;
  private Map<TagKey, TagValue> statsAttributes;

  public HeaderTracer() {
    this.tagger = Tags.getTagger();
    this.stats = Stats.getStatsRecorder();
    this.statsAttributes = ImmutableMap.of();
  }

  public void setTagger(Tagger tagger) {
    this.tagger = tagger;
  }

  public void setStats(StatsRecorder stats) {
    this.stats = stats;
  }

  public void setStatsAttributes(Map<TagKey, TagValue> statsAttributes) {
    this.statsAttributes = statsAttributes;
  }

  public void recordHeader(MeasureLong measure, long value, @Nullable SpanName spanName) {
    Preconditions.checkNotNull(measure, "Measure cannot be null.");
    MeasureMap measures = stats.newMeasureMap().put(measure, value);
    measures.record(newTagCtxBuilder(spanName).build());
  }

  public void recordHeader(MeasureDouble measure, double value, @Nullable SpanName spanName) {
    Preconditions.checkNotNull(measure, "Measure cannot be null.");
    MeasureMap measures = stats.newMeasureMap().put(measure, value);
    measures.record(newTagCtxBuilder(spanName).build());
  }

  private TagContextBuilder newTagCtxBuilder(@Nullable SpanName spanName) {
    TagContextBuilder tagContextBuilder = tagger.currentBuilder();
    if (spanName != null) {
      tagContextBuilder.putLocal(
          RpcMeasureConstants.BIGTABLE_OP, TagValue.create(spanName.toString()));
    }
    // Copy client level tags in
    for (Map.Entry<TagKey, TagValue> entry : statsAttributes.entrySet()) {
      tagContextBuilder.putLocal(entry.getKey(), entry.getValue());
    }
    return tagContextBuilder;
  }
}
