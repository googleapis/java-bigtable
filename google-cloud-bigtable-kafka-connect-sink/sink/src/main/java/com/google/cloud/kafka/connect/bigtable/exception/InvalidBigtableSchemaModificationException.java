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
package com.google.cloud.kafka.connect.bigtable.exception;

import org.apache.kafka.connect.errors.DataException;

/**
 * An {@link Exception} that signifies that input {@link org.apache.kafka.connect.sink.SinkRecord
 * SinkRecord(s)} cause attempt of invalid Cloud Bigtable schema modification and thus is invalid
 * and should not be retried.
 */
public class InvalidBigtableSchemaModificationException extends DataException {
  public InvalidBigtableSchemaModificationException(String message) {
    super(message);
  }
}
