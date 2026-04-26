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
package com.google.cloud.kafka.connect.bigtable.mapping;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** A helper class allowing to report some problem only once intended for use with */
public class SchemaErrorReporter {
  private static final Logger LOGGER = LoggerFactory.getLogger(SchemaErrorReporter.class);

  /**
   * Logs a warning about a problem.
   *
   * <p>Further warnings related to the same {@code problem} problem will not be logged to avoid the
   * possibility of flooding the log with warnings for possibly each encountered Kafka message.
   *
   * @param alreadyReportedProblems A {@link Set} of problems for which error messages have already
   *     been sent.
   * @param problem A problem associated with {@code problemMessage}.
   * @param problemMessage A message to be logged.
   * @param <T> Identifier of a problem.
   */
  public static <T> void reportProblemAtMostOnce(
      Set<T> alreadyReportedProblems, T problem, String problemMessage) {
    boolean firstSeen = alreadyReportedProblems.add(problem);
    if (firstSeen) {
      LOGGER.warn(problemMessage);
    }
  }
}
