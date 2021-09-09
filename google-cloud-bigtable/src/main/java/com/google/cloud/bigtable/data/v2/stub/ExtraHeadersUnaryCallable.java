/*
 * Copyright 2021 Google LLC
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

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ApiCallContext;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.bigtable.data.v2.stub.metrics.CompositeTracer;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** A callable that injects client timestamp and attempt count to request headers. */
final class ExtraHeadersUnaryCallable<RequestT, ResponseT>
    extends UnaryCallable<RequestT, ResponseT> {
  private final UnaryCallable innerCallable;

  public ExtraHeadersUnaryCallable(UnaryCallable innerCallable) {
    this.innerCallable = innerCallable;
  }

  @Override
  public ApiFuture futureCall(RequestT request, ApiCallContext apiCallContext) {
    int attemptCount = -1;
    if (apiCallContext.getTracer() instanceof CompositeTracer) {
      attemptCount = ((CompositeTracer) apiCallContext.getTracer()).getAttempt();
    }
    Map<String, List<String>> headers =
        ImmutableMap.of(
            EnhancedBigtableStub.ATTEMPT_HEADER_KEY.name(),
                Arrays.asList(String.valueOf(attemptCount)),
            EnhancedBigtableStub.TIMESTAMP_HEADER_KEY.name(),
                Arrays.asList(String.valueOf(System.currentTimeMillis())));
    ApiCallContext newCallContext = apiCallContext.withExtraHeaders(headers);
    return innerCallable.futureCall(request, newCallContext);
  }
}
