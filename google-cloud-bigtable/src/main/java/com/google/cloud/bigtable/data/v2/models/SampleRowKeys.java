/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.bigtable.data.v2.models;

import com.google.api.core.InternalApi;
import com.google.bigtable.v2.SampleRowKeysRequest;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Wraps a {@link com.google.bigtable.v2.SampleRowKeysRequest}. */
public final class SampleRowKeys implements Serializable {
  private final String tableId;
  @Nullable private final String authorizedViewId;

  private SampleRowKeys(@Nonnull String tableId) {
    this(tableId, null);
  }

  private SampleRowKeys(@Nonnull String tableId, @Nullable String authorizedViewId) {
    Preconditions.checkNotNull(tableId);

    this.tableId = tableId;
    this.authorizedViewId = authorizedViewId;
  }

  public static SampleRowKeys create(@Nonnull String tableId) {
    return new SampleRowKeys(tableId);
  }

  /**
   * Creates a new instance of the sample row keys builder on an authorized view.
   *
   * <p>See {@link com.google.cloud.bigtable.admin.v2.models.AuthorizedView} for more details about
   * an AuthorizedView.
   */
  public static SampleRowKeys createForAuthorizedView(
      @Nonnull String tableId, @Nonnull String authorizedViewId) {
    return new SampleRowKeys(tableId, authorizedViewId);
  }

  @InternalApi("For internal use only")
  public static SampleRowKeys create(
      String tableId, @Nullable SampleRowKeysOptions sampleRowKeysOptions) {
    if (sampleRowKeysOptions == null) {
      return new SampleRowKeys(tableId);
    }
    return new SampleRowKeys(tableId, sampleRowKeysOptions.getAuthorizedViewId());
  }

  @InternalApi
  public SampleRowKeysRequest toProto(RequestContext requestContext) {
    SampleRowKeysRequest.Builder builder = SampleRowKeysRequest.newBuilder();
    if (this.authorizedViewId != null && !this.authorizedViewId.isEmpty()) {
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
   * Wraps the protobuf {@link SampleRowKeysRequest}.
   *
   * <p>WARNING: Please note that the project id & instance id in the table/authorized view name
   * will be overwritten by the configuration in the BigtableDataClient.
   */
  public static SampleRowKeys fromProto(@Nonnull SampleRowKeysRequest request) {
    String tableName = request.getTableName();
    String authorizedViewName = request.getAuthorizedViewName();

    Preconditions.checkArgument(
        !tableName.isEmpty() || !authorizedViewName.isEmpty(),
        "Either table name or authorized view name must be specified");
    Preconditions.checkArgument(
        tableName.isEmpty() || authorizedViewName.isEmpty(),
        "Table name and authorized view name cannot be specified at the same time");

    SampleRowKeys sampleRowKeys;
    if (!tableName.isEmpty()) {
      sampleRowKeys = new SampleRowKeys(NameUtil.extractTableIdFromTableName(tableName));
    } else {
      sampleRowKeys =
          new SampleRowKeys(
              NameUtil.extractTableIdFromAuthorizedViewName(authorizedViewName),
              NameUtil.extractAuthorizedViewIdFromAuthorizedViewName(authorizedViewName));
    }

    return sampleRowKeys;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SampleRowKeys sampleRowKeys = (SampleRowKeys) o;
    return Objects.equal(tableId, sampleRowKeys.tableId)
        && Objects.equal(authorizedViewId, sampleRowKeys.authorizedViewId);
  }
}
