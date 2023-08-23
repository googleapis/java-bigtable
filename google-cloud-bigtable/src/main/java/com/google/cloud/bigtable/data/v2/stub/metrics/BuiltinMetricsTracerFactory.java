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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsAttributes.SCOPE;

import com.google.api.core.InternalApi;
import com.google.api.gax.tracing.ApiTracer;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.BaseApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import java.io.IOException;

/**
 * {@link ApiTracerFactory} that will generate OpenTelemetry metrics by using the {@link ApiTracer}
 * api.
 */
@InternalApi("For internal use only")
public class BuiltinMetricsTracerFactory extends BaseApiTracerFactory {

  private final BigtableMetricsRecorder bigtableMetricsRecorder;
  private final Attributes attributes;

  public static BuiltinMetricsTracerFactory create(
      OpenTelemetry openTelemetry, Attributes attributes) throws IOException {
    return new BuiltinMetricsTracerFactory(openTelemetry, attributes);
  }

  BuiltinMetricsTracerFactory(OpenTelemetry openTelemetry, Attributes attributes) {
    this.attributes = attributes;
    bigtableMetricsRecorder = new BuiltinInMetricsRecorder(openTelemetry.getMeter(SCOPE));
  }

  @Override
  public ApiTracer newTracer(ApiTracer parent, SpanName spanName, OperationType operationType) {
    return new BuiltinMetricsTracer(operationType, spanName, bigtableMetricsRecorder, attributes);
  }
}
