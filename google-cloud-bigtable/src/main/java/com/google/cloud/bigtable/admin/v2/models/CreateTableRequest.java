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
package com.google.cloud.bigtable.admin.v2.models;

import com.google.api.core.InternalApi;
import com.google.bigtable.admin.v2.ChangeStreamConfig;
import com.google.bigtable.admin.v2.ColumnFamily;
import com.google.cloud.bigtable.admin.v2.internal.NameUtil;
import com.google.cloud.bigtable.admin.v2.models.GCRules.GCRule;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.protobuf.ByteString;
import javax.annotation.Nonnull;
import org.threeten.bp.Duration;

/**
 * Fluent wrapper for {@link com.google.bigtable.admin.v2.CreateTableRequest}
 *
 * <p>Allows for creating table with:
 *
 * <ul>
 *   <li>optional columnFamilies, including optional {@link GCRule}
 *   <li>optional granularity
 *   <li>and optional split points
 * </ul>
 */
public final class CreateTableRequest {
  private final com.google.bigtable.admin.v2.CreateTableRequest.Builder requestBuilder =
      com.google.bigtable.admin.v2.CreateTableRequest.newBuilder();

  public static CreateTableRequest of(String tableId) {
    return new CreateTableRequest(tableId);
  }

  /** Configures table with the specified id */
  private CreateTableRequest(String tableId) {
    requestBuilder.setTableId(tableId);
  }

  /** Adds a new columnFamily to the configuration */
  public CreateTableRequest addFamily(String familyId) {
    Preconditions.checkNotNull(familyId);
    requestBuilder.getTableBuilder().putColumnFamilies(familyId, ColumnFamily.getDefaultInstance());
    return this;
  }

  /**
   * Adds a new columnFamily with {@link GCRule} to the configuration. Please note that calling this
   * method with the same familyId will overwrite the previous family.
   *
   * @see GCRule for available options.
   */
  public CreateTableRequest addFamily(@Nonnull String familyId, @Nonnull GCRule gcRule) {
    return addFamily(familyId, gcRule, Type.raw());
  }

  /**
   * Adds a new columnFamily with a {@link Type} to the configuration. Please note that calling this
   * method with the same familyId will overwrite the previous family.
   *
   * @see Type for available options.
   */
  public CreateTableRequest addFamily(@Nonnull String familyId, @Nonnull Type valueType) {
    return addFamily(familyId, GCRules.GCRULES.defaultRule(), valueType);
  }

  /**
   * Adds a new columnFamily with a {@link GCRule} and {@link Type} to the configuration. Please
   * note that calling this method with the same familyId will overwrite the previous family.
   *
   * @see GCRule for available options.
   * @see Type for available options.
   */
  public CreateTableRequest addFamily(
      @Nonnull String familyId, @Nonnull GCRule gcRule, @Nonnull Type valueType) {
    Preconditions.checkNotNull(familyId);
    Preconditions.checkNotNull(gcRule);
    Preconditions.checkNotNull(valueType);

    ColumnFamily.Builder builder = ColumnFamily.newBuilder().setGcRule(gcRule.toProto());

    // Don't set the type if it's the default ("raw")
    if (!valueType.equals(Type.raw())) {
      builder.setValueType(valueType.toProto());
    }
    requestBuilder.getTableBuilder().putColumnFamilies(familyId, builder.build());
    return this;
  }

  /** Adds split at the specified key to the configuration */
  public CreateTableRequest addSplit(ByteString key) {
    Preconditions.checkNotNull(key);
    requestBuilder.addInitialSplitsBuilder().setKey(key);
    return this;
  }

  /** Add change stream retention period between 1 day and 7 days */
  public CreateTableRequest addChangeStreamRetention(Duration retention) {
    Preconditions.checkNotNull(retention);
    requestBuilder
        .getTableBuilder()
        .setChangeStreamConfig(
            ChangeStreamConfig.newBuilder()
                .setRetentionPeriod(
                    com.google.protobuf.Duration.newBuilder()
                        .setSeconds(retention.getSeconds())
                        .setNanos(retention.getNano())
                        .build())
                .build());
    return this;
  }

  /** Configures if the table is deletion protected. */
  public CreateTableRequest setDeletionProtection(boolean deletionProtection) {
    requestBuilder.getTableBuilder().setDeletionProtection(deletionProtection);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateTableRequest that = (CreateTableRequest) o;
    return Objects.equal(requestBuilder.build(), that.requestBuilder.build());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(requestBuilder.build());
  }

  @InternalApi
  public com.google.bigtable.admin.v2.CreateTableRequest toProto(
      @Nonnull String projectId, @Nonnull String instanceId) {
    Preconditions.checkNotNull(projectId);
    Preconditions.checkNotNull(instanceId);

    return requestBuilder.setParent(NameUtil.formatInstanceName(projectId, instanceId)).build();
  }
}
