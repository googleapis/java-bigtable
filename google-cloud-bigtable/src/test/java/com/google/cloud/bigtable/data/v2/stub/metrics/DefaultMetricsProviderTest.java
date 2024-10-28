/*
 * Copyright 2024 Google LLC
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
package com.google.cloud.bigtable.data.v2.stub.metrics;

import com.google.auth.Credentials;
import com.google.cloud.NoCredentials;
import com.google.common.collect.ImmutableMap;
import io.opentelemetry.api.OpenTelemetry;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DefaultMetricsProviderTest {

  @Test
  public void test() throws IOException {
    DefaultMetricsProvider provider = DefaultMetricsProvider.INSTANCE;

    OpenTelemetry toCompare =
        provider.getOpenTelemetry("fake-endpoint:123", NoCredentials.getInstance());
    // different endpoint should return a different instance
    OpenTelemetry otel2 = provider.getOpenTelemetry(null, NoCredentials.getInstance());
    // different endpoint and credential should return a different instance
    OpenTelemetry otel3 = provider.getOpenTelemetry(null, null);
    // same endpoint, different credential, should return a different instance
    OpenTelemetry otel4 = provider.getOpenTelemetry("fake-endpoint:123", new FakeCredentials());
    // everything is the same, should return the same instance
    OpenTelemetry otel5 =
        provider.getOpenTelemetry("fake-endpoint:123", NoCredentials.getInstance());

    Assert.assertNotEquals(toCompare, otel2);
    Assert.assertNotEquals(toCompare, otel3);
    Assert.assertNotEquals(toCompare, otel4);
    Assert.assertEquals(toCompare, otel5);

    provider.toString();
  }

  private static class FakeCredentials extends Credentials {
    @Override
    public String getAuthenticationType() {
      return "fake";
    }

    @Override
    public Map<String, List<String>> getRequestMetadata(URI uri) throws IOException {
      return ImmutableMap.of("my-header", Arrays.asList("fake-credential"));
    }

    @Override
    public boolean hasRequestMetadata() {
      return true;
    }

    @Override
    public boolean hasRequestMetadataOnly() {
      return true;
    }

    @Override
    public void refresh() throws IOException {}
  }
}
