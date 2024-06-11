/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * A client to Cloud Bigtable API
 *
 * <p>The interfaces provided are listed below, along with usage samples.
 *
 * <p>======================= BaseBigtableDataClient =======================
 *
 * <p>Service Description: Service for reading from and writing to existing Bigtable tables.
 *
 * <p>Sample for BaseBigtableDataClient:
 *
 * <pre>{@code
 * // This snippet has been automatically generated and should be regarded as a code template only.
 * // It will require modifications to work:
 * // - It may require correct/in-range values for request initialization.
 * // - It may require specifying regional endpoints when creating the service client as shown in
 * // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
 * try (BaseBigtableDataClient baseBigtableDataClient = BaseBigtableDataClient.create()) {
 *   TableName tableName = TableName.of("[PROJECT]", "[INSTANCE]", "[TABLE]");
 *   ByteString rowKey = ByteString.EMPTY;
 *   List<Mutation> mutations = new ArrayList<>();
 *   MutateRowResponse response = baseBigtableDataClient.mutateRow(tableName, rowKey, mutations);
 * }
 * }</pre>
 */
@Generated("by gapic-generator-java")
package com.google.cloud.bigtable.data.v2;

import javax.annotation.Generated;
