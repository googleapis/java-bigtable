package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.ApiCallContext;

import java.util.concurrent.ScheduledExecutorService;

public class RetryCallable<RequestT, ResponseT> implements Callable2<RequestT, ResponseT> {

    Callable2 callable;
    StreamResumptionStrategy resumptionStrategy;
    ScheduledExecutorService executor;
    RetrySettings settings;

    RetryCallable(Callable2 callable, StreamResumptionStrategy streamResumptionStrategy, ScheduledExecutorService executor, RetrySettings settings) {
        this.callable = callable;
        this.resumptionStrategy = streamResumptionStrategy;
        this.executor = executor;
        this.settings = settings;
    }

    @Override
    public void call(RequestT request, ResponseObserver2<ResponseT> observer, ApiCallContext context) {
        System.out.println("retry callable is called");
        Operation<RequestT, ResponseT> operation = new Operation<>(callable, resumptionStrategy, observer, executor, request, context, settings);
        operation.start();
    }
}