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

import com.google.api.core.InternalApi;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentSelector;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.View;
import java.util.Map;
import java.util.Set;

/** Defining Bigtable builit-in metrics scope, attributes, metric names and views. */
@InternalApi
public class BuiltinMetricsConstants {

  // Metric attribute keys for monitored resource
  public static final AttributeKey<String> PROJECT_ID_KEY = AttributeKey.stringKey("project_id");
  public static final AttributeKey<String> INSTANCE_ID_KEY = AttributeKey.stringKey("instance");
  public static final AttributeKey<String> TABLE_ID_KEY = AttributeKey.stringKey("table");
  public static final AttributeKey<String> CLUSTER_ID_KEY = AttributeKey.stringKey("cluster");
  public static final AttributeKey<String> ZONE_ID_KEY = AttributeKey.stringKey("zone");

  // Metric attribute keys for labels
  public static final AttributeKey<String> APP_PROFILE_KEY = AttributeKey.stringKey("app_profile");
  public static final AttributeKey<Boolean> STREAMING_KEY = AttributeKey.booleanKey("streaming");
  static final AttributeKey<String> METHOD_KEY = AttributeKey.stringKey("method");
  static final AttributeKey<String> STATUS_KEY = AttributeKey.stringKey("status");
  static final AttributeKey<String> CLIENT_NAME_KEY = AttributeKey.stringKey("client_name");
  static final AttributeKey<String> CLIENT_UID_KEY = AttributeKey.stringKey("client_uid");

  // Metric names
  public static final String OPERATION_LATENCIES_NAME = "operation_latencies";
  static final String ATTEMPT_LATENCIES_NAME = "attempt_latencies";
  static final String RETRY_COUNT_NAME = "retry_count";
  static final String CONNECTIVITY_ERROR_COUNT_NAME = "connectivity_error_count";
  static final String SERVER_LATENCIES_NAME = "server_latencies";
  static final String FIRST_RESPONSE_LATENCIES_NAME = "first_response_latencies";
  static final String APPLICATION_BLOCKING_LATENCIES_NAME = "application_latencies";
  static final String CLIENT_BLOCKING_LATENCIES_NAME = "throttling_latencies";

  // Buckets under 100,000 are identical to buckets for server side metrics handler_latencies.
  // Extending client side bucket to up to 3,200,000.
  private static final Aggregation AGGREGATION_WITH_MILLIS_HISTOGRAM =
      Aggregation.explicitBucketHistogram(
          ImmutableList.of(
              0.0, 0.01, 0.05, 0.1, 0.3, 0.6, 0.8, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0, 13.0,
              16.0, 20.0, 25.0, 30.0, 40.0, 50.0, 65.0, 80.0, 100.0, 130.0, 160.0, 200.0, 250.0,
              300.0, 400.0, 500.0, 650.0, 800.0, 1000.0, 2000.0, 5000.0, 10000.0, 20000.0, 50000.0,
              100000.0, 200000.0, 400000.0, 800000.0, 1600000.0, 3200000.0)); // max is 53.3 minutes

  public static final String METER_NAME = "bigtable.googleapis.com/internal/client/";

  static final Set<String> COMMON_ATTRIBUTES =
      ImmutableSet.of(
          PROJECT_ID_KEY.getKey(),
          INSTANCE_ID_KEY.getKey(),
          TABLE_ID_KEY.getKey(),
          APP_PROFILE_KEY.getKey(),
          CLUSTER_ID_KEY.getKey(),
          ZONE_ID_KEY.getKey(),
          METHOD_KEY.getKey(),
          CLIENT_NAME_KEY.getKey());

  static void defineView(
      ImmutableMap.Builder<InstrumentSelector, View> viewMap,
      String id,
      Aggregation aggregation,
      InstrumentType type,
      String unit,
      Set<String> extraAttributes) {
    InstrumentSelector selector =
        InstrumentSelector.builder()
            .setName(id)
            .setMeterName(METER_NAME)
            .setType(type)
            .setUnit(unit)
            .build();
    Set<String> attributesFilter =
        ImmutableSet.<String>builder().addAll(COMMON_ATTRIBUTES).addAll(extraAttributes).build();
    View view =
        View.builder()
            .setName(METER_NAME + id)
            .setAggregation(aggregation)
            .setAttributeFilter(attributesFilter)
            .build();

    viewMap.put(selector, view);
  }

  public static Map<InstrumentSelector, View> getAllViews() {
    ImmutableMap.Builder<InstrumentSelector, View> views = ImmutableMap.builder();

    defineView(
        views,
        OPERATION_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.of(STREAMING_KEY.getKey(), STATUS_KEY.getKey()));
    defineView(
        views,
        ATTEMPT_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.of(STREAMING_KEY.getKey(), STATUS_KEY.getKey()));
    defineView(
        views,
        SERVER_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.of(STREAMING_KEY.getKey(), STATUS_KEY.getKey()));
    defineView(
        views,
        FIRST_RESPONSE_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.of(STATUS_KEY.getKey()));
    defineView(
        views,
        APPLICATION_BLOCKING_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.of());
    defineView(
        views,
        CLIENT_BLOCKING_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.of());
    defineView(
        views,
        RETRY_COUNT_NAME,
        Aggregation.sum(),
        InstrumentType.COUNTER,
        "1",
        ImmutableSet.of(STATUS_KEY.getKey()));
    defineView(
        views,
        CONNECTIVITY_ERROR_COUNT_NAME,
        Aggregation.sum(),
        InstrumentType.COUNTER,
        "1",
        ImmutableSet.of(STATUS_KEY.getKey()));

    return views.build();
  }
}
