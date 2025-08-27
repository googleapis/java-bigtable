/*
 * Copyright 2025 Google LLC
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

import com.google.api.core.InternalApi;
import com.google.api.core.SettableApiFuture;
import com.google.bigtable.v2.PingAndWarmResponse;
import com.google.cloud.bigtable.gaxx.grpc.ChannelPrimer;
import io.grpc.ManagedChannel;

@InternalApi
public class NoOpChannelPrimer implements ChannelPrimer {
  static NoOpChannelPrimer create() {
    return new NoOpChannelPrimer();
  }

  private NoOpChannelPrimer() {}

  @Override
  public void primeChannel(ManagedChannel managedChannel) {
    // No op
  }

  @Override
  public SettableApiFuture<PingAndWarmResponse> sendPrimeRequestsAsync(ManagedChannel var1) {
    SettableApiFuture future = SettableApiFuture.create();
    future.set(PingAndWarmResponse.getDefaultInstance());
    return future;
  }
}
