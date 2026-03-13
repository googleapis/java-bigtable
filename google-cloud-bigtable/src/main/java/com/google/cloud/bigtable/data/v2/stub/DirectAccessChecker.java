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
package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.InternalApi;
import com.google.cloud.bigtable.data.v2.internal.csm.tracers.DirectPathCompatibleTracer;
import javax.annotation.Nullable;

@InternalApi
/* Evaluates whether a given channel supports Direct Access. */
public interface DirectAccessChecker {
  /**
   * Evaluates if Direct Access is available by creating a test channel.
   *
   * @param channelFactory A factory to create the test channel
   * @return true if the channel is eligible for Direct Access
   */
  boolean check(BigtableChannelFactory channelFactory, @Nullable DirectPathCompatibleTracer tracer);
}
