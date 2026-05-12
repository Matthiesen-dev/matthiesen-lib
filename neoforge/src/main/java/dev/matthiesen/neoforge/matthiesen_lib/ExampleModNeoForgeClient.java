package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.ExampleModCommonClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class ExampleModNeoForgeClient {
    public ExampleModNeoForgeClient(IEventBus modBus) {
        Constants.createInfoLog("Loading client-side for NeoForge Mod Loader");
        modBus.addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        ExampleModCommonClient.initialize();
    }
}
