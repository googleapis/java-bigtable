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
package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.DeadlineExceededException;
import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.UnavailableException;
import com.google.bigtable.v2.FeatureFlags;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.StreamController;
import com.google.bigtable.v2.ServerStats;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.RateLimiter;
import io.grpc.Metadata;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

public class RateLimitingServerStreamingCallable
    extends ServerStreamingCallable<MutateRowsRequest, MutateRowsResponse> {
  private final static long DEFAULT_QPS = 10_000;
  private final static double DEFAULT_TARGET_CPU = 70.0;
  private final static long minimumTimeMsBetweenUpdates = 200_000;

  private RateLimiter limiter;
  private RateLimitingStats stats;
  private final ServerStreamingCallable<MutateRowsRequest, MutateRowsResponse> innerCallable;

  public RateLimitingServerStreamingCallable(
      @Nonnull ServerStreamingCallable<MutateRowsRequest, MutateRowsResponse> innerCallable, RateLimitingStats stats) {
    this.limiter =  RateLimiter.create(DEFAULT_QPS);
    this.stats = stats;
    this.innerCallable = Preconditions.checkNotNull(
        innerCallable, "Inner callable must be set");

  }
  @Override
  public void call(
      MutateRowsRequest request, ResponseObserver<MutateRowsResponse> responseObserver, ApiCallContext context) {
    //System.out.println("Call made");
    //System.out.println("Current QPS: "+limiter.getRate());
    limiter.acquire();
    CpuMetadataResponseObserver innerObserver =
        new CpuMetadataResponseObserver(responseObserver);
    innerCallable.call(request, innerObserver, context);
  }

  private class CpuMetadataResponseObserver extends SafeResponseObserver<MutateRowsResponse> {
    private final ResponseObserver<MutateRowsResponse> outerObserver;

    CpuMetadataResponseObserver(
        ResponseObserver<MutateRowsResponse> observer) {
      super(observer);

      this.outerObserver = observer;
    }

    @Override
    protected void onStartImpl(final StreamController controller) {
      //System.out.println("Rate Limiting onStart");
      outerObserver.onStart(controller);
    }

    @Override
    protected void onResponseImpl(MutateRowsResponse response) {
      // Ensure enough time has passed since updates to QPS
      long lastQpsUpdateTime = stats.getLastQpsUpdateTime();
      long currentTime = System.currentTimeMillis();
      if (currentTime - lastQpsUpdateTime < minimumTimeMsBetweenUpdates) {
        outerObserver.onResponse(response);
        return;
      }

      double[] cpus = stats.getCpuList(response);
      double newQps;

      if (cpus.length > 0) {
        newQps = stats.calculateQpsChange(cpus, DEFAULT_TARGET_CPU, limiter.getRate());
        //System.out.println("Callable: ("+Thread.currentThread().getId()+") Changing QPS, Server Stats: "+response.getServerStats());
        //System.out.println("Callable: ("+Thread.currentThread().getId()+") Old rate: "+limiter.getRate()+" New Rate: "+newQps);
      } else {
        outerObserver.onResponse(response);
        return;
      }

      limiter.setRate(newQps);
      stats.updateLastQpsUpdateTime(currentTime);
      stats.updateQps(newQps);

      outerObserver.onResponse(response);
    }

    @Override
    protected void onErrorImpl(Throwable t) {
      //System.out.println("Error: ("+Thread.currentThread()+") "+t.toString());
      if (t instanceof DeadlineExceededException || t instanceof UnavailableException) {
        // Ensure enough time has passed since updates to QPS
        long lastQpsUpdateTime = stats.getLastQpsUpdateTime();
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastQpsUpdateTime < minimumTimeMsBetweenUpdates) {
          outerObserver.onError(t);
          return;
        }

        // If deadlines are being reached, assume cbt server is overloaded
        double newQps = stats.calculateQpsChange(new double[]{99.9}, DEFAULT_TARGET_CPU, limiter.getRate());

        limiter.setRate(newQps);
        stats.updateLastQpsUpdateTime(currentTime);
        stats.updateQps(newQps);
      }
      outerObserver.onError(t);
    }

    @Override
    protected void onCompleteImpl() {
      outerObserver.onComplete();
    }
  }
}