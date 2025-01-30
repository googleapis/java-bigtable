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
// source: google/bigtable/v2/data.proto

// Protobuf Java Version: 3.25.3
package com.google.bigtable.v2;

public interface ReadModifyWriteRuleOrBuilder
    extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ReadModifyWriteRule)
    com.google.protobuf.MessageOrBuilder {

  /**
   *
   *
   * <pre>
   * The name of the family to which the read/modify/write should be applied.
   * Must match `[-_.a-zA-Z0-9]+`
   * </pre>
   *
   * <code>string family_name = 1;</code>
   *
   * @return The familyName.
   */
  java.lang.String getFamilyName();
  /**
   *
   *
   * <pre>
   * The name of the family to which the read/modify/write should be applied.
   * Must match `[-_.a-zA-Z0-9]+`
   * </pre>
   *
   * <code>string family_name = 1;</code>
   *
   * @return The bytes for familyName.
   */
  com.google.protobuf.ByteString getFamilyNameBytes();

  /**
   *
   *
   * <pre>
   * The qualifier of the column to which the read/modify/write should be
   * applied.
   * Can be any byte string, including the empty string.
   * </pre>
   *
   * <code>bytes column_qualifier = 2;</code>
   *
   * @return The columnQualifier.
   */
  com.google.protobuf.ByteString getColumnQualifier();

  /**
   *
   *
   * <pre>
   * Rule specifying that `append_value` be appended to the existing value.
   * If the targeted cell is unset, it will be treated as containing the
   * empty string.
   * </pre>
   *
   * <code>bytes append_value = 3;</code>
   *
   * @return Whether the appendValue field is set.
   */
  boolean hasAppendValue();
  /**
   *
   *
   * <pre>
   * Rule specifying that `append_value` be appended to the existing value.
   * If the targeted cell is unset, it will be treated as containing the
   * empty string.
   * </pre>
   *
   * <code>bytes append_value = 3;</code>
   *
   * @return The appendValue.
   */
  com.google.protobuf.ByteString getAppendValue();

  /**
   *
   *
   * <pre>
   * Rule specifying that `increment_amount` be added to the existing value.
   * If the targeted cell is unset, it will be treated as containing a zero.
   * Otherwise, the targeted cell must contain an 8-byte value (interpreted
   * as a 64-bit big-endian signed integer), or the entire request will fail.
   * </pre>
   *
   * <code>int64 increment_amount = 4;</code>
   *
   * @return Whether the incrementAmount field is set.
   */
  boolean hasIncrementAmount();
  /**
   *
   *
   * <pre>
   * Rule specifying that `increment_amount` be added to the existing value.
   * If the targeted cell is unset, it will be treated as containing a zero.
   * Otherwise, the targeted cell must contain an 8-byte value (interpreted
   * as a 64-bit big-endian signed integer), or the entire request will fail.
   * </pre>
   *
   * <code>int64 increment_amount = 4;</code>
   *
   * @return The incrementAmount.
   */
  long getIncrementAmount();

  com.google.bigtable.v2.ReadModifyWriteRule.RuleCase getRuleCase();
}
