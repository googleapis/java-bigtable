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

import static com.google.api.gax.util.TimeConversionUtils.toJavaTimeInstant;

import com.google.api.core.InternalApi;
import com.google.api.core.ObsoleteApi;
import com.google.bigtable.admin.v2.Backup;
import com.google.cloud.bigtable.admin.v2.internal.NameUtil;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.protobuf.FieldMask;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.FieldMaskUtil;
import javax.annotation.Nonnull;

/** Fluent wrapper for {@link com.google.bigtable.admin.v2.UpdateBackupRequest} */
public final class UpdateBackupRequest {
  private final com.google.bigtable.admin.v2.UpdateBackupRequest.Builder requestBuilder =
      com.google.bigtable.admin.v2.UpdateBackupRequest.newBuilder();
  private final String backupId;
  private final String clusterId;

  public static UpdateBackupRequest of(String clusterId, String backupId) {
    UpdateBackupRequest request = new UpdateBackupRequest(clusterId, backupId);
    return request;
  }

  private UpdateBackupRequest(String clusterId, String backupId) {
    Preconditions.checkNotNull(clusterId);
    Preconditions.checkNotNull(backupId);
    this.backupId = backupId;
    this.clusterId = clusterId;
  }

  private void updateFieldMask(int fieldNumber) {
    FieldMask newMask = FieldMaskUtil.fromFieldNumbers(Backup.class, fieldNumber);
    requestBuilder.setUpdateMask(FieldMaskUtil.union(requestBuilder.getUpdateMask(), newMask));
  }

  /** This method is obsolete. Use {@link #setExpireTimeInstant(java.time.Instant)} instead. */
  @ObsoleteApi("Use setExpireTimeInstant(java.time.Instant) instead.")
  public UpdateBackupRequest setExpireTime(org.threeten.bp.Instant expireTime) {
    return setExpireTimeInstant(toJavaTimeInstant(expireTime));
  }

  public UpdateBackupRequest setExpireTimeInstant(java.time.Instant expireTime) {
    Preconditions.checkNotNull(expireTime);
    requestBuilder
        .getBackupBuilder()
        .setExpireTime(
            Timestamp.newBuilder()
                .setSeconds(expireTime.getEpochSecond())
                .setNanos(expireTime.getNano())
                .build());
    updateFieldMask(Backup.EXPIRE_TIME_FIELD_NUMBER);
    return this;
  }
  /**
   * This method is obsolete. Use {@link #setHotToStandardTimeInstant(java.time.Instant)} instead.
   */
  @ObsoleteApi("Use setHotToStandardTimeInstant(java.time.Instant) instead.")
  public UpdateBackupRequest setHotToStandardTime(org.threeten.bp.Instant hotToStandardTime) {
    return setHotToStandardTimeInstant(toJavaTimeInstant(hotToStandardTime));
  }

  // The time at which this backup will be converted from a hot backup to a standard backup. Only
  // applicable for hot backups. If not set, the backup will remain as a hot backup until it is
  // deleted.
  public UpdateBackupRequest setHotToStandardTimeInstant(java.time.Instant hotToStandardTime) {
    Preconditions.checkNotNull(hotToStandardTime);
    requestBuilder
        .getBackupBuilder()
        .setHotToStandardTime(
            Timestamp.newBuilder()
                .setSeconds(hotToStandardTime.getEpochSecond())
                .setNanos(hotToStandardTime.getNano())
                .build());
    updateFieldMask(Backup.HOT_TO_STANDARD_TIME_FIELD_NUMBER);
    return this;
  }

  public UpdateBackupRequest clearHotToStandardTime() {
    requestBuilder.getBackupBuilder().clearHotToStandardTime();
    updateFieldMask(Backup.HOT_TO_STANDARD_TIME_FIELD_NUMBER);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateBackupRequest that = (UpdateBackupRequest) o;
    return Objects.equal(
            requestBuilder.getBackupBuilder().getExpireTime(),
            that.requestBuilder.getBackupBuilder().getExpireTime())
        && Objects.equal(
            requestBuilder.getBackupBuilder().getHotToStandardTime(),
            that.requestBuilder.getBackupBuilder().getHotToStandardTime())
        && Objects.equal(requestBuilder.getUpdateMask(), that.requestBuilder.getUpdateMask())
        && Objects.equal(clusterId, that.clusterId)
        && Objects.equal(backupId, that.backupId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        requestBuilder.getBackupBuilder().getExpireTime(),
        requestBuilder.getBackupBuilder().getHotToStandardTime(),
        requestBuilder.getUpdateMask(),
        backupId);
  }

  @InternalApi
  public com.google.bigtable.admin.v2.UpdateBackupRequest toProto(
      @Nonnull String projectId, @Nonnull String instanceId) {
    Preconditions.checkNotNull(projectId);
    Preconditions.checkNotNull(instanceId);

    requestBuilder
        .getBackupBuilder()
        .setName(NameUtil.formatBackupName(projectId, instanceId, clusterId, backupId));
    return requestBuilder.build();
  }
}
