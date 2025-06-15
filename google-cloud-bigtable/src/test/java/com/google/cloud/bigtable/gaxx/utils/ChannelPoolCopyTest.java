package com.google.cloud.bigtable.gaxx.utils;

import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.grpc.ChannelPoolSettings;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelPoolSettings;
import java.lang.reflect.Modifier;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@RunWith(JUnit4.class)
public class ChannelPoolCopyTest {
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

  @Test
  public void testToBigtableChannelPoolSettingsAllFieldsSetCopiesCorrectly() throws Exception {
    ChannelPoolSettings originalSettings =
        ChannelPoolSettings.builder()
            .setMinRpcsPerChannel(10)
            .setMaxRpcsPerChannel(50)
            .setMinChannelCount(5)
            .setMaxChannelCount(100)
            .setInitialChannelCount(20)
            .setPreemptiveRefreshEnabled(true)
            .build();

    BigtableChannelPoolSettings copiedSettings = BigtableChannelPoolSettings.copyFrom(originalSettings);
    assertSettingsCopiedCorrectly(originalSettings, copiedSettings);
  }

  @Test
  public void testToBigtableChannelPoolSettingsDefaultValuesCopiesCorrectly() throws Exception {
    ChannelPoolSettings originalSettings = ChannelPoolSettings.builder().build();
    BigtableChannelPoolSettings copiedSettings = BigtableChannelPoolSettings.copyFrom(originalSettings);
    assertSettingsCopiedCorrectly(originalSettings, copiedSettings);
  }

  // @Test
  // public void testToBigtableChannelPoolSettingsNullInputReturnsNull() {
  //   ChannelPoolSettings originalSettings = null;
  //   BigtableChannelPoolSettings copiedSettings =
  //       ChannelPoolSettingsCopier.toBigtableChannelPoolSettings(originalSettings);
  //   assertThat(copiedSettings).isNull();
  // }

  private void assertSettingsCopiedCorrectly(
      ChannelPoolSettings originalSettings, BigtableChannelPoolSettings copiedSettings)
      throws Exception {

    Set<String> supportedGetters = ImmutableSet.of("getMinRpcsPerChannel", "getMaxRpcsPerChannel", "getMinChannelCount", "getMaxChannelCount", "getInitialChannelCount", "isPreemptiveRefreshEnabled", "isStaticSize");

    Set<String> actualGetters = Arrays.stream(ChannelPoolSettings.class.getDeclaredMethods())
        .filter(method -> Modifier.isPublic(method.getModifiers())
            && Modifier.isAbstract(method.getModifiers())
            && (method.getName().startsWith("get") || method.getName().startsWith("is")))
        .map(Method::getName)
        .collect(Collectors.toSet());

    // If this fails then we need to add support for the additional attributes on the gax ChannelPool
    // Relevant things to update the copier and the other tests in this file
    assertThat(supportedGetters).containsAtLeastElementsIn(actualGetters);

    assertThat(originalSettings.getInitialChannelCount()).isEqualTo(copiedSettings.getInitialChannelCount());
    assertThat(originalSettings.getMaxChannelCount()).isEqualTo(copiedSettings.getMaxChannelCount());
    assertThat(originalSettings.getMinChannelCount()).isEqualTo(copiedSettings.getMinChannelCount());
    assertThat(originalSettings.getMaxRpcsPerChannel()).isEqualTo(copiedSettings.getMaxRpcsPerChannel());
    assertThat(originalSettings.getMinRpcsPerChannel()).isEqualTo(copiedSettings.getMinRpcsPerChannel());
    assertThat(originalSettings.getInitialChannelCount()).isEqualTo(copiedSettings.getInitialChannelCount());
    assertThat(originalSettings.isPreemptiveRefreshEnabled()).isEqualTo(copiedSettings.isPreemptiveRefreshEnabled());
  }
}