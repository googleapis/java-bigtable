/*
 * Copyright 2024 Google LLC
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
// source: google/bigtable/v2/bigtable.proto

// Protobuf Java Version: 3.25.4
package com.google.bigtable.v2;

public interface MutateRowsResponseOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.MutateRowsResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * One or more results for Entries from the batch request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.MutateRowsResponse.Entry entries = 1;</code>
   */
  java.util.List<com.google.bigtable.v2.MutateRowsResponse.Entry> getEntriesList();
  /**
   *
   *
   * <pre>
   * One or more results for Entries from the batch request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.MutateRowsResponse.Entry entries = 1;</code>
   */
  com.google.bigtable.v2.MutateRowsResponse.Entry getEntries(int index);
  /**
   *
   *
   * <pre>
   * One or more results for Entries from the batch request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.MutateRowsResponse.Entry entries = 1;</code>
   */
  int getEntriesCount();
  /**
   *
   *
   * <pre>
   * One or more results for Entries from the batch request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.MutateRowsResponse.Entry entries = 1;</code>
   */
  java.util.List<? extends com.google.bigtable.v2.MutateRowsResponse.EntryOrBuilder>
      getEntriesOrBuilderList();
  /**
   *
   *
   * <pre>
   * One or more results for Entries from the batch request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.MutateRowsResponse.Entry entries = 1;</code>
   */
  com.google.bigtable.v2.MutateRowsResponse.EntryOrBuilder getEntriesOrBuilder(int index);

  /**
   *
   *
   * <pre>
   * Information about how client should limit the rate (QPS). Primirily used by
   * supported official Cloud Bigtable clients. If unset, the rate limit info is
   * not provided by the server.
   * </pre>
   *
   * <code>optional .google.bigtable.v2.RateLimitInfo rate_limit_info = 3;</code>
   *
   * @return Whether the rateLimitInfo field is set.
   */
  boolean hasRateLimitInfo();
  /**
   *
   *
   * <pre>
   * Information about how client should limit the rate (QPS). Primirily used by
   * supported official Cloud Bigtable clients. If unset, the rate limit info is
   * not provided by the server.
   * </pre>
   *
   * <code>optional .google.bigtable.v2.RateLimitInfo rate_limit_info = 3;</code>
   *
   * @return The rateLimitInfo.
   */
  com.google.bigtable.v2.RateLimitInfo getRateLimitInfo();
  /**
   *
   *
   * <pre>
   * Information about how client should limit the rate (QPS). Primirily used by
   * supported official Cloud Bigtable clients. If unset, the rate limit info is
   * not provided by the server.
   * </pre>
   *
   * <code>optional .google.bigtable.v2.RateLimitInfo rate_limit_info = 3;</code>
   */
  com.google.bigtable.v2.RateLimitInfoOrBuilder getRateLimitInfoOrBuilder();
}
