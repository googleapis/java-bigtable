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
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.dateType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.dateValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.float32Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.float64Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.floatValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.int64Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.int64Value;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.mapElement;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.mapType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.structField;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.structType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.structValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.timestampType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.timestampValue;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.google.bigtable.v2.ArrayValue;
import com.google.bigtable.v2.Type.Struct;
import com.google.bigtable.v2.Value;
import com.google.cloud.Date;
import com.google.protobuf.ByteString;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Instant;

@RunWith(JUnit4.class)
public class ProtoStructTest {

  static ProtoStruct struct =
      ProtoStruct.create(
          structType(
                  structField("bytesField", bytesType()),
                  structField("stringField", stringType()),
                  structField("longField", int64Type()),
                  structField("doubleField", float64Type()),
                  structField("floatField", float32Type()),
                  structField("booleanField", boolType()),
                  structField("timestampField", timestampType()),
                  structField("dateField", dateType()),
                  structField("structField", structType(structField("stringField", stringType()))),
                  structField("listField", arrayType(stringType())),
                  structField("mapField", mapType(stringType(), stringType())))
              .getStructType(),
          arrayValue(
                  bytesValue("testBytes"),
                  stringValue("testString"),
                  int64Value(123),
                  floatValue(1.23),
                  floatValue(1.23),
                  boolValue(true),
                  timestampValue(100000, 100),
                  dateValue(2024, 6, 1),
                  structValue(stringValue("string")),
                  arrayValue(stringValue("foo"), stringValue("bar")),
                  arrayValue(
                      mapElement(stringValue("foo"), stringValue("bar")),
                      mapElement(stringValue("key"), stringValue("val"))))
              .getArrayValue());

  // These are more extensively tested in AbstractProtoStructReaderTest since that is what
  // implements the logic
  @Test
  public void getByIndex_supportsAllTypes() {
    assertThat(struct.getBytes(0)).isEqualTo(ByteString.copyFromUtf8("testBytes"));
    assertThat(struct.getString(1)).isEqualTo("testString");
    assertThat(struct.getLong(2)).isEqualTo(123);
    assertThat(struct.getDouble(3)).isEqualTo(1.23d);
    assertThat(struct.getFloat(4)).isEqualTo(1.23f);
    assertThat(struct.getBoolean(5)).isTrue();
    assertThat(struct.getTimestamp(6)).isEqualTo(Instant.ofEpochSecond(100000, 100));
    assertThat(struct.getDate(7)).isEqualTo(Date.fromYearMonthDay(2024, 6, 1));
    assertThat(struct.getStruct(8))
        .isEqualTo(
            ProtoStruct.create(
                structType(structField("stringField", stringType())).getStructType(),
                structValue(stringValue("string")).getArrayValue()));
    assertThat(struct.getList(9)).isEqualTo(Arrays.asList("foo", "bar"));
    assertThat(struct.getMap(10))
        .isEqualTo(
            new HashMap<String, String>() {
              {
                put("foo", "bar");
                put("key", "val");
              }
            });
  }

  @Test
  public void getByNameSupportsAllTypes() {
    assertThat(struct.getBytes("bytesField")).isEqualTo(ByteString.copyFromUtf8("testBytes"));
    assertThat(struct.getString("stringField")).isEqualTo("testString");
    assertThat(struct.getLong("longField")).isEqualTo(123);
    assertThat(struct.getDouble("doubleField")).isEqualTo(1.23d);
    assertThat(struct.getFloat("floatField")).isEqualTo(1.23f);
    assertThat(struct.getBoolean("booleanField")).isTrue();
    assertThat(struct.getTimestamp("timestampField")).isEqualTo(Instant.ofEpochSecond(100000, 100));
    assertThat(struct.getDate("dateField")).isEqualTo(Date.fromYearMonthDay(2024, 6, 1));
    assertThat(struct.getStruct("structField"))
        .isEqualTo(
            ProtoStruct.create(
                structType(structField("stringField", stringType())).getStructType(),
                structValue(stringValue("string")).getArrayValue()));
    assertThat(struct.getList("listField")).isEqualTo(Arrays.asList("foo", "bar"));
    assertThat(struct.getMap("mapField"))
        .isEqualTo(
            new HashMap<String, String>() {
              {
                put("foo", "bar");
                put("key", "val");
              }
            });
  }

  @Test
  public void getColumnType_byName() {
    assertThat(struct.getColumnType("bytesField")).isEqualTo(bytesType());
    assertThat(struct.getColumnType("stringField")).isEqualTo(stringType());
    assertThat(struct.getColumnType("longField")).isEqualTo(int64Type());
    assertThat(struct.getColumnType("doubleField")).isEqualTo(float64Type());
    assertThat(struct.getColumnType("floatField")).isEqualTo(float32Type());
    assertThat(struct.getColumnType("booleanField")).isEqualTo(boolType());
    assertThat(struct.getColumnType("timestampField")).isEqualTo(timestampType());
    assertThat(struct.getColumnType("dateField")).isEqualTo(dateType());
    assertThat(struct.getColumnType("structField"))
        .isEqualTo(structType(structField("stringField", stringType())));
    assertThat(struct.getColumnType("listField")).isEqualTo(arrayType(stringType()));
    assertThat(struct.getColumnType("mapField")).isEqualTo(mapType(stringType(), stringType()));
  }

  @Test
  public void getColumnType_byIndex() {
    assertThat(struct.getColumnType(0)).isEqualTo(bytesType());
    assertThat(struct.getColumnType(1)).isEqualTo(stringType());
    assertThat(struct.getColumnType(2)).isEqualTo(int64Type());
    assertThat(struct.getColumnType(3)).isEqualTo(float64Type());
    assertThat(struct.getColumnType(4)).isEqualTo(float32Type());
    assertThat(struct.getColumnType(5)).isEqualTo(boolType());
    assertThat(struct.getColumnType(6)).isEqualTo(timestampType());
    assertThat(struct.getColumnType(7)).isEqualTo(dateType());
    assertThat(struct.getColumnType(8))
        .isEqualTo(structType(structField("stringField", stringType())));
    assertThat(struct.getColumnType(9)).isEqualTo(arrayType(stringType()));
    assertThat(struct.getColumnType(10)).isEqualTo(mapType(stringType(), stringType()));
  }

  @Test
  public void getColumnIndex_worksForExistingColumns() {
    assertThat(struct.getColumnIndex("bytesField")).isEqualTo(0);
    assertThat(struct.getColumnIndex("stringField")).isEqualTo(1);
    assertThat(struct.getColumnIndex("longField")).isEqualTo(2);
    assertThat(struct.getColumnIndex("doubleField")).isEqualTo(3);
    assertThat(struct.getColumnIndex("floatField")).isEqualTo(4);
    assertThat(struct.getColumnIndex("booleanField")).isEqualTo(5);
    assertThat(struct.getColumnIndex("timestampField")).isEqualTo(6);
    assertThat(struct.getColumnIndex("dateField")).isEqualTo(7);
    assertThat(struct.getColumnIndex("structField")).isEqualTo(8);
    assertThat(struct.getColumnIndex("listField")).isEqualTo(9);
    assertThat(struct.getColumnIndex("mapField")).isEqualTo(10);
  }

  @Test
  public void getColumnIndex_throwsExceptionForNonExistentIndex() {
    assertThrows(IllegalArgumentException.class, () -> struct.getColumnIndex("nonexistent"));
  }

  @Test
  public void values_populatedFromFieldValues() {
    List<Value> values = Arrays.asList(stringValue("foo"), stringValue("bar"));
    ProtoStruct s =
        ProtoStruct.create(
            structType(
                    structField("stringField1", stringType()),
                    structField("stringField2", stringType()))
                .getStructType(),
            arrayValue(values.toArray(new Value[] {})).getArrayValue());

    assertThat(s.values()).isEqualTo(values);
  }

  @Test
  public void getByColumnIndex_supportsUnnamedColumn() {
    ProtoStruct s =
        ProtoStruct.create(
            // This creates a struct with two unnamed string fields
            structType(stringType(), stringType()).getStructType(),
            arrayValue(stringValue("foo"), stringValue("bar")).getArrayValue());

    assertThat(s.getString(0)).isEqualTo("foo");
    assertThat(s.getString(1)).isEqualTo("bar");
  }

  @Test
  public void getByColumnName_supportsUnnamedColumn() {
    ProtoStruct s =
        ProtoStruct.create(
            // This creates a struct with one unnamed string fields
            structType(stringType()).getStructType(),
            arrayValue(stringValue("foo")).getArrayValue());

    assertThat(s.getString("")).isEqualTo("foo");
  }

  @Test
  public void emptyStruct_behavesCorrectly() {
    ProtoStruct empty =
        ProtoStruct.create(Struct.newBuilder().build(), ArrayValue.newBuilder().build());

    assertThrows(IndexOutOfBoundsException.class, () -> empty.getString(0));
    assertThrows(IllegalArgumentException.class, () -> empty.getString(""));
    assertThrows(IndexOutOfBoundsException.class, () -> empty.getColumnType(0));
    assertThrows(IllegalArgumentException.class, () -> empty.getColumnType(""));
  }

  // TODO(jackdingilian): implement this when we have proper metadata wrapper
  @Test
  public void getColumnIndexOnDuplicateField_throwsException() {
    assertTrue(true);
  }

  // TODO(jackdingilian): implement this when we have proper metadata wrapper
  @Test
  public void getByFieldNameOnDuplicateField_throwsException() {
    assertTrue(true);
  }
}
