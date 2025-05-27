package com.google.cloud.bigtable;

import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.data.v2.models.TableId;

import java.io.IOException;

public class UniverseDomainExample {

    public static void main(String[] args) throws IOException {
        BigtableDataSettings.Builder settings = BigtableDataSettings.newBuilder()
                .setProjectId("mpeddada-community")
                .setInstanceId("test-domain");

        settings.stubSettings().setUniverseDomain("{universe-domain}");

        settings.stubSettings().setMetricsEndpoint("monitoring.{universe-domain}:443");

        System.out.println("testing data API");
        try (BigtableDataClient client = BigtableDataClient.create(settings.build())) {
            System.out.println("sending request");
            client.mutateRow(RowMutation.create(TableId.of("test"), "row-key")
                    .setCell("cf", "q", "value"));
            System.out.println(client.readRow(TableId.of("test"), "row-key"));
        }

    }
}
