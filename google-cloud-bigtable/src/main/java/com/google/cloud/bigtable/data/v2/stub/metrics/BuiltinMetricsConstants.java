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
import java.util.stream.Collectors;

/** Defining Bigtable builit-in metrics scope, attributes, metric names and views. */
@InternalApi
public class BuiltinMetricsConstants {

  // Metric attribute keys for monitored resource
  public static final AttributeKey<String> BIGTABLE_PROJECT_ID_KEY =
      AttributeKey.stringKey("project_id");
  public static final AttributeKey<String> INSTANCE_ID_KEY = AttributeKey.stringKey("instance");
  public static final AttributeKey<String> TABLE_ID_KEY = AttributeKey.stringKey("table");
  public static final AttributeKey<String> CLUSTER_ID_KEY = AttributeKey.stringKey("cluster");
  public static final AttributeKey<String> ZONE_ID_KEY = AttributeKey.stringKey("zone");

  // Metric attribute keys for labels
  // We need to access APP_PROFILE_KEY in EnhancedBigtableStubSettings and STREAMING_KEY in
  // IT tests, so they're public.
  public static final AttributeKey<String> APP_PROFILE_KEY = AttributeKey.stringKey("app_profile");
  public static final AttributeKey<Boolean> STREAMING_KEY = AttributeKey.booleanKey("streaming");
  public static final AttributeKey<String> CLIENT_NAME_KEY = AttributeKey.stringKey("client_name");
  static final AttributeKey<String> METHOD_KEY = AttributeKey.stringKey("method");
  static final AttributeKey<String> STATUS_KEY = AttributeKey.stringKey("status");
  static final AttributeKey<String> CLIENT_UID_KEY = AttributeKey.stringKey("client_uid");

  static final AttributeKey<String> TRANSPORT_TYPE = AttributeKey.stringKey("transport_type");
  static final AttributeKey<String> TRANSPORT_REGION = AttributeKey.stringKey("transport_region");
  static final AttributeKey<String> TRANSPORT_ZONE = AttributeKey.stringKey("transport_zone");
  static final AttributeKey<String> TRANSPORT_SUBZONE = AttributeKey.stringKey("transport_subzone");

  public static final String METER_NAME = "bigtable.googleapis.com/internal/client/";

  // Metric names
  public static final String OPERATION_LATENCIES_NAME = "operation_latencies";
  public static final String ATTEMPT_LATENCIES_NAME = "attempt_latencies";
  // Temporary workaround for not being able to add new labels to ATTEMPT_LATENCIES_NAME
  public static final String ATTEMPT_LATENCIES2_NAME = "attempt_latencies2";
  static final String RETRY_COUNT_NAME = "retry_count";
  static final String CONNECTIVITY_ERROR_COUNT_NAME = "connectivity_error_count";
  static final String SERVER_LATENCIES_NAME = "server_latencies";
  static final String FIRST_RESPONSE_LATENCIES_NAME = "first_response_latencies";
  static final String APPLICATION_BLOCKING_LATENCIES_NAME = "application_latencies";
  static final String REMAINING_DEADLINE_NAME = "remaining_deadline";
  static final String CLIENT_BLOCKING_LATENCIES_NAME = "throttling_latencies";
  static final String PER_CONNECTION_ERROR_COUNT_NAME = "per_connection_error_count";
  static final String OUTSTANDING_RPCS_PER_CHANNEL_NAME = "connection_pool/outstanding_rpcs";

  // Start allow list of metrics that will be exported as internal
  public static final Map<String, Set<String>> GRPC_METRICS =
      ImmutableMap.<String, Set<String>>builder()
          .put(
              "grpc.client.attempt.duration",
              ImmutableSet.of("grpc.lb.locality", "grpc.method", "grpc.target", "grpc.status"))
          .put(
              "grpc.lb.rls.default_target_picks",
              ImmutableSet.of("grpc.lb.rls.data_plane_target", "grpc.lb.pick_result"))
          .put(
              "grpc.lb.rls.target_picks",
              ImmutableSet.of(
                  "grpc.target",
                  "grpc.lb.rls.server_target",
                  "grpc.lb.rls.data_plane_target",
                  "grpc.lb.pick_result"))
          .put(
              "grpc.lb.rls.failed_picks",
              ImmutableSet.of("grpc.target", "grpc.lb.rls.server_target"))
          // TODO: "grpc.xds_client.connected"
          .put("grpc.xds_client.server_failure", ImmutableSet.of("grpc.target", "grpc.xds.server"))
          // TODO: "grpc.xds_client.resource_updates_valid",
          .put(
              "grpc.xds_client.resource_updates_invalid",
              ImmutableSet.of("grpc.target", "grpc.xds.server", "grpc.xds.resource_type"))
          // TODO: "grpc.xds_client.resources"
          .build();

  public static final Set<String> INTERNAL_METRICS =
      ImmutableSet.of(PER_CONNECTION_ERROR_COUNT_NAME).stream()
          .map(m -> METER_NAME + m)
          .collect(ImmutableSet.toImmutableSet());
  // End allow list of metrics that will be exported

  // Buckets under 100,000 are identical to buckets for server side metrics handler_latencies.
  // Extending client side bucket to up to 3,200,000.
  private static final Aggregation AGGREGATION_WITH_MILLIS_HISTOGRAM =
      Aggregation.explicitBucketHistogram(
          ImmutableList.of(
              0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 8.0, 10.0, 13.0, 16.0, 20.0, 25.0, 30.0, 40.0,
              50.0, 65.0, 80.0, 100.0, 130.0, 160.0, 200.0, 250.0, 300.0, 400.0, 500.0, 650.0,
              800.0, 1000.0, 2000.0, 5000.0, 10000.0, 20000.0, 50000.0, 100000.0, 200000.0,
              400000.0, 800000.0, 1600000.0, 3200000.0)); // max is 53.3 minutes

  private static final Aggregation AGGREGATION_PER_CONNECTION_ERROR_COUNT_HISTOGRAM =
      Aggregation.explicitBucketHistogram(
          ImmutableList.of(
              1.0,
              2.0,
              4.0,
              8.0,
              16.0,
              32.0,
              64.0,
              125.0,
              250.0,
              500.0,
              1_000.0,
              2_000.0,
              4_000.0,
              8_000.0,
              16_000.0,
              32_000.0,
              64_000.0,
              128_000.0,
              250_000.0,
              500_000.0,
              1_000_000.0));

  // Buckets for outstanding RPCs per channel, max ~100
  private static final Aggregation AGGREGATION_OUTSTANDING_RPCS_HISTOGRAM =
      Aggregation.explicitBucketHistogram(
          ImmutableList.of(
              0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0,
              25.0, 30.0, 35.0, 40.0, 45.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0, 110.0)); // Added

  static final Set<AttributeKey> COMMON_ATTRIBUTES =
      ImmutableSet.of(
          BIGTABLE_PROJECT_ID_KEY,
          INSTANCE_ID_KEY,
          TABLE_ID_KEY,
          APP_PROFILE_KEY,
          CLUSTER_ID_KEY,
          ZONE_ID_KEY,
          METHOD_KEY,
          CLIENT_NAME_KEY);

  static void defineView(
      ImmutableMap.Builder<InstrumentSelector, View> viewMap,
      String id,
      Aggregation aggregation,
      InstrumentType type,
      String unit,
      Set<AttributeKey> attributes) {
    InstrumentSelector selector =
        InstrumentSelector.builder()
            .setName(id)
            .setMeterName(METER_NAME)
            .setType(type)
            .setUnit(unit)
            .build();
    Set<String> attributesFilter =
        ImmutableSet.<String>builder()
            .addAll(
                COMMON_ATTRIBUTES.stream().map(AttributeKey::getKey).collect(Collectors.toSet()))
            .addAll(attributes.stream().map(AttributeKey::getKey).collect(Collectors.toSet()))
            .build();
    View view =
        View.builder()
            .setName(METER_NAME + id)
            .setAggregation(aggregation)
            .setAttributeFilter(attributesFilter)
            .build();

    viewMap.put(selector, view);
  }

  // uses cloud.BigtableClient schema
  public static Map<InstrumentSelector, View> getInternalViews() {
    ImmutableMap.Builder<InstrumentSelector, View> views = ImmutableMap.builder();
    defineView(
        views,
        PER_CONNECTION_ERROR_COUNT_NAME,
        AGGREGATION_PER_CONNECTION_ERROR_COUNT_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "1",
        ImmutableSet.<AttributeKey>builder()
            .add(BIGTABLE_PROJECT_ID_KEY, INSTANCE_ID_KEY, APP_PROFILE_KEY, CLIENT_NAME_KEY)
            .build());
    defineView(
        views,
        OUTSTANDING_RPCS_PER_CHANNEL_NAME,
        AGGREGATION_OUTSTANDING_RPCS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "1",
        ImmutableSet.<AttributeKey>builder()
            .add(BIGTABLE_PROJECT_ID_KEY, INSTANCE_ID_KEY, APP_PROFILE_KEY, CLIENT_NAME_KEY)
            .build());
    return views.build();
  }

  public static Map<InstrumentSelector, View> getAllViews() {
    ImmutableMap.Builder<InstrumentSelector, View> views = ImmutableMap.builder();

    defineView(
        views,
        OPERATION_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(STREAMING_KEY, STATUS_KEY)
            .build());
    defineView(
        views,
        ATTEMPT_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(STREAMING_KEY, STATUS_KEY)
            .build());
    defineView(
        views,
        ATTEMPT_LATENCIES2_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(
                STREAMING_KEY,
                STATUS_KEY,
                TRANSPORT_TYPE,
                TRANSPORT_REGION,
                TRANSPORT_ZONE,
                TRANSPORT_SUBZONE)
            .build());
    defineView(
        views,
        SERVER_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.<AttributeKey>builder().addAll(COMMON_ATTRIBUTES).add(STATUS_KEY).build());
    defineView(
        views,
        FIRST_RESPONSE_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.<AttributeKey>builder().addAll(COMMON_ATTRIBUTES).add(STATUS_KEY).build());
    defineView(
        views,
        APPLICATION_BLOCKING_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.<AttributeKey>builder().addAll(COMMON_ATTRIBUTES).build());
    defineView(
        views,
        CLIENT_BLOCKING_LATENCIES_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.<AttributeKey>builder().addAll(COMMON_ATTRIBUTES).build());
    defineView(
        views,
        RETRY_COUNT_NAME,
        Aggregation.sum(),
        InstrumentType.COUNTER,
        "1",
        ImmutableSet.<AttributeKey>builder().addAll(COMMON_ATTRIBUTES).add(STATUS_KEY).build());
    defineView(
        views,
        CONNECTIVITY_ERROR_COUNT_NAME,
        Aggregation.sum(),
        InstrumentType.COUNTER,
        "1",
        ImmutableSet.<AttributeKey>builder().addAll(COMMON_ATTRIBUTES).add(STATUS_KEY).build());
    defineView(
        views,
        REMAINING_DEADLINE_NAME,
        AGGREGATION_WITH_MILLIS_HISTOGRAM,
        InstrumentType.HISTOGRAM,
        "ms",
        ImmutableSet.<AttributeKey>builder()
            .addAll(COMMON_ATTRIBUTES)
            .add(STREAMING_KEY, STATUS_KEY)
            .build());

    return views.build();
  }
}
