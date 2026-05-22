package dev.matthiesen.common.matthiesen_lib.core.platform;

import dev.matthiesen.common.matthiesen_lib.core.compat.MatthiesenLibImmersiveMessageBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

/**
 * Interface for the messaging platform used to send messages to players in the Embers mod.
 * This interface is implemented by the platform-specific services in the respective modules.
 */
public interface MatthiesenLibEmbersMessagingPlatform {
    /**
     * Sends a message to a player.
     * @param player The player to send the message to.
     * @param message The message to send, represented as an Object for compatibility with the Embers mod's message format.
     * @param duration The duration for which the message should be displayed, in ticks.
     */
    void sendMessage(ServerPlayer player, Component message, float duration);

    /**
     * Sends a component message to a player using a custom builder for immersive messages.
     *
     * @param player The player to send the message to.
     * @param message The component message to send.
     * @param duration The duration for which the message should be displayed, in ticks.
     * @param builder The custom builder for immersive message visual behavior.
     */
    void sendMessage(ServerPlayer player, Component message, float duration, MatthiesenLibImmersiveMessageBuilder builder);

    /**
     * Sends a component message using an inline builder configuration callback.
     *
     * <p>This is a convenience overload that creates a new
     * {@link MatthiesenLibImmersiveMessageBuilder}, applies the provided configuration,
     * then delegates to {@link #sendMessage(ServerPlayer, Component, float, MatthiesenLibImmersiveMessageBuilder)}.
     *
     * @param player The player to send the message to.
     * @param message The component message to send.
     * @param duration The duration for which the message should be displayed, in ticks.
     * @param builderConfigurer Callback used to configure a new builder instance.
     */
    default void sendMessage(ServerPlayer player, Component message, float duration,
                             Consumer<MatthiesenLibImmersiveMessageBuilder> builderConfigurer) {
        if (builderConfigurer == null) {
            sendMessage(player, message, duration);
            return;
        }

        MatthiesenLibImmersiveMessageBuilder builder = MatthiesenLibImmersiveMessageBuilder.create();
        builderConfigurer.accept(builder);
        sendMessage(player, message, duration, builder);
    }

    /**
     * Sends a message to a player using markup.
     * @param player The player to send the message to.
     * @param message The message to send, represented as a String in markup format for compatibility with the Embers mod's message format.
     * @param duration The duration for which the message should be displayed, in ticks.
     */
    void sendMessage(ServerPlayer player, String message, float duration);

    /**
     * Sends a message to a player using a custom builder for immersive messages.
     * @param player The player to send the message to.
     * @param message The message to send, represented as a String in markup format for compatibility with the Embers mod's message format.
     * @param duration The duration for which the message should be displayed, in ticks.
     * @param builder The custom builder for immersive messages, allowing for additional customization of the message beyond the standard markup format.
     */
    void sendMessage(ServerPlayer player, String message, float duration, MatthiesenLibImmersiveMessageBuilder builder);

    /**
     * Sends a message using an inline builder configuration callback.
     *
     * <p>This is a convenience overload that creates a new
     * {@link MatthiesenLibImmersiveMessageBuilder}, applies the provided configuration,
     * then delegates to {@link #sendMessage(ServerPlayer, String, float, MatthiesenLibImmersiveMessageBuilder)}.
     *
     * @param player The player to send the message to.
     * @param message The message to send, represented as a String in markup format.
     * @param duration The duration for which the message should be displayed, in ticks.
     * @param builderConfigurer Callback used to configure a new builder instance.
     */
    default void sendMessage(ServerPlayer player, String message, float duration,
                             Consumer<MatthiesenLibImmersiveMessageBuilder> builderConfigurer) {
        if (builderConfigurer == null) {
            sendMessage(player, message, duration);
            return;
        }

        MatthiesenLibImmersiveMessageBuilder builder = MatthiesenLibImmersiveMessageBuilder.create();
        builderConfigurer.accept(builder);
        sendMessage(player, message, duration, builder);
    }

    /**
     * Updates an existing message for a player.
     * @param player The player to update the message for.
     * @param messageId The ID of the message to update.
     * @param message The new message content, represented as an Object for compatibility with the Embers mod's message format.
     * @param duration The duration for which the updated message should be displayed, in ticks.
     */
    void sendUpdateMessage(ServerPlayer player, String messageId, Component message, float duration);

    /**
     * Updates an existing component message for a player using a custom builder.
     *
     * @param player The player to update the message for.
     * @param messageId The ID of the message to update.
     * @param message The new component message content.
     * @param duration The duration for which the updated message should be displayed, in ticks.
     * @param builder The custom builder for immersive message visual behavior.
     */
    void sendUpdateMessage(ServerPlayer player, String messageId, Component message, float duration,
                           MatthiesenLibImmersiveMessageBuilder builder);

    /**
     * Updates an existing component message using an inline builder configuration callback.
     *
     * <p>This is a convenience overload that creates a new
     * {@link MatthiesenLibImmersiveMessageBuilder}, applies the provided configuration,
     * then delegates to {@link #sendUpdateMessage(ServerPlayer, String, Component, float, MatthiesenLibImmersiveMessageBuilder)}.
     *
     * @param player The player to update the message for.
     * @param messageId The ID of the message to update.
     * @param message The new component message content.
     * @param duration The duration for which the updated message should be displayed, in ticks.
     * @param builderConfigurer Callback used to configure a new builder instance.
     */
    default void sendUpdateMessage(ServerPlayer player, String messageId, Component message, float duration,
                                   Consumer<MatthiesenLibImmersiveMessageBuilder> builderConfigurer) {
        if (builderConfigurer == null) {
            sendUpdateMessage(player, messageId, message, duration);
            return;
        }

        MatthiesenLibImmersiveMessageBuilder builder = MatthiesenLibImmersiveMessageBuilder.create();
        builderConfigurer.accept(builder);
        sendUpdateMessage(player, messageId, message, duration, builder);
    }

    /**
     * Updates an existing message for a player using markup.
     * @param player The player to update the message for.
     * @param messageId The ID of the message to update.
     * @param message The new message content, represented as a String in markup format for compatibility with the Embers mod's message format.
     * @param duration The duration for which the updated message should be displayed, in ticks.
     */
    void sendUpdateMessage(ServerPlayer player, String messageId, String message, float duration);

    /**
     * Updates an existing message for a player using a custom builder for immersive messages.
     * @param player The player to update the message for.
     * @param messageId The ID of the message to update.
     * @param message The new message content, represented as a String in markup format for compatibility with the Embers mod's message format.
     * @param duration The duration for which the updated message should be displayed, in ticks.
     * @param builder The custom builder for immersive messages, allowing for additional customization of the updated message beyond the standard markup format.
     */
    void sendUpdateMessage(ServerPlayer player, String messageId, String message, float duration, MatthiesenLibImmersiveMessageBuilder builder);

    /**
     * Updates an existing message using an inline builder configuration callback.
     *
     * <p>This is a convenience overload that creates a new
     * {@link MatthiesenLibImmersiveMessageBuilder}, applies the provided configuration,
     * then delegates to {@link #sendUpdateMessage(ServerPlayer, String, String, float, MatthiesenLibImmersiveMessageBuilder)}.
     *
     * @param player The player to update the message for.
     * @param messageId The ID of the message to update.
     * @param message The new message content, represented as a String in markup format.
     * @param duration The duration for which the updated message should be displayed, in ticks.
     * @param builderConfigurer Callback used to configure a new builder instance.
     */
    default void sendUpdateMessage(ServerPlayer player, String messageId, String message, float duration,
                                   Consumer<MatthiesenLibImmersiveMessageBuilder> builderConfigurer) {
        if (builderConfigurer == null) {
            sendUpdateMessage(player, messageId, message, duration);
            return;
        }

        MatthiesenLibImmersiveMessageBuilder builder = MatthiesenLibImmersiveMessageBuilder.create();
        builderConfigurer.accept(builder);
        sendUpdateMessage(player, messageId, message, duration, builder);
    }

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
