package dev.matthiesen.common.matthiesen_lib_api.utility;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.playerdata.SavedPlayerData;
import dev.matthiesen.common.matthiesen_lib_api.core.playerdata.fakeplayer.FakePlayerFactory;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Represents a user on the server, which can be either online or offline.
 */
@SuppressWarnings("unused")
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

    public List<String> getAliases() {
        return SavedPlayerData.getPlayerAliases(this.uuid);
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getStringUUID() {
        return this.uuid.toString();
    }

    public static class CmdArgument implements ArgumentType<String> {
        private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "dd12be42-52a9-4a91-a8a1-11c01849e498");

        public static CmdArgument playerArg() {
            return new CmdArgument();
        }

        public static ServerUser getUser(CommandContext<CommandSourceStack> context, String string) {
            var name = context.getArgument(string, String.class);
            if (name == null) return null;
            UUID uuid = null;
            // If string is a valid UUID
            try {
                uuid = UUID.fromString(name);
            } catch (IllegalArgumentException e) {
                // Do nothing
            }
            if (uuid != null) {
                // If UUID is valid, check if player exists
                if (SavedPlayerData.hasSavedPlayerData(uuid)) return new ServerUser(uuid);
                // Else continue
            }
            // Check if the player name exists in the saved player data
            if (SavedPlayerData.hasSavedPlayerData(name)) return new ServerUser(name);
            return null;
        }

        @Override
        public String parse(StringReader reader) throws CommandSyntaxException {
            return reader.readString();
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            Object var4 = context.getSource();
            if (var4 instanceof SharedSuggestionProvider sharedSuggestionProvider) {
                StringReader stringReader = new StringReader(builder.getInput());
                stringReader.setCursor(builder.getStart());

                EntitySelectorParser entitySelectorParser = new EntitySelectorParser(stringReader, EntitySelectorParser.allowSelectors(sharedSuggestionProvider));

                try {
                    entitySelectorParser.parse();
                } catch (CommandSyntaxException e) {
                    // Handle exception
                }

                return entitySelectorParser.fillSuggestions(builder, (suggestionsBuilder -> {
                    Collection<String> collection = sharedSuggestionProvider.getOnlinePlayerNames();
                    SharedSuggestionProvider.suggest(collection, suggestionsBuilder);
                }));
            } else {
                return Suggestions.empty();
            }
        }

        @Override
        public Collection<String> getExamples() {
            return EXAMPLES;
        }
    }
}
