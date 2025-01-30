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
package com.google.cloud.kafka.connect.bigtable.util;

import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

public class NestedNullStructFactory {
  public static final String NESTED_NULL_STRUCT_FIELD_NAME = "struct";
  public static final ByteString NESTED_NULL_STRUCT_FIELD_NAME_BYTES =
      ByteString.copyFrom(NESTED_NULL_STRUCT_FIELD_NAME.getBytes(StandardCharsets.UTF_8));

  public static Struct getStructhWithNullOnNthNestingLevel(int n) {
    assert n > 0;

    Schema schema =
        SchemaBuilder.struct()
            .field(NESTED_NULL_STRUCT_FIELD_NAME, SchemaBuilder.struct().optional())
            .build();
    // We consider a Struct with a null child to be a level 1 nested struct.
    Struct struct = new Struct(schema);

    while (n > 1) {
      n -= 1;
      schema =
          SchemaBuilder.struct().field(NESTED_NULL_STRUCT_FIELD_NAME, schema).optional().build();
      final Struct outerStruct = new Struct(schema);
      outerStruct.put(NESTED_NULL_STRUCT_FIELD_NAME, struct);
      struct = outerStruct;
    }
    return struct;
  }
}
