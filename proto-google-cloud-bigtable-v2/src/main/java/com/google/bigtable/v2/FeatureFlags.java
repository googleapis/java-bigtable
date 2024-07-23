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
// source: google/bigtable/v2/feature_flags.proto

// Protobuf Java Version: 3.25.3
package com.google.bigtable.v2;

/**
 *
 *
 * <pre>
 * Feature flags supported or enabled by a client.
 * This is intended to be sent as part of request metadata to assure the server
 * that certain behaviors are safe to enable. This proto is meant to be
 * serialized and websafe-base64 encoded under the `bigtable-features` metadata
 * key. The value will remain constant for the lifetime of a client and due to
 * HTTP2's HPACK compression, the request overhead will be tiny.
 * This is an internal implementation detail and should not be used by end users
 * directly.
 * </pre>
 *
 * Protobuf type {@code google.bigtable.v2.FeatureFlags}
 */
public final class FeatureFlags extends com.google.protobuf.GeneratedMessageV3
    implements
    // @@protoc_insertion_point(message_implements:google.bigtable.v2.FeatureFlags)
    FeatureFlagsOrBuilder {
  private static final long serialVersionUID = 0L;
  // Use FeatureFlags.newBuilder() to construct.
  private FeatureFlags(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }

  private FeatureFlags() {}

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(UnusedPrivateParameter unused) {
    return new FeatureFlags();
  }

  public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
    return com.google.bigtable.v2.FeatureFlagsProto
        .internal_static_google_bigtable_v2_FeatureFlags_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.google.bigtable.v2.FeatureFlagsProto
        .internal_static_google_bigtable_v2_FeatureFlags_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.google.bigtable.v2.FeatureFlags.class,
            com.google.bigtable.v2.FeatureFlags.Builder.class);
  }

  public static final int REVERSE_SCANS_FIELD_NUMBER = 1;
  private boolean reverseScans_ = false;
  /**
   *
   *
   * <pre>
   * Notify the server that the client supports reverse scans. The server will
   * reject ReadRowsRequests with the reverse bit set when this is absent.
   * </pre>
   *
   * <code>bool reverse_scans = 1;</code>
   *
   * @return The reverseScans.
   */
  @java.lang.Override
  public boolean getReverseScans() {
    return reverseScans_;
  }

  public static final int MUTATE_ROWS_RATE_LIMIT_FIELD_NUMBER = 3;
  private boolean mutateRowsRateLimit_ = false;
  /**
   *
   *
   * <pre>
   * Notify the server that the client enables batch write flow control by
   * requesting RateLimitInfo from MutateRowsResponse. Due to technical reasons,
   * this disables partial retries.
   * </pre>
   *
   * <code>bool mutate_rows_rate_limit = 3;</code>
   *
   * @return The mutateRowsRateLimit.
   */
  @java.lang.Override
  public boolean getMutateRowsRateLimit() {
    return mutateRowsRateLimit_;
  }

  public static final int MUTATE_ROWS_RATE_LIMIT2_FIELD_NUMBER = 5;
  private boolean mutateRowsRateLimit2_ = false;
  /**
   *
   *
   * <pre>
   * Notify the server that the client enables batch write flow control by
   * requesting RateLimitInfo from MutateRowsResponse. With partial retries
   * enabled.
   * </pre>
   *
   * <code>bool mutate_rows_rate_limit2 = 5;</code>
   *
   * @return The mutateRowsRateLimit2.
   */
  @java.lang.Override
  public boolean getMutateRowsRateLimit2() {
    return mutateRowsRateLimit2_;
  }

  public static final int LAST_SCANNED_ROW_RESPONSES_FIELD_NUMBER = 4;
  private boolean lastScannedRowResponses_ = false;
  /**
   *
   *
   * <pre>
   * Notify the server that the client supports the last_scanned_row field
   * in ReadRowsResponse for long-running scans.
   * </pre>
   *
   * <code>bool last_scanned_row_responses = 4;</code>
   *
   * @return The lastScannedRowResponses.
   */
  @java.lang.Override
  public boolean getLastScannedRowResponses() {
    return lastScannedRowResponses_;
  }

  public static final int ROUTING_COOKIE_FIELD_NUMBER = 6;
  private boolean routingCookie_ = false;
  /**
   *
   *
   * <pre>
   * Notify the server that the client supports using encoded routing cookie
   * strings to retry requests with.
   * </pre>
   *
   * <code>bool routing_cookie = 6;</code>
   *
   * @return The routingCookie.
   */
  @java.lang.Override
  public boolean getRoutingCookie() {
    return routingCookie_;
  }

  public static final int RETRY_INFO_FIELD_NUMBER = 7;
  private boolean retryInfo_ = false;
  /**
   *
   *
   * <pre>
   * Notify the server that the client supports using retry info back off
   * durations to retry requests with.
   * </pre>
   *
   * <code>bool retry_info = 7;</code>
   *
   * @return The retryInfo.
   */
  @java.lang.Override
  public boolean getRetryInfo() {
    return retryInfo_;
  }

  public static final int CLIENT_SIDE_METRICS_ENABLED_FIELD_NUMBER = 8;
  private boolean clientSideMetricsEnabled_ = false;
  /**
   *
   *
   * <pre>
   * Notify the server that the client has client side metrics enabled.
   * </pre>
   *
   * <code>bool client_side_metrics_enabled = 8;</code>
   *
   * @return The clientSideMetricsEnabled.
   */
  @java.lang.Override
  public boolean getClientSideMetricsEnabled() {
    return clientSideMetricsEnabled_;
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
    if (reverseScans_ != false) {
      output.writeBool(1, reverseScans_);
    }
    if (mutateRowsRateLimit_ != false) {
      output.writeBool(3, mutateRowsRateLimit_);
    }
    if (lastScannedRowResponses_ != false) {
      output.writeBool(4, lastScannedRowResponses_);
    }
    if (mutateRowsRateLimit2_ != false) {
      output.writeBool(5, mutateRowsRateLimit2_);
    }
    if (routingCookie_ != false) {
      output.writeBool(6, routingCookie_);
    }
    if (retryInfo_ != false) {
      output.writeBool(7, retryInfo_);
    }
    if (clientSideMetricsEnabled_ != false) {
      output.writeBool(8, clientSideMetricsEnabled_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (reverseScans_ != false) {
      size += com.google.protobuf.CodedOutputStream.computeBoolSize(1, reverseScans_);
    }
    if (mutateRowsRateLimit_ != false) {
      size += com.google.protobuf.CodedOutputStream.computeBoolSize(3, mutateRowsRateLimit_);
    }
    if (lastScannedRowResponses_ != false) {
      size += com.google.protobuf.CodedOutputStream.computeBoolSize(4, lastScannedRowResponses_);
    }
    if (mutateRowsRateLimit2_ != false) {
      size += com.google.protobuf.CodedOutputStream.computeBoolSize(5, mutateRowsRateLimit2_);
    }
    if (routingCookie_ != false) {
      size += com.google.protobuf.CodedOutputStream.computeBoolSize(6, routingCookie_);
    }
    if (retryInfo_ != false) {
      size += com.google.protobuf.CodedOutputStream.computeBoolSize(7, retryInfo_);
    }
    if (clientSideMetricsEnabled_ != false) {
      size += com.google.protobuf.CodedOutputStream.computeBoolSize(8, clientSideMetricsEnabled_);
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
    if (!(obj instanceof com.google.bigtable.v2.FeatureFlags)) {
      return super.equals(obj);
    }
    com.google.bigtable.v2.FeatureFlags other = (com.google.bigtable.v2.FeatureFlags) obj;

    if (getReverseScans() != other.getReverseScans()) return false;
    if (getMutateRowsRateLimit() != other.getMutateRowsRateLimit()) return false;
    if (getMutateRowsRateLimit2() != other.getMutateRowsRateLimit2()) return false;
    if (getLastScannedRowResponses() != other.getLastScannedRowResponses()) return false;
    if (getRoutingCookie() != other.getRoutingCookie()) return false;
    if (getRetryInfo() != other.getRetryInfo()) return false;
    if (getClientSideMetricsEnabled() != other.getClientSideMetricsEnabled()) return false;
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
    hash = (37 * hash) + REVERSE_SCANS_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(getReverseScans());
    hash = (37 * hash) + MUTATE_ROWS_RATE_LIMIT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(getMutateRowsRateLimit());
    hash = (37 * hash) + MUTATE_ROWS_RATE_LIMIT2_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(getMutateRowsRateLimit2());
    hash = (37 * hash) + LAST_SCANNED_ROW_RESPONSES_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(getLastScannedRowResponses());
    hash = (37 * hash) + ROUTING_COOKIE_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(getRoutingCookie());
    hash = (37 * hash) + RETRY_INFO_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(getRetryInfo());
    hash = (37 * hash) + CLIENT_SIDE_METRICS_ENABLED_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashBoolean(getClientSideMetricsEnabled());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(
      java.nio.ByteBuffer data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(
      byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.FeatureFlags parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.FeatureFlags parseDelimitedFrom(
      java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(
        PARSER, input, extensionRegistry);
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(
      com.google.protobuf.CodedInputStream input) throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
  }

  public static com.google.bigtable.v2.FeatureFlags parseFrom(
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

  public static Builder newBuilder(com.google.bigtable.v2.FeatureFlags prototype) {
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
   * Feature flags supported or enabled by a client.
   * This is intended to be sent as part of request metadata to assure the server
   * that certain behaviors are safe to enable. This proto is meant to be
   * serialized and websafe-base64 encoded under the `bigtable-features` metadata
   * key. The value will remain constant for the lifetime of a client and due to
   * HTTP2's HPACK compression, the request overhead will be tiny.
   * This is an internal implementation detail and should not be used by end users
   * directly.
   * </pre>
   *
   * Protobuf type {@code google.bigtable.v2.FeatureFlags}
   */
  public static final class Builder extends com.google.protobuf.GeneratedMessageV3.Builder<Builder>
      implements
      // @@protoc_insertion_point(builder_implements:google.bigtable.v2.FeatureFlags)
      com.google.bigtable.v2.FeatureFlagsOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
      return com.google.bigtable.v2.FeatureFlagsProto
          .internal_static_google_bigtable_v2_FeatureFlags_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.bigtable.v2.FeatureFlagsProto
          .internal_static_google_bigtable_v2_FeatureFlags_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.bigtable.v2.FeatureFlags.class,
              com.google.bigtable.v2.FeatureFlags.Builder.class);
    }

    // Construct using com.google.bigtable.v2.FeatureFlags.newBuilder()
    private Builder() {}

    private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
    }

    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      reverseScans_ = false;
      mutateRowsRateLimit_ = false;
      mutateRowsRateLimit2_ = false;
      lastScannedRowResponses_ = false;
      routingCookie_ = false;
      retryInfo_ = false;
      clientSideMetricsEnabled_ = false;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
      return com.google.bigtable.v2.FeatureFlagsProto
          .internal_static_google_bigtable_v2_FeatureFlags_descriptor;
    }

    @java.lang.Override
    public com.google.bigtable.v2.FeatureFlags getDefaultInstanceForType() {
      return com.google.bigtable.v2.FeatureFlags.getDefaultInstance();
    }

    @java.lang.Override
    public com.google.bigtable.v2.FeatureFlags build() {
      com.google.bigtable.v2.FeatureFlags result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.google.bigtable.v2.FeatureFlags buildPartial() {
      com.google.bigtable.v2.FeatureFlags result = new com.google.bigtable.v2.FeatureFlags(this);
      if (bitField0_ != 0) {
        buildPartial0(result);
      }
      onBuilt();
      return result;
    }

    private void buildPartial0(com.google.bigtable.v2.FeatureFlags result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.reverseScans_ = reverseScans_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.mutateRowsRateLimit_ = mutateRowsRateLimit_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.mutateRowsRateLimit2_ = mutateRowsRateLimit2_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.lastScannedRowResponses_ = lastScannedRowResponses_;
      }
      if (((from_bitField0_ & 0x00000010) != 0)) {
        result.routingCookie_ = routingCookie_;
      }
      if (((from_bitField0_ & 0x00000020) != 0)) {
        result.retryInfo_ = retryInfo_;
      }
      if (((from_bitField0_ & 0x00000040) != 0)) {
        result.clientSideMetricsEnabled_ = clientSideMetricsEnabled_;
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
      if (other instanceof com.google.bigtable.v2.FeatureFlags) {
        return mergeFrom((com.google.bigtable.v2.FeatureFlags) other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.google.bigtable.v2.FeatureFlags other) {
      if (other == com.google.bigtable.v2.FeatureFlags.getDefaultInstance()) return this;
      if (other.getReverseScans() != false) {
        setReverseScans(other.getReverseScans());
      }
      if (other.getMutateRowsRateLimit() != false) {
        setMutateRowsRateLimit(other.getMutateRowsRateLimit());
      }
      if (other.getMutateRowsRateLimit2() != false) {
        setMutateRowsRateLimit2(other.getMutateRowsRateLimit2());
      }
      if (other.getLastScannedRowResponses() != false) {
        setLastScannedRowResponses(other.getLastScannedRowResponses());
      }
      if (other.getRoutingCookie() != false) {
        setRoutingCookie(other.getRoutingCookie());
      }
      if (other.getRetryInfo() != false) {
        setRetryInfo(other.getRetryInfo());
      }
      if (other.getClientSideMetricsEnabled() != false) {
        setClientSideMetricsEnabled(other.getClientSideMetricsEnabled());
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
                reverseScans_ = input.readBool();
                bitField0_ |= 0x00000001;
                break;
              } // case 8
            case 24:
              {
                mutateRowsRateLimit_ = input.readBool();
                bitField0_ |= 0x00000002;
                break;
              } // case 24
            case 32:
              {
                lastScannedRowResponses_ = input.readBool();
                bitField0_ |= 0x00000008;
                break;
              } // case 32
            case 40:
              {
                mutateRowsRateLimit2_ = input.readBool();
                bitField0_ |= 0x00000004;
                break;
              } // case 40
            case 48:
              {
                routingCookie_ = input.readBool();
                bitField0_ |= 0x00000010;
                break;
              } // case 48
            case 56:
              {
                retryInfo_ = input.readBool();
                bitField0_ |= 0x00000020;
                break;
              } // case 56
            case 64:
              {
                clientSideMetricsEnabled_ = input.readBool();
                bitField0_ |= 0x00000040;
                break;
              } // case 64
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

    private boolean reverseScans_;
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports reverse scans. The server will
     * reject ReadRowsRequests with the reverse bit set when this is absent.
     * </pre>
     *
     * <code>bool reverse_scans = 1;</code>
     *
     * @return The reverseScans.
     */
    @java.lang.Override
    public boolean getReverseScans() {
      return reverseScans_;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports reverse scans. The server will
     * reject ReadRowsRequests with the reverse bit set when this is absent.
     * </pre>
     *
     * <code>bool reverse_scans = 1;</code>
     *
     * @param value The reverseScans to set.
     * @return This builder for chaining.
     */
    public Builder setReverseScans(boolean value) {

      reverseScans_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports reverse scans. The server will
     * reject ReadRowsRequests with the reverse bit set when this is absent.
     * </pre>
     *
     * <code>bool reverse_scans = 1;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearReverseScans() {
      bitField0_ = (bitField0_ & ~0x00000001);
      reverseScans_ = false;
      onChanged();
      return this;
    }

    private boolean mutateRowsRateLimit_;
    /**
     *
     *
     * <pre>
     * Notify the server that the client enables batch write flow control by
     * requesting RateLimitInfo from MutateRowsResponse. Due to technical reasons,
     * this disables partial retries.
     * </pre>
     *
     * <code>bool mutate_rows_rate_limit = 3;</code>
     *
     * @return The mutateRowsRateLimit.
     */
    @java.lang.Override
    public boolean getMutateRowsRateLimit() {
      return mutateRowsRateLimit_;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client enables batch write flow control by
     * requesting RateLimitInfo from MutateRowsResponse. Due to technical reasons,
     * this disables partial retries.
     * </pre>
     *
     * <code>bool mutate_rows_rate_limit = 3;</code>
     *
     * @param value The mutateRowsRateLimit to set.
     * @return This builder for chaining.
     */
    public Builder setMutateRowsRateLimit(boolean value) {

      mutateRowsRateLimit_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client enables batch write flow control by
     * requesting RateLimitInfo from MutateRowsResponse. Due to technical reasons,
     * this disables partial retries.
     * </pre>
     *
     * <code>bool mutate_rows_rate_limit = 3;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearMutateRowsRateLimit() {
      bitField0_ = (bitField0_ & ~0x00000002);
      mutateRowsRateLimit_ = false;
      onChanged();
      return this;
    }

    private boolean mutateRowsRateLimit2_;
    /**
     *
     *
     * <pre>
     * Notify the server that the client enables batch write flow control by
     * requesting RateLimitInfo from MutateRowsResponse. With partial retries
     * enabled.
     * </pre>
     *
     * <code>bool mutate_rows_rate_limit2 = 5;</code>
     *
     * @return The mutateRowsRateLimit2.
     */
    @java.lang.Override
    public boolean getMutateRowsRateLimit2() {
      return mutateRowsRateLimit2_;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client enables batch write flow control by
     * requesting RateLimitInfo from MutateRowsResponse. With partial retries
     * enabled.
     * </pre>
     *
     * <code>bool mutate_rows_rate_limit2 = 5;</code>
     *
     * @param value The mutateRowsRateLimit2 to set.
     * @return This builder for chaining.
     */
    public Builder setMutateRowsRateLimit2(boolean value) {

      mutateRowsRateLimit2_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client enables batch write flow control by
     * requesting RateLimitInfo from MutateRowsResponse. With partial retries
     * enabled.
     * </pre>
     *
     * <code>bool mutate_rows_rate_limit2 = 5;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearMutateRowsRateLimit2() {
      bitField0_ = (bitField0_ & ~0x00000004);
      mutateRowsRateLimit2_ = false;
      onChanged();
      return this;
    }

    private boolean lastScannedRowResponses_;
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports the last_scanned_row field
     * in ReadRowsResponse for long-running scans.
     * </pre>
     *
     * <code>bool last_scanned_row_responses = 4;</code>
     *
     * @return The lastScannedRowResponses.
     */
    @java.lang.Override
    public boolean getLastScannedRowResponses() {
      return lastScannedRowResponses_;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports the last_scanned_row field
     * in ReadRowsResponse for long-running scans.
     * </pre>
     *
     * <code>bool last_scanned_row_responses = 4;</code>
     *
     * @param value The lastScannedRowResponses to set.
     * @return This builder for chaining.
     */
    public Builder setLastScannedRowResponses(boolean value) {

      lastScannedRowResponses_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports the last_scanned_row field
     * in ReadRowsResponse for long-running scans.
     * </pre>
     *
     * <code>bool last_scanned_row_responses = 4;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearLastScannedRowResponses() {
      bitField0_ = (bitField0_ & ~0x00000008);
      lastScannedRowResponses_ = false;
      onChanged();
      return this;
    }

    private boolean routingCookie_;
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports using encoded routing cookie
     * strings to retry requests with.
     * </pre>
     *
     * <code>bool routing_cookie = 6;</code>
     *
     * @return The routingCookie.
     */
    @java.lang.Override
    public boolean getRoutingCookie() {
      return routingCookie_;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports using encoded routing cookie
     * strings to retry requests with.
     * </pre>
     *
     * <code>bool routing_cookie = 6;</code>
     *
     * @param value The routingCookie to set.
     * @return This builder for chaining.
     */
    public Builder setRoutingCookie(boolean value) {

      routingCookie_ = value;
      bitField0_ |= 0x00000010;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports using encoded routing cookie
     * strings to retry requests with.
     * </pre>
     *
     * <code>bool routing_cookie = 6;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearRoutingCookie() {
      bitField0_ = (bitField0_ & ~0x00000010);
      routingCookie_ = false;
      onChanged();
      return this;
    }

    private boolean retryInfo_;
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports using retry info back off
     * durations to retry requests with.
     * </pre>
     *
     * <code>bool retry_info = 7;</code>
     *
     * @return The retryInfo.
     */
    @java.lang.Override
    public boolean getRetryInfo() {
      return retryInfo_;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports using retry info back off
     * durations to retry requests with.
     * </pre>
     *
     * <code>bool retry_info = 7;</code>
     *
     * @param value The retryInfo to set.
     * @return This builder for chaining.
     */
    public Builder setRetryInfo(boolean value) {

      retryInfo_ = value;
      bitField0_ |= 0x00000020;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client supports using retry info back off
     * durations to retry requests with.
     * </pre>
     *
     * <code>bool retry_info = 7;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearRetryInfo() {
      bitField0_ = (bitField0_ & ~0x00000020);
      retryInfo_ = false;
      onChanged();
      return this;
    }

    private boolean clientSideMetricsEnabled_;
    /**
     *
     *
     * <pre>
     * Notify the server that the client has client side metrics enabled.
     * </pre>
     *
     * <code>bool client_side_metrics_enabled = 8;</code>
     *
     * @return The clientSideMetricsEnabled.
     */
    @java.lang.Override
    public boolean getClientSideMetricsEnabled() {
      return clientSideMetricsEnabled_;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client has client side metrics enabled.
     * </pre>
     *
     * <code>bool client_side_metrics_enabled = 8;</code>
     *
     * @param value The clientSideMetricsEnabled to set.
     * @return This builder for chaining.
     */
    public Builder setClientSideMetricsEnabled(boolean value) {

      clientSideMetricsEnabled_ = value;
      bitField0_ |= 0x00000040;
      onChanged();
      return this;
    }
    /**
     *
     *
     * <pre>
     * Notify the server that the client has client side metrics enabled.
     * </pre>
     *
     * <code>bool client_side_metrics_enabled = 8;</code>
     *
     * @return This builder for chaining.
     */
    public Builder clearClientSideMetricsEnabled() {
      bitField0_ = (bitField0_ & ~0x00000040);
      clientSideMetricsEnabled_ = false;
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

    // @@protoc_insertion_point(builder_scope:google.bigtable.v2.FeatureFlags)
  }

  // @@protoc_insertion_point(class_scope:google.bigtable.v2.FeatureFlags)
  private static final com.google.bigtable.v2.FeatureFlags DEFAULT_INSTANCE;

  static {
    DEFAULT_INSTANCE = new com.google.bigtable.v2.FeatureFlags();
  }

  public static com.google.bigtable.v2.FeatureFlags getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<FeatureFlags> PARSER =
      new com.google.protobuf.AbstractParser<FeatureFlags>() {
        @java.lang.Override
        public FeatureFlags parsePartialFrom(
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

  public static com.google.protobuf.Parser<FeatureFlags> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<FeatureFlags> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.bigtable.v2.FeatureFlags getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }
}
