package dev.matthiesen.common.matthiesen_lib.core.text_parser;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEmbersTextParserCompat;
import dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibEmbersMessagingPlatform;
import net.minecraft.server.level.ServerPlayer;

import java.util.ServiceLoader;

/**
 * Class for compatibility with the Embers mod's text parser.
 */
@SuppressWarnings("unused")
public final class MatthiesenLibEmbersCompat implements MatthiesenLibEmbersTextParserCompat {
    private static final MatthiesenLibEmbersMessagingPlatform MESSAGING_PLATFORM =
            ServiceLoader.load(MatthiesenLibEmbersMessagingPlatform.class).findFirst().orElseThrow();

    private static final MatthiesenLibEmbersCompat INSTANCE = new MatthiesenLibEmbersCompat();

    /**
     * Private constructor to prevent instantiation. Use getInstance() to access the singleton instance.
     */
    private MatthiesenLibEmbersCompat() {}

    /**
     * Gets the singleton instance of MatthiesenLibEmbersCompat.
     * @return The singleton instance of MatthiesenLibEmbersCompat.
     */
    public static MatthiesenLibEmbersCompat getInstance() {
        return INSTANCE;
    }

    /**
     * Sends a message to the specified player using the Embers messaging platform.
     * @param player The player to send the message to.
     * @param message The message to send, represented as an Object for compatibility with the Embers mod's message format.
     */
    @Override
    public void sendMessage(ServerPlayer player, Object message) {
        MESSAGING_PLATFORM.sendMessage(player, message);
    }

    /**
     * Updates an existing message for the specified player using the Embers messaging platform.
     * @param player The player to update the message for.
     * @param messageId The ID of the message to update.
     * @param message The new message content, represented as an Object for compatibility with the Embers mod's message format.
     */
    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, Object message) {
        MESSAGING_PLATFORM.sendUpdateMessage(player, messageId, message);
    }

    /**
     * Closes a message for the specified player using the Embers messaging platform.
     * @param player The player to close the message for.
     * @param messageId The ID of the message to close.
     */
    @Override
    public void sendCloseMessage(ServerPlayer player, String messageId) {
        MESSAGING_PLATFORM.sendCloseMessage(player, messageId);
    }

    /**
     * Closes all messages for the specified player using the Embers messaging platform.
     * @param player The player to close all messages for.
     */
    @Override
    public void sendCloseAllMessages(ServerPlayer player) {
        MESSAGING_PLATFORM.sendCloseAllMessages(player);
    }
}
