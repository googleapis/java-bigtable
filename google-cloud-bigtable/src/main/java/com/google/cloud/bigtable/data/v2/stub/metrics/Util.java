/*
 * Copyright 2019 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import com.google.api.core.InternalApi;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.grpc.GrpcResponseMetadata;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.StatusCode.Code;
import com.google.bigtable.v2.AuthorizedViewName;
import com.google.bigtable.v2.CheckAndMutateRowRequest;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.ReadModifyWriteRowRequest;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ResponseParams;
import com.google.bigtable.v2.SampleRowKeysRequest;
import com.google.bigtable.v2.TableName;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.CallOptions;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.opencensus.tags.TagValue;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/** Utilities to help integrating with OpenCensus. */
@InternalApi("For internal use only")
public class Util {
  static final Metadata.Key<String> ATTEMPT_HEADER_KEY =
      Metadata.Key.of("bigtable-attempt", Metadata.ASCII_STRING_MARSHALLER);
  static final Metadata.Key<String> ATTEMPT_EPOCH_KEY =
      Metadata.Key.of("bigtable-client-attempt-epoch-usec", Metadata.ASCII_STRING_MARSHALLER);

  private static final Metadata.Key<String> SERVER_TIMING_HEADER_KEY =
      Metadata.Key.of("server-timing", Metadata.ASCII_STRING_MARSHALLER);
  private static final Pattern SERVER_TIMING_HEADER_PATTERN = Pattern.compile(".*dur=(?<dur>\\d+)");
  static final Metadata.Key<byte[]> LOCATION_METADATA_KEY =
      Metadata.Key.of("x-goog-ext-425905942-bin", Metadata.BINARY_BYTE_MARSHALLER);

  /** Convert an exception into a value that can be used to create an OpenCensus tag value. */
  static String extractStatus(@Nullable Throwable error) {
    final String statusString;

    if (error == null) {
      return StatusCode.Code.OK.toString();
    } else if (error instanceof CancellationException) {
      statusString = Status.Code.CANCELLED.toString();
    } else if (error instanceof ApiException) {
      statusString = ((ApiException) error).getStatusCode().getCode().toString();
    } else if (error instanceof StatusRuntimeException) {
      statusString = ((StatusRuntimeException) error).getStatus().getCode().toString();
    } else if (error instanceof StatusException) {
      statusString = ((StatusException) error).getStatus().getCode().toString();
    } else {
      statusString = Code.UNKNOWN.toString();
    }

    return statusString;
  }

  /**
   * Await the result of the future and convert it into a value that can be used as an OpenCensus
   * tag value.
   */
  static TagValue extractStatusFromFuture(Future<?> future) {
    Throwable error = null;

    try {
      future.get();
    } catch (InterruptedException e) {
      error = e;
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      error = e.getCause();
    } catch (RuntimeException e) {
      error = e;
    }
    return TagValue.create(extractStatus(error));
  }

  static String extractTableId(Object request) {
    String tableName = null;
    String authorizedViewName = null;
    if (request instanceof ReadRowsRequest) {
      tableName = ((ReadRowsRequest) request).getTableName();
      authorizedViewName = ((ReadRowsRequest) request).getAuthorizedViewName();
    } else if (request instanceof MutateRowsRequest) {
      tableName = ((MutateRowsRequest) request).getTableName();
      authorizedViewName = ((MutateRowsRequest) request).getAuthorizedViewName();
    } else if (request instanceof MutateRowRequest) {
      tableName = ((MutateRowRequest) request).getTableName();
      authorizedViewName = ((MutateRowRequest) request).getAuthorizedViewName();
    } else if (request instanceof SampleRowKeysRequest) {
      tableName = ((SampleRowKeysRequest) request).getTableName();
      authorizedViewName = ((SampleRowKeysRequest) request).getAuthorizedViewName();
    } else if (request instanceof CheckAndMutateRowRequest) {
      tableName = ((CheckAndMutateRowRequest) request).getTableName();
      authorizedViewName = ((CheckAndMutateRowRequest) request).getAuthorizedViewName();
    } else if (request instanceof ReadModifyWriteRowRequest) {
      tableName = ((ReadModifyWriteRowRequest) request).getTableName();
      authorizedViewName = ((ReadModifyWriteRowRequest) request).getAuthorizedViewName();
    }
    if (tableName == null && authorizedViewName == null) return "undefined";
    if (tableName.isEmpty() && authorizedViewName.isEmpty()) return "undefined";
    if (!tableName.isEmpty()) {
      return TableName.parse(tableName).getTable();
    } else {
      return AuthorizedViewName.parse(authorizedViewName).getTable();
    }
  }

  /**
   * Add attempt number and client timestamp from api call context to request headers. Attempt
   * number starts from 0.
   */
  static Map<String, List<String>> createStatsHeaders(ApiCallContext apiCallContext) {
    ImmutableMap.Builder<String, List<String>> headers = ImmutableMap.builder();
    headers.put(
        ATTEMPT_EPOCH_KEY.name(),
        Arrays.asList(String.valueOf(Instant.EPOCH.until(Instant.now(), ChronoUnit.MICROS))));
    // This should always be true
    if (apiCallContext.getTracer() instanceof BigtableTracer) {
      int attemptCount = ((BigtableTracer) apiCallContext.getTracer()).getAttempt();
      headers.put(ATTEMPT_HEADER_KEY.name(), Arrays.asList(String.valueOf(attemptCount)));
    }
    return headers.build();
  }

  private static Long getGfeLatency(@Nullable Metadata metadata) {
    if (metadata == null) {
      return null;
    }
    String serverTiming = metadata.get(SERVER_TIMING_HEADER_KEY);
    if (serverTiming == null) {
      return null;
    }
    Matcher matcher = SERVER_TIMING_HEADER_PATTERN.matcher(serverTiming);
    // this should always be true
    if (matcher.find()) {
      long latency = Long.valueOf(matcher.group("dur"));
      return latency;
    }
    return null;
  }

  private static ResponseParams getResponseParams(@Nullable Metadata metadata) {
    if (metadata == null) {
      return null;
    }
    byte[] responseParams = metadata.get(Util.LOCATION_METADATA_KEY);
    if (responseParams != null) {
      try {
        return ResponseParams.parseFrom(responseParams);
      } catch (InvalidProtocolBufferException e) {
      }
    }
    return null;
  }

  static void recordMetricsFromMetadata(
      GrpcResponseMetadata responseMetadata, BigtableTracer tracer, Throwable throwable) {
    Metadata metadata = responseMetadata.getMetadata();

    // Get the response params from the metadata. Check both headers and trailers
    // because in different environments the metadata could be returned in headers or trailers
    @Nullable ResponseParams responseParams = getResponseParams(responseMetadata.getMetadata());
    if (responseParams == null) {
      responseParams = getResponseParams(responseMetadata.getTrailingMetadata());
    }
    // Set tracer locations if response params is not null
    if (responseParams != null) {
      tracer.setLocations(responseParams.getZoneId(), responseParams.getClusterId());
    }

    // server-timing metric will be added through GrpcResponseMetadata#onHeaders(Metadata),
    // so it's not checking trailing metadata here.
    @Nullable Long latency = getGfeLatency(metadata);
    // For direct path, we won't see GFE server-timing header. However, if we received the
    // location info, we know that there isn't a connectivity issue. Set the latency to
    // 0 so gfe missing header won't get incremented.
    if (responseParams != null && latency == null) {
      latency = 0L;
    }
    // Record gfe metrics
    tracer.recordGfeMetadata(latency, throwable);
  }

  /**
   * This method bridges gRPC stream tracing to bigtable tracing by adding a {@link
   * io.grpc.ClientStreamTracer} to the callContext.
   */
  static GrpcCallContext injectBigtableStreamTracer(
      ApiCallContext context, GrpcResponseMetadata responseMetadata, BigtableTracer tracer) {
    if (context instanceof GrpcCallContext) {
      GrpcCallContext callContext = (GrpcCallContext) context;
      CallOptions callOptions = callContext.getCallOptions();
      return responseMetadata.addHandlers(
          callContext.withCallOptions(
              callOptions.withStreamTracerFactory(new BigtableGrpcStreamTracer.Factory(tracer))));
    } else {
      // context should always be an instance of GrpcCallContext. If not throw an exception
      // so we can see what class context is.
      throw new RuntimeException("Unexpected context class: " + context.getClass().getName());
    }
  }
}
