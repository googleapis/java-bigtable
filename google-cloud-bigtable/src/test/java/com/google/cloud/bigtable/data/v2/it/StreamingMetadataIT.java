package com.google.cloud.bigtable.data.v2.it;

import com.google.api.core.ApiFuture;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.cloud.bigtable.data.v2.models.RowCell;
import com.google.cloud.bigtable.data.v2.models.RowMutation;
import com.google.cloud.bigtable.test_helpers.env.TestEnvRule;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import org.junit.ClassRule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StreamingMetadataIT {
    @ClassRule
    public static TestEnvRule testEnvRule = new TestEnvRule();

    public void test() {
        String prefix = UUID.randomUUID().toString();
        int numRows = 5;
        List<Row> expectedRows = Lists.newArrayList();
        String uniqueKey = prefix + "-read";

        long timestampMicros = System.currentTimeMillis() * 1_000;

        for (int i = 0; i < numRows; i++) {
            testEnvRule
                    .env()
                    .getDataClient()
                    .mutateRowCallable()
                    .call(
                            RowMutation.create(testEnvRule.env().getTableId(), uniqueKey + "-" + i)
                                    .setCell(testEnvRule.env().getFamilyId(), "q", timestampMicros, "my-value"));

            expectedRows.add(
                    Row.create(
                            ByteString.copyFromUtf8(uniqueKey + "-" + i),
                            ImmutableList.of(
                                    RowCell.create(
                                            testEnvRule.env().getFamilyId(),
                                            ByteString.copyFromUtf8("q"),
                                            timestampMicros,
                                            ImmutableList.<String>of(),
                                            ByteString.copyFromUtf8("my-value")))));
        }

        String tableId = testEnvRule.env().getTableId();
    }
}
