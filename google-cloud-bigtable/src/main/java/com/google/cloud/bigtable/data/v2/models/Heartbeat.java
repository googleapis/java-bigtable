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
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.protobuf.Timestamp;
import java.io.Serializable;
import javax.annotation.Nonnull;

public final class Heartbeat implements ChangeStreamRecord, Serializable {
  private static final long serialVersionUID = 7316215828353608504L;
  private final Timestamp lowWatermark;
  private final ChangeStreamContinuationToken changeStreamContinuationToken;

  private Heartbeat(
      Timestamp lowWatermark, ChangeStreamContinuationToken changeStreamContinuationToken) {
    this.lowWatermark = lowWatermark;
    this.changeStreamContinuationToken = changeStreamContinuationToken;
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public ChangeStreamContinuationToken getChangeStreamContinuationToken() {
    return changeStreamContinuationToken;
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public Timestamp getLowWatermark() {
    return lowWatermark;
  }

  /** Wraps the protobuf {@link ReadChangeStreamResponse.Heartbeat}. */
  static Heartbeat fromProto(@Nonnull ReadChangeStreamResponse.Heartbeat heartbeat) {
    return new Heartbeat(
        heartbeat.getLowWatermark(),
        ChangeStreamContinuationToken.fromProto(heartbeat.getContinuationToken()));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Heartbeat record = (Heartbeat) o;
    return Objects.equal(lowWatermark, record.getLowWatermark())
        && Objects.equal(changeStreamContinuationToken, record.getChangeStreamContinuationToken());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(lowWatermark, changeStreamContinuationToken);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("lowWatermark", lowWatermark)
        .add("changeStreamContinuationToken", changeStreamContinuationToken)
        .toString();
  }
}
