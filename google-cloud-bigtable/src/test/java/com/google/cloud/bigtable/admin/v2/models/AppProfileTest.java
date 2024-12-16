/*
 * Copyright 2018 Google LLC
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
package com.google.cloud.bigtable.admin.v2.models;

import static com.google.common.truth.Truth.assertThat;

import com.google.bigtable.admin.v2.AppProfile.SingleClusterRouting;
import com.google.bigtable.admin.v2.AppProfileName;
import com.google.cloud.bigtable.admin.v2.models.AppProfile.Priority;
import com.google.cloud.bigtable.admin.v2.models.AppProfile.SingleClusterRoutingPolicy;
import com.google.cloud.bigtable.admin.v2.models.AppProfile.StandardIsolationPolicy;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class AppProfileTest {
  private static final com.google.bigtable.admin.v2.AppProfile TEST_PROTO =
      com.google.bigtable.admin.v2.AppProfile.newBuilder()
          .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
          .setDescription("my description")
          .setSingleClusterRouting(
              SingleClusterRouting.newBuilder()
                  .setClusterId("my-cluster")
                  .setAllowTransactionalWrites(true)
                  .build())
          .setEtag("my-etag")
          .build();

  @Test
  public void testFromProto() {
    AppProfile profile = AppProfile.fromProto(TEST_PROTO);

    assertThat(profile.getInstanceId()).isEqualTo("my-instance");
    assertThat(profile.getId()).isEqualTo("my-profile");
    assertThat(profile.getDescription()).isEqualTo("my description");
    assertThat(profile.getPolicy()).isEqualTo(SingleClusterRoutingPolicy.of("my-cluster", true));
  }

  @Test
  public void testFromProtoWithMultiCluster() {
    AppProfile profile =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setMultiClusterRoutingUseAny(
                    com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny.newBuilder()
                        .build())
                .setEtag("my-etag")
                .build());

    assertThat(profile.getInstanceId()).isEqualTo("my-instance");
    assertThat(profile.getId()).isEqualTo("my-profile");
    assertThat(profile.getDescription()).isEqualTo("my description");
    assertThat(profile.getPolicy()).isEqualTo(AppProfile.MultiClusterRoutingPolicy.of());
  }

  @Test
  public void testFromProtoWithMultiClusterWithIds() {
    AppProfile profile =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setMultiClusterRoutingUseAny(
                    com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny.newBuilder()
                        .addAllClusterIds(ImmutableList.of("cluster-id-1", "cluster-id-2"))
                        .build())
                .setEtag("my-etag")
                .build());

    assertThat(profile.getInstanceId()).isEqualTo("my-instance");
    assertThat(profile.getId()).isEqualTo("my-profile");
    assertThat(profile.getDescription()).isEqualTo("my description");
    assertThat(profile.getPolicy())
        .isEqualTo(AppProfile.MultiClusterRoutingPolicy.of("cluster-id-1", "cluster-id-2"));
  }

  @Test
  public void testFromProtoWithStandardIsolation() {
    AppProfile profile =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setSingleClusterRouting(
                    SingleClusterRouting.newBuilder()
                        .setClusterId("my-cluster")
                        .setAllowTransactionalWrites(true)
                        .build())
                .setStandardIsolation(
                    com.google.bigtable.admin.v2.AppProfile.StandardIsolation.newBuilder()
                        .setPriority(
                            com.google.bigtable.admin.v2.AppProfile.Priority.PRIORITY_MEDIUM)
                        .build())
                .setEtag("my-etag")
                .build());

    assertThat(profile.getInstanceId()).isEqualTo("my-instance");
    assertThat(profile.getId()).isEqualTo("my-profile");
    assertThat(profile.getDescription()).isEqualTo("my description");
    assertThat(profile.getPolicy()).isEqualTo(SingleClusterRoutingPolicy.of("my-cluster", true));
    assertThat(profile.getIsolationPolicy()).isEqualTo(StandardIsolationPolicy.of(Priority.MEDIUM));
  }

  @Test
  public void testNoNameError() {
    Exception actualException = null;

    try {
      AppProfile.fromProto(TEST_PROTO.toBuilder().setName("").build());
    } catch (Exception e) {
      actualException = e;
    }

    assertThat(actualException).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void testNoPolicyError() {
    Exception actualException = null;

    try {
      AppProfile.fromProto(TEST_PROTO.toBuilder().clearSingleClusterRouting().build());
    } catch (Exception e) {
      actualException = e;
    }

    assertThat(actualException).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  public void testEquals() {
    AppProfile profile =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setMultiClusterRoutingUseAny(
                    com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny.newBuilder()
                        .addAllClusterIds(ImmutableList.of("cluster-id-1", "cluster-id-2"))
                        .build())
                .setStandardIsolation(
                    com.google.bigtable.admin.v2.AppProfile.StandardIsolation.newBuilder()
                        .setPriority(
                            com.google.bigtable.admin.v2.AppProfile.Priority.PRIORITY_MEDIUM)
                        .build())
                .setEtag("my-etag")
                .build());

    UpdateAppProfileRequest updateAppProfileRequest = UpdateAppProfileRequest.of(profile);
    UpdateAppProfileRequest updateAppProfileRequest2 = UpdateAppProfileRequest.of(profile);

    assertThat(updateAppProfileRequest).isEqualTo(updateAppProfileRequest2);

    AppProfile profile2 =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project-2", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setMultiClusterRoutingUseAny(
                    com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny.newBuilder()
                        .addAllClusterIds(ImmutableList.of("cluster-id-1", "cluster-id-2"))
                        .build())
                .setStandardIsolation(
                    com.google.bigtable.admin.v2.AppProfile.StandardIsolation.newBuilder()
                        .setPriority(
                            com.google.bigtable.admin.v2.AppProfile.Priority.PRIORITY_MEDIUM)
                        .build())
                .setEtag("my-etag")
                .build());
    UpdateAppProfileRequest updateAppProfileRequest3 = UpdateAppProfileRequest.of(profile2);

    assertThat(updateAppProfileRequest).isNotEqualTo(updateAppProfileRequest3);
  }

  @Test
  public void testHashCode() {
    AppProfile profile =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setMultiClusterRoutingUseAny(
                    com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny.newBuilder()
                        .addAllClusterIds(ImmutableList.of("cluster-id-1", "cluster-id-2"))
                        .build())
                .setStandardIsolation(
                    com.google.bigtable.admin.v2.AppProfile.StandardIsolation.newBuilder()
                        .setPriority(
                            com.google.bigtable.admin.v2.AppProfile.Priority.PRIORITY_MEDIUM)
                        .build())
                .setEtag("my-etag")
                .build());

    UpdateAppProfileRequest updateAppProfileRequest = UpdateAppProfileRequest.of(profile);
    UpdateAppProfileRequest updateAppProfileRequest2 = UpdateAppProfileRequest.of(profile);

    assertThat(updateAppProfileRequest.hashCode()).isEqualTo(updateAppProfileRequest2.hashCode());

    AppProfile profile2 =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project-2", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setMultiClusterRoutingUseAny(
                    com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny.newBuilder()
                        .addAllClusterIds(ImmutableList.of("cluster-id-1", "cluster-id-2"))
                        .build())
                .setStandardIsolation(
                    com.google.bigtable.admin.v2.AppProfile.StandardIsolation.newBuilder()
                        .setPriority(
                            com.google.bigtable.admin.v2.AppProfile.Priority.PRIORITY_MEDIUM)
                        .build())
                .setEtag("my-etag")
                .build());
    UpdateAppProfileRequest updateAppProfileRequest3 = UpdateAppProfileRequest.of(profile2);

    assertThat(updateAppProfileRequest.hashCode())
        .isNotEqualTo(updateAppProfileRequest3.hashCode());
  }

  @Test
  public void testFromProtoWithDataBoostIsolation() {
    AppProfile producer =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setSingleClusterRouting(
                    SingleClusterRouting.newBuilder()
                        .setClusterId("my-cluster")
                        .setAllowTransactionalWrites(true)
                        .build())
                .setDataBoostIsolationReadOnly(
                    com.google.bigtable.admin.v2.AppProfile.DataBoostIsolationReadOnly.newBuilder()
                        .setComputeBillingOwner(
                            com.google.bigtable.admin.v2.AppProfile.DataBoostIsolationReadOnly
                                .ComputeBillingOwner.HOST_PAYS))
                .setEtag("my-etag")
                .build());

    assertThat(producer.getInstanceId()).isEqualTo("my-instance");
    assertThat(producer.getId()).isEqualTo("my-profile");
    assertThat(producer.getDescription()).isEqualTo("my description");
    assertThat(producer.getPolicy()).isEqualTo(SingleClusterRoutingPolicy.of("my-cluster", true));
    assertThat(producer.getIsolationPolicy())
        .isEqualTo(
            AppProfile.DataBoostIsolationReadOnlyPolicy.of(
                AppProfile.ComputeBillingOwner.HOST_PAYS));

    AppProfile consumer =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setSingleClusterRouting(
                    SingleClusterRouting.newBuilder()
                        .setClusterId("my-cluster")
                        .setAllowTransactionalWrites(true)
                        .build())
                .setDataBoostIsolationReadOnly(
                    com.google.bigtable.admin.v2.AppProfile.DataBoostIsolationReadOnly.newBuilder()
                        .setComputeBillingOwner(
                            com.google.bigtable.admin.v2.AppProfile.DataBoostIsolationReadOnly
                                .ComputeBillingOwner.COMPUTE_BILLING_OWNER_UNSPECIFIED))
                .setEtag("my-etag")
                .build());

    assertThat(consumer.getInstanceId()).isEqualTo("my-instance");
    assertThat(consumer.getId()).isEqualTo("my-profile");
    assertThat(consumer.getDescription()).isEqualTo("my description");
    assertThat(consumer.getPolicy()).isEqualTo(SingleClusterRoutingPolicy.of("my-cluster", true));
    assertThat(consumer.getIsolationPolicy())
        .isEqualTo(
            AppProfile.DataBoostIsolationReadOnlyPolicy.of(
                AppProfile.ComputeBillingOwner.UNSPECIFIED));
  }

  @Test
  public void testFromProtoWithRowAffinityNoClusterGroup() {
    AppProfile profile =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setMultiClusterRoutingUseAny(
                    com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny.newBuilder()
                        .setRowAffinity(
                            com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny
                                .RowAffinity.getDefaultInstance())
                        .build())
                .setEtag("my-etag")
                .build());

    assertThat(profile.getInstanceId()).isEqualTo("my-instance");
    assertThat(profile.getId()).isEqualTo("my-profile");
    assertThat(profile.getDescription()).isEqualTo("my description");
    System.out.println(profile.getPolicy());
    System.out.println(AppProfile.MultiClusterRoutingPolicy.withRowAffinity());
    assertThat(profile.getPolicy())
        .isEqualTo(AppProfile.MultiClusterRoutingPolicy.withRowAffinity());
  }

  @Test
  public void testFromProtoWithRowAffinityClusterGroup() {
    AppProfile profile =
        AppProfile.fromProto(
            com.google.bigtable.admin.v2.AppProfile.newBuilder()
                .setName(AppProfileName.of("my-project", "my-instance", "my-profile").toString())
                .setDescription("my description")
                .setMultiClusterRoutingUseAny(
                    com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny.newBuilder()
                        .addAllClusterIds(ImmutableList.of("cluster-id-1", "cluster-id-2"))
                        .setRowAffinity(
                            com.google.bigtable.admin.v2.AppProfile.MultiClusterRoutingUseAny
                                .RowAffinity.getDefaultInstance())
                        .build())
                .setEtag("my-etag")
                .build());

    assertThat(profile.getInstanceId()).isEqualTo("my-instance");
    assertThat(profile.getId()).isEqualTo("my-profile");
    assertThat(profile.getDescription()).isEqualTo("my description");
    assertThat(profile.getPolicy())
        .isEqualTo(
            AppProfile.MultiClusterRoutingPolicy.withRowAffinity("cluster-id-1", "cluster-id-2"));
  }
}
