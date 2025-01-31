/*
 * Copyright 2025 Google LLC
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

import static com.google.common.truth.Truth.assertThat;

import java.util.HashMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PrepareQueryRequestTest {

  @Test
  public void testProtoConversion() {
    // TODO test param conversion when supported
    PrepareQueryRequest request =
        PrepareQueryRequest.create("SELECT * FROM table", new HashMap<>());
    RequestContext rc = RequestContext.create("project", "instance", "profile");
    com.google.bigtable.v2.PrepareQueryRequest proto = request.toProto(rc);

    assertThat(proto.getQuery()).isEqualTo("SELECT * FROM table");
    assertThat(proto.getAppProfileId()).isEqualTo("profile");
    assertThat(proto.getInstanceName())
        .isEqualTo(NameUtil.formatInstanceName("project", "instance"));
  }
}
