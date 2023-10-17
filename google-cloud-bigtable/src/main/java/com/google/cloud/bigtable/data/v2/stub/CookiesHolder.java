/*
 * Copyright 2023 Google LLC
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

import com.google.protobuf.ByteString;
import io.grpc.CallOptions;
import io.grpc.Metadata;
import javax.annotation.Nullable;

/** A cookie that holds information for the retry */
class CookiesHolder {

  static final CallOptions.Key<CookiesHolder> COOKIES_HOLDER_KEY =
      CallOptions.Key.create("bigtable-cookies");

  static final String ROUTING_COOKIE_KEY = "x-goog-cbt-cookie-routing";

  static final Metadata.Key<byte[]> ROUTING_COOKIE_METADATA_KEY =
      Metadata.Key.of(ROUTING_COOKIE_KEY + "-bin", Metadata.BINARY_BYTE_MARSHALLER);

  @Nullable private ByteString routingCookie;

  void setRoutingCookie(@Nullable ByteString routingCookie) {
    this.routingCookie = routingCookie;
  }

  ByteString getRoutingCookie() {
    return this.routingCookie;
  }
}
