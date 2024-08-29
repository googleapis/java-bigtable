package com.google.cloud.bigtable.admin.v2.models;

import com.google.bigtable.admin.v2.TableName;

public class CheckConsistencyParams {
    public enum CheckConsistencyMode {
        STANDARD, DATA_BOOST;
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
