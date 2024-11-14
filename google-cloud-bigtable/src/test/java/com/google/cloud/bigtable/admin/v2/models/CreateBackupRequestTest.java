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
package com.google.cloud.bigtable.admin.v2.models;

import static com.google.common.truth.Truth.assertThat;

import com.google.bigtable.admin.v2.Backup;
import com.google.cloud.bigtable.admin.v2.internal.NameUtil;
import com.google.cloud.bigtable.admin.v2.models.Backup.BackupType;
import com.google.protobuf.Timestamp;
import java.time.Duration;
import java.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CreateBackupRequestTest {

  private static final String TABLE_ID = "my-table";
  private static final String BACKUP_ID = "my-backup";
  private static final String PROJECT_ID = "my-project";
  private static final String INSTANCE_ID = "my-instance";
  private static final String CLUSTER_ID = "my-cluster";
  private static final Instant EXPIRE_TIME = Instant.now().plus(Duration.ofDays(15));
  private static final Instant HOT_TO_STANDARD_TIME = Instant.now().plus(Duration.ofDays(10));

  @Test
  public void testToProto() {
    CreateBackupRequest request =
        CreateBackupRequest.of(CLUSTER_ID, BACKUP_ID)
            .setSourceTableId(TABLE_ID)
            .setExpireTimeInstant(EXPIRE_TIME)
            .setBackupType(BackupType.HOT)
            .setHotToStandardTimeInstant(HOT_TO_STANDARD_TIME);

    com.google.bigtable.admin.v2.CreateBackupRequest requestProto =
        com.google.bigtable.admin.v2.CreateBackupRequest.newBuilder()
            .setBackupId(BACKUP_ID)
            .setBackup(
                Backup.newBuilder()
                    .setSourceTable(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
                    .setExpireTime(
                        Timestamp.newBuilder()
                            .setSeconds(EXPIRE_TIME.getEpochSecond())
                            .setNanos(EXPIRE_TIME.getNano())
                            .build())
                    .setBackupType(Backup.BackupType.HOT)
                    .setHotToStandardTime(
                        Timestamp.newBuilder()
                            .setSeconds(HOT_TO_STANDARD_TIME.getEpochSecond())
                            .setNanos(HOT_TO_STANDARD_TIME.getNano())
                            .build())
                    .build())
            .setParent(NameUtil.formatClusterName(PROJECT_ID, INSTANCE_ID, CLUSTER_ID))
            .build();
    assertThat(request.toProto(PROJECT_ID, INSTANCE_ID)).isEqualTo(requestProto);
  }

  @Test
  public void testEquality() {
    CreateBackupRequest request =
        CreateBackupRequest.of(CLUSTER_ID, BACKUP_ID)
            .setSourceTableId(TABLE_ID)
            .setExpireTimeInstant(EXPIRE_TIME)
            .setBackupType(BackupType.HOT)
            .setHotToStandardTimeInstant(HOT_TO_STANDARD_TIME);

    assertThat(request)
        .isEqualTo(
            CreateBackupRequest.of(CLUSTER_ID, BACKUP_ID)
                .setSourceTableId(TABLE_ID)
                .setExpireTimeInstant(EXPIRE_TIME)
                .setBackupType(BackupType.HOT)
                .setHotToStandardTimeInstant(HOT_TO_STANDARD_TIME));

    assertThat(request)
        .isNotEqualTo(
            CreateBackupRequest.of(CLUSTER_ID, BACKUP_ID)
                .setSourceTableId("another-table")
                .setExpireTimeInstant(EXPIRE_TIME)
                .setBackupType(BackupType.HOT)
                .setHotToStandardTimeInstant(HOT_TO_STANDARD_TIME));

    assertThat(request)
        .isNotEqualTo(
            CreateBackupRequest.of(CLUSTER_ID, BACKUP_ID)
                .setSourceTableId(TABLE_ID)
                .setExpireTimeInstant(EXPIRE_TIME)
                .setBackupType(BackupType.STANDARD)
                .setHotToStandardTimeInstant(HOT_TO_STANDARD_TIME));
  }

  @Test
  public void testHashCode() {
    CreateBackupRequest request =
        CreateBackupRequest.of(CLUSTER_ID, BACKUP_ID)
            .setSourceTableId(TABLE_ID)
            .setExpireTimeInstant(EXPIRE_TIME)
            .setBackupType(BackupType.HOT)
            .setHotToStandardTimeInstant(HOT_TO_STANDARD_TIME);

    assertThat(request.hashCode())
        .isEqualTo(
            CreateBackupRequest.of(CLUSTER_ID, BACKUP_ID)
                .setSourceTableId(TABLE_ID)
                .setExpireTimeInstant(EXPIRE_TIME)
                .setBackupType(BackupType.HOT)
                .setHotToStandardTimeInstant(HOT_TO_STANDARD_TIME)
                .hashCode());

    assertThat(request.hashCode())
        .isNotEqualTo(
            CreateBackupRequest.of(CLUSTER_ID, BACKUP_ID)
                .setSourceTableId("another-table")
                .setExpireTimeInstant(EXPIRE_TIME)
                .setBackupType(BackupType.HOT)
                .setHotToStandardTimeInstant(HOT_TO_STANDARD_TIME)
                .hashCode());

    assertThat(request.hashCode())
        .isNotEqualTo(
            CreateBackupRequest.of(CLUSTER_ID, BACKUP_ID)
                .setSourceTableId(TABLE_ID)
                .setExpireTimeInstant(EXPIRE_TIME)
                .setBackupType(BackupType.BACKUP_TYPE_UNSPECIFIED)
                .setHotToStandardTimeInstant(HOT_TO_STANDARD_TIME)
                .hashCode());
  }
}
