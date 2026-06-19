package dev.matthiesen.neoforge.matthiesen_lib_api.platform;

import dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibClientPlatform;

import java.util.List;

/**
 * Implementation of the MatthiesenLibClientPlatform interface for the NeoForge platform. This class is responsible for registering client load runnables to be executed during the client's initialization phase. It uses the FMLClientSetupEvent to trigger the execution of the registered runnables when the client setup event is fired. This ensures that any client-specific initialization code is executed at the appropriate time during the client's lifecycle, allowing for proper setup of client-side features and functionality on the NeoForge platform.
 */
public class MatthiesenLibNeoForgeClientPlatformService implements MatthiesenLibClientPlatform {
    /**
     * Default constructor for the MatthiesenLibNeoForgeClientPlatformService. No initialization is required as client load runnables are registered through the onClientLoad method, which utilizes the FMLClientSetupEvent to execute the runnables during the client's setup phase.
     */
    public MatthiesenLibNeoForgeClientPlatformService() {}

    @Override
    public void onClientShutdown(List<Runnable> runnables) {

    }
}
