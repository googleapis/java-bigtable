/*
 * Copyright 2019 Google LLC
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

import com.google.api.gax.grpc.GrpcResponseMetadata;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import com.google.cloud.bigtable.data.v2.stub.SafeResponseObserver;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.RateLimiter;
import io.grpc.Metadata;
import java.time.Instant;
import javax.annotation.Nonnull;

public class RateLimitingServerStreamingCallable<RequestT, ResponseT>
    extends ServerStreamingCallable<RequestT, ResponseT> {
  private final static long DEFAULT_QPS = 10_000;
  private final static long minimumTimeBetweenUpdates = 60_000;

  private RateLimiter limiter; // Capture .setRate() and pass in mocked RateLimiter
  private RateLimitingStats stats;
  private final ServerStreamingCallable<RequestT, ResponseT> innerCallable;

  public RateLimitingServerStreamingCallable(
      @Nonnull ServerStreamingCallable<RequestT, ResponseT> innerCallable) {
    limiter =  RateLimiter.create(DEFAULT_QPS);
    stats = new RateLimitingStats();
    this.innerCallable = Preconditions.checkNotNull(
        innerCallable, "Inner callable must be set");

  }

  public RateLimitingServerStreamingCallable(
      @Nonnull ServerStreamingCallable<RequestT, ResponseT> innerCallable, RateLimitingStats stats) {
    this.limiter =  RateLimiter.create(DEFAULT_QPS);
    this.stats = stats;
    this.innerCallable = Preconditions.checkNotNull(
        innerCallable, "Inner callable must be set");

  }
  @Override
  public void call(
      RequestT request, ResponseObserver<ResponseT> responseObserver, ApiCallContext context) {
    if (context.getOption(Util.GRPC_METADATA) == null) { // get metadata?
      context = context.withOption(Util.GRPC_METADATA, new GrpcResponseMetadata());
    }
    final GrpcResponseMetadata responseMetadata = context.getOption(Util.GRPC_METADATA);
    final ApiCallContext contextWithResponseMetadata = responseMetadata.addHandlers(context);

    limiter.acquire();
    CpuMetadataResponseObserver<ResponseT> innerObserver =
        new CpuMetadataResponseObserver<>(responseObserver, responseMetadata);

    innerCallable.call(request, innerObserver, contextWithResponseMetadata);
    //ApiFutures.addCallback(future, new CpuThrottlingUnaryCallback<>(responseMetadata), MoreExecutors.directExecutor());
  }

  private class CpuMetadataResponseObserver<ResponseT> extends SafeResponseObserver<ResponseT> {

    private final ResponseObserver<ResponseT> outerObserver;
    private final GrpcResponseMetadata responseMetadata;

    CpuMetadataResponseObserver(
        ResponseObserver<ResponseT> observer,
        GrpcResponseMetadata metadata) {
      super(observer);

      this.outerObserver = observer;
      this.responseMetadata = metadata;
    }

    @Override
    protected void onStartImpl(final StreamController controller) {
      outerObserver.onStart(controller);
    }

    @Override
    protected void onResponseImpl(ResponseT response) {
      outerObserver.onResponse(response);
      // Handle the MutateRowsResponse inside of here
      // Change implementation from onError and onComplete to be here
      // How am I suppose to handle failed or onError cases?
      //
    }

    @Override
    protected void onErrorImpl(Throwable t) {
      Metadata metadata = responseMetadata.getMetadata();

      double[] cpus = Util.getCpuList(metadata);
      double newQps = limiter.getRate();
      if (cpus.length > 0) {
        newQps = Util.calculateQpsChange(cpus, 70, limiter.getRate());
        if (newQps < stats.getLowerQpsBound() || newQps > stats.getUpperQpsBound()) {
          System.out.println("Calculated QPS is not within bounds"); // Going to change
          return;
        }
      }

      // Ensure enough time has passed since updates to QPS
      long lastQpsUpdateTime = stats.getLastQpsUpdateTime();
      long currentTime = System.currentTimeMillis();
      if (currentTime - lastQpsUpdateTime < minimumTimeBetweenUpdates) {
        outerObserver.onError(t);
        return;
      }
      limiter.setRate(newQps);
      stats.updateLastQpsUpdateTime(System.currentTimeMillis());
      stats.updateQps(newQps);

      outerObserver.onError(t);
    }

    @Override
    protected void onCompleteImpl() {
      Metadata metadata = responseMetadata.getMetadata();

      double[] cpus = Util.getCpuList(metadata);
      double newQps = limiter.getRate();
      if (cpus.length > 0) {
        newQps = Util.calculateQpsChange(cpus, 70, limiter.getRate());
        System.out.println(newQps);
        if (newQps < stats.getLowerQpsBound() || newQps > stats.getUpperQpsBound()) {
          System.out.println("Calculated QPS is not within bounds"); // Going to change
          return;
        }
      }

      long lastQpsUpdateTime = stats.getLastQpsUpdateTime();
      long currentTime = System.currentTimeMillis();
      if (currentTime - lastQpsUpdateTime < minimumTimeBetweenUpdates) {
        outerObserver.onComplete();
        return;
      }
      limiter.setRate(newQps);
      stats.updateLastQpsUpdateTime(System.currentTimeMillis());
      stats.updateQps(newQps);

      outerObserver.onComplete();
    }
  }
}