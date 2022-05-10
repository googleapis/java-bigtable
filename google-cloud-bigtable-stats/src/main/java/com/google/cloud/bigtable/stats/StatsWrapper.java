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
import com.google.common.collect.ImmutableMap;
import io.opencensus.impl.stats.StatsComponentImpl;
import io.opencensus.stats.AggregationData;
import io.opencensus.stats.Stats;
import io.opencensus.stats.StatsComponent;
import io.opencensus.stats.StatsRecorder;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewData;
import io.opencensus.stats.ViewManager;
import io.opencensus.tags.TagKey;
import io.opencensus.tags.TagValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Wrapper class for accessing opencensus * */
@InternalApi("For internal use only")
public class StatsWrapper {

  private final StatsComponent component;
  private final boolean useLocalRecorder;

  public StatsWrapper(boolean useLocalRecorder) {
    this.component = new StatsComponentImpl();
    this.useLocalRecorder = useLocalRecorder;
  }

  StatsRecorder getStatsRecorder() {
    if (!useLocalRecorder) {
      return Stats.getStatsRecorder();
    } else {
      return component.getStatsRecorder();
    }
  }

  ViewManager getViewManager() {
    if (!useLocalRecorder) {
      return Stats.getViewManager();
    } else {
      return component.getViewManager();
    }
  }

  long getAggregationValueAsLong(
      View view,
      ImmutableMap<TagKey, String> tags,
      String projectId,
      String instanceId,
      String appProfileId) {
    ViewData viewData = getViewManager().getView(view.getName());
    Map<List<TagValue>, AggregationData> aggregationMap =
        Objects.requireNonNull(viewData).getAggregationMap();

    List<TagValue> tagValues = new ArrayList<>();

    for (TagKey column : view.getColumns()) {
      if (BuiltinMeasureConstants.PROJECT_ID == column) {
        tagValues.add(TagValue.create(projectId));
      } else if (BuiltinMeasureConstants.INSTANCE_ID == column) {
        tagValues.add(TagValue.create(instanceId));
      } else if (BuiltinMeasureConstants.APP_PROFILE == column) {
        tagValues.add(TagValue.create(appProfileId));
      } else {
        tagValues.add(TagValue.create(tags.get(column)));
      }
    }

    AggregationData aggregationData = aggregationMap.get(tagValues);

    return aggregationData.match(
        new io.opencensus.common.Function<AggregationData.SumDataDouble, Long>() {
          @Override
          public Long apply(AggregationData.SumDataDouble arg) {
            return (long) arg.getSum();
          }
        },
        new io.opencensus.common.Function<AggregationData.SumDataLong, Long>() {
          @Override
          public Long apply(AggregationData.SumDataLong arg) {
            return arg.getSum();
          }
        },
        new io.opencensus.common.Function<AggregationData.CountData, Long>() {
          @Override
          public Long apply(AggregationData.CountData arg) {
            return arg.getCount();
          }
        },
        new io.opencensus.common.Function<AggregationData.DistributionData, Long>() {
          @Override
          public Long apply(AggregationData.DistributionData arg) {
            return (long) arg.getMean();
          }
        },
        new io.opencensus.common.Function<AggregationData.LastValueDataDouble, Long>() {
          @Override
          public Long apply(AggregationData.LastValueDataDouble arg) {
            return (long) arg.getLastValue();
          }
        },
        new io.opencensus.common.Function<AggregationData.LastValueDataLong, Long>() {
          @Override
          public Long apply(AggregationData.LastValueDataLong arg) {
            return arg.getLastValue();
          }
        },
        new io.opencensus.common.Function<AggregationData, Long>() {
          @Override
          public Long apply(AggregationData arg) {
            throw new UnsupportedOperationException();
          }
        });
  }
}
