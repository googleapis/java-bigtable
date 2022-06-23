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

package com.google.cloud.bigtable.data.v2;

import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.httpjson.GaxHttpJsonProperties;
import com.google.api.gax.httpjson.testing.MockHttpService;
import com.google.api.gax.rpc.ApiClientHeaderProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.ApiExceptionFactory;
import com.google.api.gax.rpc.InvalidArgumentException;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.testing.FakeStatusCode;
import com.google.bigtable.v2.CheckAndMutateRowResponse;
import com.google.bigtable.v2.InstanceName;
import com.google.bigtable.v2.MutateRowResponse;
import com.google.bigtable.v2.Mutation;
import com.google.bigtable.v2.PingAndWarmResponse;
import com.google.bigtable.v2.ReadModifyWriteRowResponse;
import com.google.bigtable.v2.ReadModifyWriteRule;
import com.google.bigtable.v2.Row;
import com.google.bigtable.v2.RowFilter;
import com.google.bigtable.v2.TableName;
import com.google.cloud.bigtable.data.v2.stub.HttpJsonBigtableStub;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

@Generated("by gapic-generator-java")
public class BaseBigtableDataClientHttpJsonTest {
  private static MockHttpService mockService;
  private static BaseBigtableDataClient client;

  @BeforeClass
  public static void startStaticServer() throws IOException {
    mockService =
        new MockHttpService(
            HttpJsonBigtableStub.getMethodDescriptors(),
            BaseBigtableDataSettings.getDefaultEndpoint());
    BaseBigtableDataSettings settings =
        BaseBigtableDataSettings.newHttpJsonBuilder()
            .setTransportChannelProvider(
                BaseBigtableDataSettings.defaultHttpJsonTransportProviderBuilder()
                    .setHttpTransport(mockService)
                    .build())
            .setCredentialsProvider(NoCredentialsProvider.create())
            .build();
    client = BaseBigtableDataClient.create(settings);
  }

  @AfterClass
  public static void stopServer() {
    client.close();
  }

  @Before
  public void setUp() {}

  @After
  public void tearDown() throws Exception {
    mockService.reset();
  }

  @Test
  public void readRowsTest() throws Exception {}

  @Test
  public void readRowsExceptionTest() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);
  }

  @Test
  public void sampleRowKeysTest() throws Exception {}

  @Test
  public void sampleRowKeysExceptionTest() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);
  }

  @Test
  public void mutateRowTest() throws Exception {
    MutateRowResponse expectedResponse = MutateRowResponse.newBuilder().build();
    mockService.addResponse(expectedResponse);

    TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
    ByteString rowKey = ByteString.EMPTY;
    List<Mutation> mutations = new ArrayList<>();

    MutateRowResponse actualResponse = client.mutateRow(tableName, rowKey, mutations);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void mutateRowExceptionTest() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
      ByteString rowKey = ByteString.EMPTY;
      List<Mutation> mutations = new ArrayList<>();
      client.mutateRow(tableName, rowKey, mutations);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void mutateRowTest2() throws Exception {
    MutateRowResponse expectedResponse = MutateRowResponse.newBuilder().build();
    mockService.addResponse(expectedResponse);

    String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
    ByteString rowKey = ByteString.EMPTY;
    List<Mutation> mutations = new ArrayList<>();

    MutateRowResponse actualResponse = client.mutateRow(tableName, rowKey, mutations);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void mutateRowExceptionTest2() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
      ByteString rowKey = ByteString.EMPTY;
      List<Mutation> mutations = new ArrayList<>();
      client.mutateRow(tableName, rowKey, mutations);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void mutateRowTest3() throws Exception {
    MutateRowResponse expectedResponse = MutateRowResponse.newBuilder().build();
    mockService.addResponse(expectedResponse);

    TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
    ByteString rowKey = ByteString.EMPTY;
    List<Mutation> mutations = new ArrayList<>();
    String appProfileId = "appProfileId704923523";

    MutateRowResponse actualResponse = client.mutateRow(tableName, rowKey, mutations, appProfileId);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void mutateRowExceptionTest3() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
      ByteString rowKey = ByteString.EMPTY;
      List<Mutation> mutations = new ArrayList<>();
      String appProfileId = "appProfileId704923523";
      client.mutateRow(tableName, rowKey, mutations, appProfileId);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void mutateRowTest4() throws Exception {
    MutateRowResponse expectedResponse = MutateRowResponse.newBuilder().build();
    mockService.addResponse(expectedResponse);

    String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
    ByteString rowKey = ByteString.EMPTY;
    List<Mutation> mutations = new ArrayList<>();
    String appProfileId = "appProfileId704923523";

    MutateRowResponse actualResponse = client.mutateRow(tableName, rowKey, mutations, appProfileId);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void mutateRowExceptionTest4() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
      ByteString rowKey = ByteString.EMPTY;
      List<Mutation> mutations = new ArrayList<>();
      String appProfileId = "appProfileId704923523";
      client.mutateRow(tableName, rowKey, mutations, appProfileId);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void mutateRowsTest() throws Exception {}

  @Test
  public void mutateRowsExceptionTest() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);
  }

  @Test
  public void checkAndMutateRowTest() throws Exception {
    CheckAndMutateRowResponse expectedResponse =
        CheckAndMutateRowResponse.newBuilder().setPredicateMatched(true).build();
    mockService.addResponse(expectedResponse);

    TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
    ByteString rowKey = ByteString.EMPTY;
    RowFilter predicateFilter = RowFilter.newBuilder().build();
    List<Mutation> trueMutations = new ArrayList<>();
    List<Mutation> falseMutations = new ArrayList<>();

    CheckAndMutateRowResponse actualResponse =
        client.checkAndMutateRow(tableName, rowKey, predicateFilter, trueMutations, falseMutations);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void checkAndMutateRowExceptionTest() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
      ByteString rowKey = ByteString.EMPTY;
      RowFilter predicateFilter = RowFilter.newBuilder().build();
      List<Mutation> trueMutations = new ArrayList<>();
      List<Mutation> falseMutations = new ArrayList<>();
      client.checkAndMutateRow(tableName, rowKey, predicateFilter, trueMutations, falseMutations);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void checkAndMutateRowTest2() throws Exception {
    CheckAndMutateRowResponse expectedResponse =
        CheckAndMutateRowResponse.newBuilder().setPredicateMatched(true).build();
    mockService.addResponse(expectedResponse);

    String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
    ByteString rowKey = ByteString.EMPTY;
    RowFilter predicateFilter = RowFilter.newBuilder().build();
    List<Mutation> trueMutations = new ArrayList<>();
    List<Mutation> falseMutations = new ArrayList<>();

    CheckAndMutateRowResponse actualResponse =
        client.checkAndMutateRow(tableName, rowKey, predicateFilter, trueMutations, falseMutations);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void checkAndMutateRowExceptionTest2() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
      ByteString rowKey = ByteString.EMPTY;
      RowFilter predicateFilter = RowFilter.newBuilder().build();
      List<Mutation> trueMutations = new ArrayList<>();
      List<Mutation> falseMutations = new ArrayList<>();
      client.checkAndMutateRow(tableName, rowKey, predicateFilter, trueMutations, falseMutations);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void checkAndMutateRowTest3() throws Exception {
    CheckAndMutateRowResponse expectedResponse =
        CheckAndMutateRowResponse.newBuilder().setPredicateMatched(true).build();
    mockService.addResponse(expectedResponse);

    TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
    ByteString rowKey = ByteString.EMPTY;
    RowFilter predicateFilter = RowFilter.newBuilder().build();
    List<Mutation> trueMutations = new ArrayList<>();
    List<Mutation> falseMutations = new ArrayList<>();
    String appProfileId = "appProfileId704923523";

    CheckAndMutateRowResponse actualResponse =
        client.checkAndMutateRow(
            tableName, rowKey, predicateFilter, trueMutations, falseMutations, appProfileId);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void checkAndMutateRowExceptionTest3() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
      ByteString rowKey = ByteString.EMPTY;
      RowFilter predicateFilter = RowFilter.newBuilder().build();
      List<Mutation> trueMutations = new ArrayList<>();
      List<Mutation> falseMutations = new ArrayList<>();
      String appProfileId = "appProfileId704923523";
      client.checkAndMutateRow(
          tableName, rowKey, predicateFilter, trueMutations, falseMutations, appProfileId);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void checkAndMutateRowTest4() throws Exception {
    CheckAndMutateRowResponse expectedResponse =
        CheckAndMutateRowResponse.newBuilder().setPredicateMatched(true).build();
    mockService.addResponse(expectedResponse);

    String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
    ByteString rowKey = ByteString.EMPTY;
    RowFilter predicateFilter = RowFilter.newBuilder().build();
    List<Mutation> trueMutations = new ArrayList<>();
    List<Mutation> falseMutations = new ArrayList<>();
    String appProfileId = "appProfileId704923523";

    CheckAndMutateRowResponse actualResponse =
        client.checkAndMutateRow(
            tableName, rowKey, predicateFilter, trueMutations, falseMutations, appProfileId);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void checkAndMutateRowExceptionTest4() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
      ByteString rowKey = ByteString.EMPTY;
      RowFilter predicateFilter = RowFilter.newBuilder().build();
      List<Mutation> trueMutations = new ArrayList<>();
      List<Mutation> falseMutations = new ArrayList<>();
      String appProfileId = "appProfileId704923523";
      client.checkAndMutateRow(
          tableName, rowKey, predicateFilter, trueMutations, falseMutations, appProfileId);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void pingAndWarmTest() throws Exception {
    PingAndWarmResponse expectedResponse = PingAndWarmResponse.newBuilder().build();
    mockService.addResponse(expectedResponse);

    InstanceName name = InstanceName.of("[PROJECT]", "[INSTANCE]");

    PingAndWarmResponse actualResponse = client.pingAndWarm(name);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void pingAndWarmExceptionTest() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      InstanceName name = InstanceName.of("[PROJECT]", "[INSTANCE]");
      client.pingAndWarm(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void pingAndWarmTest2() throws Exception {
    PingAndWarmResponse expectedResponse = PingAndWarmResponse.newBuilder().build();
    mockService.addResponse(expectedResponse);

    String name = "projects/project-3514/instances/instance-3514";

    PingAndWarmResponse actualResponse = client.pingAndWarm(name);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void pingAndWarmExceptionTest2() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      String name = "projects/project-3514/instances/instance-3514";
      client.pingAndWarm(name);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void pingAndWarmTest3() throws Exception {
    PingAndWarmResponse expectedResponse = PingAndWarmResponse.newBuilder().build();
    mockService.addResponse(expectedResponse);

    InstanceName name = InstanceName.of("[PROJECT]", "[INSTANCE]");
    String appProfileId = "appProfileId704923523";

    PingAndWarmResponse actualResponse = client.pingAndWarm(name, appProfileId);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void pingAndWarmExceptionTest3() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      InstanceName name = InstanceName.of("[PROJECT]", "[INSTANCE]");
      String appProfileId = "appProfileId704923523";
      client.pingAndWarm(name, appProfileId);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void pingAndWarmTest4() throws Exception {
    PingAndWarmResponse expectedResponse = PingAndWarmResponse.newBuilder().build();
    mockService.addResponse(expectedResponse);

    String name = "projects/project-3514/instances/instance-3514";
    String appProfileId = "appProfileId704923523";

    PingAndWarmResponse actualResponse = client.pingAndWarm(name, appProfileId);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void pingAndWarmExceptionTest4() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      String name = "projects/project-3514/instances/instance-3514";
      String appProfileId = "appProfileId704923523";
      client.pingAndWarm(name, appProfileId);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void readModifyWriteRowTest() throws Exception {
    ReadModifyWriteRowResponse expectedResponse =
        ReadModifyWriteRowResponse.newBuilder().setRow(Row.newBuilder().build()).build();
    mockService.addResponse(expectedResponse);

    TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
    ByteString rowKey = ByteString.EMPTY;
    List<ReadModifyWriteRule> rules = new ArrayList<>();

    ReadModifyWriteRowResponse actualResponse = client.readModifyWriteRow(tableName, rowKey, rules);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void readModifyWriteRowExceptionTest() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
      ByteString rowKey = ByteString.EMPTY;
      List<ReadModifyWriteRule> rules = new ArrayList<>();
      client.readModifyWriteRow(tableName, rowKey, rules);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void readModifyWriteRowTest2() throws Exception {
    ReadModifyWriteRowResponse expectedResponse =
        ReadModifyWriteRowResponse.newBuilder().setRow(Row.newBuilder().build()).build();
    mockService.addResponse(expectedResponse);

    String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
    ByteString rowKey = ByteString.EMPTY;
    List<ReadModifyWriteRule> rules = new ArrayList<>();

    ReadModifyWriteRowResponse actualResponse = client.readModifyWriteRow(tableName, rowKey, rules);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void readModifyWriteRowExceptionTest2() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
      ByteString rowKey = ByteString.EMPTY;
      List<ReadModifyWriteRule> rules = new ArrayList<>();
      client.readModifyWriteRow(tableName, rowKey, rules);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void readModifyWriteRowTest3() throws Exception {
    ReadModifyWriteRowResponse expectedResponse =
        ReadModifyWriteRowResponse.newBuilder().setRow(Row.newBuilder().build()).build();
    mockService.addResponse(expectedResponse);

    TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
    ByteString rowKey = ByteString.EMPTY;
    List<ReadModifyWriteRule> rules = new ArrayList<>();
    String appProfileId = "appProfileId704923523";

    ReadModifyWriteRowResponse actualResponse =
        client.readModifyWriteRow(tableName, rowKey, rules, appProfileId);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void readModifyWriteRowExceptionTest3() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
      ByteString rowKey = ByteString.EMPTY;
      List<ReadModifyWriteRule> rules = new ArrayList<>();
      String appProfileId = "appProfileId704923523";
      client.readModifyWriteRow(tableName, rowKey, rules, appProfileId);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }

  @Test
  public void readModifyWriteRowTest4() throws Exception {
    ReadModifyWriteRowResponse expectedResponse =
        ReadModifyWriteRowResponse.newBuilder().setRow(Row.newBuilder().build()).build();
    mockService.addResponse(expectedResponse);

    String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
    ByteString rowKey = ByteString.EMPTY;
    List<ReadModifyWriteRule> rules = new ArrayList<>();
    String appProfileId = "appProfileId704923523";

    ReadModifyWriteRowResponse actualResponse =
        client.readModifyWriteRow(tableName, rowKey, rules, appProfileId);
    Assert.assertEquals(expectedResponse, actualResponse);

    List<String> actualRequests = mockService.getRequestPaths();
    Assert.assertEquals(1, actualRequests.size());

    String apiClientHeaderKey =
        mockService
            .getRequestHeaders()
            .get(ApiClientHeaderProvider.getDefaultApiClientHeaderKey())
            .iterator()
            .next();
    Assert.assertTrue(
        GaxHttpJsonProperties.getDefaultApiClientHeaderPattern()
            .matcher(apiClientHeaderKey)
            .matches());
  }

  @Test
  public void readModifyWriteRowExceptionTest4() throws Exception {
    ApiException exception =
        ApiExceptionFactory.createException(
            new Exception(), FakeStatusCode.of(StatusCode.Code.INVALID_ARGUMENT), false);
    mockService.addException(exception);

    try {
      String tableName = "projects/project-6423/instances/instance-6423/tables/table-6423";
      ByteString rowKey = ByteString.EMPTY;
      List<ReadModifyWriteRule> rules = new ArrayList<>();
      String appProfileId = "appProfileId704923523";
      client.readModifyWriteRow(tableName, rowKey, rules, appProfileId);
      Assert.fail("No exception raised");
    } catch (InvalidArgumentException e) {
      // Expected exception.
    }
  }
}
