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
package com.google.cloud.bigtable.data.v2.stub.readrows;

import com.google.api.core.InternalApi;
import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.ApiException;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsRequest.Builder;
import com.google.bigtable.v2.RowSet;
import com.google.cloud.bigtable.data.v2.internal.RowSetUtil;
import com.google.cloud.bigtable.data.v2.models.RowAdapter;
import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An implementation of a {@link StreamResumptionStrategy} for merged rows. This class tracks -
 *
 * <ul>
 *   <li>row key for the last row that was read successfully
 *   <li>row key for large-row that couldn't be read
 *   <li>list of all row keys for large-rows
 * </ul>
 *
 * Upon retry this class builds a request to omit the large rows & retry from the last row key that
 * was read successfully off.
 *
 * <p>This class is considered an internal implementation detail and not meant to be used by
 * applications.
 */
@InternalApi
public class LargeReadRowsResumptionStrategy<RowT>
    implements StreamResumptionStrategy<ReadRowsRequest, RowT> {
  private final RowAdapter<RowT> rowAdapter;
  private ByteString lastSuccessKey = ByteString.EMPTY;
  // Number of rows processed excluding Marker row.
  private long numProcessed;
  private ByteString largeRowKey = ByteString.EMPTY;
  private AtomicInteger largeRowsCount = new AtomicInteger(0);

  private List<ByteString> largeRowKeys = new ArrayList<ByteString>();

  private ReadRowsRequest originalRequest;

  private ReadRowsRequest lastModifiedRequestOnTimeOfError;

  private RowSet previousFailedRequestRowset = null;

  public LargeReadRowsResumptionStrategy(RowAdapter<RowT> rowAdapter) {
    this.rowAdapter = rowAdapter;
  }

  @Override
  public boolean canResume() {
    return true;
  }

  @Override
  public StreamResumptionStrategy<ReadRowsRequest, RowT> createNew() {
    return new LargeReadRowsResumptionStrategy<>(rowAdapter);
  }

  public StreamResumptionStrategy<ReadRowsRequest, RowT> createNew(
      ReadRowsRequest originalRequest) {
    this.originalRequest = originalRequest;
    return new LargeReadRowsResumptionStrategy<>(rowAdapter);
  }

  @Override
  public RowT processResponse(RowT response) {
    // Last key can come from both the last processed row key and a synthetic row marker. The
    // synthetic row marker is emitted when the server has read a lot of data that was filtered out.
    // The row marker can be used to trim the start of the scan, but does not contribute to the row
    // limit.
    lastSuccessKey = rowAdapter.getKey(response);

    if (!rowAdapter.isScanMarkerRow(response)) {
      // Only real rows count towards the rows limit.
      numProcessed++;
    }
    this.largeRowKey = ByteString.EMPTY;
    return response;
  }

  public void setLargeRowKey(Throwable t) {
    String rowKeyExtracted = extractLargeRowKey(t);
    if (rowKeyExtracted != null) {
      this.largeRowKey = ByteString.copyFromUtf8(rowKeyExtracted);
      largeRowsCount.addAndGet(1);
      this.largeRowKeys.add(largeRowKey);
    }
  }

  /**
   * This method should be implemented to expose the large-row keys ({@link
   * LargeReadRowsResumptionStrategy#largeRowKeys}) to application via side channel/dlq or some
   * other way ToDo (@sarthakbhutani) : add implementation of this method as described above
   */
  public void dumpLargeRowKeys() {}

  private String extractLargeRowKey(Throwable t) {
    if (t instanceof ApiException
        && ((ApiException) t).getReason() != null
        && ((ApiException) t).getReason().equals("LargeRowReadError")) {
      return ((ApiException) t).getMetadata().get("rowKey");
    }
    return null;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Given a request, this implementation will narrow that request to exclude all row keys and
   * ranges that would produce rows that come before {@link #lastSuccessKey}. Furthermore this
   * implementation takes care to update the row limit of the request to account for all of the
   * received rows.
   */
  @Override
  public ReadRowsRequest getResumeRequest(ReadRowsRequest originalRequest) {

    // An empty lastSuccessKey means that we have not successfully read the first row,
    // so resume with the original request object.
    if (lastSuccessKey.isEmpty() && largeRowKey.isEmpty()) {
      return originalRequest;
    }

    RowSet remaining;
    if (previousFailedRequestRowset == null) {
      remaining = originalRequest.getRows();
    } else {
      remaining = previousFailedRequestRowset;
    }

    if (!lastSuccessKey.isEmpty()) {
      remaining = RowSetUtil.erase(remaining, lastSuccessKey, !originalRequest.getReversed());
    }
    if (!largeRowKey.isEmpty()) {
      remaining =
          RowSetUtil.createSplitRanges(remaining, largeRowKey, !originalRequest.getReversed());
    }
    this.largeRowKey = ByteString.EMPTY;

    previousFailedRequestRowset = remaining;

    // Edge case: retrying a fulfilled request.
    // A fulfilled request is one that has had all of its row keys and ranges fulfilled, or if it
    // had a row limit, has seen enough rows. These requests are replaced with a marker request that
    // will be handled by ReadRowsRetryCompletedCallable. See docs in ReadRowsRetryCompletedCallable
    // for more details.
    if (remaining == null
        || (originalRequest.getRowsLimit() > 0 && originalRequest.getRowsLimit() == numProcessed)) {
      return ReadRowsRetryCompletedCallable.FULFILLED_REQUEST_MARKER;
    }

    Builder builder = originalRequest.toBuilder().setRows(remaining);

    if (originalRequest.getRowsLimit() > 0) {
      Preconditions.checkState(
          originalRequest.getRowsLimit() > numProcessed + largeRowsCount.get(),
          "Processed rows and number of large rows should not exceed the row limit in the original request");
      builder.setRowsLimit(originalRequest.getRowsLimit() - numProcessed - largeRowsCount.get());
    }

    return builder.build();
  }
}
