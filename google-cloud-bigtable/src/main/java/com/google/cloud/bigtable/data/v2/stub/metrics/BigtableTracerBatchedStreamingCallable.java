/*
 * Copyright 2023 Google LLC
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
import javax.annotation.Nonnull;

/**
 * This callable will do everything described in {@link BigtableTracerStreamingCallable} except that
 * it won't inject a {@link BigtableGrpcStreamTracer}. For batching calls, we only want to calculate
 * the total time client is blocked because of flow control.
 */
@InternalApi
public class BigtableTracerBatchedStreamingCallable<RequestT, ResponseT>
    extends BigtableTracerStreamingCallable<RequestT, ResponseT> {

  private ServerStreamingCallable<RequestT, ResponseT> innerCallable;

  public BigtableTracerBatchedStreamingCallable(
      @Nonnull ServerStreamingCallable<RequestT, ResponseT> innerCallable) {
    super(innerCallable);
    this.innerCallable = innerCallable;
  }

  @Override
  public void call(
      RequestT request, ResponseObserver<ResponseT> responseObserver, ApiCallContext context) {
    final GrpcResponseMetadata responseMetadata = new GrpcResponseMetadata();
    // tracer should always be an instance of bigtable tracer
    if (context.getTracer() instanceof BigtableTracer) {
      BigtableTracerResponseObserver<ResponseT> innerObserver =
          new BigtableTracerResponseObserver<ResponseT>(
              responseObserver, (BigtableTracer) context.getTracer(), responseMetadata);
      innerCallable.call(request, innerObserver, responseMetadata.addHandlers(context));
    } else {
      innerCallable.call(request, responseObserver, context);
    }
  }
}
