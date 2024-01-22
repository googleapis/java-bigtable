/*
 * Copyright 2023 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import io.opentelemetry.api.common.AttributeKey;

class BuiltinMetricsAttributes {

  static final AttributeKey<String> PROJECT_ID = AttributeKey.stringKey("project_id");
  static final AttributeKey<String> INSTANCE_ID = AttributeKey.stringKey("instance");
  static final AttributeKey<String> TABLE_ID = AttributeKey.stringKey("table");
  static final AttributeKey<String> CLUSTER_ID = AttributeKey.stringKey("cluster");
  static final AttributeKey<String> ZONE_ID = AttributeKey.stringKey("zone");

  static final AttributeKey<String> APP_PROFILE = AttributeKey.stringKey("app_profile");
  static final AttributeKey<Boolean> STREAMING = AttributeKey.booleanKey("streaming");
  static final AttributeKey<String> METHOD = AttributeKey.stringKey("method");
  static final AttributeKey<String> STATUS = AttributeKey.stringKey("status");
  static final AttributeKey<String> CLIENT_NAME = AttributeKey.stringKey("client_name");
  static final AttributeKey<String> CLIENT_UID = AttributeKey.stringKey("client_uid");
}
