package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallSettings;
import com.google.api.gax.rpc.ServerStreamingCallable;

import java.util.concurrent.ScheduledExecutorService;

public class StreamingRetryCallable<RequestT, ResponseT> extends ServerStreamingCallable<RequestT, ResponseT> {

    private final StreamingOperation<RequestT, ResponseT> operation;

    StreamingRetryCallable(ServerStreamingCallable<RequestT, ResponseT> innerCallable,
                           ScheduledExecutorService executor,
                           ServerStreamingCallSettings<RequestT, ResponseT> settings,
                           StreamResumptionStrategy<RequestT, ResponseT> resumptionStrategy) {
        this.operation = new StreamingOperation<>(resumptionStrategy, executor, settings, innerCallable);
    }

    @Override
    public void call(RequestT request, ResponseObserver<ResponseT> responseObserver, ApiCallContext apiCallContext) {
        operation.startOperation(request, responseObserver, apiCallContext);
    }
}
