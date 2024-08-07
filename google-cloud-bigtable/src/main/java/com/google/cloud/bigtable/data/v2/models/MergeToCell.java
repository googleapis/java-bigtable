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

import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import java.io.Serializable;
import javax.annotation.Nonnull;

/** Representation of an MergeToCell mod in a data change. */
@InternalApi("Intended for use by the BigtableIO in apache/beam only.")
@AutoValue
public abstract class MergeToCell implements Entry, Serializable {
  public static MergeToCell create(
      @Nonnull String family,
      @Nonnull Value qualifier,
      @Nonnull Value timestamp,
      @Nonnull Value input) {
    return new AutoValue_MergeToCell(family, qualifier, timestamp, input);
  }

  @Nonnull
  public abstract String getFamily();

  @Nonnull
  public abstract Value getQualifier();

  @Nonnull
  public abstract Value getTimestamp();

  @Nonnull
  public abstract Value getInput();
}
