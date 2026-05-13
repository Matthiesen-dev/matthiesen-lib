package dev.matthiesen.neoforge.matthiesen_lib.platform;

import dev.matthiesen.common.matthiesen_lib.interfaces.ScreenRegistrar;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public final class NeoForgeClientRegistryHelper {
    private static final List<Consumer<ScreenRegistrar>> REGISTRATION_HANDLERS = new CopyOnWriteArrayList<>();

    private static volatile IEventBus modBus;
    private static volatile boolean listenerRegistered;

    private NeoForgeClientRegistryHelper() {
    }

    public static void init(IEventBus eventBus) {
        modBus = eventBus;
    }

    public static void registerMenuScreens(Consumer<ScreenRegistrar> registrationHandler) {
        REGISTRATION_HANDLERS.add(registrationHandler);
        ensureListenerRegistered();
    }

    private static void ensureListenerRegistered() {
        if (listenerRegistered) {
            return;
        }

        synchronized (NeoForgeClientRegistryHelper.class) {
            if (listenerRegistered) {
                return;
            }

            IEventBus eventBus = modBus;
            if (eventBus == null) {
                throw new IllegalStateException("NeoForgeClientRegistryHelper has not been initialized yet");
            }

            eventBus.addListener(NeoForgeClientRegistryHelper::onRegisterMenuScreens);
            listenerRegistered = true;
        }
    }

    private static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        ScreenRegistrar registrar = event::register;

        for (Consumer<ScreenRegistrar> registrationHandler : REGISTRATION_HANDLERS) {
            registrationHandler.accept(registrar);
        }
    }
}


