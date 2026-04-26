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
package com.google.cloud.kafka.connect.bigtable.version;

import java.util.Optional;

/** A class responsible for extracting maven-generated package metadata. */
public class PackageMetadata {
  public static String UNKNOWN_VERSION = "unknown";

  /**
   * Extracts version information from the package metadata.
   *
   * @return String representation of the version of the package. Is equal to {@link
   *     PackageMetadata#UNKNOWN_VERSION} when the information is missing from package metadata.
   */
  public static String getVersion() {
    Optional<String> discoveredVersion = Optional.empty();
    try {
      discoveredVersion =
          Optional.ofNullable(PackageMetadata.class.getPackage().getImplementationVersion());
    } catch (NullPointerException ignored) {
    }
    return discoveredVersion.orElse(UNKNOWN_VERSION);
  }
}
