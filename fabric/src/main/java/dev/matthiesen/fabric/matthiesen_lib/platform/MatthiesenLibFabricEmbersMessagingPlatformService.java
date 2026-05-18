package dev.matthiesen.fabric.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibEmbersMessagingPlatform;
import net.minecraft.server.level.ServerPlayer;

public class MatthiesenLibFabricEmbersMessagingPlatformService  implements MatthiesenLibEmbersMessagingPlatform {
    @Override
    public void sendMessage(ServerPlayer player, /* ImmersiveMessage */ Object message) {

    }

    @Override
    public void sendUpdateMessage(ServerPlayer player, String messageId, /* ImmersiveMessage */ Object message) {

    }

    @Override
    public void sendCloseMessage(ServerPlayer player, String messageId) {

    }

    @Override
    public void sendCloseAllMessages(ServerPlayer player) {

    }
}
