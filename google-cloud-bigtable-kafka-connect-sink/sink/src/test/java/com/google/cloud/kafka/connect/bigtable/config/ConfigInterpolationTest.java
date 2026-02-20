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
package com.google.cloud.kafka.connect.bigtable.config;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ConfigInterpolationTest {
  private static final String TOPIC = "TOPIC";

  private final String template;
  private final String expected;

  public ConfigInterpolationTest(String template, String expected) {
    this.template = template;
    this.expected = expected;
  }

  @Parameterized.Parameters
  public static Collection testCases() {
    return Arrays.asList(
        new Object[][] {
          {"prefix_${topic}_suffix", "prefix_TOPIC_suffix"},
          {"prefix_${topic_suffix", "prefix_${topic_suffix"},
          {"prefix_$topic_suffix", "prefix_$topic_suffix"},
          {"prefix_${bad}_suffix", "prefix_${bad}_suffix"},
          {"noSubstitution", "noSubstitution"},
        });
  }

  @Test
  public void testReplace() {
    assertEquals(ConfigInterpolation.replace(template, TOPIC), expected);
  }
}
