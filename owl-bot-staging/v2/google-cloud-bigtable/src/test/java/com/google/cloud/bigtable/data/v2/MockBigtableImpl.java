/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.bigtable.data.v2;

import com.google.api.core.BetaApi;
import com.google.bigtable.v2.BigtableGrpc.BigtableImplBase;
import com.google.bigtable.v2.CheckAndMutateRowRequest;
import com.google.bigtable.v2.CheckAndMutateRowResponse;
import com.google.bigtable.v2.ExecuteQueryRequest;
import com.google.bigtable.v2.ExecuteQueryResponse;
import com.google.bigtable.v2.GenerateInitialChangeStreamPartitionsRequest;
import com.google.bigtable.v2.GenerateInitialChangeStreamPartitionsResponse;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.PingAndWarmRequest;
import com.google.bigtable.v2.PingAndWarmResponse;
import com.google.bigtable.v2.ReadChangeStreamRequest;
import com.google.bigtable.v2.ReadChangeStreamResponse;
import com.google.bigtable.v2.ReadModifyWriteRowRequest;
import com.google.bigtable.v2.ReadModifyWriteRowResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.SampleRowKeysRequest;
import com.google.bigtable.v2.SampleRowKeysResponse;
import com.google.protobuf.AbstractMessage;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.annotation.Generated;

@BetaApi
@Generated("by gapic-generator-java")
public class MockBigtableImpl extends BigtableImplBase {
  private List<AbstractMessage> requests;
  private Queue<Object> responses;

  public MockBigtableImpl() {
    requests = new ArrayList<>();
    responses = new LinkedList<>();
  }

  public List<AbstractMessage> getRequests() {
    return requests;
  }

  public void addResponse(AbstractMessage response) {
    responses.add(response);
  }

  public void setResponses(List<AbstractMessage> responses) {
    this.responses = new LinkedList<Object>(responses);
  }

  public void addException(Exception exception) {
    responses.add(exception);
  }

  public void reset() {
    requests = new ArrayList<>();
    responses = new LinkedList<>();
  }

  @Override
  public void readRows(ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof ReadRowsResponse) {
      requests.add(request);
      responseObserver.onNext(((ReadRowsResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method ReadRows, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  ReadRowsResponse.class.getName(),
                  Exception.class.getName())));
    }
  }

  @Override
  public void sampleRowKeys(
      SampleRowKeysRequest request, StreamObserver<SampleRowKeysResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof SampleRowKeysResponse) {
      requests.add(request);
      responseObserver.onNext(((SampleRowKeysResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method SampleRowKeys, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  SampleRowKeysResponse.class.getName(),
                  Exception.class.getName())));
    }
  }

  @Override
  public void mutateRow(
      MutateRowRequest request, StreamObserver<MutateRowResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof MutateRowResponse) {
      requests.add(request);
      responseObserver.onNext(((MutateRowResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method MutateRow, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  MutateRowResponse.class.getName(),
                  Exception.class.getName())));
    }
  }

  @Override
  public void mutateRows(
      MutateRowsRequest request, StreamObserver<MutateRowsResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof MutateRowsResponse) {
      requests.add(request);
      responseObserver.onNext(((MutateRowsResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method MutateRows, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  MutateRowsResponse.class.getName(),
                  Exception.class.getName())));
    }
  }

  @Override
  public void checkAndMutateRow(
      CheckAndMutateRowRequest request,
      StreamObserver<CheckAndMutateRowResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof CheckAndMutateRowResponse) {
      requests.add(request);
      responseObserver.onNext(((CheckAndMutateRowResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method CheckAndMutateRow, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  CheckAndMutateRowResponse.class.getName(),
                  Exception.class.getName())));
    }
  }

  @Override
  public void pingAndWarm(
      PingAndWarmRequest request, StreamObserver<PingAndWarmResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof PingAndWarmResponse) {
      requests.add(request);
      responseObserver.onNext(((PingAndWarmResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method PingAndWarm, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  PingAndWarmResponse.class.getName(),
                  Exception.class.getName())));
    }
  }

  @Override
  public void readModifyWriteRow(
      ReadModifyWriteRowRequest request,
      StreamObserver<ReadModifyWriteRowResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof ReadModifyWriteRowResponse) {
      requests.add(request);
      responseObserver.onNext(((ReadModifyWriteRowResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method ReadModifyWriteRow, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  ReadModifyWriteRowResponse.class.getName(),
                  Exception.class.getName())));
    }
  }

  @Override
  public void generateInitialChangeStreamPartitions(
      GenerateInitialChangeStreamPartitionsRequest request,
      StreamObserver<GenerateInitialChangeStreamPartitionsResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof GenerateInitialChangeStreamPartitionsResponse) {
      requests.add(request);
      responseObserver.onNext(((GenerateInitialChangeStreamPartitionsResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method GenerateInitialChangeStreamPartitions, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  GenerateInitialChangeStreamPartitionsResponse.class.getName(),
                  Exception.class.getName())));
    }
  }

  @Override
  public void readChangeStream(
      ReadChangeStreamRequest request, StreamObserver<ReadChangeStreamResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof ReadChangeStreamResponse) {
      requests.add(request);
      responseObserver.onNext(((ReadChangeStreamResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method ReadChangeStream, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  ReadChangeStreamResponse.class.getName(),
                  Exception.class.getName())));
    }
  }

  @Override
  public void executeQuery(
      ExecuteQueryRequest request, StreamObserver<ExecuteQueryResponse> responseObserver) {
    Object response = responses.poll();
    if (response instanceof ExecuteQueryResponse) {
      requests.add(request);
      responseObserver.onNext(((ExecuteQueryResponse) response));
      responseObserver.onCompleted();
    } else if (response instanceof Exception) {
      responseObserver.onError(((Exception) response));
    } else {
      responseObserver.onError(
          new IllegalArgumentException(
              String.format(
                  "Unrecognized response type %s for method ExecuteQuery, expected %s or %s",
                  response == null ? "null" : response.getClass().getName(),
                  ExecuteQueryResponse.class.getName(),
                  Exception.class.getName())));
    }
  }
}
