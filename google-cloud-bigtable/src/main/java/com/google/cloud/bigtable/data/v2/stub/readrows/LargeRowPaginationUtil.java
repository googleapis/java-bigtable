/*
 * Copyright 2026 Google LLC
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

import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode.Code;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Filters;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.TableId;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Public utility class to read a large row by paginating over cells. */
public final class LargeRowPaginationUtil {
  private static final Logger LOGGER = Logger.getLogger(LargeRowPaginationUtil.class.getName());

  private LargeRowPaginationUtil() {}

  /**
   * Reads a large row by paginating over cells. 1. Reads the total count of cells without fetching
   * values using strip filter. 2. Reads cells in chunks using limit and offset filters. 3. Divides
   * the chunk size by half if a failure occurs with FAILED_PRECONDITION.
   */
  public static Row readLargeRow(BigtableDataClient client, String tableId, ByteString rowKey) {
    // Step 1: Read count with strip
    Row countRow = client.readRow(TableId.of(tableId), rowKey, Filters.FILTERS.value().strip());
    if (countRow == null) {
      return null; // row not found
    }
    int totalCells = countRow.getCells().size();

    List<RowCell> resultCells = Lists.newArrayList();
    int offset = 0;
    int limit = totalCells; // start with trying to read all cells

    while (offset < totalCells) {
      try {
        Filters.ChainFilter chain = Filters.FILTERS.chain();
        if (offset > 0) {
          chain.filter(Filters.FILTERS.offset().cellsPerRow(offset));
        }
        if (limit > 0) {
          chain.filter(Filters.FILTERS.limit().cellsPerRow(limit));
        }

        Row partialRow = client.readRow(TableId.of(tableId), rowKey, chain);
        Preconditions.checkState(partialRow != null);
        resultCells.addAll(partialRow.getCells());
        offset += partialRow.getCells().size();
        // Reset limit to try reading remaining cells
        limit = totalCells - offset;
      } catch (ApiException e) {
        if (e.getStatusCode().getCode() != Code.FAILED_PRECONDITION) {
          throw e;
        }
        LOGGER.log(
            Level.FINE,
            "Failed to read chunk with limit {0} at offset {1}. Dividing limit by half.",
            new Object[] {limit, offset});
        limit = limit / 2;
        if (limit == 0) {
          throw new RuntimeException("Cannot divide limit further. Cell might be too large.", e);
        }
      }
    }
    return Row.create(rowKey, resultCells);
  }
}
