package com.google.cloud.bigtable.data.v2.stub;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.testing.FakeServiceGrpc.FakeServiceImplBase;
import com.google.auth.Credentials;
import com.google.auth.oauth2.IdToken;
import com.google.auth.oauth2.OAuth2Credentials;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(JUnit4.class)
public class ChannelPrimingTest {
  @Rule
  public final MockitoRule mockitoJUnit = MockitoJUnit.rule();

  private Server server;
  private FakeService fakeService;
  private EnhancedBigtableStub stub;
  private FakeCrendentialsProvider fakeCredsProvider;

  @Before
  public void setup() throws IOException {
    final int port;
    try (ServerSocket ss = new ServerSocket(0)) {
      port = ss.getLocalPort();
    }

    server = ServerBuilder.forPort(port)
        .addService(new FakeService())
        .build();

    fakeCredsProvider = new FakeCrendentialsProvider();

    EnhancedBigtableStubSettings settings = EnhancedBigtableStubSettings.newBuilder()
        .setProjectId("my-project")
        .setInstanceId("my-instance")
        .setAppProfileId("my-app-profile")
        .setRefreshingChannel(true)
        .setPrimedTableId("table1", "table2")
        .setCredentialsProvider(fakeCredsProvider)
        .setEndpoint("localhost:" + port)
        .build();

    stub = EnhancedBigtableStub.create(settings);
  }

  @After
  public void teardown() {
    if (stub != null) {
      stub.close();
    }
    if (server != null) {
      server.shutdown();
    }
  }

  @Test
  public void testMoo() {

  }

  static class FakeCrendentialsProvider implements CredentialsProvider {
    private AtomicInteger generation = new AtomicInteger();

    @Override
    public Credentials getCredentials() throws IOException {
      return new FakeCredentials("generation" + generation.getAndIncrement());
    }
  }

  static class FakeCredentials extends Credentials {
    private final String token;

    FakeCredentials(String token) {
      this.token = token;
    }

    @Override
    public String getAuthenticationType() {
      return "fake";
    }

    @Override
    public Map<String, List<String>> getRequestMetadata(URI uri) throws IOException {
      return ImmutableMap.<String, List<String>>of("key", ImmutableList.of(token));
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
    public void refresh() throws IOException {
    }
  }

  static class FakeService extends BigtableGrpc.BigtableImplBase {
    List<ReadRowsRequest> requests = new ArrayList<>();


  }
}
