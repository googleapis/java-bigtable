/*
 * Copyright 2018 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.readrows;

import com.google.api.core.InternalApi;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.cloud.bigtable.data.v2.models.RowAdapter.RowBuilder;
import com.google.cloud.bigtable.gaxx.reframing.Reframer;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * An implementation of a {@link Reframer} that feeds the row merging {@link StateMachine}.
 *
 * <p>{@link com.google.cloud.bigtable.gaxx.reframing.ReframingResponseObserver} pushes {@link
 * ReadRowsResponse.CellChunk}s into this class and pops fully merged logical rows. Example usage:
 *
 * <pre>{@code
 * RowMerger<Row> rowMerger = new RowMerger<>(myRowBuilder);
 *
 * while(responseIterator.hasNext()) {
 *   ReadRowsResponse response = responseIterator.next();
 *
 *   if (rowMerger.hasFullFrame()) {
 *     Row row = rowMerger.pop();
 *     // Do something with row.
 *   } else {
 *     rowMerger.push(response);
 *   }
 * }
 *
 * if (rowMerger.hasPartialFrame()) {
 *   throw new RuntimeException("Incomplete stream");
 * }
 *
 * }</pre>
 *
 * <p>This class is considered an internal implementation detail and not meant to be used by
 * applications.
 *
 * <p>Package-private for internal use.
 *
 * @see com.google.cloud.bigtable.gaxx.reframing.ReframingResponseObserver for more details
 */
@InternalApi
public class RowMerger<RowT> implements Reframer<RowT, ReadRowsResponse> {
  private final StateMachine<RowT> stateMachine;
  private Queue<RowT> mergedRows;

  public RowMerger(RowBuilder<RowT> rowBuilder, boolean reversed) {
    stateMachine = new StateMachine<>(rowBuilder, reversed);
    mergedRows = new ArrayDeque<>();
  }

  @Override
  public void push(ReadRowsResponse response) {
    // If the server sends a scan heartbeat, notify the StateMachine. It will generate a synthetic
    // row marker. See RowAdapter for more info.
    if (!response.getLastScannedRowKey().isEmpty()) {
      stateMachine.handleLastScannedRow(response.getLastScannedRowKey());
      if (stateMachine.hasCompleteRow()) {
        mergedRows.add(stateMachine.consumeRow());
      }
    }
    for (ReadRowsResponse.CellChunk cellChunk : response.getChunksList()) {
      stateMachine.handleChunk(cellChunk);
      if (stateMachine.hasCompleteRow()) {
        mergedRows.add(stateMachine.consumeRow());
      }
    }
  }

  @Override
  public boolean hasFullFrame() {
    return !mergedRows.isEmpty();
  }

  @Override
  public boolean hasPartialFrame() {
    // Check if buffer in this class contains data. If an assembled is still not available, then
    // that means `buffer` has been fully consumed. The last place to check is the StateMachine
    // buffer, to see if its holding on to an incomplete row.
    return hasFullFrame() || stateMachine.isRowInProgress();
  }

  @Override
  public RowT pop() {
    return Preconditions.checkNotNull(
        mergedRows.poll(), "RowMerger.pop() called when there are no rows");
  }
}
