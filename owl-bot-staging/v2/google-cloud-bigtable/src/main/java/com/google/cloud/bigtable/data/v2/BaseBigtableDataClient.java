/*
 * Copyright 2021 Google LLC
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
import com.google.api.gax.core.BackgroundResource;
import com.google.api.gax.rpc.ServerStreamingCallable;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.bigtable.v2.CheckAndMutateRowRequest;
import com.google.bigtable.v2.CheckAndMutateRowResponse;
import com.google.bigtable.v2.MutateRowRequest;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.MutateRowsRequest;
import com.google.bigtable.v2.MutateRowsResponse;
import com.google.bigtable.v2.Mutation;
import com.google.bigtable.v2.ReadModifyWriteRowRequest;
import com.google.bigtable.v2.ReadModifyWriteRowResponse;
import com.google.bigtable.v2.ReadModifyWriteRule;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.bigtable.v2.RowFilter;
import com.google.bigtable.v2.SampleRowKeysRequest;
import com.google.bigtable.v2.SampleRowKeysResponse;
import com.google.bigtable.v2.TableName;
import com.google.cloud.bigtable.data.v2.stub.BigtableStub;
import com.google.cloud.bigtable.data.v2.stub.BigtableStubSettings;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Generated;

// AUTO-GENERATED DOCUMENTATION AND CLASS.
/**
 * Service Description: Service for reading from and writing to existing Bigtable tables.
 *
 * <p>This class provides the ability to make remote calls to the backing service through method
 * calls that map to API methods. Sample code to get started:
 *
 * <pre>{@code
 * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
 *   TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
 *   ByteString rowKey = ByteString.EMPTY;
 *   List<Mutation> mutations = new ArrayList<>();
 *   MutateRowResponse response = baseBigtableDataClient.mutateRow(tableName, rowKey, mutations);
 * }
 * }</pre>
 *
 * <p>Note: close() needs to be called on the BaseBigtableDataClient object to clean up resources
 * such as threads. In the example above, try-with-resources is used, which automatically calls
 * close().
 *
 * <p>The surface of this class includes several types of Java methods for each of the API's
 * methods:
 *
 * <ol>
 *   <li> A "flattened" method. With this type of method, the fields of the request type have been
 *       converted into function parameters. It may be the case that not all fields are available as
 *       parameters, and not every API method will have a flattened method entry point.
 *   <li> A "request object" method. This type of method only takes one parameter, a request object,
 *       which must be constructed before the call. Not every API method will have a request object
 *       method.
 *   <li> A "callable" method. This type of method takes no parameters and returns an immutable API
 *       callable object, which can be used to initiate calls to the service.
 * </ol>
 *
 * <p>See the individual methods for example code.
 *
 * <p>Many parameters require resource names to be formatted in a particular way. To assist with
 * these names, this class includes a format method for each type of name, and additionally a parse
 * method to extract the individual identifiers contained within names that are returned.
 *
 * <p>This class can be customized by passing in a custom instance of BaseBigtableDataSettings to
 * create(). For example:
 *
 * <p>To customize credentials:
 *
 * <pre>{@code
 * BaseBigtableDataSettings baseBigtableDataSettings =
 *     BaseBigtableDataSettings.newBuilder()
 *         .setCredentialsProvider(FixedCredentialsProvider.create(myCredentials))
 *         .build();
 * BaseBigtableDataClient baseBigtableDataClient =
 *     BaseBigtableDataClient.create(baseBigtableDataSettings);
 * }</pre>
 *
 * <p>To customize the endpoint:
 *
 * <pre>{@code
 * BaseBigtableDataSettings baseBigtableDataSettings =
 *     BaseBigtableDataSettings.newBuilder().setEndpoint(myEndpoint).build();
 * BaseBigtableDataClient baseBigtableDataClient =
 *     BaseBigtableDataClient.create(baseBigtableDataSettings);
 * }</pre>
 *
 * <p>Please refer to the GitHub repository's samples for more quickstart code snippets.
 */
@Generated("by gapic-generator-java")
public class BaseBigtableDataClient implements BackgroundResource {
  private final BaseBigtableDataSettings settings;
  private final BigtableStub stub;

  /** Constructs an instance of BaseBigtableDataClient with default settings. */
  public static final BaseBigtableDataClient create() throws IOException {
    return create(BaseBigtableDataSettings.newBuilder().build());
  }

  /**
   * Constructs an instance of BaseBigtableDataClient, using the given settings. The channels are
   * created based on the settings passed in, or defaults for any settings that are not set.
   */
  public static final BaseBigtableDataClient create(BaseBigtableDataSettings settings)
      throws IOException {
    return new BaseBigtableDataClient(settings);
  }

  /**
   * Constructs an instance of BaseBigtableDataClient, using the given stub for making calls. This
   * is for advanced usage - prefer using create(BaseBigtableDataSettings).
   */
  @BetaApi("A restructuring of stub classes is planned, so this may break in the future")
  public static final BaseBigtableDataClient create(BigtableStub stub) {
    return new BaseBigtableDataClient(stub);
  }

  /**
   * Constructs an instance of BaseBigtableDataClient, using the given settings. This is protected
   * so that it is easy to make a subclass, but otherwise, the static factory methods should be
   * preferred.
   */
  protected BaseBigtableDataClient(BaseBigtableDataSettings settings) throws IOException {
    this.settings = settings;
    this.stub = ((BigtableStubSettings) settings.getStubSettings()).createStub();
  }

  @BetaApi("A restructuring of stub classes is planned, so this may break in the future")
  protected BaseBigtableDataClient(BigtableStub stub) {
    this.settings = null;
    this.stub = stub;
  }

  public final BaseBigtableDataSettings getSettings() {
    return settings;
  }

  @BetaApi("A restructuring of stub classes is planned, so this may break in the future")
  public BigtableStub getStub() {
    return stub;
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Streams back the contents of all requested rows in key order, optionally applying the same
   * Reader filter to each. Depending on their size, rows and cells may be broken up across multiple
   * responses, but atomicity of each row will still be preserved. See the ReadRowsResponse
   * documentation for details.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   ReadRowsRequest request =
   *       ReadRowsRequest.newBuilder()
   *           .setTableName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
   *           .setAppProfileId("appProfileId704923523")
   *           .setRows(RowSet.newBuilder().build())
   *           .setFilter(RowFilter.newBuilder().build())
   *           .setRowsLimit(-944199211)
   *           .build();
   *   ServerStream<ReadRowsResponse> stream =
   *       baseBigtableDataClient.readRowsCallable().call(request);
   *   for (ReadRowsResponse response : stream) {
   *     // Do something when a response is received.
   *   }
   * }
   * }</pre>
   */
  public final ServerStreamingCallable<ReadRowsRequest, ReadRowsResponse> readRowsCallable() {
    return stub.readRowsCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Returns a sample of row keys in the table. The returned row keys will delimit contiguous
   * sections of the table of approximately equal size, which can be used to break up the data for
   * distributed tasks like mapreduces.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   SampleRowKeysRequest request =
   *       SampleRowKeysRequest.newBuilder()
   *           .setTableName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
   *           .setAppProfileId("appProfileId704923523")
   *           .build();
   *   ServerStream<SampleRowKeysResponse> stream =
   *       baseBigtableDataClient.sampleRowKeysCallable().call(request);
   *   for (SampleRowKeysResponse response : stream) {
   *     // Do something when a response is received.
   *   }
   * }
   * }</pre>
   */
  public final ServerStreamingCallable<SampleRowKeysRequest, SampleRowKeysResponse>
      sampleRowKeysCallable() {
    return stub.sampleRowKeysCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically. Cells already present in the row are left unchanged unless explicitly
   * changed by `mutation`.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
   *   ByteString rowKey = ByteString.EMPTY;
   *   List<Mutation> mutations = new ArrayList<>();
   *   MutateRowResponse response = baseBigtableDataClient.mutateRow(tableName, rowKey, mutations);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the mutation should be
   *     applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the mutation should be applied.
   * @param mutations Required. Changes to be atomically applied to the specified row. Entries are
   *     applied in order, meaning that earlier mutations can be masked by later ones. Must contain
   *     at least one entry and at most 100000.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final MutateRowResponse mutateRow(
      TableName tableName, ByteString rowKey, List<Mutation> mutations) {
    MutateRowRequest request =
        MutateRowRequest.newBuilder()
            .setTableName(tableName == null ? null : tableName.toString())
            .setRowKey(rowKey)
            .addAllMutations(mutations)
            .build();
    return mutateRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically. Cells already present in the row are left unchanged unless explicitly
   * changed by `mutation`.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   String tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString();
   *   ByteString rowKey = ByteString.EMPTY;
   *   List<Mutation> mutations = new ArrayList<>();
   *   MutateRowResponse response = baseBigtableDataClient.mutateRow(tableName, rowKey, mutations);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the mutation should be
   *     applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the mutation should be applied.
   * @param mutations Required. Changes to be atomically applied to the specified row. Entries are
   *     applied in order, meaning that earlier mutations can be masked by later ones. Must contain
   *     at least one entry and at most 100000.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final MutateRowResponse mutateRow(
      String tableName, ByteString rowKey, List<Mutation> mutations) {
    MutateRowRequest request =
        MutateRowRequest.newBuilder()
            .setTableName(tableName)
            .setRowKey(rowKey)
            .addAllMutations(mutations)
            .build();
    return mutateRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically. Cells already present in the row are left unchanged unless explicitly
   * changed by `mutation`.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
   *   ByteString rowKey = ByteString.EMPTY;
   *   List<Mutation> mutations = new ArrayList<>();
   *   String appProfileId = "appProfileId704923523";
   *   MutateRowResponse response =
   *       baseBigtableDataClient.mutateRow(tableName, rowKey, mutations, appProfileId);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the mutation should be
   *     applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the mutation should be applied.
   * @param mutations Required. Changes to be atomically applied to the specified row. Entries are
   *     applied in order, meaning that earlier mutations can be masked by later ones. Must contain
   *     at least one entry and at most 100000.
   * @param appProfileId This value specifies routing for replication. If not specified, the
   *     "default" application profile will be used.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final MutateRowResponse mutateRow(
      TableName tableName, ByteString rowKey, List<Mutation> mutations, String appProfileId) {
    MutateRowRequest request =
        MutateRowRequest.newBuilder()
            .setTableName(tableName == null ? null : tableName.toString())
            .setRowKey(rowKey)
            .addAllMutations(mutations)
            .setAppProfileId(appProfileId)
            .build();
    return mutateRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically. Cells already present in the row are left unchanged unless explicitly
   * changed by `mutation`.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   String tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString();
   *   ByteString rowKey = ByteString.EMPTY;
   *   List<Mutation> mutations = new ArrayList<>();
   *   String appProfileId = "appProfileId704923523";
   *   MutateRowResponse response =
   *       baseBigtableDataClient.mutateRow(tableName, rowKey, mutations, appProfileId);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the mutation should be
   *     applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the mutation should be applied.
   * @param mutations Required. Changes to be atomically applied to the specified row. Entries are
   *     applied in order, meaning that earlier mutations can be masked by later ones. Must contain
   *     at least one entry and at most 100000.
   * @param appProfileId This value specifies routing for replication. If not specified, the
   *     "default" application profile will be used.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final MutateRowResponse mutateRow(
      String tableName, ByteString rowKey, List<Mutation> mutations, String appProfileId) {
    MutateRowRequest request =
        MutateRowRequest.newBuilder()
            .setTableName(tableName)
            .setRowKey(rowKey)
            .addAllMutations(mutations)
            .setAppProfileId(appProfileId)
            .build();
    return mutateRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically. Cells already present in the row are left unchanged unless explicitly
   * changed by `mutation`.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   MutateRowRequest request =
   *       MutateRowRequest.newBuilder()
   *           .setTableName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
   *           .setAppProfileId("appProfileId704923523")
   *           .setRowKey(ByteString.EMPTY)
   *           .addAllMutations(new ArrayList<Mutation>())
   *           .build();
   *   MutateRowResponse response = baseBigtableDataClient.mutateRow(request);
   * }
   * }</pre>
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final MutateRowResponse mutateRow(MutateRowRequest request) {
    return mutateRowCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically. Cells already present in the row are left unchanged unless explicitly
   * changed by `mutation`.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   MutateRowRequest request =
   *       MutateRowRequest.newBuilder()
   *           .setTableName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
   *           .setAppProfileId("appProfileId704923523")
   *           .setRowKey(ByteString.EMPTY)
   *           .addAllMutations(new ArrayList<Mutation>())
   *           .build();
   *   ApiFuture<MutateRowResponse> future =
   *       baseBigtableDataClient.mutateRowCallable().futureCall(request);
   *   // Do something.
   *   MutateRowResponse response = future.get();
   * }
   * }</pre>
   */
  public final UnaryCallable<MutateRowRequest, MutateRowResponse> mutateRowCallable() {
    return stub.mutateRowCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates multiple rows in a batch. Each individual row is mutated atomically as in MutateRow,
   * but the entire batch is not executed atomically.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   MutateRowsRequest request =
   *       MutateRowsRequest.newBuilder()
   *           .setTableName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
   *           .setAppProfileId("appProfileId704923523")
   *           .addAllEntries(new ArrayList<MutateRowsRequest.Entry>())
   *           .build();
   *   ServerStream<MutateRowsResponse> stream =
   *       baseBigtableDataClient.mutateRowsCallable().call(request);
   *   for (MutateRowsResponse response : stream) {
   *     // Do something when a response is received.
   *   }
   * }
   * }</pre>
   */
  public final ServerStreamingCallable<MutateRowsRequest, MutateRowsResponse> mutateRowsCallable() {
    return stub.mutateRowsCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically based on the output of a predicate Reader filter.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
   *   ByteString rowKey = ByteString.EMPTY;
   *   RowFilter predicateFilter = RowFilter.newBuilder().build();
   *   List<Mutation> trueMutations = new ArrayList<>();
   *   List<Mutation> falseMutations = new ArrayList<>();
   *   CheckAndMutateRowResponse response =
   *       baseBigtableDataClient.checkAndMutateRow(
   *           tableName, rowKey, predicateFilter, trueMutations, falseMutations);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the conditional mutation
   *     should be applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the conditional mutation should be applied.
   * @param predicateFilter The filter to be applied to the contents of the specified row. Depending
   *     on whether or not any results are yielded, either `true_mutations` or `false_mutations`
   *     will be executed. If unset, checks that the row contains any values at all.
   * @param trueMutations Changes to be atomically applied to the specified row if
   *     `predicate_filter` yields at least one cell when applied to `row_key`. Entries are applied
   *     in order, meaning that earlier mutations can be masked by later ones. Must contain at least
   *     one entry if `false_mutations` is empty, and at most 100000.
   * @param falseMutations Changes to be atomically applied to the specified row if
   *     `predicate_filter` does not yield any cells when applied to `row_key`. Entries are applied
   *     in order, meaning that earlier mutations can be masked by later ones. Must contain at least
   *     one entry if `true_mutations` is empty, and at most 100000.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final CheckAndMutateRowResponse checkAndMutateRow(
      TableName tableName,
      ByteString rowKey,
      RowFilter predicateFilter,
      List<Mutation> trueMutations,
      List<Mutation> falseMutations) {
    CheckAndMutateRowRequest request =
        CheckAndMutateRowRequest.newBuilder()
            .setTableName(tableName == null ? null : tableName.toString())
            .setRowKey(rowKey)
            .setPredicateFilter(predicateFilter)
            .addAllTrueMutations(trueMutations)
            .addAllFalseMutations(falseMutations)
            .build();
    return checkAndMutateRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically based on the output of a predicate Reader filter.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   String tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString();
   *   ByteString rowKey = ByteString.EMPTY;
   *   RowFilter predicateFilter = RowFilter.newBuilder().build();
   *   List<Mutation> trueMutations = new ArrayList<>();
   *   List<Mutation> falseMutations = new ArrayList<>();
   *   CheckAndMutateRowResponse response =
   *       baseBigtableDataClient.checkAndMutateRow(
   *           tableName, rowKey, predicateFilter, trueMutations, falseMutations);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the conditional mutation
   *     should be applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the conditional mutation should be applied.
   * @param predicateFilter The filter to be applied to the contents of the specified row. Depending
   *     on whether or not any results are yielded, either `true_mutations` or `false_mutations`
   *     will be executed. If unset, checks that the row contains any values at all.
   * @param trueMutations Changes to be atomically applied to the specified row if
   *     `predicate_filter` yields at least one cell when applied to `row_key`. Entries are applied
   *     in order, meaning that earlier mutations can be masked by later ones. Must contain at least
   *     one entry if `false_mutations` is empty, and at most 100000.
   * @param falseMutations Changes to be atomically applied to the specified row if
   *     `predicate_filter` does not yield any cells when applied to `row_key`. Entries are applied
   *     in order, meaning that earlier mutations can be masked by later ones. Must contain at least
   *     one entry if `true_mutations` is empty, and at most 100000.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final CheckAndMutateRowResponse checkAndMutateRow(
      String tableName,
      ByteString rowKey,
      RowFilter predicateFilter,
      List<Mutation> trueMutations,
      List<Mutation> falseMutations) {
    CheckAndMutateRowRequest request =
        CheckAndMutateRowRequest.newBuilder()
            .setTableName(tableName)
            .setRowKey(rowKey)
            .setPredicateFilter(predicateFilter)
            .addAllTrueMutations(trueMutations)
            .addAllFalseMutations(falseMutations)
            .build();
    return checkAndMutateRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically based on the output of a predicate Reader filter.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
   *   ByteString rowKey = ByteString.EMPTY;
   *   RowFilter predicateFilter = RowFilter.newBuilder().build();
   *   List<Mutation> trueMutations = new ArrayList<>();
   *   List<Mutation> falseMutations = new ArrayList<>();
   *   String appProfileId = "appProfileId704923523";
   *   CheckAndMutateRowResponse response =
   *       baseBigtableDataClient.checkAndMutateRow(
   *           tableName, rowKey, predicateFilter, trueMutations, falseMutations, appProfileId);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the conditional mutation
   *     should be applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the conditional mutation should be applied.
   * @param predicateFilter The filter to be applied to the contents of the specified row. Depending
   *     on whether or not any results are yielded, either `true_mutations` or `false_mutations`
   *     will be executed. If unset, checks that the row contains any values at all.
   * @param trueMutations Changes to be atomically applied to the specified row if
   *     `predicate_filter` yields at least one cell when applied to `row_key`. Entries are applied
   *     in order, meaning that earlier mutations can be masked by later ones. Must contain at least
   *     one entry if `false_mutations` is empty, and at most 100000.
   * @param falseMutations Changes to be atomically applied to the specified row if
   *     `predicate_filter` does not yield any cells when applied to `row_key`. Entries are applied
   *     in order, meaning that earlier mutations can be masked by later ones. Must contain at least
   *     one entry if `true_mutations` is empty, and at most 100000.
   * @param appProfileId This value specifies routing for replication. If not specified, the
   *     "default" application profile will be used.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final CheckAndMutateRowResponse checkAndMutateRow(
      TableName tableName,
      ByteString rowKey,
      RowFilter predicateFilter,
      List<Mutation> trueMutations,
      List<Mutation> falseMutations,
      String appProfileId) {
    CheckAndMutateRowRequest request =
        CheckAndMutateRowRequest.newBuilder()
            .setTableName(tableName == null ? null : tableName.toString())
            .setRowKey(rowKey)
            .setPredicateFilter(predicateFilter)
            .addAllTrueMutations(trueMutations)
            .addAllFalseMutations(falseMutations)
            .setAppProfileId(appProfileId)
            .build();
    return checkAndMutateRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically based on the output of a predicate Reader filter.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   String tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString();
   *   ByteString rowKey = ByteString.EMPTY;
   *   RowFilter predicateFilter = RowFilter.newBuilder().build();
   *   List<Mutation> trueMutations = new ArrayList<>();
   *   List<Mutation> falseMutations = new ArrayList<>();
   *   String appProfileId = "appProfileId704923523";
   *   CheckAndMutateRowResponse response =
   *       baseBigtableDataClient.checkAndMutateRow(
   *           tableName, rowKey, predicateFilter, trueMutations, falseMutations, appProfileId);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the conditional mutation
   *     should be applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the conditional mutation should be applied.
   * @param predicateFilter The filter to be applied to the contents of the specified row. Depending
   *     on whether or not any results are yielded, either `true_mutations` or `false_mutations`
   *     will be executed. If unset, checks that the row contains any values at all.
   * @param trueMutations Changes to be atomically applied to the specified row if
   *     `predicate_filter` yields at least one cell when applied to `row_key`. Entries are applied
   *     in order, meaning that earlier mutations can be masked by later ones. Must contain at least
   *     one entry if `false_mutations` is empty, and at most 100000.
   * @param falseMutations Changes to be atomically applied to the specified row if
   *     `predicate_filter` does not yield any cells when applied to `row_key`. Entries are applied
   *     in order, meaning that earlier mutations can be masked by later ones. Must contain at least
   *     one entry if `true_mutations` is empty, and at most 100000.
   * @param appProfileId This value specifies routing for replication. If not specified, the
   *     "default" application profile will be used.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final CheckAndMutateRowResponse checkAndMutateRow(
      String tableName,
      ByteString rowKey,
      RowFilter predicateFilter,
      List<Mutation> trueMutations,
      List<Mutation> falseMutations,
      String appProfileId) {
    CheckAndMutateRowRequest request =
        CheckAndMutateRowRequest.newBuilder()
            .setTableName(tableName)
            .setRowKey(rowKey)
            .setPredicateFilter(predicateFilter)
            .addAllTrueMutations(trueMutations)
            .addAllFalseMutations(falseMutations)
            .setAppProfileId(appProfileId)
            .build();
    return checkAndMutateRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically based on the output of a predicate Reader filter.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   CheckAndMutateRowRequest request =
   *       CheckAndMutateRowRequest.newBuilder()
   *           .setTableName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
   *           .setAppProfileId("appProfileId704923523")
   *           .setRowKey(ByteString.EMPTY)
   *           .setPredicateFilter(RowFilter.newBuilder().build())
   *           .addAllTrueMutations(new ArrayList<Mutation>())
   *           .addAllFalseMutations(new ArrayList<Mutation>())
   *           .build();
   *   CheckAndMutateRowResponse response = baseBigtableDataClient.checkAndMutateRow(request);
   * }
   * }</pre>
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final CheckAndMutateRowResponse checkAndMutateRow(CheckAndMutateRowRequest request) {
    return checkAndMutateRowCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Mutates a row atomically based on the output of a predicate Reader filter.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   CheckAndMutateRowRequest request =
   *       CheckAndMutateRowRequest.newBuilder()
   *           .setTableName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
   *           .setAppProfileId("appProfileId704923523")
   *           .setRowKey(ByteString.EMPTY)
   *           .setPredicateFilter(RowFilter.newBuilder().build())
   *           .addAllTrueMutations(new ArrayList<Mutation>())
   *           .addAllFalseMutations(new ArrayList<Mutation>())
   *           .build();
   *   ApiFuture<CheckAndMutateRowResponse> future =
   *       baseBigtableDataClient.checkAndMutateRowCallable().futureCall(request);
   *   // Do something.
   *   CheckAndMutateRowResponse response = future.get();
   * }
   * }</pre>
   */
  public final UnaryCallable<CheckAndMutateRowRequest, CheckAndMutateRowResponse>
      checkAndMutateRowCallable() {
    return stub.checkAndMutateRowCallable();
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Modifies a row atomically on the server. The method reads the latest existing timestamp and
   * value from the specified columns and writes a new entry based on pre-defined read/modify/write
   * rules. The new value for the timestamp is the greater of the existing timestamp or the current
   * server time. The method returns the new contents of all modified cells.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
   *   ByteString rowKey = ByteString.EMPTY;
   *   List<ReadModifyWriteRule> rules = new ArrayList<>();
   *   ReadModifyWriteRowResponse response =
   *       baseBigtableDataClient.readModifyWriteRow(tableName, rowKey, rules);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the read/modify/write rules
   *     should be applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the read/modify/write rules should be
   *     applied.
   * @param rules Required. Rules specifying how the specified row's contents are to be transformed
   *     into writes. Entries are applied in order, meaning that earlier rules will affect the
   *     results of later ones.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ReadModifyWriteRowResponse readModifyWriteRow(
      TableName tableName, ByteString rowKey, List<ReadModifyWriteRule> rules) {
    ReadModifyWriteRowRequest request =
        ReadModifyWriteRowRequest.newBuilder()
            .setTableName(tableName == null ? null : tableName.toString())
            .setRowKey(rowKey)
            .addAllRules(rules)
            .build();
    return readModifyWriteRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Modifies a row atomically on the server. The method reads the latest existing timestamp and
   * value from the specified columns and writes a new entry based on pre-defined read/modify/write
   * rules. The new value for the timestamp is the greater of the existing timestamp or the current
   * server time. The method returns the new contents of all modified cells.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   String tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString();
   *   ByteString rowKey = ByteString.EMPTY;
   *   List<ReadModifyWriteRule> rules = new ArrayList<>();
   *   ReadModifyWriteRowResponse response =
   *       baseBigtableDataClient.readModifyWriteRow(tableName, rowKey, rules);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the read/modify/write rules
   *     should be applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the read/modify/write rules should be
   *     applied.
   * @param rules Required. Rules specifying how the specified row's contents are to be transformed
   *     into writes. Entries are applied in order, meaning that earlier rules will affect the
   *     results of later ones.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ReadModifyWriteRowResponse readModifyWriteRow(
      String tableName, ByteString rowKey, List<ReadModifyWriteRule> rules) {
    ReadModifyWriteRowRequest request =
        ReadModifyWriteRowRequest.newBuilder()
            .setTableName(tableName)
            .setRowKey(rowKey)
            .addAllRules(rules)
            .build();
    return readModifyWriteRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Modifies a row atomically on the server. The method reads the latest existing timestamp and
   * value from the specified columns and writes a new entry based on pre-defined read/modify/write
   * rules. The new value for the timestamp is the greater of the existing timestamp or the current
   * server time. The method returns the new contents of all modified cells.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
   *   ByteString rowKey = ByteString.EMPTY;
   *   List<ReadModifyWriteRule> rules = new ArrayList<>();
   *   String appProfileId = "appProfileId704923523";
   *   ReadModifyWriteRowResponse response =
   *       baseBigtableDataClient.readModifyWriteRow(tableName, rowKey, rules, appProfileId);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the read/modify/write rules
   *     should be applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the read/modify/write rules should be
   *     applied.
   * @param rules Required. Rules specifying how the specified row's contents are to be transformed
   *     into writes. Entries are applied in order, meaning that earlier rules will affect the
   *     results of later ones.
   * @param appProfileId This value specifies routing for replication. If not specified, the
   *     "default" application profile will be used.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ReadModifyWriteRowResponse readModifyWriteRow(
      TableName tableName,
      ByteString rowKey,
      List<ReadModifyWriteRule> rules,
      String appProfileId) {
    ReadModifyWriteRowRequest request =
        ReadModifyWriteRowRequest.newBuilder()
            .setTableName(tableName == null ? null : tableName.toString())
            .setRowKey(rowKey)
            .addAllRules(rules)
            .setAppProfileId(appProfileId)
            .build();
    return readModifyWriteRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Modifies a row atomically on the server. The method reads the latest existing timestamp and
   * value from the specified columns and writes a new entry based on pre-defined read/modify/write
   * rules. The new value for the timestamp is the greater of the existing timestamp or the current
   * server time. The method returns the new contents of all modified cells.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   String tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString();
   *   ByteString rowKey = ByteString.EMPTY;
   *   List<ReadModifyWriteRule> rules = new ArrayList<>();
   *   String appProfileId = "appProfileId704923523";
   *   ReadModifyWriteRowResponse response =
   *       baseBigtableDataClient.readModifyWriteRow(tableName, rowKey, rules, appProfileId);
   * }
   * }</pre>
   *
   * @param tableName Required. The unique name of the table to which the read/modify/write rules
   *     should be applied. Values are of the form
   *     `projects/&lt;project&gt;/instances/&lt;instance&gt;/tables/&lt;table&gt;`.
   * @param rowKey Required. The key of the row to which the read/modify/write rules should be
   *     applied.
   * @param rules Required. Rules specifying how the specified row's contents are to be transformed
   *     into writes. Entries are applied in order, meaning that earlier rules will affect the
   *     results of later ones.
   * @param appProfileId This value specifies routing for replication. If not specified, the
   *     "default" application profile will be used.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ReadModifyWriteRowResponse readModifyWriteRow(
      String tableName, ByteString rowKey, List<ReadModifyWriteRule> rules, String appProfileId) {
    ReadModifyWriteRowRequest request =
        ReadModifyWriteRowRequest.newBuilder()
            .setTableName(tableName)
            .setRowKey(rowKey)
            .addAllRules(rules)
            .setAppProfileId(appProfileId)
            .build();
    return readModifyWriteRow(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Modifies a row atomically on the server. The method reads the latest existing timestamp and
   * value from the specified columns and writes a new entry based on pre-defined read/modify/write
   * rules. The new value for the timestamp is the greater of the existing timestamp or the current
   * server time. The method returns the new contents of all modified cells.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   ReadModifyWriteRowRequest request =
   *       ReadModifyWriteRowRequest.newBuilder()
   *           .setTableName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
   *           .setAppProfileId("appProfileId704923523")
   *           .setRowKey(ByteString.EMPTY)
   *           .addAllRules(new ArrayList<ReadModifyWriteRule>())
   *           .build();
   *   ReadModifyWriteRowResponse response = baseBigtableDataClient.readModifyWriteRow(request);
   * }
   * }</pre>
   *
   * @param request The request object containing all of the parameters for the API call.
   * @throws com.google.api.gax.rpc.ApiException if the remote call fails
   */
  public final ReadModifyWriteRowResponse readModifyWriteRow(ReadModifyWriteRowRequest request) {
    return readModifyWriteRowCallable().call(request);
  }

  // AUTO-GENERATED DOCUMENTATION AND METHOD.
  /**
   * Modifies a row atomically on the server. The method reads the latest existing timestamp and
   * value from the specified columns and writes a new entry based on pre-defined read/modify/write
   * rules. The new value for the timestamp is the greater of the existing timestamp or the current
   * server time. The method returns the new contents of all modified cells.
   *
   * <p>Sample code:
   *
   * <pre>{@code
   * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
   *   ReadModifyWriteRowRequest request =
   *       ReadModifyWriteRowRequest.newBuilder()
   *           .setTableName(TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]").toString())
   *           .setAppProfileId("appProfileId704923523")
   *           .setRowKey(ByteString.EMPTY)
   *           .addAllRules(new ArrayList<ReadModifyWriteRule>())
   *           .build();
   *   ApiFuture<ReadModifyWriteRowResponse> future =
   *       baseBigtableDataClient.readModifyWriteRowCallable().futureCall(request);
   *   // Do something.
   *   ReadModifyWriteRowResponse response = future.get();
   * }
   * }</pre>
   */
  public final UnaryCallable<ReadModifyWriteRowRequest, ReadModifyWriteRowResponse>
      readModifyWriteRowCallable() {
    return stub.readModifyWriteRowCallable();
  }

  @Override
  public final void close() {
    stub.close();
  }

  @Override
  public void shutdown() {
    stub.shutdown();
  }

  @Override
  public boolean isShutdown() {
    return stub.isShutdown();
  }

  @Override
  public boolean isTerminated() {
    return stub.isTerminated();
  }

  @Override
  public void shutdownNow() {
    stub.shutdownNow();
  }

  @Override
  public boolean awaitTermination(long duration, TimeUnit unit) throws InterruptedException {
    return stub.awaitTermination(duration, unit);
  }
}
