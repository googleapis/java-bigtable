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

import static com.google.cloud.kafka.connect.bigtable.util.MockUtil.assertTotalNumberOfInvocations;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.cloud.bigtable.data.v2.models.Range;
import com.google.cloud.kafka.connect.bigtable.config.NullValueMode;
import com.google.cloud.kafka.connect.bigtable.util.JsonConverterFactory;
import com.google.protobuf.ByteString;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.json.JsonConverter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ValueMapperTest {
  private static final String DEFAULT_COLUMN_FAMILY = "COLUMN_FAMILY";
  private static final String DEFAULT_COLUMN = "COLUMN_QUALIFIER";
  private static final ByteString DEFAULT_COLUMN_BYTES =
      ByteString.copyFrom(DEFAULT_COLUMN.getBytes(StandardCharsets.UTF_8));
  private static final ByteString ROW_KEY =
      ByteString.copyFrom("ROW_KEY".getBytes(StandardCharsets.UTF_8));
  private static final String TARGET_TABLE_NAME = "table";
  private static final Long TIMESTAMP = 2024L;
  private static final Range.TimestampRange TIMESTAMP_RANGE =
      Range.TimestampRange.create(0, TIMESTAMP);
  private static final String DEFAULT_TOPIC = "topic";

  private static final String NESTED_NULL_STRUCT_FIELD_NAME = "struct";
  private static final ByteString NESTED_NULL_STRUCT_FIELD_NAME_BYTES =
      ByteString.copyFrom(NESTED_NULL_STRUCT_FIELD_NAME.getBytes(StandardCharsets.UTF_8));

  private static final JsonConverter jsonConverter = JsonConverterFactory.create(false, false);

  @Test
  public void testBoolean() {
    Boolean value = true;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testString() {
    String value = "rrrrrrr";
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testLong() {
    Long value = 9223372036854775807L;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testInteger() {
    Integer value = -2147483648;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testShort() {
    Short value = 32767;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testByte() {
    Byte value = -128;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testBytes() {
    byte[] value = new byte[] {(byte) 37, (byte) 21};
    ByteString expected = ByteString.copyFrom(value);
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testFloat() {
    Float value = 128.37157f;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testDouble() {
    Double value = 128.37157;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testDoubleSpecial() {
    Double value = Double.NaN;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Ignore // TODO: fix it.
  @Test
  public void testDate() {
    // TODO: is it correct? Or maybe should the implementation first convert it into logical value?
    Long value = 1732822801000L;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(new Date(value), TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Ignore // TODO: fix it.
  @Test
  public void testDecimal() {
    // TODO: is it correct? Or maybe should the implementation first convert it into logical value?
    BigDecimal value = new BigDecimal("0.30000000000000000004");
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testArray() {
    List<Object> value = List.of("1", 2, true);
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            DEFAULT_COLUMN_BYTES,
            TIMESTAMP,
            ByteString.copyFrom("[\"1\",2,true]".getBytes(StandardCharsets.UTF_8)));
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testRootValueNeedsBothDefaultColumns() {
    Integer value = 123;
    for (ValueMapper mapper :
        List.of(
            new TestValueMapper(null, null, NullValueMode.WRITE),
            new TestValueMapper(DEFAULT_COLUMN_FAMILY, null, NullValueMode.WRITE),
            new TestValueMapper(null, DEFAULT_COLUMN, NullValueMode.WRITE))) {
      MutationDataBuilder mutationDataBuilder =
          mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
      verify(mutationDataBuilder, times(0))
          .setCell(
              DEFAULT_COLUMN_FAMILY,
              DEFAULT_COLUMN_BYTES,
              TIMESTAMP,
              ByteString.copyFrom(Bytes.toBytes(value)));
    }
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            DEFAULT_COLUMN_BYTES,
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(value)));
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testValueNestedOnceNeedsOnlyDefaultColumnFamily() {
    Object value = fromJson("{\"key\": 2}");
    ValueMapper mapper = new TestValueMapper(DEFAULT_COLUMN_FAMILY, null, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            ByteString.copyFrom("key".getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(2L)));

    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testMultipleOperationsAtOnce() {
    Object value = fromJson("{\"a\":{\"b\":789},\"c\":true,\"x\":{\"y\":null},\"z\":null}");
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            "a",
            ByteString.copyFrom("b".getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(789L)));
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            ByteString.copyFrom("c".getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(true)));
    verify(mutationDataBuilder, times(1))
        .deleteCells(
            "x",
            ByteString.copyFrom("y".getBytes(StandardCharsets.UTF_8)),
            Range.TimestampRange.create(0, TIMESTAMP));
    verify(mutationDataBuilder, times(1)).deleteFamily("z");
    assertTotalNumberOfInvocations(mutationDataBuilder, 4);
  }

  @Test
  public void testMap() {
    Object outerMapKey = 123456;
    Object innerMapKey = "innerMapKey";
    String familyToBeDeleted = "familyToBeDeleted";
    String columnToBeDeleted = "columnToBeDeleted";
    ByteString columnToBeDeletedBytes =
        ByteString.copyFrom(columnToBeDeleted.getBytes(StandardCharsets.UTF_8));
    Object innermostNullKey = "innermostNullKey";

    Object value = "value";
    ByteString valueBytes = ByteString.copyFrom(((String) value).getBytes(StandardCharsets.UTF_8));
    Object valueKey = "valueKey";
    ByteString valueKeyBytes =
        ByteString.copyFrom(((String) valueKey).getBytes(StandardCharsets.UTF_8));

    Map<Object, Object> innermostMap = new HashMap<>();
    Map<Object, Object> innerMap = new HashMap<>();
    Map<Object, Object> outerMap = new HashMap<>();

    outerMap.put(outerMapKey, innerMap);
    innerMap.put(innerMapKey, innermostMap);

    outerMap.put(valueKey, value);
    innerMap.put(valueKey, value);
    innermostMap.put(valueKey, value);

    outerMap.put(familyToBeDeleted, null);
    innerMap.put(columnToBeDeleted, null);
    innermostMap.put(innermostNullKey, null);

    /*
    {
        outerMapKey: {
            innerMapKey: {
                valueKey: value,
                innermostNullKey: null,
            }
            valueKey: value,
            columnToBeDeleted: null,
        }
        valueKey: value,
        familyToBeDeleted: null,
    }
     */
    String expectedJsonification = "{\"innermostNullKey\":null,\"valueKey\":\"value\"}";
    ByteString expectedJsonificationBytes =
        ByteString.copyFrom(expectedJsonification.getBytes(StandardCharsets.UTF_8));

    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(outerMap, TIMESTAMP);
    verify(mutationDataBuilder, times(1)).deleteFamily(familyToBeDeleted);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, valueKeyBytes, TIMESTAMP, valueBytes);
    verify(mutationDataBuilder, times(1))
        .deleteCells(
            outerMapKey.toString(),
            columnToBeDeletedBytes,
            Range.TimestampRange.create(0, TIMESTAMP));
    verify(mutationDataBuilder, times(1))
        .setCell(outerMapKey.toString(), valueKeyBytes, TIMESTAMP, valueBytes);
    verify(mutationDataBuilder, times(1))
        .setCell(
            outerMapKey.toString(),
            ByteString.copyFrom(innerMapKey.toString().getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            expectedJsonificationBytes);
    assertTotalNumberOfInvocations(mutationDataBuilder, 5);
  }

  @Test
  public void testJsonificationOfNonJsonNativeTypes() {
    final String dateFieldName = "KafkaDate";
    final String timestampFieldName = "KafkaTimestamp";
    final String timeFieldName = "KafkaTime";
    final String decimalFieldName = "KafkaDecimal";
    final String bytesFieldName = "KafkaBytes";
    final Long dateLong = 1488406838808L;
    final Date date = new Date(dateLong);
    final String decimalString = "0.30000000000000004";
    final Integer decimalScale = 0;
    final BigDecimal decimal = new BigDecimal(decimalString);
    final byte[] bytes = "bytes\0".getBytes(StandardCharsets.UTF_8);
    final String schemaStructFieldName = "schema";
    final ByteString schemaStructFieldNameBytes =
        ByteString.copyFrom(schemaStructFieldName.getBytes(StandardCharsets.UTF_8));
    final String schemalessMapFieldName = "schemaless";
    final ByteString schemalessMapFieldNameBytes =
        ByteString.copyFrom(schemalessMapFieldName.getBytes(StandardCharsets.UTF_8));

    Schema structSchema =
        SchemaBuilder.struct()
            .field(dateFieldName, org.apache.kafka.connect.data.Date.SCHEMA)
            .field(timestampFieldName, org.apache.kafka.connect.data.Timestamp.SCHEMA)
            .field(timeFieldName, org.apache.kafka.connect.data.Timestamp.SCHEMA)
            .field(decimalFieldName, org.apache.kafka.connect.data.Decimal.schema(decimalScale))
            .field(bytesFieldName, Schema.BYTES_SCHEMA)
            .build();
    Struct struct = new Struct(structSchema);
    Map<Object, Object> map = new TreeMap<>(); // Note we need this map to be ordered!

    Map<Object, Object> outerMap = new HashMap<>();
    Map<Object, Object> innerMap = new HashMap<>();

    outerMap.put(DEFAULT_COLUMN_FAMILY, innerMap);
    innerMap.put(schemaStructFieldName, struct);
    innerMap.put(schemalessMapFieldName, map);
    struct.put(dateFieldName, date);
    map.put(dateFieldName, date);
    struct.put(timestampFieldName, date);
    map.put(timestampFieldName, date);
    struct.put(timeFieldName, date);
    map.put(timeFieldName, date);
    struct.put(decimalFieldName, decimal);
    map.put(decimalFieldName, decimal);
    struct.put(bytesFieldName, bytes);
    map.put(bytesFieldName, bytes);

    String expectedStringificationWithoutSchema =
        "{\"KafkaBytes\":\"Ynl0ZXMA\",\"KafkaDate\":1488406838808,\"KafkaDecimal\":0.30000000000000004,\"KafkaTime\":1488406838808,\"KafkaTimestamp\":1488406838808}";
    ByteString expectedStringificationWithoutSchemaBytes =
        ByteString.copyFrom(expectedStringificationWithoutSchema.getBytes(StandardCharsets.UTF_8));
    // TODO: shouldn't it be different than schemaless serialization? (e.g., count 'time' modulo
    // 24h)
    String expectedStringificationWithSchema =
        "{\"KafkaDate\":1488406838808,\"KafkaTimestamp\":1488406838808,\"KafkaTime\":1488406838808,\"KafkaDecimal\":0.30000000000000004,\"KafkaBytes\":\"Ynl0ZXMA\"}";
    ByteString expectedStringificationWithSchemaBytes =
        ByteString.copyFrom(expectedStringificationWithSchema.getBytes(StandardCharsets.UTF_8));

    ValueMapper mapper = new TestValueMapper(null, null, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(outerMap, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            schemalessMapFieldNameBytes,
            TIMESTAMP,
            expectedStringificationWithoutSchemaBytes);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            schemaStructFieldNameBytes,
            TIMESTAMP,
            expectedStringificationWithSchemaBytes);
    assertTotalNumberOfInvocations(mutationDataBuilder, 2);
  }

  @Test
  public void testStruct() {
    final String structFieldName = "struct";
    final ByteString structFieldNameBytes =
        ByteString.copyFrom(structFieldName.getBytes(StandardCharsets.UTF_8));
    final String valueFieldName = "value";
    final ByteString valueFieldNameBytes =
        ByteString.copyFrom(valueFieldName.getBytes(StandardCharsets.UTF_8));
    final String optionalFieldName = "optional";
    final ByteString optionalFieldNameBytes =
        ByteString.copyFrom(optionalFieldName.getBytes(StandardCharsets.UTF_8));
    final byte[] value = "value\0".getBytes(StandardCharsets.UTF_8);

    Schema innermostStructSchema =
        SchemaBuilder.struct()
            .field(valueFieldName, Schema.BYTES_SCHEMA)
            .field(optionalFieldName, Schema.OPTIONAL_INT8_SCHEMA)
            .build();
    Schema innerStructSchema =
        SchemaBuilder.struct()
            .field(structFieldName, innermostStructSchema)
            .field(valueFieldName, Schema.BYTES_SCHEMA)
            .field(optionalFieldName, Schema.OPTIONAL_INT8_SCHEMA)
            .build();
    Schema outerStructSchema =
        SchemaBuilder.struct()
            .field(structFieldName, innerStructSchema)
            .field(valueFieldName, Schema.BYTES_SCHEMA)
            .field(optionalFieldName, Schema.OPTIONAL_INT8_SCHEMA)
            .build();

    Struct innermostStruct = new Struct(innermostStructSchema);
    innermostStruct.put(valueFieldName, value);

    String expectedInnermostStringification = "{\"value\":\"dmFsdWUA\",\"optional\":null}";
    ByteString expectedInnermostStringificationBytes =
        ByteString.copyFrom(expectedInnermostStringification.getBytes(StandardCharsets.UTF_8));

    Struct innerStruct = new Struct(innerStructSchema);
    innerStruct.put(structFieldName, innermostStruct);
    innerStruct.put(valueFieldName, value);

    Struct struct = new Struct(outerStructSchema);
    struct.put(structFieldName, innerStruct);
    struct.put(valueFieldName, value);

    /*
    {
        struct: {
            struct: {
                optionalFieldName: null,
                valueFieldName: value,
            }
            optionalFieldName: null,
            valueFieldName: value,
        }
        optionalFieldName: null,
        valueFieldName: value,
    }
    */
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(struct, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, valueFieldNameBytes, TIMESTAMP, ByteString.copyFrom(value));
    verify(mutationDataBuilder, times(1)).deleteFamily(optionalFieldName);
    verify(mutationDataBuilder, times(1))
        .setCell(structFieldName, valueFieldNameBytes, TIMESTAMP, ByteString.copyFrom(value));
    verify(mutationDataBuilder, times(1))
        .deleteCells(
            structFieldName, optionalFieldNameBytes, Range.TimestampRange.create(0, TIMESTAMP));
    verify(mutationDataBuilder, times(1))
        .setCell(
            structFieldName,
            structFieldNameBytes,
            TIMESTAMP,
            expectedInnermostStringificationBytes);
    assertTotalNumberOfInvocations(mutationDataBuilder, 5);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testEmpty() {
    Schema emptyStructSchema = SchemaBuilder.struct().build();
    Struct emptyStruct = new Struct(emptyStructSchema);
    Map<Object, Object> emptyMap = new HashMap<>();
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.WRITE);
    for (Object value : List.of(emptyMap, emptyStruct)) {
      MutationDataBuilder mutationDataBuilder =
          mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
      assertTotalNumberOfInvocations(mutationDataBuilder, 0);
      assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isEmpty());
    }
  }

  @Test
  public void testSimpleCase1() {
    Object value = fromJson("{\"foo\": {\"bar\": 1}}");
    ValueMapper mapper = new TestValueMapper(null, null, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            "foo",
            ByteString.copyFrom("bar".getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(1L)));
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testSimpleCase2() {
    Object value = fromJson("{\"foo\": {\"bar\": {\"fizz\": 1}}}");
    ValueMapper mapper = new TestValueMapper(null, null, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            "foo",
            ByteString.copyFrom("bar".getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom("{\"fizz\":1}".getBytes(StandardCharsets.UTF_8)));
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testSimpleCase3() {
    Object value = fromJson("{\"foo\": 1}");
    ValueMapper mapper = new TestValueMapper(DEFAULT_COLUMN_FAMILY, null, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(value, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            ByteString.copyFrom("foo".getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(1L)));
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testNullModeIgnoreRoot() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(null, TIMESTAMP);
    assertTotalNumberOfInvocations(mutationDataBuilder, 0);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isEmpty());
  }

  @Test
  public void testNullModeIgnoreNestedOnce() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(getStructhWithNullOnNthNestingLevel(1), TIMESTAMP);
    assertTotalNumberOfInvocations(mutationDataBuilder, 0);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isEmpty());
  }

  @Test
  public void testNullModeIgnoreNestedTwice() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(getStructhWithNullOnNthNestingLevel(2), TIMESTAMP);
    assertTotalNumberOfInvocations(mutationDataBuilder, 0);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isEmpty());
  }

  @Test
  public void testNullModeWriteRoot() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(null, TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, ByteString.empty());
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testNullModeWriteNestedOnce() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(getStructhWithNullOnNthNestingLevel(1), TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            NESTED_NULL_STRUCT_FIELD_NAME_BYTES,
            TIMESTAMP,
            ByteString.empty());
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testNullModeWriteNestedTwice() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(getStructhWithNullOnNthNestingLevel(2), TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            NESTED_NULL_STRUCT_FIELD_NAME,
            NESTED_NULL_STRUCT_FIELD_NAME_BYTES,
            TIMESTAMP,
            ByteString.empty());
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testNullModeDeleteRoot() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder = mapper.getRecordMutationDataBuilder(null, TIMESTAMP);
    verify(mutationDataBuilder, times(1)).deleteRow();
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testNullModeDeleteNestedOnce() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(getStructhWithNullOnNthNestingLevel(1), TIMESTAMP);
    verify(mutationDataBuilder, times(1)).deleteFamily(NESTED_NULL_STRUCT_FIELD_NAME);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testNullModeDeleteNestedTwice() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(getStructhWithNullOnNthNestingLevel(2), TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .deleteCells(
            NESTED_NULL_STRUCT_FIELD_NAME, NESTED_NULL_STRUCT_FIELD_NAME_BYTES, TIMESTAMP_RANGE);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testNullModeNestedThrice() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    String expectedJsonification = "{\"struct\":null}";
    ByteString expectedJsonificationBytes =
        ByteString.copyFrom(expectedJsonification.getBytes(StandardCharsets.UTF_8));
    MutationDataBuilder mutationDataBuilder =
        mapper.getRecordMutationDataBuilder(getStructhWithNullOnNthNestingLevel(3), TIMESTAMP);
    verify(mutationDataBuilder, times(1))
        .setCell(
            NESTED_NULL_STRUCT_FIELD_NAME,
            NESTED_NULL_STRUCT_FIELD_NAME_BYTES,
            TIMESTAMP,
            expectedJsonificationBytes);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  private static Struct getStructhWithNullOnNthNestingLevel(int n) {
    assert n > 0;

    Schema schema =
        SchemaBuilder.struct()
            .field(NESTED_NULL_STRUCT_FIELD_NAME, SchemaBuilder.struct().optional())
            .build();
    // We consider a Struct with a null child to be a level 1 nested struct.
    Struct struct = new Struct(schema);

    while (n > 1) {
      n -= 1;
      schema =
          SchemaBuilder.struct().field(NESTED_NULL_STRUCT_FIELD_NAME, schema).optional().build();
      final Struct outerStruct = new Struct(schema);
      outerStruct.put(NESTED_NULL_STRUCT_FIELD_NAME, struct);
      struct = outerStruct;
    }
    return struct;
  }

  private static Object fromJson(String s) {
    return jsonConverter.toConnectData(DEFAULT_TOPIC, s.getBytes(StandardCharsets.UTF_8)).value();
  }

  private static class TestValueMapper extends ValueMapper {
    public TestValueMapper(
        String defaultColumnFamily, String defaultColumnQualifier, NullValueMode nullMode) {
      super(defaultColumnFamily, defaultColumnQualifier, nullMode);
    }

    @Override
    protected MutationDataBuilder createMutationDataBuilder() {
      return spy(super.createMutationDataBuilder());
    }
  }
}
