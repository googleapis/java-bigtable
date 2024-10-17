 package com.google.cloud.bigtable.data.v2.stub.opt;

 import com.google.api.gax.rpc.ApiCallContext;
 import com.google.api.gax.rpc.ResponseObserver;
 import com.google.api.gax.rpc.ServerStreamingCallable;
 import com.google.api.gax.rpc.StreamController;
 import com.google.api.gax.tracing.ApiTracerFactory;
 import com.google.cloud.bigtable.data.v2.stub.metrics.BigtableTracer;

 public class UnaryOverStreamingTracerModCallable<ReqT, RespT>
    extends ServerStreamingCallable<ReqT, RespT> {
  private final ServerStreamingCallable<ReqT, RespT> inner;

  public UnaryOverStreamingTracerModCallable(ServerStreamingCallable<ReqT, RespT> inner) {
    this.inner = inner;
  }

  @Override
  public void call(ReqT request, ResponseObserver<RespT> responseObserver, ApiCallContext context)
 {
    BigtableTracer tracer = (BigtableTracer) context.getTracer();
    tracer.overrideOperationType(ApiTracerFactory.OperationType.Unary);
    inner.call(request, new TracerModObserver<>(responseObserver, tracer), context);
  }

  static class TracerModObserver<RespT> implements ResponseObserver<RespT> {
    private final ResponseObserver<RespT> outer;
    private final BigtableTracer tracer;

    public TracerModObserver(ResponseObserver<RespT> outer, BigtableTracer tracer) {
      this.outer = outer;
      this.tracer = tracer;
    }

    @Override
    public void onStart(StreamController controller) {
      outer.onStart(controller);
    }

    @Override
    public void onResponse(RespT response) {
      outer.onResponse(response);
      tracer.operationFinishedEarly();
    }

    @Override
    public void onError(Throwable t) {
      outer.onError(t);
    }

    @Override
    public void onComplete() {
      outer.onComplete();
    }
  }
 }
