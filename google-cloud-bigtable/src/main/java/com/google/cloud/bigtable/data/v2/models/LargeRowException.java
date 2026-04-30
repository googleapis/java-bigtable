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
package com.google.cloud.bigtable.data.v2.models;

import com.google.api.core.InternalApi;
import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiException;
import com.google.protobuf.ByteString;
import io.grpc.Status;
import java.util.List;
import javax.annotation.Nonnull;

/** Exception that wraps all the large row keys encountered in a ReadRows request. */
public final class LargeRowException extends ApiException {
  private final List<ByteString> largeRowKeys;

  @InternalApi
  public LargeRowException(@Nonnull List<ByteString> largeRowKeys) {
    super(
        "Encountered large row keys",
        null,
        GrpcStatusCode.of(Status.Code.FAILED_PRECONDITION),
        false);
    this.largeRowKeys = largeRowKeys;
  }

  /** Retrieve the keys of the rows that were too large to be read. */
  @Nonnull
  public List<ByteString> getLargeRowKeys() {
    return largeRowKeys;
  }
}
