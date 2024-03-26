/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.bigtable.data.v2.it;

import static com.google.common.truth.TruthJUnit.assume;

import com.google.api.gax.grpc.InstantiatingGrpcChannelProvider;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.DeadlineExceededException;
import com.google.api.gax.rpc.UnavailableException;
import com.google.cloud.bigtable.admin.v2.BigtableInstanceAdminClient;
import com.google.cloud.bigtable.admin.v2.models.AppProfile;
import com.google.cloud.bigtable.admin.v2.models.CreateAppProfileRequest;
import com.google.cloud.bigtable.admin.v2.models.UpdateAppProfileRequest;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.test_helpers.env.EmulatorEnv;
import com.google.cloud.bigtable.test_helpers.env.PrefixGenerator;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Duration;

@RunWith(JUnit4.class)
public class RoutingCookieIT {

  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  // Test should be done within 5 minutes
  @Rule public Timeout globalTimeout = Timeout.seconds(300);

  private static BigtableInstanceAdminClient instanceAdminClient;
  private static String appProfileId;
  private static String appProfileIdFailing;

  @BeforeClass
  public static void setUpClass() {
    assume()
        .withMessage("Routing cookie integration test is not supported by emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    instanceAdminClient = testEnvRule.env().getInstanceAdminClient();

    appProfileId = PrefixGenerator.newPrefix("a");
    appProfileIdFailing = PrefixGenerator.newPrefix("b");

    instanceAdminClient.createAppProfile(
        CreateAppProfileRequest.of(testEnvRule.env().getInstanceId(), appProfileId)
            .setRoutingPolicy(
                AppProfile.SingleClusterRoutingPolicy.of(testEnvRule.env().getPrimaryClusterId()))
            .setIsolationPolicy(
                AppProfile.DataBoostIsolationReadOnlyPolicy.of(
                    AppProfile.ComputeBillingOwner.HOST_PAYS)));
    instanceAdminClient.createAppProfile(
        CreateAppProfileRequest.of(testEnvRule.env().getInstanceId(), appProfileIdFailing)
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
      instanceAdminClient.deleteAppProfile(
          testEnvRule.env().getInstanceId(), appProfileIdFailing, true);
    }
  }

  // This is an integration test for routing cookie for databoost. This test updates app profile
  // from offline to online in between 2 read rows requests. RLS hold the cache that routes the
  // request to the offline AFE for the second read rows request. Routing cookie should break
  // this cache so the retry attempt will go to the correct AFE. Without routing cookie, offline
  // AFEs are going to return unavailable errors until RLS cache expires in the order of minutes.
  // We set a short deadline on the read rows request of 30 seconds, so if client failed to send
  // the routing cookie to update the cache, the request will get deadline exceeded.
  @Test
  public void testRoutingCookieForDataBoost() throws Exception {
    BigtableDataSettings.Builder settings = testEnvRule.env().getDataClientSettings().toBuilder();

    settings.setAppProfileId(appProfileId);
    // Disable direct path
    InstantiatingGrpcChannelProvider channelProvider =
        ((InstantiatingGrpcChannelProvider) settings.stubSettings().getTransportChannelProvider())
            .toBuilder()
            .setAttemptDirectPath(false)
            .build();
    settings.stubSettings().setTransportChannelProvider(channelProvider);
    // Set a shorter readrows deadline. Without retry cookie readRows request will get deadline
    // exceeded
    settings
        .stubSettings()
        .readRowsSettings()
        .setRetrySettings(
            RetrySettings.newBuilder()
                .setInitialRpcTimeout(Duration.ofSeconds(15))
                .setMaxRpcTimeout(Duration.ofSeconds(15))
                .setTotalTimeout(Duration.ofSeconds(15))
                .setMaxAttempts(2)
                .build());

    try (BigtableDataClient dataClient = BigtableDataClient.create(settings.build())) {
      // Send a readRows request, immediately switch the app profile from offline to online.
      // GFE will have the cached results still routing to offline AFEs. Routing cookie should
      // break this cache and route the request correctly.
      dataClient
          .readRows(Query.create(testEnvRule.env().getTableId()).limit(1))
          .iterator()
          .hasNext();
      instanceAdminClient.updateAppProfile(
          UpdateAppProfileRequest.of(testEnvRule.env().getInstanceId(), appProfileId)
              .setIsolationPolicy(AppProfile.StandardIsolationPolicy.of(AppProfile.Priority.LOW))
              .setRoutingPolicy(
                  AppProfile.SingleClusterRoutingPolicy.of(testEnvRule.env().getPrimaryClusterId()))
              .setIgnoreWarnings(true));
      dataClient
          .readRows(Query.create(testEnvRule.env().getTableId()).limit(1))
          .iterator()
          .hasNext();
    }
  }

  // This integration test verifies that when Routing Cookie is not handled, ReadRows request fails
  // after
  // switching app profile fails.
  @Test
  public void testTimeoutWithoutRoutingCookieForDataBoost() throws Exception {
    BigtableDataSettings.Builder settings = testEnvRule.env().getDataClientSettings().toBuilder();

    settings.setAppProfileId(appProfileIdFailing);
    // Disable direct path
    InstantiatingGrpcChannelProvider channelProvider =
        ((InstantiatingGrpcChannelProvider) settings.stubSettings().getTransportChannelProvider())
            .toBuilder()
            .setAttemptDirectPath(false)
            .build();
    settings.stubSettings().setTransportChannelProvider(channelProvider);
    // Set a shorter readrows deadline. Without retry cookie readRows request will get deadline
    // exceeded
    settings
        .stubSettings()
        .readRowsSettings()
        .setRetrySettings(
            RetrySettings.newBuilder()
                .setInitialRpcTimeout(Duration.ofSeconds(15))
                .setMaxRpcTimeout(Duration.ofSeconds(15))
                .setTotalTimeout(Duration.ofSeconds(15))
                .setMaxAttempts(2)
                .build());

    // We want to disable handling of routing cookie from the client but still send the feature flag
    // so server won't reject the request.
    settings.stubSettings().setEnableRoutingCookie(false);
    settings.stubSettings().setEnableRetryFeatureFlags(true);

    try (BigtableDataClient dataClient = BigtableDataClient.create(settings.build())) {
      // Routing cookie is disabled. The second readRows request should fail.
      dataClient
          .readRows(Query.create(testEnvRule.env().getTableId()).limit(1))
          .iterator()
          .hasNext();
      instanceAdminClient.updateAppProfile(
          UpdateAppProfileRequest.of(testEnvRule.env().getInstanceId(), appProfileIdFailing)
              .setIsolationPolicy(AppProfile.StandardIsolationPolicy.of(AppProfile.Priority.LOW))
              .setRoutingPolicy(
                  AppProfile.SingleClusterRoutingPolicy.of(testEnvRule.env().getPrimaryClusterId()))
              .setIgnoreWarnings(true));
      try {
        dataClient
            .readRows(Query.create(testEnvRule.env().getTableId()).limit(1))
            .iterator()
            .hasNext();
        Assert.fail("Second readRows request should fail");
      } catch (ApiException e) {
        Assert.assertTrue(
            e instanceof UnavailableException || e instanceof DeadlineExceededException);
      }
    }
  }
}
