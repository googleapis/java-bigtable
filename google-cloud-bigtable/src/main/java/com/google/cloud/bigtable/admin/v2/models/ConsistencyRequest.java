package com.google.cloud.bigtable.admin.v2.models;

import com.google.api.core.InternalApi;
import com.google.auto.value.AutoValue;
import com.google.bigtable.admin.v2.*;
import com.google.cloud.bigtable.data.v2.internal.RequestContextNoAP;

import javax.annotation.Nonnull;

@AutoValue
public abstract class ConsistencyRequest {
    @Nonnull
    protected abstract String getTableId();
    @Nonnull
    protected abstract CheckConsistencyRequest.ModeCase getMode();

    public static ConsistencyRequest forReplication(String tableId) {
        return new AutoValue_ConsistencyRequest(tableId, CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES);
    }

    public static ConsistencyRequest forDataBoost(String tableId) {
        return new AutoValue_ConsistencyRequest(tableId, CheckConsistencyRequest.ModeCase.DATA_BOOST_READ_LOCAL_WRITES);
    }

    @InternalApi
    public CheckConsistencyRequest toCheckConsistencyProto(RequestContextNoAP requestContext, String token) {
        CheckConsistencyRequest.Builder builder = CheckConsistencyRequest.newBuilder();
        TableName tableName = TableName.of(requestContext.getProjectId(), requestContext.getInstanceId(), getTableId());

        if (getMode().equals(CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES)) {
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
        TableName tableName = TableName.of(requestContext.getProjectId(), requestContext.getInstanceId(), getTableId());

        return builder
                .setName(tableName.toString())
                .build();
    }
}
