/*
 * Copyright 2024 Google LLC
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
package com.google.cloud.bigtable.data.v2.it;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static com.google.common.truth.TruthJUnit.assume;
import static org.junit.Assert.assertThrows;

import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.ResourceExhaustedException;
import com.google.cloud.bigtable.admin.v2.BigtableInstanceAdminClient;
import com.google.cloud.bigtable.admin.v2.models.AppProfile;
import com.google.cloud.bigtable.admin.v2.models.CreateAppProfileRequest;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.test_helpers.env.EmulatorEnv;
import com.google.cloud.bigtable.test_helpers.env.PrefixGenerator;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.common.base.Stopwatch;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Duration;

@RunWith(JUnit4.class)
@Ignore("This test needs to run on a project with 0 SPU. Skip for now")
public class RetryInfoIT {

  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  // Test should be done within a minute
  @Rule public Timeout globalTimeout = Timeout.seconds(60);

  private static BigtableInstanceAdminClient instanceAdminClient;
  private static String appProfileId;

  @BeforeClass
  public static void setUpClass() {
    assume()
        .withMessage("Routing cookie integration test is not supported by emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    instanceAdminClient = testEnvRule.env().getInstanceAdminClient();

    appProfileId = PrefixGenerator.newPrefix("a");

    instanceAdminClient.createAppProfile(
        CreateAppProfileRequest.of(testEnvRule.env().getInstanceId(), appProfileId)
            .setRoutingPolicy(
                AppProfile.SingleClusterRoutingPolicy.of(testEnvRule.env().getPrimaryClusterId()))
            .setIsolationPolicy(
                AppProfile.DataBoostIsolationReadOnlyPolicy.of(
                    AppProfile.ComputeBillingOwner.HOST_PAYS)));
  }

  @AfterClass
  public static void tearDown() {
    if (instanceAdminClient != null) {
      instanceAdminClient.deleteAppProfile(testEnvRule.env().getInstanceId(), appProfileId, true);
    }
  }

  // Test RetryInfo on a project with 0 SPUs. The read request will fail because of the quota. And
  // we'll attach a retry_delay in the error response. The retry delay returned from server starts
  // from 100ms and grows exponentially. Configure read rows retry delay to be 1 ms, so we can
  // compare the failed request with retry info enabled takes longer than the request with retry
  // info disabled.
  @Test
  public void testRetryInfo() throws IOException {
    BigtableDataSettings.Builder settings = testEnvRule.env().getDataClientSettings().toBuilder();

    settings.setAppProfileId(appProfileId);

    settings
        .stubSettings()
        .readRowsSettings()
        .setRetrySettings(
            RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(1))
                .setMaxRetryDelay(Duration.ofMillis(1))
                // server side retry delay is calculated with:
                //  0.4 * min_delay + (rand in [0.6,1.0]) * min_delay * backoff_base^retries
                // where min_delay is 100ms and backoff_base is 2.
                // so 3 attempts will take at least 26 seconds (first attempt has no delay)
                .setMaxAttempts(3) // 10s, 16s
                .build());

    long enabledElapsed;
    try (BigtableDataClient dataClient = BigtableDataClient.create(settings.build())) {
      Stopwatch stopwatch1 = Stopwatch.createStarted();
      ResourceExhaustedException e =
          assertThrows(
              "Request should fail with resource exhausted exception",
              ResourceExhaustedException.class,
              () ->
                  dataClient
                      .readRows(Query.create(testEnvRule.env().getTableId()).limit(1))
                      .iterator()
                      .hasNext());
      assertThat(e).hasMessageThat().contains("SPUs");
      enabledElapsed = stopwatch1.elapsed(TimeUnit.MILLISECONDS);
    }

    // Disable retry info. We want to disable retry info from the client path but still send the
    // feature flag
    // so server won't reject the request.
    settings.stubSettings().setEnableRetryInfo(false);
    settings.stubSettings().setEnableRetryFeatureFlags(true);

    long disabledElapsed;
    try (BigtableDataClient dataClient = BigtableDataClient.create(settings.build())) {
      Stopwatch stopwatch2 = Stopwatch.createStarted();
      ResourceExhaustedException e =
          assertThrows(
              "Request should fail with resource exhausted exception",
              ResourceExhaustedException.class,
              () ->
                  dataClient
                      .readRows(Query.create(testEnvRule.env().getTableId()).limit(1))
                      .iterator()
                      .hasNext());
      assertThat(e).hasMessageThat().contains("SPUs");
      disabledElapsed = stopwatch2.elapsed(TimeUnit.MILLISECONDS);
    }

    assertWithMessage("Operation duration without RetryInfo")
        .that(disabledElapsed)
        .isGreaterThan(0);
    assertWithMessage("Operation duration with RetryInfo > without")
        .that(enabledElapsed)
        .isGreaterThan(disabledElapsed);
    assertWithMessage("operation duration with Retrying minimum duration")
        .that(enabledElapsed)
        .isGreaterThan(26000);
  }

  @Test
  public void testRetryInfoGuardedByTotalTimeout() throws Exception {
    BigtableDataSettings.Builder settings = testEnvRule.env().getDataClientSettings().toBuilder();

    settings.setAppProfileId(appProfileId);

    settings
        .stubSettings()
        .readRowsSettings()
        .setRetrySettings(
            RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(1))
                .setMaxRetryDelay(Duration.ofMillis(1))
                .setTotalTimeout(Duration.ofSeconds(5))
                .build());

    long enabledElapsed;
    try (BigtableDataClient dataClient = BigtableDataClient.create(settings.build())) {
      Stopwatch stopwatch1 = Stopwatch.createStarted();
      ResourceExhaustedException e =
          assertThrows(
              "Request should fail with resource exhausted exception",
              ResourceExhaustedException.class,
              () ->
                  dataClient
                      .readRows(Query.create(testEnvRule.env().getTableId()).limit(1))
                      .iterator()
                      .hasNext());
      assertThat(e).hasMessageThat().contains("SPUs");
      enabledElapsed = stopwatch1.elapsed(TimeUnit.MILLISECONDS);
    }

    assertThat(enabledElapsed).isLessThan(5000);
  }
}
