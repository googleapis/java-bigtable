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

/*
 * This software contains code derived from the BigQuery Connector for Apache Kafka,
 * Copyright Aiven Oy, which in turn contains code derived from the Confluent BigQuery
 * Kafka Connector, Copyright Confluent, Inc, which in turn contains code derived from
 * the WePay BigQuery Kafka Connector, Copyright WePay, Inc.
 */

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.cloud.kafka.connect.bigtable.util.JsonConverterFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.json.JsonConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

// Note that in many tests when we compare with toString() result, we most probably compare our and
// Confluent sink's implementations.
@RunWith(JUnit4.class)
public class KeyMapperTest {
  private static final String DELIMITER = "##";

  @Test
  public void testBoolean() {
    final String fieldName = "Boolean";
    final Boolean fieldValue = true;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.BOOLEAN_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldValue);

    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, fieldValue),
        fieldValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testByte() {
    final String fieldName = "Byte";
    final Byte fieldByteValue = (byte) 42;
    Schema kafkaConnectSchema = SchemaBuilder.struct().field(fieldName, Schema.INT8_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldByteValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, fieldByteValue),
        fieldByteValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldByteValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldByteValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testShort() {
    final String fieldName = "Short";
    final Short fieldShortValue = (short) 4242;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.INT16_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldShortValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, fieldShortValue),
        fieldShortValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldShortValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldShortValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testInteger() {
    String fieldName = "Integer";
    final Integer fieldIntegerValue = 424242;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.INT32_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldIntegerValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, fieldIntegerValue),
        fieldIntegerValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldIntegerValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldIntegerValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testLong() {
    String fieldName = "Long";
    final Long fieldLongValue = 424242424242L;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.INT64_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldLongValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, fieldLongValue),
        fieldLongValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldLongValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldLongValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testFloat() {
    final String fieldName = "Float";
    final Float fieldFloatValue = 4242424242.4242F;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.FLOAT32_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldFloatValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, fieldFloatValue),
        fieldFloatValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldFloatValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldFloatValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testDouble() {
    final String fieldName = "Double";
    final Double fieldDoubleValue = 4242424242.4242;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.FLOAT64_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldDoubleValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, fieldDoubleValue),
        fieldDoubleValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldDoubleValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldDoubleValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testDoubleSpecial() {
    final String fieldName = "Double";

    List<Double> testValues =
        Arrays.asList(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);
    List<Double> expectedValues =
        Arrays.asList(Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    assertEquals(testValues.size(), expectedValues.size());

    for (Double testValue : testValues) {
      Schema kafkaConnectSchema =
          SchemaBuilder.struct().field(fieldName, Schema.FLOAT64_SCHEMA).build();

      Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
      kafkaConnectStruct.put(fieldName, testValue);
      assertArrayEquals(
          calculateKey(List.of(), DELIMITER, testValue),
          testValue.toString().getBytes(StandardCharsets.UTF_8));
      assertArrayEquals(
          calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
          testValue.toString().getBytes(StandardCharsets.UTF_8));
      assertArrayEquals(
          calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
          testValue.toString().getBytes(StandardCharsets.UTF_8));
    }
  }

  @Test
  public void testString() {
    final String fieldName = "String";
    final String fieldValue = "42424242424242424242424242424242";
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.STRING_SCHEMA).build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, fieldValue),
        fieldValue.getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldValue.getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldValue.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testFlatStruct() {
    final String fieldStringName = "InnerString";
    final String fieldIntegerName = "InnerInt";
    final String stringValue = "forty two";
    final Integer integerValue = 42;

    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(fieldStringName, Schema.STRING_SCHEMA)
            .field(fieldIntegerName, Schema.INT32_SCHEMA)
            .build();

    Struct kafkaConnectInnerStruct = new Struct(kafkaConnectSchema);
    kafkaConnectInnerStruct.put(fieldStringName, stringValue);
    kafkaConnectInnerStruct.put(fieldIntegerName, integerValue);

    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectInnerStruct),
        (stringValue + DELIMITER + integerValue).getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldStringName), DELIMITER, kafkaConnectInnerStruct),
        stringValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldIntegerName), DELIMITER, kafkaConnectInnerStruct),
        integerValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testStructNestedOnce() {
    final String fieldArrayName = "MiddleArray";
    final String innerFieldStructName = "InnerStruct";
    final String innerFieldStringName = "InnerString";
    final String innerFieldIntegerName = "InnerInt";
    final String innerStringValue = "forty two";
    final Integer innerIntegerValue = 42;
    final List<Float> arrayValue = Arrays.asList(42.0f, 42.4f, 42.42f, 42.424f, 42.4242f);

    Schema kafkaConnectInnerSchema =
        SchemaBuilder.struct()
            .field(innerFieldStringName, Schema.STRING_SCHEMA)
            .field(innerFieldIntegerName, Schema.INT32_SCHEMA)
            .build();

    Struct kafkaConnectInnerStruct = new Struct(kafkaConnectInnerSchema);
    kafkaConnectInnerStruct.put(innerFieldStringName, innerStringValue);
    kafkaConnectInnerStruct.put(innerFieldIntegerName, innerIntegerValue);

    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(innerFieldStructName, kafkaConnectInnerSchema)
            .field(fieldArrayName, SchemaBuilder.array(Schema.FLOAT32_SCHEMA).build())
            .build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(innerFieldStructName, kafkaConnectInnerStruct);
    kafkaConnectStruct.put(fieldArrayName, arrayValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        (kafkaConnectInnerStruct.toString() + DELIMITER + arrayValue.toString())
            .getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectStruct),
        kafkaConnectInnerStruct.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldArrayName), DELIMITER, kafkaConnectStruct),
        arrayValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(
            List.of(innerFieldStructName + "." + innerFieldStringName),
            DELIMITER,
            kafkaConnectStruct),
        innerStringValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(
            List.of(innerFieldStructName + "." + innerFieldIntegerName),
            DELIMITER,
            kafkaConnectStruct),
        innerIntegerValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testStructNestedTwice() {
    final String middleFieldStructName = "MiddleStruct";
    final String middleFieldArrayName = "MiddleArray";
    final String innerFieldStructName = "InnerStruct";
    final String innerFieldStringName = "InnerString";
    final String innerFieldIntegerName = "InnerInt";
    final String innerStringValue = "forty two";
    final Integer innerIntegerValue = 42;
    final List<Float> middleArrayValue = Arrays.asList(42.0f, 42.4f, 42.42f, 42.424f, 42.4242f);

    Schema kafkaConnectInnerSchema =
        SchemaBuilder.struct()
            .field(innerFieldStringName, Schema.STRING_SCHEMA)
            .field(innerFieldIntegerName, Schema.INT32_SCHEMA)
            .build();

    Struct kafkaConnectInnerStruct = new Struct(kafkaConnectInnerSchema);
    kafkaConnectInnerStruct.put(innerFieldStringName, innerStringValue);
    kafkaConnectInnerStruct.put(innerFieldIntegerName, innerIntegerValue);

    Schema kafkaConnectMiddleSchema =
        SchemaBuilder.struct()
            .field(innerFieldStructName, kafkaConnectInnerSchema)
            .field(middleFieldArrayName, SchemaBuilder.array(Schema.FLOAT32_SCHEMA).build())
            .build();

    Struct kafkaConnectMiddleStruct = new Struct(kafkaConnectMiddleSchema);
    kafkaConnectMiddleStruct.put(innerFieldStructName, kafkaConnectInnerStruct);
    kafkaConnectMiddleStruct.put(middleFieldArrayName, middleArrayValue);

    Schema kafkaConnectOuterSchema =
        SchemaBuilder.struct()
            .field(innerFieldStructName, kafkaConnectInnerSchema)
            .field(middleFieldStructName, kafkaConnectMiddleSchema)
            .build();

    Struct kafkaConnectOuterStruct = new Struct(kafkaConnectOuterSchema);
    kafkaConnectOuterStruct.put(innerFieldStructName, kafkaConnectInnerStruct);
    kafkaConnectOuterStruct.put(middleFieldStructName, kafkaConnectMiddleStruct);

    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectOuterStruct),
        (kafkaConnectInnerStruct.toString() + DELIMITER + kafkaConnectMiddleStruct.toString())
            .getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectOuterStruct),
        kafkaConnectInnerStruct.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(middleFieldStructName), DELIMITER, kafkaConnectOuterStruct),
        kafkaConnectMiddleStruct.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(
            List.of(innerFieldStructName + "." + innerFieldStringName),
            DELIMITER,
            kafkaConnectOuterStruct),
        innerStringValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(
            List.of(
                middleFieldStructName + "." + innerFieldStructName + "." + innerFieldIntegerName),
            DELIMITER,
            kafkaConnectOuterStruct),
        innerIntegerValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testEmptyInnerStruct() {
    final String innerFieldStructName = "InnerStruct";
    final String innerFieldStringName = "InnerString";
    final String innerStringValue = "forty two";

    Schema kafkaConnectInnerSchema = SchemaBuilder.struct().build();

    Struct kafkaConnectInnerStruct = new Struct(kafkaConnectInnerSchema);

    Schema kafkaConnectOuterSchema =
        SchemaBuilder.struct()
            .field(innerFieldStructName, kafkaConnectInnerSchema)
            .field(innerFieldStringName, Schema.STRING_SCHEMA)
            .build();

    Struct kafkaConnectOuterStruct = new Struct(kafkaConnectOuterSchema);
    kafkaConnectOuterStruct.put(innerFieldStructName, kafkaConnectInnerStruct);
    kafkaConnectOuterStruct.put(innerFieldStringName, innerStringValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectOuterStruct),
        (kafkaConnectInnerStruct + DELIMITER + innerStringValue).getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectOuterStruct),
        kafkaConnectInnerStruct.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectOuterStruct),
        "Struct{}".getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testNull() {
    Schema structOnlyOptionalFieldsSchema =
        SchemaBuilder.struct().field("f", SchemaBuilder.bool().optional()).build();
    Struct structNoOptionalFields = new Struct(structOnlyOptionalFieldsSchema);

    // The following two invocations throw since `null` cannot be serialized in a key directly (only
    // as a member of a Struct/List/Map/...) to preserve compatibility with the Confluent's sink.
    assertThrows(DataException.class, () -> calculateKey(List.of(), DELIMITER, null));
    assertThrows(
        DataException.class, () -> calculateKey(List.of(), DELIMITER, structNoOptionalFields));
  }

  @Test
  public void testUnmappableValues() {
    Schema structNoFieldsSchema = SchemaBuilder.struct().build();
    Struct structNoFields = new Struct(structNoFieldsSchema);

    byte[] expected = new byte[0];
    assertArrayEquals(expected, calculateKey(List.of(), DELIMITER, ""));
    assertArrayEquals(expected, calculateKey(List.of(), DELIMITER, new byte[0]));
    assertArrayEquals(expected, calculateKey(List.of(), DELIMITER, structNoFields));
  }

  @Test
  public void testDifferentStructMappings() {
    final String fieldStringName = "String";
    final String fieldIntegerName = "Int";
    final String stringValue = "forty two";
    final Integer integerValue = 42;

    Schema kafkaConnectInnerSchema =
        SchemaBuilder.struct()
            .field(fieldStringName, Schema.STRING_SCHEMA)
            .field(fieldIntegerName, Schema.INT32_SCHEMA)
            .build();
    Struct struct = new Struct(kafkaConnectInnerSchema);
    struct.put(fieldStringName, stringValue);
    struct.put(fieldIntegerName, integerValue);

    // Note that it preserves field order from the Schema.
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, struct),
        (stringValue + DELIMITER + integerValue).getBytes(StandardCharsets.UTF_8));
    // Force another order.
    assertArrayEquals(
        calculateKey(List.of(fieldIntegerName, fieldStringName), DELIMITER, struct),
        (integerValue + DELIMITER + stringValue).getBytes(StandardCharsets.UTF_8));
    // Use the same field twice.
    assertArrayEquals(
        calculateKey(List.of(fieldIntegerName, fieldIntegerName), DELIMITER, struct),
        (integerValue + DELIMITER + integerValue).getBytes(StandardCharsets.UTF_8));
    // Try accessing nonexistent key.
    assertThrows(DataException.class, () -> calculateKey(List.of("invalid"), DELIMITER, struct));
  }

  @Test
  public void testMap() {
    final String fieldNameIntegerMap = "IntegerMap";
    final String fieldNameStringMap = "StringMap";
    final Map<Integer, Boolean> integerMap = new HashMap<>();
    final Map<String, Boolean> stringMap = new HashMap<>();

    for (int n = 2; n <= 10; n++) {
      boolean isPrime = true;
      for (int d : integerMap.keySet()) {
        if (n % d == 0) {
          isPrime = false;
          break;
        }
      }
      integerMap.put(n, isPrime);
    }
    for (int n = 2; n <= 10; n++) {
      boolean isPrime = true;
      for (String s : stringMap.keySet()) {
        Integer d = Integer.parseInt(s);
        if (n % d == 0) {
          isPrime = false;
          break;
        }
      }
      stringMap.put(Integer.toString(n), isPrime);
    }
    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(
                fieldNameIntegerMap, SchemaBuilder.map(Schema.INT32_SCHEMA, Schema.BOOLEAN_SCHEMA))
            .field(
                fieldNameStringMap, SchemaBuilder.map(Schema.STRING_SCHEMA, Schema.BOOLEAN_SCHEMA))
            .build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldNameIntegerMap, integerMap);
    kafkaConnectStruct.put(fieldNameStringMap, stringMap);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        (integerMap.toString() + DELIMITER + stringMap.toString())
            .getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldNameIntegerMap), DELIMITER, kafkaConnectStruct),
        integerMap.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldNameStringMap), DELIMITER, kafkaConnectStruct),
        stringMap.toString().getBytes(StandardCharsets.UTF_8));
    // Accessing map keys is not supported.
    assertThrows(
        DataException.class,
        () -> calculateKey(List.of(fieldNameIntegerMap + ".3"), DELIMITER, kafkaConnectStruct));
    assertThrows(
        DataException.class,
        () -> calculateKey(List.of(fieldNameStringMap + ".3"), DELIMITER, kafkaConnectStruct));
  }

  @Test
  public void testIntegerArray() {
    final String fieldName = "IntegerArray";
    final List<Integer> fieldValue = Arrays.asList(42, 4242, 424242, 42424242);

    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(fieldName, SchemaBuilder.array(Schema.INT32_SCHEMA).build())
            .build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldValue);
    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testStructArray() {
    final String innerFieldStringName = "InnerString";
    final String innerFieldIntegerName = "InnerInt";
    final String innerStringValue = "42";
    final Integer innerIntegerValue = 42;
    Schema kafkaConnectInnerSchema =
        SchemaBuilder.struct()
            .field(innerFieldStringName, Schema.STRING_SCHEMA)
            .field(innerFieldIntegerName, Schema.INT32_SCHEMA)
            .build();
    Struct kafkaConnectInnerStruct = new Struct(kafkaConnectInnerSchema);
    kafkaConnectInnerStruct.put(innerFieldStringName, innerStringValue);
    kafkaConnectInnerStruct.put(innerFieldIntegerName, innerIntegerValue);

    final String middleFieldArrayName = "MiddleArray";
    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(middleFieldArrayName, SchemaBuilder.array(kafkaConnectInnerSchema).build())
            .build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    List<Struct> innerStructList = List.of(kafkaConnectInnerStruct);
    kafkaConnectStruct.put(middleFieldArrayName, innerStructList);

    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        innerStructList.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(middleFieldArrayName), DELIMITER, kafkaConnectStruct),
        innerStructList.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testStringArray() {
    final String fieldName = "StringArray";
    final List<String> fieldValue =
        Arrays.asList("Forty-two", "forty-two", "Forty two", "forty two");

    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(fieldName, SchemaBuilder.array(Schema.STRING_SCHEMA).build())
            .build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldValue);

    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
        fieldValue.toString().getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
        fieldValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testBytes() {
    final String fieldName = "Bytes";
    final byte[] fieldBytes = new byte[] {42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54};
    final ByteBuffer fieldValueKafkaConnect = ByteBuffer.wrap(fieldBytes);
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.BYTES_SCHEMA).build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldValueKafkaConnect);
    assertArrayEquals(calculateKey(List.of(), DELIMITER, kafkaConnectStruct), fieldBytes);
    assertArrayEquals(calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct), fieldBytes);
  }

  @Test
  public void testBytesInStruct() throws IOException {
    final String innerFieldStructName = "InnerStruct";
    final String innerFieldBytesName = "InnerBytes";
    ByteArrayOutputStream inputBuilder = new ByteArrayOutputStream();
    for (int i = -128; i < 128; i++) {
      inputBuilder.write(i);
    }
    byte[] innerBytesValue = inputBuilder.toByteArray();

    Schema kafkaConnectInnerSchema =
        SchemaBuilder.struct().field(innerFieldBytesName, Schema.BYTES_SCHEMA).build();
    Struct kafkaConnectInnerStruct = new Struct(kafkaConnectInnerSchema);
    kafkaConnectInnerStruct.put(innerFieldBytesName, innerBytesValue);

    Schema kafkaConnectOuterSchema =
        SchemaBuilder.struct().field(innerFieldStructName, kafkaConnectInnerSchema).build();

    Struct kafkaConnectOuterStruct = new Struct(kafkaConnectOuterSchema);
    kafkaConnectOuterStruct.put(innerFieldStructName, kafkaConnectInnerStruct);

    ByteArrayOutputStream expectedBuilder = new ByteArrayOutputStream();
    expectedBuilder.write(("Struct{" + innerFieldBytesName + "=").getBytes(StandardCharsets.UTF_8));
    expectedBuilder.write(innerBytesValue);
    expectedBuilder.write("}".getBytes(StandardCharsets.UTF_8));
    byte[] expectedStructSerialization = expectedBuilder.toByteArray();

    assertArrayEquals(
        calculateKey(List.of(), DELIMITER, kafkaConnectOuterStruct), expectedStructSerialization);
    assertArrayEquals(
        calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectOuterStruct),
        expectedStructSerialization);
    assertArrayEquals(
        calculateKey(
            List.of(innerFieldStructName + "." + innerFieldBytesName),
            DELIMITER,
            kafkaConnectOuterStruct),
        innerBytesValue);
  }

  @Test
  public void testKafkaLogicalTypes() {
    final String dateFieldName = "KafkaDate";
    final String timestampFieldName = "KafkaTimestamp";
    final String timeFieldName = "KafkaTime";
    final String decimalFieldName = "KafkaDecimal";
    final Date date = new Date(1488406838808L);
    final String decimalString = "-1.23E-12";
    final BigDecimal decimal = new BigDecimal(decimalString);

    final String formattedDate =
        DateTimeFormatter.ISO_LOCAL_DATE.format(date.toInstant().atZone(ZoneOffset.UTC));
    final String formattedTimestamp =
        DateTimeFormatter.ISO_INSTANT.format(date.toInstant().atZone(ZoneOffset.UTC));
    final String formattedTime =
        DateTimeFormatter.ISO_LOCAL_TIME.format(date.toInstant().atZone(ZoneOffset.UTC));
    final String formattedDecimal = decimal.toPlainString();

    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(dateFieldName, org.apache.kafka.connect.data.Date.SCHEMA)
            .field(timestampFieldName, org.apache.kafka.connect.data.Timestamp.SCHEMA)
            .field(timeFieldName, org.apache.kafka.connect.data.Time.SCHEMA)
            .field(decimalFieldName, org.apache.kafka.connect.data.Decimal.schema(decimal.scale()))
            .build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(dateFieldName, date);
    kafkaConnectStruct.put(timestampFieldName, date);
    kafkaConnectStruct.put(timeFieldName, date);
    kafkaConnectStruct.put(decimalFieldName, decimal);
    assertArrayEquals(
        calculateKey(List.of(dateFieldName), DELIMITER, kafkaConnectStruct, kafkaConnectSchema),
        formattedDate.getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(
            List.of(timestampFieldName), DELIMITER, kafkaConnectStruct, kafkaConnectSchema),
        formattedTimestamp.getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(timeFieldName), DELIMITER, kafkaConnectStruct, kafkaConnectSchema),
        formattedTime.getBytes(StandardCharsets.UTF_8));
    assertArrayEquals(
        calculateKey(List.of(decimalFieldName), DELIMITER, kafkaConnectStruct, kafkaConnectSchema),
        formattedDecimal.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testKafkaLogicalTypeInStructField() {
    Date epochDate = new Date(0L);

    String dateFieldName = "date";
    String structFieldName = "struct";
    Schema innerStructSchema =
        SchemaBuilder.struct()
            .field(dateFieldName, org.apache.kafka.connect.data.Date.SCHEMA)
            .build();
    Schema outerStructSchema =
        SchemaBuilder.struct().field(structFieldName, innerStructSchema).build();

    Struct innerStruct = new Struct(innerStructSchema);
    innerStruct.put(dateFieldName, epochDate);

    Struct outerStruct = new Struct(outerStructSchema);
    outerStruct.put(structFieldName, innerStruct);

    byte[] keyBytes =
        calculateKey(List.of(structFieldName), DELIMITER, outerStruct, outerStructSchema);
    assertEquals("Struct{date=1970-01-01}", new String(keyBytes, StandardCharsets.UTF_8));
  }

  @Test
  public void testNullable() {
    final String nullableFieldName = "nullable";
    final String requiredFieldName = "required";
    final Integer nullableFieldValue = null;
    final Integer requiredFieldValue = 42;

    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(nullableFieldName, SchemaBuilder.int32().optional().build())
            .field(requiredFieldName, SchemaBuilder.int32().required().build())
            .build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(nullableFieldName, nullableFieldValue);
    kafkaConnectStruct.put(requiredFieldName, requiredFieldValue);
    // The following two invocations throw since `null` cannot be serialized in a key directly (only
    // as a member of a Struct/List/Map/...) to preserve compatibility with the Confluent's sink.
    assertThrows(DataException.class, () -> calculateKey(List.of(), DELIMITER, kafkaConnectStruct));
    assertThrows(
        DataException.class,
        () -> calculateKey(List.of(nullableFieldName), DELIMITER, kafkaConnectStruct));
    assertArrayEquals(
        calculateKey(List.of(requiredFieldName), DELIMITER, kafkaConnectStruct),
        requiredFieldValue.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Test
  public void testNullableStruct() {
    final String nullableFieldName = "nullableStruct";
    final String innerStructFieldName = "foobar";

    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(
                nullableFieldName,
                SchemaBuilder.struct()
                    .field(innerStructFieldName, SchemaBuilder.bool().build())
                    .optional()
                    .build())
            .build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(nullableFieldName, null);
    // The following two invocations throw since `null` cannot be serialized in a key directly (only
    // as a member of a Struct/List/Map/...) to preserve compatibility with the Confluent's sink.
    assertThrows(DataException.class, () -> calculateKey(List.of(), DELIMITER, kafkaConnectStruct));
    assertThrows(
        DataException.class,
        () -> calculateKey(List.of(nullableFieldName), DELIMITER, kafkaConnectStruct));
    // Access to a field of a nonexistent struct.
    assertThrows(
        DataException.class,
        () ->
            calculateKey(
                List.of(nullableFieldName + "." + innerStructFieldName),
                DELIMITER,
                kafkaConnectStruct));
  }

  @Test
  public void testSchemalessRecordSuccesses() {
    JsonConverter jsonConverter = JsonConverterFactory.create(false, true);

    String topic = "topic";
    String delimiter = "##";

    for (Object[] testCase :
        List.of(
            // Default key definition and all kinds of types.
            // Note that logical types cannot be used without schema.
            new Object[] {List.of(), "2.130", "2.13"},
            new Object[] {List.of(), "7", "7"},
            new Object[] {List.of(), "\"x\"", "x"},
            new Object[] {List.of(), "true", "true"},
            new Object[] {List.of(), "[]", "[]"},
            new Object[] {List.of(), "[1,\"s\",true]", "[1, s, true]"},
            // Default key definition when using on a map (schemaless data is converted into a Map
            // rather than a Struct, so its fields are not accessed when serializing!).
            new Object[] {List.of(), "{\"a\":1,\"b\":true,\"c\":\"str\"}", "{a=1, b=true, c=str}"},
            new Object[] {List.of(), "{\"b\":1,\"a\":3}", "{a=3, b=1}"},
            new Object[] {
              List.of(),
              "{\"b\":[1,2],\"a\":3,\"c\":{\"x\":\"D\",\"y\":2137}}",
              "{a=3, b=[1, 2], c={x=D, y=2137}}"
            }
            // Note that there are no tests for accessing fields of the values. Maps, to which
            // schemaless JSON data deserializes when using JsonConverter, does not support
            // accessing its values by keys.
            )) {
      KeyMapper mapper = new KeyMapper(delimiter, (List<String>) testCase[0]);
      SchemaAndValue connectData =
          jsonConverter.toConnectData(
              topic, ((String) testCase[1]).getBytes(StandardCharsets.UTF_8));
      byte[] expectedResult = ((String) testCase[2]).getBytes(StandardCharsets.UTF_8);
      byte[] result = mapper.getKey(new SchemaAndValue(null, connectData.value()));
      List<String> expectedAndReal =
          List.of((String) testCase[2], new String(result, StandardCharsets.UTF_8));
      assertArrayEquals(expectedResult, result);
    }
  }

  @Test
  public void testAccessingSchemalessPrimitiveField() {
    KeyMapper mapper = new KeyMapper("#", List.of("fieldName"));
    assertThrows(
        DataException.class, () -> mapper.getKey(new SchemaAndValue(null, "primitiveString")));
  }

  private static byte[] calculateKey(
      List<String> mapperDefinition, String mapperDelimiter, Object kafkaKey) {
    // We use `null` in this test since our code for now uses  Schema only to warn the user when an
    // unsupported logical type is encountered.
    return calculateKey(mapperDefinition, mapperDelimiter, kafkaKey, null);
  }

  private static byte[] calculateKey(
      List<String> mapperDefinition,
      String mapperDelimiter,
      Object kafkaKey,
      Schema kafkaKeySchema) {
    KeyMapper mapper = new KeyMapper(mapperDelimiter, mapperDefinition);
    return mapper.getKey(new SchemaAndValue(kafkaKeySchema, kafkaKey));
  }
}
