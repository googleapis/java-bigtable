/*
 * Copyright 2026 Google LLC
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
package com.google.cloud.bigtable.data.v2.internal.dp;

import com.google.api.core.InternalApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Verifies that the VM can reach the GCP metadata server and checks whether GCP has successfully
 * assigned DirectPath-eligible IPv4 or IPv6 addresses to the instance's primary network interface
 * (nic0).
 */
@InternalApi
class MetadataServer {
  private static final String METADATA_BASE_URL =
      "http://metadata.google.internal/computeMetadata/v1/";
  private static final String METADATA_IPV4_URL =
      "http://metadata.google.internal/computeMetadata/v1/instance/network-interfaces/0/ip";
  private static final String METADATA_IPV6_URL =
      "http://metadata.google.internal/computeMetadata/v1/instance/network-interfaces/0/ipv6s";

  private static final int REACHABILITY_TIMEOUT_MS = 2000;
  private static final int FETCH_IP_TIMEOUT_MS = 5000;

  static boolean isReachable() {
    HttpURLConnection conn = null;
    try {
      conn = createConnection(METADATA_BASE_URL, REACHABILITY_TIMEOUT_MS, REACHABILITY_TIMEOUT_MS);
      return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
    } catch (Exception e) {
      return false;
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
  }

  static InetAddress getIPv4() {
    return fetchIP(METADATA_IPV4_URL);
  }

  static InetAddress getIPv6() {
    return fetchIP(METADATA_IPV6_URL);
  }

  private static InetAddress fetchIP(String urlStr) {
    HttpURLConnection conn = null;
    try {
      conn = createConnection(urlStr, REACHABILITY_TIMEOUT_MS, FETCH_IP_TIMEOUT_MS);
      if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
        try (BufferedReader br =
            new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
          String ipStr = br.readLine();
          if (ipStr != null && !ipStr.isEmpty()) {
            return InetAddress.getByName(ipStr.trim());
          }
        }
      }
    } catch (Exception e) {
      // investigator handles the exceptio
    } finally {
      if (conn != null) {
        conn.disconnect();
      }
    }
    return null;
  }

  /** Helper to consistently configure the HttpURLConnection for the GCE Metadata Server. */
  private static HttpURLConnection createConnection(
      String urlStr, int connectTimeout, int readTimeout) throws Exception {
    HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
    conn.setConnectTimeout(connectTimeout);
    conn.setReadTimeout(readTimeout);
    conn.setRequestProperty("Metadata-Flavor", "Google");
    return conn;
  }
}
