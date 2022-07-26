/*
 * Copyright 2022 Google LLC
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
 *
 *
 * <pre>
 * NOTE: This API is not generally available. Users must be allowlisted.
 * Response message for Bigtable.ListChangeStreamPartitions.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.ListChangeStreamPartitionsResponse}
 */
public final class ListChangeStreamPartitionsResponse extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.ListChangeStreamPartitionsResponse)
    ListChangeStreamPartitionsResponseOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use ListChangeStreamPartitionsResponse.newBuilder() to construct.
  private ListChangeStreamPartitionsResponse(
      com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private ListChangeStreamPartitionsResponse() {}

  @Override
  @SuppressWarnings({"unused"})
  protected Object newInstance(UnusedPrivateParameter unused) {
    return new ListChangeStreamPartitionsResponse();
  }

  @Override
  public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
    return this.unknownFields;
  }

  private ListChangeStreamPartitionsResponse(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
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
              com.google.bigtable.v2.StreamPartition.Builder subBuilder = null;
              if (partition_ != null) {
                subBuilder = partition_.toBuilder();
              }
              partition_ =
                  input.readMessage(
                      com.google.bigtable.v2.StreamPartition.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(partition_);
                partition_ = subBuilder.buildPartial();
              }

              break;
            }
          default:
            {
              if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (com.google.protobuf.UninitializedMessageException e) {
      throw e.asInvalidProtocolBufferException().setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return BigtableProto
        .internal_static_google_bigtable_v2_ListChangeStreamPartitionsResponse_descriptor;
  }

  @Override
  protected FieldAccessorTable internalGetFieldAccessorTable() {
    return BigtableProto
        .internal_static_google_bigtable_v2_ListChangeStreamPartitionsResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(ListChangeStreamPartitionsResponse.class, Builder.class);
  }

  public static final int PARTITION_FIELD_NUMBER = 1;
  private com.google.bigtable.v2.StreamPartition partition_;
  /**
   *
   *
   * <pre>
   * A partition of the change stream.
   * </pre>
   *
   * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
   *
   * @return Whether the partition field is set.
   */
  @Override
  public boolean hasPartition() {
    return partition_ != null;
  }
  /**
   *
   *
   * <pre>
   * A partition of the change stream.
   * </pre>
   *
   * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
   *
   * @return The partition.
   */
  @Override
  public com.google.bigtable.v2.StreamPartition getPartition() {
    return partition_ == null
        ? com.google.bigtable.v2.StreamPartition.getDefaultInstance()
        : partition_;
  }
  /**
   *
   *
   * <pre>
   * A partition of the change stream.
   * </pre>
   *
   * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
   */
  @Override
  public com.google.bigtable.v2.StreamPartitionOrBuilder getPartitionOrBuilder() {
    return getPartition();
  }

  private byte memoizedIsInitialized = -1;

  @Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @Override
  public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
    if (partition_ != null) {
      output.writeMessage(1, getPartition());
    }
    unknownFields.writeTo(output);
  }

  @Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (partition_ != null) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, getPartition());
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof ListChangeStreamPartitionsResponse)) {
      return super.equals(obj);
    }
    ListChangeStreamPartitionsResponse other = (ListChangeStreamPartitionsResponse) obj;

    if (hasPartition() != other.hasPartition()) return false;
    if (hasPartition()) {
      if (!getPartition().equals(other.getPartition())) return false;
    }
    if (!unknownFields.equals(other.unknownFields)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasPartition()) {
      hash = (37 * hash) + PARTITION_FIELD_NUMBER;
      hash = (53 * hash) + getPartition().hashCode();
    }
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static ListChangeStreamPartitionsResponse parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static ListChangeStreamPartitionsResponse parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static ListChangeStreamPartitionsResponse parseFrom(com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static ListChangeStreamPartitionsResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static ListChangeStreamPartitionsResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static ListChangeStreamPartitionsResponse parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static ListChangeStreamPartitionsResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static ListChangeStreamPartitionsResponse parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static ListChangeStreamPartitionsResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static ListChangeStreamPartitionsResponse parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static ListChangeStreamPartitionsResponse parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static ListChangeStreamPartitionsResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  @Override
  public Builder newBuilderForType() {
    return newBuilder();
  }

  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }

  public static Builder newBuilder(ListChangeStreamPartitionsResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }

  @Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
  }

  @Override
  protected Builder newBuilderForType(BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   *
   *
   * <pre>
   * NOTE: This API is not generally available. Users must be allowlisted.
   * Response message for Bigtable.ListChangeStreamPartitions.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.ListChangeStreamPartitionsResponse}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.ListChangeStreamPartitionsResponse)
      ListChangeStreamPartitionsResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return BigtableProto
          .internal_static_google_bigtable_v2_ListChangeStreamPartitionsResponse_descriptor;
    }

    @Override
    protected FieldAccessorTable internalGetFieldAccessorTable() {
      return BigtableProto
          .internal_static_google_bigtable_v2_ListChangeStreamPartitionsResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(ListChangeStreamPartitionsResponse.class, Builder.class);
    }

    // Construct using com.google.bigtable.v2.ListChangeStreamPartitionsResponse.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }

    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders) {}
    }

    @Override
    public Builder clear() {
      super.clear();
      if (partitionBuilder_ == null) {
        partition_ = null;
      } else {
        partition_ = null;
        partitionBuilder_ = null;
      }
      return this;
    }

    @Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return BigtableProto
          .internal_static_google_bigtable_v2_ListChangeStreamPartitionsResponse_descriptor;
    }

    @Override
    public ListChangeStreamPartitionsResponse getDefaultInstanceForType() {
      return ListChangeStreamPartitionsResponse.getDefaultInstance();
    }

    @Override
    public ListChangeStreamPartitionsResponse build() {
      ListChangeStreamPartitionsResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @Override
    public ListChangeStreamPartitionsResponse buildPartial() {
      ListChangeStreamPartitionsResponse result = new ListChangeStreamPartitionsResponse(this);
      if (partitionBuilder_ == null) {
        result.partition_ = partition_;
      } else {
        result.partition_ = partitionBuilder_.build();
      }
      onBuilt();
      return result;
    }

    @Override
    public Builder clone() {
      return super.clone();
    }

    @Override
    public Builder setField(com.google.protobuf.Descriptors.FieldDescriptor field, Object value) {
      return super.setField(field, value);
    }

    @Override
    public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }

    @Override
    public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }

    @Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field, int index, Object value) {
      return super.setRepeatedField(field, index, value);
    }

    @Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field, Object value) {
      return super.addRepeatedField(field, value);
    }

    @Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof ListChangeStreamPartitionsResponse) {
        return mergeFrom((ListChangeStreamPartitionsResponse) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(ListChangeStreamPartitionsResponse other) {
      if (other == ListChangeStreamPartitionsResponse.getDefaultInstance()) return this;
      if (other.hasPartition()) {
        mergePartition(other.getPartition());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @Override
    public final boolean isInitialized() {
      return true;
    }

    @Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      ListChangeStreamPartitionsResponse parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (ListChangeStreamPartitionsResponse) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private com.google.bigtable.v2.StreamPartition partition_;
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.StreamPartition,
            com.google.bigtable.v2.StreamPartition.Builder,
            com.google.bigtable.v2.StreamPartitionOrBuilder>
        partitionBuilder_;
    /**
     *
     *
     * <pre>
     * A partition of the change stream.
     * </pre>
     *
     * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
     *
     * @return Whether the partition field is set.
     */
    public boolean hasPartition() {
      return partitionBuilder_ != null || partition_ != null;
    }
    /**
     *
     *
     * <pre>
     * A partition of the change stream.
     * </pre>
     *
     * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
     *
     * @return The partition.
     */
    public com.google.bigtable.v2.StreamPartition getPartition() {
      if (partitionBuilder_ == null) {
        return partition_ == null
            ? com.google.bigtable.v2.StreamPartition.getDefaultInstance()
            : partition_;
      } else {
        return partitionBuilder_.getMessage();
      }
    }
    /**
     *
     *
     * <pre>
     * A partition of the change stream.
     * </pre>
     *
     * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
     */
    public Builder setPartition(com.google.bigtable.v2.StreamPartition value) {
      if (partitionBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        partition_ = value;
        onChanged();
      } else {
        partitionBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * A partition of the change stream.
     * </pre>
     *
     * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
     */
    public Builder setPartition(com.google.bigtable.v2.StreamPartition.Builder builderForValue) {
      if (partitionBuilder_ == null) {
        partition_ = builderForValue.build();
        onChanged();
      } else {
        partitionBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * A partition of the change stream.
     * </pre>
     *
     * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
     */
    public Builder mergePartition(com.google.bigtable.v2.StreamPartition value) {
      if (partitionBuilder_ == null) {
        if (partition_ != null) {
          partition_ =
              com.google.bigtable.v2.StreamPartition.newBuilder(partition_)
                  .mergeFrom(value)
                  .buildPartial();
        } else {
          partition_ = value;
        }
        onChanged();
      } else {
        partitionBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * A partition of the change stream.
     * </pre>
     *
     * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
     */
    public Builder clearPartition() {
      if (partitionBuilder_ == null) {
        partition_ = null;
        onChanged();
      } else {
        partition_ = null;
        partitionBuilder_ = null;
      }

      return this;
    }
    /**
     *
     *
     * <pre>
     * A partition of the change stream.
     * </pre>
     *
     * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
     */
    public com.google.bigtable.v2.StreamPartition.Builder getPartitionBuilder() {

      onChanged();
      return getPartitionFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * A partition of the change stream.
     * </pre>
     *
     * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
     */
    public com.google.bigtable.v2.StreamPartitionOrBuilder getPartitionOrBuilder() {
      if (partitionBuilder_ != null) {
        return partitionBuilder_.getMessageOrBuilder();
      } else {
        return partition_ == null
            ? com.google.bigtable.v2.StreamPartition.getDefaultInstance()
            : partition_;
      }
    }
    /**
     *
     *
     * <pre>
     * A partition of the change stream.
     * </pre>
     *
     * <code>.google.bigtable.v2.StreamPartition partition = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.StreamPartition,
            com.google.bigtable.v2.StreamPartition.Builder,
            com.google.bigtable.v2.StreamPartitionOrBuilder>
        getPartitionFieldBuilder() {
      if (partitionBuilder_ == null) {
        partitionBuilder_ =
            new com.google.protobuf.SingleFieldBuilderV3<
                com.google.bigtable.v2.StreamPartition,
                com.google.bigtable.v2.StreamPartition.Builder,
                com.google.bigtable.v2.StreamPartitionOrBuilder>(
                getPartition(), getParentForChildren(), isClean());
        partition_ = null;
      }
      return partitionBuilder_;
    }

    @Override
    public final Builder setUnknownFields(final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.ListChangeStreamPartitionsResponse)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.ListChangeStreamPartitionsResponse)
  private static final ListChangeStreamPartitionsResponse DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new ListChangeStreamPartitionsResponse();
  }

  public static ListChangeStreamPartitionsResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListChangeStreamPartitionsResponse> PARSER =
      new com.google.protobuf.AbstractParser<ListChangeStreamPartitionsResponse>() {
        @Override
        public ListChangeStreamPartitionsResponse parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new ListChangeStreamPartitionsResponse(input, extensionRegistry);
        }
      };

  public static com.google.protobuf.Parser<ListChangeStreamPartitionsResponse> parser() {
    return PARSER;
  }

  @Override
  public com.google.protobuf.Parser<ListChangeStreamPartitionsResponse> getParserForType() {
    return PARSER;
  }

  @Override
  public ListChangeStreamPartitionsResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}