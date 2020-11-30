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

import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.grpc.Metadata;
import io.opencensus.stats.MeasureMap;
import io.opencensus.stats.Stats;
import io.opencensus.stats.StatsRecorder;
import io.opencensus.tags.TagContextBuilder;
import io.opencensus.tags.TagKey;
import io.opencensus.tags.TagValue;
import io.opencensus.tags.Tagger;
import io.opencensus.tags.Tags;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@InternalApi
@AutoValue
public abstract class HeaderTracer {

  public static final Metadata.Key<String> SERVER_TIMING_HEADER_KEY =
      Metadata.Key.of("server-timing", Metadata.ASCII_STRING_MARSHALLER);
  public static final Pattern SERVER_TIMING_HEADER_PATTERN = Pattern.compile(".*dur=(?<dur>\\d+)");

  @AutoValue.Builder
  public abstract static class Builder {
    // <editor-fold desc="Public API">
    public abstract Builder setTagger(@Nonnull Tagger tagger);

    public abstract Builder setStats(@Nonnull StatsRecorder stats);

    public abstract Builder setStatsAttributes(@Nonnull Map<TagKey, TagValue> statsAttributes);

    public abstract Tagger getTagger();

    public abstract StatsRecorder getStats();

    public abstract Map<TagKey, TagValue> getStatsAttributes();

    abstract HeaderTracer autoBuild();

    public HeaderTracer build() {
      HeaderTracer headerTracer = autoBuild();
      Preconditions.checkNotNull(headerTracer.getStats(), "StatsRecorder must be set");
      Preconditions.checkNotNull(headerTracer.getTagger(), "Tagger must be set");
      Preconditions.checkNotNull(headerTracer.getStatsAttributes(), "Stats attributes must be set");
      return headerTracer;
    }
    // </editor-fold>
  }

  public abstract Tagger getTagger();

  public abstract StatsRecorder getStats();

  public abstract Map<TagKey, TagValue> getStatsAttributes();

  public void recordGfeMetrics(@Nonnull Metadata metadata, String spanName) {
    MeasureMap measures = getStats().newMeasureMap();
    if (metadata.get(SERVER_TIMING_HEADER_KEY) != null) {
      String serverTiming = metadata.get(SERVER_TIMING_HEADER_KEY);
      Matcher matcher = SERVER_TIMING_HEADER_PATTERN.matcher(serverTiming);
      measures.put(RpcMeasureConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT, 0L);
      if (matcher.find()) {
        long latency = Long.valueOf(matcher.group("dur"));
        measures.put(RpcMeasureConstants.BIGTABLE_GFE_LATENCY, latency);
      }
    } else {
      measures.put(RpcMeasureConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT, 1L);
    }
    measures.record(newTagCtxBuilder(spanName).build());
  }

  private TagContextBuilder newTagCtxBuilder(@Nullable String span) {
    TagContextBuilder tagContextBuilder = getTagger().currentBuilder();
    if (span != null) {
      tagContextBuilder.putLocal(RpcMeasureConstants.BIGTABLE_OP, TagValue.create(span));
    }
    // Copy client level tags in
    for (Map.Entry<TagKey, TagValue> entry : getStatsAttributes().entrySet()) {
      tagContextBuilder.putLocal(entry.getKey(), entry.getValue());
    }
    return tagContextBuilder;
  }

  public static Builder newBuilder() {
    return new AutoValue_HeaderTracer.Builder()
        .setTagger(Tags.getTagger())
        .setStats(Stats.getStatsRecorder())
        .setStatsAttributes(Collections.<TagKey, TagValue>emptyMap());
  }

  public abstract Builder toBuilder();

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("stats", getStats())
        .add("tagger", getTagger())
        .add("statsAttributes", getStatsAttributes())
        .toString();
  }
}
