package dev.matthiesen.fabric.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

import java.util.Map;

/**
 * Main class for the MatthiesenLib API on the Fabric platform. This class is responsible for initializing the API and
 * managing the Minecraft server instance.
 * It provides a thread-safe way to access the current Minecraft server instance through the getMinecraftServer method.
 * The server instance is updated in response to server lifecycle events, ensuring that the API has access to the server
 * instance when it is running and prevents access when the server is not running.
 */
public class MatthiesenLibApiFabric implements ModInitializer {
    private static volatile MinecraftServer MC_SERVER;

    /**
     * Default constructor for the MatthiesenLibApiFabric class. Initializes the API and registers necessary resources.
     * This method is called when the mod is loaded by the Fabric mod loader.
     */
    @Override
    public void onInitialize() {
        MatthiesenLibApiConstants.createInfoLog("Loading API for Fabric Mod Loader");
        MatthiesenLibApi.modInitializer();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> MC_SERVER = server);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> MC_SERVER = null);

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) {
                Map<String, Runnable> runnables = MatthiesenLibApi.getReloadRunnables();
                if (runnables.isEmpty()) return;
                for (Map.Entry<String, Runnable> entry : runnables.entrySet()) {
                    try {
                        MatthiesenLibApiConstants.createInfoLog("Executing reload runnable for mod: " + entry.getKey());
                        entry.getValue().run();
                    } catch (Exception e) {
                        MatthiesenLibApiConstants.createErrorLog("Error executing reload runnable for mod: " + entry.getKey(), e);
                    }
                }
            }
        });
    }

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public static MinecraftServer getMinecraftServer() {
        return MC_SERVER;
    }
}
