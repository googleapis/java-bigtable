/*
 * Copyright 2020 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.tracing.SpanName;
import com.google.cloud.bigtable.data.v2.stub.metrics.HeaderTracer;
import com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.ForwardingClientCallListener.SimpleForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHeaderInterceptor implements ClientInterceptor {
  public static final Metadata.Key<String> SERVER_TIMING_HEADER_KEY =
      Metadata.Key.of("server-timing", Metadata.ASCII_STRING_MARSHALLER);

  private static final Logger LOGGER = Logger.getLogger(ClientHeaderInterceptor.class.getName());
  private static final String CLIENT_NAME = "Bigtable";
  private static final Pattern GFE_HEADER_PATTERN = Pattern.compile(".*dur=(?<dur>\\d+)");
  private static final Pattern METHOD_DESCRIPTOR_PATTERN = Pattern.compile(".*/(?<op>[a-zA-Z]+)");

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      final MethodDescriptor<ReqT, RespT> method, final CallOptions callOptions, Channel channel) {
    final ClientCall<ReqT, RespT> clientCall = channel.newCall(method, callOptions);
    final SpanName spanName = processMethodName(method.getFullMethodName());
    final HeaderTracer tracer = callOptions.getOption(HeaderTracer.HEADER_TRACER_CONTEXT_KEY);
    return new SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        super.start(
            new SimpleForwardingClientCallListener<RespT>(responseListener) {
              @Override
              public void onHeaders(Metadata headers) {
                processHeader(headers, tracer, spanName);
                super.onHeaders(headers);
              }
            },
            headers);
      }
    };
  }

  private SpanName processMethodName(String method) {
    Matcher matcher = METHOD_DESCRIPTOR_PATTERN.matcher(method);
    if (matcher.find()) {
      return SpanName.of(CLIENT_NAME, matcher.group("op"));
    }
    LOGGER.warning(
        String.format("Failed to get bigtable op name. Received method descriptor: %s.", method));
    return null;
  }

  private void processHeader(Metadata headers, HeaderTracer tracer, SpanName span) {
    if (tracer == null) {
      LOGGER.warning("Couldn't find HeaderTracer in call options. Skip extracting gfe metrics");
      return;
    }
    if (headers.get(SERVER_TIMING_HEADER_KEY) != null) {
      String serverTiming = headers.get(SERVER_TIMING_HEADER_KEY);
      Matcher matcher = GFE_HEADER_PATTERN.matcher(serverTiming);
      tracer.recordHeader(RpcMeasureConstants.BIGTABLE_GFE_MISSING_COUNT, 0, span);
      if (matcher.find()) {
        long latency = Long.valueOf(matcher.group("dur"));
        tracer.recordHeader(RpcMeasureConstants.BIGTABLE_GFE_LATENCY, latency, span);
      } else {
        LOGGER.warning(
            String.format(
                "Received invalid %s header: %s, failed to add to metrics.",
                SERVER_TIMING_HEADER_KEY.name(), serverTiming));
      }
    } else {
      tracer.recordHeader(RpcMeasureConstants.BIGTABLE_GFE_MISSING_COUNT, 1l, span);
    }
  }
}
