package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.cloud.bigtable.data.v2.stub.metrics.BigtableTracer;
import io.grpc.Deadline;
import io.grpc.Status;

import java.time.Duration;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Operation<RequestT, ResponseT> {

    private final StreamResumptionStrategy<RequestT, ResponseT> resumptionStrategy;
    private StateListener operationState;
    private final ResponseObserver2<ResponseT> outerObserver;
    private final Callable2<RequestT, ResponseT> callable;
    private final ScheduledExecutorService executor;
    private RequestT request;
    private ApiCallContext context;
    private final RetrySettings settings;
    private Deadline deadline;
    private final AtomicInteger attempt;

    Operation(Callable2<RequestT, ResponseT> callable, StreamResumptionStrategy<RequestT, ResponseT> resumptionStrategy, ResponseObserver2<ResponseT> outerObserver,
              ScheduledExecutorService executor, RequestT request, ApiCallContext context, RetrySettings settings) {
        // TODO future debuggability
        // TODO for example: ring buffer to buffer the last few steps
        this.callable = callable;
        this.resumptionStrategy = resumptionStrategy;
        this.outerObserver = outerObserver;
        this.executor = executor;
        this.operationState = new Idle(this, outerObserver);
        this.request = request;
        this.settings = settings;
        this.context = context;
        this.attempt = new AtomicInteger(0);
    }

    void start() {
        outerObserver.onStart(new StreamController2() {
            @Override
            public void cancel(String reason) {
                onCancel(reason);
            }

            @Override
            public void onReady() {
                deadline = Deadline.after(settings.getTotalTimeoutDuration().toMillis(), TimeUnit.MILLISECONDS);
                if (context.getTracer() instanceof BigtableTracer) {
                    ((BigtableTracer) context.getTracer()).attemptStarted(attempt.get());
                }

                operationState.onReady();
            }
        });
    }

    void onCancel(String reason) {
        operationState.onCancel(reason);
    }

    public void onStateChange(StateListener state) {
        this.operationState = state;
    }

    long getRetryDelay() {
        long maxRetryDelay = settings.getMaxRetryDelayDuration().toMillis();
        long initialRetryDelay = settings.getInitialRetryDelayDuration().toMillis();
        double multiplier = settings.getRetryDelayMultiplier();

        long nextDelay =  (long) Math.min(initialRetryDelay * Math.pow(multiplier, attempt.get()), maxRetryDelay);
        return ThreadLocalRandom.current().nextLong(nextDelay);
    }

    long getTimeout() {
        long rpcTimeout = settings.getInitialRpcTimeoutDuration().toMillis();

        return Math.min(rpcTimeout, deadline.timeRemaining(TimeUnit.MILLISECONDS));
    }

    abstract class StateListener {

        protected Operation<RequestT, ResponseT> operation;
        protected ResponseObserver2<ResponseT> outerObserver;

        StateListener(Operation<RequestT, ResponseT> operation, ResponseObserver2<ResponseT> outerObserver) {
            this.operation = operation;
            this.outerObserver = outerObserver;
        }

        abstract public void onReady();
        abstract public void onCancel(String reason);
    }

    private class Idle extends StateListener {

        Idle(Operation<RequestT, ResponseT> operation, ResponseObserver2<ResponseT> outerObserver) {
            super(operation, outerObserver);
        }

        @Override
        public void onReady() {
            Active active = new Active(super.operation, super.outerObserver);
            callable.call(operation.request, active, operation.context.withTimeoutDuration(Duration.ofMillis(getTimeout())));
            System.out.println("attempt=" + attempt.get() + " timeout=" + context.getTimeoutDuration());
            operation.onStateChange(active);
        }

        @Override
        public void onCancel(String reason) {
            outerObserver.onClose(Status.CANCELLED.withDescription(reason));
        }
    }

    private class Active extends StateListener implements ResponseObserver2<ResponseT> {

        StreamController2 grpcController;
        boolean userWaitingResponse = false;

        Active(Operation<RequestT, ResponseT> operation, ResponseObserver2<ResponseT> outerObserver) {
            super(operation, outerObserver);
        }

        @Override
        public void onReady() {
            userWaitingResponse = true;
            grpcController.onReady();
        }

        @Override
        public void onCancel(String reason) {
            grpcController.cancel(reason);
        }

        @Override
        public void onStart(StreamController2 streamController) {
            this.grpcController = streamController;
            onReady();
        }

        @Override
        public void onResponse(ResponseT response) {
            userWaitingResponse = false;
            resumptionStrategy.processResponse(response);
            outerObserver.onResponse(response);
        }

        @Override
        public void onClose(Status status) {
            if (!status.isOk()) {
                // TODO placeholder, need to check for retryable code and error details
                if (status.equals(Status.DEADLINE_EXCEEDED) || status.equals(Status.UNAVAILABLE)) {
                    attempt.getAndIncrement();
                    request = resumptionStrategy.getResumeRequest(request);
                    if (!userWaitingResponse) {
                        // TODO wait retry delay ?
                        Idle idle = new Idle(super.operation, outerObserver);
                        onStateChange(idle);
                        return;
                    } else {
                        long retryDelay = getRetryDelay();
                        if (deadline.timeRemaining(TimeUnit.MILLISECONDS) - retryDelay > 1) {
                            Scheduled scheduled = new Scheduled(super.operation, outerObserver);
                            ScheduledFuture future = executor.schedule(scheduled::onReady, retryDelay, TimeUnit.MILLISECONDS);
                            scheduled.setScheduledFuture(future);
                            onStateChange(scheduled);
                            return;
                        }
                    }
                }
            }
            outerObserver.onClose(status);
        }

        // TODO state transition
    }


    class Scheduled extends StateListener {

        ScheduledFuture scheduledFuture;
        Scheduled(Operation<RequestT, ResponseT> operation, ResponseObserver2<ResponseT> outerObserver) {
            super(operation, outerObserver);
        }

        @Override
        public void onReady() {
            Active active = new Active(super.operation, super.outerObserver);
            callable.call(request, active, context.withTimeoutDuration(Duration.ofMillis(getTimeout())));
            System.out.println("attempt=" + attempt.get() + " timeout=" + context.getTimeoutDuration());
            onStateChange(active);
        }

        @Override
        public void onCancel(String reason) {
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
                scheduledFuture = null;
            }
        }

        void setScheduledFuture(ScheduledFuture future) {
            this.scheduledFuture = future;
        }
    }
}
