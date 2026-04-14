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
package com.google.cloud.bigtable.admin.v2.models;

import static com.google.common.truth.Truth.assertThat;

import com.google.bigtable.admin.v2.CheckConsistencyRequest;
import com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ConsistencyRequestTest {
  private final String TABLE_NAME = "projects/my-project/instances/my-instance/tables/my-table";
  private final String CONSISTENCY_TOKEN = "my-token";

  @Test
  public void testToCheckConsistencyProtoWithStandard() {
    ConsistencyRequest consistencyRequest = ConsistencyRequest.forReplication(TABLE_NAME);

    CheckConsistencyRequest checkConsistencyRequest =
        consistencyRequest.toCheckConsistencyProto(CONSISTENCY_TOKEN);

    assertThat(checkConsistencyRequest.getName()).isEqualTo(TABLE_NAME);
    assertThat(checkConsistencyRequest.getConsistencyToken()).isEqualTo(CONSISTENCY_TOKEN);
    assertThat(checkConsistencyRequest.getModeCase())
        .isEqualTo(CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES);
  }

  @Test
  public void testToCheckConsistencyProtoWithDataBoost() {
    ConsistencyRequest consistencyRequest = ConsistencyRequest.forDataBoost(TABLE_NAME);

    CheckConsistencyRequest checkConsistencyRequest =
        consistencyRequest.toCheckConsistencyProto(CONSISTENCY_TOKEN);

    assertThat(checkConsistencyRequest.getName()).isEqualTo(TABLE_NAME);
    assertThat(checkConsistencyRequest.getConsistencyToken()).isEqualTo(CONSISTENCY_TOKEN);
    assertThat(checkConsistencyRequest.getModeCase())
        .isEqualTo(CheckConsistencyRequest.ModeCase.DATA_BOOST_READ_LOCAL_WRITES);
  }

  @Test
  public void testToGenerateTokenProto() {
    ConsistencyRequest consistencyRequest = ConsistencyRequest.forDataBoost(TABLE_NAME);

    GenerateConsistencyTokenRequest generateRequest =
        consistencyRequest.toGenerateTokenProto();

    assertThat(generateRequest.getName()).isEqualTo(TABLE_NAME);
  }

  @Test
  public void testToCheckConsistencyProtoWithToken() {
    ConsistencyRequest consistencyRequest =
        ConsistencyRequest.forReplication(TABLE_NAME, CONSISTENCY_TOKEN);

    CheckConsistencyRequest checkConsistencyRequest =
        consistencyRequest.toCheckConsistencyProto(CONSISTENCY_TOKEN);

    assertThat(checkConsistencyRequest.getName()).isEqualTo(TABLE_NAME);
    assertThat(checkConsistencyRequest.getConsistencyToken()).isEqualTo(CONSISTENCY_TOKEN);
    assertThat(checkConsistencyRequest.getModeCase())
        .isEqualTo(CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES);
  }
}
