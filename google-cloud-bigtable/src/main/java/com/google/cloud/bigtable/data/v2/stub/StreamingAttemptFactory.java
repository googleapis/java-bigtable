package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.SettableApiFuture;
import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;

public class StreamingAttemptFactory<RequestT, ResponseT> {

    private RequestT request;
    private StreamResumptionStrategy<RequestT, ResponseT> resumptionStrategy;
    private ServerStreamingCallable<RequestT, ResponseT> callable;

    StreamingAttemptFactory(ServerStreamingCallable<RequestT, ResponseT> callable,
                             RequestT request,
                             StreamResumptionStrategy<RequestT, ResponseT> resumptionStrategy) {
        this.request = request;
        this.resumptionStrategy = resumptionStrategy;
        this.callable = callable;
    }

    SettableApiFuture<Void> sendAttempt(ResponseObserver<ResponseT> observer, ApiCallContext context) {
        StreamingAttemptCallable<RequestT, ResponseT> attemptCallable = new StreamingAttemptCallable<>(callable);

        request = resumptionStrategy.getResumeRequest(request);

        SettableApiFuture<Void> future = SettableApiFuture.create();
        attemptCallable.call(request, new ResponseObserver<ResponseT>() {
            @Override
            public void onStart(StreamController streamController) {

            }

            @Override
            public void onResponse(ResponseT responseT) {
                resumptionStrategy.processResponse(responseT);
                observer.onResponse(responseT);
            }

            @Override
            public void onError(Throwable throwable) {
                future.setException(throwable);
            }

            @Override
            public void onComplete() {
                future.set(null);
            }
        }, context);

        return future;
    }
}
