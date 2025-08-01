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
package com.google.cloud.bigtable.gaxx.grpc;

import com.google.api.core.SettableApiFuture;
import com.google.bigtable.v2.PingAndWarmResponse;
import com.google.cloud.bigtable.data.v2.stub.BigtableChannelPrimer;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelPool.Entry;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/** Stub for a class that will manage the health checking in the BigtableChannelPool */
public class ChannelPoolHealthChecker {

  // Configuration constants
  private static final Duration WINDOW_DURATION = Duration.ofMinutes(5);
  static final Duration PROBE_RATE = Duration.ofSeconds(30);
  @VisibleForTesting static final Duration PROBE_DEADLINE = Duration.ofMillis(500);
  private static final Duration MIN_EVICTION_INTERVAL = Duration.ofMinutes(10);
  private static final int MIN_PROBES_FOR_EVALUATION = 4;
  private static final int SINGLE_CHANNEL_FAILURE_PERCENT_THRESHOLD = 60;
  private static final int POOLWIDE_BAD_CHANNEL_CIRCUITBREAKER_PERCENT = 70;

  /** Inner class to represent the result of a single probe. */
  static class ProbeResult {
    final Instant startTime;
    final boolean success;

    ProbeResult(Instant startTime, boolean success) {
      this.startTime = startTime;
      this.success = success;
    }

    public boolean isSuccessful() {
      return success;
    }
  }

  // Class fields
  private final Supplier<ImmutableList<Entry>> entrySupplier;
  private Instant lastEviction;
  private ScheduledExecutorService executor;

  private BigtableChannelPrimer channelPrimer;

  private final Clock clock;

  /** Constructor for the pool health checker. */
  public ChannelPoolHealthChecker(
      Supplier<ImmutableList<Entry>> entrySupplier,
      BigtableChannelPrimer channelPrimer,
      ScheduledExecutorService executor,
      Clock clock) {
    this.entrySupplier = entrySupplier;
    this.lastEviction = Instant.MIN;
    this.channelPrimer = channelPrimer;
    this.executor = executor;
    this.clock = clock;
  }

  void start() {
    Duration initialDelayProbe =
        Duration.ofMillis(ThreadLocalRandom.current().nextLong(PROBE_RATE.toMillis()));
    executor.scheduleAtFixedRate(
        this::runProbes,
        initialDelayProbe.toMillis(),
        PROBE_RATE.toMillis(),
        TimeUnit.MILLISECONDS);
    Duration initialDelayDetect =
        Duration.ofMillis(ThreadLocalRandom.current().nextLong(PROBE_RATE.toMillis()));
    executor.scheduleAtFixedRate(
        this::detectAndRemoveOutlierEntries,
        initialDelayDetect.toMillis(),
        PROBE_RATE.toMillis(),
        TimeUnit.MILLISECONDS);
  }

  /** Stop running health checking (No-op stub) */
  public void stop() {
    executor.shutdownNow();
  }

  /** Runs probes on all the channels in the pool. */
  @VisibleForTesting
  void runProbes() {
    // Method stub, no operation.
    for (Entry entry : this.entrySupplier.get()) {
      Instant startTime = clock.instant();
      SettableApiFuture<PingAndWarmResponse> probeFuture =
          channelPrimer.sendPrimeRequestsAsync(entry.getManagedChannel());
      probeFuture.addListener(() -> onComplete(entry, startTime, probeFuture), executor);
    }
  }

  /** Callback that will update Entry data on probe complete. */
  @VisibleForTesting
  void onComplete(
      Entry entry, Instant startTime, SettableApiFuture<PingAndWarmResponse> probeFuture) {
    boolean success;
    try {
      probeFuture.get(PROBE_DEADLINE.toMillis(), TimeUnit.MILLISECONDS);
      success = true;
    } catch (Exception e) {
      success = false;
    }
    addProbeResult(entry, new ProbeResult(startTime, success));
  }

  @VisibleForTesting
  void addProbeResult(Entry entry, ProbeResult result) {
    entry.probeHistory.add(result);
    if (result.isSuccessful()) {
      entry.successfulProbesInWindow.incrementAndGet();
    } else {
      entry.failedProbesInWindow.incrementAndGet();
    }
  }

  @VisibleForTesting
  void pruneHistoryFor(Entry entry) {
    Instant windowStart = clock.instant().minus(WINDOW_DURATION);
    while (!entry.probeHistory.isEmpty()
        && entry.probeHistory.peek().startTime.isBefore(windowStart)) {
      ProbeResult removedResult = entry.probeHistory.poll();
      if (removedResult.isSuccessful()) {
        entry.successfulProbesInWindow.decrementAndGet();
      } else {
        entry.failedProbesInWindow.decrementAndGet();
      }
    }
  }

  /** Checks if a single entry is currently healthy based on its probe history. */
  @VisibleForTesting
  boolean isEntryHealthy(Entry entry) {
    pruneHistoryFor(entry); // Ensure window is current before calculation

    int failedProbes = entry.failedProbesInWindow.get();
    int totalProbes = failedProbes + entry.successfulProbesInWindow.get();

    if (totalProbes < MIN_PROBES_FOR_EVALUATION) {
      return true; // Not enough data, assume healthy.
    }

    double failureRate = ((double) failedProbes / totalProbes) * 100.0;
    return failureRate < SINGLE_CHANNEL_FAILURE_PERCENT_THRESHOLD;
  }

  /**
   * Finds a channel that is an outlier in terms of health.
   *
   * @return Entry
   */
  @Nullable
  @VisibleForTesting
  Entry findOutlierEntry() {
    if (lastEviction.plus(WINDOW_DURATION).isAfter(clock.instant())) {
      return null;
    }

    List<Entry> unhealthyEntries =
        this.entrySupplier.get().stream()
            .peek(this::pruneHistoryFor)
            .filter(entry -> !isEntryHealthy(entry))
            .collect(Collectors.toList());

    int poolSize = this.entrySupplier.get().size();
    if (unhealthyEntries.isEmpty() || poolSize == 0) {
      return null;
    }

    // If more than CIRCUITBREAKER_PERCENT of channels are unhealthy we won't evict
    double unhealthyPercent = (double) unhealthyEntries.size() / poolSize * 100.0;
    if (unhealthyPercent >= POOLWIDE_BAD_CHANNEL_CIRCUITBREAKER_PERCENT) {
      return null;
    }

    return unhealthyEntries.stream()
        .max(Comparator.comparingInt(entry -> entry.failedProbesInWindow.get()))
        .orElse(null);
  }

  /** Periodically detects and removes outlier channels from the pool. (No-op stub) */
  @VisibleForTesting
  void detectAndRemoveOutlierEntries() {
    if (clock.instant().isBefore(lastEviction.plus(MIN_EVICTION_INTERVAL))) {
      // Primitive but effective rate-limiting.
      return;
    }
    Entry outlier = findOutlierEntry();
    if (outlier != null) {
      this.lastEviction = clock.instant();
      outlier.getManagedChannel().enterIdle();
    }
  }
}
