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

import com.google.api.core.ApiFuture;
import com.google.api.core.SettableApiFuture;

public class FutureUtil {
  public static <T> ApiFuture<T> completedApiFuture(T value) {
    SettableApiFuture<T> future = SettableApiFuture.create();
    future.set(value);
    return future;
  }

  public static <T> ApiFuture<T> failedApiFuture(Exception exception) {
    SettableApiFuture<T> future = SettableApiFuture.create();
    future.setException(exception);
    return future;
  }
}
