package com.google.cloud.bigtable.data.v2.stub;

import static com.google.cloud.bigtable.data.v2.models.Filters.FILTERS;

import com.google.api.core.ApiFuture;
import com.google.api.core.BetaApi;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.core.InstantiatingExecutorProvider;
import com.google.api.gax.grpc.ChannelPrimer;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.FixedWatchdogProvider;
import com.google.auth.Credentials;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.common.collect.ImmutableList;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.threeten.bp.Duration;

/**
 * A channel warmer that ensures that a Bigtable channel is ready to be used before being added to
 * the active {@link com.google.api.gax.grpc.ChannelPool}.
 */
@BetaApi()
class BigtableChannelPrimer implements ChannelPrimer {
  private static Logger LOG = Logger.getLogger(BigtableChannelPrimer.class.toString());

  EnhancedBigtableStubSettings settingsTemplate;
  List<String> tableIds;

  static BigtableChannelPrimer create(Credentials credentials, String projectId, String instanceId, String appProfileId, List<String> tableIds) {
    EnhancedBigtableStubSettings.Builder builder = EnhancedBigtableStubSettings.newBuilder()
        .setProjectId(projectId)
        .setInstanceId(instanceId)
        .setAppProfileId(appProfileId)
        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
        .setExecutorProvider(
            InstantiatingExecutorProvider.newBuilder()
              .setExecutorThreadCount(1)
              .build()
        )
        // Disable watchdog creation - its unnecessary.
        .setStreamWatchdogProvider(FixedWatchdogProvider.create(null));

    // Disable retries for priming request
    Duration timeout = Duration.ofSeconds(1);
    builder.readRowSettings().setRetrySettings(
        builder.readRowSettings().getRetrySettings().toBuilder()
          .setMaxAttempts(0)
          .setJittered(false)
          .setInitialRpcTimeout(timeout)
          .setMaxRpcTimeout(timeout)
          .setTotalTimeout(timeout)
          .build()
    );
    return new BigtableChannelPrimer(builder.build(), tableIds);
  }
  private BigtableChannelPrimer(EnhancedBigtableStubSettings settingsTemplate, List<String> tableIds) {
    this.settingsTemplate = settingsTemplate;
    this.tableIds = ImmutableList.copyOf(tableIds);
  }

  @Override
  public void primeChannel(ManagedChannel managedChannel) {
    try {
      primeChannelUnsafe(managedChannel);
    } catch (IOException|RuntimeException e) {
      LOG.warning(String.format("Unexpected error while trying to prime the channel: %s", e));
    }
  }

  private void primeChannelUnsafe(ManagedChannel managedChannel) throws IOException {
    if (tableIds.isEmpty()) {
      waitForChannelReady(managedChannel);
    } else {
      sendPrimeRequests(managedChannel);
    }
  }

  void waitForChannelReady(ManagedChannel managedChannel) {
    for (int i = 0; i < 10; i++) {
      ConnectivityState connectivityState = managedChannel.getState(true);
      if (connectivityState == ConnectivityState.READY) {
        break;
      }
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        break;
      }
    }
  }

  void sendPrimeRequests(ManagedChannel managedChannel) throws IOException {
    // Wrap the channel in a temporary stub
    EnhancedBigtableStubSettings primingSettings = settingsTemplate.toBuilder()
        .setTransportChannelProvider(
            FixedTransportChannelProvider.create(
                GrpcTransportChannel.create(managedChannel)
            )
        ).build();

    try (EnhancedBigtableStub stub = EnhancedBigtableStub.create(primingSettings)) {
      Map<String, ApiFuture<?>> primeFutures = new HashMap<>();

      // Prime all of the table ids in parallel
      for (String tableId : tableIds) {
        ApiFuture<Row> f = stub.readRowCallable()
            .futureCall(Query.create(tableId).filter(FILTERS.block()));

        primeFutures.put(tableId, f);
      }

      // Wait for all of the prime requests to complete.
      for (Map.Entry<String, ApiFuture<?>> entry : primeFutures.entrySet()) {
        try {
          entry.getValue().get();
        } catch (Throwable e) {
          LOG.warning(String
              .format("Failed to prime channel for table: %s: %s", entry.getKey(), e.getMessage()));
        }
      }
    }
  }
}
