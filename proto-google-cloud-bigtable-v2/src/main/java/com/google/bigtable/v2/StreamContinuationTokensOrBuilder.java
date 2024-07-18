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
// source: google/bigtable/v2/data.proto

// Protobuf Java Version: 3.25.3
package com.google.bigtable.v2;

public interface StreamContinuationTokensOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.StreamContinuationTokens)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * List of continuation tokens.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.StreamContinuationToken tokens = 1;</code>
   */
  java.util.List<com.google.bigtable.v2.StreamContinuationToken> getTokensList();
  /**
   *
   *
   * <pre>
   * List of continuation tokens.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.StreamContinuationToken tokens = 1;</code>
   */
  com.google.bigtable.v2.StreamContinuationToken getTokens(int index);
  /**
   *
   *
   * <pre>
   * List of continuation tokens.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.StreamContinuationToken tokens = 1;</code>
   */
  int getTokensCount();
  /**
   *
   *
   * <pre>
   * List of continuation tokens.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.StreamContinuationToken tokens = 1;</code>
   */
  java.util.List<? extends com.google.bigtable.v2.StreamContinuationTokenOrBuilder>
      getTokensOrBuilderList();
  /**
   *
   *
   * <pre>
   * List of continuation tokens.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.StreamContinuationToken tokens = 1;</code>
   */
  com.google.bigtable.v2.StreamContinuationTokenOrBuilder getTokensOrBuilder(int index);
}
