 package com.google.cloud.bigtable.data.v2.stub.opt;

 import com.google.api.core.AbstractApiFuture;
 import com.google.api.core.ApiFuture;
 import com.google.api.gax.rpc.ApiCallContext;
 import com.google.api.gax.rpc.ResponseObserver;
 import com.google.api.gax.rpc.ServerStreamingCallable;
 import com.google.api.gax.rpc.StreamController;
 import com.google.api.gax.rpc.UnaryCallable;

 public class UnaryOverStreamingCallable<ReqT, RespT> extends UnaryCallable<ReqT, RespT> {
  private final ServerStreamingCallable<ReqT, RespT> inner;

  public UnaryOverStreamingCallable(ServerStreamingCallable<ReqT, RespT> inner) {
    this.inner = inner;
  }

  @Override
  public ApiFuture<RespT> futureCall(ReqT request, ApiCallContext context) {
    UnaryFuture<RespT> future = new UnaryFuture<>();
    inner.call(request, future.observer, context);
    return future;
  }

  private static class UnaryFuture<RespT> extends AbstractApiFuture<RespT> {
    private StreamController controller;

    private final ResponseObserver<RespT> observer =
        new ResponseObserver<RespT>() {
          @Override
          public void onStart(StreamController streamController) {
            UnaryFuture.this.controller = streamController;
          }

          @Override
          public void onResponse(RespT response) {
            UnaryFuture.this.set(response);
          }

          @Override
          public void onError(Throwable throwable) {
            UnaryFuture.this.setException(throwable);
          }

          @Override
          public void onComplete() {
            UnaryFuture.this.set(null);
          }
        };

    @Override
    protected void interruptTask() {
      controller.cancel();
    }
  }
 }
