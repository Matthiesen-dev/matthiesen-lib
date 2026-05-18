package dev.matthiesen.neoforge.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers;
import dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibEmbersMessagingPlatform;
import net.minecraft.server.level.ServerPlayer;
import net.tysontheember.emberstextapi.immersivemessages.api.ImmersiveMessage;
import net.tysontheember.emberstextapi.platform.NetworkHelper;

public class MatthiesenLibNeoForgeEmbersMessagingPlatformService implements MatthiesenLibEmbersMessagingPlatform {
    @Override
    public void sendMessage(ServerPlayer player, /* ImmersiveMessage */ Object message) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendMessage(player, (ImmersiveMessage) message);
        }
    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, /* ImmersiveMessage */ Object message) {
        if (MatthiesenLib.isModLoaded(MatthiesenLibBuiltInTextParsers.EMBER.getName())) {
            NetworkHelper.getInstance().sendUpdateMessage(player, messageId, (ImmersiveMessage) message);
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
