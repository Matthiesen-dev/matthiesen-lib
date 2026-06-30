package dev.matthiesen.neoforge.matthiesen_lib.network;

import dev.matthiesen.common.matthiesen_lib.core.network.NetworkService;
import dev.matthiesen.common.matthiesen_lib.core.network.PacketContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class NeoForgeNetworkService implements NetworkService {
    // Defer processing until the lifecycle event runs
    protected static final List<PayloadRegistration<?, ?>> PENDING_C2S = new ArrayList<>();
    protected static final List<PayloadRegistration<?, ?>> PENDING_S2C = new ArrayList<>();

    @Override
    public <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<T, PacketContext> handler) {
        PENDING_C2S.add(new PayloadRegistration<>(type, codec, handler));
    }

    @Override
    public <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<RegistryFriendlyByteBuf, T> codec, BiConsumer<T, PacketContext> handler) {
        PENDING_S2C.add(new PayloadRegistration<>(type, codec, handler));
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        PacketDistributor.sendToServer(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }

    // Immutable record to store cross-thread cache data
    protected record PayloadRegistration<T extends CustomPacketPayload, B extends RegistryFriendlyByteBuf>(
            CustomPacketPayload.Type<T> type,
            StreamCodec<B, T> codec,
            BiConsumer<T, PacketContext> handler
    ) {}
}
