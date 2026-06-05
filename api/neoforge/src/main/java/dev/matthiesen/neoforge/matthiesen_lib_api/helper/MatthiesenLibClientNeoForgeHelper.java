package dev.matthiesen.neoforge.matthiesen_lib_api.helper;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

public class MatthiesenLibClientNeoForgeHelper {
    private static volatile IEventBus modBus;

    private MatthiesenLibClientNeoForgeHelper() {
    }

    public static void init(IEventBus eventBus) {
        modBus = eventBus;
    }

    public static void registerStartupEvent(Consumer<FMLClientSetupEvent> eventConsumer) {
        registerBusListener(eventConsumer);
    }

    public static <T extends Event> void registerBusListener(Consumer<T> listener) {
        modBus.addListener(listener);
    }
}
