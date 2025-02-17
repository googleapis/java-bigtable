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
package com.google.cloud.bigtable.data.v2.stub.sql;

import com.google.api.core.InternalApi;
import com.google.api.core.SettableApiFuture;
import com.google.bigtable.v2.ExecuteQueryRequest;
import com.google.cloud.bigtable.data.v2.internal.PrepareResponse;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.cloud.bigtable.data.v2.models.sql.BoundStatement;
import com.google.cloud.bigtable.data.v2.models.sql.ResultSetMetadata;
import com.google.protobuf.ByteString;
import javax.annotation.Nullable;

/**
 * Used to provide a future to the ExecuteQuery callable chain in order to return metadata to users
 * outside of the stream of rows.
 *
 * <p>This should only be constructed by {@link ExecuteQueryCallable} not directly by users.
 *
 * <p>This is considered an internal implementation detail and should not be used by applications.
 */
@InternalApi("For internal use only")
public class ExecuteQueryCallContext {

  private final BoundStatement boundStatement;
  private final SettableApiFuture<ResultSetMetadata> metadataFuture;
  private final PrepareResponse latestPrepareResponse;
  private @Nullable ByteString resumeToken;

  private ExecuteQueryCallContext(
      BoundStatement boundStatement, SettableApiFuture<ResultSetMetadata> metadataFuture) {
    this.boundStatement = boundStatement;
    this.metadataFuture = metadataFuture;
    this.latestPrepareResponse = boundStatement.getLatestPrepareResponse();
  }

  public static ExecuteQueryCallContext create(
      BoundStatement boundStatement, SettableApiFuture<ResultSetMetadata> metadataFuture) {
    return new ExecuteQueryCallContext(boundStatement, metadataFuture);
  }

  ExecuteQueryRequest toRequest(RequestContext requestContext) {
    return boundStatement.toProto(
        latestPrepareResponse.preparedQuery(), requestContext, resumeToken);
  }

  /**
   * Metadata can change as the plan is refreshed. Once a resume token or complete has been received
   * from the stream we know that the {@link com.google.bigtable.v2.PrepareQueryResponse} can no
   * longer change, so we can set the metadata.
   */
  void finalizeMetadata() {
    metadataFuture.set(latestPrepareResponse.resultSetMetadata());
  }

  /**
   * If the stream receives an error before receiving any response it needs to be passed through to
   * the metadata future
   */
  void setMetadataException(Throwable t) {
    metadataFuture.setException(t);
  }

  SettableApiFuture<ResultSetMetadata> resultSetMetadataFuture() {
    return this.metadataFuture;
  }

  void setLatestResumeToken(ByteString resumeToken) {
    this.resumeToken = resumeToken;
  }
}
