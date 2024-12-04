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
package com.google.cloud.bigtable.data.v2.internal;

import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import java.io.Serializable;

/**
 * Contains information necessary to construct Bigtable protobuf requests from user facing models.
 *
 * <p>The intention is to extract repetitive details like instance names into a configurable values
 * in {@link com.google.cloud.bigtable.data.v2.BigtableDataSettings} and expose them (via this
 * class) to each wrapper's toProto method.
 *
 * <p>This class is considered an internal implementation detail and not meant to be used by
 * applications.
 */
@InternalApi
@AutoValue
public abstract class TableAdminRequestContext implements Serializable {

  /** Creates a new instance of the {@link TableAdminRequestContext}. */
  public static TableAdminRequestContext create(String projectId, String instanceId) {
    return new AutoValue_TableAdminRequestContext(projectId, instanceId);
  }

  /** The project id that the client is configured to target. */
  public abstract String getProjectId();

  /** The instance id that the client is configured to target. */
  public abstract String getInstanceId();
}
