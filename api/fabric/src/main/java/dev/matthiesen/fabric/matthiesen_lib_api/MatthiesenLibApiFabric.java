package dev.matthiesen.fabric.matthiesen_lib_api;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class MatthiesenLibApiFabric implements ModInitializer {
    private static volatile MinecraftServer MC_SERVER;

    @Override
    public void onInitialize() {
        MatthiesenLibApiConstants.createInfoLog("Loading API for Fabric Mod Loader");
        MatthiesenLibApi.modInitializer();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> MC_SERVER = server);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> MC_SERVER = null);
    }

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public static MinecraftServer getMinecraftServer() {
        return MC_SERVER;
    }
}
