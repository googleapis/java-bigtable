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

import com.google.api.core.ApiFuture;
import com.google.api.core.InternalApi;
import com.google.api.gax.grpc.GrpcResponseMetadata;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.Metadata;

/**
 * Record GFE metrics.
 *
 * <p>This class is considered an internal implementation detail and not meant to be used by
 * applications.
 */
@InternalApi
public class HeaderTracerUnaryCallable<RequestT, ResponseT>
    extends UnaryCallable<RequestT, ResponseT> {

  private UnaryCallable<RequestT, ResponseT> innerCallable;
  private HeaderTracer tracer;
  private String spanName;

  public HeaderTracerUnaryCallable(
      UnaryCallable<RequestT, ResponseT> callable, HeaderTracer tracer, String spanName) {
    this.innerCallable = callable;
    this.tracer = tracer;
    this.spanName = spanName;
  }

  @Override
  public ApiFuture futureCall(RequestT request, ApiCallContext context) {
    final GrpcResponseMetadata responseMetadata = new GrpcResponseMetadata();
    ApiFuture<ResponseT> future =
        innerCallable.futureCall(request, responseMetadata.addHandlers(context));
    future.addListener(
        new Runnable() {
          @Override
          public void run() {
            Metadata metadata = responseMetadata.getMetadata();
            if (metadata != null) {
              tracer.recordGfeMetrics(metadata, spanName);
            }
          }
        },
        MoreExecutors.directExecutor());
    return future;
  }
}
