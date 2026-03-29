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
import com.google.cloud.bigtable.data.v2.internal.csm.tracers.DirectPathCompatibleTracer;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

@InternalApi
public class DirectAccessInvestigator {
  private static final Logger LOG = Logger.getLogger(DirectAccessInvestigator.class.getName());

  // Telemetry metric reason codes
  private static final String REASON_NOT_IN_GCP = "not_in_gcp";
  private static final String REASON_METADATA_UNREACHABLE = "metadata_unreachable";
  private static final String REASON_NO_IP_ASSIGNED = "no_ip_assigned";
  private static final String REASON_LOOPBACK_DOWN = "loopback_misconfigured";
  private static final String REASON_LOOPBACK_V4_MISSING = "loopback_misconfigured_ipv4";
  private static final String REASON_LOOPBACK_V6_MISSING = "loopback_misconfigured_ipv6";
  private static final String REASON_UNKNOWN = "";

  public static void investigateAndReport(
      DirectPathCompatibleTracer tracer, Throwable originalError) {
    try {
      if (!GCECheck.isRunningOnGCP()) {
        recordAndLog(
            tracer, REASON_NOT_IN_GCP, "Direct Access investigation: not in GCP.", originalError);
        return;
      }

      if (!MetadataServer.isReachable()) {
        recordAndLog(
            tracer,
            REASON_METADATA_UNREACHABLE,
            "Direct Access investigation: Metadata unreachable.",
            null);
        return;
      }

      InetAddress ipv4 = MetadataServer.getIPv4();
      InetAddress ipv6 = MetadataServer.getIPv6();

      if (ipv4 == null && ipv6 == null) {
        recordAndLog(
            tracer,
            REASON_NO_IP_ASSIGNED,
            "Direct Access investigation: Neither IPv4 nor IPv6 assigned.",
            null);
        return;
      }

      if (!LoopBackInterface.isUp()) {
        recordAndLog(
            tracer,
            REASON_LOOPBACK_DOWN,
            "Direct Access investigation: Loopback interface down.",
            null);
        return;
      }

      // ONLY check for IPv4 loopback if we are using an IPv4 address
      if (ipv4 != null && !LoopBackInterface.hasLocalIpv4Loopback()) {
        recordAndLog(
            tracer,
            REASON_LOOPBACK_V4_MISSING,
            "Direct Access investigation: IPv4 loopback missing.",
            null);
        return;
      }

      // ONLY check for IPv6 loopback if we are using an IPv6 address
      if (ipv6 != null && !LoopBackInterface.hasLocalIpv6Loopback()) {
        recordAndLog(
            tracer,
            REASON_LOOPBACK_V6_MISSING,
            "Direct Access investigation: IPv6 loopback missing.",
            null);
        return;
      }

      boolean v4Plumbed = ipv4 != null && LoopBackInterface.isIpPlumbed(ipv4);
      if (ipv4 != null && !v4Plumbed) {
        LOG.log(
            Level.FINE,
            "Direct Access investigation: IPv4 assigned by metadata but not found on NIC.");
      }

      boolean v6Plumbed = ipv6 != null && LoopBackInterface.isIpPlumbed(ipv6);
      if (ipv6 != null && !v6Plumbed) {
        LOG.log(
            Level.FINE,
            "Direct Access investigation: IPv6 assigned by metadata but not found on NIC.");
      }

      // If the metadata server assigned IPs, but the guest OS hasn't configured any of them on an
      // interface.
      // Do NOT return early here, because this is how GKE pods work (relying on default kernel
      // routing).
      if (!v4Plumbed && !v6Plumbed) {
        LOG.log(
            Level.FINE,
            "Direct Access investigation: Metadata IPs are not plumbed to local interfaces (likely containerized). Relying on kernel default routing.");
      }
      recordAndLog(
          tracer,
          REASON_UNKNOWN,
          "Direct Access investigation: Running on GCP, metadata reachable, IPs assigned and plumbed, but Direct Access still failed.",
          originalError);

    } catch (Exception e) {
      LOG.log(Level.WARNING, "Failed to complete Direct Access investigation", e);
    }
  }

  /** Helper method to consistently log the failure reason and record it to the tracer. */
  private static void recordAndLog(
      DirectPathCompatibleTracer tracer, String reasonCode, String logMessage, Throwable error) {
    if (error != null) {
      LOG.log(Level.FINE, logMessage, error);
    } else {
      LOG.log(Level.FINE, logMessage);
    }
    tracer.recordFailure(reasonCode);
  }
}
