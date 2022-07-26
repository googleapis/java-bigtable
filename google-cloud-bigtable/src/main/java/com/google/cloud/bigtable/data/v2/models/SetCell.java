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
import com.google.protobuf.ByteString;
import java.io.Serializable;
import javax.annotation.Nonnull;

/**
 * Representation of a SetCell mod in a data change, whose value is concatenated by
 * (TODO:ChangeStreamRecordMerger) in case of SetCell value chunking.
 */
public final class SetCell implements Entry, Serializable {
  private static final long serialVersionUID = 77123872266724154L;

  private final String familyName;
  private final ByteString qualifier;
  private final long timestamp;
  private final ByteString value;

  SetCell(
      @Nonnull String familyName,
      @Nonnull ByteString qualifier,
      long timestamp,
      @Nonnull ByteString value) {
    this.familyName = familyName;
    this.qualifier = qualifier;
    this.timestamp = timestamp;
    this.value = value;
  }

  public String getFamilyName() {
    return this.familyName;
  }

  public ByteString getQualifier() {
    return this.qualifier;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public ByteString getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetCell otherSetCell = (SetCell) o;
    return Objects.equal(familyName, otherSetCell.getFamilyName())
        && Objects.equal(qualifier, otherSetCell.getQualifier())
        && Objects.equal(timestamp, otherSetCell.getTimestamp())
        && Objects.equal(value, otherSetCell.getValue());
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
        .add("timestamp", timestamp)
        .add("value", value.toStringUtf8())
        .toString();
  }
}
