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

package com.google.cloud.bigtable.admin.v2.models;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;

import com.google.cloud.bigtable.admin.v2.internal.NameUtil;
import com.google.protobuf.ByteString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CreateSchemaBundleRequestTest {
  private static final String PROJECT_ID = "my-project";
  private static final String INSTANCE_ID = "my-instance";
  private static final String TABLE_ID = "my-table";
  private static final String SCHEMA_BUNDLE_ID = "my-schema-bundle";

  @Test
  public void testToProto() throws IOException{
    CreateSchemaBundleRequest request =
        CreateSchemaBundleRequest.of(TABLE_ID, SCHEMA_BUNDLE_ID).setProtoSchema("file.pb");

    com.google.bigtable.admin.v2.CreateSchemaBundleRequest requestProto =
        com.google.bigtable.admin.v2.CreateSchemaBundleRequest.newBuilder()
            .setParent(NameUtil.formatTableName(PROJECT_ID, INSTANCE_ID, TABLE_ID))
            .setSchemaBundleId(SCHEMA_BUNDLE_ID)
            .setSchemaBundle(
                com.google.bigtable.admin.v2.SchemaBundle.newBuilder()
                    .setProtoSchema(
                        com.google.bigtable.admin.v2.ProtoSchema.newBuilder()
                            .setProtoDescriptors(ByteString.copyFromUtf8("schema"))
                            .build())
                    .build())
            .build();
    assertThat(request.toProto(PROJECT_ID, INSTANCE_ID)).isEqualTo(requestProto);
  }

  @Test
  public void testEquality() throws IOException{
    CreateSchemaBundleRequest request =
        CreateSchemaBundleRequest.of(TABLE_ID, SCHEMA_BUNDLE_ID).setProtoSchema("file.pb");

    assertThat(request)
        .isEqualTo(
            CreateSchemaBundleRequest.of(TABLE_ID, SCHEMA_BUNDLE_ID).setProtoSchema("file.pb"));

    assertThat(request)
        .isNotEqualTo(
            CreateSchemaBundleRequest.of(TABLE_ID, SCHEMA_BUNDLE_ID)
                .setProtoSchema("updated_file.pb"));
  }

  @Test
  public void testHashCode() throws IOException{
    CreateSchemaBundleRequest request =
        CreateSchemaBundleRequest.of(TABLE_ID, SCHEMA_BUNDLE_ID).setProtoSchema("file.pb");

    assertThat(request.hashCode())
        .isEqualTo(
            CreateSchemaBundleRequest.of(TABLE_ID, SCHEMA_BUNDLE_ID)
                .setProtoSchema("file.pb")
                .hashCode());

    assertThat(request.hashCode())
        .isNotEqualTo(
            CreateSchemaBundleRequest.of(TABLE_ID, SCHEMA_BUNDLE_ID)
                .setProtoSchema("updated_file.pb")
                .hashCode());
  }
}
