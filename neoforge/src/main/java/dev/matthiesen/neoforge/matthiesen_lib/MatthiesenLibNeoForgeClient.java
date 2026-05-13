package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import dev.matthiesen.neoforge.matthiesen_lib.platform.NeoForgeClientRegistryHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class MatthiesenLibNeoForgeClient {
    public MatthiesenLibNeoForgeClient(IEventBus modBus) {
        Constants.createInfoLog("Loading Client resources for NeoForge Mod Loader");
        NeoForgeClientRegistryHelper.init(modBus);
        NeoForge.EVENT_BUS.register(this);
    }

    /**
     * Handles the client setup event for NeoForge, initializing client-specific components of MatthiesenLib.
     */
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        MatthiesenLibClient.modInitializer();
    }
}
