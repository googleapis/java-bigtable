/*
 * Copyright 2021 Google LLC
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

import com.google.api.core.InternalApi;
import com.google.bigtable.veneer.repackaged.io.opencensus.stats.Stats;
import com.google.bigtable.veneer.repackaged.io.opencensus.stats.View;
import com.google.bigtable.veneer.repackaged.io.opencensus.stats.ViewManager;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;

@InternalApi
public class BuiltinViews {
  private static final ImmutableSet<View> BIGTABLE_BUILTIN_VIEWS =
      ImmutableSet.of(
          BuiltinViewConstants.OPERATION_LATENCIES_VIEW,
          BuiltinViewConstants.ATTEMPT_LATENCIES_VIEW,
          BuiltinViewConstants.RETRY_COUNT_VIEW,
          BuiltinViewConstants.FIRST_RESPONSE_LATENCIES_VIEW,
          BuiltinViewConstants.SERVER_LATENCIES_VIEW,
          BuiltinViewConstants.CONNECTIVITY_ERROR_COUNT_VIEW,
          BuiltinViewConstants.APPLICATION_LATENCIES_VIEW,
          BuiltinViewConstants.THROTTLING_LATENCIES_VIEW);

  public void registerBigtableBuiltinViews() {
    registerBigtableBuiltinViews(Stats.getViewManager());
  }

  @VisibleForTesting
  public static void registerBigtableBuiltinViews(ViewManager viewManager) {
    for (View view : BIGTABLE_BUILTIN_VIEWS) {
      viewManager.registerView(view);
    }
  }
}
