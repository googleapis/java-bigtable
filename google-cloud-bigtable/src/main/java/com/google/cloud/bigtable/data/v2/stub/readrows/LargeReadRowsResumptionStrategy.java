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
import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.InternalException;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsRequest.Builder;
import com.google.bigtable.v2.RowSet;
import com.google.cloud.bigtable.data.v2.internal.RowSetUtil;
import com.google.cloud.bigtable.data.v2.models.RowAdapter;
import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An implementation of a {@link StreamResumptionStrategy} for merged rows. This class tracks the
 * last complete row seen and upon retry can build a request to resume the stream from where it left
 * off.
 *
 * <p>This class is considered an internal implementation detail and not meant to be used by
 * applications.
 */
@InternalApi
public class LargeReadRowsResumptionStrategy<RowT>
    implements StreamResumptionStrategy<ReadRowsRequest, RowT> {
  private final RowAdapter<RowT> rowAdapter;
  private ByteString lastKey = ByteString.EMPTY;
  private ByteString lastSuccessKey = ByteString.EMPTY;
  // Number of rows processed excluding Marker row.
  private long numProcessed;
  private ByteString largeRowKey = ByteString.EMPTY;
  private AtomicInteger largeRowsCount = new AtomicInteger(0);


  // sarthak - would this be threadsafe? we can have multiple readrowrequest going async at the same time. this won't create an issue, ri8?
  private ReadRowsRequest originalRequest;

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

  public StreamResumptionStrategy<ReadRowsRequest, RowT> createNew(ReadRowsRequest originalRequest) {
    this.originalRequest = originalRequest;
    return new LargeReadRowsResumptionStrategy<>(rowAdapter);
  }


  // Sarthak - what's a synthetic row marker
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

  public void setLargeRowKey(Throwable t){
    this.largeRowKey = ByteString.copyFromUtf8(extractLargeRowKey(t));
    largeRowsCount.addAndGet(1);
  }


  private String extractLargeRowKey(Throwable t){
    if (t instanceof InternalException && ((InternalException) t).getReason().equals("LargeRowReadError")){
      return  ((InternalException) t).getMetadata().get("rowKey");
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

    // Sarthak - sarthakImp - assuming that I am getting largeRowKey from the error & I am not incrementing numsProcessed on OnError or on processResponse of this faulty error.
    // Sarthak - sarthakImp - make sure you dont increase the numsProcessed for the same row on retry


    // An empty lastSuccessKey means that we have not successfully read the first row,
    // so resume with the original request object.
    if (lastSuccessKey.isEmpty() && largeRowKey.isEmpty()) {
      return originalRequest;
    }

    RowSet remaining;

    remaining =
        RowSetUtil.erase(originalRequest.getRows(), lastSuccessKey, !originalRequest.getReversed());
    if(!largeRowKey.isEmpty()){
      // skip the row that was faulty -> that would be the split point
      remaining =
          RowSetUtil.createSplitRanges(remaining, largeRowKey, !originalRequest.getReversed());
    }
    this.largeRowKey = ByteString.EMPTY;

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

    // sarthak - didn't understand this
    if (originalRequest.getRowsLimit() > 0) {
      Preconditions.checkState(
          originalRequest.getRowsLimit() > numProcessed + largeRowsCount.get(),
          "Detected too many rows for the current row limit during a retry.");
      builder.setRowsLimit(originalRequest.getRowsLimit() - numProcessed - largeRowsCount.get());
    }

    return builder.build();
  //   we are building the new request here - where & how is it getting sent -> is this after retry block or before -> not able to tie them together
  }
}
