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
package com.google.cloud.bigtable.testproxy;

import static com.google.cloud.bigtable.data.v2.models.Filters.FILTERS;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.ServerStream;
import com.google.auth.oauth2.ServiceAccountJwtAccessCredentials;
import com.google.auto.value.AutoValue;
import com.google.cloud.bigtable.testproxy.CloudBigtableV2TestProxyGrpc.CloudBigtableV2TestProxyImplBase;
import com.google.bigtable.v2.Cell;
import com.google.bigtable.v2.Column;
import com.google.bigtable.v2.Family;
import com.google.bigtable.v2.Row;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.ConditionalRowMutation;
import com.google.cloud.bigtable.data.v2.models.KeyOffset;
import com.google.cloud.bigtable.data.v2.models.MutateRowsException;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.ReadModifyWriteRow;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.stub.EnhancedBigtableStubSettings;
import com.google.protobuf.ByteString;
import com.google.protobuf.util.Durations;
import com.google.rpc.Code;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import io.grpc.stub.StreamObserver;
import io.netty.handler.ssl.SslContext;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.threeten.bp.Duration;

/** Java implementation of the CBT test proxy. Used to test the Java CBT client. */
public class CbtTestProxy extends CloudBigtableV2TestProxyImplBase implements Closeable {

  /**
   * Class that holds BigtableDataSettings and a BigtableDataClient created with those settings.
   * Used so users can retrieve settings for a particular client.
   */
  @AutoValue
  abstract static class CbtClient {
    static CbtClient create(BigtableDataSettings settings, BigtableDataClient dataClient) {
      return new AutoValue_CbtTestProxy_CbtClient(settings, dataClient);
    }

    abstract BigtableDataSettings settings();

    abstract BigtableDataClient dataClient();
  }

  private static final Logger logger = Logger.getLogger(CbtTestProxy.class.getName());

  public CbtTestProxy() {
    this.idClientMap = new ConcurrentHashMap<>();
  }

  /**
   * Helper method to override the timeout settings of data APIs.
   * TODO(developer): per-attempt timeout may also be overridden, which will involve test
   * harness update.
   *
   * @param settingsBuilder The Builder object of BigtableDataSettings.
   * @param newTimeout The value that is used to set the timeout.
   */
  private static BigtableDataSettings.Builder overrideTimeoutSetting(
      Duration newTimeout, BigtableDataSettings.Builder settingsBuilder) {
    // TODO(developer): remove the initialRpcTimeout update below by updating the client library.
    Duration initialRpcTimeout =
        settingsBuilder
            .stubSettings()
            .bulkMutateRowsSettings()
            .getRetrySettings()
            .getInitialRpcTimeout();
    if (initialRpcTimeout.compareTo(newTimeout) > 0) {
      // Total timeout is smaller than initialRpcTimeout, which will cause deadline-related problem.
      initialRpcTimeout = newTimeout;
    }
    settingsBuilder.stubSettings()
        .bulkMutateRowsSettings()
        .retrySettings()
        .setTotalTimeout(newTimeout)
        .setInitialRpcTimeout(initialRpcTimeout);

    settingsBuilder.stubSettings().mutateRowSettings().retrySettings().setTotalTimeout(newTimeout);

    settingsBuilder.stubSettings().readRowSettings().retrySettings().setTotalTimeout(newTimeout);

    settingsBuilder.stubSettings().readRowsSettings().retrySettings().setTotalTimeout(newTimeout);

    settingsBuilder.stubSettings().sampleRowKeysSettings().retrySettings().setTotalTimeout(
        newTimeout);

    settingsBuilder.stubSettings().checkAndMutateRowSettings().retrySettings().setTotalTimeout(
        newTimeout);

    settingsBuilder.stubSettings().readModifyWriteRowSettings().retrySettings().setTotalTimeout(
        newTimeout);

    return settingsBuilder;
  }

  /** Helper method to get a client object by its id. */
  private CbtClient getClient(String id) throws StatusException {
    CbtClient client = idClientMap.get(id);
    if (client == null) {
      throw Status.NOT_FOUND.withDescription("Client " + id + " not found.")
          .asException();
    }
    return client;
  }

  @Override
  public synchronized void createClient(
      CreateClientRequest request, StreamObserver<CreateClientResponse> responseObserver) {
    if (request.getClientId().isEmpty()
        || request.getProjectId().isEmpty()
        || request.getInstanceId().isEmpty()
        || request.getDataTarget().isEmpty()) {
      responseObserver.onError(
          Status.INVALID_ARGUMENT
              .withDescription("clientId, projectId, instanceId, and dataTarget must be provided.")
              .asException());
      return;
    }

    if (idClientMap.get(request.getClientId()) != null) {
      responseObserver.onError(
          Status.ALREADY_EXISTS
              .withDescription("Client " + request.getClientId() + " already exists.")
              .asException());
      return;
    }

    BigtableDataSettings.Builder settingsBuilder = BigtableDataSettings.newBuilder();
    if (request.hasPerOperationTimeout()) {
      com.google.protobuf.Duration timeoutFromReq = request.getPerOperationTimeout();
      Duration newTimeout =
          Duration.ofSeconds(timeoutFromReq.getSeconds(), timeoutFromReq.getNanos());
      settingsBuilder = overrideTimeoutSetting(newTimeout, settingsBuilder);
      logger.info(String.format(
          "Total timeout is set to %s for all the methods", Durations.toString(timeoutFromReq)));
    }

    // Create and store CbtClient for later use
    try {
      settingsBuilder.setProjectId(request.getProjectId())
          .setInstanceId(request.getInstanceId())
          .stubSettings()
          .setEndpoint(request.getDataTarget())
          .setTransportChannelProvider(getTransportChannel(request))
          .setCredentialsProvider(getCredentialsProvider(request));
      BigtableDataSettings settings = settingsBuilder.build();
      BigtableDataClient client = BigtableDataClient.create(settings);
      CbtClient cbtClient = CbtClient.create(settings, client);
      idClientMap.put(request.getClientId(), cbtClient);
    } catch (IOException e) {
      responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asException());
      return;
    }

    responseObserver.onNext(CreateClientResponse.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void removeClient(
      RemoveClientRequest request, StreamObserver<RemoveClientResponse> responseObserver) {
    CbtClient client = idClientMap.remove(request.getClientId());
    if (client == null) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("Client " + request.getClientId() + " not found.")
              .asException());
      return;
    }

    client.dataClient().close();

    responseObserver.onNext(RemoveClientResponse.getDefaultInstance());
    responseObserver.onCompleted();
  }

  @Override
  public void mutateRow(
      MutateRowRequest request, StreamObserver<MutateRowResult> responseObserver) {
    CbtClient client = idClientMap.get(request.getClientId());
    if (client == null) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("Client " + request.getClientId() + " not found.")
              .asException());
      return;
    }

    // TODO(developer): evaluate if we want to manually unpack the proto into a model, instead of
    // using fromProto. Same for the other methods.
    RowMutation mutation = RowMutation.fromProto(request.getRequest());
    try {
      // This response is empty.
      client.dataClient().mutateRow(mutation);
    } catch (ApiException e) {
      responseObserver.onNext(
          MutateRowResult.newBuilder()
              .setStatus(
                  com.google.rpc.Status.newBuilder()
                      .setCode(e.getStatusCode().getCode().ordinal())
                      .setMessage(e.getMessage())
                      .build())
              .build());
      responseObserver.onCompleted();
      return;
    }

    responseObserver.onNext(
        MutateRowResult.newBuilder().setStatus(com.google.rpc.Status.getDefaultInstance()).build());
    responseObserver.onCompleted();
  }

  @Override
  public void bulkMutateRows(
      MutateRowsRequest request, StreamObserver<MutateRowsResult> responseObserver) {
    CbtClient client = idClientMap.get(request.getClientId());
    if (client == null) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("Client " + request.getClientId() + " not found.")
              .asException());
      return;
    }

    BulkMutation batch = BulkMutation.fromProto(request.getRequest());
    try {
      client.dataClient().bulkMutateRows(batch);
    } catch (MutateRowsException e) {
      MutateRowsResult.Builder resultBuilder = MutateRowsResult.newBuilder();
      for (MutateRowsException.FailedMutation failed : e.getFailedMutations()) {
        resultBuilder
            .addEntryBuilder()
            .setIndex(failed.getIndex())
            .setStatus(
                com.google.rpc.Status.newBuilder()
                    .setCode(failed.getError().getStatusCode().getCode().ordinal())
                    .setMessage(failed.getError().getMessage())
                    .build());
      }
      responseObserver.onNext(
          resultBuilder
              .setStatus(com.google.rpc.Status.getDefaultInstance())
              .build());
      responseObserver.onCompleted();
      return;
    } catch (ApiException e) {
      responseObserver.onNext(
          MutateRowsResult.newBuilder()
              .setStatus(
                  com.google.rpc.Status.newBuilder()
                      .setCode(e.getStatusCode().getCode().ordinal())
                      .setMessage(e.getMessage())
                      .build())
              .build());
      responseObserver.onCompleted();
      return;
    }

    responseObserver.onNext(
        MutateRowsResult.newBuilder()
            .setStatus(com.google.rpc.Status.getDefaultInstance())
            .build());
    responseObserver.onCompleted();
  }

  @Override
  public void readRow(ReadRowRequest request, StreamObserver<RowResult> responseObserver) {
    CbtClient client = idClientMap.get(request.getClientId());
    if (client == null) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("Client " + request.getClientId() + " not found.")
              .asException());
      return;
    }

    String tableId;
    try {
      tableId = extractTableIdFromTableName(request.getTableName());
    } catch (IllegalArgumentException e) {
      responseObserver.onError(
          Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asException());
      return;
    }

    com.google.cloud.bigtable.data.v2.models.Row row;
    try {
      row = client.dataClient().readRow(
          tableId, request.getRowKey(), FILTERS.fromProto(request.getFilter()));
    } catch (ApiException e) {
      responseObserver.onNext(
          RowResult.newBuilder()
              .setStatus(
                  com.google.rpc.Status.newBuilder()
                      .setCode(e.getStatusCode().getCode().ordinal())
                      .setMessage(e.getMessage())
                      .build())
              .build());
      responseObserver.onCompleted();
      return;
    }

    if (row != null) {
      try {
        RowResult.Builder resultBuilder = convertRowResult(row);
        responseObserver.onNext(
            resultBuilder.setStatus(com.google.rpc.Status.getDefaultInstance()).build());
      } catch (RuntimeException e) {
        // If client encounters problem, don't return any row result.
        responseObserver.onNext(
            RowResult.newBuilder()
                .setStatus(
                    com.google.rpc.Status.newBuilder()
                        .setCode(Code.INTERNAL.getNumber())
                        .setMessage(e.getMessage())
                        .build())
                .build());
        responseObserver.onCompleted();
        return;
      }
    } else {
      logger.info(String.format("readRow() did not find row: %s", request.getRowKey()));
      responseObserver.onNext(
          RowResult.newBuilder().setStatus(com.google.rpc.Status.getDefaultInstance()).build());
    }
    responseObserver.onCompleted();
  }

  @Override
  public void readRows(ReadRowsRequest request, StreamObserver<RowsResult> responseObserver) {
    CbtClient client = idClientMap.get(request.getClientId());
    if (client == null) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("Client " + request.getClientId() + " not found.")
              .asException());
      return;
    }

    ServerStream<com.google.cloud.bigtable.data.v2.models.Row> rows;
    Query query = Query.fromProto(request.getRequest());
    try {
      rows = client.dataClient().readRows(query);
    } catch (ApiException e) {
      responseObserver.onNext(
          RowsResult.newBuilder()
              .setStatus(
                  com.google.rpc.Status.newBuilder()
                      .setCode(e.getStatusCode().getCode().ordinal())
                      .setMessage(e.getMessage())
                      .build())
              .build());
      responseObserver.onCompleted();
      return;
    }

    int cancelAfterRows = request.getCancelAfterRows();
    try {
      RowsResult.Builder resultBuilder = convertRowsResult(rows, cancelAfterRows);
      responseObserver.onNext(
          resultBuilder.setStatus(com.google.rpc.Status.getDefaultInstance()).build());
    } catch (RuntimeException e) {
      // If client encounters problem, don't return any row result.
      responseObserver.onNext(
          RowsResult.newBuilder()
              .setStatus(
                  com.google.rpc.Status.newBuilder()
                      .setCode(Code.INTERNAL.getNumber())
                      .setMessage(e.getMessage())
                      .build())
              .build());
      responseObserver.onCompleted();
      return;
    }

    responseObserver.onCompleted();
  }

  /**
   * Helper method to convert row from type com.google.cloud.bigtable.data.v2.models.Row to type
   * com.google.bigtable.v2.Row. After conversion, row cells within the same column and family are
   * grouped and ordered; but the ordering of family and qualifier is not preserved.
   *
   * @param row Logical row of type com.google.cloud.bigtable.data.v2.models.Row
   * @return the converted row in RowResult Builder
   */
  private static RowResult.Builder convertRowResult(
      com.google.cloud.bigtable.data.v2.models.Row row) {
    Row.Builder rowBuilder = Row.newBuilder();
    rowBuilder.setKey(row.getKey());

    Map<String, Map<ByteString, List<RowCell>>> grouped = row.getCells().stream().collect(
        Collectors.groupingBy(RowCell::getFamily, Collectors.groupingBy(RowCell::getQualifier)));
    for (Map.Entry<String, Map<ByteString, List<RowCell>>> e : grouped.entrySet()) {
      Family.Builder family = rowBuilder.addFamiliesBuilder().setName(e.getKey());

      for (Map.Entry<ByteString, List<RowCell>> e2 : e.getValue().entrySet()) {
        Column.Builder column = family.addColumnsBuilder().setQualifier(e2.getKey());

        for (RowCell rowCell : e2.getValue()) {
          column.addCellsBuilder()
              .setTimestampMicros(rowCell.getTimestamp())
              .setValue(rowCell.getValue())
              .addAllLabels(rowCell.getLabels());
        }
      }
    }

    RowResult.Builder resultBuilder = RowResult.newBuilder();
    resultBuilder.setRow(rowBuilder.build());
    return resultBuilder;
  }

  /**
   * Helper method to convert rows from type com.google.cloud.bigtable.data.v2.models.Row to type
   * com.google.bigtable.v2.Row. Row order is preserved.
   *
   * @param rows Logical rows in ServerStream<com.google.cloud.bigtable.data.v2.models.Row>
   * @param cancelAfterRows Ignore the results after this row if set positive
   * @return the converted rows in RowsResult Builder
   */
  private static RowsResult.Builder convertRowsResult(
      ServerStream<com.google.cloud.bigtable.data.v2.models.Row> rows, int cancelAfterRows) {
    RowsResult.Builder resultBuilder = RowsResult.newBuilder();
    int rowCounter = 0;
    for (com.google.cloud.bigtable.data.v2.models.Row row : rows) {
      rowCounter++;
      RowResult.Builder rowResultBuilder = convertRowResult(row);
      resultBuilder.addRow(rowResultBuilder.getRow());

      if (cancelAfterRows > 0 && rowCounter >= cancelAfterRows) {
        logger.info(
            String.format("Canceling ReadRows() to respect cancel_after_rows=%d", cancelAfterRows));
        break;
      }
    }
    return resultBuilder;
  }

  @Override
  public void sampleRowKeys(
      SampleRowKeysRequest request, StreamObserver<SampleRowKeysResult> responseObserver) {
    CbtClient client = idClientMap.get(request.getClientId());
    if (client == null) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("Client " + request.getClientId() + " not found.")
              .asException());
      return;
    }

    String tableId;
    try {
      tableId = extractTableIdFromTableName(request.getRequest().getTableName());
    } catch (IllegalArgumentException e) {
      responseObserver.onError(
          Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asException());
      return;
    }

    List<KeyOffset> keyOffsets;
    try {
      keyOffsets = client.dataClient().sampleRowKeys(tableId);
    } catch (ApiException e) {
      responseObserver.onNext(
          SampleRowKeysResult.newBuilder()
              .setStatus(
                  com.google.rpc.Status.newBuilder()
                      .setCode(e.getStatusCode().getCode().ordinal())
                      .setMessage(e.getMessage())
                      .build())
              .build());
      responseObserver.onCompleted();
      return;
    }

    SampleRowKeysResult.Builder resultBuilder = SampleRowKeysResult.newBuilder();
    for (KeyOffset keyOffset : keyOffsets) {
      resultBuilder.addSampleBuilder()
          .setRowKey(keyOffset.getKey())
          .setOffsetBytes(keyOffset.getOffsetBytes());
    }
    responseObserver.onNext(
        resultBuilder.setStatus(com.google.rpc.Status.getDefaultInstance()).build());
    responseObserver.onCompleted();
  }

  @Override
  public void checkAndMutateRow(
      CheckAndMutateRowRequest request, StreamObserver<CheckAndMutateRowResult> responseObserver) {
    CbtClient client = idClientMap.get(request.getClientId());
    if (client == null) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("Client " + request.getClientId() + " not found.")
              .asException());
      return;
    }

    ConditionalRowMutation mutation = ConditionalRowMutation.fromProto(request.getRequest());
    Boolean matched;
    try {
      matched = client.dataClient().checkAndMutateRow(mutation);
    } catch (ApiException e) {
      responseObserver.onNext(
          CheckAndMutateRowResult.newBuilder()
              .setStatus(
                  com.google.rpc.Status.newBuilder()
                      .setCode(e.getStatusCode().getCode().ordinal())
                      .setMessage(e.getMessage())
                      .build())
              .build());
      responseObserver.onCompleted();
      return;
    }

    CheckAndMutateRowResult.Builder resultBuilder = CheckAndMutateRowResult.newBuilder();
    resultBuilder.getResultBuilder().setPredicateMatched(matched);
    responseObserver.onNext(
        resultBuilder.setStatus(com.google.rpc.Status.getDefaultInstance()).build());
    responseObserver.onCompleted();
  }

  @Override
  public void readModifyWriteRow(
      ReadModifyWriteRowRequest request, StreamObserver<RowResult> responseObserver) {
    CbtClient client = idClientMap.get(request.getClientId());
    if (client == null) {
      responseObserver.onError(
          Status.NOT_FOUND
              .withDescription("Client " + request.getClientId() + " not found.")
              .asException());
      return;
    }

    com.google.cloud.bigtable.data.v2.models.Row row;
    ReadModifyWriteRow mutation = ReadModifyWriteRow.fromProto(request.getRequest());
    try {
      row = client.dataClient().readModifyWriteRow(mutation);
    } catch (ApiException e) {
      responseObserver.onNext(
          RowResult.newBuilder()
              .setStatus(
                  com.google.rpc.Status.newBuilder()
                      .setCode(e.getStatusCode().getCode().ordinal())
                      .setMessage(e.getMessage())
                      .build())
              .build());
      responseObserver.onCompleted();
      return;
    }

    if (row != null) {
      try {
        RowResult.Builder resultBuilder = convertRowResult(row);
        responseObserver.onNext(
            resultBuilder.setStatus(com.google.rpc.Status.getDefaultInstance()).build());
      } catch (RuntimeException e) {
        // If client encounters problem, fail the whole operation.
        responseObserver.onNext(
            RowResult.newBuilder()
                .setStatus(
                    com.google.rpc.Status.newBuilder()
                        .setCode(Code.INTERNAL.getNumber())
                        .setMessage(e.getMessage())
                        .build())
                .build());
        responseObserver.onCompleted();
        return;
      }
    } else {
      logger.info(String.format(
          "readModifyWriteRow() did not find row: %s", request.getRequest().getRowKey()));
      responseObserver.onNext(
          RowResult.newBuilder().setStatus(com.google.rpc.Status.getDefaultInstance()).build());
    }
    responseObserver.onCompleted();
  }

  @Override
  public synchronized void close() {
    Iterator<Map.Entry<String, CbtClient>> it = idClientMap.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, CbtClient> entry = it.next();
      entry.getValue().dataClient().close();
      it.remove();
    }
  }

  private static String extractTableIdFromTableName(String fullTableName)
      throws IllegalArgumentException {
    Matcher matcher = tablePattern.matcher(fullTableName);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid table name: " + fullTableName);
    }
    return matcher.group(3);
  }

  private static InstantiatingGrpcChannelProvider getTransportChannel(
      final CreateClientRequest request) throws IOException {
    switch (request.getChannelCredential().getValueCase()) {
      case NONE:
        return EnhancedBigtableStubSettings.defaultGrpcTransportProviderBuilder()
            .setChannelConfigurator(
                new ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder>() {
                  @Override
                  public ManagedChannelBuilder apply(ManagedChannelBuilder input) {
                    // Use in test code only.
                    return input.usePlaintext();
                  }
                })
            .build();
      case SSL:
        final SslContext secureContext =
            GrpcSslContexts.forClient()
                .trustManager(
                    new ByteArrayInputStream(
                        request.getChannelCredential().getSsl().getPemRootCerts().getBytes(UTF_8)))
                .build();
        return EnhancedBigtableStubSettings.defaultGrpcTransportProviderBuilder()
            .setChannelConfigurator(
                new ApiFunction<ManagedChannelBuilder, ManagedChannelBuilder>() {
                  @Override
                  public ManagedChannelBuilder apply(ManagedChannelBuilder input) {
                    NettyChannelBuilder channelBuilder = (NettyChannelBuilder) input;
                    channelBuilder
                        .sslContext(secureContext)
                        .overrideAuthority(request.getOverrideSslTargetName());
                    return channelBuilder;
                  }
                })
            .build();
      default:
        // VALUE_NOT_SET.
        return EnhancedBigtableStubSettings.defaultGrpcTransportProviderBuilder().build();
    }
  }

  private static CredentialsProvider getCredentialsProvider(CreateClientRequest request)
      throws IOException {
    switch (request.getCallCredential().getValueCase()) {
      case JSON_SERVICE_ACCOUNT:
        ServiceAccountJwtAccessCredentials credential =
            ServiceAccountJwtAccessCredentials.fromStream(
                new ByteArrayInputStream(
                    request.getCallCredential().getJsonServiceAccount().getBytes(UTF_8)));
        return FixedCredentialsProvider.create(credential);
      default:
        // VALUE_NOT_SET.
        return NoCredentialsProvider.create();
    }
  }

  private final ConcurrentHashMap<String, CbtClient> idClientMap;
  private static final Pattern tablePattern =
      Pattern.compile("projects/([^/]+)/instances/([^/]+)/tables/([^/]+)");
}
