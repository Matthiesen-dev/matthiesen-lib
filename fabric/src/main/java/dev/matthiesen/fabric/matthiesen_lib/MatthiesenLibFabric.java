package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.fabric.matthiesen_lib.text_parser.MatthiesenLibEmbersTextParserFabric;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

/**
 * Main class for the MatthiesenLib mod on the Fabric platform.
 */
public class MatthiesenLibFabric implements ModInitializer {
    private static volatile MinecraftServer MC_SERVER;

    /**
     * Default constructor for the MatthiesenLibFabric class. No initialization is required as setup is handled in the onInitialize method.
     */
    public MatthiesenLibFabric() {}

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public static MinecraftServer getMinecraftServer() {
        return MC_SERVER;
    }

    /**
     * Initializes the MatthiesenLib mod for the Fabric platform.
     */
    @Override
    public void onInitialize() {
        MatthiesenLibConstants.createInfoLog("Loading for Fabric Mod Loader");
        MatthiesenLib.modInitializer();
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            MatthiesenLib.registerTextParser(new MatthiesenLibEmbersTextParserFabric());
        }

        ServerLifecycleEvents.SERVER_STARTING.register(server -> MC_SERVER = server);
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> MC_SERVER = null);
    }
}
