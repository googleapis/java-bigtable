/*
 * Copyright 2020 Google LLC
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
// source: google/bigtable/v2/request_stats.proto

package com.google.bigtable.v2;

public interface RequestStatsOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.RequestStats)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * Available with the ReadRowsRequest.RequestStatsView.REQUEST_STATS_FULL
   * view, see package google.bigtable.v2.
   * </pre>
   *
   * <code>.google.bigtable.v2.FullReadStatsView full_read_stats_view = 1;</code>
   *
   * @return Whether the fullReadStatsView field is set.
   */
  boolean hasFullReadStatsView();
  /**
   *
   *
   * <pre>
   * Available with the ReadRowsRequest.RequestStatsView.REQUEST_STATS_FULL
   * view, see package google.bigtable.v2.
   * </pre>
   *
   * <code>.google.bigtable.v2.FullReadStatsView full_read_stats_view = 1;</code>
   *
   * @return The fullReadStatsView.
   */
  com.google.bigtable.v2.FullReadStatsView getFullReadStatsView();
  /**
   *
   *
   * <pre>
   * Available with the ReadRowsRequest.RequestStatsView.REQUEST_STATS_FULL
   * view, see package google.bigtable.v2.
   * </pre>
   *
   * <code>.google.bigtable.v2.FullReadStatsView full_read_stats_view = 1;</code>
   */
  com.google.bigtable.v2.FullReadStatsViewOrBuilder getFullReadStatsViewOrBuilder();

  public com.google.bigtable.v2.RequestStats.StatsViewCase getStatsViewCase();
}
