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
package com.google.cloud.kafka.connect.bigtable.mapping;

import java.util.Optional;
import org.apache.kafka.connect.data.ConnectSchema;
import org.apache.kafka.connect.data.Date;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LogicalTypeUtilsTest {
  @Test
  public void testSupportedType() {
    LogicalTypeUtils.logIfLogicalTypeUnsupported(Optional.of(Date.SCHEMA));
  }

  @Test
  public void testStruct() {
    LogicalTypeUtils.logIfLogicalTypeUnsupported(Optional.of(SchemaBuilder.struct().name("name")));
  }

  @Test
  public void testUnsupportedType() {
    Optional<Schema> schema =
        Optional.of(new ConnectSchema(Schema.Type.INT64, false, null, "name", 1, "doc"));
    LogicalTypeUtils.logIfLogicalTypeUnsupported(schema);
    LogicalTypeUtils.logIfLogicalTypeUnsupported(schema);
  }

  @Test
  public void testNoSchema() {
    LogicalTypeUtils.logIfLogicalTypeUnsupported(Optional.empty());
  }
}
