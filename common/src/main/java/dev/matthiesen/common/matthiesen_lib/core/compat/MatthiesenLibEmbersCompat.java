package dev.matthiesen.common.matthiesen_lib.core.compat;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEmbersTextParserCompat;
import dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibEmbersMessagingPlatform;
import net.minecraft.network.chat.Component;
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

    @Override
    public void sendMessage(ServerPlayer player, Component message, float duration) {
        MESSAGING_PLATFORM.sendMessage(player, message, duration);
    }

    @Override
    public void sendMessage(ServerPlayer player, String message, float duration) {
        MESSAGING_PLATFORM.sendMessage(player, message, duration);
    }

    @Override
    public void sendMessage(ServerPlayer player, String message, float duration, MatthiesenLibImmersiveMessageBuilder builder) {
        MESSAGING_PLATFORM.sendMessage(player, message, duration, builder);
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, Component message, float duration) {
        MESSAGING_PLATFORM.sendUpdateMessage(player, messageId, message, duration);
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, String message, float duration) {
        MESSAGING_PLATFORM.sendUpdateMessage(player, messageId, message, duration);
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, String message, float duration, MatthiesenLibImmersiveMessageBuilder builder) {
        MESSAGING_PLATFORM.sendUpdateMessage(player, messageId, message, duration, builder);
    }

    @Override
    public void sendCloseMessage(ServerPlayer player, String messageId) {
        MESSAGING_PLATFORM.sendCloseMessage(player, messageId);
    }

    @Override
    public void sendCloseAllMessages(ServerPlayer player) {
        MESSAGING_PLATFORM.sendCloseAllMessages(player);
    }
}
