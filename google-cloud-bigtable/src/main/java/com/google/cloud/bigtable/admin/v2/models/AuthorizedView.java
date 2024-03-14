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
import com.google.bigtable.admin.v2.AuthorizedViewName;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.ByteString;
import java.util.Map.Entry;
import javax.annotation.Nonnull;

/**
 * A class that wraps the {@link com.google.bigtable.admin.v2.AuthorizedView} protocol buffer
 * object.
 *
 * <p>An AuthorizedView represents subsets of a particular table based on rules. The access to each
 * AuthorizedView can be configured separately from the Table.
 *
 * <p>Users can perform read/write operation on an AuthorizedView by providing an authorizedView id
 * besides a table id, in which case the semantics remain identical as reading/writing on a Table
 * except that visibility is restricted to the subset of the Table that the AuthorizedView
 * represents.
 */
public final class AuthorizedView {
  private final com.google.bigtable.admin.v2.AuthorizedView proto;

  /**
   * Wraps the protobuf. This method is considered an internal implementation detail and not meant
   * to be used by applications.
   */
  @InternalApi
  public static AuthorizedView fromProto(
      @Nonnull com.google.bigtable.admin.v2.AuthorizedView proto) {
    return new AuthorizedView(proto);
  }

  private AuthorizedView(@Nonnull com.google.bigtable.admin.v2.AuthorizedView proto) {
    Preconditions.checkNotNull(proto);
    Preconditions.checkArgument(!proto.getName().isEmpty(), "AuthorizedView must have a name");
    Preconditions.checkArgument(
        proto.hasSubsetView(), "AuthorizedView must have a subset_view field");
    this.proto = proto;
  }

  /** Gets the authorized view's id. */
  public String getId() {
    // Constructor ensures that name is not null.
    AuthorizedViewName fullName = AuthorizedViewName.parse(proto.getName());

    //noinspection ConstantConditions
    return fullName.getAuthorizedView();
  }

  /** Gets the id of the table that owns this authorized view. */
  public String getTableId() {
    // Constructor ensures that name is not null.
    AuthorizedViewName fullName = AuthorizedViewName.parse(proto.getName());

    //noinspection ConstantConditions
    return fullName.getTable();
  }

  /** Returns whether this authorized view is deletion protected. */
  public boolean isDeletionProtected() {
    return proto.getDeletionProtection();
  }

  /** Gets the type of this authorized view, which currently can only be a subset view. */
  public AuthorizedViewType getAuthorizedViewType() {
    if (proto.hasSubsetView()) {
      return SubsetView.fromProto(proto.getSubsetView());
    } else {
      // Should never happen because the constructor verifies that one must exist.
      throw new IllegalStateException("This AuthorizedView doesn't have a valid type specified");
    }
  }

  /**
   * Creates the request protobuf. This method is considered an internal implementation detail and
   * not meant to be used by applications.
   */
  @InternalApi
  public com.google.bigtable.admin.v2.AuthorizedView toProto() {
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
    AuthorizedView that = (AuthorizedView) o;
    return Objects.equal(proto, that.proto);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(proto);
  }

  /**
   * Represents a subset of a Table. Please check the implementations of this interface for more
   * details.
   */
  public interface AuthorizedViewType {}

  /**
   * Defines a simple authorized view that is a subset of the underlying Table.
   *
   * <p>Users can specify the rows in the form of row key prefixes, and specify the column families
   * by adding the family id along with its familySubsets rule to the family subsets map. The subset
   * is defined by the intersection of the specified row key prefixes and column family subsets.
   */
  public static class SubsetView implements AuthorizedViewType {
    private final com.google.bigtable.admin.v2.AuthorizedView.SubsetView.Builder builder;

    /**
     * Wraps the protobuf. This method is considered an internal implementation detail and not meant
     * to be used by applications.
     */
    @InternalApi
    public static SubsetView fromProto(
        @Nonnull com.google.bigtable.admin.v2.AuthorizedView.SubsetView proto) {
      return new SubsetView(proto);
    }

    private SubsetView(@Nonnull com.google.bigtable.admin.v2.AuthorizedView.SubsetView proto) {
      this.builder = proto.toBuilder();
    }

    public SubsetView() {
      this.builder = com.google.bigtable.admin.v2.AuthorizedView.SubsetView.newBuilder();
    }

    /** Gets the row prefixes to be included in this subset view. */
    public ImmutableList<ByteString> getRowPrefixes() {
      return ImmutableList.copyOf(this.builder.getRowPrefixesList());
    }

    /** Gets the map from familyId to familySubsets in this subset view. */
    public ImmutableMap<String, FamilySubsets> getFamilySubsets() {
      ImmutableMap.Builder<String, FamilySubsets> familySubsets = ImmutableMap.builder();
      for (Entry<String, com.google.bigtable.admin.v2.AuthorizedView.FamilySubsets> entry :
          builder.getFamilySubsetsMap().entrySet()) {
        familySubsets.put(entry.getKey(), FamilySubsets.fromProto(entry.getValue()));
      }
      return familySubsets.build();
    }

    /** Adds a new rowPrefix to the subset view. */
    public SubsetView addRowPrefix(ByteString rowPrefix) {
      this.builder.addRowPrefixes(rowPrefix);
      return this;
    }

    /** Adds a new rowPrefix to the subset view. */
    public SubsetView addRowPrefix(String rowPrefix) {
      this.builder.addRowPrefixes(ByteString.copyFromUtf8(rowPrefix));
      return this;
    }

    /**
     * Adds a new familyId with its familySubsets to the subset view. Please note that calling this
     * method with the same familyId will overwrite the previous rule set on the family.
     */
    public SubsetView addFamilySubsets(String familyId, FamilySubsets familySubsets) {
      this.builder.putFamilySubsets(familyId, familySubsets.toProto());
      return this;
    }

    /**
     * Creates the request protobuf. This method is considered an internal implementation detail and
     * not meant to be used by applications.
     */
    @InternalApi
    public com.google.bigtable.admin.v2.AuthorizedView.SubsetView toProto() {
      return builder.build();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      SubsetView that = (SubsetView) o;
      return Objects.equal(builder.build(), that.builder.build());
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(builder.build());
    }
  }

  /** Represents subsets of a particular column family that are included in this authorized view. */
  public static final class FamilySubsets {
    private final com.google.bigtable.admin.v2.AuthorizedView.FamilySubsets.Builder builder;

    /**
     * Wraps the protobuf. This method is considered an internal implementation detail and not meant
     * to be used by applications.
     */
    @InternalApi
    public static FamilySubsets fromProto(
        @Nonnull com.google.bigtable.admin.v2.AuthorizedView.FamilySubsets proto) {
      return new FamilySubsets(proto);
    }

    private FamilySubsets(
        @Nonnull com.google.bigtable.admin.v2.AuthorizedView.FamilySubsets proto) {
      this.builder = proto.toBuilder();
    }

    public FamilySubsets() {
      this.builder = com.google.bigtable.admin.v2.AuthorizedView.FamilySubsets.newBuilder();
    }

    /** Gets the list of column qualifiers included in this authorized view. */
    public ImmutableList<ByteString> getQualifiers() {
      return ImmutableList.copyOf(this.builder.getQualifiersList());
    }

    /** Gets the list of column qualifier prefixes included in this authorized view. */
    public ImmutableList<ByteString> getQualifierPrefixes() {
      return ImmutableList.copyOf(this.builder.getQualifierPrefixesList());
    }

    /** Adds an individual column qualifier to be included in this authorized view. */
    public FamilySubsets addQualifier(ByteString qualifier) {
      this.builder.addQualifiers(qualifier);
      return this;
    }

    /** Adds an individual column qualifier to be included in this authorized view. */
    public FamilySubsets addQualifier(String qualifier) {
      this.builder.addQualifiers(ByteString.copyFromUtf8(qualifier));
      return this;
    }

    /**
     * Adds a prefix for column qualifiers to be included in this authorized view. Every qualifier
     * starting with the prefix will be included in this authorized view. An empty string ("")
     * prefix means to provide access to all qualifiers.
     */
    public FamilySubsets addQualifierPrefix(ByteString qualifierPrefix) {
      this.builder.addQualifierPrefixes(qualifierPrefix);
      return this;
    }

    /**
     * Adds a prefix for column qualifiers to be included in this authorized view. Every qualifier
     * starting with the prefix will be included in this authorized view. An empty string ("")
     * prefix means to provide access to all qualifiers.
     */
    public FamilySubsets addQualifierPrefix(String qualifierPrefix) {
      this.builder.addQualifierPrefixes(ByteString.copyFromUtf8(qualifierPrefix));
      return this;
    }

    /**
     * Creates the request protobuf. This method is considered an internal implementation detail and
     * not meant to be used by applications.
     */
    @InternalApi
    public com.google.bigtable.admin.v2.AuthorizedView.FamilySubsets toProto() {
      return builder.build();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      FamilySubsets that = (FamilySubsets) o;
      return Objects.equal(builder.build(), that.builder.build());
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(builder.build());
    }
  }
}
