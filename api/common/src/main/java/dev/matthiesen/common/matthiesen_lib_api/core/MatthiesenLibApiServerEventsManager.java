package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibServerEventHandler;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;

/**
 * The MatthiesenLibApiServerEventsManager class is responsible for managing server event handlers registered by mods. It provides
 * methods for mods to register their own implementations of the IServerEventHandler interface, allowing them to receive callbacks
 * for server events such as starting, ticking, and stopping. The manager maintains a map of registered event handlers associated
 * with their respective mod IDs, enabling organized management and invocation of these handlers during the server lifecycle events.
 */
public final class MatthiesenLibApiServerEventsManager {
    private static final Map<String, MatthiesenLibServerEventHandler> eventHandlers = new HashMap<>();
    private static boolean initialized;

    /**
     * Private constructor to prevent instantiation of the MatthiesenLibApiServerEventsManager class.
     */
    private MatthiesenLibApiServerEventsManager() {}

    /**
     * Initializes the server events manager. This method should be called during the mod initialization phase to set up the necessary
     * infrastructure for managing server event handlers. It ensures that the manager is only initialized once, preventing redundant setup
     * and potential issues with multiple initializations. Once initialized, the manager is ready to accept registrations of server event
     * handlers from mods, allowing them to receive callbacks for server events such as starting, ticking, and stopping.
     */
    public static void modInitializer() {
        if (initialized) return;
        initialized = true;
        MatthiesenLibApiConstants.createInfoLog("Initialized server events manager");
    }

    /**
     * Registers a server event handler for a specific mod. This method allows mods to register their own implementations of the IServerEventHandler interface,
     * enabling them to receive callbacks for server events such as starting, ticking, and stopping. By registering a server event handler, mods can define custom logic to be executed in response to these events, allowing for enhanced functionality and integration with the server lifecycle.
     * @param modId the unique identifier of the mod registering the event handler. This parameter is used to associate the handler with a specific mod, allowing for organized management of handlers and potential debugging or logging purposes.
     * @param handler the implementation of the MatthiesenLibServerEventHandler interface that will handle server events for the specified mod. This parameter allows mods to define their own logic for handling server start, tick, and stop events, enabling custom behavior in response to these events.
     */
    public static void registerServerEventHandler(String modId, MatthiesenLibServerEventHandler handler) {
        eventHandlers.put(modId, handler);
    }

    /**
     * Called when the server has started. This method iterates through all registered server event handlers and invokes their onServerStart method,
     * allowing each handler to perform any necessary setup or initialization after the server has fully started. This can include tasks such as initializing mod-specific data, registering commands, or setting up scheduled tasks that need to run while the server is active.
     * @param server the MinecraftServer instance representing the server that has started. This parameter provides access to the server's information and allows for interaction with the server context, if necessary, after it has fully started.
     */
    public static void onServerStart(MinecraftServer server) {
        for (MatthiesenLibServerEventHandler handler : eventHandlers.values()) {
            try {
                handler.onServerStart(server);
            } catch (RuntimeException e) {
                MatthiesenLibApiConstants.getLogger().error("Error handling server started event in mod {}", handler.getClass().getName(), e);
            }
        }
    }

    /**
     * Called on every server tick. This method iterates through all registered server event handlers and invokes their onServerTick method,
     * allowing each handler to perform any necessary logic that needs to be executed on each tick of the server, such as updating game state, processing scheduled tasks, or performing periodic checks.
     * @param server the MinecraftServer instance representing the server that is ticking. This parameter provides access to the server's information and allows for interaction with the server context, if necessary, during the tick event.
     */
    public static void onServerTick(MinecraftServer server) {
        for (MatthiesenLibServerEventHandler handler : eventHandlers.values()) {
            try {
                handler.onServerTick(server);
            } catch (RuntimeException e) {
                MatthiesenLibApiConstants.getLogger().error("Error handling server tick event in mod {}", handler.getClass().getName(), e);
            }
        }
    }

    /**
     * Called when the server is stopping. This method iterates through all registered server event handlers and invokes their onServerStop method,
     * allowing each handler to perform any necessary cleanup or finalization before the server fully shuts down.
     * @param server the MinecraftServer instance representing the server that is stopping. This parameter provides access to the server's information and allows for interaction with the server context, if necessary, before it is fully shut down.
     */
    public static void onServerStop(MinecraftServer server) {
        for (MatthiesenLibServerEventHandler handler : eventHandlers.values()) {
            try {
                handler.onServerStop(server);
            } catch (RuntimeException e) {
                MatthiesenLibApiConstants.getLogger().error("Error handling server stopping event in mod {}", handler.getClass().getName(), e);
            }
        }
    }
}
