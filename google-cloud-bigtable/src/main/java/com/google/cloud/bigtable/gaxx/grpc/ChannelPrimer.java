package com.google.cloud.bigtable.gaxx.grpc;

import com.google.api.core.InternalApi;
import com.google.api.core.SettableApiFuture;
import com.google.bigtable.v2.PingAndWarmResponse;
import io.grpc.ManagedChannel;

@InternalApi("For internal use by google-cloud-java clients only")
public interface ChannelPrimer {
  void primeChannel(ManagedChannel var1);

  SettableApiFuture<PingAndWarmResponse> sendPrimeRequestsAsync(ManagedChannel var1);
}
