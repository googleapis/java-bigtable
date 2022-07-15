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

import static com.google.common.truth.Truth.assertThat;

import com.google.bigtable.v2.ListChangeStreamPartitionsRequest;
import com.google.bigtable.v2.ListChangeStreamPartitionsResponse;
import com.google.bigtable.v2.RowRange;
import com.google.bigtable.v2.StreamPartition;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.cloud.bigtable.data.v2.models.Range.ByteStringRange;
import com.google.cloud.bigtable.gaxx.testing.FakeStreamingApi;
import com.google.common.collect.Lists;
import com.google.common.truth.Truth;
import com.google.protobuf.ByteString;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ListChangeStreamPartitionsUserCallableTest {
  private final RequestContext requestContext =
      RequestContext.create("my-project", "my-instance", "my-profile");

  @Test
  public void requestIsCorrect() {
    FakeStreamingApi.ServerStreamingStashCallable<
            ListChangeStreamPartitionsRequest, ListChangeStreamPartitionsResponse>
        inner = new FakeStreamingApi.ServerStreamingStashCallable<>(Lists.newArrayList());
    ListChangeStreamPartitionsUserCallable listChangeStreamPartitionsUserCallable =
        new ListChangeStreamPartitionsUserCallable(inner, requestContext);

    listChangeStreamPartitionsUserCallable.all().call("my-table");
    assertThat(inner.getActualRequest())
        .isEqualTo(
            ListChangeStreamPartitionsRequest.newBuilder()
                .setTableName(
                    NameUtil.formatTableName(
                        requestContext.getProjectId(), requestContext.getInstanceId(), "my-table"))
                .setAppProfileId(requestContext.getAppProfileId())
                .build());
  }

  @Test
  public void responseIsConverted() {
    FakeStreamingApi.ServerStreamingStashCallable<
            ListChangeStreamPartitionsRequest, ListChangeStreamPartitionsResponse>
        inner =
            new FakeStreamingApi.ServerStreamingStashCallable<>(
                Lists.newArrayList(
                    ListChangeStreamPartitionsResponse.newBuilder()
                        .setPartition(
                            StreamPartition.newBuilder()
                                .setRowRange(
                                    RowRange.newBuilder()
                                        .setStartKeyClosed(ByteString.copyFromUtf8("apple"))
                                        .setEndKeyOpen(ByteString.copyFromUtf8("banana"))
                                        .build())
                                .build())
                        .build()));
    ListChangeStreamPartitionsUserCallable listChangeStreamPartitionsUserCallable =
        new ListChangeStreamPartitionsUserCallable(inner, requestContext);

    List<ByteStringRange> results = listChangeStreamPartitionsUserCallable.all().call("my-table");
    Truth.assertThat(results).containsExactly(ByteStringRange.create("apple", "banana"));
  }
}
