package com.google.cloud.bigtable.admin.v2.models;

import com.google.api.core.InternalApi;
import com.google.bigtable.admin.v2.CheckConsistencyRequest;
import com.google.bigtable.admin.v2.TableName;
import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

public class ConsistencyParams {
    public enum ConsistencyMode {
        /**
         * Checks that reads using an app profile with `StandardIsolation` can
         * see all writes committed before the token was created, even if the
         * read and write target different clusters.
         */
        STANDARD(CheckConsistencyRequest.ModeCase.STANDARD_READ_REMOTE_WRITES),

        /**
         * Checks that reads using an app profile with `DataBoostIsolationReadOnly`
         * can see all writes committed before the token was created, but only if
         * the read and write target the same cluster.
         */
        DATA_BOOST(CheckConsistencyRequest.ModeCase.DATA_BOOST_READ_LOCAL_WRITES);

        @Nonnull
        private final CheckConsistencyRequest.ModeCase proto;

        ConsistencyMode(CheckConsistencyRequest.ModeCase proto) {
            this.proto = proto;
        }

        /**
         * Wraps the protobuf. This method is considered an internal implementation detail and not meant
         * to be used by applications.
         */
        @InternalApi
        public static ConsistencyParams.ConsistencyMode fromProto(CheckConsistencyRequest.ModeCase proto) {
            for (ConsistencyParams.ConsistencyMode mode : values()) {
                if (mode.proto.equals(proto)) {
                    return mode;
                }
            }
            throw new IllegalArgumentException("Unknown consistency mode: " + proto);
        }

        /**
         * Creates the request protobuf. This method is considered an internal implementation detail and
         * not meant to be used by applications.
         */
        @InternalApi
        public CheckConsistencyRequest.ModeCase toProto() {
            return proto;
        }
    }

    private final TableName tableName;

    private final ConsistencyMode consistencyMode;

    ConsistencyParams(TableName tableName, ConsistencyMode mode) {
        Preconditions.checkNotNull(tableName);
        Preconditions.checkNotNull(mode);
        this.tableName = tableName;
        this.consistencyMode = mode;
    }

    public static ConsistencyParams of(TableName tableName, ConsistencyMode mode) {
        return new ConsistencyParams(tableName, mode);
    }

    public TableName getTableName() {
        return tableName;
    }

    public ConsistencyMode getConsistencyMode() {
        return consistencyMode;
    }
}
