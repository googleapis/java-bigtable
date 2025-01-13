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
package com.google.cloud.kafka.connect.bigtable.util;

import com.google.common.collect.Streams;

public class TestId {
  public static String getTestClassId(Class<?> testClass) {
    return testClass.getSimpleName();
  }

  public static String getTestCaseId(Class<?> testClass) {
    StackWalker.StackFrame frame =
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
            .walk(s -> Streams.findLast(s.filter(f -> f.getDeclaringClass().equals(testClass))))
            .get();
    return getTestClassId(frame.getDeclaringClass()) + frame.getMethodName();
  }
}
