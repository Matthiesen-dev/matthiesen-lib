package dev.matthiesen.fabric.matthiesen_lib_api.platform;

import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibClientPlatform;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.util.List;

/**
 * Implementation of the MatthiesenLibClientPlatform interface for the Fabric platform. This class is responsible for registering client load runnables to be executed during the client's initialization phase. It uses the ClientLifecycleEvents.CLIENT_STARTED event to trigger the execution of the registered runnables when the client starts. This ensures that any client-specific initialization code is executed at the appropriate time during the client's lifecycle, allowing for proper setup of client-side features and functionality on the Fabric platform.
 */
public class MatthiesenLibFabricClientPlatformService implements MatthiesenLibClientPlatform {
    @Override
    public void onClientLoad(List<Runnable> runnables) {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> runnables.forEach(Runnable::run));
    }
}
