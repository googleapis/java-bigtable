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

import com.google.api.core.InternalApi;
import com.google.api.gax.tracing.ApiTracer;
import com.google.api.gax.tracing.BaseApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.bigtable.veneer.repackaged.io.opencensus.stats.StatsRecorder;
import com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagKey;
import com.google.bigtable.veneer.repackaged.io.opencensus.tags.TagValue;
import com.google.bigtable.veneer.repackaged.io.opencensus.tags.Tagger;
import com.google.common.collect.ImmutableMap;

@InternalApi("For internal use only")
public class BuiltinMetricsTracerFactory extends BaseApiTracerFactory {
  private final Tagger tagger;
  private final StatsRecorder statsRecorder;
  private final ImmutableMap<TagKey, TagValue> statsAttributes;

  public static BuiltinMetricsTracerFactory create(
      Tagger tagger, StatsRecorder statsRecorder, ImmutableMap<TagKey, TagValue> statsAttributes) {
    return new BuiltinMetricsTracerFactory(tagger, statsRecorder, statsAttributes);
  }

  private BuiltinMetricsTracerFactory(
      Tagger tagger, StatsRecorder statsRecorder, ImmutableMap<TagKey, TagValue> statsAttributes) {
    this.tagger = tagger;
    this.statsRecorder = statsRecorder;
    this.statsAttributes = statsAttributes;
  }

  @Override
  public ApiTracer newTracer(ApiTracer parent, SpanName spanName, OperationType operationType) {
    return new BuiltinMetricsTracer(
        operationType, tagger, statsRecorder, spanName, statsAttributes);
  }
}
