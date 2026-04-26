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
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;

/**
 * A class containing operations on {@link Schema Schema(s)}.
 *
 * <p>The mappers ({@link KeyMapper}, {@link ValueMapper}) that make use of this class treat {@link
 * Schema Schema (s)} as a hint, not as a requirement. Thus, despite the fact that the operations in
 * {@code try} blocks should not throw, we do catch possible exceptions in order not to fail the
 * records in case the converter the user configured does something unexpected.
 *
 * <p>This class uses {@link SchemaErrorReporter#reportProblemAtMostOnce(Set, Object, String)} to
 * avoid flooding the logs since these errors are not fatal and could pessimistically be very
 * frequent.
 */
public class SchemaUtils {
  private static final Set<Schema> PROBLEMS_WITH_VALUE_SCHEMA = new HashSet<>();
  private static final Set<Schema> PROBLEMS_WITH_KEY_SCHEMA = new HashSet<>();
  private static final Set<SchemaAndField> PROBLEMS_WITH_FIELD_SCHEMA = new HashSet<>();

  public static Optional<Schema> maybeExtractValueSchema(Optional<Schema> maybeSchema) {
    try {
      return maybeSchema.map(Schema::valueSchema);
    } catch (Exception ignored) {
      synchronized (PROBLEMS_WITH_VALUE_SCHEMA) {
        maybeSchema.ifPresent(
            s ->
                SchemaErrorReporter.reportProblemAtMostOnce(
                    PROBLEMS_WITH_VALUE_SCHEMA,
                    s,
                    String.format(
                        "A Kafka Connect object with schema `%s` has no schema for its values.",
                        s)));
      }
      return Optional.empty();
    }
  }

  public static Optional<Schema> maybeExtractKeySchema(Optional<Schema> maybeSchema) {
    try {
      return maybeSchema.map(Schema::keySchema);
    } catch (Exception ignored) {
      synchronized (PROBLEMS_WITH_KEY_SCHEMA) {
        maybeSchema.ifPresent(
            s ->
                SchemaErrorReporter.reportProblemAtMostOnce(
                    PROBLEMS_WITH_KEY_SCHEMA,
                    s,
                    String.format(
                        "A Kafka Connect object with schema `%s` has no schema for its keys.", s)));
        return Optional.empty();
      }
    }
  }

  public static Optional<Schema> maybeExtractFieldSchema(
      Optional<Schema> maybeSchema, String field) {
    try {
      return maybeSchema.map(s -> s.field(field)).map(Field::schema);
    } catch (Exception ignored) {
      maybeSchema.ifPresent(
          s ->
              SchemaErrorReporter.reportProblemAtMostOnce(
                  PROBLEMS_WITH_FIELD_SCHEMA,
                  new SchemaAndField(s, field),
                  String.format(
                      "A Kafka Connect object with schema `%s` has no schema for its field `%s`.",
                      s, field)));
      return Optional.empty();
    }
  }

  private static class SchemaAndField {
    private final Schema schema;
    private final String field;

    public SchemaAndField(Schema schema, String field) {
      this.schema = schema;
      this.field = field;
    }

    @Override
    public int hashCode() {
      return Objects.hash(schema, field);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      SchemaAndField other = (SchemaAndField) o;
      return Objects.equals(schema, other.schema) && Objects.equals(field, other.field);
    }
  }
}
