package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.Constants;
import net.fabricmc.api.ModInitializer;

/**
 * Main class for the MatthiesenLib mod on the Fabric platform.
 */
public class MatthiesenLibFabric implements ModInitializer {

    /**
     * Initializes the MatthiesenLib mod for the Fabric platform.
     */
    @Override
    public void onInitialize() {
        Constants.createInfoLog("Loading for Fabric Mod Loader");
        MatthiesenLib.modInitializer();
    }
}
