/*
 * Copyright 2019 Google LLC
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
package com.google.cloud.bigtable.admin.v2.internal;

import com.google.api.core.InternalApi;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

/**
 * Internal helper to compose full resource names.
 *
 * <p>This class is considered an internal implementation detail and not meant to be used by
 * applications.
 */
@InternalApi
public class NameUtil {
  private static final Pattern TABLE_PATTERN =
      Pattern.compile("projects/([^/]+)/instances/([^/]+)/tables/([^/]+)");
  private static final Pattern LOCATION_PATTERN =
      Pattern.compile("projects/([^/]+)/locations/([^/]+)");
  private static final Pattern BACKUP_PATTERN =
      Pattern.compile("projects/([^/]+)/instances/([^/]+)/clusters/([^/]+)/backups/([^/]+)");

  private static final Pattern AUTHORIZED_VIEW_PATTERN =
      Pattern.compile("projects/([^/]+)/instances/([^/]+)/tables/([^/]+)/authorizedViews/([^/]+)");

  private static final Pattern SCHEMA_BUNDLE_PATTERN =
      Pattern.compile("projects/([^/]+)/instances/([^/]+)/tables/([^/]+)/schemaBundles/([^/]+)");

  public static String formatProjectName(String projectId) {
    return "projects/" + projectId;
  }

  public static String formatInstanceName(String projectId, String instanceId) {
    return formatProjectName(projectId) + "/instances/" + instanceId;
  }

  public static String formatTableName(String projectId, String instanceId, String tableId) {
    return formatInstanceName(projectId, instanceId) + "/tables/" + tableId;
  }

  public static String formatMaterializedViewName(
      @Nonnull String projectId, @Nonnull String instanceId, @Nonnull String materializedViewId) {
    return formatInstanceName(projectId, instanceId) + "/materializedViews/" + materializedViewId;
  }

  public static String formatLogicalViewName(
      @Nonnull String projectId, @Nonnull String instanceId, @Nonnull String logicalViewId) {
    return formatInstanceName(projectId, instanceId) + "/logicalViews/" + logicalViewId;
  }

  public static String formatLocationName(String projectId, String zone) {
    return formatProjectName(projectId) + "/locations/" + zone;
  }

  public static String formatBackupName(
      String projectId, String instanceId, String clusterId, String backupId) {
    return formatClusterName(projectId, instanceId, clusterId) + "/backups/" + backupId;
  }

  public static String formatAuthorizedViewName(
      String projectId, String instanceId, String tableId, String viewId) {
    return formatTableName(projectId, instanceId, tableId) + "/authorizedViews/" + viewId;
  }

  public static String formatSchemaBundleName(
      String projectId, String instanceId, String tableId, String bundleId) {
    return formatTableName(projectId, instanceId, tableId) + "/schemaBundles/" + bundleId;
  }

  public static String extractTableIdFromTableName(String fullTableName) {
    Matcher matcher = TABLE_PATTERN.matcher(fullTableName);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid table name: " + fullTableName);
    }
    return matcher.group(3);
  }

  public static String extractBackupIdFromBackupName(String fullBackupName) {
    Matcher matcher = BACKUP_PATTERN.matcher(fullBackupName);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid backup name: " + fullBackupName);
    }
    return matcher.group(4);
  }

  public static String extractAuthorizedViewIdFromAuthorizedViewName(
      String fullAuthorizedViewName) {
    Matcher matcher = AUTHORIZED_VIEW_PATTERN.matcher(fullAuthorizedViewName);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid authorized view name: " + fullAuthorizedViewName);
    }
    return matcher.group(4);
  }

  public static String extractSchemaBundleIdFromSchemaBundleName(String fullSchemaBundleName) {
    Matcher matcher = SCHEMA_BUNDLE_PATTERN.matcher(fullSchemaBundleName);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid schema bundle name: " + fullSchemaBundleName);
    }
    return matcher.group(4);
  }

  public static String extractZoneIdFromLocationName(String fullLocationName) {
    Matcher matcher = LOCATION_PATTERN.matcher(fullLocationName);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Invalid location name: " + fullLocationName);
    }
    return matcher.group(2);
  }

  public static String formatClusterName(String projectId, String instanceId, String clusterId) {
    return formatInstanceName(projectId, instanceId) + "/clusters/" + clusterId;
  }

  public static String formatAppProfileName(
      String projectId, String instanceId, String appProfileId) {
    return formatInstanceName(projectId, instanceId) + "/appProfiles/" + appProfileId;
  }
}
