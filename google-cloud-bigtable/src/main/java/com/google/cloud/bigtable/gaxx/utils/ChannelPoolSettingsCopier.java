package com.google.cloud.bigtable.gaxx.utils;

import com.google.api.gax.grpc.ChannelPoolSettings;
import com.google.cloud.bigtable.gaxx.grpc.BigtableChannelPoolSettings;

/**
 * Utility class to convert ChannelPoolSettings to BigtableChannelPoolSettings.
 */
public class ChannelPoolSettingsCopier {
  public static BigtableChannelPoolSettings toBigtableChannelPoolSettings(ChannelPoolSettings original) {
    if (original == null) {
      return null;
    }

    return BigtableChannelPoolSettings.builder()
        .setMinRpcsPerChannel(original.getMinRpcsPerChannel())
        .setMaxRpcsPerChannel(original.getMaxRpcsPerChannel())
        .setMinChannelCount(original.getMinChannelCount())
        .setMaxChannelCount(original.getMaxChannelCount())
        .setInitialChannelCount(original.getInitialChannelCount())
        .setPreemptiveRefreshEnabled(original.isPreemptiveRefreshEnabled())
        .build();
  }
}
