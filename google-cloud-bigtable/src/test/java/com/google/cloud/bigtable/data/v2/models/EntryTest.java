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

import com.google.protobuf.ByteString;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EntryTest {
  private void validateSerializationRoundTrip(Object obj)
      throws IOException, ClassNotFoundException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(obj);
    oos.close();
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
    assertThat(ois.readObject()).isEqualTo(obj);
  }

  @Test
  public void serializationTest() throws IOException, ClassNotFoundException {
    // DeleteFamily
    Entry deleteFamilyEntry = new DeleteFamily("fake-family");
    validateSerializationRoundTrip(deleteFamilyEntry);

    // DeleteCell
    Entry deleteCellsEntry =
        new DeleteCells(
            "fake-family",
            ByteString.copyFromUtf8("fake-qualifier"),
            Range.TimestampRange.create(1000L, 2000L));
    validateSerializationRoundTrip(deleteCellsEntry);

    // SetCell
    Entry setCellEntry =
        new SetCell(
            "fake-family",
            ByteString.copyFromUtf8("fake-qualifier"),
            1000,
            ByteString.copyFromUtf8("fake-value"));
    validateSerializationRoundTrip(setCellEntry);
  }

  @Test
  public void deleteFamilyTest() {
    Entry deleteFamilyEntry = new DeleteFamily("fake-family");
    DeleteFamily deleteFamily = (DeleteFamily) deleteFamilyEntry;
    Assert.assertEquals("fake-family", deleteFamily.getFamilyName());
  }

  @Test
  public void deleteCellsTest() {
    Entry deleteCellEntry =
        new DeleteCells(
            "fake-family",
            ByteString.copyFromUtf8("fake-qualifier"),
            Range.TimestampRange.create(1000L, 2000L));
    DeleteCells deleteCells = (DeleteCells) deleteCellEntry;
    Assert.assertEquals("fake-family", deleteCells.getFamilyName());
    Assert.assertEquals(ByteString.copyFromUtf8("fake-qualifier"), deleteCells.getQualifier());
    Assert.assertEquals(Range.TimestampRange.create(1000L, 2000L), deleteCells.getTimestampRange());
  }

  @Test
  public void setSellTest() {
    Entry setCellEntry =
        new SetCell(
            "fake-family",
            ByteString.copyFromUtf8("fake-qualifier"),
            1000,
            ByteString.copyFromUtf8("fake-value"));
    SetCell setCell = (SetCell) setCellEntry;
    Assert.assertEquals("fake-family", setCell.getFamilyName());
    Assert.assertEquals(ByteString.copyFromUtf8("fake-qualifier"), setCell.getQualifier());
    Assert.assertEquals(1000, setCell.getTimestamp());
    Assert.assertEquals(ByteString.copyFromUtf8("fake-value"), setCell.getValue());
  }
}
