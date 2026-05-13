package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import dev.matthiesen.neoforge.matthiesen_lib.platform.NeoForgeClientRegistryHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class MatthiesenLibNeoForgeClient {
    public MatthiesenLibNeoForgeClient(IEventBus modBus) {
        Constants.createInfoLog("Loading Client resources for NeoForge Mod Loader");
        NeoForgeClientRegistryHelper.init(modBus);
        modBus.addListener(this::clientSetup);
    }

    /**
     * Handles the client setup event for NeoForge, initializing client-specific components of MatthiesenLib.
     */
    public void clientSetup(FMLClientSetupEvent event) {
        MatthiesenLibClient.modInitializer();
    }
}
