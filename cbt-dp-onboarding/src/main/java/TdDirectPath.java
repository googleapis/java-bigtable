import com.google.auth.oauth2.GoogleCredentials;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.BigtableGrpc.BigtableBlockingStub;
import com.google.bigtable.v2.FeatureFlags;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import io.grpc.CallCredentials;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ChannelCredentials;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.Metadata.Key;
import io.grpc.MethodDescriptor;
import io.grpc.alts.GoogleDefaultChannelCredentials;
import io.grpc.auth.MoreCallCredentials;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * Invoke with:
 * {@code
 *  mvn exec:java -Dexec.mainClass=TdDirectPath \
 *    -Dbigtable.host="<ip target>"
 * }
 *
 * To enable verbose logs:
 * {@code
 *   mvn compile exec:java -Dexec.mainClass=TdDirectPath \
 *     -Dbigtable.host="<td target>" \
 *     -Djava.util.logging.config.file=src/main/resources/logging.properties
 * }
 */
public class TdDirectPath {
  private static final Logger LOG = Logger.getLogger(TdDirectPath.class.getName());

  private static final Metadata.Key<String> REQUEST_PARAMS_KEY =
      Key.of("x-goog-request-params", Metadata.ASCII_STRING_MARSHALLER);
  private static final Key<String> FEATURE_FLAGS_KEY =
      Key.of("bigtable-features", Metadata.ASCII_STRING_MARSHALLER);

  private static final String PROJECT_ID = "google.com:cloud-bigtable-dev";
  private static final String INSTANCE_ID = "igorbernstein-dev";
  private static final String TABLE_ID = "table2";
  private static final String TABLE_NAME = String.format("projects/%s/instances/%s/tables/%s",
      PROJECT_ID, INSTANCE_ID, TABLE_ID);

  public static void main(String[] args) throws IOException {
    String host = System.getProperty("bigtable.endpoint");

    // DirectPath specific
    String target = "google-c2p:///" + host;

    ChannelCredentials channelCredentials = GoogleDefaultChannelCredentials.create();
    ManagedChannel channel = Grpc.newChannelBuilder(target, channelCredentials).build();

    // Common to DirectPath & CloudPath
    CallCredentials callCredentials = MoreCallCredentials.from(
        GoogleCredentials.getApplicationDefault());
    BigtableBlockingStub stub = BigtableGrpc.newBlockingStub(channel)
        .withCallCredentials(callCredentials);


    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    FeatureFlags featureFlags =
        FeatureFlags.newBuilder()
            .setTrafficDirectorEnabled(true)
            .setDirectAccessRequested(true)
            .build();

    featureFlags.writeTo(baos);
    String encodedFeatureFlags = new String(Base64.getUrlEncoder().encode(baos.toByteArray()));
    String requestParams = String.format(
        "table_name=%s&app_profile_id=%s",
        URLEncoder.encode(TABLE_NAME, StandardCharsets.UTF_8),
        URLEncoder.encode("default", StandardCharsets.UTF_8));


    ReadRowsRequest request = ReadRowsRequest.newBuilder()
        .setTableName(TABLE_NAME)
        .setRowsLimit(1)
        .build();

    stub = stub.withInterceptors(
        new ClientInterceptor() {
          @Override
          public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
              MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
            return new SimpleForwardingClientCall<>(next.newCall(method, callOptions)) {
              @Override
              public void start(Listener<RespT> responseListener, Metadata headers) {
                headers.put(FEATURE_FLAGS_KEY, encodedFeatureFlags);
                headers.put(REQUEST_PARAMS_KEY, requestParams);
                super.start(responseListener, headers);
              }
            };
          }
        }
    );

    LOG.info("About send Request");

    for (Iterator<ReadRowsResponse> it = stub.readRows(request); it.hasNext();) {
      System.out.println(it.next());
    }
    LOG.info("Done with request");

    // Cleanup
    channel.shutdown();
  }
}
