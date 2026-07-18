package dev.matthiesen.common.matthiesen_lib_api.utility;

import com.mojang.authlib.GameProfile;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.playerdata.SavedPlayerData;
import dev.matthiesen.common.matthiesen_lib_api.core.playerdata.fakeplayer.FakePlayerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ServerUser {
    private final UUID uuid;
    private Player offlinePlayer;

    public ServerUser(ServerPlayer player) {
        this.uuid = player.getUUID();
        this.offlinePlayer = player;
    }

    public ServerUser(Player player) {
        this.uuid = player.getUUID();
        this.offlinePlayer = player;
    }

    public ServerUser(UUID uuid) {
        this.uuid = uuid;
    }

    public ServerUser(String playerName) {
        this.uuid = getPlayerUUID(playerName);
    }

    private UUID getPlayerUUID(String name) {
        return SavedPlayerData.findPlayerByUsername(name);
    }

    public ServerPlayer getOnlinePlayer() {
        return MatthiesenLibApi.getMinecraftServer().getPlayerList().getPlayer(this.uuid);
    }

    public boolean isOnline() {
        return getOnlinePlayer() != null;
    }

    public Player getOfflinePlayer() {
        Player player = this.getOnlinePlayer();
        if (player != null) return player;
        if (this.offlinePlayer != null) return this.offlinePlayer;
        MinecraftServer server = MatthiesenLibApi.getMinecraftServer();
        if (server == null) return null;
        this.offlinePlayer = server.getPlayerList().getPlayer(this.uuid);
        if (this.offlinePlayer != null) return this.offlinePlayer;
        GameProfileCache profileCache = server.getProfileCache();
        if (profileCache == null) return null;
        GameProfile gameProfile = profileCache.get(this.uuid).orElse(null);
        if (gameProfile == null) return null;
        this.offlinePlayer = FakePlayerFactory.get(server.overworld(), gameProfile);
        return this.offlinePlayer;
    }

    public String getUsername() {
        Player player = getOnlinePlayer();
        if (player != null) return player.getScoreboardName();
        player = getOfflinePlayer();
        if (player != null) return player.getScoreboardName();
        return SavedPlayerData.findPlayerNameByUUID(this.uuid);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getStringUUID() {
        return this.uuid.toString();
    }
}
