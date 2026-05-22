package dev.matthiesen.fabric.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import net.fabricmc.api.ModInitializer;

public class MatthiesenLibApiFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        MatthiesenLibApiConstants.createInfoLog("Loading API for Fabric Mod Loader");
    }
}
