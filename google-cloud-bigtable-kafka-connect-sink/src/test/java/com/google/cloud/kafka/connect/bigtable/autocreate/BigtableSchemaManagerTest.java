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
package com.google.cloud.kafka.connect.bigtable.autocreate;

import static com.google.cloud.kafka.connect.bigtable.util.FutureUtil.completedApiFuture;
import static com.google.cloud.kafka.connect.bigtable.util.FutureUtil.failedApiFuture;
import static com.google.cloud.kafka.connect.bigtable.util.MockUtil.assertTotalNumberOfInvocations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.google.api.core.ApiFuture;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.models.ColumnFamily;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.admin.v2.models.ModifyColumnFamiliesRequest;
import com.google.cloud.bigtable.admin.v2.models.Table;
import com.google.cloud.kafka.connect.bigtable.autocreate.BigtableSchemaManager.ResourceAndRecords;
import com.google.cloud.kafka.connect.bigtable.mapping.MutationData;
import com.google.cloud.kafka.connect.bigtable.util.ApiExceptionFactory;
import io.grpc.Status;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BigtableSchemaManagerTest {
  BigtableTableAdminClient bigtable;
  TestBigtableSchemaManager bigtableSchemaManager;

  @Before
  public void setUp() {
    bigtable = mock(BigtableTableAdminClient.class);
    bigtableSchemaManager = spy(new TestBigtableSchemaManager(bigtable));
  }

  @Test
  public void testTableCachePopulationSuccess() {
    List<String> tables = List.of("table1", "table2");
    doReturn(tables).when(bigtable).listTables();
    bigtableSchemaManager.refreshTableNamesCache();
    assertEquals(new HashSet<>(tables), bigtableSchemaManager.getCache().keySet());
    assertTotalNumberOfInvocations(bigtable, 1);

    reset(bigtable);
    verifyNoInteractions(bigtable);
    Map<SinkRecord, MutationData> input =
        generateInput(
            tables.stream()
                .map(l -> new AbstractMap.SimpleImmutableEntry<>(l, Set.of("cf")))
                .collect(Collectors.toList()));
    ResourceCreationResult result = bigtableSchemaManager.ensureTablesExist(input);
    assertTrue(result.getBigtableErrors().isEmpty());
    assertTrue(result.getDataErrors().isEmpty());
  }

  @Test
  public void testTableCachePopulationMayRemoveElements() {
    List<String> tables1 = List.of("table1", "table2");
    List<String> tables2 = List.of(tables1.get(0));

    doReturn(tables1).when(bigtable).listTables();
    bigtableSchemaManager.refreshTableNamesCache();
    assertEquals(new HashSet<>(tables1), bigtableSchemaManager.getCache().keySet());
    reset(bigtable);

    doReturn(tables2).when(bigtable).listTables();
    bigtableSchemaManager.refreshTableNamesCache();
    assertEquals(new HashSet<>(tables2), bigtableSchemaManager.getCache().keySet());
    verify(bigtable, times(1)).listTables();
    assertTotalNumberOfInvocations(bigtable, 1);
  }

  @Test
  public void testTableCachePopulationError() {
    doThrow(ApiExceptionFactory.create()).when(bigtable).listTables();
    assertThrows(ConnectException.class, () -> bigtableSchemaManager.refreshTableNamesCache());
  }

  @Test
  public void testTableColumnFamiliesCachePopulationSuccess() {
    Map<String, Set<String>> tablesAndColumnFamilies =
        Map.of(
            "table1", Set.of("cf1", "cf2"),
            "table2", Set.of("cf3", "cf4"));
    doReturn(new ArrayList<>(tablesAndColumnFamilies.keySet())).when(bigtable).listTables();
    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      mockGetTableSuccess(bigtable, entry.getKey(), entry.getValue());
    }

    Set<String> refreshedTables = tablesAndColumnFamilies.keySet();
    bigtableSchemaManager.refreshTableColumnFamiliesCache(refreshedTables);
    verify(bigtableSchemaManager, times(1)).refreshTableColumnFamiliesCache(refreshedTables);
    verify(bigtableSchemaManager, times(1)).refreshTableNamesCache();
    assertTotalNumberOfInvocations(bigtableSchemaManager, 2);
    assertEquals(
        bigtableSchemaManager.getCache(),
        tablesAndColumnFamilies.entrySet().stream()
            .map(e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey(), Optional.of(e.getValue())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    verify(bigtable, times(1)).listTables();
    for (String tableName : tablesAndColumnFamilies.keySet()) {
      verify(bigtable, times(1)).getTableAsync(tableName);
    }
    // One for listTables() and one for each table lookup.
    int expectedBigtableCalls = tablesAndColumnFamilies.size() + 1;
    assertTotalNumberOfInvocations(bigtable, expectedBigtableCalls);

    reset(bigtable);
    verifyNoInteractions(bigtable);
    Map<SinkRecord, MutationData> input =
        generateInput(new ArrayList<>(tablesAndColumnFamilies.entrySet()));
    ResourceCreationResult result = bigtableSchemaManager.ensureTablesExist(input);
    assertTrue(result.getBigtableErrors().isEmpty());
    assertTrue(result.getDataErrors().isEmpty());
  }

  @Test
  public void testTableColumnFamiliesCachePopulationErrors() {
    doThrow(ApiExceptionFactory.create()).when(bigtable).listTables();
    assertThrows(
        ConnectException.class,
        () -> bigtableSchemaManager.refreshTableColumnFamiliesCache(Collections.emptySet()));
    verify(bigtable, times(1)).listTables();
    reset(bigtable);

    String successTable = "table1";
    String errorTable = "table2";
    List<String> allTables = List.of(successTable, errorTable);

    doReturn(allTables).when(bigtable).listTables();
    mockGetTableSuccess(bigtable, successTable, Collections.emptySet());
    // We simulate an error due to e.g., deletion of the table by another user.
    doReturn(failedApiFuture(ApiExceptionFactory.create()))
        .when(bigtable)
        .getTableAsync(errorTable);
    bigtableSchemaManager.refreshTableColumnFamiliesCache(new HashSet<>(allTables));
    assertEquals(Set.of(successTable), bigtableSchemaManager.getCache().keySet());
    verify(bigtable, times(1)).listTables();
    verify(bigtable, times(1)).getTableAsync(successTable);
    verify(bigtable, times(1)).getTableAsync(errorTable);
    assertTotalNumberOfInvocations(bigtable, 3);
  }

  @Test
  public void testEnsureTablesExistAllExisted() {
    // Prepopulate the cache.
    List<String> tables = List.of("table1", "table2");
    doReturn(tables).when(bigtable).listTables();
    bigtableSchemaManager.refreshTableNamesCache();
    reset(bigtable);

    Map<SinkRecord, MutationData> ensureTablesExistInput =
        generateInput(
            List.of(
                new AbstractMap.SimpleImmutableEntry<>(tables.get(0), Set.of("missingCF")),
                new AbstractMap.SimpleImmutableEntry<>(tables.get(0), Set.of("missingCF")),
                new AbstractMap.SimpleImmutableEntry<>(tables.get(1), Set.of("missingCF")),
                new AbstractMap.SimpleImmutableEntry<>(tables.get(1), Set.of("missingCF"))));
    ResourceCreationResult result = bigtableSchemaManager.ensureTablesExist(ensureTablesExistInput);
    assertTrue(result.getBigtableErrors().isEmpty());
    assertTrue(result.getDataErrors().isEmpty());
    assertTotalNumberOfInvocations(bigtable, 0);
  }

  @Test
  public void testEnsureTablesExistAllCreatedSuccessfully() {
    List<String> tables = List.of("table1", "table2");
    // We call listTables() only once, after sending all the create requests. In this case all the
    // requests were successful.
    doReturn(tables).when(bigtable).listTables();
    for (String table : tables) {
      mockCreateTableSuccess(bigtable, table, Collections.emptySet());
    }

    assertTrue(bigtableSchemaManager.getCache().isEmpty());
    Map<SinkRecord, MutationData> ensureTablesExistInput =
        generateInput(
            List.of(
                new AbstractMap.SimpleImmutableEntry<>(tables.get(0), Set.of("missingCF")),
                new AbstractMap.SimpleImmutableEntry<>(tables.get(0), Set.of("missingCF")),
                new AbstractMap.SimpleImmutableEntry<>(tables.get(1), Set.of("missingCF")),
                new AbstractMap.SimpleImmutableEntry<>(tables.get(1), Set.of("missingCF"))));
    ResourceCreationResult result = bigtableSchemaManager.ensureTablesExist(ensureTablesExistInput);
    assertTrue(result.getBigtableErrors().isEmpty());
    assertTrue(result.getDataErrors().isEmpty());
    for (String table : tables) {
      assertTrue(bigtableSchemaManager.getCache().containsKey(table));
      verify(bigtable, times(1))
          .createTableAsync(argThat(ctr -> createTableMockRefersTable(table, ctr)));
    }

    // One for each table creation and one for result check.
    int expectedBigtableCalls = tables.size() + 1;
    assertTotalNumberOfInvocations(bigtable, expectedBigtableCalls);
  }

  @Test
  public void testEnsureTablesExistSomeCreatedSuccessfullySomeErrorsDueToRaces() {
    List<String> tables = List.of("table1", "table2");
    // We call listTables() only once, after sending all the create requests. In this case some
    // requests failed since another thread concurrently created one of these tables.
    doReturn(tables).when(bigtable).listTables();
    String tableWhoseCreationFailed = tables.get(1);
    for (String table : tables) {
      if (!table.equals(tableWhoseCreationFailed)) {
        mockCreateTableSuccess(bigtable, table, Collections.emptySet());
      }
    }
    doReturn(failedApiFuture(ApiExceptionFactory.create()))
        .when(bigtable)
        .createTableAsync(
            argThat(ctr -> createTableMockRefersTable(tableWhoseCreationFailed, ctr)));

    assertTrue(bigtableSchemaManager.getCache().isEmpty());
    Map<SinkRecord, MutationData> ensureTablesExistInput =
        generateInput(
            List.of(
                new AbstractMap.SimpleImmutableEntry<>(tables.get(0), Set.of("missingCF")),
                new AbstractMap.SimpleImmutableEntry<>(tables.get(0), Set.of("missingCF")),
                new AbstractMap.SimpleImmutableEntry<>(tables.get(1), Set.of("missingCF")),
                new AbstractMap.SimpleImmutableEntry<>(tables.get(1), Set.of("missingCF"))));
    ResourceCreationResult result = bigtableSchemaManager.ensureTablesExist(ensureTablesExistInput);
    assertTrue(result.getBigtableErrors().isEmpty());
    assertTrue(result.getDataErrors().isEmpty());
    for (String table : tables) {
      assertTrue(bigtableSchemaManager.getCache().containsKey(table));
      verify(bigtable, times(1))
          .createTableAsync(argThat(ctr -> createTableMockRefersTable(table, ctr)));
    }

    // One for each table creation and one for result check.
    int expectedBigtableCalls = tables.size() + 1;
    assertTotalNumberOfInvocations(bigtable, expectedBigtableCalls);
  }

  @Test
  public void testEnsureTablesExistSomeCreatedSuccessfullySomeErrors() {
    String successfulTable = "table1";
    String bigtableErrorTable = "table2";
    String dataErrorTable = "table3";
    Set<String> columnFamilies = Set.of("cf1");

    doReturn(List.of(successfulTable)).when(bigtable).listTables();
    mockCreateTableSuccess(bigtable, successfulTable, columnFamilies);
    doReturn(failedApiFuture(ApiExceptionFactory.create(Status.Code.RESOURCE_EXHAUSTED)))
        .when(bigtable)
        .createTableAsync(argThat(ctr -> createTableMockRefersTable(bigtableErrorTable, ctr)));
    doReturn(failedApiFuture(ApiExceptionFactory.create(Status.Code.INVALID_ARGUMENT)))
        .when(bigtable)
        .createTableAsync(argThat(ctr -> createTableMockRefersTable(dataErrorTable, ctr)));

    assertTrue(bigtableSchemaManager.getCache().isEmpty());
    Map<SinkRecord, MutationData> ensureTablesExistInput =
        generateInput(
            List.of(
                new AbstractMap.SimpleImmutableEntry<>(successfulTable, columnFamilies),
                new AbstractMap.SimpleImmutableEntry<>(successfulTable, columnFamilies),
                new AbstractMap.SimpleImmutableEntry<>(bigtableErrorTable, columnFamilies),
                new AbstractMap.SimpleImmutableEntry<>(bigtableErrorTable, columnFamilies),
                new AbstractMap.SimpleImmutableEntry<>(dataErrorTable, columnFamilies),
                new AbstractMap.SimpleImmutableEntry<>(dataErrorTable, columnFamilies)));
    ResourceCreationResult result = bigtableSchemaManager.ensureTablesExist(ensureTablesExistInput);
    Set<SinkRecord> bigtableErrors = result.getBigtableErrors();
    Set<SinkRecord> dataErrors = result.getDataErrors();
    assertEquals(
        ensureTablesExistInput.entrySet().stream()
            .filter(e -> e.getValue().getTargetTable().equals(bigtableErrorTable))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet()),
        bigtableErrors);
    assertEquals(
        ensureTablesExistInput.entrySet().stream()
            .filter(e -> e.getValue().getTargetTable().equals(dataErrorTable))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet()),
        dataErrors);
    Map<String, Optional<Set<String>>> cache = bigtableSchemaManager.getCache();
    assertTrue(cache.containsKey(successfulTable));
    verify(bigtable, times(1))
        .createTableAsync(argThat(ctr -> createTableMockRefersTable(successfulTable, ctr)));
    assertFalse(cache.containsKey(bigtableErrorTable));
    verify(bigtable, times(1))
        .createTableAsync(argThat(ctr -> createTableMockRefersTable(bigtableErrorTable, ctr)));
    assertFalse(cache.containsKey(dataErrorTable));
    verify(bigtable, times(1))
        .createTableAsync(argThat(ctr -> createTableMockRefersTable(dataErrorTable, ctr)));

    // One for each table creation and one for result check.
    int expectedBigtableCalls = 4;
    assertTotalNumberOfInvocations(bigtable, expectedBigtableCalls);
  }

  @Test
  public void testEnsureTablesExistConcurrentDeletion() {
    String createdTable = "table1";
    String createdAndThenConcurrentlyDeletedTable = "table2";
    Set<String> columnFamilies = Set.of("cf1");

    // Note that only a single table is returned - we simulate concurrent deletion of the other
    // table.
    doAnswer(ignored -> List.of(createdTable)).when(bigtable).listTables();
    mockCreateTableSuccess(bigtable, createdTable, columnFamilies);
    mockCreateTableSuccess(bigtable, createdAndThenConcurrentlyDeletedTable, columnFamilies);

    assertTrue(bigtableSchemaManager.getCache().isEmpty());
    Map<SinkRecord, MutationData> ensureTablesExistInput =
        generateInput(
            List.of(
                new AbstractMap.SimpleImmutableEntry<>(createdTable, columnFamilies),
                new AbstractMap.SimpleImmutableEntry<>(createdTable, columnFamilies),
                new AbstractMap.SimpleImmutableEntry<>(
                    createdAndThenConcurrentlyDeletedTable, columnFamilies),
                new AbstractMap.SimpleImmutableEntry<>(
                    createdAndThenConcurrentlyDeletedTable, columnFamilies)));
    ResourceCreationResult result = bigtableSchemaManager.ensureTablesExist(ensureTablesExistInput);
    assertTrue(result.getDataErrors().isEmpty());
    Set<SinkRecord> missingTables = result.getBigtableErrors();
    assertEquals(
        ensureTablesExistInput.entrySet().stream()
            .filter(
                e -> e.getValue().getTargetTable().equals(createdAndThenConcurrentlyDeletedTable))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet()),
        missingTables);
    Map<String, Optional<Set<String>>> cache = bigtableSchemaManager.getCache();
    assertTrue(cache.containsKey(createdTable));
    verify(bigtable, times(1))
        .createTableAsync(argThat(ctr -> createTableMockRefersTable(createdTable, ctr)));
    assertFalse(cache.containsKey(createdAndThenConcurrentlyDeletedTable));
    verify(bigtable, times(1))
        .createTableAsync(
            argThat(
                ctr -> createTableMockRefersTable(createdAndThenConcurrentlyDeletedTable, ctr)));

    // One for each table creation and one for result check.
    int expectedBigtableCalls = 3;
    assertTotalNumberOfInvocations(bigtable, expectedBigtableCalls);
  }

  @Test
  public void testEnsureColumnFamiliesExistAllExisted() {
    Map<String, Set<String>> tablesAndColumnFamilies =
        Map.of(
            "table1", Set.of("cf1", "cf2"),
            "table2", Set.of("cf3", "cf4"));
    doReturn(new ArrayList<>(tablesAndColumnFamilies.keySet())).when(bigtable).listTables();
    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      mockGetTableSuccess(bigtable, entry.getKey(), entry.getValue());
    }
    Set<String> refreshedTables = tablesAndColumnFamilies.keySet();
    bigtableSchemaManager.refreshTableColumnFamiliesCache(refreshedTables);
    reset(bigtable);
    verifyNoInteractions(bigtable);

    Map<SinkRecord, MutationData> ensureColumnFamiliesExistInput =
        generateInput(new ArrayList<>(tablesAndColumnFamilies.entrySet()));
    ResourceCreationResult result =
        bigtableSchemaManager.ensureColumnFamiliesExist(ensureColumnFamiliesExistInput);
    assertTrue(result.getDataErrors().isEmpty());
    assertTrue(result.getBigtableErrors().isEmpty());
  }

  @Test
  public void testEnsureColumnFamiliesExistAllCreatedSuccessfully() {
    Map<String, Set<String>> tablesAndColumnFamilies =
        Map.of(
            "table1", Set.of("cf1", "cf2"),
            "table2", Set.of("cf3", "cf4"));
    doReturn(new ArrayList<>(tablesAndColumnFamilies.keySet())).when(bigtable).listTables();
    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      mockCreateColumnFamilySuccess(bigtable, entry.getKey(), entry.getValue());
      mockGetTableSuccess(bigtable, entry.getKey(), entry.getValue());
    }
    Map<SinkRecord, MutationData> ensureColumnFamiliesExistInput =
        generateInput(new ArrayList<>(tablesAndColumnFamilies.entrySet()));

    ResourceCreationResult result =
        bigtableSchemaManager.ensureColumnFamiliesExist(ensureColumnFamiliesExistInput);
    assertTrue(result.getDataErrors().isEmpty());
    assertTrue(result.getBigtableErrors().isEmpty());
    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      String tableName = entry.getKey();
      for (String columnFamily : entry.getValue()) {
        verify(bigtable, times(1))
            .modifyFamiliesAsync(
                argThat(
                    mcfr ->
                        createColumnFamilyMockRefersTableAndColumnFamily(
                            tableName, columnFamily, mcfr)));
      }
      verify(bigtable, times(1)).getTableAsync(tableName);
    }
    int expectedBigtableInteractions =
        1 // listTables()
            + tablesAndColumnFamilies.values().stream()
                .mapToInt(Set::size)
                .sum() // modifyColumnFamily()
            + tablesAndColumnFamilies.keySet().size(); // getTable()
    assertTotalNumberOfInvocations(bigtable, expectedBigtableInteractions);
  }

  @Test
  public void
      testEnsureColumnFamiliesExistSomeCreatedSuccessfullySomeErrorsDueToRacesOrInvalidRequests() {
    String successTable = "table1";
    String bigtableErrorTable = "table2";
    String dataErrorTable = "table3";
    String invalidArgumentColumnFamilyName = "INVALID_ARGUMENT_COLUMN_FAMILY_NAME";
    Map<String, Set<String>> tablesAndColumnFamilies =
        Map.of(
            successTable, Set.of("cf1", "cf2"),
            bigtableErrorTable, Set.of("cf3", "cf4"),
            dataErrorTable, Set.of("cf5", invalidArgumentColumnFamilyName));
    doReturn(new ArrayList<>(tablesAndColumnFamilies.keySet())).when(bigtable).listTables();
    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      String table = entry.getKey();
      for (String columnFamily : entry.getValue()) {
        if (table.equals(bigtableErrorTable)) {
          doReturn(failedApiFuture(ApiExceptionFactory.create(Status.Code.RESOURCE_EXHAUSTED)))
              .when(bigtable)
              .modifyFamiliesAsync(
                  argThat(
                      mcfr ->
                          createColumnFamilyMockRefersTableAndColumnFamily(
                              table, columnFamily, mcfr)));
        } else if (table.equals(dataErrorTable)) {
          doReturn(failedApiFuture(ApiExceptionFactory.create(Status.Code.INVALID_ARGUMENT)))
              .when(bigtable)
              .modifyFamiliesAsync(
                  argThat(
                      mcfr ->
                          createColumnFamilyMockRefersTableAndColumnFamily(
                              table, columnFamily, mcfr)));
        } else {
          mockCreateColumnFamilySuccess(bigtable, entry.getKey(), entry.getValue());
        }
      }
      Set<String> columnFamilies = new HashSet<>(entry.getValue());
      columnFamilies.remove(invalidArgumentColumnFamilyName);
      mockGetTableSuccess(bigtable, table, columnFamilies);
    }
    Map<SinkRecord, MutationData> ensureColumnFamiliesExistInput =
        generateInput(new ArrayList<>(tablesAndColumnFamilies.entrySet()));
    ResourceCreationResult result =
        bigtableSchemaManager.ensureColumnFamiliesExist(ensureColumnFamiliesExistInput);
    assertTrue(result.getBigtableErrors().isEmpty());
    Set<SinkRecord> missingColumnFamilies = result.getDataErrors();
    assertEquals(
        ensureColumnFamiliesExistInput.entrySet().stream()
            .filter(e -> e.getValue().getTargetTable().equals(dataErrorTable))
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet()),
        missingColumnFamilies);

    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      String tableName = entry.getKey();
      for (String columnFamily : entry.getValue()) {
        verify(bigtable, times(1))
            .modifyFamiliesAsync(
                argThat(
                    mcfr ->
                        createColumnFamilyMockRefersTableAndColumnFamily(
                            tableName, columnFamily, mcfr)));
      }
      verify(bigtable, times(1)).getTableAsync(tableName);
    }
    int expectedBigtableInteractions =
        1 // listTables()
            + tablesAndColumnFamilies.values().stream()
                .mapToInt(Set::size)
                .sum() // modifyColumnFamily()
            + tablesAndColumnFamilies.keySet().size(); // getTable()
    assertTotalNumberOfInvocations(bigtable, expectedBigtableInteractions);
  }

  @Test
  public void testEnsureColumnFamiliesExistSomeSomeErrorsDueToConcurrentColumnFamilyDeletion() {
    String successTable = "table1";
    String errorTable = "table2";
    Map<String, Set<String>> tablesAndColumnFamilies =
        Map.of(
            successTable, Set.of("cf1", "cf2"),
            errorTable, Set.of("cf3", "cf4"));
    doReturn(new ArrayList<>(tablesAndColumnFamilies.keySet())).when(bigtable).listTables();
    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      String table = entry.getKey();
      mockCreateColumnFamilySuccess(bigtable, table, entry.getValue());
      if (table.equals(errorTable)) {
        doReturn(failedApiFuture(ApiExceptionFactory.create())).when(bigtable).getTableAsync(table);
      } else {
        mockGetTableSuccess(bigtable, table, entry.getValue());
      }
    }
    Map<SinkRecord, MutationData> ensureColumnFamiliesExistInput =
        generateInput(new ArrayList<>(tablesAndColumnFamilies.entrySet()));
    ResourceCreationResult result =
        bigtableSchemaManager.ensureColumnFamiliesExist(ensureColumnFamiliesExistInput);
    assertTrue(result.getDataErrors().isEmpty());
    Set<SinkRecord> missingColumnFamilies = result.getBigtableErrors();
    assertEquals(1, missingColumnFamilies.size());
    assertEquals(
        ensureColumnFamiliesExistInput.entrySet().stream()
            .filter(e -> e.getValue().getTargetTable().equals(errorTable))
            .findFirst()
            .get()
            .getKey(),
        missingColumnFamilies.stream().findFirst().get());

    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      String tableName = entry.getKey();
      for (String columnFamily : entry.getValue()) {
        verify(bigtable, times(1))
            .modifyFamiliesAsync(
                argThat(
                    mcfr ->
                        createColumnFamilyMockRefersTableAndColumnFamily(
                            tableName, columnFamily, mcfr)));
      }
      verify(bigtable, times(1)).getTableAsync(tableName);
    }
    int expectedBigtableInteractions =
        1 // listTables()
            + tablesAndColumnFamilies.values().stream()
                .mapToInt(Set::size)
                .sum() // modifyColumnFamily()
            + tablesAndColumnFamilies.keySet().size(); // getTable()
    assertTotalNumberOfInvocations(bigtable, expectedBigtableInteractions);
  }

  @Test
  public void testEnsureColumnFamiliesExistMissingTable() {
    String successTable = "table1";
    String errorTable = "table2";
    Map<String, Set<String>> tablesAndColumnFamilies =
        Map.of(
            successTable, Set.of("cf1", "cf2"),
            errorTable, Set.of("cf3", "cf4"));
    doReturn(List.of(successTable)).when(bigtable).listTables();
    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      String table = entry.getKey();
      mockGetTableSuccess(bigtable, table, entry.getValue());
      if (table.equals(errorTable)) {
        doReturn(failedApiFuture(ApiExceptionFactory.create()))
            .when(bigtable)
            .modifyFamiliesAsync(argThat(mcfr -> createColumnFamilyMockRefersTable(table, mcfr)));
      } else {
        mockCreateColumnFamilySuccess(bigtable, table, entry.getValue());
      }
    }
    Map<SinkRecord, MutationData> ensureColumnFamiliesExistInput =
        generateInput(new ArrayList<>(tablesAndColumnFamilies.entrySet()));
    ResourceCreationResult result =
        bigtableSchemaManager.ensureColumnFamiliesExist(ensureColumnFamiliesExistInput);
    assertTrue(result.getDataErrors().isEmpty());
    Set<SinkRecord> missingColumnFamilies = result.getBigtableErrors();
    assertEquals(1, missingColumnFamilies.size());
    assertEquals(
        ensureColumnFamiliesExistInput.entrySet().stream()
            .filter(e -> e.getValue().getTargetTable().equals(errorTable))
            .findFirst()
            .get()
            .getKey(),
        missingColumnFamilies.stream().findFirst().get());

    for (Map.Entry<String, Set<String>> entry : tablesAndColumnFamilies.entrySet()) {
      String table = entry.getKey();
      for (String columnFamily : entry.getValue()) {
        verify(bigtable, times(1))
            .modifyFamiliesAsync(
                argThat(
                    mcfr ->
                        createColumnFamilyMockRefersTableAndColumnFamily(
                            table, columnFamily, mcfr)));
      }
      if (!table.equals(errorTable)) {
        verify(bigtable, times(1)).getTableAsync(table);
      }
    }
    int expectedBigtableInteractions =
        1 // listTables()
            + tablesAndColumnFamilies.values().stream()
                .mapToInt(Set::size)
                .sum() // modifyColumnFamily()
            + 1; // getTable()
    assertTotalNumberOfInvocations(bigtable, expectedBigtableInteractions);
  }

  @Test
  public void testAwaitResourceCreationAndHandleInvalidInputErrors() {
    int uniqueKafkaOffset = 0;
    SinkRecord ok1 = spoofSinkRecord("topic1", uniqueKafkaOffset++);
    SinkRecord ok2 = spoofSinkRecord("topic2", uniqueKafkaOffset++);
    SinkRecord dataError1 = spoofSinkRecord("topic3", uniqueKafkaOffset++);
    SinkRecord dataError2 = spoofSinkRecord("topic4", uniqueKafkaOffset++);
    SinkRecord bigtableError1 = spoofSinkRecord("topic5", uniqueKafkaOffset++);
    SinkRecord bigtableError2 = spoofSinkRecord("topic6", uniqueKafkaOffset++);

    ResourceAndRecords<String> ok = new ResourceAndRecords("ok", List.of(ok1, ok2));
    ResourceAndRecords<String> dataError =
        new ResourceAndRecords("data", List.of(dataError1, dataError2));
    ResourceAndRecords<String> bigtableError =
        new ResourceAndRecords("bigtable", List.of(bigtableError1, bigtableError2));

    Map<ApiFuture<Void>, ResourceAndRecords<String>> input =
        Map.of(
            completedApiFuture(null), ok,
            failedApiFuture(ApiExceptionFactory.create(Status.Code.INVALID_ARGUMENT)), dataError,
            failedApiFuture(ApiExceptionFactory.create(Status.Code.RESOURCE_EXHAUSTED)),
                bigtableError);

    Set<SinkRecord> dataErrors =
        bigtableSchemaManager.awaitResourceCreationAndHandleInvalidInputErrors(input, "%s");
    assertEquals(new HashSet<>(dataError.getRecords()), dataErrors);
    verify(bigtableSchemaManager.logger, times(1))
        .info(eq(bigtableError.getResource()), any(Throwable.class));
  }

  private static Map<SinkRecord, MutationData> generateInput(
      List<Map.Entry<String, Set<String>>> records) {
    int uniqueKafkaOffset = 1;
    Map<SinkRecord, MutationData> result = new HashMap<>();
    for (Map.Entry<String, Set<String>> record : records) {
      SinkRecord sinkRecord = spoofSinkRecord("topic" + record.getKey(), uniqueKafkaOffset++);
      MutationData mutationData = spoofSinkRecordOutput(record.getKey(), record.getValue());
      result.put(sinkRecord, mutationData);
    }
    return result;
  }

  private static SinkRecord spoofSinkRecord(String topic, int uniqueKafkaOffset) {
    return new SinkRecord(topic, 1, null, new Object(), null, new Object(), uniqueKafkaOffset);
  }

  private static MutationData spoofSinkRecordOutput(
      String targetTable, Set<String> columnFamilies) {
    return new MutationData(targetTable, null, null, columnFamilies);
  }

  private boolean createTableMockRefersTable(String tableName, CreateTableRequest ctr) {
    return tableName.equals(ctr.toProto("unused", "unused").getTableId());
  }

  private boolean createColumnFamilyMockRefersTable(
      String tableName, ModifyColumnFamiliesRequest mcfr) {
    // getName() returns whole table id comprising project ID, instance ID, table name, ...
    return mcfr.toProto("unused", "unused").getName().endsWith("/" + tableName);
  }

  private boolean createColumnFamilyMockRefersTableAndColumnFamily(
      String tableName, String columnFamily, ModifyColumnFamiliesRequest mcfr) {
    boolean refersTable = createColumnFamilyMockRefersTable(tableName, mcfr);
    List<com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest.Modification> modifications =
        mcfr.toProto("unused", "unused").getModificationsList();
    return refersTable
        && modifications.stream()
            .filter(
                com.google.bigtable.admin.v2.ModifyColumnFamiliesRequest.Modification::hasCreate)
            .anyMatch(m -> columnFamily.equals(m.getId()));
  }

  private void mockCreateTableSuccess(
      BigtableTableAdminClient bigtable, String tableName, Set<String> tableColumnFamilies) {
    Table table = mockTable(tableName, tableColumnFamilies);
    doAnswer(ignored -> completedApiFuture(table))
        .when(bigtable)
        .createTableAsync(argThat(ctr -> createTableMockRefersTable(tableName, ctr)));
  }

  private void mockCreateColumnFamilySuccess(
      BigtableTableAdminClient bigtable, String tableName, Set<String> tableColumnFamilies) {
    Table table = mockTable(tableName, tableColumnFamilies);
    doAnswer(ignored -> completedApiFuture(table))
        .when(bigtable)
        .modifyFamiliesAsync(argThat(mcfr -> createColumnFamilyMockRefersTable(tableName, mcfr)));
  }

  private void mockGetTableSuccess(
      BigtableTableAdminClient bigtable, String tableName, Set<String> tableColumnFamilies) {
    Table table = mockTable(tableName, tableColumnFamilies);
    doAnswer(ignored -> completedApiFuture(table)).when(bigtable).getTableAsync(tableName);
  }

  private Table mockTable(String tableName, Set<String> tableColumnFamilies) {
    List<ColumnFamily> columnFamilies = new ArrayList<>();
    for (String tableColumnFamily : tableColumnFamilies) {
      ColumnFamily columnFamily = mock(ColumnFamily.class);
      doReturn(tableColumnFamily).when(columnFamily).getId();
      columnFamilies.add(columnFamily);
    }
    Table table = mock(Table.class);
    doReturn(tableName).when(table).getId();
    doReturn(columnFamilies).when(table).getColumnFamilies();
    return table;
  }

  private static class TestBigtableSchemaManager extends BigtableSchemaManager {
    public TestBigtableSchemaManager(BigtableTableAdminClient bigtable) {
      super(bigtable);
      this.logger = spy(this.logger);
    }

    public Map<String, Optional<Set<String>>> getCache() {
      return tableNameToColumnFamilies;
    }
  }
}
