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
package com.google.cloud.bigtable.data.v2.internal;

import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.arrayType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.arrayValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.boolType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.boolValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.bytesType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.bytesValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.columnMetadata;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.dateType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.dateValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.float32Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.float64Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.floatValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.int64Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.int64Value;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.mapElement;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.mapType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.mapValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.nullValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.timestampType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.timestampValue;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.ColumnMetadata;
import com.google.bigtable.v2.Type;
import com.google.bigtable.v2.Type.KindCase;
import com.google.bigtable.v2.Value;
import com.google.cloud.Date;
import com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.threeten.bp.Instant;

@RunWith(Enclosed.class)
public class AbstractProtoStructReaderTest {

  @AutoValue
  public abstract static class TestProtoStruct extends AbstractProtoStructReader {
    public static TestProtoStruct create(List<ColumnMetadata> schema, List<Value> values) {
      return new AutoValue_AbstractProtoStructReaderTest_TestProtoStruct(values, schema);
    }

    abstract List<ColumnMetadata> schema();

    @Override
    // This is a temporary hack. The type/metadata wrappers will provide an efficient way to lookup
    // fields and type by columnName
    // TODO(jackdingilian): replace with efficient lookup
    // TODO(jackdingilian): fail on requests for column that appears multiple times
    public int getColumnIndex(String columnName) {
      for (int i = 0; i < schema().size(); i++) {
        if (schema().get(i).getName().equals(columnName)) {
          return i;
        }
      }
      throw new IllegalArgumentException("No field found with name: " + columnName);
    }

    @Override
    public Type getColumnType(int columnIndex) {
      return schema().get(columnIndex).getType();
    }
  }

  @Parameterized.Parameters()
  public static List<Object[]> parameters() {
    return Arrays.asList(
        new Object[][] {
          // Bytes
          {
            Collections.singletonList(columnMetadata("testField", bytesType())),
            Collections.singletonList(bytesValue("test")),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, ByteString>) TestProtoStruct::getBytes,
            (BiFunction<TestProtoStruct, Integer, ByteString>) TestProtoStruct::getBytes,
            ByteString.copyFromUtf8("test")
          },
          // String
          {
            Collections.singletonList(columnMetadata("testField", stringType())),
            Collections.singletonList(stringValue("test")),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, String>) TestProtoStruct::getString,
            (BiFunction<TestProtoStruct, Integer, String>) TestProtoStruct::getString,
            "test"
          },
          // Long
          {
            Collections.singletonList(columnMetadata("testField", int64Type())),
            Collections.singletonList(int64Value(110L)),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Long>) TestProtoStruct::getLong,
            (BiFunction<TestProtoStruct, Integer, Long>) TestProtoStruct::getLong,
            110L
          },
          // Double
          {
            Collections.singletonList(columnMetadata("testField", float64Type())),
            Collections.singletonList(floatValue(100.3d)),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Double>) TestProtoStruct::getDouble,
            (BiFunction<TestProtoStruct, Integer, Double>) TestProtoStruct::getDouble,
            100.3d
          },
          // Float
          {
            Collections.singletonList(columnMetadata("testField", float32Type())),
            Collections.singletonList(floatValue(100.3f)),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Float>) TestProtoStruct::getFloat,
            (BiFunction<TestProtoStruct, Integer, Float>) TestProtoStruct::getFloat,
            100.3f
          },
          // Boolean
          {
            Collections.singletonList(columnMetadata("testField", boolType())),
            Collections.singletonList(boolValue(true)),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Boolean>) TestProtoStruct::getBoolean,
            (BiFunction<TestProtoStruct, Integer, Boolean>) TestProtoStruct::getBoolean,
            true
          },
          // Timestamp
          {
            Collections.singletonList(columnMetadata("testField", timestampType())),
            Collections.singletonList(timestampValue(1000000, 100)),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Instant>) TestProtoStruct::getTimestamp,
            (BiFunction<TestProtoStruct, Integer, Instant>) TestProtoStruct::getTimestamp,
            Instant.ofEpochSecond(1000000, 100)
          },
          // MAX long timestamp - bigtable allows users to set timestamp to any long so the
          // client should parse them
          {
            Collections.singletonList(columnMetadata("testField", timestampType())),
            Collections.singletonList(timestampValue(Long.MAX_VALUE, 0)),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Instant>) TestProtoStruct::getTimestamp,
            (BiFunction<TestProtoStruct, Integer, Instant>) TestProtoStruct::getTimestamp,
            Instant.ofEpochSecond(Long.MAX_VALUE)
          },
          // Date
          {
            Collections.singletonList(columnMetadata("testField", dateType())),
            Collections.singletonList(dateValue(2024, 6, 1)),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Date>) TestProtoStruct::getDate,
            (BiFunction<TestProtoStruct, Integer, Date>) TestProtoStruct::getDate,
            Date.fromYearMonthDay(2024, 6, 1)
          },
          // TODO(jackdingilian): struct
          // Simple List
          {
            Collections.singletonList(columnMetadata("testField", arrayType(stringType()))),
            Collections.singletonList(
                arrayValue(stringValue("foo"), stringValue("bar"), stringValue("baz"))),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, List<String>>) TestProtoStruct::getList,
            (BiFunction<TestProtoStruct, Integer, List<String>>) TestProtoStruct::getList,
            Arrays.asList("foo", "bar", "baz")
          },
          // List With Null Values
          {
            Collections.singletonList(columnMetadata("testField", arrayType(stringType()))),
            Collections.singletonList(
                arrayValue(stringValue("foo"), nullValue(), stringValue("baz"))),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, List<String>>) TestProtoStruct::getList,
            (BiFunction<TestProtoStruct, Integer, List<String>>) TestProtoStruct::getList,
            Arrays.asList("foo", null, "baz")
          },
          // Simple Map
          {
            Collections.singletonList(
                columnMetadata("testField", mapType(bytesType(), stringType()))),
            Collections.singletonList(
                mapValue(
                    mapElement(bytesValue("foo"), stringValue("bar")),
                    mapElement(bytesValue("key"), stringValue("val")))),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Map<ByteString, String>>) TestProtoStruct::getMap,
            (BiFunction<TestProtoStruct, Integer, Map<ByteString, String>>) TestProtoStruct::getMap,
            new HashMap<ByteString, String>() {
              {
                put(ByteString.copyFromUtf8("foo"), "bar");
                put(ByteString.copyFromUtf8("key"), "val");
              }
            }
          },
          // Map With Null Keys and Values
          {
            Collections.singletonList(
                columnMetadata("testField", mapType(bytesType(), stringType()))),
            Collections.singletonList(
                mapValue(
                    mapElement(bytesValue("foo"), nullValue()),
                    mapElement(nullValue(), stringValue("val")))),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Map<ByteString, String>>) TestProtoStruct::getMap,
            (BiFunction<TestProtoStruct, Integer, Map<ByteString, String>>) TestProtoStruct::getMap,
            new HashMap<ByteString, String>() {
              {
                put(ByteString.copyFromUtf8("foo"), null);
                put(null, "val");
              }
            }
          },
          // Map With List Values
          {
            Collections.singletonList(
                columnMetadata("testField", mapType(bytesType(), arrayType(stringType())))),
            Collections.singletonList(
                mapValue(
                    mapElement(
                        bytesValue("key1"), arrayValue(stringValue("1.1"), stringValue("1.2"))),
                    mapElement(bytesValue("key2"), arrayValue(stringValue("2.1"))))),
            0,
            "testField",
            (BiFunction<TestProtoStruct, String, Map<ByteString, List<String>>>)
                TestProtoStruct::getMap,
            (BiFunction<TestProtoStruct, Integer, Map<ByteString, List<String>>>)
                TestProtoStruct::getMap,
            new HashMap<ByteString, List<String>>() {
              {
                put(ByteString.copyFromUtf8("key1"), Arrays.asList("1.1", "1.2"));
                put(ByteString.copyFromUtf8("key2"), Collections.singletonList("2.1"));
              }
            }
          },
          // TODO(jackdingilian): when we have struct Historical Map format
        });
  }

  @Parameter(value = 0)
  public List<ColumnMetadata> schema;

  @Parameter(value = 1)
  public List<Value> values;

  @Parameter(value = 2)
  public Integer index;

  @Parameter(value = 3)
  public String columnName;

  @Parameter(value = 4)
  public BiFunction<TestProtoStruct, String, Object> getByColumn;

  @Parameter(value = 5)
  public BiFunction<TestProtoStruct, Integer, Object> getByIndex;

  @Parameter(value = 6)
  public Object expectedJavaValue;

  @Test
  public void getByColumnName_convertsValues() {
    TestProtoStruct row = TestProtoStruct.create(schema, values);

    assertThat(expectedJavaValue).isEqualTo(getByColumn.apply(row, columnName));
  }

  @Test
  public void getByIndex_convertsValues() {
    TestProtoStruct row = TestProtoStruct.create(schema, values);

    assertThat(expectedJavaValue).isEqualTo(getByIndex.apply(row, index));
  }

  @Test
  public void getByColumnName_throwsExceptionOnNonExistentColumn() {
    TestProtoStruct row = TestProtoStruct.create(schema, values);

    assertThrows(IllegalArgumentException.class, () -> getByColumn.apply(row, "invalid"));
  }

  @Test
  public void getByColumnIndex_throwsExceptionOnNonExistentColumn() {
    TestProtoStruct row = TestProtoStruct.create(schema, values);

    // Assume none of the tests have 10k columns
    assertThrows(IndexOutOfBoundsException.class, () -> getByIndex.apply(row, 10000));
  }

  @Test
  public void getByColumnIndex_throwsNullPointerOnNullValue() {
    TestProtoStruct row =
        TestProtoStruct.create(
            schema,
            schema.stream()
                .map((ColumnMetadata t) -> SqlProtoFactory.nullValue())
                .collect(Collectors.toList()));

    assertThrows(NullPointerException.class, () -> getByIndex.apply(row, index));
  }

  @Test
  public void getByColumnName_throwsNullPointerOnNullValue() {
    TestProtoStruct row =
        TestProtoStruct.create(
            schema,
            schema.stream()
                .map((ColumnMetadata t) -> SqlProtoFactory.nullValue())
                .collect(Collectors.toList()));

    assertThrows(NullPointerException.class, () -> getByColumn.apply(row, columnName));
  }

  @Test
  public void getByColumnIndex_throwsExceptionOnWrongType() {
    // Replace the given column with a column of a different type
    Type updatedType = stringType();
    Value updatedValue = stringValue("test");
    if (schema.get(index).getType().getKindCase().equals(KindCase.STRING_TYPE)) {
      updatedType = int64Type();
      updatedValue = int64Value(1000);
    }
    schema.add(index, columnMetadata(columnName, updatedType));
    values.add(index, updatedValue);
    TestProtoStruct row = TestProtoStruct.create(schema, values);

    assertThrows(IllegalStateException.class, () -> getByIndex.apply(row, index));
  }

  @Test
  public void getByColumnName_throwsExceptionOnWrongType() {
    // Replace the given column with a column of a different type
    Type updatedType = stringType();
    Value updatedValue = stringValue("test");
    if (schema.get(index).getType().getKindCase().equals(KindCase.STRING_TYPE)) {
      updatedType = int64Type();
      updatedValue = int64Value(1000);
    }
    schema.add(index, columnMetadata(columnName, updatedType));
    values.add(index, updatedValue);
    TestProtoStruct row = TestProtoStruct.create(schema, values);

    assertThrows(IllegalStateException.class, () -> getByColumn.apply(row, columnName));
  }

  @Test
  public void isNull_worksForNullValues() {
    TestProtoStruct row =
        TestProtoStruct.create(
            schema,
            schema.stream()
                .map((ColumnMetadata t) -> SqlProtoFactory.nullValue())
                .collect(Collectors.toList()));

    assertTrue(row.isNull(columnName));
    assertTrue(row.isNull(index));
  }

  @Test
  public void isNull_worksForNonNullValues() {
    TestProtoStruct row = TestProtoStruct.create(schema, values);

    assertFalse(row.isNull(columnName));
    assertFalse(row.isNull(index));
  }

  @Test
  public void getColumnTypeByName() {
    TestProtoStruct row = TestProtoStruct.create(schema, values);

    assertThat(schema.get(index).getType()).isEqualTo(row.getColumnType(columnName));
  }

  // TODO(jackdingilian): Test this once we have a ResultSetMetadata wrapper.
  // consider moving it to non-parameterized test
  @Test
  public void getByColumnName_throwsExceptionForDuplicateColumnName() {
    assertTrue(true);
  }

  @Test
  public void getByIndex_worksWithDuplicateColumnName() {
    List<ColumnMetadata> duplicatedSchema = new ArrayList<>(schema);
    duplicatedSchema.addAll(schema);
    List<Value> duplicatedValues = new ArrayList<>(values);
    duplicatedValues.addAll(values);
    TestProtoStruct row = TestProtoStruct.create(duplicatedSchema, duplicatedValues);

    assertThat(expectedJavaValue).isEqualTo(getByIndex.apply(row, index));
  }
}
