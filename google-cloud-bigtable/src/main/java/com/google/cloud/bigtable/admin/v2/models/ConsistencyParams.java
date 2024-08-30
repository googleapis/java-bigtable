package com.google.cloud.bigtable.admin.v2.models;

import com.google.auto.value.AutoValue;
import com.google.bigtable.admin.v2.TableName;

@AutoValue
public abstract class ConsistencyParams {
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
    public static ConsistencyParams create(TableName tableName, ConsistencyMode mode) {
        return new AutoValue_ConsistencyParams(tableName, mode);
    }

    public abstract TableName getTableName();

    public abstract ConsistencyMode getConsistencyMode();
}
