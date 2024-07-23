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

// Protobuf Java Version: 3.25.3
package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * Specifies a contiguous range of rows.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.RowRange}
 */
public final class RowRange extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.RowRange)
    RowRangeOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use RowRange.newBuilder() to construct.
  private RowRange(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private RowRange() {}

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new RowRange();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_RowRange_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_RowRange_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.RowRange.class, com.google.bigtable.v2.RowRange.Builder.class);
  }

  private int startKeyCase_ = 0;

  @SuppressWarnings("serial")
  private java.lang.Object startKey_;

  public enum StartKeyCase
      implements
          com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    START_KEY_CLOSED(1),
    START_KEY_OPEN(2),
    STARTKEY_NOT_SET(0);
    private final int value;

    private StartKeyCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static StartKeyCase valueOf(int value) {
      return forNumber(value);
    }

    public static StartKeyCase forNumber(int value) {
      switch (value) {
        case 1:
          return START_KEY_CLOSED;
        case 2:
          return START_KEY_OPEN;
        case 0:
          return STARTKEY_NOT_SET;
        default:
          return null;
      }
    }

    public int getNumber() {
      return this.value;
    }
  };

  public StartKeyCase getStartKeyCase() {
    return StartKeyCase.forNumber(startKeyCase_);
  }

  private int endKeyCase_ = 0;

  @SuppressWarnings("serial")
  private java.lang.Object endKey_;

  public enum EndKeyCase
      implements
          com.google.protobuf.Internal.EnumLite,
          com.google.protobuf.AbstractMessage.InternalOneOfEnum {
    END_KEY_OPEN(3),
    END_KEY_CLOSED(4),
    ENDKEY_NOT_SET(0);
    private final int value;

    private EndKeyCase(int value) {
      this.value = value;
    }
    /**
     * @param value The number of the enum to look for.
     * @return The enum associated with the given number.
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static EndKeyCase valueOf(int value) {
      return forNumber(value);
    }

    public static EndKeyCase forNumber(int value) {
      switch (value) {
        case 3:
          return END_KEY_OPEN;
        case 4:
          return END_KEY_CLOSED;
        case 0:
          return ENDKEY_NOT_SET;
        default:
          return null;
      }
    }

    public int getNumber() {
      return this.value;
    }
  };

  public EndKeyCase getEndKeyCase() {
    return EndKeyCase.forNumber(endKeyCase_);
  }

  public static final int START_KEY_CLOSED_FIELD_NUMBER = 1;
  /**
   *
   *
   * <pre>
   * Used when giving an inclusive lower bound for the range.
   * </pre>
   *
   * <code>bytes start_key_closed = 1;</code>
   *
   * @return Whether the startKeyClosed field is set.
   */
  @java.lang.Override
  public boolean hasStartKeyClosed() {
    return startKeyCase_ == 1;
  }
  /**
   *
   *
   * <pre>
   * Used when giving an inclusive lower bound for the range.
   * </pre>
   *
   * <code>bytes start_key_closed = 1;</code>
   *
   * @return The startKeyClosed.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getStartKeyClosed() {
    if (startKeyCase_ == 1) {
      return (com.google.protobuf.ByteString) startKey_;
    }
    return com.google.protobuf.ByteString.EMPTY;
  }

  public static final int START_KEY_OPEN_FIELD_NUMBER = 2;
  /**
   *
   *
   * <pre>
   * Used when giving an exclusive lower bound for the range.
   * </pre>
   *
   * <code>bytes start_key_open = 2;</code>
   *
   * @return Whether the startKeyOpen field is set.
   */
  @java.lang.Override
  public boolean hasStartKeyOpen() {
    return startKeyCase_ == 2;
  }
  /**
   *
   *
   * <pre>
   * Used when giving an exclusive lower bound for the range.
   * </pre>
   *
   * <code>bytes start_key_open = 2;</code>
   *
   * @return The startKeyOpen.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getStartKeyOpen() {
    if (startKeyCase_ == 2) {
      return (com.google.protobuf.ByteString) startKey_;
    }
    return com.google.protobuf.ByteString.EMPTY;
  }

  public static final int END_KEY_OPEN_FIELD_NUMBER = 3;
  /**
   *
   *
   * <pre>
   * Used when giving an exclusive upper bound for the range.
   * </pre>
   *
   * <code>bytes end_key_open = 3;</code>
   *
   * @return Whether the endKeyOpen field is set.
   */
  @java.lang.Override
  public boolean hasEndKeyOpen() {
    return endKeyCase_ == 3;
  }
  /**
   *
   *
   * <pre>
   * Used when giving an exclusive upper bound for the range.
   * </pre>
   *
   * <code>bytes end_key_open = 3;</code>
   *
   * @return The endKeyOpen.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getEndKeyOpen() {
    if (endKeyCase_ == 3) {
      return (com.google.protobuf.ByteString) endKey_;
    }
    return com.google.protobuf.ByteString.EMPTY;
  }

  public static final int END_KEY_CLOSED_FIELD_NUMBER = 4;
  /**
   *
   *
   * <pre>
   * Used when giving an inclusive upper bound for the range.
   * </pre>
   *
   * <code>bytes end_key_closed = 4;</code>
   *
   * @return Whether the endKeyClosed field is set.
   */
  @java.lang.Override
  public boolean hasEndKeyClosed() {
    return endKeyCase_ == 4;
  }
  /**
   *
   *
   * <pre>
   * Used when giving an inclusive upper bound for the range.
   * </pre>
   *
   * <code>bytes end_key_closed = 4;</code>
   *
   * @return The endKeyClosed.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getEndKeyClosed() {
    if (endKeyCase_ == 4) {
      return (com.google.protobuf.ByteString) endKey_;
    }
    return com.google.protobuf.ByteString.EMPTY;
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
    if (startKeyCase_ == 1) {
      output.writeBytes(1, (com.google.protobuf.ByteString) startKey_);
    }
    if (startKeyCase_ == 2) {
      output.writeBytes(2, (com.google.protobuf.ByteString) startKey_);
    }
    if (endKeyCase_ == 3) {
      output.writeBytes(3, (com.google.protobuf.ByteString) endKey_);
    }
    if (endKeyCase_ == 4) {
      output.writeBytes(4, (com.google.protobuf.ByteString) endKey_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (startKeyCase_ == 1) {
      size +=
          com.google.protobuf.CodedOutputStream.computeBytesSize(
              1, (com.google.protobuf.ByteString) startKey_);
    }
    if (startKeyCase_ == 2) {
      size +=
          com.google.protobuf.CodedOutputStream.computeBytesSize(
              2, (com.google.protobuf.ByteString) startKey_);
    }
    if (endKeyCase_ == 3) {
      size +=
          com.google.protobuf.CodedOutputStream.computeBytesSize(
              3, (com.google.protobuf.ByteString) endKey_);
    }
    if (endKeyCase_ == 4) {
      size +=
          com.google.protobuf.CodedOutputStream.computeBytesSize(
              4, (com.google.protobuf.ByteString) endKey_);
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
    if (!(obj instanceof com.google.bigtable.v2.RowRange)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.RowRange other = (com.google.bigtable.v2.RowRange) obj;

    if (!getStartKeyCase().equals(other.getStartKeyCase())) return false;
    switch (startKeyCase_) {
      case 1:
        if (!getStartKeyClosed().equals(other.getStartKeyClosed())) return false;
        break;
      case 2:
        if (!getStartKeyOpen().equals(other.getStartKeyOpen())) return false;
        break;
      case 0:
      default:
    }
    if (!getEndKeyCase().equals(other.getEndKeyCase())) return false;
    switch (endKeyCase_) {
      case 3:
        if (!getEndKeyOpen().equals(other.getEndKeyOpen())) return false;
        break;
      case 4:
        if (!getEndKeyClosed().equals(other.getEndKeyClosed())) return false;
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
    switch (startKeyCase_) {
      case 1:
        hash = (37 * hash) + START_KEY_CLOSED_FIELD_NUMBER;
        hash = (53 * hash) + getStartKeyClosed().hashCode();
        break;
      case 2:
        hash = (37 * hash) + START_KEY_OPEN_FIELD_NUMBER;
        hash = (53 * hash) + getStartKeyOpen().hashCode();
        break;
      case 0:
      default:
    }
    switch (endKeyCase_) {
      case 3:
        hash = (37 * hash) + END_KEY_OPEN_FIELD_NUMBER;
        hash = (53 * hash) + getEndKeyOpen().hashCode();
        break;
      case 4:
        hash = (37 * hash) + END_KEY_CLOSED_FIELD_NUMBER;
        hash = (53 * hash) + getEndKeyClosed().hashCode();
        break;
      case 0:
      default:
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.RowRange parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.RowRange parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.RowRange parseFrom(com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.RowRange parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.RowRange parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.RowRange parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.RowRange parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.RowRange parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.RowRange parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.RowRange parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.RowRange parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.RowRange parseFrom(
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

  public static Builder newBuilder(com.google.bigtable.v2.RowRange prototype) {
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
   * Specifies a contiguous range of rows.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.RowRange}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.RowRange)
      com.google.bigtable.v2.RowRangeOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_RowRange_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_RowRange_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.RowRange.class, com.google.bigtable.v2.RowRange.Builder.class);
    }

    // Construct using com.google.bigtable.v2.RowRange.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      startKeyCase_ = 0;
      startKey_ = null;
      endKeyCase_ = 0;
      endKey_ = null;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_RowRange_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.RowRange getDefaultInstanceForType() {
      return com.google.bigtable.v2.RowRange.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.RowRange build() {
      com.google.bigtable.v2.RowRange result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.RowRange buildPartial() {
      com.google.bigtable.v2.RowRange result = new com.google.bigtable.v2.RowRange(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      buildPartialOneofs(result);
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.bigtable.v2.RowRange result) {
      int from_bitField0_ = bitField0_;
    }

    private void buildPartialOneofs(com.google.bigtable.v2.RowRange result) {
      result.startKeyCase_ = startKeyCase_;
      result.startKey_ = this.startKey_;
      result.endKeyCase_ = endKeyCase_;
      result.endKey_ = this.endKey_;
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
      if (other instanceof com.google.bigtable.v2.RowRange) {
        return mergeFrom((com.google.bigtable.v2.RowRange) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.RowRange other) {
      if (other == com.google.bigtable.v2.RowRange.getDefaultInstance()) return this;
      switch (other.getStartKeyCase()) {
        case START_KEY_CLOSED:
          {
            setStartKeyClosed(other.getStartKeyClosed());
            break;
          }
        case START_KEY_OPEN:
          {
            setStartKeyOpen(other.getStartKeyOpen());
            break;
          }
        case STARTKEY_NOT_SET:
          {
            break;
          }
      }
      switch (other.getEndKeyCase()) {
        case END_KEY_OPEN:
          {
            setEndKeyOpen(other.getEndKeyOpen());
            break;
          }
        case END_KEY_CLOSED:
          {
            setEndKeyClosed(other.getEndKeyClosed());
            break;
          }
        case ENDKEY_NOT_SET:
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
                startKey_ = input.readBytes();
                startKeyCase_ = 1;
                break;
              } // case 10
            case 18:
              {
                startKey_ = input.readBytes();
                startKeyCase_ = 2;
                break;
              } // case 18
            case 26:
              {
                endKey_ = input.readBytes();
                endKeyCase_ = 3;
                break;
              } // case 26
            case 34:
              {
                endKey_ = input.readBytes();
                endKeyCase_ = 4;
                break;
              } // case 34
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

    private int startKeyCase_ = 0;
    private java.lang.Object startKey_;

    public StartKeyCase getStartKeyCase() {
      return StartKeyCase.forNumber(startKeyCase_);
    }

    public Builder clearStartKey() {
      startKeyCase_ = 0;
      startKey_ = null;
      onChanged();
      return this;
    }

    private int endKeyCase_ = 0;
    private java.lang.Object endKey_;

    public EndKeyCase getEndKeyCase() {
      return EndKeyCase.forNumber(endKeyCase_);
    }

    public Builder clearEndKey() {
      endKeyCase_ = 0;
      endKey_ = null;
      onChanged();
      return this;
    }

    private int bitField0_;

    /**
     *
     *
     * <pre>
     * Used when giving an inclusive lower bound for the range.
     * </pre>
     *
     * <code>bytes start_key_closed = 1;</code>
     *
     * @return Whether the startKeyClosed field is set.
     */
    public boolean hasStartKeyClosed() {
      return startKeyCase_ == 1;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an inclusive lower bound for the range.
     * </pre>
     *
     * <code>bytes start_key_closed = 1;</code>
     *
     * @return The startKeyClosed.
     */
    public com.google.protobuf.ByteString getStartKeyClosed() {
      if (startKeyCase_ == 1) {
        return (com.google.protobuf.ByteString) startKey_;
      }
      return com.google.protobuf.ByteString.EMPTY;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an inclusive lower bound for the range.
     * </pre>
     *
     * <code>bytes start_key_closed = 1;</code>
     *
     * @param value The startKeyClosed to set.
     * @return This builder for chaining.
     */
    public Builder setStartKeyClosed(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      startKeyCase_ = 1;
      startKey_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an inclusive lower bound for the range.
     * </pre>
     *
     * <code>bytes start_key_closed = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearStartKeyClosed() {
      if (startKeyCase_ == 1) {
        startKeyCase_ = 0;
        startKey_ = null;
        onChanged();
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * Used when giving an exclusive lower bound for the range.
     * </pre>
     *
     * <code>bytes start_key_open = 2;</code>
     *
     * @return Whether the startKeyOpen field is set.
     */
    public boolean hasStartKeyOpen() {
      return startKeyCase_ == 2;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an exclusive lower bound for the range.
     * </pre>
     *
     * <code>bytes start_key_open = 2;</code>
     *
     * @return The startKeyOpen.
     */
    public com.google.protobuf.ByteString getStartKeyOpen() {
      if (startKeyCase_ == 2) {
        return (com.google.protobuf.ByteString) startKey_;
      }
      return com.google.protobuf.ByteString.EMPTY;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an exclusive lower bound for the range.
     * </pre>
     *
     * <code>bytes start_key_open = 2;</code>
     *
     * @param value The startKeyOpen to set.
     * @return This builder for chaining.
     */
    public Builder setStartKeyOpen(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      startKeyCase_ = 2;
      startKey_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an exclusive lower bound for the range.
     * </pre>
     *
     * <code>bytes start_key_open = 2;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearStartKeyOpen() {
      if (startKeyCase_ == 2) {
        startKeyCase_ = 0;
        startKey_ = null;
        onChanged();
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * Used when giving an exclusive upper bound for the range.
     * </pre>
     *
     * <code>bytes end_key_open = 3;</code>
     *
     * @return Whether the endKeyOpen field is set.
     */
    public boolean hasEndKeyOpen() {
      return endKeyCase_ == 3;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an exclusive upper bound for the range.
     * </pre>
     *
     * <code>bytes end_key_open = 3;</code>
     *
     * @return The endKeyOpen.
     */
    public com.google.protobuf.ByteString getEndKeyOpen() {
      if (endKeyCase_ == 3) {
        return (com.google.protobuf.ByteString) endKey_;
      }
      return com.google.protobuf.ByteString.EMPTY;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an exclusive upper bound for the range.
     * </pre>
     *
     * <code>bytes end_key_open = 3;</code>
     *
     * @param value The endKeyOpen to set.
     * @return This builder for chaining.
     */
    public Builder setEndKeyOpen(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      endKeyCase_ = 3;
      endKey_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an exclusive upper bound for the range.
     * </pre>
     *
     * <code>bytes end_key_open = 3;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearEndKeyOpen() {
      if (endKeyCase_ == 3) {
        endKeyCase_ = 0;
        endKey_ = null;
        onChanged();
      }
      return this;
    }

    /**
     *
     *
     * <pre>
     * Used when giving an inclusive upper bound for the range.
     * </pre>
     *
     * <code>bytes end_key_closed = 4;</code>
     *
     * @return Whether the endKeyClosed field is set.
     */
    public boolean hasEndKeyClosed() {
      return endKeyCase_ == 4;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an inclusive upper bound for the range.
     * </pre>
     *
     * <code>bytes end_key_closed = 4;</code>
     *
     * @return The endKeyClosed.
     */
    public com.google.protobuf.ByteString getEndKeyClosed() {
      if (endKeyCase_ == 4) {
        return (com.google.protobuf.ByteString) endKey_;
      }
      return com.google.protobuf.ByteString.EMPTY;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an inclusive upper bound for the range.
     * </pre>
     *
     * <code>bytes end_key_closed = 4;</code>
     *
     * @param value The endKeyClosed to set.
     * @return This builder for chaining.
     */
    public Builder setEndKeyClosed(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      endKeyCase_ = 4;
      endKey_ = value;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Used when giving an inclusive upper bound for the range.
     * </pre>
     *
     * <code>bytes end_key_closed = 4;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearEndKeyClosed() {
      if (endKeyCase_ == 4) {
        endKeyCase_ = 0;
        endKey_ = null;
        onChanged();
      }
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

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.RowRange)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.RowRange)
  private static final com.google.bigtable.v2.RowRange DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.RowRange();
  }

  public static com.google.bigtable.v2.RowRange getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<RowRange> PARSER =
      new com.google.protobuf.AbstractParser<RowRange>() {
        @java.lang.Override
        public RowRange parsePartialFrom(
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

  public static com.google.protobuf.Parser<RowRange> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<RowRange> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.RowRange getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
