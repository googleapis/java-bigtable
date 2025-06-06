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
// source: google/bigtable/v2/data.proto

// Protobuf Java Version: 3.25.8
package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * ResultSet schema in proto format
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.ProtoSchema}
 */
public final class ProtoSchema extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.ProtoSchema)
    ProtoSchemaOrBuilder {
  private static final long serialVersionUID = 0L;

  // Use ProtoSchema.newBuilder() to construct.
  private ProtoSchema(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private ProtoSchema() {
    columns_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new ProtoSchema();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_ProtoSchema_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_ProtoSchema_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.ProtoSchema.class,
            com.google.bigtable.v2.ProtoSchema.Builder.class);
  }

  public static final int COLUMNS_FIELD_NUMBER = 1;

  @SuppressWarnings("serial")
  private java.util.List<com.google.bigtable.v2.ColumnMetadata> columns_;

  /**
   *
   *
   * <pre>
   * The columns in the result set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
   */
  @java.lang.Override
  public java.util.List<com.google.bigtable.v2.ColumnMetadata> getColumnsList() {
    return columns_;
  }

  /**
   *
   *
   * <pre>
   * The columns in the result set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.google.bigtable.v2.ColumnMetadataOrBuilder>
      getColumnsOrBuilderList() {
    return columns_;
  }

  /**
   *
   *
   * <pre>
   * The columns in the result set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
   */
  @java.lang.Override
  public int getColumnsCount() {
    return columns_.size();
  }

  /**
   *
   *
   * <pre>
   * The columns in the result set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.ColumnMetadata getColumns(int index) {
    return columns_.get(index);
  }

  /**
   *
   *
   * <pre>
   * The columns in the result set.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.ColumnMetadataOrBuilder getColumnsOrBuilder(int index) {
    return columns_.get(index);
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
    for (int i = 0; i < columns_.size(); i++) {
      output.writeMessage(1, columns_.get(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < columns_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, columns_.get(i));
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
    if (!(obj instanceof com.google.bigtable.v2.ProtoSchema)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.ProtoSchema other = (com.google.bigtable.v2.ProtoSchema) obj;

    if (!getColumnsList().equals(other.getColumnsList())) return false;
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
    if (getColumnsCount() > 0) {
      hash = (37 * hash) + COLUMNS_FIELD_NUMBER;
      hash = (53 * hash) + getColumnsList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.ProtoSchema parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.ProtoSchema parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.ProtoSchema parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() {
    return newBuilder();
  }

  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }

  public static Builder newBuilder(com.google.bigtable.v2.ProtoSchema prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }

  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }

  /**
   *
   *
   * <pre>
   * ResultSet schema in proto format
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.ProtoSchema}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.ProtoSchema)
      com.google.bigtable.v2.ProtoSchemaOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_ProtoSchema_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_ProtoSchema_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.ProtoSchema.class,
              com.google.bigtable.v2.ProtoSchema.Builder.class);
    }

    // Construct using com.google.bigtable.v2.ProtoSchema.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      if (columnsBuilder_ == null) {
        columns_ = java.util.Collections.emptyList();
      } else {
        columns_ = null;
        columnsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000001);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_ProtoSchema_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.ProtoSchema getDefaultInstanceForType() {
      return com.google.bigtable.v2.ProtoSchema.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.ProtoSchema build() {
      com.google.bigtable.v2.ProtoSchema result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.ProtoSchema buildPartial() {
      com.google.bigtable.v2.ProtoSchema result = new com.google.bigtable.v2.ProtoSchema(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(com.google.bigtable.v2.ProtoSchema result) {
      if (columnsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          columns_ = java.util.Collections.unmodifiableList(columns_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.columns_ = columns_;
      } else {
        result.columns_ = columnsBuilder_.build();
      }
    }

    private void buildPartial0(com.google.bigtable.v2.ProtoSchema result) {
      int from_bitField0_ = bitField0_;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }

    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
      return super.setField(field, value);
    }

    @java.lang.Override
    public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }

    @java.lang.Override
    public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }

    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field, int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }

    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.google.bigtable.v2.ProtoSchema) {
        return mergeFrom((com.google.bigtable.v2.ProtoSchema) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.ProtoSchema other) {
      if (other == com.google.bigtable.v2.ProtoSchema.getDefaultInstance()) return this;
      if (columnsBuilder_ == null) {
        if (!other.columns_.isEmpty()) {
          if (columns_.isEmpty()) {
            columns_ = other.columns_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureColumnsIsMutable();
            columns_.addAll(other.columns_);
          }
          onChanged();
        }
      } else {
        if (!other.columns_.isEmpty()) {
          if (columnsBuilder_.isEmpty()) {
            columnsBuilder_.dispose();
            columnsBuilder_ = null;
            columns_ = other.columns_;
            bitField0_ = (bitField0_ & ~0x00000001);
            columnsBuilder_ =
                com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders
                    ? getColumnsFieldBuilder()
                    : null;
          } else {
            columnsBuilder_.addAllMessages(other.columns_);
          }
        }
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
                com.google.bigtable.v2.ColumnMetadata m =
                    input.readMessage(
                        com.google.bigtable.v2.ColumnMetadata.parser(), extensionRegistry);
                if (columnsBuilder_ == null) {
                  ensureColumnsIsMutable();
                  columns_.add(m);
                } else {
                  columnsBuilder_.addMessage(m);
                }
                break;
              } // case 10
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

    private java.util.List<com.google.bigtable.v2.ColumnMetadata> columns_ =
        java.util.Collections.emptyList();

    private void ensureColumnsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        columns_ = new java.util.ArrayList<com.google.bigtable.v2.ColumnMetadata>(columns_);
        bitField0_ |= 0x00000001;
      }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
            com.google.bigtable.v2.ColumnMetadata,
            com.google.bigtable.v2.ColumnMetadata.Builder,
            com.google.bigtable.v2.ColumnMetadataOrBuilder>
        columnsBuilder_;

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public java.util.List<com.google.bigtable.v2.ColumnMetadata> getColumnsList() {
      if (columnsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(columns_);
      } else {
        return columnsBuilder_.getMessageList();
      }
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public int getColumnsCount() {
      if (columnsBuilder_ == null) {
        return columns_.size();
      } else {
        return columnsBuilder_.getCount();
      }
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public com.google.bigtable.v2.ColumnMetadata getColumns(int index) {
      if (columnsBuilder_ == null) {
        return columns_.get(index);
      } else {
        return columnsBuilder_.getMessage(index);
      }
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public Builder setColumns(int index, com.google.bigtable.v2.ColumnMetadata value) {
      if (columnsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureColumnsIsMutable();
        columns_.set(index, value);
        onChanged();
      } else {
        columnsBuilder_.setMessage(index, value);
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public Builder setColumns(
        int index, com.google.bigtable.v2.ColumnMetadata.Builder builderForValue) {
      if (columnsBuilder_ == null) {
        ensureColumnsIsMutable();
        columns_.set(index, builderForValue.build());
        onChanged();
      } else {
        columnsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public Builder addColumns(com.google.bigtable.v2.ColumnMetadata value) {
      if (columnsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureColumnsIsMutable();
        columns_.add(value);
        onChanged();
      } else {
        columnsBuilder_.addMessage(value);
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public Builder addColumns(int index, com.google.bigtable.v2.ColumnMetadata value) {
      if (columnsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureColumnsIsMutable();
        columns_.add(index, value);
        onChanged();
      } else {
        columnsBuilder_.addMessage(index, value);
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public Builder addColumns(com.google.bigtable.v2.ColumnMetadata.Builder builderForValue) {
      if (columnsBuilder_ == null) {
        ensureColumnsIsMutable();
        columns_.add(builderForValue.build());
        onChanged();
      } else {
        columnsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public Builder addColumns(
        int index, com.google.bigtable.v2.ColumnMetadata.Builder builderForValue) {
      if (columnsBuilder_ == null) {
        ensureColumnsIsMutable();
        columns_.add(index, builderForValue.build());
        onChanged();
      } else {
        columnsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public Builder addAllColumns(
        java.lang.Iterable<? extends com.google.bigtable.v2.ColumnMetadata> values) {
      if (columnsBuilder_ == null) {
        ensureColumnsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(values, columns_);
        onChanged();
      } else {
        columnsBuilder_.addAllMessages(values);
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public Builder clearColumns() {
      if (columnsBuilder_ == null) {
        columns_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        columnsBuilder_.clear();
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public Builder removeColumns(int index) {
      if (columnsBuilder_ == null) {
        ensureColumnsIsMutable();
        columns_.remove(index);
        onChanged();
      } else {
        columnsBuilder_.remove(index);
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public com.google.bigtable.v2.ColumnMetadata.Builder getColumnsBuilder(int index) {
      return getColumnsFieldBuilder().getBuilder(index);
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public com.google.bigtable.v2.ColumnMetadataOrBuilder getColumnsOrBuilder(int index) {
      if (columnsBuilder_ == null) {
        return columns_.get(index);
      } else {
        return columnsBuilder_.getMessageOrBuilder(index);
      }
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public java.util.List<? extends com.google.bigtable.v2.ColumnMetadataOrBuilder>
        getColumnsOrBuilderList() {
      if (columnsBuilder_ != null) {
        return columnsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(columns_);
      }
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public com.google.bigtable.v2.ColumnMetadata.Builder addColumnsBuilder() {
      return getColumnsFieldBuilder()
          .addBuilder(com.google.bigtable.v2.ColumnMetadata.getDefaultInstance());
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public com.google.bigtable.v2.ColumnMetadata.Builder addColumnsBuilder(int index) {
      return getColumnsFieldBuilder()
          .addBuilder(index, com.google.bigtable.v2.ColumnMetadata.getDefaultInstance());
    }

    /**
     *
     *
     * <pre>
     * The columns in the result set.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.ColumnMetadata columns = 1;</code>
     */
    public java.util.List<com.google.bigtable.v2.ColumnMetadata.Builder> getColumnsBuilderList() {
      return getColumnsFieldBuilder().getBuilderList();
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
            com.google.bigtable.v2.ColumnMetadata,
            com.google.bigtable.v2.ColumnMetadata.Builder,
            com.google.bigtable.v2.ColumnMetadataOrBuilder>
        getColumnsFieldBuilder() {
      if (columnsBuilder_ == null) {
        columnsBuilder_ =
            new com.google.protobuf.RepeatedFieldBuilderV3<
                com.google.bigtable.v2.ColumnMetadata,
                com.google.bigtable.v2.ColumnMetadata.Builder,
                com.google.bigtable.v2.ColumnMetadataOrBuilder>(
                columns_, ((bitField0_ & 0x00000001) != 0), getParentForChildren(), isClean());
        columns_ = null;
      }
      return columnsBuilder_;
    }

    @java.lang.Override
    public final Builder setUnknownFields(final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.ProtoSchema)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.ProtoSchema)
  private static final com.google.bigtable.v2.ProtoSchema DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.ProtoSchema();
  }

  public static com.google.bigtable.v2.ProtoSchema getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ProtoSchema> PARSER =
      new com.google.protobuf.AbstractParser<ProtoSchema>() {
        @java.lang.Override
        public ProtoSchema parsePartialFrom(
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

  public static com.google.protobuf.Parser<ProtoSchema> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ProtoSchema> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.ProtoSchema getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
