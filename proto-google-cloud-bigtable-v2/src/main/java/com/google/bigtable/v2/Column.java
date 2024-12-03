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

// Protobuf Java Version: 3.25.5
package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * Specifies (some of) the contents of a single row/column intersection of a
 * table.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.Column}
 */
public final class Column extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.Column)
    ColumnOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use Column.newBuilder() to construct.
  private Column(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private Column() {
    qualifier_ = com.google.protobuf.ByteString.EMPTY;
    cells_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new Column();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_Column_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.DataProto
        .internal_static_google_bigtable_v2_Column_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.Column.class, com.google.bigtable.v2.Column.Builder.class);
  }

  public static final int QUALIFIER_FIELD_NUMBER = 1;
  private com.google.protobuf.ByteString qualifier_ = com.google.protobuf.ByteString.EMPTY;
  /**
   *
   *
   * <pre>
   * The unique key which identifies this column within its family. This is the
   * same key that's used to identify the column in, for example, a RowFilter
   * which sets its `column_qualifier_regex_filter` field.
   * May contain any byte string, including the empty string, up to 16kiB in
   * length.
   * </pre>
   *
   * <code>bytes qualifier = 1;</code>
   *
   * @return The qualifier.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getQualifier() {
    return qualifier_;
  }

  public static final int CELLS_FIELD_NUMBER = 2;

  @SuppressWarnings("serial")
  private java.util.List<com.google.bigtable.v2.Cell> cells_;
  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of decreasing "timestamp_micros".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
   */
  @java.lang.Override
  public java.util.List<com.google.bigtable.v2.Cell> getCellsList() {
    return cells_;
  }
  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of decreasing "timestamp_micros".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.google.bigtable.v2.CellOrBuilder> getCellsOrBuilderList() {
    return cells_;
  }
  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of decreasing "timestamp_micros".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
   */
  @java.lang.Override
  public int getCellsCount() {
    return cells_.size();
  }
  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of decreasing "timestamp_micros".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.Cell getCells(int index) {
    return cells_.get(index);
  }
  /**
   *
   *
   * <pre>
   * Must not be empty. Sorted in order of decreasing "timestamp_micros".
   * </pre>
   *
   * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.CellOrBuilder getCellsOrBuilder(int index) {
    return cells_.get(index);
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
    if (!qualifier_.isEmpty()) {
      output.writeBytes(1, qualifier_);
    }
    for (int i = 0; i < cells_.size(); i++) {
      output.writeMessage(2, cells_.get(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!qualifier_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, qualifier_);
    }
    for (int i = 0; i < cells_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(2, cells_.get(i));
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
    if (!(obj instanceof com.google.bigtable.v2.Column)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.Column other = (com.google.bigtable.v2.Column) obj;

    if (!getQualifier().equals(other.getQualifier())) return false;
    if (!getCellsList().equals(other.getCellsList())) return false;
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
    hash = (37 * hash) + QUALIFIER_FIELD_NUMBER;
    hash = (53 * hash) + getQualifier().hashCode();
    if (getCellsCount() > 0) {
      hash = (37 * hash) + CELLS_FIELD_NUMBER;
      hash = (53 * hash) + getCellsList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.Column parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.Column parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.Column parseFrom(com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.Column parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.Column parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.Column parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.Column parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.Column parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.Column parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.Column parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.Column parseFrom(com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.Column parseFrom(
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

  public static Builder newBuilder(com.google.bigtable.v2.Column prototype) {
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
   * Specifies (some of) the contents of a single row/column intersection of a
   * table.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.Column}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.Column)
      com.google.bigtable.v2.ColumnOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_Column_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.DataProto
          .internal_static_google_bigtable_v2_Column_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.Column.class, com.google.bigtable.v2.Column.Builder.class);
    }

    // Construct using com.google.bigtable.v2.Column.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      qualifier_ = com.google.protobuf.ByteString.EMPTY;
      if (cellsBuilder_ == null) {
        cells_ = java.util.Collections.emptyList();
      } else {
        cells_ = null;
        cellsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000002);
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.DataProto.internal_static_google_bigtable_v2_Column_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.Column getDefaultInstanceForType() {
      return com.google.bigtable.v2.Column.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.Column build() {
      com.google.bigtable.v2.Column result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.Column buildPartial() {
      com.google.bigtable.v2.Column result = new com.google.bigtable.v2.Column(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(com.google.bigtable.v2.Column result) {
      if (cellsBuilder_ == null) {
        if (((bitField0_ & 0x00000002) != 0)) {
          cells_ = java.util.Collections.unmodifiableList(cells_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.cells_ = cells_;
      } else {
        result.cells_ = cellsBuilder_.build();
      }
    }

    private void buildPartial0(com.google.bigtable.v2.Column result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.qualifier_ = qualifier_;
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
      if (other instanceof com.google.bigtable.v2.Column) {
        return mergeFrom((com.google.bigtable.v2.Column) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.Column other) {
      if (other == com.google.bigtable.v2.Column.getDefaultInstance()) return this;
      if (other.getQualifier() != com.google.protobuf.ByteString.EMPTY) {
        setQualifier(other.getQualifier());
      }
      if (cellsBuilder_ == null) {
        if (!other.cells_.isEmpty()) {
          if (cells_.isEmpty()) {
            cells_ = other.cells_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureCellsIsMutable();
            cells_.addAll(other.cells_);
          }
          onChanged();
        }
      } else {
        if (!other.cells_.isEmpty()) {
          if (cellsBuilder_.isEmpty()) {
            cellsBuilder_.dispose();
            cellsBuilder_ = null;
            cells_ = other.cells_;
            bitField0_ = (bitField0_ & ~0x00000002);
            cellsBuilder_ =
                com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders
                    ? getCellsFieldBuilder()
                    : null;
          } else {
            cellsBuilder_.addAllMessages(other.cells_);
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
                qualifier_ = input.readBytes();
                bitField0_ |= 0x00000001;
                break;
              } // case 10
            case 18:
              {
                com.google.bigtable.v2.Cell m =
                    input.readMessage(com.google.bigtable.v2.Cell.parser(), extensionRegistry);
                if (cellsBuilder_ == null) {
                  ensureCellsIsMutable();
                  cells_.add(m);
                } else {
                  cellsBuilder_.addMessage(m);
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

    private com.google.protobuf.ByteString qualifier_ = com.google.protobuf.ByteString.EMPTY;
    /**
     *
     *
     * <pre>
     * The unique key which identifies this column within its family. This is the
     * same key that's used to identify the column in, for example, a RowFilter
     * which sets its `column_qualifier_regex_filter` field.
     * May contain any byte string, including the empty string, up to 16kiB in
     * length.
     * </pre>
     *
     * <code>bytes qualifier = 1;</code>
     *
     * @return The qualifier.
     */
    @java.lang.Override
    public com.google.protobuf.ByteString getQualifier() {
      return qualifier_;
    }
    /**
     *
     *
     * <pre>
     * The unique key which identifies this column within its family. This is the
     * same key that's used to identify the column in, for example, a RowFilter
     * which sets its `column_qualifier_regex_filter` field.
     * May contain any byte string, including the empty string, up to 16kiB in
     * length.
     * </pre>
     *
     * <code>bytes qualifier = 1;</code>
     *
     * @param value The qualifier to set.
     * @return This builder for chaining.
     */
    public Builder setQualifier(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      qualifier_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * The unique key which identifies this column within its family. This is the
     * same key that's used to identify the column in, for example, a RowFilter
     * which sets its `column_qualifier_regex_filter` field.
     * May contain any byte string, including the empty string, up to 16kiB in
     * length.
     * </pre>
     *
     * <code>bytes qualifier = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearQualifier() {
      bitField0_ = (bitField0_ & ~0x00000001);
      qualifier_ = getDefaultInstance().getQualifier();
      onChanged();
      return this;
    }

    private java.util.List<com.google.bigtable.v2.Cell> cells_ = java.util.Collections.emptyList();

    private void ensureCellsIsMutable() {
      if (!((bitField0_ & 0x00000002) != 0)) {
        cells_ = new java.util.ArrayList<com.google.bigtable.v2.Cell>(cells_);
        bitField0_ |= 0x00000002;
      }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
            com.google.bigtable.v2.Cell,
            com.google.bigtable.v2.Cell.Builder,
            com.google.bigtable.v2.CellOrBuilder>
        cellsBuilder_;

    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public java.util.List<com.google.bigtable.v2.Cell> getCellsList() {
      if (cellsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(cells_);
      } else {
        return cellsBuilder_.getMessageList();
      }
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public int getCellsCount() {
      if (cellsBuilder_ == null) {
        return cells_.size();
      } else {
        return cellsBuilder_.getCount();
      }
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public com.google.bigtable.v2.Cell getCells(int index) {
      if (cellsBuilder_ == null) {
        return cells_.get(index);
      } else {
        return cellsBuilder_.getMessage(index);
      }
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public Builder setCells(int index, com.google.bigtable.v2.Cell value) {
      if (cellsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCellsIsMutable();
        cells_.set(index, value);
        onChanged();
      } else {
        cellsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public Builder setCells(int index, com.google.bigtable.v2.Cell.Builder builderForValue) {
      if (cellsBuilder_ == null) {
        ensureCellsIsMutable();
        cells_.set(index, builderForValue.build());
        onChanged();
      } else {
        cellsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public Builder addCells(com.google.bigtable.v2.Cell value) {
      if (cellsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCellsIsMutable();
        cells_.add(value);
        onChanged();
      } else {
        cellsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public Builder addCells(int index, com.google.bigtable.v2.Cell value) {
      if (cellsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureCellsIsMutable();
        cells_.add(index, value);
        onChanged();
      } else {
        cellsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public Builder addCells(com.google.bigtable.v2.Cell.Builder builderForValue) {
      if (cellsBuilder_ == null) {
        ensureCellsIsMutable();
        cells_.add(builderForValue.build());
        onChanged();
      } else {
        cellsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public Builder addCells(int index, com.google.bigtable.v2.Cell.Builder builderForValue) {
      if (cellsBuilder_ == null) {
        ensureCellsIsMutable();
        cells_.add(index, builderForValue.build());
        onChanged();
      } else {
        cellsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public Builder addAllCells(java.lang.Iterable<? extends com.google.bigtable.v2.Cell> values) {
      if (cellsBuilder_ == null) {
        ensureCellsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(values, cells_);
        onChanged();
      } else {
        cellsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public Builder clearCells() {
      if (cellsBuilder_ == null) {
        cells_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
      } else {
        cellsBuilder_.clear();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public Builder removeCells(int index) {
      if (cellsBuilder_ == null) {
        ensureCellsIsMutable();
        cells_.remove(index);
        onChanged();
      } else {
        cellsBuilder_.remove(index);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public com.google.bigtable.v2.Cell.Builder getCellsBuilder(int index) {
      return getCellsFieldBuilder().getBuilder(index);
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public com.google.bigtable.v2.CellOrBuilder getCellsOrBuilder(int index) {
      if (cellsBuilder_ == null) {
        return cells_.get(index);
      } else {
        return cellsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public java.util.List<? extends com.google.bigtable.v2.CellOrBuilder> getCellsOrBuilderList() {
      if (cellsBuilder_ != null) {
        return cellsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(cells_);
      }
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public com.google.bigtable.v2.Cell.Builder addCellsBuilder() {
      return getCellsFieldBuilder().addBuilder(com.google.bigtable.v2.Cell.getDefaultInstance());
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public com.google.bigtable.v2.Cell.Builder addCellsBuilder(int index) {
      return getCellsFieldBuilder()
          .addBuilder(index, com.google.bigtable.v2.Cell.getDefaultInstance());
    }
    /**
     *
     *
     * <pre>
     * Must not be empty. Sorted in order of decreasing "timestamp_micros".
     * </pre>
     *
     * <code>repeated .google.bigtable.v2.Cell cells = 2;</code>
     */
    public java.util.List<com.google.bigtable.v2.Cell.Builder> getCellsBuilderList() {
      return getCellsFieldBuilder().getBuilderList();
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
            com.google.bigtable.v2.Cell,
            com.google.bigtable.v2.Cell.Builder,
            com.google.bigtable.v2.CellOrBuilder>
        getCellsFieldBuilder() {
      if (cellsBuilder_ == null) {
        cellsBuilder_ =
            new com.google.protobuf.RepeatedFieldBuilderV3<
                com.google.bigtable.v2.Cell,
                com.google.bigtable.v2.Cell.Builder,
                com.google.bigtable.v2.CellOrBuilder>(
                cells_, ((bitField0_ & 0x00000002) != 0), getParentForChildren(), isClean());
        cells_ = null;
      }
      return cellsBuilder_;
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

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.Column)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.Column)
  private static final com.google.bigtable.v2.Column DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.Column();
  }

  public static com.google.bigtable.v2.Column getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<Column> PARSER =
      new com.google.protobuf.AbstractParser<Column>() {
        @java.lang.Override
        public Column parsePartialFrom(
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

  public static com.google.protobuf.Parser<Column> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<Column> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.Column getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
