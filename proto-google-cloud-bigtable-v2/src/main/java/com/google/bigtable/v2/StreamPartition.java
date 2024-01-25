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
// source: google/bigtable/v2/data.proto

package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * NOTE: This API is intended to be used by Apache Beam BigtableIO.
 * A partition of a change stream.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.StreamPartition}
 */
public final class StreamPartition extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.StreamPartition)
    StreamPartitionOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use StreamPartition.newBuilder() to construct.
  private StreamPartition(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private StreamPartition() {}

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new StreamPartition();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_StreamPartition_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_StreamPartition_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.StreamPartition.class,
            com.google.bigtable.v2.StreamPartition.Builder.class);
  }

  public static final int ROW_RANGE_FIELD_NUMBER = 1;
  private com.google.bigtable.v2.RowRange rowRange_;
  /**
   *
   *
   * <pre>
   * The row range covered by this partition and is specified by
   * [`start_key_closed`, `end_key_open`).
   * </pre>
   *
   * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
   *
   * @return Whether the rowRange field is set.
   */
  @java.lang.Override
  public boolean hasRowRange() {
    return rowRange_ != null;
  }
  /**
   *
   *
   * <pre>
   * The row range covered by this partition and is specified by
   * [`start_key_closed`, `end_key_open`).
   * </pre>
   *
   * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
   *
   * @return The rowRange.
   */
  @java.lang.Override
  public com.google.bigtable.v2.RowRange getRowRange() {
    return rowRange_ == null ? com.google.bigtable.v2.RowRange.getDefaultInstance() : rowRange_;
  }
  /**
   *
   *
   * <pre>
   * The row range covered by this partition and is specified by
   * [`start_key_closed`, `end_key_open`).
   * </pre>
   *
   * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.RowRangeOrBuilder getRowRangeOrBuilder() {
    return rowRange_ == null ? com.google.bigtable.v2.RowRange.getDefaultInstance() : rowRange_;
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
    if (rowRange_ != null) {
      output.writeMessage(1, getRowRange());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (rowRange_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getRowRange());
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
    if (!(obj instanceof com.google.bigtable.v2.StreamPartition)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.StreamPartition other = (com.google.bigtable.v2.StreamPartition) obj;

    if (hasRowRange() != other.hasRowRange()) return false;
    if (hasRowRange()) {
      if (!getRowRange().equals(other.getRowRange())) return false;
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
    if (hasRowRange()) {
      hash = (37 * hash) + ROW_RANGE_FIELD_NUMBER;
      hash = (53 * hash) + getRowRange().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.StreamPartition parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.StreamPartition parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.StreamPartition parseFrom(
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

  public static Builder newBuilder(com.google.bigtable.v2.StreamPartition prototype) {
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
   * NOTE: This API is intended to be used by Apache Beam BigtableIO.
   * A partition of a change stream.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.StreamPartition}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.StreamPartition)
      com.google.bigtable.v2.StreamPartitionOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_StreamPartition_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_StreamPartition_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.StreamPartition.class,
              com.google.bigtable.v2.StreamPartition.Builder.class);
    }

    // Construct using com.google.bigtable.v2.StreamPartition.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      rowRange_ = null;
      if (rowRangeBuilder_ != null) {
        rowRangeBuilder_.dispose();
        rowRangeBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_StreamPartition_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.StreamPartition getDefaultInstanceForType() {
      return com.google.bigtable.v2.StreamPartition.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.StreamPartition build() {
      com.google.bigtable.v2.StreamPartition result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.StreamPartition buildPartial() {
      com.google.bigtable.v2.StreamPartition result =
          new com.google.bigtable.v2.StreamPartition(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.bigtable.v2.StreamPartition result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.rowRange_ = rowRangeBuilder_ == null ? rowRange_ : rowRangeBuilder_.build();
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
      if (other instanceof com.google.bigtable.v2.StreamPartition) {
        return mergeFrom((com.google.bigtable.v2.StreamPartition) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.StreamPartition other) {
      if (other == com.google.bigtable.v2.StreamPartition.getDefaultInstance()) return this;
      if (other.hasRowRange()) {
        mergeRowRange(other.getRowRange());
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
                input.readMessage(getRowRangeFieldBuilder().getBuilder(), extensionRegistry);
                bitField0_ |= 0x00000001;
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

    private com.google.bigtable.v2.RowRange rowRange_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.RowRange,
            com.google.bigtable.v2.RowRange.Builder,
            com.google.bigtable.v2.RowRangeOrBuilder>
        rowRangeBuilder_;
    /**
     *
     *
     * <pre>
     * The row range covered by this partition and is specified by
     * [`start_key_closed`, `end_key_open`).
     * </pre>
     *
     * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
     *
     * @return Whether the rowRange field is set.
     */
    public boolean hasRowRange() {
      return ((bitField0_ & 0x00000001) != 0);
    }
    /**
     *
     *
     * <pre>
     * The row range covered by this partition and is specified by
     * [`start_key_closed`, `end_key_open`).
     * </pre>
     *
     * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
     *
     * @return The rowRange.
     */
    public com.google.bigtable.v2.RowRange getRowRange() {
      if (rowRangeBuilder_ == null) {
        return rowRange_ == null ? com.google.bigtable.v2.RowRange.getDefaultInstance() : rowRange_;
      } else {
        return rowRangeBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * The row range covered by this partition and is specified by
     * [`start_key_closed`, `end_key_open`).
     * </pre>
     *
     * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
     */
    public Builder setRowRange(com.google.bigtable.v2.RowRange value) {
      if (rowRangeBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        rowRange_ = value;
      } else {
        rowRangeBuilder_.setMessage(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The row range covered by this partition and is specified by
     * [`start_key_closed`, `end_key_open`).
     * </pre>
     *
     * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
     */
    public Builder setRowRange(com.google.bigtable.v2.RowRange.Builder builderForValue) {
      if (rowRangeBuilder_ == null) {
        rowRange_ = builderForValue.build();
      } else {
        rowRangeBuilder_.setMessage(builderForValue.build());
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The row range covered by this partition and is specified by
     * [`start_key_closed`, `end_key_open`).
     * </pre>
     *
     * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
     */
    public Builder mergeRowRange(com.google.bigtable.v2.RowRange value) {
      if (rowRangeBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)
            && rowRange_ != null
            && rowRange_ != com.google.bigtable.v2.RowRange.getDefaultInstance()) {
          getRowRangeBuilder().mergeFrom(value);
        } else {
          rowRange_ = value;
        }
      } else {
        rowRangeBuilder_.mergeFrom(value);
      }
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The row range covered by this partition and is specified by
     * [`start_key_closed`, `end_key_open`).
     * </pre>
     *
     * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
     */
    public Builder clearRowRange() {
      bitField0_ = (bitField0_ & ~0x00000001);
      rowRange_ = null;
      if (rowRangeBuilder_ != null) {
        rowRangeBuilder_.dispose();
        rowRangeBuilder_ = null;
      }
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The row range covered by this partition and is specified by
     * [`start_key_closed`, `end_key_open`).
     * </pre>
     *
     * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
     */
    public com.google.bigtable.v2.RowRange.Builder getRowRangeBuilder() {
      bitField0_ |= 0x00000001;
      onChanged();
      return getRowRangeFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * The row range covered by this partition and is specified by
     * [`start_key_closed`, `end_key_open`).
     * </pre>
     *
     * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
     */
    public com.google.bigtable.v2.RowRangeOrBuilder getRowRangeOrBuilder() {
      if (rowRangeBuilder_ != null) {
        return rowRangeBuilder_.getMessageOrBuilder();
      } else {
        return rowRange_ == null ? com.google.bigtable.v2.RowRange.getDefaultInstance() : rowRange_;
      }
    }
    /**
     *
     *
     * <pre>
     * The row range covered by this partition and is specified by
     * [`start_key_closed`, `end_key_open`).
     * </pre>
     *
     * <code>.google.bigtable.v2.RowRange row_range = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.RowRange,
            com.google.bigtable.v2.RowRange.Builder,
            com.google.bigtable.v2.RowRangeOrBuilder>
        getRowRangeFieldBuilder() {
      if (rowRangeBuilder_ == null) {
        rowRangeBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.bigtable.v2.RowRange,
                com.google.bigtable.v2.RowRange.Builder,
                com.google.bigtable.v2.RowRangeOrBuilder>(
                getRowRange(), getParentForChildren(), isClean());
        rowRange_ = null;
      }
      return rowRangeBuilder_;
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

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.StreamPartition)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.StreamPartition)
  private static final com.google.bigtable.v2.StreamPartition DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.StreamPartition();
  }

  public static com.google.bigtable.v2.StreamPartition getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<StreamPartition> PARSER =
      new com.google.protobuf.AbstractParser<StreamPartition>() {
        @java.lang.Override
        public StreamPartition parsePartialFrom(
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

  public static com.google.protobuf.Parser<StreamPartition> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<StreamPartition> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.StreamPartition getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
