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
import static com.google.cloud.kafka.connect.bigtable.util.NestedNullStructFactory.NESTED_NULL_STRUCT_FIELD_NAME;
import static com.google.cloud.kafka.connect.bigtable.util.NestedNullStructFactory.NESTED_NULL_STRUCT_FIELD_NAME_BYTES;
import static com.google.cloud.kafka.connect.bigtable.util.NestedNullStructFactory.getStructhWithNullOnNthNestingLevel;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.cloud.bigtable.data.v2.models.Range;
import com.google.cloud.kafka.connect.bigtable.config.ConfigInterpolation;
import com.google.cloud.kafka.connect.bigtable.config.NullValueMode;
import com.google.protobuf.ByteString;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
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

  @Test
  public void testBoolean() {
    Boolean value = true;
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testDate() {
    Date date = new Date(1732822801000L);
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(date.getTime()));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, date);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testDecimal() {
    BigDecimal value = new BigDecimal("0.300000000000000000000000000000001");
    ByteString expected = ByteString.copyFrom(Bytes.toBytes(value));
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expected);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testArray() {
    List<Object> value = List.of("1", 2, true);
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
      MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
      verify(mutationDataBuilder, times(0))
          .setCell(
              DEFAULT_COLUMN_FAMILY,
              DEFAULT_COLUMN_BYTES,
              TIMESTAMP,
              ByteString.copyFrom(Bytes.toBytes(value)));
    }
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value);
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
    String key = "key";
    Long value = 2L;
    Struct struct = new Struct(SchemaBuilder.struct().field(key, Schema.INT64_SCHEMA));
    struct.put(key, value);

    ValueMapper mapper = new TestValueMapper(DEFAULT_COLUMN_FAMILY, null, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, struct);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            ByteString.copyFrom(key.getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(value)));

    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testDefaultColumnFamilyInterpolation() {
    for (Map.Entry<String, String> test :
        List.of(
            new AbstractMap.SimpleImmutableEntry<>("prefix_${topic}_suffix", "prefix_topic_suffix"),
            new AbstractMap.SimpleImmutableEntry<>(
                "prefix_${topic_suffix", "prefix_${topic_suffix"),
            new AbstractMap.SimpleImmutableEntry<>("prefix_$topic_suffix", "prefix_$topic_suffix"),
            new AbstractMap.SimpleImmutableEntry<>("prefix_${bad}_suffix", "prefix_${bad}_suffix"),
            new AbstractMap.SimpleImmutableEntry<>("noSubstitution", "noSubstitution"))) {
      String topic = "topic";
      String value = "value";
      ValueMapper mapper = new TestValueMapper(test.getKey(), DEFAULT_COLUMN, NullValueMode.WRITE);
      MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, value, topic);
      verify(mutationDataBuilder, times(1))
          .setCell(
              test.getValue(),
              DEFAULT_COLUMN_BYTES,
              TIMESTAMP,
              ByteString.copyFrom(value.getBytes(StandardCharsets.UTF_8)));
      assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    }
  }

  @Test
  public void testMultipleOperationsAtOnce() {
    String setColumnFamily = "setColumnFamily";
    String setColumn = "setColumn";
    String setRoot = "setRoot";
    String deleteColumnFamily = "deleteColumnFamily";
    String deleteColumn = "deleteColumn";
    String deleteRoot = "deleteRoot";

    Integer value = 789;
    Boolean rootValue = true;

    Struct createStruct =
        new Struct(SchemaBuilder.struct().field(setColumn, Schema.INT32_SCHEMA))
            .put(setColumn, value);
    Struct deleteStruct =
        new Struct(SchemaBuilder.struct().field(deleteColumn, Schema.OPTIONAL_INT8_SCHEMA))
            .put(deleteColumn, null);
    Struct struct =
        new Struct(
                SchemaBuilder.struct()
                    .field(setColumnFamily, createStruct.schema())
                    .field(setRoot, Schema.BOOLEAN_SCHEMA)
                    .field(deleteColumnFamily, deleteStruct.schema())
                    .field(deleteRoot, Schema.OPTIONAL_INT8_SCHEMA))
            .put(setColumnFamily, createStruct)
            .put(setRoot, rootValue)
            .put(deleteColumnFamily, deleteStruct)
            .put(deleteRoot, null);

    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, struct);
    verify(mutationDataBuilder, times(1))
        .setCell(
            setColumnFamily,
            ByteString.copyFrom(setColumn.getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(value)));
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            ByteString.copyFrom(setRoot.getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(rootValue)));
    verify(mutationDataBuilder, times(1))
        .deleteCells(
            deleteColumnFamily,
            ByteString.copyFrom(deleteColumn.getBytes(StandardCharsets.UTF_8)),
            Range.TimestampRange.create(0, TIMESTAMP));
    verify(mutationDataBuilder, times(1)).deleteFamily(deleteRoot);
    assertTotalNumberOfInvocations(mutationDataBuilder, 4);
  }

  @Test
  public void testMap() {
    Object outerMapKey = 123456;
    Object innerMapKey = "innerMapKey";
    String familyToBeDeleted = "familyToBeDeleted";
    String columnToBeDeleted = "columnToBeDeleted";
    Object innermostNullKey = "innermostNullKey";

    Object value = "value";
    Object valueKey = "valueKey";

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
    String expectedJsonification =
        "{\"123456\":{\"columnToBeDeleted\":null,\"innerMapKey\":{\"innermostNullKey\":null,\"valueKey\":\"value\"},\"valueKey\":\"value\"},\"familyToBeDeleted\":null,\"valueKey\":\"value\"}";
    ByteString expectedJsonificationBytes =
        ByteString.copyFrom(expectedJsonification.getBytes(StandardCharsets.UTF_8));

    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, outerMap);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN_BYTES, TIMESTAMP, expectedJsonificationBytes);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
  }

  @Test
  public void testJsonificationOfNonJsonNativeTypes() {
    final String dateFieldName = "KafkaDate";
    final String timestampFieldName = "KafkaTimestamp";
    final String timeFieldName = "KafkaTime";
    final String decimalFieldName = "KafkaDecimal";
    final String bytesFieldName = "KafkaBytes";
    final Date timestamp = new Date(1488406838808L);
    final Date time = Date.from(Instant.EPOCH.plus(1234567890, ChronoUnit.MILLIS));
    final Date date = Date.from(Instant.EPOCH.plus(363, ChronoUnit.DAYS));
    final String decimalString = "0.30000000000000004";
    final Integer decimalScale = 17;
    final BigDecimal decimal = new BigDecimal(decimalString);
    final byte[] bytes = "bytes\0".getBytes(StandardCharsets.UTF_8);

    Struct structToBeJsonified =
        new Struct(
                SchemaBuilder.struct()
                    .field(dateFieldName, org.apache.kafka.connect.data.Date.SCHEMA)
                    .field(timestampFieldName, org.apache.kafka.connect.data.Timestamp.SCHEMA)
                    .field(timeFieldName, org.apache.kafka.connect.data.Timestamp.SCHEMA)
                    .field(
                        decimalFieldName,
                        org.apache.kafka.connect.data.Decimal.schema(decimalScale))
                    .field(bytesFieldName, Schema.BYTES_SCHEMA)
                    .build())
            .put(dateFieldName, date)
            .put(timestampFieldName, timestamp)
            .put(timeFieldName, time)
            .put(decimalFieldName, decimal)
            .put(bytesFieldName, bytes);

    String innerField = "innerField";
    String outerField = "outerField";
    Struct innerStruct =
        new Struct(SchemaBuilder.struct().field(innerField, structToBeJsonified.schema()))
            .put(innerField, structToBeJsonified);
    Struct outerStruct =
        new Struct(SchemaBuilder.struct().field(outerField, innerStruct.schema()))
            .put(outerField, innerStruct);

    String expectedStringification =
        "{\"KafkaDate\":363,\"KafkaTimestamp\":1488406838808,\"KafkaTime\":1234567890,\"KafkaDecimal\":\"apTXT0MABA==\",\"KafkaBytes\":\"Ynl0ZXMA\"}";
    ByteString expectedStringificationBytes =
        ByteString.copyFrom(expectedStringification.getBytes(StandardCharsets.UTF_8));

    ValueMapper mapper = new TestValueMapper(null, null, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, outerStruct);
    verify(mutationDataBuilder, times(1))
        .setCell(
            outerField,
            ByteString.copyFrom(innerField.getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            expectedStringificationBytes);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, struct);
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
  public void testEmptyStruct() {
    Schema emptyStructSchema = SchemaBuilder.struct().build();
    Struct emptyStruct = new Struct(emptyStructSchema);
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, emptyStruct);
    assertTotalNumberOfInvocations(mutationDataBuilder, 0);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isEmpty());
  }

  @Test
  public void testSimpleCase1() {
    Integer value = 1;
    String innerField = "bar";
    String outerField = "foo";

    Struct innerStruct =
        new Struct(SchemaBuilder.struct().field(innerField, Schema.INT32_SCHEMA))
            .put(innerField, value);
    Struct outerStruct =
        new Struct(SchemaBuilder.struct().field(outerField, innerStruct.schema()))
            .put(outerField, innerStruct);

    ValueMapper mapper = new TestValueMapper(null, null, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, outerStruct);
    verify(mutationDataBuilder, times(1))
        .setCell(
            outerField,
            ByteString.copyFrom(innerField.getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(value)));
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testSimpleCase2() {
    Integer value = 1;
    String innerField = "fizz";
    String middleField = "bar";
    String outerField = "foo";

    Struct innerStruct =
        new Struct(SchemaBuilder.struct().field(innerField, Schema.INT32_SCHEMA))
            .put(innerField, value);
    Struct middleStruct =
        new Struct(SchemaBuilder.struct().field(middleField, innerStruct.schema()))
            .put(middleField, innerStruct);
    Struct outerStruct =
        new Struct(SchemaBuilder.struct().field(outerField, middleStruct.schema()))
            .put(outerField, middleStruct);

    ValueMapper mapper = new TestValueMapper(null, null, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, outerStruct);
    verify(mutationDataBuilder, times(1))
        .setCell(
            outerField,
            ByteString.copyFrom(middleField.getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(
                ("{\"" + innerField + "\":" + value + "}").getBytes(StandardCharsets.UTF_8)));
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testSimpleCase3() {
    Integer value = 1;
    String field = "foo";
    Struct struct =
        new Struct(SchemaBuilder.struct().field(field, Schema.INT32_SCHEMA)).put(field, value);

    ValueMapper mapper = new TestValueMapper(DEFAULT_COLUMN_FAMILY, null, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, struct);
    verify(mutationDataBuilder, times(1))
        .setCell(
            DEFAULT_COLUMN_FAMILY,
            ByteString.copyFrom(field.getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            ByteString.copyFrom(Bytes.toBytes(value)));
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testComplicatedCase() {
    String innerStructKey = "innerStructKey";
    String familyToBeDeleted = "familyToBeDeleted";
    String columnToBeDeleted = "columnToBeDeleted";
    ByteString columnToBeDeletedBytes =
        ByteString.copyFrom(columnToBeDeleted.getBytes(StandardCharsets.UTF_8));
    String innermostNullKey = "innermostNullKey";
    String outerStructKey = "outerStructKey";

    String value = "value";
    ByteString valueBytes = ByteString.copyFrom((value).getBytes(StandardCharsets.UTF_8));
    String valueKey = "valueKey";
    ByteString valueKeyBytes = ByteString.copyFrom((valueKey).getBytes(StandardCharsets.UTF_8));

    Struct innermostStruct =
        new Struct(
                SchemaBuilder.struct()
                    .field(valueKey, Schema.STRING_SCHEMA)
                    .field(innermostNullKey, Schema.OPTIONAL_INT8_SCHEMA))
            .put(valueKey, value)
            .put(innermostNullKey, null);
    Struct innerStruct =
        new Struct(
                SchemaBuilder.struct()
                    .field(innerStructKey, innermostStruct.schema())
                    .field(valueKey, Schema.STRING_SCHEMA)
                    .field(columnToBeDeleted, Schema.OPTIONAL_INT8_SCHEMA))
            .put(innerStructKey, innermostStruct)
            .put(valueKey, value)
            .put(columnToBeDeleted, null);
    Struct outerStruct =
        new Struct(
                SchemaBuilder.struct()
                    .field(outerStructKey, innerStruct.schema())
                    .field(valueKey, Schema.STRING_SCHEMA)
                    .field(familyToBeDeleted, Schema.OPTIONAL_INT8_SCHEMA))
            .put(outerStructKey, innerStruct)
            .put(valueKey, value)
            .put(familyToBeDeleted, null);

    /*
    {
        outerStructKey: {
            innerStructKey: {
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
    String expectedJsonification = "{\"valueKey\":\"value\",\"innermostNullKey\":null}";
    ByteString expectedJsonificationBytes =
        ByteString.copyFrom(expectedJsonification.getBytes(StandardCharsets.UTF_8));

    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, outerStruct);
    verify(mutationDataBuilder, times(1)).deleteFamily(familyToBeDeleted);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_COLUMN_FAMILY, valueKeyBytes, TIMESTAMP, valueBytes);
    verify(mutationDataBuilder, times(1))
        .deleteCells(
            outerStructKey.toString(),
            columnToBeDeletedBytes,
            Range.TimestampRange.create(0, TIMESTAMP));
    verify(mutationDataBuilder, times(1))
        .setCell(outerStructKey.toString(), valueKeyBytes, TIMESTAMP, valueBytes);
    verify(mutationDataBuilder, times(1))
        .setCell(
            outerStructKey.toString(),
            ByteString.copyFrom(innerStructKey.toString().getBytes(StandardCharsets.UTF_8)),
            TIMESTAMP,
            expectedJsonificationBytes);
    assertTotalNumberOfInvocations(mutationDataBuilder, 5);
  }

  @Test
  public void testNullModeIgnoreRoot() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, null);
    assertTotalNumberOfInvocations(mutationDataBuilder, 0);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isEmpty());
  }

  @Test
  public void testNullModeIgnoreNestedOnce() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder =
        getRecordMutationDataBuilder(mapper, getStructhWithNullOnNthNestingLevel(1));
    assertTotalNumberOfInvocations(mutationDataBuilder, 0);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isEmpty());
  }

  @Test
  public void testNullModeIgnoreNestedTwice() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.IGNORE);
    MutationDataBuilder mutationDataBuilder =
        getRecordMutationDataBuilder(mapper, getStructhWithNullOnNthNestingLevel(2));
    assertTotalNumberOfInvocations(mutationDataBuilder, 0);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isEmpty());
  }

  @Test
  public void testNullModeWriteRoot() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, null);
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
        getRecordMutationDataBuilder(mapper, getStructhWithNullOnNthNestingLevel(1));
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
        getRecordMutationDataBuilder(mapper, getStructhWithNullOnNthNestingLevel(2));
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
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, null);
    verify(mutationDataBuilder, times(1)).deleteRow();
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testNullModeDeleteNestedOnce() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder =
        getRecordMutationDataBuilder(mapper, getStructhWithNullOnNthNestingLevel(1));
    verify(mutationDataBuilder, times(1)).deleteFamily(NESTED_NULL_STRUCT_FIELD_NAME);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testNullModeDeleteNestedTwice() {
    ValueMapper mapper =
        new TestValueMapper(DEFAULT_COLUMN_FAMILY, DEFAULT_COLUMN, NullValueMode.DELETE);
    MutationDataBuilder mutationDataBuilder =
        getRecordMutationDataBuilder(mapper, getStructhWithNullOnNthNestingLevel(2));
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
        getRecordMutationDataBuilder(mapper, getStructhWithNullOnNthNestingLevel(3));
    verify(mutationDataBuilder, times(1))
        .setCell(
            NESTED_NULL_STRUCT_FIELD_NAME,
            NESTED_NULL_STRUCT_FIELD_NAME_BYTES,
            TIMESTAMP,
            expectedJsonificationBytes);
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  @Test
  public void testDefaultColumnFamilySubstitution() {
    ValueMapper mapper =
        new TestValueMapper(
            ConfigInterpolation.TOPIC_PLACEHOLDER, DEFAULT_COLUMN, NullValueMode.WRITE);
    MutationDataBuilder mutationDataBuilder = getRecordMutationDataBuilder(mapper, null);
    verify(mutationDataBuilder, times(1))
        .setCell(DEFAULT_TOPIC, DEFAULT_COLUMN_BYTES, TIMESTAMP, ByteString.empty());
    assertTotalNumberOfInvocations(mutationDataBuilder, 1);
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isPresent());
  }

  private MutationDataBuilder getRecordMutationDataBuilder(ValueMapper mapper, Object kafkaValue) {
    return getRecordMutationDataBuilder(mapper, kafkaValue, DEFAULT_TOPIC);
  }

  private MutationDataBuilder getRecordMutationDataBuilder(
      ValueMapper mapper, Object kafkaValue, String topic) {
    // We use `null` in this test since our code for now uses  Schema only to warn the user when an
    // unsupported logical type is encountered.
    SchemaAndValue schemaAndValue = new SchemaAndValue(null, kafkaValue);
    return mapper.getRecordMutationDataBuilder(schemaAndValue, topic, TIMESTAMP);
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
