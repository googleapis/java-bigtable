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
package com.google.cloud.bigtable.testproxy;

import com.google.bigtable.v2.Value;
import com.google.bigtable.v2.Value.KindCase;
import com.google.cloud.Date;
import com.google.cloud.bigtable.data.v2.models.sql.BoundStatement;
import com.google.cloud.bigtable.data.v2.models.sql.PreparedStatement;
import com.google.cloud.bigtable.data.v2.models.sql.SqlType;
import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoundStatementDeserializer {

  static BoundStatement toBoundStatement(
      PreparedStatement preparedStatement, ExecuteQueryRequest request) {
    BoundStatement.Builder boundStatementBuilder = preparedStatement.bind();
    for (Map.Entry<String, Value> paramEntry : request.getRequest().getParamsMap().entrySet()) {
      String name = paramEntry.getKey();
      Value value = paramEntry.getValue();
      switch (value.getType().getKindCase()) {
        case BYTES_TYPE:
          if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
            boundStatementBuilder.setBytesParam(name, null);
          } else if (value.getKindCase().equals(KindCase.BYTES_VALUE)) {
            boundStatementBuilder.setBytesParam(name, value.getBytesValue());
          } else {
            throw new IllegalArgumentException("Unexpected bytes value: " + value);
          }
          break;
        case STRING_TYPE:
          if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
            boundStatementBuilder.setStringParam(name, null);
          } else if (value.getKindCase().equals(KindCase.STRING_VALUE)) {
            boundStatementBuilder.setStringParam(name, value.getStringValue());
          } else {
            throw new IllegalArgumentException("Malformed string value: " + value);
          }
          break;
        case INT64_TYPE:
          if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
            boundStatementBuilder.setLongParam(name, null);
          } else if (value.getKindCase().equals(KindCase.INT_VALUE)) {
            boundStatementBuilder.setLongParam(name, value.getIntValue());
          } else {
            throw new IllegalArgumentException("Malformed int64 value: " + value);
          }
          break;
        case FLOAT32_TYPE:
          if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
            boundStatementBuilder.setFloatParam(name, null);
          } else if (value.getKindCase().equals(KindCase.FLOAT_VALUE)) {
            boundStatementBuilder.setFloatParam(name, (float) value.getFloatValue());
          } else {
            throw new IllegalArgumentException("Malformed float32 value: " + value);
          }
          break;
        case FLOAT64_TYPE:
          if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
            boundStatementBuilder.setDoubleParam(name, null);
          } else if (value.getKindCase().equals(KindCase.FLOAT_VALUE)) {
            boundStatementBuilder.setDoubleParam(name, value.getFloatValue());
          } else {
            throw new IllegalArgumentException("Malformed float64 value: " + value);
          }
          break;
        case BOOL_TYPE:
          if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
            boundStatementBuilder.setBooleanParam(name, null);
          } else if (value.getKindCase().equals(KindCase.BOOL_VALUE)) {
            boundStatementBuilder.setBooleanParam(name, value.getBoolValue());
          } else {
            throw new IllegalArgumentException("Malformed boolean value: " + value);
          }
          break;
        case TIMESTAMP_TYPE:
          if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
            boundStatementBuilder.setTimestampParam(name, null);
          } else if (value.getKindCase().equals(KindCase.TIMESTAMP_VALUE)) {
            Timestamp ts = value.getTimestampValue();
            boundStatementBuilder.setTimestampParam(name, toInstant(ts));
          } else {
            throw new IllegalArgumentException("Malformed timestamp value: " + value);
          }
          break;
        case DATE_TYPE:
          if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
            boundStatementBuilder.setDateParam(name, null);
          } else if (value.getKindCase().equals(KindCase.DATE_VALUE)) {
            com.google.type.Date protoDate = value.getDateValue();
            boundStatementBuilder.setDateParam(name, fromProto(protoDate));
          } else {
            throw new IllegalArgumentException("Malformed boolean value: " + value);
          }
          break;
        case ARRAY_TYPE:
          SqlType.Array sqlType = (SqlType.Array) SqlType.fromProto(value.getType());
          if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
            boundStatementBuilder.setListParam(name, null, sqlType);
          } else if (value.getKindCase().equals(KindCase.ARRAY_VALUE)) {
            List<Object> array = new ArrayList<>();
            for (Value elem : value.getArrayValue().getValuesList()) {
              array.add(decodeArrayElement(elem, sqlType.getElementType()));
            }
            boundStatementBuilder.setListParam(name, array, sqlType);
          } else {
            throw new IllegalArgumentException("Malformed array value: " + value);
          }
          break;
        default:
          throw new IllegalArgumentException("Unexpected query param type in param: " + value);
      }
    }
    return boundStatementBuilder.build();
  }

  static Object decodeArrayElement(Value value, SqlType<?> elemType) {
    if (value.getKindCase().equals(KindCase.KIND_NOT_SET)) {
      return null;
    }
    switch (elemType.getCode()) {
      case BYTES:
        return value.getBytesValue();
      case STRING:
        return value.getStringValue();
      case INT64:
        return value.getIntValue();
      case FLOAT64:
        return value.getFloatValue();
      case FLOAT32:
        // cast to float so we produce List<Float>, etc
        return (float) value.getFloatValue();
      case BOOL:
        return value.getBoolValue();
      case TIMESTAMP:
        return toInstant(value.getTimestampValue());
      case DATE:
        return fromProto(value.getDateValue());
      default:
        // We should have already thrown an exception in the SqlRowMerger
        throw new IllegalStateException("Unsupported array query param element type: " + elemType);
    }
  }

  private static Instant toInstant(Timestamp timestamp) {
    return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
  }

  private static Date fromProto(com.google.type.Date proto) {
    return Date.fromYearMonthDay(proto.getYear(), proto.getMonth(), proto.getDay());
  }
}
