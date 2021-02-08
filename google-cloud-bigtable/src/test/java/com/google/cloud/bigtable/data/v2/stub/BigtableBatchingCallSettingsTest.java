/*
 * Copyright 2019 Google LLC
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

import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.batching.BatchingSettings;
import com.google.api.gax.batching.FlowControlSettings;
import com.google.api.gax.batching.FlowController;
import com.google.api.gax.retrying.RetrySettings;
import com.google.api.gax.rpc.StatusCode;
import com.google.cloud.bigtable.data.v2.stub.mutaterows.MutateRowsBatchingDescriptor;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Duration;

@RunWith(JUnit4.class)
public class BigtableBatchingCallSettingsTest {

  private static final BatchingSettings BATCHING_SETTINGS =
      BatchingSettings.newBuilder()
          .setElementCountThreshold(10L)
          .setRequestByteThreshold(20L)
          .setDelayThreshold(Duration.ofMillis(5))
          .setFlowControlSettings(
              FlowControlSettings.newBuilder()
                  .setMaxOutstandingElementCount(100L)
                  .setMaxOutstandingRequestBytes(100L)
                  .setLimitExceededBehavior(FlowController.LimitExceededBehavior.Block)
                  .build())
          .build();

  @Test
  public void testEmptyBuilder() {
    BigtableBatchingCallSettings.Builder builder =
        BigtableBatchingCallSettings.newBuilder(new MutateRowsBatchingDescriptor());
    assertThat(builder.getBatchingSettings()).isNull();
    assertThat(builder.getRetryableCodes()).isEmpty();
    assertThat(builder.getRetrySettings()).isNotNull();
    assertThat(builder.isLatencyBasedThrottlingEnabled()).isFalse();
    assertThat(builder.getTargetRpcLatencyMs()).isNull();
    assertThat(builder.getFlowController()).isNull();
  }

  @Test
  public void testBuilder() {
    BigtableBatchingCallSettings.Builder builder =
        BigtableBatchingCallSettings.newBuilder(new MutateRowsBatchingDescriptor());

    Set<StatusCode.Code> retryCodes = ImmutableSet.of(StatusCode.Code.UNAVAILABLE);
    RetrySettings retrySettings = RetrySettings.newBuilder().build();
    builder
        .setBatchingSettings(BATCHING_SETTINGS)
        .setRetryableCodes(retryCodes)
        .setRetrySettings(retrySettings);

    BigtableBatchingCallSettings settings = builder.build();
    assertThat(settings.getBatchingSettings()).isEqualTo(BATCHING_SETTINGS);
    assertThat(settings.getRetryableCodes()).isEqualTo(retryCodes);
    assertThat(settings.getRetrySettings()).isEqualTo(retrySettings);
    assertThat(settings.isLatencyBasedThrottlingEnabled()).isFalse();
    assertThat(settings.getTargetRpcLatencyMs()).isNull();
    assertThat(settings.getFlowController()).isNotNull();
    FlowController flowController = settings.getFlowController();

    builder.setLatencyBasedThrottling(true, 10L);
    settings = builder.build();
    assertThat(settings.isLatencyBasedThrottlingEnabled()).isTrue();
    assertThat(settings.getTargetRpcLatencyMs()).isEqualTo(10);
    assertThat(settings.getFlowController()).isNotSameInstanceAs(flowController);
    flowController = settings.getFlowController();

    builder.setLatencyBasedThrottling(false, 10L);
    settings = builder.build();
    assertThat(settings.isLatencyBasedThrottlingEnabled()).isFalse();
    assertThat(settings.getTargetRpcLatencyMs()).isNull();
    assertThat(settings.getFlowController()).isNotSameInstanceAs(flowController);
  }

  @Test
  public void testBuilderFromSettings() {
    BigtableBatchingCallSettings.Builder builder =
        BigtableBatchingCallSettings.newBuilder(new MutateRowsBatchingDescriptor());
    RetrySettings retrySettings =
        RetrySettings.newBuilder().setTotalTimeout(Duration.ofMinutes(1)).build();
    builder
        .setBatchingSettings(BATCHING_SETTINGS)
        .setRetryableCodes(StatusCode.Code.UNAVAILABLE, StatusCode.Code.UNAUTHENTICATED)
        .setRetrySettings(retrySettings)
        .setLatencyBasedThrottling(true, 10L);

    BigtableBatchingCallSettings settings = builder.build();
    BigtableBatchingCallSettings.Builder newBuilder = settings.toBuilder();

    assertThat(newBuilder.getBatchingSettings()).isEqualTo(BATCHING_SETTINGS);
    assertThat(newBuilder.getRetryableCodes())
        .containsExactly(StatusCode.Code.UNAVAILABLE, StatusCode.Code.UNAUTHENTICATED);
    assertThat(newBuilder.getRetrySettings()).isEqualTo(retrySettings);
    assertThat(newBuilder.isLatencyBasedThrottlingEnabled()).isTrue();
    assertThat(newBuilder.getTargetRpcLatencyMs()).isEqualTo(10L);
    assertThat(newBuilder.getFlowController()).isSameInstanceAs(builder.getFlowController());
  }

  @Test
  public void testFlowControllerSetUp() {
    BigtableBatchingCallSettings.Builder builder =
        BigtableBatchingCallSettings.newBuilder(new MutateRowsBatchingDescriptor());
    BatchingSettings settings =
        BatchingSettings.newBuilder()
            .setElementCountThreshold(100L)
            .setRequestByteThreshold(100L)
            .setFlowControlSettings(
                FlowControlSettings.newBuilder()
                    .setMaxOutstandingElementCount(150L)
                    .setMaxOutstandingRequestBytes(150L)
                    .build())
            .build();
    builder.setBatchingSettings(settings).setLatencyBasedThrottling(true, 10L);
    FlowController flowController = builder.build().getFlowController();
    assertThat(flowController).isNotNull();
    assertThat(flowController.getCurrentOutstandingElementCount()).isEqualTo(100);
    assertThat(flowController.getMinOutstandingElementCount()).isEqualTo(100);
    assertThat(flowController.getMaxOutstandingElementCount()).isEqualTo(150);
    assertThat(flowController.getCurrentOutstandingRequestBytes()).isEqualTo(150);
    assertThat(flowController.getMinOutstandingRequestBytes()).isEqualTo(150);
    assertThat(flowController.getMaxOutstandingRequestBytes()).isEqualTo(150);
  }

  @Test
  public void testMandatorySettings() {
    Exception actualEx = null;
    try {
      BigtableBatchingCallSettings.newBuilder(null);
    } catch (Exception ex) {
      actualEx = ex;
    }
    assertThat(actualEx).isInstanceOf(NullPointerException.class);
    actualEx = null;
    try {
      BigtableBatchingCallSettings.newBuilder(new MutateRowsBatchingDescriptor()).build();
    } catch (Exception ex) {
      actualEx = ex;
    }
    assertThat(actualEx).isInstanceOf(IllegalStateException.class);
  }
}
