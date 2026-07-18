package dev.matthiesen.common.matthiesen_lib_api.core.playerdata;

import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public final class SavedPlayerData extends SavedData {
    public record PlayerRecord(String name, List<String> aliases) {}

    private final Map<String, PlayerRecord> playerRecords = new HashMap<>();

    public SavedPlayerData() {
    }

    private static SavedPlayerData create() { return new SavedPlayerData(); }

    private static SavedPlayerData load(CompoundTag nbt, HolderLookup.Provider provider) {
        SavedPlayerData data = create();
        // Load player records from NBT
        CompoundTag playerRecordsNBT = nbt.getCompound("playerRecords");
        for (String key : playerRecordsNBT.getAllKeys()) {
            CompoundTag recordNBT = playerRecordsNBT.getCompound(key);
            String name = recordNBT.getString("name");
            List<String> aliases = recordNBT.getList("aliases", 8).stream().map(Tag::getAsString).toList();
            data.playerRecords.put(key, new PlayerRecord(name, aliases));
        }
        return data;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.Provider provider) {
        CompoundTag playerRecordsNBT = new CompoundTag();
        for (Map.Entry<String, PlayerRecord> entry : playerRecords.entrySet()) {
            PlayerRecord record = entry.getValue();
            CompoundTag recordNBT = new CompoundTag();
            recordNBT.putString("name", record.name());
            recordNBT.put("aliases", record.aliases().stream().map(StringTag::valueOf).collect(Collectors.toCollection(ListTag::new)));
            playerRecordsNBT.put(entry.getKey(), recordNBT);
        }
        compoundTag.put("playerRecords", playerRecordsNBT);
        return compoundTag;
    }

    private static final SavedData.Factory<SavedPlayerData> FACTORY = new Factory<>(
            SavedPlayerData::create,
            SavedPlayerData::load,
            null
    );

    private static SavedPlayerData getStore() {
        MinecraftServer server = MatthiesenLibApi.getMinecraftServer();
        if (server == null) return null;
        return server.overworld().getDataStorage().computeIfAbsent(FACTORY, MatthiesenLibApiConstants.PLAYER_DATA_STORE_ID);
    }

    public static void verifyPlayerData(ServerPlayer player) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return;

        String playerName = player.getScoreboardName();
        UUID playerUUID = player.getUUID();

        PlayerRecord existingRecord = dataStore.playerRecords.get(playerUUID.toString());
        if (existingRecord == null) {
            // No record exists, create a new one
            PlayerRecord newRecord = new PlayerRecord(playerName, List.of());
            dataStore.playerRecords.put(playerUUID.toString(), newRecord);
            dataStore.setDirty();
        } else {
            // Record exists, check for name changes
            if (!existingRecord.name().equals(playerName)) {
                // Name has changed, update the record
                List<String> updatedAliases = new ArrayList<>(existingRecord.aliases());
                if (!updatedAliases.contains(existingRecord.name())) {
                    updatedAliases.add(existingRecord.name());
                }
                PlayerRecord updatedRecord = new PlayerRecord(playerName, updatedAliases);
                dataStore.playerRecords.put(playerUUID.toString(), updatedRecord);
                dataStore.setDirty();
            }
        }
    }

    public static boolean hasSavedPlayerData(UUID uuid) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return false;
        return dataStore.playerRecords.containsKey(uuid.toString());
    }

    public static boolean hasSavedPlayerData(String name) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return false;
        for (PlayerRecord record : dataStore.playerRecords.values()) {
            if (record.name().equalsIgnoreCase(name) || record.aliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(name))) {
                return true;
            }
        }
        return false;
    }

    public static UUID findPlayerByUsername(String username) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return null;
        for (Map.Entry<String, PlayerRecord> entry : dataStore.playerRecords.entrySet()) {
            PlayerRecord record = entry.getValue();
            if (record.name().equalsIgnoreCase(username) || record.aliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(username))) {
                return UUID.fromString(entry.getKey());
            }
        }
        return null;
    }

    public static String findPlayerNameByUUID(UUID uuid) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return null;
        PlayerRecord record = dataStore.playerRecords.get(uuid.toString());
        return record != null ? record.name() : null;
    }

    public static List<String> getPlayerAliases(UUID uuid) {
        SavedPlayerData dataStore = getStore();
        if (dataStore == null) return Collections.emptyList();
        PlayerRecord record = dataStore.playerRecords.get(uuid.toString());
        return record != null ? record.aliases() : Collections.emptyList();
    }
}
