package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.ApiFuture;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.api.gax.rpc.InternalException;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.SpanName;
import com.google.cloud.bigtable.data.v2.stub.metrics.BigtableTracer;
import com.google.cloud.bigtable.gaxx.testing.FakeStreamingApi;
import com.google.cloud.bigtable.gaxx.testing.MockStreamingApi.MockServerStreamingCall;
import com.google.cloud.bigtable.gaxx.testing.MockStreamingApi.MockServerStreamingCallable;
import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(JUnit4.class)
public class BigtableUnaryOperationCallableTest {
    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private ApiTracerFactory tracerFactory;
    @Mock
    private BigtableTracer tracer;

    @Before
    public void setUp() throws Exception {
        Mockito.when(tracerFactory.newTracer(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(tracer);
    }

    @Test
    public void testFutureResolve() throws Exception {
        BigtableUnaryOperationCallable<String, String> callable = new BigtableUnaryOperationCallable<>(
                new FakeStreamingApi.ServerStreamingStashCallable<>(ImmutableList.of("value")),
                GrpcCallContext.createDefault(),
                tracerFactory,
                SpanName.of("Fake", "method"),
                false
        );

        ApiFuture<String> f = callable.futureCall("fake");
        assertThat(f.get()).isEqualTo("value");
    }

    @Test
    public void testMultipleResponses() throws Exception {
        MockServerStreamingCallable<String, String> inner = new MockServerStreamingCallable<>();

        BigtableUnaryOperationCallable<String, String> callable = new BigtableUnaryOperationCallable<>(
                inner,
                GrpcCallContext.createDefault(),
                tracerFactory,
                SpanName.of("Fake", "method"),
                false
        );
        callable.logger = Mockito.mock(Logger.class);

        ApiFuture<String> f = callable.futureCall("fake");
        MockServerStreamingCall<String, String> call = inner.popLastCall();
        call.getController().getObserver().onResponse("first");
        call.getController().getObserver().onResponse("second");

        Throwable e = Assert.assertThrows(ExecutionException.class, f::get).getCause();
        assertThat(e).isInstanceOf(InternalException.class);
        assertThat(e).hasMessageThat().contains("Received multiple responses for a Fake.method unary operation. Previous: first, New: second");

        ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
        verify(callable.logger).log(Mockito.any(), msgCaptor.capture());
        assertThat(msgCaptor.getValue()).isEqualTo("Received multiple responses for a Fake.method unary operation. Previous: first, New: second");

        assertThat(call.getController().isCancelled()).isTrue();

    }

    @Test
    public void testCancel() {
        MockServerStreamingCallable<String, String> inner = new MockServerStreamingCallable<>();
        BigtableUnaryOperationCallable<String, String> callable = new BigtableUnaryOperationCallable<>(
                inner,
                GrpcCallContext.createDefault(),
                tracerFactory,
                SpanName.of("Fake", "method"),
                false
        );
        ApiFuture<String> f = callable.futureCall("req");
        f.cancel(true);

        MockServerStreamingCall<String, String> call = inner.popLastCall();
        assertThat(call.getController().isCancelled()).isTrue();
    }

    @Test
    public void testMissingResponse() {
        MockServerStreamingCallable<String, String> inner = new MockServerStreamingCallable<>();
        BigtableUnaryOperationCallable<String, String> callable = new BigtableUnaryOperationCallable<>(
                inner,
                GrpcCallContext.createDefault(),
                tracerFactory,
                SpanName.of("Fake", "method"),
                false
        );
        ApiFuture<String> f = callable.futureCall("req");
        MockServerStreamingCall<String, String> call = inner.popLastCall();
        call.getController().getObserver().onComplete();

        Throwable cause = Assert.assertThrows(ExecutionException.class, f::get).getCause();
        assertThat(cause).hasMessageThat().isEqualTo("Fake.method unary operation completed without a response message");
    }

    @Test
    public void testTracing() throws Exception {
        MockServerStreamingCallable<String, String> inner = new MockServerStreamingCallable<>();
        BigtableUnaryOperationCallable<String, String> callable = new BigtableUnaryOperationCallable<>(
                inner,
                GrpcCallContext.createDefault(),
                tracerFactory,
                SpanName.of("Fake", "method"),
                false
        );
        ApiFuture<String> f = callable.futureCall("req");
        MockServerStreamingCall<String, String> call = inner.popLastCall();
        call.getController().getObserver().onResponse("value");
        call.getController().getObserver().onComplete();

        f.get();
        verify(tracer).responseReceived();
        verify(tracer).operationSucceeded();

        // afterResponse is the responsibility of BigtableTracerStreamingCallable
        verify(tracer, never()).afterResponse(Mockito.anyLong());
    }
}