/*
 * Copyright 2021 Google LLC
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
package com.google.cloud.bigtable;

import com.google.api.core.InternalApi;
import com.google.cloud.bigtable.data.v2.BigtableDataClient;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import java.io.IOException;
import java.util.Collection;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

/** Internal helper to update clirr-ignored-differences */
@InternalApi
class GenerateClirrIgnoredDifferences {
  private static final ImmutableSet<Integer> IGNORED_INTERNAL_DIFFS =
      ImmutableSet.of(
          6001, // (removed field): className, field
          7004, // (Method Argument Count Changed): className, method
          7005, // (Method Argument Type changed): className, method, to (to is a full new
                // signature)
          8001 // (Class removed): className
          );
  private static final Predicate<ClassInfo> IS_INTERNAL_API_CLASS =
      new Predicate<ClassInfo>() {
        @Override
        public boolean apply(@NullableDecl ClassInfo input) {
          return input.load().getAnnotation(InternalApi.class) != null;
        }
      };
  private static final Predicate<ClassInfo> IS_PKG_INFO =
      new Predicate<ClassInfo>() {
        @Override
        public boolean apply(@NullableDecl ClassInfo input) {
          return input.getSimpleName().equals("package-info");
        }
      };

  public static void main(String[] args) throws IOException {
    Collection<ClassInfo> bigtableClasses = getBigtableClasses();
    Collection<ClassInfo> bigtableInternalClasses =
        Collections2.filter(bigtableClasses, IS_INTERNAL_API_CLASS);

    for (ClassInfo cls : bigtableInternalClasses) {
      for (Integer diffType : IGNORED_INTERNAL_DIFFS) {
        System.out.printf(
            ""
                + "  <difference>\n"
                + "    <differenceType>%d</differenceType>\n"
                + "    <className>%s</className>\n"
                + "    <field>*</field>\n"
                + "    <method>*</method>\n"
                + "  </difference>\n",
            diffType, cls.getName());
      }
    }
  }

  private static Collection<ClassInfo> getBigtableClasses() throws IOException {
    ClassPath path = ClassPath.from(BigtableDataClient.class.getClassLoader());
    ImmutableSet<ClassInfo> results = path.getTopLevelClassesRecursive("com.google.cloud.bigtable");

    return Collections2.filter(results, Predicates.not(IS_PKG_INFO));
  }
}
