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

import com.google.api.core.InternalApi;
import com.google.api.gax.tracing.ApiTracer;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.BaseApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.cloud.bigtable.stats.StatsRecorderWrapper;
import com.google.cloud.bigtable.stats.StatsWrapper;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;

/**
 * {@link ApiTracerFactory} that will generate OpenCensus metrics by using the {@link ApiTracer}
 * api.
 */
@InternalApi("For internal use only")
public class BuiltinMetricsTracerFactory extends BaseApiTracerFactory {

  private final ImmutableMap<String, String> statsAttributes;
  private final StatsWrapper statsWrapper;
  private final StatsRecorderWrapper statsRecorderWrapper;

  public static BuiltinMetricsTracerFactory create(
      StatsWrapper statsWrapper, ImmutableMap<String, String> statsAttributes) {
    return new BuiltinMetricsTracerFactory(statsWrapper, statsAttributes, null);
  }

  @VisibleForTesting
  static BuiltinMetricsTracerFactory create(
      StatsWrapper statsWrapper,
      ImmutableMap<String, String> statsAttributes,
      StatsRecorderWrapper recorder) {
    return new BuiltinMetricsTracerFactory(statsWrapper, statsAttributes, recorder);
  }

  private BuiltinMetricsTracerFactory(
      StatsWrapper statsWrapper,
      ImmutableMap<String, String> statsAttributes,
      StatsRecorderWrapper recorder) {
    this.statsAttributes = statsAttributes;
    this.statsWrapper = statsWrapper;
    this.statsRecorderWrapper = recorder;
  }

  @Override
  public ApiTracer newTracer(ApiTracer parent, SpanName spanName, OperationType operationType) {
    return new BuiltinMetricsTracer(
        operationType, spanName, statsAttributes, statsWrapper, statsRecorderWrapper);
  }
}
