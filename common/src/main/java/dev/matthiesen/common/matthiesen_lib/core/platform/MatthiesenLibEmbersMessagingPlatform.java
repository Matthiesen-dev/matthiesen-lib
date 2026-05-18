package dev.matthiesen.common.matthiesen_lib.core.platform;

import net.minecraft.server.level.ServerPlayer;

/**
 * Interface for the messaging platform used to send messages to players in the Embers mod.
 * This interface is implemented by the platform-specific services in the respective modules.
 */
public interface MatthiesenLibEmbersMessagingPlatform {
    /**
     * Sends a message to a player.
     * @param player The player to send the message to.
     * @param message The message to send, represented as an Object for compatibility with the Embers mod's message format.
     */
    void sendMessage(ServerPlayer player, Object message);

    /**
     * Updates an existing message for a player.
     * @param player The player to update the message for.
     * @param messageId The ID of the message to update.
     * @param message The new message content, represented as an Object for compatibility with the Embers mod's message format.
     */
    void sendUpdateMessage(ServerPlayer player, String messageId, Object message);

    /**
     * Closes a message for a player.
     * @param player The player to close the message for.
     * @param messageId The ID of the message to close.
     */
    void sendCloseMessage(ServerPlayer player, String messageId);

    /**
     * Closes all messages for a player.
     * @param player The player to close all messages for.
     */
    void sendCloseAllMessages(ServerPlayer player);
}
