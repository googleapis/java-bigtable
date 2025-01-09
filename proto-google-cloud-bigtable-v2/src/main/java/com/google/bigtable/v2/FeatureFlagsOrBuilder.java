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
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/v2/feature_flags.proto

// Protobuf Java Version: 3.25.5
package com.google.bigtable.v2;

public interface FeatureFlagsOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.FeatureFlags)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Notify the server that the client supports reverse scans. The server will
   * reject ReadRowsRequests with the reverse bit set when this is absent.
   * </pre>
   *
   * <code>bool reverse_scans = 1;</code>
   *
   * @return The reverseScans.
   */
  boolean getReverseScans();

  /**
   *
   *
   * <pre>
   * Notify the server that the client enables batch write flow control by
   * requesting RateLimitInfo from MutateRowsResponse. Due to technical reasons,
   * this disables partial retries.
   * </pre>
   *
   * <code>bool mutate_rows_rate_limit = 3;</code>
   *
   * @return The mutateRowsRateLimit.
   */
  boolean getMutateRowsRateLimit();

  /**
   *
   *
   * <pre>
   * Notify the server that the client enables batch write flow control by
   * requesting RateLimitInfo from MutateRowsResponse. With partial retries
   * enabled.
   * </pre>
   *
   * <code>bool mutate_rows_rate_limit2 = 5;</code>
   *
   * @return The mutateRowsRateLimit2.
   */
  boolean getMutateRowsRateLimit2();

  /**
   *
   *
   * <pre>
   * Notify the server that the client supports the last_scanned_row field
   * in ReadRowsResponse for long-running scans.
   * </pre>
   *
   * <code>bool last_scanned_row_responses = 4;</code>
   *
   * @return The lastScannedRowResponses.
   */
  boolean getLastScannedRowResponses();

  /**
   *
   *
   * <pre>
   * Notify the server that the client supports using encoded routing cookie
   * strings to retry requests with.
   * </pre>
   *
   * <code>bool routing_cookie = 6;</code>
   *
   * @return The routingCookie.
   */
  boolean getRoutingCookie();

  /**
   *
   *
   * <pre>
   * Notify the server that the client supports using retry info back off
   * durations to retry requests with.
   * </pre>
   *
   * <code>bool retry_info = 7;</code>
   *
   * @return The retryInfo.
   */
  boolean getRetryInfo();

  /**
   *
   *
   * <pre>
   * Notify the server that the client has client side metrics enabled.
   * </pre>
   *
   * <code>bool client_side_metrics_enabled = 8;</code>
   *
   * @return The clientSideMetricsEnabled.
   */
  boolean getClientSideMetricsEnabled();

  /**
   *
   *
   * <pre>
   * Notify the server that the client using Traffic Director endpoint.
   * </pre>
   *
   * <code>bool traffic_director_enabled = 9;</code>
   *
   * @return The trafficDirectorEnabled.
   */
  boolean getTrafficDirectorEnabled();

  /**
   *
   *
   * <pre>
   * Notify the server that the client explicitly opted in for Direct Access.
   * </pre>
   *
   * <code>bool direct_access_requested = 10;</code>
   *
   * @return The directAccessRequested.
   */
  boolean getDirectAccessRequested();
}
