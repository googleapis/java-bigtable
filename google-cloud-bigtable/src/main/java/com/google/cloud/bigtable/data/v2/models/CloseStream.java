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
import com.google.bigtable.v2.ReadChangeStreamResponse;
import com.google.bigtable.v2.StreamContinuationToken;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.rpc.Status;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Nonnull;

public final class CloseStream implements ChangeStreamRecord, Serializable {
  private static final long serialVersionUID = 7316215828353608505L;
  private final Status status;
  private transient ImmutableList.Builder<ChangeStreamContinuationToken>
      changeStreamContinuationTokens = ImmutableList.builder();

  private CloseStream(Status status, List<StreamContinuationToken> continuationTokens) {
    this.status = status;
    for (StreamContinuationToken streamContinuationToken : continuationTokens) {
      changeStreamContinuationTokens.add(
          ChangeStreamContinuationToken.fromProto(streamContinuationToken));
    }
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public Status getStatus() {
    return this.status;
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public List<ChangeStreamContinuationToken> getChangeStreamContinuationTokens() {
    return changeStreamContinuationTokens.build();
  }

  private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
    input.defaultReadObject();

    @SuppressWarnings("unchecked")
    ImmutableList<ChangeStreamContinuationToken> deserialized =
        (ImmutableList<ChangeStreamContinuationToken>) input.readObject();
    this.changeStreamContinuationTokens =
        ImmutableList.<ChangeStreamContinuationToken>builder().addAll(deserialized);
  }

  private void writeObject(ObjectOutputStream output) throws IOException {
    output.defaultWriteObject();
    output.writeObject(changeStreamContinuationTokens.build());
  }

  /** Wraps the protobuf {@link ReadChangeStreamResponse.CloseStream}. */
  @InternalApi("Used in java veneer client.")
  @VisibleForTesting
  public static CloseStream fromProto(@Nonnull ReadChangeStreamResponse.CloseStream closeStream) {
    return new CloseStream(closeStream.getStatus(), closeStream.getContinuationTokensList());
  }

  @VisibleForTesting
  public ReadChangeStreamResponse.CloseStream toProto() {
    ReadChangeStreamResponse.CloseStream.Builder builder =
        ReadChangeStreamResponse.CloseStream.newBuilder().setStatus(getStatus());
    for (ChangeStreamContinuationToken token : getChangeStreamContinuationTokens()) {
      builder.addContinuationTokens(token.toProto());
    }
    return builder.build();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CloseStream record = (CloseStream) o;
    return Objects.equal(status, record.getStatus())
        && Objects.equal(
            changeStreamContinuationTokens.build(), record.getChangeStreamContinuationTokens());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(status, changeStreamContinuationTokens);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("status", status)
        .add("changeStreamContinuationTokens", changeStreamContinuationTokens)
        .toString();
  }
}
