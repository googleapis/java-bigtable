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

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.bigtable.v2.ColumnMetadata;
import com.google.bigtable.v2.PartialResultSet;
import com.google.bigtable.v2.ProtoRows;
import com.google.bigtable.v2.ProtoSchema;
import com.google.bigtable.v2.Type;
import com.google.bigtable.v2.Value;
import com.google.cloud.bigtable.data.v2.internal.ProtoSqlRow;
import com.google.cloud.bigtable.data.v2.internal.SqlRow;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Used to transform a stream of {@link com.google.bigtable.v2.ProtoRowsBatch} bytes chunks into
 * {@link ProtoSqlRow}s for the given schema. Each SqlRow represents a logical row for a sql
 * response.
 *
 * <p>The intended usage of this class is:
 *
 * <ul>
 *   <li>Add results with {@link #addPartialResultSet(PartialResultSet)} until {@link
 *       #hasCompleteBatch()} is true
 *   <li>Call {@link #populateQueue(Queue)} to materialize results from the complete batch.
 *   <li>Repeat until all {@link PartialResultSet}s have been processed
 *   <li>Ensure that there is no incomplete data using {@link #isBatchInProgress()}
 * </ul>
 *
 * <p>Package-private for internal use. This class is not thread safe.
 */
@InternalApi
@BetaApi
final class ProtoRowsMergingStateMachine {
  enum State {
    /** Waiting for the first chunk of bytes for a new batch */
    AWAITING_NEW_BATCH,
    /** Waiting for the next chunk of bytes, to combine with the bytes currently being buffered. */
    AWAITING_PARTIAL_BATCH,
    /** Buffering a complete batch of rows, waiting for populateQueue to be called for the batch */
    AWAITING_BATCH_CONSUME,
  }

  private final ProtoSchema schema;
  private State state;
  private ByteString batchBuffer;
  private ProtoRows completeBatch;

  ProtoRowsMergingStateMachine(ProtoSchema schema) {
    validateSchema(schema);
    this.schema = schema;
    state = State.AWAITING_NEW_BATCH;
    batchBuffer = ByteString.empty();
  }

  /**
   * Adds the bytes from the given PartialResultSet to the current buffer. If a resume token is
   * present, attempts to parse the bytes to the underlying protobuf row format
   */
  void addPartialResultSet(PartialResultSet results) {
    Preconditions.checkState(
        state != State.AWAITING_BATCH_CONSUME,
        "Attempting to add partial result set to state machine in state AWAITING_BATCH_CONSUME");
    // ByteString has an efficient concat which generally involves no copying
    batchBuffer = batchBuffer.concat(results.getProtoRowsBatch().getBatchData());
    state = State.AWAITING_PARTIAL_BATCH;
    if (results.getResumeToken().isEmpty()) {
      return;
    }
    // A resume token means the batch is complete and safe to yield
    // We can receive resume tokens with no new data. In this case we yield an empty batch.
    if (batchBuffer.isEmpty()) {
      completeBatch = ProtoRows.getDefaultInstance();
    } else {
      try {
        completeBatch = ProtoRows.parseFrom(batchBuffer);
      } catch (InvalidProtocolBufferException e) {
        throw new InternalError("Unexpected exception parsing response protobuf", e);
      }
    }
    // Empty buffers can benefit from resetting because ByteString.concat builds a rope
    batchBuffer = ByteString.empty();
    state = State.AWAITING_BATCH_CONSUME;
  }

  /** Returns true if there is a complete batch buffered, false otherwise */
  boolean hasCompleteBatch() {
    return state == State.AWAITING_BATCH_CONSUME;
  }

  /** Returns true if there is a partial or complete batch buffered, false otherwise */
  boolean isBatchInProgress() {
    return hasCompleteBatch() || state == State.AWAITING_PARTIAL_BATCH;
  }

  /**
   * Populates the given queue with the complete batch of rows
   *
   * @throws IllegalStateException if there is not a complete batch
   */
  void populateQueue(Queue<SqlRow> queue) {
    Preconditions.checkState(
        state == State.AWAITING_BATCH_CONSUME,
        "Attempting to populate Queue from state machine without completed batch");
    Iterator<Value> valuesIterator = completeBatch.getValuesList().iterator();
    while (valuesIterator.hasNext()) {
      ImmutableList.Builder<Value> rowDataBuilder = ImmutableList.builder();
      for (ColumnMetadata c : schema.getColumnsList()) {
        Preconditions.checkState(
            valuesIterator.hasNext(), "Incomplete row received with first missing column: %s", c);
        Value v = valuesIterator.next();
        validateValueAndType(c.getType(), v);
        rowDataBuilder.add(v);
      }
      queue.add(ProtoSqlRow.create(schema.getColumnsList(), rowDataBuilder.build()));
    }
    // reset the batch to be empty
    completeBatch = ProtoRows.getDefaultInstance();
    state = State.AWAITING_NEW_BATCH;
  }

  private static void validateSchema(ProtoSchema schema) {
    List<ColumnMetadata> columns = schema.getColumnsList();
    Preconditions.checkState(!columns.isEmpty(), "columns cannot be empty");
    for (ColumnMetadata column : columns) {
      Preconditions.checkState(
          column.getType().getKindCase() != Type.KindCase.KIND_NOT_SET,
          "Column type cannot be empty");
    }
  }

  @InternalApi("VisibleForTestingOnly")
  static void validateValueAndType(Type type, Value value) {
    // Null is represented as a value with none of the kind fields set
    if (value.getKindCase() == Value.KindCase.KIND_NOT_SET) {
      return;
    }
    switch (type.getKindCase()) {
        // Primitive types
      case STRING_TYPE:
        checkExpectedKind(value, Value.KindCase.STRING_VALUE, Type.KindCase.STRING_TYPE);
        break;
      case BYTES_TYPE:
        checkExpectedKind(value, Value.KindCase.BYTES_VALUE, Type.KindCase.BYTES_TYPE);
        break;
      case INT64_TYPE:
        checkExpectedKind(value, Value.KindCase.INT_VALUE, Type.KindCase.INT64_TYPE);
        break;
      case FLOAT32_TYPE:
        checkExpectedKind(value, Value.KindCase.FLOAT_VALUE, Type.KindCase.FLOAT32_TYPE);
        break;
      case FLOAT64_TYPE:
        checkExpectedKind(value, Value.KindCase.FLOAT_VALUE, Type.KindCase.FLOAT64_TYPE);
        break;
      case BOOL_TYPE:
        checkExpectedKind(value, Value.KindCase.BOOL_VALUE, Type.KindCase.BOOL_TYPE);
        break;
      case TIMESTAMP_TYPE:
        checkExpectedKind(value, Value.KindCase.TIMESTAMP_VALUE, Type.KindCase.TIMESTAMP_TYPE);
        break;
      case DATE_TYPE:
        checkExpectedKind(value, Value.KindCase.DATE_VALUE, Type.KindCase.DATE_TYPE);
        break;
        // Complex types
      case ARRAY_TYPE:
        checkExpectedKind(value, Value.KindCase.ARRAY_VALUE, Type.KindCase.ARRAY_TYPE);
        for (Value element : value.getArrayValue().getValuesList()) {
          validateValueAndType(type.getArrayType().getElementType(), element);
        }
        break;
      case STRUCT_TYPE:
        checkExpectedKind(value, Value.KindCase.ARRAY_VALUE, Type.KindCase.STRUCT_TYPE);
        List<Value> fieldValues = value.getArrayValue().getValuesList();
        List<Type.Struct.Field> fieldTypes = type.getStructType().getFieldsList();
        for (int i = 0; i < fieldValues.size(); i++) {
          validateValueAndType(fieldTypes.get(i).getType(), fieldValues.get(i));
        }
        break;
      case MAP_TYPE:
        checkExpectedKind(value, Value.KindCase.ARRAY_VALUE, Type.KindCase.MAP_TYPE);
        for (Value mapElement : value.getArrayValue().getValuesList()) {
          Preconditions.checkState(
              mapElement.getArrayValue().getValuesCount() == 2,
              "Map elements must have exactly 2 elementss");
          validateValueAndType(
              type.getMapType().getKeyType(), mapElement.getArrayValue().getValuesList().get(0));
          validateValueAndType(
              type.getMapType().getValueType(), mapElement.getArrayValue().getValuesList().get(1));
        }
        break;
      case AGGREGATE_TYPE:
        throw new IllegalStateException("Aggregate type is not supported");
      case KIND_NOT_SET:
        throw new IllegalStateException("Column type cannot be empty");
      default:
        // Fail open for types the client doesn't have a validation case for yet
    }
  }

  private static void checkExpectedKind(
      Value value, Value.KindCase expectedKind, Type.KindCase currentColumnType) {
    Preconditions.checkState(
        value.getKindCase() == expectedKind,
        "Value kind must be %s for columns of type: %s",
        expectedKind.name(),
        currentColumnType.name());
  }
}
