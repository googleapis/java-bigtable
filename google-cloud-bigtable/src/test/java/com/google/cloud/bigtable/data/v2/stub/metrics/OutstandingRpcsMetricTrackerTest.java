/*
 * Copyright 2025 Google LLC
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

import static com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants.OUTSTANDING_RPCS_PER_CHANNEL_NAME;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelObserver;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelPoolObserver;
import com.google.common.collect.ImmutableList;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.data.HistogramPointData;
import io.opentelemetry.sdk.metrics.data.MetricData;
import io.opentelemetry.sdk.testing.exporter.InMemoryMetricReader;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

@RunWith(JUnit4.class)
public class OutstandingRpcsMetricTrackerTest {

  @Rule public final MockitoRule mockito = MockitoJUnit.rule();

  private InMemoryMetricReader metricReader;
  @Mock private ScheduledExecutorService mockScheduler;
  private ArgumentCaptor<Runnable> runnableCaptor;

  private OutstandingRpcsMetricTracker tracker;

  @Mock private BigtableChannelPoolObserver mockInsightsProvider;
  @Mock private BigtableChannelObserver mockInsight1;
  @Mock private BigtableChannelObserver mockInsight2;

  @Before
  public void setUp() {
    metricReader = InMemoryMetricReader.create();
    SdkMeterProvider meterProvider =
        SdkMeterProvider.builder().registerMetricReader(metricReader).build();
    OpenTelemetry openTelemetry =
        OpenTelemetrySdk.builder().setMeterProvider(meterProvider).build();

    tracker = new OutstandingRpcsMetricTracker(openTelemetry);

    runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
    // Configure mockScheduler to capture the runnable when tracker.start() is called
    when(mockScheduler.scheduleAtFixedRate(runnableCaptor.capture(), anyLong(), anyLong(), any()))
        .then((Answer<ScheduledFuture<?>>) invocation -> Mockito.mock(ScheduledFuture.class));

    // Default stubbing for insights provider
    List<BigtableChannelObserver> defaultInsights = ImmutableList.of(mockInsight1, mockInsight2);
    when(mockInsightsProvider.getChannelInfos()).thenAnswer(invocation -> defaultInsights);
  }

  /** Helper to run the captured OutstandingRpcsMetricTracker task. */
  void runTrackerTask() {
    List<Runnable> capturedRunnables = runnableCaptor.getAllValues();
    assertThat(capturedRunnables).hasSize(1); // Expect only one task scheduled
    Runnable trackerRunnable = capturedRunnables.get(0);
    assertThat(trackerRunnable).isInstanceOf(OutstandingRpcsMetricTracker.class);
    trackerRunnable.run();
  }

  private static HistogramPointData getPointForStreaming(
      Collection<HistogramPointData> points, boolean streaming) {
    return points.stream()
        .filter(
            p ->
                Boolean.TRUE.equals(p.getAttributes().get(AttributeKey.booleanKey("streaming")))
                    == streaming)
        .findFirst()
        .orElseThrow(
            () -> new AssertionError("Missing HistogramPointData for streaming=" + streaming));
  }

  /** Helper to create expected Attributes for assertions. */
  private static Attributes getExpectedAttributes(String lbPolicy, boolean streaming) {
    return Attributes.builder()
        .put(AttributeKey.stringKey("transport_type"), "grpc")
        .put(AttributeKey.stringKey("lb_policy"), lbPolicy)
        .put(AttributeKey.booleanKey("streaming"), streaming)
        .build();
  }

  @Test
  public void testSingleRun() {
    // Arrange
    tracker.registerChannelInsightsProvider(mockInsightsProvider);
    tracker.registerLoadBalancingStrategy("LEAST_IN_FLIGHT");
    tracker.start(mockScheduler);

    when(mockInsight1.getOutstandingUnaryRpcs()).thenReturn(5);
    when(mockInsight1.getOutstandingStreamingRpcs()).thenReturn(2);
    when(mockInsight2.getOutstandingUnaryRpcs()).thenReturn(10);
    when(mockInsight2.getOutstandingStreamingRpcs()).thenReturn(8);

    runTrackerTask();

    // Assert
    Collection<MetricData> metrics = metricReader.collectAllMetrics();
    assertThat(metrics).hasSize(1);
    MetricData metricData = metrics.iterator().next();
    assertThat(metricData.getName()).isEqualTo(OUTSTANDING_RPCS_PER_CHANNEL_NAME);

    Collection<HistogramPointData> points = metricData.getHistogramData().getPoints();
    assertThat(points).hasSize(2); // One for streaming=false, one for streaming=true

    // Assert unary point (streaming=false)
    HistogramPointData unaryPoint = getPointForStreaming(points, false);
    assertThat(unaryPoint.getAttributes())
        .isEqualTo(getExpectedAttributes("LEAST_IN_FLIGHT", false));
    assertThat(unaryPoint.getCount()).isEqualTo(2); // Two insights
    assertThat(unaryPoint.getSum()).isWithin(1e-9).of(15.0); // 5 + 10 = 15

    // Assert streaming point (streaming=true)
    HistogramPointData streamingPoint = getPointForStreaming(points, true);
    assertThat(streamingPoint.getAttributes())
        .isEqualTo(getExpectedAttributes("LEAST_IN_FLIGHT", true));
    assertThat(streamingPoint.getCount()).isEqualTo(2); // Two insights
    assertThat(streamingPoint.getSum()).isWithin(1e-9).of(10.0); // 2 + 8 = 10
  }

  @Test
  public void testMultipleRuns() {
    // Arrange
    tracker.registerChannelInsightsProvider(mockInsightsProvider);
    tracker.registerLoadBalancingStrategy("ROUND_ROBIN");
    tracker.start(mockScheduler);

    // First run
    when(mockInsight1.getOutstandingUnaryRpcs()).thenReturn(1);
    when(mockInsight1.getOutstandingStreamingRpcs()).thenReturn(2);
    when(mockInsight2.getOutstandingUnaryRpcs()).thenReturn(3);
    when(mockInsight2.getOutstandingStreamingRpcs()).thenReturn(4);
    runTrackerTask();

    // Second run - values change
    when(mockInsight1.getOutstandingUnaryRpcs()).thenReturn(10);
    when(mockInsight1.getOutstandingStreamingRpcs()).thenReturn(20);
    when(mockInsight2.getOutstandingUnaryRpcs()).thenReturn(30);
    when(mockInsight2.getOutstandingStreamingRpcs()).thenReturn(40);
    runTrackerTask();

    // Assert cumulative metrics
    Collection<MetricData> metrics = metricReader.collectAllMetrics();
    assertThat(metrics).hasSize(1);
    MetricData metricData = metrics.iterator().next();
    Collection<HistogramPointData> points = metricData.getHistogramData().getPoints();
    assertThat(points).hasSize(2);

    // Assert unary point (streaming=false)
    HistogramPointData unaryPoint = getPointForStreaming(points, false);
    assertThat(unaryPoint.getAttributes()).isEqualTo(getExpectedAttributes("ROUND_ROBIN", false));
    assertThat(unaryPoint.getCount()).isEqualTo(4); // 2 insights * 2 runs
    assertThat(unaryPoint.getSum()).isWithin(1e-9).of(44.0); // (1 + 3) + (10 + 30) = 44

    // Assert streaming point (streaming=true)
    HistogramPointData streamingPoint = getPointForStreaming(points, true);
    assertThat(streamingPoint.getAttributes())
        .isEqualTo(getExpectedAttributes("ROUND_ROBIN", true));
    assertThat(streamingPoint.getCount()).isEqualTo(4); // 2 insights * 2 runs
    assertThat(streamingPoint.getSum()).isWithin(1e-9).of(66.0); // (2 + 4) + (20 + 40) = 66
  }

  @Test
  public void testDefaultLbPolicy() {
    // Arrange: Only register insights provider, not LB strategy
    tracker.registerChannelInsightsProvider(mockInsightsProvider);
    tracker.start(mockScheduler);

    when(mockInsight1.getOutstandingUnaryRpcs()).thenReturn(1);
    when(mockInsight1.getOutstandingStreamingRpcs()).thenReturn(1);

    // Act
    runTrackerTask();

    // Assert
    Collection<MetricData> metrics = metricReader.collectAllMetrics();
    assertThat(metrics).hasSize(1);
    MetricData metricData = metrics.iterator().next();
    Collection<HistogramPointData> points = metricData.getHistogramData().getPoints();
    assertThat(points).hasSize(2);

    // Verify both unary and streaming points use the default "ROUND_ROBIN"
    points.forEach(
        point ->
            assertThat(point.getAttributes().asMap())
                .containsEntry(AttributeKey.stringKey("lb_policy"), "ROUND_ROBIN"));
  }

  @Test
  public void testNoMetricsIfChannelInsightsProviderInactive() {
    // Arrange
    tracker.registerLoadBalancingStrategy("POWER_OF_TWO_LEAST_IN_FLIGHT");
    // Do not call registerChannelInsightsProvider
    tracker.start(mockScheduler);

    when(mockInsight1.getOutstandingUnaryRpcs()).thenReturn(1);
    runTrackerTask();

    // Assert: No metrics should be collected if the insights provider is not registered.
    assertThat(metricReader.collectAllMetrics()).isEmpty();
  }

  @Test
  public void testNoMetricsIfChannelInsightsEmpty() {
    // Arrange
    tracker.registerChannelInsightsProvider(mockInsightsProvider);
    tracker.registerLoadBalancingStrategy("LEAST_IN_FLIGHT");
    when(mockInsightsProvider.getChannelInfos()).thenReturn(ImmutableList.of()); // Empty list
    tracker.start(mockScheduler);

    // Act
    runTrackerTask();

    // Assert: No metrics should be collected if the insights provider returns an empty list.
    assertThat(metricReader.collectAllMetrics()).isEmpty();
  }

  @Test
  public void testNoMetricsIfChannelInsightsNull() {
    // Arrange
    tracker.registerChannelInsightsProvider(mockInsightsProvider);
    tracker.registerLoadBalancingStrategy("LEAST_IN_FLIGHT");
    when(mockInsightsProvider.getChannelInfos()).thenReturn(null); // Null list
    tracker.start(mockScheduler);

    // Act
    runTrackerTask();

    // Assert: No metrics should be collected if the insights provider returns null.
    assertThat(metricReader.collectAllMetrics()).isEmpty();
  }
}
