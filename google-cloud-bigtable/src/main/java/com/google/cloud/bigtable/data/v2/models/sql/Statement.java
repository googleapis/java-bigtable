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
package com.google.cloud.bigtable.data.v2.models.sql;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.bigtable.v2.ArrayValue;
import com.google.bigtable.v2.ExecuteQueryRequest;
import com.google.bigtable.v2.Type;
import com.google.bigtable.v2.Value;
import com.google.cloud.Date;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.QueryParamUtil;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

/**
 * A SQL statement that can be executed by calling {@link
 * com.google.cloud.bigtable.data.v2.BigtableDataClient#executeQuery(Statement)}.
 *
 * <p>A statement contains a SQL string and optional parameters. A parameterized query should
 * contain placeholders in the form of {@literal @} followed by the parameter name. Parameter names
 * may consist of any combination of letters, numbers, and underscores.
 *
 * <p>Parameters can appear anywhere that a literal value is expected. The same parameter name can
 * be used more than once, for example: {@code WHERE cf["qualifier1"] = @value OR cf["qualifier2"]
 * = @value }
 *
 * <p>It is an error to execute an SQL query with placeholders for unset parameters.
 *
 * <p>Parameterized Statements are constructed using a {@link Builder} and calling
 * setTypeParam(String paramName, Type value) for the appropriate type. For example:
 *
 * <pre>{@code
 * Statement statement = Statement
 *     .newBuilder("SELECT cf[@qualifer] FROM table WHERE _key=@key")
 *     .setBytesParam("qualifier", ByteString.copyFromUtf8("test"))
 *     .setBytesParam("key", ByteString.copyFromUtf8("testKey"))
 *     .build();
 * }</pre>
 */
@BetaApi
public class Statement {

  private final String sql;
  private final Map<String, Value> params;

  private Statement(String sql, Map<String, Value> params) {
    this.sql = sql;
    this.params = params;
  }

  /** Creates a {@code Statement} with the given SQL query and no query parameters. */
  public static Statement of(String sql) {
    return newBuilder(sql).build();
  }

  /** Creates a new {@code Builder} with the given SQL query */
  public static Builder newBuilder(String sql) {
    return new Builder(sql);
  }

  public static class Builder {
    private final String sql;
    private final Map<String, Value> params;

    private Builder(String sql) {
      this.sql = sql;
      this.params = new HashMap<>();
    }

    /** Builds a {@code Statement} from the builder */
    public Statement build() {
      return new Statement(sql, ImmutableMap.copyOf(params));
    }

    /**
     * Sets a query parameter with the name {@code paramName} and the String typed value {@code
     * value}
     */
    public Builder setStringParam(String paramName, @Nullable String value) {
      params.put(paramName, stringParamOf(value));
      return this;
    }

    /**
     * Sets a query parameter with the name {@code paramName} and the Bytes typed value {@code
     * value}
     */
    public Builder setBytesParam(String paramName, @Nullable ByteString value) {
      params.put(paramName, bytesParamOf(value));
      return this;
    }

    /**
     * Sets a query parameter with the name {@code paramName} and the INT64 typed value {@code
     * value}
     */
    public Builder setLongParam(String paramName, @Nullable Long value) {
      params.put(paramName, int64ParamOf(value));
      return this;
    }

    /**
     * Sets a query parameter with the name {@code paramName} and the FLOAT32 typed value {@code
     * value}
     */
    public Builder setFloatParam(String paramName, @Nullable Float value) {
      params.put(paramName, float32ParamOf(value));
      return this;
    }

    /**
     * Sets a query parameter with the name {@code paramName} and the FLOAT64 typed value {@code
     * value}
     */
    public Builder setDoubleParam(String paramName, @Nullable Double value) {
      params.put(paramName, float64ParamOf(value));
      return this;
    }

    /**
     * Sets a query parameter with the name {@code paramName} and the BOOL typed value {@code value}
     */
    public Builder setBooleanParam(String paramName, @Nullable Boolean value) {
      params.put(paramName, booleanParamOf(value));
      return this;
    }

    /**
     * Sets a query parameter with the name {@code paramName} and the TIMESTAMP typed value {@code
     * value}
     */
    public Builder setTimestampParam(String paramName, @Nullable Instant value) {
      params.put(paramName, timestampParamOf(value));
      return this;
    }

    /**
     * Sets a query parameter with the name {@code paramName} and the DATE typed value {@code value}
     */
    public Builder setDateParam(String paramName, @Nullable Date value) {
      params.put(paramName, dateParamOf(value));
      return this;
    }

    /**
     * Sets a query parameter with the name {@code paramName} and the ARRAY typed value {@code
     * value}. The array element type is specified by {@code arrayType} and the List elements must
     * be of the corresponding Java type. Null array elements are valid.
     */
    public <T> Builder setListParam(
        String paramName, @Nullable List<T> value, SqlType.Array<T> arrayType) {
      params.put(paramName, arrayParamOf(value, arrayType));
      return this;
    }

    private static Value stringParamOf(@Nullable String value) {
      Type type = QueryParamUtil.convertToQueryParamProto(SqlType.string());
      Value.Builder builder = nullValueWithType(type);
      if (value != null) {
        builder.setStringValue(value);
      }
      return builder.build();
    }

    private static Value bytesParamOf(@Nullable ByteString value) {
      Type type = QueryParamUtil.convertToQueryParamProto(SqlType.bytes());
      Value.Builder builder = nullValueWithType(type);
      if (value != null) {
        builder.setBytesValue(value);
      }
      return builder.build();
    }

    private static Value int64ParamOf(@Nullable Long value) {
      Type type = QueryParamUtil.convertToQueryParamProto(SqlType.int64());
      Value.Builder builder = nullValueWithType(type);
      if (value != null) {
        builder.setIntValue(value);
      }
      return builder.build();
    }

    private static Value float32ParamOf(@Nullable Float value) {
      Type type = QueryParamUtil.convertToQueryParamProto(SqlType.float32());
      Value.Builder builder = nullValueWithType(type);
      if (value != null) {
        builder.setFloatValue(value);
      }
      return builder.build();
    }

    private static Value float64ParamOf(@Nullable Double value) {
      Type type = QueryParamUtil.convertToQueryParamProto(SqlType.float64());
      Value.Builder builder = nullValueWithType(type);
      if (value != null) {
        builder.setFloatValue(value);
      }
      return builder.build();
    }

    private static Value booleanParamOf(@Nullable Boolean value) {
      Type type = QueryParamUtil.convertToQueryParamProto(SqlType.bool());
      Value.Builder builder = nullValueWithType(type);
      if (value != null) {
        builder.setBoolValue(value);
      }
      return builder.build();
    }

    private static Value timestampParamOf(@Nullable Instant value) {
      Type type = QueryParamUtil.convertToQueryParamProto(SqlType.timestamp());
      Value.Builder builder = nullValueWithType(type);
      if (value != null) {
        builder.setTimestampValue(toTimestamp(value));
      }
      return builder.build();
    }

    private static Value dateParamOf(@Nullable Date value) {
      Type type = QueryParamUtil.convertToQueryParamProto(SqlType.date());
      Value.Builder builder = nullValueWithType(type);
      if (value != null) {
        builder.setDateValue(toProtoDate(value));
      }
      return builder.build();
    }

    private static <T> Value arrayParamOf(@Nullable List<T> value, SqlType.Array<T> arrayType) {
      Type type = QueryParamUtil.convertToQueryParamProto(arrayType);
      Value.Builder builder = nullValueWithType(type);
      if (value != null) {
        builder.setArrayValue(arrayValueOf(value, arrayType));
      }
      return builder.build();
    }

    private static ArrayValue arrayValueOf(List<?> value, SqlType.Array<?> arrayType) {
      ArrayValue.Builder valueBuilder = ArrayValue.newBuilder();
      for (Object element : value) {
        if (element == null) {
          valueBuilder.addValues(Value.getDefaultInstance());
          continue;
        }
        switch (arrayType.getElementType().getCode()) {
          case BYTES:
            ByteString bytesElem = (ByteString) element;
            valueBuilder.addValues(Value.newBuilder().setBytesValue(bytesElem).build());
            break;
          case STRING:
            String stringElem = (String) element;
            valueBuilder.addValues(Value.newBuilder().setStringValue(stringElem).build());
            break;
          case INT64:
            Long longElem = (Long) element;
            valueBuilder.addValues(Value.newBuilder().setIntValue(longElem).build());
            break;
          case FLOAT32:
            Float floatElem = (Float) element;
            valueBuilder.addValues(Value.newBuilder().setFloatValue(floatElem).build());
            break;
          case FLOAT64:
            Double doubleElem = (Double) element;
            valueBuilder.addValues(Value.newBuilder().setFloatValue(doubleElem).build());
            break;
          case BOOL:
            Boolean boolElem = (Boolean) element;
            valueBuilder.addValues(Value.newBuilder().setBoolValue(boolElem).build());
            break;
          case TIMESTAMP:
            Instant timestampElem = (Instant) element;
            valueBuilder.addValues(
                Value.newBuilder().setTimestampValue(toTimestamp(timestampElem)).build());
            break;
          case DATE:
            Date dateElem = (Date) element;
            valueBuilder.addValues(Value.newBuilder().setDateValue(toProtoDate(dateElem)).build());
            break;
          default:
            throw new IllegalArgumentException(
                "Unsupported query parameter Array element type: " + arrayType.getElementType());
        }
      }
      return valueBuilder.build();
    }

    private static Timestamp toTimestamp(Instant instant) {
      return Timestamp.newBuilder()
          .setSeconds(instant.getEpochSecond())
          .setNanos(instant.getNano())
          .build();
    }

    private static com.google.type.Date toProtoDate(Date date) {
      return com.google.type.Date.newBuilder()
          .setYear(date.getYear())
          .setMonth(date.getMonth())
          .setDay(date.getDayOfMonth())
          .build();
    }

    private static Value.Builder nullValueWithType(Type type) {
      return Value.newBuilder().setType(type);
    }
  }

  /**
   * Creates the request protobuf. This method is considered an internal implementation detail and
   * not meant to be used by applications.
   */
  @InternalApi("For internal use only")
  public ExecuteQueryRequest toProto(RequestContext requestContext) {
    return ExecuteQueryRequest.newBuilder()
        .setInstanceName(
            NameUtil.formatInstanceName(
                requestContext.getProjectId(), requestContext.getInstanceId()))
        .setAppProfileId(requestContext.getAppProfileId())
        .setQuery(sql)
        .putAllParams(params)
        .build();
  }
}
