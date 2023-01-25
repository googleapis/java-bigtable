package com.google.cloud.bigtable.data.v2.stub.metrics;

import com.google.common.base.Stopwatch;
import io.grpc.Attributes;
import io.grpc.ClientStreamTracer;
import io.grpc.Metadata;
import java.util.concurrent.TimeUnit;

/** Records the time a request is blocked on the grpc channel */
class BigtableGrpcStreamTracer extends ClientStreamTracer {

  private final Stopwatch stopwatch = Stopwatch.createUnstarted();
  private final BigtableTracer tracer;

  public BigtableGrpcStreamTracer(BigtableTracer tracer) {
    this.tracer = tracer;
  }

  @Override
  public void streamCreated(Attributes transportAttrs, Metadata headers) {
    stopwatch.start();
  }

  @Override
  public void outboundMessageSent(int seqNo, long optionalWireSize, long optionalUncompressedSize) {
    tracer.requestBlockedOnChannel(stopwatch.elapsed(TimeUnit.MILLISECONDS));
  }

  static class Factory extends ClientStreamTracer.Factory {

    private final BigtableTracer tracer;

    Factory(BigtableTracer tracer) {
      this.tracer = tracer;
    }

    @Override
    public ClientStreamTracer newClientStreamTracer(
        ClientStreamTracer.StreamInfo info, Metadata headers) {
      return new BigtableGrpcStreamTracer(tracer);
    }
  }
}
