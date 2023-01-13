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
// source: google/bigtable/v2/bigtable.proto

package com.google.bigtable.v2;

public interface ReadRowsResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.bigtable.v2.ReadRowsResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * A collection of a row's contents as part of the read request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ReadRowsResponse.CellChunk chunks = 1;</code>
   */
  java.util.List<com.google.bigtable.v2.ReadRowsResponse.CellChunk> 
      getChunksList();
  /**
   * <pre>
   * A collection of a row's contents as part of the read request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ReadRowsResponse.CellChunk chunks = 1;</code>
   */
  com.google.bigtable.v2.ReadRowsResponse.CellChunk getChunks(int index);
  /**
   * <pre>
   * A collection of a row's contents as part of the read request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ReadRowsResponse.CellChunk chunks = 1;</code>
   */
  int getChunksCount();
  /**
   * <pre>
   * A collection of a row's contents as part of the read request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ReadRowsResponse.CellChunk chunks = 1;</code>
   */
  java.util.List<? extends com.google.bigtable.v2.ReadRowsResponse.CellChunkOrBuilder> 
      getChunksOrBuilderList();
  /**
   * <pre>
   * A collection of a row's contents as part of the read request.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ReadRowsResponse.CellChunk chunks = 1;</code>
   */
  com.google.bigtable.v2.ReadRowsResponse.CellChunkOrBuilder getChunksOrBuilder(
      int index);

  /**
   * <pre>
   * Optionally the server might return the row key of the last row it
   * has scanned.  The client can use this to construct a more
   * efficient retry request if needed: any row keys or portions of
   * ranges less than this row key can be dropped from the request.
   * This is primarily useful for cases where the server has read a
   * lot of data that was filtered out since the last committed row
   * key, allowing the client to skip that work on a retry.
   * </pre>
   *
   * <code>bytes last_scanned_row_key = 2;</code>
   * @return The lastScannedRowKey.
   */
  com.google.protobuf.ByteString getLastScannedRowKey();

  /**
   * <pre>
   * If requested, provide enhanced query performance statistics. The semantics
   * dictate:
   *   * request_stats is empty on every (streamed) response, except
   *   * request_stats has non-empty information after all chunks have been
   *     streamed, where the ReadRowsResponse message only contains
   *     request_stats.
   *       * For example, if a read request would have returned an empty
   *         response instead a single ReadRowsResponse is streamed with empty
   *         chunks and request_stats filled.
   * Visually, response messages will stream as follows:
   *    ... -&gt; {chunks: [...]} -&gt; {chunks: [], request_stats: {...}}
   *   &#92;______________________/  &#92;________________________________/
   *       Primary response         Trailer of RequestStats info
   * Or if the read did not return any values:
   *   {chunks: [], request_stats: {...}}
   *   &#92;________________________________/
   *      Trailer of RequestStats info
   * </pre>
   *
   * <code>.google.bigtable.v2.RequestStats request_stats = 3;</code>
   * @return Whether the requestStats field is set.
   */
  boolean hasRequestStats();
  /**
   * <pre>
   * If requested, provide enhanced query performance statistics. The semantics
   * dictate:
   *   * request_stats is empty on every (streamed) response, except
   *   * request_stats has non-empty information after all chunks have been
   *     streamed, where the ReadRowsResponse message only contains
   *     request_stats.
   *       * For example, if a read request would have returned an empty
   *         response instead a single ReadRowsResponse is streamed with empty
   *         chunks and request_stats filled.
   * Visually, response messages will stream as follows:
   *    ... -&gt; {chunks: [...]} -&gt; {chunks: [], request_stats: {...}}
   *   &#92;______________________/  &#92;________________________________/
   *       Primary response         Trailer of RequestStats info
   * Or if the read did not return any values:
   *   {chunks: [], request_stats: {...}}
   *   &#92;________________________________/
   *      Trailer of RequestStats info
   * </pre>
   *
   * <code>.google.bigtable.v2.RequestStats request_stats = 3;</code>
   * @return The requestStats.
   */
  com.google.bigtable.v2.RequestStats getRequestStats();
  /**
   * <pre>
   * If requested, provide enhanced query performance statistics. The semantics
   * dictate:
   *   * request_stats is empty on every (streamed) response, except
   *   * request_stats has non-empty information after all chunks have been
   *     streamed, where the ReadRowsResponse message only contains
   *     request_stats.
   *       * For example, if a read request would have returned an empty
   *         response instead a single ReadRowsResponse is streamed with empty
   *         chunks and request_stats filled.
   * Visually, response messages will stream as follows:
   *    ... -&gt; {chunks: [...]} -&gt; {chunks: [], request_stats: {...}}
   *   &#92;______________________/  &#92;________________________________/
   *       Primary response         Trailer of RequestStats info
   * Or if the read did not return any values:
   *   {chunks: [], request_stats: {...}}
   *   &#92;________________________________/
   *      Trailer of RequestStats info
   * </pre>
   *
   * <code>.google.bigtable.v2.RequestStats request_stats = 3;</code>
   */
  com.google.bigtable.v2.RequestStatsOrBuilder getRequestStatsOrBuilder();
}
