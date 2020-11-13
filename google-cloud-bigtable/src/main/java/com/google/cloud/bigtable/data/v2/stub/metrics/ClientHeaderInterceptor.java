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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import com.google.api.core.BetaApi;
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

/**
 * Surface GFE server-timing metric.
 *
 * <p>This class exports the metric from server-timing header that tracks the latency between GFE
 * receives the first byte of a request and reads the first byte of the response. It also tracks the
 * number of occurrences of missing server-timing header.
 */
@BetaApi
public class ClientHeaderInterceptor implements ClientInterceptor {
  private static final Logger LOGGER = Logger.getLogger(ClientHeaderInterceptor.class.getName());

  public static final Metadata.Key<String> SERVER_TIMING_HEADER_KEY =
      Metadata.Key.of("server-timing", Metadata.ASCII_STRING_MARSHALLER);
  private static final Pattern SERVER_TIMING_HEADER_PATTERN = Pattern.compile(".*dur=(?<dur>\\d+)");

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      final MethodDescriptor<ReqT, RespT> method, final CallOptions callOptions, Channel channel) {
    final ClientCall<ReqT, RespT> clientCall = channel.newCall(method, callOptions);
    final String span = callOptions.getOption(HeaderTracer.SPAN_NAME_CONTEXT_KEY);
    final HeaderTracer tracer = callOptions.getOption(HeaderTracer.HEADER_TRACER_CONTEXT_KEY);
    return new SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        super.start(
            new SimpleForwardingClientCallListener<RespT>(responseListener) {
              @Override
              public void onHeaders(Metadata headers) {
                processHeader(headers, tracer, span);
                super.onHeaders(headers);
              }
            },
            headers);
      }
    };
  }

  private void processHeader(Metadata headers, HeaderTracer tracer, String span) {
    if (tracer == null) {
      LOGGER.warning("Couldn't find HeaderTracer in call options. Skip exporting gfe metrics");
      return;
    }
    if (headers.get(SERVER_TIMING_HEADER_KEY) != null) {
      String serverTiming = headers.get(SERVER_TIMING_HEADER_KEY);
      Matcher matcher = SERVER_TIMING_HEADER_PATTERN.matcher(serverTiming);
      tracer.record(RpcMeasureConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT, 0L, span);
      if (matcher.find()) {
        long latency = Long.valueOf(matcher.group("dur"));
        tracer.record(RpcMeasureConstants.BIGTABLE_GFE_LATENCY, latency, span);
      } else {
        LOGGER.warning(
            String.format(
                "Failed to get GFE latency from %s header: %s.",
                SERVER_TIMING_HEADER_KEY.name(), serverTiming));
      }
    } else {
      tracer.record(RpcMeasureConstants.BIGTABLE_GFE_HEADER_MISSING_COUNT, 1L, span);
    }
  }
}
