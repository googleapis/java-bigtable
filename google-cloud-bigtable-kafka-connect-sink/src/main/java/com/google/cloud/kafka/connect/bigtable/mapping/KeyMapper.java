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

import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Time;
import org.apache.kafka.connect.data.Timestamp;
import org.apache.kafka.connect.errors.DataException;

/**
 * A class responsible for converting Kafka {@link org.apache.kafka.connect.sink.SinkRecord
 * SinkRecord(s)} into Cloud Bigtable row keys.
 */
public class KeyMapper {
  final List<List<String>> definition;
  final byte[] delimiter;

  /**
   * The main constructor.
   *
   * @param delimiter Delimiter in the mapping as per {@link
   *     com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig#ROW_KEY_DELIMITER_CONFIG}
   * @param definition Definition of the mapping as per {@link
   *     com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig#ROW_KEY_DEFINITION_CONFIG}.
   */
  public KeyMapper(String delimiter, List<String> definition) {
    this.delimiter = delimiter.getBytes(StandardCharsets.UTF_8);
    this.definition =
        definition.stream()
            .map(s -> s.split("\\."))
            .map(Arrays::asList)
            .collect(Collectors.toList());
  }

  /**
   * Converts input data into Cloud Bigtable row key bytes as described in {@link
   * BigtableSinkConfig#getDefinition()}.
   *
   * @param kafkaKeyAndSchema Value and optional {@link Schema} of a Kafka message's key to be
   *     converted into a Cloud Bigtable row key.
   * @return {@link Optional#empty()} if the input doesn't convert into a valid Cloud Bigtable row
   *     key, {@link Optional} containing row Cloud Bigtable row key bytes the input converts into
   *     otherwise.
   */
  public byte[] getKey(SchemaAndValue kafkaKeyAndSchema) {
    Object kafkaKey = kafkaKeyAndSchema.value();
    Optional<Schema> kafkaKeySchema = Optional.ofNullable(kafkaKeyAndSchema.schema());
    ensureKeyElementIsNotNull(kafkaKey);
    Stream<byte[]> keyParts =
        this.getDefinition(kafkaKey).stream()
            .map(
                (d) ->
                    serializeTopLevelKeyElement(
                        extractField(kafkaKey, kafkaKeySchema, d.iterator())));
    return concatenateByteArrays(new byte[0], keyParts, delimiter, new byte[0]);
  }

  /**
   * Returns key definition as configured during object creation or extracted from the object being
   * mapped if it's been configured to an empty {@link List}.
   *
   * @param kafkaKey {@link org.apache.kafka.connect.sink.SinkRecord SinkRecord's} key.
   * @return {@link List} containing {@link List Lists} of key fields that need to be retrieved and
   *     concatenated to construct the Cloud Bigtable row key.
   *     <p>See {@link KeyMapper#extractField(Object, Optional, Iterator)} for details on semantics
   *     of the inner list.
   */
  private List<List<String>> getDefinition(Object kafkaKey) {
    if (this.definition.isEmpty()) {
      Optional<List<String>> maybeRootFields = getFieldsOfRootValue(kafkaKey);
      if (maybeRootFields.isEmpty()) {
        List<String> rootElementDefinition = List.of();
        return List.of(rootElementDefinition);
      } else {
        return maybeRootFields.get().stream()
            .map(Collections::singletonList)
            .collect(Collectors.toList());
      }
    }
    return this.definition;
  }

  /**
   * Extracts names of child fields of the value.
   *
   * @param kafkaKey {@link org.apache.kafka.connect.sink.SinkRecord SinkRecord's} key.
   * @return {@link Optional#empty()} if the input value has no children, {@link Optional}
   *     containing names of its child fields otherwise.
   */
  private static Optional<List<String>> getFieldsOfRootValue(Object kafkaKey) {
    if (kafkaKey instanceof Struct) {
      return Optional.of(
          ((Struct) kafkaKey)
              .schema().fields().stream().map(Field::name).collect(Collectors.toList()));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Extract possibly nested fields from the input value.
   *
   * @param value {@link org.apache.kafka.connect.sink.SinkRecord SinkRecord's} key or some its
   *     child.
   * @param schema A schema of {@code value}.
   * @param fields Fields that need to be accessed before the target value is reached.
   * @return Extracted nested field.
   */
  private SchemaAndValue extractField(
      Object value, Optional<Schema> schema, Iterator<String> fields) {
    ensureKeyElementIsNotNull(value);
    LogicalTypeUtils.logIfLogicalTypeUnsupported(schema);
    if (!fields.hasNext()) {
      return new SchemaAndValue(schema.orElse(null), value);
    }
    String field = fields.next();
    if (value instanceof Struct) {
      Struct struct = (Struct) value;
      // Note that getWithoutDefault() throws if such a field does not exist.
      return extractField(
          struct.getWithoutDefault(field),
          SchemaUtils.maybeExtractFieldSchema(schema, field),
          fields);
    } else {
      throw new DataException(
          "Unexpected class `"
              + value.getClass().getName()
              + "` doesn't support extracting field `"
              + field
              + "` using a dot.");
    }
  }

  private static byte[] serializeTopLevelKeyElement(SchemaAndValue keyElementAndSchema) {
    Object keyElement = keyElementAndSchema.value();
    ensureKeyElementIsNotNull(keyElement);
    return serializeKeyElement(keyElement, Optional.ofNullable(keyElementAndSchema.schema()));
  }

  /**
   * Serializes Kafka Connect entry key.
   *
   * <p>We implement custom serialization since {@link Object#toString()} mangles arrays.
   *
   * @param keyElement {@link org.apache.kafka.connect.sink.SinkRecord SinkRecord's} key to be
   *     serialized.
   * @param keyElementSchema An optional schema of {@code keyElement}.
   * @return Serialization of the input value.
   */
  private static byte[] serializeKeyElement(Object keyElement, Optional<Schema> keyElementSchema) {
    if (keyElement == null) {
      // Note that it's needed for serializing null-containing Maps and Lists.
      return serializeKeyElement("null", Optional.empty());
    } else if (keyElement instanceof byte[]) {
      return (byte[]) keyElement;
    } else if (keyElement instanceof String) {
      return ((String) keyElement).getBytes(StandardCharsets.UTF_8);
    } else if (keyElement instanceof ByteBuffer) {
      return ((ByteBuffer) keyElement).array();
    } else if (keyElement instanceof Boolean
        || keyElement instanceof Byte
        || keyElement instanceof Short
        || keyElement instanceof Integer
        || keyElement instanceof Long
        || keyElement instanceof Float
        || keyElement instanceof Double
        || keyElement instanceof Character) {
      return serializeKeyElement(keyElement.toString(), Optional.empty());
    } else if (keyElement instanceof List) {
      // Note that it breaks compatibility with Confluent's sink when serializing byte array
      // elements.
      List<?> list = (List<?>) keyElement;
      Optional<Schema> elementSchema = SchemaUtils.maybeExtractValueSchema(keyElementSchema);
      return concatenateByteArrays(
          "[", list.stream().map(e -> serializeKeyElement(e, elementSchema)), ", ", "]");
    } else if (keyElement instanceof Map) {
      // Note that it breaks compatibility with Confluent's sink when serializing byte array keys or
      // values.
      Map<?, ?> map = (Map<?, ?>) keyElement;
      Optional<Schema> keySchema = SchemaUtils.maybeExtractKeySchema(keyElementSchema);
      Optional<Schema> valueSchema = SchemaUtils.maybeExtractValueSchema(keyElementSchema);
      return concatenateByteArrays(
          "{",
          // Note that it inherits ordering of entries from the configured converter.
          map.entrySet().stream()
              .map(
                  e ->
                      concatenateByteArrays(
                          new byte[0],
                          Stream.of(
                              serializeKeyElement(e.getKey(), keySchema),
                              serializeKeyElement(e.getValue(), valueSchema)),
                          "=".getBytes(StandardCharsets.UTF_8),
                          new byte[0])),
          // Note that Map and Struct have different delimiters for compatibility's sake.
          ", ",
          "}");
    } else if (keyElement instanceof Struct) {
      // Note that it breaks compatibility with Confluent's sink when serializing byte array fields.
      Struct struct = (Struct) keyElement;
      Optional<Schema> fieldNameSchema = Optional.empty();
      return concatenateByteArrays(
          "Struct{",
          struct.schema().fields().stream()
              .flatMap(
                  f ->
                      Optional.ofNullable(struct.get(f))
                          .map(v -> new AbstractMap.SimpleImmutableEntry<>(f.name(), v))
                          .stream())
              .map(
                  e ->
                      concatenateByteArrays(
                          new byte[0],
                          Stream.of(
                              serializeKeyElement(e.getKey(), Optional.empty()),
                              serializeKeyElement(
                                  e.getValue(),
                                  SchemaUtils.maybeExtractFieldSchema(
                                      keyElementSchema, e.getKey()))),
                          "=".getBytes(StandardCharsets.UTF_8),
                          new byte[0])),
          // Note that Map and Struct have different delimiters for compatibility's sake.
          ",",
          "}");
    } else if (keyElement instanceof Date) {
      // Note that it breaks compatibility with Confluent's sink, which seems to use toString().
      DateTimeFormatter fmt;
      switch (keyElementSchema.map(Schema::name).orElse(Timestamp.LOGICAL_NAME)) {
        case org.apache.kafka.connect.data.Date.LOGICAL_NAME:
          fmt = DateTimeFormatter.ISO_LOCAL_DATE;
          break;
        case Time.LOGICAL_NAME:
          fmt = DateTimeFormatter.ISO_LOCAL_TIME;
          break;
        default:
          fmt = DateTimeFormatter.ISO_INSTANT;
          break;
      }
      String formatted = fmt.format(((Date) keyElement).toInstant().atZone(ZoneOffset.UTC));
      return serializeKeyElement(formatted, Optional.empty());
    } else if (keyElement instanceof BigDecimal) {
      // Note that it breaks compatibility with Confluent's sink when serializing values for which
      // toString() returns values using scientific notation.
      return serializeKeyElement(((BigDecimal) keyElement).toPlainString(), Optional.empty());
    } else {
      throw new DataException(
          "Unsupported serialization of an unexpected class `"
              + keyElement.getClass().getName()
              + "` in key.");
    }
  }

  private static void ensureKeyElementIsNotNull(Object value) {
    if (value == null) {
      // Matching Confluent's sink behavior.
      throw new DataException("Error with row key definition: row key fields cannot be null.");
    }
  }

  private static byte[] concatenateByteArrays(
      byte[] start, Stream<byte[]> byteArrays, byte[] delimiter, byte[] end) {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
      bos.write(start);
      for (Iterator<byte[]> it = byteArrays.iterator(); it.hasNext(); ) {
        byte[] keyPart = it.next();
        bos.write(keyPart);
        if (it.hasNext()) {
          bos.write(delimiter);
        }
      }
      bos.write(end);
      return bos.toByteArray();
    } catch (IOException e) {
      throw new DataException("Concatenation of Cloud Bigtable key failed.", e);
    }
  }

  private static byte[] concatenateByteArrays(
      String start, Stream<byte[]> byteArrays, String delimiter, String end) {
    return concatenateByteArrays(
        start.getBytes(StandardCharsets.UTF_8),
        byteArrays,
        delimiter.getBytes(StandardCharsets.UTF_8),
        end.getBytes(StandardCharsets.UTF_8));
  }
}
