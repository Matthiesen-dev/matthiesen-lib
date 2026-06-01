package dev.matthiesen.common.matthiesen_lib_api.core.interfaces;

import net.minecraft.server.MinecraftServer;

/**
 * This interface defines the contract for handling server events such as starting, ticking, and stopping. Mods can implement this interface
 * to receive callbacks when these server events occur, allowing them to perform custom logic in response to these events. The methods in this
 * interface are called by the MatthiesenLibApiServerEventsManager when the corresponding server events occur.
 */
@SuppressWarnings("unused")
public interface MatthiesenLibServerEventHandler {

    /**
     * Called when the server has started. Implementations of this method can perform any necessary setup or initialization after the server has fully started,
     * @param server the MinecraftServer instance representing the server that has started. This parameter provides access to the server's information and allows for interaction with the server context, if necessary, after it has fully started.
     */
    default void onServerStart(MinecraftServer server) {}

    /**
     * Called on every server tick. Implementations of this method can perform any necessary logic that needs to be executed on each tick of the server,
     * such as updating game state, processing scheduled tasks, or performing periodic checks.
     * @param server the MinecraftServer instance representing the server that is ticking. This parameter provides access to the server's information and allows for interaction with the server context, if necessary, during the tick event.
     */
    default void onServerTick(MinecraftServer server) {}

    /**
     * Called when the server is stopping. Implementations of this method can perform any necessary cleanup or finalization before the server fully shuts down.
     * @param server the MinecraftServer instance representing the server that is stopping. This parameter provides access to the server's information and allows for interaction with the server context, if necessary, before it is fully shut down.
     */
    default void onServerStop(MinecraftServer server) {}
}
