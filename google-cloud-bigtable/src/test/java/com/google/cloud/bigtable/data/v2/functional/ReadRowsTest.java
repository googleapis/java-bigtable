package com.google.cloud.bigtable.data.v2.functional;

import com.google.api.gax.rpc.InternalException;
import com.google.bigtable.v2.BigtableGrpc;
import com.google.bigtable.v2.ReadRowsRequest;
import com.google.bigtable.v2.ReadRowsResponse;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.cloud.bigtable.data.v2.BigtableDataSettings;
import com.google.cloud.bigtable.data.v2.FakeServiceBuilder;
import com.google.cloud.bigtable.data.v2.models.Query;
import com.google.cloud.bigtable.data.v2.models.Row;
import com.google.protobuf.ByteString;
import com.google.protobuf.BytesValue;
import com.google.protobuf.StringValue;
import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class ReadRowsTest {
    private FakeService service;
    private Server server;

    @Before
    public void setUp() throws Exception {
        service = new FakeService();
        server = FakeServiceBuilder.create(service)
                .start();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void rowMergingErrorsUseInternalStatus() throws Exception {
        BigtableDataSettings settings = BigtableDataSettings.newBuilderForEmulator(server.getPort())
                .setProjectId("fake-project")
                .setInstanceId("fake-instance")
                .build();

        service.readRowsResponses.add(
                ReadRowsResponse.newBuilder()
                        .addChunks(
                                ReadRowsResponse.CellChunk.newBuilder()
                                        .setRowKey(ByteString.copyFromUtf8("z"))
                                        .setFamilyName(StringValue.newBuilder().setValue("f"))
                                        .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")).build())
                                        .setTimestampMicros(1000)
                                        .setValue(ByteString.copyFromUtf8("v"))
                                        .setCommitRow(true)
                        )
                        .addChunks(
                                ReadRowsResponse.CellChunk.newBuilder()
                                        .setRowKey(ByteString.copyFromUtf8("a"))
                                        .setFamilyName(StringValue.newBuilder().setValue("f"))
                                        .setQualifier(BytesValue.newBuilder().setValue(ByteString.copyFromUtf8("q")).build())
                                        .setTimestampMicros(1000)
                                        .setValue(ByteString.copyFromUtf8("v"))
                                        .setCommitRow(true)
                        )
                        .build()
        );

        try (BigtableDataClient client = BigtableDataClient.create(settings)) {
            Assert.assertThrows(
                    InternalException.class,
                    () -> {
                        for (Row ignored : client.readRows(Query.create("fake-table"))) {

                        }
                    }
            );
        }
    }


    static class FakeService extends BigtableGrpc.BigtableImplBase {
        private List<ReadRowsResponse> readRowsResponses = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void readRows(ReadRowsRequest request, StreamObserver<ReadRowsResponse> responseObserver) {
            for (ReadRowsResponse r : readRowsResponses) {
                responseObserver.onNext(r);
            }
            responseObserver.onCompleted();
        }
    }
}
