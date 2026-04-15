/*
 * Copyright 2026 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.admin.v2;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiExceptions;
import com.google.cloud.bigtable.admin.v2.models.ConsistencyRequest;
import com.google.cloud.bigtable.admin.v2.models.OptimizeRestoredTableOperationToken;
import com.google.cloud.bigtable.admin.v2.models.RestoredTableResult;
import com.google.cloud.bigtable.admin.v2.stub.BigtableTableAdminStub;
import com.google.cloud.bigtable.admin.v2.stub.EnhancedBigtableTableAdminStub;
import com.google.common.base.Strings;
import com.google.protobuf.Empty;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Modern Cloud Bigtable Table Admin Client.
 *
 * <p>This client extends the auto-generated {@link BaseBigtableTableAdminClient} to provide manual
 * overrides and additional convenience methods for Critical User Journeys (CUJs) that the GAPIC
 * generator cannot handle natively (e.g., chained Long Running Operations, Consistency Polling).
 */
public class BigtableTableAdminClientV2 extends BaseBigtableTableAdminClient {

  protected BigtableTableAdminClientV2(BaseBigtableTableAdminSettings settings) throws IOException {
    super(settings);
  }

  protected BigtableTableAdminClientV2(BigtableTableAdminStub stub) {
    super(stub);
  }

  /** Constructs an instance of BigtableTableAdminClientV2 with the given settings. */
  public static final BigtableTableAdminClientV2 createClient(
      BaseBigtableTableAdminSettings settings) throws IOException {
    // Explicitly create the enhanced stub
    EnhancedBigtableTableAdminStub stub =
        EnhancedBigtableTableAdminStub.createEnhanced(
            (com.google.cloud.bigtable.admin.v2.stub.BigtableTableAdminStubSettings)
                settings.getStubSettings());
    // Pass the enhanced stub to the existing stub-based constructor
    return new BigtableTableAdminClientV2(stub);
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
    return ((EnhancedBigtableTableAdminStub) getStub())
        .awaitOptimizeRestoredTableCallable()
        .resumeFutureCall(token.getOperationName());
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
        ((EnhancedBigtableTableAdminStub) getStub())
            .awaitOptimizeRestoredTableCallable()
            .resumeFutureCall(token.getOperationName());
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
    return ((EnhancedBigtableTableAdminStub) getStub())
        .awaitConsistencyCallable()
        .futureCall(ConsistencyRequest.forReplication(tableName, consistencyToken));
  }
}
