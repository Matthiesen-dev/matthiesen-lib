package dev.matthiesen.fabric.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.utility.ClientUtils;
import net.fabricmc.api.ClientModInitializer;

public class MatthiesenLibApiClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientUtils.registerClientLoadRunnables();
    }
}
