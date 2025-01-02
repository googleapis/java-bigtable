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
// source: google/bigtable/v2/bigtable.proto
// Protobuf Java Version: 4.29.0

package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * Response message for Bigtable.ExecuteQuery
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.ExecuteQueryResponse}
 */
public final class ExecuteQueryResponse extends com.google.protobuf.GeneratedMessage
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.ExecuteQueryResponse)
    ExecuteQueryResponseOrBuilder {
  private static final long serialVersionUID = 0L;

  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
        com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
        /* major= */ 4,
        /* minor= */ 29,
        /* patch= */ 0,
        /* suffix= */ "",
        ExecuteQueryResponse.class.getName());
  }
  // Use ExecuteQueryResponse.newBuilder() to construct.
  private ExecuteQueryResponse(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }

  private ExecuteQueryResponse() {}

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.BigtableProto
        .internal_static_google_bigtable_v2_ExecuteQueryResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.BigtableProto
        .internal_static_google_bigtable_v2_ExecuteQueryResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.ExecuteQueryResponse.class,
            com.google.bigtable.v2.ExecuteQueryResponse.Builder.class);
  }

  private int responseCase_ = 0;

  @SuppressWarnings("serial")
  private java.lang.Object response_;

  public enum ResponseCase
      implements
          com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    METADATA(1),
    RESULTS(2),
    RESPONSE_NOT_SET(0);
    private final int value;

    private ResponseCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static ResponseCase valueOf(int value) {
      return forNumber(value);
    }

    public static ResponseCase forNumber(int value) {
      switch (value) {
        case 1:
          return METADATA;
        case 2:
          return RESULTS;
        case 0:
          return RESPONSE_NOT_SET;
        default:
          return null;
      }
    }

    public int getNumber() {
      return this.value;
    }
  };

  public ResponseCase getResponseCase() {
    return ResponseCase.forNumber(responseCase_);
  }

  public static final int METADATA_FIELD_NUMBER = 1;
  /**
   *
   *
   * <pre>
   * Structure of rows in this response stream. The first (and only the first)
   * response streamed from the server will be of this type.
   * </pre>
   *
   * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
   *
   * @return Whether the metadata field is set.
   */
  @java.lang.Override
  public boolean hasMetadata() {
    return responseCase_ == 1;
  }
  /**
   *
   *
   * <pre>
   * Structure of rows in this response stream. The first (and only the first)
   * response streamed from the server will be of this type.
   * </pre>
   *
   * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
   *
   * @return The metadata.
   */
  @java.lang.Override
  public com.google.bigtable.v2.ResultSetMetadata getMetadata() {
    if (responseCase_ == 1) {
      return (com.google.bigtable.v2.ResultSetMetadata) response_;
    }
    return com.google.bigtable.v2.ResultSetMetadata.getDefaultInstance();
  }
  /**
   *
   *
   * <pre>
   * Structure of rows in this response stream. The first (and only the first)
   * response streamed from the server will be of this type.
   * </pre>
   *
   * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.ResultSetMetadataOrBuilder getMetadataOrBuilder() {
    if (responseCase_ == 1) {
      return (com.google.bigtable.v2.ResultSetMetadata) response_;
    }
    return com.google.bigtable.v2.ResultSetMetadata.getDefaultInstance();
  }

  public static final int RESULTS_FIELD_NUMBER = 2;
  /**
   *
   *
   * <pre>
   * A partial result set with row data potentially including additional
   * instructions on how recent past and future partial responses should be
   * interpreted.
   * </pre>
   *
   * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
   *
   * @return Whether the results field is set.
   */
  @java.lang.Override
  public boolean hasResults() {
    return responseCase_ == 2;
  }
  /**
   *
   *
   * <pre>
   * A partial result set with row data potentially including additional
   * instructions on how recent past and future partial responses should be
   * interpreted.
   * </pre>
   *
   * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
   *
   * @return The results.
   */
  @java.lang.Override
  public com.google.bigtable.v2.PartialResultSet getResults() {
    if (responseCase_ == 2) {
      return (com.google.bigtable.v2.PartialResultSet) response_;
    }
    return com.google.bigtable.v2.PartialResultSet.getDefaultInstance();
  }
  /**
   *
   *
   * <pre>
   * A partial result set with row data potentially including additional
   * instructions on how recent past and future partial responses should be
   * interpreted.
   * </pre>
   *
   * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.PartialResultSetOrBuilder getResultsOrBuilder() {
    if (responseCase_ == 2) {
      return (com.google.bigtable.v2.PartialResultSet) response_;
    }
    return com.google.bigtable.v2.PartialResultSet.getDefaultInstance();
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
    if (responseCase_ == 1) {
      output.writeMessage(1, (com.google.bigtable.v2.ResultSetMetadata) response_);
    }
    if (responseCase_ == 2) {
      output.writeMessage(2, (com.google.bigtable.v2.PartialResultSet) response_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (responseCase_ == 1) {
      size +=
          com.google.protobuf.CodedOutputStream.computeMessageSize(
              1, (com.google.bigtable.v2.ResultSetMetadata) response_);
    }
    if (responseCase_ == 2) {
      size +=
          com.google.protobuf.CodedOutputStream.computeMessageSize(
              2, (com.google.bigtable.v2.PartialResultSet) response_);
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
    if (!(obj instanceof com.google.bigtable.v2.ExecuteQueryResponse)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.ExecuteQueryResponse other =
        (com.google.bigtable.v2.ExecuteQueryResponse) obj;

    if (!getResponseCase().equals(other.getResponseCase())) return false;
    switch (responseCase_) {
      case 1:
        if (!getMetadata().equals(other.getMetadata())) return false;
        break;
      case 2:
        if (!getResults().equals(other.getResults())) return false;
        break;
      case 0:
      default:
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
    switch (responseCase_) {
      case 1:
        hash = (37 * hash) + METADATA_FIELD_NUMBER;
        hash = (53 * hash) + getMetadata().hashCode();
        break;
      case 2:
        hash = (37 * hash) + RESULTS_FIELD_NUMBER;
        hash = (53 * hash) + getResults().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse parseFrom(
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

  public static Builder newBuilder(com.google.bigtable.v2.ExecuteQueryResponse prototype) {
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
   * Response message for Bigtable.ExecuteQuery
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.ExecuteQueryResponse}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.ExecuteQueryResponse)
      com.google.bigtable.v2.ExecuteQueryResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.BigtableProto
          .internal_static_google_bigtable_v2_ExecuteQueryResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.BigtableProto
          .internal_static_google_bigtable_v2_ExecuteQueryResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.ExecuteQueryResponse.class,
              com.google.bigtable.v2.ExecuteQueryResponse.Builder.class);
    }

    // Construct using com.google.bigtable.v2.ExecuteQueryResponse.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      if (metadataBuilder_ != null) {
        metadataBuilder_.clear();
      }
      if (resultsBuilder_ != null) {
        resultsBuilder_.clear();
      }
      responseCase_ = 0;
      response_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.BigtableProto
          .internal_static_google_bigtable_v2_ExecuteQueryResponse_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.ExecuteQueryResponse getDefaultInstanceForType() {
      return com.google.bigtable.v2.ExecuteQueryResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.ExecuteQueryResponse build() {
      com.google.bigtable.v2.ExecuteQueryResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.ExecuteQueryResponse buildPartial() {
      com.google.bigtable.v2.ExecuteQueryResponse result =
          new com.google.bigtable.v2.ExecuteQueryResponse(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      buildPartialOneofs(result);
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.bigtable.v2.ExecuteQueryResponse result) {
      int from_bitField0_ = bitField0_;
    }

    private void buildPartialOneofs(com.google.bigtable.v2.ExecuteQueryResponse result) {
      result.responseCase_ = responseCase_;
      result.response_ = this.response_;
      if (responseCase_ == 1 && metadataBuilder_ != null) {
        result.response_ = metadataBuilder_.build();
      }
      if (responseCase_ == 2 && resultsBuilder_ != null) {
        result.response_ = resultsBuilder_.build();
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.google.bigtable.v2.ExecuteQueryResponse) {
        return mergeFrom((com.google.bigtable.v2.ExecuteQueryResponse) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.ExecuteQueryResponse other) {
      if (other == com.google.bigtable.v2.ExecuteQueryResponse.getDefaultInstance()) return this;
      switch (other.getResponseCase()) {
        case METADATA:
          {
            mergeMetadata(other.getMetadata());
            break;
          }
        case RESULTS:
          {
            mergeResults(other.getResults());
            break;
          }
        case RESPONSE_NOT_SET:
          {
            break;
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
                input.readMessage(getMetadataFieldBuilder().getBuilder(), extensionRegistry);
                responseCase_ = 1;
                break;
              } // case 10
            case 18:
              {
                input.readMessage(getResultsFieldBuilder().getBuilder(), extensionRegistry);
                responseCase_ = 2;
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

    private int responseCase_ = 0;
    private java.lang.Object response_;

    public ResponseCase getResponseCase() {
      return ResponseCase.forNumber(responseCase_);
    }

    public Builder clearResponse() {
      responseCase_ = 0;
      response_ = null;
      onChanged();
      return this;
    }

    private int bitField0_;

    private com.google.protobuf.SingleFieldBuilder<
            com.google.bigtable.v2.ResultSetMetadata,
            com.google.bigtable.v2.ResultSetMetadata.Builder,
            com.google.bigtable.v2.ResultSetMetadataOrBuilder>
        metadataBuilder_;
    /**
     *
     *
     * <pre>
     * Structure of rows in this response stream. The first (and only the first)
     * response streamed from the server will be of this type.
     * </pre>
     *
     * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
     *
     * @return Whether the metadata field is set.
     */
    @java.lang.Override
    public boolean hasMetadata() {
      return responseCase_ == 1;
    }
    /**
     *
     *
     * <pre>
     * Structure of rows in this response stream. The first (and only the first)
     * response streamed from the server will be of this type.
     * </pre>
     *
     * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
     *
     * @return The metadata.
     */
    @java.lang.Override
    public com.google.bigtable.v2.ResultSetMetadata getMetadata() {
      if (metadataBuilder_ == null) {
        if (responseCase_ == 1) {
          return (com.google.bigtable.v2.ResultSetMetadata) response_;
        }
        return com.google.bigtable.v2.ResultSetMetadata.getDefaultInstance();
      } else {
        if (responseCase_ == 1) {
          return metadataBuilder_.getMessage();
        }
        return com.google.bigtable.v2.ResultSetMetadata.getDefaultInstance();
      }
    }
    /**
     *
     *
     * <pre>
     * Structure of rows in this response stream. The first (and only the first)
     * response streamed from the server will be of this type.
     * </pre>
     *
     * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
     */
    public Builder setMetadata(com.google.bigtable.v2.ResultSetMetadata value) {
      if (metadataBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        response_ = value;
        onChanged();
      } else {
        metadataBuilder_.setMessage(value);
      }
      responseCase_ = 1;
      return this;
    }
    /**
     *
     *
     * <pre>
     * Structure of rows in this response stream. The first (and only the first)
     * response streamed from the server will be of this type.
     * </pre>
     *
     * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
     */
    public Builder setMetadata(com.google.bigtable.v2.ResultSetMetadata.Builder builderForValue) {
      if (metadataBuilder_ == null) {
        response_ = builderForValue.build();
        onChanged();
      } else {
        metadataBuilder_.setMessage(builderForValue.build());
      }
      responseCase_ = 1;
      return this;
    }
    /**
     *
     *
     * <pre>
     * Structure of rows in this response stream. The first (and only the first)
     * response streamed from the server will be of this type.
     * </pre>
     *
     * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
     */
    public Builder mergeMetadata(com.google.bigtable.v2.ResultSetMetadata value) {
      if (metadataBuilder_ == null) {
        if (responseCase_ == 1
            && response_ != com.google.bigtable.v2.ResultSetMetadata.getDefaultInstance()) {
          response_ =
              com.google.bigtable.v2.ResultSetMetadata.newBuilder(
                      (com.google.bigtable.v2.ResultSetMetadata) response_)
                  .mergeFrom(value)
                  .buildPartial();
        } else {
          response_ = value;
        }
        onChanged();
      } else {
        if (responseCase_ == 1) {
          metadataBuilder_.mergeFrom(value);
        } else {
          metadataBuilder_.setMessage(value);
        }
      }
      responseCase_ = 1;
      return this;
    }
    /**
     *
     *
     * <pre>
     * Structure of rows in this response stream. The first (and only the first)
     * response streamed from the server will be of this type.
     * </pre>
     *
     * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
     */
    public Builder clearMetadata() {
      if (metadataBuilder_ == null) {
        if (responseCase_ == 1) {
          responseCase_ = 0;
          response_ = null;
          onChanged();
        }
      } else {
        if (responseCase_ == 1) {
          responseCase_ = 0;
          response_ = null;
        }
        metadataBuilder_.clear();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Structure of rows in this response stream. The first (and only the first)
     * response streamed from the server will be of this type.
     * </pre>
     *
     * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
     */
    public com.google.bigtable.v2.ResultSetMetadata.Builder getMetadataBuilder() {
      return getMetadataFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * Structure of rows in this response stream. The first (and only the first)
     * response streamed from the server will be of this type.
     * </pre>
     *
     * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
     */
    @java.lang.Override
    public com.google.bigtable.v2.ResultSetMetadataOrBuilder getMetadataOrBuilder() {
      if ((responseCase_ == 1) && (metadataBuilder_ != null)) {
        return metadataBuilder_.getMessageOrBuilder();
      } else {
        if (responseCase_ == 1) {
          return (com.google.bigtable.v2.ResultSetMetadata) response_;
        }
        return com.google.bigtable.v2.ResultSetMetadata.getDefaultInstance();
      }
    }
    /**
     *
     *
     * <pre>
     * Structure of rows in this response stream. The first (and only the first)
     * response streamed from the server will be of this type.
     * </pre>
     *
     * <code>.google.bigtable.v2.ResultSetMetadata metadata = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilder<
            com.google.bigtable.v2.ResultSetMetadata,
            com.google.bigtable.v2.ResultSetMetadata.Builder,
            com.google.bigtable.v2.ResultSetMetadataOrBuilder>
        getMetadataFieldBuilder() {
      if (metadataBuilder_ == null) {
        if (!(responseCase_ == 1)) {
          response_ = com.google.bigtable.v2.ResultSetMetadata.getDefaultInstance();
        }
        metadataBuilder_ =
            new com.google.protobuf.SingleFieldBuilder<
                com.google.bigtable.v2.ResultSetMetadata,
                com.google.bigtable.v2.ResultSetMetadata.Builder,
                com.google.bigtable.v2.ResultSetMetadataOrBuilder>(
                (com.google.bigtable.v2.ResultSetMetadata) response_,
                getParentForChildren(),
                isClean());
        response_ = null;
      }
      responseCase_ = 1;
      onChanged();
      return metadataBuilder_;
    }

    private com.google.protobuf.SingleFieldBuilder<
            com.google.bigtable.v2.PartialResultSet,
            com.google.bigtable.v2.PartialResultSet.Builder,
            com.google.bigtable.v2.PartialResultSetOrBuilder>
        resultsBuilder_;
    /**
     *
     *
     * <pre>
     * A partial result set with row data potentially including additional
     * instructions on how recent past and future partial responses should be
     * interpreted.
     * </pre>
     *
     * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
     *
     * @return Whether the results field is set.
     */
    @java.lang.Override
    public boolean hasResults() {
      return responseCase_ == 2;
    }
    /**
     *
     *
     * <pre>
     * A partial result set with row data potentially including additional
     * instructions on how recent past and future partial responses should be
     * interpreted.
     * </pre>
     *
     * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
     *
     * @return The results.
     */
    @java.lang.Override
    public com.google.bigtable.v2.PartialResultSet getResults() {
      if (resultsBuilder_ == null) {
        if (responseCase_ == 2) {
          return (com.google.bigtable.v2.PartialResultSet) response_;
        }
        return com.google.bigtable.v2.PartialResultSet.getDefaultInstance();
      } else {
        if (responseCase_ == 2) {
          return resultsBuilder_.getMessage();
        }
        return com.google.bigtable.v2.PartialResultSet.getDefaultInstance();
      }
    }
    /**
     *
     *
     * <pre>
     * A partial result set with row data potentially including additional
     * instructions on how recent past and future partial responses should be
     * interpreted.
     * </pre>
     *
     * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
     */
    public Builder setResults(com.google.bigtable.v2.PartialResultSet value) {
      if (resultsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        response_ = value;
        onChanged();
      } else {
        resultsBuilder_.setMessage(value);
      }
      responseCase_ = 2;
      return this;
    }
    /**
     *
     *
     * <pre>
     * A partial result set with row data potentially including additional
     * instructions on how recent past and future partial responses should be
     * interpreted.
     * </pre>
     *
     * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
     */
    public Builder setResults(com.google.bigtable.v2.PartialResultSet.Builder builderForValue) {
      if (resultsBuilder_ == null) {
        response_ = builderForValue.build();
        onChanged();
      } else {
        resultsBuilder_.setMessage(builderForValue.build());
      }
      responseCase_ = 2;
      return this;
    }
    /**
     *
     *
     * <pre>
     * A partial result set with row data potentially including additional
     * instructions on how recent past and future partial responses should be
     * interpreted.
     * </pre>
     *
     * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
     */
    public Builder mergeResults(com.google.bigtable.v2.PartialResultSet value) {
      if (resultsBuilder_ == null) {
        if (responseCase_ == 2
            && response_ != com.google.bigtable.v2.PartialResultSet.getDefaultInstance()) {
          response_ =
              com.google.bigtable.v2.PartialResultSet.newBuilder(
                      (com.google.bigtable.v2.PartialResultSet) response_)
                  .mergeFrom(value)
                  .buildPartial();
        } else {
          response_ = value;
        }
        onChanged();
      } else {
        if (responseCase_ == 2) {
          resultsBuilder_.mergeFrom(value);
        } else {
          resultsBuilder_.setMessage(value);
        }
      }
      responseCase_ = 2;
      return this;
    }
    /**
     *
     *
     * <pre>
     * A partial result set with row data potentially including additional
     * instructions on how recent past and future partial responses should be
     * interpreted.
     * </pre>
     *
     * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
     */
    public Builder clearResults() {
      if (resultsBuilder_ == null) {
        if (responseCase_ == 2) {
          responseCase_ = 0;
          response_ = null;
          onChanged();
        }
      } else {
        if (responseCase_ == 2) {
          responseCase_ = 0;
          response_ = null;
        }
        resultsBuilder_.clear();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * A partial result set with row data potentially including additional
     * instructions on how recent past and future partial responses should be
     * interpreted.
     * </pre>
     *
     * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
     */
    public com.google.bigtable.v2.PartialResultSet.Builder getResultsBuilder() {
      return getResultsFieldBuilder().getBuilder();
    }
    /**
     *
     *
     * <pre>
     * A partial result set with row data potentially including additional
     * instructions on how recent past and future partial responses should be
     * interpreted.
     * </pre>
     *
     * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
     */
    @java.lang.Override
    public com.google.bigtable.v2.PartialResultSetOrBuilder getResultsOrBuilder() {
      if ((responseCase_ == 2) && (resultsBuilder_ != null)) {
        return resultsBuilder_.getMessageOrBuilder();
      } else {
        if (responseCase_ == 2) {
          return (com.google.bigtable.v2.PartialResultSet) response_;
        }
        return com.google.bigtable.v2.PartialResultSet.getDefaultInstance();
      }
    }
    /**
     *
     *
     * <pre>
     * A partial result set with row data potentially including additional
     * instructions on how recent past and future partial responses should be
     * interpreted.
     * </pre>
     *
     * <code>.google.bigtable.v2.PartialResultSet results = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilder<
            com.google.bigtable.v2.PartialResultSet,
            com.google.bigtable.v2.PartialResultSet.Builder,
            com.google.bigtable.v2.PartialResultSetOrBuilder>
        getResultsFieldBuilder() {
      if (resultsBuilder_ == null) {
        if (!(responseCase_ == 2)) {
          response_ = com.google.bigtable.v2.PartialResultSet.getDefaultInstance();
        }
        resultsBuilder_ =
            new com.google.protobuf.SingleFieldBuilder<
                com.google.bigtable.v2.PartialResultSet,
                com.google.bigtable.v2.PartialResultSet.Builder,
                com.google.bigtable.v2.PartialResultSetOrBuilder>(
                (com.google.bigtable.v2.PartialResultSet) response_,
                getParentForChildren(),
                isClean());
        response_ = null;
      }
      responseCase_ = 2;
      onChanged();
      return resultsBuilder_;
    }

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.ExecuteQueryResponse)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.ExecuteQueryResponse)
  private static final com.google.bigtable.v2.ExecuteQueryResponse DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.ExecuteQueryResponse();
  }

  public static com.google.bigtable.v2.ExecuteQueryResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ExecuteQueryResponse> PARSER =
      new com.google.protobuf.AbstractParser<ExecuteQueryResponse>() {
        @java.lang.Override
        public ExecuteQueryResponse parsePartialFrom(
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

  public static com.google.protobuf.Parser<ExecuteQueryResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ExecuteQueryResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.ExecuteQueryResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
