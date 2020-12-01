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

import com.google.api.core.InternalApi;
import com.google.api.gax.grpc.GrpcResponseMetadata;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import io.grpc.Metadata;
import javax.annotation.Nonnull;

/**
 * Record GFE metrics.
 *
 * <p>This class is considered an internal implementation detail and not meant to be used by
 * applications.
 */
@InternalApi
public class HeaderTracerStreamingCallable<RequestT, ResponseT>
    extends ServerStreamingCallable<RequestT, ResponseT> {

  private ServerStreamingCallable<RequestT, ResponseT> innerCallable;
  private HeaderTracer headerTracer;
  private String spanName;

  public HeaderTracerStreamingCallable(
      @Nonnull ServerStreamingCallable<RequestT, ResponseT> callable,
      @Nonnull HeaderTracer headerTracer,
      @Nonnull String spanName) {
    this.innerCallable = callable;
    this.headerTracer = headerTracer;
    this.spanName = spanName;
  }

  @Override
  public void call(
      RequestT request, ResponseObserver<ResponseT> responseObserver, ApiCallContext context) {
    final GrpcResponseMetadata responseMetadata = new GrpcResponseMetadata();
    HeaderTracerResponseObserver<ResponseT> innerObserver =
        new HeaderTracerResponseObserver<>(
            responseObserver, headerTracer, responseMetadata, spanName);
    innerCallable.call(request, innerObserver, responseMetadata.addHandlers(context));
  }

  private class HeaderTracerResponseObserver<ResponseT> implements ResponseObserver<ResponseT> {

    private ResponseObserver<ResponseT> outerObserver;
    private HeaderTracer headerTracer;
    private GrpcResponseMetadata responseMetadata;
    private String spanName;

    HeaderTracerResponseObserver(
        ResponseObserver<ResponseT> observer,
        HeaderTracer headerTracer,
        GrpcResponseMetadata metadata,
        String spanName) {
      this.outerObserver = observer;
      this.headerTracer = headerTracer;
      this.responseMetadata = metadata;
      this.spanName = spanName;
    }

    @Override
    public void onStart(final StreamController controller) {
      outerObserver.onStart(controller);
    }

    @Override
    public void onResponse(ResponseT response) {
      outerObserver.onResponse(response);
    }

    @Override
    public void onError(Throwable t) {
      Metadata metadata = responseMetadata.getMetadata();
      if (metadata != null) {
        headerTracer.recordGfeMetrics(metadata, spanName);
      }
      outerObserver.onError(t);
    }

    @Override
    public void onComplete() {
      Metadata metadata = responseMetadata.getMetadata();
      if (metadata != null) {
        headerTracer.recordGfeMetrics(metadata, spanName);
      }
      outerObserver.onComplete();
    }
  }
}
