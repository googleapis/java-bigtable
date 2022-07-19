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
package com.google.cloud.bigtable.data.v2.stub.changestream;

import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import com.google.bigtable.v2.ListChangeStreamPartitionsRequest;
import com.google.bigtable.v2.ListChangeStreamPartitionsResponse;
import com.google.bigtable.v2.RowRange;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;

/** Simple wrapper for ListChangeStreamPartitions to wrap the request and response protobufs. */
public class ListChangeStreamPartitionsUserCallable
    extends ServerStreamingCallable<String, RowRange> {
  private final RequestContext requestContext;
  private final ServerStreamingCallable<
          ListChangeStreamPartitionsRequest, ListChangeStreamPartitionsResponse>
      inner;

  public ListChangeStreamPartitionsUserCallable(
      ServerStreamingCallable<ListChangeStreamPartitionsRequest, ListChangeStreamPartitionsResponse>
          inner,
      RequestContext requestContext) {
    this.requestContext = requestContext;
    this.inner = inner;
  }

  @Override
  public void call(
      String tableId, ResponseObserver<RowRange> responseObserver, ApiCallContext context) {
    String tableName =
        NameUtil.formatTableName(
            requestContext.getProjectId(), requestContext.getInstanceId(), tableId);
    ListChangeStreamPartitionsRequest request =
        ListChangeStreamPartitionsRequest.newBuilder()
            .setTableName(tableName)
            .setAppProfileId(requestContext.getAppProfileId())
            .build();

    inner.call(request, new ConvertPartitionToRangeObserver(responseObserver), context);
  }

  private class ConvertPartitionToRangeObserver
      implements ResponseObserver<ListChangeStreamPartitionsResponse> {

    private final ResponseObserver<RowRange> outerObserver;

    ConvertPartitionToRangeObserver(ResponseObserver<RowRange> observer) {
      this.outerObserver = observer;
    }

    @Override
    public void onStart(final StreamController controller) {
      outerObserver.onStart(controller);
    }

    @Override
    public void onResponse(ListChangeStreamPartitionsResponse response) {
      RowRange rowRange =
          RowRange.newBuilder()
              .setStartKeyClosed(response.getPartition().getRowRange().getStartKeyClosed())
              .setEndKeyOpen(response.getPartition().getRowRange().getEndKeyOpen())
              .build();
      outerObserver.onResponse(rowRange);
    }

    @Override
    public void onError(Throwable t) {
      outerObserver.onError(t);
    }

    @Override
    public void onComplete() {
      outerObserver.onComplete();
    }
  }
}
