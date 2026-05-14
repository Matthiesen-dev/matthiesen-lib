package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import net.fabricmc.api.ModInitializer;

/**
 * Main class for the MatthiesenLib mod on the Fabric platform.
 */
public class MatthiesenLibFabric implements ModInitializer {
    /**
     * Default constructor for the MatthiesenLibFabric class. No initialization is required as setup is handled in the onInitialize method.
     */
    private MatthiesenLibFabric() {}

    /**
     * Initializes the MatthiesenLib mod for the Fabric platform.
     */
    @Override
    public void onInitialize() {
        MatthiesenLibConstants.createInfoLog("Loading for Fabric Mod Loader");
        MatthiesenLib.modInitializer();
    }
}
