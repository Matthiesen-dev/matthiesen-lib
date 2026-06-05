package dev.matthiesen.fabric.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiClientUtils;
import net.fabricmc.api.ClientModInitializer;

/**
 * Main class for the MatthiesenLibApi Fabric client. This class is responsible for initializing the client-side components of the API.
 * It registers client load runnables to ensure that any client-specific initialization code is executed when the client starts. This allows for proper setup of client-side features and ensures that the API functions correctly on the Fabric platform. The use of ClientUtils allows for a clean separation of client-specific code from common code, preventing class loading issues on the server side while still enabling client-side functionality.
 */
public class MatthiesenLibApiClientFabric implements ClientModInitializer {
    /**
     * Default constructor for the MatthiesenLibApiClientFabric class. This constructor is required by the Fabric mod loader to instantiate the mod class when loading the mod.
     */
    public MatthiesenLibApiClientFabric() {}

    /**
     * Initializes the client-side components of the MatthiesenLib API. This method is called by the Fabric mod loader during the client's initialization phase. It registers any client load runnables that have been appended to the ClientUtils, ensuring that all necessary client-specific initialization code is executed when the client starts.
     */
    @Override
    public void onInitializeClient() {
        MatthiesenLibApiClientUtils.registerClientLoadRunnables();
    }
}
