package com.google.cloud.bigtable.admin.v2.models;

import com.google.bigtable.admin.v2.TableName;

public class CheckConsistencyParams {
    public enum CheckConsistencyMode {
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
    private CheckConsistencyMode mode;

    public CheckConsistencyParams(TableName tableName, CheckConsistencyMode mode) {
        this.tableName = tableName;
        this.mode = mode;
    }

    public TableName TableName() {
        return tableName;
    }

    public CheckConsistencyMode Mode() {
        return mode;
    }
}
