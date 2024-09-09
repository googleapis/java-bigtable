package com.google.cloud.bigtable.admin.v2.models;

import com.google.api.core.InternalApi;
import com.google.bigtable.admin.v2.*;
import com.google.cloud.bigtable.data.v2.internal.RequestContextNoAP;
import com.google.common.base.Preconditions;

public class ConsistencyRequest {
    private final String tableId;

    private final CheckConsistencyRequest.ModeCase consistencyMode;

    ConsistencyRequest(String tableId, CheckConsistencyRequest.ModeCase consistencyMode) {
        Preconditions.checkNotNull(tableId);
        Preconditions.checkNotNull(consistencyMode);
        this.tableId = tableId;
        this.consistencyMode = consistencyMode;
    }

    public static ConsistencyRequest getStandardConsistencyRequest(String tableId) {
        return new ConsistencyRequest(tableId, CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES);
    }

    public static ConsistencyRequest getDataBoostConsistencyRequest(String tableId) {
        return new ConsistencyRequest(tableId, CheckConsistencyRequest.ModeCase.DATA_BOOST_READ_LOCAL_WRITES);
    }

    @InternalApi
    public CheckConsistencyRequest toCheckConsistencyProto(RequestContextNoAP requestContext, String token) {
        CheckConsistencyRequest.Builder builder = CheckConsistencyRequest.newBuilder();
        TableName tableName = TableName.of(requestContext.getProjectId(), requestContext.getInstanceId(), tableId);

        if (consistencyMode.equals(CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES)) {
            builder.setStandardReadRemoteWrites(StandardReadRemoteWrites.newBuilder().build());
        } else {
            builder.setDataBoostReadLocalWrites(DataBoostReadLocalWrites.newBuilder().build());
        }

        return builder
                .setName(tableName.toString())
                .setConsistencyToken(token)
                .build();
    }

    @InternalApi
    public GenerateConsistencyTokenRequest toGenerateTokenProto(RequestContextNoAP requestContext) {
        GenerateConsistencyTokenRequest.Builder builder =
                GenerateConsistencyTokenRequest.newBuilder();
        TableName tableName = TableName.of(requestContext.getProjectId(), requestContext.getInstanceId(), tableId);

        return builder
                .setName(tableName.toString())
                .build();
    }
}
