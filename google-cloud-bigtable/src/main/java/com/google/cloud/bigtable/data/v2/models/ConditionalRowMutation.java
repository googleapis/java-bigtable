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
import com.google.bigtable.v2.CheckAndMutateRowRequest;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.cloud.bigtable.data.v2.models.Filters.Filter;
import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Mutates a row atomically based on the output of a condition filter. */
public final class ConditionalRowMutation implements Serializable {
  private static final long serialVersionUID = -3699904745621909502L;

  private final String tableId;
  @Nullable private final String authorizedViewId;
  private transient CheckAndMutateRowRequest.Builder builder =
      CheckAndMutateRowRequest.newBuilder();

  private ConditionalRowMutation(@Nonnull String tableId, ByteString rowKey) {
    this(tableId, null, rowKey);
  }

  private ConditionalRowMutation(
      @Nonnull String tableId, @Nullable String authorizedViewId, ByteString rowKey) {
    Preconditions.checkNotNull(tableId);

    this.tableId = tableId;
    this.authorizedViewId = authorizedViewId;
    builder.setRowKey(rowKey);
  }

  /** Creates a new instance of the mutation builder. */
  public static ConditionalRowMutation create(String tableId, String rowKey) {
    return create(tableId, ByteString.copyFromUtf8(rowKey));
  }

  /**
   * Creates a new instance of the mutation builder on an AuthorizedView.
   *
   * <p>See {@link com.google.cloud.bigtable.admin.v2.models.AuthorizedView} for more details about
   * an AuthorizedView.
   */
  public static ConditionalRowMutation createForAuthorizedView(
      String tableId, String authorizedViewId, String rowKey) {
    return createForAuthorizedView(tableId, authorizedViewId, ByteString.copyFromUtf8(rowKey));
  }

  /** Creates a new instance of the mutation builder. */
  public static ConditionalRowMutation create(String tableId, ByteString rowKey) {
    Validations.validateTableId(tableId);

    return new ConditionalRowMutation(tableId, rowKey);
  }

  /**
   * Creates a new instance of the mutation builder on an AuthorizedView.
   *
   * <p>See {@link com.google.cloud.bigtable.admin.v2.models.AuthorizedView} for more details about
   * an AuthorizedView.
   */
  public static ConditionalRowMutation createForAuthorizedView(
      String tableId, String authorizedViewId, ByteString rowKey) {
    Validations.validateTableId(tableId);
    Validations.validateAuthorizedViewId(authorizedViewId);

    return new ConditionalRowMutation(tableId, authorizedViewId, rowKey);
  }

  private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
    input.defaultReadObject();
    builder = CheckAndMutateRowRequest.newBuilder().mergeFrom(input);
  }

  private void writeObject(ObjectOutputStream output) throws IOException {
    output.defaultWriteObject();
    builder.build().writeTo(output);
  }

  /**
   * The filter to be applied to the contents of the specified row. Depending on whether or not any
   * results are yielded, either the mutations added via {@link #then(Mutation)} or {@link
   * #otherwise(Mutation)} will be executed. If unset, checks that the row contains any values at
   * all.
   *
   * <p>Unlike {@link #then(Mutation)} and {@link #otherwise(Mutation)}, only a single condition can
   * be set. However that filter can be a {@link Filters#chain()} or {@link Filters#interleave()},
   * which can wrap multiple other filters.
   *
   * <p>WARNING: {@link Filters#condition(Filter)} is not supported.
   */
  public ConditionalRowMutation condition(@Nonnull Filter condition) {
    Preconditions.checkNotNull(condition);
    Preconditions.checkState(
        !builder.hasPredicateFilter(),
        "Can only have a single condition, please use a Filters#chain or Filters#interleave filter"
            + " instead");
    // TODO: verify that the condition does not use any FILTERS.condition() filters

    builder.setPredicateFilter(condition.toProto());

    return this;
  }

  /**
   * Adds changes to be atomically applied to the specified row if the condition yields at least one
   * cell when applied to the row.
   *
   * <p>Each {@code mutation} can specify multiple changes and the changes are accumulated each time
   * this method is called. Mutations are applied in order, meaning that earlier mutations can be
   * masked by later ones. Must contain at least one entry if {@link #otherwise(Mutation)} is empty,
   * and at most 100000.
   */
  public ConditionalRowMutation then(@Nonnull Mutation mutation) {
    Preconditions.checkNotNull(mutation);
    builder.addAllTrueMutations(mutation.getMutations());
    return this;
  }

  /**
   * Adds changes to be atomically applied to the specified row if the condition does not yields any
   * cells when applied to the row.
   *
   * <p>Each {@code mutation} can specify multiple changes and the changes are accumulated each time
   * this method is called. Mutations are applied in order, meaning that earlier mutations can be
   * masked by later ones. Must contain at least one entry if {@link #then(Mutation)} is empty, and
   * at most 100000.
   */
  public ConditionalRowMutation otherwise(@Nonnull Mutation mutation) {
    Preconditions.checkNotNull(mutation);
    builder.addAllFalseMutations(mutation.getMutations());
    return this;
  }

  /**
   * Creates the underlying {@link CheckAndMutateRowRequest} protobuf.
   *
   * <p>This method is considered an internal implementation detail and not meant to be used by
   * applications.
   */
  @InternalApi
  public CheckAndMutateRowRequest toProto(RequestContext requestContext) {
    Preconditions.checkState(
        !builder.getTrueMutationsList().isEmpty() || !builder.getFalseMutationsList().isEmpty(),
        "ConditionalRowMutations must have `then` or `otherwise` mutations.");

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
   * Wraps the protobuf {@link CheckAndMutateRowRequest}.
   *
   * <p>WARNING: Please note that the table_name will be overwritten by the configuration in the
   * BigtableDataClient.
   */
  @BetaApi
  public static ConditionalRowMutation fromProto(@Nonnull CheckAndMutateRowRequest request) {
    String tableName = request.getTableName();
    String authorizedViewName = request.getAuthorizedViewName();

    Preconditions.checkArgument(
        !tableName.isEmpty() || !authorizedViewName.isEmpty(),
        "Either table name or authorized view name must be specified");
    Preconditions.checkArgument(
        tableName.isEmpty() || authorizedViewName.isEmpty(),
        "Table name and authorized view name cannot be specified at the same time");

    ConditionalRowMutation rowMutation;
    if (!tableName.isEmpty()) {
      String tableId = NameUtil.extractTableIdFromTableName(tableName);
      rowMutation = ConditionalRowMutation.create(tableId, request.getRowKey());
    } else {
      String tableId = NameUtil.extractTableIdFromAuthorizedViewName(authorizedViewName);
      String authorizedViewId =
          NameUtil.extractAuthorizedViewIdFromAuthorizedViewName(authorizedViewName);
      rowMutation =
          ConditionalRowMutation.createForAuthorizedView(
              tableId, authorizedViewId, request.getRowKey());
    }
    rowMutation.builder = request.toBuilder();

    return rowMutation;
  }
}
