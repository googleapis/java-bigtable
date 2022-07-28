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
import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.ReadChangeStreamResponse;
import com.google.common.base.MoreObjects;
import com.google.protobuf.Timestamp;
import java.io.Serializable;
import javax.annotation.Nonnull;

@AutoValue
public abstract class Heartbeat implements ChangeStreamRecord, Serializable {
  private static final long serialVersionUID = 7316215828353608504L;

  public static Heartbeat create(
      ChangeStreamContinuationToken changeStreamContinuationToken, Timestamp lowWatermark) {
    return new AutoValue_Heartbeat(changeStreamContinuationToken, lowWatermark);
  }

  /** Wraps the protobuf {@link ReadChangeStreamResponse.Heartbeat}. */
  static Heartbeat fromProto(@Nonnull ReadChangeStreamResponse.Heartbeat heartbeat) {
    return create(
        ChangeStreamContinuationToken.fromProto(heartbeat.getContinuationToken()),
        heartbeat.getLowWatermark());
  }

  @InternalApi("Used in Changestream beam pipeline.")
  public abstract ChangeStreamContinuationToken getChangeStreamContinuationToken();

  @InternalApi("Used in Changestream beam pipeline.")
  public abstract Timestamp getLowWatermark();

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("lowWatermark", getLowWatermark())
        .add("changeStreamContinuationToken", getChangeStreamContinuationToken())
        .toString();
  }
}
