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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.APPLICATION_BLOCKING_LATENCIES_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.ATTEMPT_LATENCIES_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CLIENT_BLOCKING_LATENCIES_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.CONNECTIVITY_ERROR_COUNT_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.FIRST_RESPONSE_LATENCIES_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.OPERATION_LATENCIES_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.RETRY_COUNT_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.SERVER_LATENCIES_NAME;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;

/** Implementation of {@link BigtableMetricsRecorder} that creates Builtin metrics measurements. */
class BuiltinInMetricsRecorder extends BigtableMetricsRecorder {

  private static final String MILLISECOND = "ms";
  private static final String COUNT = "1";

  private final LongHistogram operationLatencies;
  private final LongHistogram attemptLatencies;
  private final LongHistogram serverLatencies;
  private final LongHistogram firstResponseLatencies;
  private final LongHistogram clientBlockingLatencies;
  private final LongHistogram applicationBlockingLatencies;
  private final LongCounter connectivityErrorCount;
  private final LongCounter retryCount;

  BuiltinInMetricsRecorder(Meter meter) {
    operationLatencies =
        meter
            .histogramBuilder(OPERATION_LATENCIES_NAME)
            .ofLongs()
            .setDescription(
                "Total time until final operation success or failure, including retries and backoff.")
            .setUnit(MILLISECOND)
            .build();
    attemptLatencies =
        meter
            .histogramBuilder(ATTEMPT_LATENCIES_NAME)
            .ofLongs()
            .setDescription("Client observed latency per RPC attempt.")
            .setUnit(MILLISECOND)
            .build();
    serverLatencies =
        meter
            .histogramBuilder(SERVER_LATENCIES_NAME)
            .ofLongs()
            .setDescription(
                "The latency measured from the moment that the RPC entered the Google data center until the RPC was completed.")
            .setUnit(MILLISECOND)
            .build();
    firstResponseLatencies =
        meter
            .histogramBuilder(FIRST_RESPONSE_LATENCIES_NAME)
            .ofLongs()
            .setDescription(
                "Latency from operation start until the response headers were received. The publishing of the measurement will be delayed until the attempt response has been received.")
            .setUnit(MILLISECOND)
            .build();
    clientBlockingLatencies =
        meter
            .histogramBuilder(CLIENT_BLOCKING_LATENCIES_NAME)
            .ofLongs()
            .setDescription(
                "The artificial latency introduced by the client to limit the number of outstanding requests. The publishing of the measurement will be delayed until the attempt trailers have been received.")
            .setUnit(MILLISECOND)
            .build();
    applicationBlockingLatencies =
        meter
            .histogramBuilder(APPLICATION_BLOCKING_LATENCIES_NAME)
            .ofLongs()
            .setDescription(
                "The latency of the client application consuming available response data.")
            .setUnit(MILLISECOND)
            .build();
    connectivityErrorCount =
        meter
            .counterBuilder(CONNECTIVITY_ERROR_COUNT_NAME)
            .setDescription(
                "Number of requests that failed to reach the Google datacenter. (Requests without google response headers")
            .setUnit(COUNT)
            .build();
    retryCount =
        meter
            .counterBuilder(RETRY_COUNT_NAME)
            .setDescription("The number of additional RPCs sent after the initial attempt.")
            .setUnit(COUNT)
            .build();
  }

  @Override
  void recordOperationLatencies(long value, Attributes attributes) {
    operationLatencies.record(value, attributes);
  }

  @Override
  void recordAttemptLatencies(long value, Attributes attributes) {
    attemptLatencies.record(value, attributes);
  }

  @Override
  void recordFirstResponseLatencies(long value, Attributes attributes) {
    firstResponseLatencies.record(value, attributes);
  }

  @Override
  void recordRetryCount(long value, Attributes attributes) {
    retryCount.add(value, attributes);
  }

  @Override
  void recordServerLatencies(long value, Attributes attributes) {
    serverLatencies.record(value, attributes);
  }

  @Override
  void recordConnectivityErrorCount(long value, Attributes attributes) {
    connectivityErrorCount.add(value, attributes);
  }

  @Override
  void recordApplicationBlockingLatencies(long value, Attributes attributes) {
    applicationBlockingLatencies.record(value, attributes);
  }

  @Override
  void recordClientBlockingLatencies(long value, Attributes attributes) {
    clientBlockingLatencies.record(value, attributes);
  }
}
