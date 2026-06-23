package dev.matthiesen.common.matthiesen_lib.core.compat;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibEmbersTextParserCompat;
import dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibEmbersMessagingPlatform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ServiceLoader;
import java.util.function.Consumer;

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
    public void sendMessage(ServerPlayer player, Component message, int duration) {
        MESSAGING_PLATFORM.sendMessage(player, message, duration);
    }

    @Override
    public void sendMessage(ServerPlayer player, Component message, int duration, MatthiesenLibImmersiveMessageBuilder builder) {
        MESSAGING_PLATFORM.sendMessage(player, message, duration, builder);
    }

    /**
     * Sends a component message with inline builder configuration using a lambda.
     *
     * @param player player receiving the message
     * @param message component message content
     * @param duration display duration in ticks
     * @param builderConfigurer callback to configure a fresh builder instance
     */
    @Override
    public void sendMessage(ServerPlayer player, Component message, int duration,
                            Consumer<MatthiesenLibImmersiveMessageBuilder> builderConfigurer) {
        MatthiesenLibEmbersTextParserCompat.super.sendMessage(player, message, duration, builderConfigurer);
    }

    @Override
    public void sendMessage(ServerPlayer player, String message, int duration) {
        MESSAGING_PLATFORM.sendMessage(player, message, duration);
    }

    @Override
    public void sendMessage(ServerPlayer player, String message, int duration, MatthiesenLibImmersiveMessageBuilder builder) {
        MESSAGING_PLATFORM.sendMessage(player, message, duration, builder);
    }

    /**
     * Sends a message with inline builder configuration using a lambda.
     *
     * @param player player receiving the message
     * @param message message markup string
     * @param duration display duration in ticks
     * @param builderConfigurer callback to configure a fresh builder instance
     */
    @Override
    public void sendMessage(ServerPlayer player, String message, int duration,
                            Consumer<MatthiesenLibImmersiveMessageBuilder> builderConfigurer) {
        MatthiesenLibEmbersTextParserCompat.super.sendMessage(player, message, duration, builderConfigurer);
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, Component message, int duration) {
        MESSAGING_PLATFORM.sendUpdateMessage(player, messageId, message, duration);
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, Component message, int duration,
                                  MatthiesenLibImmersiveMessageBuilder builder) {
        MESSAGING_PLATFORM.sendUpdateMessage(player, messageId, message, duration, builder);
    }

    /**
     * Updates a component message with inline builder configuration using a lambda.
     *
     * @param player player receiving the update
     * @param messageId id of the message to update
     * @param message updated component message content
     * @param duration display duration in ticks
     * @param builderConfigurer callback to configure a fresh builder instance
     */
    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, Component message, int duration,
                                  Consumer<MatthiesenLibImmersiveMessageBuilder> builderConfigurer) {
        MatthiesenLibEmbersTextParserCompat.super.sendUpdateMessage(player, messageId, message, duration, builderConfigurer);
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, String message, int duration) {
        MESSAGING_PLATFORM.sendUpdateMessage(player, messageId, message, duration);
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, String message, int duration, MatthiesenLibImmersiveMessageBuilder builder) {
        MESSAGING_PLATFORM.sendUpdateMessage(player, messageId, message, duration, builder);
    }

    /**
     * Updates a message with inline builder configuration using a lambda.
     *
     * @param player player receiving the update
     * @param messageId id of the message to update
     * @param message updated message markup string
     * @param duration display duration in ticks
     * @param builderConfigurer callback to configure a fresh builder instance
     */
    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, String message, int duration,
                                  Consumer<MatthiesenLibImmersiveMessageBuilder> builderConfigurer) {
        MatthiesenLibEmbersTextParserCompat.super.sendUpdateMessage(player, messageId, message, duration, builderConfigurer);
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
