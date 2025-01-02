package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import io.grpc.Status;

import java.util.concurrent.atomic.AtomicInteger;

interface Callable2<RequestT, ResponseT> {

    void call(RequestT request, ResponseObserver2<ResponseT> observer, ApiCallContext context);

}

class ToOldCallableAdapter<RequestT, ResponseT> extends ServerStreamingCallable<RequestT, ResponseT> {

    Callable2<RequestT, ResponseT> callable;

    ToOldCallableAdapter(Callable2<RequestT, ResponseT> callable) {
        this.callable = callable;
    }

    @Override
    public void call(RequestT requestT, ResponseObserver<ResponseT> responseObserver, ApiCallContext apiCallContext) {
        callable.call(requestT, new ToOldObserverAdapter<>(responseObserver), apiCallContext);
    }
}

class ToNewCallableAdapter<RequestT, ResponseT> implements Callable2<RequestT, ResponseT> {

    ServerStreamingCallable<RequestT, ResponseT> callable;

    ToNewCallableAdapter(ServerStreamingCallable<RequestT, ResponseT> callable) {
        this.callable = callable;
    }

    @Override
    public void call(RequestT request, ResponseObserver2<ResponseT> observer, ApiCallContext context) {
        callable.call(request, new ToNewObserverAdapter<>(observer), context);
    }
}

class ToNewObserverAdapter<ResponseT> implements ResponseObserver<ResponseT> {

    private ResponseObserver2<ResponseT> userObserver;
    private StreamController grpcController;

    ToNewObserverAdapter(ResponseObserver2 observer) {
        this.userObserver = observer;
    }

    @Override
    public void onStart(StreamController streamController) {
        grpcController = streamController;
        grpcController.disableAutoInboundFlowControl();
        userObserver.onStart(new StreamController2() {
            @Override
            public void cancel(String reason) {
                grpcController.cancel();
            }

            @Override
            public void onReady() {
                grpcController.request(1);
            }
        });
    }

    @Override
    public void onResponse(ResponseT responseT) {
        userObserver.onResponse(responseT);
    }

    @Override
    public void onError(Throwable throwable) {
        userObserver.onClose(Status.fromThrowable(throwable));
    }

    @Override
    public void onComplete() {
        userObserver.onClose(Status.OK);
    }
}

class ToOldObserverAdapter<ResponseT> implements ResponseObserver2<ResponseT> {

    private ResponseObserver<ResponseT> userOberver;
    private StreamController2 grpcController;
    private AtomicInteger userRequested = new AtomicInteger(0);
    private boolean autoFlowControl = true;

    ToOldObserverAdapter(ResponseObserver<ResponseT> observer) {
        this.userOberver = observer;
    }

    @Override
    public void onStart(StreamController2 streamController2) {
        grpcController = streamController2;
        userOberver.onStart(new StreamController() {
            @Override
            public void cancel() {
                grpcController.cancel("user cancelled stream");
            }

            @Override
            public void disableAutoInboundFlowControl() {
                System.out.println("already disabled");
                autoFlowControl = false;
            }

            @Override
            public void request(int i) {
                int oldN = userRequested.getAndAdd(i);
                if (oldN == 0) {
                    grpcController.onReady();
                }

            }
        });

        if (autoFlowControl) {
            grpcController.onReady();
        }
    }

    @Override
    public void onResponse(ResponseT response) {
        userOberver.onResponse(response);
        if (userRequested.decrementAndGet() > 0) {
            grpcController.onReady();
        }

        if (autoFlowControl) {
            grpcController.onReady();
        }
    }

    @Override
    public void onClose(Status status) {
        if (status.isOk()) {
            userOberver.onComplete();
        } else {
            userOberver.onError(status.asException());
        }
    }
}

interface ResponseObserver2<ResponseT> {
    void onStart(StreamController2 streamController2);
    void onResponse(ResponseT response);
    void onClose(Status status);
}

interface StreamController2 {
    void cancel(String reason);
    void onReady();
}




