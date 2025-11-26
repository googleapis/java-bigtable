import com.google.auth.oauth2.GoogleCredentials;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.BigtableGrpc.BigtableBlockingStub;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import io.grpc.CallCredentials;
import io.grpc.ChannelCredentials;
import io.grpc.Grpc;
import io.grpc.ManagedChannel;
import io.grpc.alts.AltsChannelCredentials;
import io.grpc.auth.MoreCallCredentials;
import java.io.IOException;
import java.util.Iterator;

/**
 * Invoke with:
 * {@code
 *  mvn exec:java -Dexec.mainClass=RawDirectPath \
 *    -Dbigtable.host="<ip address>" -Dbigtable.port=<port>
 * }
 *
 * To enable verbose logs:
 * {@code
 *   mvn compile exec:java -Dexec.mainClass=RawDirectPath \
 *     -Dbigtable.host="<ip address>" -Dbigtable.port=<port> \
 *     -Djava.util.logging.config.file=src/main/resources/logging.properties
 * }
 */
public class RawDirectPath {
  private static final String PROJECT_ID = "autonomous-mote-782";
  private static final String INSTANCE_ID = "sushanb-prober";
  private static final String TABLE_ID = "repro";
  private static final String TABLE_NAME = String.format("projects/%s/instances/%s/tables/%s",
      PROJECT_ID, INSTANCE_ID, TABLE_ID);

  public static void main(String[] args) throws IOException {
    String host = System.getProperty("bigtable.host");
    int port = Integer.getInteger("bigtable.port");

    // DirectPath specific
    ChannelCredentials channelCredentials = AltsChannelCredentials.create();
    ManagedChannel channel = Grpc.newChannelBuilderForAddress(host, port, channelCredentials).build();

    // Common to DirectPath & CloudPath
    CallCredentials callCredentials = MoreCallCredentials.from(
        GoogleCredentials.getApplicationDefault());
    BigtableBlockingStub stub = BigtableGrpc.newBlockingStub(channel)
        .withCallCredentials(callCredentials);

    ReadRowsRequest request = ReadRowsRequest.newBuilder()
        .setTableName(TABLE_NAME)
        .setRowsLimit(1)
        .build();

    for (Iterator<ReadRowsResponse> it = stub.readRows(request); it.hasNext();) {
      System.out.println(it.next());
    }

    // Cleanup
    channel.shutdown();
  }
}
