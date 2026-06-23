package dev.matthiesen.fabric.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.core.compat.MatthiesenLibImmersiveMessageBuilder;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibEmbersMessagingPlatform;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.tysontheember.emberstextapi.immersivemessages.api.ImmersiveMessage;
import net.tysontheember.emberstextapi.platform.NetworkHelper;

/**
 * Implementation of the MatthiesenLibEmbersMessagingPlatform interface for Fabric, using the Embers mod's messaging system.
 */
public class MatthiesenLibFabricEmbersMessagingPlatformService implements MatthiesenLibEmbersMessagingPlatform {
    /**
     * Default constructor for the MatthiesenLibFabricEmbersMessagingPlatformService class.
     */
    public MatthiesenLibFabricEmbersMessagingPlatformService() {}

    @Override
    public void sendMessage(ServerPlayer player, Component message, int duration) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendMessage(player, new ImmersiveMessage(message, duration));
        }
    }

    @Override
    public void sendMessage(ServerPlayer player, Component message, int duration, MatthiesenLibImmersiveMessageBuilder builder) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            ImmersiveMessage msg = new ImmersiveMessage(message, duration);
            if (builder != null) {
                builder.applyTo(msg);
            }
            NetworkHelper.getInstance().sendMessage(player, msg);
        }
    }

    @Override
    public void sendMessage(ServerPlayer player, String message, int duration) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendMessage(player, ImmersiveMessage.fromMarkup(duration, message));
        }
    }

    @Override
    public void sendMessage(ServerPlayer player, String message, int duration, MatthiesenLibImmersiveMessageBuilder builder) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            ImmersiveMessage msg = ImmersiveMessage.builder(duration, message);
            if (builder != null) {
                builder.applyTo(msg);
            }
            NetworkHelper.getInstance().sendMessage(player, msg);
        }
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, Component message, int duration) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendUpdateMessage(player, messageId, new ImmersiveMessage(message, duration));
        }
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, Component message, int duration, MatthiesenLibImmersiveMessageBuilder builder) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            ImmersiveMessage msg = new ImmersiveMessage(message, duration);
            if (builder != null) {
                builder.applyTo(msg);
            }
            NetworkHelper.getInstance().sendUpdateMessage(player, messageId, msg);
        }
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, String message, int duration) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendUpdateMessage(player, messageId, ImmersiveMessage.fromMarkup(duration, message));
        }
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, String message, int duration, MatthiesenLibImmersiveMessageBuilder builder) {
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
