package dev.matthiesen.neoforge.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import dev.matthiesen.neoforge.matthiesen_lib_api.helper.MatthiesenLibNeoForgeRegistryHelper;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

/**
 * Main class for the MatthiesenLib API on the NeoForge platform. This class is responsible for initializing the API and
 * managing the Minecraft server instance.
 * It provides a thread-safe way to access the current Minecraft server instance through the getMinecraftServer method,
 * and allows for setting the server instance through the setMinecraftServer method, which is intended to be called from
 * server lifecycle event handlers. The constructor initializes the API and registers necessary resources on the provided event bus.
 */
@Mod(MatthiesenLibApiConstants.MOD_ID)
public class MatthiesenLibApiNeoForge {
    private static volatile MinecraftServer MC_SERVER;

    /**
     * Default constructor for the MatthiesenLibApiNeoForge class. Initializes the API and registers necessary resources on the provided event bus.
     * @param modBus The event bus to register mod events on. This constructor is used for NeoForge's event-driven initialization
     *               process and ensures that the API is properly set up when the mod is loaded.
     */
    public MatthiesenLibApiNeoForge(IEventBus modBus) {
        MatthiesenLibApiConstants.createInfoLog("Loading API for NeoForge Mod Loader");
        MatthiesenLibNeoForgeRegistryHelper.init(modBus);
        MatthiesenLibApi.modInitializer();
    }

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server
     * is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public static MinecraftServer getMinecraftServer() {
        return MC_SERVER;
    }

    /**
     * Sets the Minecraft server instance. This method is intended to be called from server lifecycle event handlers to
     * update the MC_SERVER field when the server starts and stops.
     * @param server The MinecraftServer instance to set, or null if the server is stopping. This method is thread-safe
     *               and should be called with appropriate synchronization if accessed from multiple threads.
     */
    public static void setMinecraftServer(@Nullable MinecraftServer server) {
        MC_SERVER = server;
    }
}
