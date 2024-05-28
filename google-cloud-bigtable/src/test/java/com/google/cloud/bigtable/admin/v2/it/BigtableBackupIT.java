/*
 * Copyright 2020 Google LLC
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
package com.google.cloud.bigtable.admin.v2.it;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static com.google.common.truth.TruthJUnit.assume;
import static org.junit.Assert.assertThrows;

import com.google.api.gax.batching.Batcher;
import com.google.api.gax.rpc.DeadlineExceededException;
import com.google.api.gax.rpc.NotFoundException;
import com.google.cloud.Policy;
import com.google.cloud.bigtable.admin.v2.BigtableInstanceAdminClient;
import com.google.cloud.bigtable.admin.v2.BigtableTableAdminClient;
import com.google.cloud.bigtable.admin.v2.models.Backup;
import com.google.cloud.bigtable.admin.v2.models.CopyBackupRequest;
import com.google.cloud.bigtable.admin.v2.models.CreateBackupRequest;
import com.google.cloud.bigtable.admin.v2.models.CreateInstanceRequest;
import com.google.cloud.bigtable.admin.v2.models.CreateTableRequest;
import com.google.cloud.bigtable.admin.v2.models.Instance.Type;
import com.google.cloud.bigtable.admin.v2.models.RestoreTableRequest;
import com.google.cloud.bigtable.admin.v2.models.RestoredTableResult;
import com.google.cloud.bigtable.admin.v2.models.StorageType;
import com.google.cloud.bigtable.admin.v2.models.Table;
import com.google.cloud.bigtable.admin.v2.models.UpdateBackupRequest;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.models.RowMutationEntry;
import com.google.cloud.bigtable.test_helpers.env.EmulatorEnv;
import com.google.cloud.bigtable.test_helpers.env.PrefixGenerator;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.common.base.Stopwatch;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.threeten.bp.Duration;
import org.threeten.bp.Instant;

@RunWith(JUnit4.class)
public class BigtableBackupIT {
  @ClassRule public static final TestEnvRule testEnvRule = new TestEnvRule();
  @Rule public final PrefixGenerator prefixGenerator = new PrefixGenerator();

  private static final Logger LOGGER = Logger.getLogger(BigtableBackupIT.class.getName());

  private static final int[] BACKOFF_DURATION = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};

  private static BigtableTableAdminClient tableAdmin;
  private static BigtableInstanceAdminClient instanceAdmin;
  private static BigtableDataClient dataClient;

  private static String targetCluster;
  private static Table testTable;

  @BeforeClass
  public static void setUpClass() throws InterruptedException {
    assume()
        .withMessage("BigtableInstanceAdminClient is not supported on Emulator")
        .that(testEnvRule.env())
        .isNotInstanceOf(EmulatorEnv.class);

    tableAdmin = testEnvRule.env().getTableAdminClient();
    instanceAdmin = testEnvRule.env().getInstanceAdminClient();
    dataClient = testEnvRule.env().getDataClient();

    targetCluster = testEnvRule.env().getPrimaryClusterId();
    testTable = createAndPopulateTestTable(tableAdmin, dataClient);
  }

  @AfterClass
  public static void tearDownClass() {
    if (testTable != null) {
      try {
        tableAdmin.deleteTable(testTable.getId());
      } catch (Exception e) {
        // Ignore.
      }
    }
  }

  private static void deleteBackupIgnoreErrors(
      BigtableTableAdminClient tableAdmin, String clusterId, String backupId) {
    try {
      tableAdmin.deleteBackup(clusterId, backupId);
    } catch (DeadlineExceededException ex) {
      LOGGER.log(Level.WARNING, "Error deleting backup", ex);
      // Don't rethrow
    }
  }

  private void deleteBackupIgnoreErrors(String clusterId, String backupId) {
    deleteBackupIgnoreErrors(tableAdmin, clusterId, backupId);
  }

  @Test
  public void createAndGetBackupTest() {
    String backupId = prefixGenerator.newPrefix();
    Instant expireTime = Instant.now().plus(Duration.ofHours(6));

    CreateBackupRequest request =
        CreateBackupRequest.of(targetCluster, backupId)
            .setSourceTableId(testTable.getId())
            .setExpireTime(expireTime);
    try {
      Backup response = tableAdmin.createBackup(request);
      assertWithMessage("Got wrong backup Id in CreateBackup")
          .that(response.getId())
          .isEqualTo(backupId);
      assertWithMessage("Got wrong source table name in CreateBackup")
          .that(response.getSourceTableId())
          .isEqualTo(testTable.getId());
      assertWithMessage("Got wrong expire time in CreateBackup")
          .that(response.getExpireTime())
          .isEqualTo(expireTime);

      Backup result = tableAdmin.getBackup(targetCluster, backupId);
      assertWithMessage("Got wrong backup Id in GetBackup API")
          .that(result.getId())
          .isEqualTo(backupId);
      assertWithMessage("Got wrong source table name in GetBackup API")
          .that(result.getSourceTableId())
          .isEqualTo(testTable.getId());
      assertWithMessage("Got wrong expire time in GetBackup API")
          .that(result.getExpireTime())
          .isEqualTo(expireTime);
      assertWithMessage("Got empty start time in GetBackup API")
          .that(result.getStartTime())
          .isNotNull();
      assertWithMessage("Got wrong size bytes in GetBackup API")
          .that(result.getSizeBytes())
          .isEqualTo(0L);
      assertWithMessage("Got wrong state in GetBackup API")
          .that(result.getState())
          .isAnyOf(Backup.State.CREATING, Backup.State.READY);

    } finally {
      deleteBackupIgnoreErrors(targetCluster, backupId);
    }
  }

  @Test
  public void listBackupTest() {
    String backupId1 = prefixGenerator.newPrefix();
    String backupId2 = prefixGenerator.newPrefix();

    try {
      tableAdmin.createBackup(createBackupRequest(backupId1));
      tableAdmin.createBackup(createBackupRequest(backupId2));

      List<String> response = tableAdmin.listBackups(targetCluster);
      // Concurrent tests running may cause flakiness. Use containsAtLeast instead of
      // containsExactly.
      assertWithMessage("Incorrect backup name")
          .that(response)
          .containsAtLeast(backupId1, backupId2);
    } finally {
      deleteBackupIgnoreErrors(targetCluster, backupId1);
      deleteBackupIgnoreErrors(targetCluster, backupId2);
    }
  }

  @Test
  public void updateBackupTest() {
    String backupId = prefixGenerator.newPrefix();
    tableAdmin.createBackup(createBackupRequest(backupId));

    Instant expireTime = Instant.now().plus(Duration.ofDays(20));
    UpdateBackupRequest req =
        UpdateBackupRequest.of(targetCluster, backupId).setExpireTime(expireTime);
    try {
      Backup backup = tableAdmin.updateBackup(req);
      assertWithMessage("Incorrect expire time").that(backup.getExpireTime()).isEqualTo(expireTime);
    } finally {
      deleteBackupIgnoreErrors(targetCluster, backupId);
    }
  }

  @Test
  public void deleteBackupTest() {
    String backupId = prefixGenerator.newPrefix();

    tableAdmin.createBackup(createBackupRequest(backupId));
    tableAdmin.deleteBackup(targetCluster, backupId);

    assertThrows(NotFoundException.class, () -> tableAdmin.getBackup(targetCluster, backupId));
  }

  @Test
  public void restoreTableTest() throws InterruptedException, ExecutionException {
    String backupId = prefixGenerator.newPrefix();
    String restoredTableId = prefixGenerator.newPrefix();
    tableAdmin.createBackup(createBackupRequest(backupId));

    // Wait 2 minutes so that the RestoreTable API will trigger an optimize restored
    // table operation.
    Thread.sleep(120 * 1000);

    try {
      RestoreTableRequest req =
          RestoreTableRequest.of(targetCluster, backupId).setTableId(restoredTableId);
      RestoredTableResult result = tableAdmin.restoreTable(req);
      assertWithMessage("Incorrect restored table id")
          .that(result.getTable().getId())
          .isEqualTo(restoredTableId);

      if (result.getOptimizeRestoredTableOperationToken() != null) {
        // The assertion might be missing if the test is running against a HDD cluster or an
        // optimization is not necessary.
        tableAdmin.awaitOptimizeRestoredTable(result.getOptimizeRestoredTableOperationToken());
        Table restoredTable = tableAdmin.getTable(restoredTableId);
        assertWithMessage("Incorrect restored table id")
            .that(restoredTable.getId())
            .isEqualTo(restoredTableId);
      }
    } finally {
      deleteBackupIgnoreErrors(targetCluster, backupId);
      tableAdmin.deleteTable(restoredTableId);
    }
  }

  @Test
  public void crossInstanceRestoreTest()
      throws InterruptedException, IOException, ExecutionException, TimeoutException {
    String backupId = prefixGenerator.newPrefix();
    String restoredTableId = prefixGenerator.newPrefix();

    // Create the backup
    tableAdmin.createBackup(
        CreateBackupRequest.of(targetCluster, backupId)
            .setSourceTableId(testTable.getId())
            .setExpireTime(Instant.now().plus(Duration.ofHours(6))));

    Stopwatch stopwatch = Stopwatch.createStarted();

    // Set up a new instance to test cross-instance restore. The backup will be restored here
    String targetInstance = prefixGenerator.newPrefix();
    instanceAdmin.createInstance(
        CreateInstanceRequest.of(targetInstance)
            .addCluster(targetInstance, testEnvRule.env().getSecondaryZone(), 1, StorageType.SSD)
            .setDisplayName("backups-dest-test-instance")
            .addLabel("state", "readytodelete")
            .setType(Type.PRODUCTION));

    try (BigtableTableAdminClient destTableAdmin =
        testEnvRule.env().getTableAdminClientForInstance(targetInstance)) {

      // Wait 2 minutes so that the RestoreTable API will trigger an optimize restored
      // table operation.
      Thread.sleep(
          Duration.ofMinutes(2)
              .minus(Duration.ofMillis(stopwatch.elapsed(TimeUnit.MILLISECONDS)))
              .toMillis());

      try {
        RestoreTableRequest req =
            RestoreTableRequest.of(testEnvRule.env().getInstanceId(), targetCluster, backupId)
                .setTableId(restoredTableId);
        RestoredTableResult result = destTableAdmin.restoreTable(req);
        assertWithMessage("Incorrect restored table id")
            .that(result.getTable().getId())
            .isEqualTo(restoredTableId);
        assertWithMessage("Incorrect instance id")
            .that(result.getTable().getInstanceId())
            .isEqualTo(targetInstance);

        // The assertion might be missing if the test is running against a HDD cluster or an
        // optimization is not necessary.
        assertWithMessage("Empty OptimizeRestoredTable token")
            .that(result.getOptimizeRestoredTableOperationToken())
            .isNotNull();
        destTableAdmin.awaitOptimizeRestoredTable(result.getOptimizeRestoredTableOperationToken());
        destTableAdmin.getTable(restoredTableId);
      } finally {
        deleteBackupIgnoreErrors(targetCluster, backupId);
        instanceAdmin.deleteInstance(targetInstance);
      }
    }
  }

  @Test
  public void copyBackupTest()
      throws InterruptedException, IOException, ExecutionException, TimeoutException {
    String backupId = prefixGenerator.newPrefix();
    String copiedBackupId = prefixGenerator.newPrefix();
    Instant expireTime = Instant.now().plus(Duration.ofHours(36));

    // Create the backup
    tableAdmin.createBackup(
        CreateBackupRequest.of(targetCluster, backupId)
            .setSourceTableId(testTable.getId())
            .setExpireTime(expireTime));

    try {
      CopyBackupRequest req =
          CopyBackupRequest.of(targetCluster, backupId)
              .setDestination(targetCluster, copiedBackupId)
              .setExpireTime(expireTime);
      Backup result = tableAdmin.copyBackup(req);
      assertWithMessage("Got wrong copied backup id in CopyBackup API")
          .that(result.getId())
          .isEqualTo(copiedBackupId);
      assertWithMessage("Got wrong source backup id in CopyBackup API")
          .that(result.getSourceBackupId())
          .isEqualTo(backupId);
      assertWithMessage("Got wrong expire time in CopyBackup API")
          .that(result.getExpireTime())
          .isEqualTo(expireTime);
      assertWithMessage("Got empty start time in CopyBackup API")
          .that(result.getStartTime())
          .isNotNull();
      assertWithMessage("Got wrong state in CopyBackup API")
          .that(result.getState())
          .isAnyOf(Backup.State.CREATING, Backup.State.READY);

    } finally {
      deleteBackupIgnoreErrors(targetCluster, copiedBackupId);
      deleteBackupIgnoreErrors(targetCluster, backupId);
    }
  }

  @Test
  public void crossInstanceCopyBackupTest()
      throws InterruptedException, IOException, ExecutionException, TimeoutException {
    String backupId = prefixGenerator.newPrefix();
    String copiedBackupId = prefixGenerator.newPrefix();
    Instant expireTime = Instant.now().plus(Duration.ofHours(36));

    // Create the backup
    tableAdmin.createBackup(
        CreateBackupRequest.of(targetCluster, backupId)
            .setSourceTableId(testTable.getId())
            .setExpireTime(expireTime));

    // Set up a new instance to test cross-instance copy. The backup will be copied here
    String destInstance = prefixGenerator.newPrefix();
    String destCluster = prefixGenerator.newPrefix();
    instanceAdmin.createInstance(
        CreateInstanceRequest.of(destInstance)
            .addCluster(destCluster, testEnvRule.env().getSecondaryZone(), 1, StorageType.SSD)
            .setDisplayName("backups-dest-test-instance")
            .addLabel("state", "readytodelete")
            .setType(Type.PRODUCTION));

    try (BigtableTableAdminClient destTableAdmin =
        testEnvRule.env().getTableAdminClientForInstance(destInstance)) {

      try {
        CopyBackupRequest req =
            CopyBackupRequest.of(targetCluster, backupId)
                .setSourceInstance(testEnvRule.env().getInstanceId())
                .setDestination(destCluster, copiedBackupId)
                .setExpireTime(expireTime);
        Backup result = destTableAdmin.copyBackup(req);
        assertWithMessage("Got wrong copied backup id in CopyBackup API")
            .that(result.getId())
            .isEqualTo(copiedBackupId);
        assertWithMessage("Got wrong source backup id in CopyBackup API")
            .that(result.getSourceBackupId())
            .isEqualTo(backupId);
        assertWithMessage("Got wrong expire time in CopyBackup API")
            .that(result.getExpireTime())
            .isEqualTo(expireTime);
        assertWithMessage("Got empty start time in CopyBackup API")
            .that(result.getStartTime())
            .isNotNull();
        assertWithMessage("Got wrong state in CopyBackup API")
            .that(result.getState())
            .isAnyOf(Backup.State.CREATING, Backup.State.READY);

      } finally {
        deleteBackupIgnoreErrors(destTableAdmin, destCluster, copiedBackupId);
        deleteBackupIgnoreErrors(targetCluster, backupId);
        instanceAdmin.deleteInstance(destInstance);
      }
    }
  }

  @Test
  public void backupIamTest() {
    String backupId = prefixGenerator.newPrefix();

    try {
      tableAdmin.createBackup(createBackupRequest(backupId));

      Policy policy = tableAdmin.getBackupIamPolicy(targetCluster, backupId);
      assertThat(policy).isNotNull();

      Exception actualEx = null;
      try {
        assertThat(tableAdmin.setBackupIamPolicy(targetCluster, backupId, policy)).isNotNull();
      } catch (Exception iamException) {
        actualEx = iamException;
      }
      assertThat(actualEx).isNull();

      List<String> permissions =
          tableAdmin.testBackupIamPermission(
              targetCluster,
              backupId,
              "bigtable.backups.get",
              "bigtable.backups.delete",
              "bigtable.backups.update",
              "bigtable.backups.restore");
      assertThat(permissions).hasSize(4);
    } finally {
      deleteBackupIgnoreErrors(targetCluster, backupId);
    }
  }

  private CreateBackupRequest createBackupRequest(String backupId) {
    return CreateBackupRequest.of(targetCluster, backupId)
        .setSourceTableId(testTable.getId())
        .setExpireTime(Instant.now().plus(Duration.ofDays(15)));
  }

  private static Table createAndPopulateTestTable(
      BigtableTableAdminClient tableAdmin, BigtableDataClient dataClient)
      throws InterruptedException {
    String tableId = PrefixGenerator.newPrefix("BigtableBackupIT#createAndPopulateTestTable");
    Table testTable = tableAdmin.createTable(CreateTableRequest.of(tableId).addFamily("cf1"));

    // Populate test data.
    byte[] rowBytes = new byte[1024];
    Random random = new Random();
    random.nextBytes(rowBytes);

    try (Batcher<RowMutationEntry, Void> batcher = dataClient.newBulkMutationBatcher(tableId)) {
      for (int i = 0; i < 10; i++) {
        batcher.add(
            RowMutationEntry.create("test-row-" + i)
                .setCell("cf1", ByteString.EMPTY, ByteString.copyFrom(rowBytes)));
      }
    }
    return testTable;
  }
}
