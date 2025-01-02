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
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: google/bigtable/admin/v2/bigtable_table_admin.proto
// Protobuf Java Version: 4.29.0

package com.google.bigtable.admin.v2;

/**
 *
 *
 * <pre>
 * Response message for
 * [google.bigtable.admin.v2.BigtableTableAdmin.ListTables][google.bigtable.admin.v2.BigtableTableAdmin.ListTables]
 * </pre>
 *
 * Protobuf type {@code google.bigtable.admin.v2.ListTablesResponse}
 */
public final class ListTablesResponse extends com.google.protobuf.GeneratedMessage
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.admin.v2.ListTablesResponse)
    ListTablesResponseOrBuilder {
  private static final long serialVersionUID = 0L;

  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
        com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
        /* major= */ 4,
        /* minor= */ 29,
        /* patch= */ 0,
        /* suffix= */ "",
        ListTablesResponse.class.getName());
  }
  // Use ListTablesResponse.newBuilder() to construct.
  private ListTablesResponse(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }

  private ListTablesResponse() {
    tables_ = java.util.Collections.emptyList();
    nextPageToken_ = "";
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.admin.v2.BigtableTableAdminProto
        .internal_static_google_bigtable_admin_v2_ListTablesResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.admin.v2.BigtableTableAdminProto
        .internal_static_google_bigtable_admin_v2_ListTablesResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.admin.v2.ListTablesResponse.class,
            com.google.bigtable.admin.v2.ListTablesResponse.Builder.class);
  }

  public static final int TABLES_FIELD_NUMBER = 1;

  @SuppressWarnings("serial")
  private java.util.List<com.google.bigtable.admin.v2.Table> tables_;
  /**
   *
   *
   * <pre>
   * The tables present in the requested instance.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
   */
  @java.lang.Override
  public java.util.List<com.google.bigtable.admin.v2.Table> getTablesList() {
    return tables_;
  }
  /**
   *
   *
   * <pre>
   * The tables present in the requested instance.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.google.bigtable.admin.v2.TableOrBuilder>
      getTablesOrBuilderList() {
    return tables_;
  }
  /**
   *
   *
   * <pre>
   * The tables present in the requested instance.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
   */
  @java.lang.Override
  public int getTablesCount() {
    return tables_.size();
  }
  /**
   *
   *
   * <pre>
   * The tables present in the requested instance.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.admin.v2.Table getTables(int index) {
    return tables_.get(index);
  }
  /**
   *
   *
   * <pre>
   * The tables present in the requested instance.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.admin.v2.TableOrBuilder getTablesOrBuilder(int index) {
    return tables_.get(index);
  }

  public static final int NEXT_PAGE_TOKEN_FIELD_NUMBER = 2;

  @SuppressWarnings("serial")
  private volatile java.lang.Object nextPageToken_ = "";
  /**
   *
   *
   * <pre>
   * Set if not all tables could be returned in a single response.
   * Pass this value to `page_token` in another request to get the next
   * page of results.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   *
   * @return The nextPageToken.
   */
  @java.lang.Override
  public java.lang.String getNextPageToken() {
    java.lang.Object ref = nextPageToken_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      nextPageToken_ = s;
      return s;
    }
  }
  /**
   *
   *
   * <pre>
   * Set if not all tables could be returned in a single response.
   * Pass this value to `page_token` in another request to get the next
   * page of results.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   *
   * @return The bytes for nextPageToken.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getNextPageTokenBytes() {
    java.lang.Object ref = nextPageToken_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
      nextPageToken_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  private byte memoizedIsInitialized = -1;

  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
    for (int i = 0; i < tables_.size(); i++) {
      output.writeMessage(1, tables_.get(i));
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(nextPageToken_)) {
      com.google.protobuf.GeneratedMessage.writeString(output, 2, nextPageToken_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < tables_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, tables_.get(i));
    }
    if (!com.google.protobuf.GeneratedMessage.isStringEmpty(nextPageToken_)) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(2, nextPageToken_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof com.google.bigtable.admin.v2.ListTablesResponse)) {
      return super.equals(obj);
    }
    com.google.bigtable.admin.v2.ListTablesResponse other =
        (com.google.bigtable.admin.v2.ListTablesResponse) obj;

    if (!getTablesList().equals(other.getTablesList())) return false;
    if (!getNextPageToken().equals(other.getNextPageToken())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getTablesCount() > 0) {
      hash = (37 * hash) + TABLES_FIELD_NUMBER;
      hash = (53 * hash) + getTablesList().hashCode();
    }
    hash = (37 * hash) + NEXT_PAGE_TOKEN_FIELD_NUMBER;
    hash = (53 * hash) + getNextPageToken().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() {
    return newBuilder();
  }

  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }

  public static Builder newBuilder(com.google.bigtable.admin.v2.ListTablesResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }

  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   *
   *
   * <pre>
   * Response message for
   * [google.bigtable.admin.v2.BigtableTableAdmin.ListTables][google.bigtable.admin.v2.BigtableTableAdmin.ListTables]
   * </pre>
   *
   * Protobuf type {@code google.bigtable.admin.v2.ListTablesResponse}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.admin.v2.ListTablesResponse)
      com.google.bigtable.admin.v2.ListTablesResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.admin.v2.BigtableTableAdminProto
          .internal_static_google_bigtable_admin_v2_ListTablesResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.admin.v2.BigtableTableAdminProto
          .internal_static_google_bigtable_admin_v2_ListTablesResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.admin.v2.ListTablesResponse.class,
              com.google.bigtable.admin.v2.ListTablesResponse.Builder.class);
    }

    // Construct using com.google.bigtable.admin.v2.ListTablesResponse.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      if (tablesBuilder_ == null) {
        tables_ = java.util.Collections.emptyList();
      } else {
        tables_ = null;
        tablesBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000001);
      nextPageToken_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.admin.v2.BigtableTableAdminProto
          .internal_static_google_bigtable_admin_v2_ListTablesResponse_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.ListTablesResponse getDefaultInstanceForType() {
      return com.google.bigtable.admin.v2.ListTablesResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.ListTablesResponse build() {
      com.google.bigtable.admin.v2.ListTablesResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.ListTablesResponse buildPartial() {
      com.google.bigtable.admin.v2.ListTablesResponse result =
          new com.google.bigtable.admin.v2.ListTablesResponse(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(
        com.google.bigtable.admin.v2.ListTablesResponse result) {
      if (tablesBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          tables_ = java.util.Collections.unmodifiableList(tables_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.tables_ = tables_;
      } else {
        result.tables_ = tablesBuilder_.build();
      }
    }

    private void buildPartial0(com.google.bigtable.admin.v2.ListTablesResponse result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.nextPageToken_ = nextPageToken_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.google.bigtable.admin.v2.ListTablesResponse) {
        return mergeFrom((com.google.bigtable.admin.v2.ListTablesResponse) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.admin.v2.ListTablesResponse other) {
      if (other == com.google.bigtable.admin.v2.ListTablesResponse.getDefaultInstance())
        return this;
      if (tablesBuilder_ == null) {
        if (!other.tables_.isEmpty()) {
          if (tables_.isEmpty()) {
            tables_ = other.tables_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureTablesIsMutable();
            tables_.addAll(other.tables_);
          }
          onChanged();
        }
      } else {
        if (!other.tables_.isEmpty()) {
          if (tablesBuilder_.isEmpty()) {
            tablesBuilder_.dispose();
            tablesBuilder_ = null;
            tables_ = other.tables_;
            bitField0_ = (bitField0_ & ~0x00000001);
            tablesBuilder_ =
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders
                    ? getTablesFieldBuilder()
                    : null;
          } else {
            tablesBuilder_.addAllMessages(other.tables_);
          }
        }
      }
      if (!other.getNextPageToken().isEmpty()) {
        nextPageToken_ = other.nextPageToken_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 10:
              {
                com.google.bigtable.admin.v2.Table m =
                    input.readMessage(
                        com.google.bigtable.admin.v2.Table.parser(), extensionRegistry);
                if (tablesBuilder_ == null) {
                  ensureTablesIsMutable();
                  tables_.add(m);
                } else {
                  tablesBuilder_.addMessage(m);
                }
                break;
              } // case 10
            case 18:
              {
                nextPageToken_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000002;
                break;
              } // case 18
            default:
              {
                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                  done = true; // was an endgroup tag
                }
                break;
              } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }

    private int bitField0_;

    private java.util.List<com.google.bigtable.admin.v2.Table> tables_ =
        java.util.Collections.emptyList();

    private void ensureTablesIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        tables_ = new java.util.ArrayList<com.google.bigtable.admin.v2.Table>(tables_);
        bitField0_ |= 0x00000001;
      }
    }

    private com.google.protobuf.RepeatedFieldBuilder<
            com.google.bigtable.admin.v2.Table,
            com.google.bigtable.admin.v2.Table.Builder,
            com.google.bigtable.admin.v2.TableOrBuilder>
        tablesBuilder_;

    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public java.util.List<com.google.bigtable.admin.v2.Table> getTablesList() {
      if (tablesBuilder_ == null) {
        return java.util.Collections.unmodifiableList(tables_);
      } else {
        return tablesBuilder_.getMessageList();
      }
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public int getTablesCount() {
      if (tablesBuilder_ == null) {
        return tables_.size();
      } else {
        return tablesBuilder_.getCount();
      }
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public com.google.bigtable.admin.v2.Table getTables(int index) {
      if (tablesBuilder_ == null) {
        return tables_.get(index);
      } else {
        return tablesBuilder_.getMessage(index);
      }
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public Builder setTables(int index, com.google.bigtable.admin.v2.Table value) {
      if (tablesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureTablesIsMutable();
        tables_.set(index, value);
        onChanged();
      } else {
        tablesBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public Builder setTables(
        int index, com.google.bigtable.admin.v2.Table.Builder builderForValue) {
      if (tablesBuilder_ == null) {
        ensureTablesIsMutable();
        tables_.set(index, builderForValue.build());
        onChanged();
      } else {
        tablesBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public Builder addTables(com.google.bigtable.admin.v2.Table value) {
      if (tablesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureTablesIsMutable();
        tables_.add(value);
        onChanged();
      } else {
        tablesBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public Builder addTables(int index, com.google.bigtable.admin.v2.Table value) {
      if (tablesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureTablesIsMutable();
        tables_.add(index, value);
        onChanged();
      } else {
        tablesBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public Builder addTables(com.google.bigtable.admin.v2.Table.Builder builderForValue) {
      if (tablesBuilder_ == null) {
        ensureTablesIsMutable();
        tables_.add(builderForValue.build());
        onChanged();
      } else {
        tablesBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public Builder addTables(
        int index, com.google.bigtable.admin.v2.Table.Builder builderForValue) {
      if (tablesBuilder_ == null) {
        ensureTablesIsMutable();
        tables_.add(index, builderForValue.build());
        onChanged();
      } else {
        tablesBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public Builder addAllTables(
        java.lang.Iterable<? extends com.google.bigtable.admin.v2.Table> values) {
      if (tablesBuilder_ == null) {
        ensureTablesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(values, tables_);
        onChanged();
      } else {
        tablesBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public Builder clearTables() {
      if (tablesBuilder_ == null) {
        tables_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        tablesBuilder_.clear();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public Builder removeTables(int index) {
      if (tablesBuilder_ == null) {
        ensureTablesIsMutable();
        tables_.remove(index);
        onChanged();
      } else {
        tablesBuilder_.remove(index);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public com.google.bigtable.admin.v2.Table.Builder getTablesBuilder(int index) {
      return getTablesFieldBuilder().getBuilder(index);
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public com.google.bigtable.admin.v2.TableOrBuilder getTablesOrBuilder(int index) {
      if (tablesBuilder_ == null) {
        return tables_.get(index);
      } else {
        return tablesBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public java.util.List<? extends com.google.bigtable.admin.v2.TableOrBuilder>
        getTablesOrBuilderList() {
      if (tablesBuilder_ != null) {
        return tablesBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(tables_);
      }
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public com.google.bigtable.admin.v2.Table.Builder addTablesBuilder() {
      return getTablesFieldBuilder()
          .addBuilder(com.google.bigtable.admin.v2.Table.getDefaultInstance());
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public com.google.bigtable.admin.v2.Table.Builder addTablesBuilder(int index) {
      return getTablesFieldBuilder()
          .addBuilder(index, com.google.bigtable.admin.v2.Table.getDefaultInstance());
    }
    /**
     *
     *
     * <pre>
     * The tables present in the requested instance.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.Table tables = 1;</code>
     */
    public java.util.List<com.google.bigtable.admin.v2.Table.Builder> getTablesBuilderList() {
      return getTablesFieldBuilder().getBuilderList();
    }

    private com.google.protobuf.RepeatedFieldBuilder<
            com.google.bigtable.admin.v2.Table,
            com.google.bigtable.admin.v2.Table.Builder,
            com.google.bigtable.admin.v2.TableOrBuilder>
        getTablesFieldBuilder() {
      if (tablesBuilder_ == null) {
        tablesBuilder_ =
            new com.google.protobuf.RepeatedFieldBuilder<
                com.google.bigtable.admin.v2.Table,
                com.google.bigtable.admin.v2.Table.Builder,
                com.google.bigtable.admin.v2.TableOrBuilder>(
                tables_, ((bitField0_ & 0x00000001) != 0), getParentForChildren(), isClean());
        tables_ = null;
      }
      return tablesBuilder_;
    }

    private java.lang.Object nextPageToken_ = "";
    /**
     *
     *
     * <pre>
     * Set if not all tables could be returned in a single response.
     * Pass this value to `page_token` in another request to get the next
     * page of results.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @return The nextPageToken.
     */
    public java.lang.String getNextPageToken() {
      java.lang.Object ref = nextPageToken_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nextPageToken_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * Set if not all tables could be returned in a single response.
     * Pass this value to `page_token` in another request to get the next
     * page of results.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @return The bytes for nextPageToken.
     */
    public com.google.protobuf.ByteString getNextPageTokenBytes() {
      java.lang.Object ref = nextPageToken_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
        nextPageToken_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * Set if not all tables could be returned in a single response.
     * Pass this value to `page_token` in another request to get the next
     * page of results.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @param value The nextPageToken to set.
     * @return This builder for chaining.
     */
    public Builder setNextPageToken(java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }
      nextPageToken_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Set if not all tables could be returned in a single response.
     * Pass this value to `page_token` in another request to get the next
     * page of results.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearNextPageToken() {
      nextPageToken_ = getDefaultInstance().getNextPageToken();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Set if not all tables could be returned in a single response.
     * Pass this value to `page_token` in another request to get the next
     * page of results.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @param value The bytes for nextPageToken to set.
     * @return This builder for chaining.
     */
    public Builder setNextPageTokenBytes(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      checkByteStringIsUtf8(value);
      nextPageToken_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:google.bigtable.admin.v2.ListTablesResponse)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.admin.v2.ListTablesResponse)
  private static final com.google.bigtable.admin.v2.ListTablesResponse DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.admin.v2.ListTablesResponse();
  }

  public static com.google.bigtable.admin.v2.ListTablesResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListTablesResponse> PARSER =
      new com.google.protobuf.AbstractParser<ListTablesResponse>() {
        @java.lang.Override
        public ListTablesResponse parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          Builder builder = newBuilder();
          try {
            builder.mergeFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(builder.buildPartial());
          } catch (com.google.protobuf.UninitializedMessageException e) {
            throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
          } catch (java.io.IOException e) {
            throw new com.google.protobuf.InvalidProtocolBufferException(e)
                .setUnfinishedMessage(builder.buildPartial());
          }
          return builder.buildPartial();
        }
      };

  public static com.google.protobuf.Parser<ListTablesResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ListTablesResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.admin.v2.ListTablesResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
