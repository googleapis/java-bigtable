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
package com.google.cloud.bigtable.data.v2.stub.sql;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.bigtable.v2.ExecuteQueryResponse;
import com.google.bigtable.v2.PartialResultSet;
import com.google.bigtable.v2.ResultSetMetadata;
import com.google.bigtable.v2.ResultSetMetadata.SchemaCase;
import com.google.cloud.bigtable.data.v2.internal.SqlRow;
import com.google.cloud.bigtable.gaxx.reframing.Reframer;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;
import javax.annotation.Nullable;

/**
 * Used to transform a stream of ExecuteQueryResponse objects into rows. This class is not thread
 * safe.
 */
@InternalApi
@BetaApi
public final class SqlRowMerger implements Reframer<SqlRow, ExecuteQueryResponse> {

  enum State {
    AWAITING_METADATA,
    PROCESSING_DATA,
  }

  private final Queue<SqlRow> queue;
  private ResultSetMetadata metadata;
  private ProtoRowsMergingStateMachine stateMachine;
  private State currentState;

  public SqlRowMerger() {
    queue = new ArrayDeque<>();
    currentState = State.AWAITING_METADATA;
  }

  /**
   * Used to add responses to the SqlRowMerger as they are received.
   *
   * @param response the next response in the stream of query responses
   */
  // Suppress this because it won't be forced to be exhaustive once it is open-sourced, so we want a
  // default.
  @SuppressWarnings("UnnecessaryDefaultInEnumSwitch")
  @Override
  public void push(ExecuteQueryResponse response) {
    switch (currentState) {
      case AWAITING_METADATA:
        Preconditions.checkState(
            response.hasMetadata(),
            "Expected metadata response, but received: %s",
            response.getResponseCase().name());
        metadata = response.getMetadata();
        Preconditions.checkState(
            metadata.getSchemaCase().equals(SchemaCase.PROTO_SCHEMA),
            "Unrecognized Schema type: %s",
            metadata.getSchemaCase());
        stateMachine = new ProtoRowsMergingStateMachine(metadata.getProtoSchema());
        currentState = State.PROCESSING_DATA;
        break;
      case PROCESSING_DATA:
        Preconditions.checkState(
            response.hasResults(),
            "Expected results response, but received: %s",
            response.getResponseCase().name());
        PartialResultSet results = response.getResults();
        processProtoRows(results);
        break;
      default:
        throw new IllegalStateException("Unknown State: " + currentState.name());
    }
  }

  private void processProtoRows(PartialResultSet results) {
    stateMachine.addPartialResultSet(results);
    if (stateMachine.hasCompleteBatch()) {
      stateMachine.populateQueue(queue);
    }
  }

  /**
   * Check if the merger has consumable data
   *
   * @return true if there is a complete row, false otherwise.
   */
  @Override
  public boolean hasFullFrame() {
    return !queue.isEmpty();
  }

  /**
   * Check if the merger contains partially complete (or complete) data.
   *
   * @return true if there is a partial (or complete) batch, false otherwise.
   */
  @Override
  public boolean hasPartialFrame() {
    switch (currentState) {
      case AWAITING_METADATA:
        return false;
      case PROCESSING_DATA:
        return hasFullFrame() || stateMachine.isBatchInProgress();
      default:
        throw new IllegalStateException("Unknown State: " + currentState.name());
    }
  }

  /** pops a completed row from the FIFO queue built from the given responses. */
  @Override
  public SqlRow pop() {
    return Preconditions.checkNotNull(
        queue.poll(), "SqlRowMerger.pop() called when there are no complete rows.");
  }

  /**
   * Get the metadata for the current query.
   *
   * @return the {@link ResultSetMetadata} for the current query. Null until the query has received
   *     the metadata. The metadata will be the first response in the stream.
   */
  @Nullable
  // TODO(jackdingilian): update this to return a wrapper instead of the raw proto.
  // TODO(jackdingilian): per design doc this will be updated to block until metadata is populated.
  public ResultSetMetadata getMetadata() {
    return this.metadata;
  }

  /**
   * Check if the merger has received the metadata.
   *
   * @return true if the SqlRowMerger has received the metadata for the current query, false
   *     otherwise
   */
  public boolean hasMetadata() {
    return this.metadata != null;
  }
}
