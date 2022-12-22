/*
 * Copyright 2020 Google LLC
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
// source: google/bigtable/v2/bigtable.proto

package com.google.bigtable.v2;

/**
 * <pre>
 * Response message for Bigtable.ReadModifyWriteRow.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.ReadModifyWriteRowResponse}
 */
public final class ReadModifyWriteRowResponse extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.ReadModifyWriteRowResponse)
    ReadModifyWriteRowResponseOrBuilder {
private static final long serialVersionUID = 0L;
  // Use ReadModifyWriteRowResponse.newBuilder() to construct.
  private ReadModifyWriteRowResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private ReadModifyWriteRowResponse() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new ReadModifyWriteRowResponse();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.google.bigtable.v2.BigtableProto.internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.BigtableProto.internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.ReadModifyWriteRowResponse.class, com.google.bigtable.v2.ReadModifyWriteRowResponse.Builder.class);
  }

  public static final int ROW_FIELD_NUMBER = 1;
  private com.google.bigtable.v2.Row row_;
  /**
   * <pre>
   * A Row containing the new contents of all cells modified by the request.
   * </pre>
   *
   * <code>.google.bigtable.v2.Row row = 1;</code>
   * @return Whether the row field is set.
   */
  @java.lang.Override
  public boolean hasRow() {
    return row_ != null;
  }
  /**
   * <pre>
   * A Row containing the new contents of all cells modified by the request.
   * </pre>
   *
   * <code>.google.bigtable.v2.Row row = 1;</code>
   * @return The row.
   */
  @java.lang.Override
  public com.google.bigtable.v2.Row getRow() {
    return row_ == null ? com.google.bigtable.v2.Row.getDefaultInstance() : row_;
  }
  /**
   * <pre>
   * A Row containing the new contents of all cells modified by the request.
   * </pre>
   *
   * <code>.google.bigtable.v2.Row row = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.RowOrBuilder getRowOrBuilder() {
    return getRow();
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
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (row_ != null) {
      output.writeMessage(1, getRow());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (row_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getRow());
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
    if (!(obj instanceof com.google.bigtable.v2.ReadModifyWriteRowResponse)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.ReadModifyWriteRowResponse other = (com.google.bigtable.v2.ReadModifyWriteRowResponse) obj;

    if (hasRow() != other.hasRow()) return false;
    if (hasRow()) {
      if (!getRow()
          .equals(other.getRow())) return false;
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
    if (hasRow()) {
      hash = (37 * hash) + ROW_FIELD_NUMBER;
      hash = (53 * hash) + getRow().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.google.bigtable.v2.ReadModifyWriteRowResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.google.bigtable.v2.ReadModifyWriteRowResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * <pre>
   * Response message for Bigtable.ReadModifyWriteRow.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.ReadModifyWriteRowResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.ReadModifyWriteRowResponse)
      com.google.bigtable.v2.ReadModifyWriteRowResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.bigtable.v2.BigtableProto.internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.BigtableProto.internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.ReadModifyWriteRowResponse.class, com.google.bigtable.v2.ReadModifyWriteRowResponse.Builder.class);
    }

    // Construct using com.google.bigtable.v2.ReadModifyWriteRowResponse.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (rowBuilder_ == null) {
        row_ = null;
      } else {
        row_ = null;
        rowBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.google.bigtable.v2.BigtableProto.internal_static_google_bigtable_v2_ReadModifyWriteRowResponse_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.ReadModifyWriteRowResponse getDefaultInstanceForType() {
      return com.google.bigtable.v2.ReadModifyWriteRowResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.ReadModifyWriteRowResponse build() {
      com.google.bigtable.v2.ReadModifyWriteRowResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.ReadModifyWriteRowResponse buildPartial() {
      com.google.bigtable.v2.ReadModifyWriteRowResponse result = new com.google.bigtable.v2.ReadModifyWriteRowResponse(this);
      if (rowBuilder_ == null) {
        result.row_ = row_;
      } else {
        result.row_ = rowBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.google.bigtable.v2.ReadModifyWriteRowResponse) {
        return mergeFrom((com.google.bigtable.v2.ReadModifyWriteRowResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.ReadModifyWriteRowResponse other) {
      if (other == com.google.bigtable.v2.ReadModifyWriteRowResponse.getDefaultInstance()) return this;
      if (other.hasRow()) {
        mergeRow(other.getRow());
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
            case 10: {
              input.readMessage(
                  getRowFieldBuilder().getBuilder(),
                  extensionRegistry);

              break;
            } // case 10
            default: {
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

    private com.google.bigtable.v2.Row row_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.bigtable.v2.Row, com.google.bigtable.v2.Row.Builder, com.google.bigtable.v2.RowOrBuilder> rowBuilder_;
    /**
     * <pre>
     * A Row containing the new contents of all cells modified by the request.
     * </pre>
     *
     * <code>.google.bigtable.v2.Row row = 1;</code>
     * @return Whether the row field is set.
     */
    public boolean hasRow() {
      return rowBuilder_ != null || row_ != null;
    }
    /**
     * <pre>
     * A Row containing the new contents of all cells modified by the request.
     * </pre>
     *
     * <code>.google.bigtable.v2.Row row = 1;</code>
     * @return The row.
     */
    public com.google.bigtable.v2.Row getRow() {
      if (rowBuilder_ == null) {
        return row_ == null ? com.google.bigtable.v2.Row.getDefaultInstance() : row_;
      } else {
        return rowBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * A Row containing the new contents of all cells modified by the request.
     * </pre>
     *
     * <code>.google.bigtable.v2.Row row = 1;</code>
     */
    public Builder setRow(com.google.bigtable.v2.Row value) {
      if (rowBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        row_ = value;
        onChanged();
      } else {
        rowBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <pre>
     * A Row containing the new contents of all cells modified by the request.
     * </pre>
     *
     * <code>.google.bigtable.v2.Row row = 1;</code>
     */
    public Builder setRow(
        com.google.bigtable.v2.Row.Builder builderForValue) {
      if (rowBuilder_ == null) {
        row_ = builderForValue.build();
        onChanged();
      } else {
        rowBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <pre>
     * A Row containing the new contents of all cells modified by the request.
     * </pre>
     *
     * <code>.google.bigtable.v2.Row row = 1;</code>
     */
    public Builder mergeRow(com.google.bigtable.v2.Row value) {
      if (rowBuilder_ == null) {
        if (row_ != null) {
          row_ =
            com.google.bigtable.v2.Row.newBuilder(row_).mergeFrom(value).buildPartial();
        } else {
          row_ = value;
        }
        onChanged();
      } else {
        rowBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <pre>
     * A Row containing the new contents of all cells modified by the request.
     * </pre>
     *
     * <code>.google.bigtable.v2.Row row = 1;</code>
     */
    public Builder clearRow() {
      if (rowBuilder_ == null) {
        row_ = null;
        onChanged();
      } else {
        row_ = null;
        rowBuilder_ = null;
      }

      return this;
    }
    /**
     * <pre>
     * A Row containing the new contents of all cells modified by the request.
     * </pre>
     *
     * <code>.google.bigtable.v2.Row row = 1;</code>
     */
    public com.google.bigtable.v2.Row.Builder getRowBuilder() {
      
      onChanged();
      return getRowFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * A Row containing the new contents of all cells modified by the request.
     * </pre>
     *
     * <code>.google.bigtable.v2.Row row = 1;</code>
     */
    public com.google.bigtable.v2.RowOrBuilder getRowOrBuilder() {
      if (rowBuilder_ != null) {
        return rowBuilder_.getMessageOrBuilder();
      } else {
        return row_ == null ?
            com.google.bigtable.v2.Row.getDefaultInstance() : row_;
      }
    }
    /**
     * <pre>
     * A Row containing the new contents of all cells modified by the request.
     * </pre>
     *
     * <code>.google.bigtable.v2.Row row = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.bigtable.v2.Row, com.google.bigtable.v2.Row.Builder, com.google.bigtable.v2.RowOrBuilder> 
        getRowFieldBuilder() {
      if (rowBuilder_ == null) {
        rowBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.Row, com.google.bigtable.v2.Row.Builder, com.google.bigtable.v2.RowOrBuilder>(
                getRow(),
                getParentForChildren(),
                isClean());
        row_ = null;
      }
      return rowBuilder_;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.ReadModifyWriteRowResponse)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.ReadModifyWriteRowResponse)
  private static final com.google.bigtable.v2.ReadModifyWriteRowResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.ReadModifyWriteRowResponse();
  }

  public static com.google.bigtable.v2.ReadModifyWriteRowResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ReadModifyWriteRowResponse>
      PARSER = new com.google.protobuf.AbstractParser<ReadModifyWriteRowResponse>() {
    @java.lang.Override
    public ReadModifyWriteRowResponse parsePartialFrom(
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

  public static com.google.protobuf.Parser<ReadModifyWriteRowResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ReadModifyWriteRowResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.ReadModifyWriteRowResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

