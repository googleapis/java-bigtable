package com.google.cloud.bigtable.data.v2.it;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.TruthJUnit.assume;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.test_helpers.env.AbstractTestEnv.ConnectionMode;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.protobuf.ByteString;
import java.util.UUID;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BatchEndpointIT {
  @ClassRule public static TestEnvRule testEnvRule = new TestEnvRule();

  private BigtableDataClient client;

  @Before
  public void setUp() throws Exception {
    assume()
        .withMessage("Batch endpoint only exist prod")
        .that(testEnvRule.env().getDataClientSettings().getStubSettings().getEndpoint())
        .isEqualTo("bigtable.googleapis.com:443");

    // TODO: remove this when batch-bigtable.googleapis.com supports DirectPath
    assume()
        .withMessage("batch endpoint cannot only be tested with DirectPath requirements")
        .that(testEnvRule.env().getConnectionMode())
        .isNoneOf(ConnectionMode.REQUIRE_DIRECT_PATH, ConnectionMode.REQUIRE_DIRECT_PATH);

    BigtableDataSettings.Builder settingsBuilder =
        testEnvRule.env().getDataClientSettings().toBuilder();

    // Override the endpoint
    settingsBuilder.stubSettings().setEndpoint("batch-bigtable.googleapis.com:443");

    client = BigtableDataClient.create(settingsBuilder.build());
  }

  @Test
  public void testCanSendRPCs() {
    String tableId = testEnvRule.env().getTableId();
    String rowKey = UUID.randomUUID().toString();
    String family = testEnvRule.env().getFamilyId();
    client.mutateRow(RowMutation.create(tableId, rowKey).setCell(family, "q", "value"));
    Row row = client.readRow(testEnvRule.env().getTableId(), rowKey);
    assertThat(row.getKey()).isEqualTo(ByteString.copyFromUtf8(rowKey));
  }
}
