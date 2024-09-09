package com.google.cloud.bigtable.admin.v2.models;

import com.google.bigtable.admin.v2.CheckConsistencyRequest;
import com.google.bigtable.admin.v2.GenerateConsistencyTokenRequest;
import com.google.cloud.bigtable.data.v2.internal.RequestContextNoAP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class ConsistencyRequestTest {
    private final String PROJECT_ID = "my-project";
    private final String INSTANCE_ID = "my-instance";
    private final String TABLE_ID = "my-table";
    private final String CONSISTENCY_TOKEN = "my-token";

    @Test
    public void testToCheckConsistencyProtoWithStandard() {
        ConsistencyRequest consistencyRequest = ConsistencyRequest.forReplication(TABLE_ID);

        RequestContextNoAP requestContext = RequestContextNoAP.create(PROJECT_ID, INSTANCE_ID);

        CheckConsistencyRequest checkConsistencyRequest = consistencyRequest.toCheckConsistencyProto(requestContext, CONSISTENCY_TOKEN);

        assertThat(checkConsistencyRequest.getName().equals(TABLE_ID));
        assertThat(checkConsistencyRequest.getConsistencyToken().equals(CONSISTENCY_TOKEN));
        assertThat(checkConsistencyRequest.getModeCase().equals(CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES));
    }

    @Test
    public void testToCheckConsistencyProtoWithDataBoost() {
        ConsistencyRequest consistencyRequest = ConsistencyRequest.forDataBoost(TABLE_ID);

        RequestContextNoAP requestContext = RequestContextNoAP.create(PROJECT_ID, INSTANCE_ID);

        CheckConsistencyRequest checkConsistencyRequest = consistencyRequest.toCheckConsistencyProto(requestContext, CONSISTENCY_TOKEN);

        assertThat(checkConsistencyRequest.getName().equals(TABLE_ID));
        assertThat(checkConsistencyRequest.getConsistencyToken().equals(CONSISTENCY_TOKEN));
        assertThat(checkConsistencyRequest.getModeCase().equals(CheckConsistencyRequest.ModeCase.DATA_BOOST_READ_LOCAL_WRITES));
    }

    @Test
    public void testToGenerateTokenProto() {
        ConsistencyRequest consistencyRequest = ConsistencyRequest.forDataBoost(TABLE_ID);

        RequestContextNoAP requestContext = RequestContextNoAP.create(PROJECT_ID, INSTANCE_ID);

        GenerateConsistencyTokenRequest generateRequest = consistencyRequest.toGenerateTokenProto(requestContext);

        assertThat(generateRequest.getName().equals(TABLE_ID));
    }
}
