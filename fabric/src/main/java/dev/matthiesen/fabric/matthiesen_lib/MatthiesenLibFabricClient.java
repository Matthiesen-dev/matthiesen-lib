package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import net.fabricmc.api.ClientModInitializer;

/**
 * Client-side initialization class for MatthiesenLib on the Fabric platform.
 */
public class MatthiesenLibFabricClient implements ClientModInitializer {

    /**
     * Initializes the client-side components of MatthiesenLib for the Fabric platform.
     */
    @Override
    public void onInitializeClient() {
        MatthiesenLibClient.initialize();
    }
}
