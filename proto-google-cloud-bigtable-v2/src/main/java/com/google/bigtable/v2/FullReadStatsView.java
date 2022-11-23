// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/bigtable/v2/request_stats.proto

package com.google.bigtable.v2;

/**
 * <pre>
 * FullReadStatsView captures all known information about a read.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.FullReadStatsView}
 */
public final class FullReadStatsView extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.FullReadStatsView)
    FullReadStatsViewOrBuilder {
private static final long serialVersionUID = 0L;
  // Use FullReadStatsView.newBuilder() to construct.
  private FullReadStatsView(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private FullReadStatsView() {
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new FullReadStatsView();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.google.bigtable.v2.RequestStatsProto.internal_static_google_bigtable_v2_FullReadStatsView_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.RequestStatsProto.internal_static_google_bigtable_v2_FullReadStatsView_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.FullReadStatsView.class, com.google.bigtable.v2.FullReadStatsView.Builder.class);
  }

  public static final int READ_ITERATION_STATS_FIELD_NUMBER = 1;
  private com.google.bigtable.v2.ReadIterationStats readIterationStats_;
  /**
   * <pre>
   * Iteration stats describe how efficient the read is, e.g. comparing
   * rows seen vs. rows returned or cells seen vs cells returned can provide an
   * indication of read efficiency (the higher the ratio of seen to retuned the
   * better).
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
   * @return Whether the readIterationStats field is set.
   */
  @java.lang.Override
  public boolean hasReadIterationStats() {
    return readIterationStats_ != null;
  }
  /**
   * <pre>
   * Iteration stats describe how efficient the read is, e.g. comparing
   * rows seen vs. rows returned or cells seen vs cells returned can provide an
   * indication of read efficiency (the higher the ratio of seen to retuned the
   * better).
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
   * @return The readIterationStats.
   */
  @java.lang.Override
  public com.google.bigtable.v2.ReadIterationStats getReadIterationStats() {
    return readIterationStats_ == null ? com.google.bigtable.v2.ReadIterationStats.getDefaultInstance() : readIterationStats_;
  }
  /**
   * <pre>
   * Iteration stats describe how efficient the read is, e.g. comparing
   * rows seen vs. rows returned or cells seen vs cells returned can provide an
   * indication of read efficiency (the higher the ratio of seen to retuned the
   * better).
   * </pre>
   *
   * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.ReadIterationStatsOrBuilder getReadIterationStatsOrBuilder() {
    return getReadIterationStats();
  }

  public static final int REQUEST_LATENCY_STATS_FIELD_NUMBER = 2;
  private com.google.bigtable.v2.RequestLatencyStats requestLatencyStats_;
  /**
   * <pre>
   * Request latency stats describe the time taken to complete a request, from
   * the server side.
   * </pre>
   *
   * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
   * @return Whether the requestLatencyStats field is set.
   */
  @java.lang.Override
  public boolean hasRequestLatencyStats() {
    return requestLatencyStats_ != null;
  }
  /**
   * <pre>
   * Request latency stats describe the time taken to complete a request, from
   * the server side.
   * </pre>
   *
   * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
   * @return The requestLatencyStats.
   */
  @java.lang.Override
  public com.google.bigtable.v2.RequestLatencyStats getRequestLatencyStats() {
    return requestLatencyStats_ == null ? com.google.bigtable.v2.RequestLatencyStats.getDefaultInstance() : requestLatencyStats_;
  }
  /**
   * <pre>
   * Request latency stats describe the time taken to complete a request, from
   * the server side.
   * </pre>
   *
   * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
   */
  @java.lang.Override
  public com.google.bigtable.v2.RequestLatencyStatsOrBuilder getRequestLatencyStatsOrBuilder() {
    return getRequestLatencyStats();
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
    if (readIterationStats_ != null) {
      output.writeMessage(1, getReadIterationStats());
    }
    if (requestLatencyStats_ != null) {
      output.writeMessage(2, getRequestLatencyStats());
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (readIterationStats_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getReadIterationStats());
    }
    if (requestLatencyStats_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(2, getRequestLatencyStats());
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
    if (!(obj instanceof com.google.bigtable.v2.FullReadStatsView)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.FullReadStatsView other = (com.google.bigtable.v2.FullReadStatsView) obj;

    if (hasReadIterationStats() != other.hasReadIterationStats()) return false;
    if (hasReadIterationStats()) {
      if (!getReadIterationStats()
          .equals(other.getReadIterationStats())) return false;
    }
    if (hasRequestLatencyStats() != other.hasRequestLatencyStats()) return false;
    if (hasRequestLatencyStats()) {
      if (!getRequestLatencyStats()
          .equals(other.getRequestLatencyStats())) return false;
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
    if (hasReadIterationStats()) {
      hash = (37 * hash) + READ_ITERATION_STATS_FIELD_NUMBER;
      hash = (53 * hash) + getReadIterationStats().hashCode();
    }
    if (hasRequestLatencyStats()) {
      hash = (37 * hash) + REQUEST_LATENCY_STATS_FIELD_NUMBER;
      hash = (53 * hash) + getRequestLatencyStats().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.FullReadStatsView parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.google.bigtable.v2.FullReadStatsView parseFrom(
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
  public static Builder newBuilder(com.google.bigtable.v2.FullReadStatsView prototype) {
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
   * FullReadStatsView captures all known information about a read.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.FullReadStatsView}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.FullReadStatsView)
      com.google.bigtable.v2.FullReadStatsViewOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.bigtable.v2.RequestStatsProto.internal_static_google_bigtable_v2_FullReadStatsView_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.RequestStatsProto.internal_static_google_bigtable_v2_FullReadStatsView_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.FullReadStatsView.class, com.google.bigtable.v2.FullReadStatsView.Builder.class);
    }

    // Construct using com.google.bigtable.v2.FullReadStatsView.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      if (readIterationStatsBuilder_ == null) {
        readIterationStats_ = null;
      } else {
        readIterationStats_ = null;
        readIterationStatsBuilder_ = null;
      }
      if (requestLatencyStatsBuilder_ == null) {
        requestLatencyStats_ = null;
      } else {
        requestLatencyStats_ = null;
        requestLatencyStatsBuilder_ = null;
      }
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.google.bigtable.v2.RequestStatsProto.internal_static_google_bigtable_v2_FullReadStatsView_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.FullReadStatsView getDefaultInstanceForType() {
      return com.google.bigtable.v2.FullReadStatsView.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.FullReadStatsView build() {
      com.google.bigtable.v2.FullReadStatsView result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.FullReadStatsView buildPartial() {
      com.google.bigtable.v2.FullReadStatsView result = new com.google.bigtable.v2.FullReadStatsView(this);
      if (readIterationStatsBuilder_ == null) {
        result.readIterationStats_ = readIterationStats_;
      } else {
        result.readIterationStats_ = readIterationStatsBuilder_.build();
      }
      if (requestLatencyStatsBuilder_ == null) {
        result.requestLatencyStats_ = requestLatencyStats_;
      } else {
        result.requestLatencyStats_ = requestLatencyStatsBuilder_.build();
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
      if (other instanceof com.google.bigtable.v2.FullReadStatsView) {
        return mergeFrom((com.google.bigtable.v2.FullReadStatsView)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.FullReadStatsView other) {
      if (other == com.google.bigtable.v2.FullReadStatsView.getDefaultInstance()) return this;
      if (other.hasReadIterationStats()) {
        mergeReadIterationStats(other.getReadIterationStats());
      }
      if (other.hasRequestLatencyStats()) {
        mergeRequestLatencyStats(other.getRequestLatencyStats());
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
                  getReadIterationStatsFieldBuilder().getBuilder(),
                  extensionRegistry);

              break;
            } // case 10
            case 18: {
              input.readMessage(
                  getRequestLatencyStatsFieldBuilder().getBuilder(),
                  extensionRegistry);

              break;
            } // case 18
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

    private com.google.bigtable.v2.ReadIterationStats readIterationStats_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.bigtable.v2.ReadIterationStats, com.google.bigtable.v2.ReadIterationStats.Builder, com.google.bigtable.v2.ReadIterationStatsOrBuilder> readIterationStatsBuilder_;
    /**
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
     * @return Whether the readIterationStats field is set.
     */
    public boolean hasReadIterationStats() {
      return readIterationStatsBuilder_ != null || readIterationStats_ != null;
    }
    /**
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
     * @return The readIterationStats.
     */
    public com.google.bigtable.v2.ReadIterationStats getReadIterationStats() {
      if (readIterationStatsBuilder_ == null) {
        return readIterationStats_ == null ? com.google.bigtable.v2.ReadIterationStats.getDefaultInstance() : readIterationStats_;
      } else {
        return readIterationStatsBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
     */
    public Builder setReadIterationStats(com.google.bigtable.v2.ReadIterationStats value) {
      if (readIterationStatsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        readIterationStats_ = value;
        onChanged();
      } else {
        readIterationStatsBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
     */
    public Builder setReadIterationStats(
        com.google.bigtable.v2.ReadIterationStats.Builder builderForValue) {
      if (readIterationStatsBuilder_ == null) {
        readIterationStats_ = builderForValue.build();
        onChanged();
      } else {
        readIterationStatsBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
     */
    public Builder mergeReadIterationStats(com.google.bigtable.v2.ReadIterationStats value) {
      if (readIterationStatsBuilder_ == null) {
        if (readIterationStats_ != null) {
          readIterationStats_ =
            com.google.bigtable.v2.ReadIterationStats.newBuilder(readIterationStats_).mergeFrom(value).buildPartial();
        } else {
          readIterationStats_ = value;
        }
        onChanged();
      } else {
        readIterationStatsBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
     */
    public Builder clearReadIterationStats() {
      if (readIterationStatsBuilder_ == null) {
        readIterationStats_ = null;
        onChanged();
      } else {
        readIterationStats_ = null;
        readIterationStatsBuilder_ = null;
      }

      return this;
    }
    /**
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
     */
    public com.google.bigtable.v2.ReadIterationStats.Builder getReadIterationStatsBuilder() {
      
      onChanged();
      return getReadIterationStatsFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
     */
    public com.google.bigtable.v2.ReadIterationStatsOrBuilder getReadIterationStatsOrBuilder() {
      if (readIterationStatsBuilder_ != null) {
        return readIterationStatsBuilder_.getMessageOrBuilder();
      } else {
        return readIterationStats_ == null ?
            com.google.bigtable.v2.ReadIterationStats.getDefaultInstance() : readIterationStats_;
      }
    }
    /**
     * <pre>
     * Iteration stats describe how efficient the read is, e.g. comparing
     * rows seen vs. rows returned or cells seen vs cells returned can provide an
     * indication of read efficiency (the higher the ratio of seen to retuned the
     * better).
     * </pre>
     *
     * <code>.google.bigtable.v2.ReadIterationStats read_iteration_stats = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.bigtable.v2.ReadIterationStats, com.google.bigtable.v2.ReadIterationStats.Builder, com.google.bigtable.v2.ReadIterationStatsOrBuilder> 
        getReadIterationStatsFieldBuilder() {
      if (readIterationStatsBuilder_ == null) {
        readIterationStatsBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.ReadIterationStats, com.google.bigtable.v2.ReadIterationStats.Builder, com.google.bigtable.v2.ReadIterationStatsOrBuilder>(
                getReadIterationStats(),
                getParentForChildren(),
                isClean());
        readIterationStats_ = null;
      }
      return readIterationStatsBuilder_;
    }

    private com.google.bigtable.v2.RequestLatencyStats requestLatencyStats_;
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.bigtable.v2.RequestLatencyStats, com.google.bigtable.v2.RequestLatencyStats.Builder, com.google.bigtable.v2.RequestLatencyStatsOrBuilder> requestLatencyStatsBuilder_;
    /**
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     * @return Whether the requestLatencyStats field is set.
     */
    public boolean hasRequestLatencyStats() {
      return requestLatencyStatsBuilder_ != null || requestLatencyStats_ != null;
    }
    /**
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     * @return The requestLatencyStats.
     */
    public com.google.bigtable.v2.RequestLatencyStats getRequestLatencyStats() {
      if (requestLatencyStatsBuilder_ == null) {
        return requestLatencyStats_ == null ? com.google.bigtable.v2.RequestLatencyStats.getDefaultInstance() : requestLatencyStats_;
      } else {
        return requestLatencyStatsBuilder_.getMessage();
      }
    }
    /**
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public Builder setRequestLatencyStats(com.google.bigtable.v2.RequestLatencyStats value) {
      if (requestLatencyStatsBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        requestLatencyStats_ = value;
        onChanged();
      } else {
        requestLatencyStatsBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public Builder setRequestLatencyStats(
        com.google.bigtable.v2.RequestLatencyStats.Builder builderForValue) {
      if (requestLatencyStatsBuilder_ == null) {
        requestLatencyStats_ = builderForValue.build();
        onChanged();
      } else {
        requestLatencyStatsBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public Builder mergeRequestLatencyStats(com.google.bigtable.v2.RequestLatencyStats value) {
      if (requestLatencyStatsBuilder_ == null) {
        if (requestLatencyStats_ != null) {
          requestLatencyStats_ =
            com.google.bigtable.v2.RequestLatencyStats.newBuilder(requestLatencyStats_).mergeFrom(value).buildPartial();
        } else {
          requestLatencyStats_ = value;
        }
        onChanged();
      } else {
        requestLatencyStatsBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public Builder clearRequestLatencyStats() {
      if (requestLatencyStatsBuilder_ == null) {
        requestLatencyStats_ = null;
        onChanged();
      } else {
        requestLatencyStats_ = null;
        requestLatencyStatsBuilder_ = null;
      }

      return this;
    }
    /**
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public com.google.bigtable.v2.RequestLatencyStats.Builder getRequestLatencyStatsBuilder() {
      
      onChanged();
      return getRequestLatencyStatsFieldBuilder().getBuilder();
    }
    /**
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    public com.google.bigtable.v2.RequestLatencyStatsOrBuilder getRequestLatencyStatsOrBuilder() {
      if (requestLatencyStatsBuilder_ != null) {
        return requestLatencyStatsBuilder_.getMessageOrBuilder();
      } else {
        return requestLatencyStats_ == null ?
            com.google.bigtable.v2.RequestLatencyStats.getDefaultInstance() : requestLatencyStats_;
      }
    }
    /**
     * <pre>
     * Request latency stats describe the time taken to complete a request, from
     * the server side.
     * </pre>
     *
     * <code>.google.bigtable.v2.RequestLatencyStats request_latency_stats = 2;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        com.google.bigtable.v2.RequestLatencyStats, com.google.bigtable.v2.RequestLatencyStats.Builder, com.google.bigtable.v2.RequestLatencyStatsOrBuilder> 
        getRequestLatencyStatsFieldBuilder() {
      if (requestLatencyStatsBuilder_ == null) {
        requestLatencyStatsBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            com.google.bigtable.v2.RequestLatencyStats, com.google.bigtable.v2.RequestLatencyStats.Builder, com.google.bigtable.v2.RequestLatencyStatsOrBuilder>(
                getRequestLatencyStats(),
                getParentForChildren(),
                isClean());
        requestLatencyStats_ = null;
      }
      return requestLatencyStatsBuilder_;
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


    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.FullReadStatsView)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.FullReadStatsView)
  private static final com.google.bigtable.v2.FullReadStatsView DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.FullReadStatsView();
  }

  public static com.google.bigtable.v2.FullReadStatsView getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<FullReadStatsView>
      PARSER = new com.google.protobuf.AbstractParser<FullReadStatsView>() {
    @java.lang.Override
    public FullReadStatsView parsePartialFrom(
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

  public static com.google.protobuf.Parser<FullReadStatsView> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<FullReadStatsView> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.FullReadStatsView getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

