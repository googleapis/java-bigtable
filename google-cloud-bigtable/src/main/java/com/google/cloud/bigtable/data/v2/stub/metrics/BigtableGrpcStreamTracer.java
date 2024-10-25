/*
 * Copyright 2023 Google LLC
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

import io.grpc.ClientStreamTracer;
import io.grpc.Metadata;
import org.threeten.bp.Duration;

/**
 * Records the time a request is enqueued in a grpc channel queue. This a bridge between gRPC stream
 * tracing and Bigtable tracing. Its primary purpose is to measure the transition time between
 * asking gRPC to start an RPC and gRPC actually serializing that RPC.
 */
class BigtableGrpcStreamTracer extends ClientStreamTracer {

  private final BigtableTracer tracer;
  private final long deadline;

  public BigtableGrpcStreamTracer(BigtableTracer tracer, long deadline) {
    this.tracer = tracer;
    this.deadline = deadline;
  }

  @Override
  public void outboundMessageSent(int seqNo, long optionalWireSize, long optionalUncompressedSize) {
    System.out.println("HERE " + deadline + "\n");
    tracer.grpcMessageSent();
    tracer.setRemainingDeadline(deadline);
  }

  static class Factory extends ClientStreamTracer.Factory {

    private final BigtableTracer tracer;
    private final long deadline;

    Factory(BigtableTracer tracer, long deadline) {
      this.tracer = tracer;
      this.deadline = deadline;
    }

    @Override
    public ClientStreamTracer newClientStreamTracer(
        ClientStreamTracer.StreamInfo info, Metadata headers) {
      return new BigtableGrpcStreamTracer(tracer, deadline);
    }
  }
}
