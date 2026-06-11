package dev.matthiesen.common.matthiesen_lib_api.core.discord.api;


import dev.matthiesen.common.matthiesen_lib_api.core.discord.exception.DiscordWebhookException;
import dev.matthiesen.common.matthiesen_lib_api.core.discord.model.WebhookMessage;

/**
 * Interface for sending messages to Discord webhooks.
 */
@SuppressWarnings("unused")
public interface DiscordWebhookClient {
    /**
     * Sends a message to the specified Discord webhook URL.
     * @param webhookUrl The URL of the Discord webhook to send the message to.
     * @param message The message to be sent, encapsulated in a WebhookMessage object.
     * @throws DiscordWebhookException If an error occurs during message sending, such as network issues or invalid webhook URL.
     */
    void sendMessage(String webhookUrl, WebhookMessage message) throws DiscordWebhookException;
}

