package dev.matthiesen.common.matthiesen_lib_api.core;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibPlayerEventHandler;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages player events such as joining and leaving the server. It allows mods to register their own event handlers for player events,
 * and ensures that these handlers are called when the corresponding events occur. The manager maintains a map of registered player event handlers,
 * allowing for organized management and invocation of these handlers in response to player events.
 */
public final class MatthiesenLibApiPlayerEventsManager {
    private static final Map<String, MatthiesenLibPlayerEventHandler> playerEventHandlers = new HashMap<>();
    private static boolean initialized;

    /**
     * Default constructor for the MatthiesenLibApiPlayerEventsManager class. This constructor is private to prevent instantiation of the class,
     */
    private MatthiesenLibApiPlayerEventsManager() {}

    /**
     * Default constructor for the MatthiesenLibApiPlayerEventsManager class. No initialization is required as setup is handled in the modInitializer method.
     */
    public static void modInitializer() {
        if (initialized) return;
        initialized = true;
        MatthiesenLibApiConstants.createInfoLog("Initialized permission registry");
    }

    /**
     * Registers a player event handler for a specific mod. This method allows mods to register their own implementations of the IPlayerEventHandler interface,
     * @param modId the unique identifier of the mod registering the event handler. This parameter is used to associate the handler with a specific mod, allowing for organized management of handlers and potential debugging or logging purposes.
     * @param handler the implementation of the MatthiesenLibPlayerEventHandler interface that will handle player events for the specified mod. This parameter allows mods to define their own logic for handling player join and leave events, enabling custom behavior in response to these events.
     */
    public static void registerPlayerEventHandler(String modId, MatthiesenLibPlayerEventHandler handler) {
        playerEventHandlers.put(modId, handler);
    }

    /**
     * Called when a player joins the server. This method iterates through all registered player event handlers and invokes their onPlayerJoin method,
     * allowing each handler to perform any necessary setup or initialization for the player who joined.
     * @param player the ServerPlayer instance representing the player who joined. This parameter provides access to the player's information and allows for interaction with the player entity.
     */
    public static void onPlayerJoin(ServerPlayer player) {
        for (MatthiesenLibPlayerEventHandler handler : playerEventHandlers.values()) {
            try {
                handler.onPlayerJoin(player);
            } catch (RuntimeException e) {
                MatthiesenLibApi.ERROR_TRACKER.trackError(e);
                MatthiesenLibApiConstants.getLogger().error("Error handling player join event for player {} in mod {}", player.getName().getString(), handler.getClass().getName(), e);
            }
        }
    }

    /**
     * Called when a player leaves the server. This method iterates through all registered player event handlers and invokes their onPlayerLeave method,
     * allowing each handler to perform any necessary cleanup or finalization for the player who left.
     * @param player the ServerPlayer instance representing the player who left. This parameter provides access to the player's information and allows
     *               for interaction with the player entity, if necessary, before it is fully removed from the server context.
     */
    public static void onPlayerLeave(ServerPlayer player) {
        for (MatthiesenLibPlayerEventHandler handler : playerEventHandlers.values()) {
            try {
                handler.onPlayerLeave(player);
            } catch (RuntimeException e) {
                MatthiesenLibApi.ERROR_TRACKER.trackError(e);
                MatthiesenLibApiConstants.getLogger().error("Error handling player leave event for player {} in mod {}", player.getName().getString(), handler.getClass().getName(), e);
            }
        }
    }

    /**
     * This interface defines the contract for handling player events such as joining and leaving the server. Mods can implement this interface
     * to receive callbacks when players join or leave, allowing them to perform custom logic in response to these events. The methods
     * in this interface are called by the MatthiesenLibApiPlayerEventsManager when the corresponding player events occur.
     *
     * @deprecated This interface is deprecated and will be removed in a future version. Mods should implement the MatthiesenLibPlayerEventHandler interface directly instead of using this deprecated interface.
     */
    @SuppressWarnings("unused")
    @Deprecated(forRemoval = true)
    public interface IPlayerEventHandler extends MatthiesenLibPlayerEventHandler {}
}
