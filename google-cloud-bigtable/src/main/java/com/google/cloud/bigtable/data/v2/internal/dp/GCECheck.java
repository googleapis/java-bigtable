package com.google.cloud.bigtable.data.v2.internal.dp;

import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

class GCECheck {
    private static final String GCE_PRODUCTION_NAME_PRIOR_2016 = "Google";
    private static final String GCE_PRODUCTION_NAME_AFTER_2016 = "Google Compute Engine";

    @VisibleForTesting
    static String systemProductName = null;

    static boolean isRunningOnGCP() {
        String osName = System.getProperty("os.name");
        if ("Linux".equals(osName)) {
            String productName = getSystemProductName();
            return productName.contains(GCE_PRODUCTION_NAME_PRIOR_2016)
                    || productName.contains(GCE_PRODUCTION_NAME_AFTER_2016);
        }
        return false;
    }

    private static String getSystemProductName() {
        if (systemProductName != null) {
            return systemProductName;
        }
        try {
            return new String(
                    Files.readAllBytes(Paths.get("/sys/class/dmi/id/product_name")),
                    StandardCharsets.UTF_8).trim();
        } catch (IOException e) {
            return "";
        }
    }
}