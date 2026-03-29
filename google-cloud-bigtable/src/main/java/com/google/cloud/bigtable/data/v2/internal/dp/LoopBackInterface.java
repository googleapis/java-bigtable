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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * This class verifies two main things: The OS has a functioning loopback interface (lo) with
 * standard localhost IPs configured.
 */
@InternalApi
class LoopBackInterface {

  static boolean isUp() throws Exception {
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
      NetworkInterface iface = interfaces.nextElement();
      if (iface.isLoopback() && iface.isUp()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Verifies that the standard IPv4 localhost address (127.0.0.1) is bound to a loopback interface.
   */
  static boolean hasLocalIpv4Loopback() throws Exception {
    return checkLocalLoopbackAddress("127.0.0.1");
  }

  /** Verifies that the standard IPv6 localhost address (::1) is bound to a loopback interface. */
  static boolean hasLocalIpv6Loopback() throws Exception {
    return checkLocalLoopbackAddress("::1");
  }

  static boolean isIpPlumbed(InetAddress expectedIp) throws Exception {
    if (expectedIp == null) {
      return false;
    }
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
      NetworkInterface iface = interfaces.nextElement();
      if (!iface.isLoopback() && iface.isUp()) {
        Enumeration<InetAddress> addrs = iface.getInetAddresses();
        while (addrs.hasMoreElements()) {
          if (addrs.nextElement().equals(expectedIp)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private static boolean checkLocalLoopbackAddress(String expectedIp) throws Exception {
    InetAddress expected = InetAddress.getByName(expectedIp);
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
      NetworkInterface iface = interfaces.nextElement();
      if (iface.isLoopback() && iface.isUp()) {
        Enumeration<InetAddress> addrs = iface.getInetAddresses();
        while (addrs.hasMoreElements()) {
          if (addrs.nextElement().equals(expected)) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
