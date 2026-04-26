/*
 * Copyright 2025 Google LLC
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
package com.google.cloud.kafka.connect.bigtable.tracing;

import com.google.cloud.kafka.connect.bigtable.BigtableSinkConnector;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import java.util.Collections;
import java.util.Optional;
import org.apache.kafka.connect.header.Header;
import org.apache.kafka.connect.sink.SinkRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageTracer {
  private static final String KAFKA_HEADER_NAME = "traceparent";
  private static final Logger logger = LoggerFactory.getLogger(MessageTracer.class);
  private static final OpenTelemetry otel = GlobalOpenTelemetry.get();

  private static Context extractParentContext(SinkRecord record) {
    Object traceparentHeaderValue =
        Optional.ofNullable(record)
            .map(SinkRecord::headers)
            .map(hs -> hs.lastWithName(KAFKA_HEADER_NAME))
            .map(Header::value)
            .orElse(null);
    String traceparent;
    if (traceparentHeaderValue instanceof String) {
      traceparent = (String) traceparentHeaderValue;
    } else {
      logger.warn(
          "Parent not found for '{}' header value '{}'", KAFKA_HEADER_NAME, traceparentHeaderValue);
      return null;
    }
    // https://github.com/open-telemetry/opentelemetry-java-instrumentation/discussions/4546#discussioncomment-1572327
    return W3CTraceContextPropagator.getInstance()
        .extract(
            Context.root(),
            traceparent,
            new TextMapGetter<>() {
              @Override
              public Iterable<String> keys(String carrier) {
                return Collections.singleton(KAFKA_HEADER_NAME);
              }

              @Override
              public String get(String carrier, String key) {
                return key.equals(KAFKA_HEADER_NAME) ? carrier : null;
              }
            });
  }

  public static Span getRecordSpan(SinkRecord record, String spanName) {
    Tracer tracer = otel.getTracer(BigtableSinkConnector.class.getName());
    SpanBuilder spanBuilder = tracer.spanBuilder(spanName);
    Context parentContext = extractParentContext(record);
    if (parentContext != null) {
      spanBuilder.setParent(parentContext);
    }
    return spanBuilder.startSpan();
  }
}
