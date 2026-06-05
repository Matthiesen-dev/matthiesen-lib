package dev.matthiesen.common.matthiesen_lib_api.utility;

import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibClientPlatform;

import java.util.List;
import java.util.ServiceLoader;

public class ClientUtils {
    private static final MatthiesenLibClientPlatform PLATFORM =
            ServiceLoader.load(MatthiesenLibClientPlatform.class).findFirst().orElseThrow();

    private static final List<Runnable> CLIENT_LOAD_RUNNABLES = new java.util.ArrayList<>();

    public static void appendRunnable(Runnable runnable) {
        synchronized (CLIENT_LOAD_RUNNABLES) {
            CLIENT_LOAD_RUNNABLES.add(runnable);
        }
    }

    public static void registerClientLoadRunnables() {
        PLATFORM.onClientLoad(CLIENT_LOAD_RUNNABLES);
    }
}
