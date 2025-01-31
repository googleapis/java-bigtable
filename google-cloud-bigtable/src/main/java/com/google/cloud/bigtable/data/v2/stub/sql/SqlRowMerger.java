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

import com.google.api.core.InternalApi;
import com.google.bigtable.v2.ExecuteQueryResponse;
import com.google.bigtable.v2.PartialResultSet;
import com.google.cloud.bigtable.data.v2.internal.SqlRow;
import com.google.cloud.bigtable.data.v2.models.sql.ResultSetMetadata;
import com.google.cloud.bigtable.gaxx.reframing.Reframer;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * Used to transform a stream of ExecuteQueryResponse objects into rows. This class is not thread
 * safe.
 */
@InternalApi
public final class SqlRowMerger implements Reframer<SqlRow, ExecuteQueryResponse> {

  private final Queue<SqlRow> queue;
  private ProtoRowsMergingStateMachine stateMachine;
  private final Supplier<ResultSetMetadata> metadataSupplier;
  private Boolean isFirstResponse;

  /**
   * @param metadataSupplier a supplier of {@link ResultSetMetadata}. This is expected to return
   *     successfully once the first call to push has been made.
   *     <p>This exists to facilitate plan refresh that can happen after creation of the row merger.
   */
  public SqlRowMerger(Supplier<ResultSetMetadata> metadataSupplier) {
    this.metadataSupplier = metadataSupplier;
    queue = new ArrayDeque<>();
    isFirstResponse = true;
  }

  /**
   * Used to add responses to the SqlRowMerger as they are received.
   *
   * @param response the next response in the stream of query responses
   */
  @Override
  public void push(ExecuteQueryResponse response) {
    if (isFirstResponse) {
      // Wait until we've received the first response to get the metadata, as a
      // PreparedQuery may need to be refreshed based on initial errors. Once we've
      // received a response, it will never change, even upon request resumption.
      stateMachine = new ProtoRowsMergingStateMachine(metadataSupplier.get());
      isFirstResponse = false;
    }
    Preconditions.checkState(
        response.hasResults(),
        "Expected results response, but received: %s",
        response.getResponseCase().name());
    PartialResultSet results = response.getResults();
    processProtoRows(results);
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
    if (isFirstResponse) {
      return false;
    }
    return hasFullFrame() || stateMachine.isBatchInProgress();
  }

  /** pops a completed row from the FIFO queue built from the given responses. */
  @Override
  public SqlRow pop() {
    return Preconditions.checkNotNull(
        queue.poll(), "SqlRowMerger.pop() called when there are no complete rows.");
  }
}
