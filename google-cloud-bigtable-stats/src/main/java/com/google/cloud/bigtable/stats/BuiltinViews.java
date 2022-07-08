/*
 * Copyright 2022 Google LLC
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
package com.google.cloud.bigtable.stats;

import com.google.api.core.InternalApi;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableSet;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewData;
import io.opencensus.stats.ViewManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** For registering built-in metric views */
@InternalApi("For internal use only")
public class BuiltinViews {
  ViewManager viewManager = Stats.getViewManager();

  @VisibleForTesting
  static final ImmutableSet<View> BIGTABLE_BUILTIN_VIEWS =
      ImmutableSet.of(
          BuiltinViewConstants.OPERATION_LATENCIES_VIEW,
          BuiltinViewConstants.ATTEMPT_LATENCIES_VIEW,
          BuiltinViewConstants.RETRY_COUNT_VIEW,
          BuiltinViewConstants.FIRST_RESPONSE_LATENCIES_VIEW,
          BuiltinViewConstants.SERVER_LATENCIES_VIEW,
          BuiltinViewConstants.CONNECTIVITY_ERROR_COUNT_VIEW,
          BuiltinViewConstants.APPLICATION_LATENCIES_VIEW,
          BuiltinViewConstants.THROTTLING_LATENCIES_VIEW);

  @VisibleForTesting
  void registerPrivateViews(ViewManager viewManager) {
    for (View view : BIGTABLE_BUILTIN_VIEWS) {
      viewManager.registerView(view);
    }
  }

  public void registerBigtableBuiltinViews() {
    for (View view : BIGTABLE_BUILTIN_VIEWS) {
      viewManager.registerView(view);
    }
  }

  @VisibleForTesting
  public List<String> getTagValueString() {
    return viewManager.getView(BuiltinViewConstants.OPERATION_LATENCIES_VIEW.getName())
            .getAggregationMap()
            .entrySet().stream()
            .map(Map.Entry::getKey)
            .flatMap(x -> x.stream())
            .map(x -> x.toString())
            .collect(Collectors.toCollection(ArrayList::new));
  }
}
