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

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsRequest.Entry;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.cloud.bigtable.data.v2.models.Range.TimestampRange;
import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a list of mutations targeted at a single row. It's meant to be used as an parameter
 * for {@link com.google.cloud.bigtable.data.v2.BigtableDataClient#mutateRowAsync(RowMutation)}.
 */
public final class RowMutation implements MutationApi<RowMutation>, Serializable {
  private static final long serialVersionUID = 6529002234913236318L;

  private final String tableId;
  @Nullable private final String authorizedViewId;
  private final ByteString key;
  private final Mutation mutation;

  private RowMutation(@Nonnull String tableId, ByteString key, Mutation mutation) {
    this(tableId, null, key, mutation);
  }

  private RowMutation(
      @Nonnull String tableId,
      @Nullable String authorizedViewId,
      ByteString key,
      Mutation mutation) {
    Preconditions.checkNotNull(tableId);

    this.tableId = tableId;
    this.authorizedViewId = authorizedViewId;
    this.key = key;
    this.mutation = mutation;
  }

  /** Creates a new instance of the mutation builder. */
  public static RowMutation create(@Nonnull String tableId, @Nonnull String key) {
    return create(tableId, ByteString.copyFromUtf8(key));
  }

  /**
   * Creates a new instance of the mutation builder on an authorized view.
   *
   * <p>See {@link com.google.cloud.bigtable.admin.v2.models.AuthorizedView} for more details about
   * an AuthorizedView.
   */
  public static RowMutation createForAuthorizedView(
      @Nonnull String tableId, @Nonnull String authorizedViewId, @Nonnull String key) {
    return createForAuthorizedView(tableId, authorizedViewId, ByteString.copyFromUtf8(key));
  }

  /** Creates a new instance of the mutation builder. */
  public static RowMutation create(@Nonnull String tableId, @Nonnull ByteString key) {
    return new RowMutation(tableId, key, Mutation.create());
  }

  /**
   * Creates a new instance of the mutation builder on an authorized view.
   *
   * <p>See {@link com.google.cloud.bigtable.admin.v2.models.AuthorizedView} for more details about
   * an AuthorizedView.
   */
  public static RowMutation createForAuthorizedView(
      @Nonnull String tableId, @Nonnull String authorizedViewId, @Nonnull ByteString key) {
    return new RowMutation(tableId, authorizedViewId, key, Mutation.create());
  }

  /**
   * Creates new instance of mutation builder by wrapping existing set of row mutations. The builder
   * will be owned by this RowMutation and should not be used by the caller after this call. This
   * functionality is intended for advanced usage.
   *
   * <p>Sample code:
   *
   * <pre><code>
   * Mutation mutation = Mutation.create()
   *     .setCell("[FAMILY_NAME]", "[QUALIFIER]", [TIMESTAMP], "[VALUE]");
   * RowMutation rowMutation = RowMutation.create("[TABLE]", "[ROW_KEY]", mutation);
   * </code></pre>
   */
  public static RowMutation create(
      @Nonnull String tableId, @Nonnull String key, @Nonnull Mutation mutation) {
    return create(tableId, ByteString.copyFromUtf8(key), mutation);
  }

  /**
   * Creates new instance of mutation builder on an authorized view by wrapping existing set of row
   * mutations. The builder will be owned by this RowMutation and should not be used by the caller
   * after this call. This functionality is intended for advanced usage.
   *
   * <p>Sample code:
   *
   * <pre><code>
   * Mutation mutation = Mutation.create()
   *     .setCell("[FAMILY_NAME]", "[QUALIFIER]", [TIMESTAMP], "[VALUE]");
   * RowMutation rowMutation = RowMutation.create("[TABLE]", "[AUTHORIZED_VIEW]", "[ROW_KEY]", mutation);
   * </code></pre>
   *
   * <p>See {@link com.google.cloud.bigtable.admin.v2.models.AuthorizedView} for more details about
   * an AuthorizedView.
   */
  public static RowMutation createForAuthorizedView(
      @Nonnull String tableId,
      @Nonnull String authorizedViewId,
      @Nonnull String key,
      @Nonnull Mutation mutation) {
    return createForAuthorizedView(
        tableId, authorizedViewId, ByteString.copyFromUtf8(key), mutation);
  }

  /**
   * Creates new instance of mutation builder by wrapping existing set of row mutations. The builder
   * will be owned by this RowMutation and should not be used by the caller after this call. This
   * functionality is intended for advanced usage.
   *
   * <p>Sample code:
   *
   * <pre><code>
   * Mutation mutation = Mutation.create()
   *     .setCell("[FAMILY_NAME]", "[QUALIFIER]", [TIMESTAMP], "[VALUE]");
   * RowMutation rowMutation = RowMutation.create("[TABLE]", [BYTE_STRING_ROW_KEY], mutation);
   * </code></pre>
   */
  public static RowMutation create(
      @Nonnull String tableId, @Nonnull ByteString key, @Nonnull Mutation mutation) {
    return new RowMutation(tableId, key, mutation);
  }

  /**
   * Creates new instance of mutation builder on an authorized view by wrapping existing set of row
   * mutations. The builder will be owned by this RowMutation and should not be used by the caller
   * after this call. This functionality is intended for advanced usage.
   *
   * <p>Sample code:
   *
   * <pre><code>
   * Mutation mutation = Mutation.create()
   *     .setCell("[FAMILY_NAME]", "[QUALIFIER]", [TIMESTAMP], "[VALUE]");
   * RowMutation rowMutation = RowMutation.create("[TABLE]", "[AUTHORIZED_VIEW]", [BYTE_STRING_ROW_KEY], mutation);
   * </code></pre>
   *
   * <p>See {@link com.google.cloud.bigtable.admin.v2.models.AuthorizedView} for more details about
   * an AuthorizedView.
   */
  public static RowMutation createForAuthorizedView(
      @Nonnull String tableId,
      @Nonnull String authorizedViewId,
      @Nonnull ByteString key,
      @Nonnull Mutation mutation) {
    return new RowMutation(tableId, authorizedViewId, key, mutation);
  }

  @Override
  public RowMutation setCell(
      @Nonnull String familyName, @Nonnull String qualifier, @Nonnull String value) {
    mutation.setCell(familyName, qualifier, value);
    return this;
  }

  @Override
  public RowMutation setCell(
      @Nonnull String familyName,
      @Nonnull String qualifier,
      long timestamp,
      @Nonnull String value) {
    mutation.setCell(familyName, qualifier, timestamp, value);
    return this;
  }

  @Override
  public RowMutation setCell(
      @Nonnull String familyName, @Nonnull ByteString qualifier, @Nonnull ByteString value) {
    mutation.setCell(familyName, qualifier, value);
    return this;
  }

  @Override
  public RowMutation setCell(
      @Nonnull String familyName,
      @Nonnull ByteString qualifier,
      long timestamp,
      @Nonnull ByteString value) {
    mutation.setCell(familyName, qualifier, timestamp, value);
    return this;
  }

  @Override
  public RowMutation setCell(@Nonnull String familyName, @Nonnull String qualifier, long value) {
    mutation.setCell(familyName, qualifier, value);
    return this;
  }

  @Override
  public RowMutation setCell(
      @Nonnull String familyName, @Nonnull String qualifier, long timestamp, long value) {
    mutation.setCell(familyName, qualifier, timestamp, value);
    return this;
  }

  @Override
  public RowMutation setCell(
      @Nonnull String familyName, @Nonnull ByteString qualifier, long value) {
    mutation.setCell(familyName, qualifier, value);
    return this;
  }

  @Override
  public RowMutation setCell(
      @Nonnull String familyName, @Nonnull ByteString qualifier, long timestamp, long value) {
    mutation.setCell(familyName, qualifier, timestamp, value);
    return this;
  }

  @Override
  public RowMutation deleteCells(@Nonnull String familyName, @Nonnull String qualifier) {
    mutation.deleteCells(familyName, qualifier);
    return this;
  }

  @Override
  public RowMutation deleteCells(@Nonnull String familyName, @Nonnull ByteString qualifier) {
    mutation.deleteCells(familyName, qualifier);
    return this;
  }

  @Override
  public RowMutation deleteCells(
      @Nonnull String familyName,
      @Nonnull ByteString qualifier,
      @Nonnull TimestampRange timestampRange) {
    mutation.deleteCells(familyName, qualifier, timestampRange);
    return this;
  }

  @Override
  public RowMutation deleteFamily(@Nonnull String familyName) {
    mutation.deleteFamily(familyName);
    return this;
  }

  @Override
  public RowMutation deleteRow() {
    mutation.deleteRow();
    return this;
  }

  @Override
  public RowMutation addToCell(
      @Nonnull String familyName,
      @Nonnull Value qualifier,
      @Nonnull Value timestamp,
      @Nonnull Value input) {
    mutation.addToCell(familyName, qualifier, timestamp, input);
    return this;
  }

  @InternalApi
  public MutateRowRequest toProto(RequestContext requestContext) {
    MutateRowRequest.Builder builder = MutateRowRequest.newBuilder();
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

    return builder
        .setAppProfileId(requestContext.getAppProfileId())
        .setRowKey(key)
        .addAllMutations(mutation.getMutations())
        .build();
  }

  /**
   * Creates a single entry bulk {@link com.google.bigtable.v2.MutateRowsRequest}, which will be
   * merged by the batching logic in the callable chain.
   */
  @InternalApi
  public MutateRowsRequest toBulkProto(RequestContext requestContext) {
    MutateRowsRequest.Builder builder = MutateRowsRequest.newBuilder();
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

    return builder
        .setAppProfileId(requestContext.getAppProfileId())
        .addEntries(
            Entry.newBuilder().setRowKey(key).addAllMutations(mutation.getMutations()).build())
        .build();
  }

  /**
   * Wraps the protobuf {@link MutateRowRequest}.
   *
   * <p>This is meant for advanced usage only. Please ensure that the MutateRowRequest does not use
   * server side timestamps. The BigtableDataClient assumes that RowMutations are idempotent and is
   * configured to enable retries by default. If serverside timestamps are enabled, this can lead to
   * duplicate mutations.
   *
   * <p>WARNING: when applied, the resulting mutation object will ignore the project id and instance
   * id in the table_name and instead apply the configuration in the client.
   */
  @BetaApi
  public static RowMutation fromProto(@Nonnull MutateRowRequest request) {
    String tableName = request.getTableName();
    String authorizedViewName = request.getAuthorizedViewName();

    Preconditions.checkArgument(
        !tableName.isEmpty() || !authorizedViewName.isEmpty(),
        "Either table name or authorized view name must be specified");
    Preconditions.checkArgument(
        tableName.isEmpty() || authorizedViewName.isEmpty(),
        "Table name and authorized view name cannot be specified at the same time");

    if (!tableName.isEmpty()) {
      String tableId = NameUtil.extractTableIdFromTableName(tableName);
      return RowMutation.create(
          tableId, request.getRowKey(), Mutation.fromProto(request.getMutationsList()));
    } else {
      String tableId = NameUtil.extractTableIdFromAuthorizedViewName(authorizedViewName);
      String authorizedViewId =
          NameUtil.extractAuthorizedViewIdFromAuthorizedViewName(authorizedViewName);
      return RowMutation.createForAuthorizedView(
          tableId,
          authorizedViewId,
          request.getRowKey(),
          Mutation.fromProto(request.getMutationsList()));
    }
  }
}
