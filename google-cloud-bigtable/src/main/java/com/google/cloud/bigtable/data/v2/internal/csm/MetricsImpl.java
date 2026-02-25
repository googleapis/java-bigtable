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
package com.google.cloud.bigtable.data.v2.internal.csm;

import com.google.api.gax.grpc.GaxGrpcProperties;
import com.google.api.gax.tracing.ApiTracerFactory;
import com.google.api.gax.tracing.OpencensusTracerFactory;
import com.google.cloud.bigtable.Version;
import com.google.cloud.bigtable.data.v2.internal.csm.attributes.ClientInfo;
import com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsConstants;
import com.google.cloud.bigtable.data.v2.stub.metrics.BuiltinMetricsTracerFactory;
import com.google.cloud.bigtable.data.v2.stub.metrics.ChannelPoolMetricsTracer;
import com.google.cloud.bigtable.data.v2.stub.metrics.CompositeTracerFactory;
import com.google.cloud.bigtable.data.v2.stub.metrics.MetricsTracerFactory;
import com.google.cloud.bigtable.data.v2.stub.metrics.RpcMeasureConstants;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.grpc.ManagedChannelBuilder;
import io.grpc.opentelemetry.GrpcOpenTelemetry;
import io.opencensus.stats.StatsRecorder;
import io.opencensus.tags.TagKey;
import io.opencensus.tags.TagValue;
import io.opencensus.tags.Tagger;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.Nullable;

public class MetricsImpl implements Metrics, Closeable {
  private final ApiTracerFactory userTracerFactory;
  private final @Nullable OpenTelemetrySdk internalOtel;
  private final @Nullable OpenTelemetry userOtel;
  private final ScheduledExecutorService executor;
  private final Tagger ocTagger;
  private final StatsRecorder ocRecorder;

  private final GrpcOpenTelemetry grpcOtel;
  @Nullable private final ChannelPoolMetricsTracer channelPoolMetricsTracer;
  private final List<ScheduledFuture<?>> tasks = new ArrayList<>();

  public MetricsImpl(
      ApiTracerFactory userTracerFactory,
      OpenTelemetrySdk internalOtel,
      OpenTelemetry userOtel,
      Tagger ocTagger,
      StatsRecorder ocRecorder,
      ScheduledExecutorService executor) {
    this.userTracerFactory = Preconditions.checkNotNull(userTracerFactory);

    this.internalOtel = internalOtel;
    this.userOtel = userOtel;

    this.ocTagger = ocTagger;
    this.ocRecorder = ocRecorder;

    this.executor = executor;

    this.grpcOtel =
        GrpcOpenTelemetry.newBuilder()
            .sdk(internalOtel)
            .addOptionalLabel("grpc.lb.locality")
            // Disable default grpc metrics
            .disableAllMetrics()
            // Enable specific grpc metrics
            .enableMetrics(BuiltinMetricsConstants.GRPC_METRICS.keySet())
            .build();

    if (internalOtel != null) {
      this.channelPoolMetricsTracer = new ChannelPoolMetricsTracer(internalOtel);
    } else {
      this.channelPoolMetricsTracer = null;
    }
  }

  @Override
  public void close() {
    for (ScheduledFuture<?> task : tasks) {
      task.cancel(false);
    }
    if (internalOtel != null) {
      internalOtel.close();
    }
  }

  @Override
  public void start() {
    tasks.add(channelPoolMetricsTracer.start(executor));
  }

  @Override
  public <T extends ManagedChannelBuilder<?>> T configureGrpcChannel(T channelBuilder) {
    grpcOtel.configureChannelBuilder(channelBuilder);
    return channelBuilder;
  }

  @Override
  public ApiTracerFactory createTracerFactory(ClientInfo clientInfo) {
    ImmutableList.Builder<ApiTracerFactory> tracerFactories = ImmutableList.builder();
    tracerFactories
        .add(createOCTracingFactory(clientInfo))
        .add(createOCMetricsFactory(clientInfo, ocTagger, ocRecorder))
        .add(userTracerFactory);

    if (internalOtel != null) {
      tracerFactories.add(createOtelMetricsFactory(internalOtel, clientInfo));
    }
    if (userOtel != null) {
      tracerFactories.add(createOtelMetricsFactory(userOtel, clientInfo));
    }

    return new CompositeTracerFactory(tracerFactories.build());
  }

  @Override
  @Nullable
  public ChannelPoolMetricsTracer getChannelPoolMetricsTracer() {
    return channelPoolMetricsTracer;
  }

  private static ApiTracerFactory createOCTracingFactory(ClientInfo clientInfo) {
    return new OpencensusTracerFactory(
        ImmutableMap.<String, String>builder()
            // Annotate traces with the same tags as metrics
            .put(
                RpcMeasureConstants.BIGTABLE_PROJECT_ID.getName(),
                clientInfo.getInstanceName().getProject())
            .put(
                RpcMeasureConstants.BIGTABLE_INSTANCE_ID.getName(),
                clientInfo.getInstanceName().getInstance())
            .put(
                RpcMeasureConstants.BIGTABLE_APP_PROFILE_ID.getName(), clientInfo.getAppProfileId())
            // Also annotate traces with library versions
            .put("gax", GaxGrpcProperties.getGaxGrpcVersion())
            .put("grpc", GaxGrpcProperties.getGrpcVersion())
            .put("gapic", Version.VERSION)
            .build());
  }

  private static ApiTracerFactory createOCMetricsFactory(
      ClientInfo clientInfo, Tagger tagger, StatsRecorder stats) {

    ImmutableMap<TagKey, TagValue> attributes =
        ImmutableMap.<TagKey, TagValue>builder()
            .put(
                RpcMeasureConstants.BIGTABLE_PROJECT_ID,
                TagValue.create(clientInfo.getInstanceName().getProject()))
            .put(
                RpcMeasureConstants.BIGTABLE_INSTANCE_ID,
                TagValue.create(clientInfo.getInstanceName().getInstance()))
            .put(
                RpcMeasureConstants.BIGTABLE_APP_PROFILE_ID,
                TagValue.create(clientInfo.getAppProfileId()))
            .build();
    return MetricsTracerFactory.create(tagger, stats, attributes);
  }

  private static BuiltinMetricsTracerFactory createOtelMetricsFactory(
      OpenTelemetry otel, ClientInfo clientInfo) {

    return BuiltinMetricsTracerFactory.create(otel, clientInfo);
  }
}
