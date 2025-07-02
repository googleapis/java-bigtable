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

import com.google.api.core.InternalApi;
import com.google.bigtable.admin.v2.SchemaBundleName;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import javax.annotation.Nonnull;

/**
 * A class that wraps the {@link com.google.bigtable.admin.v2.SchemaBundle} protocol buffer object.
 *
 * <p>An AuthorizedView represents subsets of a particular table based on rules. The access to each
 * AuthorizedView can be configured separately from the Table.
 *
 * <p>Users can perform read/write operation on an AuthorizedView by providing an authorizedView id
 * besides a table id, in which case the semantics remain identical as reading/writing on a Table
 * except that visibility is restricted to the subset of the Table that the AuthorizedView
 * represents.
 */
public final class SchemaBundle {
  private final com.google.bigtable.admin.v2.SchemaBundle proto;

  /**
   * Wraps the protobuf. This method is considered an internal implementation detail and not meant
   * to be used by applications.
   */
  @InternalApi
  public static SchemaBundle fromProto(@Nonnull com.google.bigtable.admin.v2.SchemaBundle proto) {
    return new SchemaBundle(proto);
  }

  private SchemaBundle(@Nonnull com.google.bigtable.admin.v2.SchemaBundle proto) {
    Preconditions.checkNotNull(proto);
    Preconditions.checkArgument(!proto.getName().isEmpty(), "SchemaBundle must have a name");
    Preconditions.checkArgument(
        proto.hasProtoSchema(), "Schemabundle must have a proto_schema field");
    this.proto = proto;
  }

  /** Gets the schema bundle's id. */
  public String getId() {
    // Constructor ensures that name is not null.
    SchemaBundleName fullName = SchemaBundleName.parse(proto.getName());

    //noinspection ConstantConditions
    return fullName.getSchemaBundle();
  }

  /** Gets the id of the table that owns this schema bundle. */
  public String getTableId() {
    // Constructor ensures that name is not null.
    SchemaBundleName fullName = SchemaBundleName.parse(proto.getName());

    //noinspection ConstantConditions
    return fullName.getTable();
  }

  /** Gets the proto schema of this schema bundle. */
  public com.google.protobuf.ByteString getProtoSchema() {
    if (proto.hasProtoSchema()) {
      return proto.getProtoSchema().getProtoDescriptors();
    }
    throw new IllegalStateException("This SchemaBundle doesn't have a valid type specified");
  }

  /**
   * Creates the request protobuf. This method is considered an internal implementation detail and
   * not meant to be used by applications.
   */
  @InternalApi
  public com.google.bigtable.admin.v2.SchemaBundle toProto() {
    return proto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SchemaBundle that = (SchemaBundle) o;
    return Objects.equal(proto, that.proto);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(proto);
  }
}
