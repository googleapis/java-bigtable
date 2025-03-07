/*
 * Copyright 2025 Google LLC
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
package com.google.cloud.kafka.connect.bigtable.integration;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.kafka.connect.bigtable.BigtableSinkConnector;
import com.google.cloud.kafka.connect.bigtable.version.PackageMetadata;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import org.apache.kafka.connect.runtime.rest.entities.PluginInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class VersionIT extends BaseKafkaConnectIT {
  @Test
  // Note that it fails within IntelliJ. Test with `mvn` directly.
  public void testVersionIsSet() throws IOException, InterruptedException {
    String url = connect.endpointForResource("connector-plugins");
    HttpClient http = HttpClient.newHttpClient();
    HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
    HttpResponse<String> response = http.send(req, HttpResponse.BodyHandlers.ofString());
    ObjectMapper mapper =
        new ObjectMapper()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
    PluginInfo[] pluginInfos = mapper.readValue(response.body(), PluginInfo[].class);
    PluginInfo pluginInfo =
        Arrays.stream(pluginInfos)
            .filter(i -> i.className().equals(BigtableSinkConnector.class.getCanonicalName()))
            .findFirst()
            .get();
    assertNotEquals(PackageMetadata.UNKNOWN_VERSION, pluginInfo.version());
  }
}
