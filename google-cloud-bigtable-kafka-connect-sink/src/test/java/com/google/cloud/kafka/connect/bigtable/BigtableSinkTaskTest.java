/*
 * Copyright 2024 Google LLC
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
package com.google.cloud.kafka.connect.bigtable;

import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_AUTO_CREATE_TABLES;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_ERROR_MODE;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_INSERT_MODE;
import static com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig.CONFIG_TABLE_NAME_FORMAT;
import static com.google.cloud.kafka.connect.bigtable.util.FutureUtil.completedApiFuture;
import static com.google.cloud.kafka.connect.bigtable.util.MockUtil.assertTotalNumberOfInvocations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.openMocks;

import com.google.api.gax.batching.Batcher;
import com.google.api.gax.rpc.ApiException;
import com.google.bigtable.admin.v2.Table;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.cloud.kafka.connect.bigtable.autocreate.BigtableSchemaManager;
import com.google.cloud.kafka.connect.bigtable.autocreate.ResourceCreationResult;
import com.google.cloud.kafka.connect.bigtable.config.BigtableErrorMode;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkTaskConfig;
import com.google.cloud.kafka.connect.bigtable.config.InsertMode;
import com.google.cloud.kafka.connect.bigtable.config.NullValueMode;
import com.google.cloud.kafka.connect.bigtable.exception.BigtableSinkLogicError;
import com.google.cloud.kafka.connect.bigtable.exception.InvalidBigtableSchemaModificationException;
import com.google.cloud.kafka.connect.bigtable.mapping.KeyMapper;
import com.google.cloud.kafka.connect.bigtable.mapping.MutationData;
import com.google.cloud.kafka.connect.bigtable.mapping.MutationDataBuilder;
import com.google.cloud.kafka.connect.bigtable.mapping.ValueMapper;
import com.google.cloud.kafka.connect.bigtable.util.ApiExceptionFactory;
import com.google.cloud.kafka.connect.bigtable.util.BasicPropertiesFactory;
import com.google.cloud.kafka.connect.bigtable.util.FutureUtil;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.collections4.iterators.PermutationIterator;
import org.apache.kafka.common.record.TimestampType;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.ErrantRecordReporter;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTaskContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.slf4j.Logger;

@RunWith(JUnit4.class)
public class BigtableSinkTaskTest {
  TestBigtableSinkTask task;
  BigtableSinkTaskConfig config;
  @Mock BigtableDataClient bigtableData;
  @Mock BigtableTableAdminClient bigtableAdmin;
  @Mock KeyMapper keyMapper;
  @Mock ValueMapper valueMapper;
  @Mock BigtableSchemaManager schemaManager;
  @Mock SinkTaskContext context;
  @Mock ErrantRecordReporter errorReporter;

  @Before
  public void setUp() {
    openMocks(this);
    config = new BigtableSinkTaskConfig(BasicPropertiesFactory.getTaskProps());
  }

  @Test
  public void testStart() {
    task = spy(new TestBigtableSinkTask(null, null, null, null, null, null, null));
    task.start(BasicPropertiesFactory.getTaskProps());
  }

  @Test
  public void testStop() throws InterruptedException {
    for (List<Boolean> test :
        List.of(
            List.of(false, false),
            List.of(false, true),
            List.of(true, false),
            List.of(true, true))) {
      assertEquals(2, test.size());
      boolean adminIsNotNull = test.get(0);
      boolean dataIsNotNull = test.get(1);
      int expectedAdminCloseCallCount = adminIsNotNull ? 1 : 0;
      int expectedDataCloseCallCount = dataIsNotNull ? 1 : 0;

      BigtableTableAdminClient maybeAdmin = adminIsNotNull ? bigtableAdmin : null;
      BigtableDataClient maybeData = dataIsNotNull ? bigtableData : null;
      task = new TestBigtableSinkTask(null, maybeData, maybeAdmin, null, null, null, null);
      Batcher<RowMutationEntry, Void> batcher = mock(Batcher.class);
      doReturn(completedApiFuture(null)).when(batcher).closeAsync();
      task.getBatchers().put("batcherTable", batcher);

      doThrow(new RuntimeException()).when(bigtableAdmin).close();
      doThrow(new RuntimeException()).when(bigtableData).close();

      assertFalse(task.getBatchers().isEmpty());
      task.stop();
      assertTrue(task.getBatchers().isEmpty());
      verify(bigtableAdmin, times(expectedAdminCloseCallCount)).close();
      verify(bigtableData, times(expectedDataCloseCallCount)).close();
      verify(batcher, times(1)).closeAsync();

      reset(bigtableAdmin);
      reset(bigtableData);
    }
  }

  @Test
  public void testVersion() {
    task = spy(new TestBigtableSinkTask(null, null, null, null, null, null, null));
    assertNotNull(task.version());
  }

  @Test
  public void testGetTableName() {
    String tableFormat = "table";
    SinkRecord record = new SinkRecord("topic", 1, null, null, null, null, 1);
    Map<String, String> props = BasicPropertiesFactory.getTaskProps();
    props.put(CONFIG_TABLE_NAME_FORMAT, tableFormat);
    task =
        new TestBigtableSinkTask(
            new BigtableSinkTaskConfig(props), null, null, null, null, null, null);
    assertEquals(tableFormat, task.getTableName(record));
  }

  @Test
  public void testCreateRecordMutationDataEmptyKey() {
    task = new TestBigtableSinkTask(config, null, null, keyMapper, null, null, null);
    doReturn(new byte[0]).when(keyMapper).getKey(any());
    SinkRecord record = new SinkRecord("topic", 1, null, new Object(), null, null, 1);
    assertThrows(ConnectException.class, () -> task.createRecordMutationData(record));
  }

  @Test
  public void testCreateRecordMutationDataNonemptyKey() {
    SinkRecord emptyRecord = new SinkRecord("topic", 1, null, "key", null, null, 1);
    SinkRecord okRecord = new SinkRecord("topic", 1, null, "key", null, "value", 2);
    keyMapper = new KeyMapper("#", List.of());
    valueMapper = new ValueMapper("default", "KAFKA_VALUE", NullValueMode.IGNORE);
    task = new TestBigtableSinkTask(config, null, null, keyMapper, valueMapper, null, null);

    assertTrue(task.createRecordMutationData(emptyRecord).isEmpty());
    assertTrue(task.createRecordMutationData(okRecord).isPresent());
  }

  @Test
  public void testErrorReporterWithDLQ() {
    doReturn(errorReporter).when(context).errantRecordReporter();
    task = new TestBigtableSinkTask(null, null, null, null, null, null, context);
    SinkRecord record = new SinkRecord(null, 1, null, null, null, null, 1);
    Throwable t = new Exception("testErrorReporterWithDLQ");
    verifyNoMoreInteractions(task.getLogger());
    task.reportError(record, t);
    verify(errorReporter, times(1)).report(record, t);
  }

  @Test
  public void testErrorReporterNoDLQIgnoreMode() {
    Map<String, String> props = BasicPropertiesFactory.getTaskProps();
    props.put(CONFIG_ERROR_MODE, BigtableErrorMode.IGNORE.name());
    BigtableSinkTaskConfig config = new BigtableSinkTaskConfig(props);

    doThrow(new NoSuchMethodError()).when(context).errantRecordReporter();
    task = new TestBigtableSinkTask(config, null, null, null, null, null, context);
    SinkRecord record = new SinkRecord(null, 1, null, null, null, null, 1);
    verifyNoMoreInteractions(task.getLogger());
    verifyNoMoreInteractions(errorReporter);
    task.reportError(record, new Exception("testErrorReporterWithDLQ"));
  }

  @Test
  public void testErrorReporterNoDLQWarnMode() {
    Map<String, String> props = BasicPropertiesFactory.getTaskProps();
    props.put(CONFIG_ERROR_MODE, BigtableErrorMode.WARN.name());
    BigtableSinkTaskConfig config = new BigtableSinkTaskConfig(props);

    doReturn(null).when(context).errantRecordReporter();
    task = new TestBigtableSinkTask(config, null, null, null, null, null, context);
    SinkRecord record = new SinkRecord(null, 1, null, "key", null, null, 1);
    Throwable t = new Exception("testErrorReporterNoDLQWarnMode");
    verifyNoMoreInteractions(errorReporter);
    task.reportError(record, t);
    verify(task.getLogger(), times(1)).warn(anyString(), eq(record.key()), eq(t));
  }

  @Test
  public void testErrorReporterNoDLQFailMode() {
    Map<String, String> props = BasicPropertiesFactory.getTaskProps();
    props.put(CONFIG_ERROR_MODE, BigtableErrorMode.FAIL.name());
    BigtableSinkTaskConfig config = new BigtableSinkTaskConfig(props);

    doReturn(null).when(context).errantRecordReporter();
    task = new TestBigtableSinkTask(config, null, null, null, null, null, context);
    SinkRecord record = new SinkRecord(null, 1, null, "key", null, null, 1);
    Throwable t = new Exception("testErrorReporterNoDLQFailMode");
    verifyNoMoreInteractions(errorReporter);
    verifyNoMoreInteractions(task.getLogger());
    assertThrows(ConnectException.class, () -> task.reportError(record, t));
  }

  @Test
  public void testGetTimestamp() {
    task = new TestBigtableSinkTask(null, null, null, null, null, null, null);
    long timestampMillis = 123L;
    SinkRecord recordWithTimestamp =
        new SinkRecord(
            null, 1, null, null, null, null, 1, timestampMillis, TimestampType.CREATE_TIME);
    SinkRecord recordWithNullTimestamp = new SinkRecord(null, 1, null, null, null, null, 2);

    assertEquals(
        (Long) (1000L * timestampMillis), (Long) task.getTimestampMicros(recordWithTimestamp));
    assertNotNull(task.getTimestampMicros(recordWithNullTimestamp));

    // Assertion that the Java Bigtable client doesn't support microsecond timestamp granularity.
    // When it starts supporting it, getTimestamp() will need to get modified.
    assertEquals(
        Arrays.stream(Table.TimestampGranularity.values()).collect(Collectors.toSet()),
        Set.of(
            Table.TimestampGranularity.TIMESTAMP_GRANULARITY_UNSPECIFIED,
            Table.TimestampGranularity.MILLIS,
            Table.TimestampGranularity.UNRECOGNIZED));
  }

  @Test
  public void testHandleResults() {
    SinkRecord errorSinkRecord = new SinkRecord("", 1, null, null, null, null, 1);
    SinkRecord successSinkRecord = new SinkRecord("", 1, null, null, null, null, 2);
    Map<SinkRecord, Future<Void>> perRecordResults =
        Map.of(
            errorSinkRecord, CompletableFuture.failedFuture(new Exception("testHandleResults")),
            successSinkRecord, CompletableFuture.completedFuture(null));
    doReturn(errorReporter).when(context).errantRecordReporter();
    task = new TestBigtableSinkTask(null, null, null, null, null, null, context);
    task.handleResults(perRecordResults);
    verify(errorReporter, times(1)).report(eq(errorSinkRecord), any());
    assertTotalNumberOfInvocations(errorReporter, 1);
  }

  @Test
  public void testPrepareRecords() {
    task = spy(new TestBigtableSinkTask(null, null, null, null, null, null, context));
    doReturn(errorReporter).when(context).errantRecordReporter();

    MutationData okMutationData = mock(MutationData.class);
    Exception exception = new RuntimeException();
    doThrow(exception)
        .doReturn(Optional.empty())
        .doReturn(Optional.of(okMutationData))
        .when(task)
        .createRecordMutationData(any());

    SinkRecord exceptionRecord = new SinkRecord("", 1, null, null, null, null, 1);
    SinkRecord emptyRecord = new SinkRecord("", 1, null, null, null, null, 3);
    SinkRecord okRecord = new SinkRecord("", 1, null, null, null, null, 2);

    Map<SinkRecord, MutationData> result =
        task.prepareRecords(List.of(exceptionRecord, emptyRecord, okRecord));
    assertEquals(Map.of(okRecord, okMutationData), result);
    verify(errorReporter, times(1)).report(exceptionRecord, exception);
    assertTotalNumberOfInvocations(errorReporter, 1);
  }

  @Test
  public void testOrderMapSuccesses() {
    Integer key1 = 1;
    Integer key2 = 2;
    Integer key3 = 3;
    Integer key4 = 4;

    String value1 = "value1";
    String value2 = "value2";
    String value3 = "value3";
    String value4 = "value4";

    Map<Integer, String> map1 = new LinkedHashMap<>();
    map1.put(key4, value4);
    map1.put(key3, value3);
    map1.put(key2, value2);
    map1.put(key1, value1);

    assertEquals(List.of(key4, key3, key2, key1), new ArrayList<>(map1.keySet()));
    assertEquals(List.of(value4, value3, value2, value1), new ArrayList<>(map1.values()));
    assertEquals(
        List.of(key1, key2, key3, key4),
        new ArrayList<>(BigtableSinkTask.orderMap(map1, List.of(key1, key2, key3, key4)).keySet()));
    assertEquals(
        List.of(value1, value2, value3, value4),
        new ArrayList<>(BigtableSinkTask.orderMap(map1, List.of(key1, key2, key3, key4)).values()));
    assertEquals(
        List.of(
            new AbstractMap.SimpleImmutableEntry<>(key1, value1),
            new AbstractMap.SimpleImmutableEntry<>(key2, value2),
            new AbstractMap.SimpleImmutableEntry<>(key3, value3),
            new AbstractMap.SimpleImmutableEntry<>(key4, value4)),
        new ArrayList<>(
            BigtableSinkTask.orderMap(map1, List.of(key1, key2, key3, key4)).entrySet()));

    assertEquals(
        List.of(key1, key2, key3, key4),
        new ArrayList<>(
            BigtableSinkTask.orderMap(map1, List.of(-1, key1, -2, key2, -3, key3, -4, key4, -5))
                .keySet()));

    PermutationIterator<Integer> permutations =
        new PermutationIterator<>(List.of(key1, key2, key3, key4));
    permutations.forEachRemaining(
        p -> assertEquals(p, new ArrayList<>(BigtableSinkTask.orderMap(map1, p).keySet())));
  }

  @Test
  public void testOrderMapError() {
    Map<Integer, String> map = Map.of(1, "1", 2, "2", -1, "-1");
    assertThrows(
        BigtableSinkLogicError.class, () -> BigtableSinkTask.orderMap(map, Set.of(1, 2)));
  }

  @Test
  public void testAutoCreateTablesAndHandleErrors() {
    task = spy(new TestBigtableSinkTask(null, null, null, null, null, schemaManager, context));
    doReturn(errorReporter).when(context).errantRecordReporter();

    doReturn(errorReporter).when(context).errantRecordReporter();
    SinkRecord okRecord = new SinkRecord("", 1, null, null, null, null, 1);
    SinkRecord bigtableErrorRecord = new SinkRecord("", 1, null, null, null, null, 2);
    SinkRecord dataErrorRecord = new SinkRecord("", 1, null, null, null, null, 3);
    MutationData okMutationData = mock(MutationData.class);
    MutationData bigtableErrorMutationData = mock(MutationData.class);
    MutationData dataErrorMutationData = mock(MutationData.class);

    Map<SinkRecord, MutationData> mutations = new HashMap<>();
    mutations.put(okRecord, okMutationData);
    mutations.put(bigtableErrorRecord, bigtableErrorMutationData);
    mutations.put(dataErrorRecord, dataErrorMutationData);

    ResourceCreationResult resourceCreationResult =
        new ResourceCreationResult(Set.of(bigtableErrorRecord), Set.of(dataErrorRecord));
    doReturn(resourceCreationResult).when(schemaManager).ensureTablesExist(any());
    Map<SinkRecord, MutationData> mutationsToApply =
        task.autoCreateTablesAndHandleErrors(mutations);

    assertEquals(Map.of(okRecord, okMutationData), mutationsToApply);
    verify(errorReporter, times(1))
        .report(eq(bigtableErrorRecord), argThat(e -> e instanceof ConnectException));
    verify(errorReporter, times(1))
        .report(
            eq(dataErrorRecord),
            argThat(e -> e instanceof InvalidBigtableSchemaModificationException));
    assertTotalNumberOfInvocations(errorReporter, 2);
  }

  @Test
  public void testAutoCreateColumnFamiliesAndHandleErrors() {
    task = spy(new TestBigtableSinkTask(null, null, null, null, null, schemaManager, context));
    doReturn(errorReporter).when(context).errantRecordReporter();

    doReturn(errorReporter).when(context).errantRecordReporter();
    SinkRecord okRecord = new SinkRecord("", 1, null, null, null, null, 1);
    SinkRecord bigtableErrorRecord = new SinkRecord("", 1, null, null, null, null, 2);
    SinkRecord dataErrorRecord = new SinkRecord("", 1, null, null, null, null, 3);
    MutationData okMutationData = mock(MutationData.class);
    MutationData bigtableErrorMutationData = mock(MutationData.class);
    MutationData dataErrorMutationData = mock(MutationData.class);

    Map<SinkRecord, MutationData> mutations = new HashMap<>();
    mutations.put(okRecord, okMutationData);
    mutations.put(bigtableErrorRecord, bigtableErrorMutationData);
    mutations.put(dataErrorRecord, dataErrorMutationData);

    ResourceCreationResult resourceCreationResult =
        new ResourceCreationResult(Set.of(bigtableErrorRecord), Set.of(dataErrorRecord));
    doReturn(resourceCreationResult).when(schemaManager).ensureColumnFamiliesExist(any());
    Map<SinkRecord, MutationData> mutationsToApply =
        task.autoCreateColumnFamiliesAndHandleErrors(mutations);

    assertEquals(Map.of(okRecord, okMutationData), mutationsToApply);
    verify(errorReporter, times(1))
        .report(eq(bigtableErrorRecord), argThat(e -> e instanceof ConnectException));
    verify(errorReporter, times(1))
        .report(
            eq(dataErrorRecord),
            argThat(e -> e instanceof InvalidBigtableSchemaModificationException));
    assertTotalNumberOfInvocations(errorReporter, 2);
  }

  @Test
  public void testInsertRows() throws ExecutionException, InterruptedException {
    task = new TestBigtableSinkTask(null, bigtableData, null, null, null, null, null);
    ApiException exception = ApiExceptionFactory.create();
    doReturn(false).doReturn(true).doThrow(exception).when(bigtableData).checkAndMutateRow(any());

    SinkRecord successRecord = new SinkRecord("", 1, null, null, null, null, 1);
    SinkRecord errorRecord = new SinkRecord("", 1, null, null, null, null, 2);
    SinkRecord exceptionRecord = new SinkRecord("", 1, null, null, null, null, 3);
    MutationData commonMutationData = mock(MutationData.class);
    doReturn("ignored").when(commonMutationData).getTargetTable();
    doReturn(ByteString.copyFrom("ignored".getBytes(StandardCharsets.UTF_8)))
        .when(commonMutationData)
        .getRowKey();
    doReturn(mock(Mutation.class)).when(commonMutationData).getInsertMutation();

    // LinkedHashMap, because we mock consecutive return values of Bigtable client mock and thus
    // rely on the order.
    Map<SinkRecord, MutationData> input = new LinkedHashMap<>();
    input.put(successRecord, commonMutationData);
    input.put(errorRecord, commonMutationData);
    input.put(exceptionRecord, commonMutationData);
    Map<SinkRecord, Future<Void>> output = new HashMap<>();
    task.insertRows(input, output);

    assertEquals(input.keySet(), output.keySet());
    verify(bigtableData, times(input.size())).checkAndMutateRow(any());
    assertTotalNumberOfInvocations(bigtableData, input.size());

    output.get(successRecord).get();
    assertThrows(ExecutionException.class, () -> output.get(errorRecord).get());
    assertThrows(ExecutionException.class, () -> output.get(exceptionRecord).get());
  }

  @Test
  public void testUpsertRows() {
    Map<String, String> props = BasicPropertiesFactory.getTaskProps();
    int maxBatchSize = 3;
    int totalRecords = 1000;
    props.put(BigtableSinkTaskConfig.CONFIG_MAX_BATCH_SIZE, Integer.toString(maxBatchSize));
    BigtableSinkTaskConfig config = new BigtableSinkTaskConfig(props);

    task = spy(new TestBigtableSinkTask(config, null, null, null, null, null, null));
    String batcherTable = "batcherTable";
    Batcher<RowMutationEntry, Void> batcher = mock(Batcher.class);
    doAnswer(
            invocation -> {
              TestBigtableSinkTask task = (TestBigtableSinkTask) invocation.getMock();
              task.getBatchers().computeIfAbsent(batcherTable, ignored -> batcher);
              return null;
            })
        .when(task)
        .performUpsertBatch(any(), any());

    MutationData commonMutationData = mock(MutationData.class);

    Map<SinkRecord, MutationData> input =
        IntStream.range(0, totalRecords)
            .mapToObj(i -> new SinkRecord("", 1, null, null, null, null, i))
            .collect(Collectors.toMap(i -> i, ignored -> commonMutationData));

    Map<SinkRecord, Future<Void>> fakeMutationData = mock(Map.class);
    assertTrue(task.getBatchers().isEmpty());
    task.upsertRows(input, fakeMutationData);
    assertEquals(Set.of(batcher), task.getBatchers().values().stream().collect(Collectors.toSet()));

    int expectedFullBatches = totalRecords / maxBatchSize;
    int expectedPartialBatches = totalRecords % maxBatchSize == 0 ? 0 : 1;

    verify(task, times(expectedFullBatches))
        .performUpsertBatch(argThat(v -> v.size() == maxBatchSize), any());
    verify(task, times(expectedPartialBatches))
        .performUpsertBatch(argThat(v -> v.size() != maxBatchSize), any());
  }

  @Test
  public void testPerformUpsertBatch() throws ExecutionException, InterruptedException {
    String okTable = "okTable";
    String errorTable = "errorTable";

    Batcher<RowMutationEntry, Void> okBatcher = mock(Batcher.class);
    doReturn(completedApiFuture(null)).when(okBatcher).add(any());
    Batcher<RowMutationEntry, Void> errorBatcher = mock(Batcher.class);
    doReturn(FutureUtil.failedApiFuture(new Exception())).when(errorBatcher).add(any());

    doReturn(okBatcher).when(bigtableData).newBulkMutationBatcher(okTable);
    doReturn(errorBatcher).when(bigtableData).newBulkMutationBatcher(errorTable);
    task = new TestBigtableSinkTask(null, bigtableData, null, null, null, null, null);

    SinkRecord okRecord = new SinkRecord(okTable, 1, null, null, null, null, 1);
    SinkRecord errorRecord = new SinkRecord(errorTable, 1, null, null, null, null, 2);

    MutationData okMutationData = mock(MutationData.class);
    doReturn(okTable).when(okMutationData).getTargetTable();
    doReturn(mock(RowMutationEntry.class)).when(okMutationData).getUpsertMutation();
    MutationData errorMutationData = mock(MutationData.class);
    doReturn(errorTable).when(errorMutationData).getTargetTable();
    doReturn(mock(RowMutationEntry.class)).when(errorMutationData).getUpsertMutation();

    Map<SinkRecord, MutationData> input =
        Map.of(
            okRecord, okMutationData,
            errorRecord, errorMutationData);
    Map<SinkRecord, Future<Void>> output = new HashMap<>();

    assertTrue(task.getBatchers().isEmpty());
    task.performUpsertBatch(new ArrayList<>(input.entrySet()), output);
    assertEquals(
        Set.of(okBatcher, errorBatcher),
        task.getBatchers().values().stream().collect(Collectors.toSet()));

    assertEquals(input.keySet(), output.keySet());
    verify(okBatcher, times(1)).add(any());
    verify(okBatcher, times(1)).sendOutstanding();
    assertTotalNumberOfInvocations(okBatcher, 2);
    verify(errorBatcher, times(1)).add(any());
    verify(errorBatcher, times(1)).sendOutstanding();
    assertTotalNumberOfInvocations(errorBatcher, 2);

    output.get(okRecord).get();
    assertThrows(ExecutionException.class, () -> output.get(errorRecord).get());
  }

  @Test
  public void testPutBranches() {
    SinkRecord record1 = new SinkRecord("table1", 1, null, null, null, null, 1);
    SinkRecord record2 = new SinkRecord("table2", 1, null, null, null, null, 2);

    for (List<Boolean> test :
        List.of(
            List.of(false, false, false),
            List.of(false, false, true),
            List.of(false, true, false),
            List.of(false, true, true),
            List.of(true, false, false),
            List.of(true, false, true),
            List.of(true, true, false),
            List.of(true, true, true))) {
      boolean autoCreateTables = test.get(0);
      boolean autoCreateColumnFamilies = test.get(1);
      boolean useInsertMode = test.get(2);

      Map<String, String> props = BasicPropertiesFactory.getTaskProps();
      props.put(CONFIG_AUTO_CREATE_TABLES, Boolean.toString(autoCreateTables));
      props.put(CONFIG_AUTO_CREATE_COLUMN_FAMILIES, Boolean.toString(autoCreateColumnFamilies));
      props.put(CONFIG_INSERT_MODE, (useInsertMode ? InsertMode.INSERT : InsertMode.UPSERT).name());
      config = new BigtableSinkTaskConfig(props);

      byte[] rowKey = "rowKey".getBytes(StandardCharsets.UTF_8);
      doReturn(rowKey).when(keyMapper).getKey(any());
      doAnswer(
              i -> {
                MutationDataBuilder builder = new MutationDataBuilder();
                builder.deleteRow();
                return builder;
              })
          .when(valueMapper)
          .getRecordMutationDataBuilder(any(), anyString(), anyLong());

      Batcher<RowMutationEntry, Void> batcher = mock(Batcher.class);
      doReturn(completedApiFuture(null)).when(batcher).add(any());
      doReturn(batcher).when(bigtableData).newBulkMutationBatcher(anyString());
      doReturn(new ResourceCreationResult(Collections.emptySet(), Collections.emptySet()))
          .when(schemaManager)
          .ensureTablesExist(any());
      doReturn(new ResourceCreationResult(Collections.emptySet(), Collections.emptySet()))
          .when(schemaManager)
          .ensureColumnFamiliesExist(any());

      task =
          spy(
              new TestBigtableSinkTask(
                  config, bigtableData, null, keyMapper, valueMapper, schemaManager, null));

      task.put(List.of(record1, record2));

      verify(task, times(1)).prepareRecords(any());
      verify(schemaManager, times(autoCreateTables ? 1 : 0)).ensureTablesExist(any());
      verify(schemaManager, times(autoCreateColumnFamilies ? 1 : 0))
          .ensureColumnFamiliesExist(any());
      verify(task, times(useInsertMode ? 1 : 0)).insertRows(any(), any());
      verify(task, times(useInsertMode ? 0 : 1)).upsertRows(any(), any());
      verify(task, times(1)).handleResults(any());

      reset(task);
      reset(schemaManager);
    }
  }

  private static class TestBigtableSinkTask extends BigtableSinkTask {
    public TestBigtableSinkTask(
        BigtableSinkTaskConfig config,
        BigtableDataClient bigtableData,
        BigtableTableAdminClient bigtableAdmin,
        KeyMapper keyMapper,
        ValueMapper valueMapper,
        BigtableSchemaManager schemaManager,
        SinkTaskContext context) {
      super(config, bigtableData, bigtableAdmin, keyMapper, valueMapper, schemaManager, context);
      this.logger = mock(Logger.class);
    }

    public Logger getLogger() {
      return logger;
    }

    public Map<String, Batcher<RowMutationEntry, Void>> getBatchers() {
      return batchers;
    }
  }
}
