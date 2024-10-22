package com.google.cloud.bigtable.data.v2.stub.metrics;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.NoCredentials;
import io.opentelemetry.api.OpenTelemetry;
import java.io.IOException;
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
    OpenTelemetry otel4 =
        provider.getOpenTelemetry("fake-endpoint:123", GoogleCredentials.getApplicationDefault());
    // everything is the same, should return the same instance
    OpenTelemetry otel5 =
        provider.getOpenTelemetry("fake-endpoint:123", NoCredentials.getInstance());

    Assert.assertNotEquals(toCompare, otel2);
    Assert.assertNotEquals(toCompare, otel3);
    Assert.assertNotEquals(toCompare, otel4);
    Assert.assertEquals(toCompare, otel5);

    provider.toString();
  }
}
