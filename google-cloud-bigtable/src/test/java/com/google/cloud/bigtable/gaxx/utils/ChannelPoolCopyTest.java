package com.google.cloud.bigtable.gaxx.utils;

import static com.google.common.truth.Truth.assertThat;

import com.google.api.gax.grpc.ChannelPoolSettings;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelPoolSettings;
import java.lang.reflect.Modifier;
import java.util.List;
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

    BigtableChannelPoolSettings copiedSettings =
        ChannelPoolSettingsCopier.toBigtableChannelPoolSettings(originalSettings);
    assertSettingsCopiedCorrectly(originalSettings, copiedSettings);
  }

  @Test
  public void testToBigtableChannelPoolSettingsDefaultValuesCopiesCorrectly() throws Exception {
    // 1. Arrange
    ChannelPoolSettings originalSettings = ChannelPoolSettings.builder().build();

    // 2. Act
    BigtableChannelPoolSettings copiedSettings =
        ChannelPoolSettingsCopier.toBigtableChannelPoolSettings(originalSettings);

    // 3. Assert
    assertSettingsCopiedCorrectly(originalSettings, copiedSettings);
  }

  @Test
  public void testToBigtableChannelPoolSettingsNullInputReturnsNull() {
    ChannelPoolSettings originalSettings = null;
    BigtableChannelPoolSettings copiedSettings =
        ChannelPoolSettingsCopier.toBigtableChannelPoolSettings(originalSettings);
    assertThat(copiedSettings).isNull();
  }

  private void assertSettingsCopiedCorrectly(
      ChannelPoolSettings originalSettings, BigtableChannelPoolSettings copiedSettings)
      throws Exception {
    Class<ChannelPoolSettings> originalSettingsClass = ChannelPoolSettings.class;

    // Get all public abstract methods from ChannelPoolSettings that start with "get" or "is"
    // and exclude methods from Object.class
    List<Method> originalPoolGetters = Arrays.stream(ChannelPoolSettings.class.getMethods())
        .filter(method -> Modifier.isPublic(method.getModifiers())
            && Modifier.isAbstract(method.getModifiers())
            && (method.getName().startsWith("get") || method.getName().startsWith("is"))
            && method.getDeclaringClass() != Object.class) // Filter out java.lang.Object.getClass()
        .collect(Collectors.toList());

    for (Method getterMethod : originalPoolGetters) {
      try {
        Object originalValue = getterMethod.invoke(originalSettings);
        Method correspondingMethod = BigtableChannelPoolSettings.class.getMethod(getterMethod.getName());
        Object copiedValue = correspondingMethod.invoke(copiedSettings);

        assertThat(copiedValue).isEqualTo(originalValue);
      } catch (ReflectiveOperationException e) {
        throw new AssertionError(
            String.format("Reflection error accessing method '%s': %s", getterMethod.getName(), e.getMessage()), e);
      } catch (Exception e) { // Catch any other unexpected exceptions
        throw new Exception(
            String.format("Unexpected error during comparison for method '%s': %s", getterMethod.getName(), e.getMessage()), e);
      }
    }
  }
}