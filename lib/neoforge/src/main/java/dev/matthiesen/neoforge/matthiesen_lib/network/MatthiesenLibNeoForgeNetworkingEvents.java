package dev.matthiesen.neoforge.matthiesen_lib.network;

import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import dev.matthiesen.common.matthiesen_lib.core.network.PacketContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = MatthiesenLibConstants.MOD_ID)
public class MatthiesenLibNeoForgeNetworkingEvents {
    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0");

        // Unpack Client-To-Server registers
        for (var reg : NeoForgeNetworkService.PENDING_C2S) {
            registerC2S(registrar, reg);
        }

        NeoForgeNetworkService.PENDING_C2S.clear();

        // Unpack Server-To-Client registers
        for (var reg : NeoForgeNetworkService.PENDING_S2C) {
            registerS2C(registrar, reg);
        }
        NeoForgeNetworkService.PENDING_S2C.clear();
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPacketPayload> void registerC2S(PayloadRegistrar registrar, NeoForgeNetworkService.PayloadRegistration<?, ?> reg) {
        var type = (CustomPacketPayload.Type<T>) reg.type();
        var codec = (net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, T>) reg.codec();
        var handler = (java.util.function.BiConsumer<T, PacketContext>) reg.handler();

        registrar.playToServer(type, codec, (payload, context) -> {
            PacketContext packetContext = new PacketContext(context.player(), () -> {
                context.enqueueWork(() -> {});
                return null;
            });
            handler.accept(payload, packetContext);
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends CustomPacketPayload> void registerS2C(PayloadRegistrar registrar, NeoForgeNetworkService.PayloadRegistration<?, ?> reg) {
        var type = (CustomPacketPayload.Type<T>) reg.type();
        var codec = (net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, T>) reg.codec();
        var handler = (java.util.function.BiConsumer<T, PacketContext>) reg.handler();

        registrar.playToClient(type, codec, (payload, context) -> {
            PacketContext packetContext = new PacketContext(context.player(), () -> {
                context.enqueueWork(() -> {});
                return null;
            });
            handler.accept(payload, packetContext);
        });
    }
}
