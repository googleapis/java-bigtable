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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsTestUtils.getAggregatedValue;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsTestUtils.getMetricData;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsTestUtils.getStartTimeSeconds;
import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsTestUtils.verifyAttributes;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static com.google.common.truth.TruthJUnit.assume;

import com.google.api.client.util.Lists;
import com.google.cloud.bigtable.admin.v2.BigtableInstanceAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.models.AppProfile;
import com.google.cloud.bigtable.admin.v2.models.CreateAppProfileRequest;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.admin.v2.models.Table;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants;
import com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsView;
import com.google.cloud.bigtable.test_helpers.env.EmulatorEnv;
import com.google.cloud.bigtable.test_helpers.env.PrefixGenerator;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.cloud.monitoring.v3.MetricServiceClient;
import com.google.common.base.Stopwatch;
import com.google.common.collect.BoundType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Range;
import com.google.monitoring.v3.ListTimeSeriesRequest;
import com.google.monitoring.v3.ListTimeSeriesResponse;
import com.google.monitoring.v3.Point;
import com.google.monitoring.v3.ProjectName;
import com.google.monitoring.v3.TimeInterval;
import com.google.monitoring.v3.TimeSeries;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProviderBuilder;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.testing.exporter.InMemoryMetricReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Duration;

@RunWith(JUnit4.class)
public class BuiltinMetricsIT {
  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  private static final Logger logger = Logger.getLogger(BuiltinMetricsIT.class.getName());

  @Rule public Timeout globalTimeout = Timeout.seconds(900);

  private Table tableCustomOtel;
  private Table tableDefault;
  private BigtableDataClient clientCustomOtel;
  private BigtableDataClient clientDefault;
  private BigtableTableAdminClient tableAdminClient;
  private BigtableInstanceAdminClient instanceAdminClient;
  private MetricServiceClient metricClient;

  private InMemoryMetricReader metricReader;
  private String appProfileCustomOtel;
  private String appProfileDefault;

  public static String[] VIEWS = {
    "operation_latencies",
    "attempt_latencies",
    "connectivity_error_count",
    "application_blocking_latencies",
  };

  @Before
  public void setup() throws IOException {
    assume()
        .withMessage("Builtin metrics integration test is not supported by emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    // Create a cloud monitoring client
    metricClient = MetricServiceClient.create();

    tableAdminClient = testEnvRule.env().getTableAdminClient();
    instanceAdminClient = testEnvRule.env().getInstanceAdminClient();
    appProfileCustomOtel = PrefixGenerator.newPrefix("test1");
    appProfileDefault = PrefixGenerator.newPrefix("test2");
    instanceAdminClient.createAppProfile(
        CreateAppProfileRequest.of(testEnvRule.env().getInstanceId(), appProfileCustomOtel)
            .setRoutingPolicy(
                AppProfile.SingleClusterRoutingPolicy.of(testEnvRule.env().getPrimaryClusterId()))
            .setIsolationPolicy(AppProfile.StandardIsolationPolicy.of(AppProfile.Priority.LOW)));
    instanceAdminClient.createAppProfile(
        CreateAppProfileRequest.of(testEnvRule.env().getInstanceId(), appProfileDefault)
            .setRoutingPolicy(
                AppProfile.SingleClusterRoutingPolicy.of(testEnvRule.env().getPrimaryClusterId()))
            .setIsolationPolicy(AppProfile.StandardIsolationPolicy.of(AppProfile.Priority.LOW)));

    metricReader = InMemoryMetricReader.create();

    SdkMeterProviderBuilder meterProvider =
        SdkMeterProvider.builder().registerMetricReader(metricReader);
    BuiltinMetricsView.registerBuiltinMetrics(testEnvRule.env().getProjectId(), meterProvider);
    OpenTelemetry openTelemetry =
        OpenTelemetrySdk.builder().setMeterProvider(meterProvider.build()).build();

    BigtableDataSettings.Builder settings = testEnvRule.env().getDataClientSettings().toBuilder();

    clientCustomOtel =
        BigtableDataClient.create(
            settings.setOpenTelemetry(openTelemetry).setAppProfileId(appProfileCustomOtel).build());
    clientDefault = BigtableDataClient.create(settings.setAppProfileId(appProfileDefault).build());
  }

  @After
  public void tearDown() {
    if (metricClient != null) {
      metricClient.close();
    }
    if (tableCustomOtel != null) {
      tableAdminClient.deleteTable(tableCustomOtel.getId());
    }
    if (tableDefault != null) {
      tableAdminClient.deleteTable(tableDefault.getId());
    }
    if (instanceAdminClient != null) {
      instanceAdminClient.deleteAppProfile(
          testEnvRule.env().getInstanceId(), appProfileCustomOtel, true);
    }
    if (clientCustomOtel != null) {
      clientCustomOtel.close();
    }
    if (clientDefault != null) {
      clientDefault.close();
    }
  }

  @Test
  public void testBuiltinMetricsWithDefaultOTEL() throws Exception {
    logger.info("Started testing builtin metrics with default OTEL");
    tableDefault =
        tableAdminClient.createTable(
            CreateTableRequest.of(PrefixGenerator.newPrefix("BuiltinMetricsIT#test1"))
                .addFamily("cf"));
    logger.info("Create default table: " + tableDefault.getId());
    clientDefault.mutateRow(
        RowMutation.create(tableDefault.getId(), "a-new-key").setCell("cf", "q", "abc"));
    ArrayList<Row> rows =
        Lists.newArrayList(clientDefault.readRows(Query.create(tableDefault.getId()).limit(10)));

    Stopwatch stopwatch = Stopwatch.createStarted();

    ProjectName name = ProjectName.of(testEnvRule.env().getProjectId());

    Collection<MetricData> fromMetricReader = metricReader.collectAllMetrics();

    // Restrict time to last 10 minutes and 5 minutes after the request
    long startMillis = System.currentTimeMillis() - Duration.ofMinutes(10).toMillis();
    long endMillis = startMillis + Duration.ofMinutes(15).toMillis();
    TimeInterval interval =
        TimeInterval.newBuilder()
            .setStartTime(Timestamps.fromMillis(startMillis))
            .setEndTime(Timestamps.fromMillis(endMillis))
            .build();

    for (String view : VIEWS) {
      // Filter on instance and method name
      // Verify that metrics are published for MutateRow request
      String metricFilter =
          String.format(
              "metric.type=\"bigtable.googleapis.com/client/%s\" "
                  + "AND resource.labels.instance=\"%s\" AND metric.labels.method=\"Bigtable.MutateRow\""
                  + " AND resource.labels.table=\"%s\" AND metric.labels.app_profile=\"%s\"",
              view, testEnvRule.env().getInstanceId(), tableDefault.getId(), appProfileDefault);
      ListTimeSeriesRequest.Builder requestBuilder =
          ListTimeSeriesRequest.newBuilder()
              .setName(name.toString())
              .setFilter(metricFilter)
              .setInterval(interval)
              .setView(ListTimeSeriesRequest.TimeSeriesView.FULL);
      verifyMetrics(requestBuilder.build(), stopwatch, view, null);

      // Verify that metrics are published for ReadRows request
      metricFilter =
          String.format(
              "metric.type=\"bigtable.googleapis.com/client/%s\" "
                  + "AND resource.labels.instance=\"%s\" AND metric.labels.method=\"Bigtable.ReadRows\""
                  + " AND resource.labels.table=\"%s\" AND metric.labels.app_profile=\"%s\"",
              view, testEnvRule.env().getInstanceId(), tableDefault.getId(), appProfileDefault);
      requestBuilder.setFilter(metricFilter);

      verifyMetrics(requestBuilder.build(), stopwatch, view, null);
    }
  }

  @Test
  public void testBuiltinMetricsWithCustomOTEL() throws Exception {
    logger.info("Started testing builtin metrics with custom OTEL");
    tableCustomOtel =
        tableAdminClient.createTable(
            CreateTableRequest.of(PrefixGenerator.newPrefix("BuiltinMetricsIT#test2"))
                .addFamily("cf"));
    logger.info("Create custom table: " + tableCustomOtel.getId());
    // Send a MutateRow and ReadRows request
    clientCustomOtel.mutateRow(
        RowMutation.create(tableCustomOtel.getId(), "a-new-key").setCell("cf", "q", "abc"));
    ArrayList<Row> rows =
        Lists.newArrayList(
            clientCustomOtel.readRows(Query.create(tableCustomOtel.getId()).limit(10)));

    Stopwatch stopwatch = Stopwatch.createStarted();

    ProjectName name = ProjectName.of(testEnvRule.env().getProjectId());

    Collection<MetricData> fromMetricReader = metricReader.collectAllMetrics();

    // Restrict time to last 10 minutes and 5 minutes after the request
    long startMillis = System.currentTimeMillis() - Duration.ofMinutes(10).toMillis();
    long endMillis = startMillis + Duration.ofMinutes(15).toMillis();
    TimeInterval interval =
        TimeInterval.newBuilder()
            .setStartTime(Timestamps.fromMillis(startMillis))
            .setEndTime(Timestamps.fromMillis(endMillis))
            .build();

    for (String view : VIEWS) {
      String otelMetricName = "bigtable.googleapis.com/internal/client/" + view;
      if (view.equals("application_blocking_latencies")) {
        otelMetricName = "bigtable.googleapis.com/internal/client/application_latencies";
      }
      MetricData dataFromReader = getMetricData(fromMetricReader, otelMetricName);

      // Filter on instance and method name
      // Verify that metrics are correct for MutateRows request
      String metricFilter =
          String.format(
              "metric.type=\"bigtable.googleapis.com/client/%s\" "
                  + "AND resource.labels.instance=\"%s\" AND metric.labels.method=\"Bigtable.MutateRow\""
                  + " AND resource.labels.table=\"%s\" AND metric.labels.app_profile=\"%s\"",
              view,
              testEnvRule.env().getInstanceId(),
              tableCustomOtel.getId(),
              appProfileCustomOtel);
      ListTimeSeriesRequest.Builder requestBuilder =
          ListTimeSeriesRequest.newBuilder()
              .setName(name.toString())
              .setFilter(metricFilter)
              .setInterval(interval)
              .setView(ListTimeSeriesRequest.TimeSeriesView.FULL);

      verifyMetrics(requestBuilder.build(), stopwatch, view, dataFromReader);

      // Verify that metrics are correct for ReadRows request
      metricFilter =
          String.format(
              "metric.type=\"bigtable.googleapis.com/client/%s\" "
                  + "AND resource.labels.instance=\"%s\" AND metric.labels.method=\"Bigtable.ReadRows\""
                  + " AND resource.labels.table=\"%s\" AND metric.labels.app_profile=\"%s\"",
              view,
              testEnvRule.env().getInstanceId(),
              tableCustomOtel.getId(),
              appProfileCustomOtel);
      requestBuilder.setFilter(metricFilter);

      verifyMetrics(requestBuilder.build(), stopwatch, view, dataFromReader);
    }
  }

  private void verifyMetrics(
      ListTimeSeriesRequest request, Stopwatch stopwatch, String view, MetricData dataFromReader)
      throws Exception {
    ListTimeSeriesResponse response = metricClient.listTimeSeriesCallable().call(request);
    logger.log(
        Level.INFO,
        "Checking for view "
            + view
            + ", has timeseries="
            + response.getTimeSeriesCount()
            + " stopwatch elapsed "
            + stopwatch.elapsed(TimeUnit.MINUTES));
    while (response.getTimeSeriesCount() == 0 && stopwatch.elapsed(TimeUnit.MINUTES) < 10) {
      // Call listTimeSeries every minute
      Thread.sleep(Duration.ofMinutes(1).toMillis());
      response = metricClient.listTimeSeriesCallable().call(request);
    }

    assertWithMessage("View " + view + " didn't return any data.")
        .that(response.getTimeSeriesCount())
        .isGreaterThan(0);

    // Compare metric data with in memory metrics reader data if present
    if (dataFromReader != null) {
      for (TimeSeries ts : response.getTimeSeriesList()) {
        Map<String, String> attributesMap =
            ImmutableMap.<String, String>builder()
                .putAll(ts.getResource().getLabelsMap())
                .putAll(ts.getMetric().getLabelsMap())
                .build();
        AttributesBuilder attributesBuilder = Attributes.builder();
        String streamingKey = BuiltinMetricsConstants.STREAMING.getKey();
        attributesMap.forEach(
            (k, v) -> {
              if (!k.equals(streamingKey)) {
                attributesBuilder.put(k, v);
              }
            });
        if (attributesMap.containsKey(streamingKey)) {
          attributesBuilder.put(
              streamingKey, Boolean.parseBoolean(attributesMap.get(streamingKey)));
        }
        Attributes attributes = attributesBuilder.build();
        verifyAttributes(dataFromReader, attributes);
        long expectedValue = getAggregatedValue(dataFromReader, attributes);
        Timestamp startTime = getStartTimeSeconds(dataFromReader, attributes);
        assertThat(startTime.getSeconds()).isGreaterThan(0);
        List<Point> point =
            ts.getPointsList().stream()
                .filter(
                    p ->
                        Timestamps.between(p.getInterval().getStartTime(), startTime).getSeconds()
                            < 60)
                .collect(Collectors.toList());
        if (point.size() > 0) {
          long actualValue = (long) point.get(0).getValue().getDistributionValue().getMean();
          assertWithMessage(
                  "actual value does not match expected value, actual value "
                      + actualValue
                      + " expected value "
                      + expectedValue
                      + " actual start time "
                      + point.get(0).getInterval().getStartTime()
                      + " expected start time "
                      + startTime)
              .that(actualValue)
              .isIn(
                  Range.range(
                      expectedValue - 1, BoundType.CLOSED, expectedValue + 1, BoundType.CLOSED));
        }
      }
    }
  }
}
