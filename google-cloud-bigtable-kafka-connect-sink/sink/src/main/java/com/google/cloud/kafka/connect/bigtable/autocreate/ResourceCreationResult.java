/*
 * Copyright 2025 Google LLC
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
package com.google.cloud.kafka.connect.bigtable.autocreate;

import java.util.HashSet;
import java.util.Set;
import org.apache.kafka.connect.sink.SinkRecord;

/** A record class storing the output of {@link BigtableSchemaManager} operations. */
public class ResourceCreationResult {
  private final Set<SinkRecord> bigtableErrors;
  private final Set<SinkRecord> dataErrors;

  public static ResourceCreationResult empty() {
    return new ResourceCreationResult(new HashSet<>(), new HashSet<>());
  }

  public ResourceCreationResult(Set<SinkRecord> bigtableErrors, Set<SinkRecord> dataErrors) {
    this.bigtableErrors = bigtableErrors;
    this.dataErrors = dataErrors;
  }

  /**
   * @return A {@link Set} of {@link SinkRecord SinkRecord(s)} for which resource auto-creation
   *     failed due to some problems on Cloud Bigtable part.
   */
  public Set<SinkRecord> getBigtableErrors() {
    return bigtableErrors;
  }

  /**
   * @return A {@link Set} of {@link SinkRecord SinkRecord(s)} for which resource auto-creation
   *     failed due to invalid input data. These records should not ever be retried.
   */
  public Set<SinkRecord> getDataErrors() {
    return dataErrors;
  }
}
