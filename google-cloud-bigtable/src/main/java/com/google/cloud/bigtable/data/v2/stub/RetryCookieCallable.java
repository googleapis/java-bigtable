package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.MethodDescriptor;

public class RetryCookieCallable<RequestT, ResponseT> extends UnaryCallable<RequestT, ResponseT> {

  UnaryCallable<RequestT, ResponseT> innerCallable;

  RetryCookieCallable(UnaryCallable<RequestT, ResponseT> innerCallable) {
    this.innerCallable = innerCallable;
  }

  @Override
  public ApiFuture<ResponseT> futureCall(RequestT request, ApiCallContext apiCallContext) {
    GrpcCallContext grpcCallContext = (GrpcCallContext) apiCallContext;
    grpcCallContext =
        grpcCallContext.withCallOptions(
            grpcCallContext
                .getCallOptions()
                .withOption(CallOptions.Key.create("retry-cookie"), ""));
    RetryCookieCallback callback = new RetryCookieCallback(grpcCallContext);
    ApiFuture<ResponseT> future = innerCallable.futureCall(request, grpcCallContext);
    ApiFutures.addCallback(future, callback, MoreExecutors.directExecutor());
    return future;
  }

  class RetryCookieCallback<ResponseT> implements ApiFutureCallback<ResponseT> {
    final GrpcCallContext context;

    RetryCookieCallback(GrpcCallContext context) {
      this.context = context;
    }

    @Override
    public void onFailure(Throwable throwable) {}

    @Override
    public void onSuccess(ResponseT responseT) {}
  }

  class RetryCookieInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
        MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
      ClientCall<ReqT, RespT> call = channel.newCall(methodDescriptor, callOptions);

      return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(call) {};
    }
  }
}
