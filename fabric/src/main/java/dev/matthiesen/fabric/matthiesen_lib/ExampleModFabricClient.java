package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.ExampleModCommonClient;
import net.fabricmc.api.ClientModInitializer;

public class ExampleModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExampleModCommonClient.initialize();
    }
}
