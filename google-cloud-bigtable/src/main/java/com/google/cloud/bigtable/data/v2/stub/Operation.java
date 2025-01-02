package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.retrying.StreamResumptionStrategy;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.StreamingCallSettings;
import io.grpc.Deadline;
import io.grpc.Status;

import java.time.Duration;
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
        this.callable = callable;
        this.resumptionStrategy = resumptionStrategy;
        this.outerObserver = outerObserver;
        this.executor = executor;
        operationState = new Idle(this, outerObserver);
        this.request = request;
        this.context = context;
        this.settings = settings;
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
                operationState.onReady();
            }
        });
    }

    void onCancel(String reason) {

    }

    public void onStateChange(StateListener state) {
        this.operationState = state;
    }

    public StateListener getState() {
        return this.operationState;
    }

    public void updateRequest(RequestT updated) {
        this.request = updated;
    }

    public void updateContext(ApiCallContext updated) {
        this.context = updated;
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
        long timeout = Math.min(rpcTimeout, deadline.timeRemaining(TimeUnit.MILLISECONDS));

        return timeout;
    }

    abstract class StateListener {

        protected Operation<RequestT, ResponseT> operation;
        protected ResponseObserver2<ResponseT> outerObserver;

        StateListener(Operation<RequestT, ResponseT> operation, ResponseObserver2<ResponseT> outerObserver) {
            this.operation = operation;
            this.outerObserver = outerObserver;
        }

        abstract public void onReady();
        abstract public void onCancel();
    }

    private class Idle extends StateListener {

        Idle(Operation<RequestT, ResponseT> operation, ResponseObserver2<ResponseT> outerObserver) {
            super(operation, outerObserver);
        }

        @Override
        public void onReady() {
            System.out.println("attempt=" + attempt.get() + " timeout=" + context.getTimeoutDuration());
            Active active = new Active(super.operation, super.outerObserver);
            callable.call(operation.request, active, operation.context);
            operation.onStateChange(active);
        }

        @Override
        public void onCancel() {
            outerObserver.onClose(Status.CANCELLED);
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
        public void onCancel() {
            grpcController.cancel("user cancelled");
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
                if (status.equals(Status.DEADLINE_EXCEEDED) || status.equals(Status.UNAVAILABLE)) {
                    attempt.getAndIncrement();
                    operation.updateRequest(resumptionStrategy.getResumeRequest(request));
                    operation.updateContext(context.withTimeoutDuration(Duration.ofMillis(getTimeout())));
                    if (!userWaitingResponse) {
                        Idle idle = new Idle(super.operation, outerObserver);
                        onStateChange(idle);
                    } else {
                        Scheduled scheduled = new Scheduled(super.operation, outerObserver);
                        ScheduledFuture future = executor.schedule(scheduled::onReady, 5, TimeUnit.SECONDS);
                        scheduled.setScheduledFuture(future);
                        onStateChange(scheduled);
                    }
                } else {
                    outerObserver.onClose(status);
                }
            } else {
                outerObserver.onClose(status);
            }
        }
    }

    class Scheduled extends StateListener {

        ScheduledFuture scheduledFuture;
        Scheduled(Operation<RequestT, ResponseT> operation, ResponseObserver2<ResponseT> outerObserver) {
            super(operation, outerObserver);
        }

        @Override
        public void onReady() {
            Active active = new Active(super.operation, super.outerObserver);
            System.out.println("attempt=" + attempt.get() + " timeout=" + context.getTimeoutDuration());
            callable.call(request, active, context);
            onStateChange(active);
        }

        @Override
        public void onCancel() {
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
