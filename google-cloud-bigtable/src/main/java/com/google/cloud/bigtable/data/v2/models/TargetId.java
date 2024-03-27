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

import com.google.api.core.InternalApi;
import com.google.api.core.InternalExtensionOnly;
import java.io.Serializable;

/**
 * TargetId defines the scope a data operation can be applied to.
 *
 * @see AuthorizedViewId
 * @see TableId
 */
@InternalExtensionOnly
public interface TargetId extends Serializable {
  /**
   * Combines the table or authorized view id with the projectId and instanceId to form the actual
   * resource name in the request protobuf.
   *
   * <p>This method is considered an internal implementation detail and not meant to be used by
   * applications.
   */
  @InternalApi
  String toResourceName(String projectId, String instanceId);

  /**
   * Returns true if this TargetId object represents id for an authorized view (rather than a
   * table).
   */
  @InternalApi
  boolean scopedForAuthorizedView();
}
