/*
 * Copyright 2023 Google LLC
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

import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.ATTEMPTS_PER_OP_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.ATTEMPT_LATENCY_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.COMPLETED_OPS_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.GFE_LATENCY_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.GFE_MISSING_HEADER_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.OP_LATENCY_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.READ_ROWS_FIRST_ROW_LATENCY_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.THROTTLED_TIME_NAME;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;

public class MetricsTracerRecorder extends BigtableMetricsRecorder {
  private static final String MILLISECOND = "ms";
  private static final String COUNT = "1";

  private final LongHistogram opLatency;
  private final LongHistogram attemptLatencies;

  private final LongHistogram readFirstRowLatencies;
  private final LongCounter completedOps;

  private final LongCounter attemptsPerOp;

  private final LongHistogram gfeLatency;

  private final LongCounter gfeMissingHeaderCount;

  private final LongHistogram batchThrottledTime;

  MetricsTracerRecorder(Meter meter) {
    opLatency =
        meter
            .histogramBuilder(OP_LATENCY_NAME)
            .ofLongs()
            .setDescription(
                "Time between request being sent to last row received, "
                    + "or terminal error of the last retry attempt.")
            .setUnit(MILLISECOND)
            .build();

    attemptLatencies =
        meter
            .histogramBuilder(ATTEMPT_LATENCY_NAME)
            .ofLongs()
            .setDescription("Attempt latency in msecs")
            .setUnit(MILLISECOND)
            .build();

    readFirstRowLatencies =
        meter
            .histogramBuilder(READ_ROWS_FIRST_ROW_LATENCY_NAME)
            .ofLongs()
            .setDescription("Latency to receive the first row in a ReadRows stream")
            .setUnit(MILLISECOND)
            .build();

    completedOps =
        meter
            .counterBuilder(COMPLETED_OPS_NAME)
            .setDescription("Number of completed Bigtable client operations")
            .setUnit(COUNT)
            .build();

    attemptsPerOp =
        meter
            .counterBuilder(ATTEMPTS_PER_OP_NAME)
            .setDescription("Distribution of attempts per logical operation")
            .setUnit(COUNT)
            .build();

    gfeLatency =
        meter
            .histogramBuilder(GFE_LATENCY_NAME)
            .ofLongs()
            .setDescription(
                "Latency between Google's network receives an RPC and reads back the first byte of the response")
            .setUnit(MILLISECOND)
            .build();

    gfeMissingHeaderCount =
        meter
            .counterBuilder(GFE_MISSING_HEADER_NAME)
            .setDescription(
                "Number of RPC responses received without the server-timing header, most likely means that the RPC never reached Google's network")
            .setUnit(COUNT)
            .build();

    batchThrottledTime =
        meter
            .histogramBuilder(THROTTLED_TIME_NAME)
            .ofLongs()
            .setDescription("Total throttled time of a batch in msecs")
            .setUnit(MILLISECOND)
            .build();
  }

  @Override
  void recordOperationLatencies(long value, Attributes attributes) {
    opLatency.record(value, attributes);
    completedOps.add(1, attributes);
  }

  @Override
  void recordAttemptLatencies(long value, Attributes attributes) {
    attemptLatencies.record(value, attributes);
  }

  @Override
  void recordFirstResponseLatencies(long value, Attributes attributes) {
    readFirstRowLatencies.record(value, attributes);
  }

  @Override
  void recordServerLatencies(long value, Attributes attributes) {
    gfeLatency.record(value, attributes);
  }

  @Override
  void recordRetryCount(long value, Attributes attributes) {
    attemptsPerOp.add(value, attributes);
  }

  @Override
  void recordConnectivityErrorCount(long value, Attributes attributes) {
    gfeMissingHeaderCount.add(value, attributes);
  }

  @Override
  void recordClientBlockingLatencies(long value, Attributes attributes) {
    batchThrottledTime.record(value, attributes);
  }
}
