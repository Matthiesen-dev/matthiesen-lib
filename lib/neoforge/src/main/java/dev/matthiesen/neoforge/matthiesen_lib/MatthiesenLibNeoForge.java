package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.neoforge.matthiesen_lib.helper.MatthiesenLibNeoForgeRegistryHelper;
import dev.matthiesen.neoforge.matthiesen_lib.text_parser.MatthiesenLibEmbersTextParserNeoForge;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

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
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            MatthiesenLib.registerTextParser(new MatthiesenLibEmbersTextParserNeoForge());
        }
    }

    /**
     * Retrieves the current instance of the Minecraft server. This method is thread-safe and returns null if the server is not currently running.
     * @return The current MinecraftServer instance, or null if the server is not running.
     */
    public static MinecraftServer getMinecraftServer() {
        return MC_SERVER;
    }

    /**
     * Sets the Minecraft server instance. This method is intended to be called from server lifecycle event handlers to update the MC_SERVER field when the server starts and stops.
     * @param server The MinecraftServer instance to set, or null if the server is stopping. This method is thread-safe and should be called with appropriate synchronization if accessed from multiple threads.
     */
    public static void setMinecraftServer(@Nullable MinecraftServer server) {
        MC_SERVER = server;
    }
}
