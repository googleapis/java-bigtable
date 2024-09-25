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

// Protobuf Java Version: 3.25.4
package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * Specifies (some of) the contents of a single row/column/timestamp of a table.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.Cell}
 */
public final class Cell extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.Cell)
    CellOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use Cell.newBuilder() to construct.
  private Cell(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private Cell() {
    value_ = com.google.protobuf.ByteString.EMPTY;
    labels_ = com.google.protobuf.LazyStringArrayList.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new Cell();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_Cell_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_Cell_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.Cell.class, com.google.bigtable.v2.Cell.Builder.class);
  }

  public static final int TIMESTAMP_MICROS_FIELD_NUMBER = 1;
  private long timestampMicros_ = 0L;
  /**
   *
   *
   * <pre>
   * The cell's stored timestamp, which also uniquely identifies it within
   * its column.
   * Values are always expressed in microseconds, but individual tables may set
   * a coarser granularity to further restrict the allowed values. For
   * example, a table which specifies millisecond granularity will only allow
   * values of `timestamp_micros` which are multiples of 1000.
   * </pre>
   *
   * <code>int64 timestamp_micros = 1;</code>
   *
   * @return The timestampMicros.
   */
  @java.lang.Override
  public long getTimestampMicros() {
    return timestampMicros_;
  }

  public static final int VALUE_FIELD_NUMBER = 2;
  private com.google.protobuf.ByteString value_ = com.google.protobuf.ByteString.EMPTY;
  /**
   *
   *
   * <pre>
   * The value stored in the cell.
   * May contain any byte string, including the empty string, up to 100MiB in
   * length.
   * </pre>
   *
   * <code>bytes value = 2;</code>
   *
   * @return The value.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getValue() {
    return value_;
  }

  public static final int LABELS_FIELD_NUMBER = 3;

  @SuppressWarnings("serial")
  private com.google.protobuf.LazyStringArrayList labels_ =
      com.google.protobuf.LazyStringArrayList.emptyList();
  /**
   *
   *
   * <pre>
   * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
   * </pre>
   *
   * <code>repeated string labels = 3;</code>
   *
   * @return A list containing the labels.
   */
  public com.google.protobuf.ProtocolStringList getLabelsList() {
    return labels_;
  }
  /**
   *
   *
   * <pre>
   * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
   * </pre>
   *
   * <code>repeated string labels = 3;</code>
   *
   * @return The count of labels.
   */
  public int getLabelsCount() {
    return labels_.size();
  }
  /**
   *
   *
   * <pre>
   * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
   * </pre>
   *
   * <code>repeated string labels = 3;</code>
   *
   * @param index The index of the element to return.
   * @return The labels at the given index.
   */
  public java.lang.String getLabels(int index) {
    return labels_.get(index);
  }
  /**
   *
   *
   * <pre>
   * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
   * </pre>
   *
   * <code>repeated string labels = 3;</code>
   *
   * @param index The index of the value to return.
   * @return The bytes of the labels at the given index.
   */
  public com.google.protobuf.ByteString getLabelsBytes(int index) {
    return labels_.getByteString(index);
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
    if (timestampMicros_ != 0L) {
      output.writeInt64(1, timestampMicros_);
    }
    if (!value_.isEmpty()) {
      output.writeBytes(2, value_);
    }
    for (int i = 0; i < labels_.size(); i++) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 3, labels_.getRaw(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (timestampMicros_ != 0L) {
      size += com.google.protobuf.CodedOutputStream.computeInt64Size(1, timestampMicros_);
    }
    if (!value_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream.computeBytesSize(2, value_);
    }
    {
      int dataSize = 0;
      for (int i = 0; i < labels_.size(); i++) {
        dataSize += computeStringSizeNoTag(labels_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getLabelsList().size();
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
    if (!(obj instanceof com.google.bigtable.v2.Cell)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.Cell other = (com.google.bigtable.v2.Cell) obj;

    if (getTimestampMicros() != other.getTimestampMicros()) return false;
    if (!getValue().equals(other.getValue())) return false;
    if (!getLabelsList().equals(other.getLabelsList())) return false;
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
    hash = (37 * hash) + TIMESTAMP_MICROS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(getTimestampMicros());
    hash = (37 * hash) + VALUE_FIELD_NUMBER;
    hash = (53 * hash) + getValue().hashCode();
    if (getLabelsCount() > 0) {
      hash = (37 * hash) + LABELS_FIELD_NUMBER;
      hash = (53 * hash) + getLabelsList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.Cell parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.Cell parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.Cell parseFrom(com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.Cell parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.Cell parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.Cell parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.Cell parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.Cell parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.Cell parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.Cell parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.Cell parseFrom(com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.Cell parseFrom(
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

  public static Builder newBuilder(com.google.bigtable.v2.Cell prototype) {
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
   * Specifies (some of) the contents of a single row/column/timestamp of a table.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.Cell}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.Cell)
      com.google.bigtable.v2.CellOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_Cell_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_Cell_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.Cell.class, com.google.bigtable.v2.Cell.Builder.class);
    }

    // Construct using com.google.bigtable.v2.Cell.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      timestampMicros_ = 0L;
      value_ = com.google.protobuf.ByteString.EMPTY;
      labels_ = com.google.protobuf.LazyStringArrayList.emptyList();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_Cell_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.Cell getDefaultInstanceForType() {
      return com.google.bigtable.v2.Cell.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.Cell build() {
      com.google.bigtable.v2.Cell result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.Cell buildPartial() {
      com.google.bigtable.v2.Cell result = new com.google.bigtable.v2.Cell(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.bigtable.v2.Cell result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.timestampMicros_ = timestampMicros_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.value_ = value_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        labels_.makeImmutable();
        result.labels_ = labels_;
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
      if (other instanceof com.google.bigtable.v2.Cell) {
        return mergeFrom((com.google.bigtable.v2.Cell) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.Cell other) {
      if (other == com.google.bigtable.v2.Cell.getDefaultInstance()) return this;
      if (other.getTimestampMicros() != 0L) {
        setTimestampMicros(other.getTimestampMicros());
      }
      if (other.getValue() != com.google.protobuf.ByteString.EMPTY) {
        setValue(other.getValue());
      }
      if (!other.labels_.isEmpty()) {
        if (labels_.isEmpty()) {
          labels_ = other.labels_;
          bitField0_ |= 0x00000004;
        } else {
          ensureLabelsIsMutable();
          labels_.addAll(other.labels_);
        }
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
            case 8:
              {
                timestampMicros_ = input.readInt64();
                bitField0_ |= 0x00000001;
                break;
              } // case 8
            case 18:
              {
                value_ = input.readBytes();
                bitField0_ |= 0x00000002;
                break;
              } // case 18
            case 26:
              {
                java.lang.String s = input.readStringRequireUtf8();
                ensureLabelsIsMutable();
                labels_.add(s);
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

    private long timestampMicros_;
    /**
     *
     *
     * <pre>
     * The cell's stored timestamp, which also uniquely identifies it within
     * its column.
     * Values are always expressed in microseconds, but individual tables may set
     * a coarser granularity to further restrict the allowed values. For
     * example, a table which specifies millisecond granularity will only allow
     * values of `timestamp_micros` which are multiples of 1000.
     * </pre>
     *
     * <code>int64 timestamp_micros = 1;</code>
     *
     * @return The timestampMicros.
     */
    @java.lang.Override
    public long getTimestampMicros() {
      return timestampMicros_;
    }
    /**
     *
     *
     * <pre>
     * The cell's stored timestamp, which also uniquely identifies it within
     * its column.
     * Values are always expressed in microseconds, but individual tables may set
     * a coarser granularity to further restrict the allowed values. For
     * example, a table which specifies millisecond granularity will only allow
     * values of `timestamp_micros` which are multiples of 1000.
     * </pre>
     *
     * <code>int64 timestamp_micros = 1;</code>
     *
     * @param value The timestampMicros to set.
     * @return This builder for chaining.
     */
    public Builder setTimestampMicros(long value) {

      timestampMicros_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The cell's stored timestamp, which also uniquely identifies it within
     * its column.
     * Values are always expressed in microseconds, but individual tables may set
     * a coarser granularity to further restrict the allowed values. For
     * example, a table which specifies millisecond granularity will only allow
     * values of `timestamp_micros` which are multiples of 1000.
     * </pre>
     *
     * <code>int64 timestamp_micros = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearTimestampMicros() {
      bitField0_ = (bitField0_ & ~0x00000001);
      timestampMicros_ = 0L;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString value_ = com.google.protobuf.ByteString.EMPTY;
    /**
     *
     *
     * <pre>
     * The value stored in the cell.
     * May contain any byte string, including the empty string, up to 100MiB in
     * length.
     * </pre>
     *
     * <code>bytes value = 2;</code>
     *
     * @return The value.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getValue() {
      return value_;
    }
    /**
     *
     *
     * <pre>
     * The value stored in the cell.
     * May contain any byte string, including the empty string, up to 100MiB in
     * length.
     * </pre>
     *
     * <code>bytes value = 2;</code>
     *
     * @param value The value to set.
     * @return This builder for chaining.
     */
    public Builder setValue(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      value_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The value stored in the cell.
     * May contain any byte string, including the empty string, up to 100MiB in
     * length.
     * </pre>
     *
     * <code>bytes value = 2;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearValue() {
      bitField0_ = (bitField0_ & ~0x00000002);
      value_ = getDefaultInstance().getValue();
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringArrayList labels_ =
        com.google.protobuf.LazyStringArrayList.emptyList();

    private void ensureLabelsIsMutable() {
      if (!labels_.isModifiable()) {
        labels_ = new com.google.protobuf.LazyStringArrayList(labels_);
      }
      bitField0_ |= 0x00000004;
    }
    /**
     *
     *
     * <pre>
     * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
     * </pre>
     *
     * <code>repeated string labels = 3;</code>
     *
     * @return A list containing the labels.
     */
    public com.google.protobuf.ProtocolStringList getLabelsList() {
      labels_.makeImmutable();
      return labels_;
    }
    /**
     *
     *
     * <pre>
     * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
     * </pre>
     *
     * <code>repeated string labels = 3;</code>
     *
     * @return The count of labels.
     */
    public int getLabelsCount() {
      return labels_.size();
    }
    /**
     *
     *
     * <pre>
     * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
     * </pre>
     *
     * <code>repeated string labels = 3;</code>
     *
     * @param index The index of the element to return.
     * @return The labels at the given index.
     */
    public java.lang.String getLabels(int index) {
      return labels_.get(index);
    }
    /**
     *
     *
     * <pre>
     * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
     * </pre>
     *
     * <code>repeated string labels = 3;</code>
     *
     * @param index The index of the value to return.
     * @return The bytes of the labels at the given index.
     */
    public com.google.protobuf.ByteString getLabelsBytes(int index) {
      return labels_.getByteString(index);
    }
    /**
     *
     *
     * <pre>
     * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
     * </pre>
     *
     * <code>repeated string labels = 3;</code>
     *
     * @param index The index to set the value at.
     * @param value The labels to set.
     * @return This builder for chaining.
     */
    public Builder setLabels(int index, java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureLabelsIsMutable();
      labels_.set(index, value);
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
     * </pre>
     *
     * <code>repeated string labels = 3;</code>
     *
     * @param value The labels to add.
     * @return This builder for chaining.
     */
    public Builder addLabels(java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }
      ensureLabelsIsMutable();
      labels_.add(value);
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
     * </pre>
     *
     * <code>repeated string labels = 3;</code>
     *
     * @param values The labels to add.
     * @return This builder for chaining.
     */
    public Builder addAllLabels(java.lang.Iterable<java.lang.String> values) {
      ensureLabelsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(values, labels_);
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
     * </pre>
     *
     * <code>repeated string labels = 3;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearLabels() {
      labels_ = com.google.protobuf.LazyStringArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000004);
      ;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Labels applied to the cell by a [RowFilter][google.bigtable.v2.RowFilter].
     * </pre>
     *
     * <code>repeated string labels = 3;</code>
     *
     * @param value The bytes of the labels to add.
     * @return This builder for chaining.
     */
    public Builder addLabelsBytes(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      checkByteStringIsUtf8(value);
      ensureLabelsIsMutable();
      labels_.add(value);
      bitField0_ |= 0x00000004;
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

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.Cell)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.Cell)
  private static final com.google.bigtable.v2.Cell DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.Cell();
  }

  public static com.google.bigtable.v2.Cell getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Cell> PARSER =
      new com.google.protobuf.AbstractParser<Cell>() {
        @java.lang.Override
        public Cell parsePartialFrom(
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

  public static com.google.protobuf.Parser<Cell> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Cell> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.Cell getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
