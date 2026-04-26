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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mockingDetails;

import java.util.Collection;
import org.mockito.invocation.Invocation;

public class MockUtil {
  public static void assertTotalNumberOfInvocations(Object mock, int expected) {
    Collection<Invocation> invocations = mockingDetails(mock).getInvocations();
    assertEquals(expected, invocations.size());
  }
}
