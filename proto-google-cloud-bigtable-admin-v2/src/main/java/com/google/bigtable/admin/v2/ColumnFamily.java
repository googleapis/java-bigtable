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
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/admin/v2/table.proto

// Protobuf Java Version: 3.25.4
package com.google.bigtable.admin.v2;

/**
 *
 *
 * <pre>
 * A set of columns within a table which share a common configuration.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.admin.v2.ColumnFamily}
 */
public final class ColumnFamily extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.admin.v2.ColumnFamily)
    ColumnFamilyOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use ColumnFamily.newBuilder() to construct.
  private ColumnFamily(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private ColumnFamily() {}

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new ColumnFamily();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.admin.v2.TableProto
        .internal_static_google_bigtable_admin_v2_ColumnFamily_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.admin.v2.TableProto
        .internal_static_google_bigtable_admin_v2_ColumnFamily_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.admin.v2.ColumnFamily.class,
            com.google.bigtable.admin.v2.ColumnFamily.Builder.class);
  }

  private int bitField0_;
  public static final int GC_RULE_FIELD_NUMBER = 1;
  private com.google.bigtable.admin.v2.GcRule gcRule_;
  /**
   *
   *
   * <pre>
   * Garbage collection rule specified as a protobuf.
   * Must serialize to at most 500 bytes.
   *
   * NOTE: Garbage collection executes opportunistically in the background, and
   * so it's possible for reads to return a cell even if it matches the active
   * GC expression for its family.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
   *
   * @return Whether the gcRule field is set.
   */
  @java.lang.Override
  public boolean hasGcRule() {
    return ((bitField0_ & 0x00000001) != 0);
  }
  /**
   *
   *
   * <pre>
   * Garbage collection rule specified as a protobuf.
   * Must serialize to at most 500 bytes.
   *
   * NOTE: Garbage collection executes opportunistically in the background, and
   * so it's possible for reads to return a cell even if it matches the active
   * GC expression for its family.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
   *
   * @return The gcRule.
   */
  @java.lang.Override
  public com.google.bigtable.admin.v2.GcRule getGcRule() {
    return gcRule_ == null ? com.google.bigtable.admin.v2.GcRule.getDefaultInstance() : gcRule_;
  }
  /**
   *
   *
   * <pre>
   * Garbage collection rule specified as a protobuf.
   * Must serialize to at most 500 bytes.
   *
   * NOTE: Garbage collection executes opportunistically in the background, and
   * so it's possible for reads to return a cell even if it matches the active
   * GC expression for its family.
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.admin.v2.GcRuleOrBuilder getGcRuleOrBuilder() {
    return gcRule_ == null ? com.google.bigtable.admin.v2.GcRule.getDefaultInstance() : gcRule_;
  }

  public static final int VALUE_TYPE_FIELD_NUMBER = 3;
  private com.google.bigtable.admin.v2.Type valueType_;
  /**
   *
   *
   * <pre>
   * The type of data stored in each of this family's cell values, including its
   * full encoding. If omitted, the family only serves raw untyped bytes.
   *
   * For now, only the `Aggregate` type is supported.
   *
   * `Aggregate` can only be set at family creation and is immutable afterwards.
   *
   *
   * If `value_type` is `Aggregate`, written data must be compatible with:
   *  * `value_type.input_type` for `AddInput` mutations
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
   *
   * @return Whether the valueType field is set.
   */
  @java.lang.Override
  public boolean hasValueType() {
    return ((bitField0_ & 0x00000002) != 0);
  }
  /**
   *
   *
   * <pre>
   * The type of data stored in each of this family's cell values, including its
   * full encoding. If omitted, the family only serves raw untyped bytes.
   *
   * For now, only the `Aggregate` type is supported.
   *
   * `Aggregate` can only be set at family creation and is immutable afterwards.
   *
   *
   * If `value_type` is `Aggregate`, written data must be compatible with:
   *  * `value_type.input_type` for `AddInput` mutations
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
   *
   * @return The valueType.
   */
  @java.lang.Override
  public com.google.bigtable.admin.v2.Type getValueType() {
    return valueType_ == null ? com.google.bigtable.admin.v2.Type.getDefaultInstance() : valueType_;
  }
  /**
   *
   *
   * <pre>
   * The type of data stored in each of this family's cell values, including its
   * full encoding. If omitted, the family only serves raw untyped bytes.
   *
   * For now, only the `Aggregate` type is supported.
   *
   * `Aggregate` can only be set at family creation and is immutable afterwards.
   *
   *
   * If `value_type` is `Aggregate`, written data must be compatible with:
   *  * `value_type.input_type` for `AddInput` mutations
   * </pre>
   *
   * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
   */
  @java.lang.Override
  public com.google.bigtable.admin.v2.TypeOrBuilder getValueTypeOrBuilder() {
    return valueType_ == null ? com.google.bigtable.admin.v2.Type.getDefaultInstance() : valueType_;
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
    if (((bitField0_ & 0x00000001) != 0)) {
      output.writeMessage(1, getGcRule());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      output.writeMessage(3, getValueType());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (((bitField0_ & 0x00000001) != 0)) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getGcRule());
    }
    if (((bitField0_ & 0x00000002) != 0)) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(3, getValueType());
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
    if (!(obj instanceof com.google.bigtable.admin.v2.ColumnFamily)) {
      return super.equals(obj);
    }
    com.google.bigtable.admin.v2.ColumnFamily other =
        (com.google.bigtable.admin.v2.ColumnFamily) obj;

    if (hasGcRule() != other.hasGcRule()) return false;
    if (hasGcRule()) {
      if (!getGcRule().equals(other.getGcRule())) return false;
    }
    if (hasValueType() != other.hasValueType()) return false;
    if (hasValueType()) {
      if (!getValueType().equals(other.getValueType())) return false;
    }
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
    if (hasGcRule()) {
      hash = (37 * hash) + GC_RULE_FIELD_NUMBER;
      hash = (53 * hash) + getGcRule().hashCode();
    }
    if (hasValueType()) {
      hash = (37 * hash) + VALUE_TYPE_FIELD_NUMBER;
      hash = (53 * hash) + getValueType().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.ColumnFamily parseFrom(
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

  public static Builder newBuilder(com.google.bigtable.admin.v2.ColumnFamily prototype) {
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
   * A set of columns within a table which share a common configuration.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.admin.v2.ColumnFamily}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.admin.v2.ColumnFamily)
      com.google.bigtable.admin.v2.ColumnFamilyOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.admin.v2.TableProto
          .internal_static_google_bigtable_admin_v2_ColumnFamily_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.admin.v2.TableProto
          .internal_static_google_bigtable_admin_v2_ColumnFamily_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.admin.v2.ColumnFamily.class,
              com.google.bigtable.admin.v2.ColumnFamily.Builder.class);
    }

    // Construct using com.google.bigtable.admin.v2.ColumnFamily.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }

    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders) {
        getGcRuleFieldBuilder();
        getValueTypeFieldBuilder();
      }
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      gcRule_ = null;
      if (gcRuleBuilder_ != null) {
        gcRuleBuilder_.dispose();
        gcRuleBuilder_ = null;
      }
      valueType_ = null;
      if (valueTypeBuilder_ != null) {
        valueTypeBuilder_.dispose();
        valueTypeBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.admin.v2.TableProto
          .internal_static_google_bigtable_admin_v2_ColumnFamily_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.ColumnFamily getDefaultInstanceForType() {
      return com.google.bigtable.admin.v2.ColumnFamily.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.ColumnFamily build() {
      com.google.bigtable.admin.v2.ColumnFamily result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.ColumnFamily buildPartial() {
      com.google.bigtable.admin.v2.ColumnFamily result =
          new com.google.bigtable.admin.v2.ColumnFamily(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.bigtable.admin.v2.ColumnFamily result) {
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.gcRule_ = gcRuleBuilder_ == null ? gcRule_ : gcRuleBuilder_.build();
        to_bitField0_ |= 0x00000001;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.valueType_ = valueTypeBuilder_ == null ? valueType_ : valueTypeBuilder_.build();
        to_bitField0_ |= 0x00000002;
      }
      result.bitField0_ |= to_bitField0_;
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
      if (other instanceof com.google.bigtable.admin.v2.ColumnFamily) {
        return mergeFrom((com.google.bigtable.admin.v2.ColumnFamily) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.admin.v2.ColumnFamily other) {
      if (other == com.google.bigtable.admin.v2.ColumnFamily.getDefaultInstance()) return this;
      if (other.hasGcRule()) {
        mergeGcRule(other.getGcRule());
      }
      if (other.hasValueType()) {
        mergeValueType(other.getValueType());
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
                input.readMessage(getGcRuleFieldBuilder().getBuilder(), extensionRegistry);
                bitField0_ |= 0x00000001;
                break;
              } // case 10
            case 26:
              {
                input.readMessage(getValueTypeFieldBuilder().getBuilder(), extensionRegistry);
                bitField0_ |= 0x00000002;
                break;
              } // case 26
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

    private com.google.bigtable.admin.v2.GcRule gcRule_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.admin.v2.GcRule,
            com.google.bigtable.admin.v2.GcRule.Builder,
            com.google.bigtable.admin.v2.GcRuleOrBuilder>
        gcRuleBuilder_;
    /**
     *
     *
     * <pre>
     * Garbage collection rule specified as a protobuf.
     * Must serialize to at most 500 bytes.
     *
     * NOTE: Garbage collection executes opportunistically in the background, and
     * so it's possible for reads to return a cell even if it matches the active
     * GC expression for its family.
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
     *
     * @return Whether the gcRule field is set.
     */
    public boolean hasGcRule() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     *
     *
     * <pre>
     * Garbage collection rule specified as a protobuf.
     * Must serialize to at most 500 bytes.
     *
     * NOTE: Garbage collection executes opportunistically in the background, and
     * so it's possible for reads to return a cell even if it matches the active
     * GC expression for its family.
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
     *
     * @return The gcRule.
     */
    public com.google.bigtable.admin.v2.GcRule getGcRule() {
      if (gcRuleBuilder_ == null) {
        return gcRule_ == null ? com.google.bigtable.admin.v2.GcRule.getDefaultInstance() : gcRule_;
      } else {
        return gcRuleBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * Garbage collection rule specified as a protobuf.
     * Must serialize to at most 500 bytes.
     *
     * NOTE: Garbage collection executes opportunistically in the background, and
     * so it's possible for reads to return a cell even if it matches the active
     * GC expression for its family.
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
     */
    public Builder setGcRule(com.google.bigtable.admin.v2.GcRule value) {
      if (gcRuleBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        gcRule_ = value;
      } else {
        gcRuleBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Garbage collection rule specified as a protobuf.
     * Must serialize to at most 500 bytes.
     *
     * NOTE: Garbage collection executes opportunistically in the background, and
     * so it's possible for reads to return a cell even if it matches the active
     * GC expression for its family.
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
     */
    public Builder setGcRule(com.google.bigtable.admin.v2.GcRule.Builder builderForValue) {
      if (gcRuleBuilder_ == null) {
        gcRule_ = builderForValue.build();
      } else {
        gcRuleBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Garbage collection rule specified as a protobuf.
     * Must serialize to at most 500 bytes.
     *
     * NOTE: Garbage collection executes opportunistically in the background, and
     * so it's possible for reads to return a cell even if it matches the active
     * GC expression for its family.
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
     */
    public Builder mergeGcRule(com.google.bigtable.admin.v2.GcRule value) {
      if (gcRuleBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)
            && gcRule_ != null
            && gcRule_ != com.google.bigtable.admin.v2.GcRule.getDefaultInstance()) {
          getGcRuleBuilder().mergeFrom(value);
        } else {
          gcRule_ = value;
        }
      } else {
        gcRuleBuilder_.mergeFrom(value);
      }
      if (gcRule_ != null) {
        bitField0_ |= 0x00000001;
        onChanged();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Garbage collection rule specified as a protobuf.
     * Must serialize to at most 500 bytes.
     *
     * NOTE: Garbage collection executes opportunistically in the background, and
     * so it's possible for reads to return a cell even if it matches the active
     * GC expression for its family.
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
     */
    public Builder clearGcRule() {
      bitField0_ = (bitField0_ & ~0x00000001);
      gcRule_ = null;
      if (gcRuleBuilder_ != null) {
        gcRuleBuilder_.dispose();
        gcRuleBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Garbage collection rule specified as a protobuf.
     * Must serialize to at most 500 bytes.
     *
     * NOTE: Garbage collection executes opportunistically in the background, and
     * so it's possible for reads to return a cell even if it matches the active
     * GC expression for its family.
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
     */
    public com.google.bigtable.admin.v2.GcRule.Builder getGcRuleBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getGcRuleFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * Garbage collection rule specified as a protobuf.
     * Must serialize to at most 500 bytes.
     *
     * NOTE: Garbage collection executes opportunistically in the background, and
     * so it's possible for reads to return a cell even if it matches the active
     * GC expression for its family.
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
     */
    public com.google.bigtable.admin.v2.GcRuleOrBuilder getGcRuleOrBuilder() {
      if (gcRuleBuilder_ != null) {
        return gcRuleBuilder_.getMessageOrBuilder();
      } else {
        return gcRule_ == null ? com.google.bigtable.admin.v2.GcRule.getDefaultInstance() : gcRule_;
      }
    }
    /**
     *
     *
     * <pre>
     * Garbage collection rule specified as a protobuf.
     * Must serialize to at most 500 bytes.
     *
     * NOTE: Garbage collection executes opportunistically in the background, and
     * so it's possible for reads to return a cell even if it matches the active
     * GC expression for its family.
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.GcRule gc_rule = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.admin.v2.GcRule,
            com.google.bigtable.admin.v2.GcRule.Builder,
            com.google.bigtable.admin.v2.GcRuleOrBuilder>
        getGcRuleFieldBuilder() {
      if (gcRuleBuilder_ == null) {
        gcRuleBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.bigtable.admin.v2.GcRule,
                com.google.bigtable.admin.v2.GcRule.Builder,
                com.google.bigtable.admin.v2.GcRuleOrBuilder>(
                getGcRule(), getParentForChildren(), isClean());
        gcRule_ = null;
      }
      return gcRuleBuilder_;
    }

    private com.google.bigtable.admin.v2.Type valueType_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.admin.v2.Type,
            com.google.bigtable.admin.v2.Type.Builder,
            com.google.bigtable.admin.v2.TypeOrBuilder>
        valueTypeBuilder_;
    /**
     *
     *
     * <pre>
     * The type of data stored in each of this family's cell values, including its
     * full encoding. If omitted, the family only serves raw untyped bytes.
     *
     * For now, only the `Aggregate` type is supported.
     *
     * `Aggregate` can only be set at family creation and is immutable afterwards.
     *
     *
     * If `value_type` is `Aggregate`, written data must be compatible with:
     *  * `value_type.input_type` for `AddInput` mutations
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
     *
     * @return Whether the valueType field is set.
     */
    public boolean hasValueType() {
      return ((bitField0_ & 0x00000002) != 0);
    }
    /**
     *
     *
     * <pre>
     * The type of data stored in each of this family's cell values, including its
     * full encoding. If omitted, the family only serves raw untyped bytes.
     *
     * For now, only the `Aggregate` type is supported.
     *
     * `Aggregate` can only be set at family creation and is immutable afterwards.
     *
     *
     * If `value_type` is `Aggregate`, written data must be compatible with:
     *  * `value_type.input_type` for `AddInput` mutations
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
     *
     * @return The valueType.
     */
    public com.google.bigtable.admin.v2.Type getValueType() {
      if (valueTypeBuilder_ == null) {
        return valueType_ == null
            ? com.google.bigtable.admin.v2.Type.getDefaultInstance()
            : valueType_;
      } else {
        return valueTypeBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * The type of data stored in each of this family's cell values, including its
     * full encoding. If omitted, the family only serves raw untyped bytes.
     *
     * For now, only the `Aggregate` type is supported.
     *
     * `Aggregate` can only be set at family creation and is immutable afterwards.
     *
     *
     * If `value_type` is `Aggregate`, written data must be compatible with:
     *  * `value_type.input_type` for `AddInput` mutations
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
     */
    public Builder setValueType(com.google.bigtable.admin.v2.Type value) {
      if (valueTypeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        valueType_ = value;
      } else {
        valueTypeBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The type of data stored in each of this family's cell values, including its
     * full encoding. If omitted, the family only serves raw untyped bytes.
     *
     * For now, only the `Aggregate` type is supported.
     *
     * `Aggregate` can only be set at family creation and is immutable afterwards.
     *
     *
     * If `value_type` is `Aggregate`, written data must be compatible with:
     *  * `value_type.input_type` for `AddInput` mutations
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
     */
    public Builder setValueType(com.google.bigtable.admin.v2.Type.Builder builderForValue) {
      if (valueTypeBuilder_ == null) {
        valueType_ = builderForValue.build();
      } else {
        valueTypeBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The type of data stored in each of this family's cell values, including its
     * full encoding. If omitted, the family only serves raw untyped bytes.
     *
     * For now, only the `Aggregate` type is supported.
     *
     * `Aggregate` can only be set at family creation and is immutable afterwards.
     *
     *
     * If `value_type` is `Aggregate`, written data must be compatible with:
     *  * `value_type.input_type` for `AddInput` mutations
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
     */
    public Builder mergeValueType(com.google.bigtable.admin.v2.Type value) {
      if (valueTypeBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0)
            && valueType_ != null
            && valueType_ != com.google.bigtable.admin.v2.Type.getDefaultInstance()) {
          getValueTypeBuilder().mergeFrom(value);
        } else {
          valueType_ = value;
        }
      } else {
        valueTypeBuilder_.mergeFrom(value);
      }
      if (valueType_ != null) {
        bitField0_ |= 0x00000002;
        onChanged();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The type of data stored in each of this family's cell values, including its
     * full encoding. If omitted, the family only serves raw untyped bytes.
     *
     * For now, only the `Aggregate` type is supported.
     *
     * `Aggregate` can only be set at family creation and is immutable afterwards.
     *
     *
     * If `value_type` is `Aggregate`, written data must be compatible with:
     *  * `value_type.input_type` for `AddInput` mutations
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
     */
    public Builder clearValueType() {
      bitField0_ = (bitField0_ & ~0x00000002);
      valueType_ = null;
      if (valueTypeBuilder_ != null) {
        valueTypeBuilder_.dispose();
        valueTypeBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The type of data stored in each of this family's cell values, including its
     * full encoding. If omitted, the family only serves raw untyped bytes.
     *
     * For now, only the `Aggregate` type is supported.
     *
     * `Aggregate` can only be set at family creation and is immutable afterwards.
     *
     *
     * If `value_type` is `Aggregate`, written data must be compatible with:
     *  * `value_type.input_type` for `AddInput` mutations
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
     */
    public com.google.bigtable.admin.v2.Type.Builder getValueTypeBuilder() {
      bitField0_ |= 0x00000002;
      onChanged();
      return getValueTypeFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * The type of data stored in each of this family's cell values, including its
     * full encoding. If omitted, the family only serves raw untyped bytes.
     *
     * For now, only the `Aggregate` type is supported.
     *
     * `Aggregate` can only be set at family creation and is immutable afterwards.
     *
     *
     * If `value_type` is `Aggregate`, written data must be compatible with:
     *  * `value_type.input_type` for `AddInput` mutations
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
     */
    public com.google.bigtable.admin.v2.TypeOrBuilder getValueTypeOrBuilder() {
      if (valueTypeBuilder_ != null) {
        return valueTypeBuilder_.getMessageOrBuilder();
      } else {
        return valueType_ == null
            ? com.google.bigtable.admin.v2.Type.getDefaultInstance()
            : valueType_;
      }
    }
    /**
     *
     *
     * <pre>
     * The type of data stored in each of this family's cell values, including its
     * full encoding. If omitted, the family only serves raw untyped bytes.
     *
     * For now, only the `Aggregate` type is supported.
     *
     * `Aggregate` can only be set at family creation and is immutable afterwards.
     *
     *
     * If `value_type` is `Aggregate`, written data must be compatible with:
     *  * `value_type.input_type` for `AddInput` mutations
     * </pre>
     *
     * <code>.google.bigtable.admin.v2.Type value_type = 3;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.admin.v2.Type,
            com.google.bigtable.admin.v2.Type.Builder,
            com.google.bigtable.admin.v2.TypeOrBuilder>
        getValueTypeFieldBuilder() {
      if (valueTypeBuilder_ == null) {
        valueTypeBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.bigtable.admin.v2.Type,
                com.google.bigtable.admin.v2.Type.Builder,
                com.google.bigtable.admin.v2.TypeOrBuilder>(
                getValueType(), getParentForChildren(), isClean());
        valueType_ = null;
      }
      return valueTypeBuilder_;
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

    // @@protoc_insertion_point(builder_scope:google.bigtable.admin.v2.ColumnFamily)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.admin.v2.ColumnFamily)
  private static final com.google.bigtable.admin.v2.ColumnFamily DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.admin.v2.ColumnFamily();
  }

  public static com.google.bigtable.admin.v2.ColumnFamily getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ColumnFamily> PARSER =
      new com.google.protobuf.AbstractParser<ColumnFamily>() {
        @java.lang.Override
        public ColumnFamily parsePartialFrom(
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

  public static com.google.protobuf.Parser<ColumnFamily> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ColumnFamily> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.admin.v2.ColumnFamily getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
