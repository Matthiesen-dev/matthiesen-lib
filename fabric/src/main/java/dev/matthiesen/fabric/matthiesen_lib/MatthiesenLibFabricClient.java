package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

/**
 * Client-side initialization class for MatthiesenLib on the Fabric platform.
 */
public class MatthiesenLibFabricClient implements ClientModInitializer {
    /**
     * Default constructor for the MatthiesenLibFabricClient class. No initialization is required as setup is handled in the onInitializeClient method.
     */
    private MatthiesenLibFabricClient() {}

    /**
     * Initializes the client-side components of MatthiesenLib for the Fabric platform.
     */
    @Override
    public void onInitializeClient() {
        MatthiesenLibClient.modInitializer();
        MatthiesenLibClient.applyScreenRegistrations(MenuScreens::register);
    }
}
