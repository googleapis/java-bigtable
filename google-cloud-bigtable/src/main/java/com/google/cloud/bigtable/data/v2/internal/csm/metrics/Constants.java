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

package com.google.cloud.bigtable.data.v2.internal.csm.metrics;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;
import io.opentelemetry.api.common.AttributeKey;
import java.util.ArrayList;
import java.util.List;

public final class Constants {
  private Constants() {}

  public static final class MetricLabels {
    private MetricLabels() {}

    // TODO: remove overlapping attributes
    // Project & Instance overlap with resource labels because they were migrated from
    // an old gce/gke schema to support per_connection_error_count metric
    @Deprecated
    public static final AttributeKey<String> BIGTABLE_PROJECT_ID_KEY =
        AttributeKey.stringKey("project_id");

    @Deprecated
    public static final AttributeKey<String> INSTANCE_ID_KEY = AttributeKey.stringKey("instance");

    public static final AttributeKey<String> TRANSPORT_TYPE =
        AttributeKey.stringKey("transport_type");
    public static final AttributeKey<String> TRANSPORT_REGION =
        AttributeKey.stringKey("transport_region");
    public static final AttributeKey<String> TRANSPORT_ZONE =
        AttributeKey.stringKey("transport_zone");
    public static final AttributeKey<String> TRANSPORT_SUBZONE =
        AttributeKey.stringKey("transport_subzone");

    public static final AttributeKey<String> CLIENT_UID = AttributeKey.stringKey("client_uid");
    public static final AttributeKey<String> CLIENT_NAME = AttributeKey.stringKey("client_name");
    public static final AttributeKey<String> METHOD_KEY = AttributeKey.stringKey("method");
    public static final AttributeKey<Boolean> STREAMING_KEY = AttributeKey.booleanKey("streaming");
    public static final AttributeKey<String> APP_PROFILE_KEY =
        AttributeKey.stringKey("app_profile");
    public static final AttributeKey<String> DEBUG_TAG_KEY = AttributeKey.stringKey("tag");

    static final AttributeKey<Boolean> APPLIED_KEY = AttributeKey.booleanKey("applied");

    static final AttributeKey<String> CHANNEL_POOL_LB_POLICY = AttributeKey.stringKey("lb_policy");
    static final AttributeKey<String> DP_REASON_KEY = AttributeKey.stringKey("reason");
    static final AttributeKey<String> DP_IP_PREFERENCE_KEY = AttributeKey.stringKey("reason");

    public static final AttributeKey<String> STATUS_KEY = AttributeKey.stringKey("status");

    static final AttributeKey<String> EXECUTOR_KEY = AttributeKey.stringKey("executor");
  }

  static final class Units {
    private Units() {}

    static final String MILLISECOND = "ms";
    static final String MICROSECOND = "us";
    static final String COUNT = "1";
  }

  static final class Buckets {
    static final List<Double> AGGREGATION_WITH_MILLIS_HISTOGRAM =
        ImmutableSortedSet.<Double>naturalOrder()
            // Match `bigtable.googleapis.com/frontend_server/handler_latencies` buckets
            .add(
                new Double[] {
                  0d, 1d, 2d, 3d, 4d, 5d, 6d, 8d, 10d,
                  13d, 16d, 20d, 25d, 30d, 40d, 50d, 65d, 80d,
                  100d, 130d, 160d, 200d, 250d, 300d, 400d, 500d, 650d,
                  800d, 900d, 1_000d, 2_000d, 3_000d, 4_000d, 5_000d, 6_000d, 10_000d,
                  20_000d, 50_000d, 100_000d, 200_000d, 500_000d, 1_000_000d, 2_000_000d, 5_000_000d
                })
            // TODO: figure out what actual additional buckets we want to jetstream
            // add 100us buckets for the first 3ms
            .addAll(generateLinearSeq(0, 3, 0.1))
            .build()
            .asList();
    static final List<Double> HIGH_RES_AGGREGATION_WITH_MILLIS_HISTOGRAM =
        ImmutableSortedSet.<Double>naturalOrder()
            .add(
                new Double[] {
                  0d, 1d, 2d, 3d, 4d, 5d, 6d, 8d, 10d,
                  13d, 16d, 20d, 25d, 30d, 40d, 50d, 65d, 80d,
                  100d, 130d, 160d, 200d, 250d, 300d, 400d, 500d, 650d,
                  800d, 900d, 1_000d, 2_000d, 3_000d, 4_000d, 5_000d, 6_000d, 10_000d,
                  20_000d, 50_000d, 100_000d, 200_000d
                })
            // add 50us buckets for the first 3ms
            .addAll(generateLinearSeq(0, 3, 0.05))
            .build()
            .asList();
    static final List<Long> AGGREGATION_PER_CONNECTION_ERROR_COUNT_HISTOGRAM =
        ImmutableList.<Long>builder()
            .add(0L)
            .addAll(generateGeometricSeq(1, 64))
            .addAll(generateGeometricSeq(125, 1_000_000L))
            .build();
    static final List<Double> PACEMAKER_BUCKET =
        ImmutableList.<Double>builder()
            // Up to 67,108,864, ~1 minute in microseconds
            .addAll(generateExponentialSeq(1.0, 13, 4))
            .build();

    static List<Double> generateLinearSeq(double start, double end, double increment) {
      ImmutableList.Builder<Double> builder = ImmutableList.builder();
      for (int i = 0; true; i++) {
        double next = start + (increment * i);
        if (next > end) {
          break;
        }
        builder.add(next);
      }

      return builder.build();
    }

    static List<Double> generateExponentialSeq(double start, int count, double factor) {
      List<Double> buckets = new ArrayList<>();

      for (int i = 0; i < count; i++) {
        buckets.add(start);
        start *= factor;
      }

      return buckets;
    }

    static List<Long> generateGeometricSeq(long startClose, long endClosed) {
      ImmutableList.Builder<Long> builder = ImmutableList.builder();
      for (long i = startClose; i <= endClosed; i *= 2) {
        builder.add(i);
      }
      return builder.build();
    }
  }
}
