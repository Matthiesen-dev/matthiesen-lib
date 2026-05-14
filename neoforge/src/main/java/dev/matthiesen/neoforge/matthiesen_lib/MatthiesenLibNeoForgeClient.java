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

    /**
     * Default constructor for the MatthiesenLibNeoForgeClient class. Initializes client-side resources and registers event listeners for screen registration.
     * @param modBus The event bus to register mod events on. This constructor is used for NeoForge's event-driven initialization process, allowing the mod to listen for client-specific events such as screen registration.
     */
    public MatthiesenLibNeoForgeClient(IEventBus modBus) {
        Constants.createInfoLog("Loading Client resources for NeoForge Mod Loader");
        MatthiesenLibClient.modInitializer();
        modBus.register(this);
    }

    /**
     * Event handler for registering custom menu screens. This method listens for the RegisterMenuScreensEvent and applies any screen registrations defined in MatthiesenLibClient.
     * @param event The event object containing the context for menu screen registration. This method is called with the lowest priority to ensure that
     *              it runs after all other screen registrations have been processed, allowing MatthiesenLibClient to add its screens without interfering with other mods' registrations.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerScreens(RegisterMenuScreensEvent event) {
        MatthiesenLibClient.applyScreenRegistrations(event::register);
    }
}
