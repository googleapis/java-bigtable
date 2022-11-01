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
package com.google.cloud.bigtable.admin.v2.models;

import com.google.api.core.InternalApi;
import com.google.cloud.bigtable.admin.v2.internal.NameUtil;
import com.google.common.base.Objects;
import com.google.protobuf.util.FieldMaskUtil;

/** Fluent wrapper for {@link com.google.bigtable.admin.v2.UpdateTableRequest} */
public final class UpdateTableRequest {
  private final com.google.bigtable.admin.v2.UpdateTableRequest.Builder requestBuilder =
      com.google.bigtable.admin.v2.UpdateTableRequest.newBuilder();
  private final com.google.bigtable.admin.v2.Table.Builder tableBuilder =
      com.google.bigtable.admin.v2.Table.newBuilder();
  private final String tableId;

  public static UpdateTableRequest of(String tableId) {
    return new UpdateTableRequest(tableId);
  }

  /** Configures update table request with specified project, instance, and table id */
  private UpdateTableRequest(String tableId) {
    this.tableId = tableId;
  }

  /** Update the table's deletion protection setting * */
  public UpdateTableRequest setDeletionProtection(boolean deletionProtection) {
    requestBuilder.setUpdateMask(
        FieldMaskUtil.union(
            requestBuilder.getUpdateMask(),
            FieldMaskUtil.fromString(
                com.google.bigtable.admin.v2.Table.class, "deletion_protection")));
    tableBuilder.setDeletionProtection(deletionProtection);
    return this;
  }

  public String getTableId() {
    return this.tableId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateTableRequest that = (UpdateTableRequest) o;
    return Objects.equal(requestBuilder.getUpdateMask(), that.requestBuilder.getUpdateMask())
        && Objects.equal(this.tableId, that.tableId)
        && Objects.equal(this.tableBuilder.build(), that.tableBuilder.build());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(requestBuilder.getUpdateMask(), tableId, tableBuilder.build());
  }

  @InternalApi
  public com.google.bigtable.admin.v2.UpdateTableRequest toProto(
      String projectId, String instanceId) {
    tableBuilder.setName(NameUtil.formatTableName(projectId, instanceId, tableId));
    requestBuilder.setTable(tableBuilder.build());
    return requestBuilder.build();
  }
}
