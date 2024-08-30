package com.google.cloud.bigtable.admin.v2.models;

import com.google.bigtable.admin.v2.TableName;

public class ConsistencyParams {
    public enum ConsistencyMode {
        /**
         * Checks that reads using an app profile with `StandardIsolation` can
         * see all writes committed before the token was created, even if the
         * read and write target different clusters.
         */
        STANDARD,

        /**
         * Checks that reads using an app profile with `DataBoostIsolationReadOnly`
         * can see all writes committed before the token was created, but only if
         * the read and write target the same cluster.
         */
        DATA_BOOST;
    }
    private TableName tableName;
    private ConsistencyMode mode;

    public ConsistencyParams(TableName tableName, ConsistencyMode mode) {
        this.tableName = tableName;
        this.mode = mode;
    }

    public TableName TableName() {
        return tableName;
    }

    public ConsistencyMode Mode() {
        return mode;
    }
}
