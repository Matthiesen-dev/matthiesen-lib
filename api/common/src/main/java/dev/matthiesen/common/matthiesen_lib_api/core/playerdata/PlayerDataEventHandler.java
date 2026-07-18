package dev.matthiesen.common.matthiesen_lib_api.core.playerdata;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibPlayerEventHandler;
import net.minecraft.server.level.ServerPlayer;

public final class PlayerDataEventHandler {
    public static String DATA_ID = MatthiesenLibApiConstants.MOD_ID + "_player_data";

    public static void init() {
        MatthiesenLibApi.registerPlayerEventHandler(DATA_ID, new MatthiesenLibPlayerEventHandler() {
            @Override
            public void onPlayerJoin(ServerPlayer player) {
                SavedPlayerData.verifyPlayerData(player);
            }
        });
    }
}
