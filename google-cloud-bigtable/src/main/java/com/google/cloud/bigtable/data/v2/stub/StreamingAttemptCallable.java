package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;

public class StreamingAttemptCallable<RequestT, ResponseT> extends ServerStreamingCallable<RequestT, ResponseT> {

    private ServerStreamingCallable<RequestT, ResponseT> innerCallable;

    public StreamingAttemptCallable(ServerStreamingCallable<RequestT, ResponseT> callable) {
        this.innerCallable = callable;
    }

    @Override
    public void call(RequestT requestT, ResponseObserver<ResponseT> responseObserver, ApiCallContext apiCallContext) {
        innerCallable.call(requestT, responseObserver, apiCallContext);
    }
}
