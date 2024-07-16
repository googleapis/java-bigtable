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

import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.Type;
import com.google.cloud.bigtable.data.v2.models.sql.ColumnMetadata;

/**
 * Implementation of {@link ColumnMetadata} using AutoValue
 *
 * <p>This is considered an internal implementation detail and not meant to be used by applications.
 */
@InternalApi("For internal use only")
@AutoValue
public abstract class ColumnMetadataImpl implements ColumnMetadata {
  public static ColumnMetadata create(String name, Type type) {
    return new AutoValue_ColumnMetadataImpl(name, type);
  }

  static ColumnMetadata fromProto(com.google.bigtable.v2.ColumnMetadata proto) {
    return create(proto.getName(), proto.getType());
  }
}
