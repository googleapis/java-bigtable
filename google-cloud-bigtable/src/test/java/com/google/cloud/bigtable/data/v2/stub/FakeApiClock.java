/*
 * Copyright 2021 Google LLC
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

package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.core.ApiClock;
import java.util.concurrent.TimeUnit;

public class FakeApiClock implements ApiClock {

  private long millisTime;

  public FakeApiClock(long millisTime) {
    this.millisTime = millisTime;
  }

  @Override
  public long nanoTime() {
    return TimeUnit.MILLISECONDS.toNanos(millisTime);
  }

  @Override
  public long millisTime() {
    return millisTime;
  }

  public void tick(long millis) {
    this.millisTime += millis;
  }
}
