package com.google.cloud.bigtable.admin.v2.models;

import com.google.bigtable.admin.v2.TableName;

public class CheckConsistencyParams {
    public enum CheckConsistencyMode {
        Standard, DataBoost;
    }
    private TableName tableName;
    private CheckConsistencyMode mode;

    public CheckConsistencyParams(TableName tableName, CheckConsistencyMode mode) {
        this.tableName = tableName;
        this.mode = mode;
    }

    public TableName TableName() {
        return this.tableName;
    }

    public CheckConsistencyMode Mode() {
        return this.mode;
    }
}
