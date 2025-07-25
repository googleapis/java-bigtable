/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.bigtable.admin.v2;

import com.google.api.pathtemplate.PathTemplate;
import com.google.api.resourcenames.ResourceName;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

// AUTO-GENERATED DOCUMENTATION AND CLASS.
@Generated("by gapic-generator-java")
public class SchemaBundleName implements ResourceName {
  private static final PathTemplate PROJECT_INSTANCE_TABLE_SCHEMA_BUNDLE =
      PathTemplate.createWithoutUrlEncoding(
          "projects/{project}/instances/{instance}/tables/{table}/schemaBundles/{schema_bundle}");
  private volatile Map<String, String> fieldValuesMap;
  private final String project;
  private final String instance;
  private final String table;
  private final String schemaBundle;

  @Deprecated
  protected SchemaBundleName() {
    project = null;
    instance = null;
    table = null;
    schemaBundle = null;
  }

  private SchemaBundleName(Builder builder) {
    project = Preconditions.checkNotNull(builder.getProject());
    instance = Preconditions.checkNotNull(builder.getInstance());
    table = Preconditions.checkNotNull(builder.getTable());
    schemaBundle = Preconditions.checkNotNull(builder.getSchemaBundle());
  }

  public String getProject() {
    return project;
  }

  public String getInstance() {
    return instance;
  }

  public String getTable() {
    return table;
  }

  public String getSchemaBundle() {
    return schemaBundle;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  public static SchemaBundleName of(
      String project, String instance, String table, String schemaBundle) {
    return newBuilder()
        .setProject(project)
        .setInstance(instance)
        .setTable(table)
        .setSchemaBundle(schemaBundle)
        .build();
  }

  public static String format(String project, String instance, String table, String schemaBundle) {
    return newBuilder()
        .setProject(project)
        .setInstance(instance)
        .setTable(table)
        .setSchemaBundle(schemaBundle)
        .build()
        .toString();
  }

  public static SchemaBundleName parse(String formattedString) {
    if (formattedString.isEmpty()) {
      return null;
    }
    Map<String, String> matchMap =
        PROJECT_INSTANCE_TABLE_SCHEMA_BUNDLE.validatedMatch(
            formattedString, "SchemaBundleName.parse: formattedString not in valid format");
    return of(
        matchMap.get("project"),
        matchMap.get("instance"),
        matchMap.get("table"),
        matchMap.get("schema_bundle"));
  }

  public static List<SchemaBundleName> parseList(List<String> formattedStrings) {
    List<SchemaBundleName> list = new ArrayList<>(formattedStrings.size());
    for (String formattedString : formattedStrings) {
      list.add(parse(formattedString));
    }
    return list;
  }

  public static List<String> toStringList(List<SchemaBundleName> values) {
    List<String> list = new ArrayList<>(values.size());
    for (SchemaBundleName value : values) {
      if (value == null) {
        list.add("");
      } else {
        list.add(value.toString());
      }
    }
    return list;
  }

  public static boolean isParsableFrom(String formattedString) {
    return PROJECT_INSTANCE_TABLE_SCHEMA_BUNDLE.matches(formattedString);
  }

  @Override
  public Map<String, String> getFieldValuesMap() {
    if (fieldValuesMap == null) {
      synchronized (this) {
        if (fieldValuesMap == null) {
          ImmutableMap.Builder<String, String> fieldMapBuilder = ImmutableMap.builder();
          if (project != null) {
            fieldMapBuilder.put("project", project);
          }
          if (instance != null) {
            fieldMapBuilder.put("instance", instance);
          }
          if (table != null) {
            fieldMapBuilder.put("table", table);
          }
          if (schemaBundle != null) {
            fieldMapBuilder.put("schema_bundle", schemaBundle);
          }
          fieldValuesMap = fieldMapBuilder.build();
        }
      }
    }
    return fieldValuesMap;
  }

  public String getFieldValue(String fieldName) {
    return getFieldValuesMap().get(fieldName);
  }

  @Override
  public String toString() {
    return PROJECT_INSTANCE_TABLE_SCHEMA_BUNDLE.instantiate(
        "project", project, "instance", instance, "table", table, "schema_bundle", schemaBundle);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o != null && getClass() == o.getClass()) {
      SchemaBundleName that = ((SchemaBundleName) o);
      return Objects.equals(this.project, that.project)
          && Objects.equals(this.instance, that.instance)
          && Objects.equals(this.table, that.table)
          && Objects.equals(this.schemaBundle, that.schemaBundle);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h = 1;
    h *= 1000003;
    h ^= Objects.hashCode(project);
    h *= 1000003;
    h ^= Objects.hashCode(instance);
    h *= 1000003;
    h ^= Objects.hashCode(table);
    h *= 1000003;
    h ^= Objects.hashCode(schemaBundle);
    return h;
  }

  /**
   * Builder for
   * projects/{project}/instances/{instance}/tables/{table}/schemaBundles/{schema_bundle}.
   */
  public static class Builder {
    private String project;
    private String instance;
    private String table;
    private String schemaBundle;

    protected Builder() {}

    public String getProject() {
      return project;
    }

    public String getInstance() {
      return instance;
    }

    public String getTable() {
      return table;
    }

    public String getSchemaBundle() {
      return schemaBundle;
    }

    public Builder setProject(String project) {
      this.project = project;
      return this;
    }

    public Builder setInstance(String instance) {
      this.instance = instance;
      return this;
    }

    public Builder setTable(String table) {
      this.table = table;
      return this;
    }

    public Builder setSchemaBundle(String schemaBundle) {
      this.schemaBundle = schemaBundle;
      return this;
    }

    private Builder(SchemaBundleName schemaBundleName) {
      this.project = schemaBundleName.project;
      this.instance = schemaBundleName.instance;
      this.table = schemaBundleName.table;
      this.schemaBundle = schemaBundleName.schemaBundle;
    }

    public SchemaBundleName build() {
      return new SchemaBundleName(this);
    }
  }
}
