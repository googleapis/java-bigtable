/*
 * Copyright 2026 Google LLC
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
package com.google.cloud.bigtable.data.v2.internal.dp;

import com.google.api.core.InternalApi;
import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@InternalApi
class GCECheck {
  private static final String GCE_PRODUCTION_NAME_PRIOR_2016 = "Google";
  private static final String GCE_PRODUCTION_NAME_AFTER_2016 = "Google Compute Engine";

  @VisibleForTesting static String systemProductName = null;

  static boolean isRunningOnGCP() {
    String osName = System.getProperty("os.name");
    if ("Linux".equals(osName)) {
      String productName = getSystemProductName();
      return productName.contains(GCE_PRODUCTION_NAME_PRIOR_2016)
          || productName.contains(GCE_PRODUCTION_NAME_AFTER_2016);
    }
    return false;
  }

  private static String getSystemProductName() {
    if (systemProductName != null) {
      return systemProductName;
    }
    try {
      return new String(
              Files.readAllBytes(Paths.get("/sys/class/dmi/id/product_name")),
              StandardCharsets.UTF_8)
          .trim();
    } catch (IOException e) {
      return "";
    }
  }
}
