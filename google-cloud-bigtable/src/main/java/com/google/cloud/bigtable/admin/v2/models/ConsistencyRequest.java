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
package com.google.cloud.bigtable.admin.v2.models;

import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import com.google.bigtable.admin.v2.CheckConsistencyRequest;
import com.google.bigtable.admin.v2.DataBoostReadLocalWrites;
import com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest;
import com.google.bigtable.admin.v2.StandardReadRemoteWrites;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@AutoValue
public abstract class ConsistencyRequest {
  @Nonnull
  protected abstract String getTableName();

  @Nonnull
  protected abstract CheckConsistencyRequest.ModeCase getMode();

  /**
   * Internal accessor for the consistency token. Must be public to be accessible from the stub
   * package.
   */
  @InternalApi
  @Nullable
  public abstract String getConsistencyToken();

  public static ConsistencyRequest forReplication(String tableName) {
    return new AutoValue_ConsistencyRequest(
        tableName, CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES, null);
  }

  /**
   * Creates a request to check consistency using an existing token.
   *
   * @param tableName The table name.
   * @param consistencyToken The token to check. Must not be null.
   * @throws NullPointerException if consistencyToken is null.
   */
  public static ConsistencyRequest forReplication(String tableName, String consistencyToken) {
    Preconditions.checkNotNull(consistencyToken, "consistencyToken must not be null");

    return new AutoValue_ConsistencyRequest(
        tableName, CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES, consistencyToken);
  }

  public static ConsistencyRequest forDataBoost(String tableName) {
    return new AutoValue_ConsistencyRequest(
        tableName, CheckConsistencyRequest.ModeCase.DATA_BOOST_READ_LOCAL_WRITES, null);
  }

  @InternalApi
  public CheckConsistencyRequest toCheckConsistencyProto(String token) {
    CheckConsistencyRequest.Builder builder = CheckConsistencyRequest.newBuilder();

    if (getMode().equals(CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES)) {
      builder.setStandardReadRemoteWrites(StandardReadRemoteWrites.newBuilder().build());
    } else {
      builder.setDataBoostReadLocalWrites(DataBoostReadLocalWrites.newBuilder().build());
    }

    return builder.setName(getTableName()).setConsistencyToken(token).build();
  }

  @InternalApi
  public GenerateConsistencyTokenRequest toGenerateTokenProto() {
    GenerateConsistencyTokenRequest.Builder builder = GenerateConsistencyTokenRequest.newBuilder();
    return builder.setName(getTableName()).build();
  }
}
