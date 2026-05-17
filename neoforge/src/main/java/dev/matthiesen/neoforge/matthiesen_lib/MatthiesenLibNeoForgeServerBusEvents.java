package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

/**
 * MatthiesenLibNeoForgeServerBusEvents is a server-side event subscriber class for the NeoForge mod loader.
 */
@EventBusSubscriber(modid = MatthiesenLibConstants.MOD_ID, value = Dist.DEDICATED_SERVER)
public class MatthiesenLibNeoForgeServerBusEvents {
    /**
     * Default constructor for MatthiesenLibNeoForgeServerBusEvents.
     */
    public MatthiesenLibNeoForgeServerBusEvents() {}

    /**
     * Event handler for server starting events. This method listens for the ServerStartingEvent and sets the MC_SERVER field to the current
     * server instance when the server starts.
     * @param event The event object containing the context for the server starting event, including the server instance that is starting.
     *              This method is called with the highest priority to ensure that the MC_SERVER field is set as early as possible during server
     *              startup, allowing other parts of the mod to access the server instance as soon as it is available.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStarting(ServerStartingEvent event) {
        MatthiesenLibNeoForge.setMinecraftServer(event.getServer());
    }

    /**
     * Event handler for server stopping events. This method listens for the ServerStoppingEvent and sets the MC_SERVER field to null when the server stops.
     * @param event The event object containing the context for the server stopping event, including the server instance that is stopping.
     *              This method is called with the highest priority to ensure that the MC_SERVER field is cleared as early as possible during server
     *              shutdown, preventing access to the server instance after it has been stopped.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStopping(ServerStoppingEvent event) {
        MatthiesenLibNeoForge.setMinecraftServer(null);
    }
}
