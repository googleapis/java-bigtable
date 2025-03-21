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
import com.google.auto.value.AutoValue;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.common.base.Preconditions;

/** An implementation of a {@link TargetId} for tables. */
@AutoValue
public abstract class TableId implements TargetId {

  /** Constructs a new TableId object for the specified table id. */
  public static TableId of(String tableId) {
    Preconditions.checkNotNull(tableId, "table id can't be null.");
    return new AutoValue_TableId(tableId);
  }

  abstract String getTableId();

  @Override
  @InternalApi
  public String toResourceName(String projectId, String instanceId) {
    return NameUtil.formatTableName(projectId, instanceId, getTableId());
  }

  @Override
  @InternalApi
  public boolean scopedForAuthorizedView() {
    return false;
  }

  @Override
  @InternalApi
  public boolean scopedForMaterializedView() {
    return false;
  }
}
