package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.neoforge.matthiesen_lib.helper.MatthiesenLibNeoForgeRegistryHelper;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

/**
 * Main class for the MatthiesenLib mod on the NeoForge platform.
 */
@Mod(MatthiesenLibConstants.MOD_ID)
public class MatthiesenLibNeoForge {
    private static volatile MinecraftServer MC_SERVER;

    /**
     * Default constructor for the MatthiesenLibNeoForge class. No initialization is required as setup is handled in the constructor that
     * takes an IEventBus parameter.
     *
     * @param modBus The event bus to register mod events on. This constructor is used for NeoForge's event-driven initialization process.
     */
    public MatthiesenLibNeoForge(IEventBus modBus) {
        MatthiesenLibConstants.createInfoLog("Loading for NeoForge Mod Loader");
        MatthiesenLibNeoForgeRegistryHelper.init(modBus);
        MatthiesenLib.modInitializer();
        NeoForge.EVENT_BUS.register(this);
    }

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public static MinecraftServer getMinecraftServer() {
        return MC_SERVER;
    }

    /**
     * Event handler for server starting events. This method listens for the ServerStartingEvent and sets the MC_SERVER field to the current
     * server instance when the server starts.
     * @param event The event object containing the context for the server starting event, including the server instance that is starting.
     *              This method is called with the highest priority to ensure that the MC_SERVER field is set as early as possible during server
     *              startup, allowing other parts of the mod to access the server instance as soon as it is available.
     */
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        MC_SERVER = event.getServer();
    }

    /**
     * Event handler for server stopping events. This method listens for the ServerStoppingEvent and sets the MC_SERVER field to null when the server stops.
     * @param event The event object containing the context for the server stopping event, including the server instance that is stopping.
     *              This method is called with the highest priority to ensure that the MC_SERVER field is cleared as early as possible during server
     *              shutdown, preventing access to the server instance after it has been stopped.
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onServerStopping(ServerStoppingEvent event) {
        MC_SERVER = null;
    }
}
