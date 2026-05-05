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

import com.google.api.core.InternalApi;
import com.google.cloud.bigtable.data.v2.models.Filters;
import javax.annotation.Nullable;

/**
 * A paginator for fetching large rows from Bigtable chunk by chunk to avoid the 256MB size limit.
 * It yields Filters that chunk the row by cell limits and handles limit halving if
 * FAILED_PRECONDITION occurs.
 */
@InternalApi("For internal usage only")
public class LargeRowPaginator {
  private int currentLimit;
  private int currentOffset;
  private boolean hasMore;
  @Nullable private final Filters.Filter baseFilter;

  public LargeRowPaginator(int initialLimit, @Nullable Filters.Filter filter) {
    this.currentLimit = initialLimit;
    this.currentOffset = 0;
    this.hasMore = true;
    this.baseFilter = filter;
  }

  /** Yields the filter required to fetch the next chunk of cells for the large row. */
  public Filters.Filter getNextFilter() {
    Filters.ChainFilter chain = Filters.FILTERS.chain();
    if (baseFilter != null) {
      chain.filter(baseFilter);
    }
    if (currentOffset > 0) {
      chain.filter(Filters.FILTERS.offset().cellsPerRow(currentOffset));
    }
    chain.filter(Filters.FILTERS.limit().cellsPerRow(currentLimit));
    return chain;
  }

  /**
   * Advances the internal offset. Call this after a successful Bigtable API call.
   *
   * @param cellsReadInLastChunk The number of cells returned in the last chunk.
   * @return true if there are potentially more cells to fetch.
   */
  public boolean advance(int cellsReadInLastChunk) {
    this.currentOffset += cellsReadInLastChunk;

    // If we read fewer cells than requested, we've hit the end of the row.
    if (cellsReadInLastChunk < currentLimit) {
      this.hasMore = false;
    }
    return this.hasMore;
  }

  /**
   * Call this if the Bigtable API call fails with a FAILED_PRECONDITION due to size limits. It
   * reduces the batch size to fetch a smaller chunk on the next attempt.
   */
  public void halveLimit() {
    this.currentLimit /= 2;
    if (this.currentLimit == 0) {
      throw new RuntimeException("Cannot divide limit further. A single cell might be too large.");
    }
  }

  public boolean hasNext() {
    return this.hasMore;
  }
}
