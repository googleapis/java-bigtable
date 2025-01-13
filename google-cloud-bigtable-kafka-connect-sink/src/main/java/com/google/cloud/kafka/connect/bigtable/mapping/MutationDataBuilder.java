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
package com.google.cloud.kafka.connect.bigtable.mapping;

import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.models.Range;
import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.ByteString;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/** A builder class for {@link MutationData}. */
public class MutationDataBuilder {
  private final Mutation mutation;
  private boolean mutationIsEmpty;
  private final Set<String> requiredColumnFamilies;

  @VisibleForTesting
  MutationDataBuilder(Mutation mutation) {
    this.mutation = mutation;
    mutationIsEmpty = true;
    requiredColumnFamilies = new HashSet<>();
  }

  public MutationDataBuilder() {
    this(Mutation.create());
  }

  /**
   * Tries to convert this object into {@link MutationData}.
   *
   * @param targetTable - Cloud Bigtable {@link com.google.cloud.bigtable.admin.v2.models.Table}
   *     this mutation is to be written to.
   * @param rowKey - Cloud Bigtable row key this mutation is to be written to.
   * @return {@link Optional#empty()} if this mutation is empty, an {@link Optional} containing this
   *     mutation ready to be written to Cloud Bigtable otherwise.
   */
  public Optional<MutationData> maybeBuild(String targetTable, ByteString rowKey) {
    return this.mutationIsEmpty
        ? Optional.empty()
        : Optional.of(
            new MutationData(targetTable, rowKey, this.mutation, this.requiredColumnFamilies));
  }

  public void deleteRow() {
    mutationIsEmpty = false;
    mutation.deleteRow();
  }

  public void deleteFamily(String columnFamily) {
    mutationIsEmpty = false;
    mutation.deleteFamily(columnFamily);
  }

  public void deleteCells(
      String columnFamily, ByteString columnQualifier, Range.TimestampRange timestampRange) {
    mutationIsEmpty = false;
    mutation.deleteCells(columnFamily, columnQualifier, timestampRange);
  }

  public void setCell(
      String columnFamily, ByteString columnQualifier, long timestampMicros, ByteString value) {
    mutationIsEmpty = false;
    requiredColumnFamilies.add(columnFamily);
    mutation.setCell(columnFamily, columnQualifier, timestampMicros, value);
  }
}
