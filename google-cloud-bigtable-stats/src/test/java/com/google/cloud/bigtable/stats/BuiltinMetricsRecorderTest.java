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
package com.google.cloud.bigtable.stats;

import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.common.collect.ImmutableMap;
import io.grpc.Status;
import org.junit.Before;
import org.junit.Test;

public class BuiltinMetricsRecorderTest {

  private final String PROJECT_ID = "fake-project";
  private final String INSTANCE_ID = "fake-instance";
  private final String APP_PROFILE_ID = "fake-app-profile";

  private final String TABLE_ID = "fake-table-id";
  private final String ZONE = "fake-zone";
  private final String CLUSTER = "fake-cluster";

  private StatsWrapper wrapper;

  @Before
  public void setup() {
    this.wrapper = new StatsWrapper(true);
    BuiltinViews views = new BuiltinViews(wrapper);
    views.registerBigtableBuiltinViews();
  }

  @Test
  public void testStreamingOperation() throws InterruptedException {
    BuiltinMetricsRecorder tracer =
        new BuiltinMetricsRecorder(
            ApiTracerFactory.OperationType.ServerStreaming,
            SpanName.of("Bigtable", "ReadRows"),
            ImmutableMap.of(
                BuiltinMeasureConstants.PROJECT_ID.getName(), PROJECT_ID,
                BuiltinMeasureConstants.INSTANCE_ID.getName(), INSTANCE_ID,
                BuiltinMeasureConstants.APP_PROFILE.getName(), APP_PROFILE_ID),
            wrapper);

    long operationLatency = 1234;
    int attemptCount = 2;
    long attemptLatency = 56;
    long serverLatency = 78;
    long applicationLatency = 901;
    long connectivityErrorCount = 15;
    long throttlingLatency = 50;
    long firstResponseLatency = 90;

    tracer.recordOperationLatencies(operationLatency);
    tracer.recordRetryCount(attemptCount);
    tracer.recordAttemptLatency(attemptLatency);
    tracer.recordApplicationLatency(applicationLatency);
    tracer.recordGfeLatencies(serverLatency);
    tracer.recordGfeMissingHeaders(connectivityErrorCount);
    tracer.recordFirstResponseLatency(firstResponseLatency);
    tracer.recordBatchRequestThrottled(throttlingLatency, TABLE_ID, ZONE, CLUSTER);

    tracer.recordAttemptLevelWithoutStreaming(
        Status.UNAVAILABLE.toString(), TABLE_ID, ZONE, CLUSTER);
    tracer.recordAttemptLevelWithStreaming(Status.ABORTED.toString(), TABLE_ID, ZONE, CLUSTER);
    tracer.recordOperationLevelWithoutStreaming("OK", TABLE_ID, ZONE, CLUSTER);
    tracer.recordOperationLevelWithStreaming("OK", TABLE_ID, ZONE, CLUSTER);

    Thread.sleep(100);

    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.OPERATION_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD, "Bigtable.ReadRows",
                    BuiltinMeasureConstants.STATUS, "OK",
                    BuiltinMeasureConstants.TABLE, TABLE_ID,
                    BuiltinMeasureConstants.ZONE, ZONE,
                    BuiltinMeasureConstants.CLUSTER, CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME, "bigtable-java",
                    BuiltinMeasureConstants.STREAMING, "true"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(operationLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.ATTEMPT_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.ReadRows",
                    BuiltinMeasureConstants.STATUS,
                    Status.ABORTED.toString(),
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java",
                    BuiltinMeasureConstants.STREAMING,
                    "true"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(attemptLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.RETRY_COUNT_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.ReadRows",
                    BuiltinMeasureConstants.STATUS,
                    "OK",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(attemptCount);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.SERVER_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.ReadRows",
                    BuiltinMeasureConstants.STATUS,
                    Status.ABORTED.toString(),
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java",
                    BuiltinMeasureConstants.STREAMING,
                    "true",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(serverLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.APPLICATION_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.ReadRows",
                    BuiltinMeasureConstants.STATUS,
                    "OK",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java",
                    BuiltinMeasureConstants.STREAMING,
                    "true"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(applicationLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.CONNECTIVITY_ERROR_COUNT_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.ReadRows",
                    BuiltinMeasureConstants.STATUS,
                    Status.UNAVAILABLE.toString(),
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(connectivityErrorCount);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.THROTTLING_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD, "Bigtable.ReadRows",
                    BuiltinMeasureConstants.TABLE, TABLE_ID,
                    BuiltinMeasureConstants.ZONE, ZONE,
                    BuiltinMeasureConstants.CLUSTER, CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME, "bigtable-java"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(throttlingLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.FIRST_RESPONSE_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.ReadRows",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER,
                    BuiltinMeasureConstants.STATUS,
                    "OK",
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(firstResponseLatency);
  }

  @Test
  public void testUnaryOperations() throws InterruptedException {
    BuiltinMetricsRecorder tracer =
        new BuiltinMetricsRecorder(
            ApiTracerFactory.OperationType.ServerStreaming,
            SpanName.of("Bigtable", "MutateRow"),
            ImmutableMap.of(
                BuiltinMeasureConstants.PROJECT_ID.getName(), PROJECT_ID,
                BuiltinMeasureConstants.INSTANCE_ID.getName(), INSTANCE_ID,
                BuiltinMeasureConstants.APP_PROFILE.getName(), APP_PROFILE_ID),
            wrapper);

    long operationLatency = 1234;
    int attemptCount = 2;
    long attemptLatency = 56;
    long serverLatency = 78;
    long applicationLatency = 901;
    long connectivityErrorCount = 15;
    long throttlingLatency = 50;
    long firstResponseLatency = 90;

    tracer.recordOperationLatencies(operationLatency);
    tracer.recordRetryCount(attemptCount);
    tracer.recordAttemptLatency(attemptLatency);
    tracer.recordApplicationLatency(applicationLatency);
    tracer.recordGfeLatencies(serverLatency);
    tracer.recordGfeMissingHeaders(connectivityErrorCount);
    tracer.recordFirstResponseLatency(firstResponseLatency);
    tracer.recordBatchRequestThrottled(throttlingLatency, TABLE_ID, ZONE, CLUSTER);

    tracer.recordOperationLevelWithStreaming("OK", TABLE_ID, ZONE, CLUSTER);
    tracer.recordOperationLevelWithoutStreaming("OK", TABLE_ID, ZONE, CLUSTER);
    tracer.recordAttemptLevelWithoutStreaming(
        Status.UNAVAILABLE.toString(), TABLE_ID, ZONE, CLUSTER);
    tracer.recordAttemptLevelWithStreaming(Status.ABORTED.toString(), TABLE_ID, ZONE, CLUSTER);

    Thread.sleep(100);

    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.OPERATION_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD, "Bigtable.MutateRow",
                    BuiltinMeasureConstants.STATUS, "OK",
                    BuiltinMeasureConstants.TABLE, TABLE_ID,
                    BuiltinMeasureConstants.ZONE, ZONE,
                    BuiltinMeasureConstants.CLUSTER, CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME, "bigtable-java",
                    BuiltinMeasureConstants.STREAMING, "false"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(operationLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.ATTEMPT_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.MutateRow",
                    BuiltinMeasureConstants.STATUS,
                    Status.ABORTED.toString(),
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java",
                    BuiltinMeasureConstants.STREAMING,
                    "false"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(attemptLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.RETRY_COUNT_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.MutateRow",
                    BuiltinMeasureConstants.STATUS,
                    "OK",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(attemptCount);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.SERVER_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.MutateRow",
                    BuiltinMeasureConstants.STATUS,
                    Status.ABORTED.toString(),
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java",
                    BuiltinMeasureConstants.STREAMING,
                    "false",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(serverLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.APPLICATION_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.MutateRow",
                    BuiltinMeasureConstants.STATUS,
                    "OK",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java",
                    BuiltinMeasureConstants.STREAMING,
                    "false"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(applicationLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.CONNECTIVITY_ERROR_COUNT_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.MutateRow",
                    BuiltinMeasureConstants.STATUS,
                    Status.UNAVAILABLE.toString(),
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(connectivityErrorCount);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.THROTTLING_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD, "Bigtable.MutateRow",
                    BuiltinMeasureConstants.TABLE, TABLE_ID,
                    BuiltinMeasureConstants.ZONE, ZONE,
                    BuiltinMeasureConstants.CLUSTER, CLUSTER,
                    BuiltinMeasureConstants.CLIENT_NAME, "bigtable-java"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(throttlingLatency);
    assertThat(
            wrapper.getAggregationValueAsLong(
                BuiltinViewConstants.FIRST_RESPONSE_LATENCIES_VIEW,
                ImmutableMap.of(
                    BuiltinMeasureConstants.METHOD,
                    "Bigtable.MutateRow",
                    BuiltinMeasureConstants.TABLE,
                    TABLE_ID,
                    BuiltinMeasureConstants.ZONE,
                    ZONE,
                    BuiltinMeasureConstants.CLUSTER,
                    CLUSTER,
                    BuiltinMeasureConstants.STATUS,
                    "OK",
                    BuiltinMeasureConstants.CLIENT_NAME,
                    "bigtable-java"),
                PROJECT_ID,
                INSTANCE_ID,
                APP_PROFILE_ID))
        .isEqualTo(firstResponseLatency);
  }
}
