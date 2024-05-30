/*
 * Copyright 2024 Google LLC
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
import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.ColumnMetadata;
import java.util.List;

// TODO(jackdingilian): this will change when we add the result set interface. This will not be
// directly exposed to
// users. Leaving this as is for now to allow SqlRowMerger to work.
@InternalApi
@BetaApi
@AutoValue
public abstract class SqlRow {

  /**
   * Creates a new SqlRow
   *
   * @param schema list of column metadata for each column
   * @param values list of the values for each column
   */
  public static SqlRow create(
      List<ColumnMetadata> schema, List<com.google.bigtable.v2.Value> values) {
    return new AutoValue_SqlRow(schema, values);
  }

  /** List of {@link ColumnMetadata} containing names and types for each column in the sql query. */
  public abstract List<ColumnMetadata> schema();

  /** List of {@link Value} objects representing the sql columns for a logical row. */
  public abstract List<com.google.bigtable.v2.Value> values();
}
