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
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.protobuf.ByteString;
import java.util.Set;

/**
 * A class representing single Kafka {@link org.apache.kafka.connect.sink.SinkRecord SinkRecord's}
 * output to be written into Cloud Bigtable.
 */
public class MutationData {
  private final String targetTable;
  private final ByteString rowKey;
  private final Mutation mutation;
  private final Set<String> requiredColumnFamilies;

  public MutationData(
      String targetTable,
      ByteString rowKey,
      Mutation mutation,
      Set<String> requiredColumnFamilies) {
    this.targetTable = targetTable;
    this.rowKey = rowKey;
    this.mutation = mutation;
    this.requiredColumnFamilies = requiredColumnFamilies;
  }

  public String getTargetTable() {
    return targetTable;
  }

  public ByteString getRowKey() {
    return rowKey;
  }

  public RowMutationEntry getUpsertMutation() {
    return RowMutationEntry.createFromMutationUnsafe(this.rowKey, this.mutation);
  }

  public Mutation getInsertMutation() {
    return mutation;
  }

  public Set<String> getRequiredColumnFamilies() {
    return requiredColumnFamilies;
  }
}
