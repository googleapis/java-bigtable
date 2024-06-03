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
package com.google.cloud.bigtable.data.v2.internal;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.ArrayValue;
import com.google.bigtable.v2.Type;
import com.google.bigtable.v2.Value;
import com.google.cloud.bigtable.data.v2.models.sql.Struct;
import java.util.List;

/**
 * Implementation of a {@link Struct} backed by protobuf {@link Value}s.
 *
 * <p>This is considered an internal implementation detail and not meant to be used by applications.
 */
@BetaApi
@InternalApi("For internal use only")
@AutoValue
public abstract class ProtoStruct extends AbstractProtoStructReader implements Struct {

  @InternalApi
  static ProtoStruct create(Type.Struct type, ArrayValue fieldValues) {
    return new AutoValue_ProtoStruct(type, fieldValues);
  }

  protected abstract Type.Struct type();

  protected abstract ArrayValue fieldValues();

  @Override
  List<Value> values() {
    return fieldValues().getValuesList();
  }

  // This is a temporary hack. The type/metadata wrappers will provide an efficient way to lookup
  // fields and type by columnName
  // TODO(jackdingilian): replace with efficient lookup
  // TODO(jackdingilian): fail on requests for column that appears multiple times
  @Override
  public int getColumnIndex(String columnName) {
    for (int i = 0; i < type().getFieldsCount(); i++) {
      if (type().getFields(i).getFieldName().equals(columnName)) {
        return i;
      }
    }
    throw new IllegalArgumentException("No field found with name: " + columnName);
  }

  @Override
  public Type getColumnType(int columnIndex) {
    return type().getFields(columnIndex).getType();
  }
}
