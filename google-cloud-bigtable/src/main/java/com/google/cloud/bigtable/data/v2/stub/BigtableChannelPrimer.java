/*
 * Copyright 2020 Google LLC
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

import com.google.api.core.BetaApi;
import com.google.api.core.SettableApiFuture;
import com.google.api.gax.grpc.ChannelPrimer;
import com.google.api.gax.grpc.GrpcCallContext;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.InstanceName;
import com.google.bigtable.v2.PingAndWarmRequest;
import com.google.bigtable.v2.PingAndWarmResponse;
import com.google.common.net.PercentEscaper;
import io.grpc.CallCredentials;
import io.grpc.ClientCall;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.auth.MoreCallCredentials;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * A channel warmer that ensures that a Bigtable channel is ready to be used before being added to
 * the active {@link com.google.api.gax.grpc.ChannelPool}.
 *
 * <p>This implementation is subject to change in the future, but currently it will prime the
 * channel by sending a ReadRow request for a hardcoded, non-existent row key.
 */
@BetaApi("Channel priming is not currently stable and might change in the future")
class BigtableChannelPrimer implements ChannelPrimer {
  private static Logger LOG = Logger.getLogger(BigtableChannelPrimer.class.toString());

  private final Metadata.Key<String> requestParams =
      Metadata.Key.of("x-goog-request-params", Metadata.ASCII_STRING_MARSHALLER);
  private final PingAndWarmRequest request;
  private final Metadata metadata = new Metadata();
  private final CallCredentials credentials;

  static BigtableChannelPrimer create(EnhancedBigtableStubSettings settings) throws IOException {
    return new BigtableChannelPrimer(settings);
  }

  BigtableChannelPrimer(EnhancedBigtableStubSettings settings) throws IOException {
    String projectId = settings.getProjectId();
    String instanceId = settings.getInstanceId();
    String appProfileId = settings.getAppProfileId();

    if (settings.getCredentialsProvider().getCredentials() != null) {
      credentials = MoreCallCredentials.from(settings.getCredentialsProvider().getCredentials());
    } else {
      credentials = null;
    }

    request =
        PingAndWarmRequest.newBuilder()
            .setName(InstanceName.format(projectId, instanceId))
            .setAppProfileId(appProfileId)
            .build();

    PercentEscaper escaper = new PercentEscaper("._-~", false);

    Metadata metadata = new Metadata();
    settings
        .getHeaderProvider()
        .getHeaders()
        .forEach((k, v) -> metadata.put(Metadata.Key.of(k, Metadata.ASCII_STRING_MARSHALLER), v));

    String escapedName = escaper.escape(request.getName());
    String escapedAppProfile = escaper.escape(request.getAppProfileId());
    metadata.put(
        requestParams,
        escaper.escape(String.format("name=%s&app_profile_id=%s", escapedName, escapedAppProfile)));
  }

  @Override
  public void primeChannel(ManagedChannel managedChannel) {
    try {
      primeChannelUnsafe(managedChannel);
    } catch (IOException | RuntimeException e) {
      LOG.warning(
          String.format("Unexpected error while trying to prime a channel: %s", e.getMessage()));
    }
  }

  private void primeChannelUnsafe(ManagedChannel managedChannel) throws IOException {
    sendPrimeRequests(managedChannel);
  }

  private void sendPrimeRequests(ManagedChannel managedChannel) {
    try {
      ClientCall<PingAndWarmRequest, PingAndWarmResponse> clientCall =
          managedChannel.newCall(
              BigtableGrpc.getPingAndWarmMethod(),
              GrpcCallContext.createDefault()
                  .getCallOptions()
                  .withCallCredentials(credentials)
                  .withDeadline(Deadline.after(1, TimeUnit.MINUTES)));

      SettableApiFuture<PingAndWarmResponse> future = SettableApiFuture.create();
      clientCall.start(
          new ClientCall.Listener<PingAndWarmResponse>() {
            PingAndWarmResponse response;

            @Override
            public void onMessage(PingAndWarmResponse message) {
              response = message;
            }

            @Override
            public void onClose(Status status, Metadata trailers) {
              if (status.isOk()) {
                future.set(response);
              } else {
                future.setException(status.asException());
              }
            }
          },
          metadata);
      clientCall.sendMessage(request);
      clientCall.halfClose();
      clientCall.request(Integer.MAX_VALUE);

      future.get(1, TimeUnit.MINUTES);
    } catch (Throwable e) {
      // TODO: Not sure if we should swallow the error here. We are pre-emptively swapping
      // channels if the new
      // channel is bad.
      LOG.warning(String.format("Failed to prime channel: %s", e));
    }
  }
}
