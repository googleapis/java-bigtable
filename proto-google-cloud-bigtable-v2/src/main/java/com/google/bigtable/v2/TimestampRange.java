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
// source: google/bigtable/v2/data.proto
// Protobuf Java Version: 4.29.0

package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * Specified a contiguous range of microsecond timestamps.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.TimestampRange}
 */
public final class TimestampRange extends com.google.protobuf.GeneratedMessage
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.TimestampRange)
    TimestampRangeOrBuilder {
  private static final long serialVersionUID = 0L;

  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
        com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
        /* major= */ 4,
        /* minor= */ 29,
        /* patch= */ 0,
        /* suffix= */ "",
        TimestampRange.class.getName());
  }
  // Use TimestampRange.newBuilder() to construct.
  private TimestampRange(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }

  private TimestampRange() {}

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_TimestampRange_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_TimestampRange_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.TimestampRange.class,
            com.google.bigtable.v2.TimestampRange.Builder.class);
  }

  public static final int START_TIMESTAMP_MICROS_FIELD_NUMBER = 1;
  private long startTimestampMicros_ = 0L;
  /**
   *
   *
   * <pre>
   * Inclusive lower bound. If left empty, interpreted as 0.
   * </pre>
   *
   * <code>int64 start_timestamp_micros = 1;</code>
   *
   * @return The startTimestampMicros.
   */
  @java.lang.Override
  public long getStartTimestampMicros() {
    return startTimestampMicros_;
  }

  public static final int END_TIMESTAMP_MICROS_FIELD_NUMBER = 2;
  private long endTimestampMicros_ = 0L;
  /**
   *
   *
   * <pre>
   * Exclusive upper bound. If left empty, interpreted as infinity.
   * </pre>
   *
   * <code>int64 end_timestamp_micros = 2;</code>
   *
   * @return The endTimestampMicros.
   */
  @java.lang.Override
  public long getEndTimestampMicros() {
    return endTimestampMicros_;
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
    if (startTimestampMicros_ != 0L) {
      output.writeInt64(1, startTimestampMicros_);
    }
    if (endTimestampMicros_ != 0L) {
      output.writeInt64(2, endTimestampMicros_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (startTimestampMicros_ != 0L) {
      size += com.google.protobuf.CodedOutputStream.computeInt64Size(1, startTimestampMicros_);
    }
    if (endTimestampMicros_ != 0L) {
      size += com.google.protobuf.CodedOutputStream.computeInt64Size(2, endTimestampMicros_);
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
    if (!(obj instanceof com.google.bigtable.v2.TimestampRange)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.TimestampRange other = (com.google.bigtable.v2.TimestampRange) obj;

    if (getStartTimestampMicros() != other.getStartTimestampMicros()) return false;
    if (getEndTimestampMicros() != other.getEndTimestampMicros()) return false;
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
    hash = (37 * hash) + START_TIMESTAMP_MICROS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(getStartTimestampMicros());
    hash = (37 * hash) + END_TIMESTAMP_MICROS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(getEndTimestampMicros());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.TimestampRange parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.TimestampRange parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.TimestampRange parseFrom(
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

  public static Builder newBuilder(com.google.bigtable.v2.TimestampRange prototype) {
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
   * Specified a contiguous range of microsecond timestamps.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.TimestampRange}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.TimestampRange)
      com.google.bigtable.v2.TimestampRangeOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_TimestampRange_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_TimestampRange_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.TimestampRange.class,
              com.google.bigtable.v2.TimestampRange.Builder.class);
    }

    // Construct using com.google.bigtable.v2.TimestampRange.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      startTimestampMicros_ = 0L;
      endTimestampMicros_ = 0L;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_TimestampRange_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.TimestampRange getDefaultInstanceForType() {
      return com.google.bigtable.v2.TimestampRange.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.TimestampRange build() {
      com.google.bigtable.v2.TimestampRange result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.TimestampRange buildPartial() {
      com.google.bigtable.v2.TimestampRange result =
          new com.google.bigtable.v2.TimestampRange(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.bigtable.v2.TimestampRange result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.startTimestampMicros_ = startTimestampMicros_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.endTimestampMicros_ = endTimestampMicros_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.google.bigtable.v2.TimestampRange) {
        return mergeFrom((com.google.bigtable.v2.TimestampRange) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.TimestampRange other) {
      if (other == com.google.bigtable.v2.TimestampRange.getDefaultInstance()) return this;
      if (other.getStartTimestampMicros() != 0L) {
        setStartTimestampMicros(other.getStartTimestampMicros());
      }
      if (other.getEndTimestampMicros() != 0L) {
        setEndTimestampMicros(other.getEndTimestampMicros());
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
            case 8:
              {
                startTimestampMicros_ = input.readInt64();
                bitField0_ |= 0x00000001;
                break;
              } // case 8
            case 16:
              {
                endTimestampMicros_ = input.readInt64();
                bitField0_ |= 0x00000002;
                break;
              } // case 16
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

    private long startTimestampMicros_;
    /**
     *
     *
     * <pre>
     * Inclusive lower bound. If left empty, interpreted as 0.
     * </pre>
     *
     * <code>int64 start_timestamp_micros = 1;</code>
     *
     * @return The startTimestampMicros.
     */
    @java.lang.Override
    public long getStartTimestampMicros() {
      return startTimestampMicros_;
    }
    /**
     *
     *
     * <pre>
     * Inclusive lower bound. If left empty, interpreted as 0.
     * </pre>
     *
     * <code>int64 start_timestamp_micros = 1;</code>
     *
     * @param value The startTimestampMicros to set.
     * @return This builder for chaining.
     */
    public Builder setStartTimestampMicros(long value) {

      startTimestampMicros_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Inclusive lower bound. If left empty, interpreted as 0.
     * </pre>
     *
     * <code>int64 start_timestamp_micros = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearStartTimestampMicros() {
      bitField0_ = (bitField0_ & ~0x00000001);
      startTimestampMicros_ = 0L;
      onChanged();
      return this;
    }

    private long endTimestampMicros_;
    /**
     *
     *
     * <pre>
     * Exclusive upper bound. If left empty, interpreted as infinity.
     * </pre>
     *
     * <code>int64 end_timestamp_micros = 2;</code>
     *
     * @return The endTimestampMicros.
     */
    @java.lang.Override
    public long getEndTimestampMicros() {
      return endTimestampMicros_;
    }
    /**
     *
     *
     * <pre>
     * Exclusive upper bound. If left empty, interpreted as infinity.
     * </pre>
     *
     * <code>int64 end_timestamp_micros = 2;</code>
     *
     * @param value The endTimestampMicros to set.
     * @return This builder for chaining.
     */
    public Builder setEndTimestampMicros(long value) {

      endTimestampMicros_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Exclusive upper bound. If left empty, interpreted as infinity.
     * </pre>
     *
     * <code>int64 end_timestamp_micros = 2;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearEndTimestampMicros() {
      bitField0_ = (bitField0_ & ~0x00000002);
      endTimestampMicros_ = 0L;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.TimestampRange)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.TimestampRange)
  private static final com.google.bigtable.v2.TimestampRange DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.TimestampRange();
  }

  public static com.google.bigtable.v2.TimestampRange getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<TimestampRange> PARSER =
      new com.google.protobuf.AbstractParser<TimestampRange>() {
        @java.lang.Override
        public TimestampRange parsePartialFrom(
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

  public static com.google.protobuf.Parser<TimestampRange> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<TimestampRange> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.TimestampRange getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
