/*
 * Copyright 2022 Google LLC
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
package com.google.cloud.bigtable.data.v2.internal;

import com.google.api.core.InternalApi;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.cloud.bigtable.data.v2.models.DefaultRowAdapter.DefaultRowBuilder;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.stub.readrows.RowMerger;
import java.util.ArrayList;
import java.util.List;

@InternalApi("For internal google use only")
public class RowMergerUtil {
  public static List<Row> parseReadRowsResponses(Iterable<ReadRowsResponse> responses) {
    RowMerger<Row> merger = new RowMerger<>(new DefaultRowBuilder());
    List<Row> rows = new ArrayList<>();

    for (ReadRowsResponse response : responses) {
      merger.push(response);
      while (merger.hasFullFrame()) {
        rows.add(merger.pop());
      }
    }

    if (merger.hasPartialFrame()) {
      throw new IllegalStateException("Incomplete response stream, merger has partial data");
    }
    return rows;
  }
}
