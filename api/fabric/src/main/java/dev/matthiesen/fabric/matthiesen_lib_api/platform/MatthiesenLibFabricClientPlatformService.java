package dev.matthiesen.fabric.matthiesen_lib_api.platform;

import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibClientPlatform;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class MatthiesenLibFabricClientPlatformService implements MatthiesenLibClientPlatform {
    @Override
    public void onClientLoad(Runnable runnable) {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> runnable.run());
    }
}
