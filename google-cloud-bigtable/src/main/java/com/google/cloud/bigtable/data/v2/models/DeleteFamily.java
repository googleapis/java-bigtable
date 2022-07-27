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
package com.google.cloud.bigtable.data.v2.models;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.Serializable;
import javax.annotation.Nonnull;

/** Representation of a DeleteFamily mod in a data change. */
public final class DeleteFamily implements Entry, Serializable {
  private static final long serialVersionUID = 81806775917145615L;

  private final String familyName;

  DeleteFamily(@Nonnull String familyName) {
    this.familyName = familyName;
  }

  @Nonnull
  public String getFamilyName() {
    return this.familyName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeleteFamily otherDeleteFamily = (DeleteFamily) o;
    return Objects.equal(familyName, otherDeleteFamily.getFamilyName());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(familyName);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("familyName", familyName).toString();
  }
}
