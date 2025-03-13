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
package com.google.cloud.kafka.connect.bigtable.mapping;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.apache.kafka.connect.data.Date;
import org.apache.kafka.connect.data.Decimal;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Time;
import org.apache.kafka.connect.data.Timestamp;

/** A class facilitating support for Kafka Connect logical types. */
public class LogicalTypeUtils {
  private static final Set<String> SUPPORTED_LOGICAL_TYPES =
      Set.of(Date.LOGICAL_NAME, Time.LOGICAL_NAME, Timestamp.LOGICAL_NAME, Decimal.LOGICAL_NAME);
  private static final Set<String> REPORTED_UNSUPPORTED_LOGICAL_TYPES = new HashSet<>();

  /**
   * Logs a warning if the encountered logical type is unsupported. To prevent flooding the log, it
   * only reports each unsupported logical type at most once for each {@link
   * com.google.cloud.kafka.connect.bigtable.BigtableSinkTask}.
   *
   * @param schema A schema of a Kafka key or value about which type's lack of support the user
   *     should be notified.
   */
  public static void logIfLogicalTypeUnsupported(Optional<Schema> schema) {
    String logicalTypeName = schema.map(Schema::name).orElse(null);
    boolean isStruct = schema.map(s -> s.type() == Schema.Type.STRUCT).orElse(false);
    // Note that `Struct`s may have non-null names, but we do support them (ignoring their name).
    if (isStruct || logicalTypeName == null) {
      return;
    }
    if (!SUPPORTED_LOGICAL_TYPES.contains(logicalTypeName)) {
      synchronized (REPORTED_UNSUPPORTED_LOGICAL_TYPES) {
        SchemaErrorReporter.reportProblemAtMostOnce(
            REPORTED_UNSUPPORTED_LOGICAL_TYPES,
            logicalTypeName,
            "Unsupported logical type: `"
                + logicalTypeName
                + "`. Falling back to handling the value according to its physical type.");
      }
    }
  }
}
