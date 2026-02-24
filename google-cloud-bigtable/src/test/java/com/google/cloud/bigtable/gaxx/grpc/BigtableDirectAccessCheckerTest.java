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

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.google.cloud.bigtable.data.v2.stub.BigtableChannelPrimer;
import io.grpc.ManagedChannel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BigtableDirectAccessCheckerTest {

  private BigtableChannelPrimer mockPrimer;
  private ManagedChannel mockChannel;
  private BigtableDirectAccessChecker checker;

  @Before
  public void setUp() {
    mockPrimer = mock(BigtableChannelPrimer.class);
    mockChannel = mock(ManagedChannel.class);
    // Ensure the call returns empty attributes by default to avoid NullPointerExceptions
    checker = new BigtableDirectAccessChecker(mockPrimer);
  }

  @Test
  public void testProbeInvalidPrimerReturnsFalse() {
    ChannelPrimer invalidPrimer = mock(ChannelPrimer.class);
    BigtableDirectAccessChecker invalidChecker = new BigtableDirectAccessChecker(invalidPrimer);

    boolean result = invalidChecker.check(mockChannel);

    assertThat(result).isFalse();
  }

  @Test
  public void testProbeErrorSituationReturnsFalse() {
    doThrow(new RuntimeException("Simulated network failure"))
        .when(mockPrimer)
        .primeChannel((ManagedChannel) any(ManagedChannel.class));

    boolean result = checker.check(mockChannel);
    assertThat(result).isFalse();
  }

  @Test
  public void testProbe_TimeoutDuringPriming_ReturnsFalse() {
    doThrow(
            io.grpc.Status.DEADLINE_EXCEEDED
                .withDescription("deadline exceeded after 1m")
                .asRuntimeException())
        .when(mockPrimer)
        .primeChannel(any(ManagedChannel.class));

    boolean result = checker.check(mockChannel);
    assertThat(result).isFalse();
  }
}
