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
package com.google.cloud.bigtable.admin.v2;

import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.grpc.GrpcCallSettings;
import com.google.api.gax.grpc.GrpcCallableFactory;
import com.google.api.gax.grpc.ProtoOperationTransformers.MetadataTransformer;
import com.google.api.gax.grpc.ProtoOperationTransformers.ResponseTransformer;
import com.google.api.gax.longrunning.OperationSnapshot;
import com.google.api.gax.longrunning.OperationTimedPollAlgorithm;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.ApiExceptions;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.OperationCallSettings;
import com.google.api.gax.rpc.OperationCallable;
import com.google.api.gax.rpc.UnaryCallSettings;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.admin.v2.OptimizeRestoredTableMetadata;
import com.google.cloud.bigtable.admin.v2.models.ConsistencyRequest;
import com.google.cloud.bigtable.admin.v2.models.OptimizeRestoredTableOperationToken;
import com.google.cloud.bigtable.admin.v2.models.RestoredTableResult;
import com.google.cloud.bigtable.admin.v2.stub.AwaitConsistencyCallable;
import com.google.cloud.bigtable.admin.v2.stub.BigtableTableAdminStub;
import com.google.cloud.bigtable.admin.v2.stub.BigtableTableAdminStubSettings;
import com.google.common.base.Strings;
import com.google.longrunning.Operation;
import com.google.protobuf.Empty;
import io.grpc.MethodDescriptor;
import io.grpc.MethodDescriptor.Marshaller;
import io.grpc.MethodDescriptor.MethodType;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import org.threeten.bp.Duration;

/**
 * Modern Cloud Bigtable Table Admin Client.
 *
 * <p>This client extends the auto-generated {@link BaseBigtableTableAdminClient} to provide manual
 * overrides and additional convenience methods for Critical User Journeys (CUJs) that the GAPIC
 * generator cannot handle natively (e.g., chained Long Running Operations, Consistency Polling).
 */
public class BigtableTableAdminClientV2 extends BaseBigtableTableAdminClient {
  private final AwaitConsistencyCallable awaitConsistencyCallable;
  private final OperationCallable<Void, Empty, OptimizeRestoredTableMetadata>
      optimizeRestoredTableOperationBaseCallable;

  protected BigtableTableAdminClientV2(BaseBigtableTableAdminSettings settings) throws IOException {
    super(settings);
    this.awaitConsistencyCallable =
        createAwaitConsistencyCallable((BigtableTableAdminStubSettings) settings.getStubSettings());
    this.optimizeRestoredTableOperationBaseCallable =
        createOptimizeRestoredTableOperationBaseCallable(
            (BigtableTableAdminStubSettings) settings.getStubSettings());
  }

  protected BigtableTableAdminClientV2(BigtableTableAdminStub stub) {
    super(stub);
    this.awaitConsistencyCallable = null;
    this.optimizeRestoredTableOperationBaseCallable = null;
  }

  private AwaitConsistencyCallable createAwaitConsistencyCallable(
      BigtableTableAdminStubSettings settings) throws IOException {
    ClientContext clientContext = ClientContext.create(settings);
    // TODO(igorbernstein2): expose polling settings
    RetrySettings pollingSettings =
        RetrySettings.newBuilder()
            .setTotalTimeout(
                settings.checkConsistencySettings().getRetrySettings().getTotalTimeout())
            .setInitialRetryDelay(Duration.ofSeconds(10))
            .setRetryDelayMultiplier(1.0)
            .setMaxRetryDelay(Duration.ofSeconds(10))
            .setInitialRpcTimeout(Duration.ZERO)
            .setMaxRpcTimeout(Duration.ZERO)
            .setRpcTimeoutMultiplier(1.0)
            .build();

    return AwaitConsistencyCallable.create(
        getStub().generateConsistencyTokenCallable(),
        getStub().checkConsistencyCallable(),
        clientContext,
        pollingSettings);
  }

  private OperationCallable<Void, Empty, OptimizeRestoredTableMetadata>
      createOptimizeRestoredTableOperationBaseCallable(BigtableTableAdminStubSettings settings)
          throws IOException {
    ClientContext clientContext = ClientContext.create(settings);

    GrpcCallSettings<Void, Operation> unusedInitialCallSettings =
        GrpcCallSettings.create(
            MethodDescriptor.<Void, Operation>newBuilder()
                .setType(MethodType.UNARY)
                .setFullMethodName(
                    "google.bigtable.admin.v2.BigtableTableAdmin/OptimizeRestoredTable")
                .setRequestMarshaller(
                    new Marshaller<Void>() {
                      @Override
                      public InputStream stream(Void value) {
                        throw new UnsupportedOperationException("not used");
                      }

                      @Override
                      public Void parse(InputStream stream) {
                        throw new UnsupportedOperationException("not used");
                      }
                    })
                .setResponseMarshaller(
                    new Marshaller<Operation>() {
                      @Override
                      public InputStream stream(Operation value) {
                        throw new UnsupportedOperationException("not used");
                      }

                      @Override
                      public Operation parse(InputStream stream) {
                        throw new UnsupportedOperationException("not used");
                      }
                    })
                .build());

    final MetadataTransformer<OptimizeRestoredTableMetadata> protoMetadataTransformer =
        MetadataTransformer.create(OptimizeRestoredTableMetadata.class);

    final ResponseTransformer<com.google.protobuf.Empty> protoResponseTransformer =
        ResponseTransformer.create(com.google.protobuf.Empty.class);

    OperationCallSettings<Void, Empty, OptimizeRestoredTableMetadata> operationCallSettings =
        OperationCallSettings.<Void, Empty, OptimizeRestoredTableMetadata>newBuilder()
            .setInitialCallSettings(
                UnaryCallSettings.<Void, OperationSnapshot>newUnaryCallSettingsBuilder()
                    .setSimpleTimeoutNoRetries(Duration.ZERO)
                    .build())
            .setMetadataTransformer(
                new ApiFunction<OperationSnapshot, OptimizeRestoredTableMetadata>() {
                  @Override
                  public OptimizeRestoredTableMetadata apply(OperationSnapshot input) {
                    return protoMetadataTransformer.apply(input);
                  }
                })
            .setResponseTransformer(
                new ApiFunction<OperationSnapshot, Empty>() {
                  @Override
                  public Empty apply(OperationSnapshot input) {
                    return protoResponseTransformer.apply(input);
                  }
                })
            .setPollingAlgorithm(
                OperationTimedPollAlgorithm.create(
                    RetrySettings.newBuilder()
                        .setInitialRetryDelay(Duration.ofMillis(500L))
                        .setRetryDelayMultiplier(1.5)
                        .setMaxRetryDelay(Duration.ofMillis(5000L))
                        .setInitialRpcTimeout(Duration.ZERO)
                        .setRpcTimeoutMultiplier(1.0)
                        .setMaxRpcTimeout(Duration.ZERO)
                        .setTotalTimeout(Duration.ofMillis(600000L))
                        .build()))
            .build();

    return GrpcCallableFactory.createOperationCallable(
        unusedInitialCallSettings,
        operationCallSettings,
        clientContext,
        getStub().getOperationsStub());
  }

  /** Constructs an instance of BigtableTableAdminClientV2 with the given settings. */
  public static final BigtableTableAdminClientV2 createClient(
      BaseBigtableTableAdminSettings settings) throws IOException {
    return new BigtableTableAdminClientV2(settings);
  }

  /** Constructs an instance of BigtableTableAdminClientV2 with the given stub. */
  public static final BigtableTableAdminClientV2 createClient(BigtableTableAdminStub stub) {
    return new BigtableTableAdminClientV2(stub);
  }

  /**
   * Awaits the completion of the "Optimize Restored Table" operation.
   *
   * <p>This method blocks until the restore operation is complete, extracts the optimization token,
   * and returns an ApiFuture for the optimization phase.
   *
   * @param restoreFuture The future returned by restoreTableAsync().
   * @return An ApiFuture that tracks the optimization progress.
   */
  public ApiFuture<Empty> awaitOptimizeRestoredTable(ApiFuture<RestoredTableResult> restoreFuture) {
    // 1. Block and wait for the restore operation to complete
    RestoredTableResult result;
    try {
      result = restoreFuture.get();
    } catch (Exception e) {
      throw new RuntimeException("Restore operation failed", e);
    }

    // 2. Extract the operation token from the result
    // (RestoredTableResult already wraps the OptimizeRestoredTableOperationToken)
    OptimizeRestoredTableOperationToken token = result.getOptimizeRestoredTableOperationToken();

    if (token == null || Strings.isNullOrEmpty(token.getOperationName())) {
      // If there is no optimization operation, return immediate success.
      return ApiFutures.immediateFuture(Empty.getDefaultInstance());
    }

    // 3. Return the future for the optimization operation
    return getOptimizeRestoredTableCallable().resumeFutureCall(token.getOperationName());
  }

  /**
   * Awaits a restored table is fully optimized.
   *
   * <p>Sample code
   *
   * <pre>{@code
   * RestoredTableResult result =
   *     client.restoreTable(RestoreTableRequest.of(clusterId, backupId).setTableId(tableId));
   * client.awaitOptimizeRestoredTable(result.getOptimizeRestoredTableOperationToken());
   * }</pre>
   */
  public void awaitOptimizeRestoredTable(OptimizeRestoredTableOperationToken token)
      throws ExecutionException, InterruptedException {
    awaitOptimizeRestoredTableAsync(token).get();
  }

  /**
   * Awaits a restored table is fully optimized asynchronously.
   *
   * <p>Sample code
   *
   * <pre>{@code
   * RestoredTableResult result =
   *     client.restoreTable(RestoreTableRequest.of(clusterId, backupId).setTableId(tableId));
   * ApiFuture<Void> future = client.awaitOptimizeRestoredTableAsync(
   *     result.getOptimizeRestoredTableOperationToken());
   *
   * ApiFutures.addCallback(
   *   future,
   *   new ApiFutureCallback<Void>() {
   *     public void onSuccess(Void unused) {
   *       System.out.println("The optimization of the restored table is done.");
   *     }
   *
   *     public void onFailure(Throwable t) {
   *       t.printStackTrace();
   *     }
   *   },
   *   MoreExecutors.directExecutor()
   * );
   * }</pre>
   */
  public ApiFuture<Void> awaitOptimizeRestoredTableAsync(
      OptimizeRestoredTableOperationToken token) {
    ApiFuture<Empty> emptyFuture =
        getOptimizeRestoredTableCallable().resumeFutureCall(token.getOperationName());
    return ApiFutures.transform(
        emptyFuture,
        new com.google.api.core.ApiFunction<Empty, Void>() {
          @Override
          public Void apply(Empty input) {
            return null;
          }
        },
        com.google.common.util.concurrent.MoreExecutors.directExecutor());
  }

  /**
   * Polls an existing consistency token until table replication is consistent across all clusters.
   * Useful for checking consistency of a token generated in a separate process. Blocks until
   * completion.
   *
   * @param tableName The fully qualified table name to check.
   * @param consistencyToken The token to poll.
   */
  public void waitForConsistency(String tableName, String consistencyToken) {
    ApiExceptions.callAndTranslateApiException(
        waitForConsistencyAsync(tableName, consistencyToken));
  }

  /**
   * Asynchronously polls the consistency token. Returns a future that completes when table
   * replication is consistent across all clusters.
   *
   * @param tableName The fully qualified table name to check.
   * @param consistencyToken The token to poll.
   */
  public ApiFuture<Void> waitForConsistencyAsync(String tableName, String consistencyToken) {
    return getAwaitConsistencyCallable()
        .futureCall(ConsistencyRequest.forReplicationFromTableName(tableName, consistencyToken));
  }

  private UnaryCallable<ConsistencyRequest, Void> getAwaitConsistencyCallable() {
    if (awaitConsistencyCallable != null) {
      return awaitConsistencyCallable;
    }
    // Fallback for tests or stub-based initialization
    if (getStub()
        instanceof com.google.cloud.bigtable.admin.v2.stub.EnhancedBigtableTableAdminStub) {
      return ((com.google.cloud.bigtable.admin.v2.stub.EnhancedBigtableTableAdminStub) getStub())
          .awaitConsistencyCallable();
    }
    throw new IllegalStateException("AwaitConsistencyCallable not initialized.");
  }

  private OperationCallable<Void, Empty, OptimizeRestoredTableMetadata>
      getOptimizeRestoredTableCallable() {
    if (optimizeRestoredTableOperationBaseCallable != null) {
      return optimizeRestoredTableOperationBaseCallable;
    }
    // Fallback for tests or stub-based initialization
    if (getStub()
        instanceof com.google.cloud.bigtable.admin.v2.stub.EnhancedBigtableTableAdminStub) {
      return ((com.google.cloud.bigtable.admin.v2.stub.EnhancedBigtableTableAdminStub) getStub())
          .awaitOptimizeRestoredTableCallable();
    }
    throw new IllegalStateException("OptimizeRestoredTableCallable not initialized.");
  }
}
