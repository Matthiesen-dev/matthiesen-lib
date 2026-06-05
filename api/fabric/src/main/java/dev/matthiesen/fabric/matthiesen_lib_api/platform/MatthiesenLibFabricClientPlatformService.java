package dev.matthiesen.fabric.matthiesen_lib_api.platform;

import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibClientPlatform;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.util.List;

public class MatthiesenLibFabricClientPlatformService implements MatthiesenLibClientPlatform {
    @Override
    public void onClientLoad(List<Runnable> runnables) {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> runnables.forEach(Runnable::run));
    }
}
