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

import static com.google.api.gax.util.TimeConversionUtils.toThreetenInstant;

import com.google.api.core.InternalApi;
import com.google.api.core.ObsoleteApi;
import com.google.auto.value.AutoValue;
import com.google.bigtable.v2.ReadChangeStreamResponse;
import java.io.Serializable;
import javax.annotation.Nonnull;

/** A simple wrapper for {@link ReadChangeStreamResponse.Heartbeat}. */
@InternalApi("Intended for use by the BigtableIO in apache/beam only.")
@AutoValue
public abstract class Heartbeat implements ChangeStreamRecord, Serializable {
  private static final long serialVersionUID = 7316215828353608504L;

  private static Heartbeat create(
      ChangeStreamContinuationToken changeStreamContinuationToken,
      java.time.Instant estimatedLowWatermark) {
    return new AutoValue_Heartbeat(changeStreamContinuationToken, estimatedLowWatermark);
  }

  /** Wraps the protobuf {@link ReadChangeStreamResponse.Heartbeat}. */
  static Heartbeat fromProto(@Nonnull ReadChangeStreamResponse.Heartbeat heartbeat) {
    return create(
        ChangeStreamContinuationToken.fromProto(heartbeat.getContinuationToken()),
        java.time.Instant.ofEpochSecond(
            heartbeat.getEstimatedLowWatermark().getSeconds(),
            heartbeat.getEstimatedLowWatermark().getNanos()));
  }

  @InternalApi("Intended for use by the BigtableIO in apache/beam only.")
  public abstract ChangeStreamContinuationToken getChangeStreamContinuationToken();

  /** This method is obsolete. Use {@link #getEstimatedLowWatermarkTime()} instead. */
  @ObsoleteApi("Use getEstimatedLowWatermarkTime() instead")
  public org.threeten.bp.Instant getEstimatedLowWatermark() {
    return toThreetenInstant(getEstimatedLowWatermarkTime());
  }

  @InternalApi("Intended for use by the BigtableIO in apache/beam only.")
  public abstract java.time.Instant getEstimatedLowWatermarkTime();
}
