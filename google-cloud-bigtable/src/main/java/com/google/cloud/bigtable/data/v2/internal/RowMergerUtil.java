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
