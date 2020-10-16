/*
 * Copyright 2020 Google LLC
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
package com.google.cloud.bigtable;

import com.google.api.core.InternalApi;

/** Utility to get the current version of the Bigtable client. */
@InternalApi("For internal use only")
// Note: this file MUST be named GoogleUtils.java so that it can be picked up by releasetool
public class GoogleUtils {
  public static final String BIGTABLE_CLIENT_VERSION =
      "1.16.1-SNAPSHOT"; // {x-version-update:google-cloud-bigtable:current}
}
