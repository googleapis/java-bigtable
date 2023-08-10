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

import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_APP_PROFILE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_ATTEMPT_LATENCY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_BATCH_THROTTLED_TIME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_CLUSTER;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_GFE_LATENCY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_INSTANCE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_OP;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_OP_ATTEMPT_COUNT;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_OP_LATENCY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_PROJECT_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_READ_ROWS_FIRST_ROW_LATENCY;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_STATUS;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_TABLE_ID;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants.BIGTABLE_ZONE;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.AGGREGATION_ATTEMPT_COUNT;
import static com.google.cloud.bigtable.data.v2.stub.metrics.RpcViewConstants.AGGREGATION_WITH_MILLIS_HISTOGRAM;

import com.google.common.collect.ImmutableList;
import io.opencensus.stats.Aggregation;
import io.opencensus.stats.View;
import java.util.Arrays;

public class RpcViewsWithExtraTags {

  private static final Aggregation COUNT = Aggregation.Count.create();
  private static final Aggregation SUM = Aggregation.Sum.create();

  static final View BIGTABLE_OP_LATENCY_VIEW =
      View.create(
          View.Name.create("cloud.google.com/java/bigtable/op_latency"),
          "Operation latency in msecs",
          BIGTABLE_OP_LATENCY,
          RpcViewConstants.AGGREGATION_WITH_MILLIS_HISTOGRAM,
          ImmutableList.of(
              BIGTABLE_PROJECT_ID,
              BIGTABLE_INSTANCE_ID,
              BIGTABLE_APP_PROFILE_ID,
              BIGTABLE_TABLE_ID,
              BIGTABLE_OP,
              BIGTABLE_STATUS,
              BIGTABLE_CLUSTER,
              BIGTABLE_ZONE));

  static final View BIGTABLE_COMPLETED_OP_VIEW =
      View.create(
          View.Name.create("cloud.google.com/java/bigtable/completed_ops"),
          "Number of completed Bigtable client operations",
          BIGTABLE_OP_LATENCY,
          COUNT,
          Arrays.asList(
              BIGTABLE_PROJECT_ID,
              BIGTABLE_INSTANCE_ID,
              BIGTABLE_APP_PROFILE_ID,
              BIGTABLE_TABLE_ID,
              BIGTABLE_OP,
              BIGTABLE_STATUS,
              BIGTABLE_ZONE,
              BIGTABLE_CLUSTER));

  static final View BIGTABLE_READ_ROWS_FIRST_ROW_LATENCY_VIEW =
      View.create(
          View.Name.create("cloud.google.com/java/bigtable/read_rows_first_row_latency"),
          "Latency to receive the first row in a ReadRows stream",
          BIGTABLE_READ_ROWS_FIRST_ROW_LATENCY,
          AGGREGATION_WITH_MILLIS_HISTOGRAM,
          ImmutableList.of(
              BIGTABLE_PROJECT_ID,
              BIGTABLE_INSTANCE_ID,
              BIGTABLE_APP_PROFILE_ID,
              BIGTABLE_TABLE_ID,
              BIGTABLE_CLUSTER,
              BIGTABLE_ZONE));

  static final View BIGTABLE_ATTEMPT_LATENCY_VIEW =
      View.create(
          View.Name.create("cloud.google.com/java/bigtable/attempt_latency"),
          "Attempt latency in msecs",
          BIGTABLE_ATTEMPT_LATENCY,
          AGGREGATION_WITH_MILLIS_HISTOGRAM,
          ImmutableList.of(
              BIGTABLE_PROJECT_ID,
              BIGTABLE_INSTANCE_ID,
              BIGTABLE_APP_PROFILE_ID,
              BIGTABLE_TABLE_ID,
              BIGTABLE_OP,
              BIGTABLE_STATUS,
              BIGTABLE_CLUSTER,
              BIGTABLE_ZONE));

  static final View BIGTABLE_ATTEMPTS_PER_OP_VIEW =
      View.create(
          View.Name.create("cloud.google.com/java/bigtable/attempts_per_op"),
          "Distribution of attempts per logical operation",
          BIGTABLE_OP_ATTEMPT_COUNT,
          AGGREGATION_ATTEMPT_COUNT,
          ImmutableList.of(
              BIGTABLE_PROJECT_ID,
              BIGTABLE_INSTANCE_ID,
              BIGTABLE_APP_PROFILE_ID,
              BIGTABLE_TABLE_ID,
              BIGTABLE_OP,
              BIGTABLE_STATUS,
              BIGTABLE_CLUSTER,
              BIGTABLE_ZONE));

  static final View BIGTABLE_GFE_LATENCY_VIEW =
      View.create(
          View.Name.create("cloud.google.com/java/bigtable/gfe_latency"),
          "Latency between Google's network receives an RPC and reads back the first byte of the response",
          BIGTABLE_GFE_LATENCY,
          AGGREGATION_WITH_MILLIS_HISTOGRAM,
          ImmutableList.of(
              BIGTABLE_INSTANCE_ID,
              BIGTABLE_PROJECT_ID,
              BIGTABLE_APP_PROFILE_ID,
              BIGTABLE_TABLE_ID,
              BIGTABLE_OP,
              BIGTABLE_STATUS,
              BIGTABLE_CLUSTER,
              BIGTABLE_ZONE));

  static final View BIGTABLE_GFE_HEADER_MISSING_COUNT_VIEW =
      View.create(
          View.Name.create("cloud.google.com/java/bigtable/gfe_header_missing_count"),
          "Number of RPC responses received without the server-timing header, most likely means that the RPC never reached Google's network",
          BIGTABLE_GFE_HEADER_MISSING_COUNT,
          SUM,
          ImmutableList.of(
              BIGTABLE_INSTANCE_ID,
              BIGTABLE_PROJECT_ID,
              BIGTABLE_APP_PROFILE_ID,
              BIGTABLE_TABLE_ID,
              BIGTABLE_OP,
              BIGTABLE_STATUS,
              BIGTABLE_CLUSTER,
              BIGTABLE_ZONE));

  // use distribution so we can correlate batch throttled time with op_latency
  static final View BIGTABLE_BATCH_THROTTLED_TIME_VIEW =
      View.create(
          View.Name.create("cloud.google.com/java/bigtable/batch_throttled_time"),
          "Total throttled time of a batch in msecs",
          BIGTABLE_BATCH_THROTTLED_TIME,
          AGGREGATION_WITH_MILLIS_HISTOGRAM,
          ImmutableList.of(
              BIGTABLE_INSTANCE_ID,
              BIGTABLE_PROJECT_ID,
              BIGTABLE_APP_PROFILE_ID,
              BIGTABLE_TABLE_ID,
              BIGTABLE_OP,
              BIGTABLE_CLUSTER,
              BIGTABLE_ZONE));
}
