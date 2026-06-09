package dev.matthiesen.neoforge.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiMetricsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiPlayerEventsManager;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiServerEventsManager;
import dev.matthiesen.neoforge.matthiesen_lib_api.helper.MatthiesenLibReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * MatthiesenLibNeoForgeServerBusEvents is a server-side event subscriber class for the NeoForge mod loader.
 */
@EventBusSubscriber(modid = MatthiesenLibApiConstants.MOD_ID, value = Dist.DEDICATED_SERVER)
public class MatthiesenLibApiNeoForgeServerBusEvents {
    /**
     * Default constructor for MatthiesenLibNeoForgeServerBusEvents.
     */
    public MatthiesenLibApiNeoForgeServerBusEvents() {}

    /**
     * Event handler for server starting events. This method listens for the ServerStartingEvent and sets the MC_SERVER field to the current
     * server instance when the server starts.
     * @param event The event object containing the context for the server starting event, including the server instance that is starting.
     *              This method is called with the highest priority to ensure that the MC_SERVER field is set as early as possible during server
     *              startup, allowing other parts of the mod to access the server instance as soon as it is available.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStarting(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        MatthiesenLibApiNeoForge.setMinecraftServer(server);
        MatthiesenLibApiServerEventsManager.onServerStart(server);
    }

    /**
     * Event handler for server stopping events. This method listens for the ServerStoppingEvent and sets the MC_SERVER field to null when the server stops.
     * @param event The event object containing the context for the server stopping event, including the server instance that is stopping.
     *              This method is called with the highest priority to ensure that the MC_SERVER field is cleared as early as possible during server
     *              shutdown, preventing access to the server instance after it has been stopped.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStopping(ServerStoppingEvent event) {
        MatthiesenLibApiServerEventsManager.onServerStop(event.getServer());
        MatthiesenLibApiNeoForge.setMinecraftServer(null);
    }

    /**
     * Event handler for server tick events. This method listens for the ServerTickEvent and calls the onServerTick method of
     * the MatthiesenLibApiServerEventsManager on each server tick.
     * @param event The event object containing the context for the server tick event, including the server instance that is
     *              ticking. This method is called with the highest priority to ensure that the onServerTick method is called as
     *              early as possible during each server tick, allowing mods to perform any necessary logic or updates in response
     *              to the tick event.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerTick(ServerTickEvent.Post event) {
        MatthiesenLibApiServerEventsManager.onServerTick(event.getServer());
    }

    /**
     * Event handler for adding reload listeners. This method listens for the AddReloadListenerEvent and is intended to be used for adding resource reload listeners to the server's resource manager.
     * @param event The event object containing the context for the add reload listener event, including the resource manager to which reload listeners can be added.
     */
    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new MatthiesenLibReloadListener(MatthiesenLibApi::getReloadRunnables));
    }

    /**
     * Event handler for player join events. This method listens for the PlayerLoggedInEvent and calls the onPlayerJoin method of the MatthiesenLibApiPlayerEventsManager when a player joins the server.
     * @param event The event object containing the context for the player join event, including the player entity that joined the server. This method checks if the event is occurring on the server side and if the player entity is an instance of ServerPlayer before calling the onPlayerJoin method, ensuring that only valid player join events are processed.
     */
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            if (event.getEntity().level().isClientSide) return;
            ServerPlayer player = event.getEntity() instanceof ServerPlayer ? (ServerPlayer) event.getEntity() : null;
            if (player == null) return;
            MatthiesenLibApiPlayerEventsManager.onPlayerJoin(player);
        } catch (RuntimeException e) {
            MatthiesenLibApiMetricsManager.ERROR_TRACKER.trackError(e);
            MatthiesenLibApiConstants.getLogger().error("Error handling player join event for player {}", event.getEntity().getName().getString(), e);
        }
    }

    /**
     * Event handler for player leave events. This method listens for the PlayerLoggedOutEvent and calls the onPlayerLeave method of the MatthiesenLibApiPlayerEventsManager when a player leaves the server.
     * @param event The event object containing the context for the player leave event, including the player entity that left the server. This method checks if the event is occurring on the server side and if the player entity is an instance of ServerPlayer before calling the onPlayerLeave method, ensuring that only valid player leave events are processed.
     */
    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        try {
            if (event.getEntity().level().isClientSide) return;
            ServerPlayer player = event.getEntity() instanceof ServerPlayer ? (ServerPlayer) event.getEntity() : null;
            if (player == null) return;
            MatthiesenLibApiPlayerEventsManager.onPlayerLeave(player);
        } catch (RuntimeException e) {
            MatthiesenLibApiMetricsManager.ERROR_TRACKER.trackError(e);
            MatthiesenLibApiConstants.getLogger().error("Error handling player leave event for player {}", event.getEntity().getName().getString(), e);
        }
    }
}
