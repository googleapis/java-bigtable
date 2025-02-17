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
package com.google.cloud.bigtable.data.v2.stub.sql;

import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.columnMetadata;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.metadata;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.partialResultSetWithToken;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.partialResultSetWithoutToken;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.partialResultSets;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.prepareResponse;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.tokenOnlyResultSet;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.StatusCode;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.ExecuteQueryRequest;
import com.google.bigtable.v2.ExecuteQueryResponse;
import com.google.bigtable.v2.ResultSetMetadata;
import com.google.bigtable.v2.Value;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.PrepareResponse;
import com.google.cloud.bigtable.data.v2.internal.PreparedStatementImpl;
import com.google.cloud.bigtable.data.v2.models.sql.PreparedStatement;
import com.google.cloud.bigtable.data.v2.models.sql.ResultSet;
import com.google.cloud.bigtable.data.v2.models.sql.SqlType;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.cloud.bigtable.gaxx.reframing.IncompleteStreamException;
import com.google.common.collect.ImmutableMap;
import com.google.common.truth.Truth;
import com.google.protobuf.ByteString;
import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcServerRule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import javax.annotation.Nullable;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ExecuteQueryRetryTest {
  private static final String PROJECT_ID = "fake-project";
  private static final String INSTANCE_ID = "fake-instance";
  private static final String APP_PROFILE_ID = "fake-app-profile";
  private static final ByteString PREPARED_QUERY = ByteString.copyFromUtf8("foo");
  private static final ResultSetMetadata DEFAULT_METADATA =
      metadata(columnMetadata("strCol", stringType()));

  @Rule public GrpcServerRule serverRule = new GrpcServerRule();
  private TestBigtableService service;
  private BigtableDataClient client;
  private PreparedStatement preparedStatement;

  @Before
  public void setUp() throws IOException {
    service = new TestBigtableService();
    serverRule.getServiceRegistry().addService(service);

    BigtableDataSettings.Builder settings =
        BigtableDataSettings.newBuilder()
            .setProjectId(PROJECT_ID)
            .setInstanceId(INSTANCE_ID)
            .setAppProfileId(APP_PROFILE_ID)
            .setCredentialsProvider(NoCredentialsProvider.create());

    settings
        .stubSettings()
        .setTransportChannelProvider(
            FixedTransportChannelProvider.create(
                GrpcTransportChannel.create(serverRule.getChannel())))
        // Refreshing channel doesn't work with FixedTransportChannelProvider
        .setRefreshingChannel(false)
        .build();

    client = BigtableDataClient.create(settings.build());
    preparedStatement =
        PreparedStatementImpl.create(
            PrepareResponse.fromProto(prepareResponse(PREPARED_QUERY, DEFAULT_METADATA)),
            new HashMap<>());
  }

  @After
  public void tearDown() {
    if (client != null) {
      client.close();
    }
  }

  @Test
  public void testAllSuccesses() {
    service.addExpectation(
        RpcExpectation.create()
            .respondWith(
                partialResultSetWithoutToken(stringValue("foo")),
                partialResultSetWithoutToken(stringValue("bar")),
                partialResultSetWithToken(stringValue("baz"))));
    ResultSet rs = client.executeQuery(preparedStatement.bind().build());
    assertThat(rs.getMetadata().getColumns()).hasSize(1);
    assertThat(rs.getMetadata().getColumns().get(0).name()).isEqualTo("strCol");
    assertThat(rs.getMetadata().getColumns().get(0).type()).isEqualTo(SqlType.string());

    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("foo");
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("bar");
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("baz");
    assertThat(rs.next()).isFalse();
    rs.close();
  }

  @Test
  public void testRetryOnInitialError() {
    // - First attempt immediately fails
    // - Second attempt returns 'foo', w a token, and succeeds
    // Expect result to be 'foo'
    service.addExpectation(RpcExpectation.create().respondWithStatus(Code.UNAVAILABLE));
    service.addExpectation(
        RpcExpectation.create().respondWith(partialResultSetWithToken(stringValue("foo"))));

    ResultSet rs = client.executeQuery(preparedStatement.bind().build());
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("foo");
    assertThat(rs.next()).isFalse();
    rs.close();
    assertThat(service.requestCount).isEqualTo(2);
  }

  @Test
  public void testResumptionToken() {
    // - First attempt gets a response with a token, and then fails with unavailable
    // - Second Expects the request to contain the previous token, and returns a new response w
    //   token and then fails with unavailable
    // - Third expects the request to contain the second token, returns a new response w token
    //   and then succeeds
    // We expect the results to contain all of the returned data (no reset batches)
    service.addExpectation(
        RpcExpectation.create()
            .respondWith(
                partialResultSetWithToken(ByteString.copyFromUtf8("token1"), stringValue("foo")))
            .respondWithStatus(Code.UNAVAILABLE));
    service.addExpectation(
        RpcExpectation.create()
            .withResumeToken(ByteString.copyFromUtf8("token1"))
            .respondWith(
                partialResultSetWithToken(ByteString.copyFromUtf8("token2"), stringValue("bar")))
            .respondWithStatus(Code.UNAVAILABLE));
    service.addExpectation(
        RpcExpectation.create()
            .withResumeToken(ByteString.copyFromUtf8("token2"))
            .respondWith(
                partialResultSetWithToken(ByteString.copyFromUtf8("final"), stringValue("baz"))));

    ResultSet rs = client.executeQuery(preparedStatement.bind().build());
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("foo");
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("bar");
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("baz");
    assertThat(rs.next()).isFalse();
    rs.close();
    assertThat(service.requestCount).isEqualTo(3);
  }

  @Test
  public void testResetOnResumption() {
    // - First attempt returns 'foo' with 'token1', then 'discard' w no token, then fails
    // - Second attempt should resume w 'token1', returns an incomplete batch of two chunks. First
    //   chunk contains the reset bit and a some data, second contains some data, we fail w/o
    //   returning the final chunk w a token.
    // - Third attempt should resume w 'token1', we return 'baz' w reset & a token, succeed
    // Expect the results to be 'foo' and 'baz'
    service.addExpectation(
        RpcExpectation.create()
            .respondWith(
                partialResultSetWithToken(ByteString.copyFromUtf8("token1"), stringValue("foo")),
                // This is after the token so should be dropped
                partialResultSetWithoutToken(stringValue("discard")))
            .respondWithStatus(Code.UNAVAILABLE));
    List<ExecuteQueryResponse> chunkedResponses =
        partialResultSets(
            3,
            true,
            ByteString.copyFromUtf8("token2"),
            stringValue("longerStringDiscard"),
            stringValue("discard"));
    service.addExpectation(
        RpcExpectation.create()
            .withResumeToken(ByteString.copyFromUtf8("token1"))
            // Skip the last response, so we don't send a new token
            .respondWith(chunkedResponses.get(0), chunkedResponses.get(1))
            .respondWithStatus(Code.UNAVAILABLE));
    service.addExpectation(
        RpcExpectation.create()
            .withResumeToken(ByteString.copyFromUtf8("token1"))
            .respondWith(
                partialResultSets(1, true, ByteString.copyFromUtf8("final"), stringValue("baz"))
                    .get(0)));

    ResultSet rs = client.executeQuery(preparedStatement.bind().build());
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("foo");
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("baz");
    assertThat(rs.next()).isFalse();
    rs.close();
    assertThat(service.requestCount).isEqualTo(3);
  }

  @Test
  public void testErrorAfterFinalData() {
    // - First attempt returns 'foo', 'bar', 'baz' w 'finalToken' but fails w unavailable
    // - Second attempt uses 'finalToken' and succeeds
    // Expect results to be 'foo', 'bar', 'baz'
    service.addExpectation(
        RpcExpectation.create()
            .respondWith(
                partialResultSetWithoutToken(stringValue("foo")),
                partialResultSetWithoutToken(stringValue("bar")),
                partialResultSetWithToken(
                    ByteString.copyFromUtf8("finalToken"), stringValue("baz")))
            .respondWithStatus(Code.UNAVAILABLE));
    service.addExpectation(
        RpcExpectation.create().withResumeToken(ByteString.copyFromUtf8("finalToken")));
    ResultSet rs = client.executeQuery(preparedStatement.bind().build());
    assertThat(rs.getMetadata().getColumns()).hasSize(1);
    assertThat(rs.getMetadata().getColumns().get(0).name()).isEqualTo("strCol");
    assertThat(rs.getMetadata().getColumns().get(0).type()).isEqualTo(SqlType.string());

    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("foo");
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("bar");
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("baz");
    assertThat(rs.next()).isFalse();
    rs.close();
  }

  // TODO test changing metadata when plan refresh is implemented

  @Test
  public void permanentErrorPropagatesToMetadata() {
    service.addExpectation(RpcExpectation.create().respondWithStatus(Code.INVALID_ARGUMENT));

    ResultSet rs = client.executeQuery(preparedStatement.bind().build());
    ApiException e = assertThrows(ApiException.class, rs::getMetadata);
    assertThat(e.getStatusCode().getCode()).isEqualTo(StatusCode.Code.INVALID_ARGUMENT);
  }

  @Test
  public void exhaustedRetriesPropagatesToMetadata() throws IOException {
    int attempts =
        EnhancedBigtableStubSettings.newBuilder()
            .executeQuerySettings()
            .getRetrySettings()
            .getMaxAttempts();
    assertThat(attempts).isGreaterThan(1);
    for (int i = 0; i < attempts; i++) {
      service.addExpectation(RpcExpectation.create().respondWithStatus(Code.UNAVAILABLE));
    }

    ResultSet rs = client.executeQuery(preparedStatement.bind().build());
    ApiException e = assertThrows(ApiException.class, rs::getMetadata);
    assertThat(e.getStatusCode().getCode()).isEqualTo(StatusCode.Code.UNAVAILABLE);
  }

  @Test
  public void retryableErrorWithSuccessfulRetryDoesNotPropagateToMetadata() {
    service.addExpectation(RpcExpectation.create().respondWithStatus(Code.UNAVAILABLE));
    service.addExpectation(RpcExpectation.create().respondWithStatus(Code.UNAVAILABLE));
    service.addExpectation(
        RpcExpectation.create().respondWith(tokenOnlyResultSet(ByteString.copyFromUtf8("t"))));
    ResultSet rs = client.executeQuery(preparedStatement.bind().build());
    assertThat(rs.getMetadata().getColumns()).hasSize(1);
  }

  @Test
  public void preservesParamsOnRetry() {
    Map<String, SqlType<?>> paramTypes = ImmutableMap.of("strParam", SqlType.string());
    PreparedStatement preparedStatementWithParams =
        PreparedStatementImpl.create(
            PrepareResponse.fromProto(
                prepareResponse(metadata(columnMetadata("strCol", stringType())))),
            paramTypes);
    Map<String, Value> params =
        ImmutableMap.of("strParam", stringValue("foo").toBuilder().setType(stringType()).build());
    service.addExpectation(
        RpcExpectation.create()
            .withParams(params)
            .respondWith(
                partialResultSetWithToken(ByteString.copyFromUtf8("token1"), stringValue("foo")))
            .respondWithStatus(Code.UNAVAILABLE));
    service.addExpectation(
        RpcExpectation.create()
            .withParams(params)
            .withResumeToken(ByteString.copyFromUtf8("token1"))
            .respondWith(
                partialResultSetWithToken(ByteString.copyFromUtf8("token2"), stringValue("bar"))));

    ResultSet rs =
        client.executeQuery(
            preparedStatementWithParams.bind().setStringParam("strParam", "foo").build());
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("foo");
    assertThat(rs.next()).isTrue();
    assertThat(rs.getString("strCol")).isEqualTo("bar");
    assertThat(rs.next()).isFalse();
  }

  @Test
  public void failsOnCompleteWithOpenPartialBatch() {
    // Return 'foo' with no token, followed by ok
    // This should throw an error, as the backend has violated its contract
    service.addExpectation(
        RpcExpectation.create()
            .respondWith(partialResultSetWithoutToken(stringValue("foo")))
            .respondWithStatus(Code.OK));
    ResultSet rs = client.executeQuery(preparedStatement.bind().build());
    assertThrows(IncompleteStreamException.class, rs::next);
  }

  private static class TestBigtableService extends BigtableGrpc.BigtableImplBase {
    Queue<RpcExpectation> expectations = new LinkedBlockingDeque<>();
    int requestCount = 0;

    void addExpectation(RpcExpectation expectation) {
      expectations.add(expectation);
    }

    @Override
    public void executeQuery(
        ExecuteQueryRequest request, StreamObserver<ExecuteQueryResponse> responseObserver) {
      RpcExpectation expectedRpc = expectations.poll();
      requestCount++;
      int requestIndex = requestCount - 1;

      Truth.assertWithMessage("Unexpected request#" + requestIndex + ":" + request.toString())
          .that(expectedRpc)
          .isNotNull();
      Truth.assertWithMessage("Unexpected request#" + requestIndex)
          .that(request)
          .isEqualTo(expectedRpc.getExpectedRequest());

      for (ExecuteQueryResponse response : expectedRpc.responses) {
        responseObserver.onNext(response);
      }
      if (expectedRpc.statusCode.toStatus().isOk()) {
        responseObserver.onCompleted();
      } else if (expectedRpc.exception != null) {
        responseObserver.onError(expectedRpc.exception);
      } else {
        responseObserver.onError(expectedRpc.statusCode.toStatus().asRuntimeException());
      }
    }
  }

  private static class RpcExpectation {
    ExecuteQueryRequest.Builder request;
    Status.Code statusCode;
    @Nullable ApiException exception;
    List<ExecuteQueryResponse> responses;

    private RpcExpectation() {
      this.request = ExecuteQueryRequest.newBuilder();
      this.request.setPreparedQuery(PREPARED_QUERY);
      this.request.setInstanceName(NameUtil.formatInstanceName(PROJECT_ID, INSTANCE_ID));
      this.request.setAppProfileId(APP_PROFILE_ID);
      this.statusCode = Code.OK;
      this.responses = new ArrayList<>();
    }

    static RpcExpectation create() {
      return new RpcExpectation();
    }

    RpcExpectation withResumeToken(ByteString resumeToken) {
      this.request.setResumeToken(resumeToken);
      return this;
    }

    RpcExpectation withParams(Map<String, Value> params) {
      this.request.putAllParams(params);
      return this;
    }

    RpcExpectation respondWithStatus(Status.Code code) {
      this.statusCode = code;
      return this;
    }

    RpcExpectation respondWithException(Status.Code code, ApiException exception) {
      this.statusCode = code;
      this.exception = exception;
      return this;
    }

    RpcExpectation respondWith(ExecuteQueryResponse... responses) {
      this.responses = Arrays.asList(responses);
      return this;
    }

    ExecuteQueryRequest getExpectedRequest() {
      return this.request.build();
    }
  }
}
