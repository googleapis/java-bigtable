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
package com.google.cloud.bigtable.data.v2.models;

import static com.google.cloud.bigtable.data.v2.models.RowMutationEntry.MAX_MUTATION;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a list of mutations for multiple rows. Each mutation contains multiple changes that
 * will be atomically applied to each row. However, ordering between rows is not guaranteed.
 *
 * <p>This class is meant for manual batching.
 */
public final class BulkMutation implements Serializable, Cloneable {
  private static final long serialVersionUID = 3522061250439399088L;

  private final String tableId;
  @Nullable private final String authorizedViewId;
  private transient MutateRowsRequest.Builder builder;

  private long mutationCountSum = 0;

  public static BulkMutation create(String tableId) {
    return new BulkMutation(tableId);
  }

  /**
   * Creates a new instance of the bulk mutation builder on an AuthorizedView.
   *
   * <p>See {@link com.google.cloud.bigtable.admin.v2.models.AuthorizedView} for more details about
   * an AuthorizedView.
   */
  public static BulkMutation createForAuthorizedView(String tableId, String authorizedViewId) {
    return new BulkMutation(tableId, authorizedViewId);
  }

  @InternalApi("For internal use only")
  public static BulkMutation create(String tableId, @Nullable MutateRowOptions mutateRowOptions) {
    if (mutateRowOptions == null) {
      return new BulkMutation(tableId);
    }
    return new BulkMutation(tableId, mutateRowOptions.getAuthorizedViewId());
  }

  private BulkMutation(@Nonnull String tableId) {
    this(tableId, null);
  }

  private BulkMutation(@Nonnull String tableId, @Nullable String authorizedViewId) {
    Preconditions.checkNotNull(tableId);

    this.tableId = tableId;
    this.authorizedViewId = authorizedViewId;
    this.builder = MutateRowsRequest.newBuilder();
  }

  private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
    input.defaultReadObject();
    builder = MutateRowsRequest.newBuilder().mergeFrom(input);
  }

  private void writeObject(ObjectOutputStream output) throws IOException {
    output.defaultWriteObject();
    builder.build().writeTo(output);
  }

  /**
   * Add mutation for a particular row. The changes in the mutation will be applied atomically.
   * However there is no guarantees about the relative ordering between mutations affecting
   * different rows.
   */
  public BulkMutation add(@Nonnull String rowKey, @Nonnull Mutation mutation) {
    Preconditions.checkNotNull(rowKey);
    Preconditions.checkNotNull(mutation);

    return add(ByteString.copyFromUtf8(rowKey), mutation);
  }

  /**
   * Add mutation for a particular row. The changes in the mutation will be applied atomic. However
   * there is no guarantees about the relative ordering between mutations affecting different rows.
   */
  public BulkMutation add(@Nonnull ByteString rowKey, @Nonnull Mutation mutation) {
    Preconditions.checkNotNull(rowKey);
    Preconditions.checkNotNull(mutation);

    long mutationCount = mutation.getMutations().size();
    Preconditions.checkArgument(
        mutationCountSum + mutationCount <= MAX_MUTATION,
        String.format(
            "Too many mutations, got %s, limit is %s",
            mutationCountSum + mutationCount, MAX_MUTATION));
    this.mutationCountSum += mutationCount;

    builder.addEntries(
        MutateRowsRequest.Entry.newBuilder()
            .setRowKey(rowKey)
            .addAllMutations(mutation.getMutations())
            .build());
    return this;
  }

  /**
   * Add mutation for a particular row. The changes in the mutation will be applied atomic. However
   * there is no guarantees about the relative ordering between mutations affecting different rows.
   */
  public BulkMutation add(@Nonnull RowMutationEntry entry) {
    Preconditions.checkNotNull(entry, "Row mutation entry can't be null");
    builder.addEntries(entry.toProto());
    return this;
  }

  public int getEntryCount() {
    return builder.getEntriesCount();
  }

  @InternalApi
  public MutateRowsRequest toProto(RequestContext requestContext) {
    if (authorizedViewId != null && !authorizedViewId.isEmpty()) {
      String authorizedViewName =
          NameUtil.formatAuthorizedViewName(
              requestContext.getProjectId(),
              requestContext.getInstanceId(),
              tableId,
              authorizedViewId);
      builder.setAuthorizedViewName(authorizedViewName);
    } else {
      String tableName =
          NameUtil.formatTableName(
              requestContext.getProjectId(), requestContext.getInstanceId(), tableId);
      builder.setTableName(tableName);
    }

    return builder.setAppProfileId(requestContext.getAppProfileId()).build();
  }

  /**
   * Wraps the protobuf {@link MutateRowsRequest}.
   *
   * <p>This is meant for advanced usage only. Please ensure that the MutateRowsRequest does not use
   * server side timestamps. The BigtableDataClient assumes that mutation present in BulkMutation
   * are idempotent and is configured to enable retries by default. If serverside timestamps are
   * enabled then that can lead to duplicate mutations.
   *
   * <p>WARNING: when applied, the resulting mutation object will ignore the project id and instance
   * id in the table_name and instead apply the configuration in the client.
   */
  @BetaApi
  public static BulkMutation fromProto(@Nonnull MutateRowsRequest request) {
    String tableName = request.getTableName();
    String authorizedViewName = request.getAuthorizedViewName();

    Preconditions.checkArgument(
        !tableName.isEmpty() || !authorizedViewName.isEmpty(),
        "Either table name or authorized view name must be specified");
    Preconditions.checkArgument(
        tableName.isEmpty() || authorizedViewName.isEmpty(),
        "Table name and authorized view name cannot be specified at the same time");

    BulkMutation bulkMutation;
    if (!tableName.isEmpty()) {
      bulkMutation = BulkMutation.create(NameUtil.extractTableIdFromTableName(tableName));
    } else {
      bulkMutation =
          BulkMutation.createForAuthorizedView(
              NameUtil.extractTableIdFromAuthorizedViewName(authorizedViewName),
              NameUtil.extractAuthorizedViewIdFromAuthorizedViewName(authorizedViewName));
    }
    bulkMutation.builder = request.toBuilder();

    return bulkMutation;
  }

  /** Creates a copy of {@link BulkMutation}. */
  @Override
  public BulkMutation clone() {
    // createForAuthorizedView() copies the tableId and authorizedViewId at the same time. It is
    // essentially create() when the authorizedViewId is null.
    BulkMutation bulkMutation = BulkMutation.createForAuthorizedView(tableId, authorizedViewId);
    bulkMutation.builder = this.builder.clone();
    return bulkMutation;
  }
}
