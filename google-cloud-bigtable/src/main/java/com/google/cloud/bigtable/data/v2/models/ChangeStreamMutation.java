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

import com.google.api.core.InternalApi;
import com.google.bigtable.v2.ReadChangeStreamResponse.DataChange.Type;
import com.google.cloud.bigtable.data.v2.models.Range.TimestampRange;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * A ChangeStreamMutation represents a list of mods(represented by List<{@link Entry}>) targeted at
 * a single row, which is concatenated by (TODO:ChangeStreamRecordMerger).
 *
 * <p>It's meant to be used in {@link com.google.cloud.bigtable.data.v2.models.ChangeStreamRecord}.
 */
public final class ChangeStreamMutation implements ChangeStreamRecord, Serializable {
  private static final long serialVersionUID = 8419520253162024218L;

  private final ByteString rowKey;

  /** Possible values: USER/GARBAGE_COLLECTION. */
  private final Type type;

  /** This should only be set when type==USER. */
  private final String sourceClusterId;

  private final Timestamp commitTimestamp;

  private final int tieBreaker;

  private transient ImmutableList.Builder<Entry> entries = ImmutableList.builder();

  /** Token and lowWatermark are set when we finish building the logical mutation. */
  private String token;

  private Timestamp lowWatermark;

  private ChangeStreamMutation(
      ByteString rowKey,
      Type type,
      String sourceClusterId,
      Timestamp commitTimestamp,
      int tieBreaker) {
    this.rowKey = rowKey;
    this.type = type;
    this.sourceClusterId = sourceClusterId;
    this.commitTimestamp = commitTimestamp;
    this.tieBreaker = tieBreaker;
  }

  /** Creates a new instance of a user initiated mutation. */
  static ChangeStreamMutation create(
      @Nonnull ByteString rowKey,
      @Nonnull Type type,
      @Nonnull String sourceClusterId,
      @Nonnull Timestamp commitTimestamp,
      int tieBreaker) {
    Preconditions.checkArgument(
        type == Type.USER,
        "ChangeStreamMutation with a specified source cluster id must be a user initiated mutation.");
    return new ChangeStreamMutation(rowKey, type, sourceClusterId, commitTimestamp, tieBreaker);
  }

  /** Creates a new instance of a GC mutation. */
  static ChangeStreamMutation create(
      @Nonnull ByteString rowKey,
      @Nonnull Type type,
      @Nonnull Timestamp commitTimestamp,
      int tieBreaker) {
    Preconditions.checkArgument(
        type == Type.GARBAGE_COLLECTION,
        "ChangeStreamMutation without source cluster id must be a garbage collection mutation.");
    return new ChangeStreamMutation(rowKey, type, null, commitTimestamp, tieBreaker);
  }

  private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
    input.defaultReadObject();

    @SuppressWarnings("unchecked")
    ImmutableList<Entry> deserialized = (ImmutableList<Entry>) input.readObject();
    this.entries = ImmutableList.<Entry>builder().addAll(deserialized);
  }

  private void writeObject(ObjectOutputStream output) throws IOException {
    output.defaultWriteObject();
    output.writeObject(entries.build());
  }

  @Nonnull
  public ByteString getRowKey() {
    return this.rowKey;
  }

  @Nonnull
  public Type getType() {
    return this.type;
  }

  public String getSourceClusterId() {
    return this.sourceClusterId;
  }

  @Nonnull
  public Timestamp getCommitTimestamp() {
    return this.commitTimestamp;
  }

  public int getTieBreaker() {
    return this.tieBreaker;
  }

  public ChangeStreamMutation setToken(@Nonnull String token) {
    this.token = token;
    return this;
  }

  public String getToken() {
    return this.token;
  }

  public ChangeStreamMutation setLowWatermark(@Nonnull Timestamp lowWatermark) {
    this.lowWatermark = lowWatermark;
    return this;
  }

  public Timestamp getLowWatermark() {
    return this.lowWatermark;
  }

  @Nonnull
  public List<Entry> getEntries() {
    return this.entries.build();
  }

  ChangeStreamMutation setCell(
      @Nonnull String familyName,
      @Nonnull ByteString qualifier,
      long timestamp,
      @Nonnull ByteString value) {
    this.entries.add(new SetCell(familyName, qualifier, timestamp, value));
    return this;
  }

  ChangeStreamMutation deleteCells(
      @Nonnull String familyName,
      @Nonnull ByteString qualifier,
      @Nonnull TimestampRange timestampRange) {
    this.entries.add(new DeleteCells(familyName, qualifier, timestampRange));
    return this;
  }

  ChangeStreamMutation deleteFamily(@Nonnull String familyName) {
    this.entries.add(new DeleteFamily(familyName));
    return this;
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public RowMutation toRowMutation(@Nonnull String tableId) {
    Preconditions.checkArgument(
        token != null && lowWatermark != null,
        "ChangeStreamMutation must have a continuation token and low watermark.");
    RowMutation rowMutation = RowMutation.create(tableId, rowKey);
    for (Entry entry : this.entries.build()) {
      if (entry instanceof DeleteFamily) {
        rowMutation.deleteFamily(((DeleteFamily) entry).getFamilyName());
      } else if (entry instanceof DeleteCells) {
        DeleteCells deleteCells = (DeleteCells) entry;
        rowMutation.deleteCells(
            deleteCells.getFamilyName(),
            deleteCells.getQualifier(),
            deleteCells.getTimestampRange());
      } else if (entry instanceof SetCell) {
        SetCell setCell = (SetCell) entry;
        rowMutation.setCell(
            setCell.getFamilyName(),
            setCell.getQualifier(),
            setCell.getTimestamp(),
            setCell.getValue());
      } else {
        throw new IllegalArgumentException("Unexpected Entry type.");
      }
    }
    return rowMutation;
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public RowMutationEntry toRowMutationEntry() {
    Preconditions.checkArgument(
        token != null && lowWatermark != null,
        "ChangeStreamMutation must have a continuation token and low watermark.");
    RowMutationEntry rowMutationEntry = RowMutationEntry.create(rowKey);
    for (Entry entry : this.entries.build()) {
      if (entry instanceof DeleteFamily) {
        rowMutationEntry.deleteFamily(((DeleteFamily) entry).getFamilyName());
      } else if (entry instanceof DeleteCells) {
        DeleteCells deleteCells = (DeleteCells) entry;
        rowMutationEntry.deleteCells(
            deleteCells.getFamilyName(),
            deleteCells.getQualifier(),
            deleteCells.getTimestampRange());
      } else if (entry instanceof SetCell) {
        SetCell setCell = (SetCell) entry;
        rowMutationEntry.setCell(
            setCell.getFamilyName(),
            setCell.getQualifier(),
            setCell.getTimestamp(),
            setCell.getValue());
      } else {
        throw new IllegalArgumentException("Unexpected Entry type.");
      }
    }
    return rowMutationEntry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChangeStreamMutation otherChangeStreamMutation = (ChangeStreamMutation) o;
    return Objects.equal(this.toString(), otherChangeStreamMutation.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        rowKey, type, sourceClusterId, commitTimestamp, tieBreaker, token, lowWatermark, entries);
  }

  @Override
  public String toString() {
    List<String> entriesAsStrings = new ArrayList<>();
    for (Entry entry : this.entries.build()) {
      entriesAsStrings.add(entry.toString());
    }
    String entryString = "[" + String.join(";\t", entriesAsStrings) + "]";
    return MoreObjects.toStringHelper(this)
        .add("rowKey", this.rowKey.toStringUtf8())
        .add("type", this.type)
        .add("sourceClusterId", this.sourceClusterId)
        .add("commitTimestamp", this.commitTimestamp.toString())
        .add("token", this.token)
        .add("lowWatermark", this.lowWatermark)
        .add("entries", entryString)
        .toString();
  }
}
