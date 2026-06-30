package dev.matthiesen.common.matthiesen_lib.core.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.ServiceLoader;
import java.util.function.BiConsumer;

/**
 * NetworkingManager is a singleton class that manages the registration and sending of custom packets
 * in a Minecraft modding environment. It provides a unified interface for different platform-specific
 * implementations of the NetworkService interface.
 */
public final class NetworkingManager {
    /**
     * The NetworkService INSTANCE
     */
    public static final NetworkService INSTANCE = ServiceLoader.load(NetworkService.class).findFirst().orElseThrow();

    /**
     * Registers a client-to-server (C2S) packet type with the specified codec and handler.
     * @param type The custom packet type to register.
     * @param codec The codec for encoding and decoding the packet.
     * @param handler The handler to process the packet when received.
     * @param <T> The type of the custom packet payload.
     */
    public static <T extends CustomPacketPayload> void registerC2S(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {
        INSTANCE.registerC2S(type, codec, handler);
    }

    /**
     * Registers a server-to-client (S2C) packet type with the specified codec and handler.
     * @param type The custom packet type to register.
     * @param codec The codec for encoding and decoding the packet.
     * @param handler The handler to process the packet when received.
     * @param <T> The type of the custom packet payload.
     */
    public static <T extends CustomPacketPayload> void registerS2C(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {
        INSTANCE.registerS2C(type, codec, handler);
    }

    /**
     * Sends a custom packet payload to the server.
     * @param payload The custom packet payload to send.
     */
    public static void sendToServer(CustomPacketPayload payload) {
        INSTANCE.sendToServer(payload);
    }

    /**
     * Sends a custom packet payload to a specific player on the server.
     * @param player The player to send the packet to.
     * @param payload The custom packet payload to send.
     */
    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        INSTANCE.sendToPlayer(player, payload);
    }
}
