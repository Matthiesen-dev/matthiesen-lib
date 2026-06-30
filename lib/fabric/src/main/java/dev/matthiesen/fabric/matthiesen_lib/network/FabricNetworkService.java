package dev.matthiesen.fabric.matthiesen_lib.network;

import dev.matthiesen.common.matthiesen_lib.core.network.NetworkService;
import dev.matthiesen.common.matthiesen_lib.core.network.PacketContext;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;

public class FabricNetworkService implements NetworkService {
    @Override
    public <T extends CustomPacketPayload> void registerC2S(
            CustomPacketPayload.Type<T> type,
            StreamCodec<RegistryFriendlyByteBuf, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {
        // 1. Register the codec to Fabric's networking registry
        PayloadTypeRegistry.playC2S().register(type, codec);

        // 2. Bind the receiver to execute on Minecraft's server thread
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            PacketContext packetContext = new PacketContext(context.player(), () -> {
                context.server().execute(() -> {}); // Force sync
                return null;
            });
            handler.accept(payload, packetContext);
        });
    }

    @Override
    public <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<T, PacketContext> handler) {
        // 1. Register codec to Fabric's S2C Registry
        PayloadTypeRegistry.playS2C().register(type, codec);

        // 2. Register client-side handler (Sided isolation happens automatically inside Fabric)
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            handler.accept(payload, new PacketContext(context.player(), () -> {
                context.client().execute(() -> {}); // Force client-thread execution safely
                return null;
            }));
        });
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }
}
