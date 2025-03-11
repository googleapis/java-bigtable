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
// source: google/bigtable/admin/v2/bigtable_instance_admin.proto

// Protobuf Java Version: 3.25.5
package com.google.bigtable.admin.v2;

/**
 *
 *
 * <pre>
 * Response message for BigtableInstanceAdmin.ListLogicalViews.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.admin.v2.ListLogicalViewsResponse}
 */
public final class ListLogicalViewsResponse extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.admin.v2.ListLogicalViewsResponse)
    ListLogicalViewsResponseOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use ListLogicalViewsResponse.newBuilder() to construct.
  private ListLogicalViewsResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private ListLogicalViewsResponse() {
    logicalViews_ = java.util.Collections.emptyList();
    nextPageToken_ = "";
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new ListLogicalViewsResponse();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.admin.v2.BigtableInstanceAdminProto
        .internal_static_google_bigtable_admin_v2_ListLogicalViewsResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.admin.v2.BigtableInstanceAdminProto
        .internal_static_google_bigtable_admin_v2_ListLogicalViewsResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.admin.v2.ListLogicalViewsResponse.class,
            com.google.bigtable.admin.v2.ListLogicalViewsResponse.Builder.class);
  }

  public static final int LOGICAL_VIEWS_FIELD_NUMBER = 1;

  @SuppressWarnings("serial")
  private java.util.List<com.google.bigtable.admin.v2.LogicalView> logicalViews_;
  /**
   *
   *
   * <pre>
   * The list of requested logical views.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
   */
  @java.lang.Override
  public java.util.List<com.google.bigtable.admin.v2.LogicalView> getLogicalViewsList() {
    return logicalViews_;
  }
  /**
   *
   *
   * <pre>
   * The list of requested logical views.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.google.bigtable.admin.v2.LogicalViewOrBuilder>
      getLogicalViewsOrBuilderList() {
    return logicalViews_;
  }
  /**
   *
   *
   * <pre>
   * The list of requested logical views.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
   */
  @java.lang.Override
  public int getLogicalViewsCount() {
    return logicalViews_.size();
  }
  /**
   *
   *
   * <pre>
   * The list of requested logical views.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.admin.v2.LogicalView getLogicalViews(int index) {
    return logicalViews_.get(index);
  }
  /**
   *
   *
   * <pre>
   * The list of requested logical views.
   * </pre>
   *
   * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.admin.v2.LogicalViewOrBuilder getLogicalViewsOrBuilder(int index) {
    return logicalViews_.get(index);
  }

  public static final int NEXT_PAGE_TOKEN_FIELD_NUMBER = 2;

  @SuppressWarnings("serial")
  private volatile java.lang.Object nextPageToken_ = "";
  /**
   *
   *
   * <pre>
   * A token, which can be sent as `page_token` to retrieve the next page.
   * If this field is omitted, there are no subsequent pages.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   *
   * @return The nextPageToken.
   */
  @java.lang.Override
  public java.lang.String getNextPageToken() {
    java.lang.Object ref = nextPageToken_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      nextPageToken_ = s;
      return s;
    }
  }
  /**
   *
   *
   * <pre>
   * A token, which can be sent as `page_token` to retrieve the next page.
   * If this field is omitted, there are no subsequent pages.
   * </pre>
   *
   * <code>string next_page_token = 2;</code>
   *
   * @return The bytes for nextPageToken.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString getNextPageTokenBytes() {
    java.lang.Object ref = nextPageToken_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b =
          com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
      nextPageToken_ = b;
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
    for (int i = 0; i < logicalViews_.size(); i++) {
      output.writeMessage(1, logicalViews_.get(i));
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(nextPageToken_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, nextPageToken_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    for (int i = 0; i < logicalViews_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream.computeMessageSize(1, logicalViews_.get(i));
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(nextPageToken_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, nextPageToken_);
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
    if (!(obj instanceof com.google.bigtable.admin.v2.ListLogicalViewsResponse)) {
      return super.equals(obj);
    }
    com.google.bigtable.admin.v2.ListLogicalViewsResponse other =
        (com.google.bigtable.admin.v2.ListLogicalViewsResponse) obj;

    if (!getLogicalViewsList().equals(other.getLogicalViewsList())) return false;
    if (!getNextPageToken().equals(other.getNextPageToken())) return false;
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
    if (getLogicalViewsCount() > 0) {
      hash = (37 * hash) + LOGICAL_VIEWS_FIELD_NUMBER;
      hash = (53 * hash) + getLogicalViewsList().hashCode();
    }
    hash = (37 * hash) + NEXT_PAGE_TOKEN_FIELD_NUMBER;
    hash = (53 * hash) + getNextPageToken().hashCode();
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(
      java.nio.ByteBuffer data) throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseDelimitedFrom(
      java.io.InputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse parseFrom(
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
      com.google.bigtable.admin.v2.ListLogicalViewsResponse prototype) {
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
   * Response message for BigtableInstanceAdmin.ListLogicalViews.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.admin.v2.ListLogicalViewsResponse}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.admin.v2.ListLogicalViewsResponse)
      com.google.bigtable.admin.v2.ListLogicalViewsResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.admin.v2.BigtableInstanceAdminProto
          .internal_static_google_bigtable_admin_v2_ListLogicalViewsResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.admin.v2.BigtableInstanceAdminProto
          .internal_static_google_bigtable_admin_v2_ListLogicalViewsResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.admin.v2.ListLogicalViewsResponse.class,
              com.google.bigtable.admin.v2.ListLogicalViewsResponse.Builder.class);
    }

    // Construct using com.google.bigtable.admin.v2.ListLogicalViewsResponse.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      if (logicalViewsBuilder_ == null) {
        logicalViews_ = java.util.Collections.emptyList();
      } else {
        logicalViews_ = null;
        logicalViewsBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000001);
      nextPageToken_ = "";
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.admin.v2.BigtableInstanceAdminProto
          .internal_static_google_bigtable_admin_v2_ListLogicalViewsResponse_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.ListLogicalViewsResponse getDefaultInstanceForType() {
      return com.google.bigtable.admin.v2.ListLogicalViewsResponse.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.ListLogicalViewsResponse build() {
      com.google.bigtable.admin.v2.ListLogicalViewsResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.admin.v2.ListLogicalViewsResponse buildPartial() {
      com.google.bigtable.admin.v2.ListLogicalViewsResponse result =
          new com.google.bigtable.admin.v2.ListLogicalViewsResponse(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(
        com.google.bigtable.admin.v2.ListLogicalViewsResponse result) {
      if (logicalViewsBuilder_ == null) {
        if (((bitField0_ & 0x00000001) != 0)) {
          logicalViews_ = java.util.Collections.unmodifiableList(logicalViews_);
          bitField0_ = (bitField0_ & ~0x00000001);
        }
        result.logicalViews_ = logicalViews_;
      } else {
        result.logicalViews_ = logicalViewsBuilder_.build();
      }
    }

    private void buildPartial0(com.google.bigtable.admin.v2.ListLogicalViewsResponse result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.nextPageToken_ = nextPageToken_;
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
      if (other instanceof com.google.bigtable.admin.v2.ListLogicalViewsResponse) {
        return mergeFrom((com.google.bigtable.admin.v2.ListLogicalViewsResponse) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.admin.v2.ListLogicalViewsResponse other) {
      if (other == com.google.bigtable.admin.v2.ListLogicalViewsResponse.getDefaultInstance())
        return this;
      if (logicalViewsBuilder_ == null) {
        if (!other.logicalViews_.isEmpty()) {
          if (logicalViews_.isEmpty()) {
            logicalViews_ = other.logicalViews_;
            bitField0_ = (bitField0_ & ~0x00000001);
          } else {
            ensureLogicalViewsIsMutable();
            logicalViews_.addAll(other.logicalViews_);
          }
          onChanged();
        }
      } else {
        if (!other.logicalViews_.isEmpty()) {
          if (logicalViewsBuilder_.isEmpty()) {
            logicalViewsBuilder_.dispose();
            logicalViewsBuilder_ = null;
            logicalViews_ = other.logicalViews_;
            bitField0_ = (bitField0_ & ~0x00000001);
            logicalViewsBuilder_ =
                com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders
                    ? getLogicalViewsFieldBuilder()
                    : null;
          } else {
            logicalViewsBuilder_.addAllMessages(other.logicalViews_);
          }
        }
      }
      if (!other.getNextPageToken().isEmpty()) {
        nextPageToken_ = other.nextPageToken_;
        bitField0_ |= 0x00000002;
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
                com.google.bigtable.admin.v2.LogicalView m =
                    input.readMessage(
                        com.google.bigtable.admin.v2.LogicalView.parser(), extensionRegistry);
                if (logicalViewsBuilder_ == null) {
                  ensureLogicalViewsIsMutable();
                  logicalViews_.add(m);
                } else {
                  logicalViewsBuilder_.addMessage(m);
                }
                break;
              } // case 10
            case 18:
              {
                nextPageToken_ = input.readStringRequireUtf8();
                bitField0_ |= 0x00000002;
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

    private java.util.List<com.google.bigtable.admin.v2.LogicalView> logicalViews_ =
        java.util.Collections.emptyList();

    private void ensureLogicalViewsIsMutable() {
      if (!((bitField0_ & 0x00000001) != 0)) {
        logicalViews_ =
            new java.util.ArrayList<com.google.bigtable.admin.v2.LogicalView>(logicalViews_);
        bitField0_ |= 0x00000001;
      }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
            com.google.bigtable.admin.v2.LogicalView,
            com.google.bigtable.admin.v2.LogicalView.Builder,
            com.google.bigtable.admin.v2.LogicalViewOrBuilder>
        logicalViewsBuilder_;

    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public java.util.List<com.google.bigtable.admin.v2.LogicalView> getLogicalViewsList() {
      if (logicalViewsBuilder_ == null) {
        return java.util.Collections.unmodifiableList(logicalViews_);
      } else {
        return logicalViewsBuilder_.getMessageList();
      }
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public int getLogicalViewsCount() {
      if (logicalViewsBuilder_ == null) {
        return logicalViews_.size();
      } else {
        return logicalViewsBuilder_.getCount();
      }
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public com.google.bigtable.admin.v2.LogicalView getLogicalViews(int index) {
      if (logicalViewsBuilder_ == null) {
        return logicalViews_.get(index);
      } else {
        return logicalViewsBuilder_.getMessage(index);
      }
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public Builder setLogicalViews(int index, com.google.bigtable.admin.v2.LogicalView value) {
      if (logicalViewsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureLogicalViewsIsMutable();
        logicalViews_.set(index, value);
        onChanged();
      } else {
        logicalViewsBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public Builder setLogicalViews(
        int index, com.google.bigtable.admin.v2.LogicalView.Builder builderForValue) {
      if (logicalViewsBuilder_ == null) {
        ensureLogicalViewsIsMutable();
        logicalViews_.set(index, builderForValue.build());
        onChanged();
      } else {
        logicalViewsBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public Builder addLogicalViews(com.google.bigtable.admin.v2.LogicalView value) {
      if (logicalViewsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureLogicalViewsIsMutable();
        logicalViews_.add(value);
        onChanged();
      } else {
        logicalViewsBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public Builder addLogicalViews(int index, com.google.bigtable.admin.v2.LogicalView value) {
      if (logicalViewsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureLogicalViewsIsMutable();
        logicalViews_.add(index, value);
        onChanged();
      } else {
        logicalViewsBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public Builder addLogicalViews(
        com.google.bigtable.admin.v2.LogicalView.Builder builderForValue) {
      if (logicalViewsBuilder_ == null) {
        ensureLogicalViewsIsMutable();
        logicalViews_.add(builderForValue.build());
        onChanged();
      } else {
        logicalViewsBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public Builder addLogicalViews(
        int index, com.google.bigtable.admin.v2.LogicalView.Builder builderForValue) {
      if (logicalViewsBuilder_ == null) {
        ensureLogicalViewsIsMutable();
        logicalViews_.add(index, builderForValue.build());
        onChanged();
      } else {
        logicalViewsBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public Builder addAllLogicalViews(
        java.lang.Iterable<? extends com.google.bigtable.admin.v2.LogicalView> values) {
      if (logicalViewsBuilder_ == null) {
        ensureLogicalViewsIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(values, logicalViews_);
        onChanged();
      } else {
        logicalViewsBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public Builder clearLogicalViews() {
      if (logicalViewsBuilder_ == null) {
        logicalViews_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000001);
        onChanged();
      } else {
        logicalViewsBuilder_.clear();
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public Builder removeLogicalViews(int index) {
      if (logicalViewsBuilder_ == null) {
        ensureLogicalViewsIsMutable();
        logicalViews_.remove(index);
        onChanged();
      } else {
        logicalViewsBuilder_.remove(index);
      }
      return this;
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public com.google.bigtable.admin.v2.LogicalView.Builder getLogicalViewsBuilder(int index) {
      return getLogicalViewsFieldBuilder().getBuilder(index);
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public com.google.bigtable.admin.v2.LogicalViewOrBuilder getLogicalViewsOrBuilder(int index) {
      if (logicalViewsBuilder_ == null) {
        return logicalViews_.get(index);
      } else {
        return logicalViewsBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public java.util.List<? extends com.google.bigtable.admin.v2.LogicalViewOrBuilder>
        getLogicalViewsOrBuilderList() {
      if (logicalViewsBuilder_ != null) {
        return logicalViewsBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(logicalViews_);
      }
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public com.google.bigtable.admin.v2.LogicalView.Builder addLogicalViewsBuilder() {
      return getLogicalViewsFieldBuilder()
          .addBuilder(com.google.bigtable.admin.v2.LogicalView.getDefaultInstance());
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public com.google.bigtable.admin.v2.LogicalView.Builder addLogicalViewsBuilder(int index) {
      return getLogicalViewsFieldBuilder()
          .addBuilder(index, com.google.bigtable.admin.v2.LogicalView.getDefaultInstance());
    }
    /**
     *
     *
     * <pre>
     * The list of requested logical views.
     * </pre>
     *
     * <code>repeated .google.bigtable.admin.v2.LogicalView logical_views = 1;</code>
     */
    public java.util.List<com.google.bigtable.admin.v2.LogicalView.Builder>
        getLogicalViewsBuilderList() {
      return getLogicalViewsFieldBuilder().getBuilderList();
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
            com.google.bigtable.admin.v2.LogicalView,
            com.google.bigtable.admin.v2.LogicalView.Builder,
            com.google.bigtable.admin.v2.LogicalViewOrBuilder>
        getLogicalViewsFieldBuilder() {
      if (logicalViewsBuilder_ == null) {
        logicalViewsBuilder_ =
            new com.google.protobuf.RepeatedFieldBuilderV3<
                com.google.bigtable.admin.v2.LogicalView,
                com.google.bigtable.admin.v2.LogicalView.Builder,
                com.google.bigtable.admin.v2.LogicalViewOrBuilder>(
                logicalViews_, ((bitField0_ & 0x00000001) != 0), getParentForChildren(), isClean());
        logicalViews_ = null;
      }
      return logicalViewsBuilder_;
    }

    private java.lang.Object nextPageToken_ = "";
    /**
     *
     *
     * <pre>
     * A token, which can be sent as `page_token` to retrieve the next page.
     * If this field is omitted, there are no subsequent pages.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @return The nextPageToken.
     */
    public java.lang.String getNextPageToken() {
      java.lang.Object ref = nextPageToken_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        nextPageToken_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * A token, which can be sent as `page_token` to retrieve the next page.
     * If this field is omitted, there are no subsequent pages.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @return The bytes for nextPageToken.
     */
    public com.google.protobuf.ByteString getNextPageTokenBytes() {
      java.lang.Object ref = nextPageToken_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b =
            com.google.protobuf.ByteString.copyFromUtf8((java.lang.String) ref);
        nextPageToken_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     *
     *
     * <pre>
     * A token, which can be sent as `page_token` to retrieve the next page.
     * If this field is omitted, there are no subsequent pages.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @param value The nextPageToken to set.
     * @return This builder for chaining.
     */
    public Builder setNextPageToken(java.lang.String value) {
      if (value == null) {
        throw new NullPointerException();
      }
      nextPageToken_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * A token, which can be sent as `page_token` to retrieve the next page.
     * If this field is omitted, there are no subsequent pages.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearNextPageToken() {
      nextPageToken_ = getDefaultInstance().getNextPageToken();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * A token, which can be sent as `page_token` to retrieve the next page.
     * If this field is omitted, there are no subsequent pages.
     * </pre>
     *
     * <code>string next_page_token = 2;</code>
     *
     * @param value The bytes for nextPageToken to set.
     * @return This builder for chaining.
     */
    public Builder setNextPageTokenBytes(com.google.protobuf.ByteString value) {
      if (value == null) {
        throw new NullPointerException();
      }
      checkByteStringIsUtf8(value);
      nextPageToken_ = value;
      bitField0_ |= 0x00000002;
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

    // @@protoc_insertion_point(builder_scope:google.bigtable.admin.v2.ListLogicalViewsResponse)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.admin.v2.ListLogicalViewsResponse)
  private static final com.google.bigtable.admin.v2.ListLogicalViewsResponse DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.admin.v2.ListLogicalViewsResponse();
  }

  public static com.google.bigtable.admin.v2.ListLogicalViewsResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ListLogicalViewsResponse> PARSER =
      new com.google.protobuf.AbstractParser<ListLogicalViewsResponse>() {
        @java.lang.Override
        public ListLogicalViewsResponse parsePartialFrom(
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

  public static com.google.protobuf.Parser<ListLogicalViewsResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ListLogicalViewsResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.admin.v2.ListLogicalViewsResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
