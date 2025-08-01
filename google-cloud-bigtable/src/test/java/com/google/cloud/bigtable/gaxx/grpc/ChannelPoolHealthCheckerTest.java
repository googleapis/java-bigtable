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

import static com.google.common.truth.Truth.assertThat;

import com.google.api.core.SettableApiFuture;
import com.google.bigtable.v2.PingAndWarmResponse;
import com.google.cloud.bigtable.data.v2.stub.BigtableChannelPrimer;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelPool.Entry;
import com.google.cloud.bigtable.gaxx.grpc.ChannelPoolHealthChecker.ProbeResult;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.testing.TestingExecutors;
import io.grpc.ManagedChannel;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(JUnit4.class)
public class ChannelPoolHealthCheckerTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  // Mock the external dependencies
  @Mock private BigtableChannelPrimer mockPrimer;
  private ListeningScheduledExecutorService executor;
  @Mock private Clock mockClock;

  // The System Under Test
  private ChannelPoolHealthChecker healthChecker;

  // Controllable list of channels for the checker to use
  private List<Entry> channelList;

  @Before
  public void setUp() {
    executor = TestingExecutors.sameThreadScheduledExecutor();
    channelList = new ArrayList<>();
    Supplier<ImmutableList<Entry>> entrySupplier = () -> ImmutableList.copyOf(channelList);

    healthChecker = new ChannelPoolHealthChecker(entrySupplier, mockPrimer, executor, mockClock);

    // Default the clock to a fixed time
    Mockito.when(mockClock.instant()).thenReturn(Instant.parse("2025-08-01T10:00:00Z"));
  }

  // Helper method to create test entries
  private Entry createTestEntry() {
    ManagedChannel mockChannel = Mockito.mock(ManagedChannel.class);
    return new Entry(mockChannel);
  }

  @After
  public void tearDown() {
    executor.shutdownNow();
  }

  @Test
  public void testOnComplete_successUpdatesCounters() {
    // Given: A single channel entry
    Entry entry = createTestEntry();
    channelList.add(entry);

    // And a probe that will succeed
    SettableApiFuture<PingAndWarmResponse> successFuture = SettableApiFuture.create();
    Mockito.when(mockPrimer.sendPrimeRequestsAsync(entry.getManagedChannel()))
        .thenReturn(successFuture);

    // When: A probe runs and completes
    healthChecker.runProbes(); // This will trigger onComplete

    successFuture.set(PingAndWarmResponse.getDefaultInstance());

    // Then: The correct counters are updated
    assertThat(entry.successfulProbesInWindow.get()).isEqualTo(1);
    assertThat(entry.failedProbesInWindow.get()).isEqualTo(0);
  }

  @Test
  public void testOnComplete_timeoutIsFailure() {
    // Given: A single channel entry
    Entry entry = createTestEntry();
    channelList.add(entry);

    // And a probe that will never complete (to force a timeout)
    SettableApiFuture<PingAndWarmResponse> hangingFuture = SettableApiFuture.create();
    Mockito.when(mockPrimer.sendPrimeRequestsAsync(entry.getManagedChannel()))
        .thenReturn(hangingFuture);

    // When: A probe runs and times out inside onComplete
    healthChecker.runProbes();

    // Difficult to test timeout without flakes
    hangingFuture.cancel(true);

    // Then: The failure counter is updated
    assertThat(entry.failedProbesInWindow.get()).isEqualTo(1);
    assertThat(entry.successfulProbesInWindow.get()).isEqualTo(0);
  }

  @Test
  public void testPruning_removesOldProbesAndCounters() {
    // Given: An entry with a failed probe
    Entry entry = createTestEntry();
    healthChecker.addProbeResult(entry, new ProbeResult(mockClock.instant(), false));
    assertThat(entry.failedProbesInWindow.get()).isEqualTo(1);

    // When: The clock advances past the WINDOW_DURATION
    Instant newTime = mockClock.instant().plus(Duration.ofMinutes(6));
    Mockito.when(mockClock.instant()).thenReturn(newTime);
    healthChecker.pruneHistoryFor(entry); // Manually call for direct testing

    // Then: The probe is pruned and the counter is decremented
    assertThat(entry.probeHistory).isEmpty();
    assertThat(entry.failedProbesInWindow.get()).isEqualTo(0);
  }

  @Test
  public void testEviction_selectsWorstChannel() {
    // --- GIVEN --- 3 channels with different health states
    Entry healthyEntry = createTestEntry();
    Entry badEntry = createTestEntry();
    Entry worseEntry = createTestEntry();

    // 2. Set their state directly to control the outcome of isEntryHealthy()
    // A channel needs at least 4 probes to be considered for eviction
    healthyEntry.successfulProbesInWindow.set(10); // 0% failure -> healthy
    badEntry.failedProbesInWindow.set(3); // 3/13 = 23% failure -> healthy
    badEntry.successfulProbesInWindow.set(10);
    worseEntry.failedProbesInWindow.set(10); // 10/10 = 100% failure -> unhealthy

    channelList.addAll(Arrays.asList(healthyEntry, badEntry, worseEntry));

    // --- WHEN ---
    healthChecker.detectAndRemoveOutlierEntries();

    // --- THEN ---
    // Assert that only the unhealthy channel was evicted
    Mockito.verify(worseEntry.getManagedChannel()).enterIdle();
    Mockito.verify(badEntry.getManagedChannel(), Mockito.never()).enterIdle();
    Mockito.verify(healthyEntry.getManagedChannel(), Mockito.never()).enterIdle();
  }

  @Test
  public void testCircuitBreaker_preventsEviction() {
    // --- GIVEN --- A pool of 3 channels.
    Entry entry1 = createTestEntry();
    Entry entry2 = createTestEntry();
    Entry entry3 = createTestEntry();
    channelList.addAll(Arrays.asList(entry1, entry2, entry3));

    // To make them all unhealthy, set their failure counts high enough
    // to exceed the 60% failure threshold.
    // We need at least MIN_PROBES_FOR_EVALUATION (4) total probes.
    for (Entry entry : channelList) {
      entry.failedProbesInWindow.set(4); // 4 failures, 0 successes = 100% failure rate
    }

    healthChecker.detectAndRemoveOutlierEntries();

    // --- THEN ---
    // The circuit breaker should engage because 3/3 channels (100%) are unhealthy,
    // which is greater than the 70% threshold.
    Mockito.verify(entry1.getManagedChannel(), Mockito.never()).enterIdle();
    Mockito.verify(entry2.getManagedChannel(), Mockito.never()).enterIdle();
    Mockito.verify(entry3.getManagedChannel(), Mockito.never()).enterIdle();
  }
}
