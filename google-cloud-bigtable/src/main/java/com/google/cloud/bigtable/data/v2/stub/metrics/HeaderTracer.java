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
import com.google.common.base.MoreObjects;
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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@BetaApi
public class HeaderTracer {
  public static final CallOptions.Key<HeaderTracer> HEADER_TRACER_CONTEXT_KEY =
      CallOptions.Key.create("BigtableHeaderTracer");
  public static final CallOptions.Key<String> SPAN_NAME_CONTEXT_KEY =
      CallOptions.Key.create("BigtableSpanName");

  private Tagger tagger;
  private StatsRecorder stats;
  private Map<TagKey, TagValue> statsAttributes;

  private HeaderTracer(Builder builder) {
    tagger = builder.getTagger();
    stats = builder.getStats();
    statsAttributes = builder.getStatsAttributes();
  }

  public static class Builder {
    private Tagger tagger;
    private StatsRecorder stats;
    private Map<TagKey, TagValue> statsAttributes;

    private Builder() {
      tagger = Tags.getTagger();
      stats = Stats.getStatsRecorder();
      statsAttributes = ImmutableMap.of();
    }

    private Builder(HeaderTracer headerTracer) {
      tagger = headerTracer.tagger;
      stats = headerTracer.stats;
      statsAttributes = headerTracer.statsAttributes;
    }

    // <editor-fold desc="Public API">
    public Builder setTagger(@Nonnull Tagger tagger) {
      Preconditions.checkNotNull(tagger);
      this.tagger = tagger;
      return this;
    }

    public Builder setStats(@Nonnull StatsRecorder stats) {
      Preconditions.checkNotNull(stats);
      this.stats = stats;
      return this;
    }

    public Builder setStatsAttributes(@Nonnull Map<TagKey, TagValue> statsAttributes) {
      Preconditions.checkNotNull(statsAttributes);
      this.statsAttributes = statsAttributes;
      return this;
    }

    public Tagger getTagger() {
      return tagger;
    }

    public StatsRecorder getStats() {
      return stats;
    }

    public Map<TagKey, TagValue> getStatsAttributes() {
      return statsAttributes;
    }

    public HeaderTracer build() {
      Preconditions.checkNotNull(stats, "StatsRecorder must be set");
      Preconditions.checkNotNull(tagger, "Tagger must be set");
      Preconditions.checkNotNull(statsAttributes, "Stats attributes must be set");
      return new HeaderTracer(this);
    }
    // </editor-fold>
  }

  public Tagger getTagger() {
    return tagger;
  }

  public StatsRecorder getStats() {
    return stats;
  }

  public Map<TagKey, TagValue> getStatsAttributes() {
    return statsAttributes;
  }

  public void record(MeasureLong measure, long value, @Nullable String span) {
    Preconditions.checkNotNull(measure, "Measure cannot be null.");
    MeasureMap measures = stats.newMeasureMap().put(measure, value);
    measures.record(newTagCtxBuilder(span).build());
  }

  public void record(MeasureDouble measure, double value, @Nullable String span) {
    Preconditions.checkNotNull(measure, "Measure cannot be null.");
    MeasureMap measures = stats.newMeasureMap().put(measure, value);
    measures.record(newTagCtxBuilder(span).build());
  }

  private TagContextBuilder newTagCtxBuilder(@Nullable String span) {
    TagContextBuilder tagContextBuilder = tagger.currentBuilder();
    if (span != null) {
      tagContextBuilder.putLocal(RpcMeasureConstants.BIGTABLE_OP, TagValue.create(span));
    }
    // Copy client level tags in
    for (Map.Entry<TagKey, TagValue> entry : statsAttributes.entrySet()) {
      tagContextBuilder.putLocal(entry.getKey(), entry.getValue());
    }
    return tagContextBuilder;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("stats", stats)
        .add("tagger", tagger)
        .add("statsAttributes", stats)
        .toString();
  }
}
