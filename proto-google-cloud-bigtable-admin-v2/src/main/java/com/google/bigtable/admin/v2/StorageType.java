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
// source: google/bigtable/admin/v2/common.proto

// Protobuf Java Version: 3.25.5
package com.google.bigtable.admin.v2;

/**
 *
 *
 * <pre>
 * Storage media types for persisting Bigtable data.
 * </pre>
 *
 * Protobuf enum {@code google.bigtable.admin.v2.StorageType}
 */
public enum StorageType implements com.google.protobuf.ProtocolMessageEnum {
  /**
   *
   *
   * <pre>
   * The user did not specify a storage type.
   * </pre>
   *
   * <code>STORAGE_TYPE_UNSPECIFIED = 0;</code>
   */
  STORAGE_TYPE_UNSPECIFIED(0),
  /**
   *
   *
   * <pre>
   * Flash (SSD) storage should be used.
   * </pre>
   *
   * <code>SSD = 1;</code>
   */
  SSD(1),
  /**
   *
   *
   * <pre>
   * Magnetic drive (HDD) storage should be used.
   * </pre>
   *
   * <code>HDD = 2;</code>
   */
  HDD(2),
  UNRECOGNIZED(-1),
  ;

  /**
   *
   *
   * <pre>
   * The user did not specify a storage type.
   * </pre>
   *
   * <code>STORAGE_TYPE_UNSPECIFIED = 0;</code>
   */
  public static final int STORAGE_TYPE_UNSPECIFIED_VALUE = 0;
  /**
   *
   *
   * <pre>
   * Flash (SSD) storage should be used.
   * </pre>
   *
   * <code>SSD = 1;</code>
   */
  public static final int SSD_VALUE = 1;
  /**
   *
   *
   * <pre>
   * Magnetic drive (HDD) storage should be used.
   * </pre>
   *
   * <code>HDD = 2;</code>
   */
  public static final int HDD_VALUE = 2;

  public final int getNumber() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalArgumentException(
          "Can't get the number of an unknown enum value.");
    }
    return value;
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   * @deprecated Use {@link #forNumber(int)} instead.
   */
  @java.lang.Deprecated
  public static StorageType valueOf(int value) {
    return forNumber(value);
  }

  /**
   * @param value The numeric wire value of the corresponding enum entry.
   * @return The enum associated with the given numeric wire value.
   */
  public static StorageType forNumber(int value) {
    switch (value) {
      case 0:
        return STORAGE_TYPE_UNSPECIFIED;
      case 1:
        return SSD;
      case 2:
        return HDD;
      default:
        return null;
    }
  }

  public static com.google.protobuf.Internal.EnumLiteMap<StorageType> internalGetValueMap() {
    return internalValueMap;
  }

  private static final com.google.protobuf.Internal.EnumLiteMap<StorageType> internalValueMap =
      new com.google.protobuf.Internal.EnumLiteMap<StorageType>() {
        public StorageType findValueByNumber(int number) {
          return StorageType.forNumber(number);
        }
      };

  public final com.google.protobuf.Descriptors.EnumValueDescriptor getValueDescriptor() {
    if (this == UNRECOGNIZED) {
      throw new java.lang.IllegalStateException(
          "Can't get the descriptor of an unrecognized enum value.");
    }
    return getDescriptor().getValues().get(ordinal());
  }

  public final com.google.protobuf.Descriptors.EnumDescriptor getDescriptorForType() {
    return getDescriptor();
  }

  public static final com.google.protobuf.Descriptors.EnumDescriptor getDescriptor() {
    return com.google.bigtable.admin.v2.CommonProto.getDescriptor().getEnumTypes().get(0);
  }

  private static final StorageType[] VALUES = values();

  public static StorageType valueOf(com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
    if (desc.getType() != getDescriptor()) {
      throw new java.lang.IllegalArgumentException("EnumValueDescriptor is not for this type.");
    }
    if (desc.getIndex() == -1) {
      return UNRECOGNIZED;
    }
    return VALUES[desc.getIndex()];
  }

  private final int value;

  private StorageType(int value) {
    this.value = value;
  }

  // @@protoc_insertion_point(enum_scope:google.bigtable.admin.v2.StorageType)
}
