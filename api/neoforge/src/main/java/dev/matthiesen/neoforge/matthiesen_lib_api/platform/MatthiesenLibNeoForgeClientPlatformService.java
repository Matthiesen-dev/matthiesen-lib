package dev.matthiesen.neoforge.matthiesen_lib_api.platform;

import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibClientPlatform;
import dev.matthiesen.neoforge.matthiesen_lib_api.MatthiesenLibApiNeoForgeClient;

public class MatthiesenLibNeoForgeClientPlatformService implements MatthiesenLibClientPlatform {
    @Override
    public void onClientLoad(Runnable runnable) {
        MatthiesenLibApiNeoForgeClient.setStartingRunnable(runnable);
    }
}
