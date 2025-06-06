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
// source: google/bigtable/admin/v2/bigtable_table_admin.proto

// Protobuf Java Version: 3.25.8
package com.google.bigtable.admin.v2;

/**
 *
 *
 * <pre>
 * Response message for
 * [google.bigtable.admin.v2.BigtableTableAdmin.GenerateConsistencyToken][google.bigtable.admin.v2.BigtableTableAdmin.GenerateConsistencyToken]
 * </pre>
 *
 * Protobuf type {@code google.bigtable.admin.v2.GenerateConsistencyTokenResponse}
 */
public final class GenerateConsistencyTokenResponse extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.admin.v2.GenerateConsistencyTokenResponse)
    GenerateConsistencyTokenResponseOrBuilder {
  private static final long serialVersionUID = 0L;

  // Use GenerateConsistencyTokenResponse.newBuilder() to construct.
  private GenerateConsistencyTokenResponse(
      com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private GenerateConsistencyTokenResponse() {
    consistencyToken_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new GenerateConsistencyTokenResponse();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.admin.v2.BigtableTableAdminProto
        .internal_static_google_bigtable_admin_v2_GenerateConsistencyTokenResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.admin.v2.BigtableTableAdminProto
        .internal_static_google_bigtable_admin_v2_GenerateConsistencyTokenResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse.class,
            com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse.Builder.class);
  }

  public static final int CONSISTENCY_TOKEN_FIELD_NUMBER = 1;

  @SuppressWarnings("serial")
  private volatile java.lang.Object consistencyToken_ = "";

  /**
   *
   *
   * <pre>
   * The generated consistency token.
   * </pre>
   *
   * <code>string consistency_token = 1;</code>
   *
   * @return The consistencyToken.
   */
  @java.lang.Override
  public java.lang.String getConsistencyToken() {
    java.lang.Object ref = consistencyToken_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      consistencyToken_ = s;
      return s;
    }
  }

  /**
   *
   *
   * <pre>
   * The generated consistency token.
   * </pre>
   *
   * <code>string consistency_token = 1;</code>
   *
   * @return The bytes for consistencyToken.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getConsistencyTokenBytes() {
    java.lang.Object ref = consistencyToken_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
      consistencyToken_ = b;
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
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(consistencyToken_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, consistencyToken_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(consistencyToken_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, consistencyToken_);
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
    if (!(obj instanceof com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse)) {
      return super.equals(obj);
    }
    com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse other =
        (com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse) obj;

    if (!getConsistencyToken().equals(other.getConsistencyToken())) return false;
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
    hash = (37 * hash) + CONSISTENCY_TOKEN_FIELD_NUMBER;
    hash = (53 * hash) + getConsistencyToken().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(
      java.nio.ByteBuffer data) throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse parseFrom(
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

  public static Builder newBuilder(
      com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse prototype) {
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
   * Response message for
   * [google.bigtable.admin.v2.BigtableTableAdmin.GenerateConsistencyToken][google.bigtable.admin.v2.BigtableTableAdmin.GenerateConsistencyToken]
   * </pre>
   *
   * Protobuf type {@code google.bigtable.admin.v2.GenerateConsistencyTokenResponse}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.admin.v2.GenerateConsistencyTokenResponse)
      com.google.bigtable.admin.v2.GenerateConsistencyTokenResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.admin.v2.BigtableTableAdminProto
          .internal_static_google_bigtable_admin_v2_GenerateConsistencyTokenResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.admin.v2.BigtableTableAdminProto
          .internal_static_google_bigtable_admin_v2_GenerateConsistencyTokenResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse.class,
              com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse.Builder.class);
    }

    // Construct using com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      consistencyToken_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.admin.v2.BigtableTableAdminProto
          .internal_static_google_bigtable_admin_v2_GenerateConsistencyTokenResponse_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse
        getDefaultInstanceForType() {
      return com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse build() {
      com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse buildPartial() {
      com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse result =
          new com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(
        com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.consistencyToken_ = consistencyToken_;
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
      if (other instanceof com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse) {
        return mergeFrom((com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse other) {
      if (other
          == com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse.getDefaultInstance())
        return this;
      if (!other.getConsistencyToken().isEmpty()) {
        consistencyToken_ = other.consistencyToken_;
        bitField0_ |= 0x00000001;
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
                consistencyToken_ = input.readStringRequireUtf8();
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

    private java.lang.Object consistencyToken_ = "";

    /**
     *
     *
     * <pre>
     * The generated consistency token.
     * </pre>
     *
     * <code>string consistency_token = 1;</code>
     *
     * @return The consistencyToken.
     */
    public java.lang.String getConsistencyToken() {
      java.lang.Object ref = consistencyToken_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        consistencyToken_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }

    /**
     *
     *
     * <pre>
     * The generated consistency token.
     * </pre>
     *
     * <code>string consistency_token = 1;</code>
     *
     * @return The bytes for consistencyToken.
     */
    public com.google.protobuf.ByteString getConsistencyTokenBytes() {
      java.lang.Object ref = consistencyToken_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
        consistencyToken_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    /**
     *
     *
     * <pre>
     * The generated consistency token.
     * </pre>
     *
     * <code>string consistency_token = 1;</code>
     *
     * @param value The consistencyToken to set.
     * @return This builder for chaining.
     */
    public Builder setConsistencyToken(java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }
      consistencyToken_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }

    /**
     *
     *
     * <pre>
     * The generated consistency token.
     * </pre>
     *
     * <code>string consistency_token = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearConsistencyToken() {
      consistencyToken_ = getDefaultInstance().getConsistencyToken();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }

    /**
     *
     *
     * <pre>
     * The generated consistency token.
     * </pre>
     *
     * <code>string consistency_token = 1;</code>
     *
     * @param value The bytes for consistencyToken to set.
     * @return This builder for chaining.
     */
    public Builder setConsistencyTokenBytes(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      checkByteStringIsUtf8(value);
      consistencyToken_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
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

    // @@protoc_insertion_point(builder_scope:google.bigtable.admin.v2.GenerateConsistencyTokenResponse)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.admin.v2.GenerateConsistencyTokenResponse)
  private static final com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse
      DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse();
  }

  public static com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<GenerateConsistencyTokenResponse> PARSER =
      new com.google.protobuf.AbstractParser<GenerateConsistencyTokenResponse>() {
        @java.lang.Override
        public GenerateConsistencyTokenResponse parsePartialFrom(
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

  public static com.google.protobuf.Parser<GenerateConsistencyTokenResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<GenerateConsistencyTokenResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.admin.v2.GenerateConsistencyTokenResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
