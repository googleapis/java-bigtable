/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.bigtable.data.v2.models;

import com.google.auto.value.AutoValue;
import java.io.Serializable;
import javax.annotation.Nullable;

/**
 * Represents a collection of customized options for sampling row keys. Currently, it only wraps an
 * authorized view id option but there could be more in the future.
 */
@AutoValue
public abstract class SampleRowKeysOptions implements Serializable {
  @Nullable private String authorizedViewId = null;

  public static SampleRowKeysOptions create() {
    return new AutoValue_SampleRowKeysOptions(null);
  }

  /**
   * Limits the sampling to subsets of the table represented in an authorized view with the
   * specified authorized view id.
   *
   * @see com.google.cloud.bigtable.admin.v2.models.AuthorizedView for more details.
   */
  public static SampleRowKeysOptions createForAuthorizedView(String authorizedViewId) {
    return new AutoValue_SampleRowKeysOptions(authorizedViewId);
  }

  /**
   * Gets the id of the authorized view that this sampling is limited to. Could be null if the
   * sampling is based on the entire table rather than a specific authorized view.
   */
  @Nullable
  public abstract String getAuthorizedViewId();
}
