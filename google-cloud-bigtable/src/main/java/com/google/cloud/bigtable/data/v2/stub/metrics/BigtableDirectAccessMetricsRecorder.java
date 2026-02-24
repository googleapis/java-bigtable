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

package com.google.cloud.bigtable.data.v2.stub.metrics;

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.DIRECT_ACCESS_COMPATIBLE_NAME;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.METER_NAME;

import com.google.api.core.InternalApi;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.LongGauge;
import io.opentelemetry.api.metrics.Meter;

@InternalApi
public class BigtableDirectAccessMetricsRecorder implements DirectAccessMetricsRecorder {
  private final LongGauge compatibleGauge;

  public BigtableDirectAccessMetricsRecorder(OpenTelemetry openTelemetry) {
    Meter meter = openTelemetry.getMeter(METER_NAME);
    this.compatibleGauge =
        meter
            .gaugeBuilder(DIRECT_ACCESS_COMPATIBLE_NAME)
            .ofLongs()
            .setDescription(
                "Reports 1 if the environment is eligible for DirectPath, 0 otherwise. Based on an attempt at startup.")
            .setUnit("1")
            .build();
  }

  @Override
  public void recordEligibility(boolean isEligible) {
    if (compatibleGauge != null) {
      // Record 1 if eligible, 0 otherwise
      compatibleGauge.set(isEligible ? 1L : 0L);
    }
  }
}
