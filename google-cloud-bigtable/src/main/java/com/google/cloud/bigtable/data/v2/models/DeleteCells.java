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

import com.google.cloud.bigtable.data.v2.models.Range.TimestampRange;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.protobuf.ByteString;
import java.io.Serializable;
import javax.annotation.Nonnull;

/** Representation of a DeleteCells mod in a data change. */
public final class DeleteCells implements Entry, Serializable {
  private static final long serialVersionUID = 851772158721462017L;

  private final String familyName;
  private final ByteString qualifier;
  private final TimestampRange timestampRange;

  DeleteCells(
      @Nonnull String familyName,
      @Nonnull ByteString qualifier,
      @Nonnull TimestampRange timestampRange) {
    this.familyName = familyName;
    this.qualifier = qualifier;
    this.timestampRange = timestampRange;
  }

  public String getFamilyName() {
    return this.familyName;
  }

  public ByteString getQualifier() {
    return this.qualifier;
  }

  public TimestampRange getTimestampRange() {
    return this.timestampRange;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeleteCells otherDeleteCell = (DeleteCells) o;
    return Objects.equal(familyName, otherDeleteCell.getFamilyName())
        && Objects.equal(qualifier, otherDeleteCell.getQualifier())
        && Objects.equal(timestampRange, otherDeleteCell.getTimestampRange());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(familyName);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("familyName", familyName)
        .add("qualifier", qualifier.toStringUtf8())
        .add("timestampRange", timestampRange)
        .toString();
  }
}
