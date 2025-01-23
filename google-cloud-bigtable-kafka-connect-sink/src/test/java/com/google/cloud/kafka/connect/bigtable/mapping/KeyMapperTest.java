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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.cloud.kafka.connect.bigtable.util.JsonConverterFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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
  private static String DELIMITER = "##";

  @Test
  public void testBoolean() {
    final String fieldName = "Boolean";
    final Boolean fieldValue = true;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.BOOLEAN_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldValue);

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, fieldValue),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  public void testByte() {
    final String fieldName = "Byte";
    final Byte fieldByteValue = (byte) 42;
    Schema kafkaConnectSchema = SchemaBuilder.struct().field(fieldName, Schema.INT8_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldByteValue);

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, fieldByteValue),
            fieldByteValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldByteValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldByteValue.toString().getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  public void testShort() {
    final String fieldName = "Short";
    final Short fieldShortValue = (short) 4242;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.INT16_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldShortValue);

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, fieldShortValue),
            fieldShortValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldShortValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldShortValue.toString().getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  public void testInteger() {
    String fieldName = "Integer";
    final Integer fieldIntegerValue = 424242;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.INT32_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldIntegerValue);

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, fieldIntegerValue),
            fieldIntegerValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldIntegerValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldIntegerValue.toString().getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  public void testLong() {
    String fieldName = "Long";
    final Long fieldLongValue = 424242424242L;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.INT64_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldLongValue);

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, fieldLongValue),
            fieldLongValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldLongValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldLongValue.toString().getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  public void testFloat() {
    final String fieldName = "Float";
    final Float fieldFloatValue = 4242424242.4242F;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.FLOAT32_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldFloatValue);

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, fieldFloatValue),
            fieldFloatValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldFloatValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldFloatValue.toString().getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  public void testDouble() {
    final String fieldName = "Double";
    final Double fieldDoubleValue = 4242424242.4242;
    Schema kafkaConnectSchema =
        SchemaBuilder.struct().field(fieldName, Schema.FLOAT64_SCHEMA).build();
    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(fieldName, fieldDoubleValue);

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, fieldDoubleValue),
            fieldDoubleValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldDoubleValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldDoubleValue.toString().getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  public void testDoubleSpecial() {
    final String fieldName = "Double";

    List<Double> testValues =
        Arrays.asList(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN);
    List<Double> expectedValues =
        Arrays.asList(Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_VALUE);
    assertEquals(testValues.size(), expectedValues.size());

    for (int test = 0; test < testValues.size(); ++test) {
      Schema kafkaConnectSchema =
          SchemaBuilder.struct().field(fieldName, Schema.FLOAT64_SCHEMA).build();
      Double testValue = testValues.get(test);

      Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
      kafkaConnectStruct.put(fieldName, testValue);
      assertTrue(
          Arrays.equals(
              calculateKey(List.of(), DELIMITER, testValue),
              testValue.toString().getBytes(StandardCharsets.UTF_8)));
      assertTrue(
          Arrays.equals(
              calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
              testValue.toString().getBytes(StandardCharsets.UTF_8)));
      assertTrue(
          Arrays.equals(
              calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
              testValue.toString().getBytes(StandardCharsets.UTF_8)));
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
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, fieldValue),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
  }

  @Test
  public void testStruct() {
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

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectInnerStruct),
            (innerStringValue + DELIMITER + innerIntegerValue).getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(innerFieldStringName), DELIMITER, kafkaConnectInnerStruct),
            innerStringValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(innerFieldIntegerName), DELIMITER, kafkaConnectInnerStruct),
            innerIntegerValue.toString().getBytes(StandardCharsets.UTF_8)));

    Schema kafkaConnectMiddleSchema =
        SchemaBuilder.struct()
            .field(innerFieldStructName, kafkaConnectInnerSchema)
            .field(middleFieldArrayName, SchemaBuilder.array(Schema.FLOAT32_SCHEMA).build())
            .build();

    Struct kafkaConnectMiddleStruct = new Struct(kafkaConnectMiddleSchema);
    kafkaConnectMiddleStruct.put(innerFieldStructName, kafkaConnectInnerStruct);
    kafkaConnectMiddleStruct.put(middleFieldArrayName, middleArrayValue);
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectMiddleStruct),
            (kafkaConnectInnerStruct.toString() + DELIMITER + middleArrayValue.toString())
                .getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectMiddleStruct),
            kafkaConnectInnerStruct.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(middleFieldArrayName), DELIMITER, kafkaConnectMiddleStruct),
            middleArrayValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(
                List.of(innerFieldStructName + "." + innerFieldStringName),
                DELIMITER,
                kafkaConnectMiddleStruct),
            innerStringValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(
                List.of(innerFieldStructName + "." + innerFieldIntegerName),
                DELIMITER,
                kafkaConnectMiddleStruct),
            innerIntegerValue.toString().getBytes(StandardCharsets.UTF_8)));

    Schema kafkaConnectOuterSchema =
        SchemaBuilder.struct()
            .field(innerFieldStructName, kafkaConnectInnerSchema)
            .field(middleFieldStructName, kafkaConnectMiddleSchema)
            .build();

    Struct kafkaConnectOuterStruct = new Struct(kafkaConnectOuterSchema);
    kafkaConnectOuterStruct.put(innerFieldStructName, kafkaConnectInnerStruct);
    kafkaConnectOuterStruct.put(middleFieldStructName, kafkaConnectMiddleStruct);

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectOuterStruct),
            (kafkaConnectInnerStruct.toString() + DELIMITER + kafkaConnectMiddleStruct.toString())
                .getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectOuterStruct),
            kafkaConnectInnerStruct.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(middleFieldStructName), DELIMITER, kafkaConnectOuterStruct),
            kafkaConnectMiddleStruct.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(
                List.of(innerFieldStructName + "." + innerFieldStringName),
                DELIMITER,
                kafkaConnectOuterStruct),
            innerStringValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(
                List.of(
                    middleFieldStructName
                        + "."
                        + innerFieldStructName
                        + "."
                        + innerFieldIntegerName),
                DELIMITER,
                kafkaConnectOuterStruct),
            innerIntegerValue.toString().getBytes(StandardCharsets.UTF_8)));
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
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectOuterStruct),
            (kafkaConnectInnerStruct + DELIMITER + innerStringValue)
                .getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectOuterStruct),
            kafkaConnectInnerStruct.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectOuterStruct),
            "Struct{}".getBytes(StandardCharsets.UTF_8)));
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
    assertArrayEquals(expected, calculateKey(List.of(), DELIMITER, new HashMap<>()));
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
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, struct),
            (stringValue + DELIMITER + integerValue).getBytes(StandardCharsets.UTF_8)));
    // Force another order.
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldIntegerName, fieldStringName), DELIMITER, struct),
            (integerValue + DELIMITER + stringValue).getBytes(StandardCharsets.UTF_8)));
    // Use the same field twice.
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldIntegerName, fieldIntegerName), DELIMITER, struct),
            (integerValue + DELIMITER + integerValue).getBytes(StandardCharsets.UTF_8)));
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
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            (integerMap.toString() + DELIMITER + stringMap.toString())
                .getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldNameIntegerMap), DELIMITER, kafkaConnectStruct),
            integerMap.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldNameStringMap), DELIMITER, kafkaConnectStruct),
            stringMap.toString().getBytes(StandardCharsets.UTF_8)));
    // The key is Integer, not String - we don't support it
    assertThrows(
        DataException.class,
        () -> calculateKey(List.of(fieldNameIntegerMap + ".3"), DELIMITER, kafkaConnectStruct));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldNameStringMap + ".3"), DELIMITER, kafkaConnectStruct),
            "true".getBytes(StandardCharsets.UTF_8)));
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
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
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

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            innerStructList.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(middleFieldArrayName), DELIMITER, kafkaConnectStruct),
            innerStructList.toString().getBytes(StandardCharsets.UTF_8)));
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

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectStruct),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct),
            fieldValue.toString().getBytes(StandardCharsets.UTF_8)));
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
    assertTrue(Arrays.equals(calculateKey(List.of(), DELIMITER, kafkaConnectStruct), fieldBytes));
    assertTrue(
        Arrays.equals(calculateKey(List.of(fieldName), DELIMITER, kafkaConnectStruct), fieldBytes));
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

    assertTrue(
        Arrays.equals(
            calculateKey(List.of(), DELIMITER, kafkaConnectOuterStruct),
            expectedStructSerialization));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(innerFieldStructName), DELIMITER, kafkaConnectOuterStruct),
            expectedStructSerialization));
    assertTrue(
        Arrays.equals(
            calculateKey(
                List.of(innerFieldStructName + "." + innerFieldBytesName),
                DELIMITER,
                kafkaConnectOuterStruct),
            innerBytesValue));
  }

  @Test
  public void testKafkaLogicalTypes() {
    final String dateFieldName = "KafkaDate";
    final String timestampFieldName = "KafkaTimestamp";
    final String timeFieldName = "KafkaTime";
    final String decimalFieldName = "KafkaDecimal";
    final Long dateLong = 1488406838808L;
    final Date date = new Date(dateLong);
    final String decimalString = "0.30000000000000004";
    final Integer decimalScale = 0;
    final BigDecimal decimal = new BigDecimal(decimalString);

    Schema kafkaConnectSchema =
        SchemaBuilder.struct()
            .field(dateFieldName, org.apache.kafka.connect.data.Date.SCHEMA)
            .field(timestampFieldName, org.apache.kafka.connect.data.Timestamp.SCHEMA)
            .field(timeFieldName, org.apache.kafka.connect.data.Timestamp.SCHEMA)
            .field(decimalFieldName, org.apache.kafka.connect.data.Decimal.schema(decimalScale))
            .build();

    Struct kafkaConnectStruct = new Struct(kafkaConnectSchema);
    kafkaConnectStruct.put(dateFieldName, date);
    kafkaConnectStruct.put(timestampFieldName, date);
    kafkaConnectStruct.put(timeFieldName, date);
    kafkaConnectStruct.put(decimalFieldName, decimal);
    // TODO: test in practice whether the Confluent sink works exactly like this.
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(dateFieldName), DELIMITER, kafkaConnectStruct),
            date.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(timestampFieldName), DELIMITER, kafkaConnectStruct),
            date.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(timeFieldName), DELIMITER, kafkaConnectStruct),
            date.toString().getBytes(StandardCharsets.UTF_8)));
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(decimalFieldName), DELIMITER, kafkaConnectStruct),
            decimalString.getBytes(StandardCharsets.UTF_8)));
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
    assertTrue(
        Arrays.equals(
            calculateKey(List.of(requiredFieldName), DELIMITER, kafkaConnectStruct),
            requiredFieldValue.toString().getBytes(StandardCharsets.UTF_8)));
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
            // I know of no way to pass unserialized bytes or logical types here. I think it's only
            // possible using some kind of schema.
            new Object[] {List.of(), "2.130", "2.13"},
            new Object[] {List.of(), "7", "7"},
            new Object[] {List.of(), "\"x\"", "x"},
            new Object[] {List.of(), "true", "true"},
            new Object[] {List.of(), "[]", "[]"},
            new Object[] {List.of(), "[1,\"s\",true]", "[1, s, true]"},
            // Default key definition when using on a map (schemaless data is converted into Map not
            // Struct!).
            new Object[] {List.of(), "{\"a\":1,\"b\":true,\"c\":\"str\"}", "1##true##str"},
            new Object[] {
              List.of(), "{\"b\":1,\"a\":3}", "3##1"
            }, // Note it doesn't keep key ordering.
            new Object[] {
              List.of(),
              "{\"b\":[1,2],\"a\":3,\"c\":{\"x\":\"D\",\"y\":2137}}",
              "3##[1, 2]##{x=D, y=2137}"
            },
            // Key extraction and serialization of nested beings.
            new Object[] {List.of("f"), "{\"f\":{}}", "{}"},
            new Object[] {List.of("f"), "{\"f\":1}", "1"},
            new Object[] {List.of("f"), "{\"f\":true}", "true"},
            new Object[] {List.of("f"), "{\"f\":\"s\"}", "s"},
            new Object[] {List.of("f"), "{\"f\":[]}", "[]"},
            new Object[] {List.of("f"), "{\"f\":[1,\"a\"]}", "[1, a]"},
            new Object[] {List.of("f"), "{\"f\":{\"b\":1,\"a\":3}}", "{a=3, b=1}"},
            new Object[] {List.of("f"), "{\"f\":{\"a\":{\"b\": true}}}", "{a={b=true}}"},
            new Object[] {
              List.of("f"), "{\"f\":{\"a\":{\"b\": true,\"c\":2}}}", "{a={b=true, c=2}}"
            },
            new Object[] {List.of("f.a"), "{\"f\":{\"b\":1,\"a\":3}}", "3"})) {
      KeyMapper mapper = new KeyMapper(delimiter, (List<String>) testCase[0]);
      SchemaAndValue connectData =
          jsonConverter.toConnectData(
              topic, ((String) testCase[1]).getBytes(StandardCharsets.UTF_8));
      byte[] expectedResult = ((String) testCase[2]).getBytes(StandardCharsets.UTF_8);
      byte[] result = mapper.getKey(connectData.value());
      assertTrue(Arrays.equals(expectedResult, result));
    }
    ;
  }

  @Test
  public void testAccessingSchemalessPrimitiveField() {
    KeyMapper mapper = new KeyMapper("#", List.of("fieldName"));
    assertThrows(DataException.class, () -> mapper.getKey("primitiveString"));
  }

  private static byte[] calculateKey(
      List<String> mapperDefinition, String mapperDelimiter, Object kafkaKey) {
    KeyMapper mapper = new KeyMapper(mapperDelimiter, mapperDefinition);
    return mapper.getKey(kafkaKey);
  }
}
