/*
 * Copyright 2022 Google LLC
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
package com.google.cloud.bigtable.data.v2.models;

import static com.google.common.truth.Truth.assertThat;

import com.google.bigtable.v2.RowRange;
import com.google.bigtable.v2.StreamContinuationToken;
import com.google.bigtable.v2.StreamPartition;
import com.google.cloud.bigtable.data.v2.models.Range.ByteStringRange;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ChangeStreamContinuationTokenTest {

  private final String TOKEN = "token";

  private ByteStringRange createFakeByteStringRange() {
    return ByteStringRange.create("a", "b");
  }

  // TODO: Get rid of this once we change ChangeStreamContinuationToken::getRowRange()
  // to ChangeStreamContinuationToken::getByteStringRange().
  private RowRange rowRangeFromByteStringRange(ByteStringRange byteStringRange) {
    return RowRange.newBuilder()
        .setStartKeyClosed(byteStringRange.getStart())
        .setEndKeyOpen(byteStringRange.getEnd())
        .build();
  }

  @Test
  public void basicTest() throws Exception {
    ByteStringRange byteStringRange = createFakeByteStringRange();
    ChangeStreamContinuationToken changeStreamContinuationToken =
        new ChangeStreamContinuationToken(byteStringRange, TOKEN);
    Assert.assertEquals(
        changeStreamContinuationToken.getRowRange(), rowRangeFromByteStringRange(byteStringRange));
    Assert.assertEquals(changeStreamContinuationToken.getToken(), TOKEN);

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(changeStreamContinuationToken);
    oos.close();
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
    ChangeStreamContinuationToken actual = (ChangeStreamContinuationToken) ois.readObject();
    assertThat(actual).isEqualTo(changeStreamContinuationToken);
  }

  @Test
  public void toProtoTest() {
    ByteStringRange byteStringRange = createFakeByteStringRange();
    RowRange fakeRowRange = rowRangeFromByteStringRange(byteStringRange);
    StreamContinuationToken proto =
        StreamContinuationToken.newBuilder()
            .setPartition(StreamPartition.newBuilder().setRowRange(fakeRowRange).build())
            .setToken(TOKEN)
            .build();
    ChangeStreamContinuationToken changeStreamContinuationToken =
        ChangeStreamContinuationToken.fromProto(proto);
    Assert.assertEquals(changeStreamContinuationToken.getRowRange(), fakeRowRange);
    Assert.assertEquals(changeStreamContinuationToken.getToken(), TOKEN);
    Assert.assertEquals(
        changeStreamContinuationToken,
        ChangeStreamContinuationToken.fromProto(changeStreamContinuationToken.toProto()));
  }

  @Test
  public void toByteStringTest() throws Exception {
    ByteStringRange byteStringRange = createFakeByteStringRange();
    ChangeStreamContinuationToken changeStreamContinuationToken =
        new ChangeStreamContinuationToken(byteStringRange, TOKEN);
    Assert.assertEquals(
        changeStreamContinuationToken.getRowRange(), rowRangeFromByteStringRange(byteStringRange));
    Assert.assertEquals(changeStreamContinuationToken.getToken(), TOKEN);
    Assert.assertEquals(
        changeStreamContinuationToken,
        ChangeStreamContinuationToken.fromByteString(changeStreamContinuationToken.toByteString()));
  }
}
