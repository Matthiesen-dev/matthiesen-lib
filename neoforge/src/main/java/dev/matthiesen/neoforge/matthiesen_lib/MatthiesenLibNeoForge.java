package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import dev.matthiesen.neoforge.matthiesen_lib.platform.NeoForgeClientRegistryHelper;
import dev.matthiesen.neoforge.matthiesen_lib.platform.NeoForgeRegistryHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Main class for the MatthiesenLib mod on the NeoForge platform.
 */
@Mod(Constants.MOD_ID)
public class MatthiesenLibNeoForge {
    public MatthiesenLibNeoForge(IEventBus modBus) {
        Constants.createInfoLog("Loading for NeoForge Mod Loader");
        NeoForgeRegistryHelper.init(modBus);
        NeoForgeClientRegistryHelper.init(modBus);
        MatthiesenLib.modInitializer();
        NeoForge.EVENT_BUS.register(this);
    }

    /**
     * Client-side event subscriber for NeoForge.
     */
    @EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class NeoForgeClient {

        /**
         * Handles the client setup event for NeoForge, initializing client-specific components of MatthiesenLib.
         */
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            MatthiesenLibClient.initialize();
        }
    }
}
