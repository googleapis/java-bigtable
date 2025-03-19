/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.cloud.bigtable.data.v2.stub.sql;

import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.bytesType;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.columnMetadata;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.metadata;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.prepareResponse;
import static com.google.cloud.bigtable.data.v2.stub.sql.SqlProtoFactory.stringType;
import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.api.core.SettableApiFuture;
import com.google.bigtable.v2.ExecuteQueryRequest;
import com.google.cloud.bigtable.data.v2.internal.NameUtil;
import com.google.cloud.bigtable.data.v2.internal.PrepareResponse;
import com.google.cloud.bigtable.data.v2.internal.PreparedStatementImpl;
import com.google.cloud.bigtable.data.v2.internal.ProtoResultSetMetadata;
import com.google.cloud.bigtable.data.v2.internal.RequestContext;
import com.google.cloud.bigtable.data.v2.models.sql.PreparedStatement;
import com.google.cloud.bigtable.data.v2.models.sql.ResultSetMetadata;
import com.google.protobuf.ByteString;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ExecuteQueryCallContextTest {
  private static final ByteString PREPARED_QUERY = ByteString.copyFromUtf8("test");
  private static final com.google.bigtable.v2.ResultSetMetadata METADATA =
      metadata(columnMetadata("foo", stringType()), columnMetadata("bar", bytesType()));
  private static final PreparedStatement PREPARED_STATEMENT =
      PreparedStatementImpl.create(
          PrepareResponse.fromProto(prepareResponse(PREPARED_QUERY, METADATA)));

  @Test
  public void testToRequest() {
    ExecuteQueryCallContext callContext =
        ExecuteQueryCallContext.create(
            PREPARED_STATEMENT.bind().setStringParam("foo", "val").build(),
            SettableApiFuture.create());
    RequestContext requestContext = RequestContext.create("project", "instance", "profile");
    ExecuteQueryRequest request = callContext.toRequest(requestContext);

    assertThat(request.getPreparedQuery()).isEqualTo(PREPARED_QUERY);
    assertThat(request.getAppProfileId()).isEqualTo("profile");
    assertThat(request.getInstanceName())
        .isEqualTo(NameUtil.formatInstanceName("project", "instance"));
    assertThat(request.getParamsMap().get("foo").getStringValue()).isEqualTo("val");
    assertThat(request.getParamsMap().get("foo").getType()).isEqualTo(stringType());
  }

  @Test
  public void testFirstResponseReceived() throws ExecutionException, InterruptedException {
    SettableApiFuture<ResultSetMetadata> mdFuture = SettableApiFuture.create();
    ExecuteQueryCallContext callContext =
        ExecuteQueryCallContext.create(PREPARED_STATEMENT.bind().build(), mdFuture);

    callContext.firstResponseReceived();
    assertThat(mdFuture.isDone()).isTrue();
    assertThat(mdFuture.get()).isEqualTo(ProtoResultSetMetadata.fromProto(METADATA));
  }

  @Test
  public void testSetMetadataException() {
    SettableApiFuture<ResultSetMetadata> mdFuture = SettableApiFuture.create();
    ExecuteQueryCallContext callContext =
        ExecuteQueryCallContext.create(PREPARED_STATEMENT.bind().build(), mdFuture);

    callContext.setMetadataException(new RuntimeException("test"));
    assertThat(mdFuture.isDone()).isTrue();
    ExecutionException e = assertThrows(ExecutionException.class, mdFuture::get);
    assertThat(e.getCause()).isInstanceOf(RuntimeException.class);
  }
}
