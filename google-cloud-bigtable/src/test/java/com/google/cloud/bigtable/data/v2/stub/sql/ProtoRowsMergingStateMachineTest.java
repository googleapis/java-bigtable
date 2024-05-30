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
package com.google.cloud.bigtable.data.v2.stub.sql;

import static com.google.cloud.bigtable.data.v2.stub.sql.ProtoRowsMergingStateMachineSubject.assertThat;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.aggregateSumType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.arrayType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.arrayValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.boolType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.bytesType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.bytesValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.columnMetadata;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.float32Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.float64Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.int64Type;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.mapElement;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.mapType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.mapValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.partialResultSetWithToken;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.partialResultSetWithoutToken;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.structType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.structValue;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.timestampType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.tokenOnlyResultSet;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertThrows;

import com.google.bigtable.v2.ColumnMetadata;
import com.google.bigtable.v2.PartialResultSet;
import com.google.bigtable.v2.ProtoRows;
import com.google.bigtable.v2.ProtoRowsBatch;
import com.google.bigtable.v2.ProtoSchema;
import com.google.bigtable.v2.Type;
import com.google.bigtable.v2.Value;
import com.google.cloud.bigtable.data.v2.models.SqlRow;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import java.util.ArrayDeque;
import java.util.Arrays;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

// Use enclosed runner so we can put parameterized and non-parameterized cases in the same test
// suite
@RunWith(Enclosed.class)
public final class ProtoRowsMergingStateMachineTest {

  public static final class IndividualTests {
    @Test
    public void stateMachine_hasCompleteBatch_falseWhenEmpty() {
      ProtoSchema schema =
          ProtoSchema.newBuilder().addColumns(columnMetadata("a", stringType())).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      assertThat(stateMachine).hasCompleteBatch(false);
    }

    @Test
    public void stateMachine_hasCompleteBatch_falseWhenAwaitingPartialBatch() {
      ProtoSchema schema =
          ProtoSchema.newBuilder().addColumns(columnMetadata("a", stringType())).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      stateMachine.addPartialResultSet(
          partialResultSetWithoutToken(stringValue("foo")).getResults());
      assertThat(stateMachine).hasCompleteBatch(false);
    }

    @Test
    public void stateMachine_hasCompleteBatch_trueWhenAwaitingBatchConsume() {
      ProtoSchema schema =
          ProtoSchema.newBuilder().addColumns(columnMetadata("a", stringType())).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      stateMachine.addPartialResultSet(
          partialResultSetWithoutToken(stringValue("foo")).getResults());
      stateMachine.addPartialResultSet(partialResultSetWithToken(stringValue("bar")).getResults());
      assertThat(stateMachine).hasCompleteBatch(true);
    }

    @Test
    public void stateMachine_isBatchInProgress_falseWhenEmpty() {
      ProtoSchema schema =
          ProtoSchema.newBuilder().addColumns(columnMetadata("a", stringType())).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      assertThat(stateMachine).isBatchInProgress(false);
    }

    @Test
    public void stateMachine_isBatchInProgress_trueWhenAwaitingPartialBatch() {
      ProtoSchema schema =
          ProtoSchema.newBuilder().addColumns(columnMetadata("a", stringType())).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      stateMachine.addPartialResultSet(
          partialResultSetWithoutToken(stringValue("foo")).getResults());
      assertThat(stateMachine).isBatchInProgress(true);
    }

    @Test
    public void stateMachine_isBatchInProgress_trueWhenAwaitingBatchConsume() {
      ProtoSchema schema =
          ProtoSchema.newBuilder().addColumns(columnMetadata("a", stringType())).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      stateMachine.addPartialResultSet(
          partialResultSetWithoutToken(stringValue("foo")).getResults());
      assertThat(stateMachine).isBatchInProgress(true);
    }

    @Test
    public void stateMachine_consumeRow_throwsExceptionWhenColumnsArentComplete() {
      ProtoSchema schema =
          ProtoSchema.newBuilder()
              .addAllColumns(
                  Arrays.asList(
                      columnMetadata("a", stringType()), columnMetadata("b", stringType())))
              .build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      // this is a valid partial result set so we don't expect an error until we call populateQueue
      stateMachine.addPartialResultSet(partialResultSetWithToken(stringValue("foo")).getResults());
      assertThrows(
          IllegalStateException.class, () -> stateMachine.populateQueue(new ArrayDeque<>()));
    }

    @Test
    public void stateMachine_consumeRow_throwsExceptionWhenAwaitingPartialBatch() {
      ProtoSchema schema =
          ProtoSchema.newBuilder()
              .addAllColumns(Arrays.asList(columnMetadata("a", stringType())))
              .build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      // this doesn't have a token so we shouldn't allow results to be processed
      stateMachine.addPartialResultSet(
          partialResultSetWithoutToken(stringValue("foo")).getResults());
      assertThrows(
          IllegalStateException.class, () -> stateMachine.populateQueue(new ArrayDeque<>()));
    }

    @Test
    public void stateMachine_mergesPartialBatches() {
      ImmutableList<ColumnMetadata> columns = ImmutableList.of(columnMetadata("a", stringType()));
      ProtoSchema schema = ProtoSchema.newBuilder().addAllColumns(columns).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      stateMachine.addPartialResultSet(
          partialResultSetWithoutToken(stringValue("foo")).getResults());
      stateMachine.addPartialResultSet(
          partialResultSetWithoutToken(stringValue("bar")).getResults());
      stateMachine.addPartialResultSet(partialResultSetWithToken(stringValue("baz")).getResults());

      assertThat(stateMachine)
          .populateQueueYields(
              SqlRow.create(columns, ImmutableList.of(stringValue("foo"))),
              SqlRow.create(columns, ImmutableList.of(stringValue("bar"))),
              SqlRow.create(columns, ImmutableList.of(stringValue("baz"))));
    }

    @Test
    public void stateMachine_mergesPartialBatches_withRandomChunks() {
      ProtoSchema schema =
          ProtoSchema.newBuilder()
              .addAllColumns(
                  ImmutableList.of(columnMetadata("map", mapType(stringType(), bytesType()))))
              .build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      Value mapVal =
          mapValue(
              mapElement(
                  stringValue(Strings.repeat("a", 10)), bytesValue(Strings.repeat("aVal", 100))),
              mapElement(stringValue("b"), bytesValue(Strings.repeat("bVal", 100))));
      ProtoRows rows = ProtoRows.newBuilder().addValues(mapVal).build();
      ByteString chunk1 = rows.toByteString().substring(0, 100);
      ByteString chunk2 = rows.toByteString().substring(100);

      stateMachine.addPartialResultSet(
          PartialResultSet.newBuilder()
              .setProtoRowsBatch(ProtoRowsBatch.newBuilder().setBatchData(chunk1).build())
              .build());
      stateMachine.addPartialResultSet(
          PartialResultSet.newBuilder()
              .setResumeToken(ByteString.copyFromUtf8("token"))
              .setProtoRowsBatch(ProtoRowsBatch.newBuilder().setBatchData(chunk2).build())
              .build());

      assertThat(stateMachine)
          .populateQueueYields(
              SqlRow.create(
                  ImmutableList.copyOf(schema.getColumnsList()), ImmutableList.of(mapVal)));
    }

    @Test
    public void stateMachine_reconstructsRowWithMultipleColumns() {
      ProtoSchema schema =
          ProtoSchema.newBuilder()
              .addAllColumns(
                  ImmutableList.of(
                      columnMetadata("a", stringType()),
                      columnMetadata("b", bytesType()),
                      columnMetadata("c", arrayType(stringType())),
                      columnMetadata("d", mapType(stringType(), bytesType()))))
              .build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);

      Value stringVal = stringValue("test");
      stateMachine.addPartialResultSet(partialResultSetWithoutToken(stringVal).getResults());
      Value bytesVal = bytesValue("bytes");
      stateMachine.addPartialResultSet(partialResultSetWithoutToken(bytesVal).getResults());
      Value arrayVal = arrayValue(stringValue("foo"), stringValue("bar"));
      stateMachine.addPartialResultSet(partialResultSetWithoutToken(arrayVal).getResults());
      Value mapVal =
          mapValue(
              mapElement(stringValue("a"), bytesValue("aVal")),
              mapElement(stringValue("b"), bytesValue("bVal")));
      stateMachine.addPartialResultSet(partialResultSetWithToken(mapVal).getResults());

      assertThat(stateMachine).hasCompleteBatch(true);
      assertThat(stateMachine)
          .populateQueueYields(
              SqlRow.create(
                  ImmutableList.copyOf(schema.getColumnsList()),
                  ImmutableList.of(stringVal, bytesVal, arrayVal, mapVal)));

      // Once we consume a completed row the state machine should be reset
      assertThat(stateMachine).hasCompleteBatch(false);
      assertThrows(
          IllegalStateException.class, () -> stateMachine.populateQueue(new ArrayDeque<>()));
      assertThat(stateMachine).isBatchInProgress(false);
    }

    @Test
    public void stateMachine_throwsExceptionWhenValuesDontMatchSchema() {
      ProtoSchema schema =
          ProtoSchema.newBuilder()
              .addAllColumns(
                  ImmutableList.of(
                      columnMetadata("a", stringType()), columnMetadata("b", bytesType())))
              .build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);

      // values in wrong order
      stateMachine.addPartialResultSet(
          partialResultSetWithToken(bytesValue("test"), stringValue("test")).getResults());
      assertThrows(
          IllegalStateException.class, () -> stateMachine.populateQueue(new ArrayDeque<>()));
    }

    @Test
    public void stateMachine_handlesResumeTokenWithNoValues() {
      ImmutableList<ColumnMetadata> columns = ImmutableList.of(columnMetadata("a", stringType()));
      ProtoSchema schema = ProtoSchema.newBuilder().addAllColumns(columns).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);

      stateMachine.addPartialResultSet(partialResultSetWithToken().getResults());
      assertThat(stateMachine).populateQueueYields(new SqlRow[] {});
    }

    @Test
    public void stateMachine_handlesResumeTokenWithOpenBatch() {
      ImmutableList<ColumnMetadata> columns = ImmutableList.of(columnMetadata("a", stringType()));
      ProtoSchema schema = ProtoSchema.newBuilder().addAllColumns(columns).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);

      stateMachine.addPartialResultSet(
          partialResultSetWithoutToken(stringValue("test")).getResults());
      stateMachine.addPartialResultSet(
          tokenOnlyResultSet(ByteString.copyFromUtf8("token")).getResults());
      assertThat(stateMachine)
          .populateQueueYields(SqlRow.create(columns, ImmutableList.of(stringValue("test"))));
    }

    @Test
    public void addPartialResultSet_throwsExceptionWhenAwaitingRowConsume() {
      ProtoSchema schema =
          ProtoSchema.newBuilder().addColumns(columnMetadata("a", stringType())).build();
      ProtoRowsMergingStateMachine stateMachine = new ProtoRowsMergingStateMachine(schema);
      stateMachine.addPartialResultSet(partialResultSetWithToken(stringValue("test")).getResults());

      assertThrows(
          IllegalStateException.class,
          () ->
              stateMachine.addPartialResultSet(
                  partialResultSetWithToken(stringValue("test2")).getResults()));
    }

    @Test
    public void stateMachine_throwsExceptionWithEmptySchema() {
      assertThrows(
          IllegalStateException.class,
          () -> new ProtoRowsMergingStateMachine(ProtoSchema.getDefaultInstance()));
    }

    @Test
    public void stateMachine_withEmptyTypeInSchema_throwsException() {
      ImmutableList<ColumnMetadata> columns =
          ImmutableList.of(
              columnMetadata("a", stringType()), columnMetadata("b", Type.getDefaultInstance()));
      assertThrows(
          IllegalStateException.class,
          () ->
              new ProtoRowsMergingStateMachine(
                  ProtoSchema.newBuilder().addAllColumns(columns).build()));
    }
  }

  @RunWith(Parameterized.class)
  public static final class ParameterizedTests {

    public ParameterizedTests(Type.KindCase typeCase) {
      this.typeCase = typeCase;
    }

    private final Type.KindCase typeCase;

    @Parameters
    public static Type.KindCase[] valueTypes() {
      return Type.KindCase.values();
    }

    @Test
    @SuppressWarnings("UnnecessaryDefaultInEnumSwitch")
    public void testValidateSupportsAllTypes() {
      switch (typeCase) {
        case STRING_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      stringType(), bytesValue("test")));
          break;
        case BYTES_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      bytesType(), stringValue("test")));
          break;
        case INT64_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      int64Type(), stringValue("test")));
          break;
        case BOOL_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      boolType(), stringValue("test")));
          break;
        case FLOAT32_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      float32Type(), stringValue("test")));
          break;
        case FLOAT64_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      float64Type(), stringValue("test")));
          break;
        case TIMESTAMP_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      timestampType(), stringValue("test")));
          break;
        case DATE_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      SqlProtoFactory.dateType(), stringValue("test")));
          break;
        case ARRAY_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      arrayType(stringType()), stringValue("test")));
          // It should check nested values match
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      arrayType(stringType()),
                      arrayValue(stringValue("test"), bytesValue("test"))));
          break;
        case STRUCT_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      structType(stringType(), bytesType()), stringValue("test")));
          // It should check nested values match
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      structType(stringType(), bytesType()),
                      structValue(stringValue("test"), stringValue("test"))));
          break;
        case MAP_TYPE:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      mapType(stringType(), stringType()), stringValue("test")));
          // It should check nested values match
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      mapType(stringType(), bytesType()),
                      mapValue(
                          mapElement(stringValue("key"), bytesValue("val")),
                          mapElement(stringValue("key2"), stringValue("val2")))));
          // It should check all map elements contain only one key and one value because map
          // elements
          // are represented as structs which are represented as an array of fields.
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      mapType(stringType(), bytesType()),
                      mapValue(
                          mapElement(stringValue("key"), bytesValue("val")),
                          structValue(
                              stringValue("key2"), bytesValue("val2"), bytesValue("val3")))));
          break;
        case AGGREGATE_TYPE:
          // These aren't supported so they should throw an exception no matter what value type is
          // passed.
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      aggregateSumType(), stringValue("test")));
          break;
        case KIND_NOT_SET:
          assertThrows(
              IllegalStateException.class,
              () ->
                  ProtoRowsMergingStateMachine.validateValueAndType(
                      Type.getDefaultInstance(), stringValue("test")));
          break;
        default:
          assertWithMessage(
                  "Unknown TypeCase "
                      + typeCase.name()
                      + " seen. Check if SerializedProtoRowsMergingStateMachine.validateValueAndType"
                      + " supports all types.")
              .fail();
      }
    }
  }
}
