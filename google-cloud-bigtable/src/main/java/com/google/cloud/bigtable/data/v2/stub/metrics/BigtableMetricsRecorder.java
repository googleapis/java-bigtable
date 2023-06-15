/*
 * Copyright 2023 Google LLC
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

import io.opentelemetry.api.common.Attributes;

public class BigtableMetricsRecorder {

  void recordOperationLatencies(long value, Attributes attributes) {}

  void recordAttemptLatencies(long value, Attributes attributes) {}

  void recordFirstResponseLatencies(long value, Attributes attributes) {}

  void recordRetryCount(long value, Attributes attributes) {}

  void recordServerLatencies(long value, Attributes attributes) {}

  void recordConnectivityErrorCount(long value, Attributes attributes) {}

  void recordApplicationBlockingLatencies(long value, Attributes attributes) {}

  void recordClientBlockingLatencies(long value, Attributes attributes) {}
}
