package dev.matthiesen.common.matthiesen_lib_api.utility;

import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibClientPlatform;

import java.util.ServiceLoader;

public class ClientUtils {
    private static final MatthiesenLibClientPlatform PLATFORM =
            ServiceLoader.load(MatthiesenLibClientPlatform.class).findFirst().orElseThrow();

    public static void onClientLoad(Runnable runnable) {
        PLATFORM.onClientLoad(runnable);
    }
}
