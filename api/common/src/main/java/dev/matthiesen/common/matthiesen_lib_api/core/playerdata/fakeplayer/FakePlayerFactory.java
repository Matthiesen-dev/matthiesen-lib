package dev.matthiesen.common.matthiesen_lib_api.core.playerdata.fakeplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class FakePlayerFactory {
    private static final GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AFB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
    private static final ConcurrentMap<FakePlayerKey, FakePlayer> fakePlayers = new ConcurrentHashMap<>();
    private static final ConcurrentMap<FakePlayerKey, CompletableFuture<FakePlayer>> pending = new ConcurrentHashMap<>();

    private FakePlayerFactory() {
    }

    public static FakePlayer getMinecraft(ServerLevel level) {
        return get(level, MINECRAFT);
    }

    public static FakePlayer get(ServerLevel level, GameProfile profile) {
        MinecraftServer server = level.getServer();
        FakePlayerKey key = new FakePlayerKey(level.dimension().location().hashCode(), profile.getId());
        FakePlayer cached = fakePlayers.get(key);
        if (cached != null) {
            return cached;
        } else if (server.isSameThread()) {
            return fakePlayers.computeIfAbsent(key, (k) -> new FakePlayer(level, profile));
        } else {
            CompletableFuture<FakePlayer> future = pending.computeIfAbsent(key, (k) -> {
                CompletableFuture<FakePlayer> f = new CompletableFuture<>();
                server.execute(() -> {
                    try {
                        FakePlayer fp = fakePlayers.computeIfAbsent(key, (kk) -> new FakePlayer(level, profile));
                        f.complete(fp);
                    } finally {
                        pending.remove(key);
                    }

                });
                return f;
            });
            return future.join();
        }
    }

    public static void unloadLevel(ServerLevel level) {
        int dim = level.dimension().location().hashCode();
        fakePlayers.keySet().removeIf((k) -> k.dimensionId == dim);
        pending.keySet().removeIf((k) -> k.dimensionId == dim);
    }

    public static void clearAll() {
        fakePlayers.clear();
        pending.clear();
    }

    private static record FakePlayerKey(int dimensionId, UUID uuid) {
    }
}
