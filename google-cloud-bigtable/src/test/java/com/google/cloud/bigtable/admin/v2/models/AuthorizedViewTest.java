/*
 * Copyright 2023 Google LLC
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

import com.google.bigtable.admin.v2.AuthorizedViewName;
import com.google.cloud.bigtable.admin.v2.models.AuthorizedView.FamilySubsets;
import com.google.cloud.bigtable.admin.v2.models.AuthorizedView.SubsetView;
import com.google.protobuf.ByteString;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AuthorizedViewTest {
  private static final String PROJECT_ID = "my-project";
  private static final String INSTANCE_ID = "my-instance";
  private static final String TABLE_ID = "my-table";
  private static final String AUTHORIZED_VIEW_ID = "my-authorized-view";

  @Test
  public void testFromProto() {
    AuthorizedViewName authorizedViewName =
        AuthorizedViewName.of(PROJECT_ID, INSTANCE_ID, TABLE_ID, AUTHORIZED_VIEW_ID);

    com.google.bigtable.admin.v2.AuthorizedView.SubsetView subsetViewProto =
        com.google.bigtable.admin.v2.AuthorizedView.SubsetView.newBuilder()
            .addRowPrefixes(ByteString.copyFromUtf8("row1#"))
            .addRowPrefixes(ByteString.copyFromUtf8("row2#"))
            .putFamilySubsets(
                "family1",
                com.google.bigtable.admin.v2.AuthorizedView.FamilySubsets.newBuilder()
                    .addQualifiers(ByteString.copyFromUtf8("column1"))
                    .addQualifiers(ByteString.copyFromUtf8("column2"))
                    .addQualifierPrefixes(ByteString.copyFromUtf8("column3#"))
                    .addQualifierPrefixes(ByteString.copyFromUtf8("column4#"))
                    .build())
            .putFamilySubsets(
                "family2",
                com.google.bigtable.admin.v2.AuthorizedView.FamilySubsets.newBuilder()
                    .addQualifiers(ByteString.copyFromUtf8("column5"))
                    .addQualifierPrefixes(ByteString.copyFromUtf8(""))
                    .build())
            .build();

    com.google.bigtable.admin.v2.AuthorizedView authorizedViewProto =
        com.google.bigtable.admin.v2.AuthorizedView.newBuilder()
            .setName(authorizedViewName.toString())
            .setDeletionProtection(true)
            .setSubsetView(subsetViewProto)
            .build();

    AuthorizedView result = AuthorizedView.fromProto(authorizedViewProto);

    assertThat(result.getId()).isEqualTo(AUTHORIZED_VIEW_ID);
    assertThat(result.getTableId()).isEqualTo(TABLE_ID);
    assertThat(result.isDeletionProtected()).isTrue();
    SubsetView subsetViewResult = (SubsetView) result.getAuthorizedViewImpl();
    assertThat(subsetViewResult).isEqualTo(SubsetView.fromProto(subsetViewProto));
    assertThat(subsetViewResult.getRowPrefixes())
        .containsExactly(ByteString.copyFromUtf8("row1#"), ByteString.copyFromUtf8("row2#"));

    Map<String, FamilySubsets> familySubsetsResult = subsetViewResult.getFamilySubsets();
    assertThat(familySubsetsResult)
        .containsExactly(
            "family1",
            FamilySubsets.fromProto(subsetViewProto.getFamilySubsetsOrThrow("family1")),
            "family2",
            FamilySubsets.fromProto(subsetViewProto.getFamilySubsetsOrThrow("family2")));
    assertThat(familySubsetsResult.get("family1").getQualifiers())
        .containsExactly(ByteString.copyFromUtf8("column1"), ByteString.copyFromUtf8("column2"));
    assertThat(familySubsetsResult.get("family1").getQualifierPrefixes())
        .containsExactly(ByteString.copyFromUtf8("column3#"), ByteString.copyFromUtf8("column4#"));
    assertThat(familySubsetsResult.get("family2").getQualifiers())
        .containsExactly(ByteString.copyFromUtf8("column5"));
    assertThat(familySubsetsResult.get("family2").getQualifierPrefixes())
        .containsExactly(ByteString.copyFromUtf8(""));
  }

  @Test
  public void testRequiresName() {
    com.google.bigtable.admin.v2.AuthorizedView proto =
        com.google.bigtable.admin.v2.AuthorizedView.newBuilder()
            .setDeletionProtection(true)
            .setSubsetView(
                com.google.bigtable.admin.v2.AuthorizedView.SubsetView.newBuilder().build())
            .build();

    Exception actualException = null;

    try {
      AuthorizedView.fromProto(proto);
    } catch (Exception e) {
      actualException = e;
    }

    assertThat(actualException).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void testRequiresAuthorizedViewImpl() {
    AuthorizedViewName authorizedViewName =
        AuthorizedViewName.of(PROJECT_ID, INSTANCE_ID, TABLE_ID, AUTHORIZED_VIEW_ID);
    com.google.bigtable.admin.v2.AuthorizedView proto =
        com.google.bigtable.admin.v2.AuthorizedView.newBuilder()
            .setName(authorizedViewName.toString())
            .setDeletionProtection(true)
            .build();
    Exception actualException = null;

    try {
      AuthorizedView.fromProto(proto);
    } catch (Exception e) {
      actualException = e;
    }

    assertThat(actualException).isInstanceOf(IllegalArgumentException.class);
  }
}
