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

import com.google.api.core.ApiFuture;
import com.google.api.gax.grpc.GrpcResponseMetadata;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.stats.BuiltinViews;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.common.collect.ImmutableList;
import com.google.protobuf.ByteString;
import io.grpc.Metadata;
import io.opencensus.stats.AggregationData;
import io.opencensus.stats.Stats;
import io.opencensus.stats.View;
import io.opencensus.stats.ViewData;
import io.opencensus.stats.ViewManager;
import io.opencensus.tags.TagKey;
import io.opencensus.tags.TagValue;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.google.common.truth.Truth.assertThat;

public class UnaryTrailerIT {
    @ClassRule
    public static TestEnvRule testEnvRule = new TestEnvRule();

    @Test
    public void test() throws Exception {
        BuiltinViews.registerBigtableBuiltinViews();

        String rowKey = UUID.randomUUID().toString();
        String familyId = testEnvRule.env().getFamilyId();


        ApiFuture<Void> future =
                testEnvRule
                        .env()
                        .getDataClient()
                        .mutateRowCallable()
                        .futureCall(RowMutation.create(testEnvRule.env().getTableId(), rowKey)
                                .setCell(familyId, "q", "myVal"));

        future.get(1, TimeUnit.MINUTES);

        Thread.sleep(1000);
        ViewManager viewManager = Stats.getViewManager();
        ViewData viewData = viewManager.getView(View.Name.create("bigtable.googleapis.com/internal/client/operation_latencies"));

        int clusterIndex = viewData.getView().getColumns().indexOf(TagKey.create("cluster"));
        int zoneIndex = viewData.getView().getColumns().indexOf(TagKey.create("zone"));

        List<TagValue> tagValues = viewData.getAggregationMap().entrySet().stream().findFirst().get().getKey();
        assertThat(tagValues.get(zoneIndex).asString()).isEqualTo(testEnvRule.env().getPrimaryZone());

        assertThat(tagValues.get(clusterIndex).asString()).isEqualTo(testEnvRule.env().getPrimaryClusterId());
    }
}
