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
package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.rpc.ResponseObserver;
import com.google.api.gax.rpc.StreamController;
import com.google.common.base.Preconditions;

/**
 * Base implementation of {@link ResponseObserver} that checks the state and catches all the
 * throwables.
 */
public abstract class SafeResponseObserver<ResponseT> implements ResponseObserver<ResponseT> {

  private boolean isStarted;
  private boolean isClosed;

  private StreamController streamController;
  private ResponseObserver outerObserver;

  public SafeResponseObserver(ResponseObserver outerObserver) {
    this.outerObserver = outerObserver;
  }

  @Override
  public final void onStart(StreamController streamController) {
    Preconditions.checkState(!isStarted, getClass() + " is already started.");
    isStarted = true;

    this.streamController = streamController;
    try {
      onStartImpl(streamController);
    } catch (Throwable t) {
      streamController.cancel();
      outerObserver.onError(t);
    }
  }

  @Override
  public final void onResponse(ResponseT response) {
    Preconditions.checkState(!isClosed, getClass() + " received a response after being closed.");

    try {
      onResponseImpl(response);
    } catch (Throwable t) {
      streamController.cancel();
      outerObserver.onError(t);
    }
  }

  @Override
  public final void onError(Throwable throwable) {
    Preconditions.checkState(
        !isClosed, getClass() + " received error after being closed", throwable);
    isClosed = true;

    try {
      onErrorImpl(throwable);
    } catch (Throwable t) {
      throwable.addSuppressed(t);
      outerObserver.onError(throwable);
    }
  }

  @Override
  public final void onComplete() {
    Preconditions.checkState(!isClosed, getClass() + " tried to double close.");
    isClosed = true;

    try {
      onCompleteImpl();
    } catch (Throwable t) {
      streamController.cancel();
      outerObserver.onError(t);
    }
  }

  public abstract void onStartImpl(StreamController streamController);

  public abstract void onResponseImpl(ResponseT response);

  public abstract void onErrorImpl(Throwable throwable);

  public abstract void onCompleteImpl();
}
