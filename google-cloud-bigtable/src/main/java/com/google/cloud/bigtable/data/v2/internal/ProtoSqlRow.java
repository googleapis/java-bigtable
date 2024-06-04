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
package com.google.cloud.bigtable.data.v2.internal;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.ColumnMetadata;
import com.google.bigtable.v2.Type;
import com.google.bigtable.v2.Value;
import java.util.List;

@InternalApi
@BetaApi
@AutoValue
public abstract class ProtoSqlRow extends AbstractProtoStructReader implements SqlRow {
  /**
   * Creates a new SqlRow
   *
   * @param schema list of column metadata for each column
   * @param values list of the values for each column
   */
  public static ProtoSqlRow create(List<ColumnMetadata> schema, List<Value> values) {
    return new AutoValue_ProtoSqlRow(values, schema);
  }

  /** List of {@link ColumnMetadata} describing the schema of the row. */
  abstract List<ColumnMetadata> schema();

  // This is a temporary hack. The type/metadata wrappers will provide an efficient way to lookup
  // fields and type by columnName
  // TODO(jackdingilian): replace with efficient lookup
  // TODO(jackdingilian): fail on requests for column that appears multiple times
  @Override
  public int getColumnIndex(String columnName) {
    for (int i = 0; i < schema().size(); i++) {
      if (schema().get(i).getName().equals(columnName)) {
        return i;
      }
    }
    throw new IllegalArgumentException("No field found with name: " + columnName);
  }

  @Override
  public Type getColumnType(int columnIndex) {
    return schema().get(columnIndex).getType();
  }
}
