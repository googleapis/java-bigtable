package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallSettings;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import com.google.common.util.concurrent.MoreExecutors;
import io.grpc.Deadline;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StreamingOperation<RequestT, ResponseT> {

    private final ScheduledExecutorService executor;
    private final StreamResumptionStrategy<RequestT, ResponseT> resumptionStrategy;
    private final ServerStreamingCallable<RequestT, ResponseT> callable;
    private final RetrySettings retrySettings;
    private final Deadline deadline;
    private final AtomicInteger attemptNumber = new AtomicInteger(0);
    private StreamingAttemptFactory<RequestT, ResponseT> attemptFactory;

    StreamingOperation(StreamResumptionStrategy<RequestT, ResponseT> resumptionStrategy,
                       ScheduledExecutorService executor,
                       ServerStreamingCallSettings<RequestT, ResponseT> callSettings,
                       ServerStreamingCallable<RequestT, ResponseT> callable) {
        this.resumptionStrategy = resumptionStrategy;
        this.executor = executor;
        this.retrySettings = callSettings.getRetrySettings();
        this.callable = callable;
        this.deadline = Deadline.after(
                callSettings.getRetrySettings().getTotalTimeoutDuration().toMillis(), TimeUnit.MILLISECONDS);

    }

    public void startOperation(RequestT request, ResponseObserver<ResponseT> observer, ApiCallContext context) {
        /**
         * 1. create a new attempt
         *     new request
         *     new response observer
         * 2. attempt succeeds, notify outer observer
         * 3. attempt fails, check if it can be retried
         */
        this.attemptFactory = new StreamingAttemptFactory<RequestT, ResponseT>(callable, request, resumptionStrategy);

        retry(attemptFactory, observer, context);
    }

    public void retry(StreamingAttemptFactory<RequestT, ResponseT> attemptFactory, ResponseObserver<ResponseT> observer, ApiCallContext context) {
        SettableApiFuture<Void> future = attemptFactory.sendAttempt(observer, context);

        ApiFutures.addCallback(future, new ApiFutureCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                if (shouldRetry(throwable)) {
                    executor.schedule(() -> retry(attemptFactory, observer, context.withTimeoutDuration(Duration.ofMillis(getTimeout()))), getRetryDelay(), TimeUnit.MILLISECONDS);
                } else {
                    observer.onError(throwable);
                }
            }

            @Override
            public void onSuccess(Void unused) {
                observer.onComplete();
            }
        }, MoreExecutors.directExecutor());
    }

    boolean shouldRetry(Throwable t) {
        if (deadline.isExpired()) {
            return false;
        }
        int maxAttempt = retrySettings.getMaxAttempts();
        if (maxAttempt != 0 && attemptNumber.get() >= maxAttempt) {
            return false;
        }
        if (t instanceof ApiException) {
            return ((ApiException) t).isRetryable();
        }
        return false;
    }

    long getRetryDelay() {
        long maxRetryDelay = retrySettings.getMaxRetryDelayDuration().toMillis();
        long initialRetryDelay = retrySettings.getInitialRetryDelayDuration().toMillis();
        double multiplier = retrySettings.getRetryDelayMultiplier();

        long nextDelay =  (long) Math.min(initialRetryDelay * Math.pow(multiplier, attemptNumber.get()), maxRetryDelay);
        System.out.println("retry attempt " + attemptNumber.get() + " delay " + nextDelay);
        return ThreadLocalRandom.current().nextLong(nextDelay);
    }

    long getTimeout() {
        long rpcTimeout = retrySettings.getInitialRpcTimeoutDuration().toMillis();
        long timeout = Math.min(rpcTimeout, deadline.timeRemaining(TimeUnit.MILLISECONDS));

        System.out.println("retry attempt " + attemptNumber.get() + " timeout " + timeout);
        return timeout;
    }
}
