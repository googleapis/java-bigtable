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
import com.google.auto.value.AutoValue;
import com.google.cloud.bigtable.data.v2.models.Range.TimestampRange;
import com.google.cloud.bigtable.data.v2.stub.changestream.ChangeStreamRecordMerger;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * A ChangeStreamMutation represents a list of mods(represented by List<{@link Entry}>) targeted at
 * a single row, which is concatenated by {@link ChangeStreamRecordMerger}. It represents a logical
 * row mutation and can be converted to the original write request(i.e. {@link RowMutation} or
 * {@link RowMutationEntry}.
 *
 * <p>A ChangeStreamMutation can be constructed in two ways, depending on whether it's a user
 * initiated mutation or a Garbage Collection mutation. Either way, the caller should explicitly set
 * `token` and `lowWatermark` before build(), otherwise it'll raise an error.
 *
 * <p>Case 1) User initiated mutation.
 *
 * <pre>{@code
 * ChangeStreamMutation.Builder builder = ChangeStreamMutation.createUserMutation(...);
 * builder.setCell(...);
 * builder.deleteFamily(...);
 * builder.deleteCells(...);
 * ChangeStreamMutation changeStreamMutation = builder.setToken(...).setLowWatermark().build();
 * }</pre>
 *
 * Case 2) Garbage Collection mutation.
 *
 * <pre>{@code
 * ChangeStreamMutation.Builder builder = ChangeStreamMutation.createGcMutation(...);
 * builder.setCell(...);
 * builder.deleteFamily(...);
 * builder.deleteCells(...);
 * ChangeStreamMutation changeStreamMutation = builder.setToken(...).setLowWatermark().build();
 * }</pre>
 *
 * Make this class non-final so that we can create a subclass to mock it.
 */
@InternalApi("Intended for use by the BigtableIO in apache/beam only.")
@AutoValue
public abstract class ChangeStreamMutation implements ChangeStreamRecord, Serializable {
  private static final long serialVersionUID = 8419520253162024218L;

  public enum MutationType {
    USER,
    GARBAGE_COLLECTION
  }

  private static ChangeStreamMutation create(Builder builder) {
    return new AutoValue_ChangeStreamMutation(
        builder.rowKey,
        builder.type,
        builder.sourceClusterId,
        builder.commitTimestamp,
        builder.tieBreaker,
        builder.token,
        builder.lowWatermark,
        builder.entries.build());
  }

  /**
   * Creates a new instance of a user initiated mutation. It returns a builder instead of a
   * ChangeStreamMutation because `token` and `loWatermark` must be set later when we finish
   * building the logical mutation.
   */
  static Builder createUserMutation(
      @Nonnull ByteString rowKey,
      @Nonnull String sourceClusterId,
      @Nonnull Timestamp commitTimestamp,
      int tieBreaker) {
    return new Builder(rowKey, MutationType.USER, sourceClusterId, commitTimestamp, tieBreaker);
  }

  /**
   * Creates a new instance of a GC mutation. It returns a builder instead of a ChangeStreamMutation
   * because `token` and `loWatermark` must be set later when we finish building the logical
   * mutation.
   */
  static Builder createGcMutation(
      @Nonnull ByteString rowKey, @Nonnull Timestamp commitTimestamp, int tieBreaker) {
    return new Builder(rowKey, MutationType.GARBAGE_COLLECTION, "", commitTimestamp, tieBreaker);
  }

  /** Get the row key of the current mutation. */
  @Nonnull
  public abstract ByteString getRowKey();

  /** Get the type of the current mutation. */
  @Nonnull
  public abstract MutationType getType();

  @Nonnull
  /** Get the source cluster id of the current mutation. Null for Garbage collection mutation. */
  public abstract String getSourceClusterId();

  /** Get the commit timestamp of the current mutation. */
  @Nonnull
  public abstract Timestamp getCommitTimestamp();

  /**
   * Get the tie breaker of the current mutation. This is used to resolve conflicts when multiple
   * mutations are applied to different clusters at the same time.
   */
  @Nonnull
  public abstract int getTieBreaker();

  /** Get the token of the current mutation, which can be used to resume the changestream. */
  @Nonnull
  public abstract String getToken();

  /** Get the low watermark of the current mutation. */
  @Nonnull
  public abstract Timestamp getLowWatermark();

  /** Get the list of mods of the current mutation. */
  @Nonnull
  public abstract List<Entry> getEntries();

  /** Returns a builder containing all the values of this ChangeStreamMutation class. */
  Builder toBuilder() {
    return new Builder(this);
  }

  /** Helper class to create a ChangeStreamMutation. */
  @InternalApi("Intended for use by the BigtableIO in apache/beam only.")
  public static class Builder {
    private final ByteString rowKey;

    private final MutationType type;

    private final String sourceClusterId;

    private final Timestamp commitTimestamp;

    private final int tieBreaker;

    private transient ImmutableList.Builder<Entry> entries = ImmutableList.builder();

    private String token;

    private Timestamp lowWatermark;

    private Builder(
        ByteString rowKey,
        MutationType type,
        String sourceClusterId,
        Timestamp commitTimestamp,
        int tieBreaker) {
      this.rowKey = rowKey;
      this.type = type;
      this.sourceClusterId = sourceClusterId;
      this.commitTimestamp = commitTimestamp;
      this.tieBreaker = tieBreaker;
    }

    private Builder(ChangeStreamMutation changeStreamMutation) {
      this.rowKey = changeStreamMutation.getRowKey();
      this.type = changeStreamMutation.getType();
      this.sourceClusterId = changeStreamMutation.getSourceClusterId();
      this.commitTimestamp = changeStreamMutation.getCommitTimestamp();
      this.tieBreaker = changeStreamMutation.getTieBreaker();
      this.entries.addAll(changeStreamMutation.getEntries());
      this.token = changeStreamMutation.getToken();
      this.lowWatermark = changeStreamMutation.getLowWatermark();
    }

    Builder setCell(
        @Nonnull String familyName,
        @Nonnull ByteString qualifier,
        long timestamp,
        @Nonnull ByteString value) {
      this.entries.add(SetCell.create(familyName, qualifier, timestamp, value));
      return this;
    }

    Builder deleteCells(
        @Nonnull String familyName,
        @Nonnull ByteString qualifier,
        @Nonnull TimestampRange timestampRange) {
      this.entries.add(DeleteCells.create(familyName, qualifier, timestampRange));
      return this;
    }

    Builder deleteFamily(@Nonnull String familyName) {
      this.entries.add(DeleteFamily.create(familyName));
      return this;
    }

    Builder setToken(@Nonnull String token) {
      this.token = token;
      return this;
    }

    Builder setLowWatermark(@Nonnull Timestamp lowWatermark) {
      this.lowWatermark = lowWatermark;
      return this;
    }

    ChangeStreamMutation build() {
      Preconditions.checkArgument(
          token != null && lowWatermark != null,
          "ChangeStreamMutation must have a continuation token and low watermark.");
      return ChangeStreamMutation.create(this);
    }
  }

  public RowMutation toRowMutation(@Nonnull String tableId) {
    RowMutation rowMutation = RowMutation.create(tableId, getRowKey());
    for (Entry entry : getEntries()) {
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

  public RowMutationEntry toRowMutationEntry() {
    RowMutationEntry rowMutationEntry = RowMutationEntry.create(getRowKey());
    for (Entry entry : getEntries()) {
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
}
