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

import com.google.api.core.InternalApi;
import com.google.cloud.bigtable.data.v2.models.MutateRowsException.FailedMutation;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the result of a MutateRows attempt. It contains the list of failed
 * mutations, along with an indicator whether these errors are retryable.
 */
@InternalApi
public class MutateRowsAttemptResult {

  public final boolean isRetryable;
  public final List<FailedMutation> failedMutations;

  public MutateRowsAttemptResult() {
    this.failedMutations = new ArrayList<>();
    this.isRetryable = false;
  }

  public MutateRowsAttemptResult(List<FailedMutation> failedMutations, boolean isRetryable) {
    this.failedMutations = failedMutations;
    this.isRetryable = isRetryable;
  }
}
