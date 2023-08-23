/*
 * Copyright 2019 Google LLC
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

import com.google.common.collect.ImmutableList;
import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.View;

public class RpcViewConstants {
  // Aggregations
  private static final Aggregation AGGREGATION_WITH_MILLIS_HISTOGRAM =
      Aggregation.explicitBucketHistogram(
          ImmutableList.of(
              0.0, 0.01, 0.05, 0.1, 0.3, 0.6, 0.8, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0, 13.0,
              16.0, 20.0, 25.0, 30.0, 40.0, 50.0, 65.0, 80.0, 100.0, 130.0, 160.0, 200.0, 250.0,
              300.0, 400.0, 500.0, 650.0, 800.0, 1000.0, 2000.0, 5000.0, 10000.0, 20000.0, 50000.0,
              100000.0));

  private static final Aggregation AGGREGATION_ATTEMPT_COUNT =
      Aggregation.explicitBucketHistogram(
          ImmutableList.of(
              1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 15.0, 20.0, 30.0, 40.0, 50.0,
              100.0));

  static final String SCOPE = "bigtable.googleapis.com";

  static final String OP_LATENCY_NAME = "op_latency";

  static final String ATTEMPT_LATENCY_NAME = "attempt_latency";

  static final String ATTEMPTS_PER_OP_NAME = "attempts_per_op";

  static final String COMPLETED_OPS_NAME = "completed_ops";

  static final String READ_ROWS_FIRST_ROW_LATENCY_NAME = "read_rows_first_row_latency";

  static final String GFE_LATENCY_NAME = "gfe_latency";

  static final String GFE_MISSING_HEADER_NAME = "gfe_header_missing_count";

  static final String THROTTLED_TIME_NAME = "batch_throttled_time";

  public static final InstrumentSelector OP_LATENCY_SELECTOR =
      InstrumentSelector.builder()
          .setName(OP_LATENCY_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();

  public static final InstrumentSelector COMPLETED_OP_SELECTOR =
      InstrumentSelector.builder()
          .setName(COMPLETED_OPS_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.COUNTER)
          .setUnit("1")
          .build();

  public static final InstrumentSelector ATTEMPT_LATENCY_SELECTOR =
      InstrumentSelector.builder()
          .setName(ATTEMPT_LATENCY_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();

  public static final InstrumentSelector ATTEMPTS_PER_OP_SELECTOR =
      InstrumentSelector.builder()
          .setName(ATTEMPTS_PER_OP_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.COUNTER)
          .setUnit("1")
          .build();

  public static final InstrumentSelector GFE_LATENCY_SELECTOR =
      InstrumentSelector.builder()
          .setName(GFE_LATENCY_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();

  public static final InstrumentSelector READ_ROWS_FIRST_ROW_LATENCY_SELECTOR =
      InstrumentSelector.builder()
          .setName(READ_ROWS_FIRST_ROW_LATENCY_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();

  public static final InstrumentSelector GFE_MISSING_HEADER_SELECTOR =
      InstrumentSelector.builder()
          .setName(GFE_MISSING_HEADER_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.COUNTER)
          .setUnit("1")
          .build();

  public static final InstrumentSelector BATCH_THROTTLED_TIME_SELECTOR =
      InstrumentSelector.builder()
          .setName(THROTTLED_TIME_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();

  /**
   * {@link View} for Bigtable client roundtrip latency in milliseconds including all retry
   * attempts.
   */
  public static final View BIGTABLE_OP_LATENCY_VIEW =
      View.builder()
          .setName("cloud.google.com/java/bigtable/op_latency")
          .setDescription("Operation latency in msecs")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();

  public static final View BIGTABLE_COMPLETED_OP_VIEW =
      View.builder()
          .setName("cloud.google.com/java/bigtable/completed_ops")
          .setDescription("Number of completed Bigtable client operations")
          .setAggregation(Aggregation.sum())
          .build();

  public static final View BIGTABLE_READ_ROWS_FIRST_ROW_LATENCY_VIEW =
      View.builder()
          .setName("cloud.google.com/java/bigtable/read_rows_first_row_latency")
          .setDescription("Latency to receive the first row in a ReadRows stream")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();

  public static final View BIGTABLE_ATTEMPT_LATENCY_VIEW =
      View.builder()
          .setName("cloud.google.com/java/bigtable/attempt_latency")
          .setDescription("Attempt latency in msecs")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();

  public static final View BIGTABLE_ATTEMPTS_PER_OP_VIEW =
      View.builder()
          .setName("cloud.google.com/java/bigtable/attempts_per_op")
          .setDescription("Distribution of attempts per logical operation")
          .setAggregation(AGGREGATION_ATTEMPT_COUNT)
          .build();

  public static final View BIGTABLE_GFE_LATENCY_VIEW =
      View.builder()
          .setName("cloud.google.com/java/bigtable/gfe_latency")
          .setDescription(
              "Latency between Google's network receives an RPC and reads back the first byte of the response")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();

  public static final View BIGTABLE_GFE_HEADER_MISSING_COUNT_VIEW =
      View.builder()
          .setName("cloud.google.com/java/bigtable/gfe_header_missing_count")
          .setDescription(
              "Number of RPC responses received without the server-timing header, most likely means that the RPC never reached Google's network")
          .setAggregation(Aggregation.sum())
          .build();

  // use distribution so we can correlate batch throttled time with op_latency
  public static final View BIGTABLE_BATCH_THROTTLED_TIME_VIEW =
      View.builder()
          .setName("cloud.google.com/java/bigtable/batch_throttled_time")
          .setDescription("Total throttled time of a batch in msecs")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();
}
