/*
 * Copyright 2022 Google LLC
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

package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.BetaApi;
import com.google.api.core.InternalApi;
import com.google.api.gax.core.BackgroundResource;
import com.google.api.gax.core.BackgroundResourceAggregation;
import com.google.api.gax.httpjson.ApiMethodDescriptor;
import com.google.api.gax.httpjson.HttpJsonCallSettings;
import com.google.api.gax.httpjson.HttpJsonStubCallableFactory;
import com.google.api.gax.httpjson.ProtoMessageRequestFormatter;
import com.google.api.gax.httpjson.ProtoMessageResponseParser;
import com.google.api.gax.httpjson.ProtoRestSerializer;
import com.google.api.gax.rpc.ClientContext;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.v2.CheckAndMutateRowRequest;
import com.google.bigtable.v2.CheckAndMutateRowResponse;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.PingAndWarmRequest;
import com.google.bigtable.v2.PingAndWarmResponse;
import com.google.bigtable.v2.ReadModifyWriteRowRequest;
import com.google.bigtable.v2.ReadModifyWriteRowResponse;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.SampleRowKeysRequest;
import com.google.bigtable.v2.SampleRowKeysResponse;
import com.google.protobuf.TypeRegistry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.annotation.Generated;

// AUTO-GENERATED DOCUMENTATION AND CLASS.
/**
 * REST stub implementation for the Bigtable service API.
 *
 * <p>This class is for advanced usage and reflects the underlying API directly.
 */
@Generated("by gapic-generator-java")
@BetaApi
public class HttpJsonBigtableStub extends BigtableStub {
  private static final TypeRegistry typeRegistry = TypeRegistry.newBuilder().build();

  private static final ApiMethodDescriptor<ReadRowsRequest, ReadRowsResponse>
      readRowsMethodDescriptor =
          ApiMethodDescriptor.<ReadRowsRequest, ReadRowsResponse>newBuilder()
              .setFullMethodName("google.bigtable.v2.Bigtable/ReadRows")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.SERVER_STREAMING)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ReadRowsRequest>newBuilder()
                      .setPath(
                          "/v2/{tableName=projects/*/instances/*/tables/*}:readRows",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ReadRowsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "tableName", request.getTableName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ReadRowsRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearTableName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<ReadRowsResponse>newBuilder()
                      .setDefaultInstance(ReadRowsResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<SampleRowKeysRequest, SampleRowKeysResponse>
      sampleRowKeysMethodDescriptor =
          ApiMethodDescriptor.<SampleRowKeysRequest, SampleRowKeysResponse>newBuilder()
              .setFullMethodName("google.bigtable.v2.Bigtable/SampleRowKeys")
              .setHttpMethod("GET")
              .setType(ApiMethodDescriptor.MethodType.SERVER_STREAMING)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<SampleRowKeysRequest>newBuilder()
                      .setPath(
                          "/v2/{tableName=projects/*/instances/*/tables/*}:sampleRowKeys",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<SampleRowKeysRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "tableName", request.getTableName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<SampleRowKeysRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putQueryParam(
                                fields, "appProfileId", request.getAppProfileId());
                            return fields;
                          })
                      .setRequestBodyExtractor(request -> null)
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<SampleRowKeysResponse>newBuilder()
                      .setDefaultInstance(SampleRowKeysResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<MutateRowRequest, MutateRowResponse>
      mutateRowMethodDescriptor =
          ApiMethodDescriptor.<MutateRowRequest, MutateRowResponse>newBuilder()
              .setFullMethodName("google.bigtable.v2.Bigtable/MutateRow")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<MutateRowRequest>newBuilder()
                      .setPath(
                          "/v2/{tableName=projects/*/instances/*/tables/*}:mutateRow",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<MutateRowRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "tableName", request.getTableName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<MutateRowRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearTableName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<MutateRowResponse>newBuilder()
                      .setDefaultInstance(MutateRowResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<MutateRowsRequest, MutateRowsResponse>
      mutateRowsMethodDescriptor =
          ApiMethodDescriptor.<MutateRowsRequest, MutateRowsResponse>newBuilder()
              .setFullMethodName("google.bigtable.v2.Bigtable/MutateRows")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.SERVER_STREAMING)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<MutateRowsRequest>newBuilder()
                      .setPath(
                          "/v2/{tableName=projects/*/instances/*/tables/*}:mutateRows",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<MutateRowsRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "tableName", request.getTableName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<MutateRowsRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearTableName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<MutateRowsResponse>newBuilder()
                      .setDefaultInstance(MutateRowsResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<CheckAndMutateRowRequest, CheckAndMutateRowResponse>
      checkAndMutateRowMethodDescriptor =
          ApiMethodDescriptor.<CheckAndMutateRowRequest, CheckAndMutateRowResponse>newBuilder()
              .setFullMethodName("google.bigtable.v2.Bigtable/CheckAndMutateRow")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<CheckAndMutateRowRequest>newBuilder()
                      .setPath(
                          "/v2/{tableName=projects/*/instances/*/tables/*}:checkAndMutateRow",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<CheckAndMutateRowRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "tableName", request.getTableName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<CheckAndMutateRowRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearTableName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<CheckAndMutateRowResponse>newBuilder()
                      .setDefaultInstance(CheckAndMutateRowResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<PingAndWarmRequest, PingAndWarmResponse>
      pingAndWarmMethodDescriptor =
          ApiMethodDescriptor.<PingAndWarmRequest, PingAndWarmResponse>newBuilder()
              .setFullMethodName("google.bigtable.v2.Bigtable/PingAndWarm")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<PingAndWarmRequest>newBuilder()
                      .setPath(
                          "/v2/{name=projects/*/instances/*}:ping",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<PingAndWarmRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "name", request.getName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<PingAndWarmRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<PingAndWarmResponse>newBuilder()
                      .setDefaultInstance(PingAndWarmResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private static final ApiMethodDescriptor<ReadModifyWriteRowRequest, ReadModifyWriteRowResponse>
      readModifyWriteRowMethodDescriptor =
          ApiMethodDescriptor.<ReadModifyWriteRowRequest, ReadModifyWriteRowResponse>newBuilder()
              .setFullMethodName("google.bigtable.v2.Bigtable/ReadModifyWriteRow")
              .setHttpMethod("POST")
              .setType(ApiMethodDescriptor.MethodType.UNARY)
              .setRequestFormatter(
                  ProtoMessageRequestFormatter.<ReadModifyWriteRowRequest>newBuilder()
                      .setPath(
                          "/v2/{tableName=projects/*/instances/*/tables/*}:readModifyWriteRow",
                          request -> {
                            Map<String, String> fields = new HashMap<>();
                            ProtoRestSerializer<ReadModifyWriteRowRequest> serializer =
                                ProtoRestSerializer.create();
                            serializer.putPathParam(fields, "tableName", request.getTableName());
                            return fields;
                          })
                      .setQueryParamsExtractor(
                          request -> {
                            Map<String, List<String>> fields = new HashMap<>();
                            ProtoRestSerializer<ReadModifyWriteRowRequest> serializer =
                                ProtoRestSerializer.create();
                            return fields;
                          })
                      .setRequestBodyExtractor(
                          request ->
                              ProtoRestSerializer.create()
                                  .toBody("*", request.toBuilder().clearTableName().build()))
                      .build())
              .setResponseParser(
                  ProtoMessageResponseParser.<ReadModifyWriteRowResponse>newBuilder()
                      .setDefaultInstance(ReadModifyWriteRowResponse.getDefaultInstance())
                      .setDefaultTypeRegistry(typeRegistry)
                      .build())
              .build();

  private final ServerStreamingCallable<ReadRowsRequest, ReadRowsResponse> readRowsCallable;
  private final ServerStreamingCallable<SampleRowKeysRequest, SampleRowKeysResponse>
      sampleRowKeysCallable;
  private final UnaryCallable<MutateRowRequest, MutateRowResponse> mutateRowCallable;
  private final ServerStreamingCallable<MutateRowsRequest, MutateRowsResponse> mutateRowsCallable;
  private final UnaryCallable<CheckAndMutateRowRequest, CheckAndMutateRowResponse>
      checkAndMutateRowCallable;
  private final UnaryCallable<PingAndWarmRequest, PingAndWarmResponse> pingAndWarmCallable;
  private final UnaryCallable<ReadModifyWriteRowRequest, ReadModifyWriteRowResponse>
      readModifyWriteRowCallable;

  private final BackgroundResource backgroundResources;
  private final HttpJsonStubCallableFactory callableFactory;

  public static final HttpJsonBigtableStub create(BigtableStubSettings settings)
      throws IOException {
    return new HttpJsonBigtableStub(settings, ClientContext.create(settings));
  }

  public static final HttpJsonBigtableStub create(ClientContext clientContext) throws IOException {
    return new HttpJsonBigtableStub(
        BigtableStubSettings.newHttpJsonBuilder().build(), clientContext);
  }

  public static final HttpJsonBigtableStub create(
      ClientContext clientContext, HttpJsonStubCallableFactory callableFactory) throws IOException {
    return new HttpJsonBigtableStub(
        BigtableStubSettings.newHttpJsonBuilder().build(), clientContext, callableFactory);
  }

  /**
   * Constructs an instance of HttpJsonBigtableStub, using the given settings. This is protected so
   * that it is easy to make a subclass, but otherwise, the static factory methods should be
   * preferred.
   */
  protected HttpJsonBigtableStub(BigtableStubSettings settings, ClientContext clientContext)
      throws IOException {
    this(settings, clientContext, new HttpJsonBigtableCallableFactory());
  }

  /**
   * Constructs an instance of HttpJsonBigtableStub, using the given settings. This is protected so
   * that it is easy to make a subclass, but otherwise, the static factory methods should be
   * preferred.
   */
  protected HttpJsonBigtableStub(
      BigtableStubSettings settings,
      ClientContext clientContext,
      HttpJsonStubCallableFactory callableFactory)
      throws IOException {
    this.callableFactory = callableFactory;

    HttpJsonCallSettings<ReadRowsRequest, ReadRowsResponse> readRowsTransportSettings =
        HttpJsonCallSettings.<ReadRowsRequest, ReadRowsResponse>newBuilder()
            .setMethodDescriptor(readRowsMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<SampleRowKeysRequest, SampleRowKeysResponse>
        sampleRowKeysTransportSettings =
            HttpJsonCallSettings.<SampleRowKeysRequest, SampleRowKeysResponse>newBuilder()
                .setMethodDescriptor(sampleRowKeysMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<MutateRowRequest, MutateRowResponse> mutateRowTransportSettings =
        HttpJsonCallSettings.<MutateRowRequest, MutateRowResponse>newBuilder()
            .setMethodDescriptor(mutateRowMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<MutateRowsRequest, MutateRowsResponse> mutateRowsTransportSettings =
        HttpJsonCallSettings.<MutateRowsRequest, MutateRowsResponse>newBuilder()
            .setMethodDescriptor(mutateRowsMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<CheckAndMutateRowRequest, CheckAndMutateRowResponse>
        checkAndMutateRowTransportSettings =
            HttpJsonCallSettings.<CheckAndMutateRowRequest, CheckAndMutateRowResponse>newBuilder()
                .setMethodDescriptor(checkAndMutateRowMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();
    HttpJsonCallSettings<PingAndWarmRequest, PingAndWarmResponse> pingAndWarmTransportSettings =
        HttpJsonCallSettings.<PingAndWarmRequest, PingAndWarmResponse>newBuilder()
            .setMethodDescriptor(pingAndWarmMethodDescriptor)
            .setTypeRegistry(typeRegistry)
            .build();
    HttpJsonCallSettings<ReadModifyWriteRowRequest, ReadModifyWriteRowResponse>
        readModifyWriteRowTransportSettings =
            HttpJsonCallSettings.<ReadModifyWriteRowRequest, ReadModifyWriteRowResponse>newBuilder()
                .setMethodDescriptor(readModifyWriteRowMethodDescriptor)
                .setTypeRegistry(typeRegistry)
                .build();

    this.readRowsCallable =
        callableFactory.createServerStreamingCallable(
            readRowsTransportSettings, settings.readRowsSettings(), clientContext);
    this.sampleRowKeysCallable =
        callableFactory.createServerStreamingCallable(
            sampleRowKeysTransportSettings, settings.sampleRowKeysSettings(), clientContext);
    this.mutateRowCallable =
        callableFactory.createUnaryCallable(
            mutateRowTransportSettings, settings.mutateRowSettings(), clientContext);
    this.mutateRowsCallable =
        callableFactory.createServerStreamingCallable(
            mutateRowsTransportSettings, settings.mutateRowsSettings(), clientContext);
    this.checkAndMutateRowCallable =
        callableFactory.createUnaryCallable(
            checkAndMutateRowTransportSettings,
            settings.checkAndMutateRowSettings(),
            clientContext);
    this.pingAndWarmCallable =
        callableFactory.createUnaryCallable(
            pingAndWarmTransportSettings, settings.pingAndWarmSettings(), clientContext);
    this.readModifyWriteRowCallable =
        callableFactory.createUnaryCallable(
            readModifyWriteRowTransportSettings,
            settings.readModifyWriteRowSettings(),
            clientContext);

    this.backgroundResources =
        new BackgroundResourceAggregation(clientContext.getBackgroundResources());
  }

  @InternalApi
  public static List<ApiMethodDescriptor> getMethodDescriptors() {
    List<ApiMethodDescriptor> methodDescriptors = new ArrayList<>();
    methodDescriptors.add(readRowsMethodDescriptor);
    methodDescriptors.add(sampleRowKeysMethodDescriptor);
    methodDescriptors.add(mutateRowMethodDescriptor);
    methodDescriptors.add(mutateRowsMethodDescriptor);
    methodDescriptors.add(checkAndMutateRowMethodDescriptor);
    methodDescriptors.add(pingAndWarmMethodDescriptor);
    methodDescriptors.add(readModifyWriteRowMethodDescriptor);
    return methodDescriptors;
  }

  @Override
  public ServerStreamingCallable<ReadRowsRequest, ReadRowsResponse> readRowsCallable() {
    return readRowsCallable;
  }

  @Override
  public ServerStreamingCallable<SampleRowKeysRequest, SampleRowKeysResponse>
      sampleRowKeysCallable() {
    return sampleRowKeysCallable;
  }

  @Override
  public UnaryCallable<MutateRowRequest, MutateRowResponse> mutateRowCallable() {
    return mutateRowCallable;
  }

  @Override
  public ServerStreamingCallable<MutateRowsRequest, MutateRowsResponse> mutateRowsCallable() {
    return mutateRowsCallable;
  }

  @Override
  public UnaryCallable<CheckAndMutateRowRequest, CheckAndMutateRowResponse>
      checkAndMutateRowCallable() {
    return checkAndMutateRowCallable;
  }

  @Override
  public UnaryCallable<PingAndWarmRequest, PingAndWarmResponse> pingAndWarmCallable() {
    return pingAndWarmCallable;
  }

  @Override
  public UnaryCallable<ReadModifyWriteRowRequest, ReadModifyWriteRowResponse>
      readModifyWriteRowCallable() {
    return readModifyWriteRowCallable;
  }

  @Override
  public final void close() {
    try {
      backgroundResources.close();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException("Failed to close resource", e);
    }
  }

  @Override
  public void shutdown() {
    backgroundResources.shutdown();
  }

  @Override
  public boolean isShutdown() {
    return backgroundResources.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return backgroundResources.isTerminated();
  }

  @Override
  public void shutdownNow() {
    backgroundResources.shutdownNow();
  }

  @Override
  public boolean awaitTermination(long duration, TimeUnit unit) throws InterruptedException {
    return backgroundResources.awaitTermination(duration, unit);
  }
}
