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
package com.google.cloud.bigtable.data.v2.it;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.bigtable.admin.v2.models.Cluster;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.stats.BuiltinViews;
import com.google.cloud.bigtable.test_helpers.env.EmulatorEnv;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.common.collect.Lists;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewData;
import io.opencensus.stats.ViewManager;
import io.opencensus.tags.TagValue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class StreamingMetricsMetadataIT {
  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  static BuiltinViews builtinViews = new BuiltinViews();


  @BeforeClass
  public static void setUpClass() {
    assume()
        .withMessage("StreamingMetricsMetadataIT is not supported on Emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);
    builtinViews.registerBigtableBuiltinViews();
  }

  @Test
  public void testSuccess() throws Exception {
    String prefix = UUID.randomUUID().toString();
    String uniqueKey = prefix + "-read";

    Query query = Query.create(testEnvRule.env().getTableId()).rowKey(uniqueKey);
    ArrayList<Row> rows = Lists.newArrayList(testEnvRule.env().getDataClient().readRows(query));

    // give opencensus some time to populate view data
    Thread.sleep(100);

//    ViewManager viewManager = Stats.getViewManager();
    System.out.println("exported views: " + builtinViews.getTagValueString());


//    ViewData viewData =
//        viewManager.getView(
//            View.Name.create("bigtable.googleapis.com/internal/client/operation_latencies"));

//    List<TagValue> tagValues =
//        viewData.getAggregationMap().entrySet().stream()
//            .map(Map.Entry::getKey)
//            .flatMap(x -> x.stream())
//            .collect(Collectors.toCollection(ArrayList::new));

    builtinViews.getTagValueString();

    ApiFuture<List<Cluster>> clustersFuture =
        testEnvRule
            .env()
            .getInstanceAdminClient()
            .listClustersAsync(testEnvRule.env().getInstanceId());
    List<Cluster> clusters = clustersFuture.get(1, TimeUnit.MINUTES);

//    assertThat(builtinViews.getTagValueString()).contains(TagValue.create(clusters.get(0).getZone()));
//    assertThat(builtinViews.getTagValueString()).contains(TagValue.create(clusters.get(0).getId()));
    assertThat(builtinViews.getTagValueString()).contains(clusters.get(0).getZone());
    assertThat(builtinViews.getTagValueString()).contains(clusters.get(0).getId());
  }

  @Test
  public void testFailure() throws InterruptedException {
    Query query = Query.create("non-exist-table");
    try {
      Lists.newArrayList(testEnvRule.env().getDataClient().readRows(query));
    } catch (NotFoundException e) {
    }

    // give opencensus some time to populate view data
    Thread.sleep(100);
//
//    ViewManager viewManager = Stats.getViewManager();
//    ViewData viewData =
//        viewManager.getView(
//            View.Name.create("bigtable.googleapis.com/internal/client/operation_latencies"));
//
//    List<TagValue> tagValues =
//        viewData.getAggregationMap().entrySet().stream()
//            .map(Map.Entry::getKey)
//            .flatMap(x -> x.stream())
//            .collect(Collectors.toCollection(ArrayList::new));
//
//    assertThat(tagValues).contains(TagValue.create("undefined"));
//    assertThat(tagValues).contains(TagValue.create("undefined"));
  }
}
