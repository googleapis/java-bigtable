/*
 * Copyright 2018 Google LLC
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
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.annotation.Nonnull;

/** A simple wrapper for {@link StreamContinuationToken}. */
public final class ChangeStreamContinuationToken implements Serializable {
  private static final long serialVersionUID = 524679926247095L;

  private transient StreamContinuationToken.Builder builder;

  private ChangeStreamContinuationToken(@Nonnull StreamContinuationToken.Builder builder) {
    this.builder = builder;
  }

  private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
    input.defaultReadObject();
    builder = StreamContinuationToken.newBuilder().mergeFrom(input);
  }

  private void writeObject(ObjectOutputStream output) throws IOException {
    output.defaultWriteObject();
    builder.build().writeTo(output);
  }

  public RowRange getRowRange() {
    return this.builder.getPartition().getRowRange();
  }

  public String getToken() {
    return this.builder.getToken();
  }

  /**
   * Creates the protobuf. This method is considered an internal implementation detail and not meant
   * to be used by applications.
   */
  @InternalApi("Used in Changestream veneer client.")
  public StreamContinuationToken toProto() {
    return builder.build();
  }

  /** Wraps the protobuf {@link StreamContinuationToken}. */
  public static ChangeStreamContinuationToken fromProto(
      @Nonnull StreamContinuationToken streamContinuationToken) {
    return new ChangeStreamContinuationToken(streamContinuationToken.toBuilder());
  }

  public ChangeStreamContinuationToken clone() {
    return new ChangeStreamContinuationToken(this.builder.clone());
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
