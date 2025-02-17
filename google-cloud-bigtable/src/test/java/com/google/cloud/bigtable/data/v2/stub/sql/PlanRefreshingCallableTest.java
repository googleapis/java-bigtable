/*
 * Copyright 2024 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.sql;

import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.columnMetadata;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.int64Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.int64Value;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.metadata;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.partialResultSetWithToken;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.partialResultSetWithoutToken;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.prepareResponse;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringValue;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.api.core.SettableApiFuture;
import com.google.bigtable.v2.ExecuteQueryRequest;
import com.google.bigtable.v2.ExecuteQueryResponse;
import com.google.cloud.bigtable.data.v2.internal.PrepareResponse;
import com.google.cloud.bigtable.data.v2.internal.PreparedStatementImpl;
import com.google.cloud.bigtable.data.v2.internal.ProtoResultSetMetadata;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.cloud.bigtable.data.v2.models.sql.PreparedStatement;
import com.google.cloud.bigtable.data.v2.models.sql.ResultSetMetadata;
import com.google.cloud.bigtable.data.v2.stub.sql.PlanRefreshingCallable.MetadataObserver;
import com.google.cloud.bigtable.gaxx.testing.FakeStreamingApi.ServerStreamingStashCallable;
import com.google.cloud.bigtable.gaxx.testing.MockStreamingApi.MockResponseObserver;
import com.google.cloud.bigtable.gaxx.testing.MockStreamingApi.MockServerStreamingCall;
import com.google.cloud.bigtable.gaxx.testing.MockStreamingApi.MockServerStreamingCallable;
import com.google.cloud.bigtable.gaxx.testing.MockStreamingApi.MockStreamController;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PlanRefreshingCallableTest {

  private static final ExecuteQueryRequest FAKE_REQUEST = ExecuteQueryRequest.newBuilder().build();
  private static final com.google.bigtable.v2.ResultSetMetadata METADATA =
      metadata(columnMetadata("foo", stringType()), columnMetadata("bar", int64Type()));
  private static final ExecuteQueryResponse DATA =
      partialResultSetWithToken(stringValue("fooVal"), int64Value(100));

  ExecuteQueryCallContext callContext;
  MockResponseObserver<ExecuteQueryResponse> outerObserver;
  SettableApiFuture<ResultSetMetadata> metadataFuture;
  PlanRefreshingCallable.MetadataObserver observer;

  @Before
  public void setUp() {
    metadataFuture = SettableApiFuture.create();
    PreparedStatement preparedStatement =
        PreparedStatementImpl.create(
            PrepareResponse.fromProto(
                prepareResponse(
                    metadata(
                        columnMetadata("foo", stringType()), columnMetadata("bar", int64Type())))),
            new HashMap<>());

    callContext = ExecuteQueryCallContext.create(preparedStatement.bind().build(), metadataFuture);
    outerObserver = new MockResponseObserver<>(true);
    observer = new MetadataObserver(outerObserver, callContext);
  }

  @Test
  public void observer_doesNotSetFutureUntilTokenReceived()
      throws ExecutionException, InterruptedException {
    MockServerStreamingCallable<ExecuteQueryRequest, ExecuteQueryResponse> innerCallable =
        new MockServerStreamingCallable<>();
    innerCallable.call(FAKE_REQUEST, observer);
    MockServerStreamingCall<ExecuteQueryRequest, ExecuteQueryResponse> lastCall =
        innerCallable.popLastCall();
    MockStreamController<ExecuteQueryResponse> innerController = lastCall.getController();

    innerController.getObserver().onResponse(partialResultSetWithoutToken(stringValue("foo")));
    assertFalse(callContext.resultSetMetadataFuture().isDone());
    innerController.getObserver().onResponse(partialResultSetWithToken(stringValue("bar")));
    assertTrue(callContext.resultSetMetadataFuture().isDone());
    assertThat(callContext.resultSetMetadataFuture().get())
        .isEqualTo(ProtoResultSetMetadata.fromProto(METADATA));
  }

  @Test
  public void observer_setsFutureAndPassesThroughResponses()
      throws ExecutionException, InterruptedException {
    // This has a token so it should finalize the metadata
    ServerStreamingStashCallable<ExecuteQueryRequest, ExecuteQueryResponse> innerCallable =
        new ServerStreamingStashCallable<>(Collections.singletonList(DATA));
    innerCallable.call(FAKE_REQUEST, observer);

    assertThat(metadataFuture.isDone()).isTrue();
    assertThat(metadataFuture.get()).isEqualTo(ProtoResultSetMetadata.fromProto(METADATA));
    assertThat(outerObserver.popNextResponse()).isEqualTo(DATA);
    assertThat(outerObserver.isDone()).isTrue();
    assertThat(outerObserver.getFinalError()).isNull();
  }

  @Test
  public void observer_passThroughOnStart() {
    MockServerStreamingCallable<ExecuteQueryRequest, ExecuteQueryResponse> innerCallable =
        new MockServerStreamingCallable<>();
    innerCallable.call(FAKE_REQUEST, observer);
    MockServerStreamingCall<ExecuteQueryRequest, ExecuteQueryResponse> lastCall =
        innerCallable.popLastCall();
    MockStreamController<ExecuteQueryResponse> innerController = lastCall.getController();

    assertThat(outerObserver.getController()).isEqualTo(innerController);
  }

  @Test
  public void observer_onCompleteWithNoData_resolvesMetadata()
      throws InterruptedException, ExecutionException {
    MockServerStreamingCallable<ExecuteQueryRequest, ExecuteQueryResponse> innerCallable =
        new MockServerStreamingCallable<>();
    innerCallable.call(FAKE_REQUEST, observer);
    MockServerStreamingCall<ExecuteQueryRequest, ExecuteQueryResponse> lastCall =
        innerCallable.popLastCall();
    MockStreamController<ExecuteQueryResponse> innerController = lastCall.getController();

    innerController.getObserver().onComplete();
    assertThat(metadataFuture.get()).isEqualTo(ProtoResultSetMetadata.fromProto(METADATA));
    assertThat(outerObserver.isDone()).isTrue();
    assertThat(outerObserver.getFinalError()).isNull();
  }

  @Test
  public void testCallable() throws ExecutionException, InterruptedException {
    ServerStreamingStashCallable<ExecuteQueryRequest, ExecuteQueryResponse> innerCallable =
        new ServerStreamingStashCallable<>(Collections.singletonList(DATA));
    RequestContext requestContext = RequestContext.create("project", "instance", "profile");
    PlanRefreshingCallable callable = new PlanRefreshingCallable(innerCallable, requestContext);
    MockResponseObserver<ExecuteQueryResponse> outerObserver = new MockResponseObserver<>(true);
    SettableApiFuture<ResultSetMetadata> metadataFuture = SettableApiFuture.create();
    PreparedStatement preparedStatement =
        PreparedStatementImpl.create(
            PrepareResponse.fromProto(
                prepareResponse(
                    metadata(
                        columnMetadata("foo", stringType()), columnMetadata("bar", int64Type())))),
            new HashMap<>());

    ExecuteQueryCallContext callContext =
        ExecuteQueryCallContext.create(preparedStatement.bind().build(), metadataFuture);

    callable.call(callContext, outerObserver);

    assertThat(metadataFuture.isDone()).isTrue();
    assertThat(metadataFuture.get()).isEqualTo(ProtoResultSetMetadata.fromProto(METADATA));
    assertThat(outerObserver.popNextResponse()).isEqualTo(DATA);
    assertThat(outerObserver.isDone()).isTrue();
    assertThat(outerObserver.getFinalError()).isNull();
  }
}
