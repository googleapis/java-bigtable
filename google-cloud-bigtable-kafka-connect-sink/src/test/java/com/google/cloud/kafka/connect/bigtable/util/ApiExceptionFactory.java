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
package com.google.cloud.kafka.connect.bigtable.util;

import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import io.grpc.Status;

public class ApiExceptionFactory {
  public static ApiException create() {
    return create(Status.Code.NOT_FOUND);
  }

  public static ApiException create(Status.Code code) {
    return create(new Throwable(), GrpcStatusCode.of(code), true);
  }

  public static ApiException create(Throwable cause, StatusCode code, boolean retryable) {
    return com.google.api.gax.rpc.ApiExceptionFactory.createException(cause, code, retryable);
  }
}
