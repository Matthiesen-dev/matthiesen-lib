package dev.matthiesen.common.matthiesen_lib_api.core.interfaces;

import net.minecraft.server.level.ServerPlayer;

/**
 * This interface defines the contract for handling player events such as joining and leaving the server. Mods can implement this interface
 * to receive callbacks when players join or leave, allowing them to perform custom logic in response to these events. The methods
 * in this interface are called by the MatthiesenLibApiPlayerEventsManager when the corresponding player events occur.
 */
@SuppressWarnings("unused")
public interface MatthiesenLibPlayerEventHandler {
    /**
     * Called when a player joins the server. Implementations of this method can perform any necessary setup or initialization for the player,
     * @param player the ServerPlayer instance representing the player who joined. This parameter provides access to the player's information and
     *               allows for interaction with the player entity.
     */
    default void onPlayerJoin(ServerPlayer player) {}

    /**
     * Called when a player leaves the server. Implementations of this method can perform any necessary cleanup or finalization for the player,
     * @param player the ServerPlayer instance representing the player who left. This parameter provides access to the player's information and
     *               allows for interaction with the player entity, if necessary, before it is fully removed from the server context.
     */
    default void onPlayerLeave(ServerPlayer player) {}
}
