/*
 * Copyright 2023 Google LLC
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

package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * Specifies the complete (requested) contents of a single row of a table.
 * Rows which exceed 256MiB in size cannot be read in full.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.Row}
 */
public final class Row extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.Row)
    RowOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use Row.newBuilder() to construct.
  private Row(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private Row() {
    key_ = com.google.protobuf.ByteString.EMPTY;
    families_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new Row();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_Row_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_Row_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.Row.class, com.google.bigtable.v2.Row.Builder.class);
  }

  public static final int KEY_FIELD_NUMBER = 1;
  private com.google.protobuf.ByteString key_ = com.google.protobuf.ByteString.EMPTY;
  /**
   *
   *
   * <pre>
   * The unique key which identifies this row within its table. This is the same
   * key that's used to identify the row in, for example, a MutateRowRequest.
   * May contain any non-empty byte string up to 4KiB in length.
   * </pre>
   *
   * <code>bytes key = 1;</code>
   *
   * @return The key.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getKey() {
    return key_;
  }

  public static final int FAMILIES_FIELD_NUMBER = 2;

  @SuppressWarnings("serial")
  private java.util.List<com.google.bigtable.v2.Family> families_;
  /**
   *
   *
   * <pre>
   * May be empty, but only if the entire row is empty.
   * The mutual ordering of column families is not specified.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Family families = 2;</code>
   */
  @java.lang.Override
  public java.util.List<com.google.bigtable.v2.Family> getFamiliesList() {
    return families_;
  }
  /**
   *
   *
   * <pre>
   * May be empty, but only if the entire row is empty.
   * The mutual ordering of column families is not specified.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Family families = 2;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.google.bigtable.v2.FamilyOrBuilder>
      getFamiliesOrBuilderList() {
    return families_;
  }
  /**
   *
   *
   * <pre>
   * May be empty, but only if the entire row is empty.
   * The mutual ordering of column families is not specified.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Family families = 2;</code>
   */
  @java.lang.Override
  public int getFamiliesCount() {
    return families_.size();
  }
  /**
   *
   *
   * <pre>
   * May be empty, but only if the entire row is empty.
   * The mutual ordering of column families is not specified.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Family families = 2;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.Family getFamilies(int index) {
    return families_.get(index);
  }
  /**
   *
   *
   * <pre>
   * May be empty, but only if the entire row is empty.
   * The mutual ordering of column families is not specified.
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Family families = 2;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.FamilyOrBuilder getFamiliesOrBuilder(int index) {
    return families_.get(index);
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
    if (!key_.isEmpty()) {
      output.writeBytes(1, key_);
    }
    for (int i = 0; i < families_.size(); i++) {
      output.writeMessage(2, families_.get(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!key_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, key_);
    }
    for (int i = 0; i < families_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(2, families_.get(i));
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
    if (!(obj instanceof com.google.bigtable.v2.Row)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.Row other = (com.google.bigtable.v2.Row) obj;

    if (!getKey().equals(other.getKey())) return false;
    if (!getFamiliesList().equals(other.getFamiliesList())) return false;
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
    hash = (37 * hash) + KEY_FIELD_NUMBER;
    hash = (53 * hash) + getKey().hashCode();
    if (getFamiliesCount() > 0) {
      hash = (37 * hash) + FAMILIES_FIELD_NUMBER;
      hash = (53 * hash) + getFamiliesList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.Row parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.Row parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.Row parseFrom(com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.Row parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.Row parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.Row parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.Row parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.Row parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.Row parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.Row parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.Row parseFrom(com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.Row parseFrom(
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

  public static Builder newBuilder(com.google.bigtable.v2.Row prototype) {
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
   * Specifies the complete (requested) contents of a single row of a table.
   * Rows which exceed 256MiB in size cannot be read in full.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.Row}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.Row)
      com.google.bigtable.v2.RowOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_Row_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_Row_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.Row.class, com.google.bigtable.v2.Row.Builder.class);
    }

    // Construct using com.google.bigtable.v2.Row.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      key_ = com.google.protobuf.ByteString.EMPTY;
      if (familiesBuilder_ == null) {
        families_ = java.util.Collections.emptyList();
      } else {
        families_ = null;
        familiesBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000002);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_Row_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.Row getDefaultInstanceForType() {
      return com.google.bigtable.v2.Row.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.Row build() {
      com.google.bigtable.v2.Row result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.Row buildPartial() {
      com.google.bigtable.v2.Row result = new com.google.bigtable.v2.Row(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(com.google.bigtable.v2.Row result) {
      if (familiesBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0)) {
          families_ = java.util.Collections.unmodifiableList(families_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.families_ = families_;
      } else {
        result.families_ = familiesBuilder_.build();
      }
    }

    private void buildPartial0(com.google.bigtable.v2.Row result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.key_ = key_;
      }
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
      if (other instanceof com.google.bigtable.v2.Row) {
        return mergeFrom((com.google.bigtable.v2.Row) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.Row other) {
      if (other == com.google.bigtable.v2.Row.getDefaultInstance()) return this;
      if (other.getKey() != com.google.protobuf.ByteString.EMPTY) {
        setKey(other.getKey());
      }
      if (familiesBuilder_ == null) {
        if (!other.families_.isEmpty()) {
          if (families_.isEmpty()) {
            families_ = other.families_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureFamiliesIsMutable();
            families_.addAll(other.families_);
          }
          onChanged();
        }
      } else {
        if (!other.families_.isEmpty()) {
          if (familiesBuilder_.isEmpty()) {
            familiesBuilder_.dispose();
            familiesBuilder_ = null;
            families_ = other.families_;
            bitField0_ = (bitField0_ & ~0x00000002);
            familiesBuilder_ =
                com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders
                    ? getFamiliesFieldBuilder()
                    : null;
          } else {
            familiesBuilder_.addAllMessages(other.families_);
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
                key_ = input.readBytes();
                bitField0_ |= 0x00000001;
                break;
              } // case 10
            case 18:
              {
                com.google.bigtable.v2.Family m =
                    input.readMessage(com.google.bigtable.v2.Family.parser(), extensionRegistry);
                if (familiesBuilder_ == null) {
                  ensureFamiliesIsMutable();
                  families_.add(m);
                } else {
                  familiesBuilder_.addMessage(m);
                }
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

    private com.google.protobuf.ByteString key_ = com.google.protobuf.ByteString.EMPTY;
    /**
     *
     *
     * <pre>
     * The unique key which identifies this row within its table. This is the same
     * key that's used to identify the row in, for example, a MutateRowRequest.
     * May contain any non-empty byte string up to 4KiB in length.
     * </pre>
     *
     * <code>bytes key = 1;</code>
     *
     * @return The key.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getKey() {
      return key_;
    }
    /**
     *
     *
     * <pre>
     * The unique key which identifies this row within its table. This is the same
     * key that's used to identify the row in, for example, a MutateRowRequest.
     * May contain any non-empty byte string up to 4KiB in length.
     * </pre>
     *
     * <code>bytes key = 1;</code>
     *
     * @param value The key to set.
     * @return This builder for chaining.
     */
    public Builder setKey(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      key_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The unique key which identifies this row within its table. This is the same
     * key that's used to identify the row in, for example, a MutateRowRequest.
     * May contain any non-empty byte string up to 4KiB in length.
     * </pre>
     *
     * <code>bytes key = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearKey() {
      bitField0_ = (bitField0_ & ~0x00000001);
      key_ = getDefaultInstance().getKey();
      onChanged();
      return this;
    }

    private java.util.List<com.google.bigtable.v2.Family> families_ =
        java.util.Collections.emptyList();

    private void ensureFamiliesIsMutable() {
      if (!((bitField0_ & 0x00000002) != 0)) {
        families_ = new java.util.ArrayList<com.google.bigtable.v2.Family>(families_);
        bitField0_ |= 0x00000002;
      }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
            com.google.bigtable.v2.Family,
            com.google.bigtable.v2.Family.Builder,
            com.google.bigtable.v2.FamilyOrBuilder>
        familiesBuilder_;

    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public java.util.List<com.google.bigtable.v2.Family> getFamiliesList() {
      if (familiesBuilder_ == null) {
        return java.util.Collections.unmodifiableList(families_);
      } else {
        return familiesBuilder_.getMessageList();
      }
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public int getFamiliesCount() {
      if (familiesBuilder_ == null) {
        return families_.size();
      } else {
        return familiesBuilder_.getCount();
      }
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public com.google.bigtable.v2.Family getFamilies(int index) {
      if (familiesBuilder_ == null) {
        return families_.get(index);
      } else {
        return familiesBuilder_.getMessage(index);
      }
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public Builder setFamilies(int index, com.google.bigtable.v2.Family value) {
      if (familiesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureFamiliesIsMutable();
        families_.set(index, value);
        onChanged();
      } else {
        familiesBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public Builder setFamilies(int index, com.google.bigtable.v2.Family.Builder builderForValue) {
      if (familiesBuilder_ == null) {
        ensureFamiliesIsMutable();
        families_.set(index, builderForValue.build());
        onChanged();
      } else {
        familiesBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public Builder addFamilies(com.google.bigtable.v2.Family value) {
      if (familiesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureFamiliesIsMutable();
        families_.add(value);
        onChanged();
      } else {
        familiesBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public Builder addFamilies(int index, com.google.bigtable.v2.Family value) {
      if (familiesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureFamiliesIsMutable();
        families_.add(index, value);
        onChanged();
      } else {
        familiesBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public Builder addFamilies(com.google.bigtable.v2.Family.Builder builderForValue) {
      if (familiesBuilder_ == null) {
        ensureFamiliesIsMutable();
        families_.add(builderForValue.build());
        onChanged();
      } else {
        familiesBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public Builder addFamilies(int index, com.google.bigtable.v2.Family.Builder builderForValue) {
      if (familiesBuilder_ == null) {
        ensureFamiliesIsMutable();
        families_.add(index, builderForValue.build());
        onChanged();
      } else {
        familiesBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public Builder addAllFamilies(
        java.lang.Iterable<? extends com.google.bigtable.v2.Family> values) {
      if (familiesBuilder_ == null) {
        ensureFamiliesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(values, families_);
        onChanged();
      } else {
        familiesBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public Builder clearFamilies() {
      if (familiesBuilder_ == null) {
        families_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
      } else {
        familiesBuilder_.clear();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public Builder removeFamilies(int index) {
      if (familiesBuilder_ == null) {
        ensureFamiliesIsMutable();
        families_.remove(index);
        onChanged();
      } else {
        familiesBuilder_.remove(index);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public com.google.bigtable.v2.Family.Builder getFamiliesBuilder(int index) {
      return getFamiliesFieldBuilder().getBuilder(index);
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public com.google.bigtable.v2.FamilyOrBuilder getFamiliesOrBuilder(int index) {
      if (familiesBuilder_ == null) {
        return families_.get(index);
      } else {
        return familiesBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public java.util.List<? extends com.google.bigtable.v2.FamilyOrBuilder>
        getFamiliesOrBuilderList() {
      if (familiesBuilder_ != null) {
        return familiesBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(families_);
      }
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public com.google.bigtable.v2.Family.Builder addFamiliesBuilder() {
      return getFamiliesFieldBuilder()
          .addBuilder(com.google.bigtable.v2.Family.getDefaultInstance());
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public com.google.bigtable.v2.Family.Builder addFamiliesBuilder(int index) {
      return getFamiliesFieldBuilder()
          .addBuilder(index, com.google.bigtable.v2.Family.getDefaultInstance());
    }
    /**
     *
     *
     * <pre>
     * May be empty, but only if the entire row is empty.
     * The mutual ordering of column families is not specified.
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Family families = 2;</code>
     */
    public java.util.List<com.google.bigtable.v2.Family.Builder> getFamiliesBuilderList() {
      return getFamiliesFieldBuilder().getBuilderList();
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
            com.google.bigtable.v2.Family,
            com.google.bigtable.v2.Family.Builder,
            com.google.bigtable.v2.FamilyOrBuilder>
        getFamiliesFieldBuilder() {
      if (familiesBuilder_ == null) {
        familiesBuilder_ =
            new com.google.protobuf.RepeatedFieldBuilderV3<
                com.google.bigtable.v2.Family,
                com.google.bigtable.v2.Family.Builder,
                com.google.bigtable.v2.FamilyOrBuilder>(
                families_, ((bitField0_ & 0x00000002) != 0), getParentForChildren(), isClean());
        families_ = null;
      }
      return familiesBuilder_;
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

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.Row)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.Row)
  private static final com.google.bigtable.v2.Row DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.Row();
  }

  public static com.google.bigtable.v2.Row getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Row> PARSER =
      new com.google.protobuf.AbstractParser<Row>() {
        @java.lang.Override
        public Row parsePartialFrom(
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

  public static com.google.protobuf.Parser<Row> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Row> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.Row getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
