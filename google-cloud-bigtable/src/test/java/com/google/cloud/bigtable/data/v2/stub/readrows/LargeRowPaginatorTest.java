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

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.cloud.bigtable.data.v2.models.Filters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LargeRowPaginatorTest {

  @Test
  public void testPaginatorAdvancesProperly() {
    LargeRowPaginator paginator = new LargeRowPaginator(10, null);

    assertThat(paginator.hasNext()).isTrue();

    // Simulate reading exactly the limit (10 cells). Paginator assumes more data exists.
    boolean hasMore = paginator.advance(10);
    assertThat(hasMore).isTrue();
    assertThat(paginator.hasNext()).isTrue();

    // Simulate reading 5 cells (less than limit, indicating end of row)
    hasMore = paginator.advance(5);
    assertThat(hasMore).isFalse();
    assertThat(paginator.hasNext()).isFalse();
  }

  @Test
  public void testPaginatorHalvesLimit() {
    LargeRowPaginator paginator = new LargeRowPaginator(10, null);

    paginator.halveLimit(); // Internal limit becomes 5

    // Simulate reading exactly the new limit (5 cells)
    boolean hasMore = paginator.advance(5);
    assertThat(hasMore).isTrue();

    paginator.halveLimit(); // Internal limit becomes 2

    // Simulate reading 1 cell (less than the new limit of 2)
    hasMore = paginator.advance(1);
    assertThat(hasMore).isFalse();
  }

  @Test
  public void testPaginatorThrowsOnZeroLimit() {
    LargeRowPaginator paginator = new LargeRowPaginator(1, null);

    RuntimeException exception = assertThrows(RuntimeException.class, () -> paginator.halveLimit());
    assertThat(exception).hasMessageThat().contains("Cannot divide limit further");
  }

  @Test
  public void testPaginatorWithBaseFilter() {
    LargeRowPaginator paginator =
        new LargeRowPaginator(10, Filters.FILTERS.family().exactMatch("cf"));

    Filters.Filter nextFilter = paginator.getNextFilter();

    Filters.Filter expectedFilter =
        Filters.FILTERS
            .chain()
            .filter(Filters.FILTERS.family().exactMatch("cf"))
            .filter(Filters.FILTERS.limit().cellsPerRow(10));

    assertThat(nextFilter.toProto()).isEqualTo(expectedFilter.toProto());
  }
}
