/*
 * Copyright 2024 Google LLC
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
package com.google.cloud.kafka.connect.bigtable.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.cloud.bigtable.data.v2.models.Mutation;
import com.google.cloud.bigtable.data.v2.models.Range;
import com.google.protobuf.ByteString;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MutationDataBuilderTest {
  private static final ByteString ROW_KEY =
      ByteString.copyFrom("ROW_KEY".getBytes(StandardCharsets.UTF_8));
  private static final String TARGET_TABLE_NAME = "table";
  private static final String COLUMN_FAMILY = "family";
  private static final ByteString COLUMN_QUALIFIER =
      ByteString.copyFrom("COLUMN".getBytes(StandardCharsets.UTF_8));
  private static final ByteString VALUE =
      ByteString.copyFrom("VALUE".getBytes(StandardCharsets.UTF_8));
  private static final Long TIMESTAMP = 2024L;
  private static final Range.TimestampRange TIMESTAMP_RANGE =
      Range.TimestampRange.create(0, TIMESTAMP);

  Mutation mutation;
  MutationDataBuilder mutationDataBuilder;

  @Before
  public void setUp() {
    mutation = mock(Mutation.class);
    mutationDataBuilder = new MutationDataBuilder(mutation);
  }

  @Test
  public void testEmpty() {
    assertTrue(mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY).isEmpty());
  }

  @Test
  public void testDeleteRow() {
    mutationDataBuilder.deleteRow();
    verify(mutation, times(1)).deleteRow();
    Optional<MutationData> mutationData =
        mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY);
    assertTrue(mutationData.isPresent());
    assertTrue(mutationData.get().getRequiredColumnFamilies().isEmpty());
  }

  @Test
  public void testDeleteCells() {
    mutationDataBuilder.deleteCells(COLUMN_FAMILY, COLUMN_QUALIFIER, TIMESTAMP_RANGE);
    verify(mutation, times(1)).deleteCells(COLUMN_FAMILY, COLUMN_QUALIFIER, TIMESTAMP_RANGE);
    Optional<MutationData> mutationData =
        mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY);
    assertTrue(mutationData.isPresent());
    assertEquals(Set.of(COLUMN_FAMILY), mutationData.get().getRequiredColumnFamilies());
  }

  @Test
  public void testDeleteFamily() {
    mutationDataBuilder.deleteFamily(COLUMN_FAMILY);
    verify(mutation, times(1)).deleteFamily(COLUMN_FAMILY);
    Optional<MutationData> mutationData =
        mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY);
    assertTrue(mutationData.isPresent());
    assertEquals(Set.of(COLUMN_FAMILY), mutationData.get().getRequiredColumnFamilies());
  }

  @Test
  public void testSetCell() {
    mutationDataBuilder.setCell(COLUMN_FAMILY, COLUMN_QUALIFIER, TIMESTAMP, VALUE);
    verify(mutation, times(1)).setCell(COLUMN_FAMILY, COLUMN_QUALIFIER, TIMESTAMP, VALUE);
    Optional<MutationData> mutationData =
        mutationDataBuilder.maybeBuild(TARGET_TABLE_NAME, ROW_KEY);
    assertTrue(mutationData.isPresent());
    assertEquals(Set.of(COLUMN_FAMILY), mutationData.get().getRequiredColumnFamilies());
  }
}
