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
import com.google.bigtable.v2.ResponseParams;
import com.google.cloud.bigtable.data.v2.stub.SafeResponseObserver;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.Metadata;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;

/**
 * This callable will
 * <li>-Inject a {@link GrpcResponseMetadata} to access the headers returned by gRPC methods upon
 *     completion. The {@link BigtableTracer} will process metrics that were injected in the
 *     header/trailer and publish them to OpenCensus. If {@link GrpcResponseMetadata#getMetadata()}
 *     returned null, it probably means that the request has never reached GFE, and it'll increment
 *     the gfe_header_missing_counter in this case.
 * <li>-This class will also access trailers from {@link GrpcResponseMetadata} to record zone and
 *     cluster ids.
 * <li>-Call {@link BigtableTracer#onRequest(int)} to record the request events in a stream.
 * <li>This class is considered an internal implementation detail and not meant to be used by
 *     applications.
 */
@InternalApi
public class BigtableTracerStreamingCallable<RequestT, ResponseT>
    extends ServerStreamingCallable<RequestT, ResponseT> {

  private final ServerStreamingCallable<RequestT, ResponseT> innerCallable;

  public BigtableTracerStreamingCallable(
      @Nonnull ServerStreamingCallable<RequestT, ResponseT> callable) {
    this.innerCallable = Preconditions.checkNotNull(callable, "Inner callable must be set");
  }

  @Override
  public void call(
      RequestT request, ResponseObserver<ResponseT> responseObserver, ApiCallContext context) {
    final GrpcResponseMetadata responseMetadata = new GrpcResponseMetadata();
    // tracer should always be an instance of bigtable tracer
    if (context.getTracer() instanceof BigtableTracer) {
      BigtableTracerResponseObserver<ResponseT> innerObserver =
          new BigtableTracerResponseObserver<>(
              responseObserver, (BigtableTracer) context.getTracer(), responseMetadata);
      innerCallable.call(request, innerObserver, responseMetadata.addHandlers(context));
    } else {
      innerCallable.call(request, responseObserver, context);
    }
  }

  private class BigtableTracerResponseObserver<ResponseT> extends SafeResponseObserver<ResponseT> {

    private final BigtableTracer tracer;
    private final ResponseObserver<ResponseT> outerObserver;
    private final GrpcResponseMetadata responseMetadata;

    BigtableTracerResponseObserver(
        ResponseObserver<ResponseT> observer,
        BigtableTracer tracer,
        GrpcResponseMetadata metadata) {
      super(observer);

      this.tracer = tracer;
      this.outerObserver = observer;
      this.responseMetadata = metadata;
    }

    @Override
    protected void onStartImpl(final StreamController controller) {
      TracedStreamController tracedController = new TracedStreamController(controller, tracer);
      outerObserver.onStart(tracedController);
    }

    @Override
    protected void onResponseImpl(ResponseT response) {
      Stopwatch stopwatch = Stopwatch.createStarted();
      outerObserver.onResponse(response);
      tracer.afterResponse(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Override
    protected void onErrorImpl(Throwable t) {
      // server-timing metric will be added through GrpcResponseMetadata#onHeaders(Metadata),
      // so it's not checking trailing metadata here.
      Metadata metadata = responseMetadata.getMetadata();
      Long latency = Util.getGfeLatency(metadata);
      tracer.recordGfeMetadata(latency, t);
      try {
        // Check both headers and trailers because in different environments the metadata
        // could be returned in headers or trailers
        if (metadata != null) {
          byte[] trailers = metadata.get(Util.METADATA_KEY);
          if (trailers == null) {
            Metadata trailingMetadata = responseMetadata.getTrailingMetadata();
            if (trailingMetadata != null) {
              trailers = trailingMetadata.get(Util.METADATA_KEY);
            }
          }
          // If the response is terminated abnormally and we didn't get location information in
          // trailers or headers, skip setting the locations
          if (trailers != null) {
            ResponseParams decodedTrailers = ResponseParams.parseFrom(trailers);
            tracer.setLocations(decodedTrailers.getZoneId(), decodedTrailers.getClusterId());
          }
        }
      } catch (InvalidProtocolBufferException e) {
        t.addSuppressed(t);
      }

      outerObserver.onError(t);
    }

    @Override
    protected void onCompleteImpl() {
      Metadata metadata = responseMetadata.getMetadata();
      Long latency = Util.getGfeLatency(metadata);
      tracer.recordGfeMetadata(latency, null);
      try {
        // Check both headers and trailers because in different environments the metadata
        // could be returned in headers or trailers
        if (metadata != null) {
          byte[] trailers = metadata.get(Util.METADATA_KEY);
          if (trailers == null) {
            Metadata trailingMetadata = responseMetadata.getTrailingMetadata();
            if (trailingMetadata != null) {
              trailers = trailingMetadata.get(Util.METADATA_KEY);
            }
          }
          // If the response is terminated abnormally and we didn't get location information in
          // trailers or headers, skip setting the locations
          if (trailers != null) {
            ResponseParams decodedTrailers = ResponseParams.parseFrom(trailers);
            tracer.setLocations(decodedTrailers.getZoneId(), decodedTrailers.getClusterId());
          }
        }
      } catch (InvalidProtocolBufferException e) {
        // InvalidProtocolBufferException will only throw if something changed on
        // the server side. Location info won't be populated as a result. Ignore
        // this error and don't bubble it up to user.
      }

      outerObserver.onComplete();
    }
  }

  private class TracedStreamController implements StreamController {
    private final StreamController innerController;
    private final BigtableTracer tracer;

    TracedStreamController(StreamController innerController, BigtableTracer tracer) {
      this.innerController = innerController;
      this.tracer = tracer;
    }

    @Override
    public void cancel() {
      innerController.cancel();
    }

    @Override
    public void disableAutoInboundFlowControl() {
      tracer.disableFlowControl();
      innerController.disableAutoInboundFlowControl();
    }

    @Override
    public void request(int i) {
      tracer.onRequest(i);
      innerController.request(i);
    }
  }
}
