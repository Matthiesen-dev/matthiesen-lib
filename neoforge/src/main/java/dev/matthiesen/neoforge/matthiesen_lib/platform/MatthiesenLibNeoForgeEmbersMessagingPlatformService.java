package dev.matthiesen.neoforge.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.core.compat.MatthiesenLibImmersiveMessageBuilder;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibEmbersMessagingPlatform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.tysontheember.emberstextapi.immersivemessages.api.ImmersiveMessage;
import net.tysontheember.emberstextapi.platform.NetworkHelper;

/**
 * Implementation of the MatthiesenLibEmbersMessagingPlatform interface for NeoForge, using the Embers mod's messaging system.
 * This class provides methods to send messages to players using the Embers mod's immersive message system, and checks
 * if the Embers mod is loaded before attempting to send messages.
 */
public class MatthiesenLibNeoForgeEmbersMessagingPlatformService implements MatthiesenLibEmbersMessagingPlatform {
    /**
     * Default constructor for the MatthiesenLibNeoForgeEmbersMessagingPlatformService class.
     */
    public MatthiesenLibNeoForgeEmbersMessagingPlatformService() {}

    @Override
    public void sendMessage(ServerPlayer player, Component message, float duration) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendMessage(player, new ImmersiveMessage(message, duration));
        }
    }

    @Override
    public void sendMessage(ServerPlayer player, String message, float duration) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendMessage(player, ImmersiveMessage.fromMarkup(duration, message));
        }
    }

    @Override
    public void sendMessage(ServerPlayer player, String message, float duration, MatthiesenLibImmersiveMessageBuilder builder) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            ImmersiveMessage msg = ImmersiveMessage.builder(duration, message);
            if (builder != null) {
                builder.applyTo(msg);
            }
            NetworkHelper.getInstance().sendMessage(player, msg);
        }
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, Component message, float duration) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendUpdateMessage(player, messageId, new ImmersiveMessage(message, duration));
        }
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, String message, float duration) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendUpdateMessage(player, messageId, ImmersiveMessage.fromMarkup(duration, message));
        }
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, String message, float duration, MatthiesenLibImmersiveMessageBuilder builder) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            ImmersiveMessage msg = ImmersiveMessage.builder(duration, message);
            if (builder != null) {
                builder.applyTo(msg);
            }
            NetworkHelper.getInstance().sendUpdateMessage(player, messageId, msg);
        }
    }

    @Override
    public void sendCloseMessage(ServerPlayer player, String messageId) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendCloseMessage(player, messageId);
        }
    }

    @Override
    public void sendCloseAllMessages(ServerPlayer player) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendCloseAllMessages(player);
        }
    }
}
