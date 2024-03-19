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
package com.google.cloud.bigtable.data.v2.stub.mutaterows;

import static com.google.common.truth.Truth.assertThat;

import com.google.api.core.SettableApiFuture;
import com.google.api.gax.grpc.GrpcStatusCode;
import com.google.api.gax.rpc.ApiExceptionFactory;
import com.google.api.gax.rpc.UnaryCallable;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.cloud.bigtable.data.v2.models.BulkMutation;
import com.google.cloud.bigtable.data.v2.models.MutateRowsException;
import com.google.cloud.bigtable.data.v2.models.MutateRowsException.FailedMutation;
import com.google.cloud.bigtable.data.v2.stub.MutateRowsErrorConverterUnaryCallable;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public class MutateRowsErrorConverterUnaryCallableTest {

  private static final RequestContext REQUEST_CONTEXT =
      RequestContext.create("fake-project", "fake-instance", "fake-profile");
  private UnaryCallable<BulkMutation, MutateRowsAttemptResult> innerCallable;
  private ArgumentCaptor<BulkMutation> innerMutation;
  private SettableApiFuture<MutateRowsAttemptResult> innerResult;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    innerCallable = Mockito.mock(UnaryCallable.class);
    innerMutation = ArgumentCaptor.forClass(BulkMutation.class);
    innerResult = SettableApiFuture.create();
    Mockito.when(innerCallable.futureCall(innerMutation.capture(), Mockito.any()))
        .thenReturn(innerResult);
  }

  @Test
  public void testSuccess() {
    MutateRowsErrorConverterUnaryCallable callable =
        new MutateRowsErrorConverterUnaryCallable(innerCallable);

    innerResult.set(MutateRowsAttemptResult.success());
    callable.call(BulkMutation.create("fake-table"));
  }

  @Test
  public void testFailure() {
    MutateRowsErrorConverterUnaryCallable callable =
        new MutateRowsErrorConverterUnaryCallable(innerCallable);

    innerResult.set(
        MutateRowsAttemptResult.create(
            Arrays.asList(
                FailedMutation.create(
                    0,
                    ApiExceptionFactory.createException(
                        null, GrpcStatusCode.of(io.grpc.Status.Code.INTERNAL), false))),
            true));

    MutateRowsException exception =
        Assert.assertThrows(
            MutateRowsException.class, () -> callable.call(BulkMutation.create("fake-table")));

    assertThat(exception).isInstanceOf(MutateRowsException.class);
    assertThat((exception).isRetryable()).isTrue();
  }
}
