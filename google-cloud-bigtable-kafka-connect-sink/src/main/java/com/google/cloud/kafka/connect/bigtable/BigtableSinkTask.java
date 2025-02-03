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

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.batching.Batcher;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.ConditionalRowMutation;
import com.google.cloud.bigtable.data.v2.models.Filters;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.cloud.kafka.connect.bigtable.autocreate.BigtableSchemaManager;
import com.google.cloud.kafka.connect.bigtable.autocreate.ResourceCreationResult;
import com.google.cloud.kafka.connect.bigtable.config.BigtableErrorMode;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkConfig;
import com.google.cloud.kafka.connect.bigtable.config.BigtableSinkTaskConfig;
import com.google.cloud.kafka.connect.bigtable.config.ConfigInterpolation;
import com.google.cloud.kafka.connect.bigtable.exception.BatchException;
import com.google.cloud.kafka.connect.bigtable.exception.BigtableSinkLogicError;
import com.google.cloud.kafka.connect.bigtable.exception.InvalidBigtableSchemaModificationException;
import com.google.cloud.kafka.connect.bigtable.mapping.KeyMapper;
import com.google.cloud.kafka.connect.bigtable.mapping.MutationData;
import com.google.cloud.kafka.connect.bigtable.mapping.MutationDataBuilder;
import com.google.cloud.kafka.connect.bigtable.mapping.ValueMapper;
import com.google.cloud.kafka.connect.bigtable.version.PackageMetadata;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.apache.kafka.connect.data.SchemaAndValue;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.sink.ErrantRecordReporter;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.apache.kafka.connect.sink.SinkTaskContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link SinkTask} class used by {@link org.apache.kafka.connect.sink.SinkConnector} to write to
 * Cloud Bigtable.
 */
public class BigtableSinkTask extends SinkTask {
  private BigtableSinkTaskConfig config;
  private BigtableDataClient bigtableData;
  private BigtableTableAdminClient bigtableAdmin;
  private KeyMapper keyMapper;
  private ValueMapper valueMapper;
  private BigtableSchemaManager schemaManager;
  @VisibleForTesting protected final Map<String, Batcher<RowMutationEntry, Void>> batchers;
  @VisibleForTesting protected Logger logger = LoggerFactory.getLogger(BigtableSinkTask.class);

  /**
   * A default empty constructor. Initialization methods such as {@link BigtableSinkTask#start(Map)}
   * or {@link SinkTask#initialize(SinkTaskContext)} must be called before {@link
   * BigtableSinkTask#put(Collection)} can be called. Kafka Connect handles it well.
   */
  public BigtableSinkTask() {
    this(null, null, null, null, null, null, null);
  }

  // A constructor only used by the tests.
  @VisibleForTesting
  protected BigtableSinkTask(
      BigtableSinkTaskConfig config,
      BigtableDataClient bigtableData,
      BigtableTableAdminClient bigtableAdmin,
      KeyMapper keyMapper,
      ValueMapper valueMapper,
      BigtableSchemaManager schemaManager,
      SinkTaskContext context) {
    this.config = config;
    this.bigtableData = bigtableData;
    this.bigtableAdmin = bigtableAdmin;
    this.keyMapper = keyMapper;
    this.valueMapper = valueMapper;
    this.schemaManager = schemaManager;
    this.context = context;
    this.batchers = new HashMap<>();
  }

  @Override
  public void start(Map<String, String> props) {
    config = new BigtableSinkTaskConfig(props);
    logger =
        LoggerFactory.getLogger(
            BigtableSinkTask.class.getName()
                + config.getInt(BigtableSinkTaskConfig.CONFIG_TASK_ID));
    bigtableData = config.getBigtableDataClient();
    bigtableAdmin = config.getBigtableAdminClient();
    keyMapper =
        new KeyMapper(
            config.getString(BigtableSinkTaskConfig.CONFIG_ROW_KEY_DELIMITER),
            config.getList(BigtableSinkTaskConfig.CONFIG_ROW_KEY_DEFINITION));
    valueMapper =
        new ValueMapper(
            config.getString(BigtableSinkTaskConfig.CONFIG_DEFAULT_COLUMN_FAMILY),
            config.getString(BigtableSinkTaskConfig.CONFIG_DEFAULT_COLUMN_QUALIFIER),
            config.getNullValueMode());
    schemaManager = new BigtableSchemaManager(bigtableAdmin);
  }

  @Override
  public void stop() {
    logger.trace("stop()");
    try {
      Iterable<ApiFuture<Void>> batcherCloses =
          batchers.values().stream().map(Batcher::closeAsync).collect(Collectors.toList());
      ApiFutures.allAsList(batcherCloses).get();
    } catch (ExecutionException | InterruptedException e) {
      logger.warn("Error closing Cloud Bigtable batchers.", e);
    } finally {
      batchers.clear();
    }
    if (bigtableAdmin != null) {
      try {
        bigtableAdmin.close();
      } catch (RuntimeException e) {
        logger.warn("Error closing Cloud Bigtable admin client.", e);
      }
    }
    if (bigtableData != null) {
      try {
        bigtableData.close();
      } catch (RuntimeException e) {
        logger.warn("Error closing Cloud Bigtable data client.", e);
      }
    }
  }

  @Override
  public String version() {
    logger.trace("version()");
    return PackageMetadata.getVersion();
  }

  @Override
  public void put(Collection<SinkRecord> records) {
    logger.trace("put(#records={})", records.size());
    if (records.isEmpty()) {
      return;
    }

    Map<SinkRecord, MutationData> mutations = prepareRecords(records);
    if (config.getBoolean(BigtableSinkTaskConfig.CONFIG_AUTO_CREATE_TABLES)) {
      mutations = autoCreateTablesAndHandleErrors(mutations);
    }
    if (config.getBoolean(BigtableSinkTaskConfig.CONFIG_AUTO_CREATE_COLUMN_FAMILIES)) {
      mutations = autoCreateColumnFamiliesAndHandleErrors(mutations);
    }
    // Needed so that the batch ordering is more predictable from the user's point of view.
    mutations = orderMap(mutations, records);

    Map<SinkRecord, Future<Void>> perRecordResults = new HashMap<>();
    switch (config.getInsertMode()) {
      case INSERT:
        insertRows(mutations, perRecordResults);
        break;
      case UPSERT:
        upsertRows(mutations, perRecordResults);
        break;
    }
    handleResults(perRecordResults);
  }

  /**
   * Generate mutations for input records.
   *
   * @param records Input records.
   * @return {@link Map} containing input records and corresponding mutations that need to be
   *     applied.
   */
  @VisibleForTesting
  Map<SinkRecord, MutationData> prepareRecords(Collection<SinkRecord> records) {
    Map<SinkRecord, MutationData> mutations = new HashMap<>();
    for (SinkRecord record : records) {
      try {
        Optional<MutationData> maybeRecordMutationData = createRecordMutationData(record);
        if (maybeRecordMutationData.isPresent()) {
          mutations.put(record, maybeRecordMutationData.get());
        } else {
          logger.debug("Skipped a record that maps to an empty value.");
        }
      } catch (Throwable t) {
        reportError(record, t);
      }
    }
    return mutations;
  }

  /**
   * Generate mutation for a single input record.
   *
   * @param record Input record.
   * @return {@link Optional#empty()} if the input record requires no write to Cloud Bigtable,
   *     {@link Optional} containing mutation that it needs to be written to Cloud Bigtable
   *     otherwise.
   */
  @VisibleForTesting
  Optional<MutationData> createRecordMutationData(SinkRecord record) {
    String recordTableId = getTableName(record);
    SchemaAndValue kafkaKey = new SchemaAndValue(record.keySchema(), record.key());
    ByteString rowKey = ByteString.copyFrom(keyMapper.getKey(kafkaKey));
    if (rowKey.isEmpty()) {
      throw new DataException(
          "The record's key converts into an illegal empty Cloud Bigtable row key.");
    }
    SchemaAndValue kafkaValue = new SchemaAndValue(record.valueSchema(), record.value());
    long timestamp = getTimestampMicros(record);
    MutationDataBuilder mutationDataBuilder =
        valueMapper.getRecordMutationDataBuilder(kafkaValue, record.topic(), timestamp);

    return mutationDataBuilder.maybeBuild(recordTableId, rowKey);
  }

  /**
   * Get table name the input record's mutation will be written to.
   *
   * @param record Input record.
   * @return Cloud Bigtable table name the input record's mutation will be written to.
   */
  @VisibleForTesting
  String getTableName(SinkRecord record) {
    String template = config.getString(BigtableSinkTaskConfig.CONFIG_TABLE_NAME_FORMAT);
    return ConfigInterpolation.replace(template, record.topic());
  }

  /**
   * Get timestamp the input record's mutation's timestamp.
   *
   * @param record Input record.
   * @return UNIX timestamp in microseconds.
   */
  @VisibleForTesting
  long getTimestampMicros(SinkRecord record) {
    // From reading the Java Cloud Bigtable client, it looks that the only usable timestamp
    // granularity is the millisecond one. So we assume it.
    // There's a test that will break when it starts supporting microsecond granularity with a note
    // to modify this function then.
    Long timestampMillis = record.timestamp();
    if (timestampMillis == null) {
      // The timestamp might be null if the kafka cluster is old (<= v0.9):
      // https://github.com/apache/kafka/blob/f9615ed275c3856b73e5b6083049a8def9f59697/clients/src/main/java/org/apache/kafka/clients/consumer/ConsumerRecord.java#L49
      // In such a case, we default to wall clock time as per the design doc.
      logger.debug("Used wall clock for a record missing timestamp.");
      timestampMillis = System.currentTimeMillis();
    }
    return 1000 * timestampMillis;
  }

  /**
   * Report error as described in {@link BigtableSinkConfig#getDefinition()}.
   *
   * @param record Input record whose processing caused an error.
   * @param throwable The error.
   */
  @VisibleForTesting
  void reportError(SinkRecord record, Throwable throwable) {
    ErrantRecordReporter reporter;
    /// We get a reference to `reporter` using a procedure described in javadoc of
    /// {@link SinkTaskContext#errantRecordReporter()} that guards against old Kafka versions.
    try {
      reporter = context.errantRecordReporter();
    } catch (NoSuchMethodError | NoClassDefFoundError ignored) {
      reporter = null;
    }
    if (reporter != null) {
      reporter.report(record, throwable);
      logger.warn(
          "Used DLQ for reporting a problem with a record (throwableClass={}).",
          throwable.getClass().getName());
    } else {
      BigtableErrorMode errorMode = config.getBigtableErrorMode();
      switch (errorMode) {
        case IGNORE:
          break;
        case WARN:
          logger.warn("Processing of a record with key {} failed", record.key(), throwable);
          break;
        case FAIL:
          throw new BatchException(throwable);
      }
    }
  }

  /**
   * Generates a {@link Map} with desired key ordering.
   *
   * @param map A {@link Map} to be sorted.
   * @param order A {@link Collection} defining desired order of the output {@link Map}. Must be a
   *     superset of {@code mutations}'s key set.
   * @return A {@link Map} with the same keys and corresponding values as {@code map} with the same
   *     key ordering as {@code order}.
   */
  @VisibleForTesting
  // It is generic so that we can test it with naturally ordered values easily.
  static <K, V> Map<K, V> orderMap(Map<K, V> map, Collection<K> order) {
    if (!order.containsAll(map.keySet())) {
      throw new BigtableSinkLogicError(
          "A collection defining order of keys must be a superset of the input map's key set.");
    }
    Map<K, V> sorted = new LinkedHashMap<>();
    for (K key : order) {
      V value = map.get(key);
      if (value != null) {
        sorted.put(key, value);
      }
    }
    return sorted;
  }

  /**
   * Attempts to create Cloud Bigtable tables so that all the mutations can be applied and handles
   * errors.
   *
   * @param mutations Input records and corresponding mutations.
   * @return Subset of the input argument containing only those record for which the target Cloud
   *     Bigtable tables exist.
   */
  @VisibleForTesting
  Map<SinkRecord, MutationData> autoCreateTablesAndHandleErrors(
      Map<SinkRecord, MutationData> mutations) {
    Map<SinkRecord, MutationData> okMutations = new HashMap<>(mutations);
    ResourceCreationResult resourceCreationResult = schemaManager.ensureTablesExist(okMutations);
    String errorMessage = "Table auto-creation failed.";
    for (SinkRecord record : resourceCreationResult.getBigtableErrors()) {
      reportError(record, new ConnectException(errorMessage));
      okMutations.remove(record);
    }
    for (SinkRecord record : resourceCreationResult.getDataErrors()) {
      reportError(record, new InvalidBigtableSchemaModificationException(errorMessage));
      okMutations.remove(record);
    }
    return okMutations;
  }

  /**
   * Attempts to create Cloud Bigtable column families so that all the mutations can be applied and
   * handles errors.
   *
   * @param mutations Input records and corresponding mutations.
   * @return Subset of the input argument containing only those record for which the target Cloud
   *     Bigtable column families exist.
   */
  @VisibleForTesting
  Map<SinkRecord, MutationData> autoCreateColumnFamiliesAndHandleErrors(
      Map<SinkRecord, MutationData> mutations) {
    Map<SinkRecord, MutationData> okMutations = new HashMap<>(mutations);
    ResourceCreationResult resourceCreationResult =
        schemaManager.ensureColumnFamiliesExist(okMutations);
    String errorMessage = "Column family auto-creation failed.";
    for (SinkRecord record : resourceCreationResult.getBigtableErrors()) {
      reportError(record, new ConnectException(errorMessage));
      okMutations.remove(record);
    }
    for (SinkRecord record : resourceCreationResult.getDataErrors()) {
      reportError(record, new InvalidBigtableSchemaModificationException(errorMessage));
      okMutations.remove(record);
    }
    return okMutations;
  }

  /**
   * Applies the mutations using upserts.
   *
   * @param mutations Mutations to be applied.
   * @param perRecordResults {@link Map} the per-record results will be written to.
   */
  @VisibleForTesting
  void upsertRows(
      Map<SinkRecord, MutationData> mutations, Map<SinkRecord, Future<Void>> perRecordResults) {
    List<Map.Entry<SinkRecord, MutationData>> mutationsToApply =
        new ArrayList<>(mutations.entrySet());
    int maxBatchSize = config.getInt(BigtableSinkTaskConfig.CONFIG_MAX_BATCH_SIZE);
    List<List<Map.Entry<SinkRecord, MutationData>>> batches =
        Lists.partition(mutationsToApply, maxBatchSize);

    try {
      for (List<Map.Entry<SinkRecord, MutationData>> batch : batches) {
        performUpsertBatch(batch, perRecordResults);
      }
    } finally {
      for (Batcher<RowMutationEntry, Void> b : batchers.values()) {
        // We flush the batchers to ensure that no unsent requests remain in the batchers
        // after this method returns to make the behavior more predictable.
        // We flush asynchronously and await the results instead.
        b.sendOutstanding();
      }
    }
  }

  /**
   * Applies a single mutation batch using upserts.
   *
   * @param batch Batch of mutations to be applied.
   * @param perRecordResults A {@link Map} the per-record results will be written to.
   */
  @VisibleForTesting
  void performUpsertBatch(
      List<Map.Entry<SinkRecord, MutationData>> batch,
      Map<SinkRecord, Future<Void>> perRecordResults) {
    logger.trace("upsertBatch(#records={})", batch.size());
    for (Map.Entry<SinkRecord, MutationData> recordEntry : batch) {
      SinkRecord record = recordEntry.getKey();
      MutationData recordMutationData = recordEntry.getValue();
      String recordTableName = recordMutationData.getTargetTable();

      Batcher<RowMutationEntry, Void> batcher =
          batchers.computeIfAbsent(recordTableName, (k) -> bigtableData.newBulkMutationBatcher(k));
      perRecordResults.put(record, batcher.add(recordMutationData.getUpsertMutation()));
    }
    for (Batcher<RowMutationEntry, Void> batcher : batchers.values()) {
      // We must flush the batchers to respect CONFIG_MAX_BATCH_SIZE.
      // We flush asynchronously and await the results instead.
      batcher.sendOutstanding();
    }
  }

  /**
   * Applies the mutations using inserts.
   *
   * <p>Note that no batching is used.
   *
   * @param mutations Mutations to be applied.
   * @param perRecordResults {@link Map} the per-record results will be written to.
   */
  @VisibleForTesting
  void insertRows(
      Map<SinkRecord, MutationData> mutations, Map<SinkRecord, Future<Void>> perRecordResults) {
    logger.trace("insertRows(#records={})", mutations.size());
    for (Map.Entry<SinkRecord, MutationData> recordEntry : mutations.entrySet()) {
      // We keep compatibility with Confluent's sink and disallow batching operations that check if
      // the row already exists.
      SinkRecord record = recordEntry.getKey();
      MutationData recordMutationData = recordEntry.getValue();
      ConditionalRowMutation insert =
          // We want to perform the mutation if and only if the row does not already exist.
          ConditionalRowMutation.create(
                  recordMutationData.getTargetTable(), recordMutationData.getRowKey())
              // We first check if any cell of this row exists...
              .condition(Filters.FILTERS.pass())
              // ... and perform the mutation only if no cell exists.
              .otherwise(recordMutationData.getInsertMutation());
      boolean insertSuccessful;
      Optional<Throwable> exceptionThrown = Optional.empty();
      try {
        insertSuccessful = !bigtableData.checkAndMutateRow(insert);
      } catch (ApiException e) {
        insertSuccessful = false;
        exceptionThrown = Optional.of(e);
      }
      perRecordResults.put(
          record,
          insertSuccessful
              ? CompletableFuture.completedFuture(null)
              : CompletableFuture.failedFuture(
                  exceptionThrown.orElse(
                      new ConnectException("Insert failed since the row already existed."))));
    }
  }

  /**
   * Handles results of the whole operation.
   *
   * @param perRecordResults Results to be handled.
   */
  @VisibleForTesting
  void handleResults(Map<SinkRecord, Future<Void>> perRecordResults) {
    logger.trace("handleResults(#records={})", perRecordResults.size());
    for (Map.Entry<SinkRecord, Future<Void>> recordResult : perRecordResults.entrySet()) {
      try {
        recordResult.getValue().get();
      } catch (ExecutionException | InterruptedException e) {
        SinkRecord record = recordResult.getKey();
        reportError(record, e);
      }
    }
  }
}
