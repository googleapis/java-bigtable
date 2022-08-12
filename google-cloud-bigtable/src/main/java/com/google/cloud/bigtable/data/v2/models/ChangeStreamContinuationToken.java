/*
 * Copyright 2022 Google LLC
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
package com.google.cloud.bigtable.data.v2.models;

import com.google.api.core.InternalApi;
import com.google.bigtable.v2.RowRange;
import com.google.bigtable.v2.StreamContinuationToken;
import com.google.bigtable.v2.StreamPartition;
import com.google.cloud.bigtable.data.v2.models.Range.ByteStringRange;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.Serializable;
import javax.annotation.Nonnull;

/** A simple wrapper for {@link StreamContinuationToken}. */
public final class ChangeStreamContinuationToken implements Serializable {
  private static final long serialVersionUID = 524679926247095L;

  private final StreamContinuationToken tokenProto;

  private ChangeStreamContinuationToken(@Nonnull StreamContinuationToken tokenProto) {
    this.tokenProto = tokenProto;
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public ChangeStreamContinuationToken(
      @Nonnull ByteStringRange byteStringRange, @Nonnull String token) {
    this.tokenProto =
        StreamContinuationToken.newBuilder()
            .setPartition(
                StreamPartition.newBuilder()
                    .setRowRange(
                        RowRange.newBuilder()
                            .setStartKeyClosed(byteStringRange.getStart())
                            .setEndKeyOpen(byteStringRange.getEnd())
                            .build())
                    .build())
            .setToken(token)
            .build();
  }

  public ByteStringRange getRowRange() {
    return ByteStringRange.create(
        this.tokenProto.getPartition().getRowRange().getStartKeyClosed(),
        this.tokenProto.getPartition().getRowRange().getEndKeyOpen());
  }

  public String getToken() {
    return this.tokenProto.getToken();
  }

  // Creates the protobuf.
  StreamContinuationToken toProto() {
    return tokenProto;
  }

  /** Wraps the protobuf {@link StreamContinuationToken}. */
  static ChangeStreamContinuationToken fromProto(
      @Nonnull StreamContinuationToken streamContinuationToken) {
    return new ChangeStreamContinuationToken(streamContinuationToken);
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public ByteString toByteString() {
    return tokenProto.toByteString();
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public static ChangeStreamContinuationToken fromByteString(ByteString byteString)
      throws InvalidProtocolBufferException {
    return new ChangeStreamContinuationToken(
        StreamContinuationToken.newBuilder().mergeFrom(byteString).build());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChangeStreamContinuationToken otherToken = (ChangeStreamContinuationToken) o;
    return Objects.equal(getRowRange(), otherToken.getRowRange())
        && Objects.equal(getToken(), otherToken.getToken());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getRowRange(), getToken());
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("rowRange", getRowRange())
        .add("token", getToken())
        .toString();
  }
}
