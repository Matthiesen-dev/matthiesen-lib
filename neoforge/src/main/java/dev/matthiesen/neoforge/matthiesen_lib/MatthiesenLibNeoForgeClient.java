package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Client-side initialization class for MatthiesenLib on the NeoForge platform, responsible for setting up client-specific resources and event listeners.
 */
@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class MatthiesenLibNeoForgeClient {
    public MatthiesenLibNeoForgeClient(IEventBus modBus) {
        Constants.createInfoLog("Loading Client resources for NeoForge Mod Loader");
        MatthiesenLibClient.modInitializer();
        modBus.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerScreens(RegisterMenuScreensEvent event) {
        MatthiesenLibClient.applyScreenRegistrations(event::register);
    }
}
