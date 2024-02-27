/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.data.v2.stub.metrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a fork of com.google.cloud.opentelemetry.detection.GCPMetadataConfig.
 *
 * <p>Retrieves Google Cloud project-id and a limited set of instance attributes from Metadata
 * server.
 *
 * @see <a href="https://cloud.google.com/compute/docs/storing-retrieving-metadata">
 *     https://cloud.google.com/compute/docs/storing-retrieving-metadata</a>
 */
final class GCPMetadataConfig {
  static final GCPMetadataConfig DEFAULT_INSTANCE = new GCPMetadataConfig();

  private static final String DEFAULT_URL = "http://metadata.google.internal/computeMetadata/v1/";
  private final String url;
  private final Map<String, String> cachedAttributes = new HashMap<>();

  private GCPMetadataConfig() {
    this.url = DEFAULT_URL;
  }

  // For testing only
  GCPMetadataConfig(String url) {
    this.url = url;
  }

  // Returns null on failure to retrieve from metadata server
  String getProjectId() {
    return getAttribute("project/project-id");
  }

  /**
   * Method to extract cloud availability zone from the metadata server.
   *
   * <p>Example response: projects/640212054955/zones/australia-southeast1-a
   *
   * <p>Example zone: australia-southeast1-a
   *
   * @return the extracted zone from the metadata server response or null in case of failure to
   *     retrieve from metadata server.
   */
  String getZone() {
    String zone = getAttribute("instance/zone");
    if (zone != null && zone.contains("/")) {
      zone = zone.substring(zone.lastIndexOf('/') + 1);
    }
    return zone;
  }

  /**
   * Use this method only when the region cannot be parsed from the zone. Known use-cases of this
   * method involve detecting region in GAE standard environment.
   *
   * <p>Example response: projects/5689182099321/regions/us-central1.
   *
   * @return the retrieved region or null in case of failure to retrieve from metadata server
   */
  String getRegion() {
    String region = getAttribute("instance/region");
    if (region != null && region.contains("/")) {
      region = region.substring(region.lastIndexOf('/') + 1);
    }
    return region;
  }

  /**
   * Use this method to parse region from zone.
   *
   * <p>Example region: australia-southeast1
   *
   * @return parsed region from the zone, if zone is not found or is invalid, this method returns
   *     null.
   */
  String getRegionFromZone() {
    String region = null;
    String zone = getZone();
    if (zone != null && !zone.isEmpty()) {
      // Parsing required to scope up to a region
      String[] splitArr = zone.split("-");
      if (splitArr.length > 2) {
        region = String.join("-", splitArr[0], splitArr[1]);
      }
    }
    return region;
  }

  // Example response: projects/640212054955/machineTypes/e2-medium
  String getMachineType() {
    String machineType = getAttribute("instance/machine-type");
    if (machineType != null && machineType.contains("/")) {
      machineType = machineType.substring(machineType.lastIndexOf('/') + 1);
    }
    return machineType;
  }

  // Returns null on failure to retrieve from metadata server
  String getInstanceId() {
    return getAttribute("instance/id");
  }

  // Returns null on failure to retrieve from metadata server
  String getClusterName() {
    return getAttribute("instance/attributes/cluster-name");
  }

  // Returns null on failure to retrieve from metadata server
  String getClusterLocation() {
    return getAttribute("instance/attributes/cluster-location");
  }

  // Returns null on failure to retrieve from metadata server
  String getInstanceHostName() {
    return getAttribute("instance/hostname");
  }

  // Returns null on failure to retrieve from metadata server
  String getInstanceName() {
    return getAttribute("instance/name");
  }

  // Returns null on failure to retrieve from metadata server
  String getAttribute(String attributeName) {
    return cachedAttributes.computeIfAbsent(attributeName, this::fetchAttribute);
  }

  // Return the attribute received at <attributeName> relative path or null on failure
  private String fetchAttribute(String attributeName) {
    try {
      URL url = new URL(this.url + attributeName);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestProperty("Metadata-Flavor", "Google");
      if (connection.getResponseCode() == 200
          && ("Google").equals(connection.getHeaderField("Metadata-Flavor"))) {
        InputStream input = connection.getInputStream();
        try (BufferedReader reader =
            new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
          return reader.readLine();
        }
      }
    } catch (IOException ignore) {
      // ignore
    }
    return null;
  }
}
