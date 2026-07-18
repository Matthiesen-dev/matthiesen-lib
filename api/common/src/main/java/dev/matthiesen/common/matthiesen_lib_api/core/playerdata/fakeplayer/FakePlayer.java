package dev.matthiesen.common.matthiesen_lib_api.core.playerdata.fakeplayer;

import com.mojang.authlib.GameProfile;
import dev.matthiesen.common.matthiesen_lib_api.MatthiesenLibApi;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.stats.Stat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.entity.player.Player;

import java.util.Set;
import java.util.UUID;

public final class FakePlayer extends ServerPlayer {
    private static final GameProfile MINECRAFT = new GameProfile(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"), "[Minecraft]");
    private static final ClientInformation FAKE_CLIENT_INFO;
    private static final CommonListenerCookie COOKIE;

    public FakePlayer(ServerLevel level, GameProfile name) {
        super(level.getServer(), level, name, FAKE_CLIENT_INFO);
        this.connection = new FakePlayerNetHandler(level.getServer(), this);
    }

    public void displayClientMessage(Component chatComponent, boolean actionBar) {
    }

    public void awardStat(Stat stat, int amount) {
    }

    public boolean isInvulnerableTo(DamageSource source) {
        return true;
    }

    public boolean canHarmPlayer(Player player) {
        return false;
    }

    public void die(DamageSource source) {
    }

    public void tick() {
    }

    public void updateOptions(ClientInformation packet) {
    }

    public MinecraftServer getServer() {
        return MatthiesenLibApi.getMinecraftServer();
    }

    static {
        FAKE_CLIENT_INFO = new ClientInformation("en_US", 16, ChatVisiblity.FULL, true, 0, HumanoidArm.LEFT, false, false);
        COOKIE = CommonListenerCookie.createInitial(MINECRAFT, false);
    }

    private static class FakePlayerNetHandler extends ServerGamePacketListenerImpl {
        private static final Connection DUMMY_CONNECTION;

        public FakePlayerNetHandler(MinecraftServer server, ServerPlayer player) {
            super(server, DUMMY_CONNECTION, player, FakePlayer.COOKIE);
        }

        public void tick() {
        }

        public void resetPosition() {
        }

        public void disconnect(Component message) {
        }

        public void handlePlayerInput(ServerboundPlayerInputPacket packet) {
        }

        public void handleMoveVehicle(ServerboundMoveVehiclePacket packet) {
        }

        public void handleAcceptTeleportPacket(ServerboundAcceptTeleportationPacket packet) {
        }

        public void handleRecipeBookSeenRecipePacket(ServerboundRecipeBookSeenRecipePacket packet) {
        }

        public void handleRecipeBookChangeSettingsPacket(ServerboundRecipeBookChangeSettingsPacket packet) {
        }

        public void handleSeenAdvancements(ServerboundSeenAdvancementsPacket packet) {
        }

        public void handleCustomCommandSuggestions(ServerboundCommandSuggestionPacket packet) {
        }

        public void handleSetCommandBlock(ServerboundSetCommandBlockPacket packet) {
        }

        public void handleSetCommandMinecart(ServerboundSetCommandMinecartPacket packet) {
        }

        public void handlePickItem(ServerboundPickItemPacket packet) {
        }

        public void handleRenameItem(ServerboundRenameItemPacket packet) {
        }

        public void handleSetBeaconPacket(ServerboundSetBeaconPacket packet) {
        }

        public void handleSetStructureBlock(ServerboundSetStructureBlockPacket packet) {
        }

        public void handleSetJigsawBlock(ServerboundSetJigsawBlockPacket packet) {
        }

        public void handleJigsawGenerate(ServerboundJigsawGeneratePacket packet) {
        }

        public void handleSelectTrade(ServerboundSelectTradePacket packet) {
        }

        public void handleEditBook(ServerboundEditBookPacket packet) {
        }

        public void handleEntityTagQuery(ServerboundEntityTagQueryPacket packet) {
        }

        public void handleBlockEntityTagQuery(ServerboundBlockEntityTagQueryPacket packet) {
        }

        public void handleMovePlayer(ServerboundMovePlayerPacket packet) {
        }

        public void teleport(double x, double y, double z, float yaw, float pitch) {
        }

        public void handlePlayerAction(ServerboundPlayerActionPacket packet) {
        }

        public void handleUseItemOn(ServerboundUseItemOnPacket packet) {
        }

        public void handleUseItem(ServerboundUseItemPacket packet) {
        }

        public void handleTeleportToEntityPacket(ServerboundTeleportToEntityPacket packet) {
        }

        public void handleResourcePackResponse(ServerboundResourcePackPacket packet) {
        }

        public void handlePaddleBoat(ServerboundPaddleBoatPacket packet) {
        }

        public void send(Packet<?> packet) {
        }

        public void send(Packet<?> packet, PacketSendListener sendListener) {
        }

        public void handleSetCarriedItem(ServerboundSetCarriedItemPacket packet) {
        }

        public void handleChat(ServerboundChatPacket packet) {
        }

        public void handleAnimate(ServerboundSwingPacket packet) {
        }

        public void handlePlayerCommand(ServerboundPlayerCommandPacket packet) {
        }

        public void handleInteract(ServerboundInteractPacket packet) {
        }

        public void handleClientCommand(ServerboundClientCommandPacket packet) {
        }

        public void handleContainerClose(ServerboundContainerClosePacket packet) {
        }

        public void handleContainerClick(ServerboundContainerClickPacket packet) {
        }

        public void handlePlaceRecipe(ServerboundPlaceRecipePacket packet) {
        }

        public void handleContainerButtonClick(ServerboundContainerButtonClickPacket packet) {
        }

        public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket packet) {
        }

        public void handleSignUpdate(ServerboundSignUpdatePacket packet) {
        }

        public void handleKeepAlive(ServerboundKeepAlivePacket packet) {
        }

        public void handlePlayerAbilities(ServerboundPlayerAbilitiesPacket packet) {
        }

        public void handleClientInformation(ServerboundClientInformationPacket packet) {
        }

        public void handleCustomPayload(ServerboundCustomPayloadPacket packet) {
        }

        public void handleChangeDifficulty(ServerboundChangeDifficultyPacket packet) {
        }

        public void handleLockDifficulty(ServerboundLockDifficultyPacket packet) {
        }

        public void teleport(double x, double y, double z, float yaw, float pitch, Set<RelativeMovement> relativeSet) {
        }

        public void ackBlockChangesUpTo(int sequence) {
        }

        public void handleChatCommand(ServerboundChatCommandPacket packet) {
        }

        public void handleChatAck(ServerboundChatAckPacket packet) {
        }

        public void addPendingMessage(PlayerChatMessage message) {
        }

        public void sendPlayerChatMessage(PlayerChatMessage message, ChatType.Bound boundChatType) {
        }

        public void sendDisguisedChatMessage(Component content, ChatType.Bound boundChatType) {
        }

        public void handleChatSessionUpdate(ServerboundChatSessionUpdatePacket packet) {
        }

        static {
            DUMMY_CONNECTION = new Connection(PacketFlow.CLIENTBOUND);
        }
    }
}