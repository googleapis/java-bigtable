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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.cloud.ByteArray;
import com.google.cloud.bigtable.data.v2.models.Range;
import com.google.cloud.kafka.connect.bigtable.config.ConfigInterpolation;
import com.google.cloud.kafka.connect.bigtable.config.NullValueMode;
import com.google.common.annotations.VisibleForTesting;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.common.utils.Utils;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.DataException;

/**
 * A class responsible for converting Kafka {@link org.apache.kafka.connect.sink.SinkRecord
 * SinkRecord(s)} into Cloud Bigtable {@link com.google.cloud.bigtable.data.v2.models.Mutation
 * Mutation(s)}.
 */
public class ValueMapper {
  public final String defaultColumnFamilyTemplate;
  public final ByteString defaultColumnQualifier;
  private final NullValueMode nullMode;
  private static final ObjectMapper jsonMapper = getJsonMapper();

  /**
   * The main constructor.
   *
   * @param defaultColumnFamily Default column family as per {@link
   *     com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig#CONFIG_DEFAULT_COLUMN_FAMILY}.
   * @param defaultColumnQualifier Default column as per {@link
   *     com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig#CONFIG_ROW_KEY_DELIMITER}.
   */
  public ValueMapper(
      String defaultColumnFamily, String defaultColumnQualifier, @Nonnull NullValueMode nullMode) {
    this.defaultColumnFamilyTemplate =
        Utils.isBlank(defaultColumnFamily) ? null : defaultColumnFamily;
    this.defaultColumnQualifier =
        Utils.isBlank(defaultColumnQualifier)
            ? null
            : ByteString.copyFrom(defaultColumnQualifier.getBytes(StandardCharsets.UTF_8));
    this.nullMode = nullMode;
  }

  /**
   * Creates a {@link MutationDataBuilder} that can be used to create a {@link MutationData}
   * representing the input Kafka Connect value as Cloud Bigtable mutations that need to be applied.
   *
   * @param rootKafkaValue The value to be converted into Cloud Bigtable {@link
   *     com.google.cloud.bigtable.data.v2.models.Mutation Mutation(s)}.
   * @param topic The name of Kafka topic this value originates from.
   * @param timestampMicros The timestamp the mutations will be created at in microseconds.
   */
  public MutationDataBuilder getRecordMutationDataBuilder(
      Object rootKafkaValue, String topic, long timestampMicros) {
    MutationDataBuilder mutationDataBuilder = createMutationDataBuilder();
    if (rootKafkaValue == null && nullMode == NullValueMode.IGNORE) {
      // Do nothing
    } else if (rootKafkaValue == null && nullMode == NullValueMode.DELETE) {
      mutationDataBuilder.deleteRow();
    } else if (rootKafkaValue instanceof Map || rootKafkaValue instanceof Struct) {
      for (Map.Entry<Object, Object> field : getChildren(rootKafkaValue)) {
        String kafkaFieldName = field.getKey().toString();
        Object kafkaFieldValue = field.getValue();
        if (kafkaFieldValue == null && nullMode == NullValueMode.IGNORE) {
          continue;
        } else if (kafkaFieldValue == null && nullMode == NullValueMode.DELETE) {
          mutationDataBuilder.deleteFamily(kafkaFieldName);
        } else if (kafkaFieldValue instanceof Map || kafkaFieldValue instanceof Struct) {
          for (Map.Entry<Object, Object> subfield : getChildren(kafkaFieldValue)) {
            ByteString kafkaSubfieldName =
                ByteString.copyFrom(subfield.getKey().toString().getBytes(StandardCharsets.UTF_8));
            Object kafkaSubfieldValue = subfield.getValue();
            if (kafkaSubfieldValue == null && nullMode == NullValueMode.IGNORE) {
              continue;
            } else if (kafkaSubfieldValue == null && nullMode == NullValueMode.DELETE) {
              mutationDataBuilder.deleteCells(
                  kafkaFieldName,
                  kafkaSubfieldName,
                  Range.TimestampRange.create(0, timestampMicros));
            } else {
              mutationDataBuilder.setCell(
                  kafkaFieldName,
                  kafkaSubfieldName,
                  timestampMicros,
                  ByteString.copyFrom(serialize(kafkaSubfieldValue)));
            }
          }
        } else {
          if (defaultColumnFamilyTemplate != null) {
            mutationDataBuilder.setCell(
                getDefaultColumnFamily(topic),
                ByteString.copyFrom(kafkaFieldName.getBytes(StandardCharsets.UTF_8)),
                timestampMicros,
                ByteString.copyFrom(serialize(kafkaFieldValue)));
          }
        }
      }
    } else {
      if (defaultColumnFamilyTemplate != null && defaultColumnQualifier != null) {
        mutationDataBuilder.setCell(
            getDefaultColumnFamily(topic),
            defaultColumnQualifier,
            timestampMicros,
            ByteString.copyFrom(serialize(rootKafkaValue)));
      }
    }
    return mutationDataBuilder;
  }

  @VisibleForTesting
  // Method only needed for use in tests. It could be inlined otherwise.
  protected MutationDataBuilder createMutationDataBuilder() {
    return new MutationDataBuilder();
  }

  protected String getDefaultColumnFamily(String topic) {
    return ConfigInterpolation.replace(defaultColumnFamilyTemplate, topic);
  }

  /**
   * @param mapOrStruct {@link Map} or {@link Struct} whose children we want to list
   * @return {@link List} of names or keys of input value's child entries.
   */
  private static List<Map.Entry<Object, Object>> getChildren(Object mapOrStruct) {
    if (mapOrStruct instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<Object, Object> kafkaMapValue = (Map<Object, Object>) mapOrStruct;
      return new ArrayList<>(kafkaMapValue.entrySet());
    } else if (mapOrStruct instanceof Struct) {
      Struct kafkaStructValue = (Struct) mapOrStruct;
      return kafkaStructValue.schema().fields().stream()
          .map(
              f ->
                  new AbstractMap.SimpleImmutableEntry<>(
                      (Object) f.name(), kafkaStructValue.get(f)))
          .collect(Collectors.toList());
    } else {
      throw new IllegalStateException();
    }
  }

  /**
   * @param value Input value.
   * @return Input value's serialization's bytes that will be written to Cloud Bigtable as a cell's
   *     value.
   */
  private static byte[] serialize(Object value) {
    if (value == null) {
      return new byte[0];
    }
    if (value instanceof byte[]) {
      return (byte[]) value;
    } else if (value instanceof ByteArray) {
      return serialize(((ByteArray) value).toByteArray());
    } else if (value instanceof Integer) {
      return Bytes.toBytes((Integer) value);
    } else if (value instanceof Long) {
      return Bytes.toBytes((Long) value);
    } else if (value instanceof Short) {
      return Bytes.toBytes((Short) value);
    } else if (value instanceof Byte) {
      return Bytes.toBytes((Byte) value);
    } else if (value instanceof Float) {
      return Bytes.toBytes((Float) value);
    } else if (value instanceof Double) {
      return Bytes.toBytes((Double) value);
    } else if (value instanceof Boolean) {
      return Bytes.toBytes((Boolean) value);
    } else if (value instanceof String) {
      return Bytes.toBytes((String) value);
    } else if (value instanceof Character) {
      return serialize(Character.toString((Character) value));
    } else if (value instanceof Date) {
      // TODO: implement.
      throw new DataException("TODO");
    } else if (value instanceof BigDecimal) {
      // TODO: implement.
      throw new DataException("TODO");
    } else if (value instanceof Map || value instanceof Struct || value instanceof List) {
      try {
        return jsonMapper.writeValueAsBytes(value);
      } catch (JsonProcessingException e) {
        throw new DataException("Failed to deserialize a(n) " + value.getClass(), e);
      }
    } else {
      throw new DataException(
          "Unsupported serialization of an unexpected class `" + value.getClass() + "`.");
    }
  }

  /**
   * @return {@link ObjectMapper} that can serialize all the Kafka Connect types.
   */
  private static ObjectMapper getJsonMapper() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule mapperModule = new SimpleModule("KafkaConnectSerializer");
    mapperModule.addSerializer(Struct.class, new StructJsonSerializer(Struct.class));
    mapper.registerModule(mapperModule);
    return mapper;
  }

  private static class StructJsonSerializer extends StdSerializer<Struct> {
    protected StructJsonSerializer(Class<Struct> t) {
      super(t);
    }

    @Override
    public void serialize(Struct value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      Schema schema = value.schema();
      gen.writeStartObject();
      for (Field field : schema.fields()) {
        String fieldName = field.name();
        gen.writeObjectField(fieldName, value.getWithoutDefault(fieldName));
      }
      gen.writeEndObject();
    }
  }
}
