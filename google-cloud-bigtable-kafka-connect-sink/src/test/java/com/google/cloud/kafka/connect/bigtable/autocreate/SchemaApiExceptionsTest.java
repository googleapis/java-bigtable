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
package com.google.cloud.kafka.connect.bigtable.autocreate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.StatusCode.Code;
import com.google.cloud.kafka.connect.bigtable.util.ApiExceptionFactory;
import io.grpc.Status;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SchemaApiExceptionsTest {
  @Test
  public void testIsStatusCodeCausedByInputError() {
    for (Code causedByInputError :
        List.of(Code.INVALID_ARGUMENT, Code.OUT_OF_RANGE, Code.FAILED_PRECONDITION)) {
      assertTrue(
          BigtableSchemaManager.SchemaApiExceptions.isStatusCodeCausedByInputError(
              causedByInputError));
    }

    for (Code notCausedByInputError :
        List.of(Code.NOT_FOUND, Code.RESOURCE_EXHAUSTED, Code.CANCELLED, Code.UNKNOWN)) {
      assertFalse(
          BigtableSchemaManager.SchemaApiExceptions.isStatusCodeCausedByInputError(
              notCausedByInputError));
    }
  }

  @Test
  public void testExtractStatusCodeNonempty() {
    StatusCode code = GrpcStatusCode.of(Status.Code.RESOURCE_EXHAUSTED);
    ApiException apiException = ApiExceptionFactory.create(new Throwable(), code, true);
    Throwable one = new Throwable(apiException);
    Throwable two = new Throwable(one);
    Throwable three = new Throwable(two);
    for (Throwable t : List.of(apiException, one, two, three)) {
      assertTrue(
          BigtableSchemaManager.SchemaApiExceptions.maybeExtractBigtableStatusCode(t).isPresent());
    }
  }

  @Test
  public void testExtractGetsTheFirstStatusCode() {
    StatusCode causeCode = GrpcStatusCode.of(Status.Code.RESOURCE_EXHAUSTED);
    ApiException cause = ApiExceptionFactory.create(new Throwable(), causeCode, true);
    StatusCode exceptionCode = GrpcStatusCode.of(Status.Code.INVALID_ARGUMENT);
    ApiException exception = ApiExceptionFactory.create(cause, exceptionCode, false);

    assertEquals(
        causeCode,
        BigtableSchemaManager.SchemaApiExceptions.maybeExtractBigtableStatusCode(cause).get());
    assertEquals(
        exceptionCode,
        BigtableSchemaManager.SchemaApiExceptions.maybeExtractBigtableStatusCode(exception).get());
  }

  @Test
  public void testExtractStatusCodeEmpty() {
    Throwable one = new Throwable();
    Throwable two = new Throwable(one);
    Throwable three = new Throwable(two);

    for (Throwable t : List.of(one, two, three)) {
      assertTrue(
          BigtableSchemaManager.SchemaApiExceptions.maybeExtractBigtableStatusCode(t).isEmpty());
    }
    assertTrue(
        BigtableSchemaManager.SchemaApiExceptions.maybeExtractBigtableStatusCode(null).isEmpty());
  }

  @Test
  public void testIsCausedByInputError() {
    assertFalse(BigtableSchemaManager.SchemaApiExceptions.isCausedByInputError(null));

    ApiException inputErrorException =
        ApiExceptionFactory.create(
            new Throwable(), GrpcStatusCode.of(Status.Code.INVALID_ARGUMENT), false);
    ApiException bigtableErrorException =
        ApiExceptionFactory.create(new Throwable(), GrpcStatusCode.of(Status.Code.UNKNOWN), false);

    assertTrue(BigtableSchemaManager.SchemaApiExceptions.isCausedByInputError(inputErrorException));
    assertTrue(
        BigtableSchemaManager.SchemaApiExceptions.isCausedByInputError(
            new ExecutionException(inputErrorException)));
    assertTrue(
        BigtableSchemaManager.SchemaApiExceptions.isCausedByInputError(
            new ExecutionException(new Throwable(inputErrorException))));

    assertFalse(
        BigtableSchemaManager.SchemaApiExceptions.isCausedByInputError(bigtableErrorException));
    assertFalse(
        BigtableSchemaManager.SchemaApiExceptions.isCausedByInputError(
            new ExecutionException(bigtableErrorException)));
    assertFalse(
        BigtableSchemaManager.SchemaApiExceptions.isCausedByInputError(
            new ExecutionException(new Throwable(bigtableErrorException))));
  }

  @Test
  public void testIsCausedByInputErrorIgnoresRetriableField() {
    for (Map.Entry<Status.Code, Boolean> testCase :
        Map.of(
                Status.Code.INVALID_ARGUMENT, true,
                Status.Code.RESOURCE_EXHAUSTED, false)
            .entrySet()) {
      Status.Code code = testCase.getKey();
      Boolean expectedResult = testCase.getValue();

      for (Boolean retryableField : List.of(true, false)) {
        ApiException e =
            ApiExceptionFactory.create(new Throwable(), GrpcStatusCode.of(code), retryableField);
        assertEquals(
            expectedResult, BigtableSchemaManager.SchemaApiExceptions.isCausedByInputError(e));
      }
    }
    ;
  }
}
