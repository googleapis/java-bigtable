/*
 * Copyright 2020 Google LLC
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

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.core.InternalApi;
import com.google.api.gax.batching.Batcher;
import com.google.api.gax.grpc.GrpcResponseMetadata;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.v2.ResponseParams;
import com.google.cloud.bigtable.data.v2.stub.CpuThrottlingStats;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.RateLimiter;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.Metadata;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.annotation.Nonnull;

/**
 * This callable will:
 * <li>- Inject a {@link GrpcResponseMetadata} to access the headers returned by gRPC methods upon
 *     completion. The {@link BigtableTracer} will process metrics that were injected in the
 *     header/trailer and publish them to OpenCensus. If {@link GrpcResponseMetadata#getMetadata()}
 *     returned null, it probably means that the request has never reached GFE, and it'll increment
 *     the gfe_header_missing_counter in this case.
 * <li>-This class will also access trailers from {@link GrpcResponseMetadata} to record zone and
 *     cluster ids.
 * <li>This class is considered an internal implementation detail and not meant to be used by
 *     applications.
 */
@InternalApi
public class CpuThrottlingUnaryCallable<RequestT, ResponseT>
    extends UnaryCallable<RequestT, ResponseT> {

  private final static long DEFAULT_QPS = 10_000;
  private final static long minimumTimeBetweenUpdates = 60_000; // 1 minute?

  private final CpuThrottlingStats stats;
  private RateLimiter limiter;
  private final UnaryCallable<RequestT, ResponseT> innerCallable;

  public CpuThrottlingUnaryCallable(@Nonnull UnaryCallable<RequestT, ResponseT> innerCallable, CpuThrottlingStats cpuStats) {
    // Maybe I can use request stats to find the size and get QPS to a better estimate
    limiter =  RateLimiter.create(DEFAULT_QPS);
    this.stats = cpuStats;
    this.innerCallable = Preconditions.checkNotNull(innerCallable, "Inner callable must be set");
  }

  @Override
  public ApiFuture futureCall(RequestT request, ApiCallContext context) {
    if (context.getOption(Util.CPU_METADATA) == null) {
      context = context.withOption(Util.CPU_METADATA, new GrpcResponseMetadata());
    }
    final GrpcResponseMetadata responseMetadata = context.getOption(Util.CPU_METADATA);
    final ApiCallContext contextWithResponseMetadata = responseMetadata.addHandlers(context); // This function can only be called once so I need to figure it if when it's created or

    limiter.acquire();
    System.out.println("Acquired a permit at: "+Instant.now().toString());

    ApiFuture future = innerCallable.futureCall(request, contextWithResponseMetadata);
    ApiFutures.addCallback(future, new CpuThrottlingUnaryCallback<>(responseMetadata), MoreExecutors.directExecutor());

    return future;
  }

  class CpuThrottlingUnaryCallback<ResponseT> implements ApiFutureCallback<ResponseT> {

    private final GrpcResponseMetadata responseMetadata;

    CpuThrottlingUnaryCallback(GrpcResponseMetadata responseMetadata) {
      this.responseMetadata = responseMetadata;
    }

    @Override
    public void onFailure(Throwable throwable) {
      System.out.println("ON FAILURE");
      Metadata metadata = responseMetadata.getMetadata();
      System.out.println("Metadata: "+metadata.toString());

      double[] cpus = Util.getCpuList(metadata);
      double newQps = limiter.getRate(); // Can I captor this?
      if (cpus.length > 0) {
        System.out.println("CALCULATING QPS");
        newQps = Util.calculateQpsChange(cpus, 70, limiter.getRate());
        System.out.println("newQPS = "+newQps);
      }

      // Ensure enough time has passed since updates to QPS
      long lastQpsUpdateTime = stats.getlastQpsUpdateTime();
      long currentTime = System.currentTimeMillis();
      if (currentTime - lastQpsUpdateTime < minimumTimeBetweenUpdates) {
        return;
      }

      limiter.setRate(newQps); // I need to verify this somehow
      stats.addTimesChanged(); // I need to change this
    }

    @Override
    public void onSuccess(ResponseT response) {
      System.out.println("ON SUCCESS");
      Metadata metadata = responseMetadata.getMetadata();
      System.out.println("Metadata: "+metadata.toString());

      double[] cpus = Util.getCpuList(metadata);
      double newQps = limiter.getRate(); // Can I captor this?
      if (cpus.length > 0) {
        System.out.println("CALCULATING QPS");
        newQps = Util.calculateQpsChange(cpus, 70, limiter.getRate());
        System.out.println("newQPS = "+newQps);
      }

      // Ensure enough time has passed since updates to QPS
      long lastQpsUpdateTime = stats.getlastQpsUpdateTime();
      long currentTime = System.currentTimeMillis();
      if (currentTime - lastQpsUpdateTime < minimumTimeBetweenUpdates) {
        return;
      }

      System.out.println("Setting new QPS!");
      limiter.setRate(newQps); // I need to verify this somehow
      stats.addTimesChanged(); // I need to change this
    }
  }
}
