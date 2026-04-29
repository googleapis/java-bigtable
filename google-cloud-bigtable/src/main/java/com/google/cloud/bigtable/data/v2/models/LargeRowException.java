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
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.protobuf.ByteString;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Thrown by the ReadRows when at least one row was too large to be read. */
public final class LargeRowException extends ApiException {
  private final List<ByteString> largeRowKeys;

  /**
   * This constructor is considered an internal implementation detail and not meant to be used by
   * applications.
   */
  @InternalApi
  public LargeRowException(
      @Nullable Throwable cause,
      @Nonnull StatusCode statusCode,
      boolean retryable,
      @Nonnull List<ByteString> largeRowKeys) {
    super(cause, statusCode, retryable);
    this.largeRowKeys = largeRowKeys;
  }

  @Override
  public String getMessage() {
    return "Large rows encountered: " + largeRowKeys;
  }

  /** Retrieve the keys of the rows that were too large to be read. */
  @Nonnull
  public List<ByteString> getLargeRowKeys() {
    return largeRowKeys;
  }
}
