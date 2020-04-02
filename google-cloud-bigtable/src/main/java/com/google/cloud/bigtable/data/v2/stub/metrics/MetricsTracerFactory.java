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
import com.google.api.gax.tracing.ApiTracer;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.common.collect.ImmutableMap;
import io.opencensus.stats.StatsRecorder;
import io.opencensus.tags.TagKey;
import io.opencensus.tags.TagValue;
import io.opencensus.tags.Tagger;

/**
 * {@link ApiTracerFactory} that will generate OpenCensus metrics by using the {@link ApiTracer}
 * api.
 */
@InternalApi("For internal use only")
public class MetricsTracerFactory implements ApiTracerFactory {
  private final Tagger tagger;
  private final StatsRecorder stats;
  private final ImmutableMap<TagKey, TagValue> statsAttributes;

  public static MetricsTracerFactory create(
      Tagger tagger, StatsRecorder stats, ImmutableMap<TagKey, TagValue> statsAttributes) {
    return new MetricsTracerFactory(tagger, stats, statsAttributes);
  }

  private MetricsTracerFactory(
      Tagger tagger, StatsRecorder stats, ImmutableMap<TagKey, TagValue> statsAttributes) {
    this.tagger = tagger;
    this.stats = stats;
    this.statsAttributes = statsAttributes;
  }

  @Override
  public ApiTracer newTracer(ApiTracer parent, SpanName spanName, OperationType operationType) {
    return new MetricsTracer(operationType, tagger, stats, spanName, statsAttributes);
  }
}
