/*
 * Copyright 2025 Google LLC
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
package com.google.cloud.bigtable.data.v2.internal;

import com.google.api.core.InternalApi;
import com.google.cloud.bigtable.data.v2.models.sql.BoundStatement;
import com.google.cloud.bigtable.data.v2.models.sql.BoundStatement.Builder;
import com.google.cloud.bigtable.data.v2.models.sql.PreparedStatement;

/**
 * Implementation of PreparedStatement that handles PreparedQuery refresh
 *
 * <p>This is considered an internal implementation detail and should not be used by applications.
 */
// TODO implement plan refresh
@InternalApi("For internal use only")
public class PreparedStatementImpl implements PreparedStatement {
  private PrepareResponse response;

  public PreparedStatementImpl(PrepareResponse response) {
    this.response = response;
  }

  public static PreparedStatement create(PrepareResponse response) {
    return new PreparedStatementImpl(response);
  }

  @Override
  public BoundStatement.Builder bind() {
    return new Builder(this);
  }

  // TODO update when plan refresh is implement
  @Override
  public PrepareResponse getPrepareResponse() {
    return response;
  }

  @Override
  public void close() throws Exception {
    // TODO cancel any background refresh
  }
}
