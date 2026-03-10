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
package com.google.cloud.bigtable.gaxx.grpc;

import com.google.api.core.InternalApi;
import com.google.bigtable.v2.PeerInfo;
import com.google.cloud.bigtable.data.v2.stub.MetadataExtractorInterceptor;
import io.grpc.Channel;
import io.grpc.ClientInterceptors;

import java.util.Optional;

/**
 * Evaluates whether a given channel has Direct Access (DirectPath) routing
 * by executing a RPC and inspecting the response headers.
 */
@InternalApi
public class UnaryDirectAccessChecker implements DirectAccessChecker {

    private final ChannelPrimer channelPrimer;

    private UnaryDirectAccessChecker(ChannelPrimer channelPrimer) {
        this.channelPrimer = channelPrimer;
    }

    public static UnaryDirectAccessChecker create(ChannelPrimer channelPrimer) {
        return new UnaryDirectAccessChecker(channelPrimer);
    }

    @Override
    public boolean check(Channel channel) {
        MetadataExtractorInterceptor interceptor = new MetadataExtractorInterceptor();
        Channel interceptedChannel = ClientInterceptors.intercept(channel, interceptor);
        channelPrimer.primeChannel(interceptedChannel);

        // Extract the sideband data populated by the interceptor
        MetadataExtractorInterceptor.SidebandData sidebandData = interceptor.getSidebandData();

        return Optional.of(sidebandData)
                .map(MetadataExtractorInterceptor.SidebandData::getPeerInfo)
                .map(PeerInfo::getTransportType)
                .map(type -> type == PeerInfo.TransportType.TRANSPORT_TYPE_DIRECT_ACCESS)
                .orElse(false);
    }
}