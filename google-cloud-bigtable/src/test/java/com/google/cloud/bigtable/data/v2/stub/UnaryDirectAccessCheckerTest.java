/*
 * Copyright 2026 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.data.v2.stub;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.bigtable.v2.PeerInfo;
import com.google.cloud.bigtable.data.v2.internal.csm.tracers.DirectPathCompatibleTracer;
import com.google.cloud.bigtable.gaxx.grpc.ChannelPrimer;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(JUnit4.class)
public class UnaryDirectAccessCheckerTest {

    @Rule public final MockitoRule mockito = MockitoJUnit.rule();

    @Mock private ChannelPrimer mockChannelPrimer;
    @Mock private BigtableChannelFactory mockChannelFactory;
    @Mock private DirectPathCompatibleTracer mockTracer;
    @Mock private ManagedChannel mockChannel;
    @Mock private MetadataExtractorInterceptor.SidebandData mockSidebandData;

    private UnaryDirectAccessChecker checker;

    @Before
    public void setUp() throws Exception {
        checker = UnaryDirectAccessChecker.create(mockChannelPrimer);
        when(mockChannelFactory.createSingleChannel()).thenReturn(mockChannel);
    }

    @Test
    public void testEligibleForDirectAccess() {
        PeerInfo peerInfo = PeerInfo.newBuilder()
                .setTransportType(PeerInfo.TransportType.TRANSPORT_TYPE_DIRECT_ACCESS)
                .build();
        when(mockSidebandData.getPeerInfo()).thenReturn(peerInfo);

        when(mockSidebandData.getIpProtocol()).thenReturn(MetadataExtractorInterceptor.SidebandData.IpProtocol.IPV6);

        try (MockedConstruction<MetadataExtractorInterceptor> ignored =
                     mockConstruction(
                             MetadataExtractorInterceptor.class,
                             (mock, context) -> {
                                 when(mock.getSidebandData()).thenReturn(mockSidebandData);
                             })) {

            boolean isEligible = checker.check(mockChannelFactory, mockTracer);

            assertThat(isEligible).isFalse();
            verify(mockChannelPrimer).primeChannel(any(Channel.class));
            verify(mockTracer).recordSuccess("ipv6");
            verify(mockChannel).shutdownNow();
        }
    }

    @Test
    public void testNotEligibleProxiedRouting() {
        // 1. Setup sideband data to simulate standard CloudPath routing
        PeerInfo peerInfo = PeerInfo.newBuilder()
                .setTransportType(PeerInfo.TransportType.TRANSPORT_TYPE_CLOUD_PATH)
                .build();
        when(mockSidebandData.getPeerInfo()).thenReturn(peerInfo);

        try (MockedConstruction<MetadataExtractorInterceptor> mocked =
                     mockConstruction(
                             MetadataExtractorInterceptor.class,
                             (mock, context) -> {
                                 when(mock.getSidebandData()).thenReturn(mockSidebandData);
                             })) {

            boolean isEligible = checker.check(mockChannelFactory, mockTracer);

            assertThat(isEligible).isFalse();
            verifyNoInteractions(mockTracer);
            verify(mockChannel).shutdownNow();
        }
    }

    @Test
    public void testMissingSidebandData() {
        // Interceptor failed to capture anything (returns null)
        try (MockedConstruction<MetadataExtractorInterceptor> mocked =
                     mockConstruction(
                             MetadataExtractorInterceptor.class,
                             (mock, context) -> {
                                 when(mock.getSidebandData()).thenReturn(null);
                             })) {

            boolean isEligible = checker.check(mockChannelFactory, mockTracer);

            assertThat(isEligible).isFalse();
            verifyNoInteractions(mockTracer);
            verify(mockChannel).shutdownNow();
        }
    }

    @Test
    public void testExceptionSafetyAndCleanup() {
        doThrow(new RuntimeException("Simulated primer failure"))
                .when(mockChannelPrimer)
                .primeChannel(any(Channel.class));

        boolean isEligible = checker.check(mockChannelFactory, mockTracer);

        assertThat(isEligible).isFalse();
        verifyNoInteractions(mockTracer);
        verify(mockChannel).shutdownNow();
    }
}