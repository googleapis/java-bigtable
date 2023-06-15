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

import com.google.common.collect.ImmutableList;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.View;

public class BuiltinMetricsAttributes {

  public static final AttributeKey<String> PROJECT_ID = AttributeKey.stringKey("project_id");
  public static final AttributeKey<String> INSTANCE_ID = AttributeKey.stringKey("instance");
  public static final AttributeKey<String> TABLE_ID = AttributeKey.stringKey("table");
  public static final AttributeKey<String> CLUSTER_ID = AttributeKey.stringKey("cluster");
  public static final AttributeKey<String> ZONE_ID = AttributeKey.stringKey("zone");
  static final AttributeKey<String> CLIENT_UID = AttributeKey.stringKey("client_uid");

  public static final AttributeKey<String> APP_PROFILE = AttributeKey.stringKey("app_profile");
  static final AttributeKey<Boolean> STREAMING = AttributeKey.booleanKey("streaming");
  static final AttributeKey<String> METHOD = AttributeKey.stringKey("method");
  static final AttributeKey<String> STATUS = AttributeKey.stringKey("status");
  static final AttributeKey<String> CLIENT_NAME = AttributeKey.stringKey("client_name");

  static final String OPERATION_LATENCIES_NAME = "operation_latencies";
  static final String ATTEMPT_LATENCIES_NAME = "attempt_latencies";
  static final String RETRY_COUNT_NAME = "retry_count";
  static final String CONNECTIVITY_ERROR_COUNT_NAME = "connectivity_error_count";
  static final String SERVER_LATENCIES_NAME = "server_latencies";
  static final String FIRST_RESPONSE_LATENCIES_NAME = "first_response_latencies";
  static final String APPLICATION_BLOCKING_LATENCIES_NAME = "application_latencies";
  static final String CLIENT_BLOCKING_LATENCIES_NAME = "throttling_latencies";

  private static final Aggregation AGGREGATION_WITH_MILLIS_HISTOGRAM =
      Aggregation.explicitBucketHistogram(
          ImmutableList.of(
              0.0, 0.01, 0.05, 0.1, 0.3, 0.6, 0.8, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0, 13.0,
              16.0, 20.0, 25.0, 30.0, 40.0, 50.0, 65.0, 80.0, 100.0, 130.0, 160.0, 200.0, 250.0,
              300.0, 400.0, 500.0, 650.0, 800.0, 1000.0, 2000.0, 5000.0, 10000.0, 20000.0, 50000.0,
              100000.0));

  static final String SCOPE = "bigtable.googleapis.com";

  static final InstrumentSelector OPERATION_LATENCIES_SELECTOR =
      InstrumentSelector.builder()
          .setName(OPERATION_LATENCIES_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();
  static final InstrumentSelector ATTEMPT_LATENCIES_SELECTOR =
      InstrumentSelector.builder()
          .setName(ATTEMPT_LATENCIES_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();

  static final InstrumentSelector RETRY_COUNT_SELECTOR =
      InstrumentSelector.builder()
          .setName(RETRY_COUNT_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.COUNTER)
          .setUnit("1")
          .build();

  static final InstrumentSelector SERVER_LATENCIES_SELECTOR =
      InstrumentSelector.builder()
          .setName(SERVER_LATENCIES_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();
  static final InstrumentSelector FIRST_RESPONSE_LATENCIES_SELECTOR =
      InstrumentSelector.builder()
          .setName(FIRST_RESPONSE_LATENCIES_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();
  static final InstrumentSelector CLIENT_BLOCKING_LATENCIES_SELECTOR =
      InstrumentSelector.builder()
          .setName(CLIENT_BLOCKING_LATENCIES_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();
  static final InstrumentSelector APPLICATION_BLOCKING_LATENCIES_SELECTOR =
      InstrumentSelector.builder()
          .setName(APPLICATION_BLOCKING_LATENCIES_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.HISTOGRAM)
          .setUnit("ms")
          .build();
  static final InstrumentSelector CONNECTIVITY_ERROR_COUNT_SELECTOR =
      InstrumentSelector.builder()
          .setName(CONNECTIVITY_ERROR_COUNT_NAME)
          .setMeterName(SCOPE)
          .setType(InstrumentType.COUNTER)
          .setUnit("1")
          .build();

  static final View OPERATION_LATENCIES_VIEW =
      View.builder()
          .setName("bigtable.googleapis.com/internal/client/operation_latencies")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();
  static final View ATTEMPT_LATENCIES_VIEW =
      View.builder()
          .setName("bigtable.googleapis.com/internal/client/attempt_latencies")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();
  static final View SERVER_LATENCIES_VIEW =
      View.builder()
          .setName("bigtable.googleapis.com/internal/client/server_latencies")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();
  static final View FIRST_RESPONSE_LATENCIES_VIEW =
      View.builder()
          .setName("bigtable.googleapis.com/internal/client/first_response_latencies")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();
  static final View APPLICATION_BLOCKING_LATENCIES_VIEW =
      View.builder()
          .setName("bigtable.googleapis.com/internal/client/application_latencies")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();
  static final View CLIENT_BLOCKING_LATENCIES_VIEW =
      View.builder()
          .setName("bigtable.googleapis.com/internal/client/throttling_latencies")
          .setAggregation(AGGREGATION_WITH_MILLIS_HISTOGRAM)
          .build();
  static final View RETRY_COUNT_VIEW =
      View.builder()
          .setName("bigtable.googleapis.com/internal/client/retry_count")
          .setAggregation(Aggregation.sum())
          .build();
  static final View CONNECTIVITY_ERROR_COUNT_VIEW =
      View.builder()
          .setName("bigtable.googleapis.com/internal/client/connectivity_error_count")
          .setAggregation(Aggregation.sum())
          .build();
}
